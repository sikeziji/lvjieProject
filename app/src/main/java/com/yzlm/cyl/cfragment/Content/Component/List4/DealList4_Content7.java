package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Adapter.SerialListAdapter;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Component;
import com.yzlm.cyl.cfragment.Content.beans.SerialBean;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;

/**
 * Create by duyang
 * Create on 2020-08-13 10:26:30
 * description  串口配置
 */
public class DealList4_Content7 extends SubFragment {


    private Spinner serialType, deviceName, baudRate;//设备类型、名称、波特率的选择
    private Button btnAddSerial;//添加串口btn
    private ListView mLvAddedSerialList;//已配置的串口列表
    public static SerialListAdapter listAdapter;


    private static DealList4_Content7.Callbacks mCallbacks;
    private static DealList4_Content7 fragment = null;
    private String[] type = {"COM1", "COM2"};


    private Component mComponent;
    public static List<SerialBean> serialBeanList = new ArrayList<>();//串口列表
    //设备名称不可随意变换位置
    private String[] name = {"TECAN注射泵", "IDEX旋转阀", "磁道计量板", "W200计量板", "光谱仪", "ORP板", "滴定计量板", "奥拓尼克斯温控器TX4S系列", "CO2模块"};
    private String[] baudRates = {"2400bps", "9600bps", "14400bps", "19200bps", "38400bps", "56000bps", "57600bps", "115200bps"};

