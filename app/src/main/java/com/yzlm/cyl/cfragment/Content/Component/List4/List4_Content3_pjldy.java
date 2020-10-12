package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.Objects;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.preWinForFragment;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.R.id.tvHLjl;
import static com.yzlm.cyl.cfragment.R.id.tvHLjl2;
import static com.yzlm.cyl.cfragment.R.id.tvHLjl3;
import static com.yzlm.cyl.cfragment.R.id.tvHLjl4;
import static com.yzlm.cyl.cfragment.R.id.tvHLjl5;
import static com.yzlm.cyl.cfragment.R.id.tvHLjl6;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pjldy extends SubFragment {
    private Callbacks mCallbacks;
    private Spinner mSpcssz;
    private Spinner mSpcycs;
    private static EditText mET_djlw;
    private static EditText mET_djlw_fd;
    private static EditText mET_gjlw;
    private static EditText mET_gjlw_fd;
    private static List4_Content3_pjldy fragment = null;

    private static Button mBtnHighMeas;

    private static ToggleButton mBtnPumbLiquid;
    private static ToggleButton mBtnLiquidDischarge;
    private Thread readMeaMeteringThread;
    private Boolean threadStopFlag = false;
    private static TextView txtmeaMetering1;
    private static TextView txtmeaMetering2;
    private static TextView txtmeaMetering3;
    private static TextView txtmeaMetering4;
    private static TextView txtmeaMetering5;
    private static TextView txtmeaMetering6;
    private static TextView txtMeaPoint1;
    private static TextView txtMeaPoint2;
    private static TextView txtMeaPoint3;
    private static TextView txtMeaPoint4;
    private static TextView txtMeaPoint5;
    private static TextView txtMeaPoint6;
    private static int readMeadMeteringCounter;
    private static ToggleButton mTb_pjldy_sscl;
    private int point;

    public static List4_Content3_pjldy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pjldy();
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
        return R.layout.list4_content3_pjldy;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.LockDisplayShowFlag = true;
            preWinForFragment = "";
            if (QueryMeasCateg(mCompName).equals("5") || QueryMeasCateg(mCompName).equals("6")) {
                preWinForFragment = "module";
                Global.LockModuleDisplayShowFlag = true;
            }
            point = Integer.parseInt(getConfigData(mCompName, "YXWS"));
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());
            LinearLayout mLayout1 = (LinearLayout) v.findViewById(R.id.layout_InPumpTest);
            LinearLayout mLayout2 = (LinearLayout) v.findViewById(R.id.layout_InPumpTest2);
            LinearLayout mLayout3 = (LinearLayout) v.findViewById(R.id.layout_dilution_mode);


            if (QueryMeasCateg(mCompName).equals("5")) {

                mLayout1.setVisibility(View.GONE);
                mLayout2.setVisibility(View.GONE);
                mLayout3.setVisibility(View.VISIBLE);
            } else {
                mLayout1.setVisibility(View.VISIBLE);
                mLayout2.setVisibility(View.VISIBLE);
                mLayout3.setVisibility(View.GONE);
            }
            if (QueryMeasCateg(mCompName).equals("6")) {
                mLayout3.setVisibility(View.GONE);
            }

            mSpcssz = (Spinner) v.findViewById(R.id.spcssz);
            InitSpinner(context, mSpcssz, new String[]{"Glass", "Water", "H2SO4"}, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpcssz.setOnItemSelectedListener(new mSpcsszSelect());
            mET_djlw = (EditText) this.v.findViewById(R.id.eT_djlw);
            mET_gjlw = (EditText) this.v.findViewById(R.id.eT_gjlw);
            mET_djlw_fd = (EditText) this.v.findViewById(R.id.eT_djlw_fd);
            mET_gjlw_fd = (EditText) this.v.findViewById(R.id.eT_gjlw_fd);
            mET_djlw.setOnFocusChangeListener(new EditFocusChangeComp());
            mET_djlw.setOnEditorActionListener(new EditFocusChangeComp());
            mET_gjlw.setOnFocusChangeListener(new EditFocusChangeComp());
            mET_gjlw.setOnEditorActionListener(new EditFocusChangeComp());
            mET_djlw_fd.setOnFocusChangeListener(new EditFocusChangeComp());
            mET_djlw_fd.setOnEditorActionListener(new EditFocusChangeComp());
            mET_gjlw_fd.setOnFocusChangeListener(new EditFocusChangeComp());
            mET_gjlw_fd.setOnEditorActionListener(new EditFocusChangeComp());

            Button mBtnSave = (Button) v.findViewById(R.id.Btn_pjldy_save);
            mBtnSave.setOnClickListener(new SaveClick());

            mSpcycs = (Spinner) v.findViewById(R.id.spcycs);
            InitSpinner(context, mSpcycs, new String[]{getString(R.string.zero), getString(R.string.vitriol), getString(R.string.water)}, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpcycs.setOnItemSelectedListener(new mSpcycsSelect());

            mBtnHighMeas = (Button) v.findViewById(R.id.Btn_pjldy_gjlw);
            mBtnHighMeas.setOnClickListener(new btnHighMeasClick());

            mBtnPumbLiquid = (ToggleButton) v.findViewById(R.id.Btn_pjldy_cy);
            mBtnPumbLiquid.setOnClickListener(new btnPumbLiquidClick());

            mBtnLiquidDischarge = (ToggleButton) v.findViewById(R.id.Btn_pjldy_py);
            mBtnLiquidDischarge.setOnClickListener(new btnLiquidDischargeClick());

            mTb_pjldy_sscl = (ToggleButton) v.findViewById(R.id.tBtn_pjldy_sscl);
            mTb_pjldy_sscl.setOnCheckedChangeListener(new OnTogBtnClickListener());

            txtmeaMetering1 = (TextView) v.findViewById(tvHLjl);
            txtmeaMetering2 = (TextView) v.findViewById(tvHLjl2);
            txtmeaMetering3 = (TextView) v.findViewById(tvHLjl3);
            txtmeaMetering4 = (TextView) v.findViewById(tvHLjl4);
            txtmeaMetering5 = (TextView) v.findViewById(tvHLjl5);
            txtmeaMetering6 = (TextView) v.findViewById(tvHLjl6);

            txtMeaPoint1 = (TextView) v.findViewById(R.id.tvLPt1);
            txtMeaPoint2 = (TextView) v.findViewById(R.id.tvLPt2);
            txtMeaPoint3 = (TextView) v.findViewById(R.id.tvLPt3);
            txtMeaPoint4 = (TextView) v.findViewById(R.id.tvHPt1);
            txtMeaPoint5 = (TextView) v.findViewById(R.id.tvHPt2);
            txtMeaPoint6 = (TextView) v.findViewById(R.id.tvHPt3);


            Spinner mSpDilutionMode = (Spinner) v.findViewById(R.id.spDilutionMode);
            InitSpinner(context, mSpDilutionMode, getResources().getStringArray(R.array.dilutionMode), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpDilutionMode.setOnItemSelectedListener(new mSpinnerSelect());
            mSpDilutionMode.setSelection(Integer.parseInt(getConfigData(mCompName, "XS_MODE")) - 1);

            ParProtection();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    /**
     * 参数保护，
     */
    private void ParProtection() {
        if (getPublicConfigData("LogInName").equals("0") || getPublicConfigData("LogInName").equals("1")) {
            mET_djlw.setEnabled(false);
            mET_djlw_fd.setEnabled(false);
            mET_gjlw.setEnabled(false);
            mET_gjlw_fd.setEnabled(false);
        }
    }

    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class EditFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eT_djlw:
                        String djlw = mET_djlw.getText().toString().trim();
                        if (!djlw.equals("")) {
                            mET_djlw.setText(editDataFormat(djlw, point));
                        }
                        break;
                    case R.id.eT_djlw_fd:
                        String djlw_fd = mET_djlw_fd.getText().toString().trim();
                        if (!djlw_fd.equals("")) {
                            mET_djlw_fd.setText(editDataFormat(djlw_fd, point));
                        }
                        break;
                    case R.id.eT_gjlw:
                        String gjlw = mET_gjlw.getText().toString().trim();
                        if (!gjlw.equals("")) {
                            mET_gjlw.setText(editDataFormat(gjlw, point));
                        }
                        break;

                    case R.id.eT_gjlw_fd:
                        String gjlw_fd = mET_gjlw_fd.getText().toString().trim();
                        if (!gjlw_fd.equals("")) {
                            mET_gjlw_fd.setText(editDataFormat(gjlw_fd, point));
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

    private class OnTogBtnClickListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tBtn_pjldy_sscl:
                    if (isChecked) {
                        threadStopFlag = true;
                        readMeadMeteringCounter = 0;
                        saveOperationLogMsg(mCompName, "查计量能量-计量点", ErrorLog.msgType.操作_信息);
                        readMeaMeteringThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!Thread.currentThread().isInterrupted() && threadStopFlag) {
                                    byte[] arrayOfByte = DataUtil.shortToByte((short) 5600);
                                    DataUtil.reverse(arrayOfByte);

                                    SendManager.SendCmd(mCompName + "_查计量能量" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 12);
                                    try {
                                        sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    arrayOfByte = DataUtil.shortToByte((short) 5620);
                                    DataUtil.reverse(arrayOfByte);

                                    SendManager.SendCmd(mCompName + "_查计量点" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 6);
                                    try {
                                        sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if (readMeadMeteringCounter++ > 300 * 2) {
                                        threadStopFlag = false;
                                        readMeaMeteringThread.interrupt();
                                        Message msg = new Message();
                                        msg.what = 1;
                                        mjldyHandler.sendMessage(msg);
                                        break;
                                    }
                                }
                            }
                        });
                        readMeaMeteringThread.start();

                    } else {
                        readMeaMeteringThread.interrupt();
                        threadStopFlag = false;
                        readMeadMeteringCounter = 0;
                    }
                    break;
            }
        }
    }

    private class btnLiquidDischargeClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                if (mBtnLiquidDischarge.isChecked()) {
                    byte[] arrayOfByte = DataUtil.shortToByte((short) 143);
                    DataUtil.reverse(arrayOfByte);
                    SendManager.SendCmd(MainActivity.mCompName + "_" + context.getResources().getString(R.string.drainageTest) + "_8_04", S0, 3, 200, copybyte(arrayOfByte, new byte[]{0x00, 0x00}));
                    saveOperationLogMsg(mCompName, "排液测试", ErrorLog.msgType.操作_信息);
                } else {
                    SendManager.SendCmd(mCompName + "_紧急停止" + "_8_07", S0, 3, 200, null);
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                ToggleButton tb = (ToggleButton) v;
                if (tb.isChecked()) {
                    tb.setChecked(false);
                }
            }
        }
    }

    private class btnPumbLiquidClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                if (mBtnPumbLiquid.isChecked()) {
                    int mSpcycsIndex = (int) mSpcycs.getSelectedItemId();

                    byte valveNum = 5;
                    switch (mSpcycsIndex) {
                        case 0: {
                            valveNum = 5;
                        }
                        break;
                        case 1: {
                            valveNum = 2;
                        }
                        break;
                        case 2: {
                            valveNum = 7;
                        }
                        break;
                        default: {
                            valveNum = 5;
                        }
                        break;
                    }
                    byte measPos = 0x01;
                    if (mBtnHighMeas.getText().toString().equals(getString(R.string.low_dose_a))) {
                        measPos = 0x00;
                    } else {
                        measPos = 0x01;
                    }
                    byte[] arrayOfByte = DataUtil.shortToByte((short) 145);
                    DataUtil.reverse(arrayOfByte);
                    SendManager.SendCmd(MainActivity.mCompName + "_" + context.getResources().getString(R.string.infusionTest) + "_8_04", S0, 3, 200, copybyte(arrayOfByte, new byte[]{valveNum, measPos}));
                    saveOperationLogMsg(mCompName, "抽液测试-" + valveNum, ErrorLog.msgType.操作_信息);
                } else {
                    SendManager.SendCmd(mCompName + "_紧急停止" + "_8_07", S0, 3, 200, null);
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                ToggleButton tb = (ToggleButton) v;
                if (tb.isChecked()) {
                    tb.setChecked(false);
                }
            }
        }
    }

    private class btnHighMeasClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mBtnHighMeas.getText().toString().equals(getString(R.string.high_dose_a))) {
                mBtnHighMeas.setText(getString(R.string.low_dose_a));
            } else {
                mBtnHighMeas.setText(getString(R.string.high_dose_a));
            }
        }
    }

    private class btnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (readMeaMeteringThread != null) {
                readMeaMeteringThread.interrupt();
                threadStopFlag = false;
                readMeadMeteringCounter = 0;
            }
            if (Objects.equals(doFlowing.get(mCompName), context.getString(R.string.waiting_for_instructions))) {
                SendManager.SendCmd(mCompName + "_紧急停止" + "_8_07", S0, 3, 1000, null);
            }
            mCallbacks.onListSelected(v);
        }
    }

    public static Handler mjldyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mTb_pjldy_sscl.setChecked(false);
                    break;
                case 1026: {
                    showHighLowMeteringBit(1026);
                }
                break;
                case 1034: {
                    showHighLowMeteringBit(1034);
                }
                break;
                case 1014: {
                    showHighLowMeteringBit(1014);
                }
                break;
                case 5600:
                case 5620: {
                    showMeteringEnergy();
                }
                break;
            }
        }

        /**
         * 用于显示计量单元选项的高低计量位
         * @param regAddr 不同参数下的高低计量的寄存器起始地址
         */
        void showHighLowMeteringBit(int regAddr) {
            if (mET_djlw != null) {
                mET_djlw.setText((getCmds(mCompName).getCmd(regAddr).getValue() == null ? "" : getCmds(mCompName).getCmd(regAddr).getValue()).toString());
            }
            if (mET_gjlw != null) {
                mET_gjlw.setText((getCmds(mCompName).getCmd(regAddr + 1).getValue() == null ? "" : getCmds(mCompName).getCmd(regAddr + 1).getValue()).toString());
            }
            if (mET_djlw_fd != null) {
                mET_djlw_fd.setText((getCmds(mCompName).getCmd(regAddr + 2).getValue() == null ? "" : getCmds(mCompName).getCmd(regAddr + 2).getValue()).toString());
            }
            if (mET_gjlw_fd != null) {
                mET_gjlw_fd.setText((getCmds(mCompName).getCmd(regAddr + 3).getValue() == null ? "" : getCmds(mCompName).getCmd(regAddr + 3).getValue()).toString());
            }
        }
    };

    private static void showMeteringEnergy() {

        try {
            String[] strMeaMeteringEnergy = new String[6];
            String[] strMeaPoint = new String[6];
            for (int i = 0; i < 6; i++) {
                strMeaMeteringEnergy[i] = "";
                strMeaMeteringEnergy[i] += (getCmds(mCompName).getCmd(5600 + (5 - i)).getValue() == null ? "" : getCmds(mCompName).getCmd(5600 + (5 - i)).getValue()).toString() + "   /   " + (getCmds(mCompName).getCmd(5600 + (6 + i)).getValue() == null ? "" : getCmds(mCompName).getCmd(5600 + (6 + i)).getValue()).toString();
            }
            txtmeaMetering1.setText(strMeaMeteringEnergy[0]);
            txtmeaMetering2.setText(strMeaMeteringEnergy[1]);
            txtmeaMetering3.setText(strMeaMeteringEnergy[2]);
            txtmeaMetering4.setText(strMeaMeteringEnergy[3]);
            txtmeaMetering5.setText(strMeaMeteringEnergy[4]);
            txtmeaMetering6.setText(strMeaMeteringEnergy[5]);

            for (int i = 0; i < 6; i++) {
                strMeaPoint[i] = (getCmds(mCompName).getCmd(5620 + i).getValue() == null ? "" : getCmds(mCompName).getCmd(5620 + i).getValue()).toString();
            }

            txtMeaPoint1.setText(strMeaPoint[0]);
            txtMeaPoint2.setText(strMeaPoint[1]);
            txtMeaPoint3.setText(strMeaPoint[2]);

            txtMeaPoint4.setText(strMeaPoint[3]);
            txtMeaPoint5.setText(strMeaPoint[4]);
            txtMeaPoint6.setText(strMeaPoint[5]);
        } catch (Exception e) {

        }
    }

    private class mSpcsszSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            RealTimeStatusThread.suspend();
            switch (position) {
                case 0: {

                    clearEditText();
                    byte[] arrayOfByte = DataUtil.shortToByte((short) 1026);
                    DataUtil.reverse(arrayOfByte);
                    SendManager.SendCmd(mCompName + "_查Glass" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 4);

                }
                break;
                case 1: {
                    clearEditText();
                    byte[] arrayOfByte = DataUtil.shortToByte((short) 1034);
                    DataUtil.reverse(arrayOfByte);
                    SendManager.SendCmd(mCompName + "_查Water" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 4);
                }
                break;
                case 2: {
                    clearEditText();
                    byte[] arrayOfByte = DataUtil.shortToByte((short) 1014);
                    DataUtil.reverse(arrayOfByte);
                    SendManager.SendCmd(mCompName + "_查H2SO4" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 4);
                }
                break;
            }
            RealTimeStatusThread.resume();
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        /* 清除计量单元的高低计量位的输入框内容（WL）*/
        private void clearEditText() {
            mET_djlw.setText("");
            mET_gjlw.setText("");
            mET_djlw_fd.setText("");
            mET_gjlw_fd.setText("");
        }
    }

    private class mSpcycsSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class mSpinnerSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.spDilutionMode:
                    if (!getConfigData(mCompName, "XS_MODE").equals(String.valueOf(position + 1))) {
                        saveOperationLogDataModifyMsg(mCompName, "XS_MODE", String.valueOf(position + 1), "稀释模式", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "XS_MODE", String.valueOf(position + 1));
                    }
                    break;
            }
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class SaveClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            int mSpcsszIndex = (int) mSpcssz.getSelectedItemId();
            String djlw = mET_djlw.getText().toString().trim();
            String gjlw = mET_gjlw.getText().toString().trim();
            String djlw_fd = mET_djlw_fd.getText().toString().trim();
            String gjlw_fd = mET_gjlw_fd.getText().toString().trim();

            String valveNum = "";
            String regAddrIndex = "";
            switch (mSpcsszIndex) {
                case 0: {
                    valveNum = "5";
                    regAddrIndex = "1";
                }
                break;
                case 1: {
                    valveNum = "7";
                    regAddrIndex = "2";
                }
                break;
                case 2: {
                    valveNum = "2";
                    regAddrIndex = "3";
                }
                break;
                default: {
                    valveNum = "5";
                    regAddrIndex = "1";
                }
                break;
            }
            if (!djlw.equals("") && !gjlw.equals("") && !djlw_fd.equals("") && !gjlw_fd.equals("")) {
                byte[] jlwByte = copybyte(toByteArray(Integer.parseInt(djlw), 4), toByteArray(Integer.parseInt(gjlw), 4),
                        toByteArray(Integer.parseInt(djlw_fd), 4), toByteArray(Integer.parseInt(gjlw_fd), 4));

                saveOperationLogMsg(mCompName, "设置" + valveNum + "号计量位:" + djlw + "/" + djlw_fd + "/" + gjlw + "/" + gjlw_fd, ErrorLog.msgType.操作_信息);
                SendManager.SendCmd(MainActivity.mCompName + "_" + valveNum + "号阀计量_06_" + regAddrIndex, S0, 3, 200, jlwByte);
            } else {
                Toast.makeText(getActivity(), getString(R.string.set_meter_empty), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
