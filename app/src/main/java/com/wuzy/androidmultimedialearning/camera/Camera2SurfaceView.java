package com.wuzy.androidmultimedialearning.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;


public class Camera2SurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private Handler mWorkHandler;
    private String mCameraId;
    private CameraDevice mCameraDevice;
    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;

    public Camera2SurfaceView(Context context) {
        this(context, null);
    }

    public Camera2SurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Camera2SurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        HandlerThread handlerThread = new HandlerThread("camera2");
        handlerThread.start();
        mWorkHandler = new Handler(handlerThread.getLooper());
        checkCamera();
        openCamera();
    }

    /**
     * 检测相机
     */
    private void checkCamera() {
        CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            for (String s : cameraIdList) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(s);
                Integer lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                Integer sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                Integer supportedHardwareLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                if (lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    mCameraId = s;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 打开相机
     */
    private void openCamera() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mCameraId == null) {
            return;
        }

        CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraManager.openCamera(mCameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    mCameraDevice = camera;
                    mImageReader = ImageReader.newInstance(getWidth(), getHeight(), ImageFormat.YUV_420_888, 8);
                    mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                        @Override
                        public void onImageAvailable(ImageReader reader) {
                            Image image = reader.acquireLatestImage();
                            //我们可以将这帧数据转成字节数组，类似于Camera1的PreviewCallback回调的预览帧数据
                            //ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            //byte[] data = new byte[buffer.remaining()];
                            //buffer.get(data);
                            image.close();
                        }
                    }, mWorkHandler);

                    createCameraPreview();
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    camera.close();
                    mCameraDevice = null;
                }

                @Override
                public void onClosed(CameraDevice camera) {
                    super.onClosed(camera);
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    camera.close();
                    mCameraDevice = null;
                }
            }, mWorkHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 相机预览
     */
    private void createCameraPreview() {
        try {
            final CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface surface = mSurfaceHolder.getSurface();
            captureRequestBuilder.addTarget(surface);
            Surface imageReaderSurface = mImageReader.getSurface();
            captureRequestBuilder.addTarget(imageReaderSurface);
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
            mCameraDevice.createCaptureSession(Arrays.asList(surface, imageReaderSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mCameraCaptureSession = session;
                    CaptureRequest captureRequest = captureRequestBuilder.build();
                    try {
                        session.setRepeatingRequest(captureRequest, null, null);
                    } catch (CameraAccessException e) {
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                }
            }, mWorkHandler);
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        closeCameraPreview();
        if (mCameraDevice != null) {
            mCameraDevice.close();
        }
        if (mImageReader != null) {
            mImageReader.close();
        }
        mWorkHandler.getLooper().quitSafely();
    }

    private void closeCameraPreview() {
        if (mCameraCaptureSession != null) {
            try {
                mCameraCaptureSession.stopRepeating();
                mCameraCaptureSession.abortCaptures();
                mCameraCaptureSession.close();
            } catch (Exception e) {
            }
            mCameraCaptureSession = null;
        }
    }
}
