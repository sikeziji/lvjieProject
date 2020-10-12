package com.yzlm.cyl.cfragment.Communication.CommProtocol;

import android.util.Log;

import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.protocolDisplayList;
import static com.yzlm.cyl.cfragment.Global.protocolDisplayName;
import static com.yzlm.cyl.cfragment.Global.protocolDisplayNameOrder;
import static com.yzlm.cyl.cfragment.Global.protocolList;

public class protocolFunc {

    public static void getProtocolShowName() {
        if (protocolDisplayList == null) {
            protocolDisplayList = new String[protocolList.length];
            System.arraycopy(protocolList, 0, protocolDisplayList, 0, protocolList.length);
            for (int i = 0; i < protocolList.length; i++) {
                if (protocolDisplayName.get(protocolList[i]) != null) {
                    protocolDisplayList[i] = protocolDisplayName.get(protocolList[i]);
                }
            }
            String[] strOrderDisplay = protocolDisplayNameOrder.split(",");
            int num = 0;
            for (int i = 0; i < strOrderDisplay.length; i++) {
                for (int j = 0; j < protocolDisplayList.length; j++) {
                    if (protocolDisplayList[j].equals(strOrderDisplay[i]) && (num + 1) < protocolDisplayList.length) {
                        if((num + 1) != j){
                            String strTemp = protocolDisplayList[num + 1];
                            protocolDisplayList[num + 1] = protocolDisplayList[j];
                            protocolDisplayList[j] = strTemp;

                            String strTemp2 = protocolList[num + 1];
                            protocolList[num + 1] = protocolList[j];
                            protocolList[j] = strTemp2;
                        }
                        num++;
                    }
                }
            }
        }
    }
}
