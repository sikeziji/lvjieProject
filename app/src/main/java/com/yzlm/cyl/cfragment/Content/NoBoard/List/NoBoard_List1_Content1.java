package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.GetNoBoardPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.setNoBoardComponentEditFloatData;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.setNoBoardComponentEditNoZeroFloatData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;

/*
 * Created by zwj on 2019/07/25.
 */

public class NoBoard_List1_Content1 extends SubFragment {

    private LinearLayout mLayoutAlarm;
    private Spinner mSpRelayConf;
    private EditText mEtAlarmTime;
    private ToggleButton btnPrinter;
    private Spinner mSpUnit;
    private EditText mEtPoint;
    private ToggleButton mToggleButtonLC1;
    private ToggleButton mToggleButtonLC2;
    private ToggleButton mToggleButtonLC3;


    private int point = 2;
    private static NoBoard_List1_Content1 fragment = null;

    public static NoBoard_List1_Content1 newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content1();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.no_board_list1_content1;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        try {
            mLayoutAlarm = (LinearLayout) v.findViewById(R.id.layoutAlarm);
            mSpRelayConf = (Spinner) v.findViewById(R.id.spjdqpz);
            mEtAlarmTime = (EditText) v.findViewById(R.id.eTAlarmTime);
            EditText mEtAlarmL = (EditText) v.findViewById(R.id.NBAlarmLow);
            EditText mEtAlarmH = (EditText) v.findViewById(R.id.NBAlarmHigh);
            btnPrinter = (ToggleButton) v.findViewById(R.id.tBtnSZDY);
            mSpUnit = (Spinner) v.findViewById(R.id.spcldw);
            mEtPoint = (EditText) v.findViewById(R.id.eTyxws);
            TextView mtvAlarmValueUnit = (TextView) v.findViewById(R.id.tvAlarmUnit);

            String strUnit = getNoBoardConfigData(mNoBoardCompName, "UNIT");
            point = Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS"));
            LinearLayout mLayoutRang1 = (LinearLayout) v.findViewById(R.id.layoutRang1);
            LinearLayout mLayoutRang2 = (LinearLayout) v.findViewById(R.id.layoutRang2);
            LinearLayout mLayoutRang3 = (LinearLayout) v.findViewById(R.id.layoutRang3);
            switch (GetNoBoardPlatRangSum(mNoBoardCompName)) {
                case 3:
                    mLayoutRang3.setVisibility(View.VISIBLE);
                case 2:
                    mLayoutRang2.setVisibility(View.VISIBLE);
                case 1:
                    mLayoutRang1.setVisibility(View.VISIBLE);
                    break;
            }
            mToggleButtonLC1 = (ToggleButton) v.findViewById(R.id.btnLC1);
            mToggleButtonLC1.setEnabled(false);
            mToggleButtonLC2 = (ToggleButton) v.findViewById(R.id.btnLC2);
            mToggleButtonLC2.setEnabled(false);
            mToggleButtonLC3 = (ToggleButton) v.findViewById(R.id.btnLC3);
            mToggleButtonLC3.setEnabled(false);

            // 第一个组分为多光谱参数
            setRangeBtnShow(Integer.parseInt(getConfigData(strComponent.get(1)[0], "RANGE")));

            TextView mEtlcl1 = (TextView) v.findViewById(R.id.eTlcl1);
            mEtlcl1.setText(ConvertUnit("mg/L", strUnit, (getNoBoardConfigData(mNoBoardCompName, "LC1L")), point));
            TextView mEtlch1 = (TextView) v.findViewById(R.id.eTlch1);
            mEtlch1.setText(ConvertUnit("mg/L", strUnit, (getNoBoardConfigData(mNoBoardCompName, "LC1H")), point));
            TextView mEtlcl2 = (TextView) v.findViewById(R.id.eTlcl2);
            mEtlcl2.setText(ConvertUnit("mg/L", strUnit, (getNoBoardConfigData(mNoBoardCompName, "LC2L")), point));
            TextView mEtlch2 = (TextView) v.findViewById(R.id.eTlch2);
            mEtlch2.setText(ConvertUnit("mg/L", strUnit, (getNoBoardConfigData(mNoBoardCompName, "LC2H")), point));
            TextView mEtlcl3 = (TextView) v.findViewById(R.id.eTlcl3);
            mEtlcl3.setText(ConvertUnit("mg/L", strUnit, (getNoBoardConfigData(mNoBoardCompName, "LC3L")), point));
            TextView mEtlch3 = (TextView) v.findViewById(R.id.eTlch3);
            mEtlch3.setText(ConvertUnit("mg/L", strUnit, (getNoBoardConfigData(mNoBoardCompName, "LC3H")), point));


            float alarmH = Float.valueOf(getNoBoardConfigData(mNoBoardCompName, "ALARM_H"));
            float alarmL = Float.valueOf(getNoBoardConfigData(mNoBoardCompName, "ALARM_L"));

            mEtAlarmL.setText(ConvertUnit("mg/L", strUnit, String.format("%." + point + "f ", alarmL), point));
            mEtAlarmH.setText(ConvertUnit("mg/L", strUnit, String.format("%." + point + "f ", alarmH), point));
            mtvAlarmValueUnit.setText(strUnit);
            mEtAlarmL.setOnFocusChangeListener(new editFocusChange());
            mEtAlarmL.setOnEditorActionListener(new editFocusChange());
            mEtAlarmH.setOnFocusChangeListener(new editFocusChange());
            mEtAlarmH.setOnEditorActionListener(new editFocusChange());

            try {
                InitSpinner(context, mSpRelayConf, getResources().getStringArray(R.array.jdqpz), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
                mSpRelayConf.setSelection(Integer.parseInt(getPublicConfigData("SYS_RELAYCONF")));
                mSpRelayConf.setOnItemSelectedListener(new mSpRelayConfListener());
            } catch (Exception e) {
                Log.i("except", e.toString());
            }

            String[] ArrayItems = getResources().getStringArray(R.array.cldw2);
            InitSpinner(context, mSpUnit, ArrayItems, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);

            for (int i = 0; i < ArrayItems.length; i++) {
                if (strUnit.equals(ArrayItems[i])) {
                    mSpUnit.setSelection(i);
                }
            }
            mSpUnit.setOnItemSelectedListener(new mSpcldwListener());

            mEtPoint.setText(String.valueOf(point));
            mEtPoint.setOnFocusChangeListener(new editFocusChange());
            mEtPoint.setOnEditorActionListener(new editFocusChange());

            mEtAlarmTime.setText(getNoBoardConfigData(mNoBoardCompName, "ALARM_TIME"));
            mEtAlarmTime.setOnFocusChangeListener(new EditorClickListener());
            mEtAlarmTime.setOnEditorActionListener(new EditorClickListener());

            RelayCfgDisplayShow();

            btnPrinter.setOnCheckedChangeListener(new togBtnClickListener());
            btnPrinter.setChecked(getNoBoardConfigData(mNoBoardCompName, "PRINTER").equals("true"));


            int rangNum = GetNoBoardPlatRangSum(mNoBoardCompName);
            String[] rangeInfo = new String[rangNum];

            for (int i = 0; i < rangNum; i++) {
                rangeInfo[i] = getString(R.string.range) + (i + 1);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]" + e.toString());
        }
    }


