package com.yzlm.cyl.cfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.x6.serialportlib.SerialPortMC;
import com.yzlm.cyl.cfragment.Adapter.FlowInfo;
import com.yzlm.cyl.cfragment.Adapter.SurplusFileCfg;
import com.yzlm.cyl.cfragment.Adapter.SurplusFileInfo;
import com.yzlm.cyl.cfragment.AppFunction.GetRunFlowsInfoRunnable;
import com.yzlm.cyl.cfragment.Cal.Component.CalcEnRevise;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtuTable;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu.RtuDataStruct;
import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.Action;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionStep;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.CmdTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.IOBoardRegAddr;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.RegAddr;
import com.yzlm.cyl.cfragment.Communication.Component.AppError.AppError;
import com.yzlm.cyl.cfragment.Communication.Component.AppError.AppErrorCfg;
import com.yzlm.cyl.cfragment.Communication.Component.AppError.ErrorTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowStep;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.Component.Cmd.ProtocolResponse;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalc;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalcTable;
import com.yzlm.cyl.cfragment.Communication.SerialPort.WeiQianPort;
import com.yzlm.cyl.cfragment.Communication.SerialPort.YangChuang.YangChuangPort;
import com.yzlm.cyl.cfragment.Communication.Server.ServerPort;
import com.yzlm.cyl.cfragment.Communication.Server.ServerReceiveDataRunnable;
import com.yzlm.cyl.cfragment.Communication.TCP.TcpPort;
import com.yzlm.cyl.cfragment.Communication.Thread.ReceiveDataRunnable;
import com.yzlm.cyl.cfragment.Communication.Thread.ReceiveTcpDataRunnable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Component;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.AuthFuncParam;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.CKCfg;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.CombinationParameter;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.DataCoeK;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.DataCorrectParam;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.DoSample;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.FactorySetPar;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.Info;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.JLBCfg;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.MeasureParameter;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.OtherParameter;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.RobotPar;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.SpecPlatParam;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.SurplusCfg;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardComponent;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardErrorLog;
import com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg.NoBoardDataCoeK;
import com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg.NoBoardDataCorrectParam;
import com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg.NoBoardDoSample;
import com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg.NoBoardFactorySetPar;
import com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg.NoBoardInfo;
import com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg.NoBoardOtherParameter;
import com.yzlm.cyl.cfragment.Config.Public.CommPublic;
import com.yzlm.cyl.cfragment.Config.Public.SampleSystem;
import com.yzlm.cyl.cfragment.Config.Public.SystemPar;
import com.yzlm.cyl.cfragment.Content.Component.Config.MeaParCfg;
import com.yzlm.cyl.cfragment.Content.Component.Config.MeaParTable;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_FlowConfig;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_FunctionTest;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Display.Display;
import com.yzlm.cyl.cfragment.Thread.AnalogQuanlity_Cycle;
import com.yzlm.cyl.cfragment.Thread.AuthFuncThread;
import com.yzlm.cyl.cfragment.Thread.AutoDoCombination_Cycle;
import com.yzlm.cyl.cfragment.Thread.AutoDoSample_cycle;
import com.yzlm.cyl.cfragment.Thread.AutoDoSample_topHour;
import com.yzlm.cyl.cfragment.Thread.DigitalAnalogInQuanlity_Cycle;
import com.yzlm.cyl.cfragment.Thread.ProtocolCfgUpdate;
import com.yzlm.cyl.cfragment.Thread.ProtocolUpdateChecking;
import com.yzlm.cyl.cfragment.Thread.RealtimeStatus;
import com.yzlm.cyl.cfragment.Thread.RebootApp;
import com.yzlm.cyl.cfragment.Thread.RunningCheckThread;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yzlm.cyl.cfragment.AppFunction.DeleteSDApkPackage.removeMyApkFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GuiZhou.GuiZhou.syncListGuiZhou;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getTimeHaveSec;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.syncListModbusRtu2018;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu2.ModbusRtu2.syncListModbusRtu2;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.protocolFunc.getProtocolShowName;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.sichuan.siChuanModbusRtu.syncListSiChuanModbusRtu;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.其他_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.报错_警告;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.报错_错误;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运行_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.MeaModeSetting;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3.setUseRange;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content2.commTestHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pcldy.setBsLedStatus;
import static com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1_2.getIOBORD;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getFileConfigDir;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getMeasBordByModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getNoBoardFileConfigDir;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.WinWidgetHandler;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.phoneName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.sendSysTimeToDev;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;
import static com.yzlm.cyl.clibrary.Util.SDCardUtils.getStoragePath;
import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

/*
 * Created by caoyiliang on 2016/12/29.
 */

@SuppressWarnings("unchecked")
public class Global {
    public static String SdcardPath;
    public static String extSdcardPath;
    public static Context context;
    public static Activity activity;
    public static Communication S0;/*测控板    8 9     485*/
    public static Communication S1;/*接口板    3 4 5   232*/   // COM3
    public static Communication S2;/*通讯口    11 12   485*/   // COM1
    public static Communication S3;/*  DTU     6 7 10  232*/

    // 三路TCP数据传输
    public static Communication TCP1;
    public static Communication TCP2;
    public static Communication TCP3;

    // TCPServer数据接收
    public static Communication TCPServer1;
    public static Communication TCPServer2;

    public static RealtimeStatus RealTimeStatusThread;
    public static AnalogQuanlity_Cycle AnalogQuantity_CycleThread;
    public static Map<String, Integer> LogSize = new HashMap();
    private static List<String[]> lComponent = new ArrayList<>();   // 只有常规组分
    private static List<String[]> lAll5721Component = new ArrayList<>();    //包含模块（稀释、蒸馏）
    private static List<String[]> lNoBoardComponent = new ArrayList<>();   // 5722中无背板的监测因子组分
    private static Map<String, CmdTable> Cmds = new HashMap();
    private static Map<String, FlowTable> Flows = new HashMap();
    private static Map<String, List<String>> FlowsKey = new HashMap();//Flows的Key的按顺序的列表
    private static Map<String, ActionTable> Actions = new HashMap();
    private static Map<String, List<String>> Valve = new HashMap<>();
    private static Map<String, Map<Integer, ActionStep>> FunctionalTest = new HashMap<>();
    private static Map<String, CalcTable> lCalc = new HashMap();
    private static Map<String, NBCalcTable> lNoBoardCalc = new HashMap();
    public static Map<String, MeaParTable> lMeaPar = new HashMap();
    /*     * 解析文件中，寄存器表(ModbudRtu)     */
    public static Map<String, ModbusRtuTable> lRtuCmdData = new HashMap();
    /*     * 解析文件中，寄存器表(贵州协议)     */
    public static Map<String, ModbusRtuTable> lGuiZhouCmdData = new HashMap();
    /*能量修正计算**/
    private static Map<String, CalcEnRevise> lCalcEn = new HashMap();
    /*多光谱平台各组分能量信息**/
    private static Map<String, String> lCalcSelectEn = new HashMap();
    /*     * 解析文件中，寄存器表(ModbudRtu2018)     */
    public static Map<String, ModbusRtuTable> lRtu2018CmdData = new HashMap();

    /*     * 解析文件中，系统寄存器表     */
    public static Map<String, ModbusRtuTable> lRtu2018SysCmdData = new HashMap();

    /*     * 解析文件中，寄存器表(ModbudRtu2)     */
    public static Map<String, ModbusRtuTable> lRtu2CmdData = new HashMap();

    /*     * 将寄存器数据防止对应组份中(ModbudRtu2018)     */
    public static Map<String, Map<Integer, RtuDataStruct>> lRtu2018CompRegMap = new HashMap();

    /*     * 将寄存器数据防止对应组份中(ModbudRtu2)     */
    public static Map<String, Map<Integer, RtuDataStruct>> lRtu2CompRegMap = new HashMap();

    /*     * 将寄存器数据防止对应组份中(贵州协议)     */
    public static Map<String, Map<Integer, RtuDataStruct>> lGuiZhouCompRegMap = new HashMap();

    public static Map<String, ModbusRtuTable> lRtu2018FiveParCmdData = new HashMap();
    /*
     * 解析文件中，寄存器表(四川协议)
     */
    public static Map<String, ModbusRtuTable> lSiChuanRtuCmdData = new HashMap();
    /* * 将寄存器数据防止对应组份中(四川协议)     */
    public static Map<String, Map<Integer, RtuDataStruct>> lSiChuanRtuCompRegMap = new HashMap();
    /*协议列表(WL)*/
    public static String[] protocolList;
    /*服务端协议列表（WL）*/
    public static String[] serverProtocolList;
    /*错误列表(WL)*/
    private static Map<String, ErrorTable> lError = new HashMap();
    /*错误列表(WL)*/
    private static Map<String, ErrorTable> lNoBoardError = new HashMap();
    /*组分名中英文转换列表（WL）*/
    private static Map<String, String> lCompNameConvert = new HashMap<>();
    /*发生异常报错时是否继续下发自动做样*/
    public static Map<String, String> AutoSampleEnable = new HashMap<>();
    /*进入界面的工作状态显示*/
    public static Map<String, String> workState = new HashMap<>();
    /*进入界面的工作状态显示*/
    public static Map<String, String> noBoardWorkState = new HashMap<>();
    // 最近一次所有组份的报警信息
    public static Map<String, String> allAlarmInfo = new HashMap<>();
    //当前状态
    public static Map<String, String> doFlowing = new HashMap<>();
    //上一个状态
    public static Map<String, String> oldDoFlowing = new HashMap<>();
    //四川协议，状态或参数 变化标志
    public static Map<String, String> siChuanProtocolStatusParIsChange = new HashMap<>();
    /*通道1的4-20mA输入测试值*/
    public static float channel1Val;
    /*通道2的4-20mA输入测试值*/
    public static float channel2Val;
    /*数字量测试*/
    public static boolean digitalTest = false;
    /*模拟量输入更新标志位**/
    public static boolean channelValUpdateFlag = false;
    /*4路开关量输入值*/
    public static byte[] digitalChannelVal = new byte[4];
    //当前组份为开采样时候置位,当开采样后下一个动作之后触发进行接口板输入1开关量监测
    public static Map<String, String> sampleDoingFlag = new HashMap<>();
    //当前做样动作
    public static Map<String, String> doFlowWorking = new HashMap<>();

    private static Map<String, AutoDoSample_cycle> Timer_cycle = new HashMap<>();
    private static Map<String, String[]> Timer_topHour = new HashMap<>();
    /*有背板组分*/
    public static List<String[]> strComponent = new ArrayList<>();
    /*5721界面所以参数，组分，蒸馏（稀释）及无背板*/
    public static List<String[]> strAll5721Component = new ArrayList<>();
    /*无背板参数*/
    public static List<String[]> strNoBoardComponent = new ArrayList<>();
    /*手动停止标记*/
    public static Map<String, String> manualStopFlag = new HashMap<>();


    /*试剂瓶名称记录*/
    public static Map<String, List<String>> reagentBottle = new HashMap<>();


    /*与协议配置参数有关联，名称不要变*/
    public static final String[] protocolName = {"GB2005", "GB2017", "Modbus_Rtu", "江苏南水", "ModbusRtu2018", "江苏协议", "河北协议", "GB2017V2", "河南协议", "国家地表水协议", "Modbus_Rtu2", "贵州协议", "宁夏协议", "深圳协议", "W300", "四川协议"/*15*/};
    /*协议名称转换（）*/
    public static Map<String, String> protocolDisplayName = new HashMap<>();
    /*协议显示名称*/
    public static String protocolDisplayNameOrder;

    /*协议名称转换（）*/
    public static String[] protocolDisplayList;

    /*密码解锁一次有效事件*/
    public static String passwordUnlockEffectiveWork = "";

    /*试剂余量参数*/
    public static Map<String, SurplusFileCfg> lsurPlus = new HashMap<>();
    //////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    /*     * 是否使用接口板     */
    public static boolean IOBoardUsed = true;

    //////////////////////////////////////////////////////////////////
    /*     * 增加组份后不可以点击下导航栏     */
    public static boolean addComponentLockDisplayShowFlag = false;

    /*     * VOC的测量组份     */
    public static Map<String, String[]> lComponentVoc = new HashMap<>();

    //进入密码界面时候锁定下导航栏和侧导航栏
    public static boolean LockDisplayShowFlag = false;

    // 锁定背板的左侧控件及主界面按钮
    public static boolean LockModuleDisplayShowFlag = false;

    /*息屏状态*/
    public static boolean isOffScreenLight = false;

    /*熄屏功能关闭*/
    public static boolean isOffScreenLightSwitch = false;

    /*******************************************************************************************/

    /*测量能量吸光度提前显示标志位*/
    public static Map<String, String> meaAbsorbEnergyShowFlag = new HashMap<>();

    /*测量能量吸光度提前显示*/
    public static Map<String, String> meaAbsorbEnergy = new HashMap<>();

    /*************************************************************************/

    /*
     * 认证功能标志 2018年11月3日15:22:42
     */
    public static boolean blAuthenticationFunction = false;

    // 测控板看门狗标志位
    public static boolean blWatchDogFeedFlagForCK = true;

    // 接口板看门狗标志位
    public static boolean blWatchDogFeedFlagForIO = true;

    /*是否升级测控板标志*/
    public static boolean blUpdateCKBFlag = false;

    // 流收发数据存储
    public static List<FlowInfo> mListDevice = new ArrayList<>();  //声明一个list，动态存储要显示的信息
    public static boolean flowReceiveFlag = false;  // 流程下发通信

    public static int versionID = 0;// 上下版本同步ID，针对功能需要下板子支持

    public static String str557View = "";

    /*之类发送异常状态*/
    public static Map<String, String> cmdStatus = new HashMap<>();

    public static Map<String, String> historyCalcFlag = new HashMap<>();

    /*******************************************************************************************/

    /*光电检测板*/
    public static String GDJCB = "00";
    /*机械驱动板*/
    public static String JXQDB = "03";
    /*温度反应模块*/
    public static String WDFYMK = "05";

    //////////////////////////////////////////////////////////////////////////////////////////
    private static void syncListComponent() {
        Component component = Component.newInstance(context);
        lComponent = component.getComponent();
    }

    private static List<String[]> getListComponent() {
        syncListComponent();
        strComponent = lComponent;
        return lComponent;
    }

    private static void syncAll5721Component() {
        Component component = Component.newInstance(context);
        lAll5721Component = component.getAll5721Component();
    }

    public static List<String[]> getListAll5721Component() {

        syncAll5721Component();
        strAll5721Component = lAll5721Component;
        return lAll5721Component;
    }

    private static void syncListNoBoardComponent() {
        NoBoardComponent component = NoBoardComponent.newInstance(context);
        lNoBoardComponent = component.getComponent();
    }

    private static List<String[]> getListNoBoardComponent() {
        syncListNoBoardComponent();
        strNoBoardComponent = lNoBoardComponent;
        return lNoBoardComponent;
    }

    /*
     * 获取当前的组分的组分地址值(WL)
     */
    public static String QueryCompAddr(String compName) {
        String curCompAddres = "";

        String[] compNames = strAll5721Component.get(1);
        String[] compAddrs = strAll5721Component.get(3);

        for (int i = 0; i < compNames.length; i++) {
            if (compNames[i].equals(compName)) {
                curCompAddres = compAddrs[i];
            }
        }
        return curCompAddres;
    }

