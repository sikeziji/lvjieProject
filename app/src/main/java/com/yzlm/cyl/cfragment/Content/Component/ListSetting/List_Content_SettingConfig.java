package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Dialog.Dialog_PublicOk;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.SetMD5;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

public class List_Content_SettingConfig extends SubFragment {
    static List_Content_SettingConfig fragment = null;
    private Callbacks mCallbacks;

    List<byte[]> UpdateData = new ArrayList<byte[]>();
    public static Timer LTimer;
    //当前更新步骤
    private static String DoUpdate = "default";
    //当前更新页数
    private static int now = 0;
    private int registerAddr = 1;//测控版01，磁导计量03
    private static int addr = 268435461;//设备地址
    byte updateType=2;// 1 ：内部Flash 安全升级 2：外部Flash 安全升级 3：内部Flash 直接升级

    public static List_Content_SettingConfig newInstance() {
        if (fragment == null) {
            fragment = new List_Content_SettingConfig();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogSelected(View view, Fragment Fm);

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
        return R.layout.list_content_settingconfig;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        Button btnUpConfig = (Button) v.findViewById(R.id.btnUpdateConfig);
        btnUpConfig.setOnClickListener(new BtnClickListener());
        Button btnChooseFile = (Button) v.findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener(new BtnClickListener());
        btnChooseFile.setVisibility(View.GONE);
        Button btnUpCKB = (Button) v.findViewById(R.id.btnUpdateCKB);
        btnUpCKB.setOnClickListener(new BtnClickListener());
        btnUpCKB.setVisibility(View.GONE);
    }

    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnUpdateConfig: {
                    main.removeDestopText(mfb);
                    Dialog_PublicOk st = new Dialog_PublicOk();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", getString(R.string.update_config));
                    st.setArguments(bundle);
                    st.setTargetFragment(fragment, 1);
                    st.show(fm, "alert-ok");
                }
                break;
                case R.id.btnChooseFile: {
                    main.removeDestopText(mfb);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_the_file)), 99);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActivity(), getString(R.string.No_file_manager), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case R.id.btnUpdateCKB: {
                    main.removeDestopText(mfb);
                    Dialog_PublicOk st = new Dialog_PublicOk();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", getString(R.string.update_ckb));
                    st.setArguments(bundle);
                    st.setTargetFragment(fragment, 1);
                    st.show(fm, "alert-ok");
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
            if (stringExtra.contains(getString(R.string.update_config))) {
                SetMD5();
            } else if (stringExtra.contains(getString(R.string.update_ckb))) {
                UpdateCKB();
            }
            mCallbacks.onDialogRS();
        } else if (requestCode == 2) {
            mCallbacks.onDialogRS();
        } else if (requestCode == 99) {
            Uri uri = data.getData();
            String strAddr = uri.getPath();
            try {
                //  /storage/emulated/0/ck/APP_ZK3.bin
                //01==测控板，03==磁导计量
                //设备地址

                //String aaa = "/storage/emulated/0/ck/APP_ZK3.bin";
                byte[] getData = InputStream2ByteArray(strAddr);

                //upc.SetAddr();    //设置设备地址
                //upc.SetRegisterAddr();    //设置板子信息
                ByteToListByte(getData);

            } catch (IOException ex) {
            }

            mCallbacks.onDialogRS();
        }
    }

    public void UpdateCKB() {
        //blUpdateCKBFlag = true;

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
        if (LTimer != null) {
            LTimer.cancel();
        }
        LTimer = new Timer();
        LTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimeUpdate();
            }
        }, 500, 100);
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
                if (UpdateData.size() != now) {
                    //给73，25秒等待(主要是73最后一页数据等待时间较长)
                    sendWCom((byte) 0x73, copybyte(intToBytes(UpdateData.size()), intToBytes(now + 1), UpdateData.get(now)), /*intToByte(registerAddr), */60);
                } else {
                    //发送了71的退出指令，代表升级成功
                    sendWCom((byte) 0x71, close, /*intToByte(registerAddr),*/ 1);
                }
            }
            break;
            default:
                break;
        }
    }

    //发送软件版本升级的命令
    private void sendWCom(byte Cmd, byte[] Data, /*byte head, */int Try) {

        byte[] bAddr = intToByteArray(addr);
        //byte[] SendStarDataBuf = new byte[]{bAddr[0], bAddr[1], bAddr[2], bAddr[3], 0x09, 0x00, 0x00};
        byte[] SendStarDataBuf = new byte[]{bAddr[0], bAddr[1], bAddr[2], bAddr[3], 0x09, 0x00};
        byte[] len = intToBytes(Data.length);
        SendStarDataBuf = copybyte(SendStarDataBuf, len);
        SendStarDataBuf[5] = Cmd;
        // SendStarDataBuf[5] = head;
        // SendStarDataBuf[6] = Cmd;
        byte[] SendEndDataBuf = new byte[]{0x00, 0x00};
        byte[] full = new byte[SendStarDataBuf.length + Data.length + SendEndDataBuf.length];
        System.arraycopy(SendStarDataBuf, 0, full, 0, SendStarDataBuf.length);
        System.arraycopy(Data, 0, full, SendStarDataBuf.length, Data.length);
        System.arraycopy(SendEndDataBuf, 0, full, SendStarDataBuf.length + Data.length, SendEndDataBuf.length);
        full = addCRC16(full);

        ///发送指令
        SendManager.SendCmd(mCompName + "_" + "更新测控板_10_00", S0, 3, 500 * Try, full);
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

}
