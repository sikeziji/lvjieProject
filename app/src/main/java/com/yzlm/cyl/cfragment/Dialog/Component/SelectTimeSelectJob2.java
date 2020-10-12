package com.yzlm.cyl.cfragment.Dialog.Component;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.Spinner;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.isHaveFlow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * Created by caoyiliang on 2016/11/10.
 */

public class SelectTimeSelectJob2 extends ZDialogFragment {
    CheckBox cbk0;
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
    CheckBox cbk16;
    CheckBox cbk17;
    CheckBox cbk18;
    CheckBox cbk19;
    CheckBox cbk20;
    CheckBox cbk21;
    CheckBox cbk22;
    CheckBox cbk23;

    CheckBox cbkAll;
    Button mBtnOk, mBtnCancel;

    TextView mTvJob0, mTvJob1, mTvJob2, mTvJob3, mTvJob4, mTvJob5,
            mTvJob6, mTvJob7, mTvJob8, mTvJob9, mTvJob10, mTvJob11,
            mTvJob12, mTvJob13, mTvJob14, mTvJob15, mTvJob16, mTvJob17,
            mTvJob18, mTvJob19, mTvJob20, mTvJob21, mTvJob22, mTvJob23;

    List<CheckBox> CbkList = new ArrayList<>();
    List<TextView> TvList = new ArrayList<>();
    String[] sFlow;
    static String[] sAllSetFlow;
    TextView mPreTv = null;
    Spinner mSpFlow;

    @Override
    protected int getDialogView() {
        return R.layout.select_time_select_job2;
    }

