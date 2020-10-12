package com.yzlm.cyl.cfragment.Content.Component.List2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Control.CSeekBarPressure;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setComponentEditFloatData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/*
 * Created by caoyiliang on 2016/10/28.
 */
public class List2_Content4 extends SubFragment {
    private TextView mTvBJXX;
    private TextView mTvBJSX;
    private TextView mTbhnd_unit;
    private TextView mtvAlarmTUnit;
    private Spinner mSpRelayConf;
    private Spinner mSpcldw;
    private ToggleButton btnPrinter;
    private ToggleButton btnCalHit;
    private ToggleButton mBtnOneKeyCal;
    private ToggleButton togBtnBHPD;
    private EditText mEtyxws;
    private EditText mEtAlarmTime;
    private EditText mEtBHND;
    private double value;
    private String strUnit;
    private int point = 2;
    private static List2_Content4 fragment = null;
    private LinearLayout mLayoutAlarm;
    private LinearLayout mLayout_bhpdAll;
    private float preProgressHigh = 0;
    private float preProgressLow = 0;
    private float yblcl = 0;

    public static List2_Content4 newInstance() {
        if (fragment == null) {
            fragment = new List2_Content4();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list2_content4;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        try {
            CSeekBarPressure mCSeekBarPressure = (CSeekBarPressure) v.findViewById(R.id.CSBP);
            mCSeekBarPressure.setOnSeekBarChangeListener(new mCSBPChange());
            mLayoutAlarm = (LinearLayout) v.findViewById(R.id.layoutAlarm);
            mLayout_bhpdAll = v.findViewById(R.id.layout_BHPD_ALL);
            strUnit = getConfigData(mCompName, "UNIT");
            point = Integer.parseInt(getConfigData(mCompName, "YXWS"));
            float yblch = Float.valueOf(getConfigData(mCompName, "YBLCH"));
            yblcl = Float.valueOf(getConfigData(mCompName, "YBLCL"));
            float alarmH = Float.valueOf(getConfigData(mCompName, "ALARM_H"));
            float alarmL = Float.valueOf(getConfigData(mCompName, "ALARM_L"));


            value = (double) (yblch - yblcl) / 100.0;
            if (value != 0) {

                preProgressHigh = (float) ((alarmH - yblcl) / value);
                preProgressLow = (float) ((alarmL - yblcl) / value);
                mCSeekBarPressure.setProgressHigh((alarmH - yblcl) / value);
                mCSeekBarPressure.setProgressLow((alarmL - yblcl) / value);

                mCSeekBarPressure.isShowValue();
            }
            mTvBJXX = (TextView) v.findViewById(R.id.tvBJXX);
            mTvBJSX = (TextView) v.findViewById(R.id.tvBJSX);
            String strBjxx = String.format("%." + point + "f ", alarmL);
            String strBjsx = String.format("%." + point + "f ", alarmH);


            mTvBJXX.setText(getString(R.string.low_alarm) + ": " + (ConvertUnit("mg/L", strUnit, strBjxx, point)) + strUnit);
            mTvBJSX.setText(getString(R.string.high_alarm) + ": " + (ConvertUnit("mg/L", strUnit, strBjsx, point)) + strUnit);

            mSpRelayConf = (Spinner) v.findViewById(R.id.spjdqpz);
            try {
                String[] arrList = getResources().getStringArray(R.array.jdqpz);
                List<String> lstNotShow = new ArrayList<>();//不显示的
                if (!QueryMeasBoardType(mCompName).equals("2")) {
                    lstNotShow.add("开采样R2");
                    if (getPublicConfigData("SYS_RELAYCONF").equals("3")) {
                        updatePublicConfigData("SYS_RELAYCONF", "0");
                    }
                }
                InitSpinner(context, mSpRelayConf, arrList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black, lstNotShow);
                int mSpRelayConfSelect = Integer.parseInt(getPublicConfigData("SYS_RELAYCONF"));
                if (mSpRelayConfSelect > arrList.length - lstNotShow.size()) {
                    mSpRelayConfSelect -= lstNotShow.size();
                }
                mSpRelayConf.setSelection(mSpRelayConfSelect);
                mSpRelayConf.setOnItemSelectedListener(new mSpOnClickListener());
            } catch (Exception e) {
                Log.i("except", e.toString());
            }
            mSpcldw = (Spinner) v.findViewById(R.id.spcldw);
            String[] ArrayItems = getResources().getStringArray(R.array.cldw);
            if(QueryMeasCateg(mCompName).equals("13")){
                ArrayItems = getResources().getStringArray(R.array.cldw_swdx);
            }
            InitSpinner(context, mSpcldw, ArrayItems, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);

            for (int i = 0; i < ArrayItems.length; i++) {
                if (strUnit.equals(ArrayItems[i])) {
                    mSpcldw.setSelection(i);
                }
            }
            mSpcldw.setOnItemSelectedListener(new mSpOnClickListener());

            mEtyxws = (EditText) v.findViewById(R.id.eTyxws);
            mEtyxws.setText(String.valueOf(point));
            mEtyxws.setOnFocusChangeListener(new EditorClickListener());
            mEtyxws.setOnEditorActionListener(new EditorClickListener());

            mEtAlarmTime = (EditText) v.findViewById(R.id.eTAlarmTime);
            mEtAlarmTime.setText(getConfigData(mCompName, "ALARM_TIME"));
            mEtAlarmTime.setOnFocusChangeListener(new EditorClickListener());
            mEtAlarmTime.setOnEditorActionListener(new EditorClickListener());


            mtvAlarmTUnit = (TextView) v.findViewById(R.id.tvAlarmTime);
            RelayCfgDisplayShow();

            btnPrinter = (ToggleButton) v.findViewById(R.id.tBtnSZDY);
            btnPrinter.setOnCheckedChangeListener(new togBtnClickListener());
            btnPrinter.setChecked(getConfigData(mCompName, "PRINTER").equals("true"));

            btnCalHit = (ToggleButton) v.findViewById(R.id.tBtnJZTS);
            btnCalHit.setOnCheckedChangeListener(new togBtnClickListener());
            btnCalHit.setChecked(getConfigData(mCompName, "CALIBRATION_HIT").equals("true"));

            mBtnOneKeyCal = (ToggleButton) v.findViewById(R.id.tBtnOneKeyCalSwitch);
            mBtnOneKeyCal.setOnCheckedChangeListener(new togBtnClickListener());
            mBtnOneKeyCal.setChecked(getConfigData(mCompName, "OneKeyCalSwitch").equals("open"));

            LinearLayout mLayoutOneKeyCalSwitch = (LinearLayout) v.findViewById(R.id.layout_oneKeyCalSwitch);
            if (getConfigData(mCompName, "OneKeyCalX").equals("") || GetPlatRangSum(mCompName) < 2) {
                mLayoutOneKeyCalSwitch.setVisibility(View.GONE);
            } else {
                mLayoutOneKeyCalSwitch.setVisibility(View.VISIBLE);
            }

            Spinner mSpOutMeaDataShowMode = (Spinner) v.findViewById(R.id.spOutMeaDataShowMode);
            InitSpinner(context, mSpOutMeaDataShowMode, getResources().getStringArray(R.array.outMeaShow), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpOutMeaDataShowMode.setSelection(Integer.parseInt(getConfigData(mCompName, "MeaDataLimitMode")));
            mSpOutMeaDataShowMode.setOnItemSelectedListener(new mSpOnClickListener());

            if (doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                mBtnOneKeyCal.setEnabled(true);
                mSpOutMeaDataShowMode.setEnabled(true);
            } else {
                mBtnOneKeyCal.setEnabled(false);
                mSpOutMeaDataShowMode.setEnabled(false);
            }
            LinearLayout mLayout_outMeaData = v.findViewById(R.id.layout_outMeaData);
            if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                mLayout_outMeaData.setVisibility(View.VISIBLE);
            } else {
                mLayout_outMeaData.setVisibility(View.GONE);
            }

            LinearLayout mLayout_OutMeaDataAndoneKeyCal = v.findViewById(R.id.layout_OutMeaDataAndoneKeyCal);
            if (mLayout_outMeaData.getVisibility() == View.GONE && mLayoutOneKeyCalSwitch.getVisibility() == View.GONE) {
                mLayout_OutMeaDataAndoneKeyCal.setVisibility(View.GONE);
            } else {
                mLayout_OutMeaDataAndoneKeyCal.setVisibility(View.VISIBLE);
            }

            mTbhnd_unit = (TextView) v.findViewById(R.id.mTbhnd_unit);
            mTbhnd_unit.setText(strUnit);

            mEtBHND = (EditText) v.findViewById(R.id.eTbhnd);
            mEtBHND.setText(getConfigData(mCompName, "BHJZCon"));
            mEtBHND.setOnFocusChangeListener(new EditorClickListener());
            mEtBHND.setOnEditorActionListener(new EditorClickListener());

            togBtnBHPD = (ToggleButton) v.findViewById(R.id.tBtnBHPD);
            togBtnBHPD.setOnCheckedChangeListener(new togBtnClickListener());
            togBtnBHPD.setChecked(getConfigData(mCompName, "BHPD").equals("open"));

            if (togBtnBHPD.isChecked()) {
                mLayout_bhpdAll.setVisibility(View.VISIBLE);
                if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                    togBtnBHPD.setEnabled(true);
                    mEtBHND.setEnabled(true);
                } else {
                    togBtnBHPD.setEnabled(false);
                    mEtBHND.setEnabled(false);
                }
            } else {
                if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                    togBtnBHPD.setEnabled(true);
                } else {
                    togBtnBHPD.setEnabled(false);
                }
            }

            //非空闲则不可触发按钮
            if (!doFlowing.get(mCompName).equals(context.getResources().getString(R.string.waiting_for_instructions))) {
                togBtnBHPD.setEnabled(false);
                mEtBHND.setEnabled(false);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    /**
     * @param context
     * @param spinner
     * @param arrList
     * @param OutStyle
     * @param InStyle
     * @param lstNotShow 不显示的
     */
    public void InitSpinner(Context context, Spinner spinner, String[] arrList, int OutStyle, int InStyle, List<String> lstNotShow) {
        ArrayAdapter<String> adapter = new ArrayAdapter(context, OutStyle);
        String[] level = arrList;

        for (int i = 0; i < level.length; ++i) {
            if (!lstNotShow.contains(level[i])) {
                adapter.add(level[i]);
            }
        }

        adapter.setDropDownViewResource(InStyle);
        spinner.setAdapter(adapter);
    }

    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {

                case R.id.tBtnSZDY:
                    saveOperationLogDataModifyMsg(mCompName, "PRINTER", (btnPrinter.isChecked() ? "true" : "false"), "数据打印", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "PRINTER", btnPrinter.isChecked() ? "true" : "false");
                    Log.i("PRINTER", btnPrinter.isChecked() ? "true" : "false");
                    break;
                case R.id.tBtnJZTS:
                    saveOperationLogDataModifyMsg(mCompName, "CALIBRATION_HIT", (btnCalHit.isChecked() ? "true" : "false"), "校准提示", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "CALIBRATION_HIT", btnCalHit.isChecked() ? "true" : "false");
                    if ((btnCalHit.isChecked() ? "true" : "false").equals("false")) {
                        updateConfigData(mCompName, "CALIBRATION_INTERVAL_DAYS", "0");
                    }
                    Log.i("CALIBRATION_HIT", btnCalHit.isChecked() ? "true" : "false");
                    break;
                case R.id.tBtnOneKeyCalSwitch:
                    saveOperationLogDataModifyMsg(mCompName, "OneKeyCalSwitch", (mBtnOneKeyCal.isChecked() ? "open" : "close"), "一键校准开关", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "OneKeyCalSwitch", mBtnOneKeyCal.isChecked() ? "open" : "close");
                    break;
                case R.id.tBtnBHPD:
                    saveOperationLogDataModifyMsg(mCompName, "BHPD", (togBtnBHPD.isChecked() ? "open" : "close"), "标核判定", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "BHPD", togBtnBHPD.isChecked() ? "open" : "close");
                    if (!togBtnBHPD.isChecked()) {
                        mLayout_bhpdAll.setVisibility(View.GONE);
                    } else {
                        mLayout_bhpdAll.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            syncList();
        }
    }

    private class mCSBPChange implements CSeekBarPressure.OnSeekBarChangeListener {

        @Override
        public void onProgressBefore() {
        }

        @Override
        public void onProgressChanged(CSeekBarPressure seekBar, double progressLow, double progressHigh) {
            if (seekBar.getId() == R.id.CSBP) {
                UpPro((float) progressLow, (float) progressHigh);
            }
        }

        @Override
        public void onProgressAfter() {
        }
    }

    private void UpPro(float progressLow, float progressHigh) {

        try {
            if ((progressHigh != preProgressHigh) || progressLow != preProgressLow) {
                preProgressHigh = progressHigh;
                preProgressLow = progressLow;
                String strBjxx = String.format("%." + Integer.parseInt(getConfigData(mCompName, "YXWS")) + "f ", Double.parseDouble(String.valueOf((progressLow * value) + yblcl)));
                String strBjsx = String.format("%." + Integer.parseInt(getConfigData(mCompName, "YXWS")) + "f ", Double.parseDouble(String.valueOf((progressHigh * value) + yblcl)));

                mTvBJXX.setText(getString(R.string.low_alarm) + ": " + (ConvertUnit("mg/L", strUnit, strBjxx, point)) + strUnit);
                mTvBJSX.setText(getString(R.string.high_alarm) + ": " + (ConvertUnit("mg/L", strUnit, strBjsx, point)) + strUnit);

                saveOperationLogDataModifyMsg(mCompName, "ALARM_L", String.valueOf((float) (progressLow * value) + yblcl), "报警下限", ErrorLog.msgType.操作_信息);
                saveOperationLogDataModifyMsg(mCompName, "ALARM_H", String.valueOf((float) (progressHigh * value) + yblcl), "报警上限", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "ALARM_L", String.valueOf((float) (progressLow * value) + yblcl));
                updateConfigData(mCompName, "ALARM_H", String.valueOf((float) (progressHigh * value) + yblcl));
                syncList();
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
    }


    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }


    private class mSpOnClickListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.spOutMeaDataShowMode:
                    if (!(Integer.parseInt(getConfigData(mCompName, "MeaDataLimitMode")) == position)) {
                        if (doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                            saveOperationLogDataModifyMsg(mCompName, "MeaDataLimitMode", String.valueOf(position), "测量值显示模式" + parent.getSelectedItem().toString(), ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "MeaDataLimitMode", String.valueOf(position));
                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.spcldw:
                    saveOperationLogDataModifyMsg(mCompName, "UNIT", mSpcldw.getSelectedItem().toString(), "测量单位", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "UNIT", mSpcldw.getSelectedItem().toString());
                    mTbhnd_unit.setText(mSpcldw.getSelectedItem().toString());
                    break;
                case R.id.spjdqpz:
                    try {
                        saveOperationLogPublicDataModifyMsg("公共", "SYS_RELAYCONF", String.valueOf(mSpRelayConf.getSelectedItemId()), "继电器配置", ErrorLog.msgType.操作_信息);

                        updatePublicConfigData("SYS_RELAYCONF", GetSpjdqpzItem(String.valueOf(mSpRelayConf.getSelectedItem())));
                        syncList();
                        RelayCfgDisplayShow();
                        if (mSpRelayConf.getSelectedItemId() == 2) {
                            mLayoutAlarm.setVisibility(View.VISIBLE);
                        } else {
                            mLayoutAlarm.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception e) {
                        Log.i("except", e.toString());
                    }
                    break;
            }
            syncList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class EditorClickListener implements View.OnFocusChangeListener, TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
            }
            return false;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTAlarmTime:
                        if (mEtAlarmTime.getText().toString().equals("")) {
                            mEtAlarmTime.setText("3");
                        }
                        if (Integer.parseInt(mEtAlarmTime.getText().toString()) < 3) {
                            mEtAlarmTime.setText("3");
                        }
                        if (Integer.parseInt(mEtAlarmTime.getText().toString()) > 999) {
                            mEtAlarmTime.setText("999");
                        }
                        saveOperationLogDataModifyMsg(mCompName, "ALARM_TIME", mEtAlarmTime.getText().toString(), "上下限时间", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "ALARM_TIME", mEtAlarmTime.getText().toString());
                        syncList();
                        break;
                    case R.id.eTyxws:
                        try {
                            if (mEtyxws.getText().toString().equals("")) {
                                mEtyxws.setText("1");
                            }
                            if (Integer.parseInt(mEtyxws.getText().toString()) < 1) {
                                mEtyxws.setText("1");
                            }
                            if (Integer.parseInt(mEtyxws.getText().toString()) > 6) {
                                mEtyxws.setText("6");
                            }
                            String strValue = editDataFormat(mEtyxws.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            saveOperationLogDataModifyMsg(mCompName, "YXWS", strValue, "有效位数", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "YXWS", strValue);
                            syncList();
                            mEtyxws.setText(strValue);
                        } catch (Exception e) {
                            Log.i("except", e.toString());
                        }
                        break;
                    case R.id.eTbhnd:
                        float yblch = Float.parseFloat(getConfigData(mCompName, "YBLCH"));
                        float yblcl = Float.parseFloat(getConfigData(mCompName, "YBLCL"));
                        if ((yblch == 0 && yblcl == 0)) {
                            Toast.makeText(context, getContext().getResources().getString(R.string.pleaseInputDevRange), Toast.LENGTH_LONG).show();
                        }
                        setComponentEditFloatData(mCompName, (EditText) v, Integer.parseInt(getConfigData(mCompName, "YXWS")), yblcl, yblch, yblcl, "设置标核浓度值");
                        updateConfigData(mCompName, "BHJZCon", mEtBHND.getText().toString());
                        syncList();
                        break;
                }
            }
        }
    }

    private void RelayCfgDisplayShow() {
        /*上下限才显示 时间输入框及 单位文本*/
        if (getPublicConfigData("SYS_RELAYCONF").equals("2")) {
            mtvAlarmTUnit.setVisibility(View.VISIBLE);
            mEtAlarmTime.setVisibility(View.VISIBLE);
        } else {
            mtvAlarmTUnit.setVisibility(View.INVISIBLE);
            mEtAlarmTime.setVisibility(View.INVISIBLE);
        }
    }


    private String GetSpjdqpzItem(String Item) {
        switch (Item) {
            case "未配置":
                return "0";
            case "均质预处理":
                return "1";
            case "上下限R1":
                return "2";
            case "开采样R2":
                return "3";
            default:
                break;
        }
        return "0";
    }
}
