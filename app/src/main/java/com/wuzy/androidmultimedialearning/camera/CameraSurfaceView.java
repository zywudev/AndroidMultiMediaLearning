package com.wuzy.androidmultimedialearning.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wuzy.androidmultimedialearning.util.CameraUtil;
import com.wuzy.androidmultimedialearning.util.ThreadHelper;

import java.io.IOException;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraSurfaceView";

    private Camera mCamera;
    private Activity mActivity;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        mActivity = (Activity) context;
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated: ");
        // 异步预览
        ThreadHelper.getInstance().runOnHandlerThread(() -> {
            openCamera();
            startPreview(holder);
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed: ");
        releaseCamera();
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        // 有多少个摄像头
        int number = Camera.getNumberOfCameras();

        for (int i = 0; i < number; ++i) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

            Camera.getCameraInfo(i, cameraInfo);

            // 后置摄像头
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open(i);
                CameraUtil.setCameraDisplayOrientation(mActivity, i, mCamera);
            }
        }
    }

    /**
     * 开始预览
     *
     * @param holder
     */
    private void startPreview(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.setPreviewCallback((data, camera) -> {
                // 取得 NV21 数据，进一步处理
            });
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭相机
     */
    private void releaseCamera() {
        Log.e(TAG, "releaseCamera: ");
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.setPreviewDisplay(null);
                mCamera.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera = null;
        }
    }

}
