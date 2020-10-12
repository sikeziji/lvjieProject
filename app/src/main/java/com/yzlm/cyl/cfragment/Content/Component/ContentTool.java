package com.yzlm.cyl.cfragment.Content.Component;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yzlm.cyl.cfragment.Adapter.FlowInfo;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.Action;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowStep;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.DataDBHelper;
import com.yzlm.cyl.cfragment.Content.Component.Config.MeaParTable;
import com.yzlm.cyl.cfragment.Dialog.DialogMsg;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Excel.ExcelUtils;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.phoneName;
import static com.yzlm.cyl.cfragment.Global.AnalogQuantity_CycleThread;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getFlows;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.SDCardUtils.getStoragePath;
import static java.lang.Thread.sleep;

/**
 *
 */
public class ContentTool {


    /**
     * @return
     * @author hp
     * @time 2019/6/27  10:43
     * @parms
     * @describe 获取测量参数配置文件中的上限值
     */
    public static String getParUpLimit(MeaParTable mt, Map<String, MeaParTable> lMeaPar, String compName, String name) {
        try {

            if (mt.MeaParMap.size() > 0) {
                for (int i = 0; i < mt.MeaParMap.size(); i++) {
                    if (lMeaPar.get(compName).Get(String.valueOf(i)).getName().equals(name)) {
                        return lMeaPar.get(compName).Get(String.valueOf(i)).getParUp();
                    }
                }
            }

        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return null;
    }


    /**
     * @return
     * @author hp
     * @time 2019/6/27  10:51
     * @parms
     * @describe 获取测量参数配置文件中的参数显示名称
     */
    public static String getParShowName(MeaParTable mt, Map<String, MeaParTable> lMeaPar, String compName, String name) {
        try {

            if (mt.MeaParMap.size() > 0) {
                for (int i = 0; i < mt.MeaParMap.size(); i++) {
                    if (lMeaPar.get(compName).Get(String.valueOf(i)).getName().equals(name)) {
                        return lMeaPar.get(compName).Get(String.valueOf(i)).getShowName();
                    }
                }
            }

        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return null;
    }


    /**
     * @return
     * @author hp
     * @time 2019/6/27  10:51
     * @parms
     * @describe 获取测量参数配置文件中的参数下限值
     */
    public static String getParDownLimit(MeaParTable mt, Map<String, MeaParTable> lMeaPar, String compName, String name) {
        if (mt.MeaParMap.size() > 0) {
            for (int i = 0; i < mt.MeaParMap.size(); i++) {
                if (lMeaPar.get(compName).Get(String.valueOf(i)).getName().equals(name)) {
                    return lMeaPar.get(compName).Get(String.valueOf(i)).getParDown();
                }
            }
        }
        return null;
    }

    /**
     * @return
     * @author hp
     * @time 2019/6/27  10:51
     * @parms
     * @describe 获取测量参数配置文件中的参数默认值
     */
    public static String getParDefParValue(MeaParTable mt, Map<String, MeaParTable> lMeaPar, String compName, String name) {

        try {
            if (mt.MeaParMap.size() > 0) {
                for (int i = 0; i < mt.MeaParMap.size(); i++) {
                    if (lMeaPar.get(compName).Get(String.valueOf(i)).getName().equals(name)) {
                        return lMeaPar.get(compName).Get(String.valueOf(i)).getStrDefValue();
                    }
                }
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return null;
    }


    /**
     * @param compName 当前组分
     * @return 获取当前组份有几个量程
     * *
     */
    public static int GetPlatRangSum(String compName) {
        int sum = 0;

        try {
            String L1H = getConfigData(compName, "LC1H");
            if (!L1H.equals("0") && (!L1H.equals(""))) {
                sum++;
            } else {
                return sum;
            }
            String L2H = getConfigData(compName, "LC2H");
            if (!L2H.equals("0") && (!L2H.equals(""))) {
                sum++;
            } else {
                return sum;
            }
            String L3H = getConfigData(compName, "LC3H");
            if (!L3H.equals("0") && (!L3H.equals(""))) {
                sum++;
            } else {
                return sum;
            }
            return sum;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param compName 组分明
     * @param flowName 流程名称
     * @param strRange 当前判定的量程，null 所有量程
     * @param rangeSum 设备总的量程数，
     * @return 该测量组分是否有这个流程
     */
    public static boolean isHaveFlow(String compName, String flowName, String strRange, int rangeSum) {
        try {
            FlowTable ft = getFlows(compName);
            FlowClass fc = null;
            if (strRange == null) {
                for (int i = 0; i < rangeSum; i++) {
                    fc = ft.getFlow(flowName + "_" + (i + 1));
                    if (fc.getSteps().size() > 1) {
                        return true;
                    }
                }
            } else {
                fc = ft.getFlow(flowName + "_" + strRange);
                if (fc != null) {
                    return fc.getSteps().size() > 1;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 组分的流程进行CRC16校验和下位机进行核对
     *
     * @param compName  组分名称
     * @param flowNames 流程名称s
     */
    public static short getFlowCrc16Value(String compName, String[] flowNames) {
        FlowTable ft = Global.getFlows(compName);
        FlowClass fc;
        byte[] flows = new byte[0];
        for (String flowName : flowNames) {
            fc = ft.getFlow(flowName);
            if (fc != null) {
                Map<Integer, FlowStep> stepMap = fc.getSteps();

                for (int j = 0; j < stepMap.size(); j++) {
                    FlowStep step = stepMap.get(j + 1);
                    byte[] btSteps = new byte[4];
                    //将流程中0xffff作为流程结束动作，不进行校验
                    if (step != null && (step.getActionCoding() & 0xffff) == 0xffff) {
                        break;
                    }
                    if (step != null) {
                        btSteps[0] = (byte) ((step.getActionCoding() & 0x00ff));
                    }
                    if (step != null) {
                        btSteps[1] = (byte) ((step.getActionCoding() & 0xff00) >> 8);
                    }
                    if (step != null) {
                        btSteps[2] = (byte) (step.getSampleCount() & 0xff);
                    }
                    if (step != null) {
                        btSteps[3] = (byte) (step.getMeasurement() & 0xff);
                    }
                    flows = copybyte(flows, btSteps);
                }
            }
        }
        // 将所有流程计算CRC
        byte[] crc = crc16(flows, flows.length);
        return (short) ((short) (((crc[0] & 0x000000FF) << 8) | (0x000000FF & crc[1])) & 0xffff);
    }


    public static String getLastValue(String component, String flow, String unit) {
        String lastValue = "0";
        List<Map> list;
        History mHistory = new History(context);

        list = mHistory.select(component, flow, null, null, 0, 1);

        if (list.size() != 0) {
            try {
                lastValue = ConvertUnit(list.get(0).get("unit").toString(), unit, Double.valueOf(list.get(0).get("C").toString()), Integer.parseInt(getConfigData(component, "YXWS"))/* Integer.parseInt(mOtherParameterDB.configMap.get("YXWS").toString())*/);
            } catch (NullPointerException e) {
                android.util.Log.d("null", "GRASP Global getLastValue() 2 try");
            }
        }
        return lastValue;
    }


    public static String ConvertUnit(String oldUnit, String unit, double lastValue, int keep) {


        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.setMaximumFractionDigits(keep);
        String rs = String.format("%." + keep + "f ", lastValue);
        try {
            if (!oldUnit.equals(unit)) {
                switch (unit) {
                    case "mg/L":
                        rs = String.format("%." + keep + "f ", lastValue / 1000);
                        break;
                    case "μg/L":
                    case "ppb":
                        rs = String.format("%." + keep + "f ", lastValue * 1000);
                        break;
                    case "ppm":
                        break;
                    case "NTU":
                        break;
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("ConvertUnit：lastValue " + lastValue + "数据转换异常");
        }
        return rs;
    }


    public static String ConvertUnit(String oldUnit, String unit, String strValue, int keep) {
        String rs = "";
        try {
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
            df.setMaximumFractionDigits(keep);

            int point = strValue.indexOf(".");
            if (point == -1) {
                rs = strValue;
                if (!oldUnit.equals(unit)) {
                    switch (unit) {
                        case "mg/L":
                            rs = String.valueOf(Integer.parseInt(strValue) / 1000);
                            break;
                        case "μg/L":
                        case "ppb":
                            rs = String.valueOf(Integer.parseInt(strValue) * 1000);
                            break;
                        case "ppm":
                            break;
                        case "NTU":
                            break;
                    }
                }
            } else {
                rs = ConvertUnit(oldUnit, unit, Double.parseDouble(strValue), keep);
            }
        } catch (Exception e) {
            saveExceptInfo2File("ConvertUnit：value " + strValue + "数据转换异常");
        }

        return rs;
    }

    /////////////////////历史数据
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    /**
     * 历史数据导出
     *
     * @param dataHandler
     * @param allList
     * @param compName
     */
    public static void HistoryExport(Handler dataHandler, List<Map> allList, String compName) {

        if (allList.size() > 0) {
            List<String[]> allDataItems = new ArrayList<>();
            try {
                try {
                    for (Map item : allList) {
                        String[] dataTitle = new String[]{"time", "component", "flow", "C", "unit", "temperature", "energy", "A", "tag"};
                        String[] dataSingleItem = new String[dataTitle.length];
                        for (int i = 0; i < dataSingleItem.length; i++) {
                            try {
                                dataSingleItem[i] = item.get(dataTitle[i]).toString();
                            } catch (NullPointerException e) {
                                dataSingleItem[i] = "NULL";
                            }
                        }
                        allDataItems.add(dataSingleItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    saveExceptInfo2File("组分[" + compName + "] 导出历史数据异常 ：" + e.toString());
                }
                String extSdcard = getExtSdcard();
                if (extSdcard != null) {
                    File file = new File(extSdcard + "waterData");
                    makeDir(file);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
                    String[] title = new String[]{context.getString(R.string.time), context.getString(R.string.component), context.getString(R.string.flow),
                            context.getString(R.string.measurement_value), context.getString(R.string.unit), context.getString(R.string.temperature),
                            context.getString(R.string.energy), context.getString(R.string.absorbancy), context.getString(R.string.tag)};
                    ExcelUtils.createExcelAndTitle(file.toString() + "/" + compName + "History_" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".xls", context.getString(R.string.historyData), title);
                    ExcelUtils.writeObjListToExcel(dataHandler, allDataItems, extSdcard + "waterData" + "/" + compName + "history_" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".xls", context);
                } else {
                    Message msg = new Message();
                    msg.what = 400;
                    dataHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = 600;
                dataHandler.sendMessage(msg);
                saveExceptInfo2File("组分[" + compName + "] 导出历史数据导出异常 ：" + e.toString());
            }
        } else {
            Message msg = new Message();
            msg.what = 300;
            dataHandler.sendMessage(msg);
        }
    }

    /**
     * 历史数据打印
     *
     * @param handler
     * @param items
     * @param compName
     * @param iPoint
     */
    public static void PrintHistoryData(final Handler handler, final List<String> items, final String compName, final int iPoint) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (items.size() > 0) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                    AnalogQuantity_CycleThread.suspend();
                    saveOperationLogMsg(compName, "打印历史数据", ErrorLog.msgType.操作_信息);
                    for (int i = 0; i < items.size(); i++) {

                        String[] strData = items.get(i).split("\t");
                        String sendStr = "\r\n" + strData[2] + "\r\n" + strData[1] + "\r\n";

                        if (strData[2].contains("μg/L")) {
                            String value = strData[2].split("=")[1].split("μg/L")[0];
                            String afterValue = ConvertUnit("μg/L", "mg/L", Double.valueOf(value), iPoint);
                            sendStr = "\r\n" + compName + "=" + afterValue + "mg/L" + "\r\n" + strData[1] + "\r\n";
                        }

                        SendManager.SendCmd("IO" + "_" + "打印输出_09_10", S1, 1, 200, sendStr.getBytes());
                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        });
        thread.start();
    }


    /**
     * 主控板读取到流程解析处理
     *
     * @param stMap 存储map
     * @return 返回解析后List
     */
    public static List<FlowInfo> setLvDeviceFlowInfo(String Component, Map<String, FlowStep> stMap) {
        List<FlowInfo> mListDevice = new ArrayList<>();
        mListDevice.clear();
        ActionTable at = Global.getActions(Component);
        Map<String, Action> LAction = at.getLAction();
        if (LAction == null) return null;

        for (int i = 0; i < stMap.size(); i++) {
            FlowInfo flowInfo = new FlowInfo();
            FlowStep fs = stMap.get(String.valueOf(i + 1));
            if (fs != null) {
                flowInfo.setActionName((i + 1) + "_" + " " + at.GetActionName((short) fs.getActionCoding()));
                flowInfo.setTimes(fs.getSampleCount());
                flowInfo.setUpDown(fs.getMeasurement());
                flowInfo.setCodeing(fs.getActionCoding());
                mListDevice.add(flowInfo);
            }
        }
        return mListDevice;
    }


    /**
     * 设置W200计量参数名称
     *
     * @param compName 组分名称
     */
    public static void setW200JLBoard(String compName) {
        if (QueryMeasBoardType(compName).equals("2")) {
            for (int i = 0; i < 5; i++) {
                if (getConfigData(compName, "JLB_" + (i + 1) + "_NAME").equals("")) {
                    updateConfigData(compName, "JLB_" + (i + 1) + "_NAME", (i + 1) + "_JL" + (i + 1));
                }
            }
        }
    }

    /**
     * 导入数据
     *
     * @param excelPath
     */
    public DataDBHelper crsj;
    private List<Map> cursor;

    public void readExcel(String excelPath, int requestCode, String startTime, String endTime, String wjm, String zfmc) {
        final DialogMsg dialogMsg = new DialogMsg(); //弹窗

        History mHistory;
        mHistory = new History(context);
        crsj = DataDBHelper.getInstance(context);
        try {
            //表头列名
            List<String> list1 = new ArrayList<String>();
            InputStream input = new FileInputStream(new File(excelPath));
            POIFSFileSystem fs = new POIFSFileSystem(input);

            //数据自检中
            main.removeDestopText(mfb);
            Bundle bundle = new Bundle();
            bundle.putString("alert-msg", context.getString(R.string.data_sjdrz));
            dialogMsg.setArguments(bundle);
            dialogMsg.show(fm, "Dialog_msg");

            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            int j = 0;

            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator<Cell> cells = row.cellIterator();
                List<String> list = new ArrayList<String>();
                while (cells.hasNext()) {
                    if (row.getRowNum() == 0) {
                        HSSFCell cell = (HSSFCell) cells.next();
                        String mc = cell.toString();
                        String aaa;
                        if (isMatch(mc)) {
                            aaa = mc.substring(mc.indexOf("(") + 1, mc.indexOf(")"));
                            list1.add(aaa); //列名
                        } else {
                            main.removeDestopText(mfb);
                            Dialog_Err st = new Dialog_Err();
                            Bundle aa = new Bundle();
                            aa.putString("alert-err", context.getString(R.string.data_sjgscw));
                            st.setArguments(aa);
                            st.show(fm, "Dialog_err");
                            return;
                        }
                        //判断表格字段 和 目标数据表的字段是否相同 （）
                        boolean bool = false;
                        if (requestCode == 2 || requestCode == 5) { //History字段
                            cursor = mHistory.selectcolumns();
                        }

                        for (Map item : cursor) {
                            String a = item.get("name").toString();
                            if (a == aaa || a.equals(aaa)) {
                                bool = true;
                                break;
                            }
                        }
                        if (bool == false) {
                            dialogMsg.dismiss();
                            main.removeDestopText(mfb);
                            Dialog_Err st = new Dialog_Err();
                            Bundle aa = new Bundle();
                            aa.putString("alert-err", context.getString(R.string.data_wjcw));
                            st.setArguments(aa);
                            st.show(fm, "Dialog_err");
                            return;
                        }
                    } else {
                        HSSFCell cell = (HSSFCell) cells.next();
                        list.add(cell.toString());
                    }
                }
                if (row.getRowNum() == 0) {
                    //备份数据
                    if (requestCode == 2 || requestCode == 5) {
                        //导入数据前备份数据
                        String sql1 = "create table if not exists newHistory(id ,time,flow,component,C,unit,temperature,energy,A,tag)";  //创建
                        crsj.execSQL(sql1);
                        String insert = "insert into newHistory select *  from History"; //数据备份
                        crsj.execSQL(insert);
                        //删除备份数据库里面重复的数据
                        String sql2 = "delete from newHistory where newHistory.rowid not in (select MAX(newHistory.rowid) from newHistory group by id)";
                        crsj.execSQL(sql2);
                    }
                    //删除数据
                    //删除原来数据库的所有数据
                    if (requestCode == 2) {
                        crsj.execSQL("delete from History where component =" + "'" + zfmc + "'");
                    }
                    //按时间导入历史数据
                    if (requestCode == 5) {
                        crsj.execSQL("delete from History where component = " + "'" + zfmc + "'" + " and datetime(time) between " + "'" + startTime + "'" + " and " + "'" + endTime + "'");
                    }

                    for (String attribute : list1) {
                        j++; //获取到excel有几列数据
                    }

                }
                //去掉表头
                if (row.getRowNum() != 0) {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 0; i < j; i++) {
                        map.put(list1.get(i), list.get(i));
                    }
                    crsj.insert("History", map);
                }
            }
            //把文件名插入TXT
            String extSdcard = getStoragePath(context, false).size() == 0 ? null : (getStoragePath(context, false).get(0) + File.separator);
            String a11 = extSdcard + "crsj";
            method1(a11, wjm);
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle aa = new Bundle();
            aa.putString("alert-err", context.getString(R.string.data_sjdrcg));
            st.setArguments(aa);
            st.show(fm, "Dialog_err");
            dialogMsg.dismiss();
            saveOperationLogMsg(zfmc, "自检成功", ErrorLog.msgType.操作_信息);
        } catch (Exception ex) {
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle aa = new Bundle();
            aa.putString("alert-err", "文件错误，请检查文件格式或内容是否完整！");
            st.setArguments(aa);
            st.show(fm, "Dialog_err");
        } finally {
        }
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
     * 新增把文件名插入txt
     */
    public void method1(String path, String content) {
        FileWriter fw = null;
        File file = new File(path);
        if (!file.exists()) { //不存在则创建文件
            file.mkdirs();
        }
        try {//异常处理
            //如果file文件夹下没有test.txt就会创建该文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(path + "/crsj.txt"));
            bw.write(content);//在创建好的文件中写入"Hello"
            bw.close();//一定要关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getExtSdcard() {
        try {
            List<String> lSdcardPath = getStoragePath(context, true);
            String strSdcard = "";
            for (int i = 0; i < lSdcardPath.size(); i++) {
                if (lSdcardPath.get(i).toLowerCase().contains("usb")) {
                    strSdcard = lSdcardPath.get(i);
                }
            }
            String extSdcard = strSdcard.equals("") ? null : strSdcard + File.separator;
            //String extSdcard = getStoragePath(context, true).size() == 0 ? null : (getStoragePath(context, true).get(0) + File.separator);
            if (phoneName.equals("rk3288")) {
                extSdcard = strSdcard.equals("") ? null : (getStoragePath(context, true).get(1) + File.separator);
            }
            return extSdcard;
        } catch (Exception e) {
            Log.i("exception", "getStoragePath");
        }
        return null;
    }

}
