package com.yzlm.cyl.cfragment.Content.Component.List2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Content.Component.Config.MeaParCfg;
import com.yzlm.cyl.cfragment.Content.Component.Config.MeaParTable;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.Objects;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getParDefParValue;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getParDownLimit;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getParShowName;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getParUpLimit;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.floatToBytes;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.lMeaPar;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/*
 * Created by caoyiliang on 2016/10/28.
 *
 * 测量参数增加注意事项：
 * 1、配置文件支持参数名称更改为显示名称
 * 2、新增参数是否需要参数保护
 * 3、新增参数注意测量时候界面输入锁定
 * 3、新增参数注意根据配置文件配置进行界面显示
 *
 */
public class List2_Content1 extends SubFragment {
    private static EditText mEtxjwd;
    private static EditText mEtxjsc;
    private static EditText mEtxswd;
    private static EditText mEtxssc;
    private static EditText mEtxiajwd1;
    private static EditText mEtxiajwd2;
    private static EditText mEtxsjz;
    private static EditText mEtxddytj;
    private static EditText mEtWaitStanding;
    private static EditText eTCalibrationTemp, eTCalibrationTime;
    private static EditText mEtxjyz;
    private static EditText mEtpjsj;
    private static EditText mEtBurnTemperature;
    private static EditText mEtBurnDownTemperature;
    private static EditText mEtToxicityTestTime;

    private LinearLayout linearLayout1_1, linearLayout1_2, linearLayout1_3, linearLayout1_4, linearLayout1_5,
            linearLayout1_6, linearLayout1_7, linearLayout1_8, linearLayout1_9, linearLayout1_10, linearLayout1_11,
            linearLayout1_12, linearLayout1_13, linearLayout1_14, linearLayout1_15;
    private LinearLayout linearLayout2_1, linearLayout2_2, linearLayout2_3, linearLayout2_4, linearLayout2_5, linearLayout2_6, linearLayout2_7;
    private LinearLayout layout_ddSetting;

    private static String componetName = "";
    private static List2_Content1 fragment = null;

    private MeaParTable mt;

    private EditText mEtxsytj;
    private EditText mEtxyhjtj;
    private static EditText mEtxhyjtj;
    private EditText mEtxhyjnd;
    private static EditText mEtxchjtj;
    private static EditText mEtxjytj;

    private String[] strMeaParName = new String[22];

    public static List2_Content1 newInstance() {
        if (fragment == null) {
            fragment = new List2_Content1();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list2_content1;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            componetName = mCompName;
            RealTimeStatusThread.suspend();
            initMeaParName();
            initLayoutView();

            mEtxddytj = v.findViewById(R.id.eTddytj);
            mEtxsytj = v.findViewById(R.id.eTsytj);
            mEtxyhjtj = v.findViewById(R.id.eTyhjtj);
            mEtxhyjtj = v.findViewById(R.id.eThyjtj);
            mEtxhyjnd = v.findViewById(R.id.eThyjnd);
            mEtxchjtj = v.findViewById(R.id.eTchjtj);
            mEtxjytj = v.findViewById(R.id.eTjytj);

            getMeasCategoryWin();
            initLayoutGone();
            showMeaPar();
            winWidgetInit();

            byte[] arrayOfByte = DataUtil.shortToByte((short) 5300);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(componetName + "_查测量参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 12);

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 9220);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(componetName + "_查测量参数二" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 2);

            if (QueryMeasCateg(componetName).equals("11")) {
                byte[] arrayOfByte3 = DataUtil.shortToByte((short) 9500);
                DataUtil.reverse(arrayOfByte3);
                SendManager.SendCmd(componetName + "_查TOC测量参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 2);
            }

            if (QueryMeasCateg(componetName).equals("13")) {
                byte[] arrayOfByte4 = DataUtil.shortToByte((short) 9809);
                DataUtil.reverse(arrayOfByte4);
                SendManager.SendCmd(componetName + "_查毒性测试时间" + "_3_" + DataUtil.bytesToHexString(arrayOfByte4, 2).replace(" ", ""), Global.S0, 3, 200, 1);
            }
			
            //消解温度
            mEtxjwd = v.findViewById(R.id.eTxjwd);
            mEtxjwd.setOnFocusChangeListener(new xjwdFocusChange());
            mEtxjwd.setOnEditorActionListener(new xjwdFocusChange());

            //消解时长
            mEtxjsc = v.findViewById(R.id.eTxjsc);
            mEtxjsc.setOnFocusChangeListener(new xjscFocusChange());
            mEtxjsc.setOnEditorActionListener(new xjscFocusChange());

            //显色温度
            mEtxswd = v.findViewById(R.id.eTxswd);
            mEtxswd.setOnFocusChangeListener(new xswdFocusChange());
            mEtxswd.setOnEditorActionListener(new xswdFocusChange());

            //显色时长
            mEtxssc = v.findViewById(R.id.eTxssc);
            mEtxssc.setOnFocusChangeListener(new xsscFocusChange());
            mEtxssc.setOnEditorActionListener(new xsscFocusChange());

            //消解降温
            mEtxiajwd1 = v.findViewById(R.id.eTddwd);
            mEtxiajwd1.setOnFocusChangeListener(new xiajwd1FocusChange());
            mEtxiajwd1.setOnEditorActionListener(new xiajwd1FocusChange());

            //显色降温
            mEtxiajwd2 = v.findViewById(R.id.eTxiajwd);
            mEtxiajwd2.setOnFocusChangeListener(new xiajwd2FocusChange());
            mEtxiajwd2.setOnEditorActionListener(new xiajwd2FocusChange());

            //显色静置
            mEtxsjz = v.findViewById(R.id.eTxsjz);
            mEtxsjz.setOnFocusChangeListener(new xsjzFocusChange());
            mEtxsjz.setOnEditorActionListener(new xsjzFocusChange());


            //等待静置
            mEtWaitStanding = v.findViewById(R.id.eTWaitStanding);
            mEtWaitStanding.setOnFocusChangeListener(new waitStandingFocusChange());
            mEtWaitStanding.setOnEditorActionListener(new waitStandingFocusChange());


            //校准消解温度
            eTCalibrationTemp = v.findViewById(R.id.eTCalibrationTemp);
            eTCalibrationTemp.setOnFocusChangeListener(new calibrationTempFocusChange());
            eTCalibrationTemp.setOnEditorActionListener(new calibrationTempFocusChange());


            //校准消解时长
            eTCalibrationTime = v.findViewById(R.id.eTCalibrationTime);
            eTCalibrationTime.setOnFocusChangeListener(new calibrationTimeFocusChange());
            eTCalibrationTime.setOnEditorActionListener(new calibrationTimeFocusChange());


            //消解阀值
            mEtxjyz = v.findViewById(R.id.eTxsyz);
            mEtxjyz.setOnFocusChangeListener(new xjyzFocusChange());
            mEtxjyz.setOnEditorActionListener(new xjyzFocusChange());


            //平均时间
            mEtpjsj = v.findViewById(R.id.eTpjsj);
            mEtpjsj.setOnFocusChangeListener(new pjsjFocusChange());
            mEtpjsj.setOnEditorActionListener(new pjsjFocusChange());
            /*燃烧炉升温温度*/
            mEtBurnTemperature = v.findViewById(R.id.eTBurnerTemperature);
            mEtBurnTemperature.setOnFocusChangeListener(new editTextFocusChange());
            mEtBurnTemperature.setOnEditorActionListener(new editTextFocusChange());
            /*燃烧炉下降温度*/
            mEtBurnDownTemperature = v.findViewById(R.id.eTBurnerDownTemperature);
            mEtBurnDownTemperature.setOnFocusChangeListener(new editTextFocusChange());
            mEtBurnDownTemperature.setOnEditorActionListener(new editTextFocusChange());
            /*毒性测试时间*/
            mEtToxicityTestTime = v.findViewById(R.id.eTToxicityTestTime);
            mEtToxicityTestTime.setOnFocusChangeListener(new editTextFocusChange());
            mEtToxicityTestTime.setOnEditorActionListener(new editTextFocusChange());
            if (Objects.equals(doFlowing.get(componetName), context.getResources().getString(R.string.waiting_for_instructions))) {
                setMeasureParameterTouch(true);
            } else {
                setMeasureParameterTouch(false);
            }

            ParProtection();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + componetName + "]" + e.toString());
        } finally {
            RealTimeStatusThread.resume();
        }

    }

