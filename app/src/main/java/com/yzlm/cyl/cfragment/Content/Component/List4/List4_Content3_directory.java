package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Adapter.DirectoryAdapter;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.DBConvert.MDBConvert;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.charsToBytes;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.convertHexToString;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.System.arraycopy;
import static java.lang.Thread.sleep;

/**
 * Created by WL on 2017/03/28.
 */

public class List4_Content3_directory extends SubFragment {
    private Callbacks mCallbacks;
    private static ListView LV_directory;
    private static DirectoryAdapter lvAdapter;
    private static String componetName = "";
    public static List4_Content3_directory fragment = null;
    static private Button Btn_rootdirectory, Btn_dirLastPage, Btn_dirNextPage, Btn_txtLastPage, Btn_txtNextPage;

    static TextView txtFile;   //右半部分显示
    /*适配器数据源（WL）*/
    public static ArrayList<String[]> arrayListDirectory = new ArrayList<>();

    private int iPoint = 2;

    static DataDirectory dataDirectory = new DataDirectory();//记录文件信息

    public static List4_Content3_directory newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_directory();
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
        return R.layout.list4_content3_directory;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            componetName = mCompName;
            Global.LockDisplayShowFlag = true;
            iPoint = Integer.parseInt(getConfigData(componetName, "YXWS"));

            dataDirectoryInit();

            Btn_rootdirectory = (Button) v.findViewById(R.id.Btn_rootdirectory);
            Btn_rootdirectory.setOnClickListener(new buttonOnClickListener());
            Btn_rootdirectory.setVisibility(View.INVISIBLE);
            Btn_dirLastPage = (Button) v.findViewById(R.id.Btn_dirLastPage);
            Btn_dirLastPage.setOnClickListener(new buttonOnClickListener());
            Btn_dirLastPage.setVisibility(View.INVISIBLE);
            Btn_dirNextPage = (Button) v.findViewById(R.id.Btn_dirNextPage);
            Btn_dirNextPage.setOnClickListener(new buttonOnClickListener());
            Btn_dirNextPage.setVisibility(View.INVISIBLE);
            Btn_txtLastPage = (Button) v.findViewById(R.id.Btn_txtLastPage);
            Btn_txtLastPage.setOnClickListener(new buttonOnClickListener());
            Btn_txtLastPage.setVisibility(View.INVISIBLE);
            Btn_txtNextPage = (Button) v.findViewById(R.id.Btn_txtNextPage);
            Btn_txtNextPage.setOnClickListener(new buttonOnClickListener());
            Btn_txtNextPage.setVisibility(View.INVISIBLE);

            String str1 = "";
            txtFile = (TextView) v.findViewById(R.id.txtFile);
            txtFile.setText(str1);
            /*获取数据库存储的信息（WL）*/
            syncMeasPoint();

