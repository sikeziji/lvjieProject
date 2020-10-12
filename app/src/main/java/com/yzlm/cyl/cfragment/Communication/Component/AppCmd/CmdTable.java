package com.yzlm.cyl.cfragment.Communication.Component.AppCmd;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoyiliang on 2017/2/10. CK 03命令
 */

public class CmdTable {
    private Map<Integer, Cmd> Lcmd = new HashMap<>();
    public Date startActionTime;
    public DoSampleMode doSampleMode;

    public enum DoSampleMode {
        手动,
        自动
    }

    public CmdTable() {

        Cmd<?> Temp;
        Lcmd.put(0, new Cmd<Float>(0, "温度K", "float", Float.SIZE));
        Lcmd.put(1, new Cmd<Float>(1, "温度B", "float", Float.SIZE));
        Lcmd.put(2, Temp = new Cmd<String>(2, "测控板A版本号", "String", 32 * 8));
        Temp.setLen(8);
        Lcmd.put(10, Temp = new Cmd<String>(10, "测控板B版本号", "String", 32 * 8));
        Temp.setLen(8);
        Lcmd.put(18, Temp = new Cmd<String>(18, "磁导计量版本号", "String", 32 * 8));
        Temp.setLen(8);

        Lcmd.put(26, Temp = new Cmd<String>(26, "计量板版本号", "String", 32 * 8));
        Temp.setLen(8);

        Lcmd.put(34, Temp = new Cmd<String>(34, "ORP电极板版本号", "String", 32 * 8));
        Temp.setLen(8);

        Lcmd.put(50, new Cmd<Byte>(50, "平台", "byte", Byte.SIZE));//没用
        Lcmd.put(51, new Cmd<Byte>(51, "当前状态", "byte", Byte.SIZE));//1.显示主界面，实时刷新
        Lcmd.put(52, new Cmd<Short>(52, "当前动作", "short", Short.SIZE));//1.存第一次最后一次时间在查看运行日志2.显示主界面+秒
        Lcmd.put(53, new Cmd<Short>(53, "下一动作", "short", Short.SIZE));//1.显示主界面，实时刷新
        Lcmd.put(54, new Cmd<Byte>(54, "报错状态", "byte", Byte.SIZE));//1.存储在日志2.显示主界面
        Lcmd.put(55, new Cmd<Float>(55, "温度", "float", Float.SIZE));//1.显示主界面，实时刷新
        Lcmd.put(601, new Cmd<Float>(601, "内温温度", "float", Float.SIZE));//内温温度
        Lcmd.put(56, new Cmd<Integer>(56, "低计量", "int", Integer.SIZE));//1.显示主界面，实时刷新
        Lcmd.put(57, new Cmd<Integer>(57, "高计量", "int", Integer.SIZE));//1.显示主界面，实时刷新
        Lcmd.put(58, new Cmd<Short>(58, "阀状态", "short", Short.SIZE));//1.显示主界面，实时刷新
        Lcmd.put(59, new Cmd<Integer>(59, "LED1能量", "int", Integer.SIZE));//1.显示主界面，实时刷新
        Lcmd.put(60, new Cmd<Byte>(60, "测量完成标志位", "byte", Byte.SIZE));
        Lcmd.put(61, new Cmd<Byte>(61, "剩余步骤", "byte", Byte.SIZE));
        Lcmd.put(62, new Cmd<Short>(62, "当前动作耗时", "short", Short.SIZE));

        //改
        Lcmd.put(63, new Cmd<Short>(63, "当前流程步骤数", "short", Short.SIZE));
        Lcmd.put(64, new Cmd<Short>(64, "ORP电极", "int", Integer.SIZE));
        Lcmd.put(65, new Cmd<Short>(65, "ORP电极差值", "int", Integer.SIZE));
        Lcmd.put(66, new Cmd<Short>(66, "实时体积", "float", Float.SIZE));
        Lcmd.put(67, new Cmd<Byte>(67, "开门状态标志位", "byte", Byte.SIZE));
        Lcmd.put(68, new Cmd<Byte>(68, "LED2参比能量", "int", Integer.SIZE));
        // 改
        for (int i = 0; i < 32; i++) {
            Lcmd.put(100 + i, new Cmd<Integer>(100 + i, "错误" + (1 + i * 32) + "-" + (32 + i * 32), "int", Integer.SIZE));
        }
        Lcmd.put(200, new Cmd(200, "读取随机码", "int", Integer.SIZE));
        Lcmd.put(201, new Cmd(201, "设备延保状态", "byte", Byte.SIZE));

        Lcmd.put(202, new Cmd(202, "出厂时间，年", "short", Short.SIZE));
        Lcmd.put(203, new Cmd(203, "出厂时间，月", "byte", Byte.SIZE));
        Lcmd.put(204, new Cmd(204, "出厂时间，日", "byte", Byte.SIZE));

        Lcmd.put(205, new Cmd(205, "延保设置时间，年", "short", Short.SIZE));
        Lcmd.put(206, new Cmd(206, "延保设置时间，月", "byte", Byte.SIZE));
        Lcmd.put(207, new Cmd(207, "延保设置时间，日", "byte", Byte.SIZE));

        Lcmd.put(208, new Cmd(208, "延保设置时间，日", "byte", Byte.SIZE));
        Lcmd.put(209, new Cmd(209, "延保倒计时间，日", "short", Short.SIZE));

        // 改  计量板能量
        for (int i = 0; i < 12; i++) {
            Lcmd.put(270 + i, new Cmd<Integer>(270 + i, "计量板1" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(282 + i, new Cmd<Integer>(282 + i, "计量板2" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(294 + i, new Cmd<Integer>(294 + i, "计量板3" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(306 + i, new Cmd<Integer>(306 + i, "计量板4" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(318 + i, new Cmd<Integer>(318 + i, "计量板5" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(330 + i, new Cmd<Integer>(330 + i, "计量板6" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(342 + i, new Cmd<Integer>(342 + i, "计量板7" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(354 + i, new Cmd<Integer>(354 + i, "计量板8" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(366 + i, new Cmd<Integer>(366 + i, "计量板9" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(378 + i, new Cmd<Integer>(378 + i, "计量板10" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(390 + i, new Cmd<Integer>(390 + i, "计量板11" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(402 + i, new Cmd<Integer>(402 + i, "计量板12" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(414 + i, new Cmd<Integer>(414 + i, "计量板13" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(426 + i, new Cmd<Integer>(426 + i, "计量板14" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(438 + i, new Cmd<Integer>(438 + i, "计量板15" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(450 + i, new Cmd<Integer>(450 + i, "计量板16" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(462 + i, new Cmd<Integer>(462 + i, "计量板17" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(474 + i, new Cmd<Integer>(474 + i, "计量板18" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(486 + i, new Cmd<Integer>(486 + i, "计量板19" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        for (int i = 0; i < 12; i++) {
            Lcmd.put(498 + i, new Cmd<Integer>(498 + i, "计量板20" + "第" + (i + 1) + "路能量", "short", Short.SIZE));
        }
        Lcmd.put(550, new Cmd(550, "流程CRC校验值", "short", Short.SIZE));
        Lcmd.put(551, new Cmd(551, "年", "short", Short.SIZE));
        Lcmd.put(552, new Cmd(552, "月", "byte", Byte.SIZE));
        Lcmd.put(553, new Cmd(553, "日", "byte", Byte.SIZE));
        Lcmd.put(554, new Cmd(554, "时", "byte", Byte.SIZE));
        Lcmd.put(555, new Cmd(555, "分", "byte", Byte.SIZE));
        Lcmd.put(556, new Cmd(556, "秒", "byte", Byte.SIZE));
        Lcmd.put(557, new Cmd(557, "兼容版本号", "int", Integer.SIZE));

        Lcmd.put(558, new Cmd(558, "配置流程CRC校验值", "short", Short.SIZE));
        Lcmd.put(559, new Cmd(559, "配置流程年", "short", Short.SIZE));
        Lcmd.put(560, new Cmd(560, "配置流程月", "byte", Byte.SIZE));
        Lcmd.put(561, new Cmd(561, "配置流程日", "byte", Byte.SIZE));
        Lcmd.put(562, new Cmd(562, "配置流程时", "byte", Byte.SIZE));
        Lcmd.put(563, new Cmd(563, "配置流程分", "byte", Byte.SIZE));
        Lcmd.put(564, new Cmd(564, "配置流程秒", "byte", Byte.SIZE));

        Lcmd.put(600, new Cmd(600, "注射泵位置", "byte", Byte.SIZE));
        Lcmd.put(602, new Cmd(602, "旋转阀位置", "byte", Byte.SIZE));    // TOC

        Lcmd.put(620, new Cmd(620, "电极能量", "int", Integer.SIZE));
        Lcmd.put(621, new Cmd(621, "电极能量年", "short", Short.SIZE));
        Lcmd.put(622, new Cmd(622, "电极能量月", "byte", Byte.SIZE));
        Lcmd.put(623, new Cmd(623, "电极能量日", "byte", Byte.SIZE));
        Lcmd.put(624, new Cmd(624, "电极能量时", "byte", Byte.SIZE));
        Lcmd.put(625, new Cmd(625, "电极能量分", "byte", Byte.SIZE));
        Lcmd.put(626, new Cmd(626, "电极能量秒", "byte", Byte.SIZE));

        Lcmd.put(1010, new Cmd(1010, "1号阀下计量空管光强", "int", Integer.SIZE));
        Lcmd.put(1011, new Cmd(1011, "1号阀上计量空管光强", "int", Integer.SIZE));
        Lcmd.put(1012, new Cmd(1012, "1号阀下计量阈值", "short", Short.SIZE));
        Lcmd.put(1013, new Cmd(1013, "1号阀上计量阈值", "short", Short.SIZE));

        Lcmd.put(1014, new Cmd(1014, "2号阀下计量空管光强", "int", Integer.SIZE));
        Lcmd.put(1015, new Cmd(1015, "2号阀上计量空管光强", "int", Integer.SIZE));
        Lcmd.put(1016, new Cmd(1016, "2号阀下计量阈值", "short", Short.SIZE));
        Lcmd.put(1017, new Cmd(1017, "2号阀上计量阈值", "short", Short.SIZE));

        Lcmd.put(1026, new Cmd(1026, "5号阀下计量空管光强", "int", Integer.SIZE));
        Lcmd.put(1027, new Cmd(1027, "5号阀上计量空管光强", "int", Integer.SIZE));
        Lcmd.put(1028, new Cmd(1028, "5号阀下计量阈值", "short", Short.SIZE));
        Lcmd.put(1029, new Cmd(1029, "5号阀上计量阈值", "short", Short.SIZE));

        Lcmd.put(1034, new Cmd(1034, "7号阀下计量空管光强", "int", Integer.SIZE));
        Lcmd.put(1035, new Cmd(1035, "7号阀上计量空管光强", "int", Integer.SIZE));
        Lcmd.put(1036, new Cmd(1036, "7号阀下计量阈值", "short", Short.SIZE));
        Lcmd.put(1037, new Cmd(1037, "7号阀上计量阈值", "short", Short.SIZE));
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2110 + 100 * i, new Cmd(2110 + 100 * i, (i + 1) + "号阀快抽速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2111 + 100 * i, new Cmd(2111 + 100 * i, (i + 1) + "号阀进样速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2112 + 100 * i, new Cmd(2112 + 100 * i, (i + 1) + "号阀回排速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2113 + 100 * i, new Cmd(2113 + 100 * i, (i + 1) + "号阀回排上抽速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2114 + 100 * i, new Cmd(2114 + 100 * i, (i + 1) + "号阀冲洗速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2115 + 100 * i, new Cmd(2115 + 100 * i, (i + 1) + "号阀排消解管速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2116 + 100 * i, new Cmd(2116 + 100 * i, (i + 1) + "号阀排计量管速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2117 + 100 * i, new Cmd(2117 + 100 * i, (i + 1) + "号阀下计量进样点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2118 + 100 * i, new Cmd(2118 + 100 * i, (i + 1) + "号阀上计量进样点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2119 + 100 * i, new Cmd(2119 + 100 * i, (i + 1) + "号阀下计量冲洗点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2120 + 100 * i, new Cmd(2120 + 100 * i, (i + 1) + "号阀上计量冲洗点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2121 + 100 * i, new Cmd(2121 + 100 * i, (i + 1) + "号阀下计量回排点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2122 + 100 * i, new Cmd(2122 + 100 * i, (i + 1) + "号阀上计量回排点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2123 + 100 * i, new Cmd(2123 + 100 * i, (i + 1) + "号阀下计量回排后上抽点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2124 + 100 * i, new Cmd(2124 + 100 * i, (i + 1) + "号阀上计量回排后上抽点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2125 + 100 * i, new Cmd(2125 + 100 * i, (i + 1) + "号阀上计量快抽时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2126 + 100 * i, new Cmd(2126 + 100 * i, (i + 1) + "号阀下计量快速回排时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2127 + 100 * i, new Cmd(2127 + 100 * i, (i + 1) + "号阀上计量快速回排时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2128 + 100 * i, new Cmd(2128 + 100 * i, (i + 1) + "号阀微排阀岛时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2129 + 100 * i, new Cmd(2129 + 100 * i, (i + 1) + "号阀下计量排空时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2130 + 100 * i, new Cmd(2130 + 100 * i, (i + 1) + "号阀上计量排空时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2131 + 100 * i, new Cmd(2131 + 100 * i, (i + 1) + "号阀下计量进消解管时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(2132 + 100 * i, new Cmd(2132 + 100 * i, (i + 1) + "号阀上计量进消解管时间", "short", Short.SIZE));
        }

        Lcmd.put(5300, new Cmd(5300, "消解温度", "short", Short.SIZE));
        Lcmd.put(5301, new Cmd(5301, "消解温度2", "short", Short.SIZE));
        Lcmd.put(5302, new Cmd(5302, "消解时长", "short", Short.SIZE));
        Lcmd.put(5303, new Cmd(5303, "消解时长2", "short", Short.SIZE));
        Lcmd.put(5304, new Cmd(5304, "降温温度", "short", Short.SIZE));
        Lcmd.put(5305, new Cmd(5305, "降温温度2", "short", Short.SIZE));
        Lcmd.put(5306, new Cmd(5306, "开采样时间", "short", Short.SIZE));
        Lcmd.put(5307, new Cmd(5307, "显色静置", "short", Short.SIZE));
        Lcmd.put(5308, new Cmd(5308, "等待静置", "short", Short.SIZE));
        Lcmd.put(5309, new Cmd(5309, "无用的", "short", Short.SIZE));//没用的,可以理解字节对齐,目的是为了5310和5311能够连贯解析;增加人:sun
        Lcmd.put(5310, new Cmd(5310, "校准消解温度", "short", Short.SIZE));
        Lcmd.put(5311, new Cmd(5311, "校准消解时长", "short", Short.SIZE));
        Lcmd.put(5312, new Cmd(5312, "开采样时间2", "short", Short.SIZE));

        Lcmd.put(5111, new Cmd(5111, "消解管有液体报错空管光强", "int", Integer.SIZE));
        Lcmd.put(5112, new Cmd(5112, "消解管有液体报错阈值", "short", Short.SIZE));

        Lcmd.put(5114, new Cmd(5114, "年", "int", Integer.SIZE));
        Lcmd.put(5115, new Cmd(5115, "月", "int", Integer.SIZE));
        Lcmd.put(5116, new Cmd(5116, "日", "int", Integer.SIZE));
        Lcmd.put(5117, new Cmd(5117, "时", "int", Integer.SIZE));
        Lcmd.put(5118, new Cmd(5118, "分", "int", Integer.SIZE));
        Lcmd.put(5119, new Cmd(5119, "秒", "int", Integer.SIZE));

        Lcmd.put(5121, new Cmd(5121, "光谱地址", "short", Short.SIZE));
        Lcmd.put(5122, new Cmd(5122, "光谱积分时间", "short", Short.SIZE));
        Lcmd.put(5123, new Cmd(5123, "平均次数", "short", Short.SIZE));
        Lcmd.put(5124, new Cmd(5124, "暗电流处理", "short", Short.SIZE));
        Lcmd.put(5125, new Cmd(5125, "打灯电压", "short", Short.SIZE));
        Lcmd.put(5126, new Cmd(5126, "打灯次数", "short", Short.SIZE));
        Lcmd.put(5127, new Cmd(5127, "暗电流补偿", "short", Short.SIZE));
        Lcmd.put(5128, new Cmd(5128, "光谱校正k", "float", Float.SIZE));
        Lcmd.put(5129, new Cmd(5129, "光谱校正b", "float", Float.SIZE));
        Lcmd.put(5130, new Cmd(5130, "光谱测量波长数", "short", Short.SIZE));

        for (int i = 0; i < 20; i++) {
            Lcmd.put(5150 + 2 * i, new Cmd(5150 + 2 * i, "光谱波长" + (i + 1) + "起始点", "short", Short.SIZE));
            Lcmd.put(5150 + (2 * i) + 1, new Cmd(5150 + (2 * i) + 1, "光谱波长" + (i + 1) + "结束点", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(5190 + i, new Cmd(5190 + i, "光谱波长" + (i + 1) + "打灯电压", "byte", Byte.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(5210 + i, new Cmd(5210 + i, "光谱波长" + (i + 1) + "打灯次数", "byte", Byte.SIZE));
        }

        Lcmd.put(5500, new Cmd(5500, "LED增益", "byte", Byte.SIZE));

        //
        for (int i = 0; i < 6; i++) {
            Lcmd.put(5600 + i, new Cmd(5600 + i, "下计量第" + (i + 1) + "路计量能量", "int", Integer.SIZE));
        }
        for (int i = 0; i < 6; i++) {
            Lcmd.put(5606 + i, new Cmd(5606 + i, "上计量第" + (i + 1) + "路计量能量", "int", Integer.SIZE));
        }
        for (int i = 0; i < 3; i++) {
            Lcmd.put(5620 + i, new Cmd(5620 + i, "下计量" + (i + 1) + "测量点", "short", Short.SIZE));
        }
        for (int i = 0; i < 3; i++) {
            Lcmd.put(5623 + i, new Cmd(5623 + i, "上计量" + (i + 1) + "测量点", "short", Short.SIZE));
        }
        Lcmd.put(5700, new Cmd(5700, "CKB编号", "int", Integer.SIZE));
        Lcmd.put(5702, new Cmd(5702, "BS_LED_SWITCH", "int", Integer.SIZE));
        Lcmd.put(5703, new Cmd(5703, "配置流程开关", "byte", Byte.SIZE));

        Lcmd.put(5710, new Cmd(5710, "一滴滴定液体积", "float", Float.SIZE));
        Lcmd.put(5711, new Cmd(5711, "滴定终点判定方式", "byte", Byte.SIZE));   // 0:ORP 1：LED
        Lcmd.put(5712, new Cmd(5712, "还原剂体积", "float", Float.SIZE));// 注射泵滴定 2019年10月24日15:26:58
        Lcmd.put(5713, new Cmd(5713, "催化剂体积", "float", Float.SIZE));//注射泵滴定 2019年10月24日15:26:59
        Lcmd.put(5714, new Cmd(5714, "催化剂体积2", "float", Float.SIZE));//注射泵滴定 2019年11月5日11:59:20
        Lcmd.put(5715, new Cmd(5715, "氧化剂冲洗体积", "float", Float.SIZE));
        Lcmd.put(5716, new Cmd(5716, "还原剂冲洗体积", "float", Float.SIZE));
        Lcmd.put(5717, new Cmd(5717, "催化剂冲洗体积", "float", Float.SIZE));
        Lcmd.put(5718, new Cmd(5718, "碱液体积", "float", Float.SIZE));//注射泵滴定 2020年8月3日10:28:29
        Lcmd.put(5719, new Cmd(5719, "碱液冲洗体积", "float", Float.SIZE));//注射泵滴定 2020年8月3日10:28:29

        Lcmd.put(5720, new Cmd(5720, "滴定转速", "short", Short.SIZE));
        Lcmd.put(5721, new Cmd(5721, "填充转速", "short", Short.SIZE));
        Lcmd.put(5722, new Cmd(5722, "终点后执行滴数", "byte", Byte.SIZE));
        Lcmd.put(5723, new Cmd(5723, "校准等待时间S", "short", Short.SIZE));
        Lcmd.put(5724, new Cmd(5724, "最小滴定体积", "short", Short.SIZE));   // 废弃  2018年12月6日17:37:44
        Lcmd.put(5725, new Cmd(5725, "最大滴定体积", "short", Short.SIZE));   // 废弃  2018年12月6日17:37:53
        Lcmd.put(5726, new Cmd(5726, "最小等待时间", "short", Short.SIZE));    // 注射泵滴定：设置滴定A阶段间隔 2019年9月9日15:53:05
        Lcmd.put(5727, new Cmd(5727, "最大等待时间", "short", Short.SIZE));    // 注射泵滴定：设置滴定B阶段间隔 2019年9月9日15:53:13
        Lcmd.put(5728, new Cmd(5728, "滴定液计数阈值", "short", Short.SIZE));
        Lcmd.put(5729, new Cmd(5729, "△E判断终点阈值", "short", Short.SIZE));
        Lcmd.put(5730, new Cmd(5730, "ORP电极电位判断阈值", "short", Short.SIZE));
        Lcmd.put(5731, new Cmd(5731, "最小滴定体积", "float", Float.SIZE));
        Lcmd.put(5732, new Cmd(5732, "最大滴定体积", "float", Float.SIZE));
        Lcmd.put(5733, new Cmd(5733, "滴落阈值", "short", Short.SIZE));
        Lcmd.put(5734, new Cmd(5734, "滴定间隔", "float", Float.SIZE));         // 注射泵滴定： 氧化剂体积 2019年9月9日15:57:19
        Lcmd.put(5735, new Cmd(5735, "内环冲温温度(消解5)", "float", Float.SIZE));
        Lcmd.put(5736, new Cmd(5736, "内外保温温度(消解5)", "float", Float.SIZE));
        Lcmd.put(5737, new Cmd(5737, "保温效率1(消解5)", "float", Float.SIZE));
        Lcmd.put(5738, new Cmd(5738, "保温效率2(消解5)", "float", Float.SIZE));
        Lcmd.put(5739, new Cmd(5739, "保温效率3(消解5)", "float", Float.SIZE));
        Lcmd.put(5740, new Cmd(5740, "内环冲温温度(消解6)", "float", Float.SIZE));
        Lcmd.put(5741, new Cmd(5741, "内环保温温度(消解6)", "float", Float.SIZE));
        Lcmd.put(5742, new Cmd(5742, "保温效率1(消解6)", "float", Float.SIZE));
        Lcmd.put(5743, new Cmd(5743, "保温效率3(消解6)", "float", Float.SIZE));
        Lcmd.put(5744, new Cmd(5744, "保温效率3(消解6)", "float", Float.SIZE));
        Lcmd.put(5745, new Cmd(5745, "滴定计时A", "short", Short.SIZE));        // 注射泵滴定： 设置A阶段步长 2019年9月9日16:00:14
        Lcmd.put(5746, new Cmd(5746, "滴定计时B", "short", Short.SIZE));        // 注射泵滴定： 设置B阶段步长 2019年9月9日16:00:14
        Lcmd.put(5747, new Cmd(5747, "滴定参比光强上限", "int", Integer.SIZE)); // 注射泵滴定： 设置A阶段滴数 2019年9月9日16:00:14
        Lcmd.put(5748, new Cmd(5748, "滴定参比光强下限", "int", Integer.SIZE));
        Lcmd.put(5749, new Cmd(5749, "微量模式", "byte", Byte.SIZE));           // 注射泵滴定：设置 1 开 0关
        for (int i = 0; i < 20; i++) {
            Lcmd.put(5750 + i, new Cmd(5750 + i, "计量板" + (i + 1) + "设备地址", "byte", Byte.SIZE));
        }

        for (int i = 0; i < 20 * 4; i += 4) {
            Lcmd.put(5800 + i, new Cmd(5800 + i, "计量板1下计量空管光强", "short", Short.SIZE));
            Lcmd.put(5801 + i, new Cmd(5801 + i, "计量板1上计量空管光强", "short", Short.SIZE));
            Lcmd.put(5802 + i, new Cmd(5802 + i, "计量板1下计量阈值", "short", Short.SIZE));
            Lcmd.put(5803 + i, new Cmd(5803 + i, "计量板1下计量阈值", "short", Short.SIZE));
        }

        for (int i = 0; i < 20; i++) {
            Lcmd.put(6100 + 100 * i, new Cmd(6100 + 100 * i, "计量板" + (i + 1) + "快抽速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6101 + 100 * i, new Cmd(6101 + 100 * i, "计量板" + (i + 1) + "进样速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6102 + 100 * i, new Cmd(6102 + 100 * i, "计量板" + (i + 1) + "号阀回排速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6103 + 100 * i, new Cmd(6103 + 100 * i, "计量板" + (i + 1) + "号阀回排上抽速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6104 + 100 * i, new Cmd(6104 + 100 * i, "计量板" + (i + 1) + "号阀冲洗速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6105 + 100 * i, new Cmd(6105 + 100 * i, "计量板" + (i + 1) + "号阀排消解管速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6106 + 100 * i, new Cmd(6106 + 100 * i, "计量板" + (i + 1) + "号阀排计量管速度", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6107 + 100 * i, new Cmd(6107 + 100 * i, "计量板" + (i + 1) + "号阀下计量进样点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6108 + 100 * i, new Cmd(6108 + 100 * i, "计量板" + (i + 1) + "号阀上计量进样点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6109 + 100 * i, new Cmd(6109 + 100 * i, "计量板" + (i + 1) + "号阀下计量冲洗点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6110 + 100 * i, new Cmd(6110 + 100 * i, "计量板" + (i + 1) + "号阀上计量冲洗点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6111 + 100 * i, new Cmd(6111 + 100 * i, "计量板" + (i + 1) + "号阀下计量回排点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6112 + 100 * i, new Cmd(6112 + 100 * i, "计量板" + (i + 1) + "号阀上计量回排点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6113 + 100 * i, new Cmd(6113 + 100 * i, "计量板" + (i + 1) + "号阀下计量回排后上抽点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6114 + 100 * i, new Cmd(6114 + 100 * i, "计量板" + (i + 1) + "号阀上计量回排后上抽点数", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6115 + 100 * i, new Cmd(6115 + 100 * i, "计量板" + (i + 1) + "号阀上计量快抽时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6116 + 100 * i, new Cmd(6116 + 100 * i, "计量板" + (i + 1) + "号阀下计量快速回排时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6117 + 100 * i, new Cmd(6117 + 100 * i, "计量板" + (i + 1) + "号阀上计量快速回排时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6118 + 100 * i, new Cmd(6118 + 100 * i, "计量板" + (i + 1) + "号阀微排阀岛时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6119 + 100 * i, new Cmd(6119 + 100 * i, "计量板" + (i + 1) + "号阀下计量排空时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6120 + 100 * i, new Cmd(6120 + 100 * i, "计量板" + (i + 1) + "号阀上计量排空时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6121 + 100 * i, new Cmd(6121 + 100 * i, "计量板" + (i + 1) + "号阀下计量进消解管时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6122 + 100 * i, new Cmd(6122 + 100 * i, "计量板" + (i + 1) + "号阀上计量进消解管时间", "short", Short.SIZE));
        }

        for (int i = 0; i < 20; i++) {
            Lcmd.put(6123 + 100 * i, new Cmd(6123 + 100 * i, "计量板" + (i + 1) + "下计量超时时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6124 + 100 * i, new Cmd(6124 + 100 * i, "计量板" + (i + 1) + "上计量超时时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6125 + 100 * i, new Cmd(6125 + 100 * i, "计量板" + (i + 1) + "润洗时间", "short", Short.SIZE));
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6126 + 100 * i, new Cmd(6126 + 100 * i, "计量板" + (i + 1) + "高计量点数", "short", Short.SIZE));// 2020年4月6日10:48:09
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(6127 + 100 * i, new Cmd(6127 + 100 * i, "计量板" + (i + 1) + "低计量点数", "short", Short.SIZE));// 2020年4月6日10:48:09
        }
        for (int i = 0; i < 20; i++) {
            Lcmd.put(8100 + 8 * i, Temp = new Cmd<String>(8100 + 8 * i, "计量板" + (i + 1) + "版本号", "String", 32 * 8));
            Temp.setLen(8);
        }

        // VOC 组份
        Lcmd.put(9000, new Cmd(9000, "加压时的终点压力值PO", "short", Short.SIZE));
        Lcmd.put(9001, new Cmd(9001, "加压时压力阈值a", "short", Short.SIZE));
        Lcmd.put(9002, new Cmd(9002, "进样时的终点压力值P0", "short", Short.SIZE));
        Lcmd.put(9003, new Cmd(9003, "进样时的压力阈值a", "short", Short.SIZE));
        Lcmd.put(9004, new Cmd(9004, "加压准备时间T1", "short", Short.SIZE));
        Lcmd.put(9005, new Cmd(9005, "关载气2时间T2", "short", Short.SIZE));
        Lcmd.put(9006, new Cmd(9006, "排废时间T3", "short", Short.SIZE));
        Lcmd.put(9007, new Cmd(9007, "氮气吹扫时间T4", "short", Short.SIZE));
        Lcmd.put(9008, new Cmd(9008, "电压与压力的K值", "float", Float.SIZE));
        Lcmd.put(9009, new Cmd(9009, "电压与压力的B值", "float", Float.SIZE));
        Lcmd.put(9010, new Cmd(9010, "压力传感器压力值", "int", Float.SIZE));

        Lcmd.put(9100, new Cmd(9100, "TN自动寻峰参数值 标1浓度值", "float", Float.SIZE));
        Lcmd.put(9101, new Cmd(9101, "TN自动寻峰参数值 标2浓度值", "float", Float.SIZE));
        Lcmd.put(9102, new Cmd(9102, "TN自动寻峰参数值 △C值", "float", Float.SIZE));
        Lcmd.put(9103, new Cmd(9103, "TN自动寻峰参数值 水样浓度值", "float", Float.SIZE));

        Lcmd.put(9200, new Cmd(9200, "报错屏蔽1", "short", Short.SIZE));
        Lcmd.put(9201, new Cmd(9201, "报错屏蔽2", "short", Short.SIZE));
        Lcmd.put(9202, new Cmd(9202, "报错屏蔽3", "short", Short.SIZE));
        Lcmd.put(9203, new Cmd(9203, "报错屏蔽4", "short", Short.SIZE));
        Lcmd.put(9204, new Cmd(9204, "报错屏蔽5", "short", Short.SIZE));
        Lcmd.put(9205, new Cmd(9205, "报错屏蔽6", "short", Short.SIZE));
        Lcmd.put(9206, new Cmd(9206, "报错屏蔽7", "short", Short.SIZE));
        Lcmd.put(9207, new Cmd(9207, "报错屏蔽8", "short", Short.SIZE));
        Lcmd.put(9208, new Cmd(9208, "报错屏蔽9", "short", Short.SIZE));
        Lcmd.put(9209, new Cmd(9209, "报错屏蔽10", "short", Short.SIZE));
        Lcmd.put(9210, new Cmd(9210, "报错屏蔽11", "short", Short.SIZE));
        Lcmd.put(9211, new Cmd(9211, "报错屏蔽12", "short", Short.SIZE));
        Lcmd.put(9212, new Cmd(9212, "报错屏蔽13", "short", Short.SIZE));
        Lcmd.put(9213, new Cmd(9213, "报错屏蔽14", "short", Short.SIZE));
        Lcmd.put(9214, new Cmd(9214, "报错屏蔽15", "short", Short.SIZE));
        Lcmd.put(9215, new Cmd(9215, "报错屏蔽16", "short", Short.SIZE));
        Lcmd.put(9216, new Cmd(9216, "报错屏蔽17", "short", Short.SIZE));
        Lcmd.put(9217, new Cmd(9217, "报错屏蔽18", "short", Short.SIZE));
        Lcmd.put(9218, new Cmd(9218, "报错屏蔽19", "short", Short.SIZE));
        Lcmd.put(9219, new Cmd(9219, "报错屏蔽20", "short", Short.SIZE));

        Lcmd.put(9220, new Cmd(9220, "消解阈值", "short", Float.SIZE));
        Lcmd.put(9221, new Cmd(9221, "平均时间", "short", Float.SIZE));
        Lcmd.put(9222, new Cmd(9222, "智能消解开关", "byte", Byte.SIZE));

        Lcmd.put(9300, new Cmd(9300, "催化剂缺液-LED阈值", "int", Integer.SIZE));  // 注射泵滴定
        Lcmd.put(9301, new Cmd(9301, "氧化剂缺液-LED阈值", "int", Integer.SIZE));
        Lcmd.put(9302, new Cmd(9302, "还原剂缺液-LED阈值", "int", Integer.SIZE));

        //挥发酚参数
        Lcmd.put(9400, new Cmd(9400, "缓冲剂冲洗体积", "float", Float.SIZE));
        Lcmd.put(9401, new Cmd(9401, "缓冲剂体积", "float", Float.SIZE));
        Lcmd.put(9402, new Cmd(9402, "显色剂冲洗体积", "float", Float.SIZE));
        Lcmd.put(9403, new Cmd(9403, "显色剂体积", "float", Float.SIZE));
        Lcmd.put(9404, new Cmd(9404, "催化剂冲洗体积", "float", Float.SIZE));
        Lcmd.put(9405, new Cmd(9405, "催化剂体积", "float", Float.SIZE));

        // TOC 参数
        Lcmd.put(9500, new Cmd(9500, "上升温度", "short", Short.SIZE));
        Lcmd.put(9501, new Cmd(9501, "下降温度", "short", Short.SIZE));
        Lcmd.put(9502, new Cmd(9502, "测量次数", "short", Short.SIZE));
        Lcmd.put(9503, new Cmd(9503, "MFC流量值", "float", Float.SIZE));
        Lcmd.put(9504, new Cmd(9504, "TC流量实时值", "float", Float.SIZE));

        Lcmd.put(9510, new Cmd(9510, "IC吹扫时间", "short", Short.SIZE));
        Lcmd.put(9511, new Cmd(9511, "TC吹扫时间", "short", Short.SIZE));
        Lcmd.put(9512, new Cmd(9512, "采集时间", "short", Short.SIZE));
        Lcmd.put(9513, new Cmd(9513, "清洗3体积", "float", Float.SIZE));
        Lcmd.put(9514, new Cmd(9514, "混合抽取体积", "float", Float.SIZE));

        Lcmd.put(9530, new Cmd(9530, "量程1进样间隔时间", "short", Short.SIZE));
        Lcmd.put(9531, new Cmd(9531, "量程1进样次数", "short", Short.SIZE));
        Lcmd.put(9532, new Cmd(9532, "量程1填充体积", "float", Float.SIZE));
        Lcmd.put(9533, new Cmd(9533, "量程1单次进样体积", "float", Float.SIZE));

        Lcmd.put(9540, new Cmd(9540, "量程2进样间隔时间", "short", Short.SIZE));
        Lcmd.put(9541, new Cmd(9541, "量程2进样次数", "short", Short.SIZE));
        Lcmd.put(9542, new Cmd(9542, "量程2填充体积", "float", Float.SIZE));
        Lcmd.put(9543, new Cmd(9543, "量程2单次进样体积", "float", Float.SIZE));

        Lcmd.put(9550, new Cmd(9540, "量程3进样间隔时间", "short", Short.SIZE));
        Lcmd.put(9551, new Cmd(9541, "量程3进样次数", "short", Short.SIZE));
        Lcmd.put(9552, new Cmd(9542, "量程3填充体积", "float", Float.SIZE));
        Lcmd.put(9553, new Cmd(9543, "量程3单次进样体积", "float", Float.SIZE));

        Lcmd.put(9600, new Cmd(9600, "历史记录时间模式", "int", Integer.SIZE));
        Lcmd.put(9601, new Cmd(9601, "比色储存按钮", "int", Integer.SIZE));
        Lcmd.put(9602, new Cmd(9602, "吸收低电平能量", "int", Integer.SIZE));
        Lcmd.put(9603, new Cmd(9603, "吸收高电平能量", "int", Integer.SIZE));
        Lcmd.put(9604, new Cmd(9604, "参比低电平能量", "int", Integer.SIZE));
        Lcmd.put(9605, new Cmd(9605, "参比高电平能量", "int", Integer.SIZE));

        Lcmd.put(9800, new Cmd(9800, "清洗体积", "short", Short.SIZE));
        Lcmd.put(9801, new Cmd(9801, "润洗体积", "short", Short.SIZE));
        Lcmd.put(9802, new Cmd(9802, "菌液体积", "short", Short.SIZE));
        Lcmd.put(9803, new Cmd(9803, "填充体积", "short", Short.SIZE));
        Lcmd.put(9804, new Cmd(9804, "回排体积", "short", Short.SIZE));
        Lcmd.put(9805, new Cmd(9805, "水样体积", "short", Short.SIZE));
        Lcmd.put(9806, new Cmd(9806, "复苏体积", "short", Short.SIZE));
        Lcmd.put(9807, new Cmd(9807, "盐水体积", "short", Short.SIZE));
        Lcmd.put(9808, new Cmd(9808, "顶空体积", "short", Short.SIZE));
        Lcmd.put(9809, new Cmd(9809, "毒性测试时间", "short", Short.SIZE));
        Lcmd.put(9810, new Cmd(9810, "参比阈值", "int", Integer.SIZE));
        Lcmd.put(9811, new Cmd(9811, "吸收阈值", "int", Integer.SIZE));
        Lcmd.put(9812, new Cmd(9812, "实时读取温度按钮", "byte", Byte.SIZE));
        Lcmd.put(9813, new Cmd(9813, "当前通道倍数", "byte", Byte.SIZE));
        Lcmd.put(9814, new Cmd(9814, "缓冲体积", "short", Short.SIZE));
        Lcmd.put(9815, new Cmd(9815, "对照体积", "short", Short.SIZE));

        Lcmd.put(10000, new Cmd(10000, "进样时间1", "short", Short.SIZE));
        Lcmd.put(10001, new Cmd(10001, "进样时间2", "short", Short.SIZE));
        Lcmd.put(10002, new Cmd(10002, "冲洗时间1", "short", Short.SIZE));
        Lcmd.put(10003, new Cmd(10003, "冲洗时间2", "short", Short.SIZE));
        Lcmd.put(10004, new Cmd(10004, "初始装液时间1", "short", Short.SIZE));
        Lcmd.put(10005, new Cmd(10005, "初始装液时间2", "short", Short.SIZE));
    }

    public Cmd getCmd(int index) {
        return Lcmd.get(index);
    }

    public void setCmd(int index, Cmd cmd) {
        Lcmd.put(index, cmd);
    }

    public void clearState() {
        Lcmd.get(60).setValue(0x00);
    }
}
