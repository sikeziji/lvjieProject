package com.yzlm.cyl.cfragment.Communication.SerialPort.YangChuang;

public class YcApi {
    com.ychmi.sdk.YcApi ycapi;

    private static YcApi fragment = null;

    public static YcApi newInstance() {
        if (fragment == null) {
            fragment = new YcApi();
        }
        return fragment;
    }

    public YcApi(){
        ycapi = new com.ychmi.sdk.YcApi();
    }

    public void HideNviBarFull(){
        ycapi.HideNviBarFull();
    }

    public void ShowNviBarFull(){
        ycapi.ShowNviBarFull();
    }
}
