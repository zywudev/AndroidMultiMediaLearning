package com.wuzy.androidmultimedialearning.mediacodec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wuzy.androidmultimedialearning.BaseActivity;
import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.FileUtil;
import com.wuzy.androidmultimedialearning.util.ThreadHelper;

import java.io.File;
import java.io.IOException;

public class MediaCodecActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MediaCodecActivity";


    @Override
    protected void initView() {
        findViewById(R.id.btn_pcm2aac).setOnClickListener(this);
        findViewById(R.id.btn_aac2pcm).setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_media_codec;
    }

    @Override
    protected int getTitleResId() {
        return R.string.mediacodec;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_aac2pcm:
                aac2pcm();
                break;
            case R.id.btn_pcm2aac:
                pcm2aac();
                break;
        }
    }

    private void aac2pcm() {
        ThreadHelper.getInstance().execute(() -> {
            File pcmFile = new File(FileUtil.getExternalAssetsDir(MediaCodecActivity.this), "test.pcm");
            File aacFile = new File(FileUtil.getAacFileDir(MediaCodecActivity.this), "test_output.aac");
            if (aacFile.exists()) {
                aacFile.delete();
            }
            try {
                AacPcmCoder.encodePcmToAac(pcmFile, aacFile);
                runOnUiThread(() -> Toast.makeText(MediaCodecActivity.this, "编码完成", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                Log.e(TAG, "编码失败: " + e.getMessage());
            }
        });
    }

    public void pcm2aac() {
        ThreadHelper.getInstance().execute(() -> {
            // 44.1kHz采样率，单通道
            File aacFile = new File(FileUtil.getExternalAssetsDir(MediaCodecActivity.this), "test.aac");
            File pcmFile = new File(FileUtil.getPcmFileDir(MediaCodecActivity.this), "test_output.pcm");
            try {
                AacPcmCoder.decodeAacTomPcm(aacFile, pcmFile);
                runOnUiThread(() -> Toast.makeText(MediaCodecActivity.this, "解码完成", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                Log.e(TAG, "解码失败" + e.getMessage());
            }
        });
    }



}
