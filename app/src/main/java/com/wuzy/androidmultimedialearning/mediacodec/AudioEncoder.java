package com.wuzy.androidmultimedialearning.mediacodec;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaRecorder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wuzy on 2019/10/22.
 */
public class AudioEncoder {
    private static final int DEFAULT_SOURCE = MediaRecorder.AudioSource.MIC;            // 麦克风
    private static final int DEFAULT_SAMPLE_RATE = 44100;                               // 44.1KHz采样率
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;    // 立体声
    private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;     // 16位宽
    private static final String AAC_MIME = "audio/mp4a-latm";                           // AAC编码

    // 缓冲区字节大小
    private int mBufferSizeInBytes;
    private AudioRecord mAudioRecord;
    private MediaCodec mMediaCodec;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public void createAudioRecord() {
        mBufferSizeInBytes = AudioRecord.getMinBufferSize(DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT);
        if (mBufferSizeInBytes <= 0) {
            throw new RuntimeException("AudioRecord is not available, minBufferSize: " + mBufferSizeInBytes);
        }
        mAudioRecord = new AudioRecord(DEFAULT_SOURCE, DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT, mBufferSizeInBytes);
    }


}
