package com.wuzy.androidmultimedialearning;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.wuzy.androidmultimedialearning.audio.AudioRecordAudioTrackActivity;
import com.wuzy.androidmultimedialearning.camera.CameraPreviewActivity;
import com.wuzy.androidmultimedialearning.drawimage.CustomViewActivity;
import com.wuzy.androidmultimedialearning.drawimage.ImageViewActivity;
import com.wuzy.androidmultimedialearning.drawimage.SurfaceViewActivity;
import com.wuzy.androidmultimedialearning.mediacodec.MediaCodecActivity;
import com.wuzy.androidmultimedialearning.muxerextrator.MuxerExtractorActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 0);
        }

        findViewById(R.id.btn_draw_image).setOnClickListener(this);
        findViewById(R.id.btn_audio_record_audio_track).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_muxer_and_extractor).setOnClickListener(this);
        findViewById(R.id.btn_mediacodec).setOnClickListener(this);
    }

    @Override
    protected boolean enableBack() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_draw_image:
                showDrawImageDialog();
                break;
            case R.id.btn_audio_record_audio_track:
                startActivity(new Intent(this, AudioRecordAudioTrackActivity.class));
                break;
            case R.id.btn_camera:
                showCameraDialog();
                break;
            case R.id.btn_muxer_and_extractor:
                startActivity(new Intent(this,MuxerExtractorActivity.class));
                break;
            case R.id.btn_mediacodec:
                break;
            default:
                break;
        }
    }

    /**
     * 点击绘制图片按钮
     */
    public void showDrawImageDialog() {
        String[] types = new String[]{"ImageView", "CustomView", "SurfaceView"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择绘图类型");
        builder.setItems(types, (dialog, which) -> {
            if (which == 0) {
                startActivity(new Intent(MainActivity.this, ImageViewActivity.class));
            } else if (which == 1) {
                startActivity(new Intent(MainActivity.this, CustomViewActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, SurfaceViewActivity.class));
            }
        });
        builder.show();
    }

    /**
     *
     */
    public void showCameraDialog() {
        String[] types = new String[]{"Camera SurfaceView", "Camera TextureView", "Camera2 SurfaceView", "Camera2 TextureView"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择预览类型");
        builder.setItems(types, (dialog, which) -> {
            int previewType;
            if (which == 0) {
                previewType = CameraPreviewActivity.TYPE_CAMERA_SURFACE_VIEW;
            } else if (which == 1) {
                previewType = CameraPreviewActivity.TYPE_CAMERA_TEXTURE_VIEW;
            } else if (which == 2) {
                previewType = CameraPreviewActivity.TYPE_CAMERA2_SURFACE_VIEW;
            } else {
                previewType = CameraPreviewActivity.TYPE_CAMERA2_TEXTURE_VIEW;
            }
            Intent intent = new Intent(MainActivity.this, CameraPreviewActivity.class);
            intent.putExtra(CameraPreviewActivity.PREVIEW_TYPE, previewType);
            startActivity(intent);
        });
        builder.show();
    }


}
