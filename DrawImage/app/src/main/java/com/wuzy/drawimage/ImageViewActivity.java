package com.wuzy.drawimage;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ImageView imageView = findViewById(R.id.image_view);
        Bitmap bitmap = Util.getBitmapFromAssetsFile(this, "Jaqen.png");
        imageView.setImageBitmap(bitmap);
    }
}
