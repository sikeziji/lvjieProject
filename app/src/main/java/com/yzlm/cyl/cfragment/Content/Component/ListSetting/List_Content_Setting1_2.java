package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Global.GetPhoneType;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.InitPort;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.S2;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.protocolDisplayList;
import static com.yzlm.cyl.cfragment.Global.protocolList;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List_Content_Setting1_2 extends SubFragment {

    private Callbacks mCallbacks;
    private LinearLayout mLayoutIoBoardDigOut;
    private LinearLayout mLayoutIoBoardDigOut2;
    private LinearLayout mLayoutNoIoBoardDigOut;
    private LinearLayout mLayoutIoBoardDigOut_485;

    private Button mIoBordCfg;

    private TextView txtvYX1;

    private static List_Content_Setting1_2 fragment = null;

    public static List_Content_Setting1_2 newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Setting1_2();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();
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
        return R.layout.list_content_setting1_2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p3);
        mBtnReturn.setOnClickListener(new BtnClickListener());

        txtvYX1 = (TextView) v.findViewById(R.id.txtXY1);

        mLayoutIoBoardDigOut = (LinearLayout) v.findViewById(R.id.layout_IO_Board_DigOut);
        mLayoutIoBoardDigOut2 = (LinearLayout) v.findViewById(R.id.layout_IO_Board_DigOut2);
        mLayoutNoIoBoardDigOut = (LinearLayout) v.findViewById(R.id.layout_NO_IO_Board_DigOut);
        mLayoutIoBoardDigOut_485 = (LinearLayout) v.findViewById(R.id.layout_IO_Board_DigOut_485);
        //boolean blIoBordEn = getPublicConfigData("IO_BORD").equals("true");
        int blIoBordEn = getIOBORD();
        String sIoBordCfg = getString(R.string.unConfigured);
        if (blIoBordEn == 0) {
            sIoBordCfg = getString(R.string.unConfigured);
        } else if (blIoBordEn == 1) {
            sIoBordCfg = getString(R.string.configured1);
        } else {
            sIoBordCfg = getString(R.string.configured2);
        }
        mIoBordCfg = (Button) v.findViewById(R.id.btn_ioBord);
        mIoBordCfg.setOnClickListener(new BtnClickListener());
        mIoBordCfg.setText(sIoBordCfg);
        if (blIoBordEn == 2) {
            txtvYX1.setText("RS232");
            mLayoutIoBoardDigOut.setVisibility(View.VISIBLE);
            mLayoutIoBoardDigOut2.setVisibility(View.VISIBLE);
            mLayoutNoIoBoardDigOut.setVisibility(View.GONE);
            mLayoutIoBoardDigOut_485.setVisibility(View.VISIBLE);
        } else if (blIoBordEn == 1) {
            txtvYX1.setText(getString(R.string.forInstrument));
            mLayoutIoBoardDigOut.setVisibility(View.VISIBLE);
            mLayoutIoBoardDigOut2.setVisibility(View.VISIBLE);
            mLayoutNoIoBoardDigOut.setVisibility(View.GONE);
            mLayoutIoBoardDigOut_485.setVisibility(View.GONE);
        } else {
            mLayoutIoBoardDigOut.setVisibility(View.GONE);
            mLayoutIoBoardDigOut2.setVisibility(View.GONE);
            mLayoutNoIoBoardDigOut.setVisibility(View.VISIBLE);
        }
        //showIoBord();

        Spinner mSpDigital1 = (Spinner) v.findViewById(R.id.spXY1);
        InitSpinner(context, mSpDigital1, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpDigital1.setOnItemSelectedListener(new SpClickListener());

        Spinner mSpDigital2 = (Spinner) v.findViewById(R.id.spXY2);
        InitSpinner(context, mSpDigital2, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpDigital2.setOnItemSelectedListener(new SpClickListener());

        Spinner mSpDigital3 = (Spinner) v.findViewById(R.id.spXY3);
        InitSpinner(context, mSpDigital3, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpDigital3.setOnItemSelectedListener(new SpClickListener());

        if (getPublicConfigData("DIGITAL1").equals("-1") || Integer.parseInt(getPublicConfigData("DIGITAL1")) >= protocolList.length) {
            mSpDigital1.setSelection(0);
        } else {
            mSpDigital1.setSelection(Integer.parseInt(getPublicConfigData("DIGITAL1")));
        }
        if (getPublicConfigData("DIGITAL2").equals("-1") || Integer.parseInt(getPublicConfigData("DIGITAL2")) >= protocolList.length) {
            mSpDigital2.setSelection(0);
        } else {
            mSpDigital2.setSelection(Integer.parseInt(getPublicConfigData("DIGITAL2")));
        }
        if (getPublicConfigData("DIGITAL3").equals("-1") || Integer.parseInt(getPublicConfigData("DIGITAL3")) >= protocolList.length) {
            mSpDigital3.setSelection(0);
        } else {
            mSpDigital3.setSelection(Integer.parseInt(getPublicConfigData("DIGITAL3")));
        }
        Spinner mSpDigital1Baud = (Spinner) v.findViewById(R.id.spBaud);
        InitSpinner(context, mSpDigital1Baud, getResources().getStringArray(R.array.baud), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpDigital1Baud.setOnItemSelectedListener(new SpClickListener());
        mSpDigital1Baud.setSelection(Integer.parseInt(getPublicConfigData("MNTD1BAUD")));

        // PC
        Spinner mSpDigital2Baud = (Spinner) v.findViewById(R.id.spBaudPC);
        InitSpinner(context, mSpDigital2Baud, getResources().getStringArray(R.array.baud), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpDigital2Baud.setOnItemSelectedListener(new SpClickListener());
        mSpDigital2Baud.setSelection(Integer.parseInt(getPublicConfigData("MNTD2BAUD")));

        Spinner mSpDigital3Baud = (Spinner) v.findViewById(R.id.spBaud2);
        InitSpinner(context, mSpDigital3Baud, getResources().getStringArray(R.array.baud), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpDigital3Baud.setOnItemSelectedListener(new SpClickListener());
        mSpDigital3Baud.setSelection(Integer.parseInt(getPublicConfigData("MNTD3BAUD")));

        Spinner mSpCOM1Xy = (Spinner) v.findViewById(R.id.spCOM1Xy);
        InitSpinner(context, mSpCOM1Xy, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpCOM1Xy.setOnItemSelectedListener(new SpClickListener());
        if (getPublicConfigData("COM1_DIGITAL").equals("-1") || Integer.parseInt(getPublicConfigData("COM1_DIGITAL")) >= protocolDisplayList.length) {
            mSpCOM1Xy.setSelection(0);
        } else {
            mSpCOM1Xy.setSelection(Integer.parseInt(getPublicConfigData("COM1_DIGITAL")));
        }
        Spinner mSpCOM1Baud = (Spinner) v.findViewById(R.id.spCOM1Baud);
        InitSpinner(context, mSpCOM1Baud, getResources().getStringArray(R.array.baud), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpCOM1Baud.setOnItemSelectedListener(new SpClickListener());
        mSpCOM1Baud.setSelection(Integer.parseInt(getPublicConfigData("COM1BAUD")));

        Spinner mSpCOM3Xy = (Spinner) v.findViewById(R.id.spCOM3Xy);
        InitSpinner(context, mSpCOM3Xy, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpCOM3Xy.setOnItemSelectedListener(new SpClickListener());
        if (getPublicConfigData("COM3_DIGITAL").equals("-1") || Integer.parseInt(getPublicConfigData("COM3_DIGITAL")) >= protocolDisplayList.length) {
            mSpCOM3Xy.setSelection(0);
        } else {
            mSpCOM3Xy.setSelection(Integer.parseInt(getPublicConfigData("COM3_DIGITAL")));
        }
        Spinner mSpCOM3Baud = (Spinner) v.findViewById(R.id.spCOM3Baud);
        InitSpinner(context, mSpCOM3Baud, getResources().getStringArray(R.array.baud), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpCOM3Baud.setOnItemSelectedListener(new SpClickListener());
        mSpCOM3Baud.setSelection(Integer.parseInt(getPublicConfigData("COM3BAUD")));

    }


    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ioBord:
                    int blIoBordEn = getIOBORD();
                    if (blIoBordEn == 0) {
                        saveOperationLogPublicDataModifyMsg("公共", "IO_BORD", "true", "接口板配置", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("IO_BORD", "1");
                        mIoBordCfg.setText(getString(R.string.configured1));
                        IOBoardUsed = true;

                        txtvYX1.setText(getString(R.string.forInstrument));
                        mLayoutIoBoardDigOut.setVisibility(View.VISIBLE);
                        mLayoutIoBoardDigOut2.setVisibility(View.VISIBLE);
                        mLayoutNoIoBoardDigOut.setVisibility(View.GONE);
                        mLayoutIoBoardDigOut_485.setVisibility(View.GONE);
                    } else if (blIoBordEn == 1) {
                        saveOperationLogPublicDataModifyMsg("公共", "IO_BORD", "true", "接口板配置", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("IO_BORD", "2");
                        mIoBordCfg.setText(getString(R.string.configured2));
                        IOBoardUsed = true;

                        txtvYX1.setText("RS232");
                        mLayoutIoBoardDigOut.setVisibility(View.VISIBLE);
                        mLayoutIoBoardDigOut2.setVisibility(View.VISIBLE);
                        mLayoutNoIoBoardDigOut.setVisibility(View.GONE);
                        mLayoutIoBoardDigOut_485.setVisibility(View.VISIBLE);
                    } else {
                        saveOperationLogPublicDataModifyMsg("公共", "IO_BORD", "false", "接口板配置", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("IO_BORD", "0");
                        mIoBordCfg.setText(getString(R.string.unConfigured));
                        IOBoardUsed = false;

                        mLayoutIoBoardDigOut.setVisibility(View.GONE);
                        mLayoutIoBoardDigOut2.setVisibility(View.GONE);
                        mLayoutNoIoBoardDigOut.setVisibility(View.VISIBLE);
                    }
                    if (S1 != null) {
                        S1.close();
                        S1 = null;
                    }
                    InitPort(GetPhoneType());
                    break;
                case R.id.btnReturn_p3:
                    mCallbacks.onListSelected(v);
                    break;
            }
        }
    }

    private class SpClickListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                switch (parent.getId()) {
                    case R.id.spXY1:
                        saveOperationLogPublicDataModifyMsg("公共", "DIGITAL1", String.valueOf(position), "数字量1-" + protocolList[position], ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("DIGITAL1", String.valueOf(position));
                        break;
                    case R.id.spXY2:
                        saveOperationLogPublicDataModifyMsg("公共", "DIGITAL2", String.valueOf(position), "数字量2-" + protocolList[position], ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("DIGITAL2", String.valueOf(position));
                        break;
                    case R.id.spXY3:
                        saveOperationLogPublicDataModifyMsg("公共", "DIGITAL3", String.valueOf(position), "数字量3-" + protocolList[position], ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("DIGITAL3", String.valueOf(position));
                        break;
                    case R.id.spBaud:
                        String[] strBaud = getResources().getStringArray(R.array.baud);
                        saveOperationLogPublicDataModifyMsg("公共", "MNTD1BAUD", String.valueOf(position), "波特率-" + strBaud[position], ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("MNTD1BAUD", String.valueOf(position));
                        String[] strSelectBaud = strBaud[position].split("bps");

                        byte[] cybszByte = copybyte(toByteArray(Integer.parseInt(strSelectBaud[0]), 4));

                        if (IOBoardUsed) {
                            //数采仪设置波特率
                            SendManager.SendCmd("所有组分" + "_设置RS232波特率" + "_6_13", S1, 1, 200, copybyte(new byte[]{3}, cybszByte));
                        }
                        break;
                    case R.id.spBaudPC:
                        String[] strBaud2 = getResources().getStringArray(R.array.baud);
                        saveOperationLogPublicDataModifyMsg("公共", "MNTD2BAUD", String.valueOf(position), "波特率-" + strBaud2[position], ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("MNTD2BAUD", String.valueOf(position));

                        String[] strSelectBaud2 = strBaud2[position].split("bps");
                        byte[] cybszByte2 = copybyte(toByteArray(Integer.parseInt(strSelectBaud2[0]), 4));
                        if (IOBoardUsed) {
                            //PC设置波特率
                            SendManager.SendCmd("所有组分" + "_设置PC波特率" + "_6_13", S1, 1, 200, copybyte(new byte[]{1}, cybszByte2));
                        }
                        break;
                    case R.id.spBaud2:
                        String[] strBaud3 = getResources().getStringArray(R.array.baud);
                        saveOperationLogPublicDataModifyMsg("公共", "MNTD3BAUD", String.valueOf(position), "波特率-" + strBaud3[position], ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("MNTD3BAUD", String.valueOf(position));
                        String[] strSelectBaud3 = strBaud3[position].split("bps");

                        byte[] cybszByte3 = copybyte(toByteArray(Integer.parseInt(strSelectBaud3[0]), 4));

                        if (IOBoardUsed) {
                            //数采仪设置波特率
                            SendManager.SendCmd("所有组分" + "_设置RS485波特率" + "_6_13", S1, 1, 200, copybyte(new byte[]{4}, cybszByte3));
                        }
                        break;
                    case R.id.spCOM1Xy:
                        saveOperationLogPublicDataModifyMsg("公共", "COM1_DIGITAL", String.valueOf(position), "COM1数字量-" + protocolList[position], ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("COM1_DIGITAL", String.valueOf(position));
                        break;
                    case R.id.spCOM1Baud:
                        if (!(Integer.parseInt(getPublicConfigData("COM1BAUD")) == position)) {
                            String[] strCom1Baud = getResources().getStringArray(R.array.baud);
                            saveOperationLogPublicDataModifyMsg("公共", "COM1BAUD", String.valueOf(position), "COM1波特率-" + strCom1Baud[position], ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("COM1BAUD", String.valueOf(position));
                            if (S2 != null) {
                                S2.close();
                                S2 = null;
                            }
                            InitPort(GetPhoneType());
                        }
                        break;
                    case R.id.spCOM3Xy:
                        saveOperationLogPublicDataModifyMsg("公共", "COM3_DIGITAL", String.valueOf(position), "COM3数字量-" + protocolList[position], ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("COM3_DIGITAL", String.valueOf(position));
                        break;
                    case R.id.spCOM3Baud:
                        int blIoBordEn = getIOBORD();
                        boolean blIoBordEnable = true;
                        if (blIoBordEn == 0) {
                            blIoBordEnable = false;
                        } else {
                            blIoBordEnable = true;
                        }
                        if (!blIoBordEnable) {
                            if (!(Integer.parseInt(getPublicConfigData("COM3BAUD")) == position)) {
                                String[] strCom1Baud = getResources().getStringArray(R.array.baud);
                                saveOperationLogPublicDataModifyMsg("公共", "COM3BAUD", String.valueOf(position), "COM3波特率-" + strCom1Baud[position], ErrorLog.msgType.操作_信息);
                                updatePublicConfigData("COM3BAUD", String.valueOf(position));
                                if (S1 != null) {
                                    S1.close();
                                    S1 = null;
                                }
                                InitPort(GetPhoneType());
                            }
                        }
                        break;
                }
                FullWindows(mActivityWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private void showIoBord() {

        LinearLayout mLayout_analog = (LinearLayout) v.findViewById(R.id.layout_analog);
        LinearLayout mLayoutIoPCBaudSet = (LinearLayout) v.findViewById(R.id.layout_IO_PC_bpsSet);
        LinearLayout mLayoutIoDCIBaudSet = (LinearLayout) v.findViewById(R.id.layout_DCI_baud);
        LinearLayout mLayoutIoDCI2BaudSet = (LinearLayout) v.findViewById(R.id.layout_DCI2_baud);

        if (getPublicConfigData("IO_BORD").equals("false") || getPublicConfigData("IO_BORD").equals("0")) {
            mLayout_analog.setVisibility(View.GONE);
            mLayoutIoPCBaudSet.setVisibility(View.GONE);
            mLayoutIoDCIBaudSet.setVisibility(View.GONE);
            mLayoutIoDCI2BaudSet.setVisibility(View.GONE);

        } else {
            mLayout_analog.setVisibility(View.VISIBLE);
            mLayoutIoPCBaudSet.setVisibility(View.VISIBLE);
            mLayoutIoDCIBaudSet.setVisibility(View.VISIBLE);
            mLayoutIoDCI2BaudSet.setVisibility(View.VISIBLE);
        }
    }

    public static int getIOBORD() {
        int blIoBordEn = 0;
        switch (getPublicConfigData("IO_BORD")) {
            case "1":
            case "true":
                blIoBordEn = 1;
                break;
            case "0":
            case "false":
                blIoBordEn = 0;
                break;
            case "2":
                blIoBordEn = 2;
                break;
        }
        return blIoBordEn;
    }
}
