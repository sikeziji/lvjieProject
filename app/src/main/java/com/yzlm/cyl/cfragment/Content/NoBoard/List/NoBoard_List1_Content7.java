package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalc;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalcTable;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBEnergy;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddNoBoardError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pfxyxx.showDialogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pfxyxx.showOKDialogMsg;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.GetNoBoardPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.setNoBoardComponentEditIntData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateDay;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateHour;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMin;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMonth;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateSec;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateYear;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.devStatusIsBusy;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getNoBoardCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;

/*
 * Created by zwj on 2019/07/25.
 */

public class NoBoard_List1_Content7 extends SubFragment {

    private Spinner mSpbynd;
    private Spinner mSpSelectCalRange;
    private EditText mEtby1;
    private EditText mEtby2;
    private TextView mTxtSpan1Unit, mTxtSpan2Unit;
    private Button mBtnTssCali;
    private LinearLayout mLayoutTssCaliSetting;
    private EditText mEtReferEn, mEtAbsorbEn;

    private int point = 2;
    private int range;
    private static NoBoard_List1_Content7 fragment = null;
    private Callbacks mCallbacks;

    public static NoBoard_List1_Content7 newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new NoBoard_List1_Content7();
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
        return R.layout.no_board_list1_content7;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        try {
            range = Integer.parseInt(getConfigData(strComponent.get(1)[0], "RANGE"));
            String strUnit = getNoBoardConfigData(mNoBoardCompName, "UNIT");
            point = Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS"));

            mSpbynd = v.findViewById(R.id.spbynd);
            int rangNum = GetNoBoardPlatRangSum(mNoBoardCompName);
            String[] rangeInfo = new String[rangNum];

            for (int i = 0; i < rangNum; i++) {
                rangeInfo[i] = getString(R.string.range) + (i + 1);
            }

            InitSpinner(context, mSpbynd, rangeInfo, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpbynd.setOnItemSelectedListener(new mSpbyndSelect());

            mSpSelectCalRange = v.findViewById(R.id.spSelectCalRange);
            InitSpinner(context, mSpSelectCalRange, rangeInfo, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);


            mEtby1 = v.findViewById(R.id.eTby1);
            mEtby1.setOnFocusChangeListener(new mEtby1Change());
            mEtby1.setOnEditorActionListener(new mEtby1Change());
            mEtby2 = v.findViewById(R.id.eTby2);
            mEtby2.setOnFocusChangeListener(new mEtby2Change());
            mEtby2.setOnEditorActionListener(new mEtby2Change());

            mTxtSpan1Unit = v.findViewById(R.id.txby1nddw);
            mTxtSpan2Unit = v.findViewById(R.id.txby2nddw);
            mLayoutTssCaliSetting = v.findViewById(R.id.layout_tssCaliSetting);
            mBtnTssCali = v.findViewById(R.id.btn_tssCali);
            mBtnTssCali.setOnClickListener(new btnOnClickLister());

            if (mNoBoardCompName.equals("TSS")) {
                mTxtSpan1Unit.setText("NTU");
                mTxtSpan2Unit.setText("NTU");
                mLayoutTssCaliSetting.setVisibility(View.VISIBLE);
            } else {
                mTxtSpan1Unit.setText("mg/L");
                mTxtSpan2Unit.setText("mg/L");
                mLayoutTssCaliSetting.setVisibility(View.GONE);
            }
            mEtReferEn = v.findViewById(R.id.eTSpan1Energy);
            mEtAbsorbEn = v.findViewById(R.id.eTSpan2Energy);
            mEtReferEn.setOnFocusChangeListener(new mEditTextOnClickLister());
            mEtReferEn.setOnEditorActionListener(new mEditTextOnClickLister());
            mEtAbsorbEn.setOnFocusChangeListener(new mEditTextOnClickLister());
            mEtAbsorbEn.setOnEditorActionListener(new mEditTextOnClickLister());

            if (doFlowing.get(strComponent.get(1)[0]).equals(context.getResources().getString(R.string.waiting_for_instructions))) {
                setSpanSetTouch(true);
                mEtReferEn.setEnabled(true);
                mEtAbsorbEn.setEnabled(true);
                mBtnTssCali.setEnabled(true);
            } else {
                setSpanSetTouch(false);
                mEtReferEn.setEnabled(false);
                mEtAbsorbEn.setEnabled(false);
                mBtnTssCali.setEnabled(false);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]" + e.toString());
        }
    }

