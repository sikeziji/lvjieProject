package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yzlm.cyl.cfragment.Adapter.LVAdapter;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_MeasPointReset;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setComponentEditFloatData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setComponentEditNoZeroFloatData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.floatToBytes;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by WL on 2017/03/28.
 */

public class List4_Content3_pgpcldy extends SubFragment {
    private Callbacks mCallbacks;
    private static ListView LV_energy;
    private static TextView tvgpCalibCoef;
    private static String componetName = "";
    public static List4_Content3_pgpcldy fragment = null;

    private static EditText mEtSpecAddr;
    private static EditText mEtIntegTime;
    private static EditText mEtAvgCount;
    private static EditText mEtDarkCurTreat;
    private static EditText mEtLampVol;
    private static EditText mEtLampCount;
    private static EditText mEtDarkCurCompen;
    public static EditText mEtSpecCoefK;
    public static EditText mEtSpecCoefB;
    public static EditText mEtCompensateK;
    public static EditText mEtCompensateB;

    private static LineChart mChart;

    private static LinearLayout lay_leftrect1;
    private static LinearLayout lay_leftrect2;

    private Button mBtnSpectralCurve;

    static public String operationLog = "";//波长点的记录
    static public boolean operationFlag = false;//波长点的记录的标志位
    static public String tempLog = "";//波长点的记录

    /*测量单元选项（光谱）的能量值适配器数据源（WL）*/
    public static ArrayList<String[]> arrayListEnergy = new ArrayList<>();

    /*添加测量点后是否已经发送光谱条数命令的标志值（WL）*/
    public static boolean isSendCountCmd = false;

    public final static int specSum = 256;
    public static int[] specData = new int[specSum];
    private int iPoint = 2;

    private static boolean Admin = true;//是否可添加修改测量点

    public static boolean bChangeLV_energy = false;//是否修改过测量点

    public static List4_Content3_pgpcldy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pgpcldy();
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
        return R.layout.list4_content3_pgpcldy;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        try {
            lay_leftrect1 = (LinearLayout) this.v.findViewById(R.id.layout_leftrect1);
            lay_leftrect2 = (LinearLayout) this.v.findViewById(R.id.layout_leftrect2);
            lay_leftrect1.setVisibility(View.VISIBLE);
            lay_leftrect2.setVisibility(View.GONE);

            componetName = mCompName;
            Global.LockDisplayShowFlag = true;
            iPoint = Integer.parseInt(getConfigData(mCompName, "YXWS"));
            if (!getConfigData(mCompName, "gpcldSum").equals("")) {
                setPointSum(Integer.parseInt(getConfigData(mCompName, "gpcldSum")));
            }
            /*获取数据库存储的光谱测量点信息（WL）*/
            syncMeasPoint();
            ParProtection2();
            LV_energy = (ListView) this.v.findViewById(R.id.listview_energy);

            LV_energy.setAdapter(new LVAdapter(Admin, arrayListEnergy, this.getActivity()));
            LV_energy.setOnItemLongClickListener(new LVEnergyOnItemClick());

            Button btn_Save = (Button) this.v.findViewById(R.id.btn_gpSave);
            btn_Save.setOnClickListener(new btnSaveClick());

            Button mBtnTempStd = (Button) this.v.findViewById(R.id.btnTempStd);
            mBtnTempStd.setOnClickListener(new btnTempStdClick());

            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());

