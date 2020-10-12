package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.List;
import java.util.Objects;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus4;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setComponentEditIntData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.preWinForFragment;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.floatToBytes;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getCmdsValue;
import static com.yzlm.cyl.cfragment.Global.getValve;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.BinaryToHexbytes;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pzsbsz extends SubFragment {
    private Callbacks mCallbacks;
    private SeekBar sb;
    private static List4_Content3_pzsbsz fragment = null;

    private ToggleButton mBtnInjectionPumpDir;
    private ToggleButton mBtnTJSZ;
    EditText mEditInjectionPumpInValue;

    LinearLayout layZSB_TJZS;

    private Spinner mSpZSB;

    private Button mBtnInjectionPumpStart, mBtnInjectionPumpZero, mBtnInjectionPumpReset, mBtn_injectionPump_read_position, mBtn_injectionPump_clockwise, mBtn_injectionPump_anticlockwise;
    private EditText mEditChannel;
    private static TextView mTxtPosition;

    static EditText mEditQXTJ, mEditRXTJ, mEditJYTJ, mEditTCTJ, mEditHCTJ, mEditSYTJ, mEditFSTJ, mEditYSTJ, mEditDKTJ, mEditHuanCunTJ, mEditdzTJ;

    private LinearLayout layLine3, layLine4;

    private int iPoint = 2;

    public static List4_Content3_pzsbsz newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pzsbsz();
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
        return R.layout.list4_content3_pzsbsz;
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

            preWinForFragment = "";
            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());

            InitWidget();

            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 9800);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(mCompName + "_查生物毒性注射泵参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 16);


        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void InitWidget() {
        mSpZSB = v.findViewById(R.id.spZSB);
        InitSpinner(context, mSpZSB, new String[]{context.getString(R.string.injectionPump) + "1", context.getString(R.string.injectionPump) + "2"}, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
        mSpZSB.setOnItemSelectedListener(new mSpListener());

        sb = v.findViewById(R.id.seekBar);

        LinearLayout mLayoutInjectionPumpTest = v.findViewById(R.id.layout_injectionPumpTest);
        mLayoutInjectionPumpTest.setVisibility(View.VISIBLE);
        mBtnInjectionPumpDir = v.findViewById(R.id.Btn_injectionPumpDir);
        mBtnInjectionPumpDir.setOnClickListener(new injectionPumpClick());

        mEditInjectionPumpInValue = v.findViewById(R.id.edit_injectionValue);
        mEditInjectionPumpInValue.setOnFocusChangeListener(new EditFocusChangeComp());
        mEditInjectionPumpInValue.setOnEditorActionListener(new EditFocusChangeComp());
        mBtnInjectionPumpStart = v.findViewById(R.id.Btn_injectionPump_start);
        mBtnInjectionPumpStart.setOnClickListener(new BtnOnClick());
        mBtnInjectionPumpZero = v.findViewById(R.id.Btn_injectionPump_zero);
        mBtnInjectionPumpZero.setOnClickListener(new BtnOnClick());
        mBtnInjectionPumpReset = v.findViewById(R.id.Btn_injectionPump_reset);
        mBtnInjectionPumpReset.setOnClickListener(new BtnOnClick());

        mBtn_injectionPump_read_position = v.findViewById(R.id.Btn_injectionPump_read_position);
        mBtn_injectionPump_read_position.setOnClickListener(new BtnOnClick());

        mBtn_injectionPump_clockwise = v.findViewById(R.id.Btn_injectionPump_clockwise);
        mBtn_injectionPump_clockwise.setOnClickListener(new BtnOnClick());
        mBtn_injectionPump_anticlockwise = v.findViewById(R.id.Btn_injectionPump_anticlockwise);
        mBtn_injectionPump_anticlockwise.setOnClickListener(new BtnOnClick());
        mEditChannel = v.findViewById(R.id.edit_channel);
        mEditChannel.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditChannel.setOnEditorActionListener(new EditChangeListenerHandler());

        mTxtPosition = v.findViewById(R.id.Txt_position);

        layZSB_TJZS = v.findViewById(R.id.lay_zsb_tjsz);
        layZSB_TJZS.setVisibility(View.GONE);

        mBtnTJSZ = v.findViewById(R.id.tBtnTJSZ);
        mBtnTJSZ.setOnCheckedChangeListener(new ZSBTJSZClick());
        mBtnTJSZ.setChecked(false);

        mEditQXTJ = v.findViewById(R.id.eTqxtj);
        mEditQXTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditQXTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditRXTJ = v.findViewById(R.id.eTrxtj);
        mEditRXTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditRXTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditJYTJ = v.findViewById(R.id.eTjytj);
        mEditJYTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditJYTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTCTJ = v.findViewById(R.id.eTtctj);
        mEditTCTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTCTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditHCTJ = v.findViewById(R.id.eThctj);
        mEditHCTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditHCTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditSYTJ = v.findViewById(R.id.eTsytj);
        mEditSYTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditSYTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditFSTJ = v.findViewById(R.id.eTfstj);
        mEditFSTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditFSTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditYSTJ = v.findViewById(R.id.eTystj);
        mEditYSTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditYSTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditDKTJ = v.findViewById(R.id.eTdktj);
        mEditDKTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditDKTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditHuanCunTJ = v.findViewById(R.id.eThuanccuntj);
        mEditHuanCunTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditHuanCunTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditdzTJ = v.findViewById(R.id.eTdztj);
        mEditdzTJ.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditdzTJ.setOnEditorActionListener(new EditChangeListenerHandler());

        layLine3 = v.findViewById(R.id.lay_line3);
        layLine4 = v.findViewById(R.id.lay_line4);
    }

    private class mSpListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            SendManager.SendCmd(mCompName + "_注射泵切换" + "_8_21", S0, 3, 1000, new byte[]{(byte) (position + 1)});

            if (position == 1) {
                layLine3.setVisibility(View.INVISIBLE);
                layLine4.setVisibility(View.INVISIBLE);
            } else {
                layLine3.setVisibility(View.VISIBLE);
                layLine4.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Objects.equals(doFlowing.get(mCompName), context.getString(R.string.waiting_for_instructions))) {
                SendManager.SendCmd(mCompName + "_紧急停止" + "_8_07", S0, 3, 1000, null);
            }
            mCallbacks.onListSelected(v);
        }
    }

    private class BtnOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Objects.equals(doFlowing.get(mCompName), context.getString(R.string.waiting_for_instructions))) {
                switch (v.getId()) {
                    case R.id.Btn_injectionPump_start:
                        byte[] sByte = copybyte(new byte[]{(byte) ((mBtnInjectionPumpDir).isChecked() ? 0x01 : 0x00)}, floatToBytes(Float.parseFloat(mEditInjectionPumpInValue.getText().toString())));
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_18", S0, 3, 200, sByte);
                        break;
                    case R.id.Btn_injectionPump_zero:
                        byte[] sByte1 = copybyte(new byte[]{(byte) (0x00)});
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_1A", S0, 3, 200, sByte1);
                        break;
                    case R.id.Btn_injectionPump_reset:
                        byte[] sByte2 = copybyte(new byte[]{(byte) (0x01)});
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_1A", S0, 3, 200, sByte2);
                        break;
                    case R.id.Btn_injectionPump_read_position:
                        // 读取注射泵旋转阀位置
                        readInjectionPumpPosition();
                        break;
                    case R.id.Btn_injectionPump_clockwise:
                        byte channel = Byte.parseByte(mEditChannel.getText().toString());
                        byte[] temp = new byte[]{channel, 0};
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_19", S0, 3, 200, temp);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                readInjectionPumpPosition();
                            }
                        }).start();
                        break;
                    case R.id.Btn_injectionPump_anticlockwise:
                        byte channel1 = Byte.parseByte(mEditChannel.getText().toString());
                        byte[] temp1 = new byte[]{channel1, 1};
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_19", S0, 3, 200, temp1);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                readInjectionPumpPosition();
                            }
                        }).start();
                        break;
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readInjectionPumpPosition() {
        byte[] arrayOfByte3 = DataUtil.shortToByte((short) 600);
        DataUtil.reverse(arrayOfByte3);
        SendManager.SendCmd(mCompName + "_查旋转阀位置" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 1);
    }

    private class EditFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edit_injectionValue:
                        if (!editTextInputStatus4((EditText) v, 0.0, 2.5)) {
                            ((EditText) v).setText(String.valueOf(2.5));
                            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " [ " + 0 + " ~ " + 2.5 + " ] " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                        }
                        String strValue = editDataFormat(((EditText) v).getText().toString(), 2);
                        ((EditText) v).setText(strValue);
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


    private class injectionPumpClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Btn_injectionPumpDir:
                    break;
            }
        }
    }

    private class ZSBTJSZClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                layZSB_TJZS.setVisibility(View.VISIBLE);
            } else {
                layZSB_TJZS.setVisibility(View.GONE);
            }
        }
    }

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edit_channel: {
                        setComponentEditIntData(mCompName, (EditText) v, 1, 0, 10, 1, "旋转阀通道");
                    }
                    break;
                    case R.id.eTqxtj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 20001, 5000, 317, "设置清洗体积");
                    }
                    break;
                    case R.id.eThctj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 5001, 2000, 321, "设置回抽体积");
                    }
                    break;
                    case R.id.eTrxtj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 10001, 5000, 318, "设置润洗体积");
                    }
                    break;
                    case R.id.eTsytj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 20001, 500, 322, "设置水样体积");
                    }
                    break;
                    case R.id.eTystj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 5001, 500, 324, "设置盐水体积");
                    }
                    break;
                    case R.id.eTdktj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 5001, 1000, 325, "设置顶空体积");
                    }
                    break;
                    case R.id.eTjytj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 501, 50, 319, "设置菌液体积");
                    }
                    break;
                    case R.id.eTfstj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 1001, 50, 323, "设置复苏体积");
                    }
                    break;
                    case R.id.eTtctj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 10001, 5000, 320, "设置填充体积");
                    }
                    break;
                    case R.id.eThuanccuntj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 10001, 1500, 331, "设置缓冲体积");
                    }
                    break;
                    case R.id.eTdztj: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 20001, 1000, 334, "设置对照体积");
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

    @SuppressLint("HandlerLeak")
    public static Handler mddzsbszHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 600:
                    mTxtPosition.setText((getCmds(mCompName).getCmd(600).getValue() == null ? "" : getCmds(mCompName).getCmd(600).getValue().toString()));
                    break;
                case 9800:
                    mEditQXTJ.setText(getCmdsValue(mCompName, 9800));
                    mEditRXTJ.setText(getCmdsValue(mCompName, 9801));
                    mEditJYTJ.setText(getCmdsValue(mCompName, 9802));
                    mEditTCTJ.setText(getCmdsValue(mCompName, 9803));
                    mEditHCTJ.setText(getCmdsValue(mCompName, 9804));
                    mEditSYTJ.setText(getCmdsValue(mCompName, 9805));
                    mEditFSTJ.setText(getCmdsValue(mCompName, 9806));
                    mEditYSTJ.setText(getCmdsValue(mCompName, 9807));
                    mEditDKTJ.setText(getCmdsValue(mCompName, 9808));
                    mEditHuanCunTJ.setText(getCmdsValue(mCompName, 9814));
                    mEditdzTJ.setText(getCmdsValue(mCompName, 9815));
                    break;
            }
        }
    };

}
