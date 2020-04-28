package com.wuzy.androidmultimedialearning.audio;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wuzy.androidmultimedialearning.BaseActivity;
import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.FileUtil;
import com.wuzy.androidmultimedialearning.util.ThreadHelper;
import com.wuzy.androidmultimedialearning.util.wav.PcmToWav;

import java.io.File;

/**
 * 使用 AudioRecord 和 AudioTrack API 完成音频 PCM 数据的采集和播放，并实现 PCM 保存为 wav 文件
 */
public class AudioRecordAudioTrackActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AudioRecordAudioTrackAc";

    private Button mBtnRecord, mBtnPlay, mBtnPcm2Wav;
    private AudioRecorder mAudioRecorder;
    private AudioTracker mAudioTracker;

    private static final String mAudioName = "jaqen";

    @Override
    protected int getTitleResId() {
        return R.string.audio_record_and_audio_track;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_audio_record_audio_track;
    }

    @Override
    protected void initView() {
        super.initView();
        mBtnRecord = findViewById(R.id.btn_record_audio);
        mBtnPlay = findViewById(R.id.btn_play_audio);
        mBtnPcm2Wav = findViewById(R.id.btn_pcm_to_wav);
        mBtnRecord.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnPcm2Wav.setOnClickListener(this);
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
            case R.id.btn_play_audio:
                if (mBtnPlay.getText().toString().equals(getString(R.string.play_audio))) {
                    mBtnPlay.setText(getString(R.string.stop_play));
                    startPlay();
                } else {
                    mBtnPlay.setText(getString(R.string.play_audio));
                    stopPlay();
                }
                break;
            case R.id.btn_pcm_to_wav:
                makePCMFileToWAVFile();
                break;
            default:
                break;
        }
    }

    private void startPlay() {
        mAudioTracker = new AudioTracker(this);
        File file = new File(FileUtil.getPcmFilePath(this, mAudioName));
        mAudioTracker.createAudioTrack(file.getAbsolutePath());
        mAudioTracker.start();
    }

    private void stopPlay() {
        if (mAudioTracker != null) {
            mAudioTracker.release();
            mAudioTracker = null;
        }
    }

    // 开始录音
    private void startRecord() {
        mAudioRecorder = new AudioRecorder(this);
        mAudioRecorder.createDefaultAudio(mAudioName);
        mAudioRecorder.setRecordStreamListener(new AudioRecorder.RecordStreamListener() {
            @Override
            public void onRecording(byte[] bytes, int offset, int length) {

            }

            @Override
            public void finishRecord() {
            }
        });
        mAudioRecorder.startRecord();
    }

    // 结束录音
    private void stopRecord() {
        if (mAudioRecorder != null) {
            mAudioRecorder.stopRecord();
            mAudioRecorder = null;
        }
    }

    /**
     * 将单个pcm文件转化为wav文件
     */
    public void makePCMFileToWAVFile() {
        ThreadHelper.getInstance().execute(() -> {
            String wavFilePath = FileUtil.getWavFilePath(this, mAudioName);
            if (PcmToWav.makePcmFileToWavFile(FileUtil.getPcmFilePath(this, mAudioName), wavFilePath, false)) {
                //操作成功
                Log.i(TAG, "保存wav文件成功 " + wavFilePath);
                ThreadHelper.getInstance().runOnUiThread(() -> {
                    Toast.makeText(AudioRecordAudioTrackActivity.this, "保存wav文件成功", Toast.LENGTH_SHORT).show();
                });
            } else {
                //操作失败
                Log.e(TAG, "保存wav文件失败");
            }
        });
    }


}
