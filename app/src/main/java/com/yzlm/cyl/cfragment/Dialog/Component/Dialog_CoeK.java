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

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;


public class Dialog_CoeK extends ZDialogFragment {
    private EditText mD_text1;
    private EditText mD_text2;
    private EditText mD_text3;
    private Button btn_dataCoeKSwitch;

    private static String componentName = "";
    private View v;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_coe_k;
    }

    @Override
    protected void InitUI(View view) {
        componentName = mCompName;
        v = view;

        mD_text1 = (EditText) view.findViewById(R.id.edit_range1K);
        mD_text1.setText(getConfigData(componentName, "CoeKRange1"));
        mD_text1.setOnFocusChangeListener(new dataCoeRange1FocusChange());
        mD_text1.setOnEditorActionListener(new dataCoeRange1FocusChange());

        mD_text2 = (EditText) view.findViewById(R.id.edit_range2K);
        mD_text2.setText(getConfigData(componentName, "CoeKRange2"));
        mD_text2.setOnFocusChangeListener(new dataCoeRange2FocusChange());
        mD_text2.setOnEditorActionListener(new dataCoeRange2FocusChange());

        mD_text3 = (EditText) view.findViewById(R.id.edit_range3K);
        mD_text3.setText(getConfigData(componentName, "CoeKRange3"));
        mD_text3.setOnFocusChangeListener(new dataCoeRange3FocusChange());
        mD_text3.setOnEditorActionListener(new dataCoeRange3FocusChange());

        btn_dataCoeKSwitch = (Button) view.findViewById(R.id.btnCoeSwitch);
        String switchFlag = getConfigData(componentName, "rangeCoeOpen");
        if (switchFlag.equals("open")) {
            btn_dataCoeKSwitch.setBackgroundResource(R.drawable.dialog312btn_s);
            btn_dataCoeKSwitch.setTag("open");
        } else {
            btn_dataCoeKSwitch.setBackgroundResource(R.drawable.dialog312btn_n);
            btn_dataCoeKSwitch.setTag("close");
        }
        btn_dataCoeKSwitch.setOnClickListener(new btnClick());

        Button btn_close = (Button) view.findViewById(R.id.btnClose);
        btn_close.setOnClickListener(new btnCloseClick());
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

    private class dataCoeRange1FocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String coe1val = mD_text1.getText().toString().trim();
                try {
                    if (!coe1val.equals("")) {
                        double val1 = Double.parseDouble(coe1val);
                        if (val1 < -1000 || val1 > 9998.999) {
                            mD_text1.setText("1");
                            coe1val = "1";
                        }
                        coe1val = editDataFormat(coe1val, Integer.parseInt(getConfigData(componentName , "YXWS")));
                        saveOperationLogMsg(componentName, "COE1" + "_" + coe1val,  ErrorLog.msgType.操作_信息);
                        updateConfigData(componentName, "CoeKRange1", coe1val);

                        mD_text1.setText(coe1val);
                    } else {
                        Toast.makeText(getActivity(),getString(R.string.set_value_is_empty),Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(),getString(R.string.set_value_is_error),Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mD_text1.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class dataCoeRange2FocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String coe2val = mD_text2.getText().toString().trim();
                try {
                    if (!coe2val.equals("")) {
                        double val1 = Double.parseDouble(coe2val);
                        if (val1 < -1000 || val1 > 9998.999) {
                            mD_text2.setText("1");
                            coe2val = "1";
                        }
                        coe2val = editDataFormat(coe2val, Integer.parseInt(getConfigData(componentName , "YXWS")));
                        saveOperationLogMsg(componentName, "COE2" + "_" + coe2val, ErrorLog.msgType.操作_信息);
                        updateConfigData(componentName, "CoeKRange2", coe2val);

                        mD_text2.setText(coe2val);
                    } else {
                        Toast.makeText(getActivity(),getString(R.string.set_value_is_empty),Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(),getString(R.string.set_value_is_error),Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mD_text2.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class dataCoeRange3FocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String coe3val = mD_text3.getText().toString().trim();
                try {
                    if (!coe3val.equals("")) {
                        double val1 = Double.parseDouble(coe3val);
                        if (val1 < -1000 || val1 > 9998.999) {
                            mD_text3.setText("1");
                            coe3val = "1";
                        }
                        coe3val = editDataFormat(coe3val, Integer.parseInt(getConfigData(componentName , "YXWS")));
                        saveOperationLogMsg(componentName, "COE3" + "_" + coe3val, ErrorLog.msgType.操作_信息);
                        updateConfigData(componentName, "CoeKRange3", coe3val);

                        mD_text3.setText(coe3val);
                    } else {
                        Toast.makeText(getActivity(),getString(R.string.set_value_is_empty),Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(),getString(R.string.set_value_is_error),Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mD_text3.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getTag().toString().equals("open")) {
                btn_dataCoeKSwitch.setBackgroundResource(R.drawable.dialog312btn_n);
                btn_dataCoeKSwitch.setTag("close");
                updateConfigData(componentName, "rangeCoeOpen", "close");
                syncList();
                saveOperationLogMsg(componentName, "COES" + "_" + "false", ErrorLog.msgType.操作_信息);
            } else {
                btn_dataCoeKSwitch.setBackgroundResource(R.drawable.dialog312btn_s);
                btn_dataCoeKSwitch.setTag("open");
                updateConfigData(componentName, "rangeCoeOpen", "open");
                syncList();
                saveOperationLogMsg(componentName, "COES" + "_" + "true", ErrorLog.msgType.操作_信息);
            }
        }
    }

    private class btnCloseClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();

        }
    }
}
