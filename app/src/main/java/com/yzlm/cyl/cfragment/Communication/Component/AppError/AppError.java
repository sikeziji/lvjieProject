package com.yzlm.cyl.cfragment.Communication.Component.AppError;

/**
 * Created by WL on 2017/4/8.
 */

public class AppError {

    private int errorNum;
    /*错误名*/
    private String errorName;
    /*错误显示名*/
    private String errorShowName;
    /*错误类型*/
    private String errorCateg;
    /*错误是否显示*/
    private boolean errorIsShow;
    /*错误来源*/
    private boolean errorSrc;
    /*错误产生后到设定时间点是否继续启动做样*/
    private boolean errorIsDoSample;
    /*是否屏蔽报错,在配置文件里增加,现在这个无意义*/
    private boolean errorShield;
    /*是否屏蔽报错,这个跟按钮关联的*/
    private boolean errorHMIShield;

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String name) {
        this.errorName = name;
    }


    public String getErrorShowName() {
        return errorShowName;
    }

    public void setErrorShowName(String showName) {
        this.errorShowName = showName;
    }


    public String getErrorCateg() {
        return errorCateg;
    }

    public void setErrorCateg(String categ) {
        this.errorCateg = categ;
    }


    public boolean getErrorIsShow() {
        return errorIsShow;
    }



    public void setErrorIsShow(boolean isShow) {
        this.errorIsShow = isShow;
    }


    public boolean getErrorSrc() {
        return errorSrc;
    }

    public void setErrorSrc(boolean src) {
        this.errorSrc = src;
    }


    public boolean getErrorIsDoSample() {
        return errorIsDoSample;
    }

    public void setErrorIsDoSample(boolean isDoSample) {
        this.errorIsDoSample = isDoSample;
    }


    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    public boolean getErrorShield() {
        return errorShield;
    }
    public void setErrorShield(boolean errorShield) {
        this.errorShield = errorShield;
    }

    public boolean getErrorHMIShield() {
        return errorHMIShield;
    }
    public void setErrorHMIShield(boolean errorHMIShield) {
        this.errorHMIShield = errorHMIShield;
    }

    /** HMI报错软件配置
     * @param name      寓意名称
     * @param showName  显示名称
     * @param categ     报错类型 ： 信息，警告，错误
     * @param isShow    是否主界面显示
     * @param src       报错源  false HMI  true  测控板
     * @param isDoSample  报错后是否继续自动做样
     */
    public AppError(int errorNum,String name, String showName, String categ, boolean isShow, boolean src, boolean isDoSample) {
        this.errorNum = errorNum;
        this.errorName = name;
        this.errorShowName = showName;
        this.errorCateg = categ;
        this.errorIsShow = isShow;
        this.errorSrc = src;
        this.errorIsDoSample = isDoSample;
    }
    public AppError() {
    }
}
