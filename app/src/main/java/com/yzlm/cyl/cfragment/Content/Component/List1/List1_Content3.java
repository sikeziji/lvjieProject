package com.yzlm.cyl.cfragment.Content.Component.List1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Adapter.FlowCfgAdapter;
import com.yzlm.cyl.cfragment.Adapter.FlowInfo;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_CfgFlowSetting;
import com.yzlm.cyl.cfragment.Dialog.DialogMsg;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pfxyxx.showOKDialogMsg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setComponentEditIntData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.flowReceiveFlag;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.mListDevice;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.versionID;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.Thread.sleep;


/*
 * Created by zwj on 2019/09/11.
 */

public class List1_Content3 extends SubFragment {
    private static List1_Content3 fragment = null;
    private Callbacks mCallbacks;

    private EditText mEdFlowNum;
    private Button mBtnActionList;
    private Button mBtnSampleCfg;

    private EditText mEdActionPar1;
    private EditText mEdActionPar2;

    private Button mBtnActionLowHigh;
    private EditText mEdActionDoTimes;

    private Button mBtnFlowAdd;
    private Button mBtnFlowDel;

    private LinearLayout mLayoutSampleActionCfg;
    private LinearLayout mLayoutOtherActionCfg;
    private static LinearLayout mLayoutCfgFlowSetting;
    private static LinearLayout mLayoutCfgFlowComm;

    private Button mBtnCfgFlowSave;
    private Button mBtnCfgFlowRead;
    private Button mBtnClearDisplay;

    public static ToggleButton mBtnCfgFlowStart;
    private static ListView mlvLocalityCfgFlow;
    private static CustomAdapter lvAdapter;


    boolean blCfgSelect = true;    // false :其它参数，true：进样参数
    boolean blCfgLowHigh = false;// false :low  true:high

    String sTempActionName;
    private static DialogMsg flowDialogMsg = null;

