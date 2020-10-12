package com.yzlm.cyl.cfragment.Dialog.Component;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;

/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_CopyConfigSelect extends ZDialogFragment {
    private Button mBtnSelectConfig;
    private String msg;
    private String Component;
    private int iRange = 1;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_copy_config_select;
    }

    @Override
    protected void InitUI(View view) {

        msg = getPrompt("alert-ok");
        Component = MainActivity.mCompName;
        TextView mD_text = (TextView) view.findViewById(R.id.D_Selecttext);
        mD_text.setText(String.format("是否 %s？", msg));

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_SelectOK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_SelectCancel);
        mBtnD_Cancel.setOnClickListener(new BtnCancelClick());

        mBtnSelectConfig = (Button) view.findViewById(R.id.D_SelectConfig);
        mBtnSelectConfig.setOnClickListener(new BtnClick());
        mBtnSelectConfig.setText(getResources().getString(R.string.all));
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

    private class BtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.D_SelectConfig:
                    /*if (mBtnSelectConfig.getText().toString().equals(getResources().getString(R.string.all))) {
                        mBtnSelectConfig.setText("Csoft");
                    } else if (mBtnSelectConfig.getText().toString().equals("Csoft")) {
                        mBtnSelectConfig.setText("waterrun");
                    } else if (mBtnSelectConfig.getText().toString().equals("waterrun")) {
                        mBtnSelectConfig.setText("watercrash");
                    } else if (mBtnSelectConfig.getText().toString().equals("watercrash")) {
                        mBtnSelectConfig.setText(getResources().getString(R.string.all));
                    }*/
                    if (mBtnSelectConfig.getText().toString().equals(getResources().getString(R.string.all))) {
                        mBtnSelectConfig.setText("waterrun");
                    } else if (mBtnSelectConfig.getText().toString().equals("waterrun")) {
                        mBtnSelectConfig.setText("watercrash");
                    } else if (mBtnSelectConfig.getText().toString().equals("watercrash")) {
                        mBtnSelectConfig.setText(getResources().getString(R.string.all));
                    }
                    break;
            }
        }
    }


    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(3, "Dialog_CopyConfigSelect", mBtnSelectConfig.getText().toString());
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
