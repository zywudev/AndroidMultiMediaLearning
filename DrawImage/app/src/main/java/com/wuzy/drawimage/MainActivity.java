package com.wuzy.drawimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onButton1Click(View view) {
        startActivity(new Intent(this, ImageViewActivity.class));
    }

    public void onButton2Click(View view) {
        startActivity(new Intent(this, SurfaceViewActivity.class));
    }

    public void onButton3Click(View view) {
        startActivity(new Intent(this, CustomViewActivity.class));
    }

}