    public static void InitComponent() {
        // 有外置SD,用外置SD；
        // 无外置SD，判断有无内置SD；
        // 有内置SD,用内置SD；
        // 无内置SD,程序退出；
        try {
            //初始化串口
            switch (phoneName) {
                case "rk3288":
                    SdcardPath = (getStoragePath(context, true).size() == 0 ? null : (getStoragePath(context, true).get(0) + File.separator));
                    extSdcardPath = (getStoragePath(context, true).size() == 0 ? SdcardPath : (getStoragePath(context, true).get(1) + File.separator));
                    break;
                default:
                    SdcardPath = (getStoragePath(context, false).size() == 0 ? null : (getStoragePath(context, false).get(0) + File.separator));
                    extSdcardPath = (getStoragePath(context, true).size() == 0 ? SdcardPath : (getStoragePath(context, true).get(0) + File.separator));
                    break;
            }
            /*SdcardPath = (getStoragePath(context, false).size() == 0 ? null : (getStoragePath(context, false).get(0) + File.separator));
            extSdcardPath = (getStoragePath(context, true).size() == 0 ? SdcardPath : (getStoragePath(context, true).get(0) + File.separator));*/

            if (extSdcardPath == null || extSdcardPath.equals(SdcardPath)) {
                //            main.removeDestopText(mfb);
                //            Dialog_Err st = new Dialog_Err();
                //            Bundle bundle = new Bundle();
                //            bundle.putString("alert-err", "无外置SD卡，将在内部存储满后停止工作！");
                //            st.setArguments(bundle);
                //            st.show(fm, "Dialog_err");
                //return false;
            }
        } catch (Exception e) {
            Log.i("exception", "getStoragePath");
        }

        /*获取常规背板组份*/
        getListComponent();
        /*获取所有5721内参数*/
        getListAll5721Component();
        /*获取5722内无背板监测因子*/
        getListNoBoardComponent();
    }

    public static boolean InitGlobal() {

        InitSyncTable();

        //初始化串口
        InitPort(GetPhoneType());

        // 初始化TCP口
        InitPort(Communication.Port.TCP);

        //  初始化TCPServer服务
        InitPort(Communication.Port.TCPServer);

        new AutoDoSample_topHour(activity, false);

        //实时刷新
        RealTimeStatusThread = new RealtimeStatus("RealtimeStatus", false);
        //实时4-20mA输入输出刷新
        AnalogQuantity_CycleThread = new AnalogQuanlity_Cycle("AnalogQuanlity_Cycle", false);
        //接口板开关量输入,模拟量输入刷新
        new DigitalAnalogInQuanlity_Cycle("DigitalAnalogInQuanlity_Cycle", false);

        //初始化测控板寄存器起始地址列表
        RegAddr.initRegAddrList();
        //初始化接口板寄存器起始地址列表
        IOBoardRegAddr.initBoardRegAddrList();

        new ProtocolUpdateChecking("protocolUpdateChecking", false);
        new RunningCheckThread("runningCheckThread", false);
        new AutoDoCombination_Cycle("AutoDoCombination_Cycle", false);
        new ProtocolCfgUpdate("ProtocolCfgUpdate", false);
        new AuthFuncThread("AuthFuncThread", false);
        //定时重启软件
        new RebootApp("RebootApp", false);
        //同步自动做样循环定时器表
        syncAutoDoSample_cycle();
        //同步自动做样整点触发表
        syncAutoDoSample_topHour();

        setAllCompBSLEDStatus(null);

        saveRunInfo2File("启动应用");

        return true;
    }

    public static void setAllCompBSLEDStatus(String compName) {
        // 光比比色光源开关
        if (compName == null) {
            for (String item : strAll5721Component.get(1)) {
                setBsLedStatus(item, getConfigData(item, "BS_LED").equals("open"));
            }
        } else {
            setBsLedStatus(compName, getConfigData(compName, "BS_LED").equals("open"));
        }
    }

    private static void InitSyncTable() {
        /*比较配置文件的MD5码*/
        CompareMD5();
        manualStopFlag.clear();
        historyCalcFlag.clear();
        /*获取常规背板组份*/
        getListComponent();
        /*获取所有5721内参数*/
        getListAll5721Component();
        /*获取5722内无背板监测因子*/
        getListNoBoardComponent();

        workState.clear();
        allAlarmInfo.clear();
        doFlowing.clear();
        meaAbsorbEnergyShowFlag.clear();
        meaAbsorbEnergy.clear();
        String[] compNameUsed = lAll5721Component.get(1);
        for (String aCompNameUsed : compNameUsed) {
            workState.put(aCompNameUsed, context.getString(R.string.normal));
            allAlarmInfo.put(aCompNameUsed, context.getString(R.string.normal));
            doFlowing.put(aCompNameUsed, context.getResources().getString(R.string.waiting_for_instructions));
            oldDoFlowing.put(aCompNameUsed, context.getResources().getString(R.string.waiting_for_instructions));
            meaAbsorbEnergyShowFlag.put(aCompNameUsed, "false");
            meaAbsorbEnergy.put(aCompNameUsed, "");
            sampleDoingFlag.put(aCompNameUsed, "false");
            doFlowWorking.put(aCompNameUsed, "0");
            historyCalcFlag.put(aCompNameUsed, "");
        }

        /*同步数据系数K参数*/
        syncListDataCoeKParam();
        //同步仪表信息
        syncListInfo();
        //同步其他信息
        syncListOtherParameter();
        //同步机械手信息
        syncListRobotPar();
        // 同步组合测试参数
        syncListCombinationParameter();
        //同步测量参数
        syncListMeasureParameter();
        //同步0505界面光谱界面的参数(WL)
        syncListSpecPlatParam();
        //同步参数(WL)
        syncListDataCorrParam();
        //同步做样设置
        syncListDoSample();
        //同步命令表
        syncListCmds();
        // 同步采样系统
        syncSampleSystem();
        //同步公共通讯表
        syncListCommPublic();
        // 同步公共参数
        syncListParPublic();
        /*对异常存储记录清空，能启动自动做样*/
        syncAutoJobIsEnable();
        //认证功能
        syncListAuthFuncParam();

        //////////////无背板参数
        //其他参数
        syncListNoBoardOtherParameter();
        //302 信息
        syncListNoBoardInfo();
        // 用户参数，管理员参数
        syncNoBoardListDataCorrParam();

        syncNoBoardListDoSample();

        syncNoBoardListDataCoeParam();
        ///////////////////////////////文件
        //同步流程表
        syncListFlow();
        //同步动作表
        syncListAction();
        //同步阀号表
        syncListValve();
        //同步算法表
        syncListCalc();
        //同步算法表2
        syncListCalcEn();
        //同步组分名中英文转换
        syncListCompNameConvert();
        //同步错误表
        syncListError();
        //同步报错屏蔽
        syncListErrorShield();

        // 手动停止状态清除
        cleanAllStopFlag();
        // 测量参数文件
        syncListMeaParCfg();
        // 余量配置文件
        syncFileSurplusCfg();
        // 余量
        syncListSurplusCfg();
        // VOC组份配置文件
        syncFileVocComponentCfg();

        //////////////
        syncNoBoardEnergysSelect();

        syncNoBoardListCalc();

        syncNoBoardListError();

        //////////////////////

        //计量板配置
        syncListJLBCfg();
        //CK板配置
        syncListCKCfg();
        /*熄*/
        //HardwareControl.SetBackLight(false);
        globalSetting();
        //配置协议文件
        syncListProtocolCfg();
        //解析协议配置中显示名称文件
        syncListProtocolDisplayNameCfg();
        //协议显示名称配置
        protocolShowNameCfg();
        //删除包含 Water 或者 app 或者 jiagu_sign 的.apk文件
        removeMyApkFile();
    }

    /*协议显示名称配置*/
    private static void protocolShowNameCfg() {
        try {
            getProtocolShowName();
        } catch (Exception e) {
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle bundle = new Bundle();
            bundle.putString("alert-err", context.getString(R.string.protocol_name_file_error) + "！");
            st.setArguments(bundle);
            st.setTargetFragment(Display.newInstance(), 1);
            st.show(fm, "Dialog_err");
        }
    }

    private static void globalSetting() {
        int blIoBordEn = getIOBORD();
        if (blIoBordEn == 0) {
            IOBoardUsed = false;
        } else {
            IOBoardUsed = true;
        }
        //IOBoardUsed = getPublicConfigData("IO_BORD").equals("true");
        for (String item : lComponent.get(1)) {
            int iMeaMode = Integer.parseInt(getConfigData(item, "meaMode")) - 1;
            // 1.13.4版本当时模式5选1，后来1.15版本 4选1
            if (iMeaMode >= 4) {
                //只能选择1~4（4/5二选一）
                iMeaMode = 3;
                saveOperationLogDataModifyMsg(item, "meaMode", String.valueOf(iMeaMode), "测量模式自动切换", ErrorLog.msgType.操作_信息);
                updateConfigData(item, "meaMode", String.valueOf(iMeaMode));
                MeaModeSetting(item, String.valueOf(iMeaMode));
            }
        }
        for (String item : lComponent.get(1)) {
            if (getConfigData(item, "CAL_DATA_UPDATE").equals("true")) {
                saveOperationLogPublicDataModifyMsg("公共", "CAL_DATA_UPDATE", "true", "系统校准数据上传 ", ErrorLog.msgType.操作_信息);
                updatePublicConfigData("CAL_DATA_UPDATE", "true");
                updateConfigData(item, "CAL_DATA_UPDATE", "false");
            }
            if (getConfigData(item, "ABSORB_UPDATE").equals("true")) {
                saveOperationLogPublicDataModifyMsg("公共", "ABSORB_UPDATE", "true", "系统吸光度上传 ", ErrorLog.msgType.操作_信息);
                updatePublicConfigData("ABSORB_UPDATE", "true");
                updateConfigData(item, "ABSORB_UPDATE", "false");
            }
        }

        //取消F值功能，所以默认关闭在校准中校准失败功能
        for (String item : lComponent.get(1)) {
            updateConfigData(item, "calibrationFlag", "false");
        }
        for (String item : strNoBoardComponent.get(1)) {
            updateNoBoardConfigData(item, "calibrationFlag", "false");
        }
    }

    private static void syncListJLBCfg() {

        for (String item : lComponent.get(1)) {
            JLBCfg jlbCfg = new JLBCfg(context, item);
        }
    }

    private static void syncListCKCfg() {

        for (String item : lAll5721Component.get(1)) {
            new CKCfg(context, item);
        }
    }

    private static void syncListSurplusCfg() {

        for (String item : lComponent.get(1)) {
            SurplusCfg surplusCfg = new SurplusCfg(context, item);
        }
    }


    private static void cleanAllStopFlag() {

        /*将手动停止清空*/
        for (String item : strComponent.get(1)) {
            manualStopFlag.put(item, "false");
        }
    }

