package com.wuzy.androidmultimedialearning.drawimage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wuzy.androidmultimedialearning.BaseActivity;
import com.wuzy.androidmultimedialearning.R;

public class CustomViewActivity extends BaseActivity {

    private CustomView mCustomView;
    @Override
    protected int getTitleResId() {
        return R.string.custom_view;
    }

    @Override
    protected View getContentView() {
        mCustomView = new CustomView(this);
        return mCustomView;
    }
}
