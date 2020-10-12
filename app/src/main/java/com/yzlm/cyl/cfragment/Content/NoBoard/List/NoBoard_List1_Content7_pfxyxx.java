package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalc;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalcTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardErrorLog;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardHistory;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardKbf;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Dialog.Dialog_select;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddNoBoardError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardKbf.addNoBoardRangeKB;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.GetNoBoardPlatRangSum;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.File.MFile.deleteDir;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getNoBoardCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.syncNoBoardListFactoryParam;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class NoBoard_List1_Content7_pfxyxx extends SubFragment {

    private EditText mEtyblc_l;
    private EditText mEtyblc_h;
    private EditText mEtlc1_l;
    private EditText mEtlc1_h;
    private EditText mEtlc2_l;
    private EditText mEtlc2_h;
    private EditText mEtlc3_l;
    private EditText mEtlc3_h;

    private Callbacks mCallbacks;

    private static NoBoard_List1_Content7_pfxyxx fragment = null;

    public static NoBoard_List1_Content7_pfxyxx newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content7_pfxyxx();
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
        return R.layout.no_board_list1_content7_pfxyxx;
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

            mEtyblc_l = (EditText) v.findViewById(R.id.eT_yblc_l);
            mEtyblc_l.setText(getNoBoardConfigData(mNoBoardCompName, "YBLCL"));
            mEtyblc_l.setOnFocusChangeListener(new YBLCLFocusChange());
            mEtyblc_l.setOnEditorActionListener(new EtFocusChange());
            mEtyblc_l.setSaveEnabled(false);

            mEtyblc_h = (EditText) v.findViewById(R.id.eT_yblc_h);
            mEtyblc_h.setText(getNoBoardConfigData(mNoBoardCompName, "YBLCH"));
            mEtyblc_h.setOnFocusChangeListener(new YBLCHFocusChange());
            mEtyblc_h.setOnEditorActionListener(new EtFocusChange());
            mEtyblc_h.setSaveEnabled(false);


            mEtlc1_l = (EditText) v.findViewById(R.id.eT_lc1_l);
            mEtlc1_l.setOnFocusChangeListener(new LC1LFocusChange());
            mEtlc1_l.setOnEditorActionListener(new EtFocusChange());
            mEtlc1_l.setText(getNoBoardConfigData(mNoBoardCompName, "LC1L"));
            mEtlc1_l.setSaveEnabled(false);

            mEtlc1_h = (EditText) v.findViewById(R.id.eT_lc1_h);
            mEtlc1_h.setText(getNoBoardConfigData(mNoBoardCompName, "LC1H"));
            mEtlc1_h.setOnFocusChangeListener(new LC1HFocusChange());
            mEtlc1_h.setOnEditorActionListener(new EtFocusChange());
            mEtlc1_h.setSaveEnabled(false);

            mEtlc2_l = (EditText) v.findViewById(R.id.eT_lc2_l);
            mEtlc2_l.setText(getNoBoardConfigData(mNoBoardCompName, "LC2L"));
            mEtlc2_l.setOnFocusChangeListener(new LC2LFocusChange());
            mEtlc2_l.setOnEditorActionListener(new EtFocusChange());
            mEtlc2_l.setSaveEnabled(false);

            mEtlc2_h = (EditText) v.findViewById(R.id.eT_lc2_h);
            mEtlc2_h.setText(getNoBoardConfigData(mNoBoardCompName, "LC2H"));
            mEtlc2_h.setOnFocusChangeListener(new LC2HFocusChange());
            mEtlc2_h.setOnEditorActionListener(new EtFocusChange());
            mEtlc2_h.setSaveEnabled(false);

            mEtlc3_l = (EditText) v.findViewById(R.id.eT_lc3_l);
            mEtlc3_l.setText(getNoBoardConfigData(mNoBoardCompName, "LC3L"));
            mEtlc3_l.setOnFocusChangeListener(new LC3LFocusChange());
            mEtlc3_l.setOnEditorActionListener(new EtFocusChange());
            mEtlc3_l.setSaveEnabled(false);

            mEtlc3_h = (EditText) v.findViewById(R.id.eT_lc3_h);
            mEtlc3_h.setText(getNoBoardConfigData(mNoBoardCompName, "LC3H"));
            mEtlc3_h.setOnFocusChangeListener(new LC3HFocusChange());
            mEtlc3_h.setOnEditorActionListener(new EtFocusChange());
            mEtlc3_h.setSaveEnabled(false);

            Button btnClearHistory = (Button) v.findViewById(R.id.Btn_clearHistory);
            btnClearHistory.setOnClickListener(new ClearHistoryOnClick());

            Button btnClearLog = (Button) v.findViewById(R.id.Btn_clearLog);
            btnClearLog.setOnClickListener(new ClearLogOnClick());

            Button btnClearKBF = (Button) v.findViewById(R.id.Btn_clearKBF);
            btnClearKBF.setOnClickListener(new ClearKBFOnClick());

            Button btnClearRunNotes = (Button) v.findViewById(R.id.Btn_clearRunNote);
            btnClearRunNotes.setOnClickListener(new ClearRunNotesOnClick());

            Button btnClearOperation = (Button) v.findViewById(R.id.Btn_clearOperation);
            btnClearOperation.setOnClickListener(new ClearOperationOnClick());
            if (getPublicConfigData("LogInName").equals("3")) {
                btnClearOperation.setVisibility(View.VISIBLE);
            } else {
                btnClearOperation.setVisibility(View.GONE);
            }

            Button mBtnszccsz = (Button) v.findViewById(R.id.Btn_szccsz);
            mBtnszccsz.setOnClickListener(new btnOnClickListener());

            Button mBtnhfccsz = (Button) v.findViewById(R.id.Btn_hhccsz);
            mBtnhfccsz.setOnClickListener(new btnOnClickListener());

            if (!getPublicConfigData("LogInName").equals("3")) {
                LinearLayout layoutClearAlarHistory = v.findViewById(R.id.layoutClearAlarHistory);
                layoutClearAlarHistory.setVisibility(getPublicConfigData("ClearNotesBtnIsShow").equals("true") ? View.VISIBLE : View.GONE);
            }


        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]" + e.toString());
        }

    }


    private class btnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Btn_szccsz:
                    showDialogMsg(fragment, context.getString(R.string.set_factory_settings), "store");
                    break;
                case R.id.Btn_hhccsz:
                    showDialogMsg(fragment, context.getString(R.string.restore_factory_settings), "restore");
                    break;
            }
        }
    }

    private class ClearRunNotesOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDialogMsg(fragment, getString(R.string.clear_all_running_records), "running");
        }
    }

    private class ClearKBFOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDialogMsg(fragment, getString(R.string.clear_all_cal_records), "Calibration");
        }
    }

    private class ClearHistoryOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDialogMsg(fragment, getString(R.string.clear_all_history_records), "history");
        }
    }

    private class ClearLogOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDialogMsg(fragment, getString(R.string.clear_all_alarm_records), "alarm");
        }
    }

    private class ClearOperationOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDialogMsg(fragment, getString(R.string.clear_all_operation_records), "operation");
        }
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
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


    private class YBLCLFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtyblc_l.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtyblc_l.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "YBLCL", strValue, "设置仪表量程L", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "YBLCL", strValue);

                    mEtyblc_l.setText(strValue);
                } else {
                    mEtyblc_l.setText(getNoBoardConfigData(mNoBoardCompName, "YBLCL"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    private class YBLCHFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtyblc_h.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtyblc_h.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "YBLCH", strValue, "设置仪表量程H", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "YBLCH", strValue);
                    mEtyblc_h.setText(strValue);


                    float ALARM_H = Float.valueOf(getNoBoardConfigData(mNoBoardCompName, "ALARM_H"));
                    float ALARM_L = Float.valueOf(getNoBoardConfigData(mNoBoardCompName, "ALARM_L"));

                    if (ALARM_H > Float.valueOf(strValue)) {
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "ALARM_H", strValue, "设置报警上限", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "ALARM_H", strValue);
                    }
                    if (ALARM_L > Float.valueOf(strValue)) {
                        saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "ALARM_H", "0", "设置报警下限", ErrorLog.msgType.操作_信息);
                        updateNoBoardConfigData(mNoBoardCompName, "ALARM_H", "0");

                    }
                } else {
                    mEtyblc_h.setText(getNoBoardConfigData(mNoBoardCompName, "YBLCH"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    private class LC1LFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc1_l.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc1_l.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "LC1L", strValue, "设置量程1L", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "LC1L", strValue);

                    mEtlc1_l.setText(strValue);
                } else {
                    mEtlc1_l.setText(getNoBoardConfigData(mNoBoardCompName, "LC1L"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    private class LC1HFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc1_h.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc1_h.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));

                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "LC1H", strValue, "设置量程1H", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "LC1H", strValue);
                    mEtlc1_h.setText(strValue);
                } else {
                    mEtlc1_h.setText(getNoBoardConfigData(mNoBoardCompName, "LC1H"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    private class LC2LFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc2_l.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc2_l.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "LC2L", strValue, "设置量程2L", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "LC2L", strValue);
                    mEtlc2_l.setText(strValue);
                } else {
                    mEtlc2_l.setText(getNoBoardConfigData(mNoBoardCompName, "LC2L"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    private class LC2HFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc2_h.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc2_h.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "LC2H", strValue, "设置量程2H", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "LC2H", strValue);

                    mEtlc2_h.setText(strValue);
                } else {
                    mEtlc2_h.setText(getNoBoardConfigData(mNoBoardCompName, "LC2H"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    private class LC3LFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc3_l.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc3_l.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "LC3L", strValue, "设置量程3L", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "LC3L", strValue);
                    mEtlc3_l.setText(strValue);
                } else {
                    mEtlc3_l.setText(getNoBoardConfigData(mNoBoardCompName, "LC3L"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    private class LC3HFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc3_h.getText().toString().equals("")) {

                    String strValue = editDataFormat(mEtlc3_h.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "LC3H", strValue, "设置量程3H", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "LC3H", strValue);

                    mEtlc3_h.setText(strValue);
                } else {
                    mEtlc3_h.setText(getNoBoardConfigData(mNoBoardCompName, "LC3H"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command").split(";")[1];
            if (stringExtra.contains("history")) {

                saveNoBoardOperationLogMsg(mNoBoardCompName, "清除历史记录", ErrorLog.msgType.操作_信息);
                NoBoardHistory history = new NoBoardHistory(context);
                history.clearSelectData(mNoBoardCompName);
                showOKDialogMsg(getString(R.string.set_finished));
                AddNoBoardError(mNoBoardCompName, 586, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("alarm")) {

                saveNoBoardOperationLogMsg(mNoBoardCompName, "清除报警记录", ErrorLog.msgType.操作_信息);
                NoBoardErrorLog error = new NoBoardErrorLog(context);
                NoBoardErrorLog.msgType[] types = {NoBoardErrorLog.msgType.报错_警告, NoBoardErrorLog.msgType.报错_错误};
                error.clearSelectErrorData(mNoBoardCompName, types);
                showOKDialogMsg(getString(R.string.set_finished));
                AddNoBoardError(mNoBoardCompName, 588, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("Calibration")) {

                saveNoBoardOperationLogMsg(mNoBoardCompName, "清除校准记录", ErrorLog.msgType.操作_信息);
                backupLastKBF();
                NoBoardKbf kbf = new NoBoardKbf(context);
                kbf.clearSelectKBFData(mNoBoardCompName);
                addLastKBF();
                clearRangeCalA(mNoBoardCompName);
                showOKDialogMsg(getString(R.string.set_finished));
                AddNoBoardError(mNoBoardCompName, 587, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("running")) {

                saveNoBoardOperationLogMsg(mNoBoardCompName, "清除运行记录", ErrorLog.msgType.操作_信息);
                NoBoardErrorLog error = new NoBoardErrorLog(context);
                NoBoardErrorLog.msgType[] types = {NoBoardErrorLog.msgType.报错_信息, NoBoardErrorLog.msgType.校准_信息, NoBoardErrorLog.msgType.运行_信息};
                error.clearSelectErrorData(mNoBoardCompName, types);
                showOKDialogMsg(getString(R.string.set_finished));

            } else if (stringExtra.contains("operation")) {

                NoBoardDevLog log = new NoBoardDevLog(context);
                ErrorLog.msgType[] types = {ErrorLog.msgType.操作_信息};
                log.clearSelectErrorData(mNoBoardCompName, types);
                showOKDialogMsg(getString(R.string.set_finished));
                saveNoBoardOperationLogMsg(mNoBoardCompName, "清除操作记录", ErrorLog.msgType.操作_信息);

            } else if ((stringExtra.contains("restore"))) {

                saveNoBoardOperationLogMsg(mNoBoardCompName, "恢复出厂", ErrorLog.msgType.操作_信息);
                recoverFactorySetting();
                mCallbacks.onDialogRS();

            } else if ((stringExtra.contains("store"))) {

                saveNoBoardOperationLogMsg(mNoBoardCompName, "设置出厂", ErrorLog.msgType.操作_信息);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deleteDir("/sdcard/waterrun/");
                        deleteDir("/sdcard/WSwaterrun/");
                        deleteDir("/sdcard/watercrash/");
                        deleteDir("/sdcard/waterexcep/");
                    }
                }).start();
                setFactorySettings();
                mCallbacks.onDialogRS();
            }
        } else {
            mCallbacks.onDialogRS();
        }
    }

    private static void showDialogMsg(SubFragment fragment, String string, String sign) {
        main.removeDestopText(mfb);
        Dialog_select dialog_select = new Dialog_select(mNoBoardCompName);
        Bundle bundle = new Bundle();
        bundle.putString("alert-select", string + ";" + sign);
        dialog_select.setArguments(bundle);
        dialog_select.setTargetFragment(fragment, 1);
        dialog_select.show(fm, "Dialog_select");
    }

    private static void showOKDialogMsg(String string) {
        main.removeDestopText(mfb);
        Dialog_Err st = new Dialog_Err();
        Bundle bundle = new Bundle();
        bundle.putString("alert-err", string);
        st.setArguments(bundle);
        st.show(fm, "Dialog_err");
    }

    private void setFactorySettings() {
        SendManager.SendCmd(mNoBoardCompName + "_设置出厂设置" + "_8_0F", S0, 3, 200, null);

        updateNoBoardConfigData(mNoBoardCompName, "FAC_HMIBH", getNoBoardConfigData(mNoBoardCompName, "HMIBH"));
        updateNoBoardConfigData(mNoBoardCompName, "FAC_YQBH", getNoBoardConfigData(mNoBoardCompName, "YQBH"));
        updateNoBoardConfigData(mNoBoardCompName, "FAC_YBLCL", getNoBoardConfigData(mNoBoardCompName, "YBLCL"));
        updateNoBoardConfigData(mNoBoardCompName, "FAC_YBLCH", getNoBoardConfigData(mNoBoardCompName, "YBLCH"));

        updateNoBoardConfigData(mNoBoardCompName, "FAC_LC1L", getNoBoardConfigData(mNoBoardCompName, "LC1L"));
        updateNoBoardConfigData(mNoBoardCompName, "FAC_LC1H", getNoBoardConfigData(mNoBoardCompName, "LC1H"));
        updateNoBoardConfigData(mNoBoardCompName, "FAC_LC2L", getNoBoardConfigData(mNoBoardCompName, "LC2L"));
        updateNoBoardConfigData(mNoBoardCompName, "FAC_LC2H", getNoBoardConfigData(mNoBoardCompName, "LC2H"));
        updateNoBoardConfigData(mNoBoardCompName, "FAC_LC3L", getNoBoardConfigData(mNoBoardCompName, "LC3L"));
        updateNoBoardConfigData(mNoBoardCompName, "FAC_LC3H", getNoBoardConfigData(mNoBoardCompName, "LC3H"));
        backupLastKBF();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(new Date());
        updateNoBoardConfigData(mNoBoardCompName, "FACTORY_TIME", time);
        syncList();
        syncNoBoardListFactoryParam();
    }


    private void recoverFactorySetting() {

        SendManager.SendCmd(mNoBoardCompName + "_恢复出厂设置" + "_8_10", S0, 3, 200, null);

        updateNoBoardConfigData(mNoBoardCompName, "HMIBH", getNoBoardConfigData(mNoBoardCompName, "FAC_HMIBH"));
        updateNoBoardConfigData(mNoBoardCompName, "YQBH", getNoBoardConfigData(mNoBoardCompName, "FAC_YQBH"));

        updateNoBoardConfigData(mNoBoardCompName, "YBLCL", getNoBoardConfigData(mNoBoardCompName, "FAC_YBLCL"));
        updateNoBoardConfigData(mNoBoardCompName, "YBLCH", getNoBoardConfigData(mNoBoardCompName, "FAC_YBLCH"));

        updateNoBoardConfigData(mNoBoardCompName, "LC1L", getNoBoardConfigData(mNoBoardCompName, "FAC_LC1L"));
        updateNoBoardConfigData(mNoBoardCompName, "LC1H", getNoBoardConfigData(mNoBoardCompName, "FAC_LC1H"));

        updateNoBoardConfigData(mNoBoardCompName, "LC2L", getNoBoardConfigData(mNoBoardCompName, "FAC_LC2L"));
        updateNoBoardConfigData(mNoBoardCompName, "LC2H", getNoBoardConfigData(mNoBoardCompName, "FAC_LC2H"));

        updateNoBoardConfigData(mNoBoardCompName, "LC3L", getNoBoardConfigData(mNoBoardCompName, "FAC_LC3L"));
        updateNoBoardConfigData(mNoBoardCompName, "LC3H", getNoBoardConfigData(mNoBoardCompName, "FAC_LC3H"));

        addLastKBF();
    }


    private NBCalc getRangePar(String component, int range) {

        NBCalcTable ct = getNoBoardCalc(component);
        return ct.getCalc(String.valueOf(range));
    }

    /***
     * 备份最新的各个量程的那一组KBF
     * */
    private void backupLastKBF() {
        NBCalc calc1 = getRangePar(mNoBoardCompName, 1);
        NBCalc calc2 = getRangePar(mNoBoardCompName, 2);
        NBCalc calc3 = getRangePar(mNoBoardCompName, 3);

        if (calc1 != null) {
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC1K", String.valueOf(calc1.getK(String.valueOf(1), mNoBoardCompName)));
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC1B", String.valueOf(calc1.getB(String.valueOf(1), mNoBoardCompName)));
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC1F", String.valueOf(calc1.getF(String.valueOf(1), mNoBoardCompName)));
        }
        if (calc2 != null) {
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC2K", String.valueOf(calc2.getK(String.valueOf(2), mNoBoardCompName)));
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC2B", String.valueOf(calc2.getB(String.valueOf(2), mNoBoardCompName)));
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC2F", String.valueOf(calc2.getF(String.valueOf(2), mNoBoardCompName)));
        }
        if (calc3 != null) {
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC3K", String.valueOf(calc3.getK(String.valueOf(3), mNoBoardCompName)));
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC3B", String.valueOf(calc3.getB(String.valueOf(3), mNoBoardCompName)));
            updateNoBoardConfigData(mNoBoardCompName, "FAC_LC3F", String.valueOf(calc3.getF(String.valueOf(3), mNoBoardCompName)));
        }
        syncNoBoardListFactoryParam();
    }

    /**
     * 将最后设置出厂设置的KBF 增加到校准记录中
     */
    private void addLastKBF() {
        try {
            switch (GetNoBoardPlatRangSum(mNoBoardCompName)) {
                case 3:
                    addNoBoardRangeKB(mNoBoardCompName, "3", Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC3K")), Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC3B")),
                            Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC3F")));
                case 2:
                    addNoBoardRangeKB(mNoBoardCompName, "2", Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC2K")), Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC2B")),
                            Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC2F")));
                case 1:
                    addNoBoardRangeKB(mNoBoardCompName, "1", Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC1K")), Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC1B")),
                            Double.valueOf(getNoBoardConfigData(mNoBoardCompName, "FAC_LC1F")));
                    break;
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "] 设置出厂增加校准记录异常" + e.toString());
        }
    }

    /*清除备份的量程吸光度能量
     * 单独标1标2用参数*/
    private void clearRangeCalA(String compName) {
        updateNoBoardConfigData(compName, "Range1_CAL1_A_Value", "");
        updateNoBoardConfigData(compName, "Range2_CAL1_A_Value", "");
        updateNoBoardConfigData(compName, "Range3_CAL1_A_Value", "");

        updateNoBoardConfigData(compName, "Range1_CAL2_A_Value", "");
        updateNoBoardConfigData(compName, "Range2_CAL2_A_Value", "");
        updateNoBoardConfigData(compName, "Range3_CAL2_A_Value", "");
    }
}
