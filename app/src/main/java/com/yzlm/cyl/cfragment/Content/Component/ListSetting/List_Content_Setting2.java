package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_PublicStop;
import com.yzlm.cyl.cfragment.Dialog.Dialog_RunMsg;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.Timer;
import java.util.TimerTask;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.devStatusIsBusy;
import static com.yzlm.cyl.cfragment.Global.digitalChannelVal;
import static com.yzlm.cyl.cfragment.Global.getAnalogInQuanlityStatus;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.BinaryToHexbytes;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List_Content_Setting2 extends SubFragment {

    private static List_Content_Setting2 fragment = null;
    private static EditText mEtSamplePumpTime;
    public static EditText mEtSampleTime;
    private static EditText mETChannelRange;
    private static EditText mETChannelLimit;
    public static EditText mEtKCYSZ;


    private static Dialog_RunMsg dialogRunningMsg;
    private static Timer timer;
    private static Callbacks mCallbacks;

    LinearLayout mLayoutSampleSysA, mLayoutSampleSysB, mLayoutSampleSysC;
    public static TextView mTextNowStatus, mTextDevUnit;
    public static LinearLayout layoutDevSetting;
    Button mBtnSampleSysID, mBtnChannel, mBtnDevice, mBtnUnit;
    Button mBtnCKBSample, mBtnSampleTest, mBtnKCYSampleTest;

    String[] sampleSysList;
    String[] channelList;
    String[] devList;
    static String[] unitList;
    Button mBtnClean, mBtnMix, mBtnWaste, mBtnLevelSwitch1, mBtnLevelSwitch2, mBtnLevelSwitch3;
    LinearLayout mLayoutControlDevicesSetting;

    public static List_Content_Setting2 newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Setting2();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list_content_setting2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        sampleSysList = getResources().getStringArray(R.array.sampleSystem);
        channelList = getResources().getStringArray(R.array.channel);
        devList = getResources().getStringArray(R.array.operatingDevice);
        unitList = getResources().getStringArray(R.array.flowMeter);
        mLayoutControlDevicesSetting = v.findViewById(R.id.layout_controlDevicesSetting);
        mBtnSampleSysID = v.findViewById(R.id.btnSampleSysDev);
        mBtnSampleSysID.setOnClickListener(new btnOnClickListener());

        mLayoutSampleSysA = v.findViewById(R.id.layout_sampleA);
        mLayoutSampleSysB = v.findViewById(R.id.layout_sampleB);
        mLayoutSampleSysC = v.findViewById(R.id.layout_sampleC);

        if (IOBoardUsed) {
            mLayoutControlDevicesSetting.setVisibility(View.VISIBLE);
        } else {
            mLayoutControlDevicesSetting.setVisibility(View.GONE);
        }

        try {
            mLayoutSampleSysC.setVisibility(View.GONE);
            if (getPublicConfigData("SYS_RELAYCONF").equals("3") && IOBoardUsed) {
                //只有开采样R2的时候才显示采样系统C
                updatePublicConfigData("SAMPLE_SYSTEM_ID", String.valueOf(2));
                mLayoutSampleSysA.setVisibility(View.GONE);
                mLayoutSampleSysB.setVisibility(View.GONE);
                mLayoutSampleSysC.setVisibility(View.VISIBLE);
            } else if (!getPublicConfigData("SYS_RELAYCONF").equals("1") || !IOBoardUsed) {
                // 不是均质预处理的时候只显示采样系统A
                updatePublicConfigData("SAMPLE_SYSTEM_ID", String.valueOf(0));
                mLayoutSampleSysA.setVisibility(View.VISIBLE);
                mLayoutSampleSysB.setVisibility(View.GONE);

            } else {

                if (Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID")) == 0) {
                    mLayoutSampleSysA.setVisibility(View.VISIBLE);
                    mLayoutSampleSysB.setVisibility(View.GONE);
                } else if (Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID")) == 2) {
                    updatePublicConfigData("SAMPLE_SYSTEM_ID", String.valueOf(0));
                    mLayoutSampleSysA.setVisibility(View.VISIBLE);
                    mLayoutSampleSysB.setVisibility(View.GONE);
                } else {
                    mLayoutSampleSysB.setVisibility(View.VISIBLE);
                    mLayoutSampleSysA.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*预处理A**/
        mEtSamplePumpTime = v.findViewById(R.id.eTcybsz);
        mEtSamplePumpTime.setOnFocusChangeListener(new cybszFocusChange());
        mEtSamplePumpTime.setOnEditorActionListener(new cybszFocusChange());
        mEtSamplePumpTime.setTextColor(Color.BLACK);
        if (strComponent.get(1).length > 0) {
            byte[] arrayOfByte = shortToByte((short) 5306);
            reverse(arrayOfByte);
            SendManager.SendCmd(strComponent.get(1)[0] + "_查开采样时间" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 500, 1);
        }

        mBtnCKBSample = v.findViewById(R.id.btnCKSampleTest);
        mBtnCKBSample.setOnClickListener(new btnCKBSampleTestOnClickListener());

        /*预处理B**/
        mEtSampleTime = v.findViewById(R.id.etSampleTime);
        mEtSampleTime.setOnFocusChangeListener(new EdSampleTimeFocusChange());
        mEtSampleTime.setOnEditorActionListener(new EdSampleTimeFocusChange());
        if (IOBoardUsed) {
            byte[] arrayOfByte = shortToByte((short) 7);
            reverse(arrayOfByte);
            SendManager.SendCmd("所有组分" + "_读取均质预处理采样时间" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S1, 3, 200, 4);
        }
        mBtnSampleTest = v.findViewById(R.id.btnSampleTest);
        mBtnSampleTest.setOnClickListener(new btnSampleTestOnClickListener());

        if (QueryMeasBoardType(strComponent.get(1)[0]).equals("2")){
            /*预处理C**/
            mEtKCYSZ = v.findViewById(R.id.eTkcysz);
            mEtKCYSZ.setOnFocusChangeListener(new kcyszFocusChange());
            mEtKCYSZ.setOnEditorActionListener(new kcyszFocusChange());
            if (strComponent.get(1).length > 0) {
                byte[] arrayOfByte = shortToByte((short) 5312);
                reverse(arrayOfByte);
                SendManager.SendCmd(strComponent.get(1)[0] + "_查开采样时间2" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 500, 1);
            }
            mBtnKCYSampleTest = v.findViewById(R.id.btnKCYSampleTest);
            mBtnKCYSampleTest.setOnClickListener(new btnKCYTestOnClickListener());
        }

        mBtnClean = v.findViewById(R.id.btnQX);
        mBtnClean.setOnClickListener(new btnCleanOnClickListener());

        mBtnMix = v.findViewById(R.id.btnMix);
        mBtnMix.setOnClickListener(new btnMixOnClickListener());

        mBtnWaste = v.findViewById(R.id.btnWaste);
        mBtnWaste.setOnClickListener(new btnWasteOnClickListener());

        mBtnLevelSwitch1 = v.findViewById(R.id.btnLevelSwitch1);
        mBtnLevelSwitch2 = v.findViewById(R.id.btnLevelSwitch2);
        mBtnLevelSwitch3 = v.findViewById(R.id.btnLevelSwitch3);
        mBtnLevelSwitch1.setOnClickListener(new btnOnClickListener());
        mBtnLevelSwitch2.setOnClickListener(new btnOnClickListener());
        mBtnLevelSwitch3.setOnClickListener(new btnOnClickListener());
        int LevelSwitch = 0;

        LevelSwitch = Integer.parseInt(getPublicConfigData("SAMPLE_LEVEL_SWITCH"));

        mBtnLevelSwitch1.setText(LevelSwitch == 1 ? context.getResources().getString(R.string.open) : context.getResources().getString(R.string.close));
        mBtnLevelSwitch2.setText(LevelSwitch == 1 ? context.getResources().getString(R.string.open) : context.getResources().getString(R.string.close));
        mBtnLevelSwitch3.setText(LevelSwitch == 1 ? context.getResources().getString(R.string.open) : context.getResources().getString(R.string.close));

        // 反控测量
        mBtnChannel = v.findViewById(R.id.btnChannel);
        mBtnChannel.setOnClickListener(new btnOnClickListener());

        mBtnDevice = v.findViewById(R.id.btnChannelDev);
        mBtnDevice.setOnClickListener(new btnOnClickListener());

        mBtnUnit = v.findViewById(R.id.btnChannelUnit);
        mBtnUnit.setOnClickListener(new btnOnClickListener());

        mTextNowStatus = v.findViewById(R.id.txtDevStatus);
        mTextDevUnit = v.findViewById(R.id.txtDevStatusUnit);
        layoutDevSetting = v.findViewById(R.id.layoutDevSetting);

        mETChannelRange = v.findViewById(R.id.eTChannelRange);
        mETChannelRange.setOnFocusChangeListener(new EditTextFocusChange());
        mETChannelRange.setOnEditorActionListener(new EditTextFocusChange());
        mETChannelRange.setText(getPublicConfigData("CONTROL_RANGE"));

        mETChannelLimit = v.findViewById(R.id.eTChannelLimit);
        mETChannelLimit.setOnFocusChangeListener(new EditTextFocusChange());
        mETChannelLimit.setOnEditorActionListener(new EditTextFocusChange());
        mETChannelLimit.setText(getPublicConfigData("CONTROL_DOOR_LIMIT"));
        btnTextShow();
        /*测量防止点击*/
        if (devStatusIsBusy()) {
            mEtSampleTime.setEnabled(false);
            mEtSampleTime.setBackgroundColor(getResources().getColor(R.color.darkGray));
            mEtSamplePumpTime.setEnabled(false);
            mEtSamplePumpTime.setBackgroundColor(getResources().getColor(R.color.darkGray));

            mBtnClean.setEnabled(false);
            mBtnClean.setBackground(getResources().getDrawable(R.drawable.btnp));

            mBtnMix.setEnabled(false);
            mBtnMix.setBackground(getResources().getDrawable(R.drawable.btnp));

            mBtnWaste.setEnabled(false);
            mBtnWaste.setBackground(getResources().getDrawable(R.drawable.btnp));

            mBtnSampleTest.setEnabled(false);
            mBtnSampleTest.setBackground(getResources().getDrawable(R.drawable.btnp));
        } else {
            mEtSampleTime.setEnabled(true);
            mEtSampleTime.setBackgroundColor(Color.WHITE);
            mEtSamplePumpTime.setEnabled(true);
            mEtSamplePumpTime.setBackgroundColor(Color.WHITE);

            mBtnClean.setEnabled(true);
            mBtnClean.setBackground(getResources().getDrawable(R.drawable.btnn));
            mBtnMix.setEnabled(true);
            mBtnMix.setBackground(getResources().getDrawable(R.drawable.btnn));
            mBtnWaste.setEnabled(true);
            mBtnWaste.setBackground(getResources().getDrawable(R.drawable.btnn));

            mBtnSampleTest.setEnabled(true);
            mBtnSampleTest.setBackground(getResources().getDrawable(R.drawable.btnn));
        }
    }

    /*
     * 界面按钮显示刷新
     **/
    private void btnTextShow() {
        try {
            if (Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL")) < channelList.length) {
                mBtnChannel.setText(channelList[Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL"))]);
                if ((Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL")) == 0)
                        || (Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL")) == 1)) {
                    layoutDevSetting.setVisibility(View.GONE);
                }
            }
            if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) < devList.length) {
                mBtnDevice.setText(devList[Integer.parseInt(getPublicConfigData("CONTROL_DEV"))]);
                updateDevStatusTextShow();
            }
            if (Integer.parseInt(getPublicConfigData("DEV_UNIT")) < unitList.length) {
                mBtnUnit.setText(unitList[Integer.parseInt(getPublicConfigData("DEV_UNIT"))]);
            }
            if (Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID")) < sampleSysList.length) {
                mBtnSampleSysID.setText(sampleSysList[Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID"))]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 当前状态刷新
     */
    public static void updateDevStatusTextShow() {

        try {
            if (mTextNowStatus != null) {
                /*采样器*/
                if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 1) {
                    if (Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL")) < 2) {

                        mTextNowStatus.setText(digitalChannelVal[Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL"))] == 1 ? context.getResources().getString(R.string.open) : context.getResources().getString(R.string.close));
                        layoutDevSetting.setVisibility(View.GONE);
                    } else {
                        mTextNowStatus.setText("---");
                    }
                    mTextDevUnit.setVisibility(View.GONE);
                } else {
                    /*无反控*/
                    mTextNowStatus.setText("---");
                    if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 0) {
                        layoutDevSetting.setVisibility(View.GONE);
                        mTextDevUnit.setVisibility(View.GONE);
                    } else {
                        layoutDevSetting.setVisibility(View.VISIBLE);
                        mTextNowStatus.setText(String.format("%.3f", getAnalogInQuanlityStatus()));
                        if (Integer.parseInt(getPublicConfigData("DEV_UNIT")) == 0) {
                            mTextDevUnit.setVisibility(View.GONE);
                        } else {
                            mTextDevUnit.setVisibility(View.VISIBLE);
                            mTextDevUnit.setText(unitList[Integer.parseInt(getPublicConfigData("DEV_UNIT"))]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i("except3", e.toString());
        }
    }


    private static boolean textColor = false;
    @SuppressLint("HandlerLeak")
    public static Handler mExtendConnHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 5306: {
                    if (mEtSamplePumpTime != null) {

                        if (strComponent.size() > 0) {
                            mEtSamplePumpTime.setText((getCmds(strComponent.get(1)[0]).getCmd(5306).getValue() == null ? "" : getCmds(strComponent.get(1)[0]).getCmd(5306).getValue()).toString());
                            if (textColor) {
                                textColor = false;
                                mEtSamplePumpTime.setTextColor(Color.BLUE);
                            } else {
                                textColor = true;
                                mEtSamplePumpTime.setTextColor(Color.RED);
                            }
                        }
                    }
                }
                break;
                case 5312: {
                    if (mEtKCYSZ != null) {

                        if (strComponent.size() > 0) {
                            mEtKCYSZ.setText((getCmds(strComponent.get(1)[0]).getCmd(5312).getValue() == null ? "" : getCmds(strComponent.get(1)[0]).getCmd(5312).getValue()).toString());
                            if (textColor) {
                                textColor = false;
                                mEtKCYSZ.setTextColor(Color.BLUE);
                            } else {
                                textColor = true;
                                mEtKCYSZ.setTextColor(Color.RED);
                            }
                        }
                    }
                }
                break;
            }
        }
    };

    private class btnCKBSampleTestOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (devStatusIsBusy()) {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                return;
            }
            /*发送开采样动作*/
            for (String item : strComponent.get(1)) {
                String OpenSample = "00000000,00000000,00000000,00000001";
                byte[] arrayOfByte = DataUtil.shortToByte((short) 5306);
                DataUtil.reverse(arrayOfByte);
                SendManager.SendCmd(item + "_CK开采样_8_01", S0, 3, 500, BinaryToHexbytes(OpenSample));
            }
            main.removeDestopText(mfb);
            dialogRunningMsg = new Dialog_RunMsg();
            Bundle bundle = new Bundle();
            bundle.putString("alert-msg", getString(R.string.open_pump));
            dialogRunningMsg.setArguments(bundle);
            dialogRunningMsg.setTargetFragment(List_Content_Setting2.this, 1);
            dialogRunningMsg.show(fm, "Dialog_msg");

            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                    timer.cancel();
                }
            };
            timer.schedule(task, 1200 * 1000, 1000);
        }
    }

    private class btnSampleTestOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (devStatusIsBusy()) {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                return;
            }

            byte[] bytSampleTime = copybyte(toByteArray(600, 4));
            SendManager.SendCmd("IO" + "_" + "接口板采样时间_06_06", S1, 3, 500, bytSampleTime);

            SendManager.SendCmd("IO" + "_" + "接口板开采样_0C_02", S1, 3, 500, null);

            main.removeDestopText(mfb);
            dialogRunningMsg = new Dialog_RunMsg();
            Bundle bundle = new Bundle();
            bundle.putString("alert-msg", "开采样 ");
            dialogRunningMsg.setArguments(bundle);
            dialogRunningMsg.setTargetFragment(List_Content_Setting2.this, 1);
            dialogRunningMsg.show(fm, "Dialog_msg");

            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    timer.cancel();
                }
            };
            timer.schedule(task, 1200 * 1000, 1000);
        }
    }

    private class btnKCYTestOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (devStatusIsBusy()) {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                return;
            }

            byte[] bytSampleTime;
            if (mEtKCYSZ.getText().toString().equals("")) {
                bytSampleTime = copybyte(toByteArray(60, 4));
            } else {
                bytSampleTime = copybyte(toByteArray(Integer.parseInt(mEtKCYSZ.getText().toString()), 4));
            }
            SendManager.SendCmd("IO" + "_" + "接口板采样时间_06_06", S1, 3, 500, bytSampleTime);

            SendManager.SendCmd("IO" + "_" + "接口板开采样_06_02", S1, 3, 500, new byte[]{0x00,0x01,0x00,0x00});

            main.removeDestopText(mfb);
            dialogRunningMsg = new Dialog_RunMsg();
            Bundle bundle = new Bundle();
            bundle.putString("alert-msg", "开采样 ");
            dialogRunningMsg.setArguments(bundle);
            dialogRunningMsg.setTargetFragment(List_Content_Setting2.this, 1);
            dialogRunningMsg.show(fm, "Dialog_msg");

            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    timer.cancel();
                }
            };
            timer.schedule(task, 1200 * 1000, 1000);
        }
    }

    private class btnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (devStatusIsBusy()) {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                return;
            }
            switch (v.getId()) {
                /*预处理系统*/
                case R.id.btnSampleSysDev:
                    int sampleSysId;
                    // 开采样R2的时候只显示采样系统C
                    if (getPublicConfigData("SYS_RELAYCONF").equals("3") && IOBoardUsed) {
                        break;
                    }
                    // 不是均质预处理的时候只显示采样系统A
                    if (!getPublicConfigData("SYS_RELAYCONF").equals("1") || !IOBoardUsed) {
                        break;
                    }
                    sampleSysId = Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID"));
                    if (sampleSysId < sampleSysList.length - 2) {
                        sampleSysId++;
                    } else {
                        sampleSysId = 0;
                    }
                    updatePublicConfigData("SAMPLE_SYSTEM_ID", String.valueOf(sampleSysId));
                    if (Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID")) == 0) {
                        mLayoutSampleSysA.setVisibility(View.VISIBLE);
                        mLayoutSampleSysB.setVisibility(View.GONE);
                        mLayoutSampleSysC.setVisibility(View.GONE);
                    } else if (Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID")) == 1) {
                        mLayoutSampleSysB.setVisibility(View.VISIBLE);
                        mLayoutSampleSysA.setVisibility(View.GONE);
                        mLayoutSampleSysC.setVisibility(View.GONE);
                    } else {
                        mLayoutSampleSysC.setVisibility(View.VISIBLE);
                        mLayoutSampleSysA.setVisibility(View.GONE);
                        mLayoutSampleSysB.setVisibility(View.GONE);
                    }
                    break;
                case R.id.btnLevelSwitch1:
                case R.id.btnLevelSwitch2:
                case R.id.btnLevelSwitch3:
                    int LevelSwitch = 1;
                    try {
                        LevelSwitch = Integer.parseInt((getPublicConfigData("SAMPLE_LEVEL_SWITCH")));
                        LevelSwitch = (LevelSwitch == 1 ? 0 : 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    saveOperationLogPublicDataModifyMsg(" ", "SAMPLE_LEVEL_SWITCH", String.valueOf(LevelSwitch), "液位报警", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("SAMPLE_LEVEL_SWITCH", String.valueOf(LevelSwitch));
                    mBtnLevelSwitch1.setText(LevelSwitch == 1 ? getResources().getString(R.string.open) : getResources().getString(R.string.close));
                    mBtnLevelSwitch2.setText(LevelSwitch == 1 ? getResources().getString(R.string.open) : getResources().getString(R.string.close));
                    mBtnLevelSwitch3.setText(LevelSwitch == 1 ? getResources().getString(R.string.open) : getResources().getString(R.string.close));
                    if (LevelSwitch == 1) {
                        /*采样器情况下只能选择开关量输入*/
                        if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 1) {
                            int sampleDevId = Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL"));
                            /*当液位监测开启的时候，1#开关量输入不可选择*/
                            if (Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL")) == 0) {
                                sampleDevId = 1;
                                Toast.makeText(context, getResources().getString(R.string.measureSwitchToNo2), Toast.LENGTH_SHORT).show();
                            }
                            saveOperationLogPublicDataModifyMsg(" ", "CONTROL_CHANNEL", String.valueOf(sampleDevId), "通道选择", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("CONTROL_CHANNEL", String.valueOf(sampleDevId));
                        }
                    }
                    break;
                /*反控测量**/
                case R.id.btnChannel://通道
                    int channelId = Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL"));
                    if (channelId < channelList.length - 1) {
                        channelId++;
                    } else {
                        channelId = 0;
                    }
                    /*采样器情况下只能选择开关量输入*/
                    if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 1) {
                        if (channelId > 1) {
                            channelId = 0;
                        }
                        /*当液位监测开启的时候，1#开关量输入不可选择*/
                        if (Integer.parseInt(getPublicConfigData("SAMPLE_LEVEL_SWITCH")) == 1) {
                            channelId = 1;
                        }
                    } else if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) != 0) {
                        /*不可选择开关量，直接选择至1#模拟量*/
                        if (channelId < 2) {
                            channelId = 2;
                        }
                    }
                    saveOperationLogPublicDataModifyMsg(" ", "CONTROL_CHANNEL", String.valueOf(channelId), "通道选择", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("CONTROL_CHANNEL", String.valueOf(channelId));
                    if (channelId == 0 || channelId == 1) {
                        digitalChannelVal[channelId] = 0;
                    }
                    break;
                case R.id.btnChannelDev: { // 设备
                    int devId = Integer.parseInt(getPublicConfigData("CONTROL_DEV"));
                    if (devId < devList.length - 1) {
                        devId++;
                    } else {
                        devId = 0;
                    }
                    /*采样器情况下只能选择开关量输入*/
                    if (devId == 1) {
                        int sampleDevId = 0;
                        /*当液位监测开启的时候，1#开关量输入不可选择*/
                        if ((Integer.parseInt(getPublicConfigData("SAMPLE_LEVEL_SWITCH")) == 1)) {
                            sampleDevId = 1;
                        }
                        saveOperationLogPublicDataModifyMsg(" ", "CONTROL_CHANNEL", String.valueOf(sampleDevId), "通道选择", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("CONTROL_CHANNEL", String.valueOf(sampleDevId));

                    } else if (devId == 2 || devId == 3) {
                        /*流量计或PH*/
                        if (Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL")) < 2) {
                            /*不可选择开关量，直接选择至1#模拟量*/
                            updatePublicConfigData("CONTROL_CHANNEL", String.valueOf(2));
                        }
                        if (devId == 3) {
                            /*PH没有单位*/
                            updatePublicConfigData("DEV_UNIT", String.valueOf(0));
                        }
                    }
                    saveOperationLogPublicDataModifyMsg(" ", "CONTROL_DEV", String.valueOf(devId), "设备选择", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("CONTROL_DEV", String.valueOf(devId));
                }
                break;
                case R.id.btnChannelUnit:// 单位
                    int unitId = Integer.parseInt(getPublicConfigData("DEV_UNIT"));
                    if (unitId < unitList.length - 1) {
                        unitId++;
                    } else {
                        unitId = 0;
                    }
                    if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 3) {
                        unitId = 0;
                    }
                    saveOperationLogPublicDataModifyMsg(" ", "DEV_UNIT", String.valueOf(unitId), "单位选择", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("DEV_UNIT", String.valueOf(unitId));
                    break;
            }
            btnTextShow();
        }
    }

    private class EditTextFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (devStatusIsBusy()) {
                    Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                    return;
                }
                switch (v.getId()) {
                    case R.id.eTChannelRange:
                        if (!mETChannelRange.getText().toString().equals("")) {
                            String strValue = editDataFormat(mETChannelRange.getText().toString(), 3);
                            saveOperationLogPublicDataModifyMsg("公共", "CONTROL_RANGE", strValue, "反控量程", ErrorLog.msgType.操作_信息);
                            if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 3) {
                                if (Float.parseFloat(strValue) < 0 || Float.parseFloat(strValue) > 14) {
                                    Toast.makeText(getActivity(), context.getResources().getString(R.string.PHBeRangeValue) + " 0 - 14 !", Toast.LENGTH_SHORT).show();
                                    mETChannelRange.setText(getPublicConfigData("CONTROL_RANGE"));
                                    break;
                                }
                            } else if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 2) {
                                if (Float.parseFloat(strValue) <= 0 || Float.parseFloat(strValue) >= 9999) {
                                    Toast.makeText(getActivity(), context.getResources().getString(R.string.PHBeRangeValue) + " 0 - 9999 !", Toast.LENGTH_SHORT).show();
                                    mETChannelRange.setText(getPublicConfigData("CONTROL_RANGE"));
                                    break;
                                }
                            }
                            updatePublicConfigData("CONTROL_RANGE", strValue);
                            mETChannelRange.setText(strValue);
                        } else {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.rangeCannotBeEmpty), Toast.LENGTH_SHORT).show();
                            mETChannelRange.setText(getPublicConfigData("CONTROL_RANGE"));
                        }
                        break;
                    case R.id.eTChannelLimit:
                        if (!mETChannelLimit.getText().toString().equals("")) {
                            String strValue = editDataFormat(mETChannelLimit.getText().toString(), 3);
                            if (Float.parseFloat(strValue) < Float.parseFloat(getPublicConfigData("CONTROL_RANGE")) &&
                                    Float.parseFloat(strValue) > 0) {
                                saveOperationLogPublicDataModifyMsg("公共", "CONTROL_DOOR_LIMIT", strValue, "反控门限", ErrorLog.msgType.操作_信息);
                                updatePublicConfigData("CONTROL_DOOR_LIMIT", strValue);
                                mETChannelLimit.setText(strValue);
                            } else {
                                Toast.makeText(getActivity(), context.getResources().getString(R.string.doorLimitCannotBeRangeValue), Toast.LENGTH_SHORT).show();
                                mETChannelLimit.setText(getPublicConfigData("CONTROL_DOOR_LIMIT"));
                            }
                        } else {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.doorLimitCannotBeEmpty), Toast.LENGTH_SHORT).show();
                            mETChannelLimit.setText(getPublicConfigData("CONTROL_DOOR_LIMIT"));
                        }
                        break;
                }
                FullWindows(mActivityWindow);
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mETChannelLimit.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class cybszFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (devStatusIsBusy()) {
                    Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                    return;
                }
                String cybsz = mEtSamplePumpTime.getText().toString().trim();
                try {
                    if (!cybsz.equals("")) {
                        cybsz = editDataFormat(cybsz, 1);
                        if (Integer.parseInt(cybsz) > 1200) {
                            cybsz = "1200";
                        }
                        if (Integer.parseInt(cybsz) < 3) {
                            cybsz = "3";
                        }
                        mEtSamplePumpTime.setText(cybsz);

                        for (String item : strComponent.get(1)) {
                            saveOperationLogPublicDataModifyMsg(" ", "CK_PUMP_TIME", cybsz, "CKB开采样时间", ErrorLog.msgType.操作_信息);
                            int cybszInt = Integer.parseInt(cybsz);
                            byte[] cybszByte = copybyte(toByteArray(cybszInt, 4));

                            updatePublicConfigData("CK_PUMP_TIME", String.valueOf(cybszInt));
                            SendManager.SendCmd(item + "_" + "设置开采样时间_06_59", S0, 3, 500, cybszByte);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.open_pump_time_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), getString(R.string.open_pump_time_is_set_error), Toast.LENGTH_SHORT).show();
                }
                FullWindows(mActivityWindow);
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtSamplePumpTime.clearFocus();
                return true;
            }
            return false;
        }
    }


    private class EdSampleTimeFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (devStatusIsBusy()) {
                    Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!mEtSampleTime.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtSampleTime.getText().toString(), 1);

                    int intSampleTime = Integer.parseInt(strValue);

                    if (intSampleTime > 1200) {
                        intSampleTime = 1200;
                    }
                    if (intSampleTime < 3) {
                        intSampleTime = 3;
                    }
                    mEtSampleTime.setText(String.valueOf(intSampleTime));

                    byte[] bytSampleTime = copybyte(toByteArray(intSampleTime, 4));
                    SendManager.SendCmd("IO" + "_" + "接口板采样时间_06_06", S1, 3, 500, bytSampleTime);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.open_pump_time_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtSampleTime.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class kcyszFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (devStatusIsBusy()) {
                    Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                    return;
                }
                String cybsz = mEtKCYSZ.getText().toString().trim();
                try {
                    if (!cybsz.equals("")) {
                        cybsz = editDataFormat(cybsz, 1);
                        if (Integer.parseInt(cybsz) > 1200) {
                            cybsz = "1200";
                        }
                        if (Integer.parseInt(cybsz) < 3) {
                            cybsz = "3";
                        }
                        mEtKCYSZ.setText(cybsz);

                        for (String item : strComponent.get(1)) {
                            saveOperationLogPublicDataModifyMsg(" ", "CK_PUMP_TIME2", cybsz, "CKB开采样时间", ErrorLog.msgType.操作_信息);
                            int cybszInt = Integer.parseInt(cybsz);
                            byte[] cybszByte = copybyte(toByteArray(cybszInt, 4));

                            updatePublicConfigData("CK_PUMP_TIME2", String.valueOf(cybszInt));
                            SendManager.SendCmd(item + "_" + "设置开采样时间2_06_260", S0, 3, 500, cybszByte);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.open_pump_time_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), getString(R.string.open_pump_time_is_set_error), Toast.LENGTH_SHORT).show();
                }
                FullWindows(mActivityWindow);
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtKCYSZ.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class btnCleanOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!devStatusIsBusy()) {
                SendManager.SendCmd("IO" + "_" + "接口板清洗_0C_05", S1, 3, 500, null);
                DialogShow(getString(R.string.cleaning));
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class btnMixOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!devStatusIsBusy()) {
                SendManager.SendCmd("IO" + "_" + "接口板搅拌_0C_03", S1, 3, 500, null);
                DialogShow(getString(R.string.mixing));
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class btnWasteOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!devStatusIsBusy()) {
                SendManager.SendCmd("IO" + "_" + "接口板排废_0C_04", S1, 3, 500, null);
                DialogShow(getString(R.string.waste_discharging));
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
            }
        }
    }


    @SuppressLint("HandlerLeak")
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: /*更新界面采样泵开关显示*/

                    mCallbacks.onDialogRS();

                    if (timer != null) {
                        timer.cancel();
                    }
                    if (dialogRunningMsg != null) {
                        dialogRunningMsg.dismiss();
                    }
                    SendManager.SendCmd("IO" + "_" + "接口板停止_0C_14", S1, 3, 500, null);
                    break;
                case 2:
                    /*测控板关采样*/
                    mCallbacks.onDialogRS();
                    if (timer != null) {
                        timer.cancel();
                    }
                    if (dialogRunningMsg != null) {
                        dialogRunningMsg.dismiss();
                    }
                    for (String item : strComponent.get(1)) {
                        String OpenSample = "00000000,00000000,00000000,00000000";
                        byte[] arrayOfByte = shortToByte((short) 5306);
                        reverse(arrayOfByte);
                        SendManager.SendCmd(item + "_CK关采样_8_01", S0, 3, 500, BinaryToHexbytes(OpenSample));
                    }
                    break;
                case 3:

                    mCallbacks.onDialogRS();

                    if (timer != null) {
                        timer.cancel();
                    }
                    if (dialogRunningMsg != null) {
                        dialogRunningMsg.dismiss();
                    }
                    SendManager.SendCmd("IO" + "_" + "接口板停止_06_02", S1, 3, 500, new byte[]{0x00,0x00,0x00,0x00});
                    break;
            }
        }
    };

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    public interface Callbacks {
        void onDialogRS();

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

    private void DialogShow(String msg) {
        main.removeDestopText(mfb);
        Dialog_PublicStop diaStop = new Dialog_PublicStop();
        Bundle bundle = new Bundle();
        bundle.putString("alert-stop", msg);
        diaStop.setArguments(bundle);
        diaStop.setTargetFragment(fragment, 1);
        diaStop.show(fm, "Dialog_stop");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            SendManager.SendCmd("IO" + "_" + "接口板停止_0C_14", S1, 3, 500, null);
        } else if (requestCode == 99) {

            Message msg = new Message();
            if (Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID")) == 0) {
                msg.what = 2;
            }else if (Integer.parseInt(getPublicConfigData("SAMPLE_SYSTEM_ID")) == 2) {
                msg.what = 3;
            } else {
                msg.what = 1;
            }
            handler.sendMessage(msg);
        }
    }
}
