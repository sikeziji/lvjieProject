package com.yzlm.cyl.cfragment.Content.Component.List2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by HASEE on 2017/8/16.
 */

@SuppressWarnings("unchecked")
public class List2_Content5 extends SubFragment {
    private static EditText mETyComp;
    private static EditText mETMComp;
    private static EditText mETdComp;
    private static EditText mETHComp;
    private static EditText mETmComp;
    private static EditText mETsComp;

    private static List2_Content5 fragment = null;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int sec;

    public static List2_Content5 newInstance() {
        if (fragment == null) {
            fragment = new List2_Content5();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list2_content5;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            mETyComp = (EditText) v.findViewById(R.id.eTy_comp);
            mETMComp = (EditText) v.findViewById(R.id.eTM_comp);
            mETdComp = (EditText) v.findViewById(R.id.eTd_comp);
            mETHComp = (EditText) v.findViewById(R.id.eTH_comp);
            mETmComp = (EditText) v.findViewById(R.id.eTm_comp);
            mETsComp = (EditText) v.findViewById(R.id.eTs_comp);

            Button btnTimeManageOKComp = (Button) v.findViewById(R.id.Btn_timeManageOK_comp);
            Button btnTimeManageResetComp = (Button) v.findViewById(R.id.Btn_timeManageReset_comp);
            Button btnTimeManageReadComp = (Button) v.findViewById(R.id.Btn_timeManageRead_comp);

            Calendar c = Calendar.getInstance();
            mETyComp.setText(Integer.toString(c.get(Calendar.YEAR)));
            mETMComp.setText(Integer.toString(c.get(Calendar.MONTH) + 1));
            mETdComp.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
            mETHComp.setText(Integer.toString(c.get(Calendar.HOUR_OF_DAY)));
            mETmComp.setText(Integer.toString(c.get(Calendar.MINUTE)));
            mETsComp.setText(Integer.toString(c.get(Calendar.SECOND)));
            mETyComp.setOnFocusChangeListener(new dateFocusChangeComp());
            mETyComp.setOnEditorActionListener(new dateFocusChangeComp());
            mETMComp.setOnFocusChangeListener(new dateFocusChangeComp());
            mETMComp.setOnEditorActionListener(new dateFocusChangeComp());
            mETdComp.setOnFocusChangeListener(new dateFocusChangeComp());
            mETdComp.setOnEditorActionListener(new dateFocusChangeComp());
            mETHComp.setOnFocusChangeListener(new timeFocusChangeComp());
            mETHComp.setOnEditorActionListener(new timeFocusChangeComp());
            mETmComp.setOnFocusChangeListener(new timeFocusChangeComp());
            mETmComp.setOnEditorActionListener(new timeFocusChangeComp());
            mETsComp.setOnFocusChangeListener(new timeFocusChangeComp());
            mETsComp.setOnEditorActionListener(new timeFocusChangeComp());

            btnTimeManageOKComp.setOnClickListener(new btnOKOnClickComp());
            btnTimeManageResetComp.setOnClickListener(new btnResetOnClickComp());
            btnTimeManageReadComp.setOnClickListener(new btnReadOnClickComp());

            year = Integer.parseInt(mETyComp.getText().toString());
            month = Integer.parseInt(mETMComp.getText().toString());
            day = Integer.parseInt(mETdComp.getText().toString());
            hour = Integer.parseInt(mETHComp.getText().toString());
            min = Integer.parseInt(mETmComp.getText().toString());
            sec = Integer.parseInt(mETsComp.getText().toString());

            ParProtection();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private void ParProtection() {
        if (getPublicConfigData("LogInName").equals("0")) {
            mETyComp.setEnabled(false);
            mETMComp.setEnabled(false);
            mETdComp.setEnabled(false);
            mETHComp.setEnabled(false);
            mETmComp.setEnabled(false);
            mETsComp.setEnabled(false);
        }
    }

    private class btnReadOnClickComp implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            byte[] arrayOfByte = DataUtil.shortToByte((short) 5114);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(mCompName + "_查组分系统时间" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 6);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mjCompSysTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 5114: {
                    showTime();
                }
                break;
            }
        }

