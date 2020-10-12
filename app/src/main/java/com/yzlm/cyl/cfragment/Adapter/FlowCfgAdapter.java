package com.yzlm.cyl.cfragment.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

import java.util.List;


/**
 * Created by zwj on 2017/7/13.
 */

public class FlowCfgAdapter extends BaseAdapter {

    private List<FlowInfo> items;
    private Context context;

    protected FlowCfgAdapter(Context context, List<FlowInfo> items) {
        this.context = context;
        this.items = items;
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int position) {
        return this.items.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        FlowInfoHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(this.context, R.layout.flowitem, (ViewGroup) null);
            viewHolder = new FlowInfoHolder();

            viewHolder.actionName = (TextView) convertView.findViewById(R.id.txName);
            viewHolder.upDown = (TextView) convertView.findViewById(R.id.txUpDown);
            viewHolder.times = (TextView) convertView.findViewById(R.id.txTimes);
            viewHolder.coding = (TextView) convertView.findViewById(R.id.txCoding);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FlowInfoHolder) convertView.getTag();
        }
        FlowInfo flowInfo = items.get(position);
        viewHolder.actionName.setText(flowInfo.getActionName());
        viewHolder.upDown.setText(String.valueOf(flowInfo.getUpDown()&0XFFFF));
        viewHolder.times.setText(String.valueOf(flowInfo.getTimes()&0XFFFF));
        viewHolder.coding.setText(String.valueOf(flowInfo.getCodeing()&0XFFFF));
        return convertView;
    }

    public void UpdateList(List<FlowInfo> litems){
        this.items = litems;
        notifyDataSetChanged();
    }
}
