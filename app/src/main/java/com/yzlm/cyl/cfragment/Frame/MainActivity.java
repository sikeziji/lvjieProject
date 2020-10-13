package com.yzlm.cyl.cfragment.Frame;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.AppFunction.GetDevProtectInfo;
import com.yzlm.cyl.cfragment.AppFunction.GetFirstStartInfo;
import com.yzlm.cyl.cfragment.AppFunction.UpdateDevTimesRunnable;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.SerialPort.YangChuang.YcApi;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Content.Component.ContentTool;
import com.yzlm.cyl.cfragment.Content.Component.List1.List1_Content1;
import com.yzlm.cyl.cfragment.Content.Component.List1.List1_Content2;
import com.yzlm.cyl.cfragment.Content.Component.List1.List1_Content3;
import com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content1;
import com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2;
import com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3;
import com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3_2;
import com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content4;
import com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content5;
import com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6;
import com.yzlm.cyl.cfragment.Content.Component.List4.DealList4_Content7;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content1;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content2;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_DataRecovery;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_DevProtect;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_FileNameList;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_FlowConfig;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_SettingConfig;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_UpdateBoard;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_directory;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_p1_0505;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_p2;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pAdminParSet;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pExtKBCoe;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pExtKBCoe2;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pKZCoe;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pRangeCal;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pSpecPeak;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pTOCbfzj;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pTOCcldy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pTOCjldy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pUserParSet;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pUserPassword;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pUserPassword2;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pUserSetting;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pVOCcldy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pas_fxyxx;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pbcpb;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pbfzj;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pcldy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pczrz;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pddcldy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pddcsdy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pddcsdy2;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pddwkcsdy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pfxyxx;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pfyzs;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlbcs;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlbcscdy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlbsz;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlcs;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlcs2;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjldy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_probotset;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pswdxcldy;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pzsbcs;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pzsbsz;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_settingGlucose;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content4_KDHC_RangeSelect;
import com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content6;
import com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content1;
import com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content2;
import com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content3;
import com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content4;
import com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content5;
import com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content6;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1_1;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1_2;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1_3;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1_4;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting2;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting3;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Settingx;
import com.yzlm.cyl.cfragment.Content.Module.ListSetting.List_Content_Setting_p1_1234;
import com.yzlm.cyl.cfragment.Content.Module.ListSetting.List_Content_pfxyxx;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content1;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content2;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content3;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content4;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content5;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content6;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7_p2;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7_pAdminParSet;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7_pDevProtect;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7_pExtKBCoe;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7_pKZCoe;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7_pUserParSet;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7_pczrz;
import com.yzlm.cyl.cfragment.Content.NoBoard.List.NoBoard_List1_Content7_pfxyxx;
import com.yzlm.cyl.cfragment.Content.NoBoard.ListSetting.List_Content_SettingNoBoardx;
import com.yzlm.cyl.cfragment.Dialog.ChangePoint;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_FunctionTest;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_HistoryModify;
import com.yzlm.cyl.cfragment.Dialog.Component.SelectTimeSelectJob2;
import com.yzlm.cyl.cfragment.Dialog.Dialog_LogIn;
import com.yzlm.cyl.cfragment.Dialog.Dialog_password;
import com.yzlm.cyl.cfragment.Display.Display;
import com.yzlm.cyl.cfragment.Display.Display_swdx;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content1;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content2;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_2;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_3;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_4;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_5;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_6;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_7;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_lvjie;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content4;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content5;
import com.yzlm.cyl.cfragment.Frame.Content.componentMeasDisplayLJBean;
import com.yzlm.cyl.cfragment.Frame.ModuleContent.ModuleUp_content1;
import com.yzlm.cyl.cfragment.Frame.NoBoardContent.NoBoardUp_content1;
import com.yzlm.cyl.cfragment.Frame.PublicContent.Bottom_list;
import com.yzlm.cyl.cfragment.Frame.PublicContent.Bottom_list_lvjie;
import com.yzlm.cyl.cfragment.Frame.PublicContent.Bottom_list_setting;
import com.yzlm.cyl.cfragment.Frame.PublicContent.CatchExcep;
import com.yzlm.cyl.cfragment.Frame.PublicContent.Frame;
import com.yzlm.cyl.cfragment.Frame.PublicContent.ScreenContent;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.Server.LocalService;
import com.yzlm.cyl.cfragment.Server.RemoteService;
import com.yzlm.cyl.cfragment.list.Component.Left_list1;
import com.yzlm.cyl.cfragment.list.Component.Left_list2;
import com.yzlm.cyl.cfragment.list.Component.Left_list4;
import com.yzlm.cyl.cfragment.list.Component.Left_list5;
import com.yzlm.cyl.cfragment.list.Component.Left_list_setting;
import com.yzlm.cyl.cfragment.list.ModuleList.Module_Left_list1;
import com.yzlm.cyl.cfragment.list.NoBoard.NoBoard_Left_list1;
import com.yzlm.cyl.cfragment.setting.Up_content_setting;
import com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import weiqian.hardware.HardwareControl;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getDevMeaMode;
import static com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy.getCalcAFormula;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBEnergy.getNoBoardCalcAFormula;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getLastValue;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.isHaveFlow;
import static com.yzlm.cyl.cfragment.Content.Component.List1.List1_Content3.mBtnCfgFlowStart;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.mToggleButtonZNXJ;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_DevProtect.getWinPassword;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_DevProtect.mCKBNumber;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pUserParSet.btnHistoryDateTime;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pfxyxx.mEtCKB;
import static com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting2.mEtSampleTime;
import static com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting2.updateDevStatusTextShow;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCategoryComponent;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.showMainContent;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.showPlatformUpContent;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.getNoBoardLastValue;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateDay;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateHour;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMin;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMonth;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.intToByteArray;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Dialog.Component.Dialog_ExternalKBCoe.resetExtRangeKb;
import static com.yzlm.cyl.cfragment.Dialog.Component.Dialog_ExternalKBCoe2.resetUserExtRangeKb;
import static com.yzlm.cyl.cfragment.Display.Display.Ld;
import static com.yzlm.cyl.cfragment.Display.Display.mTxTControlDevicesInfo;
import static com.yzlm.cyl.cfragment.Display.Display_swdx.Ld_swdx;
import static com.yzlm.cyl.cfragment.File.MFile.fileCopy;
import static com.yzlm.cyl.cfragment.File.MFile.getFileSize;
import static com.yzlm.cyl.cfragment.File.MFile.getFiles;
import static com.yzlm.cyl.cfragment.Global.AnalogQuantity_CycleThread;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.InitComponent;
import static com.yzlm.cyl.cfragment.Global.InitGlobal;
import static com.yzlm.cyl.cfragment.Global.LockDisplayShowFlag;
import static com.yzlm.cyl.cfragment.Global.LockModuleDisplayShowFlag;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.SetMD5;
import static com.yzlm.cyl.cfragment.Global.blAuthenticationFunction;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowWorking;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.extSdcardPath;
import static com.yzlm.cyl.cfragment.Global.getAnalogInQuanlityStatus;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getCompNameCN;
import static com.yzlm.cyl.cfragment.Global.isOffScreenLight;
import static com.yzlm.cyl.cfragment.Global.isOffScreenLightSwitch;
import static com.yzlm.cyl.cfragment.Global.passwordUnlockEffectiveWork;
import static com.yzlm.cyl.cfragment.Global.sampleDoingFlag;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.setAllCompBSLEDStatus;
import static com.yzlm.cyl.cfragment.Global.strAll5721Component;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.strNoBoardComponent;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;
import static com.yzlm.cyl.clibrary.Util.SDCardUtils.getStoragePath;
import static java.lang.Thread.sleep;
import static weiqian.hardware.CustomFunctions.FullScreenSticky;


