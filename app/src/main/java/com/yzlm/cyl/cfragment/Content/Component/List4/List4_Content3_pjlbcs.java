package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by zwj on 2018/03/22.
 */

public class List4_Content3_pjlbcs extends SubFragment {

    private Callbacks mCallbacks;
    private static List4_Content3_pjlbcs fragment = null;

    private static EditText JLB_FES1;
    private static EditText JLB_FET1;
    private static EditText JLB_SE1;
    private static EditText JLB_C_L1;
    private static EditText JLB_C_H1;
    private static EditText JLB_RS;
    private static EditText JLB_RT;
    private static EditText JLB_C_L2;
    private static EditText JLB_C_H2;
    private static EditText JLB_SES2;
    private static EditText JLB_C_L3;
    private static EditText JLB_C_H3;
    private static EditText JLB_FLS;
    private static EditText JLB_EVT;
    private static EditText JLB_IS;
    private static EditText JLB_IT_H;
    private static EditText JLB_IT_L;
    private static EditText JLB_EMS;
    private static EditText JLB_EMT_H;
    private static EditText JLB_EMT_L;
    private static EditText JLB_RT_H;
    private static EditText JLB_FLC_L;
    private static EditText JLB_FLC_H;
    private static EditText JLB_OT_L;
    private static EditText JLB_OT_H;
    private static EditText JLB_CT;

    private Button saveBtn;

    private static Spinner mSpJlbId;


    public static List4_Content3_pjlbcs newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pjlbcs();
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
        return R.layout.list4_content3_pjlbjlcs;
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

            mSpJlbId = v.findViewById(R.id.spJlbId);
            //JLBCfg JlbCfg;
            List<String> listJlbName = new ArrayList<>();
            listJlbName.add(0, getString(R.string.notSet));

            for (int i = 0; i < 20; i++) {
                if (!getConfigData(mCompName, "JLB_" + (i + 1) + "_NAME").equals("")) {
                    listJlbName.add(listJlbName.size(), getConfigData(mCompName, "JLB_" + (i + 1) + "_NAME"));
                }
            }

