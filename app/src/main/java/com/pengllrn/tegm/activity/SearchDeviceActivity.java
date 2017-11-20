package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pengllrn.tegm.R;
import com.pengllrn.tegm.adapter.DeviceAdapter;
import com.pengllrn.tegm.bean.All;
import com.pengllrn.tegm.bean.Device;
import com.pengllrn.tegm.fragment.RightChoose;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;
import com.pengllrn.tegm.utils.FileCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class SearchDeviceActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    private DrawerLayout mDrawerLayout;

    private String path = "http://192.168.1.20:9999/getdeviceinfo/";

    private ParseJson mParseJson = new ParseJson();
    private DeviceAdapter mDeviceAdapter;
    private All all;

    private ListView mListView;
    private LinearLayout LvChoose;
    private TextView device_all;
    private TextView device_run;
    private SwipeRefreshLayout swipeRefresh;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x2020:
                    String responseData = (msg.obj).toString();
                    all = mParseJson.JsonToAll(responseData);
                    setview(all.getDevice());
                    setListener(all.getDevice());
                    setMainview(all.getDevice());
                    Toast.makeText(SearchDeviceActivity.this, "设备列表已更新成功！", Toast.LENGTH_SHORT).show();
                    save(responseData);
                    break;
                case 0x22:
                    Toast.makeText(SearchDeviceActivity.this, "服务器访问失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private TextView which_build;
    private TextView which_room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_device);

        //A
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
        }
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        //B
        initview();
        //从本地获取列表
        String data = read("list_data");
        if (data != null && !data.equals("")) {
            all = mParseJson.JsonToAll(data);
            setMainview(all.getDevice());
            setListener(all.getDevice());
        }
        //从网络更新列表
        OkHttp okHttp = new OkHttp(this, mHandler);
        okHttp.getDataFromInternet(path);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLvListener();
        setRefreshListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setHint("设备编号或传感器编号");
        searchView.setHintTextColor(Color.parseColor("#ebebeb"));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_choose:
                mDrawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.menu_refresh:
                if(swipeRefresh.isRefreshing()){
                    swipeRefresh.setRefreshing(false);
                }else
                swipeRefresh.setRefreshing(true);
                break;
            default:
        }
        return true;
    }

    private void initview() {
        mListView = (ListView) findViewById(R.id.list_item);
        LvChoose = (LinearLayout) findViewById(R.id.lv_choose);
        device_all = (TextView) findViewById(R.id.tv_device_all);
        device_run = (TextView) findViewById(R.id.tv_device_run);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        which_build = (TextView) findViewById(R.id.tv_which_build);
        which_room = (TextView) findViewById(R.id.tv_which_room);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.choose_fragment, new RightChoose());
        transaction.commit();
    }

    private void setview(List<Device> listDevice) {
        mDeviceAdapter = new DeviceAdapter(SearchDeviceActivity.this, listDevice, R.layout.item_layout);
        mListView.setAdapter(mDeviceAdapter);
    }

    private void setListener(final List<Device> listDevice) {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(SearchDeviceActivity.this, DevDetailActivity.class);
                intent.putExtra("device_id", listDevice.get(position).getDeviceId());
                startActivity(intent);
            }
        });
    }

    private void save(String data) {
        FileCache fileCache = new FileCache(SearchDeviceActivity.this);
        File file = fileCache.getFile2("list_data");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read(String filename) {
        //打开文件输入流
        FileCache fileCache = new FileCache(SearchDeviceActivity.this);
        File file = fileCache.getFile2(filename);
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

    private void setLvListener() {
        LvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }
    private void setRefreshListener() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(2000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        //从网络更新列表
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefresh.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }
    public All getAll() {
        return all;
    }

    public void setMainview(List<Device> listDevice) {
        int count=0;
        for(int i=0;i<listDevice.size();i++){
            if(listDevice.get(i).getUseFlag().equals("正在使用")){
                count++;
            }
        }
        mDeviceAdapter = new DeviceAdapter(SearchDeviceActivity.this, listDevice, R.layout.item_layout);
        mListView.setAdapter(mDeviceAdapter);
        device_all.setText(String.valueOf(listDevice.size()));
        device_run.setText(String.valueOf(count));
    }

    public void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    public void setWhich(String build,String room){
        if(build!=null) {
            which_build.setText(build);
        }
        if(room!=null) {
            which_room.setText(room);
        }
    }
}