            LV_directory = (ListView) this.v.findViewById(R.id.listview_directory);

            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturnFlow);
            mBtnReturn.setOnClickListener(new btnClick());


            SendManager.SendCmd(componetName + "_目录查询" + "_30_01", S0, 1, 200, new byte[]{0x00, 0x01});
            dataDirectory.dirPath = "/";
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + componetName + "]" + e.toString());
        }
    }


    /**
     * 从数据库同步测量点信息
     */
    private static void syncMeasPoint() {
        arrayListDirectory.clear();
        dataDirectory.arrayListDirectory.clear();
    }


    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class buttonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Btn_rootdirectory: {
                    SendManager.SendCmd(componetName + "_目录查询" + "_30_01", S0, 1, 200, new byte[]{0x00, 0x01});
                    dataDirectoryInit();
                }
                break;
                case R.id.Btn_dirLastPage: {
                    SendManager.SendCmd(componetName + "_目录查询" + "_30_01", S0, 1, 200, new byte[]{0x00, 0x01});
                }
                break;
                case R.id.Btn_dirNextPage: {//dir下一页
                    if (dataDirectory.dirNextEmptyFlag) {
                        //Toast.makeText(context, getString(R.string.fileReadPrompt), Toast.LENGTH_SHORT).show();
                    } else {
                        byte[] dirBale = toByteArray(dataDirectory.dirBaleNum, 2);
                        DataUtil.reverse(dirBale);
                        byte[] arrayOfByte = copybyte(dirBale, charsToBytes(dataDirectory.dirPath, dataDirectory.dirPath.length()));
                        if (dataDirectory.dirPath.equals("/")) {
                            SendManager.SendCmd(componetName + "_目录查询" + "_30_01", S0, 1, 200, arrayOfByte);
                        } else {
                            SendManager.SendCmd(componetName + "_目录查询" + "_30_02", S0, 1, 200, arrayOfByte);
                        }

                        Btn_dirNextPage.setEnabled(false);
                    }
                }
                break;
                case R.id.Btn_txtLastPage: {
                    SendManager.SendCmd(componetName + "_目录查询" + "_30_01", S0, 1, 200, new byte[]{0x00, 0x01});
                }
                break;
                case R.id.Btn_txtNextPage: {//txt下一页
                    if (dataDirectory.txtNextEmptyFlag) {
                        //Toast.makeText(context, getString(R.string.fileReadPrompt), Toast.LENGTH_SHORT).show();
                    } else {
                        byte[] dirBale = toByteArray(dataDirectory.txtBaleNum, 2);
                        DataUtil.reverse(dirBale);
                        byte[] arrayOfByte = copybyte(dirBale, charsToBytes(dataDirectory.txtPath, dataDirectory.txtPath.length()));
                        SendManager.SendCmd(componetName + "_文件查询" + "_30_03", S0, 1, 200, arrayOfByte);
                        Btn_txtNextPage.setEnabled(false);
                    }
                }
                break;
                default: {

                }
                break;
            }
        }
    }

    //endregion
    static private class LVEnergyOnItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView textView = (TextView) view.findViewById(R.id.textFileName);
            String measPoint = textView.getText().toString();
            dataDirectory.dirNextEmptyFlag = false;
            dataDirectory.txtNextEmptyFlag = false;
            if (position < arrayListDirectory.size()) {

            }
            if (position < dataDirectory.arrayListDirectory.size()) {
                Directory directory = dataDirectory.arrayListDirectory.get(position);
                switch (directory.type) {
                    case 1: {//文件夹
                        dataDirectory.dirBaleNum = 1;
                        if (dataDirectory.dirPath.equals("/")) {
                            dataDirectory.dirPath = dataDirectory.dirPath + directory.name;
                        } else {
                            dataDirectory.dirPath = dataDirectory.dirPath + "/" + directory.name;
                        }

                        byte[] dirBale = toByteArray(dataDirectory.dirBaleNum, 2);
                        DataUtil.reverse(dirBale);
                        byte[] arrayOfByte = copybyte(dirBale, charsToBytes(dataDirectory.dirPath, dataDirectory.dirPath.length()));
                        SendManager.SendCmd(componetName + "_目录查询" + "_30_02", S0, 1, 200, arrayOfByte);
                    }
                    break;
                    case 2: {//txt
                        dataDirectory.txtBaleNum = 1;
                        dataDirectory.txtPath = dataDirectory.dirPath;
                        if (dataDirectory.txtPath.equals("/")) {
                            dataDirectory.txtPath = dataDirectory.txtPath + directory.name;
                        } else {
                            dataDirectory.txtPath = dataDirectory.txtPath + "/" + directory.name;
                        }

                        byte[] dirBale = toByteArray(dataDirectory.txtBaleNum, 2);
                        DataUtil.reverse(dirBale);
                        byte[] arrayOfByte = copybyte(dirBale, charsToBytes(dataDirectory.txtPath, dataDirectory.txtPath.length()));
                        SendManager.SendCmd(componetName + "_文件查询" + "_30_03", S0, 1, 200, arrayOfByte);
                    }
                    break;
                    default: {

                    }
                    break;
                }
            }

