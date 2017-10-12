package com.pengllrn.tegm.utils;

import android.content.Context;

import java.io.File;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/9/21.
 */

public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        //找一个用来缓存图片的路径
        cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        //cacheDir = context.getFilesDir().getAbsoluteFile();
    }

    public File getFile(String url) {
        String filename = url.replace("/", "") + ".jpg";
        File f = new File(cacheDir, filename);
        return f;
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

    public File getFile2(String file){
        String filename = file + ".txt";
        File f = new File(cacheDir, filename);
        return f;
    }
}
