package com.yzlm.cyl.cfragment.Content.Component.List4;

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

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitDoorValue;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitHigh;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitLow;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.preWinForFragment;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
/*
 * Created by zwj on 2018/08/06
 */

public class List4_Content3_pAdminParSet extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pAdminParSet fragment = null;
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

    public static List4_Content3_pAdminParSet newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pAdminParSet();
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
        return R.layout.list4_content3_padmin_par_set;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            iPoint = Integer.parseInt(getConfigData(mCompName, "YXWS"));
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p2);
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
            String strLimitValue = getConfigData(mCompName, "limitValue");
            String strLimitLow = getConfigData(mCompName, "limitLow");
            String strLimitHigh = getConfigData(mCompName, "limitHigh");
            if (strLimitValue.equals("")) {
                saveOperationLogDataModifyMsg(mCompName, "limitValue", String.valueOf(getLimitDoorValue(mCompName)), "设置limitValue", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "limitValue", String.valueOf(getLimitDoorValue(mCompName)));
            }
            if (strLimitLow.equals("")) {
                saveOperationLogDataModifyMsg(mCompName, "limitLow", String.valueOf(getLimitLow(mCompName)), "设置limitLow", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "limitLow", String.valueOf(getLimitLow(mCompName)));
            }
            if (strLimitHigh.equals("")) {
                saveOperationLogDataModifyMsg(mCompName, "limitHigh", String.valueOf(getLimitHigh(mCompName)), "设置limitHigh", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "limitHigh", String.valueOf(getLimitHigh(mCompName)));
            }
            mEdLimitValue.setText(strLimitValue.equals("") ? String.valueOf(getLimitHigh(mCompName)) : strLimitValue);
            mEdLimitLow.setText(strLimitLow.equals("") ? String.valueOf(getLimitLow(mCompName)) : strLimitLow);
            mEdLimitHigh.setText(strLimitHigh.equals("") ? String.valueOf(getLimitHigh(mCompName)) : strLimitHigh);

            String strSwitch = getConfigData(mCompName, "limitSwitch");
            if (strSwitch.equals("")) {
                updateConfigData(mCompName, "limitSwitch", "open");
                mBtnLimitSwitch.setChecked(getConfigData(mCompName, "limitSwitch").equals("open"));
            } else {
                mBtnLimitSwitch.setChecked(strSwitch.equals("open"));
            }

            // 定量下限
            String strQtLimitValue = getConfigData(mCompName, "QuantitativeLimitValue");
            String strQtLimitLow = getConfigData(mCompName, "QuantitativeLimitLow");
            String strQtLimitHigh = getConfigData(mCompName, "QuantitativeLimitHigh");
            if (strQtLimitValue.equals("")) {
                saveOperationLogDataModifyMsg(mCompName, "QuantitativeLimitValue", String.valueOf(getLimitDoorValue(mCompName)), "设置QuantitativeLimitValue", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "QuantitativeLimitValue", String.valueOf(getLimitDoorValue(mCompName)));
            }
            if (strQtLimitLow.equals("")) {
                saveOperationLogDataModifyMsg(mCompName, "QuantitativeLimitLow", String.valueOf(getLimitLow(mCompName)), "设置QuantitativeLimitLow", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "QuantitativeLimitLow", String.valueOf(getLimitLow(mCompName)));
            }
            if (strQtLimitHigh.equals("")) {
                saveOperationLogDataModifyMsg(mCompName, "QuantitativeLimitHigh", String.valueOf(getLimitHigh(mCompName)), "设置QuantitativeLimitHigh", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "QuantitativeLimitHigh", String.valueOf(getLimitHigh(mCompName)));
            }

            mEdQtLimitValue.setText(strQtLimitValue.equals("") ? String.valueOf(getLimitHigh(mCompName)) : strQtLimitValue);
            mEdQtLimitLow.setText(strQtLimitLow.equals("") ? String.valueOf(getLimitLow(mCompName)) : strQtLimitLow);
            mEdQtLimitHigh.setText(strQtLimitHigh.equals("") ? String.valueOf(getLimitHigh(mCompName)) : strQtLimitHigh);

            String strQSwitch = getConfigData(mCompName, "QuantitativeLimitSwitch");
            if (strQSwitch.equals("")) {
                updateConfigData(mCompName, "QuantitativeLimitSwitch", "open");
                mBtnQtLimitSwitch.setChecked(getConfigData(mCompName, "QuantitativeLimitSwitch").equals("open"));
            } else {
                mBtnQtLimitSwitch.setChecked(strQSwitch.equals("open"));
            }

            // 量程告警
            mBtnNowRangeSwitch.setChecked(getConfigData(mCompName, "alarmOutNowRangeSwitch").equals("open"));
            mBtnNowRangeLimitSwitch.setChecked(getConfigData(mCompName, "alarmOutNowRangeOutLimit").equals("open"));
            mBtnDevRangeSwitch.setChecked(getConfigData(mCompName, "alarmOutDevRangeSwitch").equals("open"));
            mBtnDevRangeLimitSwitch.setChecked(getConfigData(mCompName, "alarmOutDevRangeOutLimit").equals("open"));
            mEdNowRangeUpMulti.setText(getConfigData(mCompName, "nowRangeUpMulti").equals("") ? "1" : getConfigData(mCompName, "nowRangeUpMulti"));
            mEdDevRangeUpMulti.setText(getConfigData(mCompName, "devRangeUpMulti").equals("") ? "1" : getConfigData(mCompName, "devRangeUpMulti"));

            mLayoutNowRangeSet.setVisibility(getConfigData(mCompName, "alarmOutNowRangeSwitch").equals("open") ? View.VISIBLE : View.GONE);
            mLayoutDevRangeSet.setVisibility(getConfigData(mCompName, "alarmOutDevRangeSwitch").equals("open") ? View.VISIBLE : View.GONE);

            ToggleButton mWatchDogSwitch = v.findViewById(R.id.tBtnWatchDogBoard);
            mWatchDogSwitch.setOnCheckedChangeListener(new togBtnClick());
            mWatchDogSwitch.setChecked(getPublicConfigData("watchDogSwitch").equals("open"));
            LinearLayout mLayoutWatchDogSetting = v.findViewById(R.id.layout_watchDogSetting);
            if (strComponent.get(1).length == 1) {
                mLayoutWatchDogSetting.setVisibility(View.VISIBLE);
            } else {
                mLayoutWatchDogSetting.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnReturn_p2:
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
                    if (isChecked != getConfigData(mCompName, "limitSwitch").equals("open")) {
                        String strSwitch = getConfigData(mCompName, "limitSwitch").equals("open") ? "close" : "open";
                        saveOperationLogDataModifyMsg(mCompName, "limitSwitch", strSwitch, "设置limitSwitch", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "limitSwitch", strSwitch);
                        mBtnLimitSwitch.setText(strSwitch.equals("open") ? getString(R.string.opened) : getString(R.string.closeed));

                    }
                    break;
                case R.id.BtnQtLimitSwitch:
                    if (isChecked != getConfigData(mCompName, "QuantitativeLimitSwitch").equals("open")) {
                        String strSwitch = getConfigData(mCompName, "QuantitativeLimitSwitch").equals("open") ? "close" : "open";
                        saveOperationLogDataModifyMsg(mCompName, "QuantitativeLimitSwitch", strSwitch, "设置QuantitativeLimitSwitch", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "QuantitativeLimitSwitch", strSwitch);
                        mBtnQtLimitSwitch.setText(strSwitch.equals("open") ? getString(R.string.opened) : getString(R.string.closeed));
                    }
                    break;
                case R.id.tBtnNowRange:
                    if (isChecked != getConfigData(mCompName, "alarmOutNowRangeSwitch").equals("open")) {
                        String strSwitch = getConfigData(mCompName, "alarmOutNowRangeSwitch").equals("open") ? "close" : "open";
                        saveOperationLogDataModifyMsg(mCompName, "alarmOutNowRangeSwitch", strSwitch, "设置当前量程告警开关", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "alarmOutNowRangeSwitch", strSwitch);
                        mLayoutNowRangeSet.setVisibility(getConfigData(mCompName, "alarmOutNowRangeSwitch").equals("open") ? View.VISIBLE : View.GONE);
                    }
                    break;
                case R.id.tBtnNowRangeOutLimit:
                    if (isChecked != getConfigData(mCompName, "alarmOutNowRangeOutLimit").equals("open")) {
                        String strSwitch = getConfigData(mCompName, "alarmOutNowRangeOutLimit").equals("open") ? "close" : "open";
                        saveOperationLogDataModifyMsg(mCompName, "alarmOutNowRangeOutLimit", strSwitch, "设置当前量程告警出数限制开关", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "alarmOutNowRangeOutLimit", strSwitch);
                    }
                    break;
                case R.id.tBtnDevRange:
                    if (isChecked != getConfigData(mCompName, "alarmOutDevRangeSwitch").equals("open")) {
                        String strSwitch = getConfigData(mCompName, "alarmOutDevRangeSwitch").equals("open") ? "close" : "open";
                        saveOperationLogDataModifyMsg(mCompName, "alarmOutDevRangeSwitch", strSwitch, "设置仪表量程告警开关", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "alarmOutDevRangeSwitch", strSwitch);
                        mLayoutDevRangeSet.setVisibility(getConfigData(mCompName, "alarmOutDevRangeSwitch").equals("open") ? View.VISIBLE : View.GONE);
                    }
                    break;
                case R.id.tBtnDevRangeOutLimit:
                    if (isChecked != getConfigData(mCompName, "alarmOutDevRangeOutLimit").equals("open")) {
                        String strSwitch = getConfigData(mCompName, "alarmOutDevRangeOutLimit").equals("open") ? "close" : "open";
                        saveOperationLogDataModifyMsg(mCompName, "alarmOutDevRangeOutLimit", strSwitch, "设置仪表量程告警出数限制开关", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "alarmOutDevRangeOutLimit", strSwitch);
                    }
                    break;
                case R.id.tBtnWatchDogBoard:
                    if (isChecked != getPublicConfigData("watchDogSwitch").equals("open")) {
                        String strSwitch = getPublicConfigData("watchDogSwitch").equals("open") ? "close" : "open";
                        saveOperationLogPublicDataModifyMsg("公共", "watchDogSwitch", isChecked ? "open" : "close", "看门狗开关", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("watchDogSwitch", strSwitch);
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
                            saveOperationLogDataModifyMsg(mCompName, "limitValue", strLimitValue, "设置limitValue", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "limitValue", strLimitValue);
                            mEdLimitValue.setText(strLimitValue);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdLimitValue.setText(getConfigData(mCompName, "limitValue").equals("") ? "" : getConfigData(mCompName, "limitValue"));
                        }
                        break;
                    case R.id.eTLimitLow:
                        String strLow = mEdLimitLow.getText().toString();
                        if (!strLow.equals("") && Double.parseDouble(strLow) != 0.0 &&
                                (Double.parseDouble(strLow) > -99999.0 && Double.parseDouble(strLow) < 99999.0)
                                && (Double.parseDouble(strLow) < Double.parseDouble(mEdLimitHigh.getText().toString()))) {

                            strLow = editDataFormat(mEdLimitLow.getText().toString(), iPoint);
                            saveOperationLogDataModifyMsg(mCompName, "limitLow", strLow, "设置limitLow", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "limitLow", strLow);
                            mEdLimitLow.setText(strLow);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdLimitLow.setText(getConfigData(mCompName, "limitLow").equals("") ? "0.05" : getConfigData(mCompName, "limitLow"));
                        }
                        break;
                    case R.id.eTLimitHigh:
                        String strHigh = mEdLimitHigh.getText().toString();
                        if (!strHigh.equals("") && Double.parseDouble(strHigh) != 0.0
                                && (Double.parseDouble(strHigh) > -99999.0 && Double.parseDouble(strHigh) < 99999.0)
                                && (Double.parseDouble(strHigh) > Double.parseDouble(mEdLimitLow.getText().toString()))) {

                            strHigh = editDataFormat(mEdLimitHigh.getText().toString(), iPoint);
                            saveOperationLogDataModifyMsg(mCompName, "limitHigh", strHigh, "设置limitHigh", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "limitHigh", strHigh);
                            mEdLimitHigh.setText(strHigh);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdLimitHigh.setText(getConfigData(mCompName, "limitHigh").equals("") ? "0.2" : getConfigData(mCompName, "limitHigh"));
                        }
                        break;
                    case R.id.eTQtLimitValue:
                        String strQtLimitValue = mEdQtLimitValue.getText().toString();
                        if (!strQtLimitValue.equals("") && Double.parseDouble(strQtLimitValue) != 0.0 &&
                                (Double.parseDouble(strQtLimitValue) > -99999.0 && Double.parseDouble(strQtLimitValue) < 99999.0)) {

                            strQtLimitValue = editDataFormat(mEdQtLimitValue.getText().toString(), iPoint);
                            saveOperationLogDataModifyMsg(mCompName, "QuantitativeLimitValue", strQtLimitValue, "设置QuantitativeLimitValue", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "QuantitativeLimitValue", strQtLimitValue);
                            mEdQtLimitValue.setText(strQtLimitValue);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdQtLimitValue.setText(getConfigData(mCompName, "QuantitativeLimitValue").equals("") ? "" : getConfigData(mCompName, "QuantitativeLimitValue"));
                        }
                        break;
                    case R.id.eTQtLimitLow:
                        String strQtLow = mEdQtLimitLow.getText().toString();
                        if (!strQtLow.equals("") && Double.parseDouble(strQtLow) != 0.0 &&
                                (Double.parseDouble(strQtLow) > -99999.0 && Double.parseDouble(strQtLow) < 99999.0)
                                && (Double.parseDouble(strQtLow) < Double.parseDouble(mEdQtLimitHigh.getText().toString()))) {

                            strQtLow = editDataFormat(mEdQtLimitLow.getText().toString(), iPoint);
                            saveOperationLogDataModifyMsg(mCompName, "QuantitativeLimitLow", strQtLow, "设置QuantitativeLimitLow", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "QuantitativeLimitLow", strQtLow);
                            mEdQtLimitLow.setText(strQtLow);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdQtLimitLow.setText(getConfigData(mCompName, "QuantitativeLimitLow").equals("") ? "0.05" : getConfigData(mCompName, "QuantitativeLimitLow"));
                        }
                        break;
                    case R.id.eTQtLimitHigh:
                        String strQtHigh = mEdQtLimitHigh.getText().toString();
                        if (!strQtHigh.equals("") && Double.parseDouble(strQtHigh) != 0.0
                                && (Double.parseDouble(strQtHigh) > -99999.0 && Double.parseDouble(strQtHigh) < 99999.0)
                                && (Double.parseDouble(strQtHigh) > Double.parseDouble(mEdQtLimitLow.getText().toString()))) {

                            strQtHigh = editDataFormat(mEdQtLimitHigh.getText().toString(), iPoint);
                            saveOperationLogDataModifyMsg(mCompName, "QuantitativeLimitHigh", strQtHigh, "设置QuantitativeLimitHigh", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "QuantitativeLimitHigh", strQtHigh);
                            mEdQtLimitHigh.setText(strQtHigh);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " (-99999.0~99999.0)" + getString(R.string.and) + "≠0!", Toast.LENGTH_SHORT).show();
                            mEdQtLimitHigh.setText(getConfigData(mCompName, "QuantitativeLimitHigh").equals("") ? "0.2" : getConfigData(mCompName, "QuantitativeLimitHigh"));
                        }
                        break;
                    case R.id.eTLimitRange:
                        String strValue = mEdNowRangeUpMulti.getText().toString();
                        if (!strValue.equals("") && ((Double.parseDouble(strValue) >= 1.0 && Double.parseDouble(strValue) <= 3.0))) {

                            strValue = editDataFormat(mEdNowRangeUpMulti.getText().toString(), iPoint);
                            saveOperationLogDataModifyMsg(mCompName, "nowRangeUpMulti", strValue, "设置量程告警系数", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "nowRangeUpMulti", strValue);
                            mEdNowRangeUpMulti.setText(strValue);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " [1~3]!", Toast.LENGTH_SHORT).show();
                            mEdNowRangeUpMulti.setText(getConfigData(mCompName, "nowRangeUpMulti").equals("") ? "1" : getConfigData(mCompName, "nowRangeUpMulti"));
                        }
                        break;
                    case R.id.eTLimitDevRange:
                        String strValue2 = mEdDevRangeUpMulti.getText().toString();
                        if (!strValue2.equals("") && ((Double.parseDouble(strValue2) >= 1.0 && Double.parseDouble(strValue2) <= 3.0))) {
                            strValue2 = editDataFormat(mEdDevRangeUpMulti.getText().toString(), iPoint);
                            saveOperationLogDataModifyMsg(mCompName, "devRangeUpMulti", strValue2, "设置仪表量程告警系数", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "devRangeUpMulti", strValue2);
                            mEdDevRangeUpMulti.setText(strValue2);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " [1~3]!", Toast.LENGTH_SHORT).show();
                            mEdDevRangeUpMulti.setText(getConfigData(mCompName, "devRangeUpMulti").equals("") ? "1" : getConfigData(mCompName, "devRangeUpMulti"));
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
