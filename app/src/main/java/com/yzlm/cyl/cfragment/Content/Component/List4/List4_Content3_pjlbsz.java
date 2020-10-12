package com.yzlm.cyl.cfragment.Content.Component.List4;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;

import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.setW200JLBoard;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pjlbsz extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pjlbsz fragment = null;


    private static ToggleButton mTogBtnCollect;
    private LinearLayout layoutAddrSet;
    private EditText mEditAddrNum;
    private EditText mEditAddrName;
    private EditText mEditJLBSpeed;
    private static EditText mEdJLBL1;
    private static EditText mEdJLBL2;
    private Spinner mSpJlbAddr;
    private static TextView mtxtJLB_L1;
    private static TextView mtxtJLB_L2;
    private static TextView mtxtJLB_L3;
    private static TextView mtxtJLB_H1;
    private static TextView mtxtJLB_H2;
    private static TextView mtxtJLB_H3;
    private ToggleButton togBtnJLBcy, togBtnJLBpy;
    private List<String> listJlbName;
    private Thread refreshThread = null;
    private static int jlbId = 0;
    private static Boolean threadInterrupt = true;

    public static List4_Content3_pjlbsz newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pjlbsz();
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
        return R.layout.list4_content3_pjlbsz;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        InitWidget();
    }

    private void InitWidget() {
        try {
            Global.LockDisplayShowFlag = true;
            // 计量板自动配置
            setW200JLBoard(mCompName);
            initSpinnerData();

            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());

            ToggleButton mtogBtnAddr = v.findViewById(R.id.togBtnAddr);
            layoutAddrSet = v.findViewById(R.id.layout_jlbAddr);
            mEditAddrNum = v.findViewById(R.id.etjlbAddr);
            mEditAddrName = v.findViewById(R.id.etjlbName);
            Button btnAddrSave = v.findViewById(R.id.btn_AddrSave);
            Button btnAddrAllClear = v.findViewById(R.id.btn_AddrAllClear);

            mTogBtnCollect = v.findViewById(R.id.togBtnJlbCollect);
            mSpJlbAddr = v.findViewById(R.id.spJlbAddr);
            ToggleButton togBtnJlbST = v.findViewById(R.id.togBtnJlbST);
            mtxtJLB_L1 = v.findViewById(R.id.txtJLB_L1);
            mtxtJLB_L2 = v.findViewById(R.id.txtJLB_L2);
            mtxtJLB_L3 = v.findViewById(R.id.txtJLB_L3);

            mtxtJLB_H1 = v.findViewById(R.id.txtJLB_H1);
            mtxtJLB_H2 = v.findViewById(R.id.txtJLB_H2);
            mtxtJLB_H3 = v.findViewById(R.id.txtJLB_H3);

            mEdJLBL1 = v.findViewById(R.id.edJLBL1);
            mEdJLBL2 = v.findViewById(R.id.edJLBL2);
            togBtnJLBcy = v.findViewById(R.id.Btn_pJLBsz_cy);
            togBtnJLBpy = v.findViewById(R.id.Btn_pJLBsz_py);
            mEditJLBSpeed = v.findViewById(R.id.edit_JLBSpeed);


            mtogBtnAddr.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());
            mEditAddrNum.setOnFocusChangeListener(new editCheckChangeListener());
            mEditAddrNum.setOnEditorActionListener(new editCheckChangeListener());
            btnAddrSave.setOnClickListener(new btnOnClickListener());
            btnAddrAllClear.setOnClickListener(new btnOnClickListener());
            mEditAddrName.setOnFocusChangeListener(new editCheckChangeListener());
            mEditAddrName.setOnEditorActionListener(new editCheckChangeListener());
            mTogBtnCollect.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());
            togBtnJLBcy.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());
            togBtnJLBpy.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());
            mEditJLBSpeed.setOnFocusChangeListener(new editCheckChangeListener());
            mEditJLBSpeed.setOnEditorActionListener(new editCheckChangeListener());
            mEdJLBL1.setOnFocusChangeListener(new editCheckChangeListener());
            mEdJLBL1.setOnEditorActionListener(new editCheckChangeListener());
            mEdJLBL2.setOnFocusChangeListener(new editCheckChangeListener());
            mEdJLBL2.setOnEditorActionListener(new editCheckChangeListener());

            InitSpinner(context, mSpJlbAddr, listJlbName.toArray(new String[listJlbName.size()]), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            mSpJlbAddr.setOnItemSelectedListener(new SpinnerClickListener());
            mtogBtnAddr.setChecked(false);

            togBtnJlbST.setOnCheckedChangeListener(new TogBtnOnCheckedChangeListener());
            try {
                readJLBMeasureValue();
            } catch (Exception e) {

            }
            LinearLayout mLayoutTeeValve = v.findViewById(R.id.layout_teeValve);
            if (QueryMeasBoardType(mCompName).equals("2")) {
                mLayoutTeeValve.setVisibility(View.INVISIBLE);
                mtxtJLB_L3.setVisibility(View.VISIBLE);
                mtxtJLB_H3.setVisibility(View.VISIBLE);


            } else {
                mLayoutTeeValve.setVisibility(View.VISIBLE);
                mtxtJLB_L3.setVisibility(View.GONE);
                mtxtJLB_H3.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void initSpinnerData() {
        try {
            listJlbName = new ArrayList<>();
            listJlbName.clear();
            listJlbName.add(0, context.getResources().getString(R.string.notSet));

            for (int i = 0; i < 20; i++) {
                if (!getConfigData(mCompName, "JLB_" + (i + 1) + "_NAME").equals("")) {
                    listJlbName.add(listJlbName.size(), getConfigData(mCompName, "JLB_" + (i + 1) + "_NAME"));
                }
            }
        } catch (Exception e) {

        }
    }

    private void readJLBMeasureValue() {
        if (jlbId != 0) {
            byte[] arrayOfByte = DataUtil.shortToByte((short) (5800 + (jlbId - 1) * 4));
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(mCompName + "_查计量板" + mSpJlbAddr.getSelectedItemId() + "计量阈值" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 2, 200, 4);
        }
    }

    private class btnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_AddrSave:
                    if (editTextInputStatus(mEditAddrNum, 0, 21)) {
                        RealTimeStatusThread.suspend();

                        // W200  五合一 计量板 不需要下发设置地址
                        if (!QueryMeasBoardType(mCompName).equals("2")) {
                            try {
                                byte[] sByte = copybyte(toByteArray(Integer.parseInt(mEditAddrNum.getText().toString()), 4));
                                if (!mEditAddrName.getText().toString().equals("")) {
                                    SendManager.SendCmd(mCompName + "_" + "设置计量板" + mEditAddrNum.getText().toString() + "地址" + "_06_" + (68 + Integer.parseInt(mEditAddrNum.getText().toString()) - 1), S0, 3, 100, sByte);
                                } else {
                                    byte[] sByteAddr = copybyte(toByteArray(0, 4));
                                    SendManager.SendCmd(mCompName + "_" + "设置计量板" + mEditAddrNum.getText().toString() + "地址" + "_06_" + (68 + Integer.parseInt(mEditAddrNum.getText().toString()) - 1), S0, 3, 100, sByteAddr);
                                }
                            } catch (Exception e) {
                            }
                        }
                        saveOperationLogMsg(mCompName, "设置计量板" + mEditAddrNum.getText().toString() + ":" + mEditAddrName.getText().toString(), ErrorLog.msgType.操作_信息);
                        RealTimeStatusThread.resume();
                        updateConfigData(mCompName, "JLB_" + mEditAddrNum.getText().toString() + "_NAME", mEditAddrName.getText().toString().equals("") ? "" : mEditAddrNum.getText().toString() + "_" + mEditAddrName.getText().toString());
                        initSpinnerData();
                        InitSpinner(context, mSpJlbAddr, listJlbName.toArray(new String[listJlbName.size()]), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
                    }
                    break;
                case R.id.btn_AddrAllClear:
                    RealTimeStatusThread.suspend();
                    saveOperationLogMsg(mCompName, "清空计量板所有地址", ErrorLog.msgType.操作_信息);

                    for (int i = 0; i < 20; i++) {
                        if (!QueryMeasBoardType(mCompName).equals("2")) {
                            // W200  五合一 计量板 不需要下发设置地址
                            byte[] sByteAddr = copybyte(toByteArray(0, 4));
                            SendManager.SendCmd(mCompName + "_" + "设置计量板" + (i + 1) + "地址" + "_06_" + (68 + i), S0, 3, 100, sByteAddr);
                        }
                        updateConfigData(mCompName, "JLB_" + (i + 1) + "_NAME", "");
                    }

                    initSpinnerData();
                    InitSpinner(context, mSpJlbAddr, listJlbName.toArray(new String[listJlbName.size()]), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
                    RealTimeStatusThread.resume();
                    break;
            }
        }
    }

    private class TogBtnOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.togBtnAddr:
                    layoutAddrSet.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                    break;
                case R.id.togBtnJlbCollect:
                    if (isChecked) {
                        if (listJlbName.size() == 1) {
                            Toast.makeText(getContext(), getString(R.string.please_config_metering_board_info), Toast.LENGTH_SHORT).show();
                            mTogBtnCollect.setChecked(false);
                        } else {
                            if (mSpJlbAddr.getSelectedItemId() != 0 && mSpJlbAddr.getSelectedItemId() < listJlbName.size()) {
                                jlbId = Integer.valueOf(mSpJlbAddr.getSelectedItem().toString().split("_")[0]);
                                threadInterrupt = false;

                                refreshThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int times = 60;
                                        while (times-- > 1) {
                                            byte[] arrayOfByte = DataUtil.shortToByte((short) (270 + (jlbId - 1) * 12));
                                            DataUtil.reverse(arrayOfByte);

                                            SendManager.SendCmd(mCompName + "_查计量板" + mSpJlbAddr.getSelectedItemId() + "计量能量" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 1, 200, 6);
                                            if (threadInterrupt) {
                                                threadInterrupt = false;
                                                break;
                                            }
                                            try {
                                                sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Message msg = new Message();
                                        msg.what = 1;
                                        jlbszhandler.sendMessage(msg);
                                    }
                                });
                                refreshThread.start();
                            } else {
                                mTogBtnCollect.setChecked(false);
                            }
                        }
                    } else {
                        Log.i("refreshThread", refreshThread == null ? "true" : "false");
                        if (refreshThread != null) {
                            threadInterrupt = true;
                        }
                    }
                    break;
                case R.id.togBtnJlbST:
                    if (jlbId != 0) {
                        SendManager.SendCmd(mCompName + "_计量板" + jlbId + "三通阀_8_14", S0, 3, 200, copybyte(new byte[]{(byte) jlbId, (byte) (isChecked == true ? 1 : 0)}));
                    } else {
                        buttonView.setChecked(false);
                    }
                    break;
                case R.id.Btn_pJLBsz_cy:
                   {
                       if(isChecked){
                           if(togBtnJLBpy.isChecked()){
                               togBtnJLBpy.setChecked(false);
                           }
                       }
                        if (jlbId != 0 && !(mEditJLBSpeed.getText().toString().equals(""))) {
                            byte speed = Byte.parseByte(mEditJLBSpeed.getText().toString());
                            SendManager.SendCmd(mCompName + "_计量板" + jlbId + "蠕动泵_8_15", S0, 3, 200, copybyte(new byte[]{(byte) jlbId, (byte) (isChecked == true ? 1 : 0), (byte) (isChecked == true ? 0 : 1), speed}));
                        } else {
                            buttonView.setChecked(false);
                        }
                    }
                    break;
                case R.id.Btn_pJLBsz_py:
                    {
                        if(isChecked){
                            if(togBtnJLBcy.isChecked()){
                                togBtnJLBcy.setChecked(false);
                            }
                        }
                        if (jlbId != 0 && !(mEditJLBSpeed.getText().toString().equals(""))) {
                            byte speed2 = Byte.parseByte(mEditJLBSpeed.getText().toString());
                            SendManager.SendCmd(mCompName + "_计量板" + jlbId + "蠕动泵_8_15", S0, 3, 200, copybyte(new byte[]{(byte) jlbId, (byte) (isChecked == true ? 1 : 0), (byte) (isChecked == true ? 1 : 0), speed2}));
                        } else {
                            buttonView.setChecked(false);
                        }
                    }
                    break;
            }
        }
    }


    private class SpinnerClickListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mTogBtnCollect.setChecked(false);
            try {
                if (id != 0) {
                    mEdJLBL1.setText("");
                    mEdJLBL2.setText("");
                    mtxtJLB_L1.setText("");
                    mtxtJLB_L2.setText("");
                    mtxtJLB_L3.setText("");
                    mtxtJLB_H1.setText("");
                    mtxtJLB_H2.setText("");
                    mtxtJLB_H3.setText("");
                    jlbId = Integer.valueOf(mSpJlbAddr.getSelectedItem().toString().split("_")[0]);
                    readJLBMeasureValue();
                    togBtnJLBcy.setChecked(false);
                    togBtnJLBpy.setChecked(false);
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class editCheckChangeListener implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.etjlbAddr:
                        if (!editTextInputStatus(mEditAddrNum, 0, 21)) {
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + "(0 ~ 21 ) !", Toast.LENGTH_SHORT).show();
                            mEditAddrNum.setText("");
                        } else {
                            String strValue = editDataFormat(mEditAddrNum.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            mEditAddrNum.setText(strValue);
                        }
                        break;
                    case R.id.etjlbName:
                        if (!mEditAddrName.getText().toString().equals("")) {
                        }
                        break;
                    case R.id.edit_JLBSpeed:
                        if (!editTextInputStatus(mEditJLBSpeed, 0, 96)) {
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + "( 0 ~ 96 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            mEditJLBSpeed.setText("50");
                        } else {
                            if (mEditJLBSpeed.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String strValue = editDataFormat(mEditJLBSpeed.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                            mEditJLBSpeed.setText(strValue);
                        }
                        break;
                    case R.id.edJLBL1:
                    case R.id.edJLBL2:
                        if (jlbId == 0) {
                            Toast.makeText(getContext(), getString(R.string.please_select_comm_board), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (!editTextInputStatus(mEdJLBL1, 1000, 5000)) {
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + "( 1000 ~ 5000 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            mEdJLBL1.setText("1800");
                        }
                        if (!editTextInputStatus(mEdJLBL2, 1000, 5000)) {
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 1000 ~ 5000 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            mEdJLBL2.setText("1800");
                        }
                        if (mEdJLBL1.getText().toString().equals("") || mEdJLBL2.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String strjlbl1, strjlbl2;
                        strjlbl1 = editDataFormat(mEdJLBL1.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                        strjlbl2 = editDataFormat(mEdJLBL2.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                        byte[] sByte = copybyte(toByteArray(0, 4), toByteArray(0, 4), toByteArray(Integer.parseInt(strjlbl1), 4), toByteArray(Integer.parseInt(strjlbl2), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置滴定液计数阈值_06_" + (88 + jlbId - 1), S0, 3, 100, sByte);
                        mEdJLBL1.setText(strjlbl1);
                        mEdJLBL2.setText(strjlbl2);
                        break;
                }
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

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            threadInterrupt = true;
            if (doFlowing.get(mCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                SendManager.SendCmd(mCompName + "_紧急停止" + "_8_07", S0, 3, 1000, null);
            }
            mCallbacks.onListSelected(v);
        }
    }

    public static Handler jlbszhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mTogBtnCollect.setChecked(false);
                    break;
                case 270:
                case 282:
                case 294:
                case 306:
                case 318:
                case 330:
                case 342:
                case 354:
                case 366:
                case 378:
                case 390:
                case 402:
                case 414:
                case 426:
                case 438:
                case 450:
                case 462:
                case 474:
                case 486:
                case 498:
                    if (QueryMeasBoardType(mCompName).equals("1")) {
                        mtxtJLB_L1.setText((getCmds(mCompName).getCmd(msg.what).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what).getValue()).toString());
                        mtxtJLB_L2.setText((getCmds(mCompName).getCmd(msg.what + 1).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 1).getValue()).toString());
                        mtxtJLB_H1.setText((getCmds(mCompName).getCmd(msg.what + 2).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 2).getValue()).toString());
                        mtxtJLB_H2.setText((getCmds(mCompName).getCmd(msg.what + 3).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 3).getValue()).toString());
                    } else {
                        mtxtJLB_L1.setText((getCmds(mCompName).getCmd(msg.what).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what).getValue()).toString());
                        mtxtJLB_L2.setText((getCmds(mCompName).getCmd(msg.what + 1).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 1).getValue()).toString());
                        mtxtJLB_L3.setText((getCmds(mCompName).getCmd(msg.what + 2).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 2).getValue()).toString());
                        mtxtJLB_H1.setText((getCmds(mCompName).getCmd(msg.what + 3).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 3).getValue()).toString());
                        mtxtJLB_H2.setText((getCmds(mCompName).getCmd(msg.what + 4).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 4).getValue()).toString());
                        mtxtJLB_H3.setText((getCmds(mCompName).getCmd(msg.what + 5).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 5).getValue()).toString());
                    }
                    break;
                case 5800:
                case 5804:
                case 5808:
                case 5812:
                case 5816:
                case 5820:
                case 5824:
                case 5828:
                case 5832:
                case 5836:
                case 5840:
                case 5844:
                case 5848:
                case 5852:
                case 5856:
                case 5860:
                case 5864:
                case 5868:
                case 5872:
                case 5876:
                    mEdJLBL1.setText((getCmds(mCompName).getCmd(msg.what + 2).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 2).getValue()).toString());
                    mEdJLBL2.setText((getCmds(mCompName).getCmd(msg.what + 3).getValue() == null ? "" : getCmds(mCompName).getCmd(msg.what + 3).getValue()).toString());
                    break;
            }

        }
    };
}