    /*背板测量组分Handler*/
    @SuppressLint("HandlerLeak")
    public static Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.obj != null && !msg.obj.equals("")) {
                    switch (msgType.values()[msg.what]) {
                        case 通讯_信息://发送信息
                        {
                            String[] msgstrs = ((String) msg.obj).split(";");
                            if (!(msgstrs[0].contains(context.getString(R.string.drainageTest)) || msgstrs[0].contains(context.getString(R.string.infusionTest)))) {
                                if (msgstrs[0].contains(context.getString(R.string.single_operation))) {
                                    main.removeDestopText(mfb);
                                    Dialog_FunctionTest st = new Dialog_FunctionTest();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("alert-FunctionTest", msgstrs[1].split("_")[0] + "_" + msgstrs[1].split("_")[1]);
                                    st.setArguments(bundle);
                                    st.setTargetFragment(Display.newInstance(), 1);
                                    st.show(fm, "Dialog_FunctionTest");
                                }
                            }
                            ErrorLog log = new ErrorLog(context);
                            //log.Add(msgstrs[0], ErrorLog.msgType.values()[msg.what]);
                        }
                        break;
                        case 报错_警告:
                        case 报错_错误:
                            try {
                                String[] msgStr = ((String) msg.obj).split("_");
                                allAlarmInfo.put(msgStr[0], msgStr[1] + "--" + msgStr[2]);
                            } catch (Exception e) {
                                Log.e("allAlarmInfo", e.toString());
                            }
                        case 报错_信息:
                        case 运行_信息:
                        case 登录_信息:
                        case 运维_信息:
                        case 其他_信息:
                            alarmMsgDoing(msg);
                            break;
                        case 操作_信息:
                            DevLog log = new DevLog(context);
                            String[] msgCzStr = ((String) msg.obj).split("_");
                            log.Add(msgCzStr[0], msgCzStr[1], ErrorLog.msgType.values()[msg.what]);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                Log.i("except", e.toString());
            }
        }
    };

    /**
     * 报错、运行信息、记录
     *
     * @param msg 消息
     */
    private static void alarmMsgDoing(Message msg) {
        ErrorLog error = new ErrorLog(context);
        String[] msgStr = ((String) msg.obj).split("_");
        try {

            /*将蒸馏背板的报错放入到主背板*/
            setMeasBordErrorFormModuleError(msgStr[0]);
        } catch (Exception e) {
            //Log.e("allAlarmInfo", e.toString());
        }

        error.Add(msgStr[0], msgStr[1], msgStr[2], msgType.values()[msg.what], "");
        Log.i("sun", msgStr[2] + "");

        //510:上电流路校验失败  511:流路未曾下发      报错后主动下发流路
        if (msgStr[1].equals("510") || msgStr[1].equals("511")) {
            List4_Content3_FlowConfig.newInstance().ErrSetAllClick(msgStr[0]);
        }
        if (!QueryMeasCateg(msgStr[0]).equals("5") && !QueryMeasCateg(msgStr[0]).equals("6")) {
            if (msgStr[1].equals("105")) {
                updateConfigData(msgStr[0], "zdzyTimer", "0");
            }
        }
        if (msgType.values()[msg.what] == 报错_错误) {
            try {
                String[] msgStr1 = ((String) msg.obj).split("_");
                if (getConfigData(msgStr1[0], "AUTO_RANGE_SWITCH_RECOVERY").equals("true")) {
                    //切回主量程l
                    setUseRange(msgStr1[0], getConfigData(msgStr1[0], "AUTORANGE_MAIN_RANGE"));
                    /*量程进行一次切换*/
                    AddError(msgStr1[0], 529, msgType.运维_信息);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             报错之后，蒸馏稀释背板和测量背板处理*/
            errorToModuleMeasBordDoing(msgStr[0]);

            //报错之后
            //如果当前标1启动标记则标1失败
            if (getConfigData(msgStr[0], "CAL_Start_Flag").equals("true")) {
                updateConfigData(msgStr[0], "CAL_Start_Flag", "false");
                updateConfigData(msgStr[0], "CAL_Suc_Flag", "false");
            }
            //如果当前标2启动标记则标2失败
            if (getConfigData(msgStr[0], "CAL2_Start_Flag").equals("true")) {
                updateConfigData(msgStr[0], "CAL_Start_Flag", "false");
                updateConfigData(msgStr[0], "CAL2_Start_Flag", "false");
                updateConfigData(msgStr[0], "CAL2_Suc_Flag", "false");
            }
        }
    }

    private static void errorToModuleMeasBordDoing(String compName) {
        String strComponentModule;
        if (isHaveMeasCategory(compName, "5")) {
            // 有稀释
            strComponentModule = compName + getModuleName("5");
            ComponentErrorModuleStop(compName, strComponentModule);
        }
        if (isHaveMeasCategory(compName, "6")) {
            //有蒸馏
            strComponentModule = compName + getModuleName("6");
            ComponentErrorModuleStop(compName, strComponentModule);
        }
        //蒸馏模块报错，启动背板异常处理
        if (QueryMeasCateg(compName).equals("6")) {
            ComponentModuleErrorComponentStop(getMeasBordByModuleName(compName, "6"), compName);
        }
    }


    /**
     * 将蒸馏背板的报错放入到主背板 报错中
     *
     * @param compName 组分名称
     */
    public static void setMeasBordErrorFormModuleError(String compName) {

        // 将蒸馏背板的报错放入到主背板 报错中
        if (QueryMeasCateg(compName).equals("6")) {
            String measBoard = getMeasBordByModuleName(compName, "6");
            if (!context.getString(R.string.normal).equals(Objects.requireNonNull(allAlarmInfo.get(compName)))) {
                allAlarmInfo.put(measBoard, Objects.requireNonNull(allAlarmInfo.get(compName)));
            }
            if (!context.getString(R.string.normal).equals(Objects.requireNonNull(workState.get(compName)))) {
                workState.put(measBoard, Objects.requireNonNull(workState.get(compName)));
            }
        }

    }


    @SuppressLint("HandlerLeak")
    public static Handler mNoBoardHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.obj != null && !msg.obj.equals("")) {
                    switch (msgType.values()[msg.what]) {
                        case 通讯_信息://发送信息
                        {
                            String[] msgstrs = ((String) msg.obj).split(";");
                            NoBoardErrorLog log = new NoBoardErrorLog(context);
                            //log.Add(msgstrs[0], NoBoardErrorLog.msgType.values()[msg.what]);
                        }
                        break;
                        case 报错_信息:
                        case 报错_警告:
                        case 报错_错误:
                        case 运行_信息:
                            NoBoardErrorLog error = new NoBoardErrorLog(context);
                            String[] msgStr = ((String) msg.obj).split("_");
                            error.Add(msgStr[0], msgStr[1], msgStr[2], NoBoardErrorLog.msgType.values()[msg.what], "");
                            break;
                        case 操作_信息:
                            NoBoardDevLog log = new NoBoardDevLog(context);
                            String[] msgCzStr = ((String) msg.obj).split("_");
                            log.Add(msgCzStr[0], msgCzStr[1], NoBoardErrorLog.msgType.values()[msg.what]);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                Log.i("except", e.toString());
            }
        }
    };


    /**
     * 报错出数
     *
     * @param logType 消息类型
     * @param error
     * @param msgStr
     */
    private static void errorOutPutData(ErrorLog.msgType logType, final ErrorLog error, final String[] msgStr) {
        final ErrorLog.msgType fLogType;
        fLogType = logType;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 报错
                    if (fLogType == 报错_错误) {
                        try {
                            sleep(12000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        error.Add(msgStr[0], msgStr[1], msgStr[2], fLogType, "");

                    } else {
                        error.Add(msgStr[0], msgStr[1], msgStr[2], fLogType, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /*
     * copName 测量背板名称
     * strComponentModule 模块背板名称
     * **/
    private static void ComponentErrorModuleStop(String copName, String strComponentModule) {
        if (strComponentModule != null) {
            if (!doFlowing.get(strComponentModule).equals(context.getString(R.string.waiting_for_instructions))) {
                SendManager.SendCmd(strComponentModule + "_紧急停止" + "_8_07", S0, 3, 1100, null);
                saveRunInfo2File("组分[" + copName + "]" + "报错停止模块背板 ");

                final String fCompModule;
                fCompModule = strComponentModule;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(6000);

                            List<String> flowsModule = new ArrayList<>();
                            flowsModule.add(context.getString(R.string.exceptionHandling));
                            runComponentModule(fCompModule, flowsModule);
                            saveRunInfo2File("组分[" + flowsModule + "]" + "模块背板 启动异常处理");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }


    /**
     * 稀释背板/蒸馏背板 异常时候停止测量背板
     *
     * @param copName       组分名称
     * @param copModuleName 模块背板名称
     */
    private static void ComponentModuleErrorComponentStop(String copName, String copModuleName) {
        if (copName != null) {
            if (!doFlowing.get(copName).equals(context.getString(R.string.waiting_for_instructions))) {
                stopWorking(copName, false);
                saveRunInfo2File("组分[" + copModuleName + "]" + "报错停止背板 ");

                final String fComponent;
                final String fComponentModule;
                fComponent = copName;
                fComponentModule = copModuleName;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(6000);
                            doControlJob(fComponent, context.getString(R.string.exceptionHandling));
                            saveRunInfo2File("组分[" + fComponentModule + "]" + "报错" + " 启动背板异常处理");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

    }


    public static void InitPort(Communication.Port Port) {
        boolean blIoBordEnable = false;
        int blIoBordEn = getIOBORD();
        if (blIoBordEn == 0) {
            blIoBordEnable = false;
        } else {
            blIoBordEnable = true;
        }
        //boolean blIoBordEnable = getPublicConfigData("IO_BORD").equals("true");


        //初始化服务端可选协议列表
        List<String> serverProtocol = new ArrayList<>();
        if (serverProtocolList == null) {
            for (int i = 0; i < protocolDisplayList.length; i++) {
                //服务端使用ModbusRTU2 和 ModbusRTU2018
                if (protocolDisplayList[i].contains(protocolName[4]) || protocolDisplayList[i].contains(protocolName[10])) {
                    serverProtocol.add(protocolDisplayList[i]);
                }
            }
            serverProtocolList = serverProtocol.toArray(new String[0]);
        }

        switch (Port) {
            case WeiQianPort: {
                if (S0 == null) {
                    S0 = new WeiQianPort("COM2", 115200, 8, 1, "None");
                }
                if (S1 == null) {
                    if (blIoBordEnable) {
                        // 接口板使用固定设置
                        S1 = new WeiQianPort("COM3", 115200, 8, 1, "None");
                    } else {
                        // 不接接口板则用于对外协议传输
                        int com3Baud = Integer.parseInt(getPublicConfigData("COM3BAUD"));
                        com3Baud = getComBaud(com3Baud);
                        S1 = new WeiQianPort("COM3", com3Baud, 8, 1, "None");
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread com1PortReceiveThread = new Thread(new ReceiveDataRunnable(S1));
                            com1PortReceiveThread.start();
                        }
                    });
                    thread.start();
                }
                if (S2 == null) {
                    int com1Baud = Integer.parseInt(getPublicConfigData("COM1BAUD"));
                    com1Baud = getComBaud(com1Baud);
                    S2 = new WeiQianPort("COM1", com1Baud, 8, 1, "None");
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread com1PortReceiveThread = new Thread(new ReceiveDataRunnable(S2));
                            com1PortReceiveThread.start();
                        }
                    });
                    thread.start();
                }
                if (S3 == null) {
                    S3 = new WeiQianPort("COM4", 9600, 8, 1, "None");
                    /*COM4 介入  外部设备   DTU*/
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread s3PortReceiveThread = new Thread(new ReceiveDataRunnable(S3));
                            s3PortReceiveThread.start();
                        }
                    });
                    thread.start();
                }
            }
            break;
            case YangChuangPort: {
                if (S0 == null) {
                    S0 = new YangChuangPort("dev/ttyS0", 115200, 8, 0, 1);
                }
                if (S1 == null) {
                    if (blIoBordEnable) {
                        // 接口板使用固定设置
                        S1 = new YangChuangPort("dev/ttyS3", 115200, 8, 0, 1);
                    } else {
                        // 不接接口板则用于对外协议传输
                        int com3Baud = Integer.parseInt(getPublicConfigData("COM3BAUD"));
                        com3Baud = getComBaud(com3Baud);
                        S1 = new YangChuangPort("dev/ttyS3", com3Baud, 8, 0, 1);
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread com1PortReceiveThread = new Thread(new ReceiveDataRunnable(S1));
                            com1PortReceiveThread.start();
                        }
                    });
                    thread.start();
                }
                if (S2 == null) {
                    int com1Baud = Integer.parseInt(getPublicConfigData("COM1BAUD"));
                    com1Baud = getComBaud(com1Baud);
                    S2 = new YangChuangPort("dev/ttyS1", com1Baud, 8, 0, 1);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread com1PortReceiveThread = new Thread(new ReceiveDataRunnable(S2));
                            com1PortReceiveThread.start();
                        }
                    });
                    thread.start();
                }
                if (S3 == null) {
                    S3 = new YangChuangPort("dev/ttyS4", 9600, 8, 0, 1);
                    /*COM4 介入  外部设备   DTU*/
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread s3PortReceiveThread = new Thread(new ReceiveDataRunnable(S3));
                            s3PortReceiveThread.start();
                        }
                    });
                    thread.start();
                }
            }
            break;
            case SerialPortMC: {
                if (S0 == null) {
                    S0 = new SerialPortMC("S3", 115200, 8, 1, 110);
                }
                if (S1 == null) {
                    if (blIoBordEnable) {
                        // 接口板使用固定设置
                        S1 = new SerialPortMC("S1", 115200, 8, 1, 110);
                    } else {
                        // 不接接口板则用于对外协议传输
                        int com3Baud = Integer.parseInt(getPublicConfigData("COM3BAUD"));
                        com3Baud = getComBaud(com3Baud);
                        S1 = new SerialPortMC("S1", com3Baud, 8, 1, 110);
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread com1PortReceiveThread = new Thread(new ReceiveDataRunnable(S1));
                            com1PortReceiveThread.start();
                        }
                    });
                    thread.start();

                }
                if (S2 == null) {
                    int com1Baud = Integer.parseInt(getPublicConfigData("COM1BAUD"));
                    com1Baud = getComBaud(com1Baud);
                    S2 = new SerialPortMC("S0", com1Baud, 8, 1, 110);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread com1PortReceiveThread = new Thread(new ReceiveDataRunnable(S2));
                            com1PortReceiveThread.start();
                        }
                    });
                    thread.start();
                }
                if (S3 == null) {
                    S3 = new SerialPortMC("S2", 9600, 8, 1, 110);
                    /*COM4 介入  外部设备   DTU*/
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Thread s3PortReceiveThread = new Thread(new ReceiveDataRunnable(S3));
                            s3PortReceiveThread.start();
                        }
                    });
                    thread.start();
                }
            }
            break;
            case TCP: {
                if (getPublicConfigData("TCP_1").equals("true")) {
                    if (TCP1 == null) {
                        try {
                            TCP1 = new TcpPort(getPublicConfigData("TCP_1_IP"), Integer.parseInt(getPublicConfigData("TCP_1_PORT")));
                            Thread TCP1ReceiveThread = new Thread(new ReceiveTcpDataRunnable(TCP1, 10, new ReceiveTcpDataRunnable.Callback() {
                                @Override
                                public void receiveParse(Communication port, byte[] tempData) {
                                    try {
                                        if (digitalTest) {
                                            Message msg = new Message();
                                            msg.what = 5;
                                            Bundle bundleData = new Bundle();
                                            bundleData.putByteArray("IOReceiveData", tempData);
                                            msg.setData(bundleData);
                                            commTestHandler.sendMessage(msg);
                                        }
                                    } catch (Exception e) {
                                        Log.e("exception", e.toString());
                                    }
                                    ProtocolResponse protocolResponse = new ProtocolResponse();
                                    protocolResponse.ParsingProtocol(port, 0, protocolList[Integer.parseInt(getPublicConfigData("TCP_1_Protocol"))], tempData);
                                }
                            }));
                            TCP1ReceiveThread.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    if (TCP1 != null) {
                        TCP1.close();
                        TCP1 = null;
                    }
                }
                if (getPublicConfigData("TCP_2").equals("true")) {
                    if (TCP2 == null) {
                        try {
                            TCP2 = new TcpPort(getPublicConfigData("TCP_2_IP"), Integer.parseInt(getPublicConfigData("TCP_2_PORT")));
                            Thread TCP2ReceiveThread = new Thread(new ReceiveTcpDataRunnable(TCP2, 10, new ReceiveTcpDataRunnable.Callback() {
                                @Override
                                public void receiveParse(Communication port, byte[] tempData) {
                                    try {
                                        if (digitalTest) {
                                            Message msg = new Message();
                                            msg.what = 6;
                                            Bundle bundleData = new Bundle();
                                            bundleData.putByteArray("IOReceiveData", tempData);
                                            msg.setData(bundleData);
                                            commTestHandler.sendMessage(msg);
                                        }
                                    } catch (Exception e) {
                                        Log.e("exception", e.toString());
                                    }
                                    ProtocolResponse protocolResponse = new ProtocolResponse();
                                    protocolResponse.ParsingProtocol(port, 0, protocolList[Integer.parseInt(getPublicConfigData("TCP_2_Protocol"))], tempData);
                                }
                            }));
                            TCP2ReceiveThread.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    if (TCP2 != null) {
                        TCP2.close();
                        TCP2 = null;
                    }
                }
                if (getPublicConfigData("TCP_3").equals("true")) {
                    if (TCP3 == null) {
                        try {
                            TCP3 = new TcpPort(getPublicConfigData("TCP_3_IP"), Integer.parseInt(getPublicConfigData("TCP_3_PORT")));
                            Thread TCP3ReceiveThread = new Thread(new ReceiveTcpDataRunnable(TCP3, 10, new ReceiveTcpDataRunnable.Callback() {
                                @Override
                                public void receiveParse(Communication port, byte[] tempData) {
                                    try {
                                        if (digitalTest) {
                                            Message msg = new Message();
                                            msg.what = 7;
                                            Bundle bundleData = new Bundle();
                                            bundleData.putByteArray("IOReceiveData", tempData);
                                            msg.setData(bundleData);
                                            commTestHandler.sendMessage(msg);
                                        }
                                    } catch (Exception e) {
                                        Log.e("exception", e.toString());
                                    }
                                    ProtocolResponse protocolResponse = new ProtocolResponse();
                                    protocolResponse.ParsingProtocol(port, 0, protocolList[Integer.parseInt(getPublicConfigData("TCP_3_Protocol"))], tempData);
                                }
                            }));
                            TCP3ReceiveThread.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    if (TCP3 != null) {
                        TCP3.close();
                        TCP3 = null;
                    }
                }
            }
            break;
            case TCPServer: {
                //  开启TCPServer
                if (getPublicConfigData("Server_1").equals("true")) {
                    if (TCPServer1 == null) {

                        if (!TextUtils.isEmpty(getPublicConfigData("Server_1_IP"))
                                && !TextUtils.isEmpty(getPublicConfigData("Server_1_PORT"))) {

                            TCPServer1 = new ServerPort(Integer.parseInt(getPublicConfigData("Server_1_PORT")));
                            Thread Server1 = new Thread(new ServerReceiveDataRunnable(TCPServer1, 10, new ServerReceiveDataRunnable.Callback() {
                                @Override
                                public void receiveParse(Communication serverPort, byte[] tempData) {
                                    Log.i("Server Receive", "接收到" + bytesToHexString(tempData, tempData.length));

                                    ProtocolResponse protocolResponse = new ProtocolResponse();
                                    protocolResponse.ParsingProtocol(serverPort, 0, serverProtocolList[Integer.parseInt(getPublicConfigData("Server_1_Protocol"))], tempData);
                                }

                                @Override
                                public void sendData(Communication port, byte[] data) {
                                    TCPServer1.sendData(data);
                                }

                            }));

                            Server1.start();
                        }
                    }
                } else {
                    if (TCPServer1 != null) {
                        TCPServer1 = null;
                    }
                }

                if (getPublicConfigData("Server_2").equals("true")) {
                    if (TCPServer2 == null) {

                        if (!TextUtils.isEmpty(getPublicConfigData("Server_2_IP"))
                                && !TextUtils.isEmpty(getPublicConfigData("Server_2_PORT"))) {
                            TCPServer2 = new ServerPort(Integer.parseInt(getPublicConfigData("Server_2_PORT")));
                            Thread Server2 = new Thread(new ServerReceiveDataRunnable(TCPServer2, 10, new ServerReceiveDataRunnable.Callback() {
                                @Override
                                public void receiveParse(Communication serverPort, byte[] tempData) {
                                    ProtocolResponse protocolResponse = new ProtocolResponse();
                                    protocolResponse.ParsingProtocol(serverPort, 0, serverProtocolList[Integer.parseInt(getPublicConfigData("Server_2_Protocol"))], tempData);
                                }

                                @Override
                                public void sendData(Communication serverPort, byte[] data) {
                                    TCPServer2.sendData(data);
                                }
                            }));
                            Server2.start();
                        }
                    }

                } else {
                    if (TCPServer2 != null) {
                        TCPServer2 = null;
                    }
                }
            }
            break;
        }
    }

    private static int getComBaud(int com1Baud) {
        String[] strBaud = context.getResources().getStringArray(R.array.baud);
        try {
            if (com1Baud < strBaud.length) {
                com1Baud = Integer.parseInt(strBaud[com1Baud].replace("bps", ""));
            } else {
                com1Baud = 115200;
            }
        } catch (Exception e) {
            com1Baud = 115200;
            e.printStackTrace();
        }
        return com1Baud;
    }

    /*同步数据系数K参数*/
    private static void syncListDataCoeKParam() {
        for (String item : lComponent.get(1)) {
            new DataCoeK(context, item);
        }
    }

    /**
     * 同步 测量值相关系数
     */
    private static void syncNoBoardListDataCoeParam() {

        for (String item : lNoBoardComponent.get(1)) {
            new NoBoardDataCoeK(context, item);
        }
    }

    private static void syncListNoBoardInfo() {

        for (String item : lNoBoardComponent.get(1)) {
            new NoBoardInfo(context, item);
        }
    }

    private static void syncListInfo() {

        for (String item : lAll5721Component.get(1)) {
            new Info(context, item);
        }
    }

    private static void syncListNoBoardOtherParameter() {

        for (String item : lNoBoardComponent.get(1)) {
            new NoBoardOtherParameter(context, item);
        }
    }


    private static void syncListOtherParameter() {

        for (String item : lAll5721Component.get(1)) {
            new OtherParameter(context, item);
        }
    }

    private static void syncListRobotPar() {

        for (String item : lAll5721Component.get(1)) {
            new RobotPar(context, item);
        }
    }


    private static void syncListCombinationParameter() {
        for (String item : lComponent.get(1)) {
            new CombinationParameter(context, item);
        }
    }


    private static void syncListMeasureParameter() {
        for (String item : lAll5721Component.get(1)) {
            new MeasureParameter(context, item);
        }
    }


    /*同步参数(WL)*/
    private static void syncListDataCorrParam() {
        for (String item : lComponent.get(1)) {
            new DataCorrectParam(context, item);
        }
    }

    /*同步数据系数的参数(WL)*/
    private static void syncNoBoardListDataCorrParam() {
        for (String item : lNoBoardComponent.get(1)) {
            new NoBoardDataCorrectParam(context, item);
        }
    }


    /*同步0505界面光谱界面的参数(WL)*/
    private static void syncListSpecPlatParam() {
        for (String item : lComponent.get(1)) {
            new SpecPlatParam(context, item);
        }
    }


    private static void syncListDoSample() {
        for (String item : lComponent.get(1)) {
            new DoSample(context, item);
        }
    }

    private static void syncNoBoardListDoSample() {
        for (String item : lNoBoardComponent.get(1)) {
            new NoBoardDoSample(context, item);
        }
    }


    private static void syncListAuthFuncParam() {
        for (String item : lComponent.get(1)) {
            new AuthFuncParam(context, item);
        }
    }

    /*采样系统*/
    private static void syncSampleSystem() {
        new SampleSystem(context);
    }

    private static void syncListCmds() {
        for (String item : lAll5721Component.get(1)) {
            CmdTable cmd = new CmdTable();
            Cmds.put(item, cmd);
        }
    }

    public static CmdTable getCmds(String Component) {
        return Cmds.get(Component);
    }

    public static String getCmdsValue(String Component, int index) {
        return getCmds(Component).getCmd(index).getValue() == null ? "" : getCmds(Component).getCmd(index).getValue().toString();
    }


    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (File file1 : files) {
            s.add(file1.getAbsolutePath());
        }
        return s;
    }

    private static Map<String, String> GetMD5() {
        try {
            String compNameDir = null;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.error_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return null;
                } else {
                    compNameDir = extSdcardPath;
                }
            } else {
                compNameDir = SdcardPath;
            }

            compNameDir += "Csoft/";
            File file = new File(compNameDir);
            Map<String, String> md5MP = getDirMD5(file, true);
            return md5MP;
        } catch (Exception ex) {
            //saveExcepInfo2File("计算MD5码发生异常 ：" + ex.toString());
            Log.i("计算MD5码发生异常", ex.toString());
        }
        return null;
    }

    public static void SetMD5() {
        try {
            Map<String, String> md5MP = GetMD5();
            String info = "";
            Iterator<String> iter = md5MP.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                String value = md5MP.get(key);
                if (info.equals("")) {
                    info += (key + "&" + value);
                } else {
                    info += ("#" + key + "&" + value);
                }
            }
            updatePublicConfigData("MD5", info);
        } catch (Exception ex) {
        }
    }

    private static void CompareMD5() {
        try {
            Map<String, String> md5MP = GetMD5();
            String info = "";
            Iterator<String> iter = md5MP.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                String value = md5MP.get(key);
                if (info.equals("")) {
                    info += (key + "&" + value);
                } else {
                    info += ("#" + key + "&" + value);
                }
            }
            //updatePublicConfigData("MD5", info);
            String configMd5 = getPublicConfigData("MD5");
            Map<String, String> md5ConfigMP = new HashMap<>();
            if (!configMd5.equals("")) {
                String[] strConfigMD5 = configMd5.split("#");
                for (String str : strConfigMD5) {
                    String[] strKeyAndeVal = str.split("&");
                    md5ConfigMP.put(strKeyAndeVal[0], strKeyAndeVal[1]);
                }
                String strAdd = "";
                String strDel = "";
                String strDifferent = "";
                Iterator<String> iterC = md5ConfigMP.keySet().iterator();
                while (iterC.hasNext()) {
                    String key = iterC.next();
                    String value = md5ConfigMP.get(key);
                    String strinfo = ",  " + key.substring(key.lastIndexOf("/"));
                    if (md5MP.containsKey(key)) {
                        if (!md5MP.get(key).equals(value)) {
                            strDifferent += strinfo;
                        }
                    } else {
                        strDel += strinfo;
                    }
                }
                Iterator<String> iterC2 = md5MP.keySet().iterator();
                while (iterC2.hasNext()) {
                    String key = iterC2.next();
                    if (!key.contains("/Curve/")) {
                        String strinfo = ",  " + key.substring(key.lastIndexOf("/"));
                        if (!md5ConfigMP.containsKey(key)) {
                            strAdd += strinfo;
                        }
                    }
                }
                String strErrInfo = "";
                if (!strAdd.equals("")) {
                    strErrInfo += (context.getString(R.string.files_add) + strAdd + "\n");
                }
                if (!strDel.equals("")) {
                    strErrInfo += (context.getString(R.string.files_delete) + strDel + "\n");
                }
                if (!strDifferent.equals("")) {
                    strErrInfo += (context.getString(R.string.files_modify) + strDifferent + "\n");
                }
                if (!strErrInfo.equals("")) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", strErrInfo + context.getString(R.string.files_changes));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                }
            } else {
                updatePublicConfigData("MD5", info);
            }
        } catch (Exception ex) {
            Log.i("比较MD5码发生异常", ex.toString());
        }
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取单个文件的MD5值
     *
     * @param file  文件
     * @param radix 位 16 32 64
     * @return
     */
    public static String getFileMD5s(File file, int radix) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(radix);
    }

    /**
     *   * 获取文件夹中文件的MD5值
     *   * 
     *   * @param file
     *   * @param listChild
     *   *            ;true递归子目录中的文件
     *   * @return
     *  
     */
    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        String md5;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirMD5(f, listChild));
            } else {
                if (f.isFile() && f.getAbsolutePath().endsWith(".txt")) {
                    md5 = getFileMD5(f);
                    if (md5 != null) {
                        map.put(f.getPath(), md5);
                    }
                }
            }
        }
        return map;
    }

    public static String[] getFileNames(String filepath) {
        try {

            File file = new File(filepath);
            if (file.isDirectory()) {
                String[] filelist = file.list();
                return filelist;
            }

        } catch (Exception e) {
        }
        return null;
    }

    private static void syncListFlow() {
        for (String item : lAll5721Component.get(1)) {
            String flowDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.flow_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    flowDir = extSdcardPath;
                }
            } else {
                flowDir = SdcardPath;
            }
            String sDir = getFileConfigDir(item, 0);
            if (sDir == null) {
                continue;
            } else {
                flowDir += sDir;
            }

            List<String> list = getFilesAllName(flowDir);
            String fileName = "flow_" + item;
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String flowPath = selectFilePath;

            File dbFile = new File(flowPath);
            if (dbFile.exists()) {
                String strFlow = readString(flowPath, "GBK");
                String[] strsFlow = strFlow.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
                FlowTable ft = new FlowTable();
                FlowClass flow = new FlowClass();
                List<String> listKeys = new ArrayList<>();
                for (String strItem : strsFlow) {
                    if (strItem.contains("\n")) {
                        strItem.replace("\n", "");
                    }

                    if (!strItem.equals("")) {
                        try {
                            if (strItem.contains("//")) {
                                /*向某一流程增加一个流程步骤*/
                                FlowStep fs = new FlowStep();
                                String[] cmdTemp = strItem.split("[,，]");
                                fs.setCode(cmdTemp[0]);
                                fs.setSampleCount(Integer.parseInt(cmdTemp[1]));
                                fs.setMeasurement(Integer.parseInt(cmdTemp[2]));
                                fs.setActionCoding(Integer.parseInt(cmdTemp[3]));
                                fs.setStep(Integer.parseInt(cmdTemp[4].replace("//", "")));
                                flow.Add(Integer.parseInt(cmdTemp[4].replace("//", "")), fs);
                            } else {
                                String[] cmdTemp = strItem.split("_");
                                flow = new FlowClass();
                                flow.setName(cmdTemp[1]);
                                if (strItem.contains("&")) {
                                    flow.setCombinedCommand(cmdTemp[0]);
                                    flow.setCmd((byte) 254);
                                } else {
                                    flow.setCmd((byte) Integer.parseInt(cmdTemp[0]));
                                }
                                if (cmdTemp.length > 2) {
                                    flow.setRange(Integer.parseInt(cmdTemp[2].replace(context.getString(R.string.range), "")));// 同步
                                }
                                ft.Add(flow);
                                listKeys.add(flow.getName() + "_" + flow.getRange());
                            }
                        } catch (Exception ex) {
                            main.removeDestopText(mfb);
                            Dialog_Err st = new Dialog_Err();
                            Bundle bundle = new Bundle();
                            bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.flow_cmd_failed) + "[" + strItem + "]！");
                            st.setArguments(bundle);
                            st.setTargetFragment(Display.newInstance(), 1);
                            st.show(fm, "Dialog_err");
                        }
                    }
                }
                Flows.put(item, ft);
                FlowsKey.put(item, listKeys);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.flow_file_lost) + "！");
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
            }
        }
    }

    public static FlowTable getFlows(String Component) {
        return Flows.get(Component);
    }

    public static List<String> getFlowsKey(String Component) {
        return FlowsKey.get(Component);
    }

    private static void syncListAction() {
        for (String item : lAll5721Component.get(1)) {
            String actionDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.action_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    actionDir = extSdcardPath;
                }
            } else {
                actionDir = SdcardPath;
            }
            String sDir = getFileConfigDir(item, 1);
            if (sDir == null) {
                continue;
            } else {
                actionDir += sDir;
            }

            List<String> list = getFilesAllName(actionDir);
            String fileName = "action_" + item;
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String actionPath = selectFilePath;

            File dbFile = new File(actionPath);
            if (dbFile.exists()) {
                String strAction = readString(actionPath, "GBK");
                String[] strsAction = strAction.replace("\t", "")./*replace(" ", "").*/replace("\r\n", "").replace("\n", "").split("#");
                ActionTable at = new ActionTable();
                Map<Integer, ActionStep> fcmap = new HashMap<>();
                for (String items : strsAction) {
                    try {
                        String[] AC = items.split("[,，]");
                        Action action = new Action();
                        action.setName(AC[3]);
                        try {
                            action.setCmd(Short.parseShort(AC[0].replace(" ", "")));
                        } catch (Exception e) {
                            action.setCmd((short) Integer.parseInt(AC[0].replace(" ", "")));
                        }
                        action.setCode(AC[1].replace(" ", ""));
                        action.setDescription(AC[2].replace(" ", ""));
                        at.Add(action.getName(), action);
                        if (items.contains("//")) {
                            ActionStep as = new ActionStep();
                            as.setName(AC[3]);
                            as.setSampleCount((byte) Integer.parseInt(items.replace(" ", "").split("//")[1].split("[;；]")[1].split("[,，]")[0]));
                            as.setMeasurement((byte) Integer.parseInt(items.replace(" ", "").split("//")[1].split("[;；]")[1].split("[,，]")[1]));
                            String[] param = items.replace(" ", "").split("//")[1].split("[;；]")[1].split("[,，]");
                            if (param.length > 2) {
                                as.setWindow(items.replace(" ", "").split("//")[1].split("[;；]")[1].split("[,，]")[2]);
                            }
                            fcmap.put(Integer.parseInt(items.replace(" ", "").split("//")[1].split("[;；]")[0]), as);
                        }
                    } catch (Exception ex) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.action_cmd_failed) + "[" + items + "]！");
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                    }
                }
                Actions.put(item, at);
                FunctionalTest.put(item, fcmap);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "] " + context.getString(R.string.action_file_lost) + "！");
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
            }
        }

    }

    public static ActionTable getActions(String Component) {
        return Actions.get(Component);
    }

    public static Map<Integer, ActionStep> getFunctionalTest(String Component) {
        return FunctionalTest.get(Component);
    }

    public static ActionStep getActionStep(Map<Integer, ActionStep> ft, String name) {
        Iterator iter = ft.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            ActionStep ac = (ActionStep) entry.getValue();
            if (ac.getName().equals(name))
                return ac;
        }
        return null;
    }

    private static void syncAutoJobIsEnable() {
        if (lComponent == null)
            syncListComponent();
        for (String item : lComponent.get(1)) {
            AutoSampleEnable.put(item, "true");
        }
    }

    private static void syncAutoDoSample_cycle() {
        ClearTimer_cycle();
        for (String item : lComponent.get(1)) {
            AutoDoSample_cycle autoSend = new AutoDoSample_cycle(item, "as_" + item, new boolean[]{true, true, true});
            Timer_cycle.put(item, autoSend);
            if (getConfigData(item, "LXCL").equals("true")) {
                String H = getConfigData(item, "lxclh");
                String M = getConfigData(item, "lxclm");
                if (getConfigData(item, "meaMode").equals("1")) {
                    H = "0";
                    M = "1";
                }
                getTimer_cycle(item).setTime((int) ((Float.parseFloat((H.equals("") ? "0" : H)) * 60 + Float.parseFloat((M.equals("") ? "0" : M)))), 0);
                getTimer_cycle(item).resume(0);
            }
            if (getConfigData(item, "ZDJZ").equals("true")) {
                String calibIntervalDay = getConfigData(item, "zdjzt");
                String calibStartHour = getConfigData(item, "zdjzh");
                getTimer_cycle(item).setTime((int) (Float.parseFloat(calibIntervalDay.equals("") ? "0" : calibIntervalDay)), 1);
                getTimer_cycle(item).setTime((int) (Float.parseFloat(calibStartHour.equals("") ? "0" : calibStartHour)), 2);
                getTimer_cycle(item).setCalibDate(Calendar.getInstance());
                getTimer_cycle(item).resume(1);
            }
            if (getConfigData(item, "ZDQX").equals("true")) {
                String washIntervalCount = getConfigData(item, "zdqxh");
                getTimer_cycle(item).setTime((int) (Float.parseFloat(washIntervalCount.equals("") ? "0" : washIntervalCount)), 3);
                getTimer_cycle(item).resume(2);
            }
        }
    }

    private static void ClearTimer_cycle() {
        if (Timer_cycle != null) {
            Iterator iter = Timer_cycle.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                AutoDoSample_cycle ads = (AutoDoSample_cycle) entry.getValue();
                ads.suspend(0);
                ads.suspend(1);
                ads.suspend(2);
            }
            Timer_cycle.clear();
        }
    }

    public static AutoDoSample_cycle getTimer_cycle(String Component) {
        return Timer_cycle.get(Component);
    }

    public static void syncAutoDoSample_topHour() {
        syncListComponent();
        Timer_topHour.clear();
        for (String item : lComponent.get(1)) {
            if (getConfigData(item, "ZQCL").equals("true")) {
                String[] Hs = getConfigData(item, "zqclh").split("，");
                Timer_topHour.put(item, Hs);
            }
        }
    }

    /*同步302出厂设置参数*/
    public static void syncListFactoryParam() {
        syncListComponent();
        for (String item : lAll5721Component.get(1)) {
            FactorySetPar factorySetPar = new FactorySetPar(context, item);
        }
    }

    /*同步无背板参数302出厂设置参数*/
    public static void syncNoBoardListFactoryParam() {
        syncListNoBoardComponent();
        for (String item : lNoBoardComponent.get(1)) {
            NoBoardFactorySetPar factorySetPar = new NoBoardFactorySetPar(context, item);
        }
    }


    public static void clearAutoDoSample_topHour(String comp) {
        Timer_topHour.remove(comp);
    }

    public static Map<String, String[]> getTimer_topHour() {
        return Timer_topHour;
    }

    private static void syncListValve() {
        for (String item : lAll5721Component.get(1)) {
            String actionDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.valve_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    actionDir = extSdcardPath;
                }
            } else {
                actionDir = SdcardPath;
            }
            String sDir = getFileConfigDir(item, 2);
            if (sDir == null) {
                continue;
            } else {
                actionDir += sDir;
            }

            List<String> list = getFilesAllName(actionDir);
            String fileName = "valve_" + item;
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String actionPath = selectFilePath;

            File dbFile = new File(actionPath);
            if (dbFile.exists()) {
                String strValve = readString(actionPath, "GBK");
                String[] strsValve = strValve.replace("\t", "")./*replace(" ", "").*/replace("\r\n", "").replace("\n", "").split("#");
                List<String> temp = new ArrayList<>();
                List<String> bolate = new ArrayList<>();
                for (String items : strsValve) {

                    /*获取主界面显示试剂瓶名称*/
                    String[] strBolate = items.split("//")[0].split("[,，]");
                    if (strBolate.length > 1) {
                        bolate.add(strBolate[0] + "，" + strBolate[1].replace(" ", ""));
                    }
                    temp.add(strBolate[0]);
                }
                Valve.put(item, temp);
                reagentBottle.put(item, bolate);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.valve_file_lost) + " ！");
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
            }
        }
    }

    public static List<String> getValve(String Component) {
        return Valve.get(Component);
    }

    private static void syncListCalc() {

        for (String item : lAll5721Component.get(1)) {
            String actionDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.calculate_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    actionDir = extSdcardPath;
                }
            } else {
                actionDir = SdcardPath;
            }

            String sDir = getFileConfigDir(item, 3);
            if (sDir == null) {
                continue;
            } else {
                actionDir += sDir;
            }
            List<String> list = getFilesAllName(actionDir);
            String fileName = "calc_" + item;
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String actionPath = selectFilePath;

            File dbFile = new File(actionPath);
            if (dbFile.exists()) {
                String strValve = readString(actionPath, "GBK");
                String[] strsValve = strValve.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
                CalcTable ct = new CalcTable();
                for (String items : strsValve) {
                    try {
                        String Temp = items.split("//")[0];
                        if (Temp != null && !Temp.equals("")) {
                            String Range = Temp.split("[:：]")[0];
                            String[] formulas = Temp.split("[:：]")[1].split("[;；]");
                            Calc calc = new Calc();
                            for (String formula : formulas) {
                                switch (formula.split("=")[0]) {
                                    case "A": {
                                        calc.setA_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "C": {
                                        calc.setC_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "K": {
                                        calc.setK_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "B": {
                                        calc.setB_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "F": {
                                        calc.setF_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "H": {
                                        calc.setH_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "D": {
                                        calc.setD_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                }
                            }
                            ct.Add(Range, calc);
                        }
                    } catch (Exception ex) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        if (ex.toString().contains("SQLiteException")) {
                            bundle.putString("alert-err", ex.toString() + " please check you database !!!");
                        } else {
                            bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.calculate_table_failed) + "[" + items + "]！");
                        }
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                        saveExceptInfo2File("组分[" + item + "] 解析算法文件发生异常 ：" + ex.toString());
                    }
                }
                lCalc.put(item, ct);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.calculate_file_lost) + " ！");
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
            }
        }
    }

    public static CalcTable getCalc(String Component) {
        return lCalc.get(Component);
    }

    private static void syncNoBoardListCalc() {
        for (String item : lAll5721Component.get(1)) {
            if (QueryMeasCateg(item).equals("7")) {
                syncNoBoardListCalc(item);
            }
        }
    }


    private static void syncNoBoardListCalc(String FComponent) {

        for (String item : lNoBoardComponent.get(1)) {
            String actionDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.calculate_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    actionDir = extSdcardPath;
                }
            } else {
                actionDir = SdcardPath;
            }

            String sDir = getNoBoardFileConfigDir(FComponent, item, 3);
            if (sDir == null) {
                continue;
            } else {
                actionDir += sDir;
            }
            List<String> list = getFilesAllName(actionDir);
            String fileName = "calc_" + item;
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String actionPath = selectFilePath;

            File dbFile = new File(actionPath);
            if (dbFile.exists()) {
                String strValve = readString(actionPath, "GBK");
                String[] strsValve = strValve.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
                NBCalcTable ct = new NBCalcTable();
                for (String items : strsValve) {
                    try {
                        String Temp = items.split("//")[0];
                        if (Temp != null && !Temp.equals("")) {
                            String Range = Temp.split("[:：]")[0];
                            String[] formulas = Temp.split("[:：]")[1].split("[;；]");
                            NBCalc calc = new NBCalc();
                            for (String formula : formulas) {
                                switch (formula.split("=")[0]) {
                                    case "A": {
                                        calc.setA_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "C": {
                                        calc.setC_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "K": {
                                        calc.setK_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "B": {
                                        calc.setB_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "F": {
                                        calc.setF_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "H": {
                                        calc.setH_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                    case "D": {
                                        calc.setD_Formula(formula.split("=")[1]);
                                    }
                                    break;
                                }
                            }
                            ct.Add(Range, calc);
                        }
                    } catch (Exception ex) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        if (ex.toString().contains("SQLiteException")) {
                            bundle.putString("alert-err", ex.toString() + " please check you database !!!");
                        } else {
                            bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.calculate_table_failed) + "[" + items + "]！");
                        }
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                        saveExceptInfo2File("组分[" + item + "] 解析算法文件发生异常 ：" + ex.toString());
                    }
                }
                lNoBoardCalc.put(item, ct);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.calculate_file_lost) + " ！");
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
            }
        }
    }


    public static NBCalcTable getNoBoardCalc(String Component) {
        return lNoBoardCalc.get(Component);
    }

    /*
     * 文件读取 修正能量的算法
     * 修正吸光度相关参数
     *
     * */
    private static void syncListCalcEn() {

        for (String item : lAll5721Component.get(1)) {
            String actionDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    return;
                } else {
                    actionDir = extSdcardPath;
                }
            } else {
                actionDir = SdcardPath;
            }
            actionDir += "Csoft" + File.separator + item + "/";
            //String actionPath = actionDir + "calcEn_" + item + ".txt";

            List<String> list = getFilesAllName(actionDir);
            String fileName = "calcEn_" + item;
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String actionPath = selectFilePath;

            File dbFile = new File(actionPath);
            if (dbFile.exists()) {
                String strValve = readString(actionPath, "GBK");
                String[] strsValve = strValve.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
                CalcEnRevise calcEn = new CalcEnRevise();
                for (String items : strsValve) {
                    try {
                        String Temp = items.split("//")[0];
                        if (Temp != null && !Temp.equals("")) {
                            String[] formulas = Temp.split("[;；]");
                            for (String str : formulas) {
                                switch (str.split("=")[0]) {
                                    case "EA": {
                                        calcEn.setEA_Formula(str.split("=")[1]);
                                    }
                                    break;
                                    case "EB": {
                                        calcEn.setEB_Formula(str.split("=")[1]);
                                    }
                                    break;
                                    case "EC": {
                                        calcEn.setEC_Formula(str.split("=")[1]);
                                    }
                                    break;
                                    case "ED": {
                                        calcEn.setED_Formula(str.split("=")[1]);
                                    }
                                    case "EE": {
                                        calcEn.setEE_Formula(str.split("=")[1]);
                                    }
                                    break;
                                    case "EF": {
                                        calcEn.setEF_Formula(str.split("=")[1]);
                                    }
                                    break;
                                    case "Y1": {
                                        calcEn.setRange1Y(Double.parseDouble(str.split("=")[1]));
                                    }
                                    break;
                                    case "Y2": {
                                        calcEn.setRange2Y(Double.parseDouble(str.split("=")[1]));
                                    }
                                    break;
                                    case "Y3": {
                                        calcEn.setRange3Y(Double.parseDouble(str.split("=")[1]));
                                    }
                                    break;
                                    case "XA": {
                                        calcEn.setParX(Double.parseDouble(str.split("=")[1]));
                                    }
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.calculate_table2_failed) + " [" + items + "]！");
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                    }
                }
                lCalcEn.put(item, calcEn);
            }
        }
    }

    /*
     * 获取吸光度修正参数及，能量计算公式
     * ***/
    public static CalcEnRevise getCalcEn(String Component) {
        return lCalcEn.get(Component);
    }


    private static void syncListError() {
        for (String item : lAll5721Component.get(1)) {
            String errorDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.error_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    errorDir = extSdcardPath;
                }
            } else {
                errorDir = SdcardPath;
            }

            String sDir = getFileConfigDir(item, 4);
            if (sDir == null) {
                continue;
            } else {
                errorDir += sDir;
            }
            List<String> list = getFilesAllName(errorDir);
            String fileName = "error_" + item;
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String errorPath = selectFilePath;

            File dbFile = new File(errorPath);
            if (dbFile.exists()) {
                String strValve = readString(errorPath, "GBK");
                String[] strsValve = strValve.replace("\t", "")./*replace(" ", "").*/replace("\r\n", "").replace("\n", "").split("#");
                ErrorTable et = new ErrorTable();
                // 先加载系统性报错
                try {
                    AppErrorCfg appError = new AppErrorCfg();
                    for (int i = 0; i < appError.getNoFileCfgError().size(); i++) {
                        et.Add(String.valueOf(appError.getNoFileCfgError().get(i).getErrorNum()), appError.getNoFileCfgError().get(i));
                    }
                } catch (Exception e) {
                    saveExceptInfo2File("系统报错加载错误!" + e.toString());
                }
                for (String items : strsValve) {
                    try {
                        String Temp = items.split("//")[0];
                        if (Temp != null && !Temp.equals("")) {
                            String[] errorInfo = Temp.split("[,，]");

                            String errorCode = errorInfo[0].replace(" ", "");
                            AppError error = new AppError();
                            error.setErrorName(errorInfo[1].replace(" ", ""));
                            error.setErrorShowName(errorInfo[2]);
                            error.setErrorCateg(errorInfo[3].replace(" ", ""));
                            if (errorInfo[4].replace(" ", "").equals(context.getString(R.string.yes))) {
                                error.setErrorIsShow(true);
                            } else {
                                error.setErrorIsShow(false);
                            }
                            if (errorInfo[5].replace(" ", "").equals(context.getString(R.string.CK_reported))) {
                                error.setErrorSrc(true);
                            } else {
                                error.setErrorSrc(false);
                            }
                            if (errorInfo[6].replace(" ", "").equals(context.getString(R.string.yes))) {
                                error.setErrorIsDoSample(true);
                            } else {
                                error.setErrorIsDoSample(false);
                            }
                            error.setErrorNum(Integer.parseInt(errorCode));

                            //                            if (errorInfo.length>7 && errorInfo[7].replace(" ", "").equals(context.getString(R.string.yes))) {
                            //                                error.setErrorShield(true);
                            //                            } else {
                            //                                error.setErrorShield(false);
                            //                            }
                            error.setErrorHMIShield(false);//上电读取初始化
                            et.Add(errorCode, error);
                        }
                    } catch (Exception ex) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.error_table_failed) + "：[" + items + "]！");
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                    }
                }
                lError.put(item, et);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "] " + context.getString(R.string.error_file_lost) + " ！");
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
            }

        }
    }

    /**
     * 同步报错屏蔽功能
     */
    public static void syncListErrorShield() {

        for (String item : lAll5721Component.get(1)) {
            String errorTable = getConfigData(item, "errorShieldTable");
            String[] errorNum = errorTable.split("[,，]");
            for (int i = 0; i < errorNum.length; ++i) {
                String errorCode = errorNum[i].replace(" ", "");
                if (getError(item) == null) {
                    continue;
                }
                AppError appError = getError(item).getErrorObj(errorCode);
                if (appError != null) {
                    appError.setErrorHMIShield(true);
                }
            }
        }
    }

    public static ErrorTable getError(String Component) {
        return lError.get(Component);
    }


    private static void syncNoBoardListError() {
        for (String item : lAll5721Component.get(1)) {
            if (QueryMeasCateg(item).equals("7")) {
                syncNoBoardListError(item);
            }
        }
    }

    private static void syncNoBoardListError(String FComponent) {
        for (String item : lNoBoardComponent.get(1)) {
            String errorDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.error_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    errorDir = extSdcardPath;
                }
            } else {
                errorDir = SdcardPath;
            }

            String sDir = getNoBoardFileConfigDir(FComponent, item, 4);
            if (sDir == null) {
                continue;
            } else {
                errorDir += sDir;
            }
            List<String> list = getFilesAllName(errorDir);
            String fileName = "error_" + item;
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String errorPath = selectFilePath;

            File dbFile = new File(errorPath);
            if (dbFile.exists()) {
                String strValve = readString(errorPath, "GBK");
                String[] strsValve = strValve.replace("\t", "").replace("\r\n", "").replace("\n", "").split("#");
                ErrorTable et = new ErrorTable();
                // 先加载系统性报错
                try {
                    AppErrorCfg appError = new AppErrorCfg();
                    for (int i = 0; i < appError.getNoFileCfgError().size(); i++) {
                        et.Add(String.valueOf(appError.getNoFileCfgError().get(i).getErrorNum()), appError.getNoFileCfgError().get(i));
                    }
                } catch (Exception e) {
                    saveExceptInfo2File("系统报错加载错误!" + e.toString());
                }
                for (String items : strsValve) {
                    try {
                        String Temp = items.split("//")[0];
                        if (Temp != null && !Temp.equals("")) {
                            String[] errorInfo = Temp.split("[,，]");

                            String errorCode = errorInfo[0].replace(" ", "");
                            AppError error = new AppError();
                            error.setErrorName(errorInfo[1].replace(" ", ""));
                            error.setErrorShowName(errorInfo[2]);
                            error.setErrorCateg(errorInfo[3].replace(" ", ""));
                            if (errorInfo[4].replace(" ", "").equals(context.getString(R.string.yes))) {
                                error.setErrorIsShow(true);
                            } else {
                                error.setErrorIsShow(false);
                            }
                            if (errorInfo[5].replace(" ", "").equals(context.getString(R.string.CK_reported))) {
                                error.setErrorSrc(true);
                            } else {
                                error.setErrorSrc(false);
                            }
                            if (errorInfo[6].replace(" ", "").equals(context.getString(R.string.yes))) {
                                error.setErrorIsDoSample(true);
                            } else {
                                error.setErrorIsDoSample(false);
                            }
                            error.setErrorNum(Integer.parseInt(errorCode));
                            et.Add(errorCode, error);
                        }
                    } catch (Exception ex) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.error_table_failed) + "：[" + items + "]！");
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                    }
                }
                lNoBoardError.put(item, et);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "] " + context.getString(R.string.error_file_lost) + " ！");
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
            }
        }
    }


    public static ErrorTable getNoBoardError(String Component) {
        return lNoBoardError.get(Component);
    }

    private static void syncListCompNameConvert() {

        String compNameDir;
        if (SdcardPath == null) {
            if (extSdcardPath == null) {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component_name_file_read_failed));
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
                return;
            } else {
                compNameDir = extSdcardPath;
            }
        } else {
            compNameDir = SdcardPath;
        }
        compNameDir += "Csoft" + File.separator;

        List<String> list = getFilesAllName(compNameDir);
        String fileName = "comp_name";
        String selectFilePath = "";
        if (list != null) {
            for (String name : list) {
                if (name.contains(fileName)) {
                    selectFilePath = name;
                    break;
                }
            }
        }
        String compNamePath = selectFilePath;

        File dbFile = new File(compNamePath);
        if (dbFile.exists()) {
            String strValve = readString(compNamePath, "GBK");
            String[] strsValve = strValve.replace("\t", "")./*replace(" ", "").*/replace("\r\n", "").replace("\n", "").split("#");
            for (String items : strsValve) {
                try {
                    String[] temp = items.split(",");
                    lCompNameConvert.put(temp[0], temp[1]);
                } catch (Exception ex) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.component_name_file_error) + "[" + items + "]！");
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                }
            }
        } else {
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle bundle = new Bundle();
            bundle.putString("alert-err", context.getString(R.string.component_name_file_lost) + "！");
            st.setArguments(bundle);
            st.setTargetFragment(Display.newInstance(), 1);
            st.show(fm, "Dialog_err");
        }
    }

    public static String getCompNameCN(String ComponentEng) {
        return lCompNameConvert.get(ComponentEng);
    }


    /**
     * 多光谱平台能量配置文件
     * 由于多光谱需要多各光谱测量点，各组分需要的测量点能量的时机不同
     */
    private static void syncNoBoardEnergysSelect() {
        for (String item : lAll5721Component.get(1)) {
            if (QueryMeasCateg(item).equals("7")) {
                String errorDir;
                if (SdcardPath == null) {
                    if (extSdcardPath == null) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.energys_file_read_failed));
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                        return;
                    } else {
                        errorDir = extSdcardPath;
                    }
                } else {
                    errorDir = SdcardPath;
                }

                String sDir = getFileConfigDir(item, 4);
                if (sDir == null) {
                    continue;
                } else {
                    errorDir += sDir;
                }
                List<String> list = getFilesAllName(errorDir);
                String fileName = "calSelectEn_" + item;
                String selectFilePath = "";
                if (list != null) {
                    for (String name : list) {
                        if (name.contains(fileName)) {
                            selectFilePath = name;
                            break;
                        }
                    }
                }
                String errorPath = selectFilePath;

                File dbFile = new File(errorPath);
                if (dbFile.exists()) {
                    String strValve = readString(errorPath, "GBK");
                    String[] strsValve = strValve.replace("\t", "").replace("\r\n", "").replace("\n", "").split("#");
                    for (String items : strsValve) {
                        try {
                            String Temp = items.split("//")[0];
                            if (Temp != null && !Temp.equals("")) {
                                String[] sInfo = Temp.split("[：:]");
                                lCalcSelectEn.put(sInfo[0], sInfo[1]);

                            }
                        } catch (Exception ex) {
                            main.removeDestopText(mfb);
                            Dialog_Err st = new Dialog_Err();
                            Bundle bundle = new Bundle();
                            bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "]" + context.getString(R.string.energys_table_failed) + "：[" + items + "]！");
                            st.setArguments(bundle);
                            st.setTargetFragment(Display.newInstance(), 1);
                            st.show(fm, "Dialog_err");
                        }
                    }
                    for (String comps : lNoBoardComponent.get(1)) {
                        String selectEns = lCalcSelectEn.get(comps);
                        if (selectEns == null) {
                            main.removeDestopText(mfb);
                            Dialog_Err st = new Dialog_Err();
                            Bundle bundle = new Bundle();
                            bundle.putString("alert-err", context.getString(R.string.component) + "[" + comps + "] " + context.getString(R.string.energys_table_lost) + " ！");
                            st.setArguments(bundle);
                            st.setTargetFragment(Display.newInstance(), 1);
                            st.show(fm, "Dialog_err");
                            return;
                        }
                    }
                } else {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "] " + context.getString(R.string.energys_table_failed) + " ！");
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                }
            }
        }
    }

    /*
     * 获取多光谱平台 光强测量点信息
     * ***/
    public static String getNoBoardCalcSelectEn(String Component) {
        return lCalcSelectEn.get(Component);
    }

    public synchronized static boolean runFlows(String component, List<String> flows) {
        List<FlowClass> lfc = new ArrayList<>();
        FlowTable ft = Global.getFlows(component);
        String flowName = "";
        List<String> flowstr = new ArrayList<>();
        if (ft == null) {
           /* Message msg = new Message();
           msg.what = ErrorLog.msgType.运行_信息.ordinal();
            msg.obj = "组分[" + component + "] 未从流程表找到对应命令,流程无法进行！";
            mHandler.sendMessage(msg);*/
            //            main.removeDestopText(mfb);
            //            Dialog_Err st = new Dialog_Err();
            //            Bundle bundle = new Bundle();
            //            bundle.putString("alert-err", "未从流程表找到对应命令！");
            //            st.setArguments(bundle);
            //            st.show(fm, "Dialog_err");
            return false;
        }
        for (String item : flows) {
            android.util.Log.v("FLOW", item + "/" + component);

            flowName = (item.contains(context.getString(R.string.manual)) ? item.split(context.getString(R.string.manual))[1] : (item.contains(context.getString(R.string.auto)) ? item.split(context.getString(R.string.auto))[1] : item));
            flowName = flowName.contains("反控") ? item.split("反控")[1] : flowName;

            android.util.Log.v("FLOW", flowName);

            FlowClass fc;
            String sFlowRange = getConfigData(component, "RANGE");
            flowName = flowName.replace(" ", "");
            if (flowName.equals(context.getString(R.string.GLCX)) || flowName.equals(context.getString(R.string.YBQX)) || flowName.equals(context.getString(R.string.CSZY))
                    || flowName.equals(context.getString(R.string.CHJZS))
                    || flowName.equals(context.getString(R.string.exceptionHandling)) || flowName.equals(context.getString(R.string.exceptionHandling2))
            ) {
                sFlowRange = "0";// 无量程流程
            } else if (flowName.equals(context.getString(R.string.LDHC)) || flowName.equals(context.getString(R.string.GYWDX)) || flowName.equals(context.getString(R.string.JYWDX))) {
                sFlowRange = "1";// 指定使用量程1流程
            } else if (flowName.equals(context.getResources().getString(R.string.KDHC))) {
                if (getConfigData(component, "KDHCManualRangeSwitch").equals("open")) {
                    sFlowRange = getConfigData(component, "KDHCManualRange");
                }
            }
            fc = ft.getFlow(flowName + "_" + sFlowRange);

            saveRunInfo2File(flowName + "_" + "量程" + sFlowRange);

            flowstr.add(flowName);

            if (fc == null) {
                Message msg = new Message();
                msg.what = 运行_信息.ordinal();
                msg.obj = context.getString(R.string.component) + "[" + component + "] " + context.getString(R.string.cmd_find_filed_show_info) + " ！";
                //mHandler.sendMessage(msg);
                saveRunInfo2File((String) msg.obj);
                //                main.removeDestopText(mfb);
                //                Dialog_Err st = new Dialog_Err();
                //                Bundle bundle = new Bundle();
                //                bundle.putString("alert-err", "未从流程表找到对应命令！");
                //                st.setArguments(bundle);
                //                st.show(fm, "Dialog_err");
                return false;
            }
            if (!flowName.equals(context.getString(R.string.exceptionHandling))) {
                workState.put(component, context.getString(R.string.normal));
                allAlarmInfo.put(component, context.getString(R.string.normal));
            }
            lfc.add(fc);
        }
        RealTimeStatusThread.suspend();

        cmdStatus.put("83", context.getString(R.string.exception));
        SendManager.SendCmd(component + "_" + (flows.size() == 1 ? flows.get(0) : "组合流程") + "_8_03", S0, 3, 500, lfc);
        RealTimeStatusThread.resume();
        try {
            sleep(1550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Objects.requireNonNull(cmdStatus.get("83")).equals(context.getString(R.string.exception))) {
            return false;
        }

        startFlowsStartModule(component, flowName);

        //W200 && 关联流程
        if (getPublicConfigData("SYS_RELAYCONF").equals("3") && flowName.equals(context.getString(R.string.ZY))) {
            //启动流程的时候去判断开采样、关采样动作
            GetRunFlowsInfoRunnable gfiRunnable = new GetRunFlowsInfoRunnable(component);
            gfiRunnable.run();
        }

        return true;
    }

    /*
    通知W300板子仪表状态
     */
    public static void NoticeW300FlowStart(String component, String flows, boolean open) {
        if (flows.contains(context.getString(R.string.ZY)) || flows.contains(context.getString(R.string.BYCL))
                || flows.contains(context.getString(R.string.BY2CL)) || flows.contains(context.getString(R.string.LYCL))
                || flows.contains(context.getString(R.string.B1)) || flows.contains(context.getString(R.string.B2))
                || flows.contains(context.getString(R.string.JZ)) || flows.contains(context.getString(R.string.BYHC))
                || flows.contains(context.getString(R.string.LDHC)) || flows.contains(context.getString(R.string.KDHC))
                || flows.contains(context.getString(R.string.PXY)) || flows.contains(context.getString(R.string.JBHS))) {
            String strAddr = getConfigData(component, "W300_RTU_ID");
            if (!strAddr.equals("")) {
                byte addr = Byte.parseByte(strAddr);
                byte bOpen = 0x02;
                if (open) {
                    bOpen = 0x01;
                }
                byte[] cmd = new byte[]{addr, 0x06, 0x00, 0x00, 0x00, bOpen};
                cmd = copybyte(cmd, crc16(cmd, cmd.length));
                if (IOBoardUsed) {
                    if (protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[14])) {
                        SendManager.SendCmd("IO" + "_" + "W300开始测量_09_07", S1, 3, 500, cmd);
                    }
                    if (protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[14])) {
                        SendManager.SendCmd("IO" + "_" + "W300开始测量_09_09", S1, 3, 500, cmd);
                    }
                    if (protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[14])) {
                        SendManager.SendCmd("IO" + "_" + "W300开始测量_09_08", S1, 3, 500, cmd);
                    }
                } else {
                    if (protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[14])) {
                        SendManager.SendCmd("IO" + "_" + "W300开始测量_0_0", S1, 3, 500, cmd);
                    }
                    if (protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[14])) {
                        SendManager.SendCmd("IO" + "_" + "W300开始测量_0_0", S1, 3, 500, cmd);
                    }
                }
            }
        }
    }

    /*
    启动流程时候，判断是否需求启动附属设备
    * **/
    private static void startFlowsStartModule(String component, String flowName) {
        /*均质预处理 启动接口板水样采集*/
        if (IOBoardUsed) {
            if (getPublicConfigData("SYS_RELAYCONF").equals("1") && flowName.equals(context.getString(R.string.ZY))) {
                SendManager.SendCmd("IO" + "_" + "接口板开启采集水样_0C_01", S1, 3, 500, null);
            }
        }
        try {
            if (isHaveMeasCategory(component, "5")) {
                String strComponentModule = component + getModuleName("5");
                if (flowName.equals(context.getString(R.string.ZY)) || flowName.equals(context.getString(R.string.XSJZ))) {
                    if (getConfigData(component, "RANGE").equals("3")) {
                        if (!doFlowing.get(strComponentModule).equals(context.getResources().getString(R.string.waiting_for_instructions))) {
                            saveExceptInfo2File(strComponentModule + "稀释背板正在:" + doFlowing.get(strComponentModule) + "启动做样失败!");
                        }
                        List<String> flowsModule = new ArrayList<>();
                        flowsModule.add(context.getString(R.string.ZY));
                        runComponentModule(strComponentModule, flowsModule);
                    }
                }
            }
        } catch (Exception e) {
            Log.i("except" + "启动测量", e.toString());
        }
    }


    /*
     * 组份背板启动模块背板流程
     * component            组份背板名称
     * componentModuleName  模块背板名称
     **/
    private static void runComponentModule(String componentModuleName, List<String> flows) {
        List<FlowClass> lfc = new ArrayList<>();
        FlowTable ft = Global.getFlows(componentModuleName);
        String flowName;
        if (ft == null) {
            return;
        }
        for (String item : flows) {
            flowName = (item.contains(context.getString(R.string.manual)) ? item.split(context.getString(R.string.manual))[1] : (item.contains(context.getString(R.string.auto)) ? item.split(context.getString(R.string.auto))[1] : item));
            FlowClass fc;
            if (flowName.equals(context.getString(R.string.GLCX)) || flowName.equals(context.getString(R.string.YBQX))
                    || flowName.equals(context.getString(R.string.CHJZS))
                    || flowName.equals(context.getString(R.string.CSZY)) || flowName.equals(context.getString(R.string.exceptionHandling))) {
                fc = ft.getFlow(flowName + "_0");
            } else {
                fc = ft.getFlow(flowName + "_" + getConfigData(componentModuleName, "XS_MODE"));
            }
            saveRunInfo2File(flowName);

            if (fc == null) {
                Message msg = new Message();
                msg.what = 运行_信息.ordinal();
                msg.obj = context.getString(R.string.component) + "[" + componentModuleName + "] " + context.getString(R.string.cmd_find_filed_show_info) + " ！";
                saveRunInfo2File((String) msg.obj);
                return;
            }
            lfc.add(fc);
        }
        RealTimeStatusThread.suspend();
        SendManager.SendCmd(componentModuleName + "_" + (flows.size() == 1 ? flows.get(0) : "组合流程") + "_8_03", S0, 3, 1000, lfc);
        RealTimeStatusThread.resume();
    }


    /*寻峰启动流程
     * **/
    public static void startSpecPeakFlow(String component, List<String> flows) {
        List<FlowClass> lfc = new ArrayList<>();
        FlowTable ft = Global.getFlows(component);
        String flowName = "";
        if (ft == null) {
            return;
        }
        for (String item : flows) {
            android.util.Log.v("FLOW", item + "/" + component);

            flowName = (item.contains(context.getString(R.string.manual)) ? item.split(context.getString(R.string.manual))[1] : (item.contains(context.getString(R.string.auto)) ? item.split(context.getString(R.string.auto))[1] : item));
            FlowClass fc;
            flowName = flowName.replace(" ", "");
            if (flowName.equals(context.getString(R.string.GLCX)) || flowName.equals(context.getString(R.string.YBQX))
                    || flowName.equals(context.getString(R.string.CHJZS)) || flowName.equals(context.getString(R.string.CSZY))
                    || flowName.equals(context.getString(R.string.exceptionHandling))) {
                fc = ft.getFlow(flowName + "_0");
            } else {
                // 存储一下启动校准之前的量程
                updateConfigData(component, "PreCalSelectRange", getConfigData(component, "RANGE"));
                fc = ft.getFlow(flowName + "_" + getConfigData(component, "RANGE"));
            }
            saveRunInfo2File(flowName);
            if (flowName.equals(context.getString(R.string.YBQX))) {
                AddError(component, 524, 其他_信息);
            } else if (flowName.equals(context.getString(R.string.GLCX))) {
                AddError(component, 525, 其他_信息);
            }
            if (fc == null) {
                Message msg = new Message();
                msg.what = 运行_信息.ordinal();
                msg.obj = context.getString(R.string.component) + "[" + component + "] " + context.getString(R.string.cmd_find_filed_show_info) + " ！";
                saveRunInfo2File((String) msg.obj);
                return;
            }
            workState.put(component, context.getString(R.string.normal));
            allAlarmInfo.put(component, context.getString(R.string.normal));
            lfc.add(fc);

        }
        RealTimeStatusThread.suspend();
        SendManager.SendCmd(component + "_" + (flows.size() == 1 ? flows.get(0) : "组合流程") + "_8_16", S0, 3, 1000, lfc);
        RealTimeStatusThread.resume();
        startFlowsStartModule(component, flowName);
    }


    private static void syncListCommPublic() {
        CommPublic lCommPublic = new CommPublic(context);
    }

    private static void syncListParPublic() {
        new SystemPar(context);
    }

    /*
     * 浮点数Float转换为字节数组
     */
    public static byte[] floatToBytes(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    /* double类型数据四舍五入保留num位小数*/
    public static double doubleRound(double d, int num) {
        return Math.round(d * Math.pow(10, num)) / Math.pow(10, num);
    }

    //用于格式化日期,作为日志文件名的一部分
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    /*
     * 保存异常信息到文件中
     */
    public static String saveExceptInfo2File(String exStr) {

        try {
            String time = formatter.format(new Date());
            String fileName = "excep-" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/waterexcep/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                exStr = sDateFormat.format(new Date()) + "\t" + exStr + "\r\n";

                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(exStr.getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            android.util.Log.e("Global", "an error occured while writing file1...", e);
        }
        return null;
    }

    /*
     * 保存运行信息到文件中
     */
    public static String saveRunInfo2File(String runStr) {
        try {
            String time = formatter.format(new Date());
            String fileName = "run-" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/waterrun/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                runStr = sDateFormat.format(new Date()) + "\t" + runStr + "\r\n";

                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(runStr.getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            android.util.Log.e("Global", "an error occured while writing file2...", e);
        }
        return null;
    }

    public static String saveRunInfo2ToFile(String fName, String runStr) {
        try {
            String time = formatter.format(new Date());
            String fileName = "run-" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/" + fName + "/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                runStr = sDateFormat.format(new Date()) + "\t" + runStr + "\r\n";

                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(runStr.getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            android.util.Log.e("Global", "an error occured while writing file2...", e);
        }
        return null;
    }


    private static void syncListMeaParCfg() {

        for (String item : lAll5721Component.get(1)) {
            String meaParCfgDir = null;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.mea_cfg_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    meaParCfgDir = extSdcardPath;
                }
            } else {
                meaParCfgDir = SdcardPath;
            }

            String sDir = getFileConfigDir(item, 5);
            if (sDir == null) {
                continue;
            } else {
                meaParCfgDir += sDir;
            }

            List<String> list = getFilesAllName(meaParCfgDir);
            String fileName = "meaPar_";
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String meaParCfgPath = selectFilePath;

            File dbFile = new File(meaParCfgPath);
            if (dbFile.exists()) {
                String strValve = readString(meaParCfgPath, "GBK");
                String[] strsValve = strValve.replace("\t", "").replace("\r\n", "").replace("\n", "").split("#");
                MeaParTable mt = new MeaParTable();
                int count = 0;
                for (String items : strsValve) {
                    try {
                        if (items != null && !items.equals("")) {
                            String[] strCfg = items.split("[,，]");
                            MeaParCfg meaCfg = new MeaParCfg();
                            meaCfg.setPar(strCfg[0], strCfg[1].replace(" ", ""), strCfg[2].replace(" ", ""), strCfg[3].replace(" ", ""));
                            if (strCfg.length > 4) {
                                meaCfg.setShowName(strCfg[4]);
                            } else {
                                meaCfg.setShowName(strCfg[0]);
                            }
                            mt.Add(String.valueOf(count++), meaCfg);
                        }
                    } catch (Exception ex) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.mea_cfg_table_error) + "：[" + items + "]！");
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                    }
                }
                lMeaPar.put(item, mt);
            } else {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component) + "[" + item + "] " + context.getString(R.string.mea_cfg_file_lost) + " ！");
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
            }
        }
    }


    /*
     * 根据协议配置文件，获取到可选择的协议项
     */

    private static void syncListProtocolCfg() {
        ArrayList<String> protocl = new ArrayList<>();
        String[] protocolDir = {"GB/GB2005.txt", "GB/GB2017.txt", "Modbus/Rtu.txt", "jiangsu/nanshui.txt", "Modbus/Rtu2018.txt", "jiangsu/jiangsu.txt", "HeBei/HeBei.txt", "GB/GB2017V2.txt", "HeNan/HeNan.txt", "GB/DiBiaoShui.txt", "Modbus/Rtu2.txt", "Modbus/GuiZhou.txt", "ShenZhen/ShenZhen.txt", "NingXia/NingXia.txt", "Modbus/W300.txt",
                "SiChuan/SiChuanRtu.txt",};
        String dir = null;
        if (SdcardPath == null) {
            if (extSdcardPath == null) {
                return;
            } else {
                dir = extSdcardPath;
            }
        } else {
            dir = SdcardPath;
        }
        dir += "Csoft" + "/Protocol/";
        protocl.add(context.getString(R.string.unConfigured));
        try {
            for (int i = 0; i < protocolDir.length; i++) {
                String filePath = dir + protocolDir[i];
                File dbFile = new File(filePath);

                if (dbFile.exists()) {
                    protocl.add(protocolName[i]);
                    if (protocolName[i].equals("Modbus_Rtu")) {
                        /*rtu文件内获取*/
                        // syncListModbusRtu();
                        //syncListModbusRtu2018();
                    }
                    if (protocolName[i].equals("ModbusRtu2018")) {
                        syncListModbusRtu2018();
                    }
                    if (protocolName[i].equals("Modbus_Rtu2")) {
                        syncListModbusRtu2();
                    }
                    if (protocolName[i].equals("贵州协议")) {
                        syncListGuiZhou();
                    }
                    if (protocolName[i].equals("四川协议")) {
                        syncListSiChuanModbusRtu();
                    }
                }
            }
        } catch (Exception e) {
            Log.i("except-解析协议配置文件", e.toString());
        }
        protocolList = new String[protocl.size()];
        for (int i = 0; i < protocl.size(); i++) {
            protocolList[i] = protocl.get(i);
        }
    }

    /*
    解析协议配置中显示名称文件
    * */
    private static void syncListProtocolDisplayNameCfg() {

        String protocolNameDir;
        if (SdcardPath == null) {
            if (extSdcardPath == null) {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.component_name_file_read_failed));
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
                return;
            } else {
                protocolNameDir = extSdcardPath;
            }
        } else {
            protocolNameDir = SdcardPath;
        }
        protocolNameDir += "Csoft" + "/Protocol/";

        List<String> list = getFilesAllName(protocolNameDir);
        String fileName = "ProtocolName";
        String selectFilePath = "";
        if (list != null) {
            for (String name : list) {
                if (name.contains(fileName)) {
                    selectFilePath = name;
                    break;
                }
            }
        }
        String compNamePath = selectFilePath;
        protocolDisplayNameOrder = "";
        File dbFile = new File(compNamePath);
        if (dbFile.exists()) {
            String strValve = readString(compNamePath, "GBK");
            String[] strsValve = strValve.replace("\t", "").replace("\r\n", "").replace("\n", "").split("#");
            for (String items : strsValve) {
                try {
                    if (!items.contains("//")) {
                        String[] temp = items.split("[,，]");
                        protocolDisplayName.put(temp[0], temp[1]);
                        protocolDisplayNameOrder += (temp[1] + ",");
                    }
                } catch (Exception ex) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.protocol_name_file_error) + "[" + items + "]！");
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                }
            }
        }
    }

    /*
     * 解析surplus_XX.txt 文件
     * 用于余量告警
     */
    private static void syncFileSurplusCfg() {

        for (String item : strComponent.get(1)) {
            String surplusCfgDir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.reagent_file_read_failed));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.newInstance(), 1);
                    st.show(fm, "Dialog_err");
                    return;
                } else {
                    surplusCfgDir = extSdcardPath;
                }
            } else {
                surplusCfgDir = SdcardPath;
            }

            String sDir = getFileConfigDir(item, 6);
            if (sDir == null) {
                continue;
            } else {
                surplusCfgDir += sDir;
            }
            List<String> list = getFilesAllName(surplusCfgDir);
            String fileName = "surplus_";
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String surplusCfgPath = selectFilePath;

            File dbFile = new File(surplusCfgPath);
            if (dbFile.exists()) {
                String strCfg = readString(surplusCfgPath, "GBK");
                String[] strAllCfg = strCfg.replace("\t", "")./*replace(" ", "").*/replace("\r\n", "").replace("\n", "").split("#");

                SurplusFileCfg surplusFileCfg = new SurplusFileCfg();
                int id = 1;
                for (String items : strAllCfg) {
                    try {
                        if (items.contains("//")) {
                            continue;
                        }
                        SurplusFileInfo surplusFileInfo = new SurplusFileInfo();
                        if (!items.equals("")) {

                            String[] strsCfg = items.split("[,，]");

                            switch (strsCfg.length) {
                                case 7:
                                    surplusFileInfo.setDosage(3, Float.parseFloat(strsCfg[6].replace(" ", "")));
                                case 6:
                                    surplusFileInfo.setDosage(2, Float.parseFloat(strsCfg[5].replace(" ", "")));
                                case 5:
                                    surplusFileInfo.setDosage(1, Float.parseFloat(strsCfg[4].replace(" ", "")));
                                case 4:
                                    surplusFileInfo.setDosage(0, Float.parseFloat(strsCfg[3].replace(" ", "")));
                                case 3:
                                    surplusFileInfo.setFullBottle(Integer.parseInt(strsCfg[2].replace(" ", "")));
                                case 2:
                                    surplusFileInfo.setReagentNum(strsCfg[1].replace(" ", ""));
                                case 1:
                                    surplusFileInfo.setReagent(strsCfg[0]);
                                    break;
                            }
                        }
                        surplusFileCfg.getSurplusInfoMap().put(String.valueOf(id++), surplusFileInfo);
                    } catch (Exception ex) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.reagent_file_table_error) + "：[" + items + "]！");
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                    }
                }
                lsurPlus.put(item, surplusFileCfg);
            }
        }
    }


    /*
     * 解析component_XX.txt 文件
     * 用于VOC 组份选择
     */
    private static void syncFileVocComponentCfg() {

        for (String item : strComponent.get(1)) {
            String dir;
            if (SdcardPath == null) {
                if (extSdcardPath == null) {
                    return;
                } else {
                    dir = extSdcardPath;
                }
            } else {
                dir = SdcardPath;
            }
            if (!QueryMeasCateg(item).equals("4")) {
                continue;
            } else {
                dir += "Csoft" + File.separator + item + "/";
            }

            List<String> list = getFilesAllName(dir);
            String fileName = "component_";
            String selectFilePath = "";
            if (list != null) {
                for (String name : list) {
                    if (name.contains(fileName)) {
                        selectFilePath = name;
                        break;
                    }
                }
            }
            String path = selectFilePath;

            File dbFile = new File(path);
            if (dbFile.exists()) {
                String strCfg = readString(path, "GBK");
                String[] strAllCfg = strCfg.replace("\t", "")./*replace(" ", "").*/replace("\r\n", "").replace("\n", "").split("#");
                String[] cfg = new String[strAllCfg.length];
                int index = 0;
                int removeNum = 0;
                for (String items : strAllCfg) {
                    try {
                        if (items.contains("//")) {
                            removeNum++;
                            continue;
                        }
                        if (!items.equals("")) {
                            cfg[index++] = items;
                        }
                    } catch (Exception ex) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.voc_par_cfg_table_error) + "：[" + items + "]！");
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                    }
                }
                if (cfg.length > removeNum) {
                    String[] sCfg = new String[cfg.length - removeNum];
                    for (int i = 0; i < sCfg.length; i++) {
                        sCfg[i] = cfg[i];
                        lComponentVoc.put(item, sCfg);
                    }
                } else {
                    lComponentVoc.put(item, cfg);
                }
            }
        }
    }


    /**
     * 启动测量  由于内部发送指令后需要等待返回结果，故需要执行需要较长时间，建议使用线程调用该方法
     *
     * @param compName 组分名称
     * @param flowName 启动流程名称
     */
    public static boolean doControlJob(String compName, String flowName) {
        List<String> flows = new ArrayList<>();

        try {
            if (!doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
                return false;
            }
            if (!doFlowWorking.get(compName).equals("0")) {
                return false;
            }
            // 离线
            String strMode = getConfigData(compName, "runningMode");
            if (strMode.equals("2")) {// 离线
                return false;
            }
            if (flowName.contains(context.getString(R.string.ZY)) || flowName.contains(context.getString(R.string.SDZY))
                    || flowName.contains(context.getString(R.string.autoJob))) {
                // 没有输入量的情况下
                if (IOBoardUsed) {
                    if (!getDigitalAnalogInQuanlityStatus(compName)) {
                        return false;
                    }
                }
            }

            String[] strflows = flowName.split("_");
            for (int i = 0; i < strflows.length; i++) {
                flows.add(strflows[i]);
            }
            if (!runFlows(compName, flows)) {
                return false;
            }
            /***********以下状态刷新**********/
            try {
                //记录做样时间
                if (doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
                    if (flowName.contains(context.getString(R.string.ZY)) || flowName.contains(context.getString(R.string.SDZY))
                            || flowName.contains(context.getString(R.string.autoJob))) {
                        updateConfigData(compName, "START_ZY_JOB_TIME", getTimeHaveSec());
                        saveRunInfo2File("组分[" + compName + "] 启动" + flowName + getConfigData(compName, "START_ZY_JOB_TIME"));
                    } else {

                        updateConfigData(compName, "START_ZY_JOB_TIME", "");
                        saveRunInfo2File("组分[" + compName + "] 启动" + flowName + "清除做样启动时间记录");
                    }
                }
                // 默认为空的时候先初始化为当前设备量程
                if (getConfigData(compName, "CalSelectRange").equals("")) {
                    updateConfigData(compName, "CalSelectRange", getConfigData(compName, "RANGE"));
                }
                // 存储一下启动校准之前的量程
                updateConfigData(compName, "PreCalSelectRange", getConfigData(compName, "RANGE"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 启动流程不是异常处理的情况下清除下列状态
            if (!flowName.contains(context.getString(R.string.exceptionHandling))) {
                allAlarmInfo.put(compName, context.getString(R.string.normal));
                workState.put(compName, context.getString(R.string.normal));
                AutoSampleEnable.put(compName, "true");
                meaAbsorbEnergy.put(compName, "");
                meaAbsorbEnergyShowFlag.put(compName, "false");
                manualStopFlag.put(compName, "false");
                historyCalcFlag.put(compName, "");
                if (isHaveMeasCategory(compName, "6")) {
                    String strComponentModule = compName + getModuleName("6");
                    allAlarmInfo.put(strComponentModule, context.getString(R.string.normal));
                    workState.put(strComponentModule, context.getString(R.string.normal));
                }
                updateConfigData(compName, "doJobRunningMode", getConfigData(compName, "runningMode"));
            }
            sendSysTimeToDev(compName);

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "] 启动流程[" + flowName + "]" + "状态初始化异常!");
            return false;
        }

        if (!flowName.contains(context.getString(R.string.BY2CL))) {
            updateConfigData(compName, "OneKeyCalStartFlag", "false");
            updateConfigData(compName, "OneKeyCalForceXFlag", "false");
        }
        // 不是手动校准的情况
        //if (!flowName.contains(context.getString(R.string.SDJZ))) {
        // 校准量程和当前量程不同，将当前量程切换至校准后量程
        if (!getConfigData(compName, "CalSelectRange").equals(getConfigData(compName, "RANGE"))) {
            setUseRange(compName, getConfigData(compName, "CalSelectRange"));
        }
        //}
        // 清除一些状态
        if (getConfigData(compName, "OneKeyCalStartFlag").equals("true")) {
            updateConfigData(compName, "OneKeyCalStartFlag", "false");
        }
        if (getConfigData(compName, "OneKeyCalForceXFlag").equals("true")) {
            updateConfigData(compName, "OneKeyCalForceXFlag", "false");
        }
        if (getConfigData(compName, "ycjzFlag").equals("true")) {
            updateConfigData(compName, "ycjzFlag", "false");
        }
        //清除校准启动标记
        if (flowName.contains(context.getString(R.string.JZ)) && getConfigData(compName, "CAL_Start_Flag").equals("false")) {
            updateConfigData(compName, "CAL_Start_Flag", "true");
        }
        if (flowName.contains(context.getString(R.string.JZ)) & getConfigData(compName, "CAL2_Start_Flag").equals("false")) {
            updateConfigData(compName, "CAL2_Start_Flag", "true");
        }
        //更新是否手动校准标志
        if (flowName.contains(context.getString(R.string.SDJZ))) {
            updateConfigData(compName, "CAL_CONTROL", "true");
            if (QueryMeasCateg(compName).equals("7")) {
                for (String item : lNoBoardComponent.get(1)) {
                    if (!item.equals("TSS")) {
                        updateNoBoardConfigData(compName, "CAL_CONTROL", "true");
                    }
                }
            }
        } else if (flowName.contains(context.getString(R.string.B1)) || flowName.contains(context.getString(R.string.B2)) || flowName.contains(context.getString(R.string.JZ))) {
            updateConfigData(compName, "CAL_CONTROL", "false");
            if (QueryMeasCateg(compName).equals("7")) {
                for (String item : lNoBoardComponent.get(1)) {
                    if (!item.equals("TSS")) {
                        updateNoBoardConfigData(compName, "CAL_CONTROL", "false");
                    }
                }
            }
        }
        /////////
        //启动日志记录(单一流程)
        //指令发送成功才去记录日志信息
        //        for (String item : flows) {
        if (flows.get(0) != null && flows.size() == 1) {
            //获取是手动还是自动
            String autoOrManual = (flows.get(0).contains(context.getString(R.string.manual)) ? context.getString(R.string.manual) : (flows.get(0).contains(context.getString(R.string.auto)) ? context.getString(R.string.auto) : flows.get(0)));
            //获取流程信息
            flowName = (flows.get(0).contains(context.getString(R.string.manual)) ? flows.get(0).split(context.getString(R.string.manual))[1] : (flows.get(0).contains(context.getString(R.string.auto)) ? flows.get(0).split(context.getString(R.string.auto))[1] : flows.get(0)));
            flowName = flowName.contains("反控") ? flows.get(0).split("反控")[1] : flowName;
            flowName = flowName.replace(" ", "");

            Log.i("ACT", autoOrManual + "," + flowName);

            if (flowName.equals(context.getString(R.string.YBQX))) {//仪表清洗
                AddError(compName, 524, 运维_信息);

            } else if (flowName.equals(context.getString(R.string.GLCX))) {//管路冲洗
                AddError(compName, 525, 运维_信息);

            } else if (flowName.contains(context.getString(R.string.JZ))) {
                if (autoOrManual.equals(context.getString(R.string.auto))) {//自动校准
                    AddError(compName, 668, 运维_信息);
                }
                if (autoOrManual.equals(context.getString(R.string.manual))) {//手动校准
                    AddError(mCompName, 667, 运维_信息);
                }
            } else if (flowName.contains(context.getString(R.string.B1))) {
                AddError(mCompName, 527, 运维_信息);//（标样1校准）

            } else if (flowName.contains(context.getString(R.string.B2))) {
                AddError(mCompName, 528, 运维_信息);//（标样2校准）

            } else if (flowName.contains(context.getString(R.string.LDHC))) {
                AddError(mCompName, 602, 运维_信息);//零点核查

            } else if (flowName.contains(context.getString(R.string.BYHC))) {

                //TODO 判断是否为自动，若是自动标样核查，则记录日志
                if (autoOrManual.equals(context.getString(R.string.auto))) {
                    AddError(compName, 669, 运维_信息);//自动标样核查
                }
                if (autoOrManual.equals(context.getString(R.string.manual))) {
                    AddError(mCompName, 603, 运维_信息);//标样核查
                }

            } else if (flowName.contains(context.getString(R.string.KDHC))) {
                AddError(mCompName, 604, 运维_信息);//跨度核查

            } else if (flowName.contains(context.getString(R.string.JBHS))) {
                AddError(mCompName, 622, 运维_信息);//加标回收

            }
        }

        //        }


        // 控制灯
        setAllCompBSLEDStatus(compName);
        updateConfigData(compName, "StartFlowName", flowName);
        //我需要强制更新KB,不进行校准失败判断
        //根据功能开关确定是否强制
        if (!getConfigData(compName, "calibrationFlag").equals("true")) {//sun
            updateConfigData(compName, "ycjzFlag", "true");
            //如果是多光谱平台则将其它无背板参数也强制校准
            if (QueryMeasCateg(compName).equals("7")) {
                for (String item : lNoBoardComponent.get(1)) {
                    if (!item.equals("TSS")) {
                        updateNoBoardConfigData(item, "ycjzFlag", "true");
                    }
                }
            }
        }
        if (!getConfigData(compName, "StopFlowTime").equals("")) {
            updateConfigData(compName, "StopFlowTime", "");
        }
        getCfgSwitchStatus(compName);
        if (getPublicConfigData("AIsShow").equals("true")) {
            if (getConfigData(compName, "readHistoryDataFlag").equals("false")) {
                updateConfigData(compName, "readHistoryDataFlag", "true");
            }
            if (!getConfigData(compName, "nowHistoryDataAEn").equals("")) {
                updateConfigData(compName, "nowHistoryDataAEn", "");
            }
        }
        return true;
    }

    public static Communication.Port GetPhoneType() {
        switch (phoneName) {
            case "QUAD-CORE A33 y3":
                return Communication.Port.SerialPortMC;
            case "rk3288":
                return Communication.Port.YangChuangPort;
            default:
                return Communication.Port.WeiQianPort;
        }
    }


    /**
     * 从测控板获取配置流程开关状态
     *
     * @param compName
     */
    private static void getCfgSwitchStatus(String compName) {
        byte[] arrayOfByte1 = DataUtil.shortToByte((short) 5703);
        DataUtil.reverse(arrayOfByte1);
        SendManager.SendCmd(compName + "_配置流程开关" + "_3_" + DataUtil.bytesToHexString(arrayOfByte1, 2).replace(" ", ""), Global.S0, 3, 200, 1);
    }

    public static double getAnalogInQuanlityStatus() {
        float analogIn;
        double fAnalogMeaData = 0.0f;
        int channelId = parseInt(getPublicConfigData("CONTROL_CHANNEL"));
        float devRange = Float.parseFloat(getPublicConfigData("CONTROL_RANGE"));

        /**1#模拟量*/
        if (channelId == 2) {
            analogIn = channel1Val;
        } else {
            analogIn = channel2Val;
        }
        if (analogIn < 4) {
            analogIn = 4;
        }
        fAnalogMeaData = ((analogIn - 4) * (devRange / 16));
        return fAnalogMeaData;

    }


    /*
     * PH,流量计 使用模拟量输入端的时候
     * **/
    public static boolean getDigitalAnalogInQuanlityStatus(String compName) {
        try {
            /*模拟量输入反控测量值*/
            float controlAnalogMeaData = 0;
            int channelId = parseInt(getPublicConfigData("CONTROL_CHANNEL"));
            int channelDev = Integer.parseInt(getPublicConfigData("CONTROL_DEV"));

            /**模拟量输入*/ /**PH、流量计*/
            if ((channelId == 2 || channelId == 3) && (channelDev == 2 || channelDev == 3)) {
                float devRange = Float.parseFloat(getPublicConfigData("CONTROL_RANGE"));
                float devDoor = Float.parseFloat(getPublicConfigData("CONTROL_DOOR_LIMIT"));
                channelValUpdateFlag = false;

                byte[] arrayOfByte = shortToByte((short) 1);
                reverse(arrayOfByte);
                SendManager.SendCmd("所有组分" + "_读取4-20mA输入" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S1, 3, 200, 8);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!channelValUpdateFlag) {
                    saveRunInfo2File("组分[" + compName + "] 模拟量来水检测,接口板通讯异常,运行继续执行!");
                }
                float analogIn;
                /**1#模拟量*/
                if (channelId == 2) {
                    analogIn = channel1Val;
                } else {
                    analogIn = channel2Val;
                }
                if (analogIn < 4) {
                    analogIn = 4;
                }
                controlAnalogMeaData = ((analogIn - 4) * (devRange / 16));
                Message msg = new Message();
                msg.what = 3;
                WinWidgetHandler.sendMessage(msg);
                if (controlAnalogMeaData > devDoor) {
                    if (strComponent.get(1).length > 0) {
                        if (doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))
                                && ((Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 2)
                                || (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 3))) {

                            return true;
                        }
                    }
                } else {
                    saveOperationLogMsg(compName, "581_水样流量过低:" + controlAnalogMeaData, 报错_警告);
                    Message msg1 = new Message();
                    msg1.what = 5;
                    WinWidgetHandler.sendMessage(msg1);
                    return false;
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "] 模拟量来水检测异常");
        }
        return true;
    }

    //查看是否无背板参数
    public static boolean getIsNoBoard(String compName) {
        if (QueryMeasCateg(compName).equals("7")) {
            for (String item : lNoBoardComponent.get(1)) {
                if (!item.equals("TSS") && item.equals(compName)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取设备是否测量中
     *
     * @return true :非空闲
     */
    public static boolean devStatusIsBusy() {
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {
                if (!doFlowing.get(item).equals(context.getString(R.string.waiting_for_instructions))) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * 读取对应时间的图表
     * */
    public static float[] readCurvePoint(String compName, String datetime) {
        String flowDir;
        if (SdcardPath == null) {
            if (extSdcardPath == null) {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", context.getString(R.string.flow_file_read_failed));
                st.setArguments(bundle);
                st.setTargetFragment(Display.newInstance(), 1);
                st.show(fm, "Dialog_err");
                return null;
            } else {
                flowDir = extSdcardPath;
            }
        } else {
            flowDir = SdcardPath;
        }
        String sDir = getFileConfigDir(compName, 0);
        if (sDir == null) {
            return null;
        } else {
            flowDir += sDir + "Curve" + "/";
        }
        List<String> list = getFilesAllName(flowDir);
        String selectFilePath = "";
        if (list != null) {
            for (String name : list) {
                if (name.contains(datetime)) {
                    selectFilePath = name;
                    break;
                }
            }
        }

        String flowPath = selectFilePath;
        try {
            File dbFile = new File(flowPath);
            if (dbFile.exists()) {
                String strPoint = readString(flowPath, "GBK");
                String[] strsPoint = strPoint.split("[,，]");
                float[] fVals = new float[strsPoint.length];
                for (int i = 0; i < strsPoint.length; i++) {
                    fVals[i] = Float.parseFloat(strsPoint[i]);
                }
                return fVals;
            }
        } catch (Exception ex) {

        }

        return null;
    }

    /*
     * 保存TOC图表信息到文件中
     */
    public static String saveCurveInfo2File(String compName, String datetime, String info) {
        try {
            String fileName = datetime + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String flowDir;
                if (SdcardPath == null) {
                    if (extSdcardPath == null) {
                        main.removeDestopText(mfb);
                        Dialog_Err st = new Dialog_Err();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-err", context.getString(R.string.flow_file_read_failed));
                        st.setArguments(bundle);
                        st.setTargetFragment(Display.newInstance(), 1);
                        st.show(fm, "Dialog_err");
                        return null;
                    } else {
                        flowDir = extSdcardPath;
                    }
                } else {
                    flowDir = SdcardPath;
                }
                String sDir = getFileConfigDir(compName, 0);
                if (sDir == null) {
                    return null;
                } else {
                    flowDir += sDir + "Curve" + "/";
                }
                String path = flowDir;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(path + fileName, false);
                fos.write(info.getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
        }
        return null;
    }

}

