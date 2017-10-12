package com.pengllrn.tegm.bean;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/10/5.
 */

public class DevDetail {
    private String devicenum;
    private String type;
    private String sensor;

    private String status;
    private String useflag;

    private String avgrate;
    private List<UseRate> userate_10;

    private Room roominfo;
    private String order;

    private String gis;

    private String schoolname;
    private String usedepart;

    private String checkid;
    private String checkname;
    private String checktel;

    private String regist_first;
    private String devicekind;
    private String description;
    private String configureinfo;

    public String getDevicenum() {
        return devicenum;
    }

    public String getType() {
        return type;
    }

    public String getSensor() {
        return sensor;
    }

    public String getStatus() {
        return status;
    }

    public String getUseflag() {
        return useflag;
    }

    public String getAvgrate() {
        return avgrate;
    }

    public List<UseRate> getUserate_10() {
        return userate_10;
    }

    public Room getRoominfo() {
        return roominfo;
    }

    public String getOrder() {
        return order;
    }

    public String getGis() {
        return gis;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public String getUsedepart() {
        return usedepart;
    }

    public String getCheckid() {
        return checkid;
    }

    public String getCheckname() {
        return checkname;
    }

    public String getChecktel() {
        return checktel;
    }

    public String getRegist_first() {
        return regist_first;
    }

    public String getDevicekind() {
        return devicekind;
    }

    public String getDescription() {
        return description;
    }

    public String getConfigureinfo() {
        return configureinfo;
    }

    public void setAvgrate(String avgrate) {
        this.avgrate = avgrate;
    }
}
