package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.base.ListViewAdapter;
import com.pengllrn.tegm.base.ViewHolder;
import com.pengllrn.tegm.bean.DamageApplyList;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/1.
 */

public class DamageApplyAdapter extends ListViewAdapter<DamageApplyList> {
    public DamageApplyAdapter(Context context, List<DamageApplyList> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, DamageApplyList damageApplyList) {
        holder.setText(R.id.item_name,damageApplyList.getName());
        holder.setText(R.id.item_number,damageApplyList.getDevicenum());
        holder.setText(R.id.item_type,damageApplyList.getType());
        holder.setText(R.id.item_time,damageApplyList.getDatetime());
    }
}
