package com.yzlm.cyl.cfragment.Content.NoBoard;

import android.widget.EditText;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardHistory;
import com.yzlm.cyl.cfragment.R;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus3;
import static com.yzlm.cyl.cfragment.Global.context;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/24
 */
public class NoBoardContentTool {


    public static String getNoBoardLastValue(String component, String flow, String unit) {
        String lastValue = "0";
        List<Map> list;
        NoBoardHistory mHistory = new NoBoardHistory(context);

        list = mHistory.select(component, flow, null, null, 0, 1);

        if (list.size() != 0) {
            try {
                lastValue = ConvertUnit(list.get(0).get("unit").toString(), unit, Double.valueOf(list.get(0).get("C").toString()), Integer.parseInt(getNoBoardConfigData(component, "YXWS")));
            } catch (NullPointerException e) {
                android.util.Log.d("null", "GRASP Global getLastValue() 2 try");
            }
        }
        return lastValue;
    }


    /*( sDown 与 sUp )
     * 组份 float参数 edit 输入
     * */
    public static void setNoBoardComponentEditFloatData(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, String info) {
        if (!editTextInputStatus(v, fDown, fUp)) {
            v.setText(String.valueOf(sDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + fDown + " ~ " + fUp + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveNoBoardOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }

    /*
    （ sDown  sUp)且≠0
     * 组份 float参数 edit 输入
     * */
    public static void setNoBoardComponentEditNoZeroFloatData(String mCompName, EditText v, int iPoint, double fDown, double fUp, double sDeft, String info) {
        if (!editTextInputStatus3(v, fDown, fUp)) {
            v.setText(String.valueOf(sDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + fDown + " ~ " + fUp + " ) " + context.getResources().getString(R.string.and) + "≠0!" + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveNoBoardOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }


    /*
     * 组份 int参数 edit 输入
     * */
    public static void setNoBoardComponentEditIntData(String mCompName, EditText v, int iPoint, int iDown, int iUp, int iDeft, String info) {
        if (!editTextInputStatus(v, iDown, iUp)) {
            v.setText(String.valueOf(iDeft));
            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + iDown + " ~ " + iUp + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
        }
        String strValue = editDataFormat(v.getText().toString(), iPoint);
        v.setText(strValue);
        saveNoBoardOperationLogMsg(mCompName, info + strValue, ErrorLog.msgType.操作_信息);
    }

    /**
     * @param compName 当前组分
     * @return 获取当前组份有几个量程
     * *
     */
    public static int GetNoBoardPlatRangSum(String compName) {
        int sum = 0;

        try {
            String L1H = getNoBoardConfigData(compName, "LC1H");
            if (!L1H.equals("0") && (!L1H.equals(""))) {
                sum++;
            } else {
                return sum;
            }
            String L2H = getNoBoardConfigData(compName, "LC2H");
            if (!L2H.equals("0") && (!L2H.equals(""))) {
                sum++;
            } else {
                return sum;
            }
            String L3H = getNoBoardConfigData(compName, "LC3H");
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

}
