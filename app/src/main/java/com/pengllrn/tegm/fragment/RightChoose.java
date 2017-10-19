package com.pengllrn.tegm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.activity.SearchDeviceActivity;
import com.pengllrn.tegm.adapter.MyPopuAdapter;
import com.pengllrn.tegm.bean.All;
import com.pengllrn.tegm.bean.Device;
import com.pengllrn.tegm.bean.Room;
import com.pengllrn.tegm.bean.SchoolById;
import com.pengllrn.tegm.bean.Type;

import java.util.ArrayList;
import java.util.List;

public class RightChoose extends Fragment {
    private SearchDeviceActivity mActivity;
    private MyPopuAdapter myPopuAdapter;
    private All all;

    private String c_building;
    private String c_room;
    private String c_type;
    private String c_stutas;

    private LinearLayout mBuildAll;
    private CheckBox mBuildCb;
    private LinearLayout mRoomAll;
    private CheckBox mRoomCb;
    private LinearLayout mTypeAll;
    private CheckBox mTypeCb;

    private List<String> buildName = new ArrayList<>();
    private List<String> roomName = new ArrayList<>();
    private List<String> typeName = new ArrayList<>();
    private TextView action_sure;
    private TextView action_setall;
    private TextView action_cancel;

    private List<Device> listDevice = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mActivity = (SearchDeviceActivity) context;
        }
        mActivity = (SearchDeviceActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myPopuAdapter = new MyPopuAdapter(getActivity());
        all = mActivity.getAll();
        initView(view);
        initBuildname(all.getSchoolbyid());
        initTypename(all.getType());
        setCbListener();
        setActionListener();

    }


    private void initView(View view) {
        mBuildAll = (LinearLayout) view.findViewById(R.id.ll_build_name1);
        mBuildCb = (CheckBox) view.findViewById(R.id.cb_build_name1);
        mRoomAll = (LinearLayout) view.findViewById(R.id.ll_room_name1);
        mRoomCb = (CheckBox) view.findViewById(R.id.cb_room_name1);
        mTypeAll = (LinearLayout) view.findViewById(R.id.ll_type_name1);
        mTypeCb = (CheckBox) view.findViewById(R.id.cb_type_name1);

        action_sure = (TextView) view.findViewById(R.id.tv_action_sure);
        action_setall = (TextView) view.findViewById(R.id.tv_action_setall);
        action_cancel = (TextView) view.findViewById(R.id.tv_action_cancel);
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
                            mRoomCb.setText("房间号");
                            c_room = null;
                            //实现筛选
                            c_building = buildName.get(position);
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
                            c_room = roomName.get(position);
                        }
                    }, mRoomCb, mTypeCb, mBuildCb);
                }
            }
        });
        mTypeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (typeName.size() != 0) {
                    myPopuAdapter.filterTabToggle(isChecked, mTypeAll, typeName, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            myPopuAdapter.hidePopListView();
                            mTypeCb.setText(typeName.get(position));
                            //实现筛选
                            c_type = typeName.get(position);
                        }
                    }, mTypeCb, mRoomCb, mBuildCb);
                }
            }
        });
    }

    private void setActionListener() {
        action_setall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.setMainview(all.getDevice());
                mActivity.closeDrawer();
            }
        });

        action_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.setMainview(getListDevice());
                mActivity.closeDrawer();
            }
        });

        action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.closeDrawer();
            }
        });
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

    private void initBuildname(List<SchoolById> schoolbyid) {
        buildName.clear();
        for (int i = 0; i < schoolbyid.size(); i++) {
            buildName.add(schoolbyid.get(i).getBuilding());
        }
    }

    private void initTypename(List<Type> type) {
        typeName.clear();
        for (int i = 0; i < type.size(); i++) {
            typeName.add(type.get(i).getTypename());
        }
    }

    private List<Device> getListDevice(){
        listDevice.clear();
        if(c_building!=null){
            for (int i = 0; i < all.getDevice().size(); i++) {
                if (all.getDevice().get(i).getBuildName().equals(c_building)) {
                    listDevice.add(all.getDevice().get(i));
                }
            }
        }
        if(c_room!=null){
            List<Device> roomdevice=new ArrayList<>();
            for (int i = 0; i < listDevice.size(); i++) {
                if (listDevice.get(i).getRoomName().equals(c_room)) {
                    roomdevice.add(listDevice.get(i));
                }
            }
            listDevice=roomdevice;
        }
        if(c_type!=null){
            List<Device> typedevice = new ArrayList<>();
            if(listDevice.size()>0){
                for (int i = 0; i < listDevice.size(); i++) {
                    if (listDevice.get(i).getDeviceType().equals(c_room)) {
                        typedevice.add(listDevice.get(i));
                    }
                }
                listDevice=typedevice;
            }else {
                for (int i = 0; i < all.getDevice().size(); i++) {
                    if (all.getDevice().get(i).getDeviceType().equals(c_type)) {
                        listDevice.add(all.getDevice().get(i));
                    }
                }
            }
        }
        return listDevice;
    }
}