            InitSpinner(context, mSpJlbId, listJlbName.toArray(new String[listJlbName.size()]), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpJlbId.setOnItemSelectedListener(new mSpfwhSelect());

            JLB_FES1 = v.findViewById(R.id.eT_pjlbjlcs_FES1);
            JLB_FES1.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_FES1.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_FET1 = v.findViewById(R.id.eT_pjlbjlcs_FET1);
            JLB_FET1.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_FET1.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_SE1 = v.findViewById(R.id.eT_pjlbjlcs_SE1);
            JLB_SE1.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_SE1.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_C_L1 = v.findViewById(R.id.eT_pjlbjlcs_C_L);
            JLB_C_L1.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_C_L1.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_C_H1 = v.findViewById(R.id.eT_pjlbjlcs_C_H);
            JLB_C_H1.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_C_H1.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_RS = v.findViewById(R.id.eT_pjlbjlcs_RS);
            JLB_RS.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_RS.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_RT = v.findViewById(R.id.eT_pjlbjlcs_RT);
            JLB_RT.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_RT.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_C_L2 = v.findViewById(R.id.eT_pjlbjlcs_C_L_1);
            JLB_C_L2.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_C_L2.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_C_H2 = v.findViewById(R.id.eT_pjlbjlcs_C_H_1);
            JLB_C_H2.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_C_H2.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_SES2 = v.findViewById(R.id.eT_pjlbjlcs_SES2);
            JLB_SES2.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_SES2.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_C_L3 = v.findViewById(R.id.eT_pjlbjlcs_CL);
            JLB_C_L3.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_C_L3.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_C_H3 = v.findViewById(R.id.eT_pjlbjlcs_CH);
            JLB_C_H3.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_C_H3.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_FLS = v.findViewById(R.id.eT_pjlbjlcs_FLS);
            JLB_FLS.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_FLS.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_EVT = v.findViewById(R.id.eT_pjlbjlcs_EVT);
            JLB_EVT.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_EVT.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_IS = v.findViewById(R.id.eT_pjlbjlcs_IS);
            JLB_IS.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_IS.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_IT_H = v.findViewById(R.id.eT_pjlbjlcs_IT_H);
            JLB_IT_H.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_IT_H.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_IT_L = v.findViewById(R.id.eT_pjlbjlcs_IT_L);
            JLB_IT_L.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_IT_L.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_EMS = v.findViewById(R.id.eT_pjlbjlcs_EMS);
            JLB_EMS.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_EMS.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_EMT_H = v.findViewById(R.id.eT_pjlbjlcs_EMT_H);
            JLB_EMT_H.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_EMT_H.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_EMT_L = v.findViewById(R.id.eT_pjlbjlcs_EMT_L);
            JLB_EMT_L.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_EMT_L.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_RT_H = v.findViewById(R.id.eT_pjlbjlcs_RT_H);
            JLB_RT_H.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_RT_H.setOnEditorActionListener(new EditFocusChangeComp());


            JLB_FLC_L = v.findViewById(R.id.eT_pjlbjlcs_FLCL);
            JLB_FLC_L.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_FLC_L.setOnEditorActionListener(new EditFocusChangeComp());


            JLB_FLC_H = v.findViewById(R.id.eT_pjlbjlcs_FLCH);
            JLB_FLC_H.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_FLC_H.setOnEditorActionListener(new EditFocusChangeComp());


            JLB_OT_L = v.findViewById(R.id.eT_pjlbjlcs_OT_L);
            JLB_OT_L.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_OT_L.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_OT_H = v.findViewById(R.id.eT_pjlbjlcs_OT_H);
            JLB_OT_H.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_OT_H.setOnEditorActionListener(new EditFocusChangeComp());

            JLB_CT = v.findViewById(R.id.eT_pjlbjlcs_CT);
            JLB_CT.setOnFocusChangeListener(new EditFocusChangeComp());
            JLB_CT.setOnEditorActionListener(new EditFocusChangeComp());

            saveBtn = v.findViewById(R.id.Btn_pjlbjlcs_save);
            saveBtn.setOnClickListener(new btnSaveClick());
            setEditTouch();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void setEditTouch() {
        // 非管理员情况，则全部不可修改
        if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {

            JLB_FES1.setEnabled(true);
            JLB_FET1.setEnabled(true);
            JLB_SE1.setEnabled(true);
            JLB_C_L1.setEnabled(true);
            JLB_C_H1.setEnabled(true);
            JLB_RS.setEnabled(true);
            JLB_RT.setEnabled(true);
            JLB_C_L2.setEnabled(true);
            JLB_C_H2.setEnabled(true);
            JLB_SES2.setEnabled(true);
            JLB_C_L3.setEnabled(true);
            JLB_C_H3.setEnabled(true);
            JLB_FLS.setEnabled(true);
            JLB_EVT.setEnabled(true);
            JLB_IS.setEnabled(true);
            JLB_IT_H.setEnabled(true);
            JLB_IT_L.setEnabled(true);
            JLB_EMS.setEnabled(true);
            JLB_EMT_H.setEnabled(true);
            JLB_EMT_L.setEnabled(true);
            JLB_RT_H.setEnabled(true);
            JLB_FLC_L.setEnabled(true);
            JLB_FLC_H.setEnabled(true);
            JLB_OT_L.setEnabled(true);
            JLB_OT_H.setEnabled(true);
            JLB_CT.setEnabled(true);
            saveBtn.setVisibility(View.VISIBLE);
        } else {
            JLB_FES1.setEnabled(false);
            JLB_FET1.setEnabled(false);
            JLB_SE1.setEnabled(false);
            JLB_C_L1.setEnabled(false);
            JLB_C_H1.setEnabled(false);
            JLB_RS.setEnabled(false);
            JLB_RT.setEnabled(false);
            JLB_C_L2.setEnabled(false);
            JLB_C_H2.setEnabled(false);
            JLB_SES2.setEnabled(false);
            JLB_C_L3.setEnabled(false);
            JLB_C_H3.setEnabled(false);
            JLB_FLS.setEnabled(false);
            JLB_EVT.setEnabled(false);
            JLB_IS.setEnabled(false);
            JLB_IT_H.setEnabled(false);
            JLB_IT_L.setEnabled(false);
            JLB_EMS.setEnabled(false);
            JLB_EMT_H.setEnabled(false);
            JLB_EMT_L.setEnabled(false);
            JLB_RT_H.setEnabled(false);
            JLB_FLC_L.setEnabled(false);
            JLB_FLC_H.setEnabled(false);
            JLB_OT_L.setEnabled(false);
            JLB_OT_H.setEnabled(false);
            JLB_CT.setEnabled(false);
            saveBtn.setVisibility(View.GONE);

        }
    }

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
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

    private class mSpfwhSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            try {
                int selectIndex = Integer.valueOf(mSpJlbId.getSelectedItem().toString().split("_")[0]);
                if (selectIndex != 0) {
                    clearEditText();
                    byte[] arrayOfByte = DataUtil.shortToByte((short) (6100 + 100 * (selectIndex - 1)));

                    DataUtil.reverse(arrayOfByte);
                    RealTimeStatusThread.suspend();
                    SendManager.SendCmd(mCompName + "_查计量板" + selectIndex + "的计量参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 3, 1000, 26);
                    RealTimeStatusThread.resume();
                }
            } catch (Exception e) {
                saveExceptInfo2File("组分[" + mCompName + "] mSpfwhSelect：" + e.toString());

            }
            FullWindows(mActivityWindow);
        }

        private void clearEditText() {
            JLB_FES1.setText("");
            JLB_FET1.setText("");
            JLB_SE1.setText("");
            JLB_C_L1.setText("");
            JLB_C_H1.setText("");
            JLB_RS.setText("");
            JLB_RT.setText("");
            JLB_C_L2.setText("");
            JLB_C_H2.setText("");
            JLB_SES2.setText("");
            JLB_C_L3.setText("");
            JLB_C_H3.setText("");
            JLB_FLS.setText("");
            JLB_EVT.setText("");
            JLB_IS.setText("");
            JLB_IT_H.setText("");
            JLB_IT_L.setText("");
            JLB_EMS.setText("");
            JLB_EMT_H.setText("");
            JLB_EMT_L.setText("");
            JLB_RT_H.setText("");
            JLB_FLC_L.setText("");
            JLB_FLC_H.setText("");
            JLB_OT_L.setText("");
            JLB_OT_H.setText("");
            JLB_CT.setText("");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private class btnSaveClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mSpJlbId.getSelectedItemId() == 0)
                return;
            try {

                int selectedItemIndex = Integer.valueOf(mSpJlbId.getSelectedItem().toString().split("_")[0]);
                String FES1Str = JLB_FES1.getText().toString().trim();
                String FET1Str = JLB_FET1.getText().toString().trim();
                String SE1Str = JLB_SE1.getText().toString().trim();
                String CL1Str = JLB_C_L1.getText().toString().trim();
                String CH1Str = JLB_C_H1.getText().toString().trim();

                String RSStr = JLB_RS.getText().toString().trim();
                String RTStr = JLB_RT.getText().toString().trim();
                String CL2Str = JLB_C_L2.getText().toString().trim();
                String CH2Str = JLB_C_H2.getText().toString().trim();
                String SES2Str = JLB_SES2.getText().toString().trim();

                String CL3Str = JLB_C_L3.getText().toString().trim();
                String CH3Str = JLB_C_H3.getText().toString().trim();
                String FLSStr = JLB_FLS.getText().toString().trim();
                String EVTStr = JLB_EVT.getText().toString().trim();
                String ISStr = JLB_IS.getText().toString().trim();

                String ITHStr = JLB_IT_H.getText().toString().trim();
                String ITLStr = JLB_IT_L.getText().toString().trim();
                String EMSStr = JLB_EMS.getText().toString().trim();
                String EMTHStr = JLB_EMT_H.getText().toString().trim();
                String EMTLStr = JLB_EMT_L.getText().toString().trim();

                String RTHStr = JLB_RT_H.getText().toString().trim();
                String FLCLStr = JLB_FLC_L.getText().toString().trim();
                String FLCHStr = JLB_FLC_H.getText().toString().trim();

                String OTLStr = JLB_OT_L.getText().toString().trim();
                String OTHStr = JLB_OT_H.getText().toString().trim();
                String CTStr = JLB_CT.getText().toString().trim();

                if (!FES1Str.equals("") && !FET1Str.equals("") && !SE1Str.equals("") && !CL1Str.equals("") && !CH1Str.equals("") &&
                        !RSStr.equals("") && !RTStr.equals("") && !CL2Str.equals("") && !CH2Str.equals("") && !SES2Str.equals("")
                        && !CL3Str.equals("") && !CH3Str.equals("") && !FLSStr.equals("") && !EVTStr.equals("") && !ISStr.equals("")
                        && !ITHStr.equals("") && !ITLStr.equals("") && !EMSStr.equals("") && !EMTHStr.equals("") && !EMTLStr.equals("")
                        && !RTHStr.equals("") && !FLCLStr.equals("") && !FLCHStr.equals("") && !OTLStr.equals("") && !OTHStr.equals("")
                        && !CTStr.equals("")) {

                    byte[] dataByte = copybyte(toByteArray(Integer.parseInt(FES1Str), 4), toByteArray(Integer.parseInt(SE1Str), 4),
                            toByteArray(Integer.parseInt(RSStr), 4), toByteArray(Integer.parseInt(SES2Str), 4),
                            toByteArray(Integer.parseInt(FLSStr), 4), toByteArray(Integer.parseInt(ISStr), 4),
                            toByteArray(Integer.parseInt(EMSStr), 4), toByteArray(Integer.parseInt(CL1Str), 4),
                            toByteArray(Integer.parseInt(CH1Str), 4), toByteArray(Integer.parseInt(FLCLStr), 4),
                            toByteArray(Integer.parseInt(FLCHStr), 4), toByteArray(Integer.parseInt(CL2Str), 4),
                            toByteArray(Integer.parseInt(CH2Str), 4), toByteArray(Integer.parseInt(CL3Str), 4),
                            toByteArray(Integer.parseInt(CH3Str), 4), toByteArray(Integer.parseInt(FET1Str), 4),
                            toByteArray(Integer.parseInt(RTStr), 4), toByteArray(Integer.parseInt(RTHStr), 4),
                            toByteArray(Integer.parseInt(EVTStr), 4), toByteArray(Integer.parseInt(EMTLStr), 4),
                            toByteArray(Integer.parseInt(EMTHStr), 4), toByteArray(Integer.parseInt(ITLStr), 4),
                            toByteArray(Integer.parseInt(ITHStr), 4), toByteArray(Integer.parseInt(OTLStr), 4),
                            toByteArray(Integer.parseInt(OTHStr), 4), toByteArray(Integer.parseInt(CTStr), 4));

                    if (selectedItemIndex != 0) {
                        saveOperationLogMsg(mCompName, "设置计量板" + selectedItemIndex + "计量参数", ErrorLog.msgType.操作_信息);
                        SendManager.SendCmd(MainActivity.mCompName + "_计量板" + selectedItemIndex + "计量参数设置_06_" + (108 + (selectedItemIndex - 1)), S0, 3, 200, dataByte);
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.measurement_par_empty), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
            FullWindows(mActivityWindow);
        }
    }

    public static Handler mJLBjlcsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                JLB_FES1.setText((getCmds(mCompName).getCmd(msg.what).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what).getValue()).toString());
                JLB_SE1.setText((getCmds(mCompName).getCmd(msg.what + 1).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 1).getValue()).toString());
                JLB_RS.setText((getCmds(mCompName).getCmd(msg.what + 2).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 2).getValue()).toString());
                JLB_SES2.setText((getCmds(mCompName).getCmd(msg.what + 3).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 3).getValue()).toString());
                JLB_FLS.setText((getCmds(mCompName).getCmd(msg.what + 4).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 4).getValue()).toString());

                JLB_IS.setText((getCmds(mCompName).getCmd(msg.what + 5).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 5).getValue()).toString());
                JLB_EMS.setText((getCmds(mCompName).getCmd(msg.what + 6).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 6).getValue()).toString());
                JLB_C_L1.setText((getCmds(mCompName).getCmd(msg.what + 7).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 7).getValue()).toString());
                JLB_C_H1.setText((getCmds(mCompName).getCmd(msg.what + 8).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 8).getValue()).toString());
                JLB_FLC_L.setText((getCmds(mCompName).getCmd(msg.what + 9).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 9).getValue()).toString());

                JLB_FLC_H.setText((getCmds(mCompName).getCmd(msg.what + 10).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 10).getValue()).toString());
                JLB_C_L2.setText((getCmds(mCompName).getCmd(msg.what + 11).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 11).getValue()).toString());

                JLB_C_H2.setText((getCmds(mCompName).getCmd(msg.what + 12).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 12).getValue()).toString());
                JLB_C_L3.setText((getCmds(mCompName).getCmd(msg.what + 13).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 13).getValue()).toString());
                JLB_C_H3.setText((getCmds(mCompName).getCmd(msg.what + 14).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 14).getValue()).toString());
                JLB_FET1.setText((getCmds(mCompName).getCmd(msg.what + 15).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 15).getValue()).toString());
                JLB_RT.setText((getCmds(mCompName).getCmd(msg.what + 16).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 16).getValue()).toString());


                JLB_RT_H.setText((getCmds(mCompName).getCmd(msg.what + 17).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 17).getValue()).toString());
                JLB_EVT.setText((getCmds(mCompName).getCmd(msg.what + 18).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 18).getValue()).toString());
                JLB_EMT_L.setText((getCmds(mCompName).getCmd(msg.what + 19).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 19).getValue()).toString());
                JLB_EMT_H.setText((getCmds(mCompName).getCmd(msg.what + 20).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 20).getValue()).toString());
                JLB_IT_L.setText((getCmds(mCompName).getCmd(msg.what + 21).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 21).getValue()).toString());

                JLB_IT_H.setText((getCmds(mCompName).getCmd(msg.what + 22).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 22).getValue()).toString());

                JLB_OT_L.setText((getCmds(mCompName).getCmd(msg.what + 23).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 23).getValue()).toString());
                JLB_OT_H.setText((getCmds(mCompName).getCmd(msg.what + 24).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 24).getValue()).toString());
                JLB_CT.setText((getCmds(mCompName).getCmd(msg.what + 25).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 25).getValue()).toString());
                Toast.makeText(context, context.getString(R.string.is_dirty), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, context.getString(R.string.exception_par), Toast.LENGTH_SHORT).show();
            }
        }
    };


}
