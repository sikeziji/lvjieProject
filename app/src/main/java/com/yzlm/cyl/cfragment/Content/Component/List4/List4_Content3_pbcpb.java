package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Component.AppError.AppError;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getError;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.syncListErrorShield;
import static com.yzlm.cyl.cfragment.Global.versionID;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pbcpb extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pbcpb fragment = null;

    private ToggleButton tBtnRinse;
    private static ToggleButton tBtnProtection;

    /*报错号常量区*///-----------------------------
    public static final int ERR_F_WASH = 512;   //氟管冲洗或报错
    //-----------------------------

    public static List4_Content3_pbcpb newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pbcpb();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);
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
        return R.layout.list4_content3_pbcpb;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {

            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p2);
            mBtnReturn.setOnClickListener(new btnClick());

            tBtnRinse = (ToggleButton) v.findViewById(R.id.tBtnRinse);
            tBtnRinse.setOnCheckedChangeListener(new togBtnClickListener());
            tBtnRinse.setChecked(!geterrorShieldTable(mCompName, ERR_F_WASH));

            if (versionID >= 2 && mCompName.equals("CODcr")) {
                LinearLayout linearProtection = (LinearLayout) v.findViewById(R.id.linearProtection);
                linearProtection.setVisibility(View.VISIBLE);
                tBtnProtection = (ToggleButton) v.findViewById(R.id.tBtnProtection);
                tBtnProtection.setOnCheckedChangeListener(new togBtnClickListener());
                // 读取报错屏蔽
                byte[] arrayOfByte2 = DataUtil.shortToByte((short) 9200);
                DataUtil.reverse(arrayOfByte2);
                SendManager.SendCmd(mCompName + "_读取报错屏蔽" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 20);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    public static Handler pbcpbHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                List<String> lstErr = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    lstErr.add((getCmds(mCompName).getCmd(msg.what + i).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + i).getValue()).toString());
                }
                if (lstErr.contains("93")) {
                    tBtnProtection.setChecked(false);
                } else {
                    tBtnProtection.setChecked(true);
                }
                Toast.makeText(context, context.getString(R.string.is_dirty), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, context.getString(R.string.exception_par), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tBtnRinse: {
                    saveOperationLogDataModifyMsg(mCompName, "errorShieldTable", (tBtnRinse.isChecked() ? "true" : "false"), "氟管冲洗或更换", ErrorLog.msgType.操作_信息);
                    if (tBtnRinse.isChecked()) {
                        clearErrorShieldTableNum(mCompName, ERR_F_WASH);
                    } else {
                        setErrorShieldTableNum(mCompName, ERR_F_WASH);
                    }
                }
                break;
                case R.id.tBtnProtection: {
                    byte[] dataByte;
                    if (tBtnProtection.isChecked()) {
                        dataByte = toByteArray(0, 4);
                    } else {
                        dataByte = toByteArray(93, 4);
                    }
                    saveOperationLogMsg(mCompName, "设置仪器保护屏蔽", ErrorLog.msgType.操作_信息);
                    SendManager.SendCmd(MainActivity.mCompName + "_仪器保护屏蔽设置_06_224", S0, 3, 200, dataByte);
                }
                break;
            }
            syncListErrorShield();//全局方法
            syncList();
        }
    }

    /**
     * 获得errorShieldTable表示是否有这个报错号
     *
     * @param compName
     * @param num
     * @return true:有相同的, false:没有相同
     */
    private boolean geterrorShieldTable(String compName, int num) {
        String errorTable = getConfigData(compName, "errorShieldTable");
        String[] errorNum = errorTable.split("[,，]");
        for (int i = 0; i < errorNum.length; ++i) {
            String errorCode = errorNum[i].replace(" ", "");
            if (Integer.parseInt(errorCode) == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置errorShieldTable中的报错号
     *
     * @param compName
     * @param num
     */
    void setErrorShieldTableNum(String compName, int num) {
        try {
            if (!geterrorShieldTable(compName, num)) {
                StringBuilder errorTable = new StringBuilder(getConfigData(compName, "errorShieldTable"));
                errorTable.append("," + num);
                updateConfigData(mCompName, "errorShieldTable", errorTable + "");
                AppError appError = getError(mCompName).getErrorObj(String.valueOf(num));
                appError.setErrorHMIShield(true);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 清除errorShieldTable中的报错号
     *
     * @param compName
     * @param num
     */
    void clearErrorShieldTableNum(String compName, int num) {
        try {
            if (geterrorShieldTable(compName, num)) {
                String errorTable = getConfigData(compName, "errorShieldTable");
                errorTable = errorTable.replace("," + num, "");
                updateConfigData(mCompName, "errorShieldTable", errorTable);
                AppError appError = getError(mCompName).getErrorObj(String.valueOf(num));
                appError.setErrorHMIShield(false);
            }
        } catch (Exception e) {

        }
    }

}
