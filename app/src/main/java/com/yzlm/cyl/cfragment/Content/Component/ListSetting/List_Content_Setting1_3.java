package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import weiqian.hardware.CustomFunctions;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setEditSystemiData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Global.InitPort;
import static com.yzlm.cyl.cfragment.Global.TCP1;
import static com.yzlm.cyl.cfragment.Global.TCP2;
import static com.yzlm.cyl.cfragment.Global.TCP3;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.protocolDisplayList;
import static com.yzlm.cyl.cfragment.Global.protocolList;
import static com.yzlm.cyl.cfragment.Global.serverProtocolList;

public class List_Content_Setting1_3 extends SubFragment {

    private static List_Content_Setting1_3 fragment = null;
    private ToggleButton tgBtnTCP1;
    private ToggleButton tgBtnTCP2;
    private ToggleButton tgBtnTCP3;
    private LinearLayout layXYXZ1;
    private LinearLayout layXYXZ2;
    private LinearLayout layXYXZ3;
    private EditText mEtIp1;
    private EditText mEtPort1;
    private EditText mEtIp2;
    private EditText mEtPort2;
    private EditText mEtIp3;
    private EditText mEtPort3;
    private LinearLayout layXY1;
    private LinearLayout layXY2;
    private LinearLayout layXY3;
    private Callbacks mCallbacks;


    private ToggleButton tgBtnServer1;
    private ToggleButton tgBtnServer2;
    private LinearLayout layServerXYPZ1;
    private LinearLayout layServerXYPZ2;
    private EditText mEtServerIp1;
    private EditText mEtServerIp2;
    private EditText mEtServerPort1;
    private EditText mEtServerPort2;
    private LinearLayout layXY4;
    private LinearLayout layXY5;