    /**
     * 将参数布局文件默认为 GONE
     */
    private void initLayoutGone() {
        linearLayout1_1.setVisibility(View.GONE);
        linearLayout1_2.setVisibility(View.GONE);
        linearLayout1_3.setVisibility(View.GONE);
        linearLayout1_4.setVisibility(View.GONE);
        linearLayout1_5.setVisibility(View.GONE);
        linearLayout1_6.setVisibility(View.GONE);
        linearLayout1_7.setVisibility(View.GONE);
        linearLayout1_8.setVisibility(View.GONE);
        linearLayout1_9.setVisibility(View.GONE);
        linearLayout1_10.setVisibility(View.GONE);
        linearLayout1_11.setVisibility(View.GONE);
        linearLayout1_12.setVisibility(View.GONE);
        linearLayout1_13.setVisibility(View.GONE);
        linearLayout1_14.setVisibility(View.GONE);
        linearLayout1_15.setVisibility(View.GONE);

        linearLayout2_1.setVisibility(View.GONE);
        linearLayout2_2.setVisibility(View.GONE);
        linearLayout2_3.setVisibility(View.GONE);
        linearLayout2_4.setVisibility(View.GONE);
        linearLayout2_5.setVisibility(View.GONE);
        linearLayout2_6.setVisibility(View.GONE);
        linearLayout2_7.setVisibility(View.GONE);
    }

    /**
     * 初始化参数布局文件
     */
    private void initLayoutView() {
        /*消解温度*/
        linearLayout1_1 = v.findViewById(R.id.list2con1line1);
        /*消解时长*/
        linearLayout1_2 = v.findViewById(R.id.list2con1line2);
        /*显色温度*/
        linearLayout1_3 = v.findViewById(R.id.list2con1line3);
        /*显色时长*/
        linearLayout1_4 = v.findViewById(R.id.list2con1line4);
        /*消解降温*/
        linearLayout1_5 = v.findViewById(R.id.list2con1line5);
        /*显色降温*/
        linearLayout1_6 = v.findViewById(R.id.list2con1line6);
        /*显色静置*/
        linearLayout1_7 = v.findViewById(R.id.list2con1line7);
        /*等待静置*/
        linearLayout1_8 = v.findViewById(R.id.list2con1line8);
        /*校准消解温度*/
        linearLayout1_9 = v.findViewById(R.id.list2con1line9);
        /*校准消解时长*/
        linearLayout1_10 = v.findViewById(R.id.list2con1line10);
        /*消解阈值*/
        linearLayout1_11 = v.findViewById(R.id.list2con1line11);
        /*平均时间*/
        linearLayout1_12 = v.findViewById(R.id.list2con1line12);
        /*燃烧炉升温温度*/
        linearLayout1_13 = v.findViewById(R.id.list2con1line13);
        /*燃烧炉降温温度*/
        linearLayout1_14 = v.findViewById(R.id.list2con1line14);
        /*毒性测试时间*/
        linearLayout1_15 = v.findViewById(R.id.list2con1line15);

        /*水样体积*/
        linearLayout2_1 = v.findViewById(R.id.list2con2line1);
        /*氧化剂体积*/
        linearLayout2_2 = v.findViewById(R.id.list2con2line2);
        /*还原剂体积*/
        linearLayout2_3 = v.findViewById(R.id.list2con2line3);
        /*还原剂浓度*/
        linearLayout2_4 = v.findViewById(R.id.list2con2line4);
        /*滴定液体积*/
        linearLayout2_5 = v.findViewById(R.id.list2con2line5);
        /*催化剂体积*/
        linearLayout2_6 = v.findViewById(R.id.list2con2line6);
        /*碱液体积*/
        linearLayout2_7 = v.findViewById(R.id.list2con2line7);
        /*滴定参数布局*/
        layout_ddSetting = v.findViewById(R.id.layout_ddSetting);
    }

    /**
     * 初始化默认参数显示名称
     */
    private void initMeaParName() {
        strMeaParName[0] = context.getResources().getString(R.string.digestionTemper);
        strMeaParName[1] = context.getResources().getString(R.string.digestionTimer);
        strMeaParName[2] = context.getResources().getString(R.string.colorTemper);
        strMeaParName[3] = context.getResources().getString(R.string.colorTimer);
        strMeaParName[4] = context.getResources().getString(R.string.dispellingCooling);
        strMeaParName[5] = context.getResources().getString(R.string.colorCooling);
        strMeaParName[6] = context.getResources().getString(R.string.colorStand);
        strMeaParName[7] = context.getResources().getString(R.string.waterVolume);
        strMeaParName[8] = context.getResources().getString(R.string.oxidantVolume);
        strMeaParName[9] = context.getResources().getString(R.string.reductantVolume);
        strMeaParName[10] = context.getResources().getString(R.string.reductantCoc);
        strMeaParName[11] = context.getResources().getString(R.string.titrationVolume);
        strMeaParName[12] = context.getResources().getString(R.string.waitingStand);
        strMeaParName[13] = context.getResources().getString(R.string.calibrationDigestionTempe);
        strMeaParName[14] = context.getResources().getString(R.string.calibrationDigestionTime);
        strMeaParName[15] = context.getResources().getString(R.string.catalystVolume);
        strMeaParName[16] = context.getResources().getString(R.string.XJThresholdVal);
        strMeaParName[17] = context.getResources().getString(R.string.averagetime);
        strMeaParName[18] = context.getResources().getString(R.string.burnTemperature);
        strMeaParName[19] = context.getResources().getString(R.string.burnDownTemperature);
        strMeaParName[20] = context.getResources().getString(R.string.lyeVolume);
        strMeaParName[21] = context.getResources().getString(R.string.toxicity_test_time);
    }