            tvgpCalibCoef = (TextView) v.findViewById(R.id.tvgpTempraKB);
            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 5130);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(componetName + "_查光谱测量波长数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            byte[] arrayOfByte = DataUtil.shortToByte((short) 0);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(componetName + "_查温度K" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 5150);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(componetName + "_查光谱波长1起始结束" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 40);

            byte[] arrayOfByte4 = DataUtil.shortToByte((short) 5190);
            DataUtil.reverse(arrayOfByte4);
            SendManager.SendCmd(componetName + "_查光谱波长打灯电压" + "_3_" + DataUtil.bytesToHexString(arrayOfByte4, 2).replace(" ", ""), Global.S0, 3, 200, 20);

            byte[] arrayOfByte5 = DataUtil.shortToByte((short) 5210);
            DataUtil.reverse(arrayOfByte5);
            SendManager.SendCmd(componetName + "_查光谱波长打灯次数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte5, 2).replace(" ", ""), Global.S0, 3, 200, 20);

            /* 光谱地址（获取光谱）*/
            mEtSpecAddr = (EditText) v.findViewById(R.id.etSpecAddr);
            mEtSpecAddr.setOnFocusChangeListener(new specAddrFocusChange());
            mEtSpecAddr.setOnEditorActionListener(new specAddrFocusChange());
            /*积分时间*/
            mEtIntegTime = (EditText) v.findViewById(R.id.etIntegTime);
            mEtIntegTime.setOnFocusChangeListener(new integTimeFocusChange());
            mEtIntegTime.setOnEditorActionListener(new integTimeFocusChange());
            /*平均次数*/
            mEtAvgCount = (EditText) v.findViewById(R.id.etAvgCount);
            mEtAvgCount.setOnFocusChangeListener(new avgCountFocusChange());
            mEtAvgCount.setOnEditorActionListener(new avgCountFocusChange());
            /*暗电流处理*/
            mEtDarkCurTreat = (EditText) v.findViewById(R.id.etDarkCurTreat);
            mEtDarkCurTreat.setOnFocusChangeListener(new darkCurTreatFocusChange());
            mEtDarkCurTreat.setOnEditorActionListener(new darkCurTreatFocusChange());
            /*打灯电压*/
            mEtLampVol = (EditText) v.findViewById(R.id.etLampVol);
            mEtLampVol.setOnFocusChangeListener(new lampVolFocusChange());
            mEtLampVol.setOnEditorActionListener(new lampVolFocusChange());
            /*打灯次数*/
            mEtLampCount = (EditText) v.findViewById(R.id.etLampCount);
            mEtLampCount.setOnFocusChangeListener(new lampCountFocusChange());
            mEtLampCount.setOnEditorActionListener(new lampCountFocusChange());
            /*暗电流补偿*/
            mEtDarkCurCompen = (EditText) v.findViewById(R.id.etDarkCurCompen);
            mEtDarkCurCompen.setOnFocusChangeListener(new darkCurCompenFocusChange());
            mEtDarkCurCompen.setOnEditorActionListener(new darkCurCompenFocusChange());
            /*光谱校正系数K*/
            mEtSpecCoefK = (EditText) v.findViewById(R.id.etSpecCoefK);
            mEtSpecCoefK.setOnFocusChangeListener(new specCoefKFocusChange());
            mEtSpecCoefK.setOnEditorActionListener(new specCoefKFocusChange());
            /*光谱校正系数B*/
            mEtSpecCoefB = (EditText) v.findViewById(R.id.etSpecCoefB);
            mEtSpecCoefB.setOnFocusChangeListener(new specCoefBFocusChange());
            mEtSpecCoefB.setOnEditorActionListener(new specCoefBFocusChange());

            Button mBtnGetSpec = (Button) v.findViewById(R.id.btn_getSpec);
            mBtnGetSpec.setOnClickListener(new getSpecDateClick());

            Button mBtnSpecMea = (Button) v.findViewById(R.id.btnSpecTest);
            mBtnSpecMea.setOnClickListener(new specMeaClick());

            mBtnSpectralCurve = (Button) v.findViewById(R.id.btnSpectralCurve);
            mBtnSpectralCurve.setOnClickListener(new spectralCurveClick());

            Button mBtnHiddenCurve = (Button) v.findViewById(R.id.btnHiddenCurve);
            mBtnHiddenCurve.setOnClickListener(new hiddenCurveClick());

            mEtCompensateK = (EditText) v.findViewById(R.id.etCompensateCoeK);
            mEtCompensateK.setOnFocusChangeListener(new EditFocusChange());
            mEtCompensateK.setOnEditorActionListener(new EditFocusChange());

            mEtCompensateB = (EditText) v.findViewById(R.id.etCompensateCoeB);
            mEtCompensateB.setOnFocusChangeListener(new EditFocusChange());
            mEtCompensateB.setOnEditorActionListener(new EditFocusChange());
            mEtCompensateK.setText(getConfigData(mCompName, "gpbck"));
            mEtCompensateB.setText(getConfigData(mCompName, "gpbcb"));

            LinearLayout mLayout_CompensateK = (LinearLayout) v.findViewById(R.id.layout_CompensateK);
            LinearLayout mLayout_CompensateB = (LinearLayout) v.findViewById(R.id.layout_CompensateB);
            if (mCompName.equals("TPb")) {
                mLayout_CompensateK.setVisibility(View.VISIBLE);
                mLayout_CompensateB.setVisibility(View.VISIBLE);
            } else {
                mLayout_CompensateK.setVisibility(View.GONE);
                mLayout_CompensateB.setVisibility(View.GONE);
            }

            ParProtection();


            InitChart();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    /**
     * 参数保护，
     */
    private void ParProtection() {
        LV_energy.setEnabled(true);
        if (getPublicConfigData("LogInName").equals("0") || getPublicConfigData("LogInName").equals("1")) {
            mEtAvgCount.setEnabled(false);
            mEtDarkCurTreat.setEnabled(false);
            mEtLampVol.setEnabled(false);
            mEtLampCount.setEnabled(false);
            mEtDarkCurCompen.setEnabled(false);
            mEtSpecCoefK.setEnabled(false);
            mEtSpecCoefB.setEnabled(false);
            mEtCompensateK.setEnabled(false);
            mEtCompensateB.setEnabled(false);
            LV_energy.setEnabled(false);
        }
    }

    private void ParProtection2() {
        Admin = true;
        if (getPublicConfigData("LogInName").equals("0") || getPublicConfigData("LogInName").equals("1")) {
            Admin = false;
        }
    }

    // "shrink,220 nm,4631,;shrink,275 nm,20338,;expand,null,null,;"
    /* 从数据库同步测量点信息*/
    private static void syncMeasPoint() {
        arrayListEnergy.clear();
        String measPointStr = getConfigData(mCompName, "gpcld");
        Log.i("pointstr", measPointStr);
        if (measPointStr.equals("")) {
            return;
        }
        String[] measPointGroup = measPointStr.split(";");
        for (String measPointSingle : measPointGroup) {
            String[] measPointVal = measPointSingle.split(",");
            arrayListEnergy.add(measPointVal);
        }
    }

    /*
     * 修改光谱波长值
     * pointNum :0 ~ 19
     * pointLow 起始值
     * pointHigh 结束值
     * */
    private static boolean measPointSetValue(int pointNum, int pointLow, int pointHigh) {
        String measPointStr = getConfigData(mCompName, "gpcld");
        if (measPointStr.equals("")) {
            return false;
        }
        if (arrayListEnergy.size() > pointNum) {
            ArrayList<String[]> arrayListEnergy = new ArrayList<>();
            String[] measPointGroup = measPointStr.split(";");
            for (String measPointSingle : measPointGroup) {
                String[] measPointVal = measPointSingle.split(",");
                arrayListEnergy.add(measPointVal);
            }

            String tempPointStr = arrayListEnergy.get(pointNum)[1];
            String temPointNewStr;
            if (pointLow == pointHigh) {
                temPointNewStr = pointLow + " nm";
            } else {
                temPointNewStr = pointLow + "-" + pointHigh + " nm";
            }
            if (temPointNewStr.equals(tempPointStr)) {
                return false;
            }

            arrayListEnergy.get(pointNum)[1] = temPointNewStr;
            measPointStr = "";
            for (String[] measPointSingle : arrayListEnergy) {
                for (String val : measPointSingle) {
                    measPointStr += val + ",";
                }
                measPointStr += ";";
            }

            saveOperationLogDataModifyMsg(componetName, "gpcld", measPointStr, "光谱测量点", ErrorLog.msgType.操作_信息);
            updateConfigData(componetName, "gpcld", measPointStr);
        }

        return true;
    }


    /*
     * 修改光谱波长打灯电压值
     * pointNum :0 ~ 19
     * pointVol :打灯电压
     * */
    private static boolean measPointVolSetValue(int pointNum, int pointVol) {
        String measPointVolStr = getConfigData(mCompName, "gpcld");
        if (measPointVolStr.equals("")) {
            return false;
        }
        if (arrayListEnergy.size() > pointNum) {
            ArrayList<String[]> arrayListEnergy = new ArrayList<>();
            String[] measPointGroup = measPointVolStr.split(";");
            for (String measPointSingle : measPointGroup) {
                String[] measPointVal = measPointSingle.split(",");
                arrayListEnergy.add(measPointVal);
            }

            String tempPointVolStr = arrayListEnergy.get(pointNum)[3];
            String temPointVolNewStr = String.valueOf(pointVol);

            if (temPointVolNewStr.equals(tempPointVolStr)) {
                return false;
            }

            arrayListEnergy.get(pointNum)[3] = temPointVolNewStr;
            measPointVolStr = "";
            for (String[] measPointSingle : arrayListEnergy) {
                for (String val : measPointSingle) {
                    measPointVolStr += val + ",";
                }
                measPointVolStr += ";";
            }

            saveOperationLogDataModifyMsg(componetName, "gpcld", measPointVolStr, "光谱测量点", ErrorLog.msgType.操作_信息);
            updateConfigData(componetName, "gpcld", measPointVolStr);
        }

        return true;
    }

    /*
     * 修改光谱波长打灯次数
     * pointNum :0 ~ 19
     * pointVol :打灯电压
     * */
    private static boolean measPointLampTimesSetValue(int pointNum, int pointLampTimes) {
        String measPointLampTimesStr = getConfigData(mCompName, "gpcld");
        if (measPointLampTimesStr.equals("")) {
            return false;
        }
        if (arrayListEnergy.size() > pointNum) {
            ArrayList<String[]> arrayListEnergy = new ArrayList<>();
            String[] measPointGroup = measPointLampTimesStr.split(";");
            for (String measPointSingle : measPointGroup) {
                String[] measPointVal = measPointSingle.split(",");
                arrayListEnergy.add(measPointVal);
            }

            String tempPointLampTimesStr = arrayListEnergy.get(pointNum)[4];
            String temPointLampTimesNewStr = String.valueOf(pointLampTimes);

            if (temPointLampTimesNewStr.equals(tempPointLampTimesStr)) {
                return false;
            }

            arrayListEnergy.get(pointNum)[4] = temPointLampTimesNewStr;
            measPointLampTimesStr = "";
            for (String[] measPointSingle : arrayListEnergy) {
                for (String val : measPointSingle) {
                    measPointLampTimesStr += val + ",";
                }
                measPointLampTimesStr += ";";
            }

            saveOperationLogDataModifyMsg(componetName, "gpcld", measPointLampTimesStr, "光谱测量点", ErrorLog.msgType.操作_信息);
            updateConfigData(componetName, "gpcld", measPointLampTimesStr);
        }

        return true;
    }


    /* 组装测量点信息至数据库*/
    public static void packMeasPoint() {
        String measPointStr = "";

        for (String[] measPointSingle : arrayListEnergy) {
            for (String val : measPointSingle) {
                measPointStr += val + ",";
            }
            measPointStr += ";";
        }
        saveOperationLogDataModifyMsg(componetName, "gpcld", measPointStr, "光谱测量点", ErrorLog.msgType.操作_信息);
        updateConfigData(componetName, "gpcld", measPointStr);

    }

    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class specAddrFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String specAddrStr = mEtSpecAddr.getText().toString().trim();
                if (!specAddrStr.equals("")) {
                    specAddrStr = editDataFormat(specAddrStr, Integer.parseInt(getConfigData(componetName, "YXWS")));
                    int specAddrInt = Integer.parseInt(specAddrStr);

                    saveOperationLogDataModifyMsg(componetName, "gpdz", specAddrStr, "设置光谱地址", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "gpdz", specAddrStr);

                    byte[] specAddrByte = copybyte(toByteArray(specAddrInt, 4));

                    SendManager.SendCmd(componetName + "_" + "设置光谱地址_06_8", S0, 3, 500, specAddrByte);

                    mEtSpecAddr.setText(specAddrStr);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.spec_address_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtSpecAddr.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class integTimeFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String integTimeStr = mEtIntegTime.getText().toString().trim();
                if (!integTimeStr.equals("")) {
                    integTimeStr = editDataFormat(integTimeStr, Integer.parseInt(getConfigData(componetName, "YXWS")));
                    int integTimeInt = Integer.parseInt(integTimeStr);
                    saveOperationLogDataModifyMsg(componetName, "jfsj", integTimeStr, "设置积分时间", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "jfsj", integTimeStr);

                    byte[] integTimeByte = copybyte(toByteArray(integTimeInt, 4));

                    SendManager.SendCmd(componetName + "_" + "设置积分时间_06_9", S0, 3, 500, integTimeByte);

                    mEtIntegTime.setText(integTimeStr);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.integral_time_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtIntegTime.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class avgCountFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String avgCountStr = mEtAvgCount.getText().toString().trim();
                if (!avgCountStr.equals("")) {
                    int avgCountInt = Integer.parseInt(avgCountStr);

                    if ((avgCountInt < 10 || avgCountInt > 200)) {
                        Toast.makeText(getActivity(), getString(R.string.set_measurement_pointsLampTimes_beyond_range), Toast.LENGTH_SHORT).show();
                        mEtAvgCount.setText(getConfigData(componetName, "pjcs"));
                        return;
                    }

                    avgCountStr = editDataFormat(avgCountStr, Integer.parseInt(getConfigData(componetName, "YXWS")));
                    saveOperationLogDataModifyMsg(componetName, "pjcs", avgCountStr, "设置平均次数", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "pjcs", avgCountStr);

                    byte[] avgCountByte = copybyte(toByteArray(avgCountInt, 4));

                    SendManager.SendCmd(componetName + "_" + "设置平均次数_06_10", S0, 3, 500, avgCountByte);

                    mEtAvgCount.setText(avgCountStr);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.average_time_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtAvgCount.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class darkCurTreatFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String darkCurTreatStr = mEtDarkCurTreat.getText().toString().trim();
                if (!darkCurTreatStr.equals("")) {
                    darkCurTreatStr = editDataFormat(darkCurTreatStr, Integer.parseInt(getConfigData(componetName, "YXWS")));
                    int darkCurTreatInt = Integer.parseInt(darkCurTreatStr);
                    if (darkCurTreatInt > 1) {
                        darkCurTreatInt = 1;
                    } else if (darkCurTreatInt < 0) {
                        darkCurTreatInt = 0;
                    }
                    saveOperationLogDataModifyMsg(componetName, "adlcl", String.valueOf(darkCurTreatInt), "设置暗电流处理", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "adlcl", String.valueOf(darkCurTreatInt));

                    byte[] darkCurTreatByte = copybyte(toByteArray(darkCurTreatInt, 4));

                    SendManager.SendCmd(componetName + "_" + "设置暗电流处理_06_11", S0, 3, 200, darkCurTreatByte);

                    mEtDarkCurTreat.setText(String.valueOf(darkCurTreatInt));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.dark_current_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtDarkCurTreat.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class lampVolFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String lampVolStr = mEtLampVol.getText().toString().trim();
                if (!lampVolStr.equals("")) {
                    int lampVolInt = Integer.parseInt(lampVolStr);
                    if ((lampVolInt < 20 || lampVolInt > 50)) {
                        mEtLampVol.setText(getConfigData(componetName, "dddy"));
                        Toast.makeText(getActivity(), getString(R.string.set_measurement_pointsVol_beyond_range), Toast.LENGTH_LONG).show();
                        return;
                    }

                    lampVolStr = editDataFormat(lampVolStr, Integer.parseInt(getConfigData(componetName, "YXWS")));

                    saveOperationLogDataModifyMsg(componetName, "dddy", lampVolStr, "设置打灯电压", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "dddy", lampVolStr);

                    byte[] lampVolByte = copybyte(toByteArray(lampVolInt, 4));

                    SendManager.SendCmd(componetName + "_" + "设置打灯电压_06_12", S0, 3, 200, lampVolByte);

                    mEtLampVol.setText(lampVolStr);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.lamp_voltage_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtLampVol.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class lampCountFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String lampCountStr = mEtLampCount.getText().toString().trim();
                if (!lampCountStr.equals("")) {
                    int lampCountInt = Integer.parseInt(lampCountStr);
                    if ((lampCountInt < 1 || lampCountInt > 50)) {
                        Toast.makeText(getActivity(), getString(R.string.set_measurement_pointsLampTimes_beyond_range), Toast.LENGTH_SHORT).show();
                        mEtLampCount.setText(getConfigData(componetName, "ddcs"));
                        return;
                    }

                    lampCountStr = editDataFormat(lampCountStr, Integer.parseInt(getConfigData(componetName, "YXWS")));
                    saveOperationLogDataModifyMsg(componetName, "ddcs", lampCountStr, "设置打灯次数", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "ddcs", lampCountStr);

                    byte[] lampCountByte = copybyte(toByteArray(lampCountInt, 4));

                    SendManager.SendCmd(componetName + "_" + "设置打灯次数_06_13", S0, 3, 200, lampCountByte);

                    mEtLampCount.setText(lampCountStr);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.lamp_times_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtLampCount.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class darkCurCompenFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String darkCurCompenStr = mEtDarkCurCompen.getText().toString().trim();
                if (!darkCurCompenStr.equals("")) {
                    darkCurCompenStr = editDataFormat(darkCurCompenStr, Integer.parseInt(getConfigData(componetName, "YXWS")));
                    int darkCurCompenInt = Integer.parseInt(darkCurCompenStr);
                    saveOperationLogDataModifyMsg(componetName, "adlbc", darkCurCompenStr, "设置暗电流补偿", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "adlbc", darkCurCompenStr);

                    byte[] darkCurCompenByte = copybyte(toByteArray(darkCurCompenInt, 4));

                    SendManager.SendCmd(componetName + "_" + "设置暗电流补偿_06_14", S0, 3, 200, darkCurCompenByte);

                    mEtDarkCurCompen.setText(darkCurCompenStr);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.dark_current_compensation_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtDarkCurCompen.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class specCoefKFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String specCoefKStr = mEtSpecCoefK.getText().toString().trim();
                if (!specCoefKStr.equals("")) {
                    specCoefKStr = editDataFormat(specCoefKStr, Integer.parseInt(getConfigData(componetName, "YXWS")));
                    float specCoefK = Float.parseFloat(specCoefKStr);
                    saveOperationLogDataModifyMsg(componetName, "gpxsk", specCoefKStr, "设置光谱系数K", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "gpxsk", specCoefKStr);

                    byte[] specCoefKByte = floatToBytes(specCoefK);

                    SendManager.SendCmd(componetName + "_" + "设置光谱系数K_06_15", S0, 3, 200, specCoefKByte);

                    mEtSpecCoefK.setText(specCoefKStr);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.spec_coe_k_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtSpecCoefK.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class specCoefBFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String specCoefBStr = mEtSpecCoefB.getText().toString().trim();
                if (!specCoefBStr.equals("")) {
                    specCoefBStr = editDataFormat(specCoefBStr, Integer.parseInt(getConfigData(componetName, "YXWS")));
                    float specCoefB = Float.parseFloat(specCoefBStr);
                    saveOperationLogDataModifyMsg(componetName, "gpxsb", specCoefBStr, "设置光谱系数B", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "gpxsb", specCoefBStr);

                    byte[] specCoefBByte = floatToBytes(specCoefB);

                    SendManager.SendCmd(componetName + "_" + "设置光谱系数B_06_16", S0, 3, 200, specCoefBByte);

                    mEtSpecCoefB.setText(specCoefBStr);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.spec_coe_b_empty), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtSpecCoefB.clearFocus();
                return true;
            }
            return false;
        }
    }


    private class EditFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.etCompensateCoeK:
                        setComponentEditNoZeroFloatData(mCompName, (EditText) v, iPoint, -100, 100, 1, "设置光谱补偿系数K");
                        updateConfigData(componetName, "gpbck", ((EditText) v).getText().toString());
                        break;
                    case R.id.etCompensateCoeB:
                        setComponentEditFloatData(mCompName, (EditText) v, iPoint, -100, 100, 0, "设置光谱补偿系数B");
                        updateConfigData(componetName, "gpbcb", ((EditText) v).getText().toString());
                        break;
                }
                syncList();
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

    //region 读取命令数据界面显示
    @SuppressLint("HandlerLeak")
    public static Handler mgpcldyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    if (tvgpCalibCoef != null) {
                        tvgpCalibCoef.setText("标定系数   " + (getCmds(componetName).getCmd(0).getValue() == null ? "" : getCmds(componetName).getCmd(0).getValue()).toString());
                        byte[] arrayOfByte = DataUtil.shortToByte((short) 1);
                        DataUtil.reverse(arrayOfByte);
                        SendManager.SendCmd(componetName + "_查温度B" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 1);
                    }
                }
                break;
                case 1: {
                    if (tvgpCalibCoef != null) {
                        tvgpCalibCoef.setText("标定系数   " + (getCmds(componetName).getCmd(0).getValue() == null ? "" : getCmds(componetName).getCmd(0).getValue()).toString() + " / "
                                + (getCmds(componetName).getCmd(1).getValue() == null ? "" : getCmds(componetName).getCmd(1).getValue()).toString());

                        byte[] arrayOfByte = DataUtil.shortToByte((short) 5121);
                        DataUtil.reverse(arrayOfByte);
                        SendManager.SendCmd(componetName + "_查光谱参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 10);
                    }
                }
                break;
                case 5121: {
                    mEtSpecAddr.setText(getCmds(componetName).getCmd(5121).getValue() == null ? "" : getCmds(componetName).getCmd(5121).getValue().toString());
                    mEtIntegTime.setText(getCmds(componetName).getCmd(5122).getValue() == null ? "" : getCmds(componetName).getCmd(5122).getValue().toString());
                    mEtAvgCount.setText(getCmds(componetName).getCmd(5123).getValue() == null ? "" : getCmds(componetName).getCmd(5123).getValue().toString());
                    mEtDarkCurTreat.setText(getCmds(componetName).getCmd(5124).getValue() == null ? "" : getCmds(componetName).getCmd(5124).getValue().toString());
                    mEtLampVol.setText(getCmds(componetName).getCmd(5125).getValue() == null ? "" : getCmds(componetName).getCmd(5125).getValue().toString());
                    mEtLampCount.setText(getCmds(componetName).getCmd(5126).getValue() == null ? "" : getCmds(componetName).getCmd(5126).getValue().toString());
                    mEtDarkCurCompen.setText(getCmds(componetName).getCmd(5127).getValue() == null ? "" : getCmds(componetName).getCmd(5127).getValue().toString());
                    mEtSpecCoefK.setText(getCmds(componetName).getCmd(5128).getValue() == null ? "" : getCmds(componetName).getCmd(5128).getValue().toString());
                    mEtSpecCoefB.setText(getCmds(componetName).getCmd(5129).getValue() == null ? "" : getCmds(componetName).getCmd(5129).getValue().toString());
                }
                break;
                case 5130:
                    String pointSum = getCmds(componetName).getCmd(5130).getValue() == null ? "" : getCmds(componetName).getCmd(5130).getValue().toString();
                    int iPointSum = 0;
                    if (Integer.parseInt(pointSum) > 20) {
                        iPointSum = 20;
                    } else {
                        iPointSum = Integer.parseInt(pointSum);
                    }
                    saveOperationLogDataModifyMsg(componetName, "gpcldSum", String.valueOf(iPointSum), "光谱测量波长数", ErrorLog.msgType.操作_信息);
                    updateConfigData(componetName, "gpcldSum", String.valueOf(iPointSum));
                    Log.i("pointSum", String.valueOf(iPointSum));
                    if (!(arrayListEnergy.size() > iPointSum) || (arrayListEnergy.get(0).length < 5)) {
                        arrayListEnergy.clear();
                        for (int i = 0; i < iPointSum; i++) {
                            arrayListEnergy.add(new String[]{"shrink", "null", "null", "null", "null"});
                        }
                        packMeasPoint();
                    }

                    break;
                case 5150: {
                    String[] pointLow = new String[20];
                    String[] pointHigh = new String[20];
                    int pointSum2 = Integer.parseInt(getConfigData(componetName, "gpcldSum"));
                    for (int i = 0; i < pointSum2; i++) {
                        pointLow[i] = getCmds(componetName).getCmd(5150 + 2 * i).getValue() == null ? "" : getCmds(componetName).getCmd(5150 + 2 * i).getValue().toString();
                        pointHigh[i] = getCmds(componetName).getCmd(5150 + 2 * i + 1).getValue() == null ? "" : getCmds(componetName).getCmd(5150 + 2 * i + 1).getValue().toString();
                        Log.i("pointLow", i + "/" + Integer.valueOf(pointLow[i]));
                        Log.i("pointHigh", i + "/" + Integer.valueOf(pointHigh[i]));
                        if (!pointLow[i].equals("") && !pointHigh[i].equals("")) {
                            if (measPointSetValue(i, Integer.valueOf(pointLow[i]), Integer.valueOf(pointHigh[i]))) {
                                syncMeasPoint();
                            }
                        }
                    }
                }
                break;
                case 5190:
                    String[] pointVol = new String[20];

                    int pointSum2 = Integer.parseInt(getConfigData(componetName, "gpcldSum"));
                    for (int i = 0; i < pointSum2; i++) {
                        pointVol[i] = getCmds(componetName).getCmd(5190 + i).getValue() == null ? "" : getCmds(componetName).getCmd(5190 + i).getValue().toString();
                        Log.i("pointVol", i + "/" + Integer.valueOf(pointVol[i]));
                        measPointVolSetValue(i, Integer.parseInt(pointVol[i]));
                    }
                    break;
                case 5210:
                    String[] pointLampTimes = new String[20];
                    int pointSum3 = Integer.parseInt(getConfigData(componetName, "gpcldSum"));
                    for (int i = 0; i < pointSum3; i++) {
                        pointLampTimes[i] = getCmds(componetName).getCmd(5210 + i).getValue() == null ? "" : getCmds(componetName).getCmd(5210 + i).getValue().toString();
                        Log.i("pointLampTimes", i + "/" + Integer.valueOf(pointLampTimes[i]));
                        measPointLampTimesSetValue(i, Integer.parseInt(pointLampTimes[i]));
                    }
                    msg = new Message();
                    msg.what = 1;
                    updateLV.sendMessage(msg);
                    break;
            }
        }
    };

    //endregion
    private class LVEnergyOnItemClick implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView textView = (TextView) view.findViewById(R.id.tvMeasPoint);
            String measPoint = textView.getText().toString();
            String measPointStr = getConfigData(mCompName, "gpcld");

            if (i < arrayListEnergy.size() - 1) {
                MainActivity.main.removeDestopText(mfb);/* 关闭窗体悬浮*/
                Dialog_MeasPointReset dialog_measPoint = new Dialog_MeasPointReset();
                Bundle bundle = new Bundle();
                String[] strings = arrayListEnergy.get(i);
                bundle.putString("reset-wave", strings[1]);//波长
                bundle.putString("reset-voltage", strings[3]);//电压
                bundle.putString("reset-time", strings[4]);//次数
                dialog_measPoint.setArguments(bundle);
                dialog_measPoint.setTargetFragment(fragment, i);
                dialog_measPoint.show(fm, "reset");
            }
            return false;
        }
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(bChangeLV_energy){
                Toast.makeText(getActivity(), getString(R.string.the_measuring_point_has_been_changed_please_save_it_first), Toast.LENGTH_SHORT).show();
                return;
            }
            mCallbacks.onListSelected(v);
        }
    }

    private class btnSaveClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setPointSum(arrayListEnergy.size() - 1);

            isSendCountCmd = false;
            LV_energy.setAdapter(new LVAdapter(Admin, arrayListEnergy, main));

            int size = arrayListEnergy.size() - 1;
            if (operationFlag && size > 0) {
                operationFlag = false;

                for (int i = 0; i < size; ++i) {
                    operationLog += arrayListEnergy.get(i)[1] + ";";
                }

                tempLog = operationLog;
                AddError(componetName, 595, ErrorLog.msgType.运维_信息);
                saveOperationLogMsg(componetName, componetName + "_新增测量点: " + operationLog, ErrorLog.msgType.操作_信息);
                operationLog = "";
            }
            bChangeLV_energy = false;
        }
    }

    private class btnTempStdClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SendManager.SendCmd(componetName + "_温度标定" + "_8_00", S0, 3, 200, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                bChangeLV_energy = true;
                String stringExtra = data.getStringExtra("measPointData");
                mCallbacks.onDialogRS();
                if (stringExtra.equals(";")) {
                    return;
                } else {
                    String[] str = stringExtra.split(",");
                    arrayListEnergy.remove(arrayListEnergy.size() - 1);
                    arrayListEnergy.add(arrayListEnergy.size(), str);
                    isSendCountCmd = true;

                    String[] pointVal = str[1].substring(0, str[1].length() - 3).split("-");
                    int cmdIndex = arrayListEnergy.size();
                    if (pointVal.length == 1) {
                        byte[] pointValByte = toByteArray(Integer.parseInt(pointVal[0]), 4);
                        byte[] specPointByte = copybyte(pointValByte, pointValByte);

                        SendManager.SendCmd(componetName + "_" + "设置光谱测量点_06_" + (cmdIndex + 16), S0, 3, 200, specPointByte);
                    } else {
                        byte[] specPointByte = copybyte(toByteArray(Integer.parseInt(pointVal[0]), 4), toByteArray(Integer.parseInt(pointVal[1]), 4));

                        SendManager.SendCmd(componetName + "_" + "设置光谱测量点_06_" + (cmdIndex + 16), S0, 3, 200, specPointByte);
                    }

                    String pointVol = str[3].replace(" ", "");
                    byte[] pointVolByte = toByteArray(Integer.parseInt(pointVol), 4);
                    SendManager.SendCmd(componetName + "_" + "设置光谱测量点电压_06_" + (cmdIndex + 171), S0, 3, 200, pointVolByte);

                    String pointLampTimes = str[4].replace(" ", "");
                    byte[] pointLampTimesByte = toByteArray(Integer.parseInt(pointLampTimes), 4);
                    SendManager.SendCmd(componetName + "_" + "设置光谱测量点打灯次数_06_" + (cmdIndex + 191), S0, 3, 200, pointLampTimesByte);

                    setPointSum(arrayListEnergy.size() - 1);

                    packMeasPoint();
                    LV_energy.setAdapter(new LVAdapter(Admin, arrayListEnergy, this.getActivity()));//
                    operationFlag = true;
                }
            }
            break;
            case 2: {
                mCallbacks.onDialogRS();
            }
            break;
            case 3: {
                bChangeLV_energy = true;
                String stringExtra = data.getStringExtra("measPointData");
                mCallbacks.onDialogRS();
                if (stringExtra.equals(";")) {
                    return;
                } else {
                    String[] str = stringExtra.split(",");
                    String[] index = str[0].split("_");
                    str[0] = index[0];
                    int num = Integer.parseInt(index[1]);
                    if (num < arrayListEnergy.size()) {
                        arrayListEnergy.set(num, str);
                    }

                    isSendCountCmd = true;

                    String[] pointVal = str[1].substring(0, str[1].length() - 3).split("-");
                    int cmdIndex = num + 1;
                    if (pointVal.length == 1) {
                        byte[] pointValByte = toByteArray(Integer.parseInt(pointVal[0]), 4);
                        byte[] specPointByte = copybyte(pointValByte, pointValByte);

                        SendManager.SendCmd(componetName + "_" + "设置光谱测量点_06_" + (cmdIndex + 16), S0, 3, 200, specPointByte);
                    } else {
                        byte[] specPointByte = copybyte(toByteArray(Integer.parseInt(pointVal[0]), 4), toByteArray(Integer.parseInt(pointVal[1]), 4));

                        SendManager.SendCmd(componetName + "_" + "设置光谱测量点_06_" + (cmdIndex + 16), S0, 3, 200, specPointByte);
                    }

                    String pointVol = str[3].replace(" ", "");
                    byte[] pointVolByte = toByteArray(Integer.parseInt(pointVol), 4);
                    SendManager.SendCmd(componetName + "_" + "设置光谱测量点电压_06_" + (cmdIndex + 171), S0, 3, 200, pointVolByte);

                    String pointLampTimes = str[4].replace(" ", "");
                    byte[] pointLampTimesByte = toByteArray(Integer.parseInt(pointLampTimes), 4);
                    SendManager.SendCmd(componetName + "_" + "设置光谱测量点打灯次数_06_" + (cmdIndex + 191), S0, 3, 200, pointLampTimesByte);

                    setPointSum(arrayListEnergy.size() - 1);

                    packMeasPoint();
                    LV_energy.setAdapter(new LVAdapter(arrayListEnergy, this.getActivity(), true));
                    operationFlag = true;
                }
            }
            break;
        }
    }

    public static void setPointSum(int sum) {
        byte[] pointCountByte = toByteArray(sum, 4);
        SendManager.SendCmd(componetName + "_" + "设置光谱测量波长数_06_37", S0, 3, 200, pointCountByte);
        saveOperationLogDataModifyMsg(componetName, "gpcldSum", String.valueOf(sum), "光谱测量波长数", ErrorLog.msgType.操作_信息);
        updateConfigData(componetName, "gpcldSum", String.valueOf(sum));

    }

    private class getSpecDateClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            SendManager.SendCmd(MainActivity.mCompName + "_" + "获取光谱能量_23_02", S0, 1, 200, "03_01_01");
        }
    }

    private class specMeaClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SendManager.SendCmd(componetName + "_打灯测量" + "_8_0D", S0, 1, 200, null);
            new Thread() {
                @Override
                public void run() {
                    try {
                        String strPjcs = getConfigData(componetName, "pjcs");
                        int sleep = 6;
                        if (!strPjcs.equals("")) {
                            sleep = Integer.parseInt(strPjcs) * 2;
                        }
                        Thread.sleep(sleep * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SendManager.SendCmd(MainActivity.mCompName + "_" + "获取光谱能量_23_02", S0, 1, 200, "03_01_01");
                }
            }.start();
        }
    }

    private class spectralCurveClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mBtnSpectralCurve.getText().equals(getString(R.string.spec_curve))) {
                lay_leftrect1.setVisibility(View.GONE);
                lay_leftrect2.setVisibility(View.VISIBLE);

                LineData data = new LineData();

                // 数据显示的颜色
                data.setValueTextColor(Color.WHITE);

                // 先增加一个空的数据，随后往里面动态添加
                mChart.setData(data);
                int inLoop = 0;
                for (; inLoop < specSum; inLoop++) {
                    int iVal = specData[inLoop];
                    fragment.addEntry(iVal);
                }
                mBtnSpectralCurve.setText("隐藏曲线");
            } else {
                lay_leftrect1.setVisibility(View.VISIBLE);
                lay_leftrect2.setVisibility(View.GONE);
                mBtnSpectralCurve.setText(getString(R.string.spec_curve));
            }
        }
    }

    private class hiddenCurveClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            lay_leftrect1.setVisibility(View.VISIBLE);
            lay_leftrect2.setVisibility(View.GONE);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler updateLV = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    syncMeasPoint();
                    break;
                case 2:
                    break;
            }

            LV_energy.setAdapter(new LVAdapter(Admin, arrayListEnergy, main));
            packMeasPoint();
        }
    };

    //画图
    private void InitChart() {
        mChart = (LineChart) v.findViewById(R.id.chartSpectrum);

        mChart.setDescription("");
        mChart.setNoDataTextDescription("");

        mChart.setTouchEnabled(true);

        // 没有数据的时候，显示“”
        mChart.setNoDataText("");

        // 可拖曳
        mChart.setDragEnabled(true);

        // 可缩放
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        mChart.setPinchZoom(true);

        // 设置图表的背景颜色
        //mChart.setBackgroundColor(Color.WHITE);

        // 不显示图例
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        LineData data = new LineData();

        // 数据显示的颜色
        data.setValueTextColor(Color.WHITE);

        // 先增加一个空的数据，随后往里面动态添加
        mChart.setData(data);

        // 图表的注解(只有当数据集存在时候才生效)
        Legend l = mChart.getLegend();

        // 可以修改图表注解部分的位置
        // l.setPosition(LegendPosition.LEFT_OF_CHART);

        // 线性，也可是圆
        l.setForm(Legend.LegendForm.LINE);

        // 颜色
        l.setTextColor(Color.WHITE);

        // x坐标轴
        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(false);
        //xl.setTextSize(25f);

        // 几个x坐标轴之间才绘制？
        xl.setSpaceBetweenLabels(20);

        // 如果false，那么x坐标轴将不可见
        xl.setEnabled(false);

        // 将X坐标轴放置在底部，默认是在顶部。
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 图表左边的y坐标轴线
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(25f);

        // 最大值
        //leftAxis.setAxisMaxValue(90f);

        // 最小值
        //leftAxis.setAxisMinValue(40f);

        // 不一定要从0开始
        leftAxis.setStartAtZero(false);

        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        // 不显示图表的右边y坐标轴线
        rightAxis.setEnabled(false);

        // 每点击一次按钮，增加一个点
        /*
        Button addButton = (Button) v.findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addEntry();
            }
        });
        */
    }

    // 添加进去一个坐标
    public void addEntry(int value) {
        LineData data = mChart.getData();

        // 每一个LineDataSet代表一条线，每张统计图表可以同时存在若干个统计折线，这些折线像数组一样从0开始下标。
        // 本例只有一个，那么就是第0条折线
        LineDataSet set = data.getDataSetByIndex(0);

        // 如果该统计折线图还没有数据集，则创建一条出来，如果有则跳过此处代码。
        if (set == null) {
            set = createLineDataSet();
            data.addDataSet(set);
        }
        set.setDrawCubic(true);  //设置曲线为圆滑的线

        // 先添加一个x坐标轴的值
        // 因为是从0开始，data.getXValCount()每次返回的总是全部x坐标轴上总数量，所以不必多此一举的加1
        data.addXValue((data.getXValCount()) + "");

        // 生成随机测试数
        //float f = (float) ((Math.random()) * 20 + 50);

        // set.getEntryCount()获得的是所有统计图表上的数据点总量，
        // 如从0开始一样的数组下标，那么不必多次一举的加1
        Entry entry = new Entry(value, set.getEntryCount());

        // 往linedata里面添加点。注意：addentry的第二个参数即代表折线的下标索引。
        // 因为本例只有一个统计折线，那么就是第一个，其下标为0.
        // 如果同一张统计图表中存在若干条统计折线，那么必须分清是针对哪一条（依据下标索引）统计折线添加。
        data.addEntry(entry, 0);

        // 像ListView那样的通知数据更新
        mChart.notifyDataSetChanged();

        // 当前统计图表中最多在x轴坐标线上显示的总量
        mChart.setVisibleXRangeMaximum(256);

        // y坐标轴线最大值
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);

        // 将坐标移动到最新
        // 此代码将刷新图表的绘图
        mChart.moveViewToX(data.getXValCount() - 5);

        // mChart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);
    }

    // 初始化数据集，添加一条统计折线，可以简单的理解是初始化y坐标轴线上点的表征
    private LineDataSet createLineDataSet() {

        LineDataSet set = new LineDataSet(null, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        // 折线的颜色
        //set.setColor(ColorTemplate.getHoloBlue());
        set.setColor(Color.RED);
        // 不显示坐标点的小圆点
        set.setDrawCircles(false);

        set.setCircleColor(Color.BLACK);
        set.setLineWidth(3f);
        set.setCircleSize(5f);
        set.setFillAlpha(128);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.GREEN);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        return set;
    }
}
