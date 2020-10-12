package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Dialog.Dialog_select;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.addRangeKB;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.File.MFile.deleteDir;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.syncListFactoryParam;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

public class List4_Content3_pas_fxyxx extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pas_fxyxx fragment = null;

    private Button btnClearHistory;
    private Button btnClearLog;
    private Button btnClearKBF;
    private Button btnClearRunNotes;
    private Button btnClearOperation;

    private Button mBtnszccsz;
    private Button mBtnhfccsz;

    public static List4_Content3_pas_fxyxx newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pas_fxyxx();
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
        return R.layout.list4_content3_pas_fxyxx;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p4);
        mBtnReturn.setOnClickListener(new btnClick());

        btnClearKBF = (Button) v.findViewById(R.id.Btn_clearKBF);
        btnClearKBF.setOnClickListener(new ClearKBFOnClick());

        btnClearRunNotes = (Button) v.findViewById(R.id.Btn_clearRunNote);
        btnClearRunNotes.setOnClickListener(new ClearRunNotesOnClick());

        /*btnClearOperation = (Button) v.findViewById(R.id.Btn_clearOperation);
        btnClearOperation.setOnClickListener(new ClearOperationOnClick());*/

        mBtnszccsz = (Button) v.findViewById(R.id.Btn_szccsz);
        mBtnszccsz.setOnClickListener(new btnOnClickListener());

        mBtnhfccsz = (Button) v.findViewById(R.id.Btn_hhccsz);
        mBtnhfccsz.setOnClickListener(new btnOnClickListener());
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
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

    private void syncList() {
        //getListInfo().put(mCompName, mInfoDB);
        closeInputMethod(v);
        FullWindows(mActivityWindow);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command").split(";")[1];
            if (stringExtra.contains("history")) {

                saveOperationLogMsg(mCompName, "清除历史记录", ErrorLog.msgType.其他_信息);
                History history = new History(context);
                history.clearSelectData(mCompName);
                showOKDialogMsg(getString(R.string.set_finished));
                AddError(mCompName, 586, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("alarm")) {

                saveOperationLogMsg(mCompName, "清除报警记录", ErrorLog.msgType.其他_信息);
                ErrorLog error = new ErrorLog(context);
                ErrorLog.msgType[] types = {ErrorLog.msgType.报错_警告, ErrorLog.msgType.报错_错误};
                error.clearSelectErrorData(mCompName, types);
                showOKDialogMsg(getString(R.string.set_finished));
                AddError(mCompName, 588, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("Calibration")) {

                saveOperationLogMsg(mCompName, "清除校准记录", ErrorLog.msgType.其他_信息);
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

}
