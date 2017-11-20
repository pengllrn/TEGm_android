package com.pengllrn.tegm.gson;

import com.google.gson.Gson;
import com.pengllrn.tegm.bean.All;
import com.pengllrn.tegm.bean.DamageDevice;
import com.pengllrn.tegm.bean.DevDetail;
import com.pengllrn.tegm.bean.Device;
import com.pengllrn.tegm.bean.DeviceApply;
import com.pengllrn.tegm.bean.Gis;

import java.util.ArrayList;
import java.util.List;

public class ParseJson {
    public List<Device> JsonToDevice(String json) {
        Gson gson = new Gson();
        All all = gson.fromJson(json, All.class);
        return all.getDevice();
    }

    public List<String> JsonToBuildname(String json) {
        Gson gson = new Gson();
        All all = gson.fromJson(json, All.class);
        List<String> buildName = new ArrayList<>();
        for (int i = 0; i < all.getSchoolbyid().size(); i++) {
            buildName.add(all.getSchoolbyid().get(i).getBuilding());
        }
        return buildName;
    }
    public All JsonToAll(String json){
        Gson gson = new Gson();
        All all = gson.fromJson(json, All.class);
        return all;
    }

    public DevDetail JsonToDevDetail(String json){
        Gson gson = new Gson();
        DevDetail detail = gson.fromJson(json,DevDetail.class);
        return detail;
    }

    public DeviceApply Json2DamageList(String json){
        Gson gson = new Gson();
        DeviceApply deviceApply = gson.fromJson(json,DeviceApply.class);
        return deviceApply;
    }

    public DamageDevice Json2DamageDevice(String json){
        Gson gson = new Gson();
        DamageDevice damageDevice = gson.fromJson(json,DamageDevice.class);
        return damageDevice;
    }

    public Gis Json2Gis(String json){
        Gson gson = new Gson();
        Gis gis = gson.fromJson(json,Gis.class);
        return gis;
    }

}
