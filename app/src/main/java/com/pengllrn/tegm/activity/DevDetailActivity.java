package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.bean.DevDetail;
import com.pengllrn.tegm.bean.UseRate;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;
import com.pengllrn.tegm.utils.FileCache;
import com.pengllrn.tegm.views.BrokenLineChartView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DevDetailActivity extends AppCompatActivity {
    private List<String> date = new ArrayList<>();
    private List<Double> score = new ArrayList<>();
    private TextView dev_type;
    private TextView dev_num;
    private TextView dev_kind;
    private TextView dev_sensor;
    private TextView dev_status;
    private TextView dev_useflag;
    private TextView dev_avgrate;
    private TextView dev_roominfo;
    private TextView dev_gis;
    private TextView dev_schoolname;
    private TextView dev_usedepart;
    private TextView dev_checkname;
    private TextView regist_first;
    private TextView dev_description;
    private TextView dev_configureinfo;
    private BrokenLineChartView line_chart;

    private String path = "http://192.168.1.20:9999/getdevicedetail/";

    private ParseJson mParseJson = new ParseJson();
    private String deviceid;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    DevDetail devDetail = mParseJson.JsonToDevDetail(responseData);
                    initChartData(devDetail);
                    setview(devDetail);
                    Toast.makeText(DevDetailActivity.this, "数据更新成功！", Toast.LENGTH_SHORT).show();
                    save(responseData);
                    break;
                case 0x22:
                    Toast.makeText(DevDetailActivity.this, "数据更新失败！", Toast.LENGTH_SHORT).show();
                    //line_chart.drawBrokenLine(date, score);
                    break;

            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_detail);
        initview();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        deviceid = intent.getStringExtra("device_id");
        //加载历史数据
        String data = read(deviceid);
        if (data != null && !data.equals("")) {
            DevDetail devDetail = mParseJson.JsonToDevDetail(data);
            initChartData(devDetail);
            setview(devDetail);
        }
        //自动从网络更新数据
        RequestBody requestBody = new FormBody.Builder().add("deviceid", deviceid).build();
        OkHttp okHttp = new OkHttp(this, mHandler);
        okHttp.postDataFromInternet(path, requestBody);
    }

    private void initview() {
        dev_type = (TextView) findViewById(R.id.dev_type);
        dev_num = (TextView) findViewById(R.id.dev_num);
        dev_kind = (TextView) findViewById(R.id.dev_kind);
        dev_sensor = (TextView) findViewById(R.id.dev_sensor);
        dev_status = (TextView) findViewById(R.id.dev_status);
        dev_useflag = (TextView) findViewById(R.id.dev_useflag);
        dev_avgrate = (TextView) findViewById(R.id.dev_avgrate);
        line_chart = (BrokenLineChartView) findViewById(R.id.line_chart);
        dev_roominfo = (TextView) findViewById(R.id.dev_roominfo);
        dev_gis = (TextView) findViewById(R.id.dev_gis);
        dev_schoolname = (TextView) findViewById(R.id.dev_schoolname);
        dev_usedepart = (TextView) findViewById(R.id.dev_usedepart);
        dev_checkname = (TextView) findViewById(R.id.dev_checkname);
        regist_first = (TextView) findViewById(R.id.regist_first);
        dev_description = (TextView) findViewById(R.id.dev_description);
        dev_configureinfo = (TextView) findViewById(R.id.dev_configureinfo);
    }

    private void setview(DevDetail devDetail) {
        dev_type.setText("设备名称：" + devDetail.getType());
        dev_num.setText("设备编号：" + devDetail.getDevicenum());
        dev_kind.setText("设备类型：" + devDetail.getDevicekind());
        dev_sensor.setText("传感器编号：" + devDetail.getSensor());
        dev_status.setText("设备状态：" + devDetail.getStatus());
        dev_useflag.setText("运行状态：" + devDetail.getUseflag());
        dev_avgrate.setText("设备近10天平均使用率：" + devDetail.getAvgrate() + "%");
        dev_roominfo.setText("房间信息：" + devDetail.getRoominfo().getBuildingname() + "  "
                + devDetail.getRoominfo().getRoomname() + "  " + devDetail.getOrder() + "号");
        dev_gis.setText(" " + devDetail.getGis());
        dev_schoolname.setText("所属学校：" + devDetail.getSchoolname());
        dev_usedepart.setText("使用部门：" + devDetail.getUsedepart());
        dev_checkname.setText(" " + devDetail.getCheckname());
        regist_first.setText("注册日期：" + devDetail.getRegist_first());
        dev_description.setText("设备介绍：" + devDetail.getDescription());
        dev_configureinfo.setText("配置信息：" + devDetail.getConfigureinfo());
        if(date.size()>0&&score.size()>0) {
            line_chart.drawBrokenLine(date, score);
        }
    }

    private void initChartData(DevDetail devDetail) {
        float avgrate = 0;
        date.clear();
        score.clear();
        List<UseRate> useRates = devDetail.getUserate_10();
        for (int i = 0; i < useRates.size(); i++) {
            date.add(useRates.get(i).getDate());
            score.add(Double.parseDouble(useRates.get(i).getRate()));
            avgrate += Float.parseFloat(useRates.get(i).getRate());
        }
        if (useRates.size() > 0) {
            avgrate = avgrate * 100 / useRates.size();
        }
        devDetail.setAvgrate(Float.toString(avgrate));
    }

    public void save(String data) {
        FileCache fileCache = new FileCache(DevDetailActivity.this);
        File file = fileCache.getFile2(deviceid);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read(String deviceid) {
        //打开文件输入流
        FileCache fileCache = new FileCache(DevDetailActivity.this);
        File file = fileCache.getFile2(deviceid);
        StringBuilder sb = new StringBuilder("");
        if (file.exists() && file.length() > 0) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] temp = new byte[1024];
                int len = 0;
                //读取文件内容:
                while ((len = fis.read(temp)) > 0) {
                    sb.append(new String(temp, 0, len));
                }
                //关闭输入流
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