    private void setRangeBtnShow(int range) {
        mToggleButtonLC1.setChecked(range == 1);
        mToggleButtonLC2.setChecked(range == 2);
        mToggleButtonLC3.setChecked(range == 3);
    }


    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {

                case R.id.tBtnSZDY:
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "PRINTER", (btnPrinter.isChecked() ? "true" : "false"), "数据打印", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "PRINTER", btnPrinter.isChecked() ? "true" : "false");
                    Log.i("PRINTER", btnPrinter.isChecked() ? "true" : "false");
                    break;
            }
            syncList();
        }
    }


    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class mSpRelayConfListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                saveOperationLogPublicDataModifyMsg("公共", "SYS_RELAYCONF", String.valueOf(mSpRelayConf.getSelectedItemId()), "继电器配置", ErrorLog.msgType.操作_信息);

                updatePublicConfigData("SYS_RELAYCONF", String.valueOf(mSpRelayConf.getSelectedItemId()));
                syncList();
                RelayCfgDisplayShow();

            } catch (Exception e) {
                Log.i("except", e.toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class mSpcldwListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "UNIT", mSpUnit.getSelectedItem().toString(), "测量单位", ErrorLog.msgType.操作_信息);
            updateNoBoardConfigData(mNoBoardCompName, "UNIT", mSpUnit.getSelectedItem().toString());
            syncList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class editFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    switch (v.getId()) {
                        case R.id.eTyxws:
                            if (mEtPoint.getText().toString().equals("")) {
                                mEtPoint.setText("1");
                            }
                            if (Integer.parseInt(mEtPoint.getText().toString()) < 1) {
                                mEtPoint.setText("1");
                            }
                            if (Integer.parseInt(mEtPoint.getText().toString()) > 6) {
                                mEtPoint.setText("6");
                            }
                            String strValue = editDataFormat(mEtPoint.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "YXWS", strValue, "有效位数", ErrorLog.msgType.操作_信息);
                            updateNoBoardConfigData(mNoBoardCompName, "YXWS", strValue);
                            syncList();
                            mEtPoint.setText(strValue);
                            point = Integer.parseInt(strValue);
                            break;
                        case R.id.NBAlarmLow:
                            setNoBoardComponentEditFloatData(mNoBoardCompName, (EditText) v, point, Float.valueOf(getNoBoardConfigData(mNoBoardCompName, "YBLCL")), Float.valueOf(getNoBoardConfigData(mNoBoardCompName, "ALARM_H")), 0, "设置报警下限");
                            updateNoBoardConfigData(mNoBoardCompName, "ALARM_L", ((EditText) v).getText().toString());
                            break;
                        case R.id.NBAlarmHigh:
                            setNoBoardComponentEditNoZeroFloatData(mNoBoardCompName, (EditText) v, point, Float.valueOf(getNoBoardConfigData(mNoBoardCompName, "ALARM_L")), Float.valueOf(getNoBoardConfigData(mNoBoardCompName, "YBLCH")), 100, "设置报警上限");
                            updateNoBoardConfigData(mNoBoardCompName, "ALARM_H", ((EditText) v).getText().toString());
                            break;
                    }
                } catch (Exception e) {
                    Log.i("except", e.toString());
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
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "ALARM_TIME", mEtAlarmTime.getText().toString(), "上下限时间", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "ALARM_TIME", mEtAlarmTime.getText().toString());
                        syncList();
                        break;
                }
            }
        }
    }

    private void RelayCfgDisplayShow() {
        /*上下限才显示 时间输入框及 单位文本*/
        if (getPublicConfigData("SYS_RELAYCONF").equals("2")) {
            mLayoutAlarm.setVisibility(View.VISIBLE);
        } else {
            mLayoutAlarm.setVisibility(View.INVISIBLE);
        }
    }
}
