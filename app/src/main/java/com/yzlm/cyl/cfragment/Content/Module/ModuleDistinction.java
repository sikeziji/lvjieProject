package com.yzlm.cyl.cfragment.Content.Module;

import com.yzlm.cyl.cfragment.Display.Display;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_2;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_3;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_5;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_6;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_7;
import com.yzlm.cyl.cfragment.Frame.Content.Up_content3_lvjie;
import com.yzlm.cyl.cfragment.Frame.PublicContent.Bottom_list_lvjie;
import com.yzlm.cyl.cfragment.R;

import java.io.File;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.isLvjie;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Global.getListAll5721Component;
import static com.yzlm.cyl.cfragment.Global.strAll5721Component;
import static com.yzlm.cyl.cfragment.Global.strComponent;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/11
 */
public class ModuleDistinction {


    /*
     * 获取当前的组分的硬件类型值(WL)
     * "1 常规", "2 W200",
     */
    public static String QueryMeasBoardType(String compName) {
        if (strAll5721Component.size() == 0) {
            getListAll5721Component();
        }
        String curMeasBoardType = "1";

        String[] compNames = strAll5721Component.get(1);
        String[] compMeasBoardType = strAll5721Component.get(7);

        for (int i = 0; i < compNames.length; i++) {
            if (compNames[i].equals(compName)) {
                if (i < compMeasBoardType.length) {
                    curMeasBoardType = compMeasBoardType[i];
                }
            }
        }
        return curMeasBoardType;
    }

    /*
     * 获取当前的组分的测量单元类型值(WL)
     * "1 比色", "2 光谱", "3 滴定", "4 VOC", "5 稀释" "6 蒸馏"  "7 多光谱" "8 注射泵滴定" "9 注射泵比色" "10 AAS平台" "11 TOC平台" "12 注射泵光谱" "13 生物毒性"
     */
    public static String QueryMeasCateg(String compName) {
        if (strAll5721Component.size() == 0) {
            getListAll5721Component();
        }
        String curMeasCateg = "";

        String[] compNames = strAll5721Component.get(1);
        String[] compMeasCategs = strAll5721Component.get(5);

        for (int i = 0; i < compNames.length; i++) {
            if (compNames[i].equals(compName)) {
                curMeasCateg = compMeasCategs[i];
            }
        }
        return curMeasCateg;
    }

    /**
     * 查找指定测量类型的 组分名称
     *
     * @param category
     * @return
     */
    public static String QueryMeasCategoryComponent(String category) {

        String[] compNames = strComponent.get(1);
        String[] compCategory = strComponent.get(5);
        for (int i = 0; i < compCategory.length; i++) {
            if (compCategory[i].equals(category)) {
                return compNames[i];
            }
        }
        return null;
    }


    /*获取模块 名称
     * */
    public static String getModuleName(String moduleCategory) {
        switch (moduleCategory) {
            case "5":
                return "xs";
            case "6":
                return "zl";
        }
        return "";
    }

    /*
      ·判断当前背板是否有指定模块
   *    strModule："5 稀释背板"
   *    strModule："6 蒸馏背板"
   *
   * **/
    public static boolean isHaveMeasCategory(String compName, String strModule) {

        String strMeasCategory = "";
        switch (strModule) {
            case "5":// 稀释模块
                strMeasCategory = QueryMeasCateg(compName + getModuleName(strModule));
                break;
            case "6":// 蒸馏模块
                strMeasCategory = QueryMeasCateg(compName + getModuleName(strModule));
                break;
        }
        return strMeasCategory.equals(strModule);

    }


    /**
     * 获取测量背板的名称
     *
     * @param moduleName     蒸馏稀释背板的名称
     * @param moduleCategory 背板类型（5 ：稀释  6：蒸馏）
     * @return 测量背板名称
     */
    public static String getMeasBordByModuleName(String moduleName, String moduleCategory) {
        return moduleName.split(getModuleName(moduleCategory))[0];
    }


    /**
     * 模块显示运行状态主界面
     *
     * @param compName
     * @return
     */
    private static boolean showModuleUpContent(String compName) {
        return isHaveMeasCategory(compName, "6") ||
                isHaveMeasCategory(compName, "5");
    }

