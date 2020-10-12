package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/*
 * Created by zwj on 2018/08/06.
 */

public class List4_Content3_pUserParSet extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pUserParSet fragment = null;
    private EditText mEdFMin;
    private EditText mEdFMax;
    private ToggleButton tBtnCalibration;
    private LinearLayout linear_calibration;
    private LinearLayout linear_list2con1line2;
    private ToggleButton tBtnInterference;
    public static Button btnHistoryDateTime;
    public static Button btnHistoryShow;

    public static List4_Content3_pUserParSet newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new List4_Content3_pUserParSet();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();

        void showUpContent();
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
        return R.layout.list4_content3_puser_par_set;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p2);
            mBtnReturn.setOnClickListener(new btnClick());
            linear_calibration = v.findViewById(R.id.linear_calibration);

            tBtnCalibration = v.findViewById(R.id.tBtnCalibration);
            tBtnCalibration.setOnCheckedChangeListener(new togBtnClickListener());
            tBtnCalibration.setChecked(getConfigData(mCompName, "calibrationFlag").equals("true"));
            setCalibrationShow(getConfigData(mCompName, "calibrationFlag").equals("true"));

            mEdFMin = (EditText) v.findViewById(R.id.eTFMin);
            mEdFMax = (EditText) v.findViewById(R.id.eTFMax);

            String strFMin = getConfigData(mCompName, "FMin");
            String strFMax = getConfigData(mCompName, "FMax");

            mEdFMin.setText(strFMin.equals("") ? "0.5" : strFMin);
            mEdFMax.setText(strFMax.equals("") ? "2.0" : strFMax);

            mEdFMin.setOnFocusChangeListener(new EdFocusChange());
            mEdFMin.setOnEditorActionListener(new EtFocusChange());

            mEdFMax.setOnFocusChangeListener(new EdFocusChange());
            mEdFMax.setOnEditorActionListener(new EtFocusChange());

            linear_list2con1line2 = v.findViewById(R.id.list2con1line2);
            if (QueryMeasCateg(mCompName).equals("2") && mCompName.equals("TN") && (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3"))) {
                linear_list2con1line2.setVisibility(View.VISIBLE);

                tBtnInterference = v.findViewById(R.id.tBtnInterference);
                tBtnInterference.setOnCheckedChangeListener(new togBtnClickListener());
                tBtnInterference.setChecked(getConfigData(mCompName, "interferenceFlag").equals("true"));
            } else {
                linear_list2con1line2.setVisibility(View.GONE);
            }

            btnHistoryDateTime = v.findViewById(R.id.btn_history_datetime);
            btnHistoryDateTime.setOnClickListener(new BtnOnClickListener());
            if (getConfigData(mCompName, "historyTimeFlag").equals("0")) {
                btnHistoryDateTime.setText(getString(R.string.start_time));
            } else {
                btnHistoryDateTime.setText(getString(R.string.sampling_time));
            }

            btnHistoryShow = v.findViewById(R.id.btn_history_show);
            btnHistoryShow.setOnClickListener(new BtnHisShowOnClickListener());
            switch (getConfigData(mCompName, "EnOrAIsShow")) {
                case "false":
                    updateConfigData(mCompName, "EnOrAIsShow", "0");
                case "0":
                    btnHistoryShow.setText(getString(R.string.no));
                    break;
                case "true":
                    updateConfigData(mCompName, "EnOrAIsShow", "1");
                case "1":
                    btnHistoryShow.setText(getString(R.string.light_intensity));
                    break;
                case "2":
                    btnHistoryShow.setText(getString(R.string.absorbancy));
                    break;
            }

            byte[] arrayOfByte = DataUtil.shortToByte((short) 9600);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(mCompName + "_查询历史记录时间模式" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 1);


        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (btnHistoryDateTime.getText().toString().equals(getString(R.string.start_time))) {
                btnHistoryDateTime.setText(getString(R.string.sampling_time));
                saveOperationLogMsg(mCompName, "设置历史数据时间_" + getString(R.string.sampling_time), ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "historyTimeFlag", "1");
                byte[] sjsjByte = copybyte(toByteArray(1, 4));
                SendManager.SendCmd(mCompName + "_" + "设置历史数据时间_06_281", S0, 3, 200, sjsjByte);
            } else {
                btnHistoryDateTime.setText(getString(R.string.start_time));
                saveOperationLogMsg(mCompName, "设置历史数据时间_" + getString(R.string.start_time), ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "historyTimeFlag", "0");
                byte[] sjsjByte = copybyte(toByteArray(0, 4));
                SendManager.SendCmd(mCompName + "_" + "设置历史数据时间_06_281", S0, 3, 200, sjsjByte);
            }
        }
    }

    private class BtnHisShowOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (btnHistoryShow.getText().toString().equals(getString(R.string.no))) {
                btnHistoryShow.setText(getString(R.string.light_intensity));
                saveOperationLogMsg(mCompName, "设置历史数据显示_" + getString(R.string.light_intensity), ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "EnOrAIsShow", "1");
            } else if (btnHistoryShow.getText().toString().equals(getString(R.string.light_intensity))) {
                btnHistoryShow.setText(getString(R.string.absorbancy));
                saveOperationLogMsg(mCompName, "设置历史数据时间_" + getString(R.string.absorbancy), ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "EnOrAIsShow", "2");
            } else {
                btnHistoryShow.setText(getString(R.string.no));
                saveOperationLogMsg(mCompName, "设置历史数据时间_" + getString(R.string.no), ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "EnOrAIsShow", "0");
            }
        }
    }


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btnReturn_p2:
                        mCallbacks.onListSelected(v);
                        break;
                }
            } catch (Exception e) {
                Log.e("btnClick", e.toString());
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

    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tBtnCalibration: {
                    saveOperationLogDataModifyMsg(mCompName, "calibrationFlag", (tBtnCalibration.isChecked() ? "true" : "false"), "校准失败判断", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "calibrationFlag", tBtnCalibration.isChecked() ? "true" : "false");
                    setCalibrationShow(isChecked);
                }
                break;
                case R.id.tBtnInterference: {
                    saveOperationLogDataModifyMsg(mCompName, "interferenceFlag", (tBtnInterference.isChecked() ? "true" : "false"), "干扰判断", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "interferenceFlag", tBtnInterference.isChecked() ? "true" : "false");
                }
                break;
            }
            syncList();
        }
    }

    private class EdFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTFMax:
                        String strMax = mEdFMax.getText().toString();
                        if (!strMax.equals("") && (Float.parseFloat(strMax) > 1.0 && Float.parseFloat(strMax) <= 5.0)) {

                            strMax = editDataFormat(strMax, Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            updateConfigData(mCompName, "FMax", strMax);
                            saveOperationLogDataModifyMsg(mCompName, "FMax", strMax, "设置F_MAX", ErrorLog.msgType.操作_信息);
                            mEdFMax.setText(strMax);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + "(1~5]!", Toast.LENGTH_SHORT).show();
                            mEdFMax.setText(getConfigData(mCompName, "FMax").equals("") ? "2.0" : getConfigData(mCompName, "FMax"));
                        }
                        break;
                    case R.id.eTFMin:
                        try {
                            String strMin = mEdFMin.getText().toString();
                            if (!strMin.equals("") && (Float.parseFloat(strMin) >= 0.2 && Float.parseFloat(strMin) < 1.0)) {

                                strMin = editDataFormat(strMin, Integer.parseInt(getConfigData(mCompName, "YXWS")));
                                updateConfigData(mCompName, "FMin", strMin);
                                saveOperationLogDataModifyMsg(mCompName, "FMin", mEdFMin.getText().toString(), "设置F_MIN", ErrorLog.msgType.操作_信息);
                                mEdFMin.setText(strMin);
                            } else {
                                Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " [0.2~1)!", Toast.LENGTH_SHORT).show();
                                mEdFMin.setText(getConfigData(mCompName, "FMin").equals("") ? "0.5" : getConfigData(mCompName, "FMin"));
                            }
                        } catch (Exception e) {

                        }
                        break;
                }
                syncList();
            }
        }
    }

    private void setCalibrationShow(boolean flag) {
        if (flag) {
            linear_calibration.setVisibility(View.VISIBLE);
        } else {
            linear_calibration.setVisibility(View.INVISIBLE);
        }
    }

}
