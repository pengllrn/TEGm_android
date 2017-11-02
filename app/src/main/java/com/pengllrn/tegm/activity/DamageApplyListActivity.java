package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.adapter.DamageApplyAdapter;
import com.pengllrn.tegm.bean.DamageApplyList;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DamageApplyListActivity extends AppCompatActivity {
    String applyUrl = "http://192.168.1.20:9999/damageapplylist/";
    ParseJson mParseJson = new ParseJson();
    private ListView list_damage_apply;
    private TextView apply_count;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    final List<DamageApplyList> listDamage = mParseJson.Json2DamageList(responseData).getDamagelist();
                    if(listDamage!=null) {
                        list_damage_apply.setAdapter(new DamageApplyAdapter(DamageApplyListActivity.this,
                                listDamage, R.layout.damage_apply_list_item));
                        apply_count.setText("共 "+listDamage.size()+" 条申请报废记录");
                        list_damage_apply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent = new Intent(DamageApplyListActivity.this, LookDamageDevice.class);
                                intent.putExtra("deviceid", listDamage.get(position).getDeviceid());
                                startActivity(intent);
                            }
                        });
                    }
                    break;
                default:
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damage_apply_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
        }

        initView();

        OkHttp okHttp = new OkHttp(DamageApplyListActivity.this, mHandler);
        RequestBody requestBody = new FormBody.Builder().add("type", "1").build();
        okHttp.postDataFromInternet(applyUrl, requestBody);

    }

    private void initView() {
        list_damage_apply = (ListView) findViewById(R.id.list_damage_apply);
        apply_count = (TextView) findViewById(R.id.tv_apply_count);
    }
}
