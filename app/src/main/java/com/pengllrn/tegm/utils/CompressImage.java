package com.pengllrn.tegm.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/10/29.
 */

public class CompressImage {
    /**
     * 压缩到指定分辨率的方法 以1280X960为例
     * @param file 源文件
     * @param targetPath 目标路径
     *
     *
     * **/
    public static File scalFile(File file, String targetPath){
        long fileSize = file.length();
        final long fileMaxSize = 200 * 1024;//超过200K的图片需要进行压缩
        if(fileSize > fileMaxSize){
            try {
                byte[] bytes = FileUtils.file2Byte(file);//将文件转换为字节数组
                BitmapFactory.Options options = new BitmapFactory.Options();
                //仅仅解码边缘区域
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                //得到宽高
                int width = options.outWidth;
                int height = options.outHeight;
                float scaleWidth=0f;
                float scaleHeight=0f;
                Matrix matrix=new Matrix();
                if(width>height)
                {
                    scaleWidth=(float)1280/width;
                    scaleHeight=(float)960/height;

                }else{
                    scaleWidth=(float)960/width;
                    scaleHeight=(float)1280/height;
                }
                Bitmap bitmap=BitmapFactory.decodeFile(file.getPath());
                matrix.postScale(scaleWidth, scaleHeight);//执行缩放
                Bitmap resizeBitmap=Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                if(resizeBitmap != null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int quality = 100;
                    resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    //限制压缩后图片最大为200K，否则继续压缩
                    while (baos.toByteArray().length > fileMaxSize) {
                        baos.reset();
                        quality -= 10;
                        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    }
                    baos.close();
                    File targetFile = new File(targetPath);
                    if(targetFile.exists()){
                        boolean flag = targetFile.delete();
                        Log.i("ImageUtils.scalFile()", "flag: " + flag);
                    }
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                    return targetFile;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ImageUtils.scalFile()",e.getMessage());
            }
            return null;
        }else{
            return file;
        }
    }


}
