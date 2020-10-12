package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getValve;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pjlcs extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pjlcs fragment = null;

    private List<String> valveList = new ArrayList<>();

    private static EditText FES1;
    private static EditText FET1;
    private static EditText SE1;
    private static EditText C_L1;
    private static EditText C_H1;
    private static EditText RS;
    private static EditText RT;
    private static EditText C_L2;
    private static EditText C_H2;
    private static EditText SES2;
    private static EditText C_L3;
    private static EditText C_H3;
    private static EditText FLS;
    private static EditText EVT;
    private static EditText IS;
    private static EditText IT_H;
    private static EditText IT_L;
    private static EditText EMS;
    private static EditText EMT_H;
    private static EditText EMT_L;
    private static EditText RT_H;
    private static EditText FLC_L;
    private static EditText FLC_H;

    private static Button saveBtn;

    private static Spinner mSpfwh;

    public static List4_Content3_pjlcs newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pjlcs();
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
        return R.layout.list4_content3_pjlcs;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.LockDisplayShowFlag = true;
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());
            if (QueryMeasCateg(mCompName).equals("5") || QueryMeasCateg(mCompName).equals("6")) {
                mBtnReturn.setVisibility(View.INVISIBLE);
            } else {
                mBtnReturn.setVisibility(View.VISIBLE);
            }
            mSpfwh = (Spinner) v.findViewById(R.id.spfwh);
            try {
                List<String> valve = new ArrayList<>(getValve(mCompName));
                if (valveList.size() > 0) {
                    valveList.clear();
                }
                valveList.addAll(valve);
                List<String> del = new ArrayList<>();
                del.add(getString(R.string.reserve)/*"保留"*/);
                valve.removeAll(del);
                InitSpinner(context, mSpfwh, valve.toArray(new String[valve.size()]), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            } catch (Exception e) {
                Log.i("exception", "List4_Content3_pjlcs");
            }
            mSpfwh.setOnItemSelectedListener(new mSpfwhSelect());

            FES1 = (EditText) v.findViewById(R.id.eT_pjlcs_FES1);
            FES1.setOnFocusChangeListener(new EditFocusChangeComp());
            FES1.setOnEditorActionListener(new EditFocusChangeComp());

            FET1 = (EditText) v.findViewById(R.id.eT_pjlcs_FET1);
            FET1.setOnFocusChangeListener(new EditFocusChangeComp());
            FET1.setOnEditorActionListener(new EditFocusChangeComp());

            SE1 = (EditText) v.findViewById(R.id.eT_pjlcs_SE1);
            SE1.setOnFocusChangeListener(new EditFocusChangeComp());
            SE1.setOnEditorActionListener(new EditFocusChangeComp());

            C_L1 = (EditText) v.findViewById(R.id.eT_pjlcs_C_L);
            C_L1.setOnFocusChangeListener(new EditFocusChangeComp());
            C_L1.setOnEditorActionListener(new EditFocusChangeComp());

            C_H1 = (EditText) v.findViewById(R.id.eT_pjlcs_C_H);
            C_H1.setOnFocusChangeListener(new EditFocusChangeComp());
            C_H1.setOnEditorActionListener(new EditFocusChangeComp());

            RS = (EditText) v.findViewById(R.id.eT_pjlcs_RS);
            RS.setOnFocusChangeListener(new EditFocusChangeComp());
            RS.setOnEditorActionListener(new EditFocusChangeComp());

            RT = (EditText) v.findViewById(R.id.eT_pjlcs_RT);
            RT.setOnFocusChangeListener(new EditFocusChangeComp());
            RT.setOnEditorActionListener(new EditFocusChangeComp());

            C_L2 = (EditText) v.findViewById(R.id.eT_pjlcs_C_L_1);
            C_L2.setOnFocusChangeListener(new EditFocusChangeComp());
            C_L2.setOnEditorActionListener(new EditFocusChangeComp());

            C_H2 = (EditText) v.findViewById(R.id.eT_pjlcs_C_H_1);
            C_H2.setOnFocusChangeListener(new EditFocusChangeComp());
            C_H2.setOnEditorActionListener(new EditFocusChangeComp());

            SES2 = (EditText) v.findViewById(R.id.eT_pjlcs_SES2);
            SES2.setOnFocusChangeListener(new EditFocusChangeComp());
            SES2.setOnEditorActionListener(new EditFocusChangeComp());

            C_L3 = (EditText) v.findViewById(R.id.eT_pjlcs_CL);
            C_L3.setOnFocusChangeListener(new EditFocusChangeComp());
            C_L3.setOnEditorActionListener(new EditFocusChangeComp());

            C_H3 = (EditText) v.findViewById(R.id.eT_pjlcs_CH);
            C_H3.setOnFocusChangeListener(new EditFocusChangeComp());
            C_H3.setOnEditorActionListener(new EditFocusChangeComp());

            FLS = (EditText) v.findViewById(R.id.eT_pjlcs_FLS);
            FLS.setOnFocusChangeListener(new EditFocusChangeComp());
            FLS.setOnEditorActionListener(new EditFocusChangeComp());

            EVT = (EditText) v.findViewById(R.id.eT_pjlcs_EVT);
            EVT.setOnFocusChangeListener(new EditFocusChangeComp());
            EVT.setOnEditorActionListener(new EditFocusChangeComp());

            IS = (EditText) v.findViewById(R.id.eT_pjlcs_IS);
            IS.setOnFocusChangeListener(new EditFocusChangeComp());
            IS.setOnEditorActionListener(new EditFocusChangeComp());

            IT_H = (EditText) v.findViewById(R.id.eT_pjlcs_IT_H);
            IT_H.setOnFocusChangeListener(new EditFocusChangeComp());
            IT_H.setOnEditorActionListener(new EditFocusChangeComp());

            IT_L = (EditText) v.findViewById(R.id.eT_pjlcs_IT_L);
            IT_L.setOnFocusChangeListener(new EditFocusChangeComp());
            IT_L.setOnEditorActionListener(new EditFocusChangeComp());

            EMS = (EditText) v.findViewById(R.id.eT_pjlcs_EMS);
            EMS.setOnFocusChangeListener(new EditFocusChangeComp());
            EMS.setOnEditorActionListener(new EditFocusChangeComp());

            EMT_H = (EditText) v.findViewById(R.id.eT_pjlcs_EMT_H);
            EMT_H.setOnFocusChangeListener(new EditFocusChangeComp());
            EMT_H.setOnEditorActionListener(new EditFocusChangeComp());

            EMT_L = (EditText) v.findViewById(R.id.eT_pjlcs_EMT_L);
            EMT_L.setOnFocusChangeListener(new EditFocusChangeComp());
            EMT_L.setOnEditorActionListener(new EditFocusChangeComp());

            RT_H = (EditText) v.findViewById(R.id.eT_pjlcs_RT_H);
            RT_H.setOnFocusChangeListener(new EditFocusChangeComp());
            RT_H.setOnEditorActionListener(new EditFocusChangeComp());


            FLC_L = (EditText) v.findViewById(R.id.eT_pjlcs_FLCL);
            FLC_L.setOnFocusChangeListener(new EditFocusChangeComp());
            FLC_L.setOnEditorActionListener(new EditFocusChangeComp());


            FLC_H = (EditText) v.findViewById(R.id.eT_pjlcs_FLCH);
            FLC_H.setOnFocusChangeListener(new EditFocusChangeComp());
            FLC_H.setOnEditorActionListener(new EditFocusChangeComp());

            saveBtn = (Button) v.findViewById(R.id.Btn_pjlcs_save);
            saveBtn.setOnClickListener(new btnSaveClick());
            setEditTouch();
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private void setEditTouch() {
        // 非管理员情况，则全部不可修改
        if (getPublicConfigData("LogInName").equals("2")||getPublicConfigData("LogInName").equals("3")) {

            FES1.setEnabled(true);
            FET1.setEnabled(true);
            SE1.setEnabled(true);
            C_L1.setEnabled(true);
            C_H1.setEnabled(true);
            RS.setEnabled(true);
            RT.setEnabled(true);
            C_L2.setEnabled(true);
            C_H2.setEnabled(true);
            SES2.setEnabled(true);
            C_L3.setEnabled(true);
            C_H3.setEnabled(true);
            FLS.setEnabled(true);
            EVT.setEnabled(true);
            IS.setEnabled(true);
            IT_H.setEnabled(true);
            IT_L.setEnabled(true);
            EMS.setEnabled(true);
            EMT_H.setEnabled(true);
            EMT_L.setEnabled(true);
            RT_H.setEnabled(true);
            FLC_L.setEnabled(true);
            FLC_H.setEnabled(true);
            saveBtn.setVisibility(View.VISIBLE);
        }else {
            FES1.setEnabled(false);
            FET1.setEnabled(false);
            SE1.setEnabled(false);
            C_L1.setEnabled(false);
            C_H1.setEnabled(false);
            RS.setEnabled(false);
            RT.setEnabled(false);
            C_L2.setEnabled(false);
            C_H2.setEnabled(false);
            SES2.setEnabled(false);
            C_L3.setEnabled(false);
            C_H3.setEnabled(false);
            FLS.setEnabled(false);
            EVT.setEnabled(false);
            IS.setEnabled(false);
            IT_H.setEnabled(false);
            IT_L.setEnabled(false);
            EMS.setEnabled(false);
            EMT_H.setEnabled(false);
            EMT_L.setEnabled(false);
            RT_H.setEnabled(false);
            FLC_L.setEnabled(false);
            FLC_H.setEnabled(false);
            saveBtn.setVisibility(View.GONE);

        }
    }


    private class EditFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
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

    private class mSpfwhSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int selectIndex = valveList.indexOf(mSpfwh.getSelectedItem().toString());
            if (selectIndex != -1) {
                clearEditText();
                byte[] arrayOfByte = DataUtil.shortToByte((short) (2110 + 100 * selectIndex));
                Log.i("setJLCS", String.valueOf((short) (2110 + 100 * selectIndex)));

                DataUtil.reverse(arrayOfByte);
                RealTimeStatusThread.suspend();
                SendManager.SendCmd(mCompName + "_查" + mSpfwh.getSelectedItem().toString() + "阀的计量参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 3, 1000, 23);
                RealTimeStatusThread.resume();
            }
            FullWindows(mActivityWindow);
        }

        private void clearEditText() {
            FES1.setText("");
            FET1.setText("");
            SE1.setText("");
            C_L1.setText("");
            C_H1.setText("");
            RS.setText("");
            RT.setText("");
            C_L2.setText("");
            C_H2.setText("");
            SES2.setText("");
            C_L3.setText("");
            C_H3.setText("");
            FLS.setText("");
            EVT.setText("");
            IS.setText("");
            IT_H.setText("");
            IT_L.setText("");
            EMS.setText("");
            EMT_H.setText("");
            EMT_L.setText("");
            RT_H.setText("");
            FLC_L.setText("");
            FLC_H.setText("");
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
            int selectedItemIndex = valveList.indexOf(mSpfwh.getSelectedItem().toString());
            String FES1Str = FES1.getText().toString().trim();
            String FET1Str = FET1.getText().toString().trim();
            String SE1Str = SE1.getText().toString().trim();
            String CL1Str = C_L1.getText().toString().trim();
            String CH1Str = C_H1.getText().toString().trim();

            String RSStr = RS.getText().toString().trim();
            String RTStr = RT.getText().toString().trim();
            String CL2Str = C_L2.getText().toString().trim();
            String CH2Str = C_H2.getText().toString().trim();
            String SES2Str = SES2.getText().toString().trim();

            String CL3Str = C_L3.getText().toString().trim();
            String CH3Str = C_H3.getText().toString().trim();
            String FLSStr = FLS.getText().toString().trim();
            String EVTStr = EVT.getText().toString().trim();
            String ISStr = IS.getText().toString().trim();

            String ITHStr = IT_H.getText().toString().trim();
            String ITLStr = IT_L.getText().toString().trim();
            String EMSStr = EMS.getText().toString().trim();
            String EMTHStr = EMT_H.getText().toString().trim();
            String EMTLStr = EMT_L.getText().toString().trim();

            String RTHStr = RT_H.getText().toString().trim();
            String FLCLStr = FLC_L.getText().toString().trim();
            String FLCHStr = FLC_H.getText().toString().trim();


            if (!FES1Str.equals("") && !FET1Str.equals("") && !SE1Str.equals("") && !CL1Str.equals("") && !CH1Str.equals("") &&
                    !RSStr.equals("") && !RTStr.equals("") && !CL2Str.equals("") && !CH2Str.equals("") && !SES2Str.equals("")
                    && !CL3Str.equals("") && !CH3Str.equals("") && !FLSStr.equals("") && !EVTStr.equals("") && !ISStr.equals("")
                    && !ITHStr.equals("") && !ITLStr.equals("") && !EMSStr.equals("") && !EMTHStr.equals("") && !EMTLStr.equals("")
                    && !RTHStr.equals("") && !FLCLStr.equals("") && !FLCHStr.equals("")) {

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
                        toByteArray(Integer.parseInt(ITHStr), 4));

                if (selectedItemIndex != -1) {
                    saveOperationLogMsg(mCompName, "设置计量参数", ErrorLog.msgType.操作_信息);
                    SendManager.SendCmd(MainActivity.mCompName + "_" + mSpfwh.getSelectedItem().toString() + "阀计量参数设置_06_" + (39 + selectedItemIndex), S0, 3, 200, dataByte);
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.measurement_par_empty), Toast.LENGTH_SHORT).show();
            }
            FullWindows(mActivityWindow);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mjlcsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                FES1.setText((getCmds(mCompName).getCmd(msg.what).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what).getValue()).toString());
                SE1.setText((getCmds(mCompName).getCmd(msg.what + 1).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 1).getValue()).toString());
                RS.setText((getCmds(mCompName).getCmd(msg.what + 2).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 2).getValue()).toString());
                SES2.setText((getCmds(mCompName).getCmd(msg.what + 3).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 3).getValue()).toString());
                FLS.setText((getCmds(mCompName).getCmd(msg.what + 4).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 4).getValue()).toString());

                IS.setText((getCmds(mCompName).getCmd(msg.what + 5).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 5).getValue()).toString());
                EMS.setText((getCmds(mCompName).getCmd(msg.what + 6).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 6).getValue()).toString());
                C_L1.setText((getCmds(mCompName).getCmd(msg.what + 7).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 7).getValue()).toString());
                C_H1.setText((getCmds(mCompName).getCmd(msg.what + 8).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 8).getValue()).toString());
                FLC_L.setText((getCmds(mCompName).getCmd(msg.what + 9).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 9).getValue()).toString());

                FLC_H.setText((getCmds(mCompName).getCmd(msg.what + 10).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 10).getValue()).toString());
                C_L2.setText((getCmds(mCompName).getCmd(msg.what + 11).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 11).getValue()).toString());

                C_H2.setText((getCmds(mCompName).getCmd(msg.what + 12).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 12).getValue()).toString());
                C_L3.setText((getCmds(mCompName).getCmd(msg.what + 13).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 13).getValue()).toString());
                C_H3.setText((getCmds(mCompName).getCmd(msg.what + 14).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 14).getValue()).toString());
                FET1.setText((getCmds(mCompName).getCmd(msg.what + 15).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 15).getValue()).toString());
                RT.setText((getCmds(mCompName).getCmd(msg.what + 16).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 16).getValue()).toString());


                RT_H.setText((getCmds(mCompName).getCmd(msg.what + 17).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 17).getValue()).toString());
                EVT.setText((getCmds(mCompName).getCmd(msg.what + 18).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 18).getValue()).toString());
                EMT_L.setText((getCmds(mCompName).getCmd(msg.what + 19).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 19).getValue()).toString());
                EMT_H.setText((getCmds(mCompName).getCmd(msg.what + 20).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 20).getValue()).toString());
                IT_L.setText((getCmds(mCompName).getCmd(msg.what + 21).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 21).getValue()).toString());

                IT_H.setText((getCmds(mCompName).getCmd(msg.what + 22).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 22).getValue()).toString());
                Toast.makeText(context, context.getString(R.string.is_dirty), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, context.getString(R.string.exception_par), Toast.LENGTH_SHORT).show();
            }
        }
    };

}
