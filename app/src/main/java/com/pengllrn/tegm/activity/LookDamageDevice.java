package com.pengllrn.tegm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.adapter.PhotoAdapter;
import com.pengllrn.tegm.bean.DamageDevice;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;
import com.pengllrn.tegm.listener.RecyclerItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LookDamageDevice extends AppCompatActivity {

    private String deviceid;
    private String applyUrl = "http://192.168.1.20:9999/damageapplylist/";
    private ParseJson mParseJson = new ParseJson();
    private PhotoAdapter photoAdapter;
    private ArrayList<String> Photos = new ArrayList<>();

    private TextView device_type;
    private TextView device_num;
    private TextView device_school;
    private TextView device_room;
    private TextView applier_name;
    private TextView applier_tel;
    private TextView datetime;
    private TextView device_depict;
    private RecyclerView photos_recyclerview;
    private Button btn_agree;
    private Button btn_refuse;
    private int count = 0;
    private int size =0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    DamageDevice damageDevice = mParseJson.Json2DamageDevice(responseData);
                    setViews(damageDevice);
                    break;
                case 0x22:
                    Toast.makeText(getApplicationContext(), "数据更新失败！", Toast.LENGTH_SHORT).show();
                    //line_chart.drawBrokenLine(date, score);
                    break;
                case 0x2030:
                    try {
                        File file = new File(LookDamageDevice.this.getCacheDir(), count + ".jpg");
                        //将输入流直接转换成图片
                        Bitmap bitmap = (Bitmap) msg.obj;
                        FileOutputStream fos = new FileOutputStream(file);
                        //图片压缩处理
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();
                        Photos.add(LookDamageDevice.this.getCacheDir().getAbsolutePath()+"/"+count+".jpg");
                        count++;
                        if(count==size) setRecyclerview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x40:
                    String responseMes = (msg.obj).toString();
                    if(responseMes.equals("success")) {
                        finish();
                        Toast.makeText(getApplicationContext(), "设备报废成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "设备报废失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_damage_device);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
        }
        initView();
        Intent intent = getIntent();
        deviceid = intent.getStringExtra("deviceid");
        RequestBody requestBody = new FormBody.Builder().add("deviceid", deviceid).add("type", "2").build();
        OkHttp okHttp = new OkHttp(this, mHandler);
        okHttp.postDataFromInternet(applyUrl, requestBody);

        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LookDamageDevice.this);
                dialog.setTitle("系统提示")
                .setMessage("确定要同意设备报废申请？")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestBody requestBody = new FormBody.Builder().add("deviceid", deviceid)
                                .add("type", "3")
                                .add("result","pass")
                                .build();
                        OkHttp okHttp = new OkHttp(LookDamageDevice.this, mHandler);
                        okHttp.postData2Internet(applyUrl, requestBody);
                    }
                })
               .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
                dialog.show();
            }
        });

        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LookDamageDevice.this);
                dialog.setTitle("系统提示")
                        .setMessage("确定要拒绝设备报废申请？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RequestBody requestBody = new FormBody.Builder().add("deviceid", deviceid)
                                        .add("type", "3")
                                        .add("result","refuse")
                                        .build();
                                OkHttp okHttp = new OkHttp(LookDamageDevice.this, mHandler);
                                okHttp.postData2Internet(applyUrl, requestBody);
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    private void initView() {
        device_type = (TextView) findViewById(R.id.damage_device_type);
        device_num = (TextView) findViewById(R.id.damage_device_num);
        device_school = (TextView) findViewById(R.id.damage_device_school);
        device_room = (TextView) findViewById(R.id.damage_device_room);
        applier_name = (TextView) findViewById(R.id.damage_applier_name);
        applier_tel = (TextView) findViewById(R.id.damage_applier_tel);
        datetime = (TextView) findViewById(R.id.damage_apply_datetime);
        device_depict = (TextView) findViewById(R.id.damage_device_depict);
        photos_recyclerview = (RecyclerView) findViewById(R.id.damage_photos);
        btn_agree = (Button) findViewById(R.id.btn_agree_damage);
        btn_refuse = (Button) findViewById(R.id.btn_refuse_damage);
    }

    private void setViews(DamageDevice damageDevice) {
        device_type.setText("设备名称：" + damageDevice.getType());
        device_num.setText("设备编号：" + damageDevice.getDevicenum());
        device_school.setText("所属学校：" + damageDevice.getSchoolid());
        device_room.setText("所在位置：" + damageDevice.getRoom());
        applier_name.setText("申请人：" + damageDevice.getApplier());
        applier_tel.setText("联系电话：" + damageDevice.getAppliertel());
        datetime.setText("申请时间：" + damageDevice.getDatetime());
        device_depict.setText(damageDevice.getDamagedepict());
        initPhotos(damageDevice);
    }

    private void initPhotos(DamageDevice damageDevice) {
        OkHttp okHttp = new OkHttp(LookDamageDevice.this, mHandler);
        if (damageDevice.getPhoto1() != null) {
            okHttp.getImageFromInternet(damageDevice.getPhoto1());
            size=1;
        } else
            return;
        if (damageDevice.getPhoto2() != null) {
            okHttp.getImageFromInternet(damageDevice.getPhoto2());
            size=2;
        } else
            return;
        if (damageDevice.getPhoto3() != null) {
            okHttp.getImageFromInternet(damageDevice.getPhoto3());
            size=3;
        } else
            return;
        if (damageDevice.getPhoto4() != null) {
            okHttp.getImageFromInternet(damageDevice.getPhoto4());
            size=4;
        } else
            return;
        if (damageDevice.getPhoto5() != null) {
            okHttp.getImageFromInternet(damageDevice.getPhoto5());
            size=5;
        } else
            return;
        if (damageDevice.getPhoto6() != null) {
            okHttp.getImageFromInternet(damageDevice.getPhoto6());
            size=6;
        }
    }

    private void setRecyclerview() {
        photoAdapter = new PhotoAdapter(this, Photos, Photos.size());
        photos_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        photos_recyclerview.setAdapter(photoAdapter);
        photos_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        PhotoPreview.builder()
                                .setPhotos(Photos)
                                .setCurrentItem(position)
                                .setShowDeleteButton(false)
                                .start(LookDamageDevice.this);
                    }
                }));
    }

}
