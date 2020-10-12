package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.CmdTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_ok;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.setW200JLBoard;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.allAlarmInfo;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pjlbcscdy extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pjlbcscdy fragment = null;
    private static EditText JLBC_volume1;
    private static EditText JLBC_volume2;
    private static EditText JLBC_infusionSpeed; // 抽液速度
    private static EditText JLBC_backRowSpeed;  // 回排速度
    private static EditText JLBC_backRowTime;   // 回排时间
    private static EditText JLBC_infusionOutTime;   // 预抽试剂超时时间
    private static EditText JLBC_highNumberOfPoint;   // 高计量进样点数
    private static EditText JLBC_lowNumberOfPoint;   // 低计量进样点数

    private static Spinner mSpJlbcsc;
    private Button saveBtn;
    private static Button CalBtn;


    public static List4_Content3_pjlbcscdy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pjlbcscdy();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();

        void showUpContent();
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
        return R.layout.list4_content3_pjlbcscdy;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.LockDisplayShowFlag = true;
            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());
            InitWidget();
            setW200JLBoard(mCompName);
            //JLBCfg JlbCfg;
            List<String> listJlbName = new ArrayList<>();
            listJlbName.add(0, getString(R.string.notSet));
            for (int i = 0; i < 5; i++) {
                if (!getConfigData(mCompName, "JLB_" + (i + 1) + "_NAME").equals("")) {
                    listJlbName.add(listJlbName.size(), getConfigData(mCompName, "JLB_" + (i + 1) + "_NAME"));
                }
            }
            InitSpinner(context, mSpJlbcsc, listJlbName.toArray(new String[listJlbName.size()]), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpJlbcsc.setOnItemSelectedListener(new mSpfwhSelect());

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void InitWidget() {

        mSpJlbcsc = v.findViewById(R.id.spJlbcsc);
        JLBC_volume1 = v.findViewById(R.id.eT_jlbc_volume1);
        JLBC_volume1.setOnFocusChangeListener(new EditFocusChangeComp());
        JLBC_volume1.setOnEditorActionListener(new EditFocusChangeComp());

        JLBC_volume2 = v.findViewById(R.id.eT_jlbc_volume2);
        JLBC_volume2.setOnFocusChangeListener(new EditFocusChangeComp());
        JLBC_volume2.setOnEditorActionListener(new EditFocusChangeComp());

        JLBC_infusionSpeed = v.findViewById(R.id.eT_jlbc_infusionSpeed);
        JLBC_infusionSpeed.setOnFocusChangeListener(new EditFocusChangeComp());
        JLBC_infusionSpeed.setOnEditorActionListener(new EditFocusChangeComp());

        JLBC_backRowSpeed = v.findViewById(R.id.eT_jlbc_backRowSpeed);
        JLBC_backRowSpeed.setOnFocusChangeListener(new EditFocusChangeComp());
        JLBC_backRowSpeed.setOnEditorActionListener(new EditFocusChangeComp());

        JLBC_backRowTime = v.findViewById(R.id.eT_jlbc_backRowTime);
        JLBC_backRowTime.setOnFocusChangeListener(new EditFocusChangeComp());
        JLBC_backRowTime.setOnEditorActionListener(new EditFocusChangeComp());

        JLBC_infusionOutTime = v.findViewById(R.id.eT_jlbc_infusionOutTime);
        JLBC_infusionOutTime.setOnFocusChangeListener(new EditFocusChangeComp());
        JLBC_infusionOutTime.setOnEditorActionListener(new EditFocusChangeComp());

        JLBC_highNumberOfPoint = v.findViewById(R.id.eT_jlbc_highNumberOfPoint);
        JLBC_highNumberOfPoint.setOnFocusChangeListener(new EditFocusChangeComp());
        JLBC_highNumberOfPoint.setOnEditorActionListener(new EditFocusChangeComp());

        JLBC_lowNumberOfPoint = v.findViewById(R.id.eT_jlbc_lowNumberOfPoint);
        JLBC_lowNumberOfPoint.setOnFocusChangeListener(new EditFocusChangeComp());
        JLBC_lowNumberOfPoint.setOnEditorActionListener(new EditFocusChangeComp());


        saveBtn = (Button) v.findViewById(R.id.Btn_pjlbjlcs_save);
        saveBtn.setOnClickListener(new btnSaveClick());

        CalBtn = (Button) v.findViewById(R.id.Btn_pjlbjlcs_tjjz);
        CalBtn.setOnClickListener(new btnCalClick());

        setEditTouch();


    }

    private class EditFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
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


    private void setEditTouch() {
        // 非管理员和运维情况，则全部不可修改
        if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {

            JLBC_volume1.setEnabled(true);
            JLBC_volume2.setEnabled(true);
            JLBC_infusionSpeed.setEnabled(true);
            JLBC_backRowSpeed.setEnabled(true);
            JLBC_backRowTime.setEnabled(true);
            JLBC_infusionOutTime.setEnabled(true);

            saveBtn.setVisibility(View.VISIBLE);
        } else {
            JLBC_volume1.setEnabled(false);
            JLBC_volume2.setEnabled(false);
            JLBC_infusionSpeed.setEnabled(false);
            JLBC_backRowSpeed.setEnabled(false);
            JLBC_backRowTime.setEnabled(false);
            JLBC_infusionOutTime.setEnabled(false);

            saveBtn.setVisibility(View.GONE);

        }
    }

    private class mSpfwhSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            try {
                int selectIndex = Integer.valueOf(mSpJlbcsc.getSelectedItem().toString().split("_")[0]);
                if (selectIndex != 0) {
                    clearEditText();
                    byte[] arrayOfByte = DataUtil.shortToByte((short) (6100 + 100 * (selectIndex - 1)));

                    DataUtil.reverse(arrayOfByte);
                    RealTimeStatusThread.suspend();
                    SendManager.SendCmd(mCompName + "_查计量板" + selectIndex + "的计量参数C" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 3, 1000, 28);
                    RealTimeStatusThread.resume();
                }
            } catch (Exception e) {
                saveExceptInfo2File("组分[" + mCompName + "] mSpfwhSelect：" + e.toString());

            }
            FullWindows(mActivityWindow);
        }

        private void clearEditText() {
            JLBC_volume1.setText("");
            JLBC_volume2.setText("");
            JLBC_infusionSpeed.setText("");
            JLBC_backRowSpeed.setText("");
            JLBC_backRowTime.setText("");
            JLBC_infusionOutTime.setText("");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class btnSaveClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mSpJlbcsc.getSelectedItemId() == 0)
                return;
            try {

                int selectedItemIndex = Integer.valueOf(mSpJlbcsc.getSelectedItem().toString().split("_")[0]);
                String FES1Str = JLBC_volume1.getText().toString().trim();
                String FET1Str = JLBC_volume2.getText().toString().trim();
                String SE1Str = JLBC_infusionSpeed.getText().toString().trim();
                String CL1Str = JLBC_backRowSpeed.getText().toString().trim();
                String CH1Str = JLBC_backRowTime.getText().toString().trim();

                String RSStr = JLBC_infusionOutTime.getText().toString().trim();

                String GJLDStr = JLBC_highNumberOfPoint.getText().toString().trim();
                String DJLDStr = JLBC_lowNumberOfPoint.getText().toString().trim();


                if (!FES1Str.equals("") && !FET1Str.equals("") && !SE1Str.equals("") && !CL1Str.equals("") && !CH1Str.equals("") &&
                        !RSStr.equals("")) {

                    byte[] dataByte = copybyte(toByteArray(Integer.parseInt(FES1Str), 4), toByteArray(Integer.parseInt(FET1Str), 4),
                            toByteArray(Integer.parseInt(SE1Str), 4), toByteArray(Integer.parseInt(CL1Str), 4),
                            toByteArray(Integer.parseInt(CH1Str), 4), toByteArray(Integer.parseInt(RSStr), 4)
                    );

                    if (selectedItemIndex != 0) {
                        saveOperationLogMsg(mCompName, "设置计量板" + selectedItemIndex + "计量参数C", ErrorLog.msgType.操作_信息);
                        SendManager.SendCmd(MainActivity.mCompName + "_计量板" + selectedItemIndex + "计量参数C设置_06_" + (108 + (selectedItemIndex - 1)), S0, 3, 200, dataByte);
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.measurement_par_empty), Toast.LENGTH_SHORT).show();
                }

                Thread.sleep(1000);

                if (!GJLDStr.equals("") && !DJLDStr.equals("")) {
                    byte[] dataByte = copybyte(toByteArray(Integer.parseInt(GJLDStr), 4), toByteArray(Integer.parseInt(DJLDStr), 4)
                    );

                    if (selectedItemIndex != 0) {
                        saveOperationLogMsg(mCompName, "设置计量板" + selectedItemIndex + "计量参数高低计量点", ErrorLog.msgType.操作_信息);
                        SendManager.SendCmd(MainActivity.mCompName + "_计量板" + selectedItemIndex + "计量参数高低计量点设置_06_" + (261 + (selectedItemIndex - 1)), S0, 3, 200, dataByte);
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.measurement_par_empty), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
            FullWindows(mActivityWindow);
        }
    }

    private class btnCalClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            main.removeDestopText(mfb);
            Dialog_ok st = new Dialog_ok();
            Bundle bundle = new Bundle();
            bundle.putString("alert-ok", context.getString(R.string.SDJZ));
            st.setArguments(bundle);
            st.setTargetFragment(List4_Content3_pjlbcscdy.this, 1);
            st.show(fm, "alert-ok");
        }
    }

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

    @SuppressLint("HandlerLeak")
    public static Handler mJLCS_CHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            {
                try {
                    JLBC_volume1.setText((getCmds(mCompName).getCmd(msg.what).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what).getValue()).toString());
                    JLBC_volume2.setText((getCmds(mCompName).getCmd(msg.what + 1).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 1).getValue()).toString());
                    JLBC_infusionSpeed.setText((getCmds(mCompName).getCmd(msg.what + 2).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 2).getValue()).toString());
                    JLBC_backRowSpeed.setText((getCmds(mCompName).getCmd(msg.what + 3).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 3).getValue()).toString());
                    JLBC_backRowTime.setText((getCmds(mCompName).getCmd(msg.what + 4).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 4).getValue()).toString());
                    JLBC_infusionOutTime.setText((getCmds(mCompName).getCmd(msg.what + 5).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 5).getValue()).toString());
                    JLBC_highNumberOfPoint.setText((getCmds(mCompName).getCmd(msg.what + 26).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 26).getValue()).toString());
                    JLBC_lowNumberOfPoint.setText((getCmds(mCompName).getCmd(msg.what + 27).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 27).getValue()).toString());

                    if (mCompName.equals("CODcr") && mSpJlbcsc.getSelectedItem().toString().contains("2_") && !JLBC_volume1.getText().toString().trim().equals("")) {
                        CalBtn.setVisibility(View.VISIBLE);
                    } else {
                        CalBtn.setVisibility(View.INVISIBLE);
                    }
                    Toast.makeText(context, context.getString(R.string.is_dirty), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, context.getString(R.string.exception_par), Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Global.LockDisplayShowFlag = false;
            Global.LockModuleDisplayShowFlag = false;
            final String stringExtra = data.getStringExtra("Dialog_Command");
            mCallbacks.onDialogRS();
            workState.put(MainActivity.mCompName, getString(R.string.normal));
            allAlarmInfo.put(mCompName, context.getString(R.string.normal));
            AutoSampleEnable.put(mCompName, "true");
            updateConfigData(mCompName, "Volume_Cal", "true");

            final String[] str = stringExtra.split("_");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] str = stringExtra.split("_");
                    if (doFlowing.get(str[0]).equals(context.getString(R.string.waiting_for_instructions))) {
                        doControlJob(str[0], context.getString(R.string.SDJZ) + "_" + context.getString(R.string.SDZY));
                    }
                }
            }).start();
            Log.i("T3", str[0]);
            getCmds(str[0]).doSampleMode = CmdTable.DoSampleMode.手动;
            Toast.makeText(context, context.getResources().getString(R.string.readyStarted) + stringExtra + context.getResources().getString(R.string.pleaseWaiting), Toast.LENGTH_SHORT).show();
            mCallbacks.showUpContent();

            AddError(mCompName, 594, ErrorLog.msgType.运维_信息);
            saveOperationLogMsg(mCompName, "启动体积校准", ErrorLog.msgType.操作_信息);

        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }

}
