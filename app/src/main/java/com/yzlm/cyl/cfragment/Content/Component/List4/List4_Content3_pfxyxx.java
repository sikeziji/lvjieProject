package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Dialog.Dialog_select;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.addRangeKB;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3.setUseRange;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.File.MFile.deleteDir;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.passwordUnlockEffectiveWork;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.syncListFactoryParam;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pfxyxx extends SubFragment {


    public static EditText mEtCKB = null;
    private EditText mEtyqbh;
    private EditText mEtyblc_l;
    private EditText mEtyblc_h;
    private EditText mEtlc1_l;
    private EditText mEtlc1_h;
    private EditText mEtlc2_l;
    private EditText mEtlc2_h;
    private EditText mEtlc3_l;
    private EditText mEtlc3_h;
    private EditText mEtzgsjnd;

    private Button btnClearHistory;
    private Button btnClearLog;
    private Button btnClearKBF;
    private Button btnClearRunNotes;
    private Button btnClearOperation;

    private Button mBtnszccsz;
    private Button mBtnhfccsz;
    private Spinner mspxs_baseRange;

    private Callbacks mCallbacks;
    private static List4_Content3_pfxyxx fragment = null;
    public static String[] mrcs; //从配置文件读取的默认参数
    public static String[] gnmc; //从配置文件读取功能名称
    public static String[] xxmc; //分割字符串后的名称
    private static EditText eTCalibrationTemp, eTCalibrationTime;

    public static List4_Content3_pfxyxx newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pfxyxx();
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
        return R.layout.list4_content3_pfxyxx;
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

            byte[] arrayOfByte = DataUtil.shortToByte((short) 5700);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(mCompName + "_查测控板编号" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 3, 200, 1);

            mEtCKB = (EditText) v.findViewById(R.id.eT_HMI);//测控编号
            mEtCKB.setOnFocusChangeListener(new HMIFocusChange());
            mEtCKB.setOnEditorActionListener(new EtFocusChange());
            mEtCKB.setSaveEnabled(false);

            mEtyqbh = (EditText) v.findViewById(R.id.eT_yqbh);//仪器编号
            mEtyqbh.setText(getConfigData(mCompName, "YQBH"));
            mEtyqbh.setOnFocusChangeListener(new YQBHFocusChange());
            mEtyqbh.setOnEditorActionListener(new EtFocusChange());
            mEtyqbh.setSaveEnabled(false);

            mEtyblc_l = (EditText) v.findViewById(R.id.eT_yblc_l);//仪表量程（低）
            mEtyblc_l.setText(getConfigData(mCompName, "YBLCL"));
            mEtyblc_l.setOnFocusChangeListener(new YBLCLFocusChange());
            mEtyblc_l.setOnEditorActionListener(new EtFocusChange());
            mEtyblc_l.setSaveEnabled(false);

            mEtyblc_h = (EditText) v.findViewById(R.id.eT_yblc_h);//仪表量程(高)
            mEtyblc_h.setText(getConfigData(mCompName, "YBLCH"));
            mEtyblc_h.setOnFocusChangeListener(new YBLCHFocusChange());
            mEtyblc_h.setOnEditorActionListener(new EtFocusChange());
            mEtyblc_h.setSaveEnabled(false);


            mEtlc1_l = (EditText) v.findViewById(R.id.eT_lc1_l);//量程1(低)
            mEtlc1_l.setOnFocusChangeListener(new LC1LFocusChange());
            mEtlc1_l.setOnEditorActionListener(new EtFocusChange());
            mEtlc1_l.setText(getConfigData(mCompName, "LC1L"));
            mEtlc1_l.setSaveEnabled(false);

            mEtlc1_h = (EditText) v.findViewById(R.id.eT_lc1_h);//量程1(高)
            mEtlc1_h.setText(getConfigData(mCompName, "LC1H"));
            mEtlc1_h.setOnFocusChangeListener(new LC1HFocusChange());
            mEtlc1_h.setOnEditorActionListener(new EtFocusChange());
            mEtlc1_h.setSaveEnabled(false);

            mEtlc2_l = (EditText) v.findViewById(R.id.eT_lc2_l);//量程2(低)
            mEtlc2_l.setText(getConfigData(mCompName, "LC2L"));
            mEtlc2_l.setOnFocusChangeListener(new LC2LFocusChange());
            mEtlc2_l.setOnEditorActionListener(new EtFocusChange());
            mEtlc2_l.setSaveEnabled(false);

            mEtlc2_h = (EditText) v.findViewById(R.id.eT_lc2_h);//量程2(高)
            mEtlc2_h.setText(getConfigData(mCompName, "LC2H"));
            mEtlc2_h.setOnFocusChangeListener(new LC2HFocusChange());
            mEtlc2_h.setOnEditorActionListener(new EtFocusChange());
            mEtlc2_h.setSaveEnabled(false);

            mEtlc3_l = (EditText) v.findViewById(R.id.eT_lc3_l);//量程3(低)
            mEtlc3_l.setText(getConfigData(mCompName, "LC3L"));
            mEtlc3_l.setOnFocusChangeListener(new LC3LFocusChange());
            mEtlc3_l.setOnEditorActionListener(new EtFocusChange());
            mEtlc3_l.setSaveEnabled(false);

            mEtlc3_h = (EditText) v.findViewById(R.id.eT_lc3_h);//量程3(高)
            mEtlc3_h.setText(getConfigData(mCompName, "LC3H"));
            mEtlc3_h.setOnFocusChangeListener(new LC3HFocusChange());
            mEtlc3_h.setOnEditorActionListener(new EtFocusChange());
            mEtlc3_h.setSaveEnabled(false);

            mEtzgsjnd = (EditText) v.findViewById(R.id.eT_zgsjnd);//重铬酸钾浓度
            mEtzgsjnd.setText(getConfigData(mCompName, "ZGSJND"));
            mEtzgsjnd.setOnFocusChangeListener(new ZGSJNDFocusChange());
            mEtzgsjnd.setOnEditorActionListener(new EtFocusChange());
            mEtzgsjnd.setSaveEnabled(false);

            btnClearHistory = (Button) v.findViewById(R.id.Btn_clearHistory);//清空历史数据
            btnClearHistory.setOnClickListener(new ClearHistoryOnClick());
            btnClearLog = (Button) v.findViewById(R.id.Btn_clearLog);//清空报警记录
            btnClearLog.setOnClickListener(new ClearLogOnClick());
            btnClearKBF = (Button) v.findViewById(R.id.Btn_clearKBF);//清空校准记录
            btnClearKBF.setOnClickListener(new ClearKBFOnClick());

            btnClearRunNotes = (Button) v.findViewById(R.id.Btn_clearRunNote);//清空运行记录
            btnClearRunNotes.setOnClickListener(new ClearRunNotesOnClick());

            btnClearOperation = (Button) v.findViewById(R.id.Btn_clearOperation);//清空操作记录
            btnClearOperation.setOnClickListener(new ClearOperationOnClick());

            mBtnszccsz = (Button) v.findViewById(R.id.Btn_szccsz);//设置出厂设置
            mBtnszccsz.setOnClickListener(new btnOnClickListener());

            mBtnhfccsz = (Button) v.findViewById(R.id.Btn_hhccsz);//恢复出厂设置
            mBtnhfccsz.setOnClickListener(new btnOnClickListener());
            mEtCKB.setText((getCmds(mCompName).getCmd(5700).getValue() == null ? "0" : getCmds(mCompName).getCmd(5700).getValue()).toString());


            LinearLayout layout_zgsj;
            layout_zgsj = (LinearLayout) v.findViewById(R.id.layout_zgsj);
            if (mCompName.contains("CODcr")) {
                layout_zgsj.setVisibility(View.VISIBLE);
            } else {
                layout_zgsj.setVisibility(View.INVISIBLE);
            }
            TextView mxs_baseRange = (TextView) v.findViewById(R.id.text_baseRange);
            mspxs_baseRange = (Spinner) v.findViewById(R.id.spBaseRange);
            try {
                InitSpinner(context, mspxs_baseRange, getResources().getStringArray(R.array.xsbaseRange), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
                mspxs_baseRange.setSelection(Integer.parseInt(getConfigData(mCompName, "XS_BASE_RANGE")) - 1);
                mspxs_baseRange.setOnItemSelectedListener(new mSpXSBaseRangeListener());
            } catch (Exception e) {
                Log.i("except", e.toString());
            }
            if (isHaveMeasCategory(mCompName, "5")) {
                mxs_baseRange.setVisibility(View.VISIBLE);
                mspxs_baseRange.setVisibility(View.VISIBLE);
            } else {
                mxs_baseRange.setVisibility(View.GONE);
                mspxs_baseRange.setVisibility(View.GONE);
            }
            setLoginUserPermission();

            if (passwordUnlockEffectiveWork.equals("335") || (getCmds(mCompName).getCmd(5700).getValue() == null ? "0" : getCmds(mCompName).getCmd(5700).getValue()).toString().equals("")
                    || (getCmds(mCompName).getCmd(5700).getValue() == null ? "0" : getCmds(mCompName).getCmd(5700).getValue()).toString().equals("0")
                    || (getCmds(mCompName).getCmd(5700).getValue() == null ? "0" : getCmds(mCompName).getCmd(5700).getValue()).toString().equals("1")) {
                mEtCKB.setEnabled(true);
            } else {
                mEtCKB.setEnabled(false);
            }
            if (passwordUnlockEffectiveWork.equals("336") || mEtyqbh.getText().toString().equals("") || mEtyqbh.getText().toString().equals("0")) {
                mEtyqbh.setEnabled(true);
            } else {
                mEtyqbh.setEnabled(false);
            }
            if (!getPublicConfigData("LogInName").equals("3")) {
                LinearLayout layoutClearAlarHistory = v.findViewById(R.id.layoutClearAlarHistory);
                layoutClearAlarHistory.setVisibility(getPublicConfigData("ClearNotesBtnIsShow").equals("true") ? View.VISIBLE : View.GONE);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void setLoginUserPermission() {
        if (getPublicConfigData("LogInName").equals("3")) {
            mBtnszccsz.setVisibility(View.VISIBLE);
            mBtnhfccsz.setVisibility(View.VISIBLE);
            btnClearKBF.setVisibility(View.VISIBLE);
            btnClearRunNotes.setVisibility(View.VISIBLE);
            btnClearOperation.setVisibility(View.VISIBLE);
        } else {
            mBtnszccsz.setVisibility(View.GONE);
            mBtnhfccsz.setVisibility(View.GONE);
            btnClearKBF.setVisibility(View.GONE);
            btnClearRunNotes.setVisibility(View.GONE);
            btnClearOperation.setVisibility(View.GONE);
        }
        // 非管理员情况，则全部不可修改
        if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
            mEtyblc_l.setEnabled(true);
            mEtyblc_h.setEnabled(true);
            mEtlc1_l.setEnabled(true);
            mEtlc1_h.setEnabled(true);
            mEtlc2_l.setEnabled(true);
            mEtlc2_h.setEnabled(true);
            mEtlc3_l.setEnabled(true);
            mEtlc3_h.setEnabled(true);
            mEtzgsjnd.setEnabled(true);
            btnClearHistory.setVisibility(View.VISIBLE);
            btnClearLog.setVisibility(View.VISIBLE);
        } else {
            mEtyblc_l.setEnabled(false);
            mEtyblc_h.setEnabled(false);
            mEtlc1_l.setEnabled(false);
            mEtlc1_h.setEnabled(false);
            mEtlc2_l.setEnabled(false);
            mEtlc2_h.setEnabled(false);
            mEtlc3_l.setEnabled(false);
            mEtlc3_h.setEnabled(false);
            mEtzgsjnd.setEnabled(false);
            btnClearHistory.setVisibility(View.GONE);
            btnClearLog.setVisibility(View.GONE);
        }
    }

    private class mSpXSBaseRangeListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (Integer.parseInt(getConfigData(mCompName, "XS_BASE_RANGE")) != (mspxs_baseRange.getSelectedItemId() + 1)) {
                    saveOperationLogDataModifyMsg(mCompName, "XS_BASE_RANGE", String.valueOf(mspxs_baseRange.getSelectedItemId() + 1), "稀释基础量程", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "XS_BASE_RANGE", String.valueOf(mspxs_baseRange.getSelectedItemId() + 1));

                    int baseRange = Integer.parseInt(getConfigData(mCompName, "XS_BASE_RANGE"));
                    //切换基础量程后自动更新量程三KB
                    Calc calc = getRangePar(mCompName, baseRange);
                    if (calc != null) {
                        addRangeKB(mCompName, "3", calc.getK(String.valueOf(baseRange), mCompName),
                                calc.getB(String.valueOf(baseRange), mCompName), calc.getF(String.valueOf(baseRange), mCompName));
                    }
                }
            } catch (Exception e) {
                Log.i("except", e.toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
        //getListInfo().put(mCompName, mInfoDB);
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

    private class HMIFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                setCKAEditIntData(mCompName, (EditText) v, Integer.parseInt(getConfigData(mCompName, "YXWS")), 0, 2000000000, 0, 67, "设置测控板编号");
                if (!mEtCKB.getText().toString().equals("0")) {
                    mEtCKB.setEnabled(false);
                    passwordUnlockEffectiveWork = "";
                } else {
                    mEtCKB.setText("0");
                }
                syncList();
            }
        }
    }

    private class YQBHFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                saveOperationLogDataModifyMsg(mCompName, "YQBH", mEtyqbh.getText().toString(), "设置仪器编号", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "YQBH", mEtyqbh.getText().toString());
                mEtyqbh.setEnabled(false);
                passwordUnlockEffectiveWork = "";
                syncList();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command").split(";")[1];
            if (stringExtra.contains("history")) {

                saveOperationLogMsg(mCompName, "清除历史记录", ErrorLog.msgType.操作_信息);
                History history = new History(context);
                history.clearSelectData(mCompName);
                showOKDialogMsg(getString(R.string.set_finished));
                AddError(mCompName, 586, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("alarm")) {

                saveOperationLogMsg(mCompName, "清除报警记录", ErrorLog.msgType.操作_信息);
                ErrorLog error = new ErrorLog(context);
                ErrorLog.msgType[] types = {ErrorLog.msgType.报错_警告, ErrorLog.msgType.报错_错误};
                error.clearSelectErrorData(mCompName, types);
                showOKDialogMsg(getString(R.string.set_finished));
                AddError(mCompName, 588, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("Calibration")) {

                saveOperationLogMsg(mCompName, "清除校准记录", ErrorLog.msgType.操作_信息);
                backupLastKBF();
                Kbf kbf = new Kbf(context);
                kbf.clearSelectKBFData(mCompName);
                addLastKBF();
                clearRangeCalA(mCompName);
                showOKDialogMsg(getString(R.string.set_finished));
                AddError(mCompName, 587, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("running")) {

                saveOperationLogMsg(mCompName, "清除运行记录", ErrorLog.msgType.操作_信息);
                ErrorLog error = new ErrorLog(context);
                ErrorLog.msgType[] types = {ErrorLog.msgType.报错_信息, ErrorLog.msgType.校准_信息, ErrorLog.msgType.运行_信息
                        , ErrorLog.msgType.登录_信息, ErrorLog.msgType.运维_信息
                        , ErrorLog.msgType.其他_信息};
                error.clearSelectErrorData(mCompName, types);
                showOKDialogMsg(getString(R.string.set_finished));

            } else if (stringExtra.contains("operation")) {

                DevLog log = new DevLog(context);
                ErrorLog.msgType[] types = {ErrorLog.msgType.操作_信息};
                log.clearSelectErrorData(mCompName, types);
                showOKDialogMsg(getString(R.string.set_finished));
                saveOperationLogMsg(mCompName, "清除操作记录", ErrorLog.msgType.操作_信息);

            } else if ((stringExtra.contains("restore"))) {

                saveOperationLogMsg(mCompName, "恢复出厂", ErrorLog.msgType.操作_信息);
                recoverFactorySetting();
                mCallbacks.onDialogRS();

            } else if ((stringExtra.contains("store"))) {

                saveOperationLogMsg(mCompName, "设置出厂", ErrorLog.msgType.操作_信息);
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

    private class YBLCLFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtyblc_l.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtyblc_l.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "YBLCL", strValue, "设置仪表量程L", ErrorLog.msgType.操作_信息);
                    AddError(mCompName, 646, strValue, 运维_信息);
                    updateConfigData(mCompName, "YBLCL", strValue);

                    mEtyblc_l.setText(strValue);
                } else {
                    mEtyblc_l.setText(getConfigData(mCompName, "YBLCL"));
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
                    String strValue = editDataFormat(mEtyblc_h.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "YBLCH", strValue, "设置仪表量程H", ErrorLog.msgType.操作_信息);
                    AddError(mCompName, 645, strValue, 运维_信息);
                    updateConfigData(mCompName, "YBLCH", strValue);
                    mEtyblc_h.setText(strValue);


                    float ALARM_H = Float.valueOf(getConfigData(mCompName, "ALARM_H"));
                    float ALARM_L = Float.valueOf(getConfigData(mCompName, "ALARM_L"));

                    if (ALARM_H > Float.valueOf(strValue)) {
                        saveOperationLogDataModifyMsg(mCompName, "ALARM_H", strValue, "设置报警上限", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "ALARM_H", strValue);
                    }
                    if (ALARM_L > Float.valueOf(strValue)) {
                        saveOperationLogDataModifyMsg(mCompName, "ALARM_H", "0", "设置报警下限", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "ALARM_H", "0");

                    }
                } else {
                    mEtyblc_h.setText(getConfigData(mCompName, "YBLCH"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    /**
     * 量程1下限
     */
    private class LC1LFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc1_l.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc1_l.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "LC1L", strValue, "设置量程1L", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "LC1L", strValue);
                    AddError(mCompName, 635, mEtlc1_l.getText().toString(), ErrorLog.msgType.运维_信息);

                    mEtlc1_l.setText(strValue);
                } else {
                    mEtlc1_l.setText(getConfigData(mCompName, "LC1L"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    /**
     * 量程1上限
     */
    private class LC1HFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc1_h.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc1_h.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));

                    saveOperationLogDataModifyMsg(mCompName, "LC1H", strValue, "设置量程1H", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "LC1H", strValue);
                    AddError(mCompName, 634, mEtlc1_h.getText().toString(), ErrorLog.msgType.运维_信息);


                    mEtlc1_h.setText(strValue);
                } else {
                    mEtlc1_h.setText(getConfigData(mCompName, "LC1H"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    /**
     * 量程2下限
     */
    private class LC2LFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc2_l.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc2_l.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "LC2L", strValue, "设置量程2L", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "LC2L", strValue);
                    AddError(mCompName, 652, strValue, ErrorLog.msgType.运维_信息);
                    mEtlc2_l.setText(strValue);
                } else {
                    mEtlc2_l.setText(getConfigData(mCompName, "LC2L"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    /**
     * 量程2上限
     */
    private class LC2HFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc2_h.getText().toString().equals("")) {
                    // 量程2H为1 则设备只有量程1的情况，判断当前是否选择到量程1
                    if (mEtlc2_h.getText().toString().equals("0")
                            && (!getConfigData(mCompName, "RANGE").equals("1"))) {
                        // 量程强制选择为1
                        saveOperationLogDataModifyMsg(mCompName, "RANGE", String.valueOf(1), "设置量程", ErrorLog.msgType.操作_信息);
                        setUseRange(mCompName, String.valueOf(1));
                        Toast.makeText(context, context.getString(R.string.automatic_switch_to_range_1), Toast.LENGTH_SHORT).show();
                    }

                    if (mEtlc2_h.getText().toString().equals("0")) {
                        saveOperationLogDataModifyMsg(mCompName, "OneKeyCalSwitch", "close", "只有量程1，一键校准关闭", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "OneKeyCalSwitch", "close");

                        if (getConfigData(mCompName, "AUTORANGE").equals("true")) {
                            saveOperationLogDataModifyMsg(mCompName, "AUTORANGE", "false", "只有量程1，量程切换关闭", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "AUTORANGE", "false");
                            saveOperationLogDataModifyMsg(mCompName, "AUTO_RANGE_JOB_SWITCH", "false", "只有量程1，量程切换后是否启动测量关闭", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "AUTO_RANGE_JOB_SWITCH", "false");
                            saveOperationLogDataModifyMsg(mCompName, "AUTO_RANGE_SWITCH_RECOVERY", "false", "只有量程1，量程切换后是否复原关闭", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "AUTO_RANGE_SWITCH_RECOVERY", "false");
                        }
                        Toast.makeText(context, R.string.oneKeyCalSwitchOff, Toast.LENGTH_SHORT).show();
                    }

                    String strValue = editDataFormat(mEtlc2_h.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "LC2H", strValue, "设置量程2H", ErrorLog.msgType.操作_信息);
                    AddError(mCompName, 651, strValue, ErrorLog.msgType.运维_信息);
                    updateConfigData(mCompName, "LC2H", strValue);

                    mEtlc2_h.setText(strValue);
                } else {
                    mEtlc2_h.setText(getConfigData(mCompName, "LC2H"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    /**
     * 量程3下限
     */
    private class LC3LFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc3_l.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtlc3_l.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "LC3L", strValue, "设置量程3L", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "LC3L", strValue);
                    AddError(mCompName, 654, strValue, ErrorLog.msgType.运维_信息);
                    mEtlc3_l.setText(strValue);
                } else {
                    mEtlc3_l.setText(getConfigData(mCompName, "LC3L"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    private class ZGSJNDFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtzgsjnd.getText().toString().equals("")) {
                    String strValue = editDataFormat(mEtzgsjnd.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "ZGSJND", strValue, "设置重铬酸钾浓度", ErrorLog.msgType.操作_信息);
                    updateConfigData(mCompName, "ZGSJND", strValue);

                    mEtzgsjnd.setText(strValue);
                } else {
                    mEtzgsjnd.setText(getConfigData(mCompName, "ZGSJND"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    /**
     * 量程3上限
     */
    private class LC3HFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if (!mEtlc3_h.getText().toString().equals("")) {
                    if (mEtlc3_h.getText().toString().equals("0")
                            && (getConfigData(mCompName, "RANGE").equals("3"))) {
                        // 量程强制选择为1
                        saveOperationLogDataModifyMsg(mCompName, "RANGE", String.valueOf(1), "设置量程", ErrorLog.msgType.操作_信息);
                        setUseRange(mCompName, String.valueOf(1));
                        Toast.makeText(context, context.getString(R.string.automatic_switch_to_range_1), Toast.LENGTH_SHORT).show();
                    }
                    String strValue = editDataFormat(mEtlc3_h.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "LC3H", strValue, "设置量程3H", ErrorLog.msgType.操作_信息);
                    AddError(mCompName, 653, strValue, ErrorLog.msgType.运维_信息);
                    updateConfigData(mCompName, "LC3H", strValue);

                    mEtlc3_h.setText(strValue);
                } else {
                    mEtlc3_h.setText(getConfigData(mCompName, "LC3H"));
                    Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }
    }

    public static void showDialogMsg(SubFragment fragment, String string, String sign) {
        main.removeDestopText(mfb);
        Dialog_select dialog_select = new Dialog_select(mCompName);
        Bundle bundle = new Bundle();
        bundle.putString("alert-select", string + ";" + sign);
        dialog_select.setArguments(bundle);
        dialog_select.setTargetFragment(fragment, 1);
        dialog_select.show(fm, "Dialog_select");
    }

    public static void showOKDialogMsg(String string) {
        main.removeDestopText(mfb);
        Dialog_Err st = new Dialog_Err();
        Bundle bundle = new Bundle();
        bundle.putString("alert-err", string);
        st.setArguments(bundle);
        st.show(fm, "Dialog_err");
    }

    private void setFactorySettings() {
        SendManager.SendCmd(mCompName + "_设置出厂设置" + "_8_0F", S0, 3, 200, null);

        updateConfigData(mCompName, "FAC_HMIBH", getConfigData(mCompName, "HMIBH"));
        updateConfigData(mCompName, "FAC_YQBH", getConfigData(mCompName, "YQBH"));
        updateConfigData(mCompName, "FAC_YBLCL", getConfigData(mCompName, "YBLCL"));
        updateConfigData(mCompName, "FAC_YBLCH", getConfigData(mCompName, "YBLCH"));

        updateConfigData(mCompName, "FAC_LC1L", getConfigData(mCompName, "LC1L"));
        updateConfigData(mCompName, "FAC_LC1H", getConfigData(mCompName, "LC1H"));
        updateConfigData(mCompName, "FAC_LC2L", getConfigData(mCompName, "LC2L"));
        updateConfigData(mCompName, "FAC_LC2H", getConfigData(mCompName, "LC2H"));
        updateConfigData(mCompName, "FAC_LC3L", getConfigData(mCompName, "LC3L"));
        updateConfigData(mCompName, "FAC_LC3H", getConfigData(mCompName, "LC3H"));
        backupLastKBF();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(new Date());
        updateConfigData(mCompName, "FACTORY_TIME", time);
        syncList();
        syncListFactoryParam();
    }


    private void recoverFactorySetting() {

        SendManager.SendCmd(mCompName + "_恢复出厂设置" + "_8_10", S0, 3, 200, null);
        /*if (mCompName.equals("CODmn") || mCompName == "CODmn"){
            try{
                //新增 恢复出厂设置
                if (mrcs == null){
                    String fileContent;
                    fileContent = findFile("Csoft/hfccsz/", "hfccsz");
                    if (fileContent != null) {
                        mrcs = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
                    }
                }
                //发送
                boolean tj = false;
                String jlcs = "";
                for (String cs : mrcs){
                    //计量参数
                    if (cs.equals("//begin")){
                        tj = true;
                    }
                    if (cs.equals("//end")){
                        tj = false;
                    }
                    if (tj){
                        if (!cs.contains("//begin") && !cs.contains("//end")){
                            jlcs +=cs + "#";
                        }
                    }
                    if (tj == false && !jlcs.equals("")){
                        String[] jg = jlcs.split("#");
                        int sy = jg.length - 1;
                        if (sy == 26 || sy == 1){
                            //判断计量单元(计量阀值) 计量参数
                            boolean jldy = false;
                            int i = 0;
                            String[] a = new String[sy];
                            int selectedItemIndex = 0;
                            for (String aa : jg){
                                if (aa.contains("计量阀值") || aa.equals("计量阀值")){
                                    jldy = true;
                                }
                                if (!aa.contains(",")){
                                    selectedItemIndex = Integer.parseInt(aa.split("_")[0]);
                                }else {
                                    if (jldy){
                                        String strjlbl1, strjlbl2;
                                        strjlbl1 = aa.split(",")[1] == null? "" : aa.split(",")[1];
                                        strjlbl2 = aa.split(",")[2] == null? "" : aa.split(",")[2];
                                        if (strjlbl1 =="" || strjlbl1.equals("") || strjlbl2 == "" || strjlbl2.equals("")){
                                            throw new Exception("参数错误");
                                        }
                                        byte[] sByte = copybyte(toByteArray(0, 4), toByteArray(0, 4), toByteArray(Integer.parseInt(strjlbl1), 4), toByteArray(Integer.parseInt(strjlbl2), 4));
                                        SendManager.SendCmd(mCompName + "_" + "设置滴定液计数阈值_06_" + (88 + selectedItemIndex - 1), S0, 3, 100, sByte);
                                        tj = false;
                                        jlcs = "";
                                    }else {
                                        if (aa.contains(",")){
                                            a[i] = aa.split(",")[1];
                                            i++;
                                        }
                                    }
                                }
                            }
                            if (!jldy){
                                byte[] dataByte = copybyte(toByteArray(Integer.parseInt(a[0]), 4), toByteArray(Integer.parseInt(a[1]), 4),
                                        toByteArray(Integer.parseInt(a[2]), 4), toByteArray(Integer.parseInt(a[3]), 4),
                                        toByteArray(Integer.parseInt(a[4]), 4), toByteArray(Integer.parseInt(a[5]), 4),
                                        toByteArray(Integer.parseInt(a[6]), 4), toByteArray(Integer.parseInt(a[7]), 4),
                                        toByteArray(Integer.parseInt(a[8]), 4), toByteArray(Integer.parseInt(a[9]), 4),
                                        toByteArray(Integer.parseInt(a[10]), 4), toByteArray(Integer.parseInt(a[11]), 4),
                                        toByteArray(Integer.parseInt(a[12]), 4), toByteArray(Integer.parseInt(a[13]), 4),
                                        toByteArray(Integer.parseInt(a[14]), 4), toByteArray(Integer.parseInt(a[15]), 4),
                                        toByteArray(Integer.parseInt(a[16]), 4), toByteArray(Integer.parseInt(a[17]), 4),
                                        toByteArray(Integer.parseInt(a[18]), 4), toByteArray(Integer.parseInt(a[19]), 4),
                                        toByteArray(Integer.parseInt(a[20]), 4), toByteArray(Integer.parseInt(a[21]), 4),
                                        toByteArray(Integer.parseInt(a[22]), 4), toByteArray(Integer.parseInt(a[23]), 4),
                                        toByteArray(Integer.parseInt(a[24]), 4), toByteArray(Integer.parseInt(a[25]), 4));

                                SendManager.SendCmd(MainActivity.mCompName + "_计量板" + selectedItemIndex + "计量参数设置_06_" + (108 + (selectedItemIndex - 1)), S0, 3, 200, dataByte);
                                tj = false;
                                jlcs = "";
                            }
                        }else {
                            Toast.makeText(context, "参数错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (tj == false && jlcs.equals("") && !cs.equals("//end") && !cs.equals("//begin")) {
                        String glmc = cs.split(",")[0];  //名称
                        String csz = cs.split(",")[1];   //参数默认值
                        String A = "06";                        //功能码
                        if (glmc.equals("ORP阈值")){
                            glmc ="ORP滴定判断阈值";
                        }
                        if (glmc.equals("测量阀值")){
                            glmc = "滴定液计数阈值";
                        }
                        if (glmc.equals("终点滴数")){
                            glmc ="终点后执行滴数";
                        }
                        if (glmc.equals("采集次数")){
                            glmc = "校准等待时间";
                        }
                        if (glmc.equals("LED通道")){
                            glmc = "LED增益";
                        }
                        if (glmc.equals("最小体积")){
                            glmc = "最小滴定体积";
                        }
                        if (glmc.equals("最大体积")){
                            glmc = "最大滴定体积";
                        }
                        if (glmc.equals("最小时间")){
                            glmc = "最小等待时间";
                        }
                        if (glmc.equals("最大时间")){
                            glmc = "最大等待时间";
                        }
                        if (glmc.equals("光强上限")){
                            glmc = "滴定起始光强上限";
                        }
                        if (glmc.equals("光强下限")){
                            glmc = "滴定起始光强下限";
                        }
                        String B = fscs(glmc);
                        if (B=="" || B.equals("B")){
                            throw new Exception("参数错误");
                        }
                        String fszl = "设置" + glmc + "_" + A + "_" + B;  //发送参数
                        //更新数据库
                        //根据功能名称获取对应的key
                        String clcs = glmc;
                        if (clcs.equals("消解温度") || clcs.equals("消解时长") || clcs.equals("显色温度") || clcs.equals("显色时长") || clcs.equals("滴定液体积") || clcs.equals("消解降温") || clcs.equals("显色降温") || clcs.equals("显色静置") || clcs.equals("水样体积") || clcs.equals("氧化剂体积") || clcs.equals("还原剂体积") || clcs.equals("还原剂浓度") || clcs.equals("等待静置") || clcs.equals("校准消解温度") || clcs.equals("校准消解时长")) {//测量参数设置时更改数据库key
                            String Key = Key(glmc);
                            updateConfigData(mCompName, Key, csz);
                        }
                        String strNum = "";
                        if (clcs.equals("LED增益")) {
                            strNum = csz.split("_")[1];
                        } else {
                            strNum = csz;
                        }
                        if (!strNum.equals("")) {
                            if (!clcs.equals("水样体积") || !clcs.equals("氧化剂体积") || !clcs.equals("还原剂体积") || !clcs.equals("还原剂浓度")) {
                                if (strNum.contains(".")) {
                                    float fdata = Float.parseFloat(strNum);
                                    byte[] ddytjByte = floatToBytes(fdata);
                                    SendManager.SendCmd(mCompName + "_" + fszl, S0, 3, 100, ddytjByte);
                                } else {
                                    int mrz = Integer.parseInt(strNum);
                                    byte[] xjwdByte = copybyte(toByteArray(mrz, 4));
                                    SendManager.SendCmd(mCompName + "_" + fszl, S0, 3, 100, xjwdByte);
                                }
                            }
                        }
                    }
                }

                updateConfigData(mCompName, "HMIBH", getConfigData(mCompName, "FAC_HMIBH"));
                updateConfigData(mCompName, "YQBH", getConfigData(mCompName, "FAC_YQBH"));

                updateConfigData(mCompName, "YBLCL", getConfigData(mCompName, "FAC_YBLCL"));
                updateConfigData(mCompName, "YBLCH", getConfigData(mCompName, "FAC_YBLCH"));

                updateConfigData(mCompName, "LC1L", getConfigData(mCompName, "FAC_LC1L"));
                updateConfigData(mCompName, "LC1H", getConfigData(mCompName, "FAC_LC1H"));

                updateConfigData(mCompName, "LC2L", getConfigData(mCompName, "FAC_LC2L"));
                updateConfigData(mCompName, "LC2H", getConfigData(mCompName, "FAC_LC2H"));

                updateConfigData(mCompName, "LC3L", getConfigData(mCompName, "FAC_LC3L"));
                updateConfigData(mCompName, "LC3H", getConfigData(mCompName, "FAC_LC3H"));

                addLastKBF();
                Toast.makeText(context, "恢复出厂设置成功", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(context, "参数错误恢复出厂设置失败", Toast.LENGTH_SHORT).show();
            }
        }else {*/
        updateConfigData(mCompName, "HMIBH", getConfigData(mCompName, "FAC_HMIBH"));
        updateConfigData(mCompName, "YQBH", getConfigData(mCompName, "FAC_YQBH"));

        updateConfigData(mCompName, "YBLCL", getConfigData(mCompName, "FAC_YBLCL"));
        updateConfigData(mCompName, "YBLCH", getConfigData(mCompName, "FAC_YBLCH"));

        updateConfigData(mCompName, "LC1L", getConfigData(mCompName, "FAC_LC1L"));
        updateConfigData(mCompName, "LC1H", getConfigData(mCompName, "FAC_LC1H"));

        updateConfigData(mCompName, "LC2L", getConfigData(mCompName, "FAC_LC2L"));
        updateConfigData(mCompName, "LC2H", getConfigData(mCompName, "FAC_LC2H"));

        updateConfigData(mCompName, "LC3L", getConfigData(mCompName, "FAC_LC3L"));
        updateConfigData(mCompName, "LC3H", getConfigData(mCompName, "FAC_LC3H"));

        addLastKBF();
//        }
    }


    private Calc getRangePar(String component, int range) {

        CalcTable ct = getCalc(component);
        return ct.getCalc(String.valueOf(range));
    }

    /***
     * 备份最新的各个量程的那一组KBF
     * */
    private void backupLastKBF() {
        Calc calc1 = getRangePar(mCompName, 1);
        Calc calc2 = getRangePar(mCompName, 2);
        Calc calc3 = getRangePar(mCompName, 3);

        if (calc1 != null) {
            updateConfigData(mCompName, "FAC_LC1K", String.valueOf(calc1.getK(String.valueOf(1), mCompName)));
            updateConfigData(mCompName, "FAC_LC1B", String.valueOf(calc1.getB(String.valueOf(1), mCompName)));
            updateConfigData(mCompName, "FAC_LC1F", String.valueOf(calc1.getF(String.valueOf(1), mCompName)));
        }
        if (calc2 != null) {
            updateConfigData(mCompName, "FAC_LC2K", String.valueOf(calc2.getK(String.valueOf(2), mCompName)));
            updateConfigData(mCompName, "FAC_LC2B", String.valueOf(calc2.getB(String.valueOf(2), mCompName)));
            updateConfigData(mCompName, "FAC_LC2F", String.valueOf(calc2.getF(String.valueOf(2), mCompName)));
        }
        if (calc3 != null) {
            updateConfigData(mCompName, "FAC_LC3K", String.valueOf(calc3.getK(String.valueOf(3), mCompName)));
            updateConfigData(mCompName, "FAC_LC3B", String.valueOf(calc3.getB(String.valueOf(3), mCompName)));
            updateConfigData(mCompName, "FAC_LC3F", String.valueOf(calc3.getF(String.valueOf(3), mCompName)));
        }
        syncListFactoryParam();
    }

    /**
     * 将最后设置出厂设置的KBF 增加到校准记录中
     */
    private void addLastKBF() {
        try {
            switch (GetPlatRangSum(mCompName)) {
                case 3:
                    addRangeKB(mCompName, "3", Double.valueOf(getConfigData(mCompName, "FAC_LC3K")), Double.valueOf(getConfigData(mCompName, "FAC_LC3B")),
                            Double.valueOf(getConfigData(mCompName, "FAC_LC3F")));
                case 2:
                    addRangeKB(mCompName, "2", Double.valueOf(getConfigData(mCompName, "FAC_LC2K")), Double.valueOf(getConfigData(mCompName, "FAC_LC2B")),
                            Double.valueOf(getConfigData(mCompName, "FAC_LC2F")));
                case 1:
                    addRangeKB(mCompName, "1", Double.valueOf(getConfigData(mCompName, "FAC_LC1K")), Double.valueOf(getConfigData(mCompName, "FAC_LC1B")),
                            Double.valueOf(getConfigData(mCompName, "FAC_LC1F")));
                    break;
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "] 设置出厂增加校准记录异常" + e.toString());
        }
    }

    /*清除备份的量程吸光度能量
     * 单独标1标2用参数*/
    private void clearRangeCalA(String compName) {
        updateConfigData(compName, "Range1_CAL1_A_Value", "");
        updateConfigData(compName, "Range2_CAL1_A_Value", "");
        updateConfigData(compName, "Range3_CAL1_A_Value", "");

        updateConfigData(compName, "Range1_CAL2_A_Value", "");
        updateConfigData(compName, "Range2_CAL2_A_Value", "");
        updateConfigData(compName, "Range3_CAL2_A_Value", "");
    }


    /**
     * 通过路径读取文件内容
     *
     * @param path
     * @param name
     * @return
     */
    public static String findFile(String path, String name) {

        if (Global.SdcardPath == null && Global.extSdcardPath == null) {
            return null;
        }
        String dirPath;
        if (Global.SdcardPath != null) {
            dirPath = Global.SdcardPath;
        } else {
            dirPath = Global.extSdcardPath;
        }
        String pathFileName;
        pathFileName = dirPath + path + name + ".txt";
        File dbFile = new File(dirPath + path);
        //判断文件路径是否存在；
        if (!dbFile.exists()) {
            //创建文件
            dbFile.mkdirs();
        }
        if (dbFile.exists()) {
            return readString(pathFileName, "GBK");
        } else {
            return null;
        }
    }

    /**
     * 通过功能名称获取key
     */
    public static String Key(String glmc) {
        String mc = "";
        switch (glmc) {
            case "消解温度":
                mc = "xjwd";
                break;
            case "消解时长":
                mc = "xjsc";
                break;
            case "显色温度":
                mc = "xswd";
                break;
            case "显色时长":
                mc = "xssc";
                break;
            case "滴定液体积":
                mc = "TITRATION_VOLUME";
                break;
            case "消解降温":
                mc = "xjjw";
                break;
            case "显色降温":
                mc = "xsjw";
                break;
            case "显色静置":
                mc = "xsjz";
                break;
            case "水样体积":
                mc = "WATER_VOLUME";
                break;
            case "氧化剂体积":
                mc = "OXIDANT_VOLUME";
                break;
            case "还原剂体积":
                mc = "REDUCTANT_VOLUME";
                break;
            case "还原剂浓度":
                mc = "REDUCTANT_COC";
                break;
            case "等待静置":
                mc = "waitStanding";
                break;
            case "校准消解温度":
                mc = "calibrationTemp";
                break;
            case "校准消解时长":
                mc = "calibrationTime";
                break;
        }
        return mc;
    }

    /**
     * 通过名称获取发送参数
     */
    public static String fscs(String glmc) {
        String cs = "";
        switch (glmc) {
            case "消解温度":
                cs = "4";
                break;
            case "消解时长":
                cs = "5";
                break;
            case "显色温度":
                cs = "6";
                break;
            case "显色时长":
                cs = "7";
                break;
            case "滴定液体积":
                cs = "138";
                break;
            case "滴定转速":
                cs = "128";
                break;
            case "ORP滴定判断阈值":
                cs = "156";
                break;
            case "滴定液计数阈值":
                cs = "136";
                break;
            case "填充转速":
                cs = "129";
                break;
            case "终点判断阈值":
                cs = "137";
                break;
            case "终点后执行滴数":
                cs = "130";
                break;
            case "校准等待时间":
                cs = "131";
                break;
            case "LED增益":
                cs = "66";
                break;
            case "消解降温":
                cs = "62";
                break;
            case "显色降温":
                cs = "63";
                break;
            case "显色静置":
                cs = "64";
                break;
            case "等待静置":
                cs = "159";
                break;
            case "校准消解温度":
                cs = "217";
                break;
            case "校准消解时长":
                cs = "218";
                break;
            case "消解5温度A":
                cs = "161";
                break;
            case "消解5温度B":
                cs = "162";
                break;
            case "消解5保温效率A":
                cs = "163";
                break;
            case "消解5保温效率B":
                cs = "164";
                break;
            case "消解5保温效率C":
                cs = "165";
                break;
            case "消解6温度A":
                cs = "166";
                break;
            case "消解6温度B":
                cs = "167";
                break;
            case "消解6保温效率A":
                cs = "168";
                break;
            case "消解6保温效率B":
                cs = "169";
                break;
            case "消解6保温效率C":
                cs = "170";
                break;
            case "最小滴定体积":
                cs = "157";
                break;
            case "最大滴定体积":
                cs = "158";
                break;
            case "最小等待时间":
                cs = "134";
                break;
            case "最大等待时间":
                cs = "135";
                break;
            case "滴定间隔":
                cs = "160";
                break;
            case "滴定计时A":
                cs = "212";
                break;
            case "滴定计时B":
                cs = "213";
                break;
            case "滴定起始光强上限":
                cs = "214";
                break;
            case "滴定起始光强下限":
                cs = "215";
                break;
        }
        return cs;
    }
}
