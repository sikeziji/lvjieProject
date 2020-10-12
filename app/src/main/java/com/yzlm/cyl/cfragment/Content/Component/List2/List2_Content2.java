package com.yzlm.cyl.cfragment.Content.Component.List2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.Calendar;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.操作_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setComponentEditFloatData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.clearAutoDoSample_topHour;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getTimer_cycle;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.syncAutoDoSample_topHour;
import static com.yzlm.cyl.cfragment.list.Component.Left_list2.showLeftWidget;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
/*
 * Created by caoyiliang on 2016/10/28.
 */

public class List2_Content2 extends SubFragment {

    private ToggleButton mToggleButtonZDJZ;
    private ToggleButton mToggleButtonZDQX;
    private ToggleButton mToggleButtonZDCX;
    public static ToggleButton mToggleButtonZNXJ;

    private LinearLayout mLinearLayoutLXCL;
    private LinearLayout mLinearLayoutZQCL;
    private LinearLayout mLinearLayoutZDJZ;
    private LinearLayout mLinearLayoutZDQX;
    private LinearLayout mLinearLayoutZDCX;
    private LinearLayout mLinearLayoutZNXJ;

    private LinearLayout mLayout_spanSet;
    private LinearLayout mLayout_meaMode;
    private EditText mEtlxclh;
    private EditText mEtlxclm;
    private EditText mEtzdjzt;
    private EditText mEtzdjzh;
    private EditText mEtzdqxh;
    private EditText mEtzqclh;
    private EditText mEtAutoWashLimit;

    private EditText mEtby1;
    private EditText mEtby2;
    private Spinner mSpbynd;
    private Spinner mSpMeaMode;
    private Spinner mSpRunningMode;
    private Spinner mSpJZRange;

    private Callbacks mCallbacks;
    private static List2_Content2 fragment = null;