        void showTime() {
            if (mETyComp != null) {
                mETyComp.setText((getCmds(mCompName).getCmd(5114).getValue() == null ? "" : getCmds(mCompName).getCmd(5114).getValue()).toString());
                getCmds(mCompName).getCmd(5114).setValue(null);
            }
            if (mETMComp != null) {
                mETMComp.setText((getCmds(mCompName).getCmd(5115).getValue() == null ? "" : getCmds(mCompName).getCmd(5115).getValue()).toString());
                getCmds(mCompName).getCmd(5115).setValue(null);
            }
            if (mETdComp != null) {
                mETdComp.setText((getCmds(mCompName).getCmd(5116).getValue() == null ? "" : getCmds(mCompName).getCmd(5116).getValue()).toString());
                getCmds(mCompName).getCmd(5116).setValue(null);
            }
            if (mETHComp != null) {
                mETHComp.setText((getCmds(mCompName).getCmd(5117).getValue() == null ? "" : getCmds(mCompName).getCmd(5117).getValue()).toString());
                getCmds(mCompName).getCmd(5117).setValue(null);
            }
            if (mETmComp != null) {
                mETmComp.setText((getCmds(mCompName).getCmd(5118).getValue() == null ? "" : getCmds(mCompName).getCmd(5118).getValue()).toString());
                getCmds(mCompName).getCmd(5118).setValue(null);
            }
            if (mETsComp != null) {
                mETsComp.setText((getCmds(mCompName).getCmd(5119).getValue() == null ? "" : getCmds(mCompName).getCmd(5119).getValue()).toString());
                getCmds(mCompName).getCmd(5119).setValue(null);
            }
        }
    };

    private class btnResetOnClickComp implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Calendar c = Calendar.getInstance();
            mETyComp.setText(Integer.toString(c.get(Calendar.YEAR)));
            mETMComp.setText(Integer.toString(c.get(Calendar.MONTH) + 1));
            mETdComp.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
            mETHComp.setText(Integer.toString(c.get(Calendar.HOUR_OF_DAY)));
            mETmComp.setText(Integer.toString(c.get(Calendar.MINUTE)));
            mETsComp.setText(Integer.toString(c.get(Calendar.SECOND)));
        }
    }

    private class btnOKOnClickComp implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String year = mETyComp.getText().toString().trim();
            String month = mETMComp.getText().toString().trim();
            String day = mETdComp.getText().toString().trim();
            String hour = mETHComp.getText().toString().trim();
            String minute = mETmComp.getText().toString().trim();
            String second = mETsComp.getText().toString().trim();

            if (!year.equals("") && !month.equals("") && !day.equals("") && !hour.equals("") && !minute.equals("") && !second.equals("")) {
                byte[] timeByte = copybyte(toByteArray(Integer.parseInt(year), 4), toByteArray(Integer.parseInt(month), 4),
                        toByteArray(Integer.parseInt(day), 4), toByteArray(Integer.parseInt(hour), 4),
                        toByteArray(Integer.parseInt(minute), 4), toByteArray(Integer.parseInt(second), 4));

                saveOperationLogMsg(mCompName, "设置时间" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second, ErrorLog.msgType.操作_信息);
                //时间矫正
                AddError(mCompName, 614, 运维_信息);
                SendManager.SendCmd(mCompName + "_时间管理_06_0", S0, 3, 200, timeByte);
                // testAddHistory(mCompName, Byte.valueOf(minute), Float.valueOf(second), (short) 2018, Byte.valueOf(month), Byte.valueOf(day), Byte.valueOf(hour), (byte) 12, 2, new int[]{1234,2344});
            } else {
                Toast.makeText(getActivity(), "时间不能为空", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class dateFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (mETyComp.getText().toString().equals("")) {
                        mETyComp.setText(String.valueOf(year));
                    }
                    if (Integer.parseInt(mETyComp.getText().toString()) > 3000 || Integer.parseInt(mETyComp.getText().toString()) < 2000) {
                        mETyComp.setText(String.valueOf(year));
                    }
                    if (mETMComp.getText().toString().equals("")) {
                        mETMComp.setText(String.valueOf(month));
                    }
                    if (Integer.parseInt(mETMComp.getText().toString()) > 12 || Integer.parseInt(mETMComp.getText().toString()) < 1) {
                        mETMComp.setText(String.valueOf(month));
                    }
                    if (mETdComp.getText().toString().equals("")) {
                        mETdComp.setText(String.valueOf(day));
                    }
                    if (Integer.parseInt(mETdComp.getText().toString()) > 31 || Integer.parseInt(mETdComp.getText().toString()) < 1) {
                        mETdComp.setText(String.valueOf(day));
                    }
                    String strValueY = editDataFormat(mETyComp.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    String strValueM = editDataFormat(mETMComp.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    String strValueD = editDataFormat(mETdComp.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    mETyComp.setText(strValueY);
                    mETMComp.setText(strValueM);
                    mETdComp.setText(strValueD);
                    try {
                        //setDate(Integer.parseInt(mETyComp.getText().toString()), Integer.parseInt(mETMComp.getText().toString()) - 1, Integer.parseInt(mETdComp.getText().toString()));
                        //时间矫正
                        //AddError(mCompName, 614, 运维_信息);

                        SetDateTime(mCompName, Integer.parseInt(mETyComp.getText().toString()), Integer.parseInt(mETMComp.getText().toString()) - 1, Integer.parseInt(mETdComp.getText().toString()), 0, 0, 0);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  /*  Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(mETyComp.getText().toString()), Integer.parseInt(mETMComp.getText().toString()) - 1, Integer.parseInt(mETdComp.getText().toString()),
                            Integer.parseInt(mETHComp.getText().toString()), Integer.parseInt(mETmComp.getText().toString()), Integer.parseInt(mETsComp.getText().toString()));
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.setTime(c.getTimeInMillis());*/
                    //setTimes("20180102.030405");
                    //testDate();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                closeInputMethod(v);
                FullWindows(mActivityWindow);
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

    public static void testDate() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.waitFor();
            String datetime = "20131023.112800"; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + datetime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("SetDate", e.toString());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.e("SetDate", e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 设置系统时间
     *
     * @param time 格式为“年月日.时分秒”，例如：20111209.121212
     */
    public static boolean setTimes(String time) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            process.waitFor();
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("date -s " + time + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.e("SetDate", e.toString());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                Log.e("SetDate", e.toString());
            }
        }
        return true;
    }


    private class timeFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (mETHComp.getText().toString().equals("")) {
                        mETHComp.setText(String.valueOf(hour));
                    }
                    if (Integer.parseInt(mETHComp.getText().toString()) > 24 || Integer.parseInt(mETHComp.getText().toString()) < 0) {
                        mETHComp.setText(String.valueOf(hour));
                    }

                    if (mETmComp.getText().toString().equals("")) {
                        mETmComp.setText(String.valueOf(min));
                    }
                    if (Integer.parseInt(mETmComp.getText().toString()) > 59 || Integer.parseInt(mETmComp.getText().toString()) < 0) {
                        mETmComp.setText(String.valueOf(min));
                    }

                    if (mETsComp.getText().toString().equals("")) {
                        mETsComp.setText(String.valueOf(sec));
                    }
                    if (Integer.parseInt(mETsComp.getText().toString()) > 59 || Integer.parseInt(mETsComp.getText().toString()) < 0) {
                        mETsComp.setText(String.valueOf(sec));
                    }
                    //setTime(Integer.parseInt(mETHComp.getText().toString()), Integer.parseInt(mETmComp.getText().toString()), Integer.parseInt(mETsComp.getText().toString()));
                    SetDateTime(mCompName, 0, 0, 0, Integer.parseInt(mETHComp.getText().toString()), Integer.parseInt(mETmComp.getText().toString()), Integer.parseInt(mETsComp.getText().toString()));
                   /* Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(mETyComp.getText().toString()), Integer.parseInt(mETMComp.getText().toString()) - 1, Integer.parseInt(mETdComp.getText().toString()),
                            Integer.parseInt(mETHComp.getText().toString()), Integer.parseInt(mETmComp.getText().toString()), Integer.parseInt(mETsComp.getText().toString()));
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.setTime(c.getTimeInMillis());
*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                closeInputMethod(v);
                FullWindows(mActivityWindow);
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
