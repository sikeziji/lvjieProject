package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.EditText;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Component;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.CDialogFragment;

import static com.yzlm.cyl.cfragment.Global.context;

/**
 * Created by caoyiliang on 2016/12/21.
 */

public class ChangePoint extends CDialogFragment {
    private EditText ETOldPoint;
    private EditText EtChange;

    @Override
    protected int getDialogView() {
        return R.layout.change_zf;
    }

    protected void InitUI(View view) {
        ETOldPoint = (EditText) view.findViewById(R.id.eTOldPoint);
        ETOldPoint.setText(getPrompt("alert-change_point"));
        EtChange = (EditText) view.findViewById(R.id.eTChangePoint);
        /* 组分名的地址输入框（WL）*/
        /* 组分名的地址输入框（WL）*/
        EditText etChangePointAddr = (EditText) view.findViewById(R.id.eTChangePointAddr);
    }

    @Override
    protected boolean setCanceledOnTouchOutside() {
        return true;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.modify_component_name);
    }

    @Override
    protected String getPositiveButtonName() {
        return getString(R.string.modify);
    }

    @Override
    protected String getNegativeButtonName() {
        return getString(R.string.cancel);
    }

    @Override
    protected void PositiveButtonListener() {
        Component component = Component.newInstance(context);
        component.UpdateZfName(EtChange.getText().toString(), ETOldPoint.getText().toString());
        setData(1);
    }

    @Override
    protected void NegativeButtonListener() {

    }
}
