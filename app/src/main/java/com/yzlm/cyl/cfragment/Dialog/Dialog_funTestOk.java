package com.yzlm.cyl.cfragment.Dialog;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionStep;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.util.Map;

import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setComponentEditIntData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;

/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_funTestOk extends ZDialogFragment {
    private Button Bth_high_low;
    private String Command;
    private String Component;
    private byte measurement;
    private byte sampleCount;
    private View v;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_fun_test_ok;
    }

    @Override
    protected void InitUI(View view) {
        LinearLayout linear_show = (LinearLayout) view.findViewById(R.id.Linear_show);
        v = view;

        Bth_high_low = (Button) view.findViewById(R.id.Bth_high_low);
        Bth_high_low.setOnClickListener(new BtnHighLowClick());

        EditText eNumber = (EditText) view.findViewById(R.id.eNumber);
        eNumber.setOnFocusChangeListener(new editFocusChangeComp());
        eNumber.setOnEditorActionListener(new editFocusChangeComp());

        Command = getPrompt("alert-ok");
        if (getPrompt("alert-windows").equals("是")) {
            measurement = Byte.parseByte(getPrompt("alert-high-low"));
            sampleCount = Byte.parseByte(getPrompt("alert-number"));
            if (measurement == 0 || measurement == 1) {
                linear_show.setVisibility(View.VISIBLE);

                setHighLowShow(measurement);
                eNumber.setText(getPrompt("alert-number"));
            } else {
                linear_show.setVisibility(View.GONE);
            }
        } else {
            linear_show.setVisibility(View.GONE);
        }
        Component = MainActivity.mCompName;
        TextView mD_text = (TextView) view.findViewById(R.id.D_text);
        mD_text.setText(getString(R.string.component) + "[" + Component + "] " + getString(R.string.whether_if) + " " + Command + "？");


        Button mBtnD_OK = (Button) view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_Cancel);
        mBtnD_Cancel.setOnClickListener(new BtnCancelClick());
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

    private class editFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    switch (v.getId()) {
                        case R.id.eNumber: {
                            int iPoint = 2;
                            setComponentEditIntData(mCompName,(EditText) v, iPoint, 1, 10, 1, "进零样次数");
                            sampleCount = Byte.parseByte(((EditText) v).getText().toString());
                            Map<Integer, ActionStep> ft = Global.getFunctionalTest(MainActivity.mCompName);
                            ft.get(Integer.parseInt(getPrompt("alert-ID"))).setSampleCount(sampleCount);
                            syncList();
                        }break;
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.set_value_is_error), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return true;
            }
            return false;
        }

    }

    private class BtnHighLowClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            setData(1, "Dialog_Command", Component + "_" + Command);
//            dialog.dismiss();

            if (measurement == 1) {
                measurement = 0;
            } else {
                measurement = 1;
            }
            setHighLowShow(measurement);
        }
    }

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(1, "Dialog_Command", Component + "_" + Command);
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

    private void setHighLowShow(byte falg) {
        Map<Integer, ActionStep> ft = Global.getFunctionalTest(MainActivity.mCompName);
        ft.get(Integer.parseInt(getPrompt("alert-ID"))).setMeasurement(falg);
        if (falg == 1) {
            Bth_high_low.setText(getString(R.string.high));
        } else {
            Bth_high_low.setText(getString(R.string.low));
        }
    }
    private void syncList() {
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 2);
            v.clearFocus();
        }
        FullWindows(mActivityWindow);
    }
}
