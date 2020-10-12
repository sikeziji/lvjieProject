package com.yzlm.cyl.cfragment.Dialog;

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
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.equalStr;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.strComponent;


/*
 * Created by zwj 2019年1月11日14:44:13
 *
 */


public class Dialog_UserPassword extends ZDialogFragment {

    private EditText mEdUserPassword;

    private static String componentName = "";
    private View v;

    String sSettingKey;
    String sPasswordKey;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_user_password;
    }


    @Override
    protected void InitUI(View view) {

        componentName = mCompName;
        v = view;
        String sPassword = getPrompt("alert-UserPassword");
        ;
        sSettingKey = sPassword.split("_")[0];
        sPasswordKey = sPassword.split("_")[1];
        mEdUserPassword = (EditText) view.findViewById(R.id.edit_usPassword);
        mEdUserPassword.setText(getPublicConfigData( sSettingKey));
        mEdUserPassword.setOnFocusChangeListener(new editTextFocusChange());
        mEdUserPassword.setOnEditorActionListener(new editTextFocusChange());

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

    /*
     * 用户密码
     * **/
    private boolean setTextUserPassword(String strVal) {
        try {
            if (!strVal.equals("")) {
                if (strVal.length() == 6) {
                    if (strVal.substring(0, 1).equals("1") || equalStr(strVal)) {
                        Toast.makeText(getActivity(), R.string.passwordTooSimple, Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (!strVal.substring(0, sPasswordKey.length()).equals(sPasswordKey)) {
                        Toast.makeText(getActivity(), getString(R.string.passwordHint1) + " " + sPasswordKey + " " + getString(R.string.passwordHint2), Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                            saveOperationLogPublicDataModifyMsg("公共", sSettingKey, strVal, sSettingKey, ErrorLog.msgType.操作_信息);
                            updatePublicConfigData( sSettingKey, strVal);
                        syncList();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.inputPasswordSixBit, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), getString(R.string.set_value_is_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void dialogCancel() {

    }


    private class editTextFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String strKz;
                switch (v.getId()) {
                    case R.id.edit_usPassword:
                        strKz = mEdUserPassword.getText().toString().trim();
                        setTextUserPassword(strKz);
                        mEdUserPassword.setText(getPublicConfigData( sSettingKey));
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
}