public class MainActivity extends CSingleFragmentActivity

        implements Bottom_list.Callbacks, Bottom_list_setting.Callbacks, Bottom_list_lvjie.Callbacks,
        List_Content_Settingx.Callbacks, List1_Content1.Callbacks, List1_Content2.Callbacks, List2_Content2.Callbacks, List4_Content1.Callbacks,
        Display.Callbacks, Display_swdx.Callbacks, List2_Content6.Callbacks, Left_list_setting.Callbacks, Left_list1.Callbacks, Left_list2.Callbacks, Left_list4.Callbacks,
        Left_list5.Callbacks, List4_Content3_p1_0505.Callbacks, List4_Content3_pjldy.Callbacks, List4_Content3_pcldy.Callbacks,
        List4_Content3_pgpcldy.Callbacks, List4_Content3_pbfzj.Callbacks, List4_Content3_pTOCbfzj.Callbacks, List4_Content3_pjlcs.Callbacks, List4_Content3_pjlcs2.Callbacks, List4_Content3_p2.Callbacks,
        List4_Content3_pTOCjldy.Callbacks, List4_Content3_pfxyxx.Callbacks, List4_Content3_pas_fxyxx.Callbacks, List4_Content3_pbcpb.Callbacks, List4_Content3_pczrz.Callbacks,
        List4_Content3_pddcldy.Callbacks, List4_Content3_pswdxcldy.Callbacks, List4_Content3_pjlbsz.Callbacks, List4_Content3_pjlbcs.Callbacks, Up_content3.Callbacks,
        Up_content3_2.Callbacks, List5_Content1.Callbacks, List5_Content3.Callbacks, List5_Content4.Callbacks, List5_Content5.Callbacks, List5_Content6.Callbacks,
        CSingleFragmentActivity.DataCallback, List_Content_Setting2.Callbacks, List4_Content3_FlowConfig.Callbacks, List4_Content3_pzsbcs.Callbacks, List4_Content3_pzsbsz.Callbacks, List4_Content3_probotset.Callbacks, List4_Content3_SettingConfig.Callbacks, List4_Content3_UpdateBoard.Callbacks,
        List4_Content3_DataRecovery.Callbacks, Dialog_FunctionTest.Callbacks, List4_Content3_DevProtect.Callbacks, Up_content3_3.Callbacks, Up_content3_7.Callbacks, List4_Content3_pVOCcldy.Callbacks, List4_Content3_pTOCcldy.Callbacks,
        Dialog_HistoryModify.Callbacks, List_Content_Setting_p1_1234.Callbacks, Module_Left_list1.Callbacks, Up_content3_4.Callbacks, Up_content3_5.Callbacks, Up_content3_6.Callbacks,
        List4_Content3_pKZCoe.Callbacks, List4_Content3_pAdminParSet.Callbacks, List4_Content3_pUserParSet.Callbacks, List4_Content3_pSpecPeak.Callbacks,
        List4_Content3_pExtKBCoe.Callbacks, List4_Content3_pExtKBCoe2.Callbacks, List4_Content3_settingGlucose.Callbacks,
        List4_Content3_pUserPassword.Callbacks, List4_Content3_pUserPassword2.Callbacks, List4_Content3_pddwkcsdy.Callbacks, List4_Content3_pfyzs.Callbacks,
        List4_Content3_pddcsdy.Callbacks, List_Content_Setting1.Callbacks, List_Content_Setting1_1.Callbacks, List_Content_Setting1_2.Callbacks, List4_Content3_directory.Callbacks, List4_Content3_FileNameList.Callbacks,
        List_Content_Setting1_3.Callbacks, List_Content_Setting1_4.Callbacks, List4_Content4_KDHC_RangeSelect.Callbacks, List_Content_SettingNoBoardx.Callbacks, NoBoard_Left_list1.Callbacks,
        NoBoard_List1_Content3.Callbacks, NoBoard_List1_Content4.Callbacks, NoBoard_List1_Content5.Callbacks, NoBoard_List1_Content6.Callbacks, NoBoard_List1_Content7_p2.Callbacks, NoBoard_List1_Content7_pfxyxx.Callbacks,
        NoBoard_List1_Content7_pczrz.Callbacks, NoBoard_List1_Content7_pUserParSet.Callbacks, NoBoard_List1_Content7_pAdminParSet.Callbacks, NoBoard_List1_Content7_pDevProtect.Callbacks,
        NoBoard_List1_Content7_pExtKBCoe.Callbacks, NoBoard_List1_Content7_pKZCoe.Callbacks, Dialog_LogIn.Callbacks, List4_Content6.Callbacks, List4_Content3_pjlbcscdy.Callbacks,
        List4_Content3_pRangeCal.Callbacks, List4_Content3_pddcsdy2.Callbacks, List1_Content3.Callbacks, NoBoard_List1_Content7.Callbacks, List_Content_pfxyxx.Callbacks, List4_Content3_pUserSetting.Callbacks, DealList4_Content7.Callbacks,
        Runnable {

    private static final String TAG = "MainActivity";
    //时间
    private TextView textView;
    /* 获取当前组分名的TextView*/
    private TextView textViewCompName;
    private Handler handler;
    private static TextView mTVName;
    private static TextView mtvTitle;

    //安卓屏类型
    public static String phoneName;

    public static String mCompName;// 点击进入界面选中的监测因子
    public static String mNoBoardCompName;// 点击进入界面选中的无背板监测因子
    public static String mBackUpMeasBordName;
    private String NextInto = "";
    public static Button mfb;
    public static MainActivity main;
    public static int RelayStatus = 0;/*继电器输出状态*/
    private Timer mTimer;
    private static Timer offScreenTimer;
    private static CatchExcep application;
    public static int winID = R.id.ImBtn_3;
    private static TextView mTxtLogInName;
    public static Window mActivityWindow;

    public static String passwordRandom = "";
    private Intent intent;

    private boolean isLogin = false;//登录按钮，不能连点

    public static String preWinForDialog = "";// 在进入弹窗界面之前需要知道那个界面进来的，返回的时候判断用
    public static String preWinForFragment = "";   // 在进入界面之前需要知道那个界面进来的，返回的时候判断用
    public static String preWinForImBtn_3 = "";
    private static Timer touchCheckTimer;
    String newpath;
    String kssj;
    String newjssj1;
    String zfmc;
    String zf;
    String wj;
    private static final String COMMAND_BACKUP = "backup";

    //上次点击时间
    long firstClick;

    //TODO 设置默认为绿洁项目
    public static boolean isLvjie = true;
    private int projectId = 3;

    private Fragment createFragment(int id) {
        if (id == 1) {
            return Bottom_list.newInstance();
        } else if (id == 2) {
            return Bottom_list_setting.newInstance();
        } else if (id == 3) {
            return Bottom_list_lvjie.newInstance();
        }
        return null;
    }

    @Override
    protected Fragment createFragment() {
        return Bottom_list.newInstance();
    }

    @Override
    protected int getFrameId() {
        return R.id.All;
    }

    @Override
    protected Fragment getFrameClass() {
        return Frame.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main_h;
    }

    @Override
    protected int getListResId() {
        return R.id.fragment_container;
    }

    @Override
    public void getMainFrame(Fragment fragment) {
        super.getMainFrame(fragment);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void DoThings() {
        /*崩溃捕捉*/
        application = (CatchExcep) getApplication();
        application.init();
        application.addActivity(this);
        application.setMainActivity(MainActivity.this);

        //获取手机名称
        phoneName = android.os.Build.MODEL;

        saveRunInfo2File("开机,系统已运行" + (SystemClock.elapsedRealtimeNanos() / 1000000000 / 60) + "分钟");

        FullScreenSticky(getWindow());

        context = sContext;
        Global.activity = this;
        winID = R.id.ImBtn_3;

        // 主界面根据配置路径变化
        if (blAuthenticationFunction) {
            @SuppressLint("WrongViewCast")
            LinearLayout mMainLayout = (LinearLayout) findViewById(R.id.mainLayout);
            String SdcardPath = (getStoragePath(context, false).size() == 0 ? null : (getStoragePath(context, false).get(0) + File.separator));
            Drawable bckDrawable = Drawable.createFromPath(SdcardPath + "Csoft" + File.separator + "Resource/Image/Background/bj.png");
            if (bckDrawable != null) {
                mMainLayout.setBackground(bckDrawable);
            }
        }
        InitComponent();
        main = this;
        //todo 如果为绿洁项目则添加TAB
        if (isLvjie) {
            getMainFrame(createFragment(projectId));
            showUpContent();
        } else {
            if (strComponent.get(1).length == 1 && QueryMeasCateg(strComponent.get(1)[0]).equals("13")) {
                getFragment(getFrameId(), Display_swdx.newInstance());
            } else {
                getFragment(getFrameId(), Display.newInstance());
            }
        }

        textView = (TextView) findViewById(R.id.time);
        mTVName = (TextView) findViewById(R.id.Name);
        mTVName.setVisibility(View.GONE);

        /*悬浮按钮*/
        mfb = new Button(getApplicationContext());
        mfb.setBackgroundResource(R.drawable.btn_f);
        mfb.setOnClickListener(new mfbClick());
        initDestopText(mfb, 62, 62);
        fixAPx();

        intent = new Intent();
        //设置广播发送隐藏虚拟按键命令
        broadCastFullScreen(true);

        mActivityWindow = getWindow();
        FullWindows(mActivityWindow);

        InitGlobal();
        //初始化数据
        initData();
        /*登录信息初始化**/
        if (!(getPublicConfigData("LogInName").equals("0"))) {
            updatePublicConfigData("LogInName", "0");
            saveOperationLogMsg("公共", "退出登录", ErrorLog.msgType.操作_信息);
        }
        try {
            // 吸光度显示初始化，查询一次是否有数据；
            if (getPublicConfigData("AIsShow").equals("true")) {
                if (strComponent.get(1).length > 0) {
                    for (String item : strComponent.get(1)) {
                        if (!getConfigData(item, "nowHistoryDataAEn").equals("")) {
                            updateConfigData(item, "nowHistoryDataAEn", "");
                        }
                        if (getConfigData(item, "readHistoryDataFlag").equals("false")) {
                            updateConfigData(item, "readHistoryDataFlag", "true");
                        }
                    }
                    if (strComponent.get(1).length > 1) {
                        updatePublicConfigData("AIsShow", "false");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTxtLogInName = (TextView) findViewById(R.id.txtLoginName);
        if (mTxtLogInName != null) {
            mTxtLogInName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView tvValue = (TextView) findViewById(R.id.tvValue);
                    if (tvValue != null) {
                        if (!isLogin) {
                            isLogin = true;

                            main.removeDestopText(mfb);
                            Dialog_LogIn st = new Dialog_LogIn();
                            Bundle bundle = new Bundle();
                            bundle.putString("alert-LogIn", "login");
                            st.setArguments(bundle);
                            st.setTargetFragment(Bottom_list.newInstance(), 1);
                            st.show(fm, "alert-ok");

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        // 等待8秒钟
                                        sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    isLogin = false;
                                }
                            }).start();
                        }
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.please_back_to_first_win), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        passwordRandom = getWinPassword(15);

        handler = new Handler() {

            public void handleMessage(Message msg) {
                textView.setText((String) msg.obj);
                /*外部第一主界面**/
                if (strComponent.get(1).length == 1 && QueryMeasCateg(strComponent.get(1)[0]).equals("13")) {
                    firstSWDXDisplayWin();
                } else {
                    firstDisplayWin();
                }
                try {
                    getSampleStatus();
                } catch (Exception e) {
                    Log.i("except", "获取接口板开关量输入1是否有电平异常!");
                }
                /*测量界面刷新*/
                String Component = mCompName;
                FlowTable ft = Global.getFlows(Component);
                if (ft == null) return;
                ActionTable at = Global.getActions(Component);
                if (at == null) return;
                // 当前状态
                String status = ft.GetStatus(getCmds(Component).getCmd(51).getValue());
                doFlowing.put(Component, status);

                //更新主界面UI
//                updateMainUI(Component, at, status);

                /*测量界面*/
                componentMeasDisplayWin(Component, at, status);
                // 滴定界面刷新
                titrationMainWin(Component);
                // 稀释背板的情况下
                diluteMainWin(Component);
                // 蒸馏背板的情况下
                distillMainWin(Component);
                //计量单元选项
                componentMeteringDisplayWin(Component);
                //测量单元选项
                componentMeasuringUnitDisplayWin(Component);
            }
        };

        new Thread(this).start();

        //同步下位机的系统时间
        sendSysTimeToDev(null);

        if (offScreenTimer == null) {
            offScreenTimer = new Timer();
            setOffScreenTimerTask();
        }
        /*获取延保信息*/
        new GetDevProtectInfo();
        /*首次启动信息*/
        new GetFirstStartInfo();

        if (phoneName.equals("AOSP on weiqian")) {
            HardwareControl.SetBackLight(true);
        }


        mtvTitle = (TextView) findViewById(R.id.tvTitle);
        mtvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Global.LockDisplayShowFlag = false;
                LockModuleDisplayShowFlag = false;
                return false;
            }
        });

        /**
         *  新增读取Excel数据到数据库
         */
        ImportData();

        /**
         * 新增每天备份一次数据
         */
        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    /*String s1 = SdcardPath;
                    String s2 = extSdcardPath;
                    String extSdcard = getExtSdcard();*/
                    if (extSdcardPath != null) {
                        String extSdcardBFPath = extSdcardPath + "备份数据";//数据备份路径
                        File file = new File(extSdcardBFPath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        List<String> files = getFiles("/sdcard/Csoft/", ".db");
                        for (String filePath : files) {
                            backupOrRestoreFiles("/sdcard/Csoft/", filePath, extSdcardBFPath, "ZTWaterData", COMMAND_BACKUP);
                        }
                        /*backupOrRestoreFiles("/sdcard/Csoft/", "ZT_Water_Data.db", a11, "ZTWaterData", COMMAND_BACKUP);
                        backupOrRestoreFiles("/sdcard/Csoft/", "ZT_Water_Config.db", a11, "ZTWaterData", COMMAND_BACKUP);
                        backupOrRestoreFiles("/sdcard/Csoft/", "ZT_Water_CompNoBoardData.db", a11, "ZTWaterData", COMMAND_BACKUP);
                        backupOrRestoreFiles("/sdcard/Csoft/", "ZT_Water_NoBoardConfig.db", a11, "ZTWaterData", COMMAND_BACKUP);*/
                    }
                }
            }, 1000, 24 * 60 * 60 * 1000);
        } catch (Exception e) {
        }
    }

    private void updateMainUI(String component, ActionTable at, String status) {
        Log.e("TAG", "界面更新");
        Handler up_content3_lvjie_handler = Up_content3_lvjie.newInstance().Up_content3_lvjie_Handler;
        Message message = up_content3_lvjie_handler.obtainMessage();
        componentMeasDisplayLJBean bean = new componentMeasDisplayLJBean(component, at, status);
        message.what = 1;
        message.obj = bean;
        up_content3_lvjie_handler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeDestopText(mfb);
        stopService(new Intent(this, LocalService.class));
        stopService(new Intent(this, RemoteService.class));
    }

    /**
     * 主界面显示
     */
    public void showUpContent() {
//        if (!isLvjie) {
        showPlatformUpContent(mCompName);
//        } else {
//            showMainContent();
//        }
    }


    /*外部第一主界面**/
    private void firstDisplayWin() {
        try {
            for (int i = 0; i < Ld.size(); i++) {
                if (i < (strComponent.get(1).length + strNoBoardComponent.get(1).length)) {
                    if (i < strComponent.get(1).length) {
                        try {
                            String unit = getConfigData(strComponent.get(1)[i], "UNIT");
                            ((TextView) Ld.get(i).findViewById(R.id.tvValue)).setText(getLastValue(strComponent.get(1)[i], null, unit));
                            ((TextView) Ld.get(i).findViewById(R.id.ParameterUnit)).setText(unit);
                            ((TextView) Ld.get(i).findViewById(R.id.RangeUnit)).setText(unit);
                            ((TextView) Ld.get(i).findViewById(R.id.SYRangeUnit)).setText(unit);
                            ((TextView) Ld.get(i).findViewById(i)).setText(workState.get(strComponent.get(1)[i]));
                            ((TextView) Ld.get(i).findViewById(R.id.tv_runningMode)).setText(isAutoJob(strComponent.get(1)[i]));

                        } catch (Exception e) {
                            Log.i("except", e.toString());
                        }
                    } else {

                        if (i > 0) {
                            // 无背板配置组分显示， 必须有个背板参数
                            String unit = getNoBoardConfigData(strNoBoardComponent.get(1)[i - strComponent.get(1).length], "UNIT");
                            ((TextView) Ld.get(i).findViewById(R.id.tvValue)).setText(getNoBoardLastValue(strNoBoardComponent.get(1)[i - strComponent.get(1).length], null, unit));
                            ((TextView) Ld.get(i).findViewById(R.id.ParameterUnit)).setText(unit);
                            ((TextView) Ld.get(i).findViewById(R.id.RangeUnit)).setText(unit);
                            ((TextView) Ld.get(i).findViewById(R.id.SYRangeUnit)).setText(unit);
                            ((TextView) Ld.get(i).findViewById(R.id.tv_runningMode)).setText(isAutoJob(strComponent.get(1)[0]));
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            Log.i("e", e.toString());
        }
    }

    /*外部第一主界面**/
    private void firstSWDXDisplayWin() {
        try {
            for (int i = 0; i < Ld_swdx.size(); i++) {
                if (i < (strComponent.get(1).length + strNoBoardComponent.get(1).length)) {
                    if (i < strComponent.get(1).length) {
                        try {
                            String unit = getConfigData(strComponent.get(1)[i], "UNIT");
                            String strLastVal = getLastValue(strComponent.get(1)[i], null, unit);
                            Float fLastVal = Float.parseFloat(strLastVal);
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.tvValue)).setText(strLastVal);
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.ParameterUnit)).setText(unit);
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.SYRangeUnit)).setText(unit);
                            ((TextView) Ld_swdx.get(i).findViewById(i)).setText(workState.get(strComponent.get(1)[i]));
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.tv_runningMode)).setText(isAutoJob(strComponent.get(1)[i]));
                            if (fLastVal < 30) {
                                ((TextView) Ld_swdx.get(i).findViewById(R.id.tvDX)).setText(getResources().getString(R.string.low_toxicity));
                                ((ImageButton) Ld_swdx.get(i).findViewById(R.id.imgBtn_toxicity)).setImageResource(R.drawable.toxicity2);
                            } else if (fLastVal >= 30 && fLastVal < 80) {
                                ((TextView) Ld_swdx.get(i).findViewById(R.id.tvDX)).setText(getResources().getString(R.string.moderate_toxicity));
                                ((ImageButton) Ld_swdx.get(i).findViewById(R.id.imgBtn_toxicity)).setImageResource(R.drawable.toxicity3);
                            } else {
                                ((TextView) Ld_swdx.get(i).findViewById(R.id.tvDX)).setText(getResources().getString(R.string.highly_toxicity));
                                ((ImageButton) Ld_swdx.get(i).findViewById(R.id.imgBtn_toxicity)).setImageResource(R.drawable.toxicity4);
                            }
                        } catch (Exception e) {
                            Log.i("except", e.toString());
                        }
                    } else {

                        if (i > 0) {
                            // 无背板配置组分显示， 必须有个背板参数
                            String unit = getNoBoardConfigData(strNoBoardComponent.get(1)[i - strComponent.get(1).length], "UNIT");
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.tvValue)).setText(getNoBoardLastValue(strNoBoardComponent.get(1)[i - strComponent.get(1).length], null, unit));
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.ParameterUnit)).setText(unit);
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.RangeUnit)).setText(unit);
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.SYRangeUnit)).setText(unit);
                            ((TextView) Ld_swdx.get(i).findViewById(R.id.tv_runningMode)).setText(isAutoJob(strComponent.get(1)[0]));
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            Log.i("e", e.toString());
        }
    }


    /*
    常规内容，测量主界面
    * */
    private void componentMeasDisplayWin(String component, ActionTable at, String status) {
        try {
            TextView mTvM_SCCL = (TextView) findViewById(R.id.M_SCCL);
            if (mTvM_SCCL != null) {

                boolean wait = (status.equals(getString(R.string.waiting_for_instructions)) || status.equals(getString(R.string.selfCheck)));
                int iAction = 0;
                try {
                    iAction = Integer.parseInt(getCmds(component).getCmd(52).getValue().toString());
                } catch (Exception e) {
                    iAction = 0;
                }
                String action = at.GetActionName(getCmds(component).getCmd(52).getValue());
                String actionNext = at.GetActionName(getCmds(component).getCmd(53).getValue());
                String ActionTime = getCmds(component).getCmd(62).getValue() == null ? "" : " " + getCmds(component).getCmd(62).getValue() + "s";
                Log.v("FLOW", "MAINACTIVITY:" + component);

                String Unit = getConfigData(component, "UNIT");

                TextView mTvM_SYLC = (TextView) findViewById(R.id.M_SYLC);
                String Range = getConfigData(component, "RANGE");
                if (!wait) {
                    if (!getConfigData(component, "PreCalSelectRange").equals("") && !getConfigData(component, "PreCalSelectRange").equals(getConfigData(component, "RANGE"))) {
                        Range = getConfigData(component, "PreCalSelectRange");
                    }
                }
                int point = Integer.parseInt(getConfigData(component, "YXWS"));
                mTvM_SCCL.setText(getString(R.string.beforeMea) + "： " + getLastValue(component, null, Unit) + " " + Unit);
                mTvM_SCCL.setText(getString(R.string.beforeMea) + "： " + getLastValue(component, null, Unit) + " " + Unit);
                String LCL = ConvertUnit("mg/L", Unit, (getConfigData(component, "LC" + Range + "L")), point);
                String LCH = ConvertUnit("mg/L", Unit, (getConfigData(component, "LC" + Range + "H")), point);

                mTvM_SYLC.setText(getString(R.string.use_range) + "： " + LCL + "-" + LCH + " " + Unit);
                TextView mTvM_DQZT = (TextView) findViewById(R.id.M_DQZT);
                mTvM_DQZT.setText(getString(R.string.current_status) + "： " + status);
                TextView mTvM_DQDZ = (TextView) findViewById(R.id.M_DQDZ);
                mTvM_DQDZ.setText(getString(R.string.current_action) + "： " + (wait ? (action.equals(getString(R.string.ready)) ? "——————" : action + ActionTime) : action + ActionTime));
                TextView mTvM_XYDZ = (TextView) findViewById(R.id.M_XYDZ);
                mTvM_XYDZ.setText(getString(R.string.next_action) + "： " + (wait ? (actionNext.equals(getString(R.string.ready)) ? "——————" : actionNext) : actionNext));
                TextView mTvM_SYBZ = (TextView) findViewById(R.id.M_SYBZ);
                mTvM_SYBZ.setText(getString(R.string.steps_left) + "： " + ((wait || getCmds(component).getCmd(61).getValue() == null) ? "——————" : getCmds(component).getCmd(61).getValue()));
                TextView mTvM_ZYMS = (TextView) findViewById(R.id.M_ZYMS);

                mTvM_ZYMS.setText(getString(R.string.meaMode) + "： " + (isAutoJob(component)));
                TextView mTvM_XJWD = (TextView) findViewById(R.id.M_XJWD);

                if (isHaveMeasCategory(component, "6")) {
                    mTvM_XJWD.setText(getString(R.string.ambient_temperature) + "： " + (getCmds(component).getCmd(55).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(component).getCmd(55).getValue())));
                } else {
                    if (QueryMeasCateg(component).equals("3") && component.equals("CODmn")) {
                        mTvM_XJWD.setText(getString(R.string.digestion_Tube) + getString(R.string.Outside_temperature) + "： " + (getCmds(component).getCmd(55).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(component).getCmd(55).getValue())) + "\n" + getString(R.string.digestion_Tube) + getString(R.string.Inner_temperature) + "： " + (getCmds(component).getCmd(601).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(component).getCmd(601).getValue())));
                    } else if (QueryMeasCateg(component).equals("11")) {
                        mTvM_XJWD.setText(getString(R.string.burner_temperature) + "： " + (getCmds(component).getCmd(55).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(component).getCmd(55).getValue())));
                        //TC流量值
                        /*TextView mTvM_TCLL = (TextView) findViewById(R.id.M_TCLL);
                        mTvM_TCLL.setText(getString(R.string.tc_flow) + "： " + (getCmds(component).getCmd(9504).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(component).getCmd(9504).getValue())));*/
                    } else if (QueryMeasCateg(component).equals("13")) {
                        mTvM_XJWD.setText(getString(R.string.reaction_module_temperature) + "： " + (getCmds(component).getCmd(55).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(component).getCmd(55).getValue())));
                        TextView mTvM_CJSWD2 = (TextView) findViewById(R.id.M_CJSWD2);
                        mTvM_CJSWD2.setText(getString(R.string.cooling_module_temperature) + "： " + (getCmds(component).getCmd(601).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(component).getCmd(601).getValue())));
                    } else {
                        mTvM_XJWD.setText(getString(R.string.digestion_temperature) + "： " + (getCmds(component).getCmd(55).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(component).getCmd(55).getValue())));
                    }
                }

                TextView mTvM_CLZT = (TextView) findViewById(R.id.M_CLZT);
                mTvM_CLZT.setVisibility(View.GONE);

                // 下次启动时间
                showNextDoJobInfo(component, status);
                // 下次启动量程
                //showNextDoJobUseRange(component);

                if (!QueryMeasCateg(component).equals("13")) {
                    /*比色，光谱显示一下内容*/
                    TextView mTvM_Ljl = (TextView) findViewById(R.id.M_Ljl);
                    if (mTvM_Ljl != null) {
                        mTvM_Ljl.setText((getCmds(component).getCmd(56).getValue() == null ? "" : getCmds(component).getCmd(56).getValue()).toString());
                    }
                    TextView mTvM_Hjl = (TextView) findViewById(R.id.M_Hjl);
                    if (mTvM_Hjl != null) {
                        mTvM_Hjl.setText((getCmds(component).getCmd(57).getValue() == null ? "" : "/" + getCmds(component).getCmd(57).getValue()));
                    }
                }
                TextView mTvM_nl = (TextView) findViewById(R.id.M_nl);
                if (mTvM_nl != null) {
                    LinearLayout layout_M_nl = (LinearLayout) findViewById(R.id.layout_M_nl);
                    if (getConfigData(component, "BS_LED").equals("open")) {
                        layout_M_nl.setVisibility(VISIBLE);
                        mTvM_nl.setText((getCmds(component).getCmd(59).getValue() == null ? "" : getCmds(component).getCmd(59).getValue()).toString());
                    } else {
                        if ((iAction == 519 || iAction == 520) ||
                                (getConfigData(component, "ZNXJ").equals("open") &&
                                        (iAction == 514 || iAction == 515))) {//此动作号显示
                            layout_M_nl.setVisibility(VISIBLE);
                            mTvM_nl.setText((getCmds(component).getCmd(59).getValue() == null ? "" : getCmds(component).getCmd(59).getValue()).toString());
                        } else {
                            layout_M_nl.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                try {
                    LinearLayout layoutA = (LinearLayout) findViewById(R.id.layout_A);
                    if (layoutA != null) {
                        TextView mTvM_A = (TextView) findViewById(R.id.M_A);
                        if (mTvM_A != null) {
                            String sA = "";
                            if (getPublicConfigData("AIsShow").equals("true")) {
                                if (getConfigData(component, "readHistoryDataFlag").equals("false")) {
                                    sA = getConfigData(component, "nowHistoryDataAEn");
                                    if (!sA.equals("")) {
                                        try {
                                            // 判断是否为数字
                                            double dA = Double.valueOf(sA);
                                            sA = editDataFormat(sA, point);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                                mTvM_A.setText(sA);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ImageButton mJJTZ = (ImageButton) findViewById(R.id.btn_jjtz);

                if ((!wait) || ((status.equals(getString(R.string.waiting_for_instructions)) && (!action.equals(getString(R.string.ready)) && !action.equals("——————"))))) {
                    mJJTZ.setVisibility(VISIBLE);
                    if (getConfigData(component, "zhcsSwitch").equals("true")) {
                        mTvM_CLZT.setVisibility(VISIBLE);
                    }
                } else {
                    mJJTZ.setVisibility(View.GONE);
                    if (getConfigData(component, "zhcsFlow").equals("")) {
                        if (getConfigData(component, "zhcsSwitch").equals("true")) {
                            updateConfigData(component, "zhcsSwitch", "false");
                            updateConfigData(component, "zhcsStartFlag", "false");
                        }
                    }
                }
                LinearLayout mLayoutCfgFlowSwitch = (LinearLayout) findViewById(R.id.layout_cfgFlowSwitch);
                if (mLayoutCfgFlowSwitch != null) {
                    // 显示配置流程
                    boolean blCfgFlowEnable = getConfigData(component, "CFG_Flow_Switch").equals("open");
                    mLayoutCfgFlowSwitch.setVisibility(blCfgFlowEnable ? VISIBLE : View.GONE);
                }
                //主界面信息显示（延保等）
                showMainMsg(component, (mJJTZ.getVisibility() == View.VISIBLE), (mLayoutCfgFlowSwitch.getVisibility() == View.VISIBLE));
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 主界面刷新异常");
        }
    }

    /*
     * 蒸馏背板主界面
     * **/
    private void distillMainWin(String component) {
        try {
            if (isHaveMeasCategory(component, "6")) {
                String strComponentModule = component + getModuleName("6");
                TextView mTvM_Module_DQDZ = (TextView) findViewById(R.id.M_Module_ZL_DQDZ);
                if (mTvM_Module_DQDZ != null) {
                    FlowTable ftModule = Global.getFlows(strComponentModule);
                    if (ftModule != null) {
                        String moduleStatus = ftModule.GetStatus(getCmds(strComponentModule).getCmd(51).getValue());
                        boolean moduleWait = (moduleStatus.equals(getString(R.string.waiting_for_instructions)) || moduleStatus.equals(getString(R.string.selfCheck)));

                        ActionTable atModule = Global.getActions(strComponentModule);
                        if (atModule != null) {
                            FlowTable ft = Global.getFlows(strComponentModule);
                            if (ft == null) return;
                            String status = ft.GetStatus(getCmds(strComponentModule).getCmd(51).getValue());
                            TextView mTvZL_GZZT = (TextView) findViewById(R.id.M_Module_ZL_GZZT);
                            mTvZL_GZZT.setText(getString(R.string.working_condition) + "： " + workState.get(strComponentModule));
                            TextView mTvZL_DQZT = (TextView) findViewById(R.id.M_Module_ZL_DQZT);
                            mTvZL_DQZT.setText(getString(R.string.current_status) + "： " + status);

                            String moduleAction = atModule.GetActionName(getCmds(strComponentModule).getCmd(52).getValue());
                            String moduleActionNext = atModule.GetActionName(getCmds(strComponentModule).getCmd(53).getValue());
                            String moduleActionTime = getCmds(strComponentModule).getCmd(62).getValue() == null ? "" : " " + getCmds(strComponentModule).getCmd(62).getValue() + "s";
                            mTvM_Module_DQDZ.setText(getString(R.string.current_action) + "： " + (moduleWait ? (moduleAction.equals(getString(R.string.ready)) ? "——————" : moduleAction + moduleActionTime) : moduleAction + moduleActionTime));
                            TextView mTvM_Module_XYDZ = (TextView) findViewById(R.id.M_Module_ZL_XYDZ);
                            mTvM_Module_XYDZ.setText(getString(R.string.next_action) + "： " + (moduleWait ? (moduleActionNext.equals(getString(R.string.ready)) ? "——————" : moduleActionNext) : moduleActionNext));
                            TextView mTvM_Module_SYBZ = (TextView) findViewById(R.id.M_Module_ZL_SYBZ);
                            mTvM_Module_SYBZ.setText(getString(R.string.steps_left) + "： " + ((moduleWait || getCmds(strComponentModule).getCmd(61).getValue() == null) ? "——————" : getCmds(strComponentModule).getCmd(61).getValue()));

                            TextView mTvZL_WD = (TextView) findViewById(R.id.M_Module_ZL_ZLWD);

                            mTvZL_WD.setText(getString(R.string.distillTubeTemper) + "： " + (getCmds(strComponentModule).getCmd(55).getValue() == null ? "——————" : String.format("%.1f ℃", (float) getCmds(strComponentModule).getCmd(55).getValue())));

                            ImageButton mModule_JJTZ = (ImageButton) findViewById(R.id.btn_Module_ZL_jjtz);
                            if ((!moduleWait) || ((moduleStatus.equals(getString(R.string.waiting_for_instructions)) && (!moduleAction.equals(getString(R.string.ready)) && !moduleAction.equals("——————"))))) {
                                mModule_JJTZ.setVisibility(VISIBLE);
                            } else {
                                mModule_JJTZ.setVisibility(View.GONE);
                            }
                            LinearLayout mLayoutZLCfgFlowSwitch = (LinearLayout) findViewById(R.id.layout_moduleZLCfgFlowSwitch);
                            if (mLayoutZLCfgFlowSwitch != null) {
                                // 显示配置流程
                                boolean blCfgFlowEnable = getConfigData(strComponentModule, "CFG_Flow_Switch").equals("open");
                                mLayoutZLCfgFlowSwitch.setVisibility(blCfgFlowEnable ? VISIBLE : View.GONE);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 蒸馏主界面刷新异常" + e.toString());
        }
    }

    /*
     * 稀释背板主界面
     * **/
    private void diluteMainWin(String component) {
        try {
            if (isHaveMeasCategory(component, "5")) {
                String strComponentModule = component + getModuleName("5");
                TextView mTvM_Module_DQDZ = (TextView) findViewById(R.id.M_Module_XS_DQDZ);
                if (mTvM_Module_DQDZ != null) {
                    FlowTable ftModule = Global.getFlows(strComponentModule);
                    if (ftModule != null) {
                        String moduleStatus = ftModule.GetStatus(getCmds(strComponentModule).getCmd(51).getValue());
                        boolean moduleWait = (moduleStatus.equals(getString(R.string.waiting_for_instructions)) || moduleStatus.equals(getString(R.string.selfCheck)));

                        ActionTable atModule = Global.getActions(strComponentModule);
                        if (atModule != null) {
                            String moduleAction = atModule.GetActionName(getCmds(strComponentModule).getCmd(52).getValue());
                            String moduleActionNext = atModule.GetActionName(getCmds(strComponentModule).getCmd(53).getValue());
                            String moduleActionTime = getCmds(strComponentModule).getCmd(62).getValue() == null ? "" : " " + getCmds(strComponentModule).getCmd(62).getValue() + "s";
                            mTvM_Module_DQDZ.setText(getString(R.string.current_action) + "： " + (moduleWait ? (moduleAction.equals(getString(R.string.ready)) ? "——————" : moduleAction + moduleActionTime) : moduleAction + moduleActionTime));
                            TextView mTvM_Module_XYDZ = (TextView) findViewById(R.id.M_Module_XS_XYDZ);
                            mTvM_Module_XYDZ.setText(getString(R.string.next_action) + "： " + (moduleWait ? (moduleActionNext.equals(getString(R.string.ready)) ? "——————" : moduleActionNext) : moduleActionNext));
                            TextView mTvM_Module_SYBZ = (TextView) findViewById(R.id.M_Module_XS_SYBZ);
                            mTvM_Module_SYBZ.setText(getString(R.string.steps_left) + "： " + ((moduleWait || getCmds(strComponentModule).getCmd(61).getValue() == null) ? "——————" : getCmds(strComponentModule).getCmd(61).getValue()));
                            ImageButton mModule_JJTZ = (ImageButton) findViewById(R.id.btn_Module_XS_jjtz);
                            if ((!moduleWait) || ((moduleStatus.equals(getString(R.string.waiting_for_instructions)) && (!moduleAction.equals(getString(R.string.ready)) && !moduleAction.equals("——————"))))) {
                                mModule_JJTZ.setVisibility(VISIBLE);
                            } else {
                                mModule_JJTZ.setVisibility(View.GONE);
                            }
                            LinearLayout mLayoutXSCfgFlowSwitch = (LinearLayout) findViewById(R.id.layout_moduleXSCfgFlowSwitch);
                            if (mLayoutXSCfgFlowSwitch != null) {
                                // 显示配置流程
                                boolean blCfgFlowEnable = getConfigData(strComponentModule, "CFG_Flow_Switch").equals("open");
                                mLayoutXSCfgFlowSwitch.setVisibility(blCfgFlowEnable ? VISIBLE : View.GONE);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 稀释主界面刷新异常" + e.toString());
        }
    }

    /*
     * 滴定主界面
     * **/
    private void titrationMainWin(String component) {
        if (QueryMeasCateg(component).equals("3") || QueryMeasCateg(component).equals("8")) {
            try {
                int point = Integer.parseInt(getConfigData(component, "YXWS"));
                TextView mTvCountEng = (TextView) findViewById(R.id.txtCount_ENG);
                if (mTvCountEng != null) {
                    mTvCountEng.setText((getCmds(component).getCmd(59).getValue() == null ? "" : getCmds(component).getCmd(59).getValue()).toString());

                    TextView mTvRelVolume = (TextView) findViewById(R.id.txtRelVolume);
                    String strValue = (getCmds(component).getCmd(66).getValue() == null ? "" : getCmds(component).getCmd(66).getValue()).toString();
                    mTvRelVolume.setText(strValue.equals("") ? "" : editDataFormat(strValue, point));

                    TextView mTvOrpDifVal = (TextView) findViewById(R.id.txtORPDifValue);
                    mTvOrpDifVal.setText((getCmds(component).getCmd(65).getValue() == null ? "" : getCmds(component).getCmd(65).getValue()).toString());

                    TextView mTvOrpEng = (TextView) findViewById(R.id.txtORPEng);
                    mTvOrpEng.setText((getCmds(component).getCmd(64).getValue() == null ? "" : getCmds(component).getCmd(64).getValue()).toString());
                }

            } catch (Exception e) {
                saveExceptInfo2File("组分[" + component + "] 滴定主界面刷新异常" + e.toString());
            }
        }
    }

    //测量单元选项
    private void componentMeasuringUnitDisplayWin(String component) {

        try {
            if (QueryMeasCateg(component).equals("13")) {
                return;
            }
            ToggleButton mTb_pcldy_sscl = (ToggleButton) findViewById(R.id.tBtnCLDY_sscl);
            if (mTb_pcldy_sscl != null) {
                TextView mTvTemp = (TextView) findViewById(R.id.cldy_temp);
                TextView mTvEng = (TextView) findViewById(R.id.cldy_eng);
                TextView mTxtreferenceEng = (TextView) findViewById(R.id.cldy_engReference);
                if (mTb_pcldy_sscl.isChecked()) {
                    mTvTemp.setText(getCmds(component).getCmd(55).getValue() == null ? "" : String.format("%.1f ℃", (float) getCmds(component).getCmd(55).getValue()));
                    mTvEng.setText((getCmds(component).getCmd(59).getValue() == null ? "" : getCmds(component).getCmd(59).getValue()).toString());

                    if (QueryMeasBoardType(component).equals("2")) {
                        mTxtreferenceEng.setVisibility(View.VISIBLE);
                        mTxtreferenceEng.setText((getCmds(component).getCmd(68).getValue() == null ? "" : getCmds(component).getCmd(68).getValue()).toString());
                    } else {
                        mTxtreferenceEng.setVisibility(View.GONE);
                    }
                } else {
                    mTvTemp.setText("");
                    mTvEng.setText("");
                    mTxtreferenceEng.setText("");
                }
                /*TextView mTvgddpEng = (TextView) findViewById(R.id.cldy_gddpnl);
                mTvgddpEng.setText(getCmdsValue(component, 9602) + "/" + getCmdsValue(component, 9603) + "/" + getCmdsValue(component, 9604) + "/" + getCmdsValue(component, 9605));*/
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 测量单元选项界面刷新异常");
        }
    }

    //计量单元选项
    private void componentMeteringDisplayWin(String component) {
        try {
            ToggleButton mTb_pjldy_sscl = (ToggleButton) findViewById(R.id.tBtn_pjldy_sscl);
            if (mTb_pjldy_sscl != null) {

                TextView mTvHLjl3 = (TextView) findViewById(R.id.tvHLjl3);
                if (getCmds(component).getCmd(5600).getValue() == null) {
                    if (mTb_pjldy_sscl.isChecked()) {
                        mTvHLjl3.setText((getCmds(component).getCmd(56).getValue() == null ? "" : getCmds(component).getCmd(56).getValue()).toString()
                                + "  /  " + (getCmds(component).getCmd(57).getValue() == null ? "" : getCmds(component).getCmd(57).getValue()).toString());
                    } else {
                        mTvHLjl3.setText("");
                    }
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 计量单元选项界面刷新异常");
        }
    }

    /*
     * 当id为1时，跳转至8000项目界面，当id为2时，跳转至5722 平台界面
     */
    @Override
    public void onDisplaySelected(View view, int id) {
        FullWindows(mActivityWindow);

        if (id == 1 || id == 2) {
            /*获取当前组分名的文本框（WL）*/
            textViewCompName = (TextView) view.findViewById(R.id.Name);
            boolean reNewSub = !(NextInto.equals(textViewCompName.getText().toString()));
            if (reNewSub) NextInto = textViewCompName.getText().toString();
            mTVName.setText(getCompNameCN(textViewCompName.getText().toString()));
            mTVName.setVisibility(VISIBLE);
            getMainFrame(createFragment(id));
            if (id == 1) {
                mCompName = textViewCompName.getText().toString();
                showUpContent();
            } else if (id == 2) {
                mNoBoardCompName = textViewCompName.getText().toString();
                getFragment(R.id.fragment_up_container, NoBoardUp_content1.newInstance(true, true));
            }
        }
        winID = view.getId();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        String compName = strComponent.get(1)[0];
        boolean reNewSub = !(NextInto.equals(compName));
        if (reNewSub) NextInto = compName;
        mTVName.setText(getCompNameCN(compName));
        mTVName.setVisibility(VISIBLE);
        mCompName = compName;
    }


    /*
     * 下导航栏
     */
    @Override
    public void onSelected(View view) {

        try {
            long lastClick = System.currentTimeMillis();
            // 两次点击小于300ms 也就是连续点击
            if (lastClick - firstClick < 300 && lastClick - firstClick > 0) {
                return;
            }
            firstClick = lastClick;
            switch (view.getId()) {
                case R.id.ImBtn_1:
                    if (getModePermissions(MainActivity.mCompName, "运行") && !LockDisplayShowFlag) {
//                        getContentFragment(R.id.fragment_up_container, Up_content1.newInstance(false, true));
                        cheackFrameUpadteUI(Up_content1.newInstance(false, true), 1);
                    }
                    break;
                case R.id.ImBtn_2:
                    if (!LockDisplayShowFlag) {
//                        getContentFragment(R.id.fragment_up_container, Up_content2.newInstance(false, true));
                        cheackFrameUpadteUI(Up_content2.newInstance(false, true), 1);
                    }
                    break;
                case R.id.ImBtn_3:
                    touchImgBtn3();
                    break;
                case R.id.ImBtn_4:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.selfCheck)) || LockDisplayShowFlag) {
                        break;
                    }
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
//                        getContentFragment(R.id.fragment_up_container, Up_content4.newInstance());
                        cheackFrameUpadteUI(Up_content4.newInstance(), 1);
                    } else {
                        removeDestopText(mfb);
                        Dialog_password st = new Dialog_password();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-info", "List4-3");
                        st.setArguments(bundle);
                        st.setTargetFragment(Bottom_list.newInstance(), 1);
                        st.show(fm, "Dialog_password");
                    }
                    break;
                case R.id.ImBtn_5:
                    if (!LockDisplayShowFlag) {
                        cheackFrameUpadteUI(Up_content5.newInstance(false, true), 1);
//                        getContentFragment(R.id.fragment_up_container, Up_content5.newInstance(false, true));
                    }
                    break;
            }
        } catch (IllegalArgumentException e) {
            Log.i("12", "33");
        }
    }

    private void cheackFrameUpadteUI(SubFragment fragment, int id) {
        getMainFrame(createFragment(id));
        Frame.updateUI(false);
        getContentFragment(R.id.fragment_up_container, fragment);
    }

    private void touchImgBtn3() {
        if (!LockModuleDisplayShowFlag) {
            if (!LockDisplayShowFlag) {
                try {
                    mCompName = textViewCompName.getText().toString();
                } catch (Exception e) {
                    Log.i("except", e.toString());
                }
                winID = R.id.ImBtn_3;
                Message msg = new Message();
                msg.what = 2;

                WinWidgetHandler.sendMessage(msg);

                if (isLvjie) {
                    getMainFrame(createFragment(projectId));
                    Frame.updateUI(true);
                    showUpContent();
                    System.out.println("界面显示成功");
                } else {
                    if (strComponent.get(1).length == 1 && QueryMeasCateg(strComponent.get(1)[0]).equals("13")) {
                        getFragment(getFrameId(), Display_swdx.newInstance());
                    } else {
                        getFragment(getFrameId(), Display.newInstance());
                    }
                }
//                if (preWinForImBtn_3.equals("Bottom_list")) {
//                    replaceFragment(Bottom_list.newInstance(), false, R.id.All, Display.newInstance());
//                } else if (preWinForImBtn_3.equals("Bottom_list_setting")) {
//                    replaceFragment(Bottom_list_setting.newInstance(), false, R.id.All, Display.newInstance());
//                }

                AnalogQuantity_CycleThread.resume();
            } else if (QueryMeasCateg(mCompName).equals("5") || QueryMeasCateg(mCompName).equals("6")) {
                if (isLvjie) {
                    getMainFrame(createFragment(1));
                    showUpContent();
                } else {
                    getFragment(R.id.fragment_up_container, new List_Content_Setting_p1_1234());
                }
                Global.LockDisplayShowFlag = false;
                LockModuleDisplayShowFlag = false;
                try {
                    mCompName = textViewCompName.getText().toString();
                } catch (Exception E) {
                    Log.i("except", E.toString());
                }
            }
        }
    }

    /*
     * 内部左边导航栏
     */
    @Override
    public void onListSelected(View view) {
        try {
            switch (view.getId()) {
                case R.id.Left1_1:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions)) && !LockDisplayShowFlag) {
                        if (getModePermissions(MainActivity.mCompName, "运行")) {
                            getFragment(R.id.Content1, new List1_Content1());//界面不存内存
                        }
                    }
                    break;
                case R.id.Left1_2:
                    getFragment(R.id.Content1, new List1_Content2());
                    break;
                case R.id.Left1_3:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions)) && !LockDisplayShowFlag) {
                        if (getModePermissions(MainActivity.mCompName, "运行")) {
                            getFragment(R.id.Content1, new List1_Content3());
                        }
                    }
                    break;
                case R.id.Left2_1:
                    getFragment(R.id.Content2, new List2_Content1());
                    break;
                case R.id.Left2_2:
                    getFragment(R.id.Content2, new List2_Content2());
                    break;
                case R.id.Left2_3:
                    if (QueryMeasCateg(mCompName).equals("4")) {
                        getFragment(R.id.Content2, new List2_Content3_2());
                    } else {
                        getFragment(R.id.Content2, new List2_Content3());
                    }
                    break;
                case R.id.Left2_4:
                    getFragment(R.id.Content2, new List2_Content4());
                    break;
                case R.id.Left2_5:
                    getFragment(R.id.Content2, new List2_Content5());
                    break;
                case R.id.Left2_6:
                    getFragment(R.id.Content2, new List2_Content6());
                    break;
                case R.id.Left4_1:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions)) && !LockDisplayShowFlag) {
                        if (getModePermissions(MainActivity.mCompName, "功能测试")) {
                            getFragment(R.id.Content4, new List4_Content1());
                        }
                    }
                    break;
                case R.id.Left4_2:
                    if (!LockDisplayShowFlag) {
                        getFragment(R.id.Content4, new List4_Content2());
                    }
                    break;
                case R.id.Left4_3: {
                    if (!LockDisplayShowFlag) {
                        removeDestopText(mfb);
                        Dialog_password st = new Dialog_password();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-info", "List4-3");
                        st.setArguments(bundle);
                        st.setTargetFragment(Left_list4.newInstance(), 1);
                        st.show(fm, "Dialog_password");
                    }
                }
                break;
                case R.id.Left4_4:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions)) && !LockDisplayShowFlag) {
                        float tempMax;
                        if (QueryMeasCateg(mCompName).equals("3") ||
                                QueryMeasCateg(mCompName).equals("4") || QueryMeasCateg(mCompName).equals("8") || QueryMeasCateg(mCompName).equals("13")) {
                            tempMax = 80.0f;
                        } else if (QueryMeasCateg(mCompName).equals("11")) {
                            tempMax = 700.0f;
                        } else {
                            tempMax = 60.0f;
                        }
                        if ((getCmds(MainActivity.mCompName).getCmd(55).getValue() == null ? 0.0f : (float) getCmds(MainActivity.mCompName).getCmd(55).getValue()) < tempMax) {
                            if (getModePermissions(MainActivity.mCompName, "用户管理0505"))
                                getContentFragment(R.id.Content4, List4_Content3_p1_0505.newInstance());
                        }
                    }
                    break;
                case R.id.Left4_5:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        if (getModePermissions(MainActivity.mCompName, "用户管理302")) {
                            getFragment(R.id.Content4, List4_Content3_p2.newInstance());
                        }
                    }
                    break;
                case R.id.Left4_6:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        if (getModePermissions(MainActivity.mCompName, "用户管理123")) {
                            getFragment(R.id.Content4, List4_Content6.newInstance());
                        }
                    }
                    break;
                case R.id.Left4_7:
                    getFragment(R.id.Content4, new DealList4_Content7());
                    break;
                case R.id.btn_turbidity_compensation:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        try { // 算法里有浊度系数 KZ1
                            String calcARevise = getCalcAFormula(mCompName, Byte.valueOf(getConfigData(mCompName, "RANGE")));
                            if (calcARevise.contains("KZ1")) {
                                if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                    getContentFragment(R.id.Content4, List4_Content3_pKZCoe.newInstance());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.btn_homing_peak:
                    if (mCompName.equals("TN") & doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getContentFragment(R.id.Content4, new List4_Content3_pSpecPeak());
                    }
                    break;
                case R.id.btn_kdhc_range_select:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        if (isHaveFlow(mCompName, context.getResources().getString(R.string.KDHC), null, GetPlatRangSum(mCompName))) {
                            getContentFragment(R.id.Content4, new List4_Content4_KDHC_RangeSelect());
                        }
                    }
                    break;
                case R.id.btn_flow_cfg_setting:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getContentFragment(R.id.Content4, new List4_Content3_FlowConfig());
                    }
                    break;
                case R.id.btn_range_cal_setting:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getContentFragment(R.id.Content4, List4_Content3_pRangeCal.newInstance(true));
                    }
                    break;
                case R.id.btn_update_config:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getContentFragment(R.id.Content4, new List4_Content3_SettingConfig());
                    }
                    break;
                case R.id.btn_upgrade_board:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getContentFragment(R.id.Content4, new List4_Content3_UpdateBoard());
                    }
                    break;
                //数据恢复按钮
                case R.id.btn_sjhf:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getContentFragment(R.id.Content4, new List4_Content3_DataRecovery());
                    }
                    break;
                case R.id.btn_directory:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getContentFragment(R.id.Content4, List4_Content3_directory.newInstance());
                    }
                    break;
                case R.id.btn_fileinfo:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getContentFragment(R.id.Content4, List4_Content3_FileNameList.newInstance());
                    }
                    break;
                case R.id.btnJLDY:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        String measCategory = QueryMeasCateg(textViewCompName.getText().toString());

                        switch (measCategory) {
                            case "11":
                                getFragment(R.id.Content4, new List4_Content3_pTOCjldy());
                                break;
                            default:
                                getFragment(R.id.Content4, new List4_Content3_pjldy());
                                break;
                        }

                    }
                    break;
                case R.id.btnCLDY:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        /* 若当前组分的测量单元类型为1，则显示比色界面，若为2，则显示光谱界面*/
                        String measCategory = QueryMeasCateg(textViewCompName.getText().toString());

                        switch (measCategory) {
                            case "2":
                            case "7":
                            case "12":
                                getFragment(R.id.Content4, List4_Content3_pgpcldy.newInstance());
                                break;
                            case "3":
                            case "8":
                                getFragment(R.id.Content4, List4_Content3_pddcldy.newInstance());
                                break;
                            case "13":
                                getFragment(R.id.Content4, List4_Content3_pswdxcldy.newInstance());
                                break;
                            case "4":
                                getFragment(R.id.Content4, List4_Content3_pVOCcldy.newInstance());
                                break;
                            case "11":
                                getFragment(R.id.Content4, new List4_Content3_pTOCcldy());
                                break;
                            default:
                                getFragment(R.id.Content4, new List4_Content3_pcldy());
                                break;
                        }
                    }
                    break;
                case R.id.btnBFZJ://泵阀组件
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        String measCategory = QueryMeasCateg(textViewCompName.getText().toString());

                        switch (measCategory) {
                            case "11":
                                getFragment(R.id.Content4, new List4_Content3_pTOCbfzj());
                                break;
                            default:
                                getFragment(R.id.Content4, new List4_Content3_pbfzj());
                                break;
                        }
                    }
                    break;
                case R.id.btnJLCS:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        String measCategory = QueryMeasCateg(textViewCompName.getText().toString());

                        switch (measCategory) {
                            case "12":
                                getFragment(R.id.Content4, new List4_Content3_pjlcs2());
                                break;
                            default:
                                getFragment(R.id.Content4, new List4_Content3_pjlcs());
                                break;
                        }

                    }
                    break;
                case R.id.btnJLBSZ:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_pjlbsz.newInstance());
                    }
                    break;
                case R.id.btnJLBCS:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_pjlbcs.newInstance());
                    }
                    break;
                case R.id.btnDDCS:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        if (QueryMeasCateg(mCompName).equals("3")) {
                            getFragment(R.id.Content4, List4_Content3_pddcsdy.newInstance());
                        } else {
                            getFragment(R.id.Content4, List4_Content3_pddcsdy2.newInstance());
                        }
                    }
                    break;
                case R.id.btnWKCS:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_pddwkcsdy.newInstance());
                    }
                    break;
                case R.id.btnJLBCS_C:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_pjlbcscdy.newInstance());
                    }
                    break;
                case R.id.btnZSBCS:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_pzsbcs.newInstance());
                    }
                    break;
                case R.id.btnROBOT:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_probotset.newInstance());
                    }
                    break;
                case R.id.btnZSBSZ:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_pzsbsz.newInstance());
                    }
                    break;
                case R.id.btnReturn_p0:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        Left_list1.newInstance(false).selectFirst();
                        getFragment(R.id.Content1, List1_Content1.newInstance());
                    } else {
                        getFragment(R.id.Content1, List4_Content3.newInstance());
                    }
                    Global.LockDisplayShowFlag = false;
                    LockModuleDisplayShowFlag = false;
                    break;
                case R.id.btnReturn_p1:
                    Global.LockDisplayShowFlag = false;
                    LockModuleDisplayShowFlag = false;
                    if (preWinForFragment.equals("module")) {
                        //重复了
                        //Module_Left_list1.newInstance(false).selectFirst();
                        getFragment(R.id.Module_Content1, new List5_Content1());
                    } else {
                        getFragment(R.id.Content4, List4_Content3_p1_0505.newInstance());
                    }
                    preWinForFragment = "";
                    break;
                case R.id.btnYQXX:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_pfxyxx.newInstance());
                    }
                    break;
                case R.id.btnBCPB:
                    getFragment(R.id.Content4, new List4_Content3_pbcpb());
                    break;
                case R.id.btnCZRZ:
                    getFragment(R.id.Content4, List4_Content3_pczrz.newInstance());
                    break;
                case R.id.btnUserParSet:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Content4, List4_Content3_pUserParSet.newInstance(true));
                    }
                    break;
                case R.id.btnAdminParSet:
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        preWinForFragment = "";
                        getFragment(R.id.Content4, new List4_Content3_pAdminParSet());
                    }
                    break;
                case R.id.btnReturn_p2:
                    getFragment(R.id.Content4, List4_Content3_p2.newInstance());
                    Global.LockDisplayShowFlag = false;
                    LockModuleDisplayShowFlag = false;
                    break;
                case R.id.btnReturn_p4:
                    getContentFragment(R.id.Content4, new List4_Content3());
                    break;
                case R.id.Left5_1:
                    getFragment(R.id.Content5, new List5_Content1());
                    break;
                case R.id.Left5_2:
                    getFragment(R.id.Content5, new List5_Content2());
                    break;
                case R.id.Left5_3:
                    getFragment(R.id.Content5, new List5_Content3());
                    break;
                case R.id.Left5_4:
                    getFragment(R.id.Content5, new List5_Content4());
                    break;
                case R.id.Left5_5:
                    getFragment(R.id.Content5, new List5_Content5());
                    break;
                case R.id.Left5_6:
                    getFragment(R.id.Content5, new List5_Content6());
                    break;
                case R.id.Left_setting1:
                    getContentFragment(R.id.Content_setting, List_Content_Setting1.newInstance());
                    break;
                case R.id.Left_setting2:
                    getContentFragment(R.id.Content_setting, List_Content_Setting2.newInstance());
                    break;
                case R.id.Left_setting3:
                    getContentFragment(R.id.Content_setting, new List_Content_Setting3());
                    break;
                case R.id.Left_setting5:
                    break;
                case R.id.btnReturn_p3:
                    getFragment(R.id.Content_setting, List_Content_Setting1.newInstance());
                    break;
                case R.id.btn420:
                    getContentFragment(R.id.Content_setting, new List_Content_Setting1_1());
                    break;
                case R.id.btnUartCfg:
                    getContentFragment(R.id.Content_setting, new List_Content_Setting1_2());
                    break;
                case R.id.btnTcpCfg:
                    getContentFragment(R.id.Content_setting, new List_Content_Setting1_3());
                    break;
                case R.id.btnProtocolCfg:
                    getContentFragment(R.id.Content_setting, List_Content_Setting1_4.newInstance());
                    break;
                case R.id.Left_settingx: {
                    Global.LockDisplayShowFlag = false;
                    LockModuleDisplayShowFlag = false;
                    removeDestopText(mfb);
                    Dialog_password st = new Dialog_password();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-info", "List-settingWStation");
                    st.setArguments(bundle);
                    st.setTargetFragment(Left_list_setting.newInstance(), 1);
                    st.show(fm, "Dialog_password");
                }
                break;
                case R.id.btnReturnFlow:
                    Global.LockDisplayShowFlag = false;
                    LockModuleDisplayShowFlag = false;
                    getFragment(R.id.Content4, new List4_Content6());
                    break;
                case R.id.wstation_Left1_1:
                    break;
                case R.id.wstation_Left1_2:
                    break;
                case R.id.wstation_Left1_3:
                    break;
                case R.id.btnProtectReturn:
                    getFragment(R.id.Content4, new List4_Content3());
                    break;
                /*以下稀释模块界面（与常规有共用界面）***/
                case R.id.btnXSBB:
                    winID = R.id.btnXSBB;
                    //将组份名称更改为当前组份加“xs”
                    mBackUpMeasBordName = mCompName;
                    mCompName = textViewCompName.getText().toString() + getModuleName("5");
                    getFragment(R.id.fragment_up_container, new ModuleUp_content1());
                    break;
                case R.id.btnZLBB:
                    winID = R.id.btnZLBB;
                    //将组份名称更改为当前组份加“zl”
                    mBackUpMeasBordName = mCompName;
                    mCompName = textViewCompName.getText().toString() + getModuleName("6");
                    getFragment(R.id.fragment_up_container, new ModuleUp_content1());
                    break;
                case R.id.module_Left1_1:
                    getFragment(R.id.Module_Content1, new List5_Content1());
                    break;

                case R.id.module_Left1_2:
                    if (doFlowing.get(mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Module_Content1, new List4_Content1());
                    }
                    break;
                case R.id.module_Left1_3:
                    if (doFlowing.get(mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Module_Content1, new List4_Content3_pbfzj());
                    }
                    break;
                case R.id.module_Left1_4:
                    if (doFlowing.get(mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Module_Content1, new List4_Content3_pjlcs());
                    }
                    break;
                case R.id.module_Left1_5:
                    if (doFlowing.get(mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Module_Content1, new List4_Content3_pjldy());
                    }
                    break;
                case R.id.module_Left1_6:
                    getFragment(R.id.Module_Content1, new List5_Content5());

                    break;
                case R.id.module_Left1_7:
                    getFragment(R.id.Module_Content1, new List5_Content4());

                    break;
                case R.id.module_Left1_8: {
                    removeDestopText(mfb);
                    Dialog_password st = new Dialog_password();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-info", "moduleList1-8");
                    st.setTargetFragment(Module_Left_list1.newInstance(false), 1);
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_password");
                }
                break;
                case R.id.module_Left1_9: {
                    getFragment(R.id.Module_Content1, new List2_Content1());
                }
                break;
                case R.id.module_Left1_10:
                    if (doFlowing.get(mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Module_Content1, new List1_Content3());
                    }
                    break;
                case R.id.module_Left1_11:
                    if (doFlowing.get(mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Module_Content1, new List4_Content3_FlowConfig());
                    }
                    break;
                case R.id.module_Left1_12: {
                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                        getFragment(R.id.Module_Content1, new List1_Content1());//界面不存内存
                    }
                }
                break;
                /*以上稀释模块界面***/
                case R.id.noBoard_Left1_1:
                    getFragment(R.id.NoBoard_Content1, new NoBoard_List1_Content1());
                    break;
                case R.id.noBoard_Left1_2:
                    getFragment(R.id.NoBoard_Content1, new NoBoard_List1_Content2());
                    break;
                case R.id.noBoard_Left1_3:
                    getFragment(R.id.NoBoard_Content1, new NoBoard_List1_Content3());
                    break;
                case R.id.noBoard_Left1_4:
                    getFragment(R.id.NoBoard_Content1, new NoBoard_List1_Content4());
                    break;
                case R.id.noBoard_Left1_5:
                    getFragment(R.id.NoBoard_Content1, new NoBoard_List1_Content5());
                    break;
                case R.id.noBoard_Left1_6:
                    getFragment(R.id.NoBoard_Content1, new NoBoard_List1_Content6());
                    break;
                case R.id.noBoard_Left1_7:
                    getFragment(R.id.NoBoard_Content1, NoBoard_List1_Content7.newInstance(true));
                    break;
                case R.id.noBoard_Left1_8:
                    removeDestopText(mfb);
                    Dialog_password stNb = new Dialog_password();
                    Bundle bundleNb = new Bundle();
                    bundleNb.putString("alert-info", "noBoard-Left1-8");
                    stNb.setTargetFragment(NoBoard_Left_list1.newInstance(false), 1);
                    stNb.setArguments(bundleNb);
                    stNb.show(fm, "Dialog_password");
                    break;
                case R.id.btnNBYQXX:
                    getFragment(R.id.NoBoard_Content1, NoBoard_List1_Content7_pfxyxx.newInstance());
                    break;
                case R.id.btnNBCZRZ:
                    getFragment(R.id.NoBoard_Content1, NoBoard_List1_Content7_pczrz.newInstance());
                    break;
                case R.id.btnNBUserParSet:
                    getFragment(R.id.NoBoard_Content1, NoBoard_List1_Content7_pUserParSet.newInstance(true));
                    break;
                case R.id.btnNBAdminParSet:
                    preWinForFragment = "";
                    getFragment(R.id.NoBoard_Content1, new NoBoard_List1_Content7_pAdminParSet());
                    break;
                case R.id.btnNoBoardReturn_p2:
                    getFragment(R.id.NoBoard_Content1, NoBoard_List1_Content7_p2.newInstance());
                    Global.LockDisplayShowFlag = false;
                    LockModuleDisplayShowFlag = false;
                    break;
            }
            FullWindows(mActivityWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                handler.sendMessage(handler.obtainMessage(100, getNowSystemTime()));
                sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*弹窗选择*/
    @Override
    public void onDialogSelected(View view, Fragment Fm) {
        try {
            FullWindows(mActivityWindow);
            switch (view.getId()) {
                case R.id.eTzqclh:
                    removeDestopText(mfb);
                    SelectTimeSelectJob2 st1 = new SelectTimeSelectJob2();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("alert-select_time_select_job", getConfigData(mCompName, "zqclh") + "&" + getConfigData(mCompName, "zqclFlow"));
                    st1.setArguments(bundle1);
                    st1.setTargetFragment(Fm, 1);
                    st1.show(fm, "select_time_select_job");
                    break;
                case R.id.D_LogInOK:
                case R.id.D_LogInOut:
                    String[] loIngNames = getResources().getStringArray(R.array.loginUsers);
                    int iLoginNameId = Integer.parseInt(getPublicConfigData("LogInName"));
                    mTxtLogInName.setText(iLoginNameId == 0 ? context.getText(R.string.publicUser) : loIngNames[iLoginNameId - 1]);
                    onDialogRS();
                    break;
            }
        } catch (Exception e) {
            saveExceptInfo2File("select_time 异常" + e.toString());

        }
    }

    /*弹窗选择*/
    @Override
    public void onDialogSelected(View view, Fragment Fm, String name) {
        try {
            FullWindows(mActivityWindow);
            switch (view.getId()) {
                case R.id.CsList:
                    removeDestopText(mfb);
                    ChangePoint cp = new ChangePoint();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-change_point", name);
                    cp.setArguments(bundle);
                    cp.setTargetFragment(Fm, 1);
                    cp.show(fm, "change_point");
                    break;
            }
        } catch (Exception e) {
            saveExceptInfo2File("change_point 异常" + e.toString());
        }
    }

    @Override
    public void onDialogRS() {
        /* 打开窗体悬浮（WL注释）*/
        initDestopText(mfb, 62, 62);
        FullWindows(mActivityWindow);
    }


    /*
     * 悬浮按钮
     */
    private class mfbClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!Global.LockDisplayShowFlag) {
                getMainFrame(Bottom_list_setting.newInstance());
                Frame.updateUI(false);

                Left_list_setting.newInstance().selectFirst();
                winID = R.id.fragment_up_container;
                getFragment(R.id.fragment_up_container, Up_content_setting.newInstance());

            }
        }
    }


    public void getFragment(int ViewId, Fragment fragmentClass) {
        fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(ViewId);

        if (fragment == null || fragment.isRemoving()) {
            if (!fragmentClass.isAdded()) {
                fm.beginTransaction().add(ViewId, fragmentClass).commitAllowingStateLoss();
            } else {
                Log.e(TAG, "fm.add exception");
                saveExceptInfo2File("fm.add exception");
            }
        } else {
            fm.beginTransaction().replace(ViewId, fragmentClass).commitAllowingStateLoss();
        }

    }

    @SuppressLint("RestrictedApi")
    public void getContentFragment(int ViewId, Fragment fragmentClass) {
        try {
            if (fm != null && fm.getFragments() != null) {
                for (Fragment fragmentZ : fm.getFragments()) {
                    if (fragmentZ != null) {
                        if (fragmentZ.getView() != null && fragmentZ.getView().findViewById(ViewId) != null) {
                            getFragment(ViewId, fragmentClass);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
    }

    //串口接收命令更新控件

    @SuppressLint("HandlerLeak")
    public static Handler WinWidgetHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {

                    case 5700:
                        //仪器信息，5184 界面需要更新控件
                        if (mCKBNumber != null) {
                            mCKBNumber.setText((getCmds(mCompName).getCmd(5700).getValue() == null ? "" : getCmds(mCompName).getCmd(5700).getValue()).toString());
                        }
                        if (mEtCKB != null) {
                            // 测控板编号
                            mEtCKB.setText((getCmds(mCompName).getCmd(5700).getValue() == null ? "0" : getCmds(mCompName).getCmd(5700).getValue()).toString());

                            updateConfigData(mCompName, "HMIBH", mEtCKB.getText().toString());
                            if (passwordUnlockEffectiveWork.equals("") && ((!mEtCKB.getText().toString().equals("0")) && (!mEtCKB.getText().toString().equals("1")))) {
                                mEtCKB.setEnabled(false);
                            }
                            passwordUnlockEffectiveWork = "";
                        }
                        break;
                    case 5703:
                        boolean blCfgSwitchStatus = (!(getCmds(String.valueOf(msg.obj)).getCmd(5703).getValue() == null ? false : getCmds(String.valueOf(msg.obj)).getCmd(5703).getValue()).toString().equals("0"));
                        if (mBtnCfgFlowStart != null) {
                            mBtnCfgFlowStart.setChecked(blCfgSwitchStatus);
                        }
                        updateConfigData(String.valueOf(msg.obj), "CFG_Flow_Switch", blCfgSwitchStatus ? "open" : "close");
                        break;
                    case 9222:
                        boolean bznxjSwitchStatus = (!(getCmds(String.valueOf(msg.obj)).getCmd(9222).getValue() == null ? false : getCmds(String.valueOf(msg.obj)).getCmd(9222).getValue()).toString().equals("0"));
                        if (mToggleButtonZNXJ != null) {
                            mToggleButtonZNXJ.setChecked(bznxjSwitchStatus);
                        }
                        updateConfigData(String.valueOf(msg.obj), "ZNXJ", bznxjSwitchStatus ? "open" : "close");
                        break;
                    case 9600:
                        String strHistoryDateTime = ((getCmds(String.valueOf(msg.obj)).getCmd(9600).getValue() == null ? "0" : getCmds(String.valueOf(msg.obj)).getCmd(9600).getValue()).toString());
                        if (btnHistoryDateTime != null) {
                            if (strHistoryDateTime.equals("0")) {
                                btnHistoryDateTime.setText(context.getString(R.string.start_time));
                            } else {
                                btnHistoryDateTime.setText(context.getString(R.string.sampling_time));
                            }
                        }
                        updateConfigData(String.valueOf(msg.obj), "historyTimeFlag", strHistoryDateTime);
                        break;
                    case 201:
                    case 209:
                        break;
                    case 110:
                        //  重启APP
                        reStartApp();
                        break;
                    case 1:
                        if (!getPublicConfigData("LogInName").equals("0")) {
                            updatePublicConfigData("LogInName", String.valueOf(0));
                            if (mTxtLogInName != null) {
                                String[] loIngNames = context.getResources().getStringArray(R.array.loginUsers);
                                int iLoginNameId = Integer.parseInt(getPublicConfigData("LogInName"));
                                mTxtLogInName.setText(iLoginNameId == 0 ? context.getText(R.string.publicUser) : loIngNames[iLoginNameId - 1]);
                                Toast.makeText(context, context.getText(R.string.loginOut) + "!", Toast.LENGTH_SHORT).show();
                                saveOperationLogMsg("公共", "退出登录", ErrorLog.msgType.操作_信息);
                            }
                        }
                        break;
                    case 2:// 返回主界面需要刷新控件
                        mTVName.setVisibility(View.GONE);
                        break;
                    case 3:
                        /*反控界面 测量信息*/
                        updateDevStatusTextShow();
                        // 主界面显示反控设备信息
                        mainDisplayControlDevicesInfo();
                        break;
                    case 4:
                        /*接口板开采样时间*/
                        try {
                            if (mEtSampleTime != null) {
                                mEtSampleTime.setText(String.valueOf(msg.obj));
                            }
                        } catch (Exception e) {
                            Log.i("except16", e.toString());
                        }
                        break;
                    case 5:
                        Toast.makeText(context, context.getResources().getString(R.string.CurrentFlowLow), Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 主界面显示 反控信息
     */
    public static void mainDisplayControlDevicesInfo() {
        if (mTxTControlDevicesInfo != null) {
            mTxTControlDevicesInfo.setText("");
            if (IOBoardUsed && (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 2 || Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 3)) {
                String[] unitList = context.getResources().getStringArray(R.array.flowMeter);
                String[] devList = context.getResources().getStringArray(R.array.operatingDevice);
                String sUnit = (unitList[Integer.parseInt(getPublicConfigData("DEV_UNIT"))]);
                if (Integer.parseInt(getPublicConfigData("DEV_UNIT")) == 0) {
                    sUnit = "";
                }
                String devInfo = devList[Integer.parseInt(getPublicConfigData("CONTROL_DEV"))] + ":" + String.format("%.3f", getAnalogInQuanlityStatus()) + " " + sUnit;
                mTxTControlDevicesInfo.setText(devInfo);
            }
        }
    }

    /*
     *  非静态全局Handler
     *
     */
    @SuppressLint("HandlerLeak")
    private
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                broadCastFullScreen(true);
            }
        }
    };


    /*************************************************主界面显示内容计算**************************************************************************/

    public static String getNowSystemTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss");
        return sdf.format(new Date());
    }

    /*
     * 主界面信息显示
     */
    private void showMainMsg(String Component, Boolean bJJTZ, Boolean bCfgFlowSwitch) {
        try {
            TextView mtxtCalHitMsg = (TextView) findViewById(R.id.txtDevMsg);
            ImageButton mJJTZ = (ImageButton) findViewById(R.id.btn_jjtz);

            String lockInfo = (getCmds(Component).getCmd(201).getValue() == null ? "" : getCmds(Component).getCmd(201).getValue()).toString();
            if ((getConfigData(Component, "CALIBRATION_HIT").equals("true"))
                    && (Integer.valueOf(getConfigData(Component, "CALIBRATION_INTERVAL_DAYS")) >= 10)) {
                mtxtCalHitMsg.setVisibility(VISIBLE);
                mtxtCalHitMsg.setText(getString(R.string.calibration_tip));
            }

            if (!lockInfo.equals("0") && !lockInfo.equals("")) {
                if ((Integer.parseInt(lockInfo) & 0xff) == 0xaa) {
                    String time = (getCmds(Component).getCmd(209).getValue() == null ? "" : getCmds(Component).getCmd(209).getValue()).toString();
                    if (!time.equals("")) {
                        if ((Integer.parseInt(time) & 0xFFFF) < 72) {
                            mtxtCalHitMsg.setVisibility(VISIBLE);
                            mtxtCalHitMsg.setText(getString(R.string.close_devices_3_day_info));
                        }
                    }
                } else if ((Integer.parseInt(lockInfo) & 0xff) == 0xbb) {
                    mtxtCalHitMsg.setVisibility(VISIBLE);
                    mtxtCalHitMsg.setText(getString(R.string.close_devices_info));
                }
            }
            if (mJJTZ.getVisibility() == VISIBLE) {
                mtxtCalHitMsg.setVisibility(INVISIBLE);
            }

            Boolean bCalHitMsg = (mtxtCalHitMsg.getVisibility() == View.VISIBLE);
            LinearLayout mLayoutToppadding = (LinearLayout) findViewById(R.id.layout_Toppadding);
            if ((bJJTZ && bCfgFlowSwitch) || (bJJTZ && bCalHitMsg) || (bCfgFlowSwitch && bCalHitMsg)) {
                mLayoutToppadding.setVisibility(View.GONE);
            } else {
                mLayoutToppadding.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + Component + "] 主界面信息显示异常" + e.toString());
        }
    }


    /*
     * 判断是否启动自动做样
     */
    private static String isAutoJob(String compName) {
        String[] sRunningModes = context.getResources().getStringArray(R.array.meaMode);
        int iMeaMode = getDevMeaMode(compName);
        if (iMeaMode == 6) {
            iMeaMode = 5;
        }
        return sRunningModes[iMeaMode - 1];
    }

    /*下次启动时间计算**/
    private void showNextDoJobInfo(String Component, String status) {
        try {
            TextView mTvM_ZQDSJ = (TextView) findViewById(R.id.M_ZQDSJ);
            if (AutoSampleEnable.get(Component).equals("true") && (status.equals(getString(R.string.waiting_for_instructions)))) {
                if (((getConfigData(Component, "LXCL").equals("true"))) ||
                        ((getConfigData(Component, "LXCL").equals("false"))
                                && (getConfigData(Component, "ZQCL").equals("true")))) {

                    mTvM_ZQDSJ.setText(getString(R.string.restart_time) + "： " + getNextDoJobTimeStr(Component));
                } else {
                    mTvM_ZQDSJ.setText(getString(R.string.restart_time) + "： " + "——————");

                }
            } else {
                mTvM_ZQDSJ.setText(getString(R.string.restart_time) + "： " + "——————");
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + Component + "] 再启动时间刷新异常" + e.toString());
        }
    }

    /*
     * 再启动时间，
     * 周期测量则为下一个时间整点，
     * 连续测量，则在做样结束之后
     */
    private String getNextDoJobTimeStr(String compName) {
        String strTimeInfo = "——————";
        try {
            if (getConfigData(compName, "ZQCL").equals("true")) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                /*查找到周期测量中比当前时间大的第一个时间点*/
                if (!getConfigData(compName, "zqclh").equals("")) {
                    String[] strTime = getConfigData(compName, "zqclh").split("[-，]");
                    for (int i = 0; i < strTime.length; i++) {
                        if (hour < Integer.parseInt(strTime[i])) {
                            return strTime[i] + " " + getString(R.string.time_o) + getString(R.string.time_hour);
                        }
                        if (i == (strTime.length - 1)) {
                            return strTime[0] + " " + getString(R.string.time_o) + getString(R.string.time_hour);
                        }
                    }
                }
            } else if (getConfigData(compName, "LXCL").equals("true")) {
                String H = "0";
                String M = "1";
                if (!getConfigData(compName, "meaMode").equals("1")) {

                    H = getConfigData(compName, "lxclh");
                    M = getConfigData(compName, "lxclm");
                }

                int lxclTime = ((Integer.valueOf((H.equals("") ? "0" : H)) * 60 + Integer.valueOf((M.equals("") ? "0" : M))));
                int counterTimer = Integer.parseInt(getConfigData(compName, "zdzyTimer"));
                int time = (lxclTime) - (counterTimer / 60);
                if (time == 1) {
                    return (lxclTime * 60 - counterTimer) + " " + "Sec";
                } else if (time > 1) {
                    strTimeInfo = time + " " + "Min";
                    return strTimeInfo;
                }
                return strTimeInfo;
            } else {
                return "——————";
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "] 再启动时间计算异常" + e.toString());
        }

        return strTimeInfo;
    }

    /*再启动量程*/
    private void showNextDoJobUseRange(String Component) {
        try {
            TextView mTextUseRange = (TextView) findViewById(R.id.M_ZQDLC);
            mTextUseRange.setText(getString(R.string.afterJobUseRange));
            mTextUseRange.setVisibility(View.GONE);
            if (!getConfigData(Component, "CalSelectRange").equals(getConfigData(Component, "RANGE"))) {
                if (getConfigData(Component, "CalSelectRange").equals("")) {
                    updateConfigData(Component, "CalSelectRange", getConfigData(Component, "RANGE"));
                } else {
                    mTextUseRange.setVisibility(VISIBLE);
                    mTextUseRange.setText(String.format("%s：%s   %s", getString(R.string.afterUseRange), getString(R.string.range), getConfigData(Component, "CalSelectRange")));
                }
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
    }


    /***************************************************界面密码**************************************************************************/


    /*
     * 获取随机码的方式
     */
    private static int getPasswordRandom(String winId) {
        int code = 0;
        for (int i = 0; i < winId.length(); i++) {
            code += Integer.parseInt(winId.substring(i, i + 1));
        }
        if (code > 5) {
            code = code % 5;
        }
        return code;
    }

    /*
     * 水站系统密码
     */
    @SuppressLint("DefaultLocale")
    private String wsPasswordWin(String inputPassword) {
        if (inputPassword.length() > 5) {
            String strPassword = inputPassword.substring(3);
            String code;
            int random;
            switch (inputPassword.substring(0, 3)) {
                case "501":
                    random = getPasswordRandom("501");
                    code = String.format("%04d", ((getSystemDateMonth() * 5) + (getSystemDateDay()) + Integer.parseInt(passwordRandom.substring(random, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "501";
                    }
                    return "0";
                case "818": {
                    random = getPasswordRandom("818");
                    code = String.format("%04d", ((getSystemDateMonth() * 8) + (getSystemDateDay() * 18) + Integer.parseInt(passwordRandom.substring(random, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "818";
                    }
                    return "0";
                }
            }
        }
        return "";
    }

    /*
     * 密码界面解密
     */
    @SuppressLint("DefaultLocale")
    private static String passwordWin(String inputPassword) {
        if (inputPassword.length() > 5) {
            String strPassword = inputPassword.substring(3);
            String code;
            int random;
            switch (inputPassword.substring(0, 3)) {
                case "110":
                    random = getPasswordRandom("110");
                    code = String.format("%04d", ((getSystemDateMonth() * 12) * (getSystemDateDay() * 13) * Integer.parseInt(passwordRandom.substring(random, random + 5))) % 10000);
                    if (strPassword.equals(code)) {
                        return "110";
                    }
                    return "0";
                case "312":
                    random = getPasswordRandom("312");
                    code = String.format("%04d", ((getSystemDateMonth() * 3) + (getSystemDateDay() * 12) + Integer.parseInt(passwordRandom.substring(random, random + 5))) % 10000);
                    if (strPassword.equals(code)) {
                        return "312";
                    }
                    return "0";
                case "302":
                    code = String.format("%04d", (getSystemDateMonth() + getSystemDateDay()) * 10 + 8);
                    if (strPassword.equals(code)) {
                        return "302";
                    }
                    return "0";
                case "333":
                    random = getPasswordRandom("333");
                    code = String.format("%04d", ((getSystemDateMonth() * 3) + (getSystemDateDay() * 33) + Integer.parseInt(passwordRandom.substring(random, random + 5))) % 10000);
                    if (strPassword.equals(code)) {
                        return "333";
                    }
                    return "0";
                case "334": {
                    random = getPasswordRandom("334");
                    code = String.format("%04d", ((getSystemDateMonth() * 3) + (getSystemDateDay() * 34) + Integer.parseInt(passwordRandom.substring(random, random + 4))) % 10000);
                    if (strPassword.equals(code)) {
                        return "334";
                    }
                    return "0";
                }
                case "335": {
                    random = getPasswordRandom("335");
                    code = String.format("%04d", ((getSystemDateMonth() * 3) + (getSystemDateDay() * 35) + Integer.parseInt(passwordRandom.substring(random, random + 5))) % 10000);
                    if (strPassword.equals(code)) {
                        return "335";
                    }
                    return "0";
                }
                case "336": {
                    random = getPasswordRandom("336");
                    code = String.format("%04d", ((getSystemDateMonth() * 3) + (getSystemDateDay() * 36) + Integer.parseInt(passwordRandom.substring(random, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "336";
                    }
                    return "0";
                }
                case "411": {
                    random = getPasswordRandom("411");
                    code = String.format("%04d", ((getSystemDateMonth() * 4) + (getSystemDateDay() * 11) + Integer.parseInt(passwordRandom.substring(random, random + 4))) % 10000);
                    if (strPassword.equals(code)) {
                        return "411";
                    }
                    return "0";
                }
                case "444": {
                    random = getPasswordRandom("444");
                    code = String.format("%04d", ((getSystemDateMonth() * 4) + (getSystemDateDay() * 4) + Integer.parseInt(passwordRandom.substring(random, random + 4))) % 10000);
                    if (strPassword.equals(code)) {
                        return "444";
                    }
                    return "0";
                }
                case "501":
                    random = getPasswordRandom("501");
                    code = String.format("%04d", ((getSystemDateMonth() * 5) + (getSystemDateDay()) + Integer.parseInt(passwordRandom.substring(random, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "501";
                    }
                    return "0";
                case "502":
                    random = getPasswordRandom("502");
                    code = String.format("%04d", ((getSystemDateMonth() * 5) + (getSystemDateDay() * 2) + Integer.parseInt(passwordRandom.substring(random, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "502";
                    }
                    return "0";
                case "818": {
                    random = getPasswordRandom("818");
                    code = String.format("%04d", ((getSystemDateMonth() * 8) + (getSystemDateDay() * 18) + Integer.parseInt(passwordRandom.substring(random, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "818";
                    }
                    return "0";

                }
                case "123": {
                    random = getPasswordRandom("123");
                    code = String.format("%04d", ((getSystemDateMonth() * 12) + (getSystemDateDay() * 3) + Integer.parseInt(passwordRandom.substring(random, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "123";
                    }
                    return "0";
                }
                case "555": {
                    random = getPasswordRandom("555");
                    code = String.format("%04d", ((getSystemDateDay() * 55) + (getSystemDateHour()) + Integer.parseInt(passwordRandom.substring(random, random + 4))) % 10000);
                    if (strPassword.equals(code)) {
                        return "555";
                    }
                    return "0";
                }
                case "556": {
                    random = getPasswordRandom("556");
                    code = String.format("%04d", ((getSystemDateDay() * 56) + (getSystemDateHour()) + Integer.parseInt(passwordRandom.substring(random, random + 4))) % 10000);
                    if (strPassword.equals(code)) {
                        return "556";
                    }
                    return "0";
                }
                case "606":
                    code = String.format("%04d", (getSystemDateDay() * 60 + getSystemDateHour() * 6 + getSystemDateMin()));
                    if (strPassword.equals(code)) {
                        return "606";
                    }
                    return "0";
                case "911":
                    code = String.format("%04d", ((getSystemDateMonth() + getSystemDateDay()) * 10 + 8));
                    if (strPassword.equals(code)) {
                        return "911";
                    }
                    return "0";
                case "919":
                    code = String.format("%04d", (getSystemDateMonth() * 9 + getSystemDateDay() + getSystemDateHour() * 9 + 8));
                    if (strPassword.equals(code)) {
                        return "919";
                    }
                    return "0";
                case "929":
                    code = String.format("%04d", (getSystemDateMonth() + getSystemDateDay() * 9 + getSystemDateHour() * 2 + 9));
                    if (strPassword.equals(code)) {
                        return "929";
                    }
                    return "0";
                case "999":
                    random = getPasswordRandom("9090");
                    code = String.format("%04d", ((getSystemDateMonth() * 19) + (getSystemDateDay() * 29) + Integer.parseInt(passwordRandom.substring(random + 2, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "999";
                    }
                    return "0";
                case "998":
                    random = getPasswordRandom("998");
                    code = String.format("%04d", ((getSystemDateDay() * 9) + (getSystemDateHour() * 8) + Integer.parseInt(passwordRandom.substring(random + 2, random + 6))) % 10000);
                    if (strPassword.equals(code)) {
                        return "998";
                    }
                    return "0";
                case "987":
                    random = getPasswordRandom("987");
                    code = String.format("%04d", (getSystemDateMonth() + Integer.parseInt(passwordRandom.substring(random - 1, random + 3)) * getSystemDateDay() + getSystemDateHour()) % 10000);
                    if (strPassword.equals(code)) {
                        return "987";
                    }
                    return "0";
                default:
                    break;
            }
        }
        return "";
    }

    /*
     * 密码输入之后解析
     */
    public void getData(String data) {
        try {
            boolean showPassWordFlag = false;
            if (winID != R.id.ImBtn_3) {
                if ("password".equals(data.split("_")[0])) {
                    switch (data.split("_")[2]) {
                        case "List4-3": {
                            saveOperationLogMsg(mCompName, "输入密码:" + data, ErrorLog.msgType.密码_输入);
                            switch (data.split("_")[1]) {
                                case "1234":
                                    if (isHaveMeasCategory(mCompName, "5") || isHaveMeasCategory(mCompName, "6")) {
                                        getContentFragment(R.id.fragment_up_container, new List_Content_Setting_p1_1234());
                                    }
                                    break;
                                case "550055":
                                    if (getPublicConfigData("LogInName").equals("3")) {
                                        //管理员参数
                                        getFragment(R.id.Content4, List4_Content3_pczrz.newInstance());
                                    }
                                    break;
                                case "5184":
                                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                        getContentFragment(R.id.Content4, List4_Content3_DevProtect.newInstance());
                                    }
                                    break;
                                case "5566":
                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                        if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                            if (!getModePermissions(MainActivity.mCompName, "用户管理234")) {//离线状态下主页面不显示
                                                getContentFragment(R.id.Content4, new List4_Content3_pUserSetting());
                                            }

                                        }
                                    }
                                    break;
                                /*case "5189": //导入数据密码
                                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))){
                                        getContentFragment(R.id.Content5, new List4_Content3_DataRecovery());
                                    }
                                    break;*/
                                case "10633":
                                    break;
                                case "200211":/*葡萄糖测试*/
                                    if (QueryMeasCateg(mCompName).equals("8") && mCompName.equals("CODmn")) {
                                        if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                            if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                                preWinForDialog = "Content4";
                                                getContentFragment(R.id.Content4, List4_Content3_settingGlucose.newInstance());
                                            } else {
                                                preWinForDialog = "fragment_up_container";
                                                getContentFragment(R.id.fragment_up_container, new List4_Content3_settingGlucose());
                                            }
                                        }
                                    }
                                    break;
                                case "20188":
                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                        if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                            if (getPublicConfigData("userPassword1").equals("")) {
                                                getContentFragment(R.id.Content4, new List4_Content3_pUserPassword());
                                            }
                                        }
                                    }
                                    break;
                                case "20127":
                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                        if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                            if (getPublicConfigData("userSettingFlag").equals("true")) {
                                                if (getPublicConfigData("userPassword2").equals("")) {
                                                    getContentFragment(R.id.Content4, new List4_Content3_pUserPassword2());
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case "300300":/*高锰酸钾加入量*/
                                    if (QueryMeasCateg(mCompName).equals("1") && mCompName.equals("CODcr")) {
                                        if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                            if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                                preWinForDialog = "Content4";
                                                getContentFragment(R.id.Content4, List4_Content3_pfyzs.newInstance());
                                            } else {
                                                preWinForDialog = "fragment_up_container";
                                                getContentFragment(R.id.fragment_up_container, new List4_Content3_pfyzs());
                                            }
                                        }
                                    }
                                    break;
                                case "607000":
                                    updatePublicConfigData("Multi_Point_Linear_Features", "false");
                                    Toast.makeText(context, "Close   Success", Toast.LENGTH_LONG).show();
                                    break;
                                case "945610":
                                    resetExtRangeKb(mCompName);
                                    break;
                                case "-2":
                                default:
                                    // 用户密码
                                    String strInputPassword = data.split("_")[1];
                                    String strUserPassword1 = getPublicConfigData("userPassword1");
                                    String strUserPassword2 = getPublicConfigData("userPassword2");
                                    if (strInputPassword.equals(strUserPassword1) || strInputPassword.equals(strUserPassword2)) {
                                        if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                            if (!QueryMeasCateg(mCompName).equals("4") && !QueryMeasCateg(mCompName).equals("5")
                                                    && !QueryMeasCateg(mCompName).equals("6")) {
                                                if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                                    preWinForDialog = "Content4";

                                                    if (strInputPassword.equals(strUserPassword1)) {

                                                        if (!strUserPassword1.equals("")) {
                                                            if (getModePermissions(mCompName, "校正/修正因子")) {
                                                                getFragment(R.id.Content4, new List4_Content3_pExtKBCoe());
                                                            }
                                                        }

                                                    } else {

                                                        if (!strUserPassword2.equals("")) {
                                                            if (getModePermissions(mCompName, "校正/修正因子")) {
                                                                getFragment(R.id.Content4, new List4_Content3_pExtKBCoe2());
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    preWinForDialog = "fragment_up_container";
                                                    if (strInputPassword.equals(strUserPassword1)) {
                                                        if (!strUserPassword1.equals("")) {
                                                            getFragment(R.id.fragment_up_container, new List4_Content3_pExtKBCoe());
                                                        }
                                                    } else {
                                                        if (!strUserPassword2.equals("")) {
                                                            getFragment(R.id.fragment_up_container, new List4_Content3_pExtKBCoe2());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        String pWin;
                                        pWin = passwordWin(data.split("_")[1]);
                                        if (!pWin.equals("")) {
                                            switch (pWin) {
                                                case "110":
                                                    // 清除所有不需要功能
                                                    if (getPublicConfigData("userSettingFlag").equals("true")) {
                                                        for (String item : strComponent.get(1)) {
                                                            if (!getPublicConfigData("userPassword2").equals("")) {
                                                                updatePublicConfigData("userPassword2", "");
                                                                saveOperationLogMsg(item, "_强制密码2重置", ErrorLog.msgType.操作_信息);
                                                                resetUserExtRangeKb(item);
                                                            }
                                                        }
                                                        // 关闭吸光度设置
                                                        updatePublicConfigData("AIsShow", "false");
                                                        // 关闭不需要功能
                                                        updatePublicConfigData("userSettingFlag", "false");
                                                    }
                                                    break;
                                                case "333":
                                                    if (doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                                                        if (getConfigData(mCompName, "calibrationFlag").equals("true")) {//sun
                                                            doControlJob(mCompName, getString(R.string.SDJZ));
                                                            updateConfigData(mCompName, "ycjzFlag", "true");
                                                            saveOperationLogMsg(mCompName, "直接校准", ErrorLog.msgType.操作_信息);
                                                        }
                                                    }
                                                    break;
                                                case "335":
                                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                                        passwordUnlockEffectiveWork = "335";
                                                    }
                                                    break;
                                                case "336":
                                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                                        passwordUnlockEffectiveWork = "336";
                                                    }
                                                    break;
                                                case "501":
                                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                                        //设置广播发送显示虚拟按键命令
                                                        broadCastFullScreen(false);

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    sleep(90 * 1000);
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Message msg = new Message();
                                                                msg.what = 1;
                                                                mHandler.sendMessage(msg);
                                                            }
                                                        }).start();
                                                    }
                                                    break;
                                                case "502":
                                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                                        //设置广播发送隐藏虚拟按键命令
                                                        broadCastFullScreen(true);
                                                    }
                                                    break;
                                                case "555":
                                                    break;
                                                case "556":
                                                    break;
                                                case "606":
                                                    updatePublicConfigData("Multi_Point_Linear_Features", "true");
                                                    Toast.makeText(context, "Open    Success", Toast.LENGTH_LONG).show();
                                                    break;
                                                case "818":
                                                    break;
                                                case "911":
                                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                                        if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                                            getFragment(R.id.Content4, List4_Content3_pas_fxyxx.newInstance());
                                                        }
                                                    }
                                                    break;
                                                case "919":
                                                    for (String item : strComponent.get(1)) {
                                                        if (!getPublicConfigData("userPassword1").equals("")) {
                                                            updatePublicConfigData("userPassword1", "");
                                                            saveOperationLogMsg(item, "_强制密码重置", ErrorLog.msgType.操作_信息);
                                                            resetExtRangeKb(item);
                                                        }
                                                    }
                                                    break;
                                                case "929":
                                                    for (String item : strComponent.get(1)) {
                                                        if (!getPublicConfigData("userPassword2").equals("")) {
                                                            updatePublicConfigData("userPassword2", "");
                                                            saveOperationLogMsg(item, "_强制密码2重置", ErrorLog.msgType.操作_信息);
                                                            resetUserExtRangeKb(item);
                                                        }
                                                    }
                                                    break;
                                                case "998":
                                                    // 重置用户、运维密码
                                                    if (!getPublicConfigData("LogInUserPassword").equals("20199")) {
                                                        updatePublicConfigData("LogInUserPassword", "20199");
                                                    }
                                                    if (!getPublicConfigData("LogInOPSPassword").equals("20188")) {
                                                        updatePublicConfigData("LogInOPSPassword", "88888");
                                                    }
                                                    Toast.makeText(context, context.getText(R.string.resetPasswordSuccess) + "!", Toast.LENGTH_SHORT).show();
                                                    saveOperationLogMsg("公共", "_强制重置用户运维密码", ErrorLog.msgType.操作_信息);
                                                    break;
                                                case "999": {
                                                    if (!getPublicConfigData("LogInName").equals("3")) {
                                                        updatePublicConfigData("LogInName", "3");

                                                        if (mTxtLogInName != null) {
                                                            String[] loIngNames = context.getResources().getStringArray(R.array.loginUsers);
                                                            int iLoginNameId = Integer.parseInt(getPublicConfigData("LogInName"));
                                                            mTxtLogInName.setText(iLoginNameId == 0 ? context.getText(R.string.publicUser) : loIngNames[iLoginNameId - 1]);
                                                        }
                                                        Toast.makeText(context, context.getText(R.string.loginSuccess) + "!", Toast.LENGTH_SHORT).show();
                                                        saveOperationLogMsg("公共", "管理员登录", ErrorLog.msgType.操作_信息);
                                                    }
                                                }
                                                break;
                                                case "987":
                                                    // 去除该密码，2020年5月30日
                                                    break;
                                                default:
                                                    passwordUnlockEffectiveWork = "";
                                                    break;
                                            }
                                        } else {
                                            if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                                getContentFragment(R.id.Content4, new List4_Content3());
                                            } else {
                                                showUpContent();
                                            }
                                            if (!data.split("_")[1].equals("") && data.split("_")[1].length() > 4) {
                                                if (data.split("_")[1].substring(0, 3).equals("717")
                                                        || data.split("_")[1].substring(0, 3).equals("727")
                                                        || data.split("_")[1].substring(0, 3).equals("724")) {
                                                    Log.i("密码发送给CKB", data.split("_")[1]);

                                                    int code = Integer.parseInt(data.split("_")[1]);

                                                    byte[] arrayOfByte = intToByteArray(code);
                                                    DataUtil.reverse(arrayOfByte);
                                                    SendManager.SendCmd(mCompName + "_设置延保密码" + "_8_11", S0, 3, 200, arrayOfByte);
                                                }
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                        break;
                        case "List-settingWStation": {
                            saveOperationLogMsg("公共", "输入密码:" + data, ErrorLog.msgType.密码_输入);
                            switch (data.split("_")[1]) {
                                case "5721":
                                    getContentFragment(R.id.Content_setting, new List_Content_Settingx());
                                    break;
                                case "5722":
                                    if (QueryMeasCategoryComponent("7") != null) {
                                        getContentFragment(R.id.Content_setting, new List_Content_SettingNoBoardx());
                                    }
                                    break;
                                case "9721":
                                    break;
                                case "7777":
                                    showPassWordFlag = true;
                                    break;
                                case "6666":
                                    break;
                                case "-2":
                                default:
                                    String pWin;
                                    pWin = wsPasswordWin(data.split("_")[1]);
                                    if (!pWin.equals("")) {
                                        switch (pWin) {
                                            case "501":
                                                //设置广播发送显示虚拟按键命令
                                                broadCastFullScreen(false);
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            sleep(90 * 1000);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Message msg = new Message();
                                                        msg.what = 1;
                                                        mHandler.sendMessage(msg);
                                                    }
                                                }).start();
                                                break;
                                            case "818":
                                                break;
                                        }
                                    } else {
                                        getContentFragment(R.id.Content_setting, new List_Content_Setting1());
                                    }
                                    passwordRandom = getWinPassword(15);
                                    break;
                            }
                        }
                        break;
                        case "moduleList1-8":
                            saveOperationLogMsg(mCompName, "输入密码:" + data, ErrorLog.msgType.密码_输入);
                            switch (data.split("_")[1]) {
                                case "-2":
                                    if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                        getFragment(R.id.fragment_up_container, new ModuleUp_content1());
                                    }
                                    break;
                                default:
                                    String pWin;
                                    pWin = passwordWin(data.split("_")[1]);
                                    if (!pWin.equals("")) {
                                        switch (pWin) {
                                            case "302":
                                                if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions))) {
                                                    getContentFragment(R.id.Module_Content1, List_Content_pfxyxx.newInstance(true));
                                                }
                                                break;
                                        }
                                    }
                                    break;
                            }
                            break;
                        case "noBoard-Left1-8":
                            saveNoBoardOperationLogMsg(mNoBoardCompName, "输入密码:" + data, ErrorLog.msgType.密码_输入);
                            switch (data.split("_")[1]) {
                                case "5184":
                                    if (doFlowing.get(strComponent.get(1)[0]).equals(getString(R.string.waiting_for_instructions))) {
                                        getContentFragment(R.id.NoBoard_Content1, NoBoard_List1_Content7_pDevProtect.newInstance());
                                    }
                                    break;
                                case "-2":
                                    getFragment(R.id.fragment_up_container, new NoBoardUp_content1());
                                    break;
                                default:
                                    String pWin;
                                    pWin = passwordWin(data.split("_")[1]);
                                    if (!pWin.equals("")) {
                                        switch (pWin) {
                                            case "123":
                                                try { // 算法里有浊度系数 KZ1
                                                    String calcARevise = getNoBoardCalcAFormula(mNoBoardCompName, Byte.valueOf(getConfigData(strComponent.get(1)[0], "RANGE")));
                                                    if (calcARevise.contains("KZ1")) {
                                                        if (doFlowing.get(strComponent.get(1)[0]).equals(getString(R.string.waiting_for_instructions))) {
                                                            getContentFragment(R.id.NoBoard_Content1, NoBoard_List1_Content7_pKZCoe.newInstance());
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "302":
                                                if (doFlowing.get(strComponent.get(1)[0]).equals(getString(R.string.waiting_for_instructions))) {
                                                    if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                                                        getFragment(R.id.NoBoard_Content1, new NoBoard_List1_Content7_p2());
                                                    }
                                                }
                                                break;
                                            default:
                                                passwordUnlockEffectiveWork = "";
                                                break;
                                        }
                                    }
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
            } else {
                if ("Err".equals(data.split("_")[0])) {
                    // 报错配置文件更改后点击确认
                    if (data.contains(getResources().getString(R.string.files_changes))) {
                        // 根据文件判定，更新配置文件
                        boolean blSave = false;
                        for (String item : strAll5721Component.get(1)) {
                            if (data.contains(item)) {
                                blSave = true;
                                AddError(item, 589, ErrorLog.msgType.其他_信息);
                                SetMD5();
                                Toast.makeText(context, context.getResources().getString(R.string.updateConfig_complete), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (!blSave) {
                            for (String item : strAll5721Component.get(1)) {
                                AddError(item, 589, ErrorLog.msgType.其他_信息);
                                SetMD5();
                                Toast.makeText(context, context.getResources().getString(R.string.updateConfig_complete), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
            passwordRandom = getWinPassword(15);
            onDialogRS();
            FullWindows(mActivityWindow);
            if (showPassWordFlag) {
                Toast.makeText(context, passwordRandom, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            saveExceptInfo2File("getData 异常" + e.toString());
        }
    }


    /*****************************************************系统控制，全屏，软件重启**************************************************************************/

    /*
     * 设置屏幕全屏
     */
    public static void FullWindows(Window window) {
        FullScreenSticky(window);
    }


    /*
     * 广播全屏操作
     */
    private void broadCastFullScreen(boolean enable) {
        switch (phoneName) {
            case "QUAD-CORE A33 y3": {
                Intent systemBarIntent = new Intent("com.tchip.changeBarHideStatus");

                if (enable) {
                    //设置广播发送隐藏虚拟按键命令
                    /*try {
                        String command;
                        command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui";
                        Process proc = Runtime.getRuntime().exec(new String[]{"su",
                                "-c", command});
                        proc.waitFor();
                    } catch (Exception ex) {
                    }*/
                    execRootCmdSilent("settings put system systembar_hide 1");//1为隐藏导航栏，0显示导航栏
                    MainActivity.this.sendBroadcast(systemBarIntent);
                } else {
                    //设置广播发送显示虚拟按键命令
                    /*try {
                        String command;
                        command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService";
                        Process proc = Runtime.getRuntime().exec(new String[]{"su",
                                "-c", command});
                        proc.waitFor();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    execRootCmdSilent("settings put system systembar_hide 0");//1为隐藏导航栏，0显示导航栏
                    MainActivity.this.sendBroadcast(systemBarIntent);
                }
            }
            break;
            case "rk3288": {
                if (enable) {
                    //设置广播发送隐藏虚拟按键命令
                    YcApi.newInstance().HideNviBarFull();
                } else {
                    //设置广播发送显示虚拟按键命令
                    YcApi.newInstance().ShowNviBarFull();
                }
            }
            break;
            default:
                if (enable) {
                    //设置广播发送隐藏虚拟按键命令
                    intent.setAction("com.android.intent.action.NAVBAR_SHOW");
                    intent.putExtra("cmd", "hide");
                    this.sendOrderedBroadcast(intent, null);
                    fullscreen(true);
                } else {

                    //设置广播发送显示虚拟按键命令
                    intent.setAction("com.android.intent.action.NAVBAR_SHOW");
                    intent.putExtra("cmd", "show");
                    this.sendOrderedBroadcast(intent, null);
                    fullscreen(false);
                }
                break;
        }

    }

    private static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /*
     * 全屏控制
     */
    private void fullscreen(boolean enable) {
        if (enable) { //隐藏状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else { //显示状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(lp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }


    /**
     * 重启APP
     */
    private static void reStartApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
                PendingIntent restartIntent = PendingIntent.getActivity(application.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
                if (mgr != null) {
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                            restartIntent); // 1秒钟后重启应用
                }
                application.finishActivity();
            }
        }, 1000);// 1秒钟后重启应用
    }

    /*
     * 下发系统时间 mCompName == NULL 为设置全部组份
     * 不为 NULL 时为设置指定组份
     */
    public static void sendSysTimeToDev(String mCompName) {

        Thread UpdateDevTimes = new Thread(new UpdateDevTimesRunnable(mCompName));
        UpdateDevTimes.start();
    }


    /****************************************************isOffScreenLight*********************************************************/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("touch", "touch");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        startCheckTouchTimer(600 * 1000, 600 * 1000);
        screenTouch();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 触控屏幕点亮屏幕，跳转到主界面
     */
    private void screenTouch() {
        try {
            if (isOffScreenLight) {
                if (phoneName.equals("AOSP on weiqian")) {
                    HardwareControl.SetBackLight(true);
                }
                isOffScreenLight = false;
                winID = R.id.ImBtn_3;
                onDialogRS();
                touchImgBtn3();
                Log.i("touch", "touch");
            }
            // 定时是否需要熄屏
            startScreenTimer();

        } catch (Exception e) {
            Log.i("except", e.toString());
        }
    }


    private void startCheckTouchTimer(long delay, long period) {
        if (touchCheckTimer != null) {
            touchCheckTimer.cancel();
        }
        touchCheckTimer = new Timer();
        touchCheckTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 未停留在弹窗的情况
                if (mfb.getDisplay() != null) {
                    Message msg = new Message();
                    msg.what = 1;
                    WinWidgetHandler.sendMessage(msg);


                    if (winID != R.id.ImBtn_3) {
                        winID = R.id.ImBtn_3;
                        touchImgBtn3();
                        LockDisplayShowFlag = false;
                        LockModuleDisplayShowFlag = false;
                        Log.i("offScreen", " screen show display ");
                        offScreenTimer.cancel();
                        try {
                            setAllCompBSLEDStatus(mCompName);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }, delay, period);
    }


    /*
     * 启动熄屏定时器
     */
    private void startScreenTimer() {

        if (offScreenTimer != null) {
            offScreenTimer.cancel();
        }
        offScreenTimer = new Timer();
        setOffScreenTimerTask();
    }

    /*
     * 熄屏 定时器任务
     */
    private void setOffScreenTimerTask() {

        offScreenTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                // 未停留在弹窗的情况
                if (mfb.getDisplay() != null) {

                    setScreen();
                }
            }
        }, 600 * 1000, 600 * 1000);
    }


    /**
     * 检查屏幕状态是否息屏
     */
    private void setScreen() {
        if (isOffScreenLightSwitch) {
            if (!isOffScreenLight) {
                if (isAppOnForeground()) {
                    if (phoneName.equals("AOSP on weiqian")) {
                        HardwareControl.SetBackLight(false);
                    }
                    getFragment(getFrameId(), ScreenContent.newInstance());
                    isOffScreenLight = true;
                    Log.i("offScreen", " screen Off ");
                } else {
                    Log.i("isOffScreenLight", "当前APP在后台不熄屏");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startScreenTimer();
                        }
                    }).start();
                }

            }
        }
    }

    /*** 程序是否在前台运行**/
    private boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        /* 获取Android设备中所有正在运行的App*/
        assert activityManager != null;
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {// The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /************************************************isOffScreenLight*********************************************************/

    /*
     * 捕捉开采样之后动作进行液位报警监测
     * *获取接口板开关量输入1是否有电平，有电平的情况没有液体
     **/
    private static void getSampleStatus() {

        if (IOBoardUsed) {
            if (getPublicConfigData("SAMPLE_LEVEL_SWITCH").equals("1")) {
                for (String item : strComponent.get(1)) {
                    /*开采样*/
                    if (doFlowWorking.get(item).equals("512") && sampleDoingFlag.get(item).equals("false")) {
                        sampleDoingFlag.put(item, "true");
                    } else if (doFlowWorking.get(item).equals("-1") || doFlowWorking.get(item).equals("0")) {
                        sampleDoingFlag.put(item, "false");
                    } else if ((!doFlowWorking.get(item).equals("512")) && sampleDoingFlag.get(item).equals("true")) {
                        /*获取接口板开关量输入1是否有电平，有电平的情况没有液体*/
                        byte[] arrayOfByte = shortToByte((short) 2);
                        reverse(arrayOfByte);
                        SendManager.SendCmd("所有组分" + "_读取开关量输入" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S1, 2, 200, 4);
                    }
                }
            }
        }
    }


    /**
     * 判断指定路径是否存在
     *
     * @param path
     * @return
     */
    static boolean pdlj(String path) {
        boolean a = false;
        File file = new File(path);
        if (!file.exists()) {

        } else {
            a = true;
        }
        return a;
    }

    /**
     * 获取指定文件夹下的所有名称
     */
    public List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getName());
        }
        return s;
    }

    /**
     * 截取字符串括号内容
     *
     * @param s
     * @return
     */
    static boolean isMatch(String s) {
        int result1 = s.indexOf("(");
        int result2 = s.indexOf(")");
        if (result1 != -1 && result2 != -1) {
            return true;
        }
        return false;
    }

    /**
     * 新增读取Excel数据到数据库
     */
    public void ImportData() {
        String extSdcard1 = getStoragePath(context, false).size() == 0 ? null : (getStoragePath(context, false).get(0) + File.separator);
        String pathname = extSdcard1 + "crsj/crsj.txt";
        try {
            //1,拼接表格路径 获取U盘路径
            String extSdcard;
            if (getStoragePath(context, true).size() == 2) {
                extSdcard = (getStoragePath(context, true).get(1) + File.separator);
            } else {
                extSdcard = (getStoragePath(context, true).get(0) + File.separator);
            }
            String alladd = extSdcard + "alladd/"; //导入所有数据;
            String Timeadd = extSdcard + "timeadd/";//按时间导入数据；
            List<String> s = new ArrayList<>();
            String wjm = "";
            //2.判断文件夹路径是否存在
            boolean sywj = pdlj(alladd);
            boolean sjwj = pdlj(Timeadd);
            if (sywj == true && sjwj != true) {
                s = getFilesAllName(alladd);  //获取alladd文件夹下的文件名称
                if (s.size() == 1) {
                    for (String path : s) {
                        //判断获取的文件和上一次导入的文件名称是否相等
                        try {
                            File filename = new File(pathname); // 要读取以上路径的input。txt文件
                            if (!filename.exists()) {
                                wjm = "alladd/" + path;
                            } else {
                                InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
                                BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
                                String line = br.readLine();
                                if (line == path || line.equals(path)) {
                                    wjm = "";
                                } else {
                                    wjm = "alladd/" + path;
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            } else if (sjwj == true && sywj != true) {
                s = getFilesAllName(Timeadd);  //获取Timeadd文件夹下的文件名称
                if (s.size() == 1) {
                    for (String path : s) {
                        try {
                            File filename = new File(pathname); // 要读取以上路径的input。txt文件
                            if (!filename.exists()) {
                                wjm = "timeadd/" + path;
                            } else {
                                wjm = "timeadd/" + path;
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            } else if (sywj == true && sjwj == true) {  //两个路径都存在时
                if (getFilesAllName(alladd).size() >= 1 || getFilesAllName(Timeadd).size() >= 1) { //如果两个文件夹下有文件
                    if (getFilesAllName(alladd).size() == 1 && getFilesAllName(Timeadd).size() != 1) {
                        s = getFilesAllName(alladd);  //获取alladd文件夹下的文件名称
                        if (s.size() == 1) {
                            for (String path : s) {
                                try {
                                    File filename = new File(pathname); // 要读取以上路径的input。txt文件
                                    if (!filename.exists()) {
                                        wjm = "alladd/" + path;
                                    } else {
                                        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
                                        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
                                        String line = br.readLine();
                                        if (line == path || line.equals(path)) {
                                            wjm = "";
                                        } else {
                                            wjm = "alladd/" + path;
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    if (getFilesAllName(alladd).size() != 1 && getFilesAllName(Timeadd).size() == 1) {
                        s = getFilesAllName(Timeadd);  //获取Timeadd文件夹下的文件名称
                        if (s.size() == 1) {
                            for (String path : s) {
                                try {
                                    File filename = new File(pathname); // 要读取以上路径的input。txt文件
                                    if (!filename.exists()) {
                                        wjm = "timeadd/" + path;
                                    } else {
                                        wjm = "timeadd/" + path;
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                }
            }
            //3.通过获取到的Excel文件路径写入数据
            if (wjm != "") {
                newpath = extSdcard + wjm;  //完整的Excel文件路径；
                if (pdlj(newpath)) { //判断Excel文件是否存在   如果存在就开始插入数据
                    //获取到数据路径后判断路径是全部导入还是按时间导入
                    final String[] dr = wjm.split("/");
                    String[] mc = dr[1].split(",");
                    zf = mc[0];
                    if (dr[0].equals("alladd")) {   //全部导入
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ContentTool CT = new ContentTool();
                                CT.readExcel(newpath, 2, null, null, dr[1], zf);
                            }
                        }).start();
                    }
                    if (dr[0].equals("timeadd")) {
                        String[] SJ = dr[1].split(",");
                        kssj = SJ[1];
                        String jssj = SJ[2];
                        zfmc = SJ[0];
                        int loc = jssj.indexOf(".");//首先获取字符的位置
                        newjssj1 = jssj.substring(0, loc);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ContentTool CT = new ContentTool();
                                CT.readExcel(newpath, 5, kssj, newjssj1, dr[1], zfmc);
                            }
                        }).start();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /*
par1:文件目录，par2：文件名称,par3:备份文件目录，par4：备份文件目录内子文件夹,par5:指令
* **/
    public static void backupOrRestoreFiles(String formFilePath, String formFileName, String toFilePath, String toDir, String cmd) {
        try {
            // 需要备份的数据库路径
            File dbFile = new File(formFilePath, formFileName);
            // 路径不存在就创建
            File exportDir = new File(toFilePath, toDir);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File backup = new File(exportDir, dbFile.getName());
            switch (cmd) {
                case COMMAND_BACKUP:
                    try {
                        if (getFileSize(dbFile) >= getFileSize(backup) && (getFileSize(dbFile) != -1)) {
                            backup.createNewFile();
                            fileCopy(dbFile, backup);
                            return;
                        }
                    } catch (Exception e) {
                        Log.e("FileBackups", "数据备份异常" + e.getMessage());
                        return;
                    }
                    break;
                case "RESTORE_BACKUP": {
                    try {
                        backup.createNewFile();
                        fileCopy(dbFile, backup);
                        return;
                    } catch (Exception e) {
                        Log.e("FileBackups", "数据备份异常" + e.getMessage());
                        return;
                    }
                }
            }

        } catch (Exception E) {
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);

            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}