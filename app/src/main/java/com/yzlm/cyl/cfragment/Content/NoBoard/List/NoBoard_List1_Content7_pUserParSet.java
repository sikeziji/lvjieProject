package com.yzlm.cyl.cfragment.Content.NoBoard.List;

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

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.passwordUnlockEffectiveWork;

/*
 * Created by zwj on 2018/08/06.
 */

public class NoBoard_List1_Content7_pUserParSet extends SubFragment {
    private Callbacks mCallbacks;
    private static NoBoard_List1_Content7_pUserParSet fragment = null;
    private EditText mEdFMin;
    private EditText mEdFMax;
    private ToggleButton tBtnCalibration;
    private LinearLayout linear_calibration;
    public static Button btnHistoryShow;

    public static NoBoard_List1_Content7_pUserParSet newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new NoBoard_List1_Content7_pUserParSet();
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
        return R.layout.no_board_list1_content7_puser_par_set;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnNoBoardReturn_p2);
            mBtnReturn.setOnClickListener(new btnClick());
            linear_calibration = v.findViewById(R.id.linear_calibration);

            tBtnCalibration = v.findViewById(R.id.tBtnCalibration);
            tBtnCalibration.setOnCheckedChangeListener(new togBtnClickListener());
            tBtnCalibration.setChecked(getNoBoardConfigData(mNoBoardCompName, "calibrationFlag").equals("true"));
            setCalibrationShow(getNoBoardConfigData(mNoBoardCompName, "calibrationFlag").equals("true"));

            mEdFMin = (EditText) v.findViewById(R.id.eTFMin);
            mEdFMax = (EditText) v.findViewById(R.id.eTFMax);

            String strFMin = getNoBoardConfigData(mNoBoardCompName, "FMin");
            String strFMax = getNoBoardConfigData(mNoBoardCompName, "FMax");

            mEdFMin.setText(strFMin.equals("") ? "0.5" : strFMin);
            mEdFMax.setText(strFMax.equals("") ? "2.0" : strFMax);

            mEdFMin.setOnFocusChangeListener(new EdFocusChange());
            mEdFMin.setOnEditorActionListener(new EtFocusChange());

            mEdFMax.setOnFocusChangeListener(new EdFocusChange());
            mEdFMax.setOnEditorActionListener(new EtFocusChange());

            btnHistoryShow = v.findViewById(R.id.btn_history_show);
            btnHistoryShow.setOnClickListener(new BtnHisShowOnClickListener());
            switch (getNoBoardConfigData(mNoBoardCompName, "EnOrAIsShow")) {
                case "false":
                    updateNoBoardConfigData(mNoBoardCompName, "EnOrAIsShow", "0");
                case "0":
                    btnHistoryShow.setText(getString(R.string.no));
                    break;
                case "true":
                    updateNoBoardConfigData(mNoBoardCompName, "EnOrAIsShow", "1");
                case "1":
                    btnHistoryShow.setText(getString(R.string.light_intensity));
                    break;
                case "2":
                    btnHistoryShow.setText(getString(R.string.absorbancy));
                    break;
            }


            passwordUnlockEffectiveWork = "";
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private class BtnHisShowOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (btnHistoryShow.getText().toString().equals(getString(R.string.no))) {
                btnHistoryShow.setText(getString(R.string.light_intensity));
                saveOperationLogMsg(mNoBoardCompName, "设置历史数据显示_" + getString(R.string.light_intensity), ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "EnOrAIsShow", "1");
            } else if (btnHistoryShow.getText().toString().equals(getString(R.string.light_intensity))) {
                btnHistoryShow.setText(getString(R.string.absorbancy));
                saveOperationLogMsg(mNoBoardCompName, "设置历史数据时间_" + getString(R.string.absorbancy), ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "EnOrAIsShow", "2");
            } else {
                btnHistoryShow.setText(getString(R.string.no));
                saveOperationLogMsg(mNoBoardCompName, "设置历史数据时间_" + getString(R.string.no), ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "EnOrAIsShow", "0");
            }
        }
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btnNoBoardReturn_p2:
                        mCallbacks.onListSelected(v);
                        passwordUnlockEffectiveWork = "";
                        break;
                }
            } catch (Exception e) {
                Log.e("btnClick", e.toString());
            }
        }
    }
    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tBtnCalibration: {
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "calibrationFlag", (tBtnCalibration.isChecked() ? "true" : "false"), "校准失败判断", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "calibrationFlag", tBtnCalibration.isChecked() ? "true" : "false");
                    setCalibrationShow(isChecked);
                }
                break;
            }
            syncList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command");

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

    private class EdFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTFMax:
                        String strMax = mEdFMax.getText().toString();
                        if (!strMax.equals("") && (Float.parseFloat(strMax) > 1.0 && Float.parseFloat(strMax) <= 5.0)) {

                            strMax = editDataFormat(strMax, Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                            updateNoBoardConfigData(mNoBoardCompName, "FMax", strMax);
                            saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "FMax", strMax, "设置F_MAX", ErrorLog.msgType.操作_信息);
                            mEdFMax.setText(strMax);
                        } else {
                            Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + "(1~5]!", Toast.LENGTH_SHORT).show();
                            mEdFMax.setText(getNoBoardConfigData(mNoBoardCompName, "FMax").equals("") ? "2.0" : getNoBoardConfigData(mNoBoardCompName, "FMax"));
                        }
                        break;
                    case R.id.eTFMin:
                        try {
                            String strMin = mEdFMin.getText().toString();
                            if (!strMin.equals("") && (Float.parseFloat(strMin) >= 0.2 && Float.parseFloat(strMin) < 1.0)) {

                                strMin = editDataFormat(strMin, Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                                updateNoBoardConfigData(mNoBoardCompName, "FMin", strMin);
                                saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "FMin", mEdFMin.getText().toString(), "设置F_MIN", ErrorLog.msgType.操作_信息);
                                mEdFMin.setText(strMin);
                            } else {
                                Toast.makeText(context, getString(R.string.please_confirm_the_range_input_par) + " [0.2~1)!", Toast.LENGTH_SHORT).show();
                                mEdFMin.setText(getNoBoardConfigData(mNoBoardCompName, "FMin").equals("") ? "0.5" : getNoBoardConfigData(mNoBoardCompName, "FMin"));
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
