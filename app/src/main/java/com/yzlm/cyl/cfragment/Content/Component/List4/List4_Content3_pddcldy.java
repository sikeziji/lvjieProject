package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6.FloatStringFormatInput;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pcldy.setBsLedStatus;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.R.id.togBtnDDPumpCy;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pddcldy extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pddcldy fragment = null;

    private ToggleButton mTogBtnDDPumpCy;
    private ToggleButton mTogBtnDDPumpPy;
    private static ToggleButton togBtnReadDdData;

    private static TextView tvDDEng;
    private static TextView tvORPEng;
    private static Spinner mSpDDLedGain;

    private static TextView tvTempCoef;
    private static EditText mEditMeaValue;
    private static EditText meditEndValue;
    private static EditText mEditddSpeed;
    private static EditText mEditFullSpeed;
    private static EditText mEditEndPoint;
    private static EditText mEditCollectNum;
    private static EditText mEditddVolumeThresholdVal;
    private static ToggleButton mTogBtnMeaMode;
    private static EditText mEditCatalystThresholdValue;
    private static EditText mEditOxidizingAgentThresholdValue;
    private static EditText mEditReductantThresholdValueValue;
    private Thread readDDEngThread;
    private Boolean threadStopFlag = false;
    private static int readDDEngCounter;
    private int iPoint = 2;


    public static List4_Content3_pddcldy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pddcldy();
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
        return R.layout.list4_content3_pddcldy;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.LockDisplayShowFlag = true;
            iPoint = Integer.parseInt(getConfigData(mCompName, "YXWS"));
            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());
            InitWidget();

            byte[] arrayOfByte = DataUtil.shortToByte((short) 0);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(mCompName + "_查温度K" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 2);

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 5500);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(mCompName + "_查LED增益" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 5720);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(mCompName + "_查滴定过程控制参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 11);

            byte[] arrayOfByte4 = DataUtil.shortToByte((short) 5711);
            DataUtil.reverse(arrayOfByte4);
            SendManager.SendCmd(mCompName + "_查滴定终点判定方式" + "_3_" + DataUtil.bytesToHexString(arrayOfByte4, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            byte[] arrayOfByte5 = DataUtil.shortToByte((short) 9300);
            DataUtil.reverse(arrayOfByte5);
            SendManager.SendCmd(mCompName + "_查滴定实际缺液阈值" + "_3_" + DataUtil.bytesToHexString(arrayOfByte5, 2).replace(" ", ""), Global.S0, 3, 200, 3);

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void InitWidget() {
        togBtnReadDdData = v.findViewById(R.id.tBtnDDCLDY_sscl);
        togBtnReadDdData.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());
        tvDDEng = v.findViewById(R.id.ddcldy_eng);
        tvORPEng = v.findViewById(R.id.txtORP_E);

        mSpDDLedGain = v.findViewById(R.id.spDDLedGain);
        InitSpinner(context, mSpDDLedGain, getResources().getStringArray(R.array.Gain), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpDDLedGain.setOnItemSelectedListener(new SpClickListener());

        tvTempCoef = v.findViewById(R.id.tvDDTemperKB);

        mTogBtnDDPumpCy = v.findViewById(togBtnDDPumpCy);
        mTogBtnDDPumpCy.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());
        mTogBtnDDPumpPy = v.findViewById(R.id.togBtnDDPumpPy);
        mTogBtnDDPumpPy.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());

        mEditddSpeed = v.findViewById(R.id.edit_ddSpeed);
        mEditddSpeed.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditddSpeed.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditFullSpeed = v.findViewById(R.id.edit_fullSpeed);
        mEditFullSpeed.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditFullSpeed.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditEndPoint = v.findViewById(R.id.edit_endPoint);
        mEditEndPoint.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditEndPoint.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditCollectNum = v.findViewById(R.id.edit_adjustSec);
        mEditCollectNum.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditCollectNum.setOnEditorActionListener(new EditChangeListenerHandler());


        mEditMeaValue = v.findViewById(R.id.edit_meaValue);
        mEditMeaValue.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditMeaValue.setOnEditorActionListener(new EditChangeListenerHandler());

        meditEndValue = v.findViewById(R.id.edit_endValue);
        meditEndValue.setOnFocusChangeListener(new EditChangeListenerHandler());
        meditEndValue.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditddVolumeThresholdVal = v.findViewById(R.id.edit_ddVolumeThresholdVal);
        mEditddVolumeThresholdVal.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditddVolumeThresholdVal.setOnEditorActionListener(new EditChangeListenerHandler());

        LinearLayout mLayoutDDSpeed = v.findViewById(R.id.layout_ddSpeed);
        LinearLayout mLayoutPumpTest = v.findViewById(R.id.layout_pumpTest);
        LinearLayout mLayoutDDMeaMode = v.findViewById(R.id.layout_DDMeaMode);

        mTogBtnMeaMode = v.findViewById(R.id.togBtn_meaMode);
        mTogBtnMeaMode.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());

        LinearLayout mLayoutddThresholdValue = v.findViewById(R.id.layout_ddThresholdValue);

        mEditCatalystThresholdValue = v.findViewById(R.id.editCatalystThresholdValue);
        mEditCatalystThresholdValue.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditCatalystThresholdValue.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditOxidizingAgentThresholdValue = v.findViewById(R.id.editOxidizingAgentThresholdValue);
        mEditOxidizingAgentThresholdValue.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditOxidizingAgentThresholdValue.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditReductantThresholdValueValue = v.findViewById(R.id.editReductantThresholdValue);
        mEditReductantThresholdValueValue.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditReductantThresholdValueValue.setOnEditorActionListener(new EditChangeListenerHandler());

        if (QueryMeasCateg(mCompName).equals("8")) {
            mLayoutDDSpeed.setVisibility(View.GONE);
            mLayoutPumpTest.setVisibility(View.GONE);
            mLayoutDDMeaMode.setVisibility(View.VISIBLE);
            mLayoutddThresholdValue.setVisibility(View.VISIBLE);
        } else {
            mLayoutDDSpeed.setVisibility(View.VISIBLE);
            mLayoutPumpTest.setVisibility(View.VISIBLE);
            mLayoutDDMeaMode.setVisibility(View.GONE);
            mLayoutddThresholdValue.setVisibility(View.GONE);
        }

    }

    private class TogBtnOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tBtnDDCLDY_sscl:
                    setBsLedStatus(mCompName, true);

                    if (isChecked) {
                        threadStopFlag = true;
                        readDDEngThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!Thread.currentThread().isInterrupted() && threadStopFlag) {
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (readDDEngCounter++ > 300 * 2) {
                                        threadStopFlag = false;
                                        readDDEngThread.interrupt();
                                        Message msg = new Message();
                                        msg.what = 2;
                                        mddcldyHandler.sendMessage(msg);
                                        break;
                                    } else {
                                        Message msg = new Message();
                                        msg.what = 3;
                                        mddcldyHandler.sendMessage(msg);
                                    }
                                }
                            }
                        });
                        readDDEngThread.start();
                    } else {
                        readDDEngThread.interrupt();
                        threadStopFlag = false;
                        readDDEngCounter = 0;
                    }
                    break;
                case R.id.togBtnDDPumpCy:
                    if (isChecked) {
                        if (mEditddSpeed.getText().toString().equals("")) {
                            Toast.makeText(getContext(), getString(R.string.please_set_the_titration_speed), Toast.LENGTH_SHORT).show();
                            mTogBtnDDPumpCy.setChecked(false);
                            return;
                        }
                        if (mTogBtnDDPumpPy.isChecked()) {
                            mTogBtnDDPumpPy.setChecked(false);
                        }
                        byte[] temp = new byte[]{(byte) 0x01, (byte) 0x00, Byte.parseByte(mEditddSpeed.getText().toString())};
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_02", S0, 3, 200, temp);
                    } else {
                        byte[] temp = new byte[]{(byte) 0x00, (byte) 0x00, 0};
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_02", S0, 3, 200, temp);
                    }
                    break;
                case R.id.togBtnDDPumpPy:
                    if (isChecked) {
                        if (mEditddSpeed.getText().toString().equals("")) {
                            Toast.makeText(getContext(), getString(R.string.please_set_the_titration_speed), Toast.LENGTH_SHORT).show();
                            mTogBtnDDPumpPy.setChecked(false);
                            return;
                        }
                        if (mTogBtnDDPumpCy.isChecked()) {
                            mTogBtnDDPumpCy.setChecked(false);
                        }
                        byte[] temp2 = new byte[]{(byte) 0x01, (byte) 0x01, Byte.parseByte(mEditddSpeed.getText().toString())};
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_02", S0, 3, 200, temp2);
                    } else {
                        byte[] temp2 = new byte[]{(byte) 0x00, (byte) 0x01, 0};
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_02", S0, 3, 200, temp2);
                    }
                    break;
                case R.id.togBtn_meaMode:
                    byte[] sByte = copybyte(toByteArray(Integer.parseInt(String.valueOf((mTogBtnMeaMode.isChecked() ? 0x01 : 0x00))), 4));
                    SendManager.SendCmd(mCompName + "_" + "设置滴定终点判定方式_06_220", S0, 3, 100, sByte);
                    break;

            }
        }
    }

    private class SpClickListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {

                case R.id.spDDLedGain:
                    String[] strChannel = getResources().getStringArray(R.array.Gain);
                    if (position != 0 && position < strChannel.length) {
                        String[] strSelectChannel = strChannel[position].split("_");

                        byte[] cybszByte = copybyte(toByteArray(Integer.parseInt(strSelectChannel[1]), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置LED增益_06_66", S0, 3, 100, cybszByte);
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edit_meaValue: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 20000, 500, 136, "设置滴定液计数阈值");
                    }
                    break;
                    case R.id.edit_endValue: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 29, 1000, 80, 137, "设置终点判断阈值");
                    }
                    break;
                    case R.id.edit_ddSpeed: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 21, 3, 128, "设置滴定转速");
                    }
                    break;
                    case R.id.edit_fullSpeed: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 5000, 8, 129, "设置填充转速");
                    }
                    break;
                    case R.id.edit_endPoint: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 11, 5, 130, "设置终点后执行滴数");
                    }
                    break;
                    case R.id.edit_adjustSec: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 201, 60, 131, "设置校准等待时间");
                    }
                    break;
                    case R.id.edit_ddVolumeThresholdVal: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 1500, 1000, 156, "设置ORP滴定判断阈值");
                    }
                    break;
                    case R.id.editCatalystThresholdValue:
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 40000, 8500, 250, "设置催化剂缺液阈值");
                        break;
                    case R.id.editOxidizingAgentThresholdValue:
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 40000, 1000, 251, "设置氧化剂缺液阈值");
                        break;
                    case R.id.editReductantThresholdValue:
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 40000, 8500, 252, "设置还原剂阈值");
                        break;
                }
                syncList();
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


    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setBsLedStatus(mCompName, false);
            mCallbacks.onListSelected(v);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mddcldyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1: {
                    if (tvTempCoef != null) {
                        tvTempCoef.setText((getCmds(mCompName).getCmd(0).getValue() == null ? "" : FloatStringFormatInput(getCmds(mCompName).getCmd(0).getValue().toString(), 3)) + " / "
                                + (getCmds(mCompName).getCmd(1).getValue() == null ? "" : FloatStringFormatInput(getCmds(mCompName).getCmd(1).getValue().toString(), 3)));
                        saveOperationLogMsg(mCompName, "标定系数" + "k" + getCmds(mCompName).getCmd(0).getValue().toString() + "b" + getCmds(mCompName).getCmd(1).getValue().toString(), ErrorLog.msgType.操作_信息);
                    }
                }
                break;
                case 2:
                    togBtnReadDdData.setChecked(false);
                    break;
                case 3:
                    tvDDEng.setText(getCmds(mCompName).getCmd(59).getValue() == null ? "" : getCmds(mCompName).getCmd(59).getValue().toString());
                    tvORPEng.setText(getCmds(mCompName).getCmd(64).getValue() == null ? "" : getCmds(mCompName).getCmd(64).getValue().toString());
                    break;
                case 5500: {
                    if (mSpDDLedGain != null) {
                        if ((Integer.parseInt(getCmds(mCompName).getCmd(5500).getValue().toString()) > 4)
                                || (Integer.parseInt(getCmds(mCompName).getCmd(5500).getValue().toString()) < 1)) {
                            mSpDDLedGain.setSelection(0);
                            break;
                        }
                        saveOperationLogMsg(mCompName, "增益值" + getCmds(mCompName).getCmd(5500).getValue().toString(), ErrorLog.msgType.操作_信息);
                        mSpDDLedGain.setSelection(Integer.parseInt(getCmds(mCompName).getCmd(5500).getValue() == null ? "0" : getCmds(mCompName).getCmd(5500).getValue().toString()));
                    }
                }
                break;
                case 5711:
                    mTogBtnMeaMode.setChecked((getCmds(mCompName).getCmd(5711).getValue() != null && (!getCmds(mCompName).getCmd(5711).getValue().toString().equals("0"))));
                    break;
                case 5720:
                    mEditddSpeed.setText(getCmds(mCompName).getCmd(5720).getValue() == null ? "" : getCmds(mCompName).getCmd(5720).getValue().toString());
                    mEditFullSpeed.setText(getCmds(mCompName).getCmd(5721).getValue() == null ? "" : getCmds(mCompName).getCmd(5721).getValue().toString());
                    mEditEndPoint.setText(getCmds(mCompName).getCmd(5722).getValue() == null ? "" : getCmds(mCompName).getCmd(5722).getValue().toString());
                    mEditCollectNum.setText(getCmds(mCompName).getCmd(5723).getValue() == null ? "" : getCmds(mCompName).getCmd(5723).getValue().toString());
                    mEditMeaValue.setText(getCmds(mCompName).getCmd(5728).getValue() == null ? "" : getCmds(mCompName).getCmd(5728).getValue().toString());
                    meditEndValue.setText(getCmds(mCompName).getCmd(5729).getValue() == null ? "" : getCmds(mCompName).getCmd(5729).getValue().toString());
                    mEditddVolumeThresholdVal.setText(getCmds(mCompName).getCmd(5730).getValue() == null ? "" : getCmds(mCompName).getCmd(5730).getValue().toString());
                    break;
                case 9300:
                    mEditCatalystThresholdValue.setText(getCmds(mCompName).getCmd(9300).getValue() == null ? "" : getCmds(mCompName).getCmd(9300).getValue().toString());
                    mEditOxidizingAgentThresholdValue.setText(getCmds(mCompName).getCmd(9301).getValue() == null ? "" : getCmds(mCompName).getCmd(9301).getValue().toString());
                    mEditReductantThresholdValueValue.setText(getCmds(mCompName).getCmd(9302).getValue() == null ? "" : getCmds(mCompName).getCmd(9302).getValue().toString());
                    break;
            }
        }
    };
}
