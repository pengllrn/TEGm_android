package com.pengllrn.tegm.adapter;

import android.content.Context;

import com.pengllrn.tegm.R;
import com.pengllrn.tegm.bean.Device;
import com.pengllrn.tegm.utils.ImageLoader;
import com.pengllrn.tegm.utils.ListViewAdapter;
import com.pengllrn.tegm.utils.ViewHolder;

import java.util.List;

public class DeviceAdapter extends ListViewAdapter<Device> {
    private ImageLoader mImageLoader;
    public DeviceAdapter(Context context, List<Device> datas, int layoutId) {
        super(context, datas, layoutId);
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public void convert(ViewHolder holder,Device device) {
        holder.setText(R.id.tv_devicename, device.getDeviceType() + "  [" + device.getUseFlag() + "]");
        holder.setText(R.id.tv_roomname,"房间 "+device.getBuildName()+" "+device.getRoomName()+"  序号 "+device.getOrderNum());
        holder.setText(R.id.tv_devicenum,"编号 "+device.getDeviceNum());
        holder.setImageURI(R.id.img_pic,device.getImgUrl(),mImageLoader);
    }
}
