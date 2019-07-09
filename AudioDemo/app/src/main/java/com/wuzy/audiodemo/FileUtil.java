package com.wuzy.audiodemo;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * @author wuzy
 * @date 2019/7/9
 * @description
 */
public class FileUtil {

    public static void createFile(File file) {
        if (file.exists()) {
            file.delete();
        }
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
