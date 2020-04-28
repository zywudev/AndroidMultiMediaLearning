package com.wuzy.androidmultimedialearning;

import android.app.Application;
import android.content.Context;

import com.wuzy.androidmultimedialearning.util.FileUtil;
import com.wuzy.androidmultimedialearning.util.ThreadHelper;

public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        ThreadHelper.getInstance().execute(() -> FileUtil.copyAssets2FileDir(sContext));
    }
}