//            Message fMsg = new Message();
//            fMsg.what = 1;
//            showDirectoryHandler.sendMessage(fMsg);
        }
    }


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {

            }
            break;
            case 2: {
                mCallbacks.onDialogRS();
            }
            break;
            case 3: {
            }
            break;
        }
    }


    @SuppressLint("HandlerLeak")
    static private Handler showDirectoryHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 1: {
                        SendManager.SendCmd(componetName + "_流程查询" + "_30_01", S0, 1, 200, "02");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message fMsg = new Message();
                                fMsg.what = 8;
                                showDirectoryHandler.sendMessage(fMsg);
                            }
                        }).start();
                    }
                    break;
                    case 2: {

                    }
                    break;
                    case 8: {
                        /*显示读取到的流程*/

                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @SuppressLint("HandlerLeak")
    public static Handler receiveDirectoryHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1: {
                        byte[] receive = (byte[]) msg.obj;
                        if (receive.length > 10) {
                            int size = (((short) receive[5] & 0xff) << 8) | (receive[6] & 0xff);//数据长度
                            int baleNum = (((short) receive[8] & 0xff) << 8) | (receive[9] & 0xff);//包号
                            byte identifier = (byte) (receive[10] & 0xff);
                            byte[] dest = new byte[size - 4];
                            arraycopy(receive, 11, dest, 0, size - 4);
                            String detail = convertHexToString(MDBConvert.bytesToHexString(dest));
                            switch (receive[7]) {
                                case 1: {//根目录
                                    Btn_dirNextPage.setEnabled(true);
                                    Btn_rootdirectory.setVisibility(View.INVISIBLE);//屏蔽根目录按钮
                                    Btn_dirLastPage.setVisibility(View.INVISIBLE);  //屏蔽dir按钮
                                    Btn_dirNextPage.setVisibility(View.VISIBLE);  //屏蔽dir按钮
                                    Btn_txtLastPage.setVisibility(View.INVISIBLE);  //屏蔽TXT按钮
                                    Btn_txtNextPage.setVisibility(View.INVISIBLE);  //屏蔽TXT按钮
                                    dataDirectory.dirPath = "/";
                                    dataDirectory.dirSize = size - 3;
                                    dataDirectory.dirBaleNum = baleNum;
                                    dataDirectory.dirFile = detail;
                                    String[] strTemp = dataDirectory.dirFile.split(":");

                                    dataDirectory.arrayListDirectory.clear();
                                    arrayListDirectory.clear();
                                    if (identifier != 0x02) {//内容为空(目录和文件txt为空)
                                        for (String formula : strTemp) {
                                            String dirString[] = formula.split("\\|");
                                            Directory directory = new Directory();
                                            directory.type = Integer.parseInt(dirString[0]);
                                            directory.numBytes = Integer.parseInt(dirString[1]);
                                            directory.name = String.valueOf(dirString[2]);

                                            if (dirString[2].equals("ACTTIME")){//临时使用,最后去掉
                                                dataDirectory.arrayListDirectory.add(directory);
                                                arrayListDirectory.add(dirString);
                                            }

                                        }
                                        if (identifier == 0x01) {//后续是否有包
                                            dataDirectory.dirBaleNum = baleNum + 1;
                                            dataDirectory.dirNextEmptyFlag = false;
                                        } else if (identifier == 0x00) {//后续无包
                                            dataDirectory.dirBaleNum = baleNum;
                                            dataDirectory.dirNextEmptyFlag = true;
                                        }
                                    } else {//当目录为空时,肯定后续无包的

                                    }

                                    for (Directory directory : dataDirectory.arrayListDirectory) {
                                    }
                                    if(lvAdapter == null){
                                        lvAdapter = new DirectoryAdapter(arrayListDirectory, fragment.getActivity());
                                        LV_directory.setAdapter(lvAdapter);
                                    }else{
                                        lvAdapter.UpdateList(arrayListDirectory);
                                    }
                                    //LV_directory.setAdapter(new DirectoryAdapter(arrayListDirectory, fragment.getActivity()));//调用时,就显示
                                    LV_directory.setOnItemClickListener(new LVEnergyOnItemClick());
                                    dataDirectory.txtFile = " ";
                                    txtFile.setText(dataDirectory.txtFile);
                                }
                                break;
                                case 2: {//目录
                                    Btn_dirNextPage.setEnabled(true);
                                    Btn_rootdirectory.setVisibility(View.VISIBLE);//显示根目录按钮
                                    Btn_dirLastPage.setVisibility(View.INVISIBLE);  //屏蔽dir按钮
                                    Btn_dirNextPage.setVisibility(View.VISIBLE);  //屏蔽dir按钮
                                    Btn_txtLastPage.setVisibility(View.INVISIBLE);  //屏蔽TXT按钮
                                    Btn_txtNextPage.setVisibility(View.INVISIBLE);  //屏蔽TXT按钮
                                    dataDirectory.dirSize = size - 3;
                                    dataDirectory.dirBaleNum = baleNum;
                                    dataDirectory.dirFile = detail;
                                    String[] strTemp = dataDirectory.dirFile.split(":");

                                    dataDirectory.arrayListDirectory.clear();
                                    arrayListDirectory.clear();
                                    if (identifier != 0x02) {//内容为空(目录和文件txt为空)
                                        for (String formula : strTemp) {
                                            String dirString[] = formula.split("\\|");
                                            Directory directory = new Directory();
                                            directory.type = Integer.parseInt(dirString[0]);
                                            directory.numBytes = Integer.parseInt(dirString[1]);
                                            directory.name = String.valueOf(dirString[2]);

                                            dataDirectory.arrayListDirectory.add(directory);
                                            arrayListDirectory.add(dirString);
                                        }
                                        if (identifier == 0x01) {//后续是否有包
                                            dataDirectory.dirBaleNum = baleNum + 1;
                                            dataDirectory.dirNextEmptyFlag = false;
                                        } else if (identifier == 0x00) {//后续无包
                                            dataDirectory.dirBaleNum = baleNum;
                                            dataDirectory.dirNextEmptyFlag = true;
                                        }
                                    } else {//当目录为空时,肯定后续无包的

                                    }

                                    for (Directory directory : dataDirectory.arrayListDirectory) {
                                    }
                                    if(lvAdapter == null){
                                        lvAdapter = new DirectoryAdapter(arrayListDirectory, fragment.getActivity());
                                        LV_directory.setAdapter(lvAdapter);
                                    }else{
                                        lvAdapter.UpdateList(arrayListDirectory);
                                    }
                                    //LV_directory.setAdapter(new DirectoryAdapter(arrayListDirectory, fragment.getActivity()));//调用时,就显示
                                    LV_directory.setOnItemClickListener(new LVEnergyOnItemClick());
                                    dataDirectory.txtFile = " ";
                                    txtFile.setText(dataDirectory.txtFile);
                                }
                                break;
                                case 3: {//txt
                                    Btn_txtNextPage.setEnabled(true);
                                    Btn_txtLastPage.setVisibility(View.INVISIBLE);  //屏蔽TXT按钮
                                    Btn_txtNextPage.setVisibility(View.VISIBLE);  //屏蔽TXT按钮
                                    dataDirectory.txtSize = size - 3;

                                    if (dataDirectory.txtBaleNum > 1) {
                                        dataDirectory.txtFile += detail;
                                    } else {
                                        dataDirectory.txtFile = detail;
                                    }

                                    if (identifier == 0x01) {//后续是否有包
                                        dataDirectory.txtBaleNum = baleNum + 1;
                                        dataDirectory.txtNextEmptyFlag = false;
                                    } else {//无包
                                        dataDirectory.txtBaleNum = baleNum;
                                        dataDirectory.txtNextEmptyFlag = true;
                                    }
                                    txtFile.setText(dataDirectory.txtFile);
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(context, fragment.getString(R.string.myDistracted), Toast.LENGTH_SHORT).show();
                dataDirectoryInit();
                arrayListDirectory.clear();
                if(lvAdapter == null){
                    lvAdapter = new DirectoryAdapter(arrayListDirectory, fragment.getActivity());
                    LV_directory.setAdapter(lvAdapter);
                }else{
                    lvAdapter.UpdateList(arrayListDirectory);
                }
                LV_directory.setOnItemClickListener(new LVEnergyOnItemClick());
            }
        }
    };

    /**
     * 初始化文件类属性
     */
    static public void dataDirectoryInit() {
        dataDirectory.dirNextEmptyFlag = false;
        dataDirectory.dirPath = "/";
        dataDirectory.dirSize = 0;      //实际字节大小
        dataDirectory.dirBaleNum = 1;   //包号
        dataDirectory.dirFile = "";     //当前接收内容
        dataDirectory.txtNextEmptyFlag = false;
        dataDirectory.txtPath = "/";
        dataDirectory.txtSize = 0;      //实际字节大小
        dataDirectory.txtBaleNum = 1;   //包号
        dataDirectory.txtFile = "";     //当前接收内容
    }

    /**
     *
     */
    final static class DataDirectory {
        public ArrayList<Directory> arrayListDirectory = new ArrayList<>();
        //左边显示目录
        boolean dirNextEmptyFlag; //true:下次包为空, false:不为空
        String dirPath;           //记录当前路径
        int dirSize;              //实际字节大小
        int dirBaleNum;           //包号
        String dirFile;           //当前接收内容
        //右边显示txt文件
        boolean txtNextEmptyFlag; //true:下次包为空, false:不为空
        String txtPath;           //记录当前路径
        int txtSize;              //实际字节大小
        int txtBaleNum;           //包号
        String txtFile;
    }

    /**
     * 记录文件的性质
     */
    final static class Directory {
        int type;//文件还是文件夹  1:文件夹,2:txt
        int numBytes;//占用的字节数
        String name;//文件名字
    }
}