    /**
     * 参数保护，根据登陆权限显示
     */
    private void ParProtection() {
        if (getPublicConfigData("LogInName").equals("0") || getPublicConfigData("LogInName").equals("1")) {
            mEtxjwd.setEnabled(false);
            mEtxjsc.setEnabled(false);
            mEtxswd.setEnabled(false);
            mEtxssc.setEnabled(false);
            mEtxiajwd1.setEnabled(false);
            mEtxiajwd2.setEnabled(false);
            mEtxsjz.setEnabled(false);
            mEtxddytj.setEnabled(false);
            mEtWaitStanding.setEnabled(false);
            eTCalibrationTemp.setEnabled(false);
            eTCalibrationTime.setEnabled(false);

            if (QueryMeasCateg(componetName).equals("3") || QueryMeasCateg(componetName).equals("8") || QueryMeasCateg(componetName).equals("13")) {
                mEtxyhjtj.setEnabled(false);
                mEtxsytj.setEnabled(false);
                mEtxhyjnd.setEnabled(false);
                mEtxchjtj.setEnabled(false);
                mEtxhyjtj.setEnabled(false);
                mEtxjytj.setEnabled(false);
            }
            mEtxjyz.setEnabled(false);
            mEtpjsj.setEnabled(false);
            mEtBurnTemperature.setEnabled(false);
            mEtBurnDownTemperature.setEnabled(false);
        }
    }

    /**
     * 文本名称根据配置文件显示
     */
    private void winWidgetInit() {
        try {
            TextView mTvxjwd = v.findViewById(R.id.tvxjwd);
            mTvxjwd.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[0]));

