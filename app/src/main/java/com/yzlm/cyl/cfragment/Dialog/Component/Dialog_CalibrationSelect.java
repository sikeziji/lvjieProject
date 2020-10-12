package com.yzlm.cyl.cfragment.Dialog.Component;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;

/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_CalibrationSelect extends ZDialogFragment {
    private Button mBtnSelectRange;
    private String msg;
    private String Component;
    private int iRange = 1;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_cal_range_select;
    }

    @Override
    protected void InitUI(View view) {

        msg = getPrompt("alert-ok");
        Component = MainActivity.mCompName;
        TextView mD_text = (TextView) view.findViewById(R.id.D_Selecttext);
        mD_text.setText(String.format(getString(R.string.component) + "[%s] " + getString(R.string.whether) + " %s？", Component, msg));

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_SelectOK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_SelectCancel);
        mBtnD_Cancel.setOnClickListener(new BtnCancelClick());

        mBtnSelectRange = (Button) view.findViewById(R.id.D_SelectRange);
        mBtnSelectRange.setOnClickListener(new BtnClick());
        iRange = Integer.parseInt(getConfigData(Component, "RANGE"));
        updateConfigData(Component, "CalSelectRange", String.valueOf(iRange));
        mBtnSelectRange.setText(String.valueOf(iRange));
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
                case R.id.D_SelectRange:
                    iRange++;
                    // 量程 最大为 3
                    if (iRange > GetPlatRangSum(Component)) {
                        iRange = 1;
                    }
                    if (!getConfigData(Component, "CalSelectRange").equals(String.valueOf(iRange))) {
                        saveOperationLogDataModifyMsg(Component, "CalSelectRange", String.valueOf(iRange), "设置校准后选择量程", ErrorLog.msgType.操作_信息);
                        updateConfigData(Component, "CalSelectRange", String.valueOf(iRange));
                    }
                    mBtnSelectRange.setText(String.valueOf(iRange));
                    break;
            }
        }
    }


    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(1, "Dialog_Command", Component + "_" + msg);
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
