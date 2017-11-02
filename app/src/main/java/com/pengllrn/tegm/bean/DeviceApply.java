package com.pengllrn.tegm.bean;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/2.
 */

public class DeviceApply {
    private List<DamageApplyList> damagelist;

    public DeviceApply(List<DamageApplyList> damagelist) {
        this.damagelist = damagelist;
    }

    public List<DamageApplyList> getDamagelist() {
        return damagelist;
    }
}
