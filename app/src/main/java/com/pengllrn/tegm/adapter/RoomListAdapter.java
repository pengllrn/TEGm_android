package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.base.ListViewAdapter;
import com.pengllrn.tegm.base.ViewHolder;
import com.pengllrn.tegm.bean.RoomList;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/11/1.
 */

public class RoomListAdapter extends ListViewAdapter<RoomList> {
    public RoomListAdapter(Context context, List<RoomList> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, RoomList roomList) {
        holder.setText(R.id.item1,roomList.getRoomname());
        holder.setText(R.id.item2,roomList.getTotaldevice());
        holder.setText(R.id.item3,roomList.getUsingdevice());
    }
}
