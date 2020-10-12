package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
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
import com.yzlm.cyl.cfragment.Dialog.Component.SelectTime;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandPackaging.GetForwardCmd;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.HexString2Bytes;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.JXQDB;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_probotset extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_probotset fragment = null;

    private ToggleButton tBtnOptocoupler_settings, tBtnCoordinate_settings, tBtnCoordinate_view;
    private LinearLayout laylist2con1line1_1, laylist2con1line2_2, laylist2con1line2_3, laylist2con1line4, laylist2con1line3;

    private Button btn_GOSZ_X, btn_GOSZ_Y, btn_GOSZ_Z;
    private Button btn_ZBSZ_Save, btn_ZBSZ_Delete, btn_ZBSZ_Send, btn_ZBSZ_Init;

    private Spinner mSpZBSZ;
    private Spinner mSpZBSZ_X, mSpZBSZ_Y, mSpZBSZ_Z;

    private TextView txtZBLB;

    private String[] sCoordinate, sZBD_X, sZBD_Y, sZBD_Z;
    private String[] sPoints;

    private EditText edit_ZBSZ_Name;

    private boolean zbChange = false;


    public static List4_Content3_probotset newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_probotset();
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
        return R.layout.list4_content3_robotset;
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

            InitWidget();

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void InitWidget() {
        //updateConfigData(mCompName, "PpointsAddr", "");
        //updateConfigData(mCompName, "PpointsNum", "0");
        sPoints = getConfigData(mCompName, "PpointsAddr").split(";");

        laylist2con1line1_1 = (LinearLayout) v.findViewById(R.id.list2con1line1_1);
        laylist2con1line1_1.setVisibility(View.GONE);
        laylist2con1line2_2 = (LinearLayout) v.findViewById(R.id.list2con1line2_2);
        laylist2con1line2_2.setVisibility(View.GONE);
        laylist2con1line2_3 = (LinearLayout) v.findViewById(R.id.list2con1line2_3);
        laylist2con1line2_3.setVisibility(View.GONE);
        laylist2con1line4 = (LinearLayout) v.findViewById(R.id.list2con1line4);
        laylist2con1line4.setVisibility(View.GONE);
        laylist2con1line3 = (LinearLayout) v.findViewById(R.id.list2con1line3);
        laylist2con1line3.setVisibility(View.GONE);

        tBtnOptocoupler_settings = (ToggleButton) v.findViewById(R.id.tBtnOptocoupler_settings);
        tBtnOptocoupler_settings.setOnCheckedChangeListener(new togBtnClickListener());
        tBtnOptocoupler_settings.setChecked(false);

        tBtnCoordinate_settings = (ToggleButton) v.findViewById(R.id.tBtnCoordinate_settings);
        tBtnCoordinate_settings.setOnCheckedChangeListener(new togBtnClickListener());
        tBtnCoordinate_settings.setChecked(false);

        tBtnCoordinate_view = (ToggleButton) v.findViewById(R.id.tBtnCoordinate_view);
        tBtnCoordinate_view.setOnCheckedChangeListener(new togBtnClickListener());
        tBtnCoordinate_view.setChecked(false);

        txtZBLB = (TextView) v.findViewById(R.id.txt_zblb);

        btn_GOSZ_X = (Button) v.findViewById(R.id.Btn_gosz_x);
        btn_GOSZ_X.setOnClickListener(new btnSetClick());
        btn_GOSZ_Y = (Button) v.findViewById(R.id.Btn_gosz_y);
        btn_GOSZ_Y.setOnClickListener(new btnSetClick());
        btn_GOSZ_Z = (Button) v.findViewById(R.id.Btn_gosz_z);
        btn_GOSZ_Z.setOnClickListener(new btnSetClick());

        mSpZBSZ = (Spinner) v.findViewById(R.id.sp_zbsz);
        RefreshZBD();
        mSpZBSZ.setSelection(0);
        mSpZBSZ.setOnItemSelectedListener(new mSpListener());

        edit_ZBSZ_Name = v.findViewById(R.id.edit_zbsz_name);

        sZBD_X = getConfigData(mCompName, "GOSZ_X").split("，");
        sZBD_Y = getConfigData(mCompName, "GOSZ_Y").split("，");
        sZBD_Z = getConfigData(mCompName, "GOSZ_Z").split("，");
        mSpZBSZ_X = (Spinner) v.findViewById(R.id.sp_zbsz_x);
        InitSpinner(context, mSpZBSZ_X, sZBD_X, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
        //mSpZBSZ_X.setOnItemSelectedListener(new mSpListener());
        mSpZBSZ_Y = (Spinner) v.findViewById(R.id.sp_zbsz_y);
        InitSpinner(context, mSpZBSZ_Y, sZBD_Y, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
        //mSpZBSZ_Y.setOnItemSelectedListener(new mSpListener());
        mSpZBSZ_Z = (Spinner) v.findViewById(R.id.sp_zbsz_z);
        InitSpinner(context, mSpZBSZ_Z, sZBD_Z, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
        //mSpZBSZ_Z.setOnItemSelectedListener(new mSpListener());

        btn_ZBSZ_Save = (Button) v.findViewById(R.id.Btn_zbsz_save);
        btn_ZBSZ_Save.setOnClickListener(new btnSetClick());
        btn_ZBSZ_Delete = (Button) v.findViewById(R.id.Btn_zbsz_delete);
        btn_ZBSZ_Delete.setOnClickListener(new btnSetClick());
        //btn_ZBSZ_Delete.setVisibility(View.GONE);
        btn_ZBSZ_Send = (Button) v.findViewById(R.id.Btn_zbsz_send);
        btn_ZBSZ_Send.setOnClickListener(new btnSetClick());
        btn_ZBSZ_Init = (Button) v.findViewById(R.id.Btn_zbsz_init);
        btn_ZBSZ_Init.setOnClickListener(new btnSetClick());

        ShowPoints(0);
    }

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class mSpListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {
                case R.id.sp_zbsz: {
                    if (!mSpZBSZ.getSelectedItem().equals("新增")) {
                        ShowPoints(Integer.parseInt(mSpZBSZ.getSelectedItem().toString()));
                        btn_ZBSZ_Delete.setVisibility(View.VISIBLE);
                    } else {
                        btn_ZBSZ_Delete.setVisibility(View.GONE);
                        edit_ZBSZ_Name.setText("");
                    }
                }
                break;
            }
//            if (sFlow[position].equals(context.getResources().getString(R.string.reserve))) {
//                mSpFlow.setSelection(0);
//            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tBtnOptocoupler_settings: {
                    if (isChecked) {
                        laylist2con1line1_1.setVisibility(View.VISIBLE);
                    } else {
                        laylist2con1line1_1.setVisibility(View.GONE);
                    }
                }
                break;
                case R.id.tBtnCoordinate_settings: {
                    if (isChecked) {
                        laylist2con1line2_2.setVisibility(View.VISIBLE);
                        laylist2con1line2_3.setVisibility(View.VISIBLE);
                    } else {
                        laylist2con1line2_2.setVisibility(View.GONE);
                        laylist2con1line2_3.setVisibility(View.GONE);
                    }
                }
                break;
                case R.id.tBtnCoordinate_view: {
                    if (isChecked) {
                        String strInfo = " \t\t\t\t\t\tX \t\t\t\t\t\tY \t\t\t\t\t\tZ \n";
                        if (!sPoints[0].equals("")) {
                            for (int i = 0; i < sPoints.length; i++) {
                                String[] sPoint = sPoints[i].split("-");
                                strInfo += ((i + 1) + "\t\t\t\t\t\t" + String.format("%02d", Integer.parseInt(sPoint[0])) + "\t\t\t\t\t\t" + String.format("%02d", Integer.parseInt(sPoint[1])) + "\t\t\t\t\t\t" + String.format("%02d", Integer.parseInt(sPoint[2])) + "\n");
                            }
                        }
                        txtZBLB.setText(strInfo);
                        laylist2con1line4.setVisibility(View.VISIBLE);
                    } else {
                        laylist2con1line4.setVisibility(View.GONE);
                    }
                }
                break;
            }
            syncList();
        }
    }


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (zbChange) {
                Toast.makeText(getContext(), getString(R.string.the_coordinates_have_been_modified_please_reissue), Toast.LENGTH_LONG).show();
                return;
            }
            mCallbacks.onListSelected(v);
        }
    }

    private class btnSetClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Btn_gosz_x: {
                    ShowSelectTime("alert-select_time", "GOSZ_X");
                    break;
                }
                case R.id.Btn_gosz_y: {
                    ShowSelectTime("alert-select_time", "GOSZ_Y");
                    break;
                }
                case R.id.Btn_gosz_z: {
                    ShowSelectTime("alert-select_time", "GOSZ_Z");
                    break;
                }
                case R.id.Btn_zbsz_save: {
                    int iStart = 0;
                    if (mSpZBSZ.getSelectedItem().equals("新增")) {
                        String strPpointsAddr = "";
                        if (!sPoints[0].equals("")) {
                            for (int i = 0; i < sPoints.length; i++) {
                                strPpointsAddr += sPoints[i] + ";";
                            }
                        }
                        strPpointsAddr += mSpZBSZ_X.getSelectedItem() + "-" + mSpZBSZ_Y.getSelectedItem() + "-" + mSpZBSZ_Z.getSelectedItem() + "-" + edit_ZBSZ_Name.getText().toString();
                        updateConfigData(mCompName, "PpointsAddr", strPpointsAddr);
                        sPoints = getConfigData(mCompName, "PpointsAddr").split(";");
                        iStart = Integer.parseInt(getConfigData(mCompName, "PpointsNum"));
                        updateConfigData(mCompName, "PpointsNum", String.valueOf(Integer.parseInt(getConfigData(mCompName, "PpointsNum")) + 1));
                        RefreshZBD();
                        mSpZBSZ.setSelection(0);
                        ShowPoints(0);
                        Toast.makeText(getContext(), getString(R.string.add_success), Toast.LENGTH_LONG).show();
                    } else {
                        sPoints[Integer.parseInt(mSpZBSZ.getSelectedItem().toString())] = mSpZBSZ_X.getSelectedItem() + "-" + mSpZBSZ_Y.getSelectedItem() + "-" + mSpZBSZ_Z.getSelectedItem() + "-" + edit_ZBSZ_Name.getText().toString();
                        String strPpointsAddr = "";
                        for (int i = 0; i < sPoints.length; i++) {
                            strPpointsAddr += sPoints[i] + ";";
                        }
                        updateConfigData(mCompName, "PpointsAddr", strPpointsAddr.substring(0, strPpointsAddr.length() - 1));
                        sPoints = getConfigData(mCompName, "PpointsAddr").split(";");
                        iStart = Integer.parseInt(mSpZBSZ.getSelectedItem().toString());
                        Toast.makeText(getContext(), getString(R.string.mod_success), Toast.LENGTH_LONG).show();
                    }
                    zbChange = true;
                    /*StringBuilder strZB = new StringBuilder("0000000000000000");
                    int iX = Integer.parseInt(mSpZBSZ_X.getSelectedItem().toString());
                    int iY = Integer.parseInt(mSpZBSZ_Y.getSelectedItem().toString());
                    int iZ = Integer.parseInt(mSpZBSZ_Z.getSelectedItem().toString());
                    strZB.replace(16 - (iX - 1) - 1, 16 - (iX - 1), "1");
                    strZB.replace(16 - (iY - 1) - 1, 16 - (iY - 1), "1");
                    strZB.replace(16 - (iZ - 1) - 1, 16 - (iZ - 1), "1");
                    byte[] sByte = copybyte(new byte[]{0x01, (byte) iStart}, rtnZB(strZB.toString()));
                    byte[] Cmd = GetForwardCmd(mCompName, 1, (byte) 38, "0", sByte);
                    if (Cmd != null) {
                        SendManager.SendCmd(mCompName + "_设置机械臂坐标点" + (iStart + 1) + "坐标_32_" + 02, S0, 3, 200, Cmd);
                    }*/
                    break;
                }
                case R.id.Btn_zbsz_delete: {
                    String strPpointsAddr = "";
                    for (int i = 0; i < sPoints.length; i++) {
                        if (i == Integer.parseInt(mSpZBSZ.getSelectedItem().toString())) {
                            continue;
                        }
                        strPpointsAddr += sPoints[i] + ";";
                    }
                    if (strPpointsAddr.equals("")) {
                        updateConfigData(mCompName, "PpointsAddr", strPpointsAddr);
                    } else {
                        updateConfigData(mCompName, "PpointsAddr", strPpointsAddr.substring(0, strPpointsAddr.length() - 1));
                    }
                    sPoints = getConfigData(mCompName, "PpointsAddr").split(";");
                    updateConfigData(mCompName, "PpointsNum", String.valueOf(Integer.parseInt(getConfigData(mCompName, "PpointsNum")) - 1));
                    RefreshZBD();
                    mSpZBSZ.setSelection(0);
                    ShowPoints(0);
                    Toast.makeText(getContext(), getString(R.string.delete_success), Toast.LENGTH_LONG).show();
                    zbChange = true;
                    break;
                }
                case R.id.Btn_zbsz_send: {
                    if (!sPoints[0].equals("")) {
                        byte[] sByte = copybyte(new byte[]{(byte) sPoints.length, 0x00});
                        for (int i = 0; i < sPoints.length; i++) {
                            String[] sPoint = sPoints[i].split("-");

                            StringBuilder strZB = new StringBuilder("0000000000000000");
                            int iX = Integer.parseInt(sPoint[0]);
                            int iY = Integer.parseInt(sPoint[1]);
                            int iZ = Integer.parseInt(sPoint[2]);
                            strZB.replace(16 - (iX - 1) - 1, 16 - (iX - 1), "1");
                            strZB.replace(16 - (iY - 1) - 1, 16 - (iY - 1), "1");
                            strZB.replace(16 - (iZ - 1) - 1, 16 - (iZ - 1), "1");
                            sByte = copybyte(sByte, rtnZB(strZB.toString()));
                        }
                        byte[] Cmd = GetForwardCmd(mCompName, 3, (byte) 38, "0", sByte);
                        if (Cmd != null) {
                            SendManager.SendCmd(mCompName + "_设置机械臂坐标点坐标_32_" + JXQDB, S0, 3, 200, Cmd);
                        }
                        Toast.makeText(getContext(), getString(R.string.send_success), Toast.LENGTH_LONG).show();
                    }
                    zbChange = false;
                }
                break;
                case R.id.Btn_zbsz_init: {
                    updateConfigData(mCompName, "PpointsAddr", "1-4-6;1-5-7;2-5-7;3-5-6");
                    sPoints = getConfigData(mCompName, "PpointsAddr").split(";");
                    updateConfigData(mCompName, "PpointsNum", "4");
                    RefreshZBD();
                    mSpZBSZ.setSelection(0);
                    ShowPoints(0);

                    byte[] Cmd = GetForwardCmd(mCompName, 3, (byte) 38, "0", new byte[]{0x04, 0x00, 0x29, 0x00, 0x51, 0x00, 0x52, 0x00, 0x34, 0x00});
                    if (Cmd != null) {
                        SendManager.SendCmd(mCompName + "_设置机械臂坐标点坐标_32_" + JXQDB, S0, 3, 200, Cmd);
                    }
                    Toast.makeText(getContext(), getString(R.string.send_success), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private void ShowPoints(int iPoint) {
        String[] sPoint = sPoints[iPoint].split("-");
        if (sPoint.length >= 3) {
            for (int i = 0; i < sZBD_X.length; i++) {
                if (sZBD_X[i].equals(sPoint[0])) {
                    mSpZBSZ_X.setSelection(i);
                }
            }
            for (int i = 0; i < sZBD_Y.length; i++) {
                if (sZBD_Y[i].equals(sPoint[1])) {
                    mSpZBSZ_Y.setSelection(i);
                }
            }
            for (int i = 0; i < sZBD_Z.length; i++) {
                if (sZBD_Z[i].equals(sPoint[2])) {
                    mSpZBSZ_Z.setSelection(i);
                }
            }
            if (sPoint.length >= 4) {
                edit_ZBSZ_Name.setText(sPoint[3]);
            } else {
                edit_ZBSZ_Name.setText("");
            }
        } else {
            mSpZBSZ_X.setSelection(0);
            mSpZBSZ_Y.setSelection(0);
            mSpZBSZ_Z.setSelection(0);
            edit_ZBSZ_Name.setText("");
        }
    }

    private void RefreshZBD() {
        int iPointsNum = Integer.parseInt(getConfigData(mCompName, "PpointsNum"));
        sCoordinate = new String[iPointsNum + 1];
        for (int i = 0; i < iPointsNum; i++) {
            sCoordinate[i] = String.valueOf(i);
        }
        sCoordinate[iPointsNum] = "新增";

        InitSpinner(context, mSpZBSZ, sCoordinate, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
    }

    private void ShowSelectTime(String key, String value) {
        main.removeDestopText(mfb);
        SelectTime st = new SelectTime(1);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        st.setArguments(bundle);
        st.setTargetFragment(List4_Content3_probotset.this, 1);
        st.show(fm, "select_time");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("SelectTime");
            updateConfigData(mCompName, "GOSZ_X", stringExtra);
            sZBD_X = getConfigData(mCompName, "GOSZ_X").split("，");
            InitSpinner(context, mSpZBSZ_X, sZBD_X, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
        } else if (requestCode == 10) {
            String stringExtra = data.getStringExtra("SelectTime");
            updateConfigData(mCompName, "GOSZ_Y", stringExtra);
            sZBD_Y = getConfigData(mCompName, "GOSZ_Y").split("，");
            InitSpinner(context, mSpZBSZ_Y, sZBD_Y, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
        } else if (requestCode == 11) {
            String stringExtra = data.getStringExtra("SelectTime");
            updateConfigData(mCompName, "GOSZ_Z", stringExtra);
            sZBD_Z = getConfigData(mCompName, "GOSZ_Z").split("，");
            InitSpinner(context, mSpZBSZ_Z, sZBD_Z, R.layout.simple_spinner_item_out_white, R.layout.simple_spinner_item_in_black);
        }
        if (requestCode == 1 || requestCode == 10 || requestCode == 11) {
            StringBuilder str2X = new StringBuilder("0000000000000000");
            StringBuilder str2Y = new StringBuilder("0000000000000000");
            StringBuilder str2Z = new StringBuilder("0000000000000000");
            if (!sZBD_X[0].equals("")) {
                for (int i = 0; i < sZBD_X.length; i++) {
                    int iA = Integer.parseInt(sZBD_X[i]);
                    str2X.replace(16 - (iA - 1) - 1, 16 - (iA - 1), "1");
                }
            }
            if (!sZBD_Y[0].equals("")) {
                for (int i = 0; i < sZBD_Y.length; i++) {
                    int iA = Integer.parseInt(sZBD_Y[i]);
                    str2Y.replace(16 - (iA - 1) - 1, 16 - (iA - 1), "1");
                }
            }
            if (!sZBD_Z[0].equals("")) {
                for (int i = 0; i < sZBD_Z.length; i++) {
                    int iA = Integer.parseInt(sZBD_Z[i]);
                    str2Z.replace(16 - (iA - 1) - 1, 16 - (iA - 1), "1");
                }
            }
            byte[] sByte = copybyte(new byte[]{0x03}, rtnZB(str2X.toString()), rtnZB(str2Y.toString()), rtnZB(str2Z.toString()));
            byte[] Cmd = GetForwardCmd(mCompName, 3, (byte) 38, "1", sByte);
            if (Cmd != null) {
                SendManager.SendCmd(mCompName + "_设置机械臂xyz范围_32_" + JXQDB, S0, 3, 200, Cmd);
            }
        }
    }


    private byte[] rtnZB(String strA) {
        int i = Integer.parseInt(strA, 2); //转成10进制
        String hex = Integer.toHexString(i); //转成16进制
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        byte[] rtnByte = HexString2Bytes(hex);
        reverse(rtnByte);
        return rtnByte;
    }

}
