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

import java.text.DecimalFormat;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandPackaging.GetForwardCmd;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.GDJCB;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.WDFYMK;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getCmdsValue;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pswdxcldy extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pswdxcldy fragment = null;

    private static ToggleButton cydl_sscl;

    private static Spinner mSpLedGain;
    private static String componetName = "";

    private static EditText mEditReaction;
    private static EditText mEditCooling;

    TextView mTxtreferenceEng;

    static TextView mTv_referenceIntensity;
    static TextView mTv_absorbedLightIntensity;

    static EditText mEditReference_intensity, mEditAbsorbed_light_intensity;

    private int iPoint = 2;

    private Thread readMeasureEnergyThread;
    private Boolean threadStopFlag = false;
    private static int readMeadMeteringCounter;

    public static List4_Content3_pswdxcldy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pswdxcldy();
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
        return R.layout.list4_content3_pswdxcldy;
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

            iPoint = Integer.parseInt(getConfigData(mCompName, "YXWS"));

            InitWidget();

            // 发送指令修改
            byte[] sByte = copybyte(toByteArray(1, 4));
            SendManager.SendCmd(mCompName + "_设置读取温度标志位关" + "_06_" + 329, S0, 3, 100, sByte);
            Thread.sleep(200);
            byte[] arrayOfByte = DataUtil.shortToByte((short) 0);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(componetName + "_查温度K" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 1);
            Thread.sleep(200);
            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 5500);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(componetName + "_查LED增益" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 1);
            Thread.sleep(200);
            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 9810);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(mCompName + "_查生物毒性注射泵参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 4);
            Thread.sleep(200);
            /*byte[] cybszByte = new byte[]{0x03, (byte) 0xE8, 0x00, 0x01};
            byte[] Cmd = GetForwardCmd(mCompName, 1, (byte) 4, "", cybszByte);
            if (Cmd != null) {
                SendManager.SendCmd(mCompName + "_设置通道_32_" + 05, S0, 3, 400, Cmd);
            }
            Thread.sleep(200);
            byte[] Cmd2 = GetForwardCmd(mCompName, 2, (byte) 4, "", cybszByte);
            if (Cmd2 != null) {
                SendManager.SendCmd(mCompName + "_设置通道_32_" + 05, S0, 3, 400, Cmd2);
            }*/

            byte[] cybszByte = copybyte(new byte[]{0x03, (byte) 0xEB, 0x00, 0x01});
            byte[] Cmd = GetForwardCmd(componetName, 1, (byte) 4, "", cybszByte);
            if (Cmd != null) {
                SendManager.SendCmd(componetName + "_读取反应模块温度_32_" + WDFYMK + "_1", S0, 3, 400, Cmd);
            }
            Thread.sleep(500);
            byte[] Cmd2 = GetForwardCmd(componetName, 2, (byte) 4, "", cybszByte);
            if (Cmd2 != null) {
                SendManager.SendCmd(componetName + "_读取制冷模块温度_32_" + WDFYMK + "_1", S0, 3, 400, Cmd2);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + componetName + "]" + e.toString());
        } finally {
            //设置读取温度标志位开
            byte[] sByte = copybyte(toByteArray(0, 4));
            SendManager.SendCmd(mCompName + "_设置读取温度标志位开" + "_06_" + 329, S0, 3, 100, sByte);
        }

    }

    private void InitWidget() {
        cydl_sscl = (ToggleButton) v.findViewById(R.id.tBtnCLDY_sscl);
        cydl_sscl.setOnCheckedChangeListener(new TogChangeListener());

        mEditReaction = (EditText) v.findViewById(R.id.edit_reaction_module_temperature);
        mEditReaction.setOnFocusChangeListener(new EditFocusChange());
        mEditReaction.setOnEditorActionListener(new EditFocusChange());

        mEditCooling = (EditText) v.findViewById(R.id.edit_cooling_module_temperature);
        mEditCooling.setOnFocusChangeListener(new EditFocusChange());
        mEditCooling.setOnEditorActionListener(new EditFocusChange());

        mEditReference_intensity = v.findViewById(R.id.edit_reference_intensity);
        mEditReference_intensity.setOnFocusChangeListener(new EditFocusChange());
        mEditReference_intensity.setOnEditorActionListener(new EditFocusChange());

        mEditAbsorbed_light_intensity = v.findViewById(R.id.edit_absorbed_light_intensity);
        mEditAbsorbed_light_intensity.setOnFocusChangeListener(new EditFocusChange());
        mEditAbsorbed_light_intensity.setOnEditorActionListener(new EditFocusChange());

        mTv_referenceIntensity = (TextView) v.findViewById(R.id.cldy_referenceIntensity);
        mTv_absorbedLightIntensity = (TextView) v.findViewById(R.id.cldy_absorbedLightIntensity);

        mSpLedGain = (Spinner) v.findViewById(R.id.spLedGain);
        InitSpinner(context, mSpLedGain, getResources().getStringArray(R.array.Gain), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpLedGain.setOnItemSelectedListener(new SpClickListener());

        mTxtreferenceEng = v.findViewById(R.id.cldy_engReference);

    }


    private class EditFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edit_reaction_module_temperature: {
                        if (!mEditReaction.getText().toString().equals("")) {
                            String str = String.format("%.1f", Float.parseFloat(mEditReaction.getText().toString()));
                            int iReaction = (int) (Float.parseFloat(str) * 10);
                            byte[] cybszByte = copybyte(new byte[]{0x00, 0x00}, intToBytes(iReaction));
                            byte[] Cmd = GetForwardCmd(componetName, 1, (byte) 6, "9999", cybszByte);
                            if (Cmd != null) {
                                SendManager.SendCmd(componetName + "_设置反应模块温度_32_" + WDFYMK, S0, 3, 400, Cmd);
                            }
                        }
                    }
                    break;
                    case R.id.edit_cooling_module_temperature: {
                        if (!mEditCooling.getText().toString().equals("")) {
                            String str = String.format("%.1f", Float.parseFloat(mEditCooling.getText().toString()));
                            int iCooling = (int) (Float.parseFloat(str) * 10);
                            byte[] cybszByte = copybyte(new byte[]{0x00, 0x00}, intToBytes(iCooling));
                            byte[] Cmd = GetForwardCmd(componetName, 2, (byte) 6, "9999", cybszByte);
                            if (Cmd != null) {
                                SendManager.SendCmd(componetName + "_设置制冷模块温度_32_" + WDFYMK, S0, 3, 400, Cmd);
                            }
                        }
                    }
                    break;
                    case R.id.edit_reference_intensity: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 1000, 20000, 10000, 326, "设置参比光强");
                    }
                    break;
                    case R.id.edit_absorbed_light_intensity: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 20000, 65535, 60000, 327, "设置吸收光强");
                    }
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

    public static byte[] intToBytes(int n) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            b[i] = (byte) (n >> (8 - i * 8));
        }
        return b;
    }

    private class TogChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.tBtnCLDY_sscl:
                    if (isChecked) {
                        threadStopFlag = true;
                        readMeadMeteringCounter = 0;
                        saveOperationLogMsg(mCompName, "查测量能量", ErrorLog.msgType.操作_信息);
                        readMeasureEnergyThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!Thread.currentThread().isInterrupted() && threadStopFlag) {
                                    byte[] Cmd = GetForwardCmd(mCompName, 1, (byte) 8, "0", new byte[]{0x01});
                                    if (Cmd != null) {
                                        SendManager.SendCmd(mCompName + "_读取能量值_32_" + GDJCB, S0, 1, 200, Cmd);
                                    }

                                    try {
                                        sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if (readMeadMeteringCounter++ > 300 * 2) {
                                        threadStopFlag = false;
                                        readMeasureEnergyThread.interrupt();
                                        Message msg = new Message();
                                        msg.what = 1;
                                        mswdxcldyHandler.sendMessage(msg);
                                        break;
                                    }
                                }
                            }
                        });
                        readMeasureEnergyThread.start();
                    } else {
                        readMeasureEnergyThread.interrupt();
                        threadStopFlag = false;
                        readMeadMeteringCounter = 0;
                    }
                    break;
            }
            syncList();
        }
    }


    /**
     * @param compName  組分名稱
     * @param isChecked LED 是否打開
     */
    public static void setBsLedStatus(String compName, boolean isChecked) {
        byte[] byLed1 = copybyte(toByteArray(isChecked ? 1 : 0, 4));
        SendManager.SendCmd(compName + "_" + "设置BSLED_06_171", S0, 3, 1000, byLed1);
    }

    private class SpClickListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {
                case R.id.spLedGain:
                    String[] strChannel = getResources().getStringArray(R.array.Gain);
                    if (position != 0 && position < strChannel.length) {
                        String[] strSelectChannel = strChannel[position].split("_");

                        byte[] cybszByte = new byte[]{(byte) (Integer.parseInt(strSelectChannel[1]) - 1), (byte) (Integer.parseInt(strSelectChannel[1]) - 1)};
                        byte[] Cmd = GetForwardCmd(mCompName, 1, (byte) 8, "1", cybszByte);
                        if (Cmd != null) {
                            SendManager.SendCmd(mCompName + "_设置通道_32_" + GDJCB, S0, 3, 400, Cmd);
                        }

                        //设置当前通道倍数
                        byte[] sByte = copybyte(toByteArray((Integer.parseInt(strSelectChannel[1]) - 1), 4));
                        SendManager.SendCmd(mCompName + "_设置当前通道倍数" + "_06_" + 330, S0, 3, 100, sByte);
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
            if (readMeasureEnergyThread != null) {
                readMeasureEnergyThread.interrupt();
                threadStopFlag = false;
                readMeadMeteringCounter = 0;
            }

            // 退出界面
            setBsLedStatus(componetName, false);
            cydl_sscl.setChecked(false);
            mCallbacks.onListSelected(v);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mswdxcldyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    cydl_sscl.setChecked(false);
                    break;
                case 32:
                    String[] str = ((String) msg.obj).split("_");
                    mTv_referenceIntensity.setText(str[0]);
                    mTv_absorbedLightIntensity.setText(str[1]);
                    break;
                case 11:
                    String s1 = String.valueOf((float) msg.obj);
                    mEditReaction.setText(String.valueOf((float) msg.obj));
                    break;
                case 12:
                    String s2 = String.valueOf((float) msg.obj);
                    mEditCooling.setText(String.valueOf((float) msg.obj));
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
                case 9810:
                    mEditReference_intensity.setText(getCmdsValue(mCompName, 9810));
                    mEditAbsorbed_light_intensity.setText(getCmdsValue(mCompName, 9811));
                    mSpLedGain.setSelection(Integer.parseInt(getCmdsValue(mCompName, 9813)) + 1);
                    break;
            }
        }
    };
}
