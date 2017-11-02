package com.pengllrn.tegm.bean;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/1.
 */

public class DamageApplyList {
    private String devicenum;
    private String type;
    private String name;
    private String datetime;
    private String deviceid;

    public DamageApplyList(String devicenum, String type, String name, String datetime) {
        this.devicenum = devicenum;
        this.type = type;
        this.name = name;
        this.datetime = datetime;
    }

    public String getDevicenum() {
        return devicenum;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getDeviceid() {
        return deviceid;
    }
}