    public static List2_Content2 newInstance() {
        if (fragment == null) {
            fragment = new List2_Content2();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogSelected(View view, Fragment Fm);

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
        return R.layout.list2_content2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    /*自动做样设置**/
    public static void ZDZYStatusSetting(String compName, boolean open) {
        saveOperationLogDataModifyMsg(compName, "isAuto", (open ? "true" : "false"), "自动做样开关", 操作_信息);
        updateConfigData(compName, "isAuto", open ? "true" : "false");
        if (!open) {
            updateConfigData(compName, "zdzyTimer", "0");
        }
    }

    private void ParProtection() {
        if (getPublicConfigData("LogInName").equals("0") || getPublicConfigData("LogInName").equals("1")) {
            mToggleButtonZDJZ.setEnabled(false);
            mToggleButtonZDQX.setEnabled(false);
            mToggleButtonZDCX.setEnabled(false);
            mToggleButtonZNXJ.setEnabled(false);
            mEtlxclh.setEnabled(false);
            mEtlxclm.setEnabled(false);
            mEtzdjzt.setEnabled(false);
            mEtzdjzh.setEnabled(false);
            mEtzdqxh.setEnabled(false);
            mEtAutoWashLimit.setEnabled(false);
            mEtzqclh.setEnabled(false);
            mEtby1.setEnabled(false);
            mEtby2.setEnabled(false);
            mSpbynd.setEnabled(false);
            mSpRunningMode.setEnabled(false);
            mSpMeaMode.setEnabled(false);
            mLinearLayoutZNXJ.setEnabled(false);
            mSpJZRange.setEnabled(false);
        }
    }

    /**
     * 测量类型 需要处理的界面
     */
    private void getMeasCategoryWin() {

        // 离线
        if (getConfigData(mCompName, "runningMode").equals("2")) {
            mLayout_meaMode.setVisibility(View.GONE);
            mLinearLayoutZDJZ.setVisibility(View.GONE);
            mLinearLayoutZDQX.setVisibility(View.GONE);
            mLinearLayoutZDCX.setVisibility(View.GONE);
            mLinearLayoutZNXJ.setVisibility(View.GONE);
            mLayout_spanSet.setVisibility(View.GONE);
        } else {
            mLayout_meaMode.setVisibility(View.VISIBLE);
            mLinearLayoutZDJZ.setVisibility(View.VISIBLE);
            mLinearLayoutZDQX.setVisibility(View.VISIBLE);
            mLayout_spanSet.setVisibility(View.VISIBLE);
            if (QueryMeasCateg(mCompName).equals("3") || QueryMeasCateg(mCompName).equals("8")) {
                mLinearLayoutZDCX.setVisibility(View.VISIBLE);
            }
            /*if(getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")){
                mLinearLayoutZNXJ.setVisibility(View.VISIBLE);
            }*/
        }
        if (QueryMeasCateg(mCompName).equals("4")) {
            mLayout_spanSet.setVisibility(View.GONE);
        }
        if (!QueryMeasCateg(mCompName).equals("1")) {
            mLinearLayoutZNXJ.setVisibility(View.GONE);
        }
        if (QueryMeasCateg(mCompName).equals("13")) {
            mLinearLayoutZDJZ.setVisibility(View.INVISIBLE);
            mLayout_spanSet.setVisibility(View.INVISIBLE);
        }
    }

    private void getMeaModeArray() {
        String[] ArrayItems = getResources().getStringArray(R.array.meaMode);

        switch (getConfigData(mCompName, "runningMode")) {
            case "1"://在线
                String[] ArrayItems1 = new String[]{ArrayItems[0], ArrayItems[1], ArrayItems[2], ArrayItems[3]};
                InitSpinner(context, mSpMeaMode, ArrayItems1, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
                break;
            case "2"://离线
                InitSpinner(context, mSpMeaMode, getResources().getStringArray(R.array.meaMode), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
                break;
            case "3"://维护
                String[] ArrayItems2 = new String[]{ArrayItems[0], ArrayItems[1], ArrayItems[2], ArrayItems[4]};
                InitSpinner(context, mSpMeaMode, ArrayItems2, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
                break;
            default:
                InitSpinner(context, mSpMeaMode, getResources().getStringArray(R.array.meaMode), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
                break;
        }
        mSpMeaMode.setSelection(Integer.parseInt(getConfigData(mCompName, "meaMode")) - 1);
    }

    public static boolean setMeaModeSpinnerSelect(String compName, int mode) {
        switch (getConfigData(compName, "runningMode")) {
            case "1":
                // 在线不可设置手动
                if (mode == 5) {
                    return false;
                }
                break;
            case "2":
                return false;
            case "3":
                // 维护部不可设置 受控
                if (mode == 4) {
                    return false;
                }
                break;
        }
        return true;
    }

    public static int getMeaModeSpinnerSelect(String compName) {
        switch (getConfigData(compName, "runningMode")) {
            case "1":
                break;
            case "2":
                break;
            case "3":
                // 维护情况下，4 为手动测量，返回5
                if (getConfigData(compName, "meaMode").equals("4")) {
                    return 5;
                }
                break;
        }
        return Integer.parseInt(getConfigData(compName, "meaMode"));
    }


    private void setSpanSetTouch(Boolean enable) {
        mEtby1.setEnabled(enable);
        mEtby2.setEnabled(enable);
        mToggleButtonZNXJ.setEnabled(enable);
    }

    private void syncList() {
        closeInputMethod(v);
        Log.v("close input", "close input");
        FullWindows(mActivityWindow);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            String stringExtra = data.getStringExtra("select_time_select_job");
            String[] strMsg = stringExtra.split("&");
            mEtzqclh.setText(strMsg[0]);
            mCallbacks.onDialogRS();
            mEtzqclh.clearFocus();
            updateConfigData(mCompName, "zqclh", mEtzqclh.getText().toString());
            syncList();
            syncAutoDoSample_topHour();
            if (strMsg.length > 1) {
                updateConfigData(mCompName, "zqclFlow", strMsg[1]);
            }
        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
            mEtzqclh.clearFocus();
        }
    }

    /*周期测量设置**/
    public static void ZQCLStatusSetting(String compName, boolean open) {

        saveOperationLogDataModifyMsg(compName, "ZQCL", (open ? "true" : "false"), "周期测量开关", 操作_信息);
        updateConfigData(compName, "ZQCL", open ? "true" : "false");
        if (open) {
            syncAutoDoSample_topHour();
        } else {
            clearAutoDoSample_topHour(compName);
        }
    }

    /*连续测量设置**/
    public static void LXCLStatusSetting(String compName, boolean open, String hour, String min) {
        saveOperationLogDataModifyMsg(compName, "LXCL", (open ? "true" : "false"), "连续测量开关", 操作_信息);
        updateConfigData(compName, "LXCL", open ? "true" : "false");
        if (!open) {
            updateConfigData(compName, "zdzyTimer", "0");
        }
        setTimer(compName, open, hour, min);

    }

    /*自动校准设置**/
    public static void ZDJZStatusSetting(String compName, boolean open) {

        saveOperationLogDataModifyMsg(compName, "ZDJZ", (open ? "true" : "false"), "自动校准开关", 操作_信息);
        updateConfigData(compName, "ZDJZ", open ? "true" : "false");

        if (open) {
            String autoCalibDay = getConfigData(compName, "zdjzt");
            String autoCalibHour = getConfigData(compName, "zdjzh");

            getTimer_cycle(compName).setTime((int) (Float.parseFloat(autoCalibDay.equals("") ? "0" : autoCalibDay)), 1);
            getTimer_cycle(compName).setTime((int) (Float.parseFloat(autoCalibHour.equals("") ? "0" : autoCalibHour)), 2);
            getTimer_cycle(compName).setCalibDate(Calendar.getInstance());
            getTimer_cycle(compName).resume(1);

        } else {
            getTimer_cycle(compName).suspend(1);
        }
    }

    /*自动清洗设置**/
    public static void ZDQXStatusSetting(String compName, boolean open) {

        saveOperationLogDataModifyMsg(compName, "ZDQX", (open ? "true" : "false"), "自动清洗开关", 操作_信息);
        updateConfigData(compName, "ZDQX", open ? "true" : "false");

        if (open) {
            String autoWashCount = getConfigData(compName, "zdqxh");
            int time = Integer.parseInt(autoWashCount.equals("0") ? "0" : autoWashCount);
            if (time == 0)
                return;
            getTimer_cycle(compName).setTime((int) (Float.parseFloat(autoWashCount.equals("") ? "0" : autoWashCount)), 3);
            getTimer_cycle(compName).resume(2);

        } else {
            updateConfigData(compName, "zdqxTimes", "0");
            getTimer_cycle(compName).suspend(2);

        }
    }

    /*自动冲洗设置**/
    public static void ZDCXStatusSetting(String compName, boolean open) {

        saveOperationLogDataModifyMsg(compName, "ZDCX", (open ? "true" : "false"), "自动冲洗开关", 操作_信息);
        updateConfigData(compName, "ZDCX", open ? "true" : "false");
        if (!open) {
            if (getConfigData(compName, "autoWashDoOnceFlag").equals("true")) {
                updateConfigData(compName, "autoWashDoOnceFlag", "false");
            }
        }
    }

    /*
     *  获取运行模式下相关权限
     */
    public static boolean getModePermissions(String compName, String Name) {

        boolean blPermission = false;
        try {
            String strMode = getConfigData(compName, "runningMode");
            switch (Name) {
                /*以下协议有关*/
                case "历史数据上传":
                    blPermission = true;
                    break;
                case "反控":
                    // 在线
                    blPermission = strMode.equals("1");
                    break;
                /*以上协议有关*/
                case "运行":
                case "组合测试":
                case "功能测试":
                case "测量参数":
                case "量程选择":
                case "其他设置":
                case "时间管理":
                case "余量管理":
                case "用户管理0505":
                case "用户管理302":
                case "用户管理123":
                case "校正/修正因子":
                    blPermission = strMode.equals("3");
                    break;
                case "用户管理234":
                    blPermission = strMode.equals("2");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blPermission;
    }

    /*测量模式设置**/
    public static void MeaModeSetting(String mCompName, String strMode) {

        switch (strMode) {
            case "1":// 连续模式
                ZDZYStatusSetting(mCompName, true);
                LXCLStatusSetting(mCompName, true, "0", "1");
                ZQCLStatusSetting(mCompName, false);
                break;
            case "2"://周期
                ZDZYStatusSetting(mCompName, true);
                LXCLStatusSetting(mCompName, true, getConfigData(mCompName, "lxclh"), getConfigData(mCompName, "lxclm"));
                ZQCLStatusSetting(mCompName, false);
                break;
            case "3"://定点
                ZDZYStatusSetting(mCompName, true);
                LXCLStatusSetting(mCompName, false, getConfigData(mCompName, "lxclh"), getConfigData(mCompName, "lxclm"));
                ZQCLStatusSetting(mCompName, true);
                break;
            case "4"://受控
            case "5"://手动测量
                ZDZYStatusSetting(mCompName, false);
                LXCLStatusSetting(mCompName, false, getConfigData(mCompName, "lxclh"), getConfigData(mCompName, "lxclm"));
                ZQCLStatusSetting(mCompName, false);
                break;
        }

    }

    private void meaModeWidgetShow(String strMode) {
        mEtlxclh.setText(getConfigData(mCompName, "lxclh"));
        mEtlxclm.setText(getConfigData(mCompName, "lxclm"));
        mEtlxclh.setEnabled(true);
        mEtlxclm.setEnabled(true);
        switch (strMode) {
            case "1":// 连续模式
                mLinearLayoutLXCL.setVisibility(View.VISIBLE);
                mEtlxclh.setText("0");
                mEtlxclm.setText("1");
                mEtlxclh.setEnabled(false);
                mEtlxclm.setEnabled(false);
                mLinearLayoutZQCL.setVisibility(View.GONE);
                break;
            case "2"://周期
                mLinearLayoutLXCL.setVisibility(View.VISIBLE);
                mLinearLayoutZQCL.setVisibility(View.GONE);
                break;

            case "3"://定点
                mLinearLayoutLXCL.setVisibility(View.GONE);
                mLinearLayoutZQCL.setVisibility(View.VISIBLE);
                break;
            case "4"://受控
            case "5"://手动测量
                mLinearLayoutLXCL.setVisibility(View.GONE);
                mLinearLayoutZQCL.setVisibility(View.GONE);
                break;
            default:
                mLinearLayoutLXCL.setVisibility(View.GONE);
                mLinearLayoutZQCL.setVisibility(View.GONE);
                break;
        }
    }

    /*自动校准流程的开启*/
    private void setCalibTimer() {

        if (mEtzdjzt != null && mEtzdjzh != null) {
            if (!mEtzdjzt.getText().toString().equals("") && !mEtzdjzh.getText().toString().equals("")) {
                if (mToggleButtonZDJZ.isChecked()) {
                    ZDJZStatusSetting(mCompName, true);
                } else {
                    ZDJZStatusSetting(mCompName, false);
                }
                syncList();
            }
        }
    }

    /*自动清洗流程的开启*/
    private void setWashTimer() {
        if (mEtzdqxh != null) {
            if (!mEtzdqxh.getText().toString().equals("")) {
                if (mToggleButtonZDQX.isChecked()) {
                    ZDQXStatusSetting(mCompName, true);
                } else {
                    ZDQXStatusSetting(mCompName, false);
                }
                syncList();
            }
        }
    }

    /*连续测量流程的开启*/
    private static void setTimer(String compName, boolean open, String hour, String min) {
        //都为空则为刚启用连续测量，实则不启用定时器

        //有一个不为空即可启动定时器
        if ((!hour.equals("") && !min.equals(""))) {
            if (open) {
                int time = (int) ((Float.parseFloat((hour.equals("") ? "0" : hour)) * 60 + Float.parseFloat((min.equals("") ? "0" : min))));
                if (time == 0)
                    return;
                getTimer_cycle(compName).setTime(time, 0);
                getTimer_cycle(compName).resume(0);

            } else {
                getTimer_cycle(compName).suspend(0);
            }
        }

    }

    @Override
    protected void DoThings() {
        try {
            byte[] arrayOfByte1 = DataUtil.shortToByte((short) 9222);
            DataUtil.reverse(arrayOfByte1);
            SendManager.SendCmd(mCompName + "_智能消解开关" + "_3_" + DataUtil.bytesToHexString(arrayOfByte1, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            mLayout_meaMode = v.findViewById(R.id.layout_meaMode);
            mLinearLayoutLXCL = v.findViewById(R.id.LLLXCL);
            mLinearLayoutZQCL = v.findViewById(R.id.LLZQCL);
            mLayout_spanSet = v.findViewById(R.id.layout_spanSet);

            mLinearLayoutZDJZ = v.findViewById(R.id.layout_calSetting);
            mLinearLayoutZDQX = v.findViewById(R.id.layout_autoClearSetting);
            mLinearLayoutZDCX = v.findViewById(R.id.layout_autoWashSetting);
            mLinearLayoutZNXJ = v.findViewById(R.id.layout_autoXJSetting);

            mEtzqclh = v.findViewById(R.id.eTzqclh);
            mEtzqclh.setText(getConfigData(mCompName, "zqclh"));

            mEtzqclh.setOnFocusChangeListener(new METzqclhEditor());
            mEtzqclh.setOnEditorActionListener(new METzqclhEditor());
            mEtzqclh.setInputType(InputType.TYPE_NULL);
            mToggleButtonZDJZ = v.findViewById(R.id.tBtnZDJZ);
            mToggleButtonZDJZ.setOnCheckedChangeListener(new ZDJZ());
            mToggleButtonZDJZ.setChecked(getConfigData(mCompName, "ZDJZ").equals("true"));

            mToggleButtonZDQX = v.findViewById(R.id.tBtnZDQX);
            mToggleButtonZDQX.setOnCheckedChangeListener(new ZDQX());
            mToggleButtonZDQX.setChecked(getConfigData(mCompName, "ZDQX").equals("true"));

            mToggleButtonZDCX = v.findViewById(R.id.tBtnZDCX);
            mToggleButtonZDCX.setOnCheckedChangeListener(new ZDCX());
            mToggleButtonZDCX.setChecked(getConfigData(mCompName, "ZDCX").equals("true"));

            mToggleButtonZNXJ = v.findViewById(R.id.tBtnZNXJ);
            mToggleButtonZNXJ.setOnCheckedChangeListener(new ZNXJ());
            mToggleButtonZNXJ.setChecked(getConfigData(mCompName, "ZNXJ").equals("true"));

            mEtlxclh = v.findViewById(R.id.eTlxclh);
            mEtlxclh.setText(getConfigData(mCompName, "lxclh"));
            mEtlxclh.setOnFocusChangeListener(new lxclhFocusChange());
            mEtlxclh.setOnEditorActionListener(new lxclhFocusChange());

            mEtlxclm = v.findViewById(R.id.eTlxclm);
            mEtlxclm.setText(getConfigData(mCompName, "lxclm"));
            mEtlxclm.setOnFocusChangeListener(new lxclmFocusChange());
            mEtlxclm.setOnEditorActionListener(new lxclmFocusChange());

            mEtzdjzt = v.findViewById(R.id.eTzdjzt);
            mEtzdjzt.setText(getConfigData(mCompName, "zdjzt"));
            mEtzdjzt.setOnFocusChangeListener(new zdjztFocusChange());
            mEtzdjzt.setOnEditorActionListener(new zdjztFocusChange());

            mEtzdjzh = v.findViewById(R.id.eTzdjzh);
            mEtzdjzh.setText(getConfigData(mCompName, "zdjzh"));
            mEtzdjzh.setOnFocusChangeListener(new zdjzhFocusChange());
            mEtzdjzh.setOnEditorActionListener(new zdjzhFocusChange());

            mEtzdqxh = v.findViewById(R.id.eTzdqxh);
            mEtzdqxh.setText(getConfigData(mCompName, "zdqxh"));
            mEtzdqxh.setOnFocusChangeListener(new zdqxhFocusChange());
            mEtzdqxh.setOnEditorActionListener(new zdqxhFocusChange());

            mEtAutoWashLimit = v.findViewById(R.id.eTAutoWashLimitValue);
            mEtAutoWashLimit.setText(getConfigData(mCompName, "autoWashLimitValue"));
            mEtAutoWashLimit.setOnFocusChangeListener(new etAutoWashFocusChange());
            mEtAutoWashLimit.setOnEditorActionListener(new etAutoWashFocusChange());

            mEtby1 = v.findViewById(R.id.eTby1);
            mEtby1.setOnFocusChangeListener(new mEtby1Change());
            mEtby1.setOnEditorActionListener(new mEtby1Change());
            mEtby2 = v.findViewById(R.id.eTby2);
            mEtby2.setOnFocusChangeListener(new mEtby2Change());
            mEtby2.setOnEditorActionListener(new mEtby2Change());

            mSpbynd = v.findViewById(R.id.spbynd);
            mSpRunningMode = v.findViewById(R.id.spRunningMode);
            mSpMeaMode = v.findViewById(R.id.spMeaMode);
            mSpJZRange = v.findViewById(R.id.spJZRange);

            int rangNum = GetPlatRangSum(mCompName);
            String[] rangeInfo = new String[rangNum];

            for (int i = 0; i < rangNum; i++) {
                rangeInfo[i] = getString(R.string.range) + (i + 1);
            }

            InitSpinner(context, mSpbynd, rangeInfo, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpbynd.setOnItemSelectedListener(new mSpbyndSelect());

            InitSpinner(context, mSpRunningMode, getResources().getStringArray(R.array.runningMode), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpRunningMode.setOnItemSelectedListener(new mSpSelect());
            mSpRunningMode.setSelection(Integer.parseInt(getConfigData(mCompName, "runningMode")) - 1);

            getMeaModeArray();
            int iMeaMode = Integer.parseInt(getConfigData(mCompName, "meaMode")) - 1;
            // 1.13.4版本当时模式5选1，后来1.15版本 4选1
            if (iMeaMode >= 4) {
                //只能选择1~4（4/5二选一）
                iMeaMode = 3;
                updateConfigData(mCompName, "meaMode", String.valueOf(iMeaMode));
                saveOperationLogDataModifyMsg(mCompName, "meaMode", String.valueOf(iMeaMode), "测量模式自动切换", 操作_信息);
                MeaModeSetting(mCompName, String.valueOf(iMeaMode));
            }
            mSpMeaMode.setOnItemSelectedListener(new mSpSelect());
            mSpMeaMode.setSelection(Integer.parseInt(getConfigData(mCompName, "meaMode")) - 1);
            TextView mTextby1nddw = v.findViewById(R.id.txby1nddw);
            TextView mTextby2nddw = v.findViewById(R.id.txby2nddw);

            InitSpinner(context, mSpJZRange, getResources().getStringArray(R.array.jzRange), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpJZRange.setOnItemSelectedListener(new mSpSelect());
            mSpJZRange.setSelection(Integer.parseInt(getConfigData(mCompName, "jzRange")) - 1);


            mTextby1nddw.setText("mg/L");
            mTextby2nddw.setText("mg/L");

            if (doFlowing.get(MainActivity.mCompName).equals(context.getResources().getString(R.string.waiting_for_instructions))) {
                setSpanSetTouch(true);
            } else {
                setSpanSetTouch(false);
            }
            meaModeWidgetShow(getConfigData(mCompName, "meaMode"));
            getMeasCategoryWin();

            ParProtection();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private class ZDJZ implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked && getConfigData(mCompName, "ZDJZ").equals("false")) {
                //updateConfigData(mCompName, "zdjzOnceFlag", "false");
            }
            saveOperationLogDataModifyMsg(mCompName, "ZDJZ", (mToggleButtonZDJZ.isChecked() ? "true" : "false"), "自动校准开关", 操作_信息);
            updateConfigData(mCompName, "ZDJZ", mToggleButtonZDJZ.isChecked() ? "true" : "false");
            LinearLayout mLinearLayout = v.findViewById(R.id.LLZDJZ);
            mLinearLayout.setVisibility(mToggleButtonZDJZ.isChecked() ? View.VISIBLE : View.INVISIBLE);
            setCalibTimer();
            syncList();
        }
    }

    private class ZDQX implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            saveOperationLogDataModifyMsg(mCompName, "ZDQX", (mToggleButtonZDQX.isChecked() ? "true" : "false"), "自动清洗开关", 操作_信息);
            updateConfigData(mCompName, "ZDQX", mToggleButtonZDQX.isChecked() ? "true" : "false");
            LinearLayout mLinearLayout = v.findViewById(R.id.LLZDQX);
            mLinearLayout.setVisibility(mToggleButtonZDQX.isChecked() ? View.VISIBLE : View.INVISIBLE);
            setWashTimer();
            syncList();

        }
    }

    private class lxclhFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    String strValue;
                    if (mEtlxclh.getText().toString().equals("") || Integer.parseInt(mEtlxclh.getText().toString()) >= 24 || Integer.parseInt(mEtlxclh.getText().toString()) <= 0) {
                        mEtlxclh.setText("0");
                        if (getConfigData(mCompName, "lxclm").equals("") ||
                                getConfigData(mCompName, "lxclm").equals("0")) {
                            mEtlxclm.setText("2");
                            saveOperationLogDataModifyMsg(mCompName, "lxclm", mEtlxclm.getText().toString(), "连续测量时", 操作_信息);
                            updateConfigData(mCompName, "lxclm", mEtlxclm.getText().toString());
                        }
                    }
                    strValue = editDataFormat(mEtlxclh.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "lxclh", strValue, "连续测量时", 操作_信息);
                    updateConfigData(mCompName, "lxclh", strValue);
                    setTimer(mCompName, true, getConfigData(mCompName, "lxclh"), getConfigData(mCompName, "lxclm"));
                    syncList();
                    mEtlxclh.setText(strValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtlxclh.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class lxclmFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    String strValue;
                    if (mEtlxclm.getText().toString().equals("") || Integer.parseInt(mEtlxclm.getText().toString()) > 59 || Integer.parseInt(mEtlxclm.getText().toString()) < 0) {
                        mEtlxclm.setText("1");
                    }
                    if (((Integer.parseInt(mEtlxclm.getText().toString()) == 0) || (Integer.parseInt(mEtlxclm.getText().toString()) == 1)) && (getConfigData(mCompName, "lxclh").equals("0"))) {
                        mEtlxclm.setText("2");
                    }
                    strValue = editDataFormat(mEtlxclm.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "lxclm", strValue, "连续测量分", 操作_信息);
                    updateConfigData(mCompName, "lxclm", strValue);
                    setTimer(mCompName, true, getConfigData(mCompName, "lxclh"), getConfigData(mCompName, "lxclm"));
                    syncList();
                    mEtlxclm.setText(strValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtlxclm.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class METzqclhEditor implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (!mEtzqclh.getText().toString().equals("")) {
                        saveOperationLogDataModifyMsg(mCompName, "zqclh", mEtzqclh.getText().toString(), "周期测量时间", 操作_信息);
                        updateConfigData(mCompName, "zqclh", mEtzqclh.getText().toString());

                    } else {
                        mEtzqclh.setText(getConfigData(mCompName, "zqclh"));
                        Toast.makeText(context, context.getString(R.string.cannot_be_set_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                syncList();
            } else {
                mCallbacks.onDialogSelected(v, List2_Content2.this);
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtzqclh.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class zdjztFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (!mEtzdjzt.getText().toString().equals("")) {
                        if (Integer.parseInt(mEtzdjzt.getText().toString()) > 31) {
                            mEtzdjzt.setText("0");
                        }
                        String strValue = editDataFormat(mEtzdjzt.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                        saveOperationLogDataModifyMsg(mCompName, "zdjzt", strValue, "自动校准天", 操作_信息);
                        updateConfigData(mCompName, "zdjzt", strValue);
                        mEtzdjzt.setText(strValue);
                    } else {
                        mEtzdjzt.setText("0");
                        saveOperationLogDataModifyMsg(mCompName, "zdjzt", "0", "自动校准天", 操作_信息);
                        updateConfigData(mCompName, "zdjzt", "0");
                    }
                    //updateConfigData(mCompName, "zdjzOnceFlag", "false");
                    setCalibTimer();
                    syncList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtzdjzt.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class zdjzhFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (!mEtzdjzh.getText().toString().equals("")) {
                        if (Integer.parseInt(mEtzdjzh.getText().toString()) > 23) {
                            mEtzdjzh.setText("0");
                        }
                    } else {
                        mEtzdjzh.setText("0");
                    }
                    String strValue = editDataFormat(mEtzdjzh.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    //updateConfigData(mCompName, "zdjzOnceFlag", "false");
                    saveOperationLogDataModifyMsg(mCompName, "zdjzh", strValue, "自动校准启动时间", 操作_信息);
                    updateConfigData(mCompName, "zdjzh", strValue);
                    setCalibTimer();
                    syncList();
                    mEtzdjzh.setText(strValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtzdjzh.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class mSpbyndSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mEtby1.setText(getConfigData(mCompName, "C" + (position * 2)));
            mEtby2.setText(getConfigData(mCompName, "C" + (position * 2 + 1)));
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class zdqxhFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (mEtzdqxh.getText().toString().equals("") || (Integer.parseInt(mEtzdqxh.getText().toString()) < 1) || (Integer.parseInt(mEtzdqxh.getText().toString()) > 1104)) {
                        mEtzdqxh.setText("1");
                    }
                    updateConfigData(mCompName, "zdqxTimes", "0");
                    String strValue = editDataFormat(mEtzdqxh.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    saveOperationLogDataModifyMsg(mCompName, "zdqxh", strValue, "自动清洗间隔", 操作_信息);
                    updateConfigData(mCompName, "zdqxh", strValue);
                    setWashTimer();
                    syncList();
                    mEtzdqxh.setText(strValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtzdqxh.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class mSpSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.spRunningMode:
                    if (!getConfigData(mCompName, "runningMode").equals(String.valueOf(position + 1))) {
                        saveOperationLogDataModifyMsg(mCompName, "runningMode", String.valueOf(position + 1), "运行模式", 操作_信息);
                        updateConfigData(mCompName, "runningMode", String.valueOf(position + 1));
                        switch (position + 1) {
                            case 1://在线
                                AddError(mCompName, 606, 运维_信息);
                                break;
                            case 2://离线
                                AddError(mCompName, 607, 运维_信息);
                                break;
                            case 3://维护
                                AddError(mCompName, 605, 运维_信息);
                                break;
                        }
                        syncList();
                        showLeftWidget();
                        mLayout_meaMode.setVisibility(View.VISIBLE);
                        // 离线
                        if (position == 1) {
                            ZDZYStatusSetting(mCompName, false);
                            LXCLStatusSetting(mCompName, false, getConfigData(mCompName, "lxclh"), getConfigData(mCompName, "lxclm"));
                            ZQCLStatusSetting(mCompName, false);
                            mLayout_meaMode.setVisibility(View.GONE);

                            // 关闭自动校准，自动清洗
                            ZDJZStatusSetting(mCompName, false);
                            ZDQXStatusSetting(mCompName, false);
                            ZDCXStatusSetting(mCompName, false);
                            mToggleButtonZDJZ.setChecked(getConfigData(mCompName, "ZDJZ").equals("true"));
                            mToggleButtonZDQX.setChecked(getConfigData(mCompName, "ZDQX").equals("true"));
                            mToggleButtonZDCX.setChecked(getConfigData(mCompName, "ZDCX").equals("true"));
                        } else {
                            MeaModeSetting(mCompName, getConfigData(mCompName, "meaMode"));
                        }
                        getMeasCategoryWin();
                        getMeaModeArray();
                    }
                    break;
                case R.id.spMeaMode:
                    if (!getConfigData(mCompName, "meaMode").equals(String.valueOf(position + 1))) {

                        saveOperationLogDataModifyMsg(mCompName, "meaMode", String.valueOf(position + 1), "测量模式", 操作_信息);
                        updateConfigData(mCompName, "meaMode", String.valueOf(position + 1));
                        MeaModeSetting(mCompName, String.valueOf(position + 1));
                        switch (position + 1) {
                            case 1://连续测量模式
                                AddError(mCompName, 609, 运维_信息);
                                break;
                            case 2://周期测量模式
                                AddError(mCompName, 610, 运维_信息);
                                break;
                            case 3://定点模式
                                AddError(mCompName, 608, 运维_信息);
                                break;
                            case 4://受控模式  、手动模式
                                if (getConfigData(mCompName, "runningMode").equals("1")) {
                                    //在线   受控模式
                                    AddError(mCompName, 612, 运维_信息);
                                } else {
                                    //维护   手动模式
                                    AddError(mCompName, 611, 运维_信息);
                                }
                                break;
                        }

                        meaModeWidgetShow(getConfigData(mCompName, "meaMode"));
                        syncList();
                    }
                    break;
                case R.id.spJZRange: {
                    saveOperationLogDataModifyMsg(mCompName, "jzRange", String.valueOf(position + 1), "自动校准量程", 操作_信息);
                    updateConfigData(mCompName, "jzRange", String.valueOf(position + 1));
                }
                break;
            }
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
                    String strBy1Value = editDataFormat(mEtby1.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    String strBy2Value = editDataFormat(mEtby2.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    if (strBy1Value.equals(strBy2Value) && (!strBy1Value.equals("0"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_to_same_coc), Toast.LENGTH_LONG).show();
                        mEtby1.setText(getConfigData(mCompName, "C" + mSpbynd.getSelectedItemPosition() * 2));
                        return;
                    }
                    if (Float.parseFloat(strBy1Value) < Float.parseFloat(getConfigData(mCompName, "YBLCL"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_up_rangeDown), Toast.LENGTH_LONG).show();
                        mEtby1.setText(getConfigData(mCompName, "C" + mSpbynd.getSelectedItemPosition() * 2));
                        return;
                    }
                    if (Float.parseFloat(strBy1Value) > Float.parseFloat(getConfigData(mCompName, "YBLCH"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_up_rangeHigh), Toast.LENGTH_LONG).show();
                        mEtby1.setText(getConfigData(mCompName, "C" + mSpbynd.getSelectedItemPosition() * 2));
                        return;
                    }
                    if (Float.parseFloat(strBy1Value) > Float.parseFloat(getConfigData(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample1_high_sample2), Toast.LENGTH_LONG).show();
                        mEtby1.setText(getConfigData(mCompName, "C" + mSpbynd.getSelectedItemPosition() * 2));
                        return;
                    }

                    saveOperationLogDataModifyMsg(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2), strBy1Value,
                            "量程" + (mSpbynd.getSelectedItemPosition() + 1) + "标样1浓度", 操作_信息);

                    switch (mSpbynd.getSelectedItemPosition() + 1) {
                        case 1://量程1标样1浓度
                            AddError(mCompName, 636, mEtby1.getText().toString(), 运维_信息);
                            break;
                        case 2://量程2标样1浓度
                            AddError(mCompName, 655, mEtby1.getText().toString(), 运维_信息);
                            break;
                        case 3://量程3标样1浓度
                            AddError(mCompName, 658, mEtby1.getText().toString(), 运维_信息);
                            break;
                    }

                    updateConfigData(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2), mEtby1.getText().toString());
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
                    String strBy1Value = editDataFormat(mEtby1.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    String strBy2Value = editDataFormat(mEtby2.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    if (strBy1Value.equals(strBy2Value) && (!strBy2Value.equals("0"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_to_same_coc), Toast.LENGTH_LONG).show();
                        mEtby2.setText(getConfigData(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)));
                        return;
                    }
                    if (Float.parseFloat(strBy2Value) > Float.parseFloat(getConfigData(mCompName, "YBLCH"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_up_rangeHigh), Toast.LENGTH_LONG).show();
                        mEtby2.setText(getConfigData(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)));
                        return;
                    }
                    if (Float.parseFloat(strBy2Value) < Float.parseFloat(getConfigData(mCompName, "YBLCL"))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample_up_rangeDown), Toast.LENGTH_LONG).show();
                        mEtby2.setText(getConfigData(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)));
                        return;
                    }
                    if (Float.parseFloat(strBy2Value) < Float.parseFloat(getConfigData(mCompName, "C" + mSpbynd.getSelectedItemPosition() * 2))) {
                        Toast.makeText(getContext(), getString(R.string.exception) + ":" + getString(R.string.set_standard_sample2_low_sample1), Toast.LENGTH_LONG).show();
                        mEtby2.setText(getConfigData(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1)));
                        return;
                    }

                    saveOperationLogDataModifyMsg(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1), strBy2Value,
                            "量程" + (mSpbynd.getSelectedItemPosition() + 1) + "标样2浓度", 操作_信息);

                    switch (mSpbynd.getSelectedItemPosition() + 1) {
                        case 1://量程1标样2浓度
                            AddError(mCompName, 637, mEtby2.getText().toString(), 运维_信息);
                            break;
                        case 2://量程2标样2浓度
                            AddError(mCompName, 656, mEtby2.getText().toString(), 运维_信息);
                            break;
                        case 3://量程3标样2浓度
                            AddError(mCompName, 659, mEtby2.getText().toString(), 运维_信息);
                            break;
                    }

                    updateConfigData(mCompName, "C" + (mSpbynd.getSelectedItemPosition() * 2 + 1), strBy2Value);
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

    private class ZDCX implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            saveOperationLogDataModifyMsg(mCompName, "ZDCX", (mToggleButtonZDCX.isChecked() ? "true" : "false"), "自动冲洗开关", 操作_信息);
            updateConfigData(mCompName, "ZDCX", mToggleButtonZDCX.isChecked() ? "true" : "false");
            LinearLayout mLinearLayout = v.findViewById(R.id.LLZDCX);
            mLinearLayout.setVisibility(mToggleButtonZDCX.isChecked() ? View.VISIBLE : View.INVISIBLE);
            syncList();
            ZDCXStatusSetting(mCompName, mToggleButtonZDCX.isChecked());
        }
    }

    private class ZNXJ implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            saveOperationLogDataModifyMsg(mCompName, "ZNXJ", (mToggleButtonZNXJ.isChecked() ? "true" : "false"), "智能消解开关", 操作_信息);
            updateConfigData(mCompName, "ZNXJ", mToggleButtonZNXJ.isChecked() ? "true" : "false");
            syncList();

            byte[] cznxjByte = copybyte(toByteArray(Integer.parseInt(isChecked ? "1" : "0"), 4));
            SendManager.SendCmd(mCompName + "_" + "设置智能消解开关_06_259", S0, 3, 100, cznxjByte);
        }
    }

    private class etAutoWashFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    setComponentEditFloatData(mCompName, (EditText) v, Integer.parseInt(getConfigData(mCompName, "YXWS")), 0, 21, 8, "设置自动冲洗超标值");
                    updateConfigData(mCompName, "autoWashLimitValue", ((EditText) v).getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
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
}
