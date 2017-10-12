package com.pengllrn.tegm.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.pengllrn.tegm.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/9/21.
 */

public class ImageLoader {

    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    FileCache fileCache;

    public ImageLoader(Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(1);
    }

    final int stub_id = R.drawable.defeat_image;

    public void DisplayImage(ImageView imageView,String url) {
        imageViews.put(imageView, url);
        Bitmap bitmap;
        File file = fileCache.getFile(url);
        if (file.exists() && file.length() > 0) {
            bitmap = decodeFile(file);
            imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url) {
        File file = fileCache.getFile(url);
        //从内存
        Bitmap bitmap = decodeFile(file);
        if (bitmap != null)
            return bitmap;

        //从网络
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //将输入流直接转换成图片
                bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                FileOutputStream fos = new FileOutputStream(file);
                //图片压缩处理
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //解码图像用来减少内存消耗
    private Bitmap decodeFile(File f) {
        try {
            //解码图像大小
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //找到正确的刻度值，它应该是2的幂。
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    //任务队列
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))

            return true;
        return false;
    }

    //用于显示位图在UI线程
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

}

