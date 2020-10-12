package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getNum;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.passwordRandom;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by zwj on 2017/11/03.
 */

public class List4_Content3_DevProtect extends SubFragment {

    private static List4_Content3_DevProtect fragment = null;
    private Callbacks mCallbacks;
    private static TextView mProtectInfo;
    private static TextView mPasswordCode;
    private static TextView mCKBSetFactoryTime;
    private static TextView mLockMsg;
    public static TextView mCKBNumber = null;


    public static List4_Content3_DevProtect newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_DevProtect();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list4_content3_devprotect;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            ImageButton mBtnReturn = v.findViewById(R.id.btnProtectReturn);
            mBtnReturn.setOnClickListener(new btnClick());

            byte[] arrayOfByte = DataUtil.shortToByte((short) 5700);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(mCompName + "_查测控板编号" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 3, 200, 1);
            /*安卓屏 */
            TextView mdevNumber = v.findViewById(R.id.devNumber);
            mdevNumber.setText(getConfigData(mCompName, "YQBH"));
            /*安卓屏  设置的出厂时间*/
            TextView mSetFactoryTime = v.findViewById(R.id.setFactoryTime);
            mSetFactoryTime.setText(getConfigData(mCompName, "FACTORY_TIME"));
            /*测控板编号*/
            mCKBNumber = v.findViewById(R.id.CKBNumber);
            mCKBNumber.setText(getConfigData(mCompName, "HMIBH"));
            /*测控板读取  设置的出厂时间*/
            mCKBSetFactoryTime = v.findViewById(R.id.ckbSetFactoryTime);
            /*延保时间*/
            mProtectInfo = v.findViewById(R.id.protectInfo);
            /*随机码*/
            mPasswordCode = v.findViewById(R.id.passwordCode);
            /*安卓屏界面随机码*/
            TextView mWinPassword = v.findViewById(R.id.winPassword);
            passwordRandom = getWinPassword(15);
            mWinPassword.setText(passwordRandom);

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 200);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(mCompName + "_查延保信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 10);
            mCKBNumber.setText((getCmds(mCompName).getCmd(5700).getValue() == null ? "" : getCmds(mCompName).getCmd(5700).getValue()).toString());

            mLockMsg = v.findViewById(R.id.txtLockMsg);
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }


    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            mCallbacks.onListSelected(v);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public static String getWinPassword(int size) {
        String str = "";
        for (int i = 0; i < size; i++) {
            str += String.valueOf(getNum(0, 9));
        }
        return str;
    }

    public static Handler devProtectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200: {
                    if (mPasswordCode != null) {
                        mPasswordCode.setText((getCmds(mCompName).getCmd(200).getValue() == null ? "" : getCmds(mCompName).getCmd(200).getValue()).toString());
                    }
                    if (mCKBSetFactoryTime != null) {
                        String year = (getCmds(mCompName).getCmd(202).getValue() == null ? "" : getCmds(mCompName).getCmd(202).getValue()).toString();
                        String month = (getCmds(mCompName).getCmd(203).getValue() == null ? "" : getCmds(mCompName).getCmd(203).getValue()).toString();
                        String day = (getCmds(mCompName).getCmd(204).getValue() == null ? "" : getCmds(mCompName).getCmd(204).getValue()).toString();
                        if (!year.equals("") && !month.equals("") && !day.equals("") && year.length() >= 4) {
                            mCKBSetFactoryTime.setText(year + "-" + String.format("%02d", Integer.valueOf(month)) + "-" + String.format("%02d", Integer.valueOf(day)));
                        } else {
                            mCKBSetFactoryTime.setText(context.getResources().getString(R.string.notSet));
                        }
                    }
                    if (mProtectInfo != null) {
                        String year = (getCmds(mCompName).getCmd(205).getValue() == null ? "" : getCmds(mCompName).getCmd(205).getValue()).toString();
                        String month = (getCmds(mCompName).getCmd(206).getValue() == null ? "" : getCmds(mCompName).getCmd(206).getValue()).toString();
                        String day = (getCmds(mCompName).getCmd(207).getValue() == null ? "" : getCmds(mCompName).getCmd(207).getValue()).toString();
                        String setMonth = (getCmds(mCompName).getCmd(208).getValue() == null ? "" : getCmds(mCompName).getCmd(208).getValue()).toString();
                        String lockInfo = (getCmds(mCompName).getCmd(201).getValue() == null ? "" : getCmds(mCompName).getCmd(201).getValue()).toString();
                        if (!lockInfo.equals("0") && lockInfo.equals("")) {
                            mProtectInfo.setText(context.getResources().getString(R.string.notSet));
                            mLockMsg.setText("");
                        } else {
                            if (!year.equals("") && year.length() >= 4) {
                                if (!lockInfo.equals("") && (Integer.parseInt(lockInfo) & 0xff) == 0xbb) {
                                    mLockMsg.setText(context.getString(R.string.device_no_job));
                                } else {
                                    mLockMsg.setText("");
                                }
                                mProtectInfo.setText(getEnMonthStr(Integer.valueOf(month)) + String.format("%02d", Integer.valueOf(day)) + year.substring(0, 2)
                                        + "A" + year.substring(2, 4) + "-" + String.format("%02d", Integer.valueOf(setMonth)));
                            } else {
                                mProtectInfo.setText(context.getResources().getString(R.string.notSet));
                                mLockMsg.setText("");
                            }
                        }
                        mCKBNumber.setText((getCmds(mCompName).getCmd(5700).getValue() == null ? "" : getCmds(mCompName).getCmd(5700).getValue()).toString());
                    }
                }
                break;
            }
        }
    };

    private static String getEnMonthStr(int time) {
        switch (time) {
            case 1: {
                return "JAN";
            }
            case 2: {
                return "FEB";
            }
            case 3: {
                return "MAR";
            }
            case 4: {
                return "APR";
            }
            case 5: {
                return "MAY";
            }
            case 6: {
                return "JUN";
            }
            case 7: {
                return "JUL";
            }
            case 8: {
                return "AUG";
            }
            case 9: {
                return "SEP";
            }
            case 10: {
                return "OCT";
            }
            case 11: {
                return "NOV";
            }
            case 12: {
                return "DEC";
            }
            default:
                return "";
        }
    }

}
