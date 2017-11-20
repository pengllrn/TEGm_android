package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.base.ListViewAdapter;
import com.pengllrn.tegm.base.ViewHolder;
import com.pengllrn.tegm.bean.SchoolList;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/1.
 */

public class SchoolListAdapter extends ListViewAdapter<SchoolList> {
    public SchoolListAdapter(Context context, List<SchoolList> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, SchoolList schoolList) {
        holder.setText(R.id.item1,schoolList.getSchoolname());
        holder.setText(R.id.item2,schoolList.getTotaldevice());
        holder.setText(R.id.item3,schoolList.getUsingdevice());
    }
}
