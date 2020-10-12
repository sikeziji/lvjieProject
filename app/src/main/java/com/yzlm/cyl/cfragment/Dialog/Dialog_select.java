package com.yzlm.cyl.cfragment.Dialog;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

/**
 * Created by caoyiliang on 2017/1/18.
 */

@SuppressLint("ValidFragment")
public class Dialog_select extends ZDialogFragment {
    private String msg;
    private String Component;

    public Dialog_select(String component){
        this.Component = component;
    }

    @Override
    protected int getDialogView() {
        return R.layout.dialog_select;
    }

    @Override
    protected void InitUI(View view) {

        msg = getPrompt("alert-select");

        TextView mD_text = (TextView) view.findViewById(R.id.D_Selecttext);
        try {
            mD_text.setText(msg.split(";")[0]);
        } catch (Exception e) {

        }
        Button mBtnD_OK = (Button) view.findViewById(R.id.D_SelectOK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_SelectCancel);
        mBtnD_Cancel.setOnClickListener(new BtnCancelClick());
    }

    @Override
    protected boolean setCanceledOnTouchOutside() {
        return true;
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
            setData(1, "Dialog_Command", Component + "_" + msg);
            dialog.dismiss();
        }
    }

    private class BtnCancelClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();
        }
    }
}
