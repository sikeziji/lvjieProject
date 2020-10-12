package com.yzlm.cyl.cfragment.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

import java.util.List;

import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.isSendCountCmd;

/**
 * 文件的listview数据适配器
 * Created by WL on 2017/3/28.
 */

public class DirectoryAdapter extends BaseAdapter {
    private List<String[]> directoryDataList;

    /* 获取一个LayoutInflater对象用来导入布局*/
    private LayoutInflater mInflater;

    public DirectoryAdapter(List<String[]> directoryDataList, Context context) {
        this.directoryDataList = directoryDataList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {//填充的数据集数
        return directoryDataList.size();
    }

    @Override
    public Object getItem(int i) {//数据集中指定索引对应的数据项
        return null;
    }

    @Override
    public long getItemId(int i) {//指定行所对应的ID
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {//每个Item所显示的类容
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_directory, null);

            holder.imageDirectoryOrFile = (ImageView) view.findViewById(R.id.imageDirectoryOrFile);
            holder.textFileName = (TextView) view.findViewById(R.id.textFileName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        String measPointStr = directoryDataList.get(i)[2];

        switch (Integer.parseInt(directoryDataList.get(i)[0])){
            case 1:{//文件夹
                holder.imageDirectoryOrFile.setBackgroundResource(R.drawable.gpexpand);
                holder.imageDirectoryOrFile.setTag("expand");
            }break;
            case 2:{//文件
                holder.imageDirectoryOrFile.setBackgroundResource(R.drawable.gpshrink);
                holder.imageDirectoryOrFile.setTag("shrink");
            }break;
            default:{

            }break;
        }

        if (measPointStr.equals("null")) {
            holder.textFileName.setText("");
        } else {
            holder.textFileName.setText(measPointStr);
        }

        if (isSendCountCmd) {
            holder.textFileName.setTextColor(Color.BLUE);
        } else {
            holder.textFileName.setTextColor(Color.WHITE);
        }

//        holder.imageDirectoryOrFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        holder.textFileName.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//            }
//        });

        return view;
    }

    /**
     * 存放控件的ViewHolder
     */
    final class ViewHolder {
        ImageView imageDirectoryOrFile;
        TextView textFileName;
    }

    public void UpdateList(List<String[]> litems){
        this.directoryDataList = litems;
        notifyDataSetChanged();
    }
}
