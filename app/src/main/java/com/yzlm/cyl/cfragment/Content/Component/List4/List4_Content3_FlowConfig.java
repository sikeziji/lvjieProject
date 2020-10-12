package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Adapter.FlowCfgAdapter;
import com.yzlm.cyl.cfragment.Adapter.FlowInfo;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.Action;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowStep;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.DialogMsg;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getFlowCrc16Value;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSetFormatTime;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.flowReceiveFlag;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getFlows;
import static com.yzlm.cyl.cfragment.Global.getFlowsKey;
import static com.yzlm.cyl.cfragment.Global.mListDevice;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static java.lang.Thread.sleep;

/**
 * Created by zwj on 2017/7/13.
 */

public class List4_Content3_FlowConfig extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_FlowConfig fragment = null;

    private static TextView mTxtFlowCrcInfo;
    private static TextView mTxtFlowUpdateInfo;
    private ImageButton mBtnReturn;
    private Spinner mspFlow;
    private ListView mlvLocalityFlow;
    private static ListView mlvDeviceFlow;
    private static List<FlowInfo> mListLocality = new ArrayList<>();  //声明一个list，动态存储要显示的信息

    private static CustomAdapter lvLocalityAdapter;
    private static CustomAdapter lvDeviceAdapter;

    private static Context contextFlowCfg;
    private String[] flowNames;

    private DialogMsg flowDialogMsg;
    private static flow ckbFlowInfo;
    private Short localFlowsCrc = 0;

    private static boolean IsSendFlow = false;

    class flow {
        short crc = 0;
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;
        int compatibleVersion = 0;

        void clear() {
            crc = 0;
            year = 0;
            month = 0;
            day = 0;
            hour = 0;
            min = 0;
            sec = 0;
            compatibleVersion = 0;
        }
    }

    public static List4_Content3_FlowConfig newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_FlowConfig();
        }
        return fragment;
    }


    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.flow_cfg_setread;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        lvDeviceAdapter = null;
        lvLocalityAdapter = null;
        mBtnReturn = v.findViewById(R.id.btnReturnFlow);
        getMeasCategoryWin();
        mListDevice.clear();
        contextFlowCfg = getContext();
        ckbFlowInfo = new flow();
        Button mBtnRead = v.findViewById(R.id.btnflowRead);
        Button mBtnSet = v.findViewById(R.id.btnflowSet);
        Button mBtnClean = v.findViewById(R.id.btnflowClean);
        Button mBtnALLSet = v.findViewById(R.id.btnflowAllSet);
        mBtnALLSet.setOnClickListener(new btnSetAllClick());

        mBtnReturn.setOnClickListener(new btnClick());
        mBtnRead.setOnClickListener(new btnReadClick());
        mBtnSet.setOnClickListener(new btnSetClick());
        mBtnClean.setOnClickListener(new btnCleanClick());
        mspFlow = v.findViewById(R.id.spFlow);
        String[] flows = getResources().getStringArray(R.array.flow);

        try {
            for (int i = 0; i < flows.length; i++) {
                FlowTable ft = getFlows(mCompName);
                FlowClass fc;
                String flowName;
                String flowRange = "";
                if (flows[i].split("_").length > 1) {
                    flowName = flows[i].split("_")[0];
                    flowRange = "_" + flows[i].split("_")[1];
                } else {
                    flowName = flows[i].split("_")[0];
                }
                fc = ft.getFlow(flowName + flowRange);
                if (fc == null) {
                    flowNames = new String[i];
                    for (int j = 0; j < i; j++) {
                        flowNames[j] = flows[j];
                    }
                    break;
                } else if (!(fc.getSteps().size() > 1) && i > 41) { //异常处理后的流程动作比需要大于1个动作
                    flows[i] = "保留" + flowRange;
                }
            }
            if (!(flowNames != null && flowNames.length > 0)) {
                flowNames = getResources().getStringArray(R.array.flow);
            }
        } catch (Exception e) {

        }
        InitSpinner(context, mspFlow, flowNames, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mspFlow.setOnItemSelectedListener(new SpClickListener());

        mlvLocalityFlow = v.findViewById(R.id.lvLocalityFlow);
        mlvDeviceFlow = v.findViewById(R.id.lvDeviceFlow);

        setLvInfo(mCompName, mspFlow.getSelectedItem().toString());
        lvLocalityAdapter = new CustomAdapter(getContext(), mListLocality);
        mlvLocalityFlow.setAdapter(lvLocalityAdapter);


        mTxtFlowCrcInfo = v.findViewById(R.id.txtFlowCrcInfo);
        mTxtFlowUpdateInfo = v.findViewById(R.id.txtFlowUpdateInfo);

        // 获取最近一次更新流程信息内容
        byte[] arrayOfByte2 = DataUtil.shortToByte((short) 550);
        DataUtil.reverse(arrayOfByte2);
        SendManager.SendCmd(mCompName + "_流程校验信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 8);
    }

    /**
     * 测量类型 需要处理的界面
     */
    private void getMeasCategoryWin() {
        if (QueryMeasCateg(mCompName).equals("5") || QueryMeasCateg(mCompName).equals("6")) {
            mBtnReturn.setVisibility(View.INVISIBLE);
        } else {
            mBtnReturn.setVisibility(View.VISIBLE);
        }
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

    private class SpClickListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setLvInfo(mCompName, mspFlow.getSelectedItem().toString());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class btnReadClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Message fMsg = new Message();
            fMsg.what = 1;
            showLvHandler.sendMessage(fMsg);
        }
    }

    private class btnSetClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Message fMsg = new Message();
            fMsg.what = 2;
            showLvHandler.sendMessage(fMsg);
        }
    }

    private class btnSetAllClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!IsSendFlow) {
                IsSendFlow = true;
                Message fMsg = new Message();
                fMsg.what = 5;
                showLvHandler.sendMessage(fMsg);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 等待8秒钟
                            sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        IsSendFlow = false;
                    }
                }).start();
            }
        }
    }

    private class btnCleanClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Message fMsg = new Message();
            fMsg.what = 3;
            showLvHandler.sendMessage(fMsg);
        }
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            mCallbacks.onListSelected(v);
            RealTimeStatusThread.suspend();
        }
    }

    private void setLvInfo(String compName, String flow) {
        mListLocality.clear();
        ActionTable at = Global.getActions(compName);
        Map<String, Action> LAction = at.getLAction();
        if (LAction == null) return;

        FlowTable ft = Global.getFlows(compName);
        FlowClass fc;
        fc = ft.getFlow(flow);
        if (fc != null) {
            Map<Integer, FlowStep> stepMap = fc.getSteps();

            for (int i = 0; i < stepMap.size(); i++) {
                FlowInfo flowInfo = new FlowInfo();
                FlowStep step = stepMap.get(i + 1);
                if (step != null) {
                    flowInfo.setActionName((i + 1) + "_" + " " + at.GetActionName((short) step.getActionCoding()));
                    flowInfo.setTimes(step.getSampleCount());
                    flowInfo.setUpDown(step.getMeasurement());
                    flowInfo.setCodeing(step.getActionCoding());
                    mListLocality.add(flowInfo);
                }
            }
        }
        Message fMsg = new Message();
        fMsg.what = 14;
        showLvHandler.sendMessage(fMsg);
    }

    class CustomAdapter extends FlowCfgAdapter {
        CustomAdapter(Context context, List<FlowInfo> items) {
            super(context, items);
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler showLvHandler = new Handler() {

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1: // 读取
                        SendManager.SendCmd(mCompName + "_流程查询" + "_23_02", S0, 1, 200, "02_" + (mspFlow.getSelectedItemPosition() + 1) + "_1");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message fMsg = new Message();
                                fMsg.what = 8;
                                showLvHandler.sendMessage(fMsg);
                            }
                        }).start();
                        break;
                    case 2: // 单独下发
                        sendFlowToCKB(mCompName, mspFlow.getSelectedItemPosition() + 1);
                        flowReceiveFlag = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
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
                                    Message fMsg = new Message();
                                    fMsg.what = 10;
                                    showLvHandler.sendMessage(fMsg);
                                } else {
                                    Message fMsg = new Message();
                                    fMsg.what = 9;
                                    showLvHandler.sendMessage(fMsg);
                                }
                            }
                        }).start();
                        break;
                    case 3: {   //清除流程

                        byte[] flows = new byte[263];
                        flows[0] = 2;// 日志编号

                        flows[1] = 0;//
                        flows[2] = 1;// 日志数量
                        /*日志序号*/
                        flows[3] = (byte) ((mspFlow.getSelectedItemPosition() + 1) & 0x00ff);
                        flows[4] = (byte) ((mspFlow.getSelectedItemPosition() + 1) & 0xff >> 8);
                        /*日志长度*/
                        flows[5] = 0x00;
                        flows[6] = 0x01;
                        SendManager.SendCmd(mCompName + "_流程设置" + "_26_04", S0, 1, 200, flows);
                        saveOperationLogMsg(mCompName, "_流程设置", ErrorLog.msgType.操作_信息);
                    }
                    break;
                    case 5: {   // 全部设置
                        saveOperationLogMsg(mCompName, "一键下发流程", ErrorLog.msgType.操作_信息);
                        main.removeDestopText(mfb);
                        flowDialogMsg = new DialogMsg();

                        Bundle bundle = new Bundle();
                        bundle.putString("alert-msg", context.getResources().getString(R.string.sendFlowing));
                        flowDialogMsg.setArguments(bundle);
                        flowDialogMsg.setTargetFragment(List4_Content3_FlowConfig.this, 4);
                        flowDialogMsg.show(fm, "Dialog_msg");
                        RealTimeStatusThread.suspend();

                        // 对所以流程进行校验
                        String[] flows = getResources().getStringArray(R.array.flow);
                        localFlowsCrc = getFlowCrc16Value(mCompName, flows);
                        mTxtFlowCrcInfo.setText("");
                        mTxtFlowUpdateInfo.setText("");
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int tryTimes = 3;

                                for (int i = 0, j; i < flowNames.length; ) {

                                    setLvInfo(mCompName, flowNames[i]);
                                    flowReceiveFlag = false;
                                    if (sendFlowToCKB(mCompName, i + 1)) {
                                        for (j = 0; j < 3; j++) {
                                            try {
                                                sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            /*收到数据后*/
                                            if (flowReceiveFlag) {
                                                tryTimes = 3;
                                                i++;
                                                break;
                                            }
                                        }
                                        if (j == 3) {
                                            if (tryTimes-- <= 0) {
                                                saveExceptInfo2File("组分[" + mCompName + "] 流程下发通讯异常!");
                                                flowDialogMsg.dismiss();
                                                Message fMsg = new Message();
                                                fMsg.what = 7;
                                                fMsg.obj = i;   // 下发到第几个流程失败
                                                showLvHandler.sendMessage(fMsg);
                                                RealTimeStatusThread.resume();
                                                return;
                                            }
                                        }
                                    } else {
                                        i++;
                                    }
                                }
                                try {
                                    // 等待8秒钟读取校验值
                                    sleep(8000);
                                    // 获取CRC检验值
                                    if (ckbFlowInfo == null) {
                                        ckbFlowInfo = new flow();
                                    }
                                    ckbFlowInfo.clear();
                                    byte[] arrayOfByte2 = DataUtil.shortToByte((short) 550);
                                    DataUtil.reverse(arrayOfByte2);
                                    SendManager.SendCmd(mCompName + "_流程校验信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), S0, 3, 200, 8);
                                    sleep(1000);
                                    Log.i("flowCRC2", String.valueOf(localFlowsCrc));

                                    if (ckbFlowInfo.compatibleVersion > 0) {    // 测试去掉
                                        // 两边校验了，单校验结果不同的情况
                                        if (ckbFlowInfo.crc != 0 && !((ckbFlowInfo.crc & 0xffff) == (localFlowsCrc & 0xffff))) {
                                            flowDialogMsg.dismiss();
                                            Message fMsg = new Message();
                                            fMsg.what = 11;
                                            showLvHandler.sendMessage(fMsg);
                                            RealTimeStatusThread.resume();
                                            return;
                                        }
                                    } else {
                                        saveRunInfo2File("组分[" + mCompName + "] " + "兼容版本号：" + ckbFlowInfo.compatibleVersion + "流程下发验证功能不可用，建议升级核心控制板软件!");
                                        // 完成下发，提示建议升级信息
                                        flowDialogMsg.dismiss();
                                        Message fMsg = new Message();
                                        fMsg.what = 12;
                                        showLvHandler.sendMessage(fMsg);
                                        RealTimeStatusThread.resume();
                                        return;
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                flowDialogMsg.dismiss();
                                Message fMsg = new Message();
                                fMsg.what = 6;
                                showLvHandler.sendMessage(fMsg);
                                RealTimeStatusThread.resume();
                            }
                        }).start();
                    }
                    break;
                    case 6:
                    case 7: // 全部流程下发结果
                        if (mspFlow != null && mCallbacks != null) {

                            setLvInfo(mCompName, mspFlow.getSelectedItem().toString());
                            mCallbacks.onDialogRS();
                            if (ckbFlowInfo.compatibleVersion > 0 && msg.arg1 == 0) {
                                mTxtFlowCrcInfo.setText(context.getText(R.string.locFlowCrc) + ":" + (localFlowsCrc & 0xffff) + "\r\n" + context.getText(R.string.recFlowCrc) + ":" + (ckbFlowInfo.crc & 0xffff));
                            }
                        }
                        if (msg.what == 7) {
                            if (msg.arg1 == 0) {
                                showOKDialogMsg(context.getResources().getString(R.string.sendFlowError));
                            }
                            saveOperationLogMsg(mCompName, "流程下发异常", ErrorLog.msgType.操作_信息);
                        } else {
                            if (msg.arg1 == 0) {
                                showOKDialogMsg(context.getResources().getString(R.string.sendFlowSucceed));
                            }
                            saveOperationLogMsg(mCompName, "流程下发已完成", ErrorLog.msgType.操作_信息);
                        }
                        errSetConfig = false;
                        RealTimeStatusThread.resume();
                        break;
                    case 8:
                        /*显示读取到的流程*/
                        if (lvDeviceAdapter == null) {
                            lvDeviceAdapter = new CustomAdapter(contextFlowCfg, mListDevice);
                            mlvDeviceFlow.setAdapter(lvDeviceAdapter);
                        } else {
                            lvDeviceAdapter.UpdateList(mListDevice);
                        }
                        break;
                    case 9: // 单独下发结果
                    case 10:
                        if (msg.what == 9) {
                            showOKDialogMsg(context.getResources().getString(R.string.sendFlowError));
                            saveOperationLogMsg(mCompName, "流程下发异常", ErrorLog.msgType.操作_信息);
                        } else {
                            showOKDialogMsg(context.getResources().getString(R.string.sendFlowSucceed));
                            saveOperationLogMsg(mCompName, "流程下发已完成", ErrorLog.msgType.操作_信息);
                        }
                        break;
                    case 11:  // 加验证的全部流程下发 失败
                        if (msg.arg1 == 0) {
                            showOKDialogMsg(context.getResources().getString(R.string.sendFlowCrcFail));

                            if (mTxtFlowCrcInfo != null && mTxtFlowUpdateInfo != null) {
                                mTxtFlowCrcInfo.setText(context.getText(R.string.locFlowCrc) + ":" + (localFlowsCrc & 0xffff) + "\r\n" + context.getText(R.string.recFlowCrc) + ":" + (ckbFlowInfo.crc & 0xffff));

                                String strTime = getSetFormatTime(String.valueOf(ckbFlowInfo.year), String.valueOf(ckbFlowInfo.month), String.valueOf(ckbFlowInfo.day), String.valueOf(ckbFlowInfo.hour), String.valueOf(ckbFlowInfo.min), String.valueOf(ckbFlowInfo.sec));
                                mTxtFlowUpdateInfo.setText(strTime.split(" ")[0] + "\r\n" + strTime.split(" ")[1]);
                            }
                        }
                        errSetConfig = false;
                        saveOperationLogMsg(mCompName, "流程下发验证失败", ErrorLog.msgType.操作_信息);
                        break;
                    case 12:
                        // 完成下发，提示建议升级主控板
                        if (msg.arg1 == 0) {
                            showOKDialogMsg(context.getResources().getString(R.string.sendFlowSucceed) + "\r\n" + "\r\n" + context.getString(R.string.flowCrcFuncFail));
                        }
                        errSetConfig = false;
                        saveOperationLogMsg(mCompName, "流程下发已完成", ErrorLog.msgType.操作_信息);
                        break;
                    case 13: {
                        String cname = (String) msg.obj;
                        saveOperationLogMsg(cname, "自动一键下发流程", ErrorLog.msgType.操作_信息);

                        RealTimeStatusThread.suspend();

                        // 对所以流程进行校验
                        String[] flows = context.getResources().getStringArray(R.array.flow);
                        localFlowsCrc = getFlowCrc16Value(cname, flows);
                        if (flowNames == null) {
                            try {
                                for (int i = 0; i < flows.length; i++) {
                                    FlowTable ft = getFlows(cname);
                                    FlowClass fc;
                                    String flowName;
                                    String flowRange = "";
                                    if (flows[i].split("_").length > 1) {
                                        flowName = flows[i].split("_")[0];
                                        flowRange = "_" + flows[i].split("_")[1];
                                    } else {
                                        flowName = flows[i].split("_")[0];
                                    }
                                    fc = ft.getFlow(flowName + flowRange);
                                    if (fc == null) {
                                        flowNames = new String[i];
                                        for (int j = 0; j < i; j++) {
                                            flowNames[j] = flows[j];
                                        }
                                        break;
                                    } else if (!(fc.getSteps().size() > 1) && i > 41) { //异常处理后的流程动作比需要大于1个动作
                                        flows[i] = "保留" + flowRange;
                                    }
                                }
                                if (!(flowNames != null && flowNames.length > 0)) {
                                    flowNames = context.getResources().getStringArray(R.array.flow);
                                }
                            } catch (Exception e) {
                                String sss = "ingo";
                            }
                        }
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SendFlowImplements mi = new SendFlowImplements();
                        mi.setCName(cname);
                        Thread t = new Thread(mi);
                        t.start();
                    }
                    break;
                    case 14:// 刷新本地流程列表显示
                        if (lvLocalityAdapter != null) {
                            lvLocalityAdapter.UpdateList(mListLocality);
                        }
                        break;
                }
            } catch (Exception e) {
                RealTimeStatusThread.resume();
            }
        }
    };

    private void showOKDialogMsg(String string) {
        main.removeDestopText(mfb);
        Dialog_Err st = new Dialog_Err();
        Bundle bundle = new Bundle();
        bundle.putString("alert-err", string);
        st.setArguments(bundle);
        st.show(fm, "Dialog_err");
    }


    private boolean sendFlowToCKB(String cName, int flowId) {
        if (mListLocality != null) {

            if (mListLocality.size() > 0) {
                byte[] flows = new byte[263];
                int getNum = 0;
                FlowInfo flowInfo;

                flows[0] = 2;// 日志编号

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
                SendManager.SendCmd(cName + "_流程设置" + "_26_04", S0, 2, 1000, flows);
                return true;
            }
        }
        return false;
    }


    @SuppressLint("HandlerLeak")
    public static Handler flowConfigHandler = new Handler() {

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 550:
                        String cname = (String) msg.obj;
                        short crc = Short.parseShort(((getCmds(cname).getCmd(msg.what).getValue() == null ? "0" : getCmds(cname).getCmd(msg.what).getValue()).toString()));
                        ckbFlowInfo.crc = (short) (crc & 0xffff);
                        Log.i("flowCRC", String.valueOf(ckbFlowInfo.crc));
                        ckbFlowInfo.year = Short.parseShort(((getCmds(cname).getCmd(msg.what + 1).getValue() == null ? "0" : getCmds(cname).getCmd(msg.what + 1).getValue()).toString()));
                        ckbFlowInfo.month = Short.parseShort(((getCmds(cname).getCmd(msg.what + 2).getValue() == null ? "0" : getCmds(cname).getCmd(msg.what + 2).getValue()).toString()));
                        ckbFlowInfo.day = Short.parseShort(((getCmds(cname).getCmd(msg.what + 3).getValue() == null ? "0" : getCmds(cname).getCmd(msg.what + 3).getValue()).toString()));
                        ckbFlowInfo.hour = Short.parseShort(((getCmds(cname).getCmd(msg.what + 4).getValue() == null ? "0" : getCmds(cname).getCmd(msg.what + 4).getValue()).toString()));
                        ckbFlowInfo.min = Short.parseShort(((getCmds(cname).getCmd(msg.what + 5).getValue() == null ? "0" : getCmds(cname).getCmd(msg.what + 5).getValue()).toString()));
                        ckbFlowInfo.sec = Short.parseShort(((getCmds(cname).getCmd(msg.what + 6).getValue() == null ? "0" : getCmds(cname).getCmd(msg.what + 6).getValue()).toString()));
                        ckbFlowInfo.compatibleVersion = Integer.parseInt(((getCmds(cname).getCmd(msg.what + 7).getValue() == null ? "0" : getCmds(cname).getCmd(msg.what + 7).getValue()).toString()));
                        if (ckbFlowInfo.compatibleVersion > 0 && ckbFlowInfo.year > 2000 && ckbFlowInfo.year < 3000 && mTxtFlowUpdateInfo != null) {
                            String strTime = getSetFormatTime(String.valueOf(ckbFlowInfo.year), String.valueOf(ckbFlowInfo.month), String.valueOf(ckbFlowInfo.day), String.valueOf(ckbFlowInfo.hour), String.valueOf(ckbFlowInfo.min), String.valueOf(ckbFlowInfo.sec));
                            mTxtFlowUpdateInfo.setText(strTime.split(" ")[0] + "\r\n" + strTime.split(" ")[1]);
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //自动下发流路标志位
    static boolean errSetConfig = false;

    //自动下发流路
    public void ErrSetAllClick(String name) {
        final String compName = name;
        if (errSetConfig) {
            return;
        }
        errSetConfig = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                FlowTable ft = Global.getFlows(compName);
                if (ft == null) {
                    errSetConfig = false;
                    return;
                }
                ActionTable at = Global.getActions(compName);
                if (at == null) {
                    errSetConfig = false;
                    return;
                }
                while (true) {
                    // 当前状态
                    String status = ft.GetStatus(getCmds(compName).getCmd(51).getValue());
                    if (doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions)) && !status.equals(context.getString(R.string.selfCheck))) {
                        Message fMsg = new Message();
                        fMsg.what = 13;
                        fMsg.obj = compName;
                        showLvHandler.sendMessage(fMsg);
                        break;
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    class SendFlowImplements implements Runnable {
        String cName;

        public void setCName(String name) {
            this.cName = name;
        }

        //2):在A类中覆盖Runnable接口中的run方法.
        public void run() {
            //3):在run方法中编写需要执行的操作
            int tryTimes = 3;
            if (flowNames == null) {
                errSetConfig = false;
                return;
            }
            for (int i = 0, j; i < flowNames.length; ) {

                setLvInfo(cName, flowNames[i]);
                flowReceiveFlag = false;
                if (sendFlowToCKB(cName, i + 1)) {
                    for (j = 0; j < 3; j++) {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /*收到数据后*/
                        if (flowReceiveFlag) {
                            tryTimes = 3;
                            i++;
                            break;
                        }
                    }
                    if (j == 3) {
                        if (tryTimes-- <= 0) {
                            saveExceptInfo2File("组分[" + cName + "] 流程下发通讯异常!");
                            Message fMsg = new Message();
                            fMsg.what = 7;
                            fMsg.obj = i;
                            showLvHandler.sendMessage(fMsg);
                            RealTimeStatusThread.resume();
                            return;
                        }
                    }
                } else {
                    i++;
                }
            }
            try {
                // 等待8秒钟读取校验值
                sleep(8000);
                // 获取CRC检验值
                if (ckbFlowInfo == null) {
                    ckbFlowInfo = new flow();
                }
                ckbFlowInfo.clear();
                byte[] arrayOfByte2 = DataUtil.shortToByte((short) 550);
                DataUtil.reverse(arrayOfByte2);
                SendManager.SendCmd(cName + "_流程校验信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 8);
                sleep(10000);
                Log.i("flowCRC2", String.valueOf(localFlowsCrc));

                if (ckbFlowInfo.compatibleVersion > 0) {    // 测试去掉
                    // 两边校验了，单校验结果不同的情况
                    if (ckbFlowInfo.crc != 0 && !((ckbFlowInfo.crc & 0xffff) == (localFlowsCrc & 0xffff))) {
                        Message fMsg = new Message();
                        fMsg.what = 11;
                        showLvHandler.sendMessage(fMsg);
                        RealTimeStatusThread.resume();
                        return;
                    }
                } else {
                    saveRunInfo2File("组分[" + cName + "] " + "兼容版本号：" + ckbFlowInfo.compatibleVersion + "流程下发验证功能不可用，建议升级核心控制板软件!");
                    // 完成下发，提示建议升级信息
                    Message fMsg = new Message();
                    fMsg.what = 12;
                    showLvHandler.sendMessage(fMsg);
                    RealTimeStatusThread.resume();
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message fMsg = new Message();
            fMsg.what = 6;
            showLvHandler.sendMessage(fMsg);
            RealTimeStatusThread.resume();
        }
    }

}
