package com.yzlm.cyl.cfragment.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

import java.util.List;

/**
 * Created by WL on 2017/4/16.
 */

public class CalibAdapter extends BaseAdapter {
    private List<String> items;
    private Context context;
    int iBShow = View.VISIBLE;
    int iFShow = View.VISIBLE;

    protected CalibAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    protected CalibAdapter(Context context, List<String> items, int iBShow, int iFShow) {
        this.context = context;
        this.items = items;
        this.iBShow = iBShow;
        this.iFShow = iFShow;
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
        CalibViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(this.context, R.layout.calibration_listview, (ViewGroup) null);
            viewHolder = new CalibViewHolder();
            viewHolder.textView_id = (TextView) convertView.findViewById(R.id.calib_Id);
            viewHolder.textView_time = (TextView) convertView.findViewById(R.id.calib_Time);
            viewHolder.textView_comp = (TextView) convertView.findViewById(R.id.calib_Comp);
            viewHolder.textView_range = (TextView) convertView.findViewById(R.id.calib_Range);
            viewHolder.textView_K = (TextView) convertView.findViewById(R.id.calib_K);
            viewHolder.textView_B = (TextView) convertView.findViewById(R.id.calib_B);
            viewHolder.textView_F = (TextView) convertView.findViewById(R.id.calib_F);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CalibViewHolder) convertView.getTag();
        }
        String[] itemContent = this.items.get(position).split("\t");
        viewHolder.textView_id.setText(itemContent[0]);
        viewHolder.textView_time.setText(itemContent[1]);
        viewHolder.textView_comp.setText(itemContent[2]);
        viewHolder.textView_range.setText(itemContent[3]);
        viewHolder.textView_K.setText(itemContent[4]);
        viewHolder.textView_B.setText(itemContent[5]);
        viewHolder.textView_F.setText(itemContent[6]);

        // 界面控件显示设置
        viewHolder.textView_B.setVisibility(iBShow);
        viewHolder.textView_F.setVisibility(iFShow);

        return convertView;
    }

    public void UpdateList(List<String> litems) {
        this.items = litems;
        notifyDataSetChanged();
    }
}


