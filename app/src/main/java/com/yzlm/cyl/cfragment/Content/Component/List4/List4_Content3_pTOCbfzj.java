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
import static com.yzlm.cyl.cfragment.Global.getCmdsValue;
import static com.yzlm.cyl.cfragment.Global.getValve;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.BinaryToHexbytes;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;

/**
 * Created by zwj on 2020/04/17.
 */

public class List4_Content3_pTOCbfzj extends SubFragment {

    private Callbacks mCallbacks;
    private GridLayout mGL;

    private EditText editInjectionValue;
    private ToggleButton togBtnDir;

    private Button mBtnInjectionPumpStart, mBtnInjectionPumpZero, mBtnInjectionPumpReset, mBtn_injectionPump_read_position, btnSwitchValve;
    private EditText mEditChannel;
    private static TextView mTxtPosition;


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
        return R.layout.list4_content3_tocbfzj;
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
            togBtnDir = v.findViewById(R.id.togBtnDir);
            togBtnDir.setOnClickListener(new injectionPumpClick());

            editInjectionValue = v.findViewById(R.id.editInjectionValue);
            editInjectionValue.setOnFocusChangeListener(new EditFocusChangeComp());
            editInjectionValue.setOnEditorActionListener(new EditFocusChangeComp());

            mBtnInjectionPumpStart = v.findViewById(R.id.Btn_injectionPump_start);
            mBtnInjectionPumpStart.setOnClickListener(new BtnOnClick());
            mBtnInjectionPumpZero = v.findViewById(R.id.Btn_injectionPump_zero);
            mBtnInjectionPumpZero.setOnClickListener(new BtnOnClick());
            mBtnInjectionPumpReset = v.findViewById(R.id.Btn_injectionPump_reset);
            mBtnInjectionPumpReset.setOnClickListener(new BtnOnClick());

            mBtn_injectionPump_read_position = v.findViewById(R.id.Btn_injectionPump_read_position);
            mBtn_injectionPump_read_position.setOnClickListener(new BtnOnClick());
            btnSwitchValve = v.findViewById(R.id.btnSwitchValve);
            btnSwitchValve.setOnClickListener(new BtnOnClick());
            mEditChannel = v.findViewById(R.id.edit_channel);
            mEditChannel.setOnFocusChangeListener(new EditChangeListenerHandler());
            mEditChannel.setOnEditorActionListener(new EditChangeListenerHandler());
            mTxtPosition = v.findViewById(R.id.Txt_position);

            // 默认切换到左
            byte[] sByte1 = copybyte(new byte[]{(byte) (0x00)});
            SendManager.SendCmd(mCompName + "_旋转阀方向_8_1C", S0, 3, 200, sByte1);
            readInjectionPumpPosition();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Objects.equals(doFlowing.get(mCompName), context.getString(R.string.waiting_for_instructions))) {
                SendManager.SendCmd(mCompName + "_紧急停止" + "_8_07", S0, 3, 1000, null);
                SendManager.SendCmd(mCompName + "_控蠕动泵_8_19", S0, 3, 200, new byte[]{0x01});
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
                        byte[] sByte = copybyte(new byte[]{(byte) ((togBtnDir).isChecked() ? 0x01 : 0x00)}, floatToBytes(Float.parseFloat(editInjectionValue.getText().toString())));
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
                    case R.id.btnSwitchValve:
                        int iPosition = Integer.parseInt(mEditChannel.getText().toString());
                        byte[] sByte3 = copybyte(new byte[]{(byte) (iPosition)});
                        SendManager.SendCmd(mCompName + "_控蠕动泵_8_1D", S0, 3, 200, sByte3);
                        readInjectionPumpPosition();
                        break;
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readInjectionPumpPosition() {
        byte[] arrayOfByte3 = DataUtil.shortToByte((short) 602);
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


    private class injectionPumpClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.togBtnDir:
                    if (((TextView) v).getText().toString().equals(getResources().getString(R.string.left))) {
                        byte[] sByte1 = copybyte(new byte[]{(byte) (0x00)});
                        SendManager.SendCmd(mCompName + "_旋转阀方向_8_1C", S0, 3, 200, sByte1);
                    } else {
                        byte[] sByte1 = copybyte(new byte[]{(byte) (0x01)});
                        SendManager.SendCmd(mCompName + "_旋转阀方向_8_1C", S0, 3, 200, sByte1);
                    }
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
                        setComponentEditIntData(mCompName, (EditText) v, 1, 0, 11, 1, "旋转阀方向");
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
    public static Handler mTOCbfzjHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 602:
                    mTxtPosition.setText(getCmdsValue(mCompName, 602));
                    break;
            }
        }
    };

}
