package com.wuzy.androidmultimedialearning.camera;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.BarUtils;

public class CameraPreviewActivity extends AppCompatActivity {

    public static final String PREVIEW_TYPE = "preview_type";
    public static final int TYPE_CAMERA_SURFACE_VIEW = 0x01;
    public static final int TYPE_CAMERA_TEXTURE_VIEW = 0x02;
    public static final int TYPE_CAMERA2_SURFACE_VIEW = 0x03;
    public static final int TYPE_CAMERA2_TEXTURE_VIEW = 0x04;
    private int mPreviewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        BarUtils.setStatusBarVisibility(this, false);
        mPreviewType = getIntent().getIntExtra(PREVIEW_TYPE, TYPE_CAMERA_SURFACE_VIEW);
        previewCamera();
    }

    private void previewCamera() {
        ConstraintLayout root = findViewById(R.id.cl_root);
        if (mPreviewType == TYPE_CAMERA_SURFACE_VIEW) {
            CameraSurfaceView view = new CameraSurfaceView(this);
            root.addView(view);
        } else if (mPreviewType == TYPE_CAMERA_TEXTURE_VIEW) {
            CameraTextureView view = new CameraTextureView(this);
            root.addView(view);
        } else if (mPreviewType == TYPE_CAMERA2_SURFACE_VIEW) {
            Camera2SurfaceView view = new Camera2SurfaceView(this);
            root.addView(view);
        } else {
            Camera2TextureView view = new Camera2TextureView(this);
            root.addView(view);
        }
    }
}
