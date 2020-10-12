package com.yzlm.cyl.cfragment.Communication.Component.AppCalc;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.context;

/**
 * Created by caoyiliang on 2017/3/14.
 */
public class Calc {
    private String A_Formula = "log(E1/E2)";
    private String C_Formula = "K*A+B";
    private String K_Formula;
    private String B_Formula;
    private String F_Formula;
    private String H_Formula;
    private String D_Formula;
    private Kbf mKbf;
    private List<Map> list;

    public Calc() {
        mKbf = new Kbf(context);
    }

    public String getA_Formula() {
        return A_Formula;
    }

    public void setA_Formula(String a_Formula) {
        A_Formula = a_Formula;
    }

    public String getC_Formula() {
        return C_Formula;
    }

    public void setC_Formula(String c_Formula) {
        C_Formula = c_Formula;
    }

    public String getK_Formula() {
        return K_Formula;
    }

    public void setK_Formula(String k_Formula) {
        K_Formula = k_Formula;
    }

    public String getB_Formula() {
        return B_Formula;
    }

    public void setB_Formula(String b_Formula) {
        B_Formula = b_Formula;
    }

    public String getF_Formula() {
        return F_Formula;
    }

    public void setF_Formula(String f_Formula) {
        F_Formula = f_Formula;
    }

    public String getH_Formula() {
        return H_Formula;
    }

    public void setH_Formula(String h_Formula) {
        H_Formula = h_Formula;
    }

    public String getD_Formula() {
        return D_Formula;
    }

    public void setD_Formula(String d_Formula) {
        D_Formula = d_Formula;
    }

    //E为正整数,A,K,B有小数点和正负的数;c为带小数点的整数或者0.
    private double A_Old;
    private double K_Old;
    private double B_Old;
    private double C_Old;
    private double A;
    private double K = 1;
    private double B = 0;
    private double C;
    private double H;//抑制率(生物毒性)
    private double D;//(生物毒性)

    /* 标1流程的A值(WL)*/
    private double Calib1A;
    /* 标2流程的A值(WL)*/
    private double Calib2A;
    /* 标2流程的F值(WL)*/
    private double F = 1;

    public double getCalib1A() {
        return Calib1A;
    }

    public double getCalib2A() {
        return Calib2A;
    }

    public void setCalib1A(double r1c1) {
        Calib1A = r1c1;
    }

    public void setCalib2A(double r1c2) {
        Calib2A = r1c2;
    }

    public void setF(double r1f) {
        F = r1f;
    }

    public double getA_Old() {
        return A_Old;
    }

    public double getK_Old() {
        return K_Old;
    }

    public double getB_Old() {
        return B_Old;
    }

    public double getC_Old() {
        return C_Old;
    }


    public void setA(double a) {
        A_Old = A;
        A = a;
    }


    public void setK(double k) {
        K_Old = K;
        K = k;
    }

    public void setB(double b) {
        B_Old = B;
        B = b;
    }

    public void setC(double c) {
        C_Old = C;
        C = c;
    }

    public void setH(double h) {
        H = h;
    }

    public void setD(double d) {
        D = d;
    }


    public double getA() {
        return A;
    }

    public double getC() {
        return C;
    }

    /*获取计算的F**/
    public double getCalF() {
        return F;
    }


    public double getK(String range, String comp) {

        list = mKbf.select(comp, range, null, null, 0, 1);
        if (list != null) {
            try {
                return Double.parseDouble(list.get(0).get("K").toString());
            } catch (Exception e) {
                return K;
            }
        } else {
            return K;
        }
    }

    public double getB(String range, String comp) {
        list = mKbf.select(comp, range, null, null, 0, 1);
        if (list != null) {
            try {
                return Double.parseDouble(list.get(0).get("B").toString());
            } catch (Exception e) {
                return B;
            }
        } else {
            return B;
        }
    }

    public double getF(String range, String comp) {
        list = mKbf.select(comp, range, null, null, 0, 1);
        if (list != null) {
            try {
                return Double.parseDouble(list.get(0).get("F").toString());
            } catch (Exception e) {
                return F;
            }
        } else {
            return F;
        }
    }

    public double getH() {
        return H;
    }

    public double getD() {
        return D;
    }

}
