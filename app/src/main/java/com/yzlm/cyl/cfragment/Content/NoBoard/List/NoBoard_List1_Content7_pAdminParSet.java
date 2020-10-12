package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitDoorValue;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitHigh;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitLow;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.preWinForFragment;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;


/*
 * Created by zwj on 2018/08/06
 */

public class NoBoard_List1_Content7_pAdminParSet extends SubFragment {
    private Callbacks mCallbacks;
    private static NoBoard_List1_Content7_pAdminParSet fragment = null;
    private EditText mEdLimitLow;
    private EditText mEdLimitHigh;
    private EditText mEdNowRangeUpMulti;
    private EditText mEdDevRangeUpMulti;
    private EditText mEdQtLimitLow;
    private EditText mEdQtLimitHigh;
    private EditText mEdLimitValue;
    private EditText mEdQtLimitValue;

    private ToggleButton mBtnLimitSwitch;
    private ToggleButton mBtnQtLimitSwitch;
    private LinearLayout mLayoutDevRangeSet;
    private LinearLayout mLayoutNowRangeSet;
    private int iPoint;

    public static NoBoard_List1_Content7_pAdminParSet newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content7_pAdminParSet();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.no_board_list1_content7_padmin_par_set;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            iPoint = Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS"));
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnNoBoardReturn_p2);
            mBtnReturn.setOnClickListener(new btnClick());
            if (preWinForFragment.contains("password")) {
                mBtnReturn.setVisibility(View.GONE);
            } else {
                mBtnReturn.setVisibility(View.VISIBLE);
            }
            mEdLimitValue = (EditText) v.findViewById(R.id.eTLimitValue);
            mEdLimitLow = (EditText) v.findViewById(R.id.eTLimitLow);
            mEdLimitHigh = (EditText) v.findViewById(R.id.eTLimitHigh);
            mBtnLimitSwitch = (ToggleButton) v.findViewById(R.id.BtnLimitSwitch);
            mEdNowRangeUpMulti = (EditText) v.findViewById(R.id.eTLimitRange);
            mEdDevRangeUpMulti = (EditText) v.findViewById(R.id.eTLimitDevRange);
            ToggleButton mBtnNowRangeSwitch = (ToggleButton) v.findViewById(R.id.tBtnNowRange);
            ToggleButton mBtnNowRangeLimitSwitch = (ToggleButton) v.findViewById(R.id.tBtnNowRangeOutLimit);
            ToggleButton mBtnDevRangeSwitch = (ToggleButton) v.findViewById(R.id.tBtnDevRange);
            ToggleButton mBtnDevRangeLimitSwitch = (ToggleButton) v.findViewById(R.id.tBtnDevRangeOutLimit);
            mLayoutNowRangeSet = (LinearLayout) v.findViewById(R.id.mLayout_NowRangeSet);
            mLayoutDevRangeSet = (LinearLayout) v.findViewById(R.id.mLayout_DevRangeSet);
            mEdQtLimitValue = (EditText) v.findViewById(R.id.eTQtLimitValue);
            mEdQtLimitLow = (EditText) v.findViewById(R.id.eTQtLimitLow);
            mEdQtLimitHigh = (EditText) v.findViewById(R.id.eTQtLimitHigh);
            mBtnQtLimitSwitch = (ToggleButton) v.findViewById(R.id.BtnQtLimitSwitch);
            mEdLimitValue.setOnFocusChangeListener(new EdFocusChange());
            mEdLimitValue.setOnEditorActionListener(new EtFocusChange());
            mEdLimitLow.setOnFocusChangeListener(new EdFocusChange());
            mEdLimitLow.setOnEditorActionListener(new EtFocusChange());
            mEdLimitHigh.setOnFocusChangeListener(new EdFocusChange());
            mEdLimitHigh.setOnEditorActionListener(new EtFocusChange());
            mEdNowRangeUpMulti.setOnFocusChangeListener(new EdFocusChange());
            mEdNowRangeUpMulti.setOnEditorActionListener(new EtFocusChange());
            mEdDevRangeUpMulti.setOnFocusChangeListener(new EdFocusChange());
            mEdDevRangeUpMulti.setOnEditorActionListener(new EtFocusChange());
            mEdQtLimitValue.setOnFocusChangeListener(new EdFocusChange());
            mEdQtLimitValue.setOnEditorActionListener(new EtFocusChange());
            mEdQtLimitLow.setOnFocusChangeListener(new EdFocusChange());
            mEdQtLimitLow.setOnEditorActionListener(new EtFocusChange());
            mEdQtLimitHigh.setOnFocusChangeListener(new EdFocusChange());
            mEdQtLimitHigh.setOnEditorActionListener(new EtFocusChange());
            mBtnLimitSwitch.setOnCheckedChangeListener(new togBtnClick());
            mBtnQtLimitSwitch.setOnCheckedChangeListener(new togBtnClick());
            mBtnNowRangeSwitch.setOnCheckedChangeListener(new togBtnClick());
            mBtnNowRangeLimitSwitch.setOnCheckedChangeListener(new togBtnClick());
            mBtnDevRangeSwitch.setOnCheckedChangeListener(new togBtnClick());
            mBtnDevRangeLimitSwitch.setOnCheckedChangeListener(new togBtnClick());

            // 检出限
            String strLimitValue = getNoBoardConfigData(mNoBoardCompName, "limitValue");
            String strLimitLow = getNoBoardConfigData(mNoBoardCompName, "limitLow");
            String strLimitHigh = getNoBoardConfigData(mNoBoardCompName, "limitHigh");
            if (strLimitValue.equals("")) {
                saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "limitValue", String.valueOf(getLimitDoorValue(mNoBoardCompName)), "设置limitValue", ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "limitValue", String.valueOf(getLimitDoorValue(mNoBoardCompName)));
            }
            if (strLimitLow.equals("")) {
                saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "limitLow", String.valueOf(getLimitLow(mNoBoardCompName)), "设置limitLow", ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "limitLow", String.valueOf(getLimitLow(mNoBoardCompName)));
            }
            if (strLimitHigh.equals("")) {
                saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "limitHigh", String.valueOf(getLimitHigh(mNoBoardCompName)), "设置limitHigh", ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "limitHigh", String.valueOf(getLimitHigh(mNoBoardCompName)));
            }
            mEdLimitValue.setText(strLimitValue.equals("") ? String.valueOf(getLimitHigh(mNoBoardCompName)) : strLimitValue);
            mEdLimitLow.setText(strLimitLow.equals("") ? String.valueOf(getLimitLow(mNoBoardCompName)) : strLimitLow);
            mEdLimitHigh.setText(strLimitHigh.equals("") ? String.valueOf(getLimitHigh(mNoBoardCompName)) : strLimitHigh);

            String strSwitch = getNoBoardConfigData(mNoBoardCompName, "limitSwitch");
            if (strSwitch.equals("")) {
                updateNoBoardConfigData(mNoBoardCompName, "limitSwitch", "open");
                mBtnLimitSwitch.setChecked(getNoBoardConfigData(mNoBoardCompName, "limitSwitch").equals("open"));
            } else {
                mBtnLimitSwitch.setChecked(strSwitch.equals("open"));
            }

            // 定量下限
            String strQtLimitValue = getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitValue");
            String strQtLimitLow = getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitLow");
            String strQtLimitHigh = getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitHigh");
            if (strQtLimitValue.equals("")) {
                saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "QuantitativeLimitValue", String.valueOf(getLimitDoorValue(mNoBoardCompName)), "设置QuantitativeLimitValue", ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitValue", String.valueOf(getLimitDoorValue(mNoBoardCompName)));
            }
            if (strQtLimitLow.equals("")) {
                saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "QuantitativeLimitLow", String.valueOf(getLimitLow(mNoBoardCompName)), "设置QuantitativeLimitLow", ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitLow", String.valueOf(getLimitLow(mNoBoardCompName)));
            }
            if (strQtLimitHigh.equals("")) {
                saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "QuantitativeLimitHigh", String.valueOf(getLimitHigh(mNoBoardCompName)), "设置QuantitativeLimitHigh", ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitHigh", String.valueOf(getLimitHigh(mNoBoardCompName)));
            }

            mEdQtLimitValue.setText(strQtLimitValue.equals("") ? String.valueOf(getLimitHigh(mNoBoardCompName)) : strQtLimitValue);
            mEdQtLimitLow.setText(strQtLimitLow.equals("") ? String.valueOf(getLimitLow(mNoBoardCompName)) : strQtLimitLow);
            mEdQtLimitHigh.setText(strQtLimitHigh.equals("") ? String.valueOf(getLimitHigh(mNoBoardCompName)) : strQtLimitHigh);

            String strQSwitch = getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitSwitch");
            if (strQSwitch.equals("")) {
                updateNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitSwitch", "open");
                mBtnQtLimitSwitch.setChecked(getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitSwitch").equals("open"));
            } else {
                mBtnQtLimitSwitch.setChecked(strQSwitch.equals("open"));
            }

            // 量程告警
            mBtnNowRangeSwitch.setChecked(getNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeSwitch").equals("open"));
            mBtnNowRangeLimitSwitch.setChecked(getNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeOutLimit").equals("open"));
            mBtnDevRangeSwitch.setChecked(getNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeSwitch").equals("open"));
            mBtnDevRangeLimitSwitch.setChecked(getNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeOutLimit").equals("open"));
            mEdNowRangeUpMulti.setText(getNoBoardConfigData(mNoBoardCompName, "nowRangeUpMulti").equals("") ? "1" : getNoBoardConfigData(mNoBoardCompName, "nowRangeUpMulti"));
            mEdDevRangeUpMulti.setText(getNoBoardConfigData(mNoBoardCompName, "devRangeUpMulti").equals("") ? "1" : getNoBoardConfigData(mNoBoardCompName, "devRangeUpMulti"));

            mLayoutNowRangeSet.setVisibility(getNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeSwitch").equals("open") ? View.VISIBLE : View.GONE);
            mLayoutDevRangeSet.setVisibility(getNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeSwitch").equals("open") ? View.VISIBLE : View.GONE);

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]" + e.toString());
        }

    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnNoBoardReturn_p2:
                    mCallbacks.onListSelected(v);
                    break;

            }
        }
    }

    private class togBtnClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.BtnLimitSwitch:
                    if (isChecked != getNoBoardConfigData(mNoBoardCompName, "limitSwitch").equals("open")) {
                        String strSwitch = getNoBoardConfigData(mNoBoardCompName, "limitSwitch").equals("open") ? "close" : "open";
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "limitSwitch", strSwitch, "设置limitSwitch", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "limitSwitch", strSwitch);
                        mBtnLimitSwitch.setText(strSwitch.equals("open") ? getString(R.string.opened) : getString(R.string.closeed));

                    }
                    break;
                case R.id.BtnQtLimitSwitch:
                    if (isChecked != getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitSwitch").equals("open")) {
                        String strSwitch = getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitSwitch").equals("open") ? "close" : "open";
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "QuantitativeLimitSwitch", strSwitch, "设置QuantitativeLimitSwitch", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitSwitch", strSwitch);
                        mBtnQtLimitSwitch.setText(strSwitch.equals("open") ? getString(R.string.opened) : getString(R.string.closeed));
                    }
                    break;
                case R.id.tBtnNowRange:
                    if (isChecked != getNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeSwitch").equals("open")) {
                        String strSwitch = getNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeSwitch").equals("open") ? "close" : "open";
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "alarmOutNowRangeSwitch", strSwitch, "设置当前量程告警开关", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeSwitch", strSwitch);
                        mLayoutNowRangeSet.setVisibility(getNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeSwitch").equals("open") ? View.VISIBLE : View.GONE);
                    }
                    break;
                case R.id.tBtnNowRangeOutLimit:
                    if (isChecked != getNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeOutLimit").equals("open")) {
                        String strSwitch = getNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeOutLimit").equals("open") ? "close" : "open";
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "alarmOutNowRangeOutLimit", strSwitch, "设置当前量程告警出数限制开关", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "alarmOutNowRangeOutLimit", strSwitch);
                    }
                    break;
                case R.id.tBtnDevRange:
                    if (isChecked != getNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeSwitch").equals("open")) {
                        String strSwitch = getNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeSwitch").equals("open") ? "close" : "open";
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "alarmOutDevRangeSwitch", strSwitch, "设置仪表量程告警开关", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeSwitch", strSwitch);
                        mLayoutDevRangeSet.setVisibility(getNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeSwitch").equals("open") ? View.VISIBLE : View.GONE);
                    }
                    break;
                case R.id.tBtnDevRangeOutLimit:
                    if (isChecked != getNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeOutLimit").equals("open")) {
                        String strSwitch = getNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeOutLimit").equals("open") ? "close" : "open";
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "alarmOutDevRangeOutLimit", strSwitch, "设置仪表量程告警出数限制开关", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "alarmOutDevRangeOutLimit", strSwitch);
                    }
                    break;
            }
        }
    }

    private class EtFocusChange implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return true;
            }
            return false;
        }
    }

    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class EdFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTLimitValue:
                        String strLimitValue = mEdLimitValue.getText().toString();
                        if (!strLimitValue.equals("") && Double.parseDouble(strLimitValue) != 0.0 &&
                                (Double.parseDouble(strLimitValue) > -99999.0 && Double.parseDouble(strLimitValue) < 99999.0)) {

                            strLimitValue = editDataFormat(mEdLimitValue.getText().toString(), iPoint);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "limitValue", strLimitValue, "设置limitValue", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "limitValue", strLimitValue);
                            mEdLimitValue.setText(strLimitValue);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdLimitValue.setText(getNoBoardConfigData(mNoBoardCompName, "limitValue").equals("") ? "" : getNoBoardConfigData(mNoBoardCompName, "limitValue"));
                        }
                        break;
                    case R.id.eTLimitLow:
                        String strLow = mEdLimitLow.getText().toString();
                        if (!strLow.equals("") && Double.parseDouble(strLow) != 0.0 &&
                                (Double.parseDouble(strLow) > -99999.0 && Double.parseDouble(strLow) < 99999.0)
                                && (Double.parseDouble(strLow) < Double.parseDouble(mEdLimitHigh.getText().toString()))) {

                            strLow = editDataFormat(mEdLimitLow.getText().toString(), iPoint);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "limitLow", strLow, "设置limitLow", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "limitLow", strLow);
                            mEdLimitLow.setText(strLow);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdLimitLow.setText(getNoBoardConfigData(mNoBoardCompName, "limitLow").equals("") ? "0.05" : getNoBoardConfigData(mNoBoardCompName, "limitLow"));
                        }
                        break;
                    case R.id.eTLimitHigh:
                        String strHigh = mEdLimitHigh.getText().toString();
                        if (!strHigh.equals("") && Double.parseDouble(strHigh) != 0.0
                                && (Double.parseDouble(strHigh) > -99999.0 && Double.parseDouble(strHigh) < 99999.0)
                                && (Double.parseDouble(strHigh) > Double.parseDouble(mEdLimitLow.getText().toString()))) {

                            strHigh = editDataFormat(mEdLimitHigh.getText().toString(), iPoint);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "limitHigh", strHigh, "设置limitHigh", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "limitHigh", strHigh);
                            mEdLimitHigh.setText(strHigh);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdLimitHigh.setText(getNoBoardConfigData(mNoBoardCompName, "limitHigh").equals("") ? "0.2" : getNoBoardConfigData(mNoBoardCompName, "limitHigh"));
                        }
                        break;
                    case R.id.eTQtLimitValue:
                        String strQtLimitValue = mEdQtLimitValue.getText().toString();
                        if (!strQtLimitValue.equals("") && Double.parseDouble(strQtLimitValue) != 0.0 &&
                                (Double.parseDouble(strQtLimitValue) > -99999.0 && Double.parseDouble(strQtLimitValue) < 99999.0)) {

                            strQtLimitValue = editDataFormat(mEdQtLimitValue.getText().toString(), iPoint);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "QuantitativeLimitValue", strQtLimitValue, "设置QuantitativeLimitValue", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitValue", strQtLimitValue);
                            mEdQtLimitValue.setText(strQtLimitValue);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdQtLimitValue.setText(getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitValue").equals("") ? "" : getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitValue"));
                        }
                        break;
                    case R.id.eTQtLimitLow:
                        String strQtLow = mEdQtLimitLow.getText().toString();
                        if (!strQtLow.equals("") && Double.parseDouble(strQtLow) != 0.0 &&
                                (Double.parseDouble(strQtLow) > -99999.0 && Double.parseDouble(strQtLow) < 99999.0)
                                && (Double.parseDouble(strQtLow) < Double.parseDouble(mEdQtLimitHigh.getText().toString()))) {

                            strQtLow = editDataFormat(mEdQtLimitLow.getText().toString(), iPoint);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "QuantitativeLimitLow", strQtLow, "设置QuantitativeLimitLow", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitLow", strQtLow);
                            mEdQtLimitLow.setText(strQtLow);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdQtLimitLow.setText(getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitLow").equals("") ? "0.05" : getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitLow"));
                        }
                        break;
                    case R.id.eTQtLimitHigh:
                        String strQtHigh = mEdQtLimitHigh.getText().toString();
                        if (!strQtHigh.equals("") && Double.parseDouble(strQtHigh) != 0.0
                                && (Double.parseDouble(strQtHigh) > -99999.0 && Double.parseDouble(strQtHigh) < 99999.0)
                                && (Double.parseDouble(strQtHigh) > Double.parseDouble(mEdQtLimitLow.getText().toString()))) {

                            strQtHigh = editDataFormat(mEdQtLimitHigh.getText().toString(), iPoint);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "QuantitativeLimitHigh", strQtHigh, "设置QuantitativeLimitHigh", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitHigh", strQtHigh);
                            mEdQtLimitHigh.setText(strQtHigh);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdQtLimitHigh.setText(getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitHigh").equals("") ? "0.2" : getNoBoardConfigData(mNoBoardCompName, "QuantitativeLimitHigh"));
                        }
                        break;
                    case R.id.eTLimitRange:
                        String strValue = mEdNowRangeUpMulti.getText().toString();
                        if (!strValue.equals("") && ((Double.parseDouble(strValue) >= 1.0 && Double.parseDouble(strValue) <= 3.0))) {

                            strValue = editDataFormat(mEdNowRangeUpMulti.getText().toString(), iPoint);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "nowRangeUpMulti", strValue, "设置量程告警系数", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "nowRangeUpMulti", strValue);
                            mEdNowRangeUpMulti.setText(strValue);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " [1~3]!", Toast.LENGTH_SHORT).show();
                            mEdNowRangeUpMulti.setText(getNoBoardConfigData(mNoBoardCompName, "nowRangeUpMulti").equals("") ? "1" : getNoBoardConfigData(mNoBoardCompName, "nowRangeUpMulti"));
                        }
                        break;
                    case R.id.eTLimitDevRange:
                        String strValue2 = mEdDevRangeUpMulti.getText().toString();
                        if (!strValue2.equals("") && ((Double.parseDouble(strValue2) >= 1.0 && Double.parseDouble(strValue2) <= 3.0))) {
                            strValue2 = editDataFormat(mEdDevRangeUpMulti.getText().toString(), iPoint);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "devRangeUpMulti", strValue2, "设置仪表量程告警系数", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "devRangeUpMulti", strValue2);
                            mEdDevRangeUpMulti.setText(strValue2);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " [1~3]!", Toast.LENGTH_SHORT).show();
                            mEdDevRangeUpMulti.setText(getNoBoardConfigData(mNoBoardCompName, "devRangeUpMulti").equals("") ? "1" : getNoBoardConfigData(mNoBoardCompName, "devRangeUpMulti"));
                        }
                        break;
                }
                syncList();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            mCallbacks.onDialogRS();
        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }

}
