package com.wuzy.androidmultimedialearning;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutResId() != 0) {
            setContentView(getLayoutResId());
        } else if (getContentView() != null) {
            setContentView(getContentView());
        }

        //标题栏设置
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getTitleResId());
            if (enableBack()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        initView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected int getLayoutResId() {
        return 0;
    }

    protected View getContentView() {
        return null;
    }

    protected int getTitleResId() {
        return R.string.app_name;
    }

    protected boolean enableBack() {
        return true;
    }

    protected void initView() {

    }


}