    /**
     * 根据平台显示运行状态主界面
     *
     * @param compName
     */
    public static void showPlatformUpContent(String compName) {

        if (isLvjie) {
            main.getFragment(R.id.fragment_up_container,Up_content3_lvjie.newInstance());
            return;
        }

        if (showModuleUpContent(compName)) {
            main.getFragment(R.id.fragment_up_container, new Up_content3_5());
        } else if (QueryMeasCateg(compName).equals("4")) {
            main.getFragment(R.id.fragment_up_container, new Up_content3_3());
        } else if (QueryMeasCateg(compName).equals("3") || QueryMeasCateg(compName).equals("8")) {
            main.getFragment(R.id.fragment_up_container, new Up_content3_2());
        } else if (QueryMeasCateg(compName).equals("11")) {
            main.getFragment(R.id.fragment_up_container, new Up_content3_6());
        } else if (QueryMeasCateg(compName).equals("13")) {
            main.getFragment(R.id.fragment_up_container, new Up_content3_7());
        } else {
            main.getFragment(R.id.fragment_up_container, new Up_content3());
//            main.addFragment(Display.newInstance(),false,R.id.All, new Frame());
//            main.replaceFragment(Display.newInstance(),false,R.id.fragment_up_container, new Up_content3());
//            main.replaceFragment(Display.newInstance(),false,R.id.fragment_container, Bottom_list.newInstance());
        }
    }

    /**
     * 显示绿洁项目主界面
     */
    public static void showMainContent() {
//        main.getFragment(R.id.fragment_up_container, Display.newInstance());
//        main.getMainFrame(Bottom_list_lvjie.newInstance());
    }


    /**
     * 稀释 、蒸馏 背板  配置文件的路径处理
     *
     * @param compName
     * @param iFile
     * @return
     */
    public static String getFileConfigDir(String compName, int iFile) {
        String fileDir = null;
        switch (iFile) {
            case 0: //syncListFlow
            case 1: //syncListAction
            case 2://syncListValve
            case 4://syncListError
                if (QueryMeasCateg(compName).equals("5")) {
                    fileDir = "Csoft" + File.separator + compName.split(getModuleName("5"))[0] + "/" + "module/" + getModuleName("5") + "/";
                } else if (QueryMeasCateg(compName).equals("6")) {
                    fileDir = "Csoft" + File.separator + compName.split(getModuleName("6"))[0] + "/" + "module/" + getModuleName("6") + "/";
                } else {
                    fileDir = "Csoft" + File.separator + compName + "/";
                }
                break;
            case 3://syncListCalc

                if (QueryMeasCateg(compName).equals("5")) {
                    fileDir = "Csoft" + File.separator + compName.split(getModuleName("5"))[0] + "/" + "module/" + getModuleName("5") + "/";
                } else if (QueryMeasCateg(compName).equals("6")) {

                } else {
                    fileDir = "Csoft" + File.separator + compName + "/";
                }
                break;
            case 5://syncListMeaParCfg
                if (QueryMeasCateg(compName).equals("5")) {

                } else if (QueryMeasCateg(compName).equals("6")) {
                    fileDir = "Csoft" + File.separator + compName.split(getModuleName("6"))[0] + "/" + "module/" + getModuleName("6") + "/";
                } else {
                    fileDir = "Csoft" + File.separator + compName + "/";
                }
                break;
            case 6://syncFileSurplusCfg
                if (QueryMeasCateg(compName).equals("5") || QueryMeasCateg(compName).equals("6")) {
                } else {
                    fileDir = "Csoft" + File.separator + compName + "/";
                }
                break;
        }
        return fileDir;

    }

    /**
     * 无背板组分的参数
     *
     * @param FComponent
     * @param noBoardComp
     * @param iFile
     * @return
     */
    public static String getNoBoardFileConfigDir(String FComponent, String noBoardComp, int iFile) {
        String fileDir = null;
        switch (iFile) {
            case 3://syncListCalc
            case 4://syncNoBoardListError
                fileDir = "Csoft" + File.separator + FComponent + "/" + "noBoard/" + noBoardComp + "/";
                break;
        }
        return fileDir;
    }

}
