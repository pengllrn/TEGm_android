package com.pengllrn.tegm.internet;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/10/5.
 */

public class OkHttp {
    public final int POSTOK = 0x2017;
    public final int GETOK = 0x2020;
    public final int WRANG = 0x22;
    public final int EXCEPTION = 0x30;
    private Handler handler;
    private Context mContext;

    public OkHttp(Context context, Handler handler){
        this.mContext=context;
        this.handler=handler;
    }

    public void postDataFromInternet(final String path,final RequestBody requestBody){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    //用post提交键值对格式的数据
                    Request request = new Request.Builder()
                            .url(path)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        String responseData = response.body().string();
                        Message msg = new Message();
                        msg.what = POSTOK;
                        msg.obj = responseData;
                        handler.sendMessage(msg);
                    }
                    else {
                        //TODO 错误报告
                        Message msg = new Message();
                        msg.what = WRANG;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    Message msg = new Message();
                    msg.what = EXCEPTION;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getDataFromInternet(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    //用post提交键值对格式的数据
                    Request request = new Request.Builder()
                            .url(path)
                            .build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        String responseData = response.body().string();
                        Message msg = new Message();
                        msg.what = GETOK;
                        msg.obj = responseData;
                        handler.sendMessage(msg);
                    }
                    else {
                        //TODO 错误报告
                        Message msg = new Message();
                        msg.what = WRANG;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    Message msg = new Message();
                    msg.what = EXCEPTION;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
