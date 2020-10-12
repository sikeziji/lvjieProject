package com.yzlm.cyl.cfragment.Frame.Content;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getTimeHaveSec;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.manualStopFlag;
import static com.yzlm.cyl.cfragment.Global.reagentBottle;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;

/*  常规
 * Created by caoyiliang on 2016/10/27.
 */

public class Up_content3 extends SubFragment {

    private long firstClick;
    private Callbacks mCallbacks;
    // 计算点击的次数
    private int count;
    static Up_content3 fragment = null;

    public static Up_content3 newInstance() {
        if (fragment == null) {
            fragment = new Up_content3();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.up_content3_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            ImageButton mBtnJJTZ = v.findViewById(R.id.btn_jjtz);
            mBtnJJTZ.setOnClickListener(new btnJJTZClick());
            showBottleText(mCompName, v);
            TextView tZhcs = v.findViewById(R.id.M_CLZT);
            tZhcs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), getConfigData(mCompName, "zhcsFlow"), Toast.LENGTH_SHORT).show();
                }
            });
            TextView mDqzt = v.findViewById(R.id.M_DQZT);
            mDqzt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), ((AutoSampleEnable.get(mCompName).equals("true")) ? "" : getString(R.string.auto_measurement_is_closed))
                            + getString(R.string.working_condition) + ":" + workState.get(mCompName), Toast.LENGTH_LONG).show();
                }
            });
            LinearLayout mLayoutPump = v.findViewById(R.id.peristalticPump);
            LinearLayout mLayoutMeasuringModule = v.findViewById(R.id.measuringModule);
            if (QueryMeasBoardType(mCompName).equals("2")) {
                mLayoutPump.setVisibility(View.GONE);
                mLayoutMeasuringModule.setVisibility(View.GONE);
            } else {
                mLayoutPump.setVisibility(View.VISIBLE);
                mLayoutMeasuringModule.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "] Up_content3 " + e.toString());
        }

    }

    private class btnJJTZClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
            if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
                count = 0;
            }
            count++;
            if (count == 1) {
                firstClick = System.currentTimeMillis();
            } else if (count == 2) {
                long lastClick = System.currentTimeMillis();
                // 两次点击小于300ms 也就是连续点击
                if (lastClick - firstClick < 300) {// 判断是否是执行了双击事件
                    //System.out.println(">>>>>>>>执行了双击事件");
                    stopWorking(mCompName, true);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * 主界面试剂瓶显示名称
     */
    public static void showBottleText(String comp, View v) {
        TextView tvsjp1 = v.findViewById(R.id.text_sjp1);
        TextView tvsjp2 = v.findViewById(R.id.text_sjp2);
        TextView tvsjp3 = v.findViewById(R.id.text_sjp3);
        TextView tvsjp4 = v.findViewById(R.id.text_sjp4);
        TextView tvsjp5 = v.findViewById(R.id.text_sjp5);
        TextView tvsjp6 = v.findViewById(R.id.text_sjp6);
        TextView tvsjp7 = v.findViewById(R.id.text_sjp7);
        TextView tvsjp8 = v.findViewById(R.id.text_sjp8);

        try {
            List<String> valve;
            valve = reagentBottle.get(comp);
            for (int i = 0, j = 1; i < valve.size(); i++, j++) {

                String strValue = valve.get(i);
                String[] strBolate = strValue.split("//")[0].split("[,，]");
                if (strBolate.length > 1) {
                    strValue = strBolate[0].replace(context.getResources().getString(R.string.valve), "");
                }
                switch (Integer.parseInt(strBolate[1])) {
                    case 1:
                        tvsjp1.setText(strValue);
                        break;
                    case 2:
                        tvsjp2.setText(strValue);
                        break;
                    case 3:
                        tvsjp3.setText(strValue);
                        break;
                    case 4:
                        tvsjp4.setText(strValue);
                        break;
                    case 5:
                        tvsjp5.setText(strValue);
                        break;
                    case 6:
                        tvsjp6.setText(strValue);
                        break;
                    case 7:
                        tvsjp7.setText(strValue);
                        break;
                    case 8:
                        tvsjp8.setText(strValue);
                        break;
                }
            }
        } catch (Exception e) {
            Log.i("showBottleText except", e.toString());
        }
    }

    /**
     * 紧急停止
     *
     * @param mCompName 组分名称
     * @param blManual  是否手动触发
     */
    public static void stopWorking(String mCompName, boolean blManual) {
        ActionTable at = Global.getActions(mCompName);
        String action = at.GetActionName(getCmds(mCompName).getCmd(52).getValue());
        if (!doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions)) || !action.equals(context.getString(R.string.ready))) {

            SendManager.SendCmd(mCompName + "_紧急停止" + "_8_07", S0, 3, 1000, null);
            AddError(mCompName, 613, 运维_信息);
            manualStopFlag.put(mCompName, "true");


            if (isHaveMeasCategory(mCompName, "5")) {
                SendManager.SendCmd(mCompName + getModuleName("5") + "_紧急停止" + "_8_07", S0, 3, 1000, null);
            }
            if (isHaveMeasCategory(mCompName, "6")) {
                SendManager.SendCmd(mCompName + getModuleName("6") + "_紧急停止" + "_8_07", S0, 3, 1000, null);
            }
            saveOperationLogMsg(mCompName, "紧急停止", ErrorLog.msgType.操作_信息);
            //紧急停止
            AddError(mCompName, 613, 运维_信息);
            manualStopFlag.put(mCompName, "true");

            if (getPublicConfigData("SYS_RELAYCONF").equals("1")) {
                SendManager.SendCmd("IO" + "_" + "接口板停止_0C_14", S1, 3, 500, null);
            } else if (getPublicConfigData("SYS_RELAYCONF").equals("3")) {
                SendManager.SendCmd("IO" + "_" + "接口板停止_06_02", S1, 3, 500, new byte[]{0x00, 0x00, 0x00, 0x00});
            }
            if (getConfigData(mCompName, "zhcsSwitch").equals("true")) {
                updateConfigData(mCompName, "zhcsSwitch", "false");
            }
            if (getConfigData(mCompName, "zhcsStartFlag").equals("true")) {
                updateConfigData(mCompName, "zhcsStartFlag", "false");
            }
            if (getConfigData(mCompName, "ycjzFlag").equals("true")) {
                updateConfigData(mCompName, "ycjzFlag", "false");
            }
            updateConfigData(mCompName, "zdzyTimer", String.valueOf(0));

            if (getConfigData(mCompName, "OneKeyCalStartFlag").equals("true")) {
                updateConfigData(mCompName, "OneKeyCalStartFlag", "false");
            }
            if (getConfigData(mCompName, "OneKeyCalForceXFlag").equals("true")) {
                updateConfigData(mCompName, "OneKeyCalForceXFlag", "false");
            }
            if ((getConfigData(mCompName, "OneKeyCalForceKBFlag").equals("true"))) {
                updateConfigData(mCompName, "OneKeyCalForceKBFlag", "false");
            }
            if (!getConfigData(mCompName, "START_ZY_JOB_TIME").equals("")) {
                updateConfigData(mCompName, "START_ZY_JOB_TIME", "");
            }
            //紧急停止时将多点线性标志位修改为false
            if (!getConfigData(mCompName, "Multi_Point_Linear").equals("true")) {
                updateConfigData(mCompName, "Multi_Point_Linear", "false");
            }

            // 手动
            if (blManual) {
                //W200
                if (getConfigData(mCompName, "DoorCloseDataCounter").equals("1")) {
                    updateConfigData(mCompName, "DoorCloseDataCounter", "0");
                }
                if (!getConfigData(mCompName, "MeaAValueEn").equals("")) {
                    updateConfigData(mCompName, "MeaAValueEn", "");
                }
                // 当前有吸光度显示值
                if (!getConfigData(mCompName, "nowHistoryDataAEn").equals("")) {
                    updateConfigData(mCompName, "nowHistoryDataAEn", "");
                }
            }

            if (getConfigData(mCompName, "CAL_Start_Flag").equals("true")) {
                updateConfigData(mCompName, "CAL_Start_Flag", "false");
            }
            if (getConfigData(mCompName, "CAL2_Start_Flag").equals("true")) {
                updateConfigData(mCompName, "CAL2_Start_Flag", "false");
            }
            if (!getConfigData(mCompName, "StartFlowName").equals("")) {
                updateConfigData(mCompName, "StartFlowName", "");
            }

            //刷新自动标样核查标志位
            if (getConfigData(mCompName, "BHJZFlag").equals("true")) {
                updateConfigData(mCompName, "BHJZFlag", "false");
            }
            //刷新自动标样核查次数
            if (!getConfigData(mCompName, "BHJZCount").equals("")) {
                updateConfigData(mCompName, "BHJZCount", "");
            }
            updateConfigData(mCompName, "StopFlowTime", getTimeHaveSec());
        }
    }
}
