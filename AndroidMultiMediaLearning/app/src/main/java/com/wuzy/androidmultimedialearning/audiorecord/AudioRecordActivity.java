package com.wuzy.androidmultimedialearning.audiorecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.FileUtil;

public class AudioRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AudioRecordActivity";

    private AudioRecorder mAudioRecorder;
    private Button mBtnRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        mBtnRecord = findViewById(R.id.btn_record_audio);
        mBtnRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record_audio:
                if (mBtnRecord.getText().toString().equals(getString(R.string.click_record_audio))) {
                    mBtnRecord.setText(getString(R.string.stop_record_audio));
                    startRecord();
                } else {
                    mBtnRecord.setText(getString(R.string.click_record_audio));
                    stopRecord();
                }
                break;
            default:
                break;
        }
    }


    // 开始录音
    private void startRecord() {
        mAudioRecorder = new AudioRecorder(this);
        mAudioRecorder.createDefaultAudio(FileUtil.getUUID32());
        mAudioRecorder.setRecordStreamListener(new AudioRecorder.RecordStreamListener() {
            @Override
            public void onRecording(byte[] bytes, int offset, int length) {

            }

            @Override
            public void finishRecord() {
                runOnUiThread(() -> Toast.makeText(AudioRecordActivity.this, "录音完成", Toast.LENGTH_SHORT).show());
            }
        });
        mAudioRecorder.startRecord();
        Toast.makeText(AudioRecordActivity.this, "正在录音", Toast.LENGTH_SHORT).show();
    }

    // 结束录音
    private void stopRecord() {
        if (mAudioRecorder != null) {
            mAudioRecorder.stopRecord();
            mAudioRecorder = null;
        }
    }


}
