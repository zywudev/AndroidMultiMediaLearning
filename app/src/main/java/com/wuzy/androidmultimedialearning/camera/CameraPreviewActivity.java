package com.wuzy.androidmultimedialearning.camera;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wuzy.androidmultimedialearning.BaseActivity;
import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.BarUtils;

public class CameraPreviewActivity extends BaseActivity {

    public static final String PREVIEW_TYPE = "preview_type";
    public static final int TYPE_CAMERA_SURFACE_VIEW = 0x01;
    public static final int TYPE_CAMERA_TEXTURE_VIEW = 0x02;
    public static final int TYPE_CAMERA2_SURFACE_VIEW = 0x03;
    public static final int TYPE_CAMERA2_TEXTURE_VIEW = 0x04;
    private int mPreviewType;


    @Override
    protected int getTitleResId() {
        return R.string.camera_preview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPreviewType = getIntent().getIntExtra(PREVIEW_TYPE, TYPE_CAMERA_SURFACE_VIEW);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View getContentView() {
        View view = null;
        switch (mPreviewType) {
            case TYPE_CAMERA_SURFACE_VIEW:
                view = new CameraSurfaceView(this);
                break;
            case TYPE_CAMERA_TEXTURE_VIEW:
                view = new CameraTextureView(this);
                break;
            case TYPE_CAMERA2_SURFACE_VIEW:
                view = new Camera2SurfaceView(this);
                break;
            case TYPE_CAMERA2_TEXTURE_VIEW:
                view = new Camera2TextureView(this);
                break;
            default:
                break;
        }
        return view;
    }

    @Override
    protected void initView() {
        super.initView();
        BarUtils.setStatusBarVisibility(this, false);
    }

}
