package com.yzlm.cyl.cfragment.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_MeasPoint;
import com.yzlm.cyl.cfragment.R;

import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.arrayListEnergy;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.isSendCountCmd;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.packMeasPoint;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.setPointSum;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.tempLog;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * 测量单元选项光谱界面的能量值的listview数据适配器
 * Created by WL on 2017/3/28.
 */

public class LVAdapter extends BaseAdapter {
    private List<String[]> energyDataList;

    /* 获取一个LayoutInflater对象用来导入布局*/
    private LayoutInflater mInflater;

    private boolean Admin = false;

    //哑元构造器
    public LVAdapter(List<String[]> _energyDataList, Context context,boolean flag){
        energyDataList = _energyDataList;
        this.mInflater = LayoutInflater.from(context);
        notifyDataSetChanged();
    }
    public LVAdapter(boolean admin,List<String[]> _energyDataList, Context context) {
        Admin = admin;
        energyDataList = _energyDataList;
        /* 若最后一行不是添加按钮，默认在最后一行加入添加按钮*/
        if (energyDataList.size() > 0) {
            if (!energyDataList.get(energyDataList.size() - 1)[0].equals("expand")) {
                energyDataList.add(new String[]{"expand", "null", "null", "null", "null"});
            }
        } else {
            energyDataList.add(new String[]{"expand", "null", "null", "null", "null"});
        }

        this.mInflater = LayoutInflater.from(context);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return energyDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_gpenergy, null);

            holder.ImageviewShrinkExpand = (ImageView) view.findViewById(R.id.image_shrinkOrExpand);
            holder.TvMeasPoint = (TextView) view.findViewById(R.id.tvMeasPoint);
            holder.TvEnergy = (TextView) view.findViewById(R.id.tvEnergy);
            holder.TvVoltage = (TextView) view.findViewById(R.id.tvVoltage);
            holder.TvLampTimes = (TextView) view.findViewById(R.id.tvLampTimes);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (energyDataList.get(i)[0].equals("shrink")) {
            holder.ImageviewShrinkExpand.setBackgroundResource(R.drawable.gpshrink);
            holder.ImageviewShrinkExpand.setTag("shrink");
        } else {
            holder.ImageviewShrinkExpand.setBackgroundResource(R.drawable.gpexpand);
            holder.ImageviewShrinkExpand.setTag("expand");
        }
        String measPointStr = energyDataList.get(i)[1];
        String measEnergyStr = energyDataList.get(i)[2];

        if (measPointStr.equals("null")) {
            holder.TvMeasPoint.setText("");
        } else {
            holder.TvMeasPoint.setText(measPointStr);
        }

        if (measEnergyStr.equals("null")) {
            holder.TvEnergy.setText("");
        } else {
            holder.TvEnergy.setText(measEnergyStr);
        }
        // 新增测量点打灯电压
        if (energyDataList.get(i).length > 3) {
            String measPointVoltage = energyDataList.get(i)[3];
            if (measPointVoltage.equals("null")) {
                holder.TvVoltage.setText("");
            } else {
                holder.TvVoltage.setText(measPointVoltage);
            }
        }
        // 新增测量点打灯次数
        if (energyDataList.get(i).length > 4) {
            String measPointVoltage = energyDataList.get(i)[4];
            if (measPointVoltage.equals("null")) {
                holder.TvLampTimes.setText("");
            } else {
                holder.TvLampTimes.setText(measPointVoltage);
            }
        }
        if (isSendCountCmd) {
            holder.TvMeasPoint.setTextColor(Color.BLUE);
            holder.TvEnergy.setTextColor(Color.BLUE);
            holder.TvVoltage.setTextColor(Color.BLUE);
            holder.TvLampTimes.setTextColor(Color.BLUE);
        } else {
            holder.TvMeasPoint.setTextColor(Color.WHITE);
            holder.TvEnergy.setTextColor(Color.WHITE);
            holder.TvVoltage.setTextColor(Color.WHITE);
            holder.TvLampTimes.setTextColor(Color.WHITE);
        }
        if(!Admin){
            holder.ImageviewShrinkExpand.setEnabled(false);
        }

