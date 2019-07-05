package com.wuzy.drawimage;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author wuzy
 * @date 2019/7/5
 * @description
 */
public class Util {

    public static Bitmap getBitmapFromAssetsFile(Context context, String fileName) {
        Bitmap bitmap = null;
        AssetManager manager = context.getAssets();
        InputStream is = null;
        try {
            is = manager.open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
