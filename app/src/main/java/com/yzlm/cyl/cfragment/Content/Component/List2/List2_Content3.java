package com.yzlm.cyl.cfragment.Content.Component.List2;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List2_Content3 extends SubFragment {

    private ToggleButton mToggleButtonLCQH;
    private ToggleButton mToggleButtonLCQHMea;
    private ToggleButton mToggleButtonLCQFY;
    private ToggleButton mToggleButtonLC1;
    private ToggleButton mToggleButtonLC2;
    private ToggleButton mToggleButtonLC3;

    private static List2_Content3 fragment = null;
    private static Boolean autoRangePreStatus = false;
    private static Boolean autoRangeMeaJobPreStatus = false;
    private static Boolean autoRangeMeaSwiRecStatus = false;
    private LinearLayout mLayoutAutoRangeMeaSwitch, mLayoutAutoRangeSwitchRecovery;

    public static List2_Content3 newInstance() {
        if (fragment == null) {
            fragment = new List2_Content3();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list2_content3;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void DoThings() {

        try {
            LinearLayout mLayoutAutoRange = v.findViewById(R.id.layoutAutoRange);
            mLayoutAutoRangeMeaSwitch = v.findViewById(R.id.layoutAutoRangeMeaSwitch);
            mLayoutAutoRangeSwitchRecovery = v.findViewById(R.id.layoutAutoRangeSwitchRecovery);
            LinearLayout mLayoutRang1 = v.findViewById(R.id.layoutRang1);
            LinearLayout mLayoutRang2 = v.findViewById(R.id.layoutRang2);
            LinearLayout mLayoutRang3 = v.findViewById(R.id.layoutRang3);
            switch (GetPlatRangSum(mCompName)) {
                case 3:
                    mLayoutRang3.setVisibility(View.VISIBLE);
                    mLayoutAutoRange.setVisibility(View.VISIBLE);
                case 2:
                    mLayoutRang2.setVisibility(View.VISIBLE);
                    mLayoutAutoRange.setVisibility(View.VISIBLE);
                case 1:
                    mLayoutRang1.setVisibility(View.VISIBLE);
                    break;
            }
            int point = Integer.parseInt(getConfigData(mCompName, "YXWS"));
            String strUnit = getConfigData(mCompName, "UNIT");
            TextView mEtlcl1 = v.findViewById(R.id.eTlcl1);
            mEtlcl1.setText(ConvertUnit("mg/L", strUnit, (getConfigData(mCompName, "LC1L")), point));
            TextView mEtlch1 = v.findViewById(R.id.eTlch1);
            mEtlch1.setText(ConvertUnit("mg/L", strUnit, (getConfigData(mCompName, "LC1H")), point));
            TextView mEtlcl2 = v.findViewById(R.id.eTlcl2);
            mEtlcl2.setText(ConvertUnit("mg/L", strUnit, (getConfigData(mCompName, "LC2L")), point));
            TextView mEtlch2 = v.findViewById(R.id.eTlch2);
            mEtlch2.setText(ConvertUnit("mg/L", strUnit, (getConfigData(mCompName, "LC2H")), point));
            TextView mEtlcl3 = v.findViewById(R.id.eTlcl3);
            mEtlcl3.setText(ConvertUnit("mg/L", strUnit, (getConfigData(mCompName, "LC3L")), point));
            TextView mEtlch3 = v.findViewById(R.id.eTlch3);
            mEtlch3.setText(ConvertUnit("mg/L", strUnit, (getConfigData(mCompName, "LC3H")), point));

            mToggleButtonLC1 = v.findViewById(R.id.btnLC1);
            mToggleButtonLC1.setOnClickListener(new LC1());
            mToggleButtonLC2 = v.findViewById(R.id.btnLC2);
            mToggleButtonLC2.setOnClickListener(new LC2());
            mToggleButtonLC3 = v.findViewById(R.id.btnLC3);
            mToggleButtonLC3.setOnClickListener(new LC3());
            mToggleButtonLCQH = v.findViewById(R.id.tBtnLCQH);
            autoRangePreStatus = getConfigData(mCompName, "AUTORANGE").equals("true");
            mToggleButtonLCQH.setOnCheckedChangeListener(new mToggleBtnClick());
            mToggleButtonLCQH.setChecked(getConfigData(mCompName, "AUTORANGE").equals("true"));

            autoRangeMeaJobPreStatus = getConfigData(mCompName, "AUTO_RANGE_JOB_SWITCH").equals("true");
            mToggleButtonLCQHMea = v.findViewById(R.id.tBtnQHCL);
            mToggleButtonLCQHMea.setOnCheckedChangeListener(new mToggleBtnAutoRangeMeaJOBClick());
            mToggleButtonLCQHMea.setChecked(getConfigData(mCompName, "AUTO_RANGE_JOB_SWITCH").equals("true"));

            autoRangeMeaSwiRecStatus = getConfigData(mCompName, "AUTO_RANGE_SWITCH_RECOVERY").equals("true");
            mToggleButtonLCQFY = v.findViewById(R.id.tBtnQHFY);
            mToggleButtonLCQFY.setOnCheckedChangeListener(new mToggleBtnAutoRangeSwiRecClick());
            mToggleButtonLCQFY.setChecked(autoRangeMeaSwiRecStatus);

            SetRange(Integer.parseInt(getConfigData(mCompName, "RANGE")));

            if (autoRangePreStatus) {
                mLayoutAutoRangeMeaSwitch.setVisibility(View.VISIBLE);
                mLayoutAutoRangeSwitchRecovery.setVisibility(View.VISIBLE);
            }
            if (doFlowing.get(mCompName).equals(getString(R.string.waiting_for_instructions))) {
                setRangeSelectTouch(true);
            } else {
                setRangeSelectTouch(false);
            }
            TextView mTextlc1dw = v.findViewById(R.id.textlc1dw);
            TextView mTextlc2dw = v.findViewById(R.id.textlc2dw);
            TextView mTextlc3dw = v.findViewById(R.id.textlc3dw);
            mTextlc1dw.setText(strUnit);
            mTextlc2dw.setText(strUnit);
            mTextlc3dw.setText(strUnit);
            TextView mTxtCalUseRange = v.findViewById(R.id.txtCalUseRange);
            mTxtCalUseRange.setVisibility(View.GONE);
            if (!doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                if (!getConfigData(mCompName, "PreCalSelectRange").equals("") && !getConfigData(mCompName, "PreCalSelectRange").equals(getConfigData(mCompName, "RANGE"))) {
                    mTxtCalUseRange.setText(getString(R.string.currently_in_range) + getConfigData(mCompName, "PreCalSelectRange") + "   " + doFlowing.get(mCompName));
                    mTxtCalUseRange.setVisibility(View.VISIBLE);
                }
            }

            ParProtection();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private void ParProtection() {
        if (getPublicConfigData("LogInName").equals("0")) {
            mToggleButtonLCQH.setEnabled(false);
            mToggleButtonLCQHMea.setEnabled(false);
            mToggleButtonLCQFY.setEnabled(false);
            mToggleButtonLC1.setEnabled(false);
            mToggleButtonLC2.setEnabled(false);
            mToggleButtonLC3.setEnabled(false);
        }
    }

    private void syncList() {
        closeInputMethod(v);
    }

    private class mToggleBtnClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (autoRangePreStatus != mToggleButtonLCQH.isChecked()) {
                saveOperationLogDataModifyMsg(mCompName, "AUTORANGE", mToggleButtonLCQH.isChecked() ? "true" : "false", "设置量程切换", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "AUTORANGE", mToggleButtonLCQH.isChecked() ? "true" : "false");

                if (mToggleButtonLCQH.isChecked()) {
                    SetRange(2);
                    //开启量程切换后的主量程
                    updateConfigData(mCompName, "AUTORANGE_MAIN_RANGE", String.valueOf(2));
                    mLayoutAutoRangeMeaSwitch.setVisibility(View.VISIBLE);
                    mLayoutAutoRangeSwitchRecovery.setVisibility(View.VISIBLE);
                } else {
                    mLayoutAutoRangeMeaSwitch.setVisibility(View.GONE);
                    mLayoutAutoRangeSwitchRecovery.setVisibility(View.GONE);
                }
                autoRangePreStatus = mToggleButtonLCQH.isChecked();
            }
            syncList();
        }
    }


    private void SetRange(int range) {
        mToggleButtonLC1.setChecked(range == 1);
        mToggleButtonLC2.setChecked(range == 2);
        mToggleButtonLC3.setChecked(range == 3);
        setUseRange(mCompName, String.valueOf(range));

        syncList();
    }

    /**
     * 开启/关闭量程切换
     */
    private class mToggleBtnAutoRangeMeaJOBClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (autoRangeMeaJobPreStatus != mToggleButtonLCQHMea.isChecked()) {
                saveOperationLogDataModifyMsg(mCompName, "AUTO_RANGE_JOB_SWITCH", mToggleButtonLCQHMea.isChecked() ? "true" : "false", "量程切换后是否启动测量", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "AUTO_RANGE_JOB_SWITCH", mToggleButtonLCQHMea.isChecked() ? "true" : "false");

                autoRangeMeaJobPreStatus = mToggleButtonLCQHMea.isChecked();
            }
            syncList();
        }
    }

    private class mToggleBtnAutoRangeSwiRecClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (autoRangeMeaSwiRecStatus != mToggleButtonLCQFY.isChecked()) {
                saveOperationLogDataModifyMsg(mCompName, "AUTO_RANGE_SWITCH_RECOVERY", mToggleButtonLCQFY.isChecked() ? "true" : "false", "量程切换后是否复原", ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "AUTO_RANGE_SWITCH_RECOVERY", mToggleButtonLCQFY.isChecked() ? "true" : "false");

                autoRangeMeaSwiRecStatus = mToggleButtonLCQFY.isChecked();

            }
            syncList();
        }
    }


    /*
     * 切换使用量程
     * **/
    public static void setUseRange(String compName, String strRange) {
        switch (strRange) {
            case "1":
            case "2":
            case "3":
                if (!getConfigData(compName, "RANGE").equals(strRange)) {
                    saveOperationLogDataModifyMsg(compName, "RANGE", strRange, "设置量程", ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "RANGE", strRange);
                    // 将校准后启动量程切换为当前量程
                    updateConfigData(compName, "CalSelectRange", strRange);
                }
                break;
        }
    }

    private class LC1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SetRange(1);
            //开启量程切换后的主量程
            updateConfigData(mCompName, "AUTORANGE_MAIN_RANGE", String.valueOf(1));
            AddError(mCompName, 529, ErrorLog.msgType.运维_信息);
        }
    }

    private class LC2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SetRange(2);
            //开启量程切换后的主量程
            updateConfigData(mCompName, "AUTORANGE_MAIN_RANGE", String.valueOf(2));
            AddError(mCompName, 529, ErrorLog.msgType.运维_信息);
        }
    }

    private class LC3 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SetRange(3);
            //开启量程切换后的主量程
            updateConfigData(mCompName, "AUTORANGE_MAIN_RANGE", String.valueOf(3));
            AddError(mCompName, 529, ErrorLog.msgType.运维_信息);
        }
    }

    private void setRangeSelectTouch(Boolean enable) {

        mToggleButtonLC1.setEnabled(enable);
        mToggleButtonLC2.setEnabled(enable);
        mToggleButtonLC3.setEnabled(enable);
        mToggleButtonLCQH.setEnabled(enable);
        mToggleButtonLCQHMea.setEnabled(enable);
        mToggleButtonLCQFY.setEnabled(enable);
    }
}
