package com.yzlm.cyl.cfragment.Content.Component.List1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.CmdTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_CalibrationSelect;
import com.yzlm.cyl.cfragment.Dialog.Dialog_ok;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.phoneName;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getFlows;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/*
 * Created by caoyiliang on 2016/10/28.
 */

public class List1_Content1 extends SubFragment {
    private static List1_Content1 fragment = null;
    private Callbacks mCallbacks;


    public static List1_Content1 newInstance() {
        if (fragment == null) {
            fragment = new List1_Content1();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogRS();

        void showUpContent();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!phoneName.equals("rk3288")) {
            mCallbacks = null;
        }
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list1_content1;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            String strRange = getConfigData(mCompName, "RANGE");
            String[] strFlow = new String[]{context.getString(R.string.SDZY), context.getString(R.string.LYCL),
                    context.getString(R.string.GLCX), context.getString(R.string.SDJZ), context.getResources().getString(R.string.B1), context.getResources().getString(R.string.B2),
                    context.getString(R.string.BYCL),
                    context.getString(R.string.YBQX), context.getString(R.string.CSZY), context.getString(R.string.BY2CL),
                    context.getString(R.string.XSJZ), context.getString(R.string.LDHC), context.getString(R.string.KDHC),
                    context.getString(R.string.BYHC), context.getString(R.string.PXY), context.getString(R.string.JBHS),
                    context.getString(R.string.GYWDX), context.getString(R.string.JYWDX), context.getString(R.string.CHJZS)};
            FlowTable ft = getFlows(mCompName);
            if (ft == null) return;
            GridLayout mGL = (GridLayout) v.findViewById(R.id.CGGL);
            mGL.removeAllViews();
            for (String aStrFlow : strFlow) {
                try {
                    if (!isFlowCfg(aStrFlow, strRange)) {
                        continue;
                    }
                    if (aStrFlow.equals(context.getResources().getString(R.string.B1))) {
                        if (getConfigData(mCompName, ("Range" + strRange + "_CAL2_A_Value")).equals("")) {
                            continue;
                        }
                    }
                    if (aStrFlow.equals(context.getResources().getString(R.string.B2))) {
                        if (getConfigData(mCompName, ("Range" + strRange + "_CAL1_A_Value")).equals("")) {
                            continue;
                        }
                    }
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    Button mBtn = (Button) inflater.inflate(R.layout.ft_button, null).findViewById(R.id.btn_ft);
                    mGL.addView(mBtn);
                    mBtn.setText(aStrFlow);
                    GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) mBtn.getLayoutParams();
                    layoutParams.setMargins(25, 25, 25, 25);
                    mBtn.setLayoutParams(layoutParams);
                    mBtn.setOnClickListener(new BtnClick());

                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    /*
    判断输入流程名称，流程文件是否配置了流程内容项
    * **/
    public static boolean isFlowCfg(String flowName, String strRange) {
        int stNum = 0;
        boolean bIsShow = false;
        String[] sFlowName = {
                context.getString(R.string.ZY), context.getString(R.string.LYCL), context.getString(R.string.GLCX),
                context.getString(R.string.span_1), context.getString(R.string.span_2), context.getString(R.string.BYCL),
                context.getString(R.string.YBQX), context.getString(R.string.CSZY), context.getString(R.string.BY2CL),
                context.getString(R.string.XSJZ), context.getString(R.string.LDHC), context.getString(R.string.KDHC),
                context.getString(R.string.BYHC), context.getString(R.string.PXY), context.getString(R.string.JBHS),
                context.getString(R.string.GYWDX), context.getString(R.string.JYWDX), context.getString(R.string.CHJZS)
        };
        for (stNum = 0; stNum < sFlowName.length; stNum++) {
            if (flowName.contains(sFlowName[stNum])) {
                break;
            }
            // 特殊情况
            if (flowName.contains(context.getString(R.string.SDJZ))) {
                stNum = 3;
                break;
            }
        }
        FlowTable ft = getFlows(mCompName);
        FlowClass fc;
        switch (stNum) {
            case 0:
            case 1:
            case 5:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                fc = ft.getFlow(sFlowName[stNum] + "_" + strRange);
                if (fc != null && fc.getSteps().size() > 1) {
                    bIsShow = true;
                }
                break;

            case 2:
            case 6:
            case 7:
            case 17:
                fc = ft.getFlow(sFlowName[stNum] + "_" + "0");
                if (fc != null && fc.getSteps().size() > 1) {
                    bIsShow = true;
                }
                break;
            case 3:
            case 4:
                fc = ft.getFlow(sFlowName[stNum] + "_" + strRange);
                if (fc != null && fc.getSteps().size() > 1) {
                    bIsShow = true;
                }
                fc = ft.getFlow(sFlowName[stNum + 1] + "_" + strRange);
                if (fc != null && fc.getSteps().size() > 1) {
                    bIsShow = true;
                }
                break;
            case 9:
                fc = ft.getFlow(sFlowName[stNum] + "_" + strRange);
                if (fc != null && fc.getSteps().size() > 1 && isHaveMeasCategory(mCompName, "5")) {
                    bIsShow = true;
                }
                break;
            case 15:
            case 16:
                fc = ft.getFlow(sFlowName[stNum] + "_" + "1");
                if (fc != null && fc.getSteps().size() > 1) {
                    bIsShow = true;
                }
                break;
            default:
                break;
        }
        return bIsShow;
    }

    private class BtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            workState.put(MainActivity.mCompName, context.getString(R.string.normal));
            AutoSampleEnable.put(mCompName, "true");
            main.removeDestopText(mfb);
            String strBtn = ((Button) v).getText().toString();

            if (strBtn.equals(context.getString(R.string.SDJZ)) && GetPlatRangSum(mCompName) > 1) {
                Dialog_CalibrationSelect st = new Dialog_CalibrationSelect();
                Bundle bundle = new Bundle();

                bundle.putString("alert-ok", strBtn);
                st.setArguments(bundle);
                st.setTargetFragment(List1_Content1.this, 1);
                st.show(fm, "alert-ok");
            } else {
                Dialog_ok st = new Dialog_ok();
                Bundle bundle = new Bundle();

                bundle.putString("alert-ok", strBtn);
                st.setArguments(bundle);
                st.setTargetFragment(List1_Content1.this, 1);
                st.show(fm, "alert-ok");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            final String stringExtra = data.getStringExtra("Dialog_Command");
            mCallbacks.onDialogRS();
            String[] str = stringExtra.split("_");
            getCmds(str[0]).doSampleMode = CmdTable.DoSampleMode.手动;
            Toast.makeText(context, context.getResources().getString(R.string.readyStarted) + stringExtra + context.getResources().getString(R.string.pleaseWaiting), Toast.LENGTH_SHORT).show();
            mCallbacks.showUpContent();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runningFlow(stringExtra);
                }
            }).start();

        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }

    private void runningFlow(String stringExtra) {
        Log.i("T1", stringExtra);
        String[] str = stringExtra.split("_");
        Log.i("T2", str[1]);
        if (doFlowing.get(str[0]).equals(context.getString(R.string.waiting_for_instructions))) {
            if (str[1].contains(context.getString(R.string.manual))) {
            } else {
                str[1] = context.getString(R.string.manual) + str[1];
            }
            doControlJob(str[0], str[1]);
        }
        Log.i("T3", str[0] + "," + str[1]);

        saveOperationLogMsg(mCompName, "启动" + str[1], ErrorLog.msgType.操作_信息);

        //W200
        if (getConfigData(mCompName, "DoorCloseDataCounter").equals("1")) {
            updateConfigData(mCompName, "DoorCloseDataCounter", "0");
        }
        if (!getConfigData(mCompName, "MeaAValueEn").equals("")) {
            updateConfigData(mCompName, "MeaAValueEn", "");
        }
    }
}
