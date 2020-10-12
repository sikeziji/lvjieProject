package com.yzlm.cyl.cfragment.Thread;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Thread.CThread;

import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.winID;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.strAll5721Component;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;
import static java.lang.Thread.sleep;

/*
 * Created by caoyiliang on 2017/2/8.
 */

public class RealtimeStatus extends CThread {
    public RealtimeStatus(String name, boolean suspend) {
        super(name, suspend);

        SUSPEND_TIME_MILLISECONDS = 3000;
    }


    @Override
    public void process() {


        boolean blUpdate;
        byte[] arrayOfByte = shortToByte((short) 50);
        byte[] arrayOfByte1 = shortToByte((short) 601);
        //byte[] arrayOfByte2 = shortToByte((short) 9504);
        //byte[] arrayOfByte3 = shortToByte((short) 9602);
        reverse(arrayOfByte);
        reverse(arrayOfByte1);
        //reverse(arrayOfByte2);
        //reverse(arrayOfByte3);

        blUpdate = winID != R.id.ImBtn_3;

        if (blUpdate) {
            SUSPEND_TIME_MILLISECONDS = 1100;
            try {

                if (mCompName != null && !mCompName.equals("")) {
                    SendManager.SendCmd(mCompName + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 1, 200, 19);
                    //SendManager.SendCmd(mCompName + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte3, 2).replace(" ", ""), S0, 1, 200, 4);
                    if (QueryMeasCateg(mCompName).equals("3") && mCompName.equals("CODmn") || QueryMeasCateg(mCompName).equals("13")) {
                        SendManager.SendCmd(mCompName + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte1, 2).replace(" ", ""), S0, 1, 200, 1);
                    }
                    /*else if (QueryMeasCateg(mCompName).equals("11")) {
                        SendManager.SendCmd(mCompName + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte2, 2).replace(" ", ""), S0, 1, 200, 1);
                    }*/
                    //该组份有稀释背板的情况下
                    if (isHaveMeasCategory(mCompName, "5")) {
                        sleep(300);
                        SendManager.SendCmd(mCompName + getModuleName("5") + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 1, 200, 15);
                        //SendManager.SendCmd(mCompName + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte1, 2).replace(" ", ""), S0, 1, 200, 1);
                    }
                    //该组份有蒸馏背板的情况下
                    if (isHaveMeasCategory(mCompName, "6")) {
                        sleep(300);
                        SendManager.SendCmd(mCompName + getModuleName("6") + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 1, 200, 15);
                        //SendManager.SendCmd(mCompName + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte1, 2).replace(" ", ""), S0, 1, 200, 1);
                    }
                }
            } catch (Exception e) {
                Log.i("except", e.toString());
            }
        } else {

            SUSPEND_TIME_MILLISECONDS = 3100;

            for (String item : strAll5721Component.get(1)) {
                try {
                    SendManager.SendCmd(item + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 1, 200, 19);
                    //SendManager.SendCmd(mCompName + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte3, 2).replace(" ", ""), S0, 1, 200, 4);
                    if (QueryMeasCateg(item).equals("3") && item.equals("CODmn") || QueryMeasCateg(mCompName).equals("13")) {
                        SendManager.SendCmd(item + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte1, 2).replace(" ", ""), S0, 1, 200, 1);
                    }
                    /*else if (QueryMeasCateg(mCompName).equals("11")) {
                        SendManager.SendCmd(item + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte2, 2).replace(" ", ""), S0, 1, 200, 1);
                    }*/
                    sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestory() {

    }
}
