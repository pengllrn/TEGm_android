package com.pengllrn.tegm.bean;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/9/27.
 */

public class Room {
    private String buildingname = "";
    private String roomname = "";

    public Room(String buildingname, String roomname) {
        this.buildingname = buildingname;
        this.roomname = roomname;
    }

    public String getBuildingname() {
        return buildingname;
    }

    public String getRoomname() {
        return roomname;
    }
}
