package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity;

/**
 * Created by zwj on 2017/6/28.
 */

public class Dialog_PublicOk extends ZDialogFragment {

    private String Command;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_ok;
    }

    @Override
    protected void InitUI(View view) {

        Command = getPrompt("alert-ok");
        String component = MainActivity.mCompName;
        TextView mD_text = (TextView) view.findViewById(R.id.D_text);
        mD_text.setText(getString(R.string.whether_if)+" " + Command + "？");

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new Dialog_PublicOk.BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_Cancel);
        mBtnD_Cancel.setOnClickListener(new Dialog_PublicOk.BtnCancelClick());
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

            ((CSingleFragmentActivity.DataCallback) getActivity()).getData("DialogPublicOk" +"_"+Command);
            setData(1, "DialogPublicOk", Command);
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
