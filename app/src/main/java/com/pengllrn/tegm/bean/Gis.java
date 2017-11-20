package com.pengllrn.tegm.bean;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/20.
 */

public class Gis {
    private List<SchoolList> mSchoolLists;
    private List<BuildingList> mBuildingLists;
    private List<RoomList> mRoomLists;

    public List<SchoolList> getSchoolLists() {
        return mSchoolLists;
    }

    public List<BuildingList> getBuildingLists() {
        return mBuildingLists;
    }

    public List<RoomList> getRoomLists() {
        return mRoomLists;
    }
}
