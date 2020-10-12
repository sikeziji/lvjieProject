package com.yzlm.cyl.cfragment.DBConvert.Tool;

import android.widget.EditText;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.floatToBytes;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static com.yzlm.cyl.clibrary.Util.SystemDateTime.setDate;
import static com.yzlm.cyl.clibrary.Util.SystemDateTime.setTime;

/**
 * Created by zwj on 2018/8/16.
 */

@SuppressWarnings("unchecked")
public class Tool {


    /*
     * 输入框限制
     * editType :0: 整数
     *           1: 浮点数
     *       2: 字符串
     * **/
    public static String editDataFormat(String strValue, int maxPoint) {

        String strData = strValue;

        int pointBit = strData.indexOf(".");
        if (pointBit != -1) {
            int savePoint = strData.length() - (pointBit + 1);
            if (savePoint != 0 && pointBit != 0) {
                strData = dataFormat2(strData, savePoint > maxPoint ? maxPoint : savePoint);
            } else {
                strData = String.valueOf(Float.valueOf(strData));
            }
        } else {
            strData = String.valueOf(Integer.valueOf(strData));
        }

        return strData;
    }

    /*
    保留位数
    * **/
    public static String dataFormat2(String fData, int point) {
        String strData = fData;
        if (strData.contains(".")) {
            strData = String.format("%." + point + "f", Double.parseDouble(strData));
        }
        return strData;
    }

    /*
    保留位数
     **/
    public static String dataFormat2(float fData, int zPoint, int point) {


        String str;
        DecimalFormat fNum;
        String iPoint = "", sPoint = "";
        for (int i = 0; i < zPoint; i++) {
            iPoint += "0";
        }
        for (int i = 0; i < point; i++) {
            sPoint += "0";
        }

        fNum = new DecimalFormat("##" + iPoint + "." + sPoint);
        str = fNum.format(fData);

        return str;


    }


    // 保留位数
    public static String dataFormat(float data, int point) {
        String str;
        DecimalFormat fNum;

        if (point == 3) {
            fNum = new DecimalFormat("##000.000");
            str = fNum.format(data);
        } else {
            fNum = new DecimalFormat("##0000.00");
            str = fNum.format(data);
        }
        return str;
    }

    // 保留位数
    public static String dataFormat(double data, int point) {
        String str;
        DecimalFormat fNum;
        if (point == 3) {
            fNum = new DecimalFormat("##000.000");
            str = fNum.format(data);
        } else {
            fNum = new DecimalFormat("##0000.00");
            str = fNum.format(data);
        }
        return str;
    }