            TextView mTvxjsc = v.findViewById(R.id.tvxjsc);
            mTvxjsc.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[1]));

            TextView mTvxswd = v.findViewById(R.id.tvxswd);
            mTvxswd.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[2]));

            TextView mTvxssc = v.findViewById(R.id.tvxssc);
            mTvxssc.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[3]));

            TextView mTvxjjw = v.findViewById(R.id.tvxjjw);
            mTvxjjw.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[4]));

            TextView mTvxsjw = v.findViewById(R.id.tvxsjw);
            mTvxsjw.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[5]));

            TextView mTvxsjz = v.findViewById(R.id.tvxsjz);
            mTvxsjz.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[6]));

            TextView mTvsytj = v.findViewById(R.id.tvsytj);
            mTvsytj.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[7]));

            TextView mTvyhjtj = v.findViewById(R.id.tvyhjtj);
            mTvyhjtj.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[8]));

            TextView mTvhyjtj = v.findViewById(R.id.tvhyjtj);
            mTvhyjtj.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[9]));

            TextView mTvhyjnd = v.findViewById(R.id.tvhyjnd);
            mTvhyjnd.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[10]));

            TextView mTvddytj = v.findViewById(R.id.tvddytj);
            mTvddytj.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[11]));

            TextView mTvddjz = v.findViewById(R.id.tvddjz);
            mTvddjz.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[12]));

            TextView tCalibrationTemp = v.findViewById(R.id.tCalibrationTemp);
            tCalibrationTemp.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[13]));

            TextView tCalibrationTime = v.findViewById(R.id.tCalibrationTime);
            tCalibrationTime.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[14]));

            TextView mTvchjtj = v.findViewById(R.id.tvchjtj);
            mTvchjtj.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[15]));

            // 消解阈值
            TextView tvxsyz = v.findViewById(R.id.tvxsyz);
            tvxsyz.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[16]));
            // 平均时间
            TextView tvpjsj = v.findViewById(R.id.tvpjsj);
            tvpjsj.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[17]));

            // TOC  平台参数  显示名称可配置
            TextView mTvBurnerT = v.findViewById(R.id.tvBurnerTemperature);
            mTvBurnerT.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[18]));

            TextView mTvBurnerDownT = v.findViewById(R.id.tvBurnerDownTemperature);
            mTvBurnerDownT.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[19]));

            TextView mTvjytj = v.findViewById(R.id.tvjytj);
            mTvjytj.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[20]));

            TextView mTvdxcssj = v.findViewById(R.id.tvToxicityTestTime);
            mTvdxcssj.setText(getParShowName(mt, lMeaPar, componetName, strMeaParName[21]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测量类型 需要处理的界面
     */
    private void getMeasCategoryWin() {

        if (QueryMeasCateg(componetName).equals("3") || QueryMeasCateg(componetName).equals("8") || QueryMeasCateg(componetName).equals("13")) {
            layout_ddSetting.setVisibility(View.VISIBLE);

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 5710);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(componetName + "_查试剂体积" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 9);

            /*byte[] arrayOfByte2 = DataUtil.shortToByte((short) 5710);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(componetName + "_查滴定液体积" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 1);

            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 5712);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(componetName + "_查还原剂催化剂体积" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 2);
*/
            mEtxsytj.setText(getConfigData(componetName, "WATER_VOLUME"));
            mEtxyhjtj.setText(getConfigData(componetName, "OXIDANT_VOLUME"));
            mEtxhyjtj.setText(getConfigData(componetName, "REDUCTANT_VOLUME"));
            mEtxhyjnd.setText(getConfigData(componetName, "REDUCTANT_COC"));
            //    mEtxddytj.setText(getConfigData(componetName , "TITRATION_VOLUME"));

            mEtxsytj.setOnFocusChangeListener(new editTextFocusChange());
            mEtxsytj.setOnEditorActionListener(new editTextFocusChange());
            mEtxyhjtj.setOnFocusChangeListener(new editTextFocusChange());
            mEtxyhjtj.setOnEditorActionListener(new editTextFocusChange());
            mEtxhyjtj.setOnFocusChangeListener(new editTextFocusChange());
            mEtxhyjtj.setOnEditorActionListener(new editTextFocusChange());
            mEtxhyjnd.setOnFocusChangeListener(new editTextFocusChange());
            mEtxhyjnd.setOnEditorActionListener(new editTextFocusChange());
            mEtxddytj.setOnFocusChangeListener(new editTextFocusChange());
            mEtxddytj.setOnEditorActionListener(new editTextFocusChange());

            mEtxchjtj.setOnFocusChangeListener(new editTextFocusChange());
            mEtxchjtj.setOnEditorActionListener(new editTextFocusChange());

            mEtxjytj.setOnFocusChangeListener(new editTextFocusChange());
            mEtxjytj.setOnEditorActionListener(new editTextFocusChange());
        } else {
            layout_ddSetting.setVisibility(View.GONE);
        }

    }

    @SuppressLint("HandlerLeak")
    public static Handler mclcsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 5300: {
                    if (mEtxjwd != null) {
                        // 加热温度1
                        mEtxjwd.setText((getCmds(componetName).getCmd(5300).getValue() == null ? "" : getCmds(componetName).getCmd(5300).getValue()).toString());
                        updateConfigData(componetName, "xjwd", mEtxjwd.getText().toString());
                    }
                    if (mEtxswd != null) {
                        //加热温度2
                        mEtxswd.setText((getCmds(componetName).getCmd(5301).getValue() == null ? "" : getCmds(componetName).getCmd(5301).getValue()).toString());
                        updateConfigData(componetName, "xswd", mEtxswd.getText().toString());
                    }
                    if (mEtxjsc != null) {
                        // 加热时长1
                        mEtxjsc.setText((getCmds(componetName).getCmd(5302).getValue() == null ? "" : getCmds(componetName).getCmd(5302).getValue()).toString());
                        updateConfigData(componetName, "xjsc", mEtxjsc.getText().toString());
                    }
                    if (mEtxssc != null) {
                        // 加热时长2
                        mEtxssc.setText((getCmds(componetName).getCmd(5303).getValue() == null ? "" : getCmds(componetName).getCmd(5303).getValue()).toString());
                        updateConfigData(componetName, "xssc", mEtxssc.getText().toString());
                    }
                    if (mEtxiajwd1 != null) {
                        // 降温温度1
                        mEtxiajwd1.setText((getCmds(componetName).getCmd(5304).getValue() == null ? "" : getCmds(componetName).getCmd(5304).getValue()).toString());
                        updateConfigData(componetName, "xjjw", mEtxiajwd1.getText().toString());
                    }
                    if (mEtxiajwd2 != null) {
                        // 降温温度2
                        mEtxiajwd2.setText((getCmds(componetName).getCmd(5305).getValue() == null ? "" : getCmds(componetName).getCmd(5305).getValue()).toString());
                        updateConfigData(componetName, "xsjw", mEtxiajwd2.getText().toString());
                    }
                    if (mEtxsjz != null) {
                        // 显示静置-
                        mEtxsjz.setText((getCmds(componetName).getCmd(5307).getValue() == null ? "" : getCmds(componetName).getCmd(5307).getValue()).toString());
                        updateConfigData(componetName, "xsjz", mEtxsjz.getText().toString());
                    }
                    if (mEtWaitStanding != null) {
                        // 等待静置
                        mEtWaitStanding.setText((getCmds(componetName).getCmd(5308).getValue() == null ? "" : getCmds(componetName).getCmd(5308).getValue()).toString());
                        updateConfigData(componetName, "waitStanding", mEtWaitStanding.getText().toString());
                    }
                    if (eTCalibrationTemp != null) {
                        // 校准消解温度
                        eTCalibrationTemp.setText((getCmds(componetName).getCmd(5310).getValue() == null ? "" : getCmds(componetName).getCmd(5310).getValue()).toString());
                        updateConfigData(componetName, "calibrationTemp", eTCalibrationTemp.getText().toString());
                    }
                    if (eTCalibrationTime != null) {
                        // 校准消解时长
                        eTCalibrationTime.setText((getCmds(componetName).getCmd(5311).getValue() == null ? "" : getCmds(componetName).getCmd(5311).getValue()).toString());
                        updateConfigData(componetName, "calibrationTime", eTCalibrationTime.getText().toString());
                    }
                }
                break;
                case 5710:
                    if (mEtxddytj != null) {
                        mEtxddytj.setText((getCmds(componetName).getCmd(5710).getValue() == null ? "" : getCmds(componetName).getCmd(5710).getValue()).toString());
                        updateConfigData(componetName, "TITRATION_VOLUME", mEtxddytj.getText().toString());
                    }
                    //break;
                    //case 5712:
                    if (mEtxhyjtj != null) {
                        if (QueryMeasCateg(componetName).equals("8") || QueryMeasCateg(componetName).equals("13")) {
                            mEtxhyjtj.setText((getCmds(componetName).getCmd(5712).getValue() == null ? "" : getCmds(componetName).getCmd(5712).getValue()).toString());
                            updateConfigData(componetName, "REDUCTANT_VOLUME", mEtxhyjtj.getText().toString());
                        }
                    }
                    if (mEtxchjtj != null) {
                        mEtxchjtj.setText((getCmds(componetName).getCmd(5713).getValue() == null ? "" : getCmds(componetName).getCmd(5713).getValue()).toString());
                        updateConfigData(componetName, "catalystVolume", mEtxchjtj.getText().toString());
                    }
                    if (mEtxjytj != null) {
                        if (QueryMeasCateg(componetName).equals("8") || QueryMeasCateg(componetName).equals("13")) {
                            mEtxjytj.setText((getCmds(componetName).getCmd(5718).getValue() == null ? "" : getCmds(componetName).getCmd(5718).getValue()).toString());
                            updateConfigData(componetName, "lyeVolume", mEtxjytj.getText().toString());
                        }
                    }
                    break;
                case 9220:
                    if (mEtxjyz != null) {
                        //消解阈值
                        mEtxjyz.setText((getCmds(componetName).getCmd(9220).getValue() == null ? "" : getCmds(componetName).getCmd(9220).getValue()).toString());
                        updateConfigData(componetName, "XJYZ", mEtxjyz.getText().toString());
                    }
                    if (mEtpjsj != null) {
                        //平均时间
                        mEtpjsj.setText((getCmds(componetName).getCmd(9221).getValue() == null ? "" : getCmds(componetName).getCmd(9221).getValue()).toString());
                        updateConfigData(componetName, "PJSJ", mEtpjsj.getText().toString());
                    }
                    break;
                case 9500:
                    if (mEtBurnTemperature != null) {
                        //上升温度
                        mEtBurnTemperature.setText((getCmds(componetName).getCmd(9500).getValue() == null ? "" : getCmds(componetName).getCmd(9500).getValue()).toString());
                        updateConfigData(componetName, "BurningTemperature", mEtBurnTemperature.getText().toString());
                    }
                    if (mEtBurnDownTemperature != null) {
                        //下降温度
                        mEtBurnDownTemperature.setText((getCmds(componetName).getCmd(9501).getValue() == null ? "" : getCmds(componetName).getCmd(9501).getValue()).toString());
                        updateConfigData(componetName, "BurningDownTemperature", mEtBurnDownTemperature.getText().toString());
                    }
                    break;
                case 9809:
                    if (mEtToxicityTestTime != null) {
                        //毒性测试时间
                        mEtToxicityTestTime.setText((getCmds(componetName).getCmd(9809).getValue() == null ? "" : getCmds(componetName).getCmd(9809).getValue()).toString());
                        updateConfigData(componetName, "ToxicityTestTime", mEtToxicityTestTime.getText().toString());
                    }
                    break;
            }
        }
    };

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class editTextFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTsytj: {
                        String sytj = mEtxsytj.getText().toString().trim();
                        float up = 5000;
                        float dowm = 0;
                        float def = 0;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[7]) != null) {
                                up = Float.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[7])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[7]) != null) {
                                dowm = Float.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[7])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[7]) != null) {
                                def = Float.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[7])));
                            }
                            if (!sytj.equals("")) {
                                float fSytj = Float.parseFloat(sytj);
                                if (fSytj < dowm || fSytj > up) {
                                    mEtxsytj.setText(String.valueOf(def));
                                }
                                saveOperationLogMsg(componetName, "设置的水样体积_" + mEtxsytj.getText().toString(), ErrorLog.msgType.操作_信息);
                                updateConfigData(componetName, "WATER_VOLUME", mEtxsytj.getText().toString());

                            } else {
                                Toast.makeText(getActivity(), context.getResources().getString(R.string.water_volume_is_empty), Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.water_volume_is_set_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTyhjtj: {
                        String yhjtj = mEtxyhjtj.getText().toString().trim();
                        float Yhjtjup = 5000;
                        float Yhjtjdowm = 0;
                        float Yhjtjdef = 0;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[8]) != null) {
                                Yhjtjup = Float.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[8])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[8]) != null) {
                                Yhjtjdowm = Float.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[8])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[8]) != null) {
                                Yhjtjdef = Float.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[8])));
                            }
                            if (!yhjtj.equals("")) {
                                float fYhjtj = Float.parseFloat(yhjtj);
                                if (fYhjtj < Yhjtjdowm || fYhjtj > Yhjtjup) {
                                    mEtxyhjtj.setText(String.valueOf(Yhjtjdef));
                                }
                                saveOperationLogMsg(componetName, "设置的氧化剂体积_" + mEtxyhjtj.getText().toString(), ErrorLog.msgType.操作_信息);
                                updateConfigData(componetName, "OXIDANT_VOLUME", mEtxyhjtj.getText().toString());

                            } else {
                                Toast.makeText(getActivity(), context.getResources().getString(R.string.the_oxidizer_must_not_be_empty), Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.the_size_of_the_oxidizer_is_not_correct), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eThyjtj: {
                        String hyjtj = mEtxhyjtj.getText().toString().trim();
                        float hyjtjup = 5000;
                        float hyjtjdowm = 0;
                        float hyjtjdef = 0;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[9]) != null) {
                                hyjtjup = Float.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[9])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[9]) != null) {
                                hyjtjdowm = Float.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[9])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[9]) != null) {
                                hyjtjdef = Float.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[9])));
                            }
                            if (!hyjtj.equals("")) {
                                float fdata = Float.parseFloat(hyjtj);
                                if (fdata < hyjtjdowm || fdata > hyjtjup) {
                                    mEtxhyjtj.setText(String.valueOf(hyjtjdef));
                                }
                                saveOperationLogMsg(componetName, "设置的还原剂体积_" + mEtxhyjtj.getText().toString(), ErrorLog.msgType.操作_信息);
                                updateConfigData(componetName, "REDUCTANT_VOLUME", mEtxhyjtj.getText().toString());

                                if (QueryMeasCateg(componetName).equals("8") || QueryMeasCateg(componetName).equals("13")) {
                                    /*注射泵滴定需要下发给测控板**/
                                    byte[] hyjtjByte = floatToBytes(Float.valueOf(mEtxhyjtj.getText().toString()));
                                    SendManager.SendCmd(componetName + "_" + "设置还原剂体积_06_222", S0, 3, 100, hyjtjByte);
                                }

                            } else {
                                Toast.makeText(getActivity(), context.getResources().getString(R.string.reductant_volume_is_empty), Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.reductant_volume_is_set_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eThyjnd: {
                        String hyjnd = mEtxhyjnd.getText().toString().trim();
                        float hyjndup = 5000;
                        float hyjnddowm = 0;
                        float hyjnddef = 0;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[10]) != null) {
                                hyjndup = Float.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[10])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[10]) != null) {
                                hyjnddowm = Float.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[10])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[10]) != null) {
                                hyjnddef = Float.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[10])));
                            }
                            if (!hyjnd.equals("")) {
                                float fdata = Float.parseFloat(hyjnd);
                                if (fdata < hyjnddowm || fdata > hyjndup) {
                                    mEtxhyjnd.setText(String.valueOf(hyjnddef));
                                }
                                saveOperationLogMsg(componetName, "设置的还原剂浓度_" + mEtxhyjnd.getText().toString(), ErrorLog.msgType.操作_信息);
                                updateConfigData(componetName, "REDUCTANT_COC", mEtxhyjnd.getText().toString());


                            } else {
                                Toast.makeText(getActivity(), context.getResources().getString(R.string.reductant_coc_is_empty), Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.reductant_coc_is_set_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTddytj: {
                        String ddytj = mEtxddytj.getText().toString().trim();
                        float ddytjUp = 5000;
                        float ddytjDowm = 0;
                        float ddytjDef = 0;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[11]) != null) {
                                ddytjUp = Float.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[11])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[11]) != null) {
                                ddytjDowm = Float.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[11])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[11]) != null) {
                                ddytjDef = Float.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[11])));
                            }
                            if (!ddytj.equals("")) {
                                float fdata = Float.parseFloat(ddytj);
                                if (fdata < ddytjDowm || fdata > ddytjUp) {
                                    mEtxddytj.setText(String.valueOf(ddytjDef));
                                    fdata = ddytjDef;
                                }
                                saveOperationLogMsg(componetName, "设置的滴定液体积_" + mEtxddytj.getText().toString(), ErrorLog.msgType.操作_信息);
                                updateConfigData(componetName, "TITRATION_VOLUME", mEtxddytj.getText().toString());

                                byte[] ddytjByte = floatToBytes(fdata);
                                SendManager.SendCmd(componetName + "_" + "设置滴定液体积_06_138", S0, 3, 100, ddytjByte);
                            } else {
                                Toast.makeText(getActivity(), context.getResources().getString(R.string.titration_volume_is_empty), Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.titration_volume_is_set_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTchjtj: {
                        String chjtj = mEtxchjtj.getText().toString().trim();
                        float fUp = 5000;
                        float fDowm = 0;
                        float fDef = 0;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[15]) != null) {
                                fUp = Float.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[15])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[15]) != null) {
                                fDowm = Float.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[15])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[15]) != null) {
                                fDef = Float.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[15])));
                            }
                            if (!chjtj.equals("")) {
                                float fdata = Float.parseFloat(chjtj);
                                if (fdata < fDowm || fdata > fUp) {
                                    mEtxchjtj.setText(String.valueOf(fDef));
                                    fdata = fDef;
                                }
                                saveOperationLogMsg(componetName, "设置催化剂体积_" + mEtxchjtj.getText().toString(), ErrorLog.msgType.操作_信息);
                                updateConfigData(componetName, "catalystVolume", mEtxchjtj.getText().toString());

                                byte[] chjtjByte = floatToBytes(fdata);
                                SendManager.SendCmd(componetName + "_" + "设置催化剂体积_06_223", S0, 3, 100, chjtjByte);
                            } else {
                                Toast.makeText(getActivity(), context.getResources().getString(R.string.catalyst_volume_is_empty), Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.catalyst_volume_is_set_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTjytj: {
                        String jytj = mEtxjytj.getText().toString().trim();
                        float fUp = 5000;
                        float fDowm = 0;
                        float fDef = 0;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[20]) != null) {
                                fUp = Float.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[20])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[20]) != null) {
                                fDowm = Float.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[20])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[20]) != null) {
                                fDef = Float.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[20])));
                            }
                            if (!jytj.equals("")) {
                                float fdata = Float.parseFloat(jytj);
                                if (fdata < fDowm || fdata > fUp) {
                                    mEtxjytj.setText(String.valueOf(fDef));
                                    fdata = fDef;
                                }
                                saveOperationLogMsg(componetName, "设置碱液体积_" + mEtxjytj.getText().toString(), ErrorLog.msgType.操作_信息);
                                updateConfigData(componetName, "lyeVolume", mEtxjytj.getText().toString());

                                byte[] jytjByte = floatToBytes(fdata);
                                SendManager.SendCmd(componetName + "_" + "设置碱液体积_06_315", S0, 3, 100, jytjByte);
                            } else {
                                Toast.makeText(getActivity(), context.getResources().getString(R.string.catalyst_volume_is_empty), Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.catalyst_volume_is_set_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTBurnerTemperature: {
                        int iUp = 1000;
                        int iDown = 0;
                        int iDef = 680;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[18]) != null) {
                                iUp = Integer.parseInt(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[18])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[18]) != null) {
                                iDown = Integer.parseInt(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[18])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[18]) != null) {
                                iDef = Integer.parseInt(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[18])));
                            }
                            setCKAEditIntData(componetName, (EditText) v, 2, iDown, iUp, iDef, 283, "设置燃烧上升温度");
                            updateConfigData(componetName, "BurningDownTemperature", ((EditText) v).getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.burning_temperature_is_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTBurnerDownTemperature: {
                        int iUp = 1000;
                        int iDown = 0;
                        int iDef = 40;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[19]) != null) {
                                iUp = Integer.parseInt(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[19])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[19]) != null) {
                                iDown = Integer.parseInt(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[19])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[19]) != null) {
                                iDef = Integer.parseInt(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[19])));
                            }
                            setCKAEditIntData(componetName, (EditText) v, 2, iDown, iUp, iDef, 284, "设置下降温度");
                            updateConfigData(componetName, "BurningTemperature", ((EditText) v).getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[19] + context.getResources().getString(R.string.is_set_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTToxicityTestTime: {
                        int iUp = 30;
                        int iDown = 2;
                        int iDef = 15;
                        try {
                            if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[21]) != null) {
                                iUp = Integer.parseInt(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[21])));
                            }
                            if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[21]) != null) {
                                iDown = Integer.parseInt(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[21])));
                            }
                            if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[21]) != null) {
                                iDef = Integer.parseInt(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[21])));
                            }
                            setCKAEditIntData(componetName, (EditText) v, 2, iDown, iUp, iDef, 328, "设置毒性测试时间");
                            updateConfigData(componetName, "ToxicityTestTime", ((EditText) v).getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[21] + context.getResources().getString(R.string.is_set_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }


    /**
     * 消解温度
     */
    private class xjwdFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                String xjwd = mEtxjwd.getText().toString().trim();
                int xjwdUp = 200;
                int xjwdDown = 0;
                int xjwdDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[0]) != null) {
                        xjwdUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[0])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[0]) != null) {
                        xjwdDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[0])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[0]) != null) {
                        xjwdDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[0])));
                    }
                    if (!xjwd.equals("")) {
                        int xjwdInt = Integer.parseInt(xjwd);
                        if (xjwdInt < xjwdDown || xjwdInt > xjwdUp) {
                            mEtxjwd.setText(String.valueOf(xjwdDef));
                            xjwdInt = xjwdDef;
                        }
                        saveOperationLogMsg(componetName, "设置的消解温度_" + mEtxjwd.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "xjwd", mEtxjwd.getText().toString());
                        byte[] xjwdByte = copybyte(toByteArray(xjwdInt, 4));
                        SendManager.SendCmd(componetName + "_" + "设置消解温度_06_4", S0, 3, 100, xjwdByte);
                        //将设置记录保存到日志中
                        AddError(componetName, 643, mEtxjwd.getText().toString(), ErrorLog.msgType.运维_信息);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.digestion_temper_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.digestion_temper_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtxjwd.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }

    /**
     * 消解时长
     */
    private class xjscFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String xjsc = mEtxjsc.getText().toString().trim();
                int xjscUp = 30;
                int xjscDown = 0;
                int xjscDef = 55;

                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[1]) != null) {
                        xjscUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[1])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[1]) != null) {
                        xjscDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[1])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[1]) != null) {
                        xjscDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[1])));
                    }
                    if (!xjsc.equals("")) {
                        int xjscInt = Integer.parseInt(xjsc);
                        if (xjscInt < xjscDown || xjscInt > xjscUp) {
                            mEtxjsc.setText(String.valueOf(xjscDef));
                            xjscInt = xjscDef;
                        }
                        saveOperationLogMsg(componetName, "设置的消解时长_" + mEtxjsc.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "xjsc", mEtxjsc.getText().toString());

                        byte[] xjscByte = copybyte(toByteArray(xjscInt, 4));

                        SendManager.SendCmd(componetName + "_" + "设置消解时长_06_5", S0, 3, 100, xjscByte);

                        //将设置记录保存到日志中
                        AddError(componetName, 644, mEtxjsc.getText().toString(), ErrorLog.msgType.运维_信息);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.digestion_timer_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.digestion_timer_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtxjsc.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }

    /**
     * 显色温度
     */
    private class xswdFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String xswd = mEtxswd.getText().toString().trim();
                int xswdUp = 30;
                int xswdDown = 0;
                int xswdDef = 5;

                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[2]) != null) {
                        xswdUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[2])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[2]) != null) {
                        xswdDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[2])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[2]) != null) {
                        xswdDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[2])));
                    }
                    if (!xswd.equals("")) {
                        int xjscInt = Integer.parseInt(xswd);
                        if (xjscInt < xswdDown || xjscInt > xswdUp) {
                            mEtxswd.setText(String.valueOf(xswdDef));
                            xjscInt = xswdDef;
                        }
                        saveOperationLogMsg(componetName, "设置的显色温度_" + mEtxswd.getText().toString(), ErrorLog.msgType.操作_信息);

                        updateConfigData(componetName, "xswd", mEtxswd.getText().toString());

                        byte[] xswdByte = copybyte(toByteArray(xjscInt, 4));

                        SendManager.SendCmd(componetName + "_" + "设置显色温度_06_6", S0, 3, 100, xswdByte);

                        //将设置记录保存到日志中
                        AddError(componetName, 649, mEtxswd.getText().toString(), ErrorLog.msgType.运维_信息);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.color_temper_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.color_temper_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtxswd.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }

    /**
     * 显色时长
     */
    private class xsscFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String xssc = mEtxssc.getText().toString().trim();
                int xsscUp = 30;
                int xsscDown = 0;
                int xsscDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[3]) != null) {
                        xsscUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[3])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[3]) != null) {
                        xsscDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[3])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[3]) != null) {
                        xsscDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[3])));
                    }
                    if (!xssc.equals("")) {
                        int xsscInt = Integer.parseInt(xssc);
                        if (xsscInt < xsscDown || xsscInt > xsscUp) {

                            mEtxssc.setText(String.valueOf(xsscDef));
                            xsscInt = xsscDef;
                        }
                        saveOperationLogMsg(componetName, "设置的显色时长_" + mEtxssc.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "xssc", mEtxssc.getText().toString());

                        byte[] xsscByte = copybyte(toByteArray(xsscInt, 4));

                        SendManager.SendCmd(componetName + "_" + "设置显色时长_06_7", S0, 3, 100, xsscByte);


                        //将设置记录保存到日志中
                        AddError(componetName, 650, mEtxssc.getText().toString(), ErrorLog.msgType.运维_信息);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.color_timer_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.color_timer_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtxssc.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }


    /**
     * 消解降温
     */
    private class xiajwd1FocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String xjjw = mEtxiajwd1.getText().toString().trim();
                int xjjwUp = 30;
                int xjjwDown = 0;
                int xjjwDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[4]) != null) {
                        xjjwUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[4])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[4]) != null) {
                        xjjwDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[4])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[4]) != null) {
                        xjjwDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[4])));
                    }
                    if (!xjjw.equals("")) {
                        int xjwdInt = Integer.parseInt(xjjw);
                        if (xjwdInt < xjjwDown || xjwdInt > xjjwUp) {

                            mEtxiajwd1.setText(String.valueOf(xjjwDef));
                            xjwdInt = xjjwDef;
                        }
                        saveOperationLogMsg(componetName, "设置的消解降温_" + mEtxiajwd1.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "xjjw", mEtxiajwd1.getText().toString());

                        byte[] xsjwByte = copybyte(toByteArray(xjwdInt, 4));

                        SendManager.SendCmd(componetName + "_" + "设置消解降温_06_62", S0, 3, 100, xsjwByte);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.dispelling_cooling_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.dispelling_cooling_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtxiajwd1.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }


    /**
     * 显色降温
     */
    private class xiajwd2FocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String xsjw = mEtxiajwd2.getText().toString().trim();
                int xsjwUp = 30;
                int xsjwDown = 0;
                int xsjwDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[5]) != null) {
                        xsjwUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[5])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[5]) != null) {
                        xsjwDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[5])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[5]) != null) {
                        xsjwDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[5])));
                    }
                    if (!xsjw.equals("")) {
                        int xsjwInt = Integer.parseInt(xsjw);
                        if (xsjwInt < xsjwDown || xsjwInt > xsjwUp) {

                            mEtxiajwd2.setText(String.valueOf(xsjwDef));
                            xsjwInt = xsjwDef;
                        }
                        saveOperationLogMsg(componetName, "设置的显色降温_" + mEtxiajwd2.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "xsjw", mEtxiajwd2.getText().toString());

                        byte[] xsjwByte = copybyte(toByteArray(xsjwInt, 4));
                        SendManager.SendCmd(componetName + "_" + "设置显色降温_06_63", S0, 3, 100, xsjwByte);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.color_cooling_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.color_cooling_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtxiajwd2.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }


    /**
     * 显色静置
     */
    private class xsjzFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String xsjz = mEtxsjz.getText().toString().trim();
                int xsjzUp = 30;
                int xsjzDown = 0;
                int xsjzDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[6]) != null) {
                        xsjzUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[6])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[6]) != null) {
                        xsjzDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[6])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[6]) != null) {
                        xsjzDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[6])));
                    }
                    if (!xsjz.equals("")) {
                        int xsjzInt = Integer.parseInt(xsjz);
                        if (xsjzInt < xsjzDown || xsjzInt > xsjzUp) {

                            mEtxsjz.setText(String.valueOf(xsjzDef));
                            xsjzInt = xsjzDef;
                        }
                        saveOperationLogMsg(componetName, "设置的显色静置_" + mEtxsjz.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "xsjz", mEtxsjz.getText().toString());
                        syncList();
                        byte[] xsjzByte = copybyte(toByteArray(xsjzInt, 4));
                        SendManager.SendCmd(componetName + "_" + "设置显色静置_06_64", S0, 3, 200, xsjzByte);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.color_stand_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.color_stand_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtxsjz.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }


    /**
     * 等待静置
     */
    private class waitStandingFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String waitStanding = mEtWaitStanding.getText().toString().trim();
                int waitStandingUp = 30;
                int waitStandingDown = 0;
                int waitStandingDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[12]) != null) {
                        waitStandingUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[12])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[12]) != null) {
                        waitStandingDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[12])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[12]) != null) {
                        waitStandingDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[12])));
                    }
                    if (!waitStanding.equals("")) {
                        int iWaitStanding = Integer.parseInt(waitStanding);
                        if (iWaitStanding < waitStandingDown || iWaitStanding > waitStandingUp) {

                            mEtWaitStanding.setText(String.valueOf(waitStandingDef));
                            iWaitStanding = waitStandingDef;
                        }
                        saveOperationLogMsg(componetName, "设置的等待静置_" + mEtWaitStanding.getText().toString(), ErrorLog.msgType.操作_信息);

                        updateConfigData(componetName, "waitStanding", mEtWaitStanding.getText().toString());

                        byte[] waitStandByte = copybyte(toByteArray(iWaitStanding, 4));
                        SendManager.SendCmd(componetName + "_" + "设置等待静置_06_159", S0, 3, 200, waitStandByte);

                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.wait_stand_is_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.wait_stand_is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtWaitStanding.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }

    /**
     * 校准消解温度
     */
    private class calibrationTempFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                String strNum = eTCalibrationTemp.getText().toString().trim();
                int xjwdUp = 200;
                int xjwdDown = 0;
                int xjwdDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[13]) != null) {
                        xjwdUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[13])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[13]) != null) {
                        xjwdDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[13])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[13]) != null) {
                        xjwdDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[13])));
                    }
                    if (!strNum.equals("")) {
                        int xjwdInt = Integer.parseInt(strNum);
                        if (xjwdInt < xjwdDown || xjwdInt > xjwdUp) {
                            eTCalibrationTemp.setText(String.valueOf(xjwdDef));
                            xjwdInt = xjwdDef;
                        }
                        saveOperationLogMsg(componetName, "设置的校准消解温度_" + eTCalibrationTemp.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "calibrationTemp", eTCalibrationTemp.getText().toString());
                        byte[] xjwdByte = copybyte(toByteArray(xjwdInt, 4));
                        SendManager.SendCmd(componetName + "_" + "设置校准消解温度_06_217", S0, 3, 100, xjwdByte);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[13] + context.getResources().getString(R.string.is_set_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[13] + context.getResources().getString(R.string.is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                eTCalibrationTemp.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }


    /**
     * 校准消解时长
     */
    private class calibrationTimeFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                String strNum = eTCalibrationTime.getText().toString().trim();
                int xjwdUp = 30;
                int xjwdDown = 0;
                int xjwdDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[14]) != null) {
                        xjwdUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[14])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[14]) != null) {
                        xjwdDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[14])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[14]) != null) {
                        xjwdDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[14])));
                    }
                    if (!strNum.equals("")) {
                        int xjwdInt = Integer.parseInt(strNum);
                        if (xjwdInt < xjwdDown || xjwdInt > xjwdUp) {
                            eTCalibrationTime.setText(String.valueOf(xjwdDef));
                            xjwdInt = xjwdDef;
                        }
                        saveOperationLogMsg(componetName, "设置的校准消解时长_" + eTCalibrationTime.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "calibrationTime", eTCalibrationTime.getText().toString());
                        byte[] xjwdByte = copybyte(toByteArray(xjwdInt, 4));
                        SendManager.SendCmd(componetName + "_" + "设置校准消解时长_06_218", S0, 3, 100, xjwdByte);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[14] + context.getResources().getString(R.string.is_set_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[14] + context.getResources().getString(R.string.is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                eTCalibrationTime.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }


    /**
     * 消解阀值
     */
    private class xjyzFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String xjyz = mEtxjyz.getText().toString().trim();
                int xjyzUp = 30;
                int xjyzDown = 0;
                int xjyzDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[16]) != null) {
                        xjyzUp = Integer.valueOf(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[16])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[16]) != null) {
                        xjyzDown = Integer.valueOf(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[16])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[16]) != null) {
                        xjyzDef = Integer.valueOf(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[16])));
                    }
                    if (!xjyz.equals("")) {
                        int xjyzInt = Integer.parseInt(xjyz);
                        if (xjyzInt < xjyzDown || xjyzInt > xjyzUp) {

                            mEtxjyz.setText(String.valueOf(xjyzDef));
                            xjyzInt = xjyzDef;
                        }
                        saveOperationLogMsg(componetName, "设置的消解阈值_" + mEtxjyz.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "XJYZ", mEtxjyz.getText().toString());
                        syncList();
                        byte[] xjyzByte = copybyte(toByteArray(xjyzInt, 4));
                        SendManager.SendCmd(componetName + "_" + "设置消解阈值_06_248", S0, 3, 200, xjyzByte);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[16] + context.getResources().getString(R.string.is_set_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[16] + context.getResources().getString(R.string.is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtxjyz.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }


    /**
     * 平均时间
     */
    private class pjsjFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                String pjsj = mEtpjsj.getText().toString().trim();
                int pjsjUp = 30;
                int pjsjDown = 0;
                int pjsjDef = 0;
                try {
                    if (getParUpLimit(mt, lMeaPar, componetName, strMeaParName[17]) != null) {
                        pjsjUp = Integer.parseInt(Objects.requireNonNull(getParUpLimit(mt, lMeaPar, componetName, strMeaParName[17])));
                    }
                    if (getParDownLimit(mt, lMeaPar, componetName, strMeaParName[17]) != null) {
                        pjsjDown = Integer.parseInt(Objects.requireNonNull(getParDownLimit(mt, lMeaPar, componetName, strMeaParName[17])));
                    }
                    if (getParDefParValue(mt, lMeaPar, componetName, strMeaParName[17]) != null) {
                        pjsjDef = Integer.parseInt(Objects.requireNonNull(getParDefParValue(mt, lMeaPar, componetName, strMeaParName[17])));
                    }
                    if (!pjsj.equals("")) {
                        int pjsjInt = Integer.parseInt(pjsj);
                        if (pjsjInt < pjsjDown || pjsjInt > pjsjUp) {

                            mEtpjsj.setText(String.valueOf(pjsjDef));
                            pjsjInt = pjsjDef;
                        }
                        saveOperationLogMsg(componetName, "设置的平均时间_" + mEtpjsj.getText().toString(), ErrorLog.msgType.操作_信息);
                        updateConfigData(componetName, "PJSJ", mEtpjsj.getText().toString());
                        syncList();
                        byte[] pjsjByte = copybyte(toByteArray(pjsjInt, 4));
                        SendManager.SendCmd(componetName + "_" + "设置平均时间_06_249", S0, 3, 200, pjsjByte);
                    } else {
                        Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[17] + context.getResources().getString(R.string.is_set_empty), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), context.getResources().getString(R.string.set_meaPar) + strMeaParName[17] + context.getResources().getString(R.string.is_set_error), Toast.LENGTH_SHORT).show();
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mEtpjsj.clearFocus();
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }

    /**
     * 显示测量参数，关联配置文件
     */
    private void showMeaPar() {
        try {
            if (lMeaPar.size() > 0) {
                mt = lMeaPar.get(componetName);
                if (mt != null) {
                    for (int i = 0; i < mt.MeaParMap.size(); i++) {
                        MeaParCfg cfg = mt.Get(String.valueOf(i));
                        if (cfg.getName().equals(strMeaParName[0])) {
                            linearLayout1_1.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[1])) {
                            linearLayout1_2.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[2])) {
                            if (!getPublicConfigData("LogInName").equals("0") && !getPublicConfigData("LogInName").equals("1")) {
                                linearLayout1_3.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[3])) {
                            if (!getPublicConfigData("LogInName").equals("0") && !getPublicConfigData("LogInName").equals("1")) {
                                linearLayout1_4.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[4])) {
                            if (!getPublicConfigData("LogInName").equals("0") && !getPublicConfigData("LogInName").equals("1")) {
                                linearLayout1_5.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[5])) {
                            if (!getPublicConfigData("LogInName").equals("0") && !getPublicConfigData("LogInName").equals("1")) {
                                linearLayout1_6.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[6])) {
                            if (!getPublicConfigData("LogInName").equals("0") && !getPublicConfigData("LogInName").equals("1")) {
                                linearLayout1_7.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[7])) {
                            linearLayout2_1.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[8])) {
                            linearLayout2_2.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[9])) {
                            linearLayout2_3.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[10])) {
                            linearLayout2_4.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[11])) {
                            linearLayout2_5.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[12])) {
                            linearLayout1_8.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[13])) {
                            if (getPublicConfigData("LogInName").equals("3") || (getPublicConfigData("LogInName").equals("2"))) {
                                linearLayout1_9.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[14])) {
                            if (getPublicConfigData("LogInName").equals("3") || (getPublicConfigData("LogInName").equals("2"))) {
                                linearLayout1_10.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[15])) {
                            linearLayout2_6.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[16])) {
                            if (getPublicConfigData("LogInName").equals("3") || (getPublicConfigData("LogInName").equals("2"))) {
                                linearLayout1_11.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[17])) {
                            if (getPublicConfigData("LogInName").equals("3") || (getPublicConfigData("LogInName").equals("2"))) {
                                linearLayout1_12.setVisibility(View.VISIBLE);
                            }
                        } else if (cfg.getName().equals(strMeaParName[18])) {
                            linearLayout1_13.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[19])) {
                            linearLayout1_14.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[20])) {
                            linearLayout2_7.setVisibility(View.VISIBLE);
                        } else if (cfg.getName().equals(strMeaParName[21])) {
                            linearLayout1_15.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
    }


    /**
     * 控件响应使能
     *
     * @param enable 是否使能
     */
    private void setMeasureParameterTouch(Boolean enable) {

        mEtxjwd.setEnabled(enable);
        mEtxjsc.setEnabled(enable);
        mEtxswd.setEnabled(enable);
        mEtxssc.setEnabled(enable);
        mEtxiajwd1.setEnabled(enable);
        mEtxiajwd2.setEnabled(enable);
        mEtxsjz.setEnabled(enable);
        mEtWaitStanding.setEnabled(enable);
        eTCalibrationTemp.setEnabled(enable);
        eTCalibrationTime.setEnabled(enable);
        if (QueryMeasCateg(componetName).equals("3") || QueryMeasCateg(componetName).equals("8") || QueryMeasCateg(componetName).equals("13")) {
            mEtxsytj.setEnabled(enable);
            mEtxyhjtj.setEnabled(enable);
            mEtxhyjtj.setEnabled(enable);
            mEtxhyjnd.setEnabled(enable);
            mEtxddytj.setEnabled(enable);
            mEtxchjtj.setEnabled(enable);
            mEtxjytj.setEnabled(enable);
        }
        mEtxjyz.setEnabled(enable);
        mEtpjsj.setEnabled(enable);
        mEtBurnTemperature.setEnabled(enable);
        mEtBurnDownTemperature.setEnabled(enable);
    }

}
