package com.wuzy.androidmultimedialearning.drawimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.FileUtil;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ImageView imageView = findViewById(R.id.image_view);
        Bitmap bitmap = BitmapFactory.decodeFile(new File(FileUtil.getExternalAssetsDir(this), "jaqen.png").getPath());
        imageView.setImageBitmap(bitmap);
    }
}
