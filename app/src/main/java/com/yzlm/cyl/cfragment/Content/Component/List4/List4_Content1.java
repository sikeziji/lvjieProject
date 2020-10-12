package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionStep;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Dialog.Dialog_funTestOk;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.phoneName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getFunctionalTest;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content1 extends SubFragment {
    private static Callbacks mCallbacks;
    private static List4_Content1 fragment = null;

    public static List4_Content1 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content1();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogRS();

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
        if(!phoneName.equals("rk3288")){
            mCallbacks = null;
        }
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list4_content1;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            getMeasCategoryWin();
            Map<Integer, ActionStep> Ft = getFunctionalTest(mCompName);

            if (Ft == null) return;
            GridLayout mGL = v.findViewById(R.id.GL);
            mGL.removeAllViews();
            for (int i = 0; i < Ft.size(); i++) {
                try {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    Button mBtn = inflater.inflate(R.layout.ft_button, null).findViewById(R.id.btn_ft);
                    mGL.addView(mBtn);
                    mBtn.setText(Ft.get(i + 1).getName());
                    GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) mBtn.getLayoutParams();
                    layoutParams.setMargins(20, 20, 20, 20);
                    mBtn.setLayoutParams(layoutParams);
                    mBtn.setOnClickListener(new mBtnClick());
                    mBtn.setId(i + 1);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    /**
     * 测量类型 需要处理的界面
     */
    private void getMeasCategoryWin() {
        if (QueryMeasCateg(mCompName).equals("5") || QueryMeasCateg(mCompName).equals("6")) {
            Global.LockDisplayShowFlag = true;
        }
    }


    private class mBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                Map<Integer, ActionStep> Ft = getFunctionalTest(mCompName);

                /* 关闭窗体悬浮（WL注释）*/
                main.removeDestopText(mfb);

                //W200,且是"开采样"动作
                if (QueryMeasBoardType(mCompName).equals("2") && ((Button) v).getText().toString().equals(getString(R.string.open_pump))) {
                    //开采样
                    SendManager.SendCmd("IO" + "_" + "接口板开采样_06_02", S1, 3, 500, new byte[]{0x00, 0x01, 0x00, 0x00});
                }

                Dialog_funTestOk st = new Dialog_funTestOk();
                Bundle bundle = new Bundle();
                if (Ft.get(v.getId()).getWindow().equals("是")) {
                    bundle.putString("alert-windows", "是");//是否显示高低 次数
                    bundle.putString("alert-high-low", String.valueOf(Ft.get(v.getId()).getMeasurement()));//高低
                    bundle.putString("alert-number", String.valueOf(Ft.get(v.getId()).getSampleCount()));//次数
                } else {
                    bundle.putString("alert-windows", "否");//是否显示高低 次数
                }
                bundle.putString("alert-ID", String.valueOf(v.getId()));//次数
                bundle.putString("alert-ok", ((Button) v).getText().toString());
                st.setArguments(bundle);
                st.setTargetFragment(List4_Content1.this, 1);
                st.show(fm, "alert-ok");

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command");
            mCallbacks.onDialogRS();
            String[] str = stringExtra.split("_");
            Map<Integer, ActionStep> ft = Global.getFunctionalTest(str[0]);
            if (ft == null) {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", getString(R.string.failed_find_action_command));
                st.setArguments(bundle);
                st.show(fm, "Dialog_err");
                return;
            }
            ActionStep fc = Global.getActionStep(ft, str[1]);
            if (fc == null) {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", getString(R.string.failed_find_action_command));
                st.setArguments(bundle);
                st.show(fm, "Dialog_err");
                return;
            }
            ActionTable at = Global.getActions(mCompName);
            float tempMax = 0;
            if (QueryMeasCateg(mCompName).equals("3") || QueryMeasCateg(mCompName).equals("8")
                    || QueryMeasCateg(mCompName).equals("4")) {
                tempMax = 80.0f;
            } else if (QueryMeasCateg(mCompName).equals("11")) {
                tempMax = 700.0f;
            } else {
                tempMax = 60.0f;
            }

            /*温度大于60度，除了降温动作可以执行**/
            if ((((getCmds(MainActivity.mCompName).getCmd(55).getValue() == null ? 0.0f : (float) getCmds(MainActivity.mCompName).getCmd(55).getValue()) < tempMax))
                    || ((at.getAction(fc.getName()).getCmd() == 517) || (at.getAction(fc.getName()).getCmd() == 518) || (at.getAction(fc.getName()).getCmd() == 544)
                    || (at.getAction(fc.getName()).getCmd() == 1257))) {

                saveOperationLogMsg(mCompName, "维护手动操作-" + str[1], ErrorLog.msgType.操作_信息);
                Toast.makeText(context, getString(R.string.readyStarted) + stringExtra + context.getResources().getString(R.string.pleaseWaiting), Toast.LENGTH_SHORT).show();
                SendManager.SendCmd(stringExtra + "_8_04", S0, 3, 1000, fc);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", getString(R.string.device_temper_to_high) + tempMax + "  ℃ ！");
                st.setArguments(bundle);
                st.show(fm, "Dialog_err");
                return;
            }
        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }
}
