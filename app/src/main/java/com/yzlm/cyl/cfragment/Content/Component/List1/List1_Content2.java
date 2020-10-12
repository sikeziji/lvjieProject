package com.yzlm.cyl.cfragment.Content.Component.List1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Adapter.CombinationFlowAdapter;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.CmdTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_ok;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List1.List1_Content1.isFlowCfg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.allAlarmInfo;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getFlows;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;


/*
 * Created by caoyiliang on 2016/10/28.
 */

public class List1_Content2 extends SubFragment {
    private static List1_Content2 fragment = null;
    private Callbacks mCallbacks;

    private Spinner spFlowName;
    private Button mBtnFlowStart;
    private ToggleButton mtBtnCombination;
    private EditText mEdSumNum;
    private String[] flowNames;
    private ArrayList<CombinationFlowAdapter> arrayList = new ArrayList<>();

    private LinearLayout mLayoutCombinatorialTestStart;

    public static List1_Content2 newInstance() {
        if (fragment == null) {
            fragment = new List1_Content2();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

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
        mCallbacks = null;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list1_content2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    /*
    获取组合测试中的流程  ，标记为手动启动流程
    * */
    public static String getFlowName(String compName, int index) {

        String flowName = "";
        if (!getConfigData(compName, "zhcsFlow").equals("")) {
            String[] strFlow = getConfigData(compName, "zhcsFlow").split(";");

            flowName = strFlow[index].split(",")[0];
        } else {
            return flowName;
        }

        //判定流程是否是标记为手动流程名称，启动日志需要记录（手动/自动）
        if (flowName.contains(context.getString(R.string.manual))) {
            return flowName;
        } else {
            flowName = context.getString(R.string.manual) + flowName;
        }
        return flowName;
    }

    private class EditFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!editTextInputStatus((EditText) v, 0, 101)) {
                    Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + "(0 ~ 101 ) !", Toast.LENGTH_SHORT).show();
                    ((EditText) v).setText("1");
                }
                closeInputMethod(v);
                FullWindows(mActivityWindow);
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return true;
            }
            return false;
        }
    }


    /*
    界面监听
    用于删除某流程
    * */
    private class ViewOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            String[] str = getConfigData(mCompName, "zhcsFlow").split(";");
            StringBuilder flowStr = new StringBuilder();
            for (int i = 0; i < str.length; i++) {
                if (i == v.getId()) {
                    continue;
                }
                flowStr.append(str[i]).append(";");
            }
            updateConfigData(mCompName, "zhcsFlow", flowStr.toString());
            updateConfigData(mCompName, "zhcsAllFlowSum", String.valueOf(flowStr.toString().split(";").length));

            getFlowView();

            return true;
        }
    }

    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.BtnFlowAdd:
                    Global.LockDisplayShowFlag = true;
                    if (!getConfigData(mCompName, "zhcsSwitch").equals("true")) {
                        if (!mEdSumNum.getText().toString().equals("")) {
                            int counter = 0;
                            if (mEdSumNum.getText().toString().equals("0"))
                                break;
                            String strValue = editDataFormat(mEdSumNum.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            mEdSumNum.setText(strValue);

                            if (getFlowCounter(mCompName, 0).equals("")) {
                                updateConfigData(mCompName, "zhcsAllFlowSum", String.valueOf(Integer.valueOf(0)));
                            }
                            for (int i = 0; i < Integer.valueOf(getConfigData(mCompName, "zhcsAllFlowSum")); i++) {
                                counter += Integer.parseInt(getFlowCounter(mCompName, i));
                            }
                            /*统计所有流程的测量次数*/
                            int allFlowCounter = Integer.parseInt(mEdSumNum.getText().toString()) + counter;
                            if (allFlowCounter > 100) {
                                Toast.makeText(context, getString(R.string.flows_high_limit_info) + "!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int zhcsFlowCount = Integer.valueOf(getConfigData(mCompName, "zhcsAllFlowSum")) + 1;
                            updateConfigData(mCompName, "zhcsAllFlowSum", String.valueOf(Integer.valueOf(zhcsFlowCount)));
                            String flowName = spFlowName.getSelectedItem().toString();
                            String flowCount = mEdSumNum.getText().toString();

                            String flowMsg = flowName + "," + flowCount + ";";
                            updateConfigData(mCompName, "zhcsFlow", getConfigData(mCompName, "zhcsFlow") + flowMsg);
                            getFlowView();
                        }
                    }
                    break;
                case R.id.BtnFlowStart:
                    main.removeDestopText(mfb);
                    Dialog_ok st = new Dialog_ok();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", getFlowName(mCompName, 0));
                    st.setArguments(bundle);
                    st.setTargetFragment(List1_Content2.this, 1);
                    st.show(fm, "alert-ok");
                    break;
                case R.id.btnReturn_p0:
                    Global.LockDisplayShowFlag = false;
                    Global.LockModuleDisplayShowFlag = false;
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        if (getConfigData(mCompName, "zhcsStartFlag").equals("false")) {
                            saveOperationLogPublicDataModifyMsg(mCompName, "zhcsSwitch", "false", "组合测试开关", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "zhcsSwitch", "false");
                            saveOperationLogMsg(mCompName, "未启动组合测试退出该界面", ErrorLog.msgType.操作_信息);
                            Toast.makeText(context, getString(R.string.combination_test_does_not_start), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (getConfigData(mCompName, "zhcsStartFlag").equals("false") && getConfigData(mCompName, "zhcsSwitch").equals("true")) {
                            saveOperationLogMsg(mCompName, getString(R.string.modifying_combined_tests_during_measurement), ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "zhcsStartFlag", "true");

                            Toast.makeText(context, getString(R.string.modifying_combined_tests_during_measurement), Toast.LENGTH_SHORT).show();
                        }
                    }
                    mCallbacks.onListSelected(v);
                    break;
            }
        }
    }

    @Override
    protected void DoThings() {
        try {

            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p0);
            mBtnReturn.setOnClickListener(new BtnOnClickListener());
            spFlowName = v.findViewById(R.id.spCombinatorial);

            String strRange = getConfigData(mCompName, "RANGE");

            String[] strFlow = new String[]{context.getString(R.string.SDZY), context.getString(R.string.LYCL),
                    context.getString(R.string.GLCX), context.getString(R.string.SDJZ), context.getResources().getString(R.string.B1), context.getResources().getString(R.string.B2),
                    context.getString(R.string.BYCL),
                    context.getString(R.string.YBQX), context.getString(R.string.CSZY), context.getString(R.string.BY2CL),
                    context.getString(R.string.XSJZ), context.getString(R.string.LDHC), context.getString(R.string.KDHC),
                    context.getString(R.string.BYHC), context.getString(R.string.PXY), context.getString(R.string.JBHS),
                    context.getString(R.string.GYWDX), context.getString(R.string.JYWDX)};
            //Toc才有CHJZS
            if (QueryMeasCateg(mCompName).equals("11")) {
                strFlow = new String[]{context.getString(R.string.SDZY), context.getString(R.string.LYCL),
                        context.getString(R.string.GLCX), context.getString(R.string.SDJZ), context.getResources().getString(R.string.B1), context.getResources().getString(R.string.B2),
                        context.getString(R.string.BYCL),
                        context.getString(R.string.YBQX), context.getString(R.string.CSZY), context.getString(R.string.BY2CL),
                        context.getString(R.string.XSJZ), context.getString(R.string.LDHC), context.getString(R.string.KDHC),
                        context.getString(R.string.BYHC), context.getString(R.string.PXY), context.getString(R.string.JBHS),
                        context.getString(R.string.GYWDX), context.getString(R.string.JYWDX), context.getString(R.string.CHJZS)};
            }
            FlowTable ft = getFlows(mCompName);
            if (ft == null)
                return;
            List<String> lsFlow = new ArrayList<>();
            int j = 0;
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
                    lsFlow.add(j++, aStrFlow);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
            flowNames = new String[lsFlow.size()];
            for (int i = 0; i < lsFlow.size(); i++) {
                flowNames[i] = lsFlow.get(i);
            }
            if (!(flowNames.length > 0)) {
                flowNames = getResources().getStringArray(R.array.strZHCS);
            }

            InitSpinner(context, spFlowName, flowNames, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);


            Button mBtnFlowAdd = v.findViewById(R.id.BtnFlowAdd);
            mBtnFlowAdd.setOnClickListener(new BtnOnClickListener());
            mEdSumNum = v.findViewById(R.id.edFlowSumNum);

            mEdSumNum.setOnFocusChangeListener(new EditFocusChange());
            mEdSumNum.setOnEditorActionListener(new EditFocusChange());

            mBtnFlowStart = v.findViewById(R.id.BtnFlowStart);
            mBtnFlowStart.setOnClickListener(new BtnOnClickListener());

            mtBtnCombination = v.findViewById(R.id.togBtnCombination);
            mtBtnCombination.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());

            mtBtnCombination.setChecked(getConfigData(mCompName, "zhcsSwitch").equals("true") && (!getConfigData(mCompName, "zhcsFlow").equals("")));

            getFlowView();

            mLayoutCombinatorialTestStart = v.findViewById(R.id.layoutCombinatorialTestStart);
            if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                mLayoutCombinatorialTestStart.setVisibility(View.VISIBLE);
            } else {
                mLayoutCombinatorialTestStart.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }


    // 组合测试界面
    private void getFlowView() {

        LinearLayout gridLayout = v.findViewById(R.id.gl_flow);
        gridLayout.removeAllViews();
        if (getConfigData(mCompName, "zhcsFlow").equals("")) {
            mtBtnCombination.setChecked(false);
            updateConfigData(mCompName, "zhcsSwitch", mtBtnCombination.isChecked() ? "true" : "false");
            updateConfigData(mCompName, "zhcsStartFlag", "false");
            mBtnFlowStart.setVisibility(View.INVISIBLE);
            Global.LockDisplayShowFlag = false;
            Global.LockModuleDisplayShowFlag = false;
            return;
        }
        String[] str = getConfigData(mCompName, "zhcsFlow").split(";");
        if (str.length > 0) {
            updateConfigData(mCompName, "zhcsAllFlowSum", String.valueOf(Integer.valueOf(str.length)));

            arrayList.clear();
            for (int i = 0; i < str.length; i++) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.horizontalsv_zhcs, null);
                TextView textViewFlow = linearLayout.findViewById(R.id.tvFlow);
                TextView textViewFlowNum = linearLayout.findViewById(R.id.tvNum);
                String[] str2 = str[i].split(",");
                textViewFlow.setText(str2[0]);
                textViewFlowNum.setText(str2[1] + " " + context.getString(R.string.times));
                linearLayout.setId(i);
                gridLayout.addView(linearLayout);
                arrayList.add(i, new CombinationFlowAdapter(str2[0], str2[1]));
                linearLayout.setOnLongClickListener(new ViewOnLongClickListener());
            }
        } else {
            updateConfigData(mCompName, "zhcsAllFlowSum", String.valueOf(Integer.valueOf(0)));
            updateConfigData(mCompName, "zhcsFlow", "");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Global.LockDisplayShowFlag = false;
            Global.LockModuleDisplayShowFlag = false;
            final String stringExtra = data.getStringExtra("Dialog_Command");
            mCallbacks.onDialogRS();
            workState.put(MainActivity.mCompName, getString(R.string.normal));
            allAlarmInfo.put(mCompName, context.getString(R.string.normal));
            AutoSampleEnable.put(mCompName, "true");

            Log.i("T1", stringExtra);
            final String[] str = stringExtra.split("_");
            Log.i("T2", str[1]);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] str = stringExtra.split("_");
                    if (doFlowing.get(str[0]).equals(context.getString(R.string.waiting_for_instructions))) {
                        //判定流程是否是标记为手动流程名称，启动日志需要记录（手动/自动）
                        if (str[1].contains(context.getString(R.string.manual))) {
                        } else {
                            str[1] = context.getString(R.string.manual) + str[1];
                        }
                        doControlJob(str[0], str[1]);
                    }
                }
            }).start();
            Log.i("T3", str[0]);
            getCmds(str[0]).doSampleMode = CmdTable.DoSampleMode.手动;
            Toast.makeText(context, context.getResources().getString(R.string.readyStarted) + stringExtra + context.getResources().getString(R.string.pleaseWaiting), Toast.LENGTH_SHORT).show();
            DeleteDoingJob();
            getFlowView();
            mCallbacks.showUpContent();

            updateConfigData(mCompName, "zhcsSwitch", "true");
            updateConfigData(mCompName, "zhcsStartFlag", "true");
            AddError(mCompName, 540, ErrorLog.msgType.运维_信息);
            saveOperationLogMsg(mCompName, "启动组合测试", ErrorLog.msgType.操作_信息);

        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }

    private String getFlowCounter(String compName, int index) {
        if (!getConfigData(compName, "zhcsFlow").equals("")) {
            String[] strFlow = getConfigData(compName, "zhcsFlow").split(";");

            return strFlow[index].split(",")[1];
        } else {
            return "";
        }
    }

    /*
    从组合测试流程中删除最前面的一条测量数据，  次数减一，为0则删除该流程指令
    * */
    private void DeleteDoingJob() {

        String[] str = getConfigData(mCompName, "zhcsFlow").split(";");
        int num = Integer.valueOf(str[0].split(",")[1]);
        if (num > 0) {
            num--;
            if (num <= 0) {
                // 清除第一个流程
                String strFlow = "";
                for (int i = 1; i < str.length; i++) {
                    strFlow += str[i] + ";";
                }
                updateConfigData(mCompName, "zhcsFlow", strFlow);
            } else {
                // 更改第一个流程的次数后重新写入
                String strFlow1 = str[0].split(",")[0] + "," + num + ";";
                String strFlow = "";
                for (int i = 1; i < str.length; i++) {
                    strFlow += str[i] + ";";
                }
                updateConfigData(mCompName, "zhcsFlow", strFlow1 + strFlow);
            }
        }
    }

    private class TogBtnOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked != getConfigData(mCompName, "zhcsSwitch").equals("true")) {
                if (getConfigData(mCompName, "zhcsFlow").equals("")) {
                    mtBtnCombination.setChecked(false);
                    Toast.makeText(context, getResources().getText(R.string.process_is_empty) + "!", Toast.LENGTH_SHORT).show();
                } else {
                    Global.LockDisplayShowFlag = true;
                    updateConfigData(mCompName, "zhcsSwitch", mtBtnCombination.isChecked() ? "true" : "false");
                    mBtnFlowStart.setVisibility((getConfigData(mCompName, "zhcsSwitch").equals("true") && (!getConfigData(mCompName, "zhcsFlow").equals(""))) ? View.VISIBLE : View.INVISIBLE);
                    saveRunInfo2File("组分[" + mCompName + "]" + "组合测试开关:" + (mtBtnCombination.isChecked() ? "开启" : "关闭"));
                }
            }
        }
    }

}
