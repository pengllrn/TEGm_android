package com.pengllrn.tegm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/9/27.
 */

public class All {
    private List<Device> device=new ArrayList<>();
    private List<SchoolById> schoolbyid;
    private List<Type> type;

    public List<Device> getDevice() {
        return device;
    }

    public List<SchoolById> getSchoolbyid() {
        return schoolbyid;
    }

    public List<Type> getType() {
        return type;
    }
}
