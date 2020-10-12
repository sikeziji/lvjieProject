package com.yzlm.cyl.cfragment.Dialog.Component;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.CDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.concat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;

/**
 * Created by caoyiliang on 2016/11/10.
 */

@SuppressLint("ValidFragment")
public class SelectTime extends CDialogFragment {
    //CheckBox cbk0;
    CheckBox cbk1;
    CheckBox cbk2;
    CheckBox cbk3;
    CheckBox cbk4;
    CheckBox cbk5;
    CheckBox cbk6;
    CheckBox cbk7;
    CheckBox cbk8;
    CheckBox cbk9;
    CheckBox cbk10;
    CheckBox cbk11;
    CheckBox cbk12;
    CheckBox cbk13;
    CheckBox cbk14;
    CheckBox cbk15;
    /*CheckBox cbk16;
    CheckBox cbk17;
    CheckBox cbk18;
    CheckBox cbk19;
    CheckBox cbk20;
    CheckBox cbk21;
    CheckBox cbk22;
    CheckBox cbk23;*/

    CheckBox cbkAll;
    Button mBtnOk, mBtnCancel;
    List<CheckBox> CbkList = new ArrayList<>();

    int Type = 0;

    public SelectTime(int type) {
        Type = type;
    }

    @Override
    protected int getDialogView() {
        return R.layout.select_time;
    }

    protected void InitUI(View view) {
        //cbk0 = (CheckBox) view.findViewById(R.id.cb_0);
        cbk1 = (CheckBox) view.findViewById(R.id.cb_1);
        cbk2 = (CheckBox) view.findViewById(R.id.cb_2);
        cbk3 = (CheckBox) view.findViewById(R.id.cb_3);
        cbk4 = (CheckBox) view.findViewById(R.id.cb_4);
        cbk5 = (CheckBox) view.findViewById(R.id.cb_5);
        cbk6 = (CheckBox) view.findViewById(R.id.cb_6);
        cbk7 = (CheckBox) view.findViewById(R.id.cb_7);
        cbk8 = (CheckBox) view.findViewById(R.id.cb_8);
        cbk9 = (CheckBox) view.findViewById(R.id.cb_9);
        cbk10 = (CheckBox) view.findViewById(R.id.cb_10);
        cbk11 = (CheckBox) view.findViewById(R.id.cb_11);
        cbk12 = (CheckBox) view.findViewById(R.id.cb_12);
        cbk13 = (CheckBox) view.findViewById(R.id.cb_13);
        cbk14 = (CheckBox) view.findViewById(R.id.cb_14);
        cbk15 = (CheckBox) view.findViewById(R.id.cb_15);
        /*cbk16 = (CheckBox) view.findViewById(R.id.cb_16);
        cbk17 = (CheckBox) view.findViewById(R.id.cb_17);
        cbk18 = (CheckBox) view.findViewById(R.id.cb_18);
        cbk19 = (CheckBox) view.findViewById(R.id.cb_19);
        cbk20 = (CheckBox) view.findViewById(R.id.cb_20);
        cbk21 = (CheckBox) view.findViewById(R.id.cb_21);
        cbk22 = (CheckBox) view.findViewById(R.id.cb_22);
        cbk23 = (CheckBox) view.findViewById(R.id.cb_23);*/

        //CbkList.add(cbk0);
        CbkList.add(cbk1);
        CbkList.add(cbk2);
        CbkList.add(cbk3);
        CbkList.add(cbk4);
        CbkList.add(cbk5);
        CbkList.add(cbk6);
        CbkList.add(cbk7);
        CbkList.add(cbk8);
        CbkList.add(cbk9);
        CbkList.add(cbk10);
        CbkList.add(cbk11);
        CbkList.add(cbk12);
        CbkList.add(cbk13);
        CbkList.add(cbk14);
        CbkList.add(cbk15);
        /*CbkList.add(cbk16);
        CbkList.add(cbk17);
        CbkList.add(cbk18);
        CbkList.add(cbk19);
        CbkList.add(cbk20);
        CbkList.add(cbk21);
        CbkList.add(cbk22);
        CbkList.add(cbk23);*/

        cbkAll = (CheckBox) view.findViewById(R.id.cb_all);
        cbkAll.setOnCheckedChangeListener(new cbkAllChange());
        cbkAll.setVisibility(View.GONE);
        String[] TempCheck = getConfigData(mCompName, getPrompt("alert-select_time")).split("，");
        String[] TempEnable;
        switch (getPrompt("alert-select_time")) {
            case "GOSZ_X":
                TempEnable = concat(getConfigData(mCompName, "GOSZ_Y").split("，"), getConfigData(mCompName, "GOSZ_Z").split("，"));
                break;
            case "GOSZ_Y":
                TempEnable = concat(getConfigData(mCompName, "GOSZ_X").split("，"), getConfigData(mCompName, "GOSZ_Z").split("，"));
                break;
            case "GOSZ_Z":
                TempEnable = concat(getConfigData(mCompName, "GOSZ_X").split("，"), getConfigData(mCompName, "GOSZ_Y").split("，"));
                break;
            default:
                TempEnable = new String[]{""};
                break;
        }
        List<String> TempCheckList = Arrays.asList(TempCheck);
        for (CheckBox checkbox : CbkList) {
            if (TempCheckList.contains(checkbox.getText().toString())) {
                checkbox.setChecked(true);
            }
        }
        List<String> TempEnableList = Arrays.asList(TempEnable);
        for (CheckBox checkbox : CbkList) {
            if (TempEnableList.contains(checkbox.getText().toString())) {
                checkbox.setEnabled(false);
            }
        }
        mBtnOk = (Button) view.findViewById(R.id.Btn_select_ok);
        mBtnOk.setOnClickListener(new BtnOKClick());

        mBtnCancel = (Button) view.findViewById(R.id.Btn_select_cancel);
        mBtnCancel.setOnClickListener(new BtnCancelClick());
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

    private class cbkAllChange implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            for (CheckBox checkbox : CbkList) {
                checkbox.setChecked(cbkAll.isChecked());
            }
        }
    }

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            StringBuffer sb = new StringBuffer();
            for (CheckBox checkbox : CbkList) {
                if (checkbox.isChecked()) {
                    sb.append(checkbox.getText().toString() + "，");
                }
            }
            String s = sb.toString();
            if (s.length() > 0) s = s.substring(0, s.length() - 1);
            switch (getPrompt("alert-select_time")) {
                case "GOSZ_X":
                    Type = 1;
                    break;
                case "GOSZ_Y":
                    Type = 10;
                    break;
                case "GOSZ_Z":
                    Type = 11;
                    break;
            }
            setData(Type, "SelectTime", s);
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
