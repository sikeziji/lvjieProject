package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity;


/**
 * Created by zwj on 2017/6/28.
 */

public class Dialog_PublicStop extends ZDialogFragment {

    private String Info;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_stop;
    }

    @Override
    protected void InitUI(View view) {

        Info = getPrompt("alert-stop");

        TextView txtDMsg = (TextView) view.findViewById(R.id.txtDmsg);
        txtDMsg.setText(Info);

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.btnCancel);
        mBtnD_Cancel.setOnClickListener(new Dialog_PublicStop.BtnStopClick());
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


    private class BtnStopClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            ((CSingleFragmentActivity.DataCallback) getActivity()).getData("DialogPublicStop" + "_" + Info);
            setData(3, "DialogPublicStop", Info);
            dialog.dismiss();

        }
    }
}
