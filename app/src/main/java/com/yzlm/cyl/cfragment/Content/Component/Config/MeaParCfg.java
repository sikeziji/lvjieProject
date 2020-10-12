package com.yzlm.cyl.cfragment.Content.Component.Config;

/**
 * Created by zwj on 2017/7/10.
 */

public class MeaParCfg {

    private String name;    // 界面关联名称
    private String showName;// 显示名称
    private String strUp;   // 参数上限
    private String strDown; // 参数下限
    private String strDefValue; // 参数默认值

    public void setPar(String name,String down,String up,String defValue){
        this.name = name;
        strUp = up;
        strDown = down;
        strDefValue = defValue;
    }

    public String getName(){return this.name;}
    public String getParUp() {
        return strUp;
    }
    public String getParDown(){
        return strDown;
    }
    public String getStrDefValue(){return strDefValue;}

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return showName;
    }
}
