package com.yzlm.cyl.cfragment.Dialog.Component;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;

public class Dialog_RangeGlucoseSetting extends ZDialogFragment {
    private EditText mEdGluA1;
    private EditText mEdGluB1;
    private EditText mEdGlucoseCorrect1;
    private EditText mEdGluA2;
    private EditText mEdGluB2;
    private EditText mEdGlucoseCorrect2;
    private ToggleButton mBtnGluOpen1;
    private ToggleButton mBtnGluOpen2;
    private View v;

    @Override
    protected void dialogCancel() {

    }

    @Override
    protected int getDialogView() {
        return R.layout.dialog_range_glucose_setting;
    }

    @Override
    protected void InitUI(View view) {
        v = view;

        mBtnGluOpen1 = v.findViewById(R.id.tBtn_gul1);
        mBtnGluOpen1.setChecked(getConfigData(mCompName, "GlucoseSwitch1").equals("open"));
        mBtnGluOpen1.setOnClickListener(new bGluClick());
        mBtnGluOpen1.setOnClickListener(new bGluClick());

        mBtnGluOpen2 = v.findViewById(R.id.tBtn_gul2);
        mBtnGluOpen2.setChecked(getConfigData(mCompName, "GlucoseSwitch2").equals("open"));
        mBtnGluOpen2.setOnClickListener(new bGlu2Click());
        mBtnGluOpen2.setOnClickListener(new bGlu2Click());

        mEdGluA1 = (EditText) view.findViewById(R.id.edGluA1);
        mEdGluA1.setText(getConfigData(mCompName, "GlucoseA1"));
        mEdGluA1.setOnFocusChangeListener(new EditTextFocusChange());
        mEdGluA1.setOnEditorActionListener(new EditTextFocusChange());

        mEdGluB1 = (EditText) view.findViewById(R.id.edGluB1);
        mEdGluB1.setText(getConfigData(mCompName, "GlucoseB1"));
        mEdGluB1.setOnFocusChangeListener(new EditTextFocusChange());
        mEdGluB1.setOnEditorActionListener(new EditTextFocusChange());

        mEdGlucoseCorrect1 = (EditText) view.findViewById(R.id.edGluCorrect1);
        mEdGlucoseCorrect1.setText(getConfigData(mCompName, "GlucoseCorrect1"));
        mEdGlucoseCorrect1.setOnFocusChangeListener(new EditTextFocusChange());
        mEdGlucoseCorrect1.setOnEditorActionListener(new EditTextFocusChange());

        mEdGluA2 = (EditText) view.findViewById(R.id.edGluA2);
        mEdGluA2.setText(getConfigData(mCompName, "GlucoseA2"));
        mEdGluA2.setOnFocusChangeListener(new EditTextFocusChange());
        mEdGluA2.setOnEditorActionListener(new EditTextFocusChange());

        mEdGluB2 = (EditText) view.findViewById(R.id.edGluB2);
        mEdGluB2.setText(getConfigData(mCompName, "GlucoseB2"));
        mEdGluB2.setOnFocusChangeListener(new EditTextFocusChange());
        mEdGluB2.setOnEditorActionListener(new EditTextFocusChange());

        mEdGlucoseCorrect2 = (EditText) view.findViewById(R.id.edGluCorrect2);
        mEdGlucoseCorrect2.setText(getConfigData(mCompName, "GlucoseCorrect2"));
        mEdGlucoseCorrect2.setOnFocusChangeListener(new EditTextFocusChange());
        mEdGlucoseCorrect2.setOnEditorActionListener(new EditTextFocusChange());

        Button btn_close = (Button) view.findViewById(R.id.btnDialogKBClose);
        btn_close.setOnClickListener(new btnCloseClick());
    }

    private class btnCloseClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();
        }
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

    private class EditTextFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                EditText editText = null;
                String strValue = "";
                String str = "";
                String strCon = "";
                switch (v.getId()) {
                    case R.id.edGluA1:
                        strValue = mEdGluA1.getText().toString().trim();
                        editText = mEdGluA1;
                        str = "电极常数A";
                        strCon = "GlucoseA1";
                        break;
                    case R.id.edGluB1:
                        strValue = mEdGluB1.getText().toString().trim();
                        editText = mEdGluB1;
                        str = "浓度常数B";
                        strCon = "GlucoseB1";
                        break;
                    case R.id.edGluCorrect1:
                        strValue = mEdGlucoseCorrect1.getText().toString().trim();
                        editText = mEdGlucoseCorrect1;
                        str = "浓度常数C1";
                        strCon = "GlucoseCorrect1";
                        break;
                    case R.id.edGluA2:
                        strValue = mEdGluA2.getText().toString().trim();
                        editText = mEdGluA2;
                        str = "浓度常数A2";
                        strCon = "GlucoseA2";
                        break;
                    case R.id.edGluB2:
                        strValue = mEdGluB2.getText().toString().trim();
                        editText = mEdGluB2;
                        str = "浓度常数B2";
                        strCon = "GlucoseB2";
                        break;
                    case R.id.edGluCorrect2:
                        strValue = mEdGlucoseCorrect2.getText().toString().trim();
                        editText = mEdGlucoseCorrect2;
                        str = "浓度常数C2";
                        strCon = "GlucoseCorrect2";
                        break;
                    default:
                        return;
                }
                try {
                    if (!strValue.equals("")) {
                        double val1 = Double.parseDouble(strValue);
                        if (val1 < -9999 || val1 > 9999) {
                            Toast.makeText(getActivity(), "设置值超范围(-9999 ~ 9999) !", Toast.LENGTH_SHORT).show();
                            editText.setText("1");
                            strValue = "1";
                        }
                        strValue = editDataFormat(strValue, Integer.parseInt(getConfigData(mCompName, "YXWS")));
                        saveOperationLogMsg(mCompName, "输入" + str + "_" + strValue, ErrorLog.msgType.操作_信息);

                        editText.setText(strValue);

                        updateConfigData(mCompName, strCon, strValue);
                    } else {
                        Toast.makeText(getActivity(), "设置值不能为空", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "设置值有误", Toast.LENGTH_SHORT).show();
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

    private class bGluClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mBtnGluOpen1.isChecked()) {
                updateConfigData(mCompName, "GlucoseSwitch1", "open");
            } else {
                updateConfigData(mCompName, "GlucoseSwitch1", "close");
            }
        }
    }

    private class bGlu2Click implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mBtnGluOpen2.isChecked()) {
                updateConfigData(mCompName, "GlucoseSwitch2", "open");
            } else {
                updateConfigData(mCompName, "GlucoseSwitch2", "close");
            }
        }
    }

    private void syncList() {
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 2);
            v.clearFocus();
        }
    }
}
