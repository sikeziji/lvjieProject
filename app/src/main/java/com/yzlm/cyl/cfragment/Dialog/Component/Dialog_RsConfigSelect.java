package com.yzlm.cyl.cfragment.Dialog.Component;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getFileNames;

public class Dialog_RsConfigSelect extends ZDialogFragment {
    private String msg;
    private String Component;
    private int iRange = 1;
    private Spinner spRsSelect;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_select_restore_config;
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

        String[] sPatn = getFileNames(SdcardPath + "Backup/");

        spRsSelect = (Spinner) view.findViewById(R.id.spConfig);
        InitSpinner(context, spRsSelect, sPatn, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
        spRsSelect.setSelection(0);
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
            String info = "";
            if (spRsSelect.getSelectedItem() != null) {
                info = spRsSelect.getSelectedItem().toString();
            }
            setData(4, "Dialog_RsConfigSelect", info);
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
