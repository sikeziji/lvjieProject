package com.yzlm.cyl.cfragment.Dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zwj on 2017/12/28.
 */

public class Dialog_RunMsg extends ZDialogFragment {

    private TextView txtMsg;
    private String Info;

    protected int getDialogView() {
        return R.layout.dialog_run_msg;
    }

    @Override
    protected void InitUI(View view) {

        txtMsg = (TextView) view.findViewById(R.id.txtrunmsg);
        Info = getPrompt("alert-msg");
        txtMsg.setText(Info);

        Button mBtnD_Stop = (Button) view.findViewById(R.id.btnRunMsgStop);
        mBtnD_Stop.setOnClickListener(new BtnStopClick());

        new Timer().schedule(new TimerTask() {
            int runTimer = 0;

            public void run() {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("runMsg", Info + (++runTimer) + " S");
                msg.setData(bundle);
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 1000, 1000);
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

    private class BtnStopClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            setData(99, "runMsg", "top");
            dialog.dismiss();

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    txtMsg.setText(msg.getData().get("runMsg").toString());
                    break;
            }
        }
    };

}
