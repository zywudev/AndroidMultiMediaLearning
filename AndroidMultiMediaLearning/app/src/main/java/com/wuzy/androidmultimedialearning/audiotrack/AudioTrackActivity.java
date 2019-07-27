package com.wuzy.androidmultimedialearning.audiotrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.FileUtil;

import java.io.File;

public class AudioTrackActivity extends AppCompatActivity {

    private AudioTracker mAudioTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_track);
    }

    public void onPlayClick(View view) {
        Button button = (Button) view;
        if (button.getText().toString().equals(getString(R.string.play))) {
            button.setText(getString(R.string.stop));
            mAudioTracker = new AudioTracker(this);
            File file = new File(FileUtil.getExternalAssetsDir(this), "test.pcm");
            mAudioTracker.createAudioTrack(file.getAbsolutePath());
            mAudioTracker.start();
        } else {
            button.setText(getString(R.string.play));
            mAudioTracker.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioTracker != null) {
            mAudioTracker.release();
            mAudioTracker = null;
        }
    }
}