    private void setSpanSetTouch(Boolean enable) {
        mEtby1.setEnabled(enable);
        mEtby2.setEnabled(enable);
    }


    private class mSpbyndSelect implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mEtby1.setText(getNoBoardConfigData(mNoBoardCompName, "C" + (position * 2)));
            mEtby2.setText(getNoBoardConfigData(mNoBoardCompName, "C" + (position * 2 + 1)));
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class mEtby1Change implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (mEtby1.getText().toString().equals("")) {
                        mEtby1.setText("0");
                    }
                    String strBy1Value = editDataFormat(mEtby1.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    String strBy2Value = editDataFormat(mEtby2.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    if (strBy1Value.equals(strBy2Value) && (!strBy1Value.equals("0"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_to_same_coc), Toast.LENGTH_LONG).show();
                        mEtby1.setText(getNoBoardConfigData(mNoBoardCompName, "C" + mSpbynd.getSelectedItemPosition() * 2));
                        return;
                    }
                    if (Float.parseFloat(strBy1Value) < Float.parseFloat(getNoBoardConfigData(mNoBoardCompName, "YBLCL"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_up_rangeDown), Toast.LENGTH_LONG).show();
                        mEtby1.setText(getNoBoardConfigData(mNoBoardCompName, "C" + mSpbynd.getSelectedItemPosition() * 2));
                        return;
                    }
                    if (Float.parseFloat(strBy1Value) > Float.parseFloat(getNoBoardConfigData(mNoBoardCompName, "YBLCH"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_up_rangeHigh), Toast.LENGTH_LONG).show();
                        mEtby1.setText(getNoBoardConfigData(mNoBoardCompName, "C" + mSpbynd.getSelectedItemPosition() * 2));
                        return;
                    }
                    if (Float.parseFloat(strBy1Value) > Float.parseFloat(getNoBoardConfigData(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample1_high_sample2), Toast.LENGTH_LONG).show();
                        mEtby1.setText(getNoBoardConfigData(mNoBoardCompName, "C" + mSpbynd.getSelectedItemPosition() * 2));
                        return;
                    }
                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2), strBy1Value,
                            "量程" + (mSpbynd.getSelectedItemPosition() + 1) + "标样1浓度", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2), mEtby1.getText().toString());
                    syncList();
                    mEtby1.setText(strBy1Value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtby1.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class mEtby2Change implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (mEtby2.getText().toString().equals("")) {
                        mEtby2.setText("0");
                    }
                    String strBy1Value = editDataFormat(mEtby1.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    String strBy2Value = editDataFormat(mEtby2.getText().toString(), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    if (strBy1Value.equals(strBy2Value) && (!strBy2Value.equals("0"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_to_same_coc), Toast.LENGTH_LONG).show();
                        mEtby2.setText(getNoBoardConfigData(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)));
                        return;
                    }
                    if (Float.parseFloat(strBy2Value) > Float.parseFloat(getNoBoardConfigData(mNoBoardCompName, "YBLCH"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_up_rangeHigh), Toast.LENGTH_LONG).show();
                        mEtby2.setText(getNoBoardConfigData(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)));
                        return;
                    }
                    if (Float.parseFloat(strBy2Value) < Float.parseFloat(getNoBoardConfigData(mNoBoardCompName, "YBLCL"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_up_rangeDown), Toast.LENGTH_LONG).show();
                        mEtby2.setText(getNoBoardConfigData(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)));
                        return;
                    }
                    if (Float.parseFloat(strBy2Value) < Float.parseFloat(getNoBoardConfigData(mNoBoardCompName, "C" + mSpbynd.getSelectedItemPosition() * 2))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample2_low_sample1), Toast.LENGTH_LONG).show();
                        mEtby2.setText(getNoBoardConfigData(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)));
                        return;
                    }

                    saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1), strBy2Value,
                            "量程" + (mSpbynd.getSelectedItemPosition() + 1) + "标样2浓度", ErrorLog.msgType.操作_信息);
                    updateNoBoardConfigData(mNoBoardCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1), strBy2Value);
                    syncList();
                    mEtby2.setText(strBy2Value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtby2.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class mEditTextOnClickLister implements View.OnFocusChangeListener, TextView.OnEditorActionListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTSpan1Energy:
                        setNoBoardComponentEditIntData(mNoBoardCompName, (EditText) v, 1, 0, 65000, 1, "TSS 输入 参比能量");
                        break;
                    case R.id.eTSpan2Energy:
                        setNoBoardComponentEditIntData(mNoBoardCompName, (EditText) v, 1, 0, 65000, 1, "TSS 输入 吸收能量");
                        break;
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

    private class btnOnClickLister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /* 关闭窗体悬浮（WL注释）*/
            if (devStatusIsBusy()) {
                Toast.makeText(context, context.getResources().getString(R.string.isBussy), Toast.LENGTH_LONG).show();
                return;
            }
            main.removeDestopText(mfb);
            showDialogMsg(fragment, getString(R.string.isCalibration), "tssCalibration");
        }
    }

    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command").split(";")[1];
            if (stringExtra.contains("tssCalibration")) {
                try {
                    //根据标样
                    int iCalicRange = (mSpSelectCalRange.getSelectedItemPosition() + 1);
                    //  TSS  KB  计算
                    NBEnergy nbEnergies = new NBEnergy(2);
                    for (int i = 0; i < 2; i++) {
                        switch (i) {
                            case 0:
                                if (!mEtReferEn.getText().toString().equals("")) {
                                    nbEnergies.energy[i] = Integer.parseInt(mEtReferEn.getText().toString());
                                } else {
                                    Toast.makeText(context, getString(R.string.please_sure_par), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                break;
                            case 1:
                                if (!mEtAbsorbEn.getText().toString().equals("")) {
                                    nbEnergies.energy[i] = Integer.parseInt(mEtAbsorbEn.getText().toString());
                                } else {
                                    Toast.makeText(context, getString(R.string.please_sure_par), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                break;
                        }
                    }
                    nbEnergies.setRange(String.valueOf(iCalicRange));
                    nbEnergies.setComponent(mNoBoardCompName);
                    nbEnergies.setYear((short) getSystemDateYear());
                    nbEnergies.setMonth((byte) getSystemDateMonth());
                    nbEnergies.setDay((byte) getSystemDateDay());
                    nbEnergies.setHour((byte) getSystemDateHour());
                    nbEnergies.setMinute((byte) getSystemDateMin());
                    nbEnergies.setSecond((byte) getSystemDateSec());

                    nbEnergies.setUnit("NTU");
                    nbEnergies.setFlow((byte) 6);
                    NBCalcTable ct = getNoBoardCalc(mNoBoardCompName);
                    NBCalc calc = ct.getCalc(String.valueOf(iCalicRange));
                    // 吸光度
                    double valA = Math.log10(((double) nbEnergies.energy[0]) / ((double) nbEnergies.energy[1]));

                    calc.setCalib2A(valA);
                    calc.setCalib1A(0);
                    AddNoBoardError(mNoBoardCompName, 527, ErrorLog.msgType.运维_信息);
                    AddNoBoardError(mNoBoardCompName, 528, ErrorLog.msgType.运维_信息);

                    if (!getNoBoardConfigData(mNoBoardCompName, "calibrationFlag").equals("true")) {//sun
                        updateNoBoardConfigData(mNoBoardCompName, "ycjzFlag", "true");
                    }
                    switch (iCalicRange) {
                        case 1:
                            if (nbEnergies.range1CalcKB(calc)) {
                                showOKDialogMsg(getString(R.string.CalibrationFinshMsg));
                            } else {
                                showOKDialogMsg(getString(R.string.CalibrationFail));
                            }
                            break;
                        case 2:
                            if (nbEnergies.range2CalcKB(calc)) {
                                showOKDialogMsg(getString(R.string.CalibrationFinshMsg));
                            } else {
                                showOKDialogMsg(getString(R.string.CalibrationFail));
                            }
                            break;
                        case 3:
                            if (nbEnergies.range3CalcKB(calc)) {
                                showOKDialogMsg(getString(R.string.CalibrationFinshMsg));
                            } else {
                                showOKDialogMsg(getString(R.string.CalibrationFail));
                            }
                            break;
                        default:
                            Toast.makeText(context, getString(R.string.please_sure_par), Toast.LENGTH_SHORT).show();
                            break;
                    }

                    saveNoBoardOperationLogMsg(mNoBoardCompName, "TSS_手动校准", ErrorLog.msgType.操作_信息);

                } catch (Exception e) {
                    mCallbacks.onDialogRS();
                }
            }
        } else {
            mCallbacks.onDialogRS();
        }
    }

}
