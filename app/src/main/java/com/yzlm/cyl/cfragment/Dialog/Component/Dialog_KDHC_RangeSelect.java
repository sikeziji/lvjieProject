package com.yzlm.cyl.cfragment.Dialog.Component;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;

/**
 * *******************************************
 * 项目名称：water
 *
 * @Author zwj
 * 创建时间：2019/6/18   13:51
 * 用途
 * *******************************************
 **/
public class Dialog_KDHC_RangeSelect extends ZDialogFragment {

    private static String componentName = "";
    private ToggleButton mTogKDHCManualRangeSelect;

    @Override
    protected void dialogCancel() {

    }

    @Override
    protected int getDialogView() {
        return R.layout.dialog_kdhc_rangeselect;
    }

    @Override
    protected void InitUI(View view) {
        try {
            componentName = mCompName;
            View v = view;
            Button btn_close = (Button) view.findViewById(R.id.btnClose);
            btn_close.setOnClickListener(new btnCloseClick());

            mTogKDHCManualRangeSelect = (ToggleButton) v.findViewById(R.id.togBtnKDHCManualRange);
            mTogKDHCManualRangeSelect.setOnCheckedChangeListener(new togBtnClick());
            mTogKDHCManualRangeSelect.setChecked(getConfigData(componentName, "KDHCManualRangeSwitch").equals("open"));

            Spinner mSpKDHCRange = (Spinner) view.findViewById(R.id.spKDHCRange);
            InitSpinner(context, mSpKDHCRange, R.array.range, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);

            mSpKDHCRange.setSelection(Integer.parseInt(getConfigData(componentName, "KDHCManualRange")) - 1);
            mSpKDHCRange.setOnItemSelectedListener(new SpClickListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private class btnCloseClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();

        }
    }

    private class togBtnClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.togBtnKDHCManualRange:
                    if (isChecked != getConfigData(componentName, "KDHCManualRangeSwitch").equals("open")) {
                        String strSwitch = getConfigData(componentName, "KDHCManualRangeSwitch").equals("open") ? "close" : "open";
                        saveOperationLogDataModifyMsg(componentName, "KDHCManualRangeSwitch", strSwitch, "设置跨度核查手动量程开关", ErrorLog.msgType.操作_信息);
                        updateConfigData(componentName, "KDHCManualRangeSwitch", strSwitch);
                        mTogKDHCManualRangeSelect.setText(strSwitch.equals("open") ? getString(R.string.opened) : getString(R.string.closeed));
                    }
                    break;
            }
        }
    }

    private class SpClickListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {
                case R.id.spKDHCRange:
                    if (!getConfigData(componentName, "KDHCManualRange").equals(String.valueOf(position + 1))) {
                        saveOperationLogDataModifyMsg(componentName, "KDHCManualRange", String.valueOf(position + 1), "设置跨度核查量程", ErrorLog.msgType.操作_信息);
                        updateConfigData(componentName, "KDHCManualRange", String.valueOf(position + 1));
                    }
                    break;
            }
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
