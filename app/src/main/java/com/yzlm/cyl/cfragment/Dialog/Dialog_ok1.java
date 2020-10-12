package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

public class Dialog_ok1 extends ZDialogFragment {

    private String Command;
    private String Component;
    private int okcz;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_ok1;
    }


    @Override
    protected void InitUI(View view) {

        Command = getPrompt("alert-ok1");
        TextView mD_text = view.findViewById(R.id.D_text);
        mD_text.setText( Command);

        Button mBtnD_OK = view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = view.findViewById(R.id.D_Cancel);
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

    public void  okcz(int requestCode)
    {
        okcz = requestCode;
    }

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            setData(okcz, "Dialog_Command", Component + "_" + Command);
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