    public static DealList4_Content7 newInstance() {
        if (fragment == null) {
            fragment = new DealList4_Content7();
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
        mCallbacks = (DealList4_Content7.Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list4_content7;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        mComponent = Component.newInstance(getContext());

        try{

            serialType = v.findViewById(R.id.serialType);
            deviceName = v.findViewById(R.id.deviceName);
            baudRate = v.findViewById(R.id.baudRate);
            btnAddSerial = v.findViewById(R.id.Btn_add_serial);
            btnAddSerial.setOnClickListener(new BtnOnClickListener());
            mLvAddedSerialList = v.findViewById(R.id.lv_added_serial_list);

            initList();

            getSelectData();//设置选项框数据
            getListData((byte) 0x00);//默认获取COM1的列表数据，可通过选择端口号进行切换

        }catch (Exception e){
            saveExceptInfo2File("串口配置错误" + e.toString());
        }finally {
            RealTimeStatusThread.resume();
        }
    }

    //从板子上获取可配置的串口信息，并将获取的数据放到列表中
    private void initList() {
        serialBeanList.clear();
        if (listAdapter == null) {
            listAdapter = new SerialListAdapter(getContext(), serialBeanList) {
                @Override
                public void deleteSerial(int position) {

                    for (int i = 0; i < name.length; i++) {
                        if (name[i].equals(serialBeanList.get(position))) {

                        }
                    }
                    serialBeanList.clear();
                    serialBeanList.remove(position);
                    listAdapter.notifyDataSetChanged();
                }
            };
        }
        mLvAddedSerialList.setAdapter(listAdapter);
    }


    /**
     * 读取三个选框中的数据
     */
    public void getSelectData(){


        InitSpinner(context, serialType, type, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        serialType.setSelection(0);
        serialType.setOnItemSelectedListener(new mSpOnClickListener());


        InitSpinner(context, deviceName, name, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        deviceName.setSelection(0);
        deviceName.setOnItemSelectedListener(new mSpOnClickListener());


        InitSpinner(context, baudRate, baudRates, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        baudRate.setSelection(0);
        baudRate.setOnItemSelectedListener(new mSpOnClickListener());
    }

    /**
     * 获取列表数据（默认COM1）
     */
    public void getListData(byte serialType) {
        //TODO 从板子上读取未接入的串口信息
        byte[] arrayOfByte = copybyte(new byte[]{serialType});
        SendManager.SendCmd(mCompName + "_串口配置" + "_8_20", S0, 3, 200, arrayOfByte);
    }


    private class mSpOnClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.serialType:
                    serialType.setTag(serialType.getSelectedItem().toString());
                    serialBeanList.clear();
                    switch (serialType.getSelectedItem().toString()) {
                        case "COM1":
                            getListData((byte) 0x00);
                            break;
                        case "COM2":
                            getListData((byte) 0x01);
                            break;
                        default:
                            getListData((byte) 0x00);
                            break;
                    }
                    break;
                case R.id.deviceName:
                    deviceName.setTag(deviceName.getSelectedItem().toString());
                    break;
                case R.id.baudRate:
                    baudRate.setTag(baudRate.getSelectedItem().toString());
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //TODO 做添加操作
            String type = serialType.getSelectedItem().toString();
            String name = deviceName.getSelectedItem().toString();
            String baud = baudRate.getSelectedItem().toString();

            SerialBean bean = new SerialBean();
            bean.setName(name);
            bean.setType(type);
            bean.setBaudRate(baud);
            serialBeanList.add(bean);


            List<Byte> bytes = new ArrayList<>();

            //端口号
            switch (type){
                case "COM1":
                    bytes.add((byte) 0x00);
                    break;
                case "COM2":
                    bytes.add((byte) 0x01);
                    break;
            }

            //波特率
            switch (baud){
                case "2400bps":
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x09);
                    bytes.add((byte) 0x60);
                    break;
                case "9600bps":
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x25);
                    bytes.add((byte) 0x80);
                    break;
                case "14400bps":
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x38);
                    bytes.add((byte) 0x40);
                    break;
                case "19200bps":
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x4B);
                    bytes.add((byte) 0x00);
                    break;
                case "38400bps":
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x96);
                    bytes.add((byte) 0x00);
                    break;
                case "56000bps":
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0xDA);
                    bytes.add((byte) 0xC0);
                    break;
                case "57600bps":
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0xE1);
                    bytes.add((byte) 0x00);
                    break;
                case "115200bps":
                    bytes.add((byte) 0x00);
                    bytes.add((byte) 0x01);
                    bytes.add((byte) 0xC2);
                    bytes.add((byte) 0x00);
                    break;

            }

            //设备数量
            bytes.add((byte) serialBeanList.size());

            for (SerialBean serialBean : serialBeanList){
                //设备名称
                switch (serialBean.getName()){
                    case "TECAN注射泵":
                        bytes.add((byte) 0x01);
                        break;
                    case "IDEX旋转阀":
                        bytes.add((byte) 0x02);
                        break;
                    case "磁道计量板":
                        bytes.add((byte) 0x03);
                        break;
                    case "W200计量板":
                        bytes.add((byte) 0x04);
                        break;
                    case "光谱仪":
                        bytes.add((byte) 0x05);
                        break;
                    case "ORP板":
                        bytes.add((byte) 0x06);
                        break;
                    case "滴定计量板":
                        bytes.add((byte) 0x07);
                        break;
                    case "奥拓尼克斯温控器TX4S系列":
                        bytes.add((byte) 0x08);
                        break;
                    case "CO2模块":
                        bytes.add((byte) 0x09);
                        break;
                }
            }

            byte[] arrayOfByte = copybyte(listTobyte(bytes));
            SendManager.SendCmd(mCompName + "_串口配置" + "_8_1F", S0, 3, 200, arrayOfByte);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //配置完成后，重新读取列表信息
            serialBeanList.clear();
            switch (type) {
                case "COM1":
                    getListData((byte) 0x00);
                    break;
                case "COM2":
                    getListData((byte) 0x01);
                    break;
                default:
                    getListData((byte) 0x00);
                    break;
            }

        }
    }

    //将读取的未添加的设备添加到列表中
    public static Handler editSerialListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    byte[] bytes = msg.getData().getByteArray("serialsInfo");

                    //获取端口号
                    byte[] ids = Arrays.copyOfRange(bytes,0,1);
                    //截取波特率，进行分类
                    byte[] baud = Arrays.copyOfRange(bytes,1,5);
                    //获取挂载设备数量
                    byte[] nums = Arrays.copyOfRange(bytes,5,6);

                    int num = Integer.parseInt(String.valueOf(nums[0]),16);
                    int id = Integer.parseInt(String.valueOf(ids[0]),16);

                    //若存在配置的端口，则将其显示
                    if (num > 0){
                        byte[] serialIds = Arrays.copyOfRange(bytes,6,bytes.length);

                        for (int i = 0; i < num; i++){

                            SerialBean serialBean = new SerialBean();

                            //波特率
                            serialBean.setBaudRate(new BigInteger(bytes2HexString(baud),16)+"bps");

                            //端口号
                            switch (id){
                                case 0:
                                    serialBean.setType("COM1");
                                    break;
                                case 1:
                                    serialBean.setType("COM2");
                                    break;
                            }

                            //设备名称
                            int serialId = Integer.parseInt(String.valueOf(serialIds[i]));
                            switch (serialId){
                                case 1:
                                    serialBean.setName("TECAN注射泵");
                                    break;
                                case 2:
                                    serialBean.setName("IDEX旋转阀");
                                    break;
                                case 3:
                                    serialBean.setName("磁道计量板");
                                    break;
                                case 4:
                                    serialBean.setName("W200计量板");
                                    break;
                                case 5:
                                    serialBean.setName("光谱仪");
                                    break;
                                case 6:
                                    serialBean.setName("ORP板");
                                    break;
                                case 7:
                                    serialBean.setName("滴定计量板");
                                    break;
                                case 8:
                                    serialBean.setName("奥拓尼克斯温控器TX4S系列");
                                    break;
                                case 9:
                                    serialBean.setName("CO2模块");
                                    break;
                            }
                            serialBeanList.add(serialBean);
                        }
                        listAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(context,"暂无配置",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    /**
     * 字节数组转16进制字符串
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }


    /**
     * byte集合转数组
     * @param list
     * @return
     */
    public byte[] listTobyte(List<Byte> list) {
        if (list == null || list.size() < 0)
            return null;
        byte[] bytes = new byte[list.size()];
        int i = 0;
        Iterator<Byte> iterator = list.iterator();
        while (iterator.hasNext()) {
            bytes[i] = iterator.next();
            i++;
        }
        return bytes;
    }

}
