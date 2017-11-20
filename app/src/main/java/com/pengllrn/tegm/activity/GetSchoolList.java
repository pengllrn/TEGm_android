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

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.adapter.SchoolListAdapter;
import com.pengllrn.tegm.bean.SchoolList;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GetSchoolList extends AppCompatActivity {

    String applyUrl = "http://192.168.1.20:9999/damageapplylist/";
    ParseJson mParseJson = new ParseJson();
    private ListView list_school;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2017:
                    String responseData = (msg.obj).toString();
                    final List<SchoolList> listSchool = mParseJson.Json2Gis(responseData).getSchoolLists();
                    if(listSchool!=null) {
                        list_school.setAdapter(new SchoolListAdapter(getApplicationContext(),
                                listSchool, R.layout.base_list_item));
                        list_school.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), GetBuildingList.class);
                                intent.putExtra("schoolid", listSchool.get(position).getSchoolid());
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
        setContentView(R.layout.activity_school_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
        }
        list_school = (ListView) findViewById(R.id.list_school);

        OkHttp okHttp = new OkHttp(getApplicationContext(), mHandler);
        RequestBody requestBody = new FormBody.Builder().add("type", "1").build();
        okHttp.postDataFromInternet(applyUrl, requestBody);
    }
}
