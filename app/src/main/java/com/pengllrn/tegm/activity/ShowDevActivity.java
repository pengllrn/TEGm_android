package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.adapter.DeviceAdapter;
import com.pengllrn.tegm.bean.All;
import com.pengllrn.tegm.bean.Device;
import com.pengllrn.tegm.bean.Room;
import com.pengllrn.tegm.bean.SchoolById;
import com.pengllrn.tegm.bean.Type;
import com.pengllrn.tegm.gson.ParseJson;
import com.pengllrn.tegm.internet.OkHttp;
import com.pengllrn.tegm.utils.FileCache;
import com.pengllrn.tegm.adapter.MyPopuAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShowDevActivity extends AppCompatActivity {
    private MyPopuAdapter myPopuAdapter;
    private ListView mListView;

    private List<String> buildName = new ArrayList<>();
    private List<String> roomName = new ArrayList<>();
    private List<String> typeName = new ArrayList<>();

    private DeviceAdapter mDeviceAdapter;

    //private List<Device> listDevice = new ArrayList<>();
    private String path = "http://192.168.1.20:9999/getdeviceinfo/";

    private ParseJson mParseJson = new ParseJson();

    private LinearLayout mBuildAll;
    private CheckBox mBuildCb;
    private LinearLayout mRoomAll;
    private CheckBox mRoomCb;
    private LinearLayout mTypeAll;
    private CheckBox mTypeCb;

    private All all;
    private List<Device> listDevice = new ArrayList<>();
    private List<Device> roomdevices = new ArrayList<>();
    private List<Device> typeDevice = new ArrayList<>();

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
                    Toast.makeText(ShowDevActivity.this, "设备列表已更新成功！", Toast.LENGTH_SHORT).show();
                    save(responseData);
                    initBuildname(all.getSchoolbyid());
                    initTypename(all.getType());
                    break;
                case 0x22:
                    Toast.makeText(ShowDevActivity.this, "服务器访问失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initTypename(List<Type> type) {
        typeName.clear();
        for (int i = 0; i < type.size(); i++) {
            typeName.add(type.get(i).getTypename());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dev);
        myPopuAdapter=new MyPopuAdapter(this);
        initview();
        //从本地获取列表
        String data = read("list_data");
        if (data != null && !data.equals("")) {
            all = mParseJson.JsonToAll(data);
            setview(all.getDevice());
            setListener(all.getDevice());
            initBuildname(all.getSchoolbyid());
            initTypename(all.getType());
        }
        //从网络更新列表
        OkHttp okHttp = new OkHttp(this, mHandler);
        okHttp.getDataFromInternet(path);
    }


    @Override
    protected void onResume() {
        super.onResume();

        setCbListener();
    }

    private void initview() {
        mListView = (ListView) findViewById(R.id.list_item);
        mBuildAll = (LinearLayout) findViewById(R.id.ll_build_name);
        mBuildCb = (CheckBox) findViewById(R.id.cb_build_name);
        mRoomAll = (LinearLayout) findViewById(R.id.ll_room_name);
        mRoomCb = (CheckBox) findViewById(R.id.cb_room_name);
        mTypeAll = (LinearLayout) findViewById(R.id.ll_type_name);
        mTypeCb = (CheckBox) findViewById(R.id.cb_type_name);
    }


    private void setview(List<Device> listDevice) {
        mDeviceAdapter = new DeviceAdapter(ShowDevActivity.this, listDevice, R.layout.item_layout);
        mListView.setAdapter(mDeviceAdapter);
    }

    private void setListener(final List<Device> listDevice) {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ShowDevActivity.this, DevDetailActivity.class);
                intent.putExtra("device_id", listDevice.get(position).getDeviceId());
                startActivity(intent);
            }
        });
    }

    private void save(String data) {
        FileCache fileCache = new FileCache(ShowDevActivity.this);
        File file = fileCache.getFile2("list_data");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBuildname(List<SchoolById> schoolbyid) {
        buildName.clear();
        for (int i = 0; i < schoolbyid.size(); i++) {
            buildName.add(schoolbyid.get(i).getBuilding());
        }
    }

    private void initRoomname(String buildname) {
        roomName.clear();
        for (int i = 0; i < all.getSchoolbyid().size(); i++) {
            if (all.getSchoolbyid().get(i).getBuilding().equals(buildname)) {
                List<Room> rooms = all.getSchoolbyid().get(i).getRoom();
                for (Room room : rooms) {
                    roomName.add(room.getRoomname());
                }
                return;
            }
        }
    }


    private void setCbListener() {

        // 点击选择教学楼整体
        mBuildAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuildCb.isChecked())
                    mBuildCb.setChecked(false);
                else
                    mBuildCb.setChecked(true);
            }
        });
        //点击选择房间整体
        mRoomAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRoomCb.isChecked())
                    mRoomCb.setChecked(false);
                else
                    mRoomCb.setChecked(true);
            }
        });
        //点击选择类型整体
        mTypeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTypeCb.isChecked())
                    mTypeCb.setChecked(false);
                else
                    mTypeCb.setChecked(true);
            }
        });
        //选择教学楼Cb
        mBuildCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (buildName != null) {
                    myPopuAdapter.filterTabToggle(isChecked, mBuildAll, buildName, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            myPopuAdapter.hidePopListView();
                            mBuildCb.setText(buildName.get(position));
                            initRoomname(buildName.get(position));
                            //实现筛选
                            chooseByBuild(buildName.get(position));
                        }
                    }, mBuildCb, mRoomCb, mTypeCb);
                }
            }
        });
        //
        mRoomCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (roomName != null) {
                    myPopuAdapter.filterTabToggle(isChecked, mRoomAll, roomName, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            myPopuAdapter.hidePopListView();
                            mRoomCb.setText(roomName.get(position));
                            //实现筛选
                            chooseByRoom(roomName.get(position));
                        }
                    }, mRoomCb, mTypeCb, mBuildCb);
                }
            }
        });
        mTypeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (typeName.size()!=0) {
                    myPopuAdapter.filterTabToggle(isChecked, mTypeAll, typeName, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            myPopuAdapter.hidePopListView();
                            mTypeCb.setText(typeName.get(position));
                            //实现筛选
                            chooseByType(typeName.get(position));
                        }
                    }, mTypeCb, mRoomCb, mBuildCb);
                }
            }
        });

    }

    private void chooseByType(String typename) {
        typeDevice.clear();
        if (listDevice.size()>0) {
            for (int i = 0; i < listDevice.size(); i++) {
                if (listDevice.get(i).getDeviceType().equals(typename)) {
                    typeDevice.add(listDevice.get(i));
                }
            }
            setview(typeDevice);
            setListener(typeDevice);
        } else {
            for (int i = 0; i < all.getDevice().size(); i++) {
                if (all.getDevice().get(i).getDeviceType().equals(typename)) {
                    typeDevice.add(all.getDevice().get(i));
                }
            }
            setview(typeDevice);
            setListener(typeDevice);
        }
    }

    private void chooseByRoom(String roomname) {
        roomdevices.clear();
        for (int i = 0; i < listDevice.size(); i++) {
            if (listDevice.get(i).getRoomName().equals(roomname)) {
                roomdevices.add(listDevice.get(i));
            }
        }
        setview(roomdevices);
        setListener(roomdevices);
    }

    private void chooseByBuild(String buildname) {
        listDevice.clear();
        if (typeDevice.size()>0) {
            for (int i = 0; i < typeDevice.size(); i++) {
                if (all.getDevice().get(i).getBuildName().equals(buildname)) {
                    listDevice.add(typeDevice.get(i));
                }
            }
            setview(listDevice);
            setListener(listDevice);
        } else {
            for (int i = 0; i < all.getDevice().size(); i++) {
                if (all.getDevice().get(i).getBuildName().equals(buildname)) {
                    listDevice.add(all.getDevice().get(i));
                }
            }
            setview(listDevice);
            setListener(listDevice);
        }
    }


    public String read(String filename) {
        //打开文件输入流
        FileCache fileCache = new FileCache(ShowDevActivity.this);
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


}
