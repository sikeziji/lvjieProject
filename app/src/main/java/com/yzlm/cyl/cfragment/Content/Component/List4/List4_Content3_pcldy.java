package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.Arrays;

import static com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy.getCalcAFormula;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getCmdsValue;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pcldy extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pcldy fragment = null;

    private static ToggleButton cydl_sscl;
    //private static ToggleButton cydl_bscc;
    private static ToggleButton togBsLight;
    private static ToggleButton togBsLightChannel;

    private static TextView tvCalibCoef;

    private static EditText liquidCheckLow;
    private static EditText liquidCheckHigh;

    private static Spinner mSpLedGain;
    private static String componetName = "";

    private boolean ledTwoChannel = false;

    TextView mTxtreferenceEng;

    public static List4_Content3_pcldy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pcldy();
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
        return R.layout.list4_content3_pcldy;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            componetName = mCompName;
            Global.LockDisplayShowFlag = true;
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());

            Button mBtnClTempStd = (Button) v.findViewById(R.id.Btn_pjldy_gjlw);
            mBtnClTempStd.setOnClickListener(new btnClTempStdClick());

            tvCalibCoef = (TextView) v.findViewById(R.id.tvTemperKB);
            cydl_sscl = (ToggleButton) v.findViewById(R.id.tBtnCLDY_sscl);
            cydl_sscl.setOnCheckedChangeListener(new TogChangeListener());

            /*cydl_bscc = (ToggleButton) v.findViewById(R.id.tBtnCLDY_BS_BSCC);
            cydl_bscc.setOnCheckedChangeListener(new TogChangeListener());*/

            togBsLightChannel = (ToggleButton) v.findViewById(R.id.tBtnBS_LED_2);
            togBsLightChannel.setOnCheckedChangeListener(new TogChangeListener());

            togBsLight = (ToggleButton) v.findViewById(R.id.tBtnCLDY_BS_LED);
            togBsLight.setChecked(getConfigData(componetName, "BS_LED").equals("open"));
            togBsLight.setOnCheckedChangeListener(new TogChangeListener());
            TextView tvTemp = (TextView) v.findViewById(R.id.cldy_temp);
            TextView tvEng = (TextView) v.findViewById(R.id.cldy_eng);

            liquidCheckLow = (EditText) v.findViewById(R.id.eT_djlw);
            liquidCheckLow.setOnFocusChangeListener(new liquidCheckLowChange());
            liquidCheckLow.setOnEditorActionListener(new liquidCheckLowChange());
            liquidCheckHigh = (EditText) v.findViewById(R.id.eT_djlw_fd);
            liquidCheckHigh.setOnFocusChangeListener(new liquidCheckHighChange());
            liquidCheckHigh.setOnEditorActionListener(new liquidCheckHighChange());

            byte[] arrayOfByte = DataUtil.shortToByte((short) 0);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(componetName + "_查温度K" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            mSpLedGain = (Spinner) v.findViewById(R.id.spLedGain);
            InitSpinner(context, mSpLedGain, getResources().getStringArray(R.array.Gain), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpLedGain.setOnItemSelectedListener(new SpClickListener());

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 5500);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(componetName + "_查LED增益" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 5702);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(componetName + "_BS光源开关" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), S0, 3, 200, 1);

            /*byte[] arrayOfByte4 = DataUtil.shortToByte((short) 9601);
            DataUtil.reverse(arrayOfByte4);
            SendManager.SendCmd(componetName + "_BS比色存储开关" + "_3_" + DataUtil.bytesToHexString(arrayOfByte4, 2).replace(" ", ""), S0, 3, 200, 1);*/

            LinearLayout mLayoutBsLedChannel = (LinearLayout) v.findViewById(R.id.layout_bs_led_change);
            // 当LED 设置为 浊度
            String calcARevise = getCalcAFormula(componetName, Byte.valueOf(getConfigData(componetName, "RANGE")));
            if (calcARevise.contains("KZ1")) {
                mLayoutBsLedChannel.setVisibility(View.VISIBLE);
                ledTwoChannel = true;
            } else {
                ledTwoChannel = false;
            }
            mTxtreferenceEng = v.findViewById(R.id.cldy_engReference);
            if (QueryMeasBoardType(componetName).equals("2")) {
                mTxtreferenceEng.setVisibility(View.VISIBLE);
            } else {
                mTxtreferenceEng.setVisibility(View.GONE);
            }

            ParProtection();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + componetName + "]" + e.toString());
        }

    }

    /**
     * 参数保护，
     */
    private void ParProtection() {
        if (getPublicConfigData("LogInName").equals("0") || getPublicConfigData("LogInName").equals("1")) {
            togBsLight.setEnabled(false);
            mSpLedGain.setEnabled(false);
        }
    }

    private class TogChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.tBtnCLDY_BS_LED:

                    if (!cydl_sscl.isChecked()) {
                        setBsLedStatus(componetName, isChecked);
                    }
                    byte[] arrayOfByte3 = DataUtil.shortToByte((short) 5702);
                    DataUtil.reverse(arrayOfByte3);
                    SendManager.SendCmd(componetName + "_BS光源开关" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), S0, 3, 200, 1);
                    updateConfigData(componetName, "BS_LED", isChecked ? "open" : "close");
                    Log.i("BS_LED", componetName + (isChecked ? "open" : "close"));
                    break;
                case R.id.tBtnCLDY_sscl:
                    if (!togBsLight.isChecked()) {
                        setBsLedStatus(componetName, isChecked);
                    }
                    Log.i("SSCJ", componetName);
                    break;
                case R.id.tBtnBS_LED_2:
                    setBSLedChannel(isChecked);
                    break;
                /*case R.id.tBtnCLDY_BS_BSCC:
                    setBsLedSaveStatus(componetName, isChecked);
                    break;*/
            }
            syncList();
        }
    }

    /**
     * 光源切换指令
     *
     * @param isChecked
     */
    private void setBSLedChannel(boolean isChecked) {
        byte[] cybszByte = copybyte(toByteArray(Integer.parseInt((isChecked ? "1" : "0")), 4));
        SendManager.SendCmd(componetName + "_光源切换" + "_8_17", S0, 3, 200, cybszByte);
    }

    /**
     * @param compName  組分名稱
     * @param isChecked LED 是否打開
     */
    public static void setBsLedStatus(String compName, boolean isChecked) {
        byte[] byLed1 = copybyte(toByteArray(isChecked ? 1 : 0, 4));
//        System.out.println("测试 : byte = " + Arrays.toString(byLed1));
        SendManager.SendCmd(compName + "_" + "设置BSLED_06_171", S0, 3, 1000, byLed1);
    }

    /**
     * @param compName  組分名稱
     * @param isChecked LED 是否打開
     */
    public static void setBsLedSaveStatus(String compName, boolean isChecked) {
        byte[] byLed1 = copybyte(toByteArray(isChecked ? 1 : 0, 4));
        SendManager.SendCmd(compName + "_" + "设置BSLED存储_06_335", S0, 3, 1000, byLed1);
    }

    private class SpClickListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {
                case R.id.spLedGain:
                    String[] strChannel = getResources().getStringArray(R.array.Gain);
                    if (position != 0 && position < strChannel.length) {
                        String[] strSelectChannel = strChannel[position].split("_");

                        byte[] cybszByte = copybyte(toByteArray(Integer.parseInt(strSelectChannel[1]), 4));
                        SendManager.SendCmd(componetName + "_" + "设置LED增益_06_66", S0, 3, 100, cybszByte);
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class liquidCheckHighChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String liquidCheckHighVal = liquidCheckHigh.getText().toString().trim();
                try {
                    if (!liquidCheckHighVal.equals("")) {
                        int liquidCheckHighInt = Integer.parseInt(liquidCheckHighVal);
                        syncList();
                        byte[] liquidCheckHighByte = copybyte(toByteArray(liquidCheckHighInt, 4));

                        SendManager.SendCmd(componetName + "_" + "设置液体检测值_06_61", S0, 3, 200, liquidCheckHighByte);
                    } else {
                        Toast.makeText(getActivity(), context.getString(R.string.liquid_detection_value_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {

                    Toast.makeText(getActivity(), context.getString(R.string.liquid_detection_value_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                liquidCheckHigh.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class liquidCheckLowChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String liquidCheckLowVal = liquidCheckLow.getText().toString().trim();
                try {
                    if (!liquidCheckLowVal.equals("")) {
                        int liquidCheckLowValInt = Integer.parseInt(liquidCheckLowVal);
                        syncList();
                        byte[] liquidCheckLowValByte = copybyte(toByteArray(liquidCheckLowValInt, 4));

                        SendManager.SendCmd(componetName + "_" + "设置液体检测值_06_60", S0, 3, 200, liquidCheckLowValByte);
                    } else {
                        Toast.makeText(getActivity(), context.getString(R.string.liquid_detection_value_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getString(R.string.liquid_detection_value_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                liquidCheckLow.clearFocus();
                return true;
            }
            return false;
        }
    }

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class btnClTempStdClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            saveOperationLogMsg(mCompName, "温度标定", ErrorLog.msgType.操作_信息);
            SendManager.SendCmd(componetName + "_温度标定" + "_8_00", S0, 3, 200, null);
        }
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 退出界面
            if (togBsLight != null) {
                //LED 常亮开关不需要的情况
                if (togBsLight.isChecked()) {
                    setBsLedStatus(componetName, true);
                } else {
                    setBsLedStatus(componetName, false);
                }
                cydl_sscl.setChecked(false);
                //cydl_bscc.setChecked(false);
            }
            if (ledTwoChannel) {
                if (togBsLightChannel != null) {
                    togBsLightChannel.setChecked(false);
                    setBSLedChannel(false);
                }
            }
            mCallbacks.onListSelected(v);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mcldyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    if (tvCalibCoef != null) {
                        tvCalibCoef.setText((getCmds(componetName).getCmd(0).getValue() == null ? "" : getCmds(componetName).getCmd(0).getValue()).toString());
                        byte[] arrayOfByte = DataUtil.shortToByte((short) 1);
                        DataUtil.reverse(arrayOfByte);
                        SendManager.SendCmd(componetName + "_查温度B" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 1);
                    }
                }
                break;
                case 1: {
                    if (tvCalibCoef != null) {
                        tvCalibCoef.setText((getCmds(componetName).getCmd(0).getValue() == null ? "" : getCmds(componetName).getCmd(0).getValue()).toString() + " / "
                                + (getCmds(componetName).getCmd(1).getValue() == null ? "" : getCmds(componetName).getCmd(1).getValue()).toString());
                        saveOperationLogMsg(componetName, "标定系数" + "k" + getCmds(componetName).getCmd(0).getValue().toString() + "b" + getCmds(componetName).getCmd(1).getValue().toString(), ErrorLog.msgType.操作_信息);
                        byte[] arrayOfByte = DataUtil.shortToByte((short) 5111);
                        DataUtil.reverse(arrayOfByte);
                        SendManager.SendCmd(componetName + "_查液体检测" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 2);
                    }
                }
                break;
                case 5111: {
                    if (liquidCheckLow != null && liquidCheckHigh != null) {
                        liquidCheckLow.setText(getCmds(componetName).getCmd(5111).getValue() == null ? "" : getCmds(componetName).getCmd(5111).getValue().toString());
                        liquidCheckHigh.setText(getCmds(componetName).getCmd(5112).getValue() == null ? "" : getCmds(componetName).getCmd(5112).getValue().toString());
                        saveOperationLogMsg(componetName, "液体检测值1" + getCmds(componetName).getCmd(5111).getValue().toString(), ErrorLog.msgType.操作_信息);
                        saveOperationLogMsg(componetName, "液体检测值2" + getCmds(componetName).getCmd(5112).getValue().toString(), ErrorLog.msgType.操作_信息);
                    }
                }
                break;
                case 5500: {
                    if (mSpLedGain != null) {
                        if ((Integer.parseInt(getCmds(componetName).getCmd(5500).getValue().toString()) > 4)
                                || (Integer.parseInt(getCmds(componetName).getCmd(5500).getValue().toString()) < 1)) {
                            mSpLedGain.setSelection(0);
                            break;
                        }
                        saveOperationLogMsg(componetName, "增益值" + getCmds(componetName).getCmd(5500).getValue().toString(), ErrorLog.msgType.操作_信息);
                        Log.i("led_Gain", getCmds(componetName).getCmd(5500).getValue().toString());
                        mSpLedGain.setSelection(Integer.parseInt(getCmds(componetName).getCmd(5500).getValue() == null ? "0" : getCmds(componetName).getCmd(5500).getValue().toString()));
                    }
                }
                break;
                case 5702:
                    if (togBsLight != null) {
                        String strSscl = getCmds(componetName).getCmd(5702).getValue() == null ? "" : getCmds(componetName).getCmd(5702).getValue().toString();
                    }
                    break;
                /*case 9601:
                    if (cydl_bscc != null) {
                        cydl_bscc.setChecked(getCmdsValue(componetName, 9601).equals("1"));
                    }
                    break;*/
            }
        }
    };
}
