package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Adapter.RtuIdCfgAdapter;
import com.yzlm.cyl.cfragment.Adapter.RtuIdInfo;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Adapter.RtuIdCfgAdapter.setIDConflicting;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1_2.getIOBORD;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setEditSystemData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.stringFilter;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.protocolDisplayList;
import static com.yzlm.cyl.cfragment.Global.protocolList;
import static com.yzlm.cyl.cfragment.Global.protocolName;
import static com.yzlm.cyl.cfragment.Global.strComponent;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List_Content_Setting1_4 extends SubFragment {


    private Spinner mSpXYCfgSelect;
    private Spinner mSpGB2005CRC;
    private Spinner mSpGB2017CRC;
    private Spinner mSpGB2017v2CRC;
    private Spinner mSHeNanCRC;
    private Spinner mSpAnalysisMode;
    private Spinner mSpCrcMode;

    private EditText mEdDigitalAddr;
    private EditText mEPCdDigitalAddr;
    private EditText mEdDigital2Addr;
    private EditText mEdOrg;
    private EditText mEdDevAddr;
    private EditText mEd2017Org;
    private EditText mEd2017DevAddr;
    private EditText mEdJSOrg;
    private EditText mEdJSDevAddr;
    private EditText mEdDevSN;
    private EditText mEdSysRtuId;
    private EditText mEd2017OrgV2;
    private EditText mEd2017DevAddrV2;
    private ToggleButton btnAbsorbUpdate;
    private ToggleButton btnCalUpdate;
    private TextView txtvYX1;


    private List<RtuIdInfo> mListLocality = new ArrayList<>();  //声明一个list，动态存储要显示的信息
    private List<RtuIdInfo> mGZListLocality = new ArrayList<>();  //声明一个list，动态存储要显示的信息
    private List<RtuIdInfo> mW300ListLocality = new ArrayList<>();  //声明一个list，动态存储要显示的信息
    private List<RtuIdInfo> mSiChuanListLocality = new ArrayList<>();  //声明一个list，动态存储要显示的信息

    private static List_Content_Setting1_4 fragment = null;
    private Callbacks mCallbacks;
    private String proName; //协议名称


    public static List_Content_Setting1_4 newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Setting1_4();
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
        return R.layout.list_content_setting1_4;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p3);
        mBtnReturn.setOnClickListener(new BtnClickListener());

        txtvYX1 = (TextView) v.findViewById(R.id.txtXY1);

        mSpXYCfgSelect = v.findViewById(R.id.spXYCfgSelect);
        InitSpinner(context, mSpXYCfgSelect, protocolDisplayList, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpXYCfgSelect.setOnItemSelectedListener(new SpClickListener());

        mEdDigitalAddr = v.findViewById(R.id.eTAddr);
        mEdDigitalAddr.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdDigitalAddr.setOnEditorActionListener(new EditFocusChangeClick());
        mEdDigitalAddr.setText(getPublicConfigData("DIGITAL_ADDR"));

        mEPCdDigitalAddr = v.findViewById(R.id.eTPC_Addr);
        mEPCdDigitalAddr.setOnFocusChangeListener(new EditFocusChangeClick());
        mEPCdDigitalAddr.setOnEditorActionListener(new EditFocusChangeClick());
        mEPCdDigitalAddr.setText(getPublicConfigData("PC_DIGITAL_ADDR"));

        mEdDigital2Addr = v.findViewById(R.id.eTAddr2);
        mEdDigital2Addr.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdDigital2Addr.setOnEditorActionListener(new EditFocusChangeClick());
        mEdDigital2Addr.setText(getPublicConfigData("RS485_DIGITAL_ADDR"));

        mEdOrg = v.findViewById(R.id.eTOrg);
        mEdOrg.setText(getPublicConfigData("ORG_ADDR"));
        mEdOrg.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdOrg.setOnEditorActionListener(new EditFocusChangeClick());
        mEdDevAddr = v.findViewById(R.id.eTDevAddr);
        mEdDevAddr.setText(getPublicConfigData("DEV_ADDR"));
        mEdDevAddr.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdDevAddr.setOnEditorActionListener(new EditFocusChangeClick());
        mSpGB2005CRC = v.findViewById(R.id.spgb2005crc);
        InitSpinner(context, mSpGB2005CRC, getResources().getStringArray(R.array.crc), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpGB2005CRC.setSelection(Integer.parseInt(getPublicConfigData("GB2005_CRC")));
        mSpGB2005CRC.setOnItemSelectedListener(new SpClickListener());

        mEd2017Org = v.findViewById(R.id.eTOrg2017);
        mEd2017Org.setText(getPublicConfigData("GB2017ORG_ADDR"));
        mEd2017Org.setOnFocusChangeListener(new EditFocusChangeClick());
        mEd2017Org.setOnEditorActionListener(new EditFocusChangeClick());
        mEd2017DevAddr = v.findViewById(R.id.eTDevAddr2017);
        mEd2017DevAddr.setText(getPublicConfigData("GB2017DEV_ADDR"));
        mEd2017DevAddr.setOnFocusChangeListener(new EditFocusChangeClick());
        mEd2017DevAddr.setOnEditorActionListener(new EditFocusChangeClick());
        mSpGB2017CRC = v.findViewById(R.id.spgb2017crc);
        InitSpinner(context, mSpGB2017CRC, getResources().getStringArray(R.array.crc), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpGB2017CRC.setSelection(Integer.parseInt(getPublicConfigData("GB2017_CRC")));
        mSpGB2017CRC.setOnItemSelectedListener(new SpClickListener());

        mEdDevSN = v.findViewById(R.id.eTPC_devsn);
        mEdDevSN.setText(getPublicConfigData("DEV_SERIAL_NUM"));
        mEdDevSN.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdDevSN.setOnEditorActionListener(new EditFocusChangeClick());
        mEdDevSN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = mEdDevSN.getText().toString();
                String str = stringFilter(editable);
                if (!editable.equals(str)) {
                    mEdDevSN.setText(str);
                    mEdDevSN.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText mEdUpdateTime = v.findViewById(R.id.eTProtocolUpdateSec);
        mEdUpdateTime.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdUpdateTime.setOnEditorActionListener(new EditFocusChangeClick());
        mEdUpdateTime.setText(getPublicConfigData("ProtocolUpdateTime"));

        mEdJSOrg = v.findViewById(R.id.eTOrgJS);
        mEdJSOrg.setText(getPublicConfigData("JS_ORG_ADDR"));
        mEdJSOrg.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdJSOrg.setOnEditorActionListener(new EditFocusChangeClick());
        mEdJSDevAddr = v.findViewById(R.id.eTDevAddrJS);
        mEdJSDevAddr.setText(getPublicConfigData("JS_DEV_ADDR"));
        mEdJSDevAddr.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdJSDevAddr.setOnEditorActionListener(new EditFocusChangeClick());

        if (mListLocality.size() > 0) {
            mListLocality.clear();
        }

        // 显示RTU2018数据
        try {
            for (String item : strComponent.get(1)) {

                RtuIdInfo rtuIdInfo = new RtuIdInfo();
                rtuIdInfo.setCompName(item);
                rtuIdInfo.setId(Integer.parseInt(getConfigData(item, "RTU_ID").equals("") ? "0" : getConfigData(item, "RTU_ID")));
                mListLocality.add(rtuIdInfo);
            }
        } catch (Exception e) {
        }
        ListView mListRtuID = v.findViewById(R.id.listRtuID);
        mListRtuID.setAdapter(new CustomAdapter(getContext(), mListLocality));

        if (mGZListLocality.size() > 0) {
            mGZListLocality.clear();
        }

        // 显示GZ协议数据
        try {
            for (String item : strComponent.get(1)) {

                RtuIdInfo rtuIdInfo = new RtuIdInfo();
                rtuIdInfo.setCompName(item);
                rtuIdInfo.setId(Integer.parseInt(getConfigData(item, "GZ_RTU_ID").equals("") ? "0" : getConfigData(item, "GZ_RTU_ID")));
                mGZListLocality.add(rtuIdInfo);
            }
        } catch (Exception e) {
        }
        ListView mListRtuIDGZ = v.findViewById(R.id.listRtuID_GuiZhou);
        mListRtuIDGZ.setAdapter(new GZCustomAdapter(getContext(), mGZListLocality));

        if (mW300ListLocality.size() > 0) {
            mW300ListLocality.clear();
        }

        // 显示W300数据
        try {
            for (String item : strComponent.get(1)) {

                RtuIdInfo rtuIdInfo = new RtuIdInfo();
                rtuIdInfo.setCompName(item);
                rtuIdInfo.setId(Integer.parseInt(getConfigData(item, "W300_RTU_ID").equals("") ? "0" : getConfigData(item, "W300_RTU_ID")));
                mW300ListLocality.add(rtuIdInfo);
            }
        } catch (Exception e) {
        }
        ListView mListRtuIDW300 = v.findViewById(R.id.listRtuID_W300);
        mListRtuIDW300.setAdapter(new W300CustomAdapter(getContext(), mW300ListLocality));


        if (mSiChuanListLocality.size() > 0) {
            mSiChuanListLocality.clear();
        }

        // 显示四川协议地址
        try {
            for (String item : strComponent.get(1)) {

                RtuIdInfo rtuIdInfo = new RtuIdInfo();
                rtuIdInfo.setCompName(item);
                rtuIdInfo.setId(Integer.parseInt(getConfigData(item, "SiChuan_RTU_ID").equals("") ? "0" : getConfigData(item, "SiChuan_RTU_ID")));
                mSiChuanListLocality.add(rtuIdInfo);
            }
        } catch (Exception e) {
        }
        ListView mListRtuIDSiChuan = v.findViewById(R.id.listRtuID_SiChuan);
        mListRtuIDSiChuan.setAdapter(new SiChuanCustomAdapter(getContext(), mSiChuanListLocality));


        btnCalUpdate = v.findViewById(R.id.tBtnJZSZSC);
        btnCalUpdate.setOnCheckedChangeListener(new togBtnClickListener());
        btnCalUpdate.setChecked(getPublicConfigData("CAL_DATA_UPDATE").equals("true"));

        btnAbsorbUpdate = v.findViewById(R.id.tBtnXGDSC);
        btnAbsorbUpdate.setOnCheckedChangeListener(new togBtnClickListener());
        btnAbsorbUpdate.setChecked(getPublicConfigData("ABSORB_UPDATE").equals("true"));


        mEdSysRtuId = v.findViewById(R.id.eTSysRtuId);
        mEdSysRtuId.setText(getPublicConfigData("SYS_RTU_ID"));
        mEdSysRtuId.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdSysRtuId.setOnEditorActionListener(new EditFocusChangeClick());


        mEd2017OrgV2 = v.findViewById(R.id.eTOrg2017v2);
        mEd2017OrgV2.setText(getPublicConfigData("GB2017V2ORG_ADDR"));
        mEd2017OrgV2.setOnFocusChangeListener(new EditFocusChangeClick());
        mEd2017OrgV2.setOnEditorActionListener(new EditFocusChangeClick());
        mEd2017DevAddrV2 = v.findViewById(R.id.eTDevAddr2017v2);
        mEd2017DevAddrV2.setText(getPublicConfigData("GB2017V2DEV_ADDR"));
        mEd2017DevAddrV2.setOnFocusChangeListener(new EditFocusChangeClick());
        mEd2017DevAddrV2.setOnEditorActionListener(new EditFocusChangeClick());
        mSpGB2017v2CRC = v.findViewById(R.id.spgb2017v2crc);
        InitSpinner(context, mSpGB2017v2CRC, getResources().getStringArray(R.array.crc), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpGB2017v2CRC.setSelection(Integer.parseInt(getPublicConfigData("GB2017V2_CRC")));
        mSpGB2017v2CRC.setOnItemSelectedListener(new SpClickListener());

        EditText mEdHeNanOrg = v.findViewById(R.id.eTHeNanOrg);
        mEdHeNanOrg.setText(getPublicConfigData("HE_NAN_ORG_ADDR"));
        mEdHeNanOrg.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdHeNanOrg.setOnEditorActionListener(new EditFocusChangeClick());
        EditText mEdHeNanDevAddr = v.findViewById(R.id.eTHeNanDevAddr);
        mEdHeNanDevAddr.setText(getPublicConfigData("HE_NAN_DEV_ADDR"));
        mEdHeNanDevAddr.setOnFocusChangeListener(new EditFocusChangeClick());
        mEdHeNanDevAddr.setOnEditorActionListener(new EditFocusChangeClick());
        mSHeNanCRC = v.findViewById(R.id.spHeNancrc);
        InitSpinner(context, mSHeNanCRC, getResources().getStringArray(R.array.crc), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSHeNanCRC.setSelection(Integer.parseInt(getPublicConfigData("HE_NAN_CRC")));
        mSHeNanCRC.setOnItemSelectedListener(new SpClickListener());


        mSpAnalysisMode = v.findViewById(R.id.spAnalysisMode);//解析模式
        InitSpinner(context, mSpAnalysisMode, getResources().getStringArray(R.array.dilutionMode), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpAnalysisMode.setOnItemSelectedListener(new SpClickListener());


        mSpCrcMode = v.findViewById(R.id.spCrcMode);//校验模式
        InitSpinner(context, mSpCrcMode, getResources().getStringArray(R.array.crcMode), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        mSpCrcMode.setOnItemSelectedListener(new SpClickListener());

    }

    private void syncList() {

        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tBtnJZSZSC:
                    saveOperationLogPublicDataModifyMsg("公共", "CAL_DATA_UPDATE", btnCalUpdate.getText().toString(), "系统校准数据上传 ", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("CAL_DATA_UPDATE", btnCalUpdate.isChecked() ? "true" : "false");
                    break;
                case R.id.tBtnXGDSC:
                    saveOperationLogPublicDataModifyMsg("公共", "ABSORB_UPDATE", btnAbsorbUpdate.getText().toString(), "系统吸光度上传 ", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("ABSORB_UPDATE", btnAbsorbUpdate.isChecked() ? "true" : "false");
                    break;
            }
            syncList();
        }
    }


    class CustomAdapter extends RtuIdCfgAdapter {
        CustomAdapter(Context context, List<RtuIdInfo> items) {
            super(context, items, "RTU_ID");
        }

    }

    class GZCustomAdapter extends RtuIdCfgAdapter {
        GZCustomAdapter(Context context, List<RtuIdInfo> items) {
            super(context, items, "GZ_RTU_ID");
        }

    }

    class W300CustomAdapter extends RtuIdCfgAdapter {
        W300CustomAdapter(Context context, List<RtuIdInfo> items) {
            super(context, items, "W300_RTU_ID");
        }

    }

    class SiChuanCustomAdapter extends RtuIdCfgAdapter {
        SiChuanCustomAdapter(Context context, List<RtuIdInfo> items) {
            super(context, items, "SiChuan_RTU_ID");
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

    /**
     * 根据选择的协议，显示不同的控件
     *
     * @param position
     */
    private void showMnInfoDisplay(int position) {

        LinearLayout lyProtocol = v.findViewById(R.id.layout_ProtocolSet);
        if (protocolDisplayList.length == 0) {
            lyProtocol.setVisibility(View.INVISIBLE);
            return;
        } else {
            lyProtocol.setVisibility(View.VISIBLE);
        }
        LinearLayout layoutGB2005 = v.findViewById(R.id.layout_gb2005);
        LinearLayout layoutGB2017 = v.findViewById(R.id.layout_gb2017);
        LinearLayout layoutGB2017v2 = v.findViewById(R.id.layout_gb2017v2);
        LinearLayout layoutDci = v.findViewById(R.id.layout_DCI);
        LinearLayout layoutPc = v.findViewById(R.id.layout_PC);
        LinearLayout layoutDci2 = v.findViewById(R.id.layout_DCI2);
        LinearLayout layoutSN = v.findViewById(R.id.layout_device_serial_number);
        LinearLayout layoutSN_1 = v.findViewById(R.id.layout_device_serial_number_1);
        LinearLayout layoutJS = v.findViewById(R.id.layout_JS);
        LinearLayout layoutHeNan = v.findViewById(R.id.layout_heNan);
        LinearLayout layoutGZ = v.findViewById(R.id.layout_GuiZhou);
        LinearLayout layoutW300 = v.findViewById(R.id.layout_W300);
        LinearLayout layoutSiChuan = v.findViewById(R.id.layout_SiChuan);

        LinearLayout mLayoutUpdateTime = v.findViewById(R.id.layout_updateTime);
        LinearLayout mLayoutVolUpdate = v.findViewById(R.id.layoutVOLUpdate);
        LinearLayout mLayoutAnalysisMode = v.findViewById(R.id.layout_AnalysisMode);//解析模式
        LinearLayout mLayoutCrcMode = v.findViewById(R.id.layout_CrcMode);//校验模式

        layoutGB2005.setVisibility(View.GONE);
        layoutGB2017.setVisibility(View.GONE);
        layoutDci.setVisibility(View.GONE);
        layoutPc.setVisibility(View.GONE);
        layoutDci2.setVisibility(View.GONE);
        layoutSN.setVisibility(View.GONE);
        layoutSN_1.setVisibility(View.GONE);
        layoutJS.setVisibility(View.GONE);
        mLayoutVolUpdate.setVisibility(View.GONE);
        mLayoutUpdateTime.setVisibility(View.GONE);
        layoutGB2017v2.setVisibility(View.GONE);
        layoutHeNan.setVisibility(View.GONE);
        layoutGZ.setVisibility(View.GONE);
        layoutW300.setVisibility(View.GONE);
        layoutSiChuan.setVisibility(View.GONE);
        mLayoutAnalysisMode.setVisibility(View.GONE);
        mLayoutCrcMode.setVisibility(View.GONE);

        //TODO 根据协议不同选择不同的控件和解析、校验模式

        try {
            if (protocolList[position].equals(protocolName[0])) {//GB2005
                proName = protocolName[0];

                layoutGB2005.setVisibility(View.VISIBLE);
                mLayoutVolUpdate.setVisibility(View.VISIBLE);
                mLayoutUpdateTime.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);

            } else if (protocolList[position].equals(protocolName[1])) {//GB2017
                proName = protocolName[1];

                layoutGB2017.setVisibility(View.VISIBLE);
                mLayoutUpdateTime.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);


            } else if (protocolList[position].equals(protocolName[4])) {//ModbusRtu2018
                proName = protocolName[4];

                layoutSN.setVisibility(View.VISIBLE);
                layoutSN_1.setVisibility(View.VISIBLE);
                mLayoutAnalysisMode.setVisibility(View.VISIBLE);
                mLayoutCrcMode.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);

            } else if (protocolList[position].equals(protocolName[10])) {//Modbus_Rtu2

                proName = protocolName[10];

                layoutSN.setVisibility(View.VISIBLE);
                mLayoutAnalysisMode.setVisibility(View.VISIBLE);
                mLayoutCrcMode.setVisibility(View.VISIBLE);


                setCrcAndAnalysis(proName);

            } else if (protocolList[position].equals(protocolName[11])) {//贵州协议
                proName = protocolName[11];

                layoutGZ.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);


            } else if (protocolList[position].equals(protocolName[14])) {//W300
                proName = protocolName[14];

                layoutW300.setVisibility(View.VISIBLE);
                mLayoutAnalysisMode.setVisibility(View.VISIBLE);
                mLayoutCrcMode.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);


            } else if (mSpXYCfgSelect.getSelectedItemPosition() == 0) {

            } else if (protocolList[position].equals(protocolName[5])) {//江苏协议
                proName = protocolName[5];

                layoutJS.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);


            } else if (protocolList[position].equals(protocolName[7])) {//GB2017V2
                proName = protocolName[7];

                layoutGB2017v2.setVisibility(View.VISIBLE);
                mLayoutUpdateTime.setVisibility(View.VISIBLE);
                mLayoutVolUpdate.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);

            } else if (protocolList[position].equals(protocolName[8])) {//河南协议
                proName = protocolName[8];

                layoutHeNan.setVisibility(View.VISIBLE);
                mLayoutUpdateTime.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);


            } else if (protocolList[position].equals(protocolName[6]) || protocolList[position].equals(protocolName[12]) || protocolList[position].equals(protocolName[13])) {

                //6.河北协议    12.宁夏协议     13.深圳协议
                if (protocolList[position].equals(protocolName[6])) {
                    proName = protocolName[6];

                    setCrcAndAnalysis(proName);
                }

                if (protocolList[position].equals(protocolName[12])) {
                    proName = protocolName[12];

                    setCrcAndAnalysis(proName);

                }

                if (protocolList[position].equals(protocolName[13])) {
                    proName = protocolName[13];

                    setCrcAndAnalysis(proName);
                }

            } else if (protocolList[position].equals(protocolName[15])) {//四川协议

                proName = protocolName[15];

                layoutSiChuan.setVisibility(View.VISIBLE);

                setCrcAndAnalysis(proName);

            } else {
                layoutDci.setVisibility(View.VISIBLE);
                layoutPc.setVisibility(View.VISIBLE);
                int blIoBordEn = getIOBORD();
                if (blIoBordEn == 2) {
                    txtvYX1.setText(getString(R.string.rs232_port));
                    layoutDci2.setVisibility(View.VISIBLE);
                } else {
                    txtvYX1.setText(getString(R.string.forInstrument_port));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据协议名称获取数据，设置解析模式和校验模式
     *
     * @param name
     */
    private void setCrcAndAnalysis(String name) {

        //        if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[2])) {
        //
        //        } else if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[4])){
        //
        //        } else if(mSpXYCfgSelect.getSelectedItem().equals(protocolName[10])){
        //
        //        } else{
        //
        //        }

        if (!TextUtils.isEmpty(getPublicConfigData(name + "_ANALYSIS_MODE"))) {
            //根据所选择的协议，查询数据库中所对应的解析模式
            switch (getPublicConfigData(name + "_ANALYSIS_MODE")) {
                case "FLOAT":
                    mSpAnalysisMode.setSelection(0);
                    break;
                case "FLOAT2":
                    mSpAnalysisMode.setSelection(1);
                    break;
                case "FLOAT3":
                    mSpAnalysisMode.setSelection(2);
                    break;
            }

        } else {
            updatePublicConfigData(name + "_ANALYSIS_MODE", "FLOAT2");//默认解析模式为半反
            mSpAnalysisMode.setSelection(1);
        }

        if (!TextUtils.isEmpty(getPublicConfigData(name + "_CRC_MODE"))) {
            //根据所选择的协议，查询数据库中所对应的校验模式
            mSpCrcMode.setSelection(Integer.valueOf(getPublicConfigData(name + "_CRC_MODE")) - 1);
        } else {
            updatePublicConfigData(name + "_CRC_MODE", "1");
            mSpCrcMode.setSelection(0);
        }
    }

    private class EditFocusChangeClick implements View.OnFocusChangeListener, TextView.OnEditorActionListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTAddr:
                        // 要保留前面零
                        if (!mEdDigitalAddr.getText().toString().equals("")) {
                            if (Integer.parseInt(mEdDigitalAddr.getText().toString()) > 255) {
                                mEdDigitalAddr.setText("1");
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "DIGITAL_ADDR", mEdDigitalAddr.getText().toString(), "DCI_数字量地址", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("DIGITAL_ADDR", mEdDigitalAddr.getText().toString());
                        } else {
                            mEdDigitalAddr.setText(getPublicConfigData("DIGITAL_ADDR"));
                        }
                        break;
                    case R.id.eTPC_Addr:
                        if (!mEPCdDigitalAddr.getText().toString().equals("")) {
                            if (Integer.parseInt(mEPCdDigitalAddr.getText().toString()) > 255) {
                                mEPCdDigitalAddr.setText("1");
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "PC_DIGITAL_ADDR", mEPCdDigitalAddr.getText().toString(), "PC_数字量地址", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("PC_DIGITAL_ADDR", mEPCdDigitalAddr.getText().toString());
                        } else {
                            mEPCdDigitalAddr.setText(getPublicConfigData("PC_DIGITAL_ADDR"));
                        }
                        break;
                    case R.id.eTAddr2:
                        if (!mEdDigital2Addr.getText().toString().equals("")) {
                            if (Integer.parseInt(mEdDigital2Addr.getText().toString()) > 255) {
                                mEdDigital2Addr.setText("1");
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "RS485_DIGITAL_ADDR", mEdDigital2Addr.getText().toString(), "RS485_数字量地址", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("RS485_DIGITAL_ADDR", mEdDigital2Addr.getText().toString());
                        } else {
                            mEdDigital2Addr.setText(getPublicConfigData("RS485_DIGITAL_ADDR"));
                        }
                        break;
                    case R.id.eTOrg:
                        if (!mEdOrg.getText().toString().equals("")) {
                            if (mEdOrg.getText().toString().length() < 7) {
                                Toast.makeText(context, getString(R.string.please_input_7_place) + "!", Toast.LENGTH_SHORT).show();
                                mEdOrg.setText(getPublicConfigData("ORG_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "ORG_ADDR", mEdOrg.getText().toString(), "GB2005Org ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("ORG_ADDR", mEdOrg.getText().toString());
                        } else {
                            mEdOrg.setText(getPublicConfigData("ORG_ADDR"));
                        }
                        break;
                    case R.id.eTDevAddr:
                        if (!mEdDevAddr.getText().toString().equals("")) {
                            if (mEdDevAddr.getText().toString().length() < 7) {
                                Toast.makeText(context, getString(R.string.please_input_7_place) + "!", Toast.LENGTH_SHORT).show();
                                mEdDevAddr.setText(getPublicConfigData("DEV_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "DEV_ADDR", mEdDevAddr.getText().toString(), "GB2005ODev ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("DEV_ADDR", mEdDevAddr.getText().toString());
                        } else {
                            mEdDevAddr.setText(getPublicConfigData("DEV_ADDR"));
                        }
                        break;
                    case R.id.eTOrg2017:
                        if (!mEd2017Org.getText().toString().equals("")) {
                            if (mEd2017Org.getText().toString().length() < 12) {
                                Toast.makeText(context, getString(R.string.please_input_12_place) + "!", Toast.LENGTH_SHORT).show();
                                mEd2017Org.setText(getPublicConfigData("GB2017ORG_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "GB2017ORG_ADDR", mEd2017Org.getText().toString(), "GB2017Org ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("GB2017ORG_ADDR", mEd2017Org.getText().toString());
                        } else {
                            mEd2017Org.setText(getPublicConfigData("GB2017ORG_ADDR"));
                        }
                        break;
                    case R.id.eTDevAddr2017:
                        if (!mEd2017DevAddr.getText().toString().equals("")) {
                            if (mEd2017DevAddr.getText().toString().length() < 12) {
                                Toast.makeText(context, getString(R.string.please_input_12_place) + "!", Toast.LENGTH_SHORT).show();
                                mEd2017DevAddr.setText(getPublicConfigData("GB2017DEV_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "GB2017DEV_ADDR", mEd2017DevAddr.getText().toString(), "GB2017Dev ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("GB2017DEV_ADDR", mEd2017DevAddr.getText().toString());
                        } else {
                            mEd2017DevAddr.setText(getPublicConfigData("GB2017DEV_ADDR"));
                        }
                        break;

                    case R.id.eTOrgJS:
                        if (!mEdJSOrg.getText().toString().equals("")) {
                            if (mEdJSOrg.getText().toString().length() < 12) {
                                Toast.makeText(context, getString(R.string.please_input_12_place) + "!", Toast.LENGTH_SHORT).show();
                                mEdJSOrg.setText(getPublicConfigData("JS_ORG_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "JS_ORG_ADDR", mEdJSOrg.getText().toString(), "JSORG ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("JS_ORG_ADDR", mEdJSOrg.getText().toString());
                        } else {
                            mEdJSOrg.setText(getPublicConfigData("JS_ORG_ADDR"));
                        }
                        break;
                    case R.id.eTDevAddrJS:
                        if (!mEdJSDevAddr.getText().toString().equals("")) {
                            if (mEdJSDevAddr.getText().toString().length() < 12) {
                                Toast.makeText(context, getString(R.string.please_input_12_place) + "!", Toast.LENGTH_SHORT).show();
                                mEdJSDevAddr.setText(getPublicConfigData("JS_DEV_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "JS_DEV_ADDR", mEdJSDevAddr.getText().toString(), "JS_DEV_ADDR ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("JS_DEV_ADDR", mEdJSDevAddr.getText().toString());
                        } else {
                            mEdJSDevAddr.setText(getPublicConfigData("JS_DEV_ADDR"));
                        }
                        break;

                    case R.id.eTPC_devsn:
                        if (!mEdDevSN.getText().toString().equals("")) {
                            if (mEdDevSN.getText().toString().length() < 24) {
                                Toast.makeText(context, getString(R.string.please_input_24_place) + "!", Toast.LENGTH_SHORT).show();
                                mEdDevSN.setText(getPublicConfigData("DEV_SERIAL_NUM"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "DEV_SERIAL_NUM", mEdDevSN.getText().toString(), "DevSN ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("DEV_SERIAL_NUM", mEdDevSN.getText().toString());
                        } else {
                            mEdDevSN.setText(getPublicConfigData("DEV_SERIAL_NUM"));
                        }
                        break;
                    case R.id.eTProtocolUpdateSec:
                        setEditSystemData("ProtocolUpdateTime", (EditText) v, 0, 1, 600, 60, "协议主动上传间隔时间");
                        break;
                    case R.id.eTSysRtuId: {
                        String strValue = mEdSysRtuId.getText().toString();
                        if (strValue.equals("") || (Integer.parseInt(strValue) < 0) || (Integer.parseInt(strValue) > 247)) {
                            mEdSysRtuId.setText("1");
                        }
                        String strId = getPublicConfigData("SYS_RTU_ID");
                        if (strId.equals(strValue)) {
                            return;
                        }
                        if (!setIDConflicting(strValue, "RTU_ID")) {
                            mEdSysRtuId.setText(getPublicConfigData("SYS_RTU_ID"));
                            return;
                        }
                        saveOperationLogPublicDataModifyMsg("公共", "SYS_RTU_ID", mEdSysRtuId.getText().toString(), "DevSN ", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("SYS_RTU_ID", mEdSysRtuId.getText().toString());
                    }
                    break;
                    case R.id.eTOrg2017v2:
                        if (!mEd2017OrgV2.getText().toString().equals("")) {
                            if (mEd2017OrgV2.getText().toString().length() < 12) {
                                Toast.makeText(context, getString(R.string.please_input_12_place) + "!", Toast.LENGTH_SHORT).show();
                                mEd2017OrgV2.setText(getPublicConfigData("GB2017V2ORG_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "GB2017V2ORG_ADDR", mEd2017OrgV2.getText().toString(), "GB2017V2Org ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("GB2017V2ORG_ADDR", mEd2017OrgV2.getText().toString());
                        } else {
                            mEd2017OrgV2.setText(getPublicConfigData("GB2017V2ORG_ADDR"));
                        }
                        break;
                    case R.id.eTDevAddr2017v2:
                        if (!mEd2017DevAddrV2.getText().toString().equals("")) {
                            if (mEd2017DevAddrV2.getText().toString().length() < 12) {
                                Toast.makeText(context, getString(R.string.please_input_12_place) + "!", Toast.LENGTH_SHORT).show();
                                mEd2017DevAddrV2.setText(getPublicConfigData("GB2017V2DEV_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "GB2017V2DEV_ADDR", mEd2017DevAddrV2.getText().toString(), "GB2017V2Dev ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("GB2017V2DEV_ADDR", mEd2017DevAddrV2.getText().toString());
                        } else {
                            mEd2017DevAddrV2.setText(getPublicConfigData("GB2017V2DEV_ADDR"));
                        }
                        break;
                    case R.id.eTHeNanOrg:
                        if (!((EditText) v).getText().toString().equals("")) {
                            if (((EditText) v).getText().toString().length() < 7) {
                                Toast.makeText(context, getString(R.string.please_input_7_place) + "!", Toast.LENGTH_SHORT).show();
                                ((EditText) v).setText(getPublicConfigData("HE_NAN_ORG_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "HE_NAN_ORG_ADDR", ((EditText) v).getText().toString(), "HE_NAN_ORG_ADDR ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("HE_NAN_ORG_ADDR", ((EditText) v).getText().toString());
                        } else {
                            ((EditText) v).setText(getPublicConfigData("HE_NAN_ORG_ADDR"));
                        }
                        break;
                    case R.id.eTHeNanDevAddr:
                        if (!((EditText) v).getText().toString().equals("")) {
                            if (((EditText) v).getText().toString().length() < 7) {
                                Toast.makeText(context, getString(R.string.please_input_7_place) + "!", Toast.LENGTH_SHORT).show();
                                ((EditText) v).setText(getPublicConfigData("HE_NAN_DEV_ADDR"));
                                return;
                            }
                            saveOperationLogPublicDataModifyMsg("公共", "HE_NAN_DEV_ADDR", ((EditText) v).getText().toString(), "HE_NAN_DEV_ADDR ", ErrorLog.msgType.操作_信息);
                            updatePublicConfigData("HE_NAN_DEV_ADDR", ((EditText) v).getText().toString());
                        } else {
                            ((EditText) v).setText(getPublicConfigData("HE_NAN_DEV_ADDR"));
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

    private class SpClickListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {
                case R.id.spXYCfgSelect:
                    showMnInfoDisplay(position);
                    break;
                case R.id.spgb2005crc:
                    if (!getPublicConfigData("GB2005_CRC").equals(String.valueOf(position))) {
                        saveOperationLogPublicDataModifyMsg("公共", "GB2005_CRC", String.valueOf(position), "GB2005CRC ", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("GB2005_CRC", String.valueOf(position));
                    }
                    break;
                case R.id.spgb2017crc:
                    if (!getPublicConfigData("GB2017_CRC").equals(String.valueOf(position))) {
                        saveOperationLogPublicDataModifyMsg("公共", "GB2017_CRC", String.valueOf(position), "GB2017CRC ", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("GB2017_CRC", String.valueOf(position));
                    }
                    break;
                case R.id.spgb2017v2crc:
                    if (!getPublicConfigData("GB2017V2_CRC").equals(String.valueOf(position))) {
                        saveOperationLogPublicDataModifyMsg("公共", "GB2017V2_CRC", String.valueOf(position), "GB2017V2CRC ", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("GB2017V2_CRC", String.valueOf(position));
                    }
                    break;
                case R.id.spHeNancrc:
                    if (!getPublicConfigData("HE_NAN_CRC").equals(String.valueOf(position))) {
                        saveOperationLogPublicDataModifyMsg("公共", "HE_NAN_CRC", String.valueOf(position), "HENANCRC ", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("HE_NAN_CRC", String.valueOf(position));
                    }
                    break;
                case R.id.spAnalysisMode://解析模式
                    switch (position) {
                        case 0:
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[2])) {
                                updatePublicConfigData("Modbus_Rtu_ANALYSIS_MODE", "FLOAT");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu_ANALYSIS_MODE", "FLOAT", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[4])) {
                                updatePublicConfigData("ModbusRtu2018_ANALYSIS_MODE", "FLOAT");
                                saveOperationLogPublicDataModifyMsg("公共", "ModbusRtu2018_ANALYSIS_MODE", "FLOAT", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[10])) {
                                updatePublicConfigData("Modbus_Rtu2_ANALYSIS_MODE", "FLOAT");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu2_ANALYSIS_MODE", "FLOAT", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            break;
                        case 1:
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[2])) {
                                updatePublicConfigData("Modbus_Rtu_ANALYSIS_MODE", "FLOAT2");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu_ANALYSIS_MODE", "FLOAT2", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[4])) {
                                updatePublicConfigData("ModbusRtu2018_ANALYSIS_MODE", "FLOAT2");
                                saveOperationLogPublicDataModifyMsg("公共", "ModbusRtu2018_ANALYSIS_MODE", "FLOAT2", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[10])) {
                                updatePublicConfigData("Modbus_Rtu2_ANALYSIS_MODE", "FLOAT2");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu2_ANALYSIS_MODE", "FLOAT2", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            break;
                        case 2:
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[2])) {
                                updatePublicConfigData("Modbus_Rtu_ANALYSIS_MODE", "FLOAT3");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu_ANALYSIS_MODE", "FLOAT3", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[4])) {
                                updatePublicConfigData("ModbusRtu2018_ANALYSIS_MODE", "FLOAT3");
                                saveOperationLogPublicDataModifyMsg("公共", "ModbusRtu2018_ANALYSIS_MODE", "FLOAT3", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[10])) {
                                updatePublicConfigData("Modbus_Rtu2_ANALYSIS_MODE", "FLOAT3");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu2_ANALYSIS_MODE", "FLOAT3", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            break;
                    }
                    break;
                case R.id.spCrcMode://校验模式
                    switch (position) {
                        case 0:
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[2])) {
                                updatePublicConfigData("Modbus_Rtu_CRC_MODE", "1");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu_CRC_MODE", "1", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[4])) {
                                updatePublicConfigData("ModbusRtu2018_CRC_MODE", "1");
                                saveOperationLogPublicDataModifyMsg("公共", "ModbusRtu2018_CRC_MODE", "1", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[10])) {
                                updatePublicConfigData("Modbus_Rtu2_CRC_MODE", "1");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu2_CRC_MODE", "1", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            break;
                        case 1:
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[2])) {
                                updatePublicConfigData("Modbus_Rtu_CRC_MODE", "2");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu_CRC_MODE", "2", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[4])) {
                                updatePublicConfigData("ModbusRtu2018_CRC_MODE", "2");
                                saveOperationLogPublicDataModifyMsg("公共", "ModbusRtu2018_CRC_MODE", "2", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            if (mSpXYCfgSelect.getSelectedItem().equals(protocolName[10])) {
                                updatePublicConfigData("Modbus_Rtu2_CRC_MODE", "2");
                                saveOperationLogPublicDataModifyMsg("公共", "Modbus_Rtu2_CRC_MODE", "2", "crcMode ", ErrorLog.msgType.操作_信息);

                            }
                            break;
                    }
                    break;
            }
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
