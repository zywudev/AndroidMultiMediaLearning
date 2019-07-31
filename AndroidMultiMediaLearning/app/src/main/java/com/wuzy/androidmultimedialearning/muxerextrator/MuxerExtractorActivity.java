package com.wuzy.androidmultimedialearning.muxerextrator;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 音视频混合和分离
 */
public class MuxerExtractorActivity extends AppCompatActivity {

    public static final String OUTPUT_VIDEO = "output_video.mp4";
    public static final String OUTPUT_AUDIO = "output_audio.mp3";
    private static final String VIDEO_SOURCE = "input.mp4";
    private static final String VIDEO_OUTPUT = "output.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muxer_extractor);
    }


    public void onExtractVideoClick(View view) {
        extractVideo();
    }

    public void onExtractAudioClick(View view) {
        extractAudio();
    }

    public void onMuxerVideoAndAudioClick(View view) {
        muxerVideoAndAudio();
    }

    /**
     * 分离视频的视频轨，输入视频 input.mp4，输出 output_video.mp4
     */
    private void extractVideo() {
        MediaExtractor mediaExtractor = new MediaExtractor();
        MediaMuxer mediaMuxer = null;
        File fileDir = FileUtil.getExternalAssetsDir(this);
        try {
            // 设置视频源
            mediaExtractor.setDataSource(new File(fileDir, VIDEO_SOURCE).getAbsolutePath());
            // 轨道索引
            int videoIndex = -1;
            // 视频轨道格式信息
            MediaFormat mediaFormat = null;
            // 数据源的轨道数
            int trackCount = mediaExtractor.getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                MediaFormat format = mediaExtractor.getTrackFormat(i);
                String mimeType = format.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("video/")) {
                    videoIndex = i;
                    mediaFormat = format;
                    break;
                }
            }
            mediaExtractor.selectTrack(videoIndex);
            File outFile = new File(FileUtil.getMuxerAndExtractorDir(this), OUTPUT_VIDEO);
            if (outFile.exists()) {
                outFile.delete();
            }
            mediaMuxer = new MediaMuxer(outFile.getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            // 将视频轨添加到 MediaMuxer，返回新的轨道
            int trackIndex = mediaMuxer.addTrack(mediaFormat);
            ByteBuffer byteBuffer = ByteBuffer.allocate(mediaFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE));
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            mediaMuxer.start();
            while (true) {
                // 将样本数据存储到字节缓存区
                int readSampleSize = mediaExtractor.readSampleData(byteBuffer, 0);
                // 如果没有可获取的样本，退出循环
                if (readSampleSize < 0) {
                    mediaExtractor.unselectTrack(videoIndex);
                    break;
                }
                bufferInfo.size = readSampleSize;
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.offset = 0;
                bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                mediaMuxer.writeSampleData(trackIndex, byteBuffer, bufferInfo);
                // 读取下一帧数据
                mediaExtractor.advance();
            }
            Toast.makeText(this, "分离视频完成", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mediaMuxer != null) {
                mediaMuxer.stop();
                mediaMuxer.release();
            }
            mediaExtractor.release();
        }
    }

    /**
     * 分离视频的音频轨，输入视频 input.mp4，输出 output_audio.mp3
     */
    private void extractAudio() {
        MediaExtractor mediaExtractor = new MediaExtractor();
        MediaMuxer mediaMuxer = null;

        File fileDir = FileUtil.getExternalAssetsDir(this);
        try {
            mediaExtractor.setDataSource(new File(fileDir, VIDEO_SOURCE).getAbsolutePath());
            int trackCount = mediaExtractor.getTrackCount();
            MediaFormat mediaFormat = null;
            int audioIndex = -1;
            for (int i = 0; i < trackCount; i++) {
                MediaFormat format = mediaExtractor.getTrackFormat(i);
                String mimeType = format.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("audio/")) {
                    audioIndex = i;
                    mediaFormat = format;
                    break;
                }
            }

            if (mediaFormat == null) {
                return;
            }

            mediaExtractor.selectTrack(audioIndex);

            File outFile = new File(FileUtil.getMuxerAndExtractorDir(this), OUTPUT_AUDIO);
            if (outFile.exists()) {
                outFile.delete();
            }
            mediaMuxer = new MediaMuxer(outFile.getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            // 将音频轨添加到 MediaMuxer，返回新的轨道
            int trackIndex = mediaMuxer.addTrack(mediaFormat);
            ByteBuffer byteBuffer = ByteBuffer.allocate(mediaFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE));
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            mediaMuxer.start();

            while (true) {
                // 将样本数据存储到字节缓存区
                int readSampleSize = mediaExtractor.readSampleData(byteBuffer, 0);
                // 如果没有可获取的样本，退出循环
                if (readSampleSize < 0) {
                    mediaExtractor.unselectTrack(audioIndex);
                    break;
                }
                bufferInfo.size = readSampleSize;
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.offset = 0;
                bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                mediaMuxer.writeSampleData(trackIndex, byteBuffer, bufferInfo);
                mediaExtractor.advance();
            }
            Toast.makeText(this, "分离音频完成", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mediaMuxer != null) {
                mediaMuxer.stop();
                mediaMuxer.release();
            }
            mediaExtractor.release();
        }
    }

    /**
     * 将 output_video.mp4 和 output_audio.mp3 合成为完整的视频 output.mp4
     */
    private void muxerVideoAndAudio() {
        MediaExtractor videoExtractor = new MediaExtractor();
        MediaExtractor audioExtractor = new MediaExtractor();
        MediaMuxer mediaMuxer = null;
        File fileDir = FileUtil.getMuxerAndExtractorDir(this);
        try {
            videoExtractor.setDataSource(new File(fileDir, OUTPUT_VIDEO).getAbsolutePath());
            MediaFormat videoFormat = null;
            int videoIndex = -1;
            int videoTrackCount = videoExtractor.getTrackCount();
            for (int i = 0; i < videoTrackCount; i++) {
                MediaFormat format = videoExtractor.getTrackFormat(i);
                String mimeType = format.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("video/")) {
                    videoIndex = i;
                    videoFormat = format;
                    break;
                }
            }

            if (videoFormat == null) {
                return;
            }
            audioExtractor.setDataSource(new File(fileDir, OUTPUT_AUDIO).getAbsolutePath());
            MediaFormat audioFormat = null;
            int audioIndex = -1;

            int audioTrackCount = audioExtractor.getTrackCount();
            for (int i = 0; i < audioTrackCount; i++) {
                MediaFormat format = audioExtractor.getTrackFormat(i);
                String mimeType = format.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("audio/")) {
                    audioIndex = i;
                    audioFormat = format;
                    break;
                }
            }
            if (audioFormat == null) {
                return;
            }

            String outPath = new File(fileDir, VIDEO_OUTPUT).getAbsolutePath();
            mediaMuxer = new MediaMuxer(outPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeVideoTrackIndex = mediaMuxer.addTrack(videoFormat);
            int writeAudioTrackIndex = mediaMuxer.addTrack(audioFormat);
            mediaMuxer.start();

            videoExtractor.selectTrack(videoIndex);
            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            int videoMaxInputSize = videoFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
            ByteBuffer videoByteBuffer = ByteBuffer.allocate(videoMaxInputSize);
            while (true) {
                int readVideoSampleSize = videoExtractor.readSampleData(videoByteBuffer, 0);
                if (readVideoSampleSize < 0) {
                    break;
                }

                videoBufferInfo.size = readVideoSampleSize;
                videoBufferInfo.offset = 0;
                videoBufferInfo.flags = videoExtractor.getSampleFlags();
                videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                mediaMuxer.writeSampleData(writeVideoTrackIndex, videoByteBuffer, videoBufferInfo);
                videoExtractor.advance();
            }

            audioExtractor.selectTrack(audioIndex);
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();
            int audioMaxInputSize = audioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
            ByteBuffer audioByteBuffer = ByteBuffer.allocate(audioMaxInputSize);
            while (true) {
                int readAudioSampleSize = audioExtractor.readSampleData(audioByteBuffer, 0);
                if (readAudioSampleSize < 0) {
                    audioExtractor.unselectTrack(audioIndex);
                    break;
                }
                audioBufferInfo.size = readAudioSampleSize;
                audioBufferInfo.presentationTimeUs = audioExtractor.getSampleTime();
                audioBufferInfo.offset = 0;
                audioBufferInfo.flags = audioExtractor.getSampleFlags();
                mediaMuxer.writeSampleData(writeAudioTrackIndex, audioByteBuffer, audioBufferInfo);
                audioExtractor.advance();
            }

            Toast.makeText(this, "混合音视频完成", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mediaMuxer != null) {
                mediaMuxer.stop();
                mediaMuxer.release();
            }
            videoExtractor.release();
            audioExtractor.release();
        }


    }
}
