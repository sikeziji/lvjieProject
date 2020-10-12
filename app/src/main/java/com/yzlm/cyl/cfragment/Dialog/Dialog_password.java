package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity;

/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_password extends ZDialogFragment {
    private EditText mD_ET;
    /*密码对话框界面来源*/
    private String viewSource;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_password;
    }

    @Override
    protected void InitUI(View view) {

        viewSource = getPrompt("alert-info");
        mD_ET = (EditText) view.findViewById(R.id.D_ET);

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_Cancel);
        mBtnD_Cancel.setOnClickListener(new BtnCancelClick());

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

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(!mD_ET.getText().toString().equals("")) {
                ((CSingleFragmentActivity.DataCallback) getActivity()).getData("password_" + mD_ET.getText().toString()+"_"+viewSource);
            }
            dialog.dismiss();
        }
    }

    private class BtnCancelClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((CSingleFragmentActivity.DataCallback) getActivity()).getData("password_-2_"+viewSource);
            dialog.dismiss();
        }
    }
}
