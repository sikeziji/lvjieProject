package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

/**
 * Created by zwj on 2017/5/18.
 */

public class DialogMsg extends ZDialogFragment {

    protected int getDialogView() {
        return R.layout.dialog_msg;
    }
    @Override
    protected void InitUI(View view) {
        TextView txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        String info = getPrompt("alert-msg");
        txtMsg.setText(info);
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
    public void dismiss() {

        dialog.dismiss();

    }

    @Override
    protected void dialogCancel() {

    }
}
