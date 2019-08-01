package com.wuzy.androidmultimedialearning.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.TextureView;

import com.wuzy.androidmultimedialearning.util.CameraUtil;
import com.wuzy.androidmultimedialearning.util.ThreadHelper;

import java.io.IOException;

/**
 * @author wuzy
 * @date 2019/7/10
 * @description
 */
public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private Camera mCamera;
    private Activity mActivity;

    public CameraTextureView(Context context) {
        this(context, null);
    }

    public CameraTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        ThreadHelper.getInstance().runOnHandlerThread(() -> {
            openCamera();
            startPreview(surface);
        });
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        releaseCamera();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * 打开相机
     */
    private void openCamera() {
        int number = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < number; ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 打开后置摄像头
                mCamera = Camera.open(i);
                CameraUtil.setCameraDisplayOrientation(mActivity, i, mCamera);
            }
        }
    }

    /**
     * 开始预览
     *
     * @param texture
     */
    private void startPreview(SurfaceTexture texture) {
        if (mCamera != null) {
            mCamera.setPreviewCallback((data, camera) -> {
                // 取得 NV21 数据，进一步处理
            });
            try {
                mCamera.setPreviewTexture(texture);
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
