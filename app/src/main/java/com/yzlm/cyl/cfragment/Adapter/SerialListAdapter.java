package com.yzlm.cyl.cfragment.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Content.beans.SerialBean;
import com.yzlm.cyl.cfragment.R;

import java.util.List;

/**
 * Create by duyang
 * Create on 2020-08-14
 * description 串口配置列表适配器
 */
public abstract class SerialListAdapter extends BaseAdapter {


    private List<SerialBean> serialList;
    private Context context;

    public SerialListAdapter(Context context, List<SerialBean> serialList) {
        this.context = context;
        this.serialList = serialList;
    }


    @Override
    public int getCount() {
        return serialList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.serialList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(this.context, R.layout.list4_content7_serial_item, null);

            holder.serialType = (TextView) view.findViewById(R.id.item_serial_type);
            holder.serialName = (TextView) view.findViewById(R.id.item_serial_name);
            holder.serialBaudRate = (TextView) view.findViewById(R.id.item_serial_baudRate);
            holder.btnDelete = view.findViewById(R.id.btn_serial_delete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String type = serialList.get(position).getType();
        String name = serialList.get(position).getName();
        String baudRate = serialList.get(position).getBaudRate();
        holder.serialName.setText(name);
        holder.serialType.setText(type);
        holder.serialBaudRate.setText(baudRate);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSerial(position);
            }
        });

        return view;
    }



    /**
     * 存放控件的ViewHolder
     */
    final class ViewHolder {
        TextView serialType;
        TextView serialName;
        TextView serialBaudRate;
        TextView btnDelete;
    }


    /**
     * 删除某个串口配置
     * @param position
     */
    public abstract void deleteSerial(int position);

}