    protected void InitUI(View view) {
        cbk0 = (CheckBox) view.findViewById(R.id.cb_0);
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
        cbk16 = (CheckBox) view.findViewById(R.id.cb_16);
        cbk17 = (CheckBox) view.findViewById(R.id.cb_17);
        cbk18 = (CheckBox) view.findViewById(R.id.cb_18);
        cbk19 = (CheckBox) view.findViewById(R.id.cb_19);
        cbk20 = (CheckBox) view.findViewById(R.id.cb_20);
        cbk21 = (CheckBox) view.findViewById(R.id.cb_21);
        cbk22 = (CheckBox) view.findViewById(R.id.cb_22);
        cbk23 = (CheckBox) view.findViewById(R.id.cb_23);

        CbkList.add(cbk0);
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
        CbkList.add(cbk16);
        CbkList.add(cbk17);
        CbkList.add(cbk18);
        CbkList.add(cbk19);
        CbkList.add(cbk20);
        CbkList.add(cbk21);
        CbkList.add(cbk22);
        CbkList.add(cbk23);

        cbkAll = (CheckBox) view.findViewById(R.id.cb_all);
        cbkAll.setOnCheckedChangeListener(new cbkAllChange());
        String strMsg = getPrompt("alert-select_time_select_job");
        String[] TempMsg = strMsg.split("&");

        if (TempMsg.length > 0) {
            String[] TempCheck = TempMsg[0].split("，");

            List<String> TempCheckList = Arrays.asList(TempCheck);
            for (int i = 0; i < CbkList.size(); i++) {
                if (TempCheckList.contains(CbkList.get(i).getText().toString())) {
                    CbkList.get(i).setChecked(true);
                }
                CbkList.get(i).setId(i);
                CbkList.get(i).setOnCheckedChangeListener(new cbkChange());
            }
        }
        mBtnOk = (Button) view.findViewById(R.id.Btn_select_ok);
        mBtnOk.setOnClickListener(new BtnOKClick());

        mBtnCancel = (Button) view.findViewById(R.id.Btn_select_cancel);
        mBtnCancel.setOnClickListener(new BtnCancelClick());

        mTvJob0 = (TextView) view.findViewById(R.id.tvJob0);
        mTvJob1 = (TextView) view.findViewById(R.id.tvJob1);
        mTvJob2 = (TextView) view.findViewById(R.id.tvJob2);
        mTvJob3 = (TextView) view.findViewById(R.id.tvJob3);
        mTvJob4 = (TextView) view.findViewById(R.id.tvJob4);
        mTvJob5 = (TextView) view.findViewById(R.id.tvJob5);
        mTvJob6 = (TextView) view.findViewById(R.id.tvJob6);
        mTvJob7 = (TextView) view.findViewById(R.id.tvJob7);
        mTvJob8 = (TextView) view.findViewById(R.id.tvJob8);
        mTvJob9 = (TextView) view.findViewById(R.id.tvJob9);
        mTvJob10 = (TextView) view.findViewById(R.id.tvJob10);
        mTvJob11 = (TextView) view.findViewById(R.id.tvJob11);
        mTvJob12 = (TextView) view.findViewById(R.id.tvJob12);
        mTvJob13 = (TextView) view.findViewById(R.id.tvJob13);
        mTvJob14 = (TextView) view.findViewById(R.id.tvJob14);
        mTvJob15 = (TextView) view.findViewById(R.id.tvJob15);
        mTvJob16 = (TextView) view.findViewById(R.id.tvJob16);
        mTvJob17 = (TextView) view.findViewById(R.id.tvJob17);
        mTvJob18 = (TextView) view.findViewById(R.id.tvJob18);
        mTvJob19 = (TextView) view.findViewById(R.id.tvJob19);
        mTvJob20 = (TextView) view.findViewById(R.id.tvJob20);
        mTvJob21 = (TextView) view.findViewById(R.id.tvJob21);
        mTvJob22 = (TextView) view.findViewById(R.id.tvJob22);
        mTvJob23 = (TextView) view.findViewById(R.id.tvJob23);
        TvList.add(mTvJob0);
        TvList.add(mTvJob1);
        TvList.add(mTvJob2);
        TvList.add(mTvJob3);
        TvList.add(mTvJob4);
        TvList.add(mTvJob5);
        TvList.add(mTvJob6);
        TvList.add(mTvJob7);
        TvList.add(mTvJob8);
        TvList.add(mTvJob9);
        TvList.add(mTvJob10);
        TvList.add(mTvJob11);
        TvList.add(mTvJob12);
        TvList.add(mTvJob13);
        TvList.add(mTvJob14);
        TvList.add(mTvJob15);
        TvList.add(mTvJob16);
        TvList.add(mTvJob17);
        TvList.add(mTvJob18);
        TvList.add(mTvJob19);
        TvList.add(mTvJob20);
        TvList.add(mTvJob21);
        TvList.add(mTvJob22);
        TvList.add(mTvJob23);

        // 定点测量模式 启动流程
        sFlow = context.getResources().getStringArray(R.array.strDDCL);

        for (int i = 0; i < sFlow.length; i++) {
            if (!isHaveFlow(mCompName, sFlow[i], getConfigData(mCompName, "RANGE"), GetPlatRangSum(mCompName))) {
                sFlow[i] = context.getResources().getString(R.string.reserve);
            }
        }
        if (TempMsg.length > 1) {
            if (!TempMsg[1].equals("")) {
                String[] TempSp = TempMsg[1].split("，");
                for (int i = 0; i < TvList.size(); i++) {
                    TvList.get(i).setText(sFlow[Integer.parseInt(TempSp[i])]);
                    TvList.get(i).setOnClickListener(new TvOnClickListener());
                }
            }
        } else {
            for (int i = 0; i < TvList.size(); i++) {
                TvList.get(i).setText(sFlow[0]);
                TvList.get(i).setOnClickListener(new TvOnClickListener());
            }
        }
        sAllSetFlow = context.getResources().getStringArray(R.array.strDDCL2);
        for (int i = 0; i < sAllSetFlow.length; i++) {
            if (i > 0) {
                if (!isHaveFlow(mCompName, sAllSetFlow[i], getConfigData(mCompName, "RANGE"), GetPlatRangSum(mCompName))) {
                    sAllSetFlow[i] = context.getResources().getString(R.string.reserve);
                }
            }
        }
        mSpFlow = (Spinner) view.findViewById(R.id.spFlow);
        InitSpinner(context, mSpFlow, sAllSetFlow, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);

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

    private class cbkAllChange implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            for (int i = 0; i < CbkList.size(); i++) {
                if (!TvList.get(i).getText().toString().equals(context.getResources().getString(R.string.reserve))) {
                    CbkList.get(i).setChecked(cbkAll.isChecked());
                }
            }
        }
    }

    private class cbkChange implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (b) {
                if (TvList.get(compoundButton.getId()).getText().toString().equals(context.getResources().getString(R.string.reserve))) {
                    CbkList.get(compoundButton.getId()).setChecked(false);
                }
            }
        }
    }

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CbkList.size(); i++) {
                if (CbkList.get(i).isChecked()) {
                    // 流程不为保留
                    if (!TvList.get(i).getText().toString().equals(context.getResources().getString(R.string.reserve))) {
                        sb.append(CbkList.get(i).getText().toString() + "，");
                    }
                }
            }

            StringBuilder sp = new StringBuilder();
            for (TextView textView : TvList) {
                sp.append(getStringNum(textView.getText().toString()) + "，");
            }
            String s = sb.toString() + "&" + sp.toString();

            if (s.length() > 0) s = s.substring(0, s.length() - 1);
            setData(1, "select_time_select_job", s);
            dialog.dismiss();

        }
    }

    private int getStringNum(String flowName) {
        for (int i = 0; i < sFlow.length; i++) {
            if (flowName.equals(sFlow[i])) {
                return i;
            }
        }
        return 0;
    }


    private class BtnCancelClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();

        }
    }

    private class TvOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            mPreTv = (TextView) view;
            Dialog_select_Flow dialog_select_flow = new Dialog_select_Flow();
            Bundle bundle = new Bundle();
            bundle.putString("alert-select_Flow", String.valueOf(getStringNum(mPreTv.getText().toString())));// 传流程数组编号
            dialog_select_flow.setArguments(bundle);
            dialog_select_flow.setTargetFragment(SelectTimeSelectJob2.this, 1);
            dialog_select_flow.show(fm, "select_Flow");

        }
    }

    private class mSpListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position != 0) {
                for (TextView textView : TvList) {
                    if (!sFlow[position - 1].equals(context.getResources().getString(R.string.reserve))) {
                        textView.setText(sFlow[position - 1]);
                    } else {
                        mSpFlow.setSelection(0);
                        break;
                    }
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("select_Flow");
            if (!sFlow[Integer.parseInt(stringExtra)].equals(context.getResources().getString(R.string.reserve))) {
                mPreTv.setText(sFlow[Integer.parseInt(stringExtra)]);
            }
        }
        if (requestCode == 2) {

        }
    }
}
