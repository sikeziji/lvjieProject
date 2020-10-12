package com.yzlm.cyl.cfragment.Dialog.Component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus2;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.floatToBytes;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;


/*
 * Created by zwj 2018年9月28日15:50:38
 * 寻峰参数界面
 */

public class Dialog_SpecPeak extends ZDialogFragment {

    private static EditText mEditSpan1;
    private static EditText mEditSpan2;
    private static EditText mEditMinCoc;
    private static EditText mEditWaterCoc;

    private View v;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_specpeak;
    }

    @Override
    protected void InitUI(View view) {
        v = view;

        byte[] arrayOfByte3 = DataUtil.shortToByte((short) 9100);
        DataUtil.reverse(arrayOfByte3);
        SendManager.SendCmd(mCompName + "_查寻峰参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 4);

        mEditSpan1 = (EditText) view.findViewById(R.id.editS1);
        mEditSpan1.setOnFocusChangeListener(new editTextFocusChange());
        mEditSpan1.setOnEditorActionListener(new editTextFocusChange());

        mEditSpan2 = (EditText) view.findViewById(R.id.editS2);
        mEditSpan2.setOnFocusChangeListener(new editTextFocusChange());
        mEditSpan2.setOnEditorActionListener(new editTextFocusChange());

        mEditMinCoc = (EditText) view.findViewById(R.id.editC);
        mEditMinCoc.setOnFocusChangeListener(new editTextFocusChange());
        mEditMinCoc.setOnEditorActionListener(new editTextFocusChange());

        mEditWaterCoc = (EditText) view.findViewById(R.id.editC1);
        mEditWaterCoc.setOnFocusChangeListener(new editTextFocusChange());
        mEditWaterCoc.setOnEditorActionListener(new editTextFocusChange());

        Button btn_close = (Button) view.findViewById(R.id.btnClose);
        btn_close.setOnClickListener(new btnCloseClick());

        Button btnStartSpecCal = (Button) view.findViewById(R.id.btnStartSpecCal);
        btnStartSpecCal.setOnClickListener(new btnSpecCalClick());
    }

    @Override
    protected boolean setCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected String getTitleName() {
        return null;
    }

    @Override
    protected String getPositiveButtonName() {
        return null;
    }

    @Override
    protected String getNegativeButtonName() {
        return null;
    }

    @Override
    protected void PositiveButtonListener() {

    }

    @Override
    protected void NegativeButtonListener() {

    }

    private void syncList() {

        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 2);
            v.clearFocus();
        }
    }

    @Override
    protected void dialogCancel() {

    }


    private class editTextFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.editC:
                        if (!editTextInputStatus2((EditText) v, 0.0, 5.0)) {
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 5 ] " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            ((EditText) v).setText("0.1");
                        } else {
                            String strValue = editDataFormat(((EditText) v).getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            ((EditText) v).setText(strValue);
                        }
                        saveOperationLogMsg(mCompName, "设置△C值_" + ((EditText) v).getText().toString(), ErrorLog.msgType.操作_信息);
                        byte[] sByteValueC = copybyte(floatToBytes(Float.parseFloat(((EditText) v).getText().toString())));
                        SendManager.SendCmd(mCompName + "_" + "设置△C值_06_153", S0, 3, 100, sByteValueC);
                        break;
                    case R.id.editC1:
                        if (!editTextInputStatus2((EditText) v, 0.0, 100.0)) {
                            ((EditText) v).setText("2.5");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 100 ] " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                        } else {
                            String strValue = editDataFormat(((EditText) v).getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            ((EditText) v).setText(strValue);
                        }
                        saveOperationLogMsg(mCompName, "设置水样浓度值_" + ((EditText) v).getText().toString(), ErrorLog.msgType.操作_信息);
                        byte[] sByteValueWC = copybyte(floatToBytes(Float.parseFloat(((EditText) v).getText().toString())));
                        SendManager.SendCmd(mCompName + "_" + "设置△C值_06_154", S0, 3, 100, sByteValueWC);
                        break;
                    case R.id.editS1:
                        if (!editTextInputStatus2((EditText) v, 0.0, 100.0)) {
                            ((EditText) v).setText("1");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 100 ] " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                        } else {
                            String strValue = editDataFormat(((EditText) v).getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            ((EditText) v).setText(strValue);
                        }
                        saveOperationLogMsg(mCompName, "设置标样1浓度值_" + ((EditText) v).getText().toString(), ErrorLog.msgType.操作_信息);
                        byte[] sByteValueS1 = copybyte(floatToBytes(Float.parseFloat(((EditText) v).getText().toString())));
                        SendManager.SendCmd(mCompName + "_" + "设置标样1浓度值_06_151", S0, 3, 100, sByteValueS1);
                        break;
                    case R.id.editS2:
                        if (!editTextInputStatus2((EditText) v, 0.0, 100.0)) {
                            ((EditText) v).setText("5");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 100 ] " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                        } else {
                            String strValue = editDataFormat(((EditText) v).getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            ((EditText) v).setText(strValue);
                        }
                        saveOperationLogMsg(mCompName, "设置标样2浓度值_" + ((EditText) v).getText().toString(), ErrorLog.msgType.操作_信息);
                        byte[] sByteValueS2 = copybyte(floatToBytes(Float.parseFloat(((EditText) v).getText().toString())));
                        SendManager.SendCmd(mCompName + "_" + "设置标样2浓度值_06_152", S0, 3, 100, sByteValueS2);
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


    private class btnCloseClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();
        }
    }

    private class btnSpecCalClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(1, "Dialog_SpecPeak", mCompName + "_" + getString(R.string.JZ) + "_" + getString(R.string.SDZY));
            dialog.dismiss();
        }
    }


    @SuppressLint("HandlerLeak")
    public static Handler mSpecPeakHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 9100:
                    if (mEditSpan1 != null) {
                        mEditSpan1.setText(getCmds(mCompName).getCmd(9100).getValue() == null ? "" : getCmds(mCompName).getCmd(9100).getValue().toString());
                        mEditSpan2.setText(getCmds(mCompName).getCmd(9101).getValue() == null ? "" : getCmds(mCompName).getCmd(9101).getValue().toString());
                        mEditMinCoc.setText(getCmds(mCompName).getCmd(9102).getValue() == null ? "" : getCmds(mCompName).getCmd(9102).getValue().toString());
                        mEditWaterCoc.setText(getCmds(mCompName).getCmd(9103).getValue() == null ? "" : getCmds(mCompName).getCmd(9103).getValue().toString());
                    }
                    break;
            }
        }
    };

}
