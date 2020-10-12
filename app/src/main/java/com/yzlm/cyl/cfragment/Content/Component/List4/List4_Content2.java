package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017_V2;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.HeNan.HeNan;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.JiangSu;
import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.HeBei.HeBei.packageMeaDataUpdate;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu.RtuProtocol.ModbusSend;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.nanshui.sendJiangSuProtocol;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getLastValue;
import static com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1_2.getIOBORD;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.floatToBytesBigs;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.AnalogQuantity_CycleThread;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.S2;
import static com.yzlm.cyl.cfragment.Global.TCP1;
import static com.yzlm.cyl.cfragment.Global.TCP2;
import static com.yzlm.cyl.cfragment.Global.TCP3;
import static com.yzlm.cyl.cfragment.Global.channel1Val;
import static com.yzlm.cyl.cfragment.Global.channel2Val;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.digitalTest;
import static com.yzlm.cyl.cfragment.Global.floatToBytes;
import static com.yzlm.cyl.cfragment.Global.protocolDisplayList;
import static com.yzlm.cyl.cfragment.Global.protocolList;
import static com.yzlm.cyl.cfragment.Global.protocolName;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content2 extends SubFragment {
    private static Spinner mSpXY;
    private static List4_Content2 fragment = null;
    private static Button btnIOTest;
    private static Button btnChannel;
    private static Button btnReadSerial;
    private static EditText etTestVal;
    private static EditText metMeaData;
    private static TextView mtxReceiveTest;
    private static TextView mtxtSendMsg;
    private float lastVal1 = 4, lastVal2 = 4;


    private static Button btnDigitalChannel;
    private static Button mBtNoIo;

    private static boolean txtColor = false;

    private int iTcpChannel = 0;
    private static Button btnDigitalType;
    private static Button btnTCPChanel;
    private LinearLayout mLayoutUartTestCfg;
    private LinearLayout mLayoutTcpTestCfg;
    private static Button mBtnReFormat;// hex  ascii


    private static String[] tcpChannelSum = context.getResources().getStringArray(R.array.TcpChannelSum);

    private static List<String> tcpUseChannel = new ArrayList<>();

    public static List4_Content2 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content2();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list4_content2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            digitalTest = false;
            mBtNoIo = (Button) v.findViewById(R.id.btNoIo);
            btnDigitalChannel = (Button) v.findViewById(R.id.btnSCBY);
            if (!IOBoardUsed) {
                TextView mTxtAnalogTestName = (TextView) v.findViewById(R.id.txt_analog_test_name);
                LinearLayout mLayoutAnalogTest = (LinearLayout) v.findViewById(R.id.layout_analog_test);
                mTxtAnalogTestName.setVisibility(View.INVISIBLE);
                mLayoutAnalogTest.setVisibility(View.GONE);
                mBtNoIo.setVisibility(View.VISIBLE);
                btnDigitalChannel.setVisibility(View.GONE);
            } else {
                mBtNoIo.setVisibility(View.GONE);
                btnDigitalChannel.setVisibility(View.VISIBLE);
                int blIoBordEn = getIOBORD();
                if (blIoBordEn == 2) {
                    btnDigitalChannel.setText("RS232");
                } else if (blIoBordEn == 1) {
                    btnDigitalChannel.setText(getString(R.string.forInstrument));
                }
            }
            mBtNoIo.setOnClickListener(new btnBtnClick());
            mSpXY = (Spinner) v.findViewById(R.id.spXY);
            InitSpinner(context, mSpXY, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);

            mSpXY.setOnItemSelectedListener(new mSpProtocolListener());

            btnIOTest = (Button) v.findViewById(R.id.btnIOTest);
            btnIOTest.setOnClickListener(new btnIOTestClick());
            btnChannel = (Button) v.findViewById(R.id.btnTD12);
            btnChannel.setOnClickListener(new btnChannelClick());
            Button btnAnalogOK = (Button) v.findViewById(R.id.btnAnalogTest);
            btnAnalogOK.setOnClickListener(new btnAnalogOKClick());


            etTestVal = (EditText) v.findViewById(R.id.eTmnl);
            etTestVal.setOnFocusChangeListener(new editFocusChangeComp());
            etTestVal.setOnEditorActionListener(new editFocusChangeComp());
            mtxReceiveTest = (TextView) v.findViewById(R.id.txReceTest);
            mtxReceiveTest.setMovementMethod(new ScrollingMovementMethod());
            btnReadSerial = (Button) v.findViewById(R.id.btnKSJS);
            btnReadSerial.setOnClickListener(new btnBtnClick());

            Button btnSend = (Button) v.findViewById(R.id.btnSend);
            btnSend.setOnClickListener(new btnSendClick());

            mtxtSendMsg = (TextView) v.findViewById(R.id.txtSendMsg);
            mtxtSendMsg.setMovementMethod(new ScrollingMovementMethod());

            metMeaData = (EditText) v.findViewById(R.id.eTclz);
            metMeaData.setOnFocusChangeListener(new EditorClickListener());
            metMeaData.setOnEditorActionListener(new EditorClickListener());


            btnDigitalChannel.setOnClickListener(new btnBtnClick());

            btnDigitalType = (Button) v.findViewById(R.id.btnType);
            btnDigitalType.setOnClickListener(new btnBtnClick());
            btnTCPChanel = (Button) v.findViewById(R.id.btnTypeChannel);
            btnTCPChanel.setOnClickListener(new btnBtnClick());

            mLayoutUartTestCfg = (LinearLayout) v.findViewById(R.id.layout_uartTestCfg);
            mLayoutTcpTestCfg = (LinearLayout) v.findViewById(R.id.layout_TcpTestCfg);

            boolean blTCP1Enable = getPublicConfigData("TCP_1").equals("true");
            boolean blTCP2Enable = getPublicConfigData("TCP_2").equals("true");
            boolean blTCP3Enable = getPublicConfigData("TCP_3").equals("true");
            tcpUseChannel.clear();
            if (blTCP1Enable) {
                tcpUseChannel.add(tcpChannelSum[0]);
            }
            if (blTCP2Enable) {
                tcpUseChannel.add(tcpChannelSum[1]);
            }
            if (blTCP3Enable) {
                tcpUseChannel.add(tcpChannelSum[2]);
            }

            if (blTCP1Enable || blTCP2Enable || blTCP3Enable) {
                btnDigitalType.setEnabled(true);
                if (tcpUseChannel.size() > 0) {
                    btnTCPChanel.setText(tcpUseChannel.get(0));
                }
            } else {
                btnDigitalType.setEnabled(false);
            }

            if (getPublicConfigData("MNTD1").equals("-1") || Integer.parseInt(getPublicConfigData("MNTD1")) >= strComponent.get(1).length) {
                lastVal1 = 4;
            } else {
                String comp1 = strComponent.get(1)[Integer.parseInt(getPublicConfigData("MNTD1"))];

                String RangeLow = getConfigData(mCompName, "YBLCL");
                String Range = getConfigData(mCompName, "YBLCH");
                float fullRangeLow = Float.parseFloat(RangeLow);
                float fullRange1 = Float.parseFloat(Range);

                String flow = null;
                if (!getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                    flow = getString(R.string.ZY);
                }

                lastVal1 = Float.parseFloat(getLastValue(comp1, flow, "mg/L"));
                lastVal1 = ((lastVal1 - fullRangeLow) / (fullRange1 - fullRangeLow)) * 16 + 4;
            }
            if (getPublicConfigData("MNTD2").equals("-1") || Integer.parseInt(getPublicConfigData("MNTD2")) >= strComponent.get(1).length) {
                lastVal2 = 4;
            } else {
                String comp2 = strComponent.get(1)[Integer.parseInt(getPublicConfigData("MNTD2"))];

                String RangeLow2 = getConfigData(mCompName, "YBLCL");
                String Range2 = getConfigData(mCompName, "YBLCH");
                float fullRangeLow2 = Float.parseFloat(RangeLow2);
                float fullRange2 = Float.parseFloat(Range2);

                String flow2 = null;
                if (!getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                    flow2 = getString(R.string.ZY);
                }

                lastVal2 = Float.parseFloat(getLastValue(comp2, flow2, "mg/L"));
                lastVal2 = ((lastVal2 - fullRangeLow2) / (fullRange2 - fullRangeLow2)) * 16 + 4;
            }

            mBtnReFormat = (Button) v.findViewById(R.id.btReHexAscii);
            mBtnReFormat.setOnClickListener(new btnBtnClick());

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class EditorClickListener implements View.OnFocusChangeListener, TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
            }
            return false;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    String strValue = editDataFormat(metMeaData.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    metMeaData.setText(strValue);
                } catch (Exception e) {

                }

                syncList();
            }
        }
    }

    private class btnBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnKSJS:
                    if (btnReadSerial.getText().equals(getString(R.string.startReceive))) {
                        saveOperationLogMsg(mCompName, "数字量测试-开始接收", ErrorLog.msgType.操作_信息);
                        btnReadSerial.setText(getString(R.string.stopReceive));
                        mtxtSendMsg.setText("");
                        mtxReceiveTest.setText("");
                        digitalTest = true;
                    } else {
                        saveOperationLogMsg(mCompName, "数字量测试-停止接收", ErrorLog.msgType.操作_信息);
                        btnReadSerial.setText(getString(R.string.startReceive));
                        mtxtSendMsg.setText(getString(R.string.sendingArea));
                        mtxReceiveTest.setText(getString(R.string.receivingArea));
                        digitalTest = false;
                    }
                    break;
                case R.id.btnSCBY:
                    int blIoBordEn = getIOBORD();
                    if (blIoBordEn == 2) {
                        if (btnDigitalChannel.getText().equals("RS232")) {
                            btnDigitalChannel.setText("RS485");
                        } else if (btnDigitalChannel.getText().equals("RS485")) {
                            btnDigitalChannel.setText("PC");
                        } else {
                            btnDigitalChannel.setText("RS232");
                        }
                    } else if (blIoBordEn == 1) {
                        if (btnDigitalChannel.getText().equals(getString(R.string.forInstrument))) {
                            btnDigitalChannel.setText("PC");
                        } else {
                            btnDigitalChannel.setText(getString(R.string.forInstrument));
                        }
                    }
                    break;
                case R.id.btnType:
                    if (btnDigitalType.getText().equals(getString(R.string.uart))) {
                        btnDigitalType.setText(getString(R.string.TCP));
                        mLayoutTcpTestCfg.setVisibility(View.VISIBLE);
                        mLayoutUartTestCfg.setVisibility(View.GONE);
                    } else {
                        btnDigitalType.setText(getString(R.string.uart));
                        mLayoutUartTestCfg.setVisibility(View.VISIBLE);
                        mLayoutTcpTestCfg.setVisibility(View.GONE);
                    }
                    break;
                case R.id.btnTypeChannel:
                    iTcpChannel++;
                    if (iTcpChannel >= tcpUseChannel.size()) {
                        iTcpChannel = 0;
                    }
                    btnTCPChanel.setText(tcpUseChannel.get(iTcpChannel));
                    break;
                case R.id.btNoIo:
                    if (mBtNoIo.getText().equals(getString(R.string.outPut1))) {
                        mBtNoIo.setText(getString(R.string.outPut2));
                    } else {
                        mBtNoIo.setText(getString(R.string.outPut1));
                    }
                    break;
                case R.id.btReHexAscii:
                    if (mBtnReFormat.getText().equals(getString(R.string.hex))) {
                        mBtnReFormat.setText(getString(R.string.ascii));
                    } else {
                        mBtnReFormat.setText(getString(R.string.hex));
                    }
                    break;
            }
        }
    }

    private class btnIOTestClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String IOTestStr = btnIOTest.getText().toString().trim();
            if (IOTestStr.equals(getString(R.string.outputTest))) {
                btnIOTest.setText(getString(R.string.inputTest));
                byte[] arrayOfByte = shortToByte((short) 1);
                reverse(arrayOfByte);
                SendManager.SendCmd(mCompName + "_读取4-20mA输入" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S1, 3, 500, 8);
            } else {
                btnIOTest.setText(getString(R.string.outputTest));
            }
        }
    }

    private class btnChannelClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String channelStr = btnChannel.getText().toString().trim();
            if (channelStr.equals(getString(R.string.channel) + "1")) {
                btnChannel.setText(getString(R.string.channel) + "2");
            } else {
                btnChannel.setText(getString(R.string.channel) + "1");
            }
        }
    }

    private class btnAnalogOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            AnalogQuantity_CycleThread.suspend();
            String IOTestStr = btnIOTest.getText().toString().trim();
            String channelStr = btnChannel.getText().toString().trim();
            if (IOTestStr.equals(getString(R.string.outputTest))) {
                float lastVal = 0;
                try {
                    lastVal = Float.parseFloat(etTestVal.getText().toString().trim());
                    if (lastVal <= 4) {
                        lastVal = 4;
                    } else if (lastVal >= 20) {
                        lastVal = 20;
                    } else {
                    }
                    if (channelStr.equals(getString(R.string.channel) + "1")) {
                        lastVal1 = lastVal;
                    } else {
                        lastVal2 = lastVal;
                    }
                    etTestVal.setText(String.valueOf(lastVal));
                } catch (NumberFormatException e) {
                    Message msg = new Message();
                    msg.what = 200;
                    commTestHandler.sendMessage(msg);
                }
                byte[] analogOutByte = copybyte(floatToBytes(lastVal1), floatToBytes(lastVal2));
                SendManager.SendCmd("所有组分" + "_设置4-20mA输出" + "_6_0", S1, 3, 200, analogOutByte);
                saveOperationLogMsg(mCompName, "模拟量测试-输出1=" + lastVal1 + "输出2=" + lastVal2, ErrorLog.msgType.操作_信息);
            } else {
                byte[] arrayOfByte = shortToByte((short) 1);
                reverse(arrayOfByte);
                SendManager.SendCmd(mCompName + "_读取4-20mA输入" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S1, 3, 300, 8);
                if (channelStr.equals(getString(R.string.channel) + "1")) {
                    Message msg = new Message();
                    msg.what = 1;
                    commTestHandler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 2;
                    commTestHandler.sendMessage(msg);
                }
            }
            FullWindows(mActivityWindow);
        }
    }

    class btnSendClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Communication port;
            try {
                if (btnDigitalType.getText().equals(getString(R.string.TCP))) {
                    switch (iTcpChannel) {
                        case 0:
                            port = TCP1;
                            break;
                        case 1:
                            port = TCP2;
                            break;
                        case 2:
                            port = TCP3;
                            break;
                        default:
                            return;
                    }
                } else {
                    if (IOBoardUsed) {
                        port = S1;
                    } else {
                        if ((mBtNoIo.getText().equals(getString(R.string.outPut2)))) {
                            port = S1;
                        } else {
                            port = S2;
                        }
                    }
                }
                protocolTest(port);
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void protocolTest(Communication port) {
        if (metMeaData.getText().toString().equals("")) {
            return;
        }
        String strSendMsg = "";
        DataStruct data = getTestDataStruct();
        String strTestProtocol = protocolList[mSpXY.getSelectedItemPosition()];
        int sCom = btnDigitalChannel.getText().equals("RS485") ? 3 : ((btnDigitalChannel.getText().equals("RS232") || btnDigitalChannel.getText().equals(getString(R.string.forInstrument))) ? 1 : 2);
        if ((strTestProtocol.equals(protocolName[0])) || (strTestProtocol.equals(protocolName[1]))) {

            if ((strTestProtocol.equals(protocolName[0]))) {
                strSendMsg = new GB2005().getMeaDataUpdateCmd(data, false);
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "国标浓度上传_09_" + (btnDigitalChannel.getText().equals("RS485") ? "08" : ((btnDigitalChannel.getText().equals("RS232") || btnDigitalChannel.getText().equals(getString(R.string.forInstrument))) ? "07" : "09"))), port, 1, 1000, strSendMsg.getBytes());
            } else {
                strSendMsg = new GB2017().getMeaDataUpdateCmd2017(data, false, (byte) 0);
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "国标浓度上传_09_" + (btnDigitalChannel.getText().equals("RS485") ? "08" : ((btnDigitalChannel.getText().equals("RS232") || btnDigitalChannel.getText().equals(getString(R.string.forInstrument))) ? "07" : "09"))), port, 1, 2000, strSendMsg.getBytes());
            }

        } else if ((strTestProtocol.equals(protocolName[3]))) {
            float fData = Float.parseFloat(metMeaData.getText().toString());
            strSendMsg = sendJiangSuProtocol(strComponent.get(1)[0], port, sCom, Byte.parseByte(getPublicConfigData(btnDigitalChannel.getText().equals("RS485") ? "RS485_DIGITAL_ADDR" : ((btnDigitalChannel.getText().equals("RS232") || btnDigitalChannel.getText().equals(getString(R.string.forInstrument))) ? "DIGITAL_ADDR" : "PC_DIGITAL_ADDR"))), (byte) 4, fData);
        } else if (((strTestProtocol.equals(protocolName[5])))) {
            strSendMsg = new JiangSu().getMeaDataUpdateCmdJiangSu(data, false);
            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "国标浓度上传_09_" + (btnDigitalChannel.getText().equals("RS485") ? "08" : ((btnDigitalChannel.getText().equals("RS232") || btnDigitalChannel.getText().equals(getString(R.string.forInstrument))) ? "07" : "09"))), port, 1, 1000, strSendMsg.getBytes());
        } else if (strTestProtocol.equals(protocolName[6])) {
            strSendMsg = packageMeaDataUpdate(port, sCom, data, false);
        } else if (strTestProtocol.equals(protocolName[7])) {
            strSendMsg = new GB2017_V2().testProtocolUpLoading(port, sCom, data);
        } else if (strTestProtocol.equals(protocolName[8])) {
            strSendMsg = new HeNan().testProtocolUpLoading(port, sCom, data);
        } else {
            byte[] dataBytes;
            float fData = Float.parseFloat(metMeaData.getText().toString());
            dataBytes = copybyte(floatToBytesBigs(fData));
            System.arraycopy(dataBytes, 0, dataBytes, 0, dataBytes.length);
            //protocolName[2]
            //默认地址；根据串口号定义
            String strAddress = getPublicConfigData(btnDigitalChannel.getText().equals("RS485") ? "RS485_DIGITAL_ADDR" : ((btnDigitalChannel.getText().equals("RS232") || btnDigitalChannel.getText().equals(getString(R.string.forInstrument))) ? "DIGITAL_ADDR" : "PC_DIGITAL_ADDR"));
            int iAddress = strAddress.equals("") ? 1 : Integer.parseInt(strAddress);

             if (strTestProtocol.equals(protocolName[4]) || strTestProtocol.equals(protocolName[10])) {
                if (!getConfigData(mCompName, "RTU_ID").equals("")) {
                    iAddress = Integer.parseInt(getConfigData(mCompName, "RTU_ID"));
                } else {
                    Toast.makeText(getContext(), getString(R.string.please_Configure) + "RTU Id!", Toast.LENGTH_SHORT).show();
                }
            } else if (strTestProtocol.equals(protocolName[11])) {
                if (!getConfigData(mCompName, "GZ_RTU_ID").equals("")) {
                    iAddress = Integer.parseInt(getConfigData(mCompName, "GZ_RTU_ID"));
                } else {
                    Toast.makeText(getContext(), getString(R.string.please_Configure) + "RTU Id!", Toast.LENGTH_SHORT).show();
                }
            } else if (strTestProtocol.equals(protocolName[14])) {
                if (!getConfigData(mCompName, "W300_RTU_ID").equals("")) {
                    iAddress = Integer.parseInt(getConfigData(mCompName, "W300_RTU_ID"));
                } else {
                    Toast.makeText(getContext(), getString(R.string.please_Configure) + "RTU Id!", Toast.LENGTH_SHORT).show();
                }
            } else if (strTestProtocol.equals(protocolName[15])) {
                if (!getConfigData(mCompName, "SiChuan_RTU_ID").equals("")) {
                    iAddress = Integer.parseInt(getConfigData(mCompName, "SiChuan_RTU_ID"));
                } else {
                    Toast.makeText(getContext(), getString(R.string.please_Configure) + "RTU Id!", Toast.LENGTH_SHORT).show();
                }
            }
            strSendMsg = ModbusSend(port, sCom, (byte) iAddress, dataBytes);
        }
        mtxtSendMsg.setText(strSendMsg);
        saveOperationLogMsg(mCompName, strTestProtocol + "协议测试-发送", ErrorLog.msgType.操作_信息);
    }

    @NonNull
    private DataStruct getTestDataStruct() {
        DataStruct data = new DataStruct();
        data.setData(Float.parseFloat(metMeaData.getText().toString()));

        String[] time = getFormatSystemTime().split("/");
        int year = Integer.parseInt(time[0]);
        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[2]);
        int hour = Integer.parseInt(time[3]);
        int min = Integer.parseInt(time[4]);
        int sec = Integer.parseInt(time[5]);
        data.setDataTime(year, month, day, hour, min, sec);
        data.setAi(0);
        data.setDataTag("M");
        data.setFlow(context.getResources().getString(R.string.ZY));
        data.setType(mCompName);
        return data;
    }

    private static String getShowDataFormat() {

        if (mBtnReFormat.getText().toString().equals(context.getResources().getString(R.string.hex))) {
            return "hex";
        }
        return "ascii";
    }

    private static int getCommunicationChannel() {
        if (btnDigitalType.getText().equals(context.getResources().getString(R.string.uart))) {
            if (IOBoardUsed) {
                if (btnDigitalChannel.getText().toString().equals("RS232") || btnDigitalChannel.getText().toString().equals(context.getString(R.string.forInstrument))) {
                    return 3;
                } else if (btnDigitalChannel.getText().toString().equals("PC")) {
                    return 4;
                } else if (btnDigitalChannel.getText().toString().equals("RS485")) {
                    return 10;
                }
            } else {
                if (mBtNoIo.getText().toString().equals(context.getResources().getString(R.string.outPut1))) {
                    return 8;

                } else if (mBtNoIo.getText().toString().equals(context.getResources().getString(R.string.outPut2))) {
                    return 9;
                }
            }
        } else if (btnDigitalType.getText().equals(context.getResources().getString(R.string.TCP))) {
            if (btnTCPChanel.getText().toString().equals(tcpChannelSum[0])) {
                return 5;
            } else if (btnTCPChanel.getText().toString().equals(tcpChannelSum[1])) {
                return 6;
            } else if (btnTCPChanel.getText().toString().equals(tcpChannelSum[2])) {
                return 7;
            }
        }
        return 0;
    }

    private class mSpProtocolListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class editFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    if (etTestVal.getText().toString().equals("")) {
                        etTestVal.setText(String.valueOf(4));
                    }
                    if (Float.parseFloat(etTestVal.getText().toString()) < 4) {
                        etTestVal.setText(String.valueOf(4));
                    }
                    if (Float.parseFloat(etTestVal.getText().toString()) > 20) {
                        etTestVal.setText(String.valueOf(20));
                    }
                    String strValue = editDataFormat(etTestVal.getText().toString(), Integer.parseInt(getConfigData(mCompName, "YXWS")));
                    etTestVal.setText(strValue);
                } catch (Exception e) {
                    e.printStackTrace();
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

    private static String getFormatSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        return sdf.format(new Date());
    }


    @SuppressLint("HandlerLeak")
    public static Handler commTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    String editVal = String.valueOf(channel1Val);
                    etTestVal.setText(editVal);
                }
                break;
                case 2: {
                    String editVal = String.valueOf(channel2Val);
                    etTestVal.setText(editVal);
                }
                break;
                case 200: {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getResources().getString(R.string.sorry_input_data_not_normative));
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                }

                break;
                case 3:/*数采仪*/
                case 4:/*PC*/
                case 5://TCP show
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    if (mtxReceiveTest != null) {
                        String str = "";
                        Bundle bundle = msg.getData();
                        byte[] rdata = bundle.getByteArray("IOReceiveData");
                        byte[] reSdata = new byte[0];
                        boolean blShow = false;

                        if (rdata != null) {
                            str = new String(rdata);
                            reSdata = new byte[rdata.length];
                            System.arraycopy(rdata, 0, reSdata, 0, rdata.length);
                        }
                        int iChannel = getCommunicationChannel();

                        if (btnDigitalType.getText().equals(context.getString(R.string.TCP))) {
                            if (iChannel == 5 && msg.what == 5) {
                                blShow = true;
                            } else if (iChannel == 6 && msg.what == 6) {
                                blShow = true;
                            } else if (iChannel == 7 && msg.what == 7) {
                                blShow = true;
                            }
                        } else {
                            if (IOBoardUsed) {
                                if ((iChannel == 3 && msg.what == 3) || (iChannel == 4 && msg.what == 4) || (iChannel == 10 && msg.what == 10)) {
                                    blShow = true;
                                    if (rdata != null && rdata.length > 8) {
                                        reSdata = new byte[rdata.length - 8];
                                        System.arraycopy(rdata, 6, reSdata, 0, rdata.length - 8);
                                        str = new String(reSdata);
                                        blShow = true;
                                    }
                                }
                            } else {
                                if (msg.what == 8 && iChannel == 8) {
                                    blShow = true;
                                } else if (msg.what == 9 && iChannel == 9) {
                                    blShow = true;
                                }
                            }
                        }
                        if (blShow) {
                            if (getShowDataFormat().equals("ascii")) {
                                if (txtColor) {
                                    txtColor = false;
                                    mtxReceiveTest.setTextColor(Color.WHITE);
                                } else {
                                    txtColor = true;
                                    mtxReceiveTest.setTextColor(Color.GRAY);
                                }
                                mtxReceiveTest.setText(str);
                            } else {
                                if (txtColor) {
                                    txtColor = false;
                                    mtxReceiveTest.setTextColor(Color.GRAY);

                                } else {
                                    txtColor = true;
                                    mtxReceiveTest.setTextColor(Color.WHITE);
                                }
                                mtxReceiveTest.setText(bytesToHexString(reSdata, reSdata.length));
                            }
                        }
                    }
                    break;
            }
        }
    };
}


