package com.wuzy.androidmultimedialearning.drawimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.wuzy.androidmultimedialearning.BaseActivity;
import com.wuzy.androidmultimedialearning.R;
import com.wuzy.androidmultimedialearning.util.FileUtil;

import java.io.File;

public class SurfaceViewActivity extends BaseActivity {

    private SurfaceView mSurfaceView;

    @Override
    protected int getTitleResId() {
        return R.string.surface_view;
    }

    @Override
    protected View getContentView() {
        mSurfaceView = new SurfaceView(this);
        return mSurfaceView;
    }

    @Override
    protected void initView() {
        super.initView();
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (holder == null) {
                    return;
                }

                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);

                Canvas canvas = holder.lockCanvas();
                // 绘制图片
                canvas.drawBitmap(FileUtil.getDrawImageBitmap(SurfaceViewActivity.this), 0, 0, paint);
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
}
