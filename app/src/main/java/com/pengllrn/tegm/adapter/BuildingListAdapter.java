package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.base.ListViewAdapter;
import com.pengllrn.tegm.base.ViewHolder;
import com.pengllrn.tegm.bean.BuildingList;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/1.
 */

public class BuildingListAdapter extends ListViewAdapter<BuildingList> {
    public BuildingListAdapter(Context context, List<BuildingList> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, BuildingList buildingList) {
        holder.setText(R.id.item1,buildingList.getSchoolname());
        holder.setText(R.id.item2,buildingList.getTotaldevice());
        holder.setText(R.id.item3,buildingList.getUsingdevice());
    }
}
