package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy.extKBParReset;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.addRangeKB;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3.setUseRange;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pfxyxx.showDialogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pfxyxx.showOKDialogMsg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus2;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus4;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/9/5
 */
public class List4_Content3_pRangeCal extends SubFragment {
    private Callbacks mCallbacks;

    private static List4_Content3_pRangeCal fragment = null;

    private EditText mETOneKeyCalX;
    private EditText mETOneKeyCalY;
    private EditText mETOneKeyCalB;
    private Button mBtnOneKeyCalR1RSA;
    private Button mBtnOneKeyCalR2RSA;
    private Button mBtnOneKeyCalR3RSA;
    private Button mBtnOneKeyCalXRSA;


    public static List4_Content3_pRangeCal newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new List4_Content3_pRangeCal();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list4_content3_prange_cal_set;
    }

    @Override
    protected int getListResId() {
        return 0;
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
    protected void DoThings() {

        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturnFlow);
        mBtnReturn.setOnClickListener(new btnClick());

        mBtnOneKeyCalR1RSA = v.findViewById(R.id.btnOneKeyCalR1RSA);
        mBtnOneKeyCalR2RSA = v.findViewById(R.id.btnOneKeyCalR2RSA);
        mBtnOneKeyCalR3RSA = v.findViewById(R.id.btnOneKeyCalR3RSA);
        mBtnOneKeyCalXRSA = v.findViewById(R.id.btnOneKeyCalXRSA);

        mETOneKeyCalX = v.findViewById(R.id.eTOneKeyCalX);
        mETOneKeyCalY = v.findViewById(R.id.eTOneKeyCalY);
        mETOneKeyCalB = v.findViewById(R.id.eTOneKeyCalB);
        Button mBtnOneKeyCal = v.findViewById(R.id.BtnOneKeyCal);
        Button mBtnCalRange2KB = v.findViewById(R.id.BtnCalRange2KB);
        Button mBtnCalRange3KB = v.findViewById(R.id.BtnCalRange3KB);

        LinearLayout mLayoutAutoCalRangeKB = v.findViewById(R.id.layoutAutoCalRangeKB);
        LinearLayout mLayout_OneKeyCal = v.findViewById(R.id.layout_OneKeyCal);

        mBtnOneKeyCal.setOnClickListener(new btnClick());

        mETOneKeyCalX.setOnFocusChangeListener(new EdFocusChange());
        mETOneKeyCalX.setOnEditorActionListener(new EtFocusChange());
        mETOneKeyCalY.setOnFocusChangeListener(new EdFocusChange());
        mETOneKeyCalY.setOnEditorActionListener(new EtFocusChange());
        mETOneKeyCalB.setOnFocusChangeListener(new EdFocusChange());
        mETOneKeyCalB.setOnEditorActionListener(new EtFocusChange());

        mBtnOneKeyCalR1RSA.setOnClickListener(new btnClick());
        mBtnOneKeyCalR2RSA.setOnClickListener(new btnClick());
        mBtnOneKeyCalR3RSA.setOnClickListener(new btnClick());
        mBtnOneKeyCalXRSA.setOnClickListener(new btnClick());

        mBtnCalRange2KB.setOnClickListener(new btnClick());
        mBtnCalRange3KB.setOnClickListener(new btnClick());

        LinearLayout mLayout_oneKeyR3 = v.findViewById(R.id.layout_oneKeyR3);

        mLayout_OneKeyCal.setVisibility(View.VISIBLE);
        mLayoutAutoCalRangeKB.setVisibility(View.VISIBLE);
        mBtnCalRange3KB.setVisibility(View.VISIBLE);
        mLayout_oneKeyR3.setVisibility(View.VISIBLE);

        if (QueryMeasCateg(mCompName).equals("2") || QueryMeasCateg(mCompName).equals("1") || QueryMeasCateg(mCompName).equals("9") || QueryMeasCateg(mCompName).equals("12")) {
            mLayout_OneKeyCal.setVisibility(View.VISIBLE);
        } else {
            mLayout_OneKeyCal.setVisibility(View.GONE);
        }
        mBtnOneKeyCalR1RSA.setText(getConfigData(mCompName, "OneKeyCalRange1RSA"));
        mBtnOneKeyCalR2RSA.setText(getConfigData(mCompName, "OneKeyCalRange2RSA"));
        mBtnOneKeyCalR3RSA.setText(getConfigData(mCompName, "OneKeyCalRange3RSA"));
        mBtnOneKeyCalXRSA.setText(getConfigData(mCompName, "OneKeyCalXRSA"));

        if (!getConfigData(mCompName, "OneKeyCalX").equals("")) {
            mETOneKeyCalX.setText(editDataFormat(getConfigData(mCompName, "OneKeyCalX"), Integer.parseInt(getConfigData(mCompName, "YXWS"))));
        } else {
            mETOneKeyCalX.setText("");
        }
        if (!getConfigData(mCompName, "OneKeyCalY").equals("")) {
            mETOneKeyCalY.setText(editDataFormat(getConfigData(mCompName, "OneKeyCalY"), Integer.parseInt(getConfigData(mCompName, "YXWS"))));
        } else {
            mETOneKeyCalY.setText("");
        }
        mETOneKeyCalB.setText(getConfigData(mCompName, "OneKeyCalB"));

        if (!getConfigData(mCompName, "RANGE").equals("2") || !(doFlowing.get(mCompName).equals(context.getResources().getString(R.string.waiting_for_instructions)))) {
            mBtnOneKeyCal.setVisibility(View.GONE);
        } else {
            mBtnOneKeyCal.setVisibility(View.VISIBLE);
        }
        if (mETOneKeyCalX.getText().toString().equals("")) {
            mETOneKeyCalX.setEnabled(true);
            mBtnOneKeyCalR1RSA.setEnabled(true);
            mBtnOneKeyCalR1RSA.setBackground(getResources().getDrawable(R.drawable.btn));
            mBtnOneKeyCalR2RSA.setEnabled(true);
            mBtnOneKeyCalR2RSA.setBackground(getResources().getDrawable(R.drawable.btn));
            mBtnOneKeyCalR3RSA.setEnabled(true);
            mBtnOneKeyCalR3RSA.setBackground(getResources().getDrawable(R.drawable.btn));
            mBtnOneKeyCalXRSA.setEnabled(true);
            mBtnOneKeyCalXRSA.setBackground(getResources().getDrawable(R.drawable.btn));
            mLayoutAutoCalRangeKB.setVisibility(View.VISIBLE);
        }
        // 根据当前设备有几个量程
        switch (GetPlatRangSum(mCompName)) {
            case 1:
                mLayoutAutoCalRangeKB.setVisibility(View.GONE);
                mLayout_OneKeyCal.setVisibility(View.GONE);
                break;
            case 2:
                mBtnCalRange3KB.setVisibility(View.GONE);
                mLayout_oneKeyR3.setVisibility(View.GONE);
                break;
            case 3:
                mBtnCalRange3KB.setVisibility(View.VISIBLE);
                break;
        }

    }


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btnReturnFlow:
                        mCallbacks.onListSelected(v);
                        break;
                    case R.id.btnOneKeyCalR1RSA:
                        int select = Integer.parseInt(getConfigData(mCompName, "OneKeyCalRange1RSA"));
                        select++;
                        if (select > 4) {
                            select = 1;
                        }
                        saveOperationLogDataModifyMsg(mCompName, "OneKeyCalRange1RSA", String.valueOf(select), "量程1一键校准算法-", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "OneKeyCalRange1RSA", String.valueOf(select));
                        mBtnOneKeyCalR1RSA.setText(String.valueOf(select));
                        break;
                    case R.id.btnOneKeyCalR2RSA:
                        int select2 = Integer.parseInt(getConfigData(mCompName, "OneKeyCalRange2RSA"));
                        select2++;
                        if (select2 > 4) {
                            select2 = 1;
                        }
                        saveOperationLogDataModifyMsg(mCompName, "OneKeyCalRange2RSA", String.valueOf(select2), "量程2一键校准算法-", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "OneKeyCalRange2RSA", String.valueOf(select2));
                        mBtnOneKeyCalR2RSA.setText(String.valueOf(select2));
                        break;
                    case R.id.btnOneKeyCalR3RSA:
                        int select3 = Integer.parseInt(getConfigData(mCompName, "OneKeyCalRange3RSA"));
                        select3++;
                        if (select3 > 4) {
                            select3 = 1;
                        }
                        saveOperationLogDataModifyMsg(mCompName, "OneKeyCalRange3RSA", String.valueOf(select3), "量程3一键校准算法-", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "OneKeyCalRange3RSA", String.valueOf(select3));
                        mBtnOneKeyCalR3RSA.setText(String.valueOf(select3));
                        break;
                    case R.id.BtnOneKeyCal:
                        if (getConfigData(mCompName, "OneKeyCalB").equals("")) {
                            Toast.makeText(context, "一键校准B值为空 !", Toast.LENGTH_SHORT).show();
                        } else {
                            if (doFlowing.get(mCompName).equals(context.getResources().getString(R.string.waiting_for_instructions))) {

                                setUseRange(mCompName, String.valueOf(2));
                                doControlJob(mCompName, getString(R.string.SDBY2CL));

                                mCallbacks.showUpContent();
                                saveOperationLogMsg(mCompName, "用戶界面,一键校准启动", ErrorLog.msgType.操作_信息);
                                updateConfigData(mCompName, "OneKeyCalStartFlag", "true");
                            } else {
                                Toast.makeText(context, "当前设备运行中!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case R.id.btnOneKeyCalXRSA:
                        int select4 = Integer.parseInt(getConfigData(mCompName, "OneKeyCalXRSA"));
                        select4++;
                        if (select4 > 2) {
                            select4 = 1;
                        }
                        saveOperationLogDataModifyMsg(mCompName, "OneKeyCalXRSA", String.valueOf(select4), "一键校准X算法-", ErrorLog.msgType.操作_信息);
                        updateConfigData(mCompName, "OneKeyCalXRSA", String.valueOf(select4));
                        mBtnOneKeyCalXRSA.setText(String.valueOf(select4));
                        break;
                    case R.id.BtnCalRange2KB:
                        showDialogMsg(fragment, "确定要生成量程2 K B F 吗?", "量程2");
                        break;
                    case R.id.BtnCalRange3KB:
                        showDialogMsg(fragment, "确定要生成量程3 K B F 吗?", "量程3");
                        break;
                }
            } catch (Exception e) {
                Log.e("btnClick", e.toString());
            }
        }
    }

    private class EdFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {

                    case R.id.eTOneKeyCalX:
                        try {
                            String strX = mETOneKeyCalX.getText().toString();

                            if (!editTextInputStatus2((EditText) v, 0, 10.0)) {
                                ((EditText) v).setText(getConfigData(mCompName, "OneKeyCalX"));
                                Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " [ " + 0 + " ~ " + 10 + " ] " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            }
                            saveOperationLogDataModifyMsg(mCompName, "OneKeyCalX", strX, "设置一键校准X", ErrorLog.msgType.操作_信息);
                            strX = editDataFormat(mETOneKeyCalX.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            updateConfigData(mCompName, "OneKeyCalX", strX);

                        } catch (Exception e) {

                        }
                        break;
                    case R.id.eTOneKeyCalY:
                        try {
                            String strY = mETOneKeyCalY.getText().toString();

                            if (!editTextInputStatus4((EditText) v, 0.5, 2.0)) {
                                ((EditText) v).setText(getConfigData(mCompName, "OneKeyCalY"));
                                Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " [ " + 0.5 + " ~ " + 2 + " ] " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            }
                            saveOperationLogDataModifyMsg(mCompName, "OneKeyCalY", strY, "设置一键校准Y", ErrorLog.msgType.操作_信息);
                            strY = editDataFormat(mETOneKeyCalY.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            updateConfigData(mCompName, "OneKeyCalY", strY);

                        } catch (Exception e) {

                        }
                        break;
                    case R.id.eTOneKeyCalB:
                        try {
                            String strB = mETOneKeyCalB.getText().toString();
                            if (!strB.equals("") && (Float.parseFloat(strB) > 0 && Float.parseFloat(strB) < 1000)) {
                                saveOperationLogDataModifyMsg(mCompName, "OneKeyCalB", strB, "设置一键校准B", ErrorLog.msgType.操作_信息);

                                strB = editDataFormat(mETOneKeyCalB.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                                updateConfigData(mCompName, "OneKeyCalB", strB);
                                mETOneKeyCalB.setText(strB);
                            } else {
                                Toast.makeText(context, "请检查参数设置范围 (0~1000)!", Toast.LENGTH_SHORT).show();
                                mETOneKeyCalB.setText(getConfigData(mCompName, "OneKeyCalB"));
                            }
                        } catch (Exception e) {

                        }
                        break;
                }
                syncList();
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

    /*一键校准算法计算**/
    private void getCalRangeRsa(String compName, double valK, double valB, double valF, String oneKeyCalRange2RSA, String s) {
        switch (getConfigData(compName, oneKeyCalRange2RSA)) {
            case "1":
                addRangeKB(compName, s, valK, valB, valF);
                updateConfigData(compName, "Range" + s + "K", String.valueOf(valK));
                updateConfigData(compName, "Range" + s + "B", String.valueOf(valB));
                updateConfigData(compName, "Range" + s + "F", String.valueOf(valF));
                break;
            case "2":
                double k = Float.parseFloat(getConfigData(compName, "OneKeyCalX")) * valK;
                double b = Float.parseFloat(getConfigData(compName, "OneKeyCalX")) * valB;

                addRangeKB(compName, s, k, b, valF);
                updateConfigData(compName, "Range" + s + "K", String.valueOf(k));
                updateConfigData(compName, "Range" + s + "B", String.valueOf(b));
                updateConfigData(compName, "Range" + s + "F", String.valueOf(valF));
                break;
            case "3":
                double k0 = 2 * Float.parseFloat(getConfigData(compName, "OneKeyCalX")) * valK;
                double b0 = 2 * Float.parseFloat(getConfigData(compName, "OneKeyCalX")) * valB;
                addRangeKB(compName, s, k0, b0, valF);
                updateConfigData(compName, "Range" + s + "K", String.valueOf(k0));
                updateConfigData(compName, "Range" + s + "B", String.valueOf(b0));
                updateConfigData(compName, "Range" + s + "F", String.valueOf(valF));
                break;
            case "4":
                double x = Double.parseDouble(getConfigData(compName, "OneKeyCalX"));
                double y = Double.parseDouble(getConfigData(compName, "OneKeyCalY"));
                double k1 = (1 + x) * x * y * valK;
                double b1 = (1 + x) * x * y * valB;
                addRangeKB(compName, s, k1, b1, valF);
                updateConfigData(compName, "Range" + s + "K", String.valueOf(k1));
                updateConfigData(compName, "Range" + s + "B", String.valueOf(b1));
                updateConfigData(compName, "Range" + s + "F", String.valueOf(valF));
                break;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command");
            if (stringExtra.contains("量程2")) {

                saveOperationLogMsg(mCompName, "自动生成量程2KBF", ErrorLog.msgType.操作_信息);

                double valK = getNewestKBF(mCompName, "1", "K");
                double valB = getNewestKBF(mCompName, "1", "B");

                getCalRangeRsa(mCompName, valK, valB, 1, "OneKeyCalRange2RSA", "2");
                {
                    // 校准成功清外部KB
                    extKBParReset(mCompName, "2");
                }
                showOKDialogMsg("已生成量程2 K B F...");
            } else if (stringExtra.contains("量程3")) {
                saveOperationLogMsg(mCompName, "自动生成量程3KBF", ErrorLog.msgType.操作_信息);

                double valK = getNewestKBF(mCompName, "1", "K");
                double valB = getNewestKBF(mCompName, "1", "B");

                getCalRangeRsa(mCompName, valK, valB, 1, "OneKeyCalRange3RSA", "3");
                {
                    // 校准成功清外部KB
                    extKBParReset(mCompName, "3");
                }
                showOKDialogMsg("已生成量程3 K B F...");
            } else {
                mCallbacks.onDialogRS();
            }
        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }

}
