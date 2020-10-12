package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_PublicOk;
import com.yzlm.cyl.cfragment.Dialog.Dialog_SDRes;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.blUpdateCKBFlag;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

public class List4_Content3_UpdateBoard extends SubFragment implements Dialog_SDRes.ResCallBack {
    private final String TAG = "List4_Content3_UpdateBoard";
    static List4_Content3_UpdateBoard fragment = null;
    private Callbacks mCallbacks;

    static List<byte[]> UpdateData = new ArrayList<byte[]>();
    public static Thread LTimerThread;
    public static boolean threadRun = false;
    //当前更新步骤
    private static String DoUpdate = "default";
    //当前更新页数
    private static int now = 0;
    private int registerAddr = 1;//测控版01，磁导计量03
    private static int addr = 268435461;//设备地址
    byte updateType = 2;// 1 ：内部Flash 安全升级 2：外部Flash 安全升级 3：内部Flash 直接升级

    private EditText etFileText;
    private static TextView txSchedule;
    private static Button btnUpCKB;
    private Dialog_SDRes dialog_sdRes = null;

    public static List4_Content3_UpdateBoard newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_UpdateBoard();
        }
        return fragment;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void updateBin(String path) {
        Log.e(TAG, "选择文件路径 path = " + path);
        try {
            if (path != null && !path.isEmpty()) {
                String strAddr = path;
                // 通过切割获取文件后缀名
                String str = strAddr.substring(strAddr.lastIndexOf("."), strAddr.length()).toLowerCase();
                if (!str.equals(".bin")) {
                    Toast.makeText(getActivity(), "请选择.bin文件进行升级", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dialog_sdRes != null) {
                    dialog_sdRes.dismiss();
                    dialog_sdRes = null;
                }
                etFileText.setText(strAddr);
                //  /storage/emulated/0/ck/APP_ZK3.bin
                //01==测控板，03==磁导计量
                //设备地址

                //String aaa = "/storage/emulated/0/ck/APP_ZK3.bin";
                byte[] getData = InputStream2ByteArray(strAddr);

                //upc.SetAddr();    //设置设备地址
                //upc.SetRegisterAddr();    //设置板子信息
                ByteToListByte(getData);
            }
        } catch (IOException ex) {
        }

        mCallbacks.onDialogRS();

    }

    public interface Callbacks {
        //void onDialogSelected(View view, Fragment Fm);

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
        return R.layout.list4_content3_updateboard;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturnFlow);
        mBtnReturn.setOnClickListener(new btnClick());

        Button btnChooseFile = (Button) v.findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener(new BtnClickListener());
        btnUpCKB = (Button) v.findViewById(R.id.btnUpdateCKB);
        btnUpCKB.setOnClickListener(new BtnClickListener());

        etFileText = (EditText) v.findViewById(R.id.edFileText);

        txSchedule = (TextView) v.findViewById(R.id.txSchedule);
    }

    @SuppressLint("HandlerLeak")
    private static Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    txSchedule.setText("0%");
                    btnUpCKB.setText("停止更新");
                }
                break;
                case 1: {
                    String strV = baifenbi(now + 1, UpdateData.size()) + "%";   //用字符串做拼接，得到想要的%形式
                    Log.d("更新测控板", "strV:" + strV + "   now:" + now + " size:" + UpdateData.size());
                    txSchedule.setText(strV);
                }
                break;
                case 2: {
                    blUpdateCKBFlag = false;
                    threadRun = false;
                    AddError(mCompName, 592, ErrorLog.msgType.其他_信息);
                    txSchedule.setText("更新完成");
                    btnUpCKB.setText("更新测控板");
                }
                break;
                case 3: {
                    Toast.makeText(context, "请先选择要升级的文件", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    };

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            blUpdateCKBFlag = false;
            threadRun = false;

            mCallbacks.onListSelected(v);
        }
    }

    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnChooseFile: {
                    main.removeDestopText(mfb);
                    if (dialog_sdRes == null) {
                        dialog_sdRes = new Dialog_SDRes(getActivity(), List4_Content3_UpdateBoard.this);
                    }
                    dialog_sdRes.show();

//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("*/*");
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    try {
//                        startActivityForResult(Intent.createChooser(intent, "选择文件"), 99);
//                    } catch (android.content.ActivityNotFoundException ex) {
//                        Toast.makeText(getActivity(), "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
//                    }
                }
                break;
                case R.id.btnUpdateCKB: {
                    if (btnUpCKB.getText().equals("更新测控板")) {
                        main.removeDestopText(mfb);
                        Dialog_PublicOk st = new Dialog_PublicOk();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-ok", "更新测控板");
                        st.setArguments(bundle);
                        st.setTargetFragment(List4_Content3_UpdateBoard.this, 1);
                        st.show(fm, "alert-ok");
                    } else {
                        main.removeDestopText(mfb);
                        Dialog_PublicOk st = new Dialog_PublicOk();
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-ok", "停止更新");
                        st.setArguments(bundle);
                        st.setTargetFragment(List4_Content3_UpdateBoard.this, 1);
                        st.show(fm, "alert-ok");
                    }
                }
                break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("DialogPublicOk");
            if (stringExtra.contains("更新测控板")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (etFileText.getText().toString().equals("")) {
                            Message msg = new Message();
                            msg.what = 3;
                            updateHandler.sendMessage(msg);
                            return;
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        updateHandler.sendMessage(msg);
                        UpdateCKB();
                    }
                }).start();
            } else {
                byte[] close = {0x00};
                sendWCom((byte) 0x71, close, /*intToByte(registerAddr),*/ 1);
                blUpdateCKBFlag = false;
                threadRun = false;

                txSchedule.setText("0%");
                btnUpCKB.setText("更新测控板");
            }
            mCallbacks.onDialogRS();
        } else if (requestCode == 2) {
            mCallbacks.onDialogRS();
        } else if (requestCode == 99) {
            try {
                if (data != null) {
                    Uri uri = data.getData();
                    String strAddr = uri.getPath();
                    // 通过切割获取文件后缀名
                    String str = strAddr.substring(strAddr.lastIndexOf("."), strAddr.length()).toLowerCase();
                    if (!str.equals(".bin")) {
                        Toast.makeText(getActivity(), "请选择.bin文件进行升级", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    etFileText.setText(strAddr);
                    //  /storage/emulated/0/ck/APP_ZK3.bin
                    //01==测控板，03==磁导计量
                    //设备地址

                    //String aaa = "/storage/emulated/0/ck/APP_ZK3.bin";
                    byte[] getData = InputStream2ByteArray(strAddr);

                    //upc.SetAddr();    //设置设备地址
                    //upc.SetRegisterAddr();    //设置板子信息
                    ByteToListByte(getData);
                }
            } catch (IOException ex) {
            }

            mCallbacks.onDialogRS();
        }
    }

    public void UpdateCKB() {
        blUpdateCKBFlag = true;

        //复位测控板
        byte[] full = new byte[]{0x08, 0x03, 0x0A, 0x01, 0x00};
        ///发送指令
        SendManager.SendCmd(mCompName + "_" + "更新测控板_08_00", S0, 3, 100, full);

        DoUpdate = "1";
        now = 0;

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UpdateClick();
    }

    private static byte[] InputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    private static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    public static byte[] intToBytes(int n) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            b[i] = (byte) (n >> (8 - i * 8));
        }
        return b;
    }


    public static void reverse(byte[] array) {
        if (array != null) {
            int i = 0;

            for (int j = array.length - 1; j > i; ++i) {
                byte tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
    }

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    //合并数组
    public static byte[] copybyte(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    //合并多个数组
    public static byte[] copybyte(byte[]... bytes) {
        byte[] rs = new byte[0];
        for (byte[] item : bytes) {
            byte[] temp = rs;
            int len = rs.length;
            rs = new byte[len + item.length];
            System.arraycopy(temp, 0, rs, 0, temp.length);
            System.arraycopy(item, 0, rs, len, item.length);
        }
        return rs;
    }

    //将选择的升级文件的字节数组转换成List，分多次传递
    public void ByteToListByte(byte[] data) {
        UpdateData.clear();
        for (int i = 0; i < (data.length % 1024 == 0 ? (data.length / 1024) : ((data.length / 1024) + 1)); i++) {
            byte[] temp = new byte[data.length / 1024 == i ? (data.length % 1024) : 1024];
            for (int j = 0; j < temp.length; j++) {
                //if (j + i * 1024 < read.Length)
                temp[j] = data[j + i * 1024];
            }
            UpdateData.add(temp);
        }
    }

    public void UpdateClick() {
        threadRun = true;
        LTimerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (threadRun) {
                        TimeUpdate();
                        Thread.sleep(150);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        LTimerThread.start();
    }

    public void TimeUpdate() {
        byte[] open = {0x01};
        byte[] close = {0x00};

        byte[] jsb = {(byte) 0x01};
        switch (DoUpdate) {
            case "1": {
                sendWCom((byte) 0x71, close,/* intToByte(registerAddr),*/ 5);
            }
            break;
            case "2": {
                //这边升级磁导计量的时候，可能需要Sleep一下，收到72成功的返回后不再发送72，不然可能会造成升级失败
                //给72，5秒等待(主要是CDJL，72需要10S)
                /*if (registerAddr == 3) {
                    sendWCom((byte) 0x72, jsb, *//*intToByte(registerAddr),*//* 20);
                } else */
                {                                                                             //升级方式
                    sendWCom((byte) 0x72, copybyte(intToBytes(UpdateData.size()), new byte[]{updateType}), /*intToByte(registerAddr), */5);
                }
            }
            break;
            case "3": {
                DoUpdate = "default";
                if (UpdateData.size() > now) {
                    //给73，25秒等待(主要是73最后一页数据等待时间较长)
                    sendWCom((byte) 0x73, copybyte(intToBytes(UpdateData.size()), intToBytes(now + 1), UpdateData.get(now)), /*intToByte(registerAddr), */60);
                    if (now % 10 == 0 || now == UpdateData.size() - 1) {
                        Message msg = new Message();
                        msg.what = 1;
                        updateHandler.sendMessage(msg);
                    }
                } else {
                    //发送了71的退出指令，代表升级成功
                    sendWCom((byte) 0x71, close, /*intToByte(registerAddr),*/ 1);
                    Message msg = new Message();
                    msg.what = 2;
                    updateHandler.sendMessage(msg);
                    Log.d("更新测控板", "成功结束:");
                }
            }
            break;
            default:
                break;
        }
    }

    //发送软件版本升级的命令
    private void sendWCom(byte Cmd, byte[] Data, /*byte head, */int Try) {
        byte[] SendStarDataBuf = new byte[]{0x09, 0x00};
        byte[] len = intToBytes(Data.length);
        SendStarDataBuf = copybyte(SendStarDataBuf, len);
        SendStarDataBuf[1] = Cmd;
        // SendStarDataBuf[5] = head;
        // SendStarDataBuf[6] = Cmd;
        byte[] SendEndDataBuf = new byte[]{0x00, 0x00};
        byte[] full = new byte[SendStarDataBuf.length + Data.length + SendEndDataBuf.length];
        System.arraycopy(SendStarDataBuf, 0, full, 0, SendStarDataBuf.length);
        System.arraycopy(Data, 0, full, SendStarDataBuf.length, Data.length);
        System.arraycopy(SendEndDataBuf, 0, full, SendStarDataBuf.length + Data.length, SendEndDataBuf.length);
        //full = addCRC16(full);

        ///发送指令
        SendManager.SendCmd(mCompName + "_" + "更新测控板_09_00", S0, 1, 500 * Try, full);
    }

    //接收数据处理
    public static void DataReceived(byte[] data) {
        try {
            if (data == null) return;
            if (data[4] == 0x09) {
                //软件升级版本命令接收解析
                switch (data[5]) {
                    //BootLoader
                    case 0x71:
                        if ((data[8] == (byte) 0x00 && data[9] == (byte) 0x01) || ((data[8] == (byte) 0x00 && data[9] == (byte) 0x02))) {
                            softwareUpdateReceiveEventHandle(false, "2");
                        }
                        break;
                    //准备发送升级文件
                    case 0x72:
                        if (data[8] == (byte) 0xAA && data[9] == (byte) 0x55) {
                            softwareUpdateReceiveEventHandle(false, "3");
                        }
                        break;
                    //发送版本升级文件
                    case 0x73:
                        if (data[8] == (byte) 0xAA && data[9] == (byte) 0x55) {
                            softwareUpdateReceiveEventHandle(true, "3");
                        } else if (data[8] == (byte) 0xBB && data[9] == (byte) 0x66) {
                            softwareUpdateReceiveEventHandle(false, "3");
                        }
                        break;
                    default:
                        break;
                }
            } else if (data[4] == 0x03 || data[4] == 0x06) {
                switch (data[4]) {
                    case 0x03: {
                        byte[] address = new byte[4];
                        System.arraycopy(data, 8, address, 0, 4);
                        reverse(address);
                        addr = byteArrayToInt(address);
                    }
                    break;
                    case 0x06:
                        break;
                }
            }
        } catch (Exception ex) {
        }
    }

    //软件升级时接收下位机升级命令事件进行升级参数处理
    private static void softwareUpdateReceiveEventHandle(boolean nowFlag, String doUpdateValue) {
        if (nowFlag) {
            now++;
        }

        DoUpdate = doUpdateValue;
    }

    //添加CRC
    private byte[] addCRC16(byte[] SendDataBuf) {
        byte[] newBuf = new byte[SendDataBuf.length - 2];
        System.arraycopy(SendDataBuf, 0, newBuf, 0, SendDataBuf.length - 2);
        return copybyte(newBuf, crc16(newBuf, newBuf.length));
    }

    public static byte[] crc16(byte[] data, int len) {
        byte[] temdata = new byte[2];
        int xda = '\uffff';
        int xdapoly = 'ꀁ';

        for (int i = 0; i < len; ++i) {
            xda ^= data[i] & 255;

            for (int j = 0; j < 8; ++j) {
                int xdabit = xda & 1;
                xda >>= 1;
                if (xdabit == 1) {
                    xda ^= xdapoly;
                }
            }
        }

        temdata[0] = (byte) (xda & 255);
        temdata[1] = (byte) (xda >> 8);
        return temdata;
    }

    public static int baifenbi(int a, int b) {
        return (int) ((new BigDecimal((float) a / b).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
    }

}
