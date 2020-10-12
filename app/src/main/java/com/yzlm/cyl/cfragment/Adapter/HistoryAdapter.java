package com.yzlm.cyl.cfragment.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;

/**
 * Created by WL on 2017/4/16.
 */

public abstract class HistoryAdapter extends BaseAdapter {
    private List<String> items;
    private Context context;
    //CSideslipListView cSideslipListView;
    private boolean contentIsShow = false;

    //public HistoryAdapter(Context context, List<String> items, CSideslipListView cSideslipListView,boolean isShow) {
    protected HistoryAdapter(Context context, List<String> items, boolean isShow) {
        this.context = context;
        this.items = items;
//        this.cSideslipListView = cSideslipListView;
        this.contentIsShow = isShow;
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
        historyViewHolder viewHolder;
        if (null == convertView) {
            if (contentIsShow) {
                convertView = View.inflate(this.context, R.layout.histoty_listview, (ViewGroup) null);
                viewHolder = new historyViewHolder();
                viewHolder.textView_id = (TextView) convertView.findViewById(R.id.histoty_itemId);
                viewHolder.textView_time = (TextView) convertView.findViewById(R.id.histoty_itemTime);
                viewHolder.textView_conc = (TextView) convertView.findViewById(R.id.histoty_itemConc);
                viewHolder.textView_flow = (TextView) convertView.findViewById(R.id.histoty_itemFlow);
                viewHolder.textView_energy = (TextView) convertView.findViewById(R.id.histoty_itemEnergy);
                viewHolder.textView_tag = (TextView) convertView.findViewById(R.id.histoty_itemTag);
                //viewHolder.txtv_delete = (TextView)convertView.findViewById(R.id.histoty_txtv_delete);
            } else {
                convertView = View.inflate(this.context, R.layout.histoty_listview2, (ViewGroup) null);
                viewHolder = new historyViewHolder();
                viewHolder.textView_id = (TextView) convertView.findViewById(R.id.histoty_itemId);
                viewHolder.textView_time = (TextView) convertView.findViewById(R.id.histoty_itemTime);
                viewHolder.textView_conc = (TextView) convertView.findViewById(R.id.histoty_itemConc);
                viewHolder.textView_flow = (TextView) convertView.findViewById(R.id.histoty_itemFlow);
                viewHolder.textView_tag = (TextView) convertView.findViewById(R.id.histoty_itemTag);
                //viewHolder.txtv_delete = (TextView)convertView.findViewById(R.id.histoty_txtv_delete);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (historyViewHolder) convertView.getTag();
        }
        String[] itemContent = this.items.get(position).split("\t");
        viewHolder.textView_id.setText(itemContent[0]);
        viewHolder.textView_time.setText(itemContent[1]);
        viewHolder.textView_conc.setText(itemContent[2]);
        String strTag;
        if (itemContent.length > 5) {
            strTag = itemContent[5];
            viewHolder.textView_tag.setText(strTag);
        } else {
            viewHolder.textView_tag.setText("");
        }
        viewHolder.textView_flow.setText(itemContent[3]);
        if (viewHolder.textView_energy != null) {
            try {
                if (QueryMeasCateg(itemContent[2].split("=")[0]).equals("4")) {
                    String[] item = itemContent[4].split(";");
                    String content = "";
                    for (String s : item) {
                        String unitStr = getConfigData(mCompName, "UNIT");
                        String rs = ConvertUnit("mg/L", unitStr, Double.parseDouble(s.split("=")[1].split(" ")[0]), Integer.parseInt(getConfigData(itemContent[2].split("=")[0], "YXWS")));
                        content += (s.split("=")[0] + "=" + rs + " " + unitStr + "\r\n");
                    }
                    viewHolder.textView_energy.setText(content);
                } else {
                    viewHolder.textView_energy.setText(itemContent[4]);
                }
            } catch (Exception e) {
            }
        }
//        viewHolder.txtv_delete.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                HistoryAdapter.this.Delete(position);
//                HistoryAdapter.this.notifyDataSetChanged();
//                HistoryAdapter.this.cSideslipListView.turnNormal();
//            }
//        });
        return convertView;
    }

    public abstract void Delete(int var1);

    public void UpdateList(List<String> litems, boolean isShow){
        this.items = litems;
        this.contentIsShow = isShow;
        notifyDataSetChanged();
    }
}


