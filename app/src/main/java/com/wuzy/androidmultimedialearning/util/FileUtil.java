package com.wuzy.androidmultimedialearning.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUtil {

    private static final String TAG = "FileUtil";

    private FileUtil() {
    }

    /**
     * 将 assets 里面的所有文件拷贝到应用外部存储目录下，保留文件树结构，不会覆盖原有数据
     *
     * @param context
     */
    public static void copyAssets2FileDir(Context context) {
        AssetManager assets = context.getAssets();
        List<String> fileTree = new ArrayList<>(16);
        listAssetsRecursively(assets, "", fileTree);
        File externalAssetsDir = getExternalAssetsDir(context);
        for (String s : fileTree) {
            try {
                InputStream is = assets.open(s);
                File dest = new File(externalAssetsDir, s);
                if (dest.exists()) {
                    continue;
                }
                copyFile(is, dest);
            } catch (IOException e) {
                Log.e(TAG, "copyAssets2FileDir: " + s, e);
            }
        }
    }

    private static void listAssetsRecursively(AssetManager assetManager, String dirName, List<String> fileTree) {
        try {
            String[] list = assetManager.list(dirName);
            if (list != null && list.length > 0) {
                // is dir
                for (String s : list) {
                    String dir = TextUtils.isEmpty(dirName) ? s : dirName + File.separator + s;
                    listAssetsRecursively(assetManager, dir, fileTree);
                }
            } else {
                fileTree.add(dirName);
            }
        } catch (IOException e) {
            Log.e(TAG, "listAssetsRecursively: " + dirName, e);
        }
    }

    public static void copyFile(InputStream is, File dest) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            File parentFile = dest.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (dest.exists()) {
                dest.delete();
            }
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] bytes = new byte[8192];
            int len;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }

    public static File getExternalAssetsDir(Context context) {
        File fileDir = getFileDir(context);
        File assetsDir = new File(fileDir, "assets");
        if (!assetsDir.exists()) {
            assetsDir.mkdirs();
        }
        return assetsDir;
    }

    public static File getAacFileDir(Context context) {
        File fileDir = getFileDir(context);
        File aacFileDir = new File(fileDir,"aac");
        if (!aacFileDir.exists()) {
            aacFileDir.mkdirs();
        }
        return aacFileDir;
    }

    public static File getPcmFileDir(Context context) {
        File fileDir = getFileDir(context);
        File pcmFileDir = new File(fileDir, "pcm");
        if (!pcmFileDir.exists()) {
            pcmFileDir.mkdirs();
        }
        return pcmFileDir;
    }

    public static String getPcmFilePath(Context context, String name) {
        File pcmFileDir = getPcmFileDir(context);
        File pcmFile = new File(pcmFileDir, name + ".pcm");
        return pcmFile.getAbsolutePath();
    }

    public static String getWavFilePath(Context context, String name) {
        File wavFileDir = getWavFileDir(context);
        File wavFile = new File(wavFileDir, name + ".wav");
        return wavFile.getAbsolutePath();
    }

    public static File getWavFileDir(Context context) {
        File fileDir = getFileDir(context);
        File wavFileDir = new File(fileDir, "wav");
        if (!wavFileDir.exists()) {
            wavFileDir.mkdirs();
        }
        return wavFileDir;
    }

    public static File getMuxerAndExtractorDir(Context context) {
        File fileDir = getFileDir(context);
        File muxerAndExtractorDir = new File(fileDir, "muxerandextractor");
        if (!muxerAndExtractorDir.exists()) {
            muxerAndExtractorDir.mkdirs();
        }
        return muxerAndExtractorDir;
    }


    public static File getFileDir(Context context) {
        File filesDir = context.getExternalFilesDir(null);
        if (filesDir == null) {
            filesDir = context.getFilesDir();
        }
        return filesDir;
    }

    public static Bitmap getDrawImageBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeFile(new File(FileUtil.getExternalAssetsDir(context), "jaqen.png").getPath());
        return bitmap;
    }

    /**
     * 获取 UUID 作为唯一标识 eg: c9ce6bdd155749be91153a6d76a484eb
     * @return 32 个字符
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }



}
