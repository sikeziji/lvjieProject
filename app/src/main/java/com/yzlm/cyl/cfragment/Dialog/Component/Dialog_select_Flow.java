package com.yzlm.cyl.cfragment.Dialog.Component;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.isHaveFlow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;

/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_select_Flow extends ZDialogFragment {

    private String[] sFlow;
    private Spinner mSpFlow;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_select_flow;
    }

    @Override
    protected void InitUI(View view) {

        String msg = getPrompt("alert-select_Flow");

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_SelectOK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_SelectCancel);
        mBtnD_Cancel.setOnClickListener(new BtnCancelClick());
        sFlow = context.getResources().getStringArray(R.array.strDDCL);
        for (int i = 0; i < sFlow.length; i++) {
            if (!isHaveFlow(mCompName, sFlow[i], getConfigData(mCompName, "RANGE"), GetPlatRangSum(mCompName))) {
                sFlow[i] = context.getResources().getString(R.string.reserve);
            }
        }
        mSpFlow = (Spinner) view.findViewById(R.id.spFlow);
        InitSpinner(context, mSpFlow, sFlow, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);

        mSpFlow.setSelection(Integer.parseInt(msg));
        mSpFlow.setOnItemSelectedListener(new mSpListener());
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

    private class mSpListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (sFlow[position].equals(context.getResources().getString(R.string.reserve))) {
                mSpFlow.setSelection(0);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(1, "select_Flow", String.valueOf(mSpFlow.getSelectedItemPosition()));
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
