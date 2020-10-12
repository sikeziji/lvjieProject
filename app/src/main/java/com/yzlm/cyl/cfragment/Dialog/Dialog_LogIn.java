package com.yzlm.cyl.cfragment.Dialog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Frame.PublicContent.Bottom_list;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.equalStr;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.strComponent;


/*
 * Created by zwj 2019年1月11日14:44:13
 *
 */

public class Dialog_LogIn extends ZDialogFragment {


    private Spinner mSpLogIn;
    private Button btn_out;
    private Button btn_passwordAmend;
    private Button btn_passwordAmendOk;
    private EditText mEdPassword;
    private EditText mEdNewPassword;
    private LinearLayout mLayoutNewPassword;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onDialogSelected(View view, Fragment Fm);

        void onDialogRS();
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
    protected int getDialogView() {
        return R.layout.dialog_login;
    }

    @Override
    protected void InitUI(View view) {

        View v = view;
        mSpLogIn = (Spinner) v.findViewById(R.id.spLogIn);
        String[] loginUsers = new String[2];
        String[] sDefUsers = getResources().getStringArray(R.array.loginUsers);
        System.arraycopy(sDefUsers, 0, loginUsers, 0, 2);
        InitSpinner(context, mSpLogIn, loginUsers, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mEdPassword = (EditText) view.findViewById(R.id.D_ET);

        Button btnOk = (Button) view.findViewById(R.id.D_LogInOK);
        btnOk.setOnClickListener(new btnClick());
        Button btn_close = (Button) view.findViewById(R.id.D_LogInCancel);
        btn_close.setOnClickListener(new btnClick());
        btn_out = (Button) view.findViewById(R.id.D_LogInOut);
        btn_out.setOnClickListener(new btnClick());
        LinearLayout mLayoutPassword = (LinearLayout) v.findViewById(R.id.layoutLoginPassword);
        TextView mTxtLoginName = (TextView) v.findViewById(R.id.txtNowLoginName);
        btn_passwordAmend = (Button) v.findViewById(R.id.D_LogInPasswordAmend);
        btn_passwordAmend.setOnClickListener(new btnClick());
        mLayoutNewPassword = (LinearLayout) v.findViewById(R.id.layoutNewPassword);
        btn_passwordAmendOk = (Button) v.findViewById(R.id.D_LogInPasswordAmendOk);
        btn_passwordAmendOk.setOnClickListener(new btnClick());
        mEdNewPassword = (EditText) v.findViewById(R.id.edit_NewPassword);
        if (!getPublicConfigData("LogInName").equals("0")) {
            // 已登录状态（显示登录账号、 退出登录、取消、修改密码）
            mTxtLoginName.setVisibility(View.VISIBLE);
            mTxtLoginName.setText(sDefUsers[Integer.parseInt(getPublicConfigData("LogInName")) - 1]);

            btn_out.setVisibility(View.VISIBLE);
            btn_passwordAmend.setVisibility(View.VISIBLE);

            // 隐藏按钮
            btnOk.setVisibility(View.GONE);//登录按钮
            mLayoutPassword.setVisibility(View.GONE);//登录密码
            mSpLogIn.setVisibility(View.GONE);//登录账号选择

        } else {
            //登录状态 显示 账号选择、登录密码、登录按钮、取消按钮
            btnOk.setVisibility(View.VISIBLE);
            mSpLogIn.setVisibility(View.VISIBLE);
            mLayoutPassword.setVisibility(View.VISIBLE);

            btn_out.setVisibility(View.GONE);
            mTxtLoginName.setVisibility(View.GONE);
            btn_passwordAmend.setVisibility(View.GONE);
        }
        if (getPublicConfigData("LogInName").equals("3")) {

            btn_passwordAmend.setVisibility(View.GONE);
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


    @Override
    protected void dialogCancel() {

    }


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.D_LogInCancel:
                    setData(2);
                    dialog.dismiss();
                    mCallbacks.onDialogRS();

                    break;
                case R.id.D_LogInOK:
                    String sInput = mEdPassword.getText().toString();
                    switch (mSpLogIn.getSelectedItemPosition()) {
                        case 0:
                            if (sInput.equals(getPublicConfigData("LogInUserPassword"))) {
                                updatePublicConfigData("LogInName", "1");
                                Toast.makeText(context, context.getText(R.string.loginSuccess) + "!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                saveOperationLogMsg("公共", "用户登录", ErrorLog.msgType.操作_信息);
                                for (int i = 0; i < strComponent.get(1).length; i++) {
                                    //用户账号登录（此处就把所有参数都记录一条该运行日志）
                                    AddError(strComponent.get(1)[i], 596, ErrorLog.msgType.登录_信息);
                                }
                            } else {
                                Toast.makeText(context, context.getText(R.string.passwordError) + "!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            if (sInput.equals(getPublicConfigData("LogInOPSPassword"))) {
                                updatePublicConfigData("LogInName", "2");
                                Toast.makeText(context, context.getText(R.string.loginSuccess) + "!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                saveOperationLogMsg("公共", "运维登录", ErrorLog.msgType.操作_信息);
                                for (int i = 0; i < strComponent.get(1).length; i++) {
                                    //运维账号登录（此处就把所有参数都记录一条该运行日志）
                                    AddError(strComponent.get(1)[i], 598, ErrorLog.msgType.登录_信息);
                                }
                            } else {
                                Toast.makeText(context, context.getText(R.string.passwordError) + "!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            break;
                    }

                    mCallbacks.onDialogSelected(v, Bottom_list.newInstance());
                    break;
                case R.id.D_LogInOut:
                    String oldLogin = getPublicConfigData("LogInName");
                    switch (oldLogin) {
                        case "1":
                            for (int i = 0; i < strComponent.get(1).length; i++) {
                                //用户账号退出（此处就把所有参数都记录一条该运行日志）
                                AddError(strComponent.get(1)[i], 597, ErrorLog.msgType.登录_信息);
                            }
                            break;
                        case "2":
                            for (int i = 0; i < strComponent.get(1).length; i++) {
                                //运维账号退出（此处就把所有参数都记录一条该运行日志）
                                AddError(strComponent.get(1)[i], 599, ErrorLog.msgType.登录_信息);
                            }
                            break;
                        default:
                            break;
                    }
                    updatePublicConfigData("LogInName", "0");
                    saveOperationLogMsg("公共", "退出登录", ErrorLog.msgType.操作_信息);
                    setData(2);
                    dialog.dismiss();
                    mCallbacks.onDialogSelected(v, Bottom_list.newInstance());

                    break;
                case R.id.D_LogInPasswordAmend:
                    mLayoutNewPassword.setVisibility(View.VISIBLE);     // 新密码修改界面
                    btn_passwordAmendOk.setVisibility(View.VISIBLE);    //修改密码确定按钮
                    btn_passwordAmend.setVisibility(View.GONE);         // 隐藏修改按钮
                    btn_out.setVisibility(View.GONE);                   // 隐藏登录按钮
                    break;
                case R.id.D_LogInPasswordAmendOk:
                    String sInputNewPassword = mEdNewPassword.getText().toString();
                    if (!sInputNewPassword.equals("")) {
                        if (sInputNewPassword.length() == 5) {
                            if (sInputNewPassword.substring(0, 1).equals("1") || equalStr(sInputNewPassword)) {
                                Toast.makeText(getActivity(), R.string.passwordTooSimple, Toast.LENGTH_SHORT).show();
                            } else {
                                if (getPublicConfigData("LogInName").equals("1")) {
                                    if (!getPublicConfigData("LogInOPSPassword").equals(sInputNewPassword)) {
                                        saveOperationLogPublicDataModifyMsg("公共", "LogInUserPassword", sInputNewPassword, "用户密码", ErrorLog.msgType.操作_信息);
                                        updatePublicConfigData("LogInUserPassword", sInputNewPassword);
                                        Toast.makeText(context, context.getText(R.string.passwordAmendSuccess) + "!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        mCallbacks.onDialogSelected(v, Bottom_list.newInstance());
                                        mCallbacks.onDialogRS();
                                    } else {
                                        Toast.makeText(context, context.getText(R.string.pleasSelectOtherPassword) + "!", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (getPublicConfigData("LogInName").equals("2")) {
                                    if (!getPublicConfigData("LogInUserPassword").equals(sInputNewPassword)) {
                                        saveOperationLogPublicDataModifyMsg("公共", "LogInOPSPassword", sInputNewPassword, "运维", ErrorLog.msgType.操作_信息);
                                        updatePublicConfigData("LogInOPSPassword", sInputNewPassword);
                                        Toast.makeText(context, context.getText(R.string.passwordAmendSuccess) + "!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        mCallbacks.onDialogSelected(v, Bottom_list.newInstance());
                                        mCallbacks.onDialogRS();
                                    } else {
                                        Toast.makeText(context, context.getText(R.string.pleasSelectOtherPassword) + "!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.inputPasswordFiveBit, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
