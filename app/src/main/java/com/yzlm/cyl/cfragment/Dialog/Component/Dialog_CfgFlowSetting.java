package com.yzlm.cyl.cfragment.Dialog.Component;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.yzlm.cyl.cfragment.Communication.Component.AppAction.Action;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.util.Map;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;


/*
 * Created by zwj 20190917
 *  配置流程设置界面
 */

public class Dialog_CfgFlowSetting extends ZDialogFragment {

    private static String componentName = "";
    private View v;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_cfg_flow_setting;
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void InitUI(View view) {
        componentName = mCompName;
        v = view;
        ActionTable at = Global.getActions(componentName);
        Map<String, Action> LAction = at.getLAction();
        if (LAction == null) return;
        GridLayout mGL = v.findViewById(R.id.GL);
        mGL.removeAllViews();

        int i = 0;
        for (Map.Entry<String, Action> entry : LAction.entrySet()) {

            try {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                Button mBtn = inflater.inflate(R.layout.ft_button_small, null).findViewById(R.id.btn_ft_small);
                mGL.addView(mBtn);

                mBtn.setText(entry.getKey());
                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) mBtn.getLayoutParams();
                layoutParams.setMargins(15, 15, 15, 15);
                mBtn.setLayoutParams(layoutParams);
                mBtn.setOnClickListener(new mBtnClick());
                mBtn.setId(i + 1);
            } catch (Exception ex) {
                ex.getMessage();
            }
        }

        Button btn_close = (Button) view.findViewById(R.id.btnClose);
        btn_close.setOnClickListener(new btnCloseClick());
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


    private class mBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(1, "select_Action", ((Button) v).getText().toString());
            dialog.dismiss();

        }
    }

    private class btnCloseClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();

        }
    }

}