    /*
     * 找出未出现的最小正整数
     *
     * @param
     * @param
     * @date 2016-10-7
     * @author shaobn
     */
    public static int findArrayMex(int[] A, int n) {

        int[] res = new int[n];

        for (int i = 0; i < n; i++) {

            if (A[i] > 0 && A[i] <= n) {
                res[A[i] - 1] = 1;
            }
        }
        for (int i = 0; i < n; i++) {

            if (res[i] == 0) {
                return i + 1;
            }
        }
        return n + 1;
    }


    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母、数字和汉字      
        String regEx = "[^A-F0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String getNewTime(String strTime, int hour) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retString = null;
        try {
            Date date = dateFormat.parse(strTime); // 指定日期
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR, hour);
            date = cal.getTime();
            retString = dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retString;
    }

    /*----------------------------------------------------------------------------------------------*/
            // 条件判断
    /*
     *  ( dDown 与 dUp )
     */
    public static boolean editTextInputStatus(EditText et, double sDown, double sUp) {
        return (!et.getText().toString().equals(""))
                && ((Double.parseDouble(et.getText().toString()) > sDown)
                && (Double.parseDouble(et.getText().toString()) < sUp));
    }

    /*
     * （ dDown  dUp]
     */
    public static boolean editTextInputStatus2(EditText et, double sDown, double sUp) {
        return (!et.getText().toString().equals(""))
                && ((Double.parseDouble(et.getText().toString()) > sDown)
                && (Double.parseDouble(et.getText().toString()) <= sUp));
    }

    /*
     * （ dDown  dUp)且≠0
     */
    public static boolean editTextInputStatus3(EditText et, double sDown, double sUp) {
        return (!et.getText().toString().equals(""))
                && (Double.parseDouble(et.getText().toString()) != 0.0)
                && ((Double.parseDouble(et.getText().toString()) > sDown)
                && (Double.parseDouble(et.getText().toString()) <= sUp));
    }

    /*
     * [ dDown  dUp]
     */
    public static boolean editTextInputStatus4(EditText et, double sDown, double sUp) {
        return (!et.getText().toString().equals("")) && (((Double.parseDouble(et.getText().toString()) > sDown)
                && (Double.parseDouble(et.getText().toString()) <= sUp)) || (Double.parseDouble(et.getText().toString()) == sDown));
    }

    /*----------------------------------------------------------------------------------------------*/
                // 条件判断
    /*
     * ( iDown 与 iUp )
     */
    public static boolean editTextInputStatus(EditText et, int sDown, int sUp) {
        return (!et.getText().toString().equals("")) && (Long.valueOf(et.getText().toString()) < 2147483647)
                && ((Integer.parseInt(et.getText().toString()) > sDown)
                && (Integer.parseInt(et.getText().toString()) < sUp));
    }

    /*
     * ( iDown 与 iUp ]
     */
    public static boolean editTextInputStatus2(EditText et, int sDown, int sUp) {
        return (!et.getText().toString().equals("")) && (Long.valueOf(et.getText().toString()) < 2147483647)
                && ((Integer.parseInt(et.getText().toString()) > sDown)
                && (Integer.parseInt(et.getText().toString()) <= sUp));
    }

    /*
     * ( iDown 与 iUp )且≠0
     */
    public static boolean editTextInputStatus3(EditText et, int sDown, int sUp) {
        return (!et.getText().toString().equals("")) && (Long.valueOf(et.getText().toString()) < 2147483647)
                && ((Integer.parseInt(et.getText().toString()) > sDown)
                && (Integer.parseInt(et.getText().toString()) < sUp)
                && (Integer.parseInt(et.getText().toString()) != 0));
    }

    /*
     * [ iDown  iUp]
     */
    public static boolean editTextInputStatus4(EditText et, int iDown, int iUp) {
        return (!et.getText().toString().equals("")) && (((Double.parseDouble(et.getText().toString()) > iDown)
                && (Double.parseDouble(et.getText().toString()) <= iUp)) || (Double.parseDouble(et.getText().toString()) == iDown));
    }


    /*----------------------------------------------------------------------------------------------*/

    /*下发指令给CKA
     * 界面06指令设置CK板float参数   参数范围(fDown， fUp)
     * 安卓屏不保存
     * */
    public static void setCKAEditFloatData(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, int ckRegNum, String info) {

        setComponentEditFloatData(mCompName, v, iPoint, fDown, fUp, sDeft, info);
        // 发送指令修改
        byte[] sByte = (floatToBytes(Float.parseFloat(v.getText().toString())));
        SendManager.SendCmd(mCompName + "_" + info + "_06_" + ckRegNum, S0, 3, 100, sByte);

    }

    /*下发指令给CKA
     * 界面06指令设置CK板float参数   参数范围(fDown， fUp]
     * 安卓屏不保存
     * */
    public static void setCKAEditFloatData2(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, int ckRegNum, String info) {

        setComponentEditFloatData2(mCompName, v, iPoint, fDown, fUp, sDeft, info);
        // 发送指令修改
        byte[] sByte = (floatToBytes(Float.parseFloat(v.getText().toString())));
        SendManager.SendCmd(mCompName + "_" + info + "_06_" + ckRegNum, S0, 3, 100, sByte);

    }



    /*下发指令给CKA
     * 界面06指令设置CK板float参数   参数范围[fDown， fUp]
     * 安卓屏不保存
     * */
    public static void setCKAEditFloatData4(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, int ckRegNum, String info) {

        setComponentEditFloatData4(mCompName, v, iPoint, fDown, fUp, sDeft, info);
        // 发送指令修改
        byte[] sByte = (floatToBytes(Float.parseFloat(v.getText().toString())));
        SendManager.SendCmd(mCompName + "_" + info + "_06_" + ckRegNum, S0, 3, 100, sByte);

    }


    /*----------------------------------------------------------------------------------------------*/


    /* 下发指令给CKA
     * 界面06指令设置CK板int参数（iDown,iUp）
     * 安卓屏不保存
     * */
    public static void setCKAEditIntData(String mCompName, EditText v, int iPoint, int iDown, int iUp, int iDeft, int ckRegNum, String info) {

        setComponentEditIntData(mCompName, v, iPoint, iDown, iUp, iDeft, info);
        // 发送指令修改
        byte[] sByte = copybyte(toByteArray(Integer.parseInt(v.getText().toString()), 4));
        SendManager.SendCmd(mCompName + "_" + info + "_06_" + ckRegNum, S0, 3, 100, sByte);

    }

    /* 下发指令给CKA
     * 界面06指令设置CK板int参数[iDown,iUp]
     * 安卓屏不保存
     * */
    public static void setCKAEditIntData4(String mCompName, EditText v, int iPoint, int iDown, int iUp, int iDeft, int ckRegNum, String info) {

        setComponentEditIntData4(mCompName, v, iPoint, iDown, iUp, iDeft, info);
        // 发送指令修改
        byte[] sByte = copybyte(toByteArray(Integer.parseInt(v.getText().toString()), 4));
        SendManager.SendCmd(mCompName + "_" + info + "_06_" + ckRegNum, S0, 3, 100, sByte);
    }




    /*----------------------------------------------------------------------------------------------*/

    /*
     * 组份 float参数 edit 输入 ( dDown 与 dUp )
     * 安卓屏不保存
     * */
    public static void setComponentEditFloatData(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, String info) {
        if (!editTextInputStatus(v, fDown, fUp)) {
            v.setText(String.valueOf(sDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + fDown + " ~ " + fUp + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }

    /*
     * 组份 float参数 edit 输入 ( dDown 与 dUp ]
     * 安卓屏不保存
     * */
    public static void setComponentEditFloatData2(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, String info) {
        if (!editTextInputStatus2(v, fDown, fUp)) {
            v.setText(String.valueOf(sDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + fDown + " ~ " + fUp + " ] " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }

    /*
     * 组份 float参数 edit 输入  (fDown, fUp ) "≠0!"
     * 安卓屏不保存
     * */
    public static void setComponentEditNoZeroFloatData(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, String info) {
        if (!editTextInputStatus3(v, fDown, fUp)) {
            v.setText(String.valueOf(sDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + fDown + " ~ " + fUp + " ) " + context.getResources().getString(R.string.and) + "≠0!" + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }

    /*
     * 组份 float参数 edit 输入 [ dDown  dUp]
     * 安卓屏不保存
     * */
    public static void setComponentEditFloatData4(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, String info) {
        if (!editTextInputStatus4(v, fDown, fUp)) {
            v.setText(String.valueOf(sDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + fDown + " ~ " + fUp + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }


    /*----------------------------------------------------------------------------------------------*/

    /*
     * 组份 int参数 edit 输入 (iDown, iUp )
     * 安卓屏不保存
     * */
    public static void setComponentEditIntData(String mCompName, EditText v, int iPoint, int iDown, int iUp, int iDeft, String info) {
        if (!editTextInputStatus(v, iDown, iUp)) {
            v.setText(String.valueOf(iDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + iDown + " ~ " + iUp + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }

    /*
     * 组份 int参数 edit 输入 [iDown, iUp ]
     * 安卓屏不保存
     * */
    public static void setComponentEditIntData4(String mCompName, EditText v, int iPoint, int iDown, int iUp, int iDeft, String info) {
        if (!editTextInputStatus4(v, iDown, iUp)) {
            v.setText(String.valueOf(iDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " [ " + iDown + " ~ " + iUp + " ] " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }


    /*----------------------------------------------------------------------------------------------*/
    /*
     * 保存 系统变量值  (fDown, fUp )
     * 安卓屏不保存
     * */
    public static void setEditSystemData(String key, EditText v, int iPoint, double fDown, double fUp, double sDeft, String info) {
        if (!editTextInputStatus(v, fDown, fUp)) {
            v.setText(String.valueOf(sDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + fDown + " ~ " + fUp + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveOperationLogPublicDataModifyMsg("公共", key, v.getText().toString(), info, ErrorLog.msgType.操作_信息);
        updatePublicConfigData(key, v.getText().toString());
    }


    /*
     * 保存系统变量值 (iDown, iUp )
     *      * 安卓屏不保存
     * */
    public static void setEditSystemiData(String key, EditText v, int iPoint, int iDown, int iUp, int iDeft, String info) {
        if (!editTextInputStatus(v, iDown, iUp)) {
            v.setText(String.valueOf(iDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + iDown + " ~ " + iUp + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveOperationLogPublicDataModifyMsg("公共", key, v.getText().toString(), info, ErrorLog.msgType.操作_信息);
        updatePublicConfigData(key, v.getText().toString());
    }

    /*----------------------------------------------------------------------------------------------*/


    public static Object[] byteMerger(byte[] bt1, byte[] bt2) {
        List result = new ArrayList();
        result.addAll(Collections.singleton(bt1));
        result.addAll(Collections.singleton(bt2));
        return result.toArray();
    }






    public static void SetDateTime(String compName, int year, int month, int day, int hour, int minute, int second) {
        try {
            if (year != 0 && month != 0 && day != 0) {
                setDate(year, month, day);
            }
            //if (hour != 0 && minute != 0 && second != 0) {
                setTime(hour, minute, second);
            //}
            if (compName.equals("all")) {
                for (String item : strComponent.get(1)) {
                    //时间矫正
                    AddError(item, 614, 运维_信息);
                }
            } else {
                //时间矫正
                AddError(compName, 614, 运维_信息);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
