package com.wuzy.audiodemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.wuzy.audiodemo.Config.AUDIO_ENCODING;
import static com.wuzy.audiodemo.Config.AUDIO_CHANNEL_CONFIG;
import static com.wuzy.audiodemo.Config.AUDIO_SAMPLE_RATE_INHZ;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int MY_PERMISSIONS_REQUEST = 1001;

    private String[] mPermissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private List<String> mPermissionList = new ArrayList<>();
    private boolean mIsRecording = false;
    private AudioRecord mAudioRecord;
    private AudioTrack mAudioTrack;
    private FileInputStream mFileInputStream = null;
    private byte[] mAudioData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
    }

    private void checkPermissions() {
        // Marshmallow 开始才用申请运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < mPermissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, mPermissions[i]) !=
                        PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(mPermissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, permissions[i] + " 权限被用户禁止！");
                }
            }
        }
    }

    public void onClickButton1(View view) {
        Button button = (Button) view;
        if (button.getText().toString().equals(getString(R.string.start_record))) {
            button.setText(getString(R.string.stop_record));
            startRecord();
        } else {
            button.setText(R.string.start_record);
            stopRecord();
        }
    }

    public void onClickButton2(View view) {
        File pcmFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
        File wavFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.wav");
        if (!pcmFile.exists()) {
            Toast.makeText(this, "请先录制音频", Toast.LENGTH_SHORT).show();
            return;
        }
        FileUtil.createFile(wavFile);
        PcmToWavUtil pcmToWavUtil = new PcmToWavUtil(AUDIO_SAMPLE_RATE_INHZ, AUDIO_CHANNEL_CONFIG, AUDIO_ENCODING);
        pcmToWavUtil.pcmToWav(pcmFile.getAbsolutePath(), wavFile.getAbsolutePath());
    }

    public void onClickButton3(View view) {
        Button button = (Button) view;
        if (button.getText().toString().equals(getString(R.string.play))) {
            button.setText(getString(R.string.stop_play));
            playInModeStream();
//            playInModeStatic();
        } else {
            button.setText(getString(R.string.play));
            stopPlay();
        }
    }

    private void startRecord() {
        final int minBufferSize = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE_INHZ, AUDIO_CHANNEL_CONFIG, AUDIO_ENCODING);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, AUDIO_SAMPLE_RATE_INHZ, AUDIO_CHANNEL_CONFIG, AUDIO_ENCODING, minBufferSize);
        final byte[] data = new byte[minBufferSize];
        final File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
        FileUtil.createFile(file);
        mAudioRecord.startRecording();
        mIsRecording = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (fos != null) {
                    while (mIsRecording) {
                        int read = mAudioRecord.read(data, 0, minBufferSize);
                        if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                            try {
                                fos.write(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Log.e(TAG, "关闭");
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void stopRecord() {
        mIsRecording = false;
        if (null != mAudioRecord) {
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }
    }

    /**
     * model stream 模式播放
     */
    private void playInModeStream() {
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        final int minBufferSize = AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE_INHZ, channelConfig, AUDIO_ENCODING);
        mAudioTrack = new AudioTrack(
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                new AudioFormat.Builder()
                        .setSampleRate(AUDIO_SAMPLE_RATE_INHZ)
                        .setEncoding(AUDIO_ENCODING)
                        .setChannelMask(channelConfig)
                        .build(),
                minBufferSize,
                AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE);
        mAudioTrack.play();

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm");
        try {
            mFileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] tempBuffer = new byte[minBufferSize];
                    while (mFileInputStream.available() > 0) {
                        int readCount = mFileInputStream.read(tempBuffer);
                        if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                                readCount == AudioTrack.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (readCount != 0 && readCount != -1) {
                            mAudioTrack.write(tempBuffer, 0, readCount);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * mode static 模式播放
     */
    private void playInModeStatic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = getResources().openRawResource(R.raw.ding);
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        for (int b; (b = in.read()) != -1; ) {
                            out.write(b);
                        }
                        mAudioData = out.toByteArray();
                    } finally {
                        in.close();
                    }
                } catch (IOException e) {
                    Log.wtf(TAG, "Failed to read", e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAudioTrack = new AudioTrack(
                                new AudioAttributes.Builder()
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .build(),
                                new AudioFormat.Builder().setSampleRate(22050)
                                        .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                        .build(),
                                mAudioData.length,
                                AudioTrack.MODE_STATIC,
                                AudioManager.AUDIO_SESSION_ID_GENERATE);
                        mAudioTrack.write(mAudioData, 0, mAudioData.length);
                        mAudioTrack.play();
                        Log.d(TAG, "Playing");
                    }
                });
            }
        }).start();
    }

    private void stopPlay() {
        if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }
}
