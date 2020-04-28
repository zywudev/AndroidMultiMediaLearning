package com.wuzy.androidmultimedialearning.drawimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.wuzy.androidmultimedialearning.BaseActivity;
import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.FileUtil;

import java.io.File;

public class ImageViewActivity extends BaseActivity {

    private ImageView mImageView;

    @Override
    protected View getContentView() {
        mImageView = new ImageView(this);
        return mImageView;
    }

    @Override
    protected int getTitleResId() {
        return R.string.image_view;
    }

    @Override
    protected void initView() {
        super.initView();
        //
        mImageView.setImageBitmap(FileUtil.getDrawImageBitmap(this));
    }
}
