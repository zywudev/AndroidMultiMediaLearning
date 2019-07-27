package com.wuzy.androidmultimedialearning;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wuzy.androidmultimedialearning.audiorecord.AudioRecordActivity;
import com.wuzy.androidmultimedialearning.audiotrack.AudioTrackActivity;
import com.wuzy.androidmultimedialearning.camera.CameraPreviewActivity;
import com.wuzy.androidmultimedialearning.drawimage.CustomViewActivity;
import com.wuzy.androidmultimedialearning.drawimage.ImageViewActivity;
import com.wuzy.androidmultimedialearning.drawimage.SurfaceViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 0);
        }

    }

    public void onDrawImageButtonClick(View view) {
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

    public void onAudioRecordButtonClick(View view) {
        startActivity(new Intent(this, AudioRecordActivity.class));
    }

    public void onAudioTrackButtonClick(View view) {
        startActivity(new Intent(this, AudioTrackActivity.class));
    }

    public void onCameraButtonClick(View view) {
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