    public static List1_Content3 newInstance() {
        if (fragment == null) {
            fragment = new List1_Content3();
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
        return R.layout.list1_content3;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.str557View = "List1_Content3";

            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p0);
            mBtnReturn.setOnClickListener(new BtnOnClickListener());
            if (QueryMeasCateg(mCompName).equals("5") || QueryMeasCateg(mCompName).equals("6")) {
                mBtnReturn.setVisibility(View.INVISIBLE);
            }
            mEdFlowNum = v.findViewById(R.id.edFlowNum);
            mEdFlowNum.setOnEditorActionListener(new EditFocusChange());
            mEdFlowNum.setOnFocusChangeListener(new EditFocusChange());

            mBtnActionList = v.findViewById(R.id.BtnActionList);
            mBtnActionList.setOnClickListener(new BtnOnClickListener());

            mBtnSampleCfg = v.findViewById(R.id.BtnSampleCfg);
            mBtnSampleCfg.setOnClickListener(new BtnOnClickListener());

            mEdActionPar1 = v.findViewById(R.id.edActionPar1);
            mEdActionPar1.setOnEditorActionListener(new EditFocusChange());
            mEdActionPar1.setOnFocusChangeListener(new EditFocusChange());

            mEdActionPar2 = v.findViewById(R.id.edActionPar2);
            mEdActionPar2.setOnEditorActionListener(new EditFocusChange());
            mEdActionPar2.setOnFocusChangeListener(new EditFocusChange());

            mBtnActionLowHigh = v.findViewById(R.id.BtnActionLowHigh);
            mBtnActionLowHigh.setOnClickListener(new BtnOnClickListener());

            mEdActionDoTimes = v.findViewById(R.id.edActionDoTimes);
            mEdActionDoTimes.setOnEditorActionListener(new EditFocusChange());
            mEdActionDoTimes.setOnFocusChangeListener(new EditFocusChange());

            mBtnFlowAdd = v.findViewById(R.id.BtnFlowAdd);
            mBtnFlowAdd.setOnClickListener(new BtnOnClickListener());
            mBtnFlowDel = v.findViewById(R.id.BtnFlowDel);
            mBtnFlowDel.setOnClickListener(new BtnOnClickListener());

            mLayoutSampleActionCfg = v.findViewById(R.id.layout_sampleActionCfgPar);
            mLayoutOtherActionCfg = v.findViewById(R.id.layout_otherActionCfgPar);

            mBtnCfgFlowSave = v.findViewById(R.id.btnCfgFlowSave);
            mBtnCfgFlowSave.setOnClickListener(new BtnOnClickListener());

            mBtnCfgFlowRead = v.findViewById(R.id.btnCfgFlowRead);
            mBtnCfgFlowRead.setOnClickListener(new BtnOnClickListener());

            mBtnClearDisplay = v.findViewById(R.id.btnClearDisplay);
            mBtnClearDisplay.setOnClickListener(new BtnOnClickListener());

            mBtnCfgFlowStart = v.findViewById(R.id.BtnCfgFlowStart);
            mBtnCfgFlowStart.setOnCheckedChangeListener(new togBtnClickListener());

            mLayoutSampleActionCfg = v.findViewById(R.id.layout_sampleActionCfgPar);
            mLayoutOtherActionCfg = v.findViewById(R.id.layout_otherActionCfgPar);

            mlvLocalityCfgFlow = v.findViewById(R.id.listCfgFlowLocal);
            mLayoutCfgFlowSetting = v.findViewById(R.id.layoutCfgFlowSetting);
            mLayoutCfgFlowComm = v.findViewById(R.id.layoutCfgFlowComm);
            mLayoutCfgFlowComm.setVisibility(View.GONE);
            mLayoutCfgFlowSetting.setVisibility(View.GONE);
            showActionCfgWin();
            mListDevice.clear();

            lvAdapter = new CustomAdapter(getContext(), mListDevice);
            mlvLocalityCfgFlow.setAdapter(lvAdapter);

            mEdFlowNum.setText(String.valueOf(getCfgFlowLvData()));

            // 读取组分版本兼容号31415926
            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 557);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(mCompName + "_配置流程校验信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            byte[] arrayOfByte1 = DataUtil.shortToByte((short) 5703);
            DataUtil.reverse(arrayOfByte1);
            SendManager.SendCmd(mCompName + "_配置流程开关" + "_3_" + DataUtil.bytesToHexString(arrayOfByte1, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            Message msg = new Message();
            msg.what = 557;
            cfgFlowHandler.sendMessage(msg);
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private class EditFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edFlowNum:
                        setComponentEditIntData(mCompName, (EditText) v, 1, 0, 61, 1, "步骤编号");
                        break;
                    case R.id.edActionPar1:
                        break;
                    case R.id.edActionPar2:
                        break;
                    case R.id.edActionDoTimes:
                        break;
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


    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.BtnActionList:
                    main.removeDestopText(mfb);
                    Dialog_CfgFlowSetting dialog_select_flow = new Dialog_CfgFlowSetting();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-select_Action", "");
                    dialog_select_flow.setArguments(bundle);
                    dialog_select_flow.setTargetFragment(List1_Content3.this, 1);
                    dialog_select_flow.show(fm, "select_select_Action");
                    break;
                case R.id.BtnSampleCfg:
                    blCfgSelect = !blCfgSelect;
                    showActionCfgWin();
                    break;
                case R.id.BtnActionLowHigh:
                    blCfgLowHigh = !blCfgLowHigh;
                    ((Button) v).setText(blCfgLowHigh ? context.getString(R.string.high) : context.getString(R.string.low));
                    break;
                case R.id.BtnFlowDel:
                    lvAdapter.UpdateList(removeCfgFlowLvData((short) (Short.parseShort(mEdFlowNum.getText().toString()) - 1)));
                    break;
                case R.id.BtnFlowAdd:
                    ActionTable at = Global.getActions(mCompName);
                    FlowInfo nowAction = new FlowInfo();
                    nowAction.setActionName(sTempActionName);
                    nowAction.setCodeing(at.getActionCmd(nowAction.getActionName()));
                    nowAction.setTimes(Integer.parseInt(blCfgSelect ? (mEdActionDoTimes.getText().toString().equals("") ? "1" : mEdActionDoTimes.getText().toString()) : mEdActionPar1.getText().toString().equals("") ? "1" : mEdActionPar1.getText().toString()));
                    nowAction.setUpDown(Integer.parseInt(blCfgSelect ? (blCfgLowHigh ? "1" : "0") : (mEdActionPar2.getText().toString().equals("") ? "1" : mEdActionPar2.getText().toString())));
                    List<FlowInfo> lf = Short.parseShort(mEdFlowNum.getText().toString()) < getCfgFlowLvData() ? updateCfgFlowLvData(Short.parseShort(mEdFlowNum.getText().toString()), nowAction) : addCfgFlowLvData(nowAction);
                    if (lf == null) {
                        Toast.makeText(context, context.getResources().getString(R.string.FlowingNumberSupermax), Toast.LENGTH_LONG).show();
                        return;
                    }
                    lvAdapter.UpdateList(lf);
                    mEdFlowNum.setText(String.valueOf(getCfgFlowLvData()));
                    break;
                case R.id.btnCfgFlowSave:
                    if (!doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                        Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (flowDialogMsg == null && mListDevice.size() > 0) {
                        mListDevice = checkCfgFlow(mListDevice);
                        lvAdapter.UpdateList(mListDevice);

                        main.removeDestopText(mfb);

                        flowDialogMsg = new DialogMsg();

                        Bundle bundle1 = new Bundle();
                        bundle1.putString("alert-msg", context.getResources().getString(R.string.sendFlowing));
                        flowDialogMsg.setArguments(bundle1);
                        flowDialogMsg.setTargetFragment(List1_Content3.this, 4);
                        flowDialogMsg.show(fm, "Dialog_msg");

                        Message fMsg1 = new Message();
                        fMsg1.what = 3;
                        cfgFlowHandler.sendMessage(fMsg1);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.flowEmpty), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnCfgFlowRead:
                    Message fMsg = new Message();
                    fMsg.what = 1;
                    cfgFlowHandler.sendMessage(fMsg);
                    break;
                case R.id.btnClearDisplay:
                    lvAdapter.UpdateList(clearCfgFlowLvData());
                    break;
                case R.id.btnReturn_p0:
                    mCallbacks.onListSelected(v);
                    break;
            }
        }
    }

    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.BtnCfgFlowStart: {
                    if (versionID < 2) {
                        buttonView.setChecked(false);
                        showOKDialogMsg(context.getResources().getString(R.string.FuncFailMsg));
                        return;
                    } else {
                        if (isChecked != getConfigData(mCompName, "CFG_Flow_Switch").equals("open")) {

                            if (!doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (versionID > 1) {
                                saveOperationLogDataModifyMsg(mCompName, "CFG_Flow_Switch", (isChecked ? "open" : "close"), "启动配置流程", ErrorLog.msgType.操作_信息);
                                updateConfigData(mCompName, "CFG_Flow_Switch", isChecked ? "open" : "close");
                                if(isChecked){
                                    //配置流程开启
                                    AddError(mCompName, 600, ErrorLog.msgType.运维_信息);
                                }else{
                                    //配置流程关闭
                                    AddError(mCompName, 601, ErrorLog.msgType.运维_信息);
                                }
                                mLayoutCfgFlowSetting.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                                mLayoutCfgFlowComm.setVisibility(View.VISIBLE);

                                byte[] cybszByte = copybyte(toByteArray(Integer.parseInt(isChecked ? "1" : "0"), 4));
                                SendManager.SendCmd(mCompName + "_" + "设置配置流程开关_06_221", S0, 3, 100, cybszByte);
                            }
                        }
                    }
                }
                break;
            }
        }
    }


    private void showActionCfgWin() {
        if (blCfgSelect) {
            mLayoutSampleActionCfg.setVisibility(View.VISIBLE);
            mLayoutOtherActionCfg.setVisibility(View.GONE);
        } else {
            mLayoutSampleActionCfg.setVisibility(View.GONE);
            mLayoutOtherActionCfg.setVisibility(View.VISIBLE);
        }
        mBtnSampleCfg.setText(blCfgSelect ? context.getString(R.string.sampleCfg) : context.getString(R.string.otherCfg));
    }


    /**
     * 增加配置动作
     *
     * @param cfgAction 增加配置动作
     * @return
     */
    private List<FlowInfo> addCfgFlowLvData(FlowInfo cfgAction) {

        if (mListDevice != null) {
            if (mListDevice.size() > 0) {
                if (mListDevice.size() > 61) {
                    return null;
                }
                if (mListDevice.get(mListDevice.size() - 1).getCodeing() == 65535) {
                    return mListDevice;
                }
            }
            cfgAction.setActionName((mListDevice.size() + 1) + "_" + cfgAction.getActionName());
            mListDevice.add(cfgAction);
        }
        return mListDevice;
    }

    /**
     * 移除配置流程中每一个动作
     *
     * @param index 移除位置
     * @return
     */
    private List<FlowInfo> removeCfgFlowLvData(short index) {
        if (mListDevice != null) {
            if (index < mListDevice.size()) {
                mListDevice.remove(index);
            }
            updateCfgFlowLvData();
        }
        return mListDevice;
    }


    /**
     * @return 当前配置流程数
     */
    private short getCfgFlowLvData() {
        if (mListDevice != null) {
            return (short) (mListDevice.size() + 1);
        } else {
            return 1;
        }
    }


    /**
     * 清除配置流程数据
     *
     * @return
     */
    private List<FlowInfo> clearCfgFlowLvData() {

        if (mListDevice != null) {
            mListDevice.clear();
        }
        return mListDevice;
    }

    /**
     * 修改配置流程中某步骤内容
     *
     * @param flowNum   步骤，起始1
     * @param cfgAction 动作内容
     * @return 数据列表
     */
    private List<FlowInfo> updateCfgFlowLvData(short flowNum, FlowInfo cfgAction) {
        if (mListDevice != null) {
            if ((flowNum - 1) < mListDevice.size()) {
                cfgAction.setActionName((flowNum) + "_" + cfgAction.getActionName());
                mListDevice.set((flowNum - 1), cfgAction);
            }
        }
        return mListDevice;
    }


    /**
     * 更新整个配置流程LIST
     *
     * @return
     */
    private List<FlowInfo> updateCfgFlowLvData() {
        if (mListDevice != null) {
            List<FlowInfo> mListTemp = new ArrayList<>(mListDevice);
            mListDevice.clear();
            for (int i = 0; i < mListTemp.size(); i++) {
                FlowInfo cfgAction = mListTemp.get(i);
                cfgAction.setActionName((i + 1) + "_" + cfgAction.getActionName().split("_")[1]);
                mListDevice.add(i, cfgAction);
            }
        }
        return mListDevice;
    }


    private static class CustomAdapter extends FlowCfgAdapter {
        CustomAdapter(Context context, List<FlowInfo> items) {
            super(context, items);
        }
    }

    /**
     * @param mListLocality 数据源
     * @param flowId        流程编号
     * @return 是否发送成功
     */
    private static boolean sendFlowToCKB(List<FlowInfo> mListLocality, int flowId) {
        if (mListLocality != null) {

            if (mListLocality.size() > 0) {
                byte[] flows = new byte[263];
                int getNum = 0;
                FlowInfo flowInfo;

                flows[0] = 5;// 日志编号

                flows[1] = 0;//
                flows[2] = 1;// 日志数量

                /*日志序号*/
                flows[3] = (byte) ((flowId) & 0x00ff);
                flows[4] = (byte) ((flowId) & 0xff >> 8);

                /*日志长度*/
                flows[5] = 0x00;
                flows[6] = 0x01;

                for (int i = 0; i < mListLocality.size() * 4; i += 4) {

                    flowInfo = mListLocality.get(getNum++);

                    flows[7 + i] = (byte) ((flowInfo.getCodeing() & 0x00ff));
                    flows[8 + i] = (byte) ((flowInfo.getCodeing() & 0xff00) >> 8);
                    flows[9 + i] = (byte) flowInfo.getTimes();
                    flows[10 + i] = (byte) flowInfo.getUpDown();
                }
                SendManager.SendCmd(mCompName + "_流程设置" + "_26_04", S0, 2, 1000, flows);
                return true;
            }
        }
        return false;
    }

    private List<FlowInfo> checkCfgFlow(List<FlowInfo> mListLocality) {
        //检查是否有准备动作
        if (mListLocality.size() > 0) {
            if (QueryMeasCateg(mCompName).equals("3") || QueryMeasCateg(mCompName).equals("8")) {
                // 滴定检查流程第一步是否是1024动作好
                if (mListLocality.get(0).getCodeing() != 1024) {
                    mListLocality = setActionStep(mListLocality, 0, (short) 1024);
                    updateCfgFlowLvData();
                }
            } else {
                if (mListLocality.get(0).getCodeing() != 0) {
                    mListLocality = setActionStep(mListLocality, 0, (short) 0);
                    updateCfgFlowLvData();
                }
            }
            // 检查是否有结束动作
            if ((mListLocality.get(mListLocality.size() - 1).getCodeing() & 0xffff) != 65535) {
                mListLocality = setActionStep(mListLocality, mListLocality.size(), (short) 65535);
            }
        }
        return mListLocality;

    }

    private List<FlowInfo> setActionStep(List<FlowInfo> mListLocality, int iListIndex, short iActionCode) {
        ActionTable at = Global.getActions(mCompName);
        FlowInfo nowAction = new FlowInfo();
        nowAction.setActionName((iListIndex + 1) + "_" + at.GetActionName((int)iActionCode));
        nowAction.setCodeing(iActionCode);
        nowAction.setTimes(1);
        nowAction.setUpDown(0);

        mListLocality.add(iListIndex, nowAction);
        return mListLocality;
    }

    /**
     * 组分的流程进行CRC16校验和下位机进行核对
     */
    private static short getFlowCrc16Value(List<FlowInfo> mListLocality) {

        byte[] flows = new byte[0];
        {
            if (mListLocality != null) {

                for (int j = 0; j < mListLocality.size(); j++) {
                    FlowInfo step = mListLocality.get(j);
                    byte[] btSteps = new byte[4];
                    //将流程中0xffff作为流程结束动作，不进行校验
                    if (step != null && (step.getCodeing() & 0xffff) == 0xffff) {
                        break;
                    }
                    if (step != null) {
                        btSteps[0] = (byte) ((step.getCodeing() & 0x00ff));
                    }
                    if (step != null) {
                        btSteps[1] = (byte) ((step.getCodeing() & 0xff00) >> 8);
                    }
                    if (step != null) {
                        btSteps[2] = (byte) (step.getTimes() & 0xff);
                    }
                    if (step != null) {
                        btSteps[3] = (byte) (step.getUpDown() & 0xff);
                    }
                    flows = copybyte(flows, btSteps);
                }
            }
        }
        // 将所有流程计算CRC
        byte[] crc = crc16(flows, flows.length);
        return (short) ((short) (((crc[0] & 0x000000FF) << 8) | (0x000000FF & crc[1])) & 0xffff);
    }


    @SuppressLint("HandlerLeak")
    public static Handler cfgFlowHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    SendManager.SendCmd(mCompName + "_流程查询" + "_23_02", S0, 2, 200, "05_" + 1 + "_1");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message fMsg = new Message();
                            fMsg.what = 2;
                            cfgFlowHandler.sendMessage(fMsg);
                        }
                    }).start();
                    break;
                case 2:
                    lvAdapter.UpdateList(mListDevice);
                    break;
                case 3:
                    flowReceiveFlag = false;
                    RealTimeStatusThread.suspend();
                    try {
                        sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendFlowToCKB(mListDevice, 1);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int j = 0; j < 20; j++) {
                                    try {
                                        sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    /*收到数据后*/
                                    if (flowReceiveFlag) {
                                        break;
                                    }
                                }
                                /*收到数据后*/
                                if (flowReceiveFlag) {
                                    short localFlowsCrc = getFlowCrc16Value(mListDevice);
                                    try {
                                        sleep(7000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    byte[] arrayOfByte2 = DataUtil.shortToByte((short) 558);
                                    DataUtil.reverse(arrayOfByte2);
                                    SendManager.SendCmd(mCompName + "_配置流程校验信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 7);
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    // 需要进行校验
                                    short crc = (short) ((Short.parseShort(((getCmds(mCompName).getCmd(558).getValue() == null ? "0" : getCmds(mCompName).getCmd(558).getValue()).toString()))) & 0xffff);
                                    short year = (short) ((Short.parseShort(((getCmds(mCompName).getCmd(559).getValue() == null ? "0" : getCmds(mCompName).getCmd(559).getValue()).toString()))) & 0xffff);
                                    short month = (short) ((Short.parseShort(((getCmds(mCompName).getCmd(560).getValue() == null ? "0" : getCmds(mCompName).getCmd(560).getValue()).toString()))) & 0xffff);
                                    short day = (short) ((Short.parseShort(((getCmds(mCompName).getCmd(561).getValue() == null ? "0" : getCmds(mCompName).getCmd(561).getValue()).toString()))) & 0xffff);
                                    short hour = (short) ((Short.parseShort(((getCmds(mCompName).getCmd(562).getValue() == null ? "0" : getCmds(mCompName).getCmd(562).getValue()).toString()))) & 0xffff);
                                    short min = (short) ((Short.parseShort(((getCmds(mCompName).getCmd(563).getValue() == null ? "0" : getCmds(mCompName).getCmd(563).getValue()).toString()))) & 0xffff);
                                    short sec = (short) ((Short.parseShort(((getCmds(mCompName).getCmd(564).getValue() == null ? "0" : getCmds(mCompName).getCmd(564).getValue()).toString()))) & 0xffff);

                                    Log.i("localFlowsCrc", String.valueOf(localFlowsCrc));
                                    Log.i("crc", String.valueOf(crc));
                                    Log.i("crctime", year + String.valueOf(month) + day + hour + min + sec);

                                    if (crc == localFlowsCrc) {
                                        // 校验成功
                                        Message fMsg = new Message();
                                        fMsg.what = 4;
                                        cfgFlowHandler.sendMessage(fMsg);
                                    } else {
                                        // 校验失败
                                        Message fMsg = new Message();
                                        fMsg.what = 6;
                                        cfgFlowHandler.sendMessage(fMsg);
                                    }

                                } else {
                                    Message fMsg = new Message();
                                    fMsg.what = 5;
                                    cfgFlowHandler.sendMessage(fMsg);
                                }

                            } catch (Exception e) {

                            } finally {
                                if (flowDialogMsg != null) {
                                    flowDialogMsg.dismiss();
                                    flowDialogMsg = null;
                                }
                                RealTimeStatusThread.resume();
                            }
                        }
                    }).start();
                    break;
                case 4:
                    // 下发完成，校验通过
                    showOKDialogMsg(context.getResources().getString(R.string.sendFlowSucceed));
                    saveOperationLogMsg(mCompName, "配置流程下发已完成", ErrorLog.msgType.操作_信息);
                    break;
                case 5: // 通讯异常
                    showOKDialogMsg(context.getResources().getString(R.string.sendFlowError));
                    saveOperationLogMsg(mCompName, "配置流程下发异常", ErrorLog.msgType.操作_信息);
                    break;
                case 6: // 下发校验未通过
                    showOKDialogMsg(context.getResources().getString(R.string.sendFlowCrcFail));
                    saveOperationLogMsg(mCompName, "配置流程下发验证失败", ErrorLog.msgType.操作_信息);
                    break;
                /////////////
                case 557:
                    try {
                        if (mLayoutCfgFlowComm != null && mLayoutCfgFlowSetting != null) {
                            if (versionID > 1) {
                                mLayoutCfgFlowComm.setVisibility(View.VISIBLE);
                                mLayoutCfgFlowSetting.setVisibility(getConfigData(mCompName, "CFG_Flow_Switch").equals("open") ? View.GONE : View.VISIBLE);
                            } else {
                                mLayoutCfgFlowComm.setVisibility(View.GONE);
                                mLayoutCfgFlowSetting.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception e) {

                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("select_Action");
            sTempActionName = stringExtra;

            Toast.makeText(context, stringExtra, Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2) {

        }
        mCallbacks.onDialogRS();
    }

}
