package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity;

/**
 * Created by caoyiliang on 2017/2/15.
 */

public class Dialog_Err extends ZDialogFragment {

    private String sInfo;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_err;
    }

    @Override
    protected void InitUI(View view) {
        sInfo = getPrompt("alert-err");
        TextView mD_text = (TextView) view.findViewById(R.id.D_text);
        mD_text.setText(sInfo);

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());
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
            ((CSingleFragmentActivity.DataCallback) getActivity()).getData("Err_-1" + sInfo);
            dialog.dismiss();
        }
    }
}
