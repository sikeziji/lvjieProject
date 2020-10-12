package com.yzlm.cyl.cfragment.Content.Module.ListSetting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Dialog.Dialog_select;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.addRangeKB;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mBackUpMeasBordName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.syncListFactoryParam;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List_Content_pfxyxx extends SubFragment {


    Button btnClearLog, btnClearKBF, btnClearOperation;

    private Callbacks mCallbacks;
    static List_Content_pfxyxx fragment = null;

    public static List_Content_pfxyxx newInstance(boolean reNewSub) {
        if (fragment == null || reNewSub) {
            fragment = new List_Content_pfxyxx();
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
        return R.layout.list_content_pfxyxx;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        try {
            btnClearLog = (Button) v.findViewById(R.id.Btn_clearLog);
            btnClearLog.setOnClickListener(new ClearLogOnClick());

            btnClearKBF = (Button) v.findViewById(R.id.Btn_clearKBF);
            btnClearKBF.setOnClickListener(new ClearKBFOnClick());


            btnClearOperation = (Button) v.findViewById(R.id.Btn_clearOperation);
            btnClearOperation.setOnClickListener(new ClearOperationOnClick());

            if (QueryMeasCateg(mCompName).equals("6")) {
                btnClearKBF.setVisibility(View.GONE);
            } else if (QueryMeasCateg(mCompName).equals("5")) {
                btnClearKBF.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }


    private class ClearKBFOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDialogMsg(fragment, getString(R.string.clear_all_cal_records), "Calibration");
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command").split(";")[1];
            if (stringExtra.contains("alarm")) {

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

                showOKDialogMsg(getString(R.string.set_finished));
                AddError(mCompName, 587, ErrorLog.msgType.其他_信息);

            } else if (stringExtra.contains("operation")) {

                DevLog log = new DevLog(context);
                ErrorLog.msgType[] types = {ErrorLog.msgType.其他_信息};
                log.clearSelectErrorData(mCompName, types);
                showOKDialogMsg(getString(R.string.set_finished));
                saveOperationLogMsg(mCompName, "清除操作记录", ErrorLog.msgType.其他_信息);
            }
        } else {
            mCallbacks.onDialogRS();
        }
    }

    public static void showDialogMsg(SubFragment fragment, String string, String sign) {
        main.removeDestopText(mfb);
        Dialog_select dialog_select = new Dialog_select(mCompName);
        Bundle bundle = new Bundle();
        bundle.putString("alert-select", string + ";" + String.valueOf(sign));
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


    public Calc getRangePar(String component, int range) {

        CalcTable ct = getCalc(component);
        Calc calc = ct.getCalc(String.valueOf(range));
        return calc;
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
            switch (GetPlatRangSum(mBackUpMeasBordName)) {
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

}