    public static List_Content_Setting1_3 newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Setting1_3();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogRS();

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
        return R.layout.list_content_setting1_3;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p3);
        mBtnReturn.setOnClickListener(new BtnClickListener());

        layXYXZ1 = (LinearLayout) v.findViewById(R.id.layout_XYXZ1);
        layXYXZ2 = (LinearLayout) v.findViewById(R.id.layout_XYXZ2);
        layXYXZ3 = (LinearLayout) v.findViewById(R.id.layout_XYXZ3);
        layServerXYPZ1 = (LinearLayout) v.findViewById(R.id.layout_Server_XYXZ1); //服务端布局
        layServerXYPZ2 = (LinearLayout) v.findViewById(R.id.layout_Server_XYXZ2); //服务端布局
        layXY1 = (LinearLayout) v.findViewById(R.id.layout_XY1);
        layXY2 = (LinearLayout) v.findViewById(R.id.layout_XY2);
        layXY3 = (LinearLayout) v.findViewById(R.id.layout_XY3);
        layXY4 = (LinearLayout) v.findViewById(R.id.layout_XY4); //服务端协议配置布局
        layXY5 = (LinearLayout) v.findViewById(R.id.layout_XY5); //服务端协议配置布局

        tgBtnTCP1 = (ToggleButton) v.findViewById(R.id.togTCP1);
        tgBtnTCP1.setOnCheckedChangeListener(new mTogBtnClick());
        tgBtnTCP1.setChecked(getPublicConfigData("TCP_1").equals("true"));

        tgBtnTCP2 = (ToggleButton) v.findViewById(R.id.togTCP2);
        tgBtnTCP2.setOnCheckedChangeListener(new mTogBtnClick());
        tgBtnTCP2.setChecked(getPublicConfigData("TCP_2").equals("true"));

        tgBtnTCP3 = (ToggleButton) v.findViewById(R.id.togTCP3);
        tgBtnTCP3.setOnCheckedChangeListener(new mTogBtnClick());
        tgBtnTCP3.setChecked(getPublicConfigData("TCP_3").equals("true"));


        //服务端配置
        tgBtnServer1 = (ToggleButton) v.findViewById(R.id.togServer1);
        tgBtnServer1.setOnCheckedChangeListener(new mTogBtnClick());
        tgBtnServer1.setChecked(getPublicConfigData("Server_1").equals("true"));

        tgBtnServer2 = (ToggleButton) v.findViewById(R.id.togServer2);
        tgBtnServer2.setOnCheckedChangeListener(new mTogBtnClick());
        tgBtnServer2.setChecked(getPublicConfigData("Server_2").equals("true"));

        if (getPublicConfigData("TCP_1_Protocol").equals("-1") || Integer.parseInt(getPublicConfigData("TCP_1_Protocol")) >= protocolList.length) {
            updatePublicConfigData("TCP_1_Protocol", String.valueOf(0));
        }
        if (getPublicConfigData("TCP_2_Protocol").equals("-1") || Integer.parseInt(getPublicConfigData("TCP_2_Protocol")) >= protocolList.length) {
            updatePublicConfigData("TCP_2_Protocol", String.valueOf(0));
        }
        if (getPublicConfigData("TCP_3_Protocol").equals("-1") || Integer.parseInt(getPublicConfigData("TCP_3_Protocol")) >= protocolList.length) {
            updatePublicConfigData("TCP_3_Protocol", String.valueOf(0));
        }

        //服务端配置
        if (getPublicConfigData("Server_1_Protocol").equals("-1") || Integer.parseInt(getPublicConfigData("Server_1_Protocol")) >= protocolList.length) {
            updatePublicConfigData("Server_1_Protocol", String.valueOf(0));
        }
        if (getPublicConfigData("Server_2_Protocol").equals("-1") || Integer.parseInt(getPublicConfigData("Server_2_Protocol")) >= protocolList.length) {
            updatePublicConfigData("Server_2_Protocol", String.valueOf(0));
        }


        Spinner mSpXY1 = (Spinner) v.findViewById(R.id.spXY1);
        InitSpinner(context, mSpXY1, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpXY1.setOnItemSelectedListener(new SpClickListener());
        mSpXY1.setSelection(Integer.parseInt(getPublicConfigData("TCP_1_Protocol")));



        Spinner mSpXY2 = (Spinner) v.findViewById(R.id.spXY2);
        InitSpinner(context, mSpXY2, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpXY2.setOnItemSelectedListener(new SpClickListener());
        mSpXY2.setSelection(Integer.parseInt(getPublicConfigData("TCP_2_Protocol")));

        Spinner mSpXY3 = (Spinner) v.findViewById(R.id.spXY3);
        InitSpinner(context, mSpXY3, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpXY3.setOnItemSelectedListener(new SpClickListener());
        mSpXY3.setSelection(Integer.parseInt(getPublicConfigData("TCP_3_Protocol")));

        //服务端配置
        Spinner mSpXY4 = (Spinner) v.findViewById(R.id.spXY4);
        InitSpinner(context, mSpXY4, serverProtocolList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpXY4.setOnItemSelectedListener(new SpClickListener());
        mSpXY4.setSelection(Integer.parseInt(getPublicConfigData("Server_1_Protocol")));

        Spinner mSpXY5 = (Spinner) v.findViewById(R.id.spXY5);
        InitSpinner(context, mSpXY5, serverProtocolList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpXY5.setOnItemSelectedListener(new SpClickListener());
        mSpXY5.setSelection(Integer.parseInt(getPublicConfigData("Server_2_Protocol")));

        mEtIp1 = (EditText) v.findViewById(R.id.eTIp1);
        mEtIp1.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtIp1.setOnEditorActionListener(new EditFocusChangeClick());
        mEtIp1.setText(getPublicConfigData("TCP_1_IP"));

        mEtPort1 = (EditText) v.findViewById(R.id.eTPort1);
        mEtPort1.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtPort1.setOnEditorActionListener(new EditFocusChangeClick());
        mEtPort1.setText(getPublicConfigData("TCP_1_PORT"));

        mEtIp2 = (EditText) v.findViewById(R.id.eTIp2);
        mEtIp2.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtIp2.setOnEditorActionListener(new EditFocusChangeClick());
        mEtIp2.setText(getPublicConfigData("TCP_2_IP"));

        mEtPort2 = (EditText) v.findViewById(R.id.eTPort2);
        mEtPort2.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtPort2.setOnEditorActionListener(new EditFocusChangeClick());
        mEtPort2.setText(getPublicConfigData("TCP_2_PORT"));

        mEtIp3 = (EditText) v.findViewById(R.id.eTIp3);
        mEtIp3.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtIp3.setOnEditorActionListener(new EditFocusChangeClick());
        mEtIp3.setText(getPublicConfigData("TCP_3_IP"));

        mEtPort3 = (EditText) v.findViewById(R.id.eTPort3);
        mEtPort3.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtPort3.setOnEditorActionListener(new EditFocusChangeClick());
        mEtPort3.setText(getPublicConfigData("TCP_3_PORT"));


        //服务端配置
        mEtServerIp1 = (EditText) v.findViewById(R.id.eTIp4);
        mEtServerIp1.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtServerIp1.setOnEditorActionListener(new EditFocusChangeClick());
        mEtServerIp1.setText(getPublicConfigData("Server_1_IP"));

        mEtServerPort1 = (EditText) v.findViewById(R.id.eTPort4);
        mEtServerPort1.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtServerPort1.setOnEditorActionListener(new EditFocusChangeClick());
        mEtServerPort1.setText(getPublicConfigData("Server_1_PORT"));

        mEtServerIp2 = (EditText) v.findViewById(R.id.eTIp5);
        mEtServerIp2.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtServerIp2.setOnEditorActionListener(new EditFocusChangeClick());
        mEtServerIp2.setText(getPublicConfigData("Server_2_IP"));

        mEtServerPort2 = (EditText) v.findViewById(R.id.eTPort5);
        mEtServerPort2.setOnFocusChangeListener(new EditFocusChangeClick());
        mEtServerPort2.setOnEditorActionListener(new EditFocusChangeClick());
        mEtServerPort2.setText(getPublicConfigData("Server_2_PORT"));

        if (tgBtnTCP1.isChecked()) {
            layXYXZ1.setVisibility(View.VISIBLE);
            layXY1.setVisibility(View.VISIBLE);
        } else {
            layXYXZ1.setVisibility(View.GONE);
            layXY1.setVisibility(View.GONE);
        }
        if (tgBtnTCP2.isChecked()) {
            layXYXZ2.setVisibility(View.VISIBLE);
            layXY2.setVisibility(View.VISIBLE);
        } else {
            layXYXZ2.setVisibility(View.GONE);
            layXY2.setVisibility(View.GONE);
        }
        if (tgBtnTCP3.isChecked()) {
            layXYXZ3.setVisibility(View.VISIBLE);
            layXY3.setVisibility(View.VISIBLE);
        } else {
            layXYXZ3.setVisibility(View.GONE);
            layXY3.setVisibility(View.GONE);
        }


        //服务端配置
        if (tgBtnServer1.isChecked()) {
            layServerXYPZ1.setVisibility(View.VISIBLE);
            layXY4.setVisibility(View.VISIBLE);
        } else {
            layServerXYPZ1.setVisibility(View.GONE);
            layXY4.setVisibility(View.GONE);
        }

        if (tgBtnServer2.isChecked()) {
            layServerXYPZ2.setVisibility(View.VISIBLE);
            layXY5.setVisibility(View.VISIBLE);
        } else {
            layServerXYPZ2.setVisibility(View.GONE);
            layXY5.setVisibility(View.GONE);
        }
    }

    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnReturn_p3:
                    mCallbacks.onListSelected(v);
                    break;
            }
        }
    }

    private class mTogBtnClick implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {

                case R.id.togTCP1: {
                    if (isChecked != getPublicConfigData("TCP_1").equals("true")) {
                        saveOperationLogPublicDataModifyMsg("公共", "TCP_1", String.valueOf(isChecked), "TCP_1", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("TCP_1", tgBtnTCP1.isChecked() ? "true" : "false");
                        if (tgBtnTCP1.isChecked()) {
                            layXYXZ1.setVisibility(View.VISIBLE);
                            layXY1.setVisibility(View.VISIBLE);
                        } else {
                            layXYXZ1.setVisibility(View.GONE);
                            layXY1.setVisibility(View.GONE);
                        }
                        InitPort(Communication.Port.TCP);
                    }
                }
                break;
                case R.id.togTCP2: {
                    if (isChecked != getPublicConfigData("TCP_2").equals("true")) {
                        saveOperationLogPublicDataModifyMsg("公共", "TCP_2", String.valueOf(isChecked), "TCP_2", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("TCP_2", tgBtnTCP2.isChecked() ? "true" : "false");
                        if (tgBtnTCP2.isChecked()) {
                            layXYXZ2.setVisibility(View.VISIBLE);
                            layXY2.setVisibility(View.VISIBLE);
                        } else {
                            layXYXZ2.setVisibility(View.GONE);
                            layXY2.setVisibility(View.GONE);
                        }
                        InitPort(Communication.Port.TCP);
                    }
                }
                break;
                case R.id.togTCP3: {
                    if (isChecked != getPublicConfigData("TCP_3").equals("true")) {
                        saveOperationLogPublicDataModifyMsg("公共", "TCP_3", String.valueOf(isChecked), "TCP_3", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("TCP_3", tgBtnTCP3.isChecked() ? "true" : "false");
                        if (tgBtnTCP3.isChecked()) {
                            layXYXZ3.setVisibility(View.VISIBLE);
                            layXY3.setVisibility(View.VISIBLE);
                        } else {
                            layXYXZ3.setVisibility(View.GONE);
                            layXY3.setVisibility(View.GONE);
                        }
                        InitPort(Communication.Port.TCP);
                    }
                }
                break;
                case R.id.togServer1: {

                    saveOperationLogPublicDataModifyMsg("公共", "Server_1", String.valueOf(isChecked), "Server_1", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("Server_1", tgBtnServer1.isChecked() ? "true" : "false");

                    if (tgBtnServer1.isChecked()) {
                        layServerXYPZ1.setVisibility(View.VISIBLE);
                        layXY4.setVisibility(View.VISIBLE);
                        InitPort(Communication.Port.TCPServer);
                    } else {
                        layServerXYPZ1.setVisibility(View.GONE);
                        layXY4.setVisibility(View.GONE);
                    }
                }
                break;
                case R.id.togServer2: {

                    saveOperationLogPublicDataModifyMsg("公共", "Server_2", String.valueOf(isChecked), "Server_2", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("Server_2", tgBtnServer2.isChecked() ? "true" : "false");

                    if (tgBtnServer2.isChecked()) {
                        layServerXYPZ2.setVisibility(View.VISIBLE);
                        layXY5.setVisibility(View.VISIBLE);
                        InitPort(Communication.Port.TCPServer);
                    } else {
                        layServerXYPZ2.setVisibility(View.GONE);
                        layXY5.setVisibility(View.GONE);
                    }
                }
                break;
            }

        }
    }

    private class SpClickListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {
                case R.id.spXY1:
                    saveOperationLogPublicDataModifyMsg("公共", "TCP_1_Protocol", String.valueOf(position), "TCP_1_Protocol", ErrorLog.msgType.操作_信息);
                    if (!getPublicConfigData("TCP_1_Protocol").equals(String.valueOf(position))) {
                        updatePublicConfigData("TCP_1_Protocol", String.valueOf(position));
                    }
                    break;
                case R.id.spXY2:
                    saveOperationLogPublicDataModifyMsg("公共", "TCP_2_Protocol", String.valueOf(position), "TCP_2_Protocol", ErrorLog.msgType.操作_信息);
                    if (!getPublicConfigData("TCP_2_Protocol").equals(String.valueOf(position))) {
                        updatePublicConfigData("TCP_2_Protocol", String.valueOf(position));
                    }
                    break;
                case R.id.spXY3:
                    saveOperationLogPublicDataModifyMsg("公共", "TCP_3_Protocol", String.valueOf(position), "TCP_3_Protocol", ErrorLog.msgType.操作_信息);
                    if (!getPublicConfigData("TCP_3_Protocol").equals(String.valueOf(position))) {
                        updatePublicConfigData("TCP_3_Protocol", String.valueOf(position));
                    }
                    break;
                case R.id.spXY4:
                    saveOperationLogPublicDataModifyMsg("公共", "Server_1_Protocol", String.valueOf(position), "Server_1_Protocol", ErrorLog.msgType.操作_信息);
                    if (!getPublicConfigData("Server_1_Protocol").equals(String.valueOf(position))) {
                        updatePublicConfigData("Server_1_Protocol", String.valueOf(position));
                    }
                    break;
                case R.id.spXY5:
                    saveOperationLogPublicDataModifyMsg("公共", "Server_2_Protocol", String.valueOf(position), "Server_2_Protocol", ErrorLog.msgType.操作_信息);
                    if (!getPublicConfigData("Server_2_Protocol").equals(String.valueOf(position))) {
                        updatePublicConfigData("Server_2_Protocol", String.valueOf(position));
                    }
                    break;
            }
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class EditFocusChangeClick implements View.OnFocusChangeListener, TextView.OnEditorActionListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTIp1:
                        if (!getPublicConfigData("TCP_1_IP").equals(mEtIp1.getText().toString())) {

                            saveOperationLogPublicDataModifyMsg("公共", "TCP_1_IP", mEtIp1.getText().toString(), "TCP_1_IP", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("TCP_1_IP", mEtIp1.getText().toString());
                            if (TCP1 != null) {
                                TCP1.close();
                                TCP1 = null;
                            }
                            InitPort(Communication.Port.TCP);
                        }
                        break;
                    case R.id.eTPort1:
                        if (!getPublicConfigData("TCP_1_PORT").equals(mEtPort1.getText().toString())) {
                            setEditSystemiData("TCP_1_PORT", (EditText) v, 0, 1, 99999, 7777, "TCP_1_PORT");

                            if (TCP1 != null) {
                                TCP1.close();
                                TCP1 = null;
                            }
                            InitPort(Communication.Port.TCP);
                        }
                        break;
                    case R.id.eTIp2:
                        if (!getPublicConfigData("TCP_2_IP").equals(mEtIp2.getText().toString())) {
                            saveOperationLogPublicDataModifyMsg("公共", "TCP_2_IP", mEtIp2.getText().toString(), "TCP_2_IP", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("TCP_2_IP", mEtIp2.getText().toString());
                            if (TCP2 != null) {
                                TCP2.close();
                                TCP2 = null;
                            }
                            InitPort(Communication.Port.TCP);
                        }
                        break;
                    case R.id.eTPort2:
                        if (!getPublicConfigData("TCP_2_PORT").equals(mEtPort2.getText().toString())) {
                            setEditSystemiData("TCP_2_PORT", (EditText) v, 0, 1, 99999, 7777, "TCP_2_PORT");

                            if (TCP2 != null) {
                                TCP2.close();
                                TCP2 = null;
                            }
                            InitPort(Communication.Port.TCP);
                        }
                        break;
                    case R.id.eTIp3:
                        if (!getPublicConfigData("TCP_3_IP").equals(mEtIp3.getText().toString())) {
                            saveOperationLogPublicDataModifyMsg("公共", "TCP_3_IP", mEtIp3.getText().toString(), "TCP_3_IP", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("TCP_3_IP", mEtIp3.getText().toString());
                            if (TCP3 != null) {
                                TCP3.close();
                                TCP3 = null;
                            }
                            InitPort(Communication.Port.TCP);
                        }
                        break;
                    case R.id.eTPort3:
                        if (!getPublicConfigData("TCP_3_PORT").equals(mEtPort3.getText().toString())) {
                            setEditSystemiData("TCP_3_PORT", (EditText) v, 0, 1, 99999, 7777, "TCP_3_PORT");

                            if (TCP3 != null) {
                                TCP3.close();
                                TCP3 = null;
                            }
                            InitPort(Communication.Port.TCP);
                        }
                        break;

                    case R.id.eTIp4://服务端1IP配置
                        if (!getPublicConfigData("Server_1_IP").equals(mEtServerIp1.getText().toString())) {
                            saveOperationLogPublicDataModifyMsg("公共", "Server_1_IP", mEtServerIp1.getText().toString(), "Server_1_IP", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("Server_1_IP", mEtServerIp1.getText().toString());

                            //配置静态IP
                            CustomFunctions.UseStaticIp(context.getContentResolver(),
                                    mEtServerIp1.getText().toString().trim(),
                                    "255.255.255.0",
                                    "192.168.1.1",
                                    "8.8.8.8",
                                    "8.8.4.4");

                            InitPort(Communication.Port.TCPServer);
                        }
                        break;
                    case R.id.eTPort4://服务端1Port配置
                        if (!getPublicConfigData("Server_1_PORT").equals(mEtServerPort1.getText().toString())) {
                            setEditSystemiData("Server_1_PORT", (EditText) v, 0, 1, 99999, 7777, "Server_1_PORT");
                            InitPort(Communication.Port.TCPServer);
                        }
                        break;

                    case R.id.eTIp5://服务端2IP配置
                        if (!getPublicConfigData("Server_2_IP").equals(mEtServerIp2.getText().toString())) {
                            saveOperationLogPublicDataModifyMsg("公共", "Server_2_IP", mEtServerIp2.getText().toString(), "Server_2_IP", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("Server_2_IP", mEtServerIp2.getText().toString());

                            //配置静态IP
                            CustomFunctions.UseStaticIp(context.getContentResolver(),
                                    mEtServerIp2.getText().toString().trim(),
                                    "255.255.255.0",
                                    "192.168.1.1",
                                    "8.8.8.8",
                                    "8.8.4.4");

                            InitPort(Communication.Port.TCPServer);
                        }
                        break;
                    case R.id.eTPort5://服务端2Port配置
                        if (!getPublicConfigData("Server_2_PORT").equals(mEtServerPort2.getText().toString())) {
                            setEditSystemiData("Server_2_PORT", (EditText) v, 0, 1, 99999, 7777, "Server_2_PORT");
                            InitPort(Communication.Port.TCPServer);
                        }
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

}
