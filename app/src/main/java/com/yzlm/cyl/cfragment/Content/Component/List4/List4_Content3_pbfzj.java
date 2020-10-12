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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus4;
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
import static com.yzlm.cyl.cfragment.Global.getValve;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.BinaryToHexbytes;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pbfzj extends SubFragment {
    private Callbacks mCallbacks;
    private GridLayout mGL;
    private SeekBar sb;
    private static List4_Content3_pbfzj fragment = null;
    private ToggleButton mBtnCY;
    private ToggleButton mBtnPY;

    private ToggleButton mBtnInjectionPumpDir;
    EditText mEditInjectionPumpInValue;

    private Button mBtnInjectionPumpStart, mBtnInjectionPumpZero, mBtnInjectionPumpReset, mBtn_injectionPump_read_position, mBtn_injectionPump_clockwise, mBtn_injectionPump_anticlockwise;
    private EditText mEditChannel;
    private static TextView mTxtPosition;

    public static List4_Content3_pbfzj newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pbfzj();
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
        return R.layout.list4_content3_pbfzj;
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
            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());

            mGL = v.findViewById(R.id.GL_pbfzj);
            mGL.removeAllViews();
            List<String> valve = getValve(mCompName);
            if (valve != null) {
                for (String item : valve) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    ToggleButton mBtn = inflater.inflate(R.layout.valve_button, null).findViewById(R.id.btn_valve);
                    mGL.addView(mBtn);
                    mBtn.setText(item);
                    mBtn.setTextOn(item);
                    mBtn.setTextOff(item);
                    GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) mBtn.getLayoutParams();
                    layoutParams.setMargins(5, 5, 5, 5);
                    mBtn.setLayoutParams(layoutParams);
                    if (item.equals(getString(R.string.reserve))) {
                        mBtn.setBackgroundResource(R.drawable.valve_d);
                    } else {
                        mBtn.setOnClickListener(new mBtnClick());
                    }
                }
            }

            sb = v.findViewById(R.id.seekBar);
            mBtnCY = v.findViewById(R.id.Btn_pbfzj_cy);
            mBtnPY = v.findViewById(R.id.Btn_pbfzj_py);
            mBtnCY.setOnClickListener(new bfClick());
            mBtnPY.setOnClickListener(new bfClick());

            LinearLayout mLayout_pumpTest = v.findViewById(R.id.layout_pumpTest);
            mLayout_pumpTest.setVisibility(View.VISIBLE);
            if (QueryMeasBoardType(mCompName).equals("2") || QueryMeasCateg(mCompName).equals("4") || QueryMeasCateg(mCompName).equals("7")
                    || QueryMeasCateg(mCompName).equals("8") || QueryMeasCateg(mCompName).equals("9") || QueryMeasCateg(mCompName).equals("12")
                    || QueryMeasCateg(mCompName).equals("13")) {

                mLayout_pumpTest.setVisibility(View.GONE);
            }

            LinearLayout mLayoutInjectionPumpTest = v.findViewById(R.id.layout_injectionPumpTest);
            if (QueryMeasCateg(mCompName).equals("8") || QueryMeasCateg(mCompName).equals("9") || QueryMeasCateg(mCompName).equals("12")) {
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
            } else {
                mLayoutInjectionPumpTest.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Objects.equals(doFlowing.get(mCompName), context.getString(R.string.waiting_for_instructions))) {
                SendManager.SendCmd(mCompName + "_紧急停止" + "_8_07", S0, 3, 1000, null);

                if (QueryMeasCateg(mCompName).equals("3") || QueryMeasCateg(mCompName).equals("8")) {
                    SendManager.SendCmd(mCompName + "_控蠕动泵_8_19", S0, 3, 200, new byte[]{0x01});
                }
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
                        if (!editTextInputStatus4((EditText) v, 0.0, 5)) {
                            ((EditText) v).setText(String.valueOf(5));
                            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " [ " + 0 + " ~ " + 5 + " ] " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
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

    private class mBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                String status = "";
                for (int i = 0; i < 32; i++) {
                    if (i >= mGL.getChildCount()) {
                        status = 0 + status;
                    } else {
                        ToggleButton nowChild = (ToggleButton) mGL.getChildAt(i);
                        String str = nowChild.getText().toString();
                        if (str.equals(getString(R.string.reserve))) {
                            status = "0" + status;
                        } else {
                            status = (nowChild.isChecked() ? "1" : "0") + status;
                        }
                    }
                    if (i != 31 && (i + 1) % 8 == 0) status = "," + status;
                }
                SendManager.SendCmd(mCompName + "_阀位控制_8_01", S0, 3, 200, BinaryToHexbytes(status));
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_SHORT).show();
                ToggleButton tb = (ToggleButton) v;
                if (tb.isChecked()) {
                    tb.setChecked(false);
                }
            }
        }
    }

    private class bfClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                ToggleButton tb = (ToggleButton) v;
                if (v.getId() == R.id.Btn_pbfzj_cy) {
                    if (mBtnPY.isChecked()) {
                        mBtnPY.setChecked(false);
                    }
                } else if (v.getId() == R.id.Btn_pbfzj_py) {
                    if (mBtnCY.isChecked()) {
                        mBtnCY.setChecked(false);
                    }
                }
                byte[] temp = new byte[]{(byte) (tb.isChecked() ? 0x01 : 0x00), (byte) (tb.getText().toString().equals(context.getResources().getString(R.string.infusion)) ? 0x00 : 0x01), (byte) (sb.getProgress() + 1)};
                SendManager.SendCmd(mCompName + "_控蠕动泵_8_02", S0, 3, 200, temp);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                ToggleButton tb = (ToggleButton) v;
                if (tb.isChecked()) {
                    tb.setChecked(false);
                }
            }
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
    public static Handler mddbfzjHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 600:
                    mTxtPosition.setText((getCmds(mCompName).getCmd(600).getValue() == null ? "" : getCmds(mCompName).getCmd(600).getValue().toString()));
                    break;
            }
        }
    };

}