        holder.ImageviewShrinkExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag().toString().equals("shrink")) {
                    if (tempLog.equals("")){
                        for (int i = 0; i < energyDataList.size() - 1; ++i) {
                            tempLog += energyDataList.get(i)[1] + ";";
                        }
                    }
                    energyDataList.remove(i);
                    notifyDataSetChanged();

                    byte[] specPointByte = new byte[0];
                    for (int index = 0; index < energyDataList.size() - 1; index++) {
                        String[] str = energyDataList.get(index);
                        String[] pointVal = str[1].substring(0, str[1].length() - 3).split("-");
                        if (pointVal.length == 1) {
                            byte[] pointValByte = copybyte(toByteArray(Integer.parseInt(pointVal[0]), 4), toByteArray(Integer.parseInt(pointVal[0]), 4));
                            specPointByte = copybyte(specPointByte, pointValByte);
                        } else {
                            byte[] pointValByte = copybyte(toByteArray(Integer.parseInt(pointVal[0]), 4), toByteArray(Integer.parseInt(pointVal[1]), 4));
                            specPointByte = copybyte(specPointByte, pointValByte);
                        }
                    }

                    // 17 寄存器地址索引值 (Regaddr)
                    SendManager.SendCmd(mCompName + "_" + "设置光谱测量点_06_17", S0, 3, 500, specPointByte);

                    byte[] specPointVolByte = new byte[0];
                    for (int index = 0; index < energyDataList.size() - 1; index++) {
                        String[] str = energyDataList.get(index);
                        String pointVolVal = str[3].replace(" ", "");

                        specPointVolByte = copybyte(specPointVolByte, toByteArray(Integer.parseInt(pointVolVal), 4));
                    }
                    SendManager.SendCmd(mCompName + "_" + "设置光谱测量点电压_06_" + 172, S0, 3, 200, specPointVolByte);

                    byte[] specPointLampTimesByte = new byte[0];
                    for (int index = 0; index < energyDataList.size() - 1; index++) {
                        String[] str = energyDataList.get(index);
                        String pointLapmTimesVal = str[4].replace(" ", "");

                        specPointLampTimesByte = copybyte(specPointLampTimesByte, toByteArray(Integer.parseInt(pointLapmTimesVal), 4));
                    }
                    SendManager.SendCmd(mCompName + "_" + "设置光谱测量点打灯次数_06_" + 192, S0, 3, 200, specPointLampTimesByte);

                    setPointSum(arrayListEnergy.size() - 1);

                    isSendCountCmd = true;

                    if (getCount() < 1) {
                        energyDataList.add(new String[]{"expand", "null", "null", "null", "null"});
                        arrayListEnergy.add(new String[]{"expand", "null", "null", "null", "null"});
                    }
                    notifyDataSetChanged();
                    packMeasPoint();

                    if (energyDataList.size()==1){
                        saveOperationLogMsg(mCompName, mCompName + "_删除测量点: " + tempLog, ErrorLog.msgType.操作_信息);
                        AddError(mCompName, 619, ErrorLog.msgType.运维_信息);
                    }else{
                        List4_Content3_pgpcldy.operationFlag = true;
                    }
                    List4_Content3_pgpcldy.bChangeLV_energy = true;
                } else {
                    if ((arrayListEnergy.size() - 1) < 20) {
                        if (getCount() == 1 && energyDataList.get(0)[1].equals("")) {
                            view.setBackgroundResource(R.drawable.gpexpand);
                            view.setTag("expand");
                        }
                        /* 关闭窗体悬浮*/
                        main.removeDestopText(mfb);
                        Dialog_MeasPoint dialog_measPoint = new Dialog_MeasPoint();
                        dialog_measPoint.setTargetFragment(List4_Content3_pgpcldy.fragment, 1);
                        dialog_measPoint.show(fm, "energy");
                    }else {
                        Toast.makeText(context,context.getString(R.string.set_measurement_pointsSum_beyond_range),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

    /**
     * 存放控件的ViewHolder
     */
    final class ViewHolder {
        ImageView ImageviewShrinkExpand;
        TextView TvMeasPoint;
        TextView TvEnergy;
        TextView TvVoltage;
        TextView TvLampTimes;
    }
}
