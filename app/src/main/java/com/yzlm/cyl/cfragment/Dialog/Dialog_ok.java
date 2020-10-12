package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;

/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_ok extends ZDialogFragment {

    private String Command;
    private String Component;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_ok;
    }

    @Override
    protected void InitUI(View view) {

        Command = getPrompt("alert-ok");
        Component = MainActivity.mCompName;
        TextView mD_text = (TextView) view.findViewById(R.id.D_text);
        mD_text.setText(getString(R.string.component) + "[" + Component + "] " + getString(R.string.whether_if) + " " + Command + "？");

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_Cancel);
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

            setData(1, "Dialog_Command", Component + "_" + Command);
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
