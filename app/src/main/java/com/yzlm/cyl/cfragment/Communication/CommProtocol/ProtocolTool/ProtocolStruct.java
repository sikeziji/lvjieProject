package com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool;

public class ProtocolStruct {

    private String mn = "";      //当前MN号
    private String code = "";    //当前污染物编码
    private String compName = "";//组分名称
    private String nowProtocol = "";// 当前协议
    private String password = "";
    boolean blQnRtn = true;// 正常状态下的QnRtn数据帧返回

    private String QnTime = "";
    private String QnPassword = "";
    private String QnMn = "";
    private String QnCode = "";  //请求指令编码

    private String QnCn = "";
    private String QnST = "";
    private String QnFlag = "";
    private String QnRtn = "";
    private String QnExe = "";


    public void setNowProtocol(String nowProtocol) {
        this.nowProtocol = nowProtocol;
    }

    public String getNowProtocol() {
        return nowProtocol;
    }

    public void setQnTime(String qnTime) {
        QnTime = qnTime;
    }

    public String getQnTime() {
        return QnTime;
    }

    public void setQnMn(String qnMn) {
        QnMn = qnMn;
    }

    public String getQnMn() {
        return QnMn;
    }

    public void setQnPassword(String qnPassword) {
        QnPassword = qnPassword;
    }

    public String getQnPassword() {
        return QnPassword;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompName() {
        return compName;
    }

    ///////////////////
    public void setQnCode(String qnCode) {
        QnCode = qnCode;
    }

    public String getQnCode() {
        return QnCode;
    }

    public void setQnCn(String qnCn) {
        QnCn = qnCn;
    }

    public void setQnFlag(String qnFlag) {
        QnFlag = qnFlag;
    }

    public void setQnST(String qnST) {
        QnST = qnST;
    }

    public String getQnCn() {
        return QnCn;
    }

    public String getQnFlag() {
        return QnFlag;
    }

    public String getQnST() {
        return QnST;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setQnRtn(String qnRtn) {
        QnRtn = qnRtn;
    }

    public String getQnRtn() {
        return QnRtn;
    }

    public void setQnExe(String qnExe) {
        QnExe = qnExe;
    }

    public String getQnExe() {
        return QnExe;
    }
}
