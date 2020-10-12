package com.yzlm.cyl.cfragment.Communication.Component.AppCmd;

import java.util.ArrayList;

/**
 * 测控板命令寄存器起始地址表（WL）CK  06 命令
 * Created by WL on 2017/3/31.
 */
public class RegAddr {
    private static ArrayList<byte[]> regAddrList = new ArrayList<>();

    public RegAddr() {
    }

    public static void initRegAddrList() {
        regAddrList.clear();
        /* 时间（年月日时分秒）寄存器起始地址，索引为0（WL）*/
        regAddrList.add(new byte[]{0x13, (byte) 0xFA});
        /* 5号阀的计量位寄存器起始地址，索引为1（WL）*/
        regAddrList.add(new byte[]{0x04, 0x02});
        /* 7号阀的计量位寄存器起始地址，索引为2（WL）*/
        regAddrList.add(new byte[]{0x04, 0x0A});
        /* 2号阀的计量位寄存器起始地址，索引为3（WL）*/
        regAddrList.add(new byte[]{0x03, (byte) 0xF6});
        /* 消解温度寄存器起始地址，索引为4（WL）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xB4});
        /* 消解时长寄存器起始地址，索引为5（WL）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xB6});
        /* 显色温度寄存器起始地址，索引为6（WL）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xB5});
        /* 显色时长寄存器起始地址，索引为7（WL）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xB7});
        /* 光谱地址寄存器起始地址，索引为8（WL）*/
        regAddrList.add(new byte[]{0x14, 0x01});
        /* 光谱积分时间寄存器起始地址，索引为9（WL）*/
        regAddrList.add(new byte[]{0x14, 0x02});
        /* 平均次数寄存器起始地址，索引为10（WL）*/
        regAddrList.add(new byte[]{0x14, 0x03});
        /* 暗电流处理寄存器起始地址，索引为11（WL）*/
        regAddrList.add(new byte[]{0x14, 0x04});
        /* 打灯电压寄存器起始地址，索引为12（WL）*/
        regAddrList.add(new byte[]{0x14, 0x05});
        /* 打灯次数寄存器起始地址，索引为13（WL）*/
        regAddrList.add(new byte[]{0x14, 0x06});
        /* 暗电流补偿寄存器起始地址，索引为14（WL）*/
        regAddrList.add(new byte[]{0x14, 0x07});
        /* 光谱校准系数K寄存器起始地址，索引为15（WL）*/
        regAddrList.add(new byte[]{0x14, 0x08});
        /* 光谱校准系数B寄存器起始地址，索引为16（WL）*/
        regAddrList.add(new byte[]{0x14, 0x09});
        /* 波长1起始点寄存器起始地址，索引为17（WL）*/
        regAddrList.add(new byte[]{0x14, 0x1E});
        /* 波长2起始点寄存器起始地址，索引为18（WL）*/
        regAddrList.add(new byte[]{0x14, 0x20});
        /* 波长3起始点寄存器起始地址，索引为19（WL）*/
        regAddrList.add(new byte[]{0x14, 0x22});
        /* 波长4起始点寄存器起始地址，索引为20（WL）*/
        regAddrList.add(new byte[]{0x14, 0x24});
        /* 波长5起始点寄存器起始地址，索引为21（WL）*/
        regAddrList.add(new byte[]{0x14, 0x26});
        /* 波长6起始点寄存器起始地址，索引为22（WL）*/
        regAddrList.add(new byte[]{0x14, 0x28});
        /* 波长7起始点寄存器起始地址，索引为23（WL）*/
        regAddrList.add(new byte[]{0x14, 0x2A});
        /* 波长8起始点寄存器起始地址，索引为24（WL）*/
        regAddrList.add(new byte[]{0x14, 0x2C});
        /* 波长9起始点寄存器起始地址，索引为25（WL）*/
        regAddrList.add(new byte[]{0x14, 0x2E});
        /* 波长10起始点寄存器起始地址，索引为26（WL）*/
        regAddrList.add(new byte[]{0x14, 0x30});
        /* 波长11起始点寄存器起始地址，索引为27（WL）*/
        regAddrList.add(new byte[]{0x14, 0x32});
        /* 波长12起始点寄存器起始地址，索引为28（WL）*/
        regAddrList.add(new byte[]{0x14, 0x34});
        /* 波长13起始点寄存器起始地址，索引为29（WL）*/
        regAddrList.add(new byte[]{0x14, 0x36});
        /* 波长14起始点寄存器起始地址，索引为30（WL）*/
        regAddrList.add(new byte[]{0x14, 0x38});
        /* 波长15起始点寄存器起始地址，索引为31（WL）*/
        regAddrList.add(new byte[]{0x14, 0x3A});
        /* 波长16起始点寄存器起始地址，索引为32（WL）*/
        regAddrList.add(new byte[]{0x14, 0x3C});
        /* 波长17起始点寄存器起始地址，索引为33（WL）*/
        regAddrList.add(new byte[]{0x14, 0x3E});
        /* 波长18起始点寄存器起始地址，索引为34（WL）*/
        regAddrList.add(new byte[]{0x14, 0x40});
        /* 波长19起始点寄存器起始地址，索引为35（WL）*/
        regAddrList.add(new byte[]{0x14, 0x42});
        /* 波长20起始点寄存器起始地址，索引为36（WL）*/
        regAddrList.add(new byte[]{0x14, 0x44});
        /* 光谱测量波长数寄存器起始地址，索引为37（WL）*/
        regAddrList.add(new byte[]{0x14, 0x0A});
        /* 设备通讯地址寄存器起始地址，索引为38（WL）*/
        regAddrList.add(new byte[]{0x14, 0x00});
        /* 1号阀的计量参数寄存器起始地址，索引为39（WL）*/
        regAddrList.add(new byte[]{0x08, 0x3E});
        /* 2号阀的计量参数寄存器起始地址，索引为40（WL）*/
        regAddrList.add(new byte[]{0x08, (byte) 0xA2});
        /* 3号阀的计量参数寄存器起始地址，索引为41（WL）*/
        regAddrList.add(new byte[]{0x09, 0x06});
        /* 4号阀的计量参数寄存器起始地址，索引为42（WL）*/
        regAddrList.add(new byte[]{0x09, 0x6A});
        /* 5号阀的计量参数寄存器起始地址，索引为43（WL）*/
        regAddrList.add(new byte[]{0x09, (byte) 0xCE});
        /* 6号阀的计量参数寄存器起始地址，索引为44（WL）*/
        regAddrList.add(new byte[]{0x0A, 0x32});
        /* 7号阀的计量参数寄存器起始地址，索引为45（WL）*/
        regAddrList.add(new byte[]{0x0A, (byte) 0x96});
        /* 8号阀的计量参数寄存器起始地址，索引为46（WL）*/
        regAddrList.add(new byte[]{0x0A, (byte) 0xFA});
        /* 9号阀的计量参数寄存器起始地址，索引为47（WL）*/
        regAddrList.add(new byte[]{0x0B, 0x5E});
        /* 10号阀的计量参数寄存器起始地址，索引为48（WL）*/
        regAddrList.add(new byte[]{0x0B, (byte) 0xC2});
        /* 11号阀的计量参数寄存器起始地址，索引为49（WL）*/
        regAddrList.add(new byte[]{0x0C, 0x26});
        /* 12号阀的计量参数寄存器起始地址，索引为50（WL）*/
        regAddrList.add(new byte[]{0x0C, (byte) 0x8A});
        /* 13号阀的计量参数寄存器起始地址，索引为51（WL）*/
        regAddrList.add(new byte[]{0x0C, (byte) 0xEE});
        /* 14号阀的计量参数寄存器起始地址，索引为52（WL）*/
        regAddrList.add(new byte[]{0x0D, 0x52});
        /* 15号阀的计量参数寄存器起始地址，索引为53（WL）*/
        regAddrList.add(new byte[]{0x0D, (byte) 0xB6});
        /* 16号阀的计量参数寄存器起始地址，索引为54（WL）*/
        regAddrList.add(new byte[]{0x0E, 0x1A});
        /* 17号阀的计量参数寄存器起始地址，索引为55（WL）*/
        regAddrList.add(new byte[]{0x0E, 0x7E});
        /* 18号阀的计量参数寄存器起始地址，索引为56（WL）*/
        regAddrList.add(new byte[]{0x0E, (byte) 0xE2});
        /* 19号阀的计量参数寄存器起始地址，索引为57（WL）*/
        regAddrList.add(new byte[]{0x0F, 0x46});
        /* 20号阀的计量参数寄存器起始地址，索引为58（WL）*/
        regAddrList.add(new byte[]{0x0F, (byte) 0xAA});
        /* 开采样时间寄存器起始地址，索引为59（WL）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xBA});
        /* 消解管有液体报错空管光强寄存器起始地址，索引为60（WL）*/
        regAddrList.add(new byte[]{0x13, (byte) 0xF7});
        /* 消解管有液体报错阈值寄存器起始地址，索引为61（WL）*/
        regAddrList.add(new byte[]{0x13, (byte) 0xF8});
        /* 消解降温寄存器起始地址，索引为62（Z）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xB8});
        /* 显色降温寄存器起始地址，索引为63（Z）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xB9});
        /* 显色静置寄存器起始地址，索引为64（Z）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xBB});
        /* 组份起始地址，索引为65（Z）*/
        regAddrList.add(new byte[]{0x13, (byte) 0xF9});
        /*LED 增益    索引为 66 (Z)*/
        regAddrList.add(new byte[]{0x15, (byte) 0x7C});
        /*测控板编号    索引为 67 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x44});
        // 改
        /*计量板1地址    索引为 68 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x76});
        /*计量板2地址    索引为 69 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x77});
        /*计量板3地址    索引为 70 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x78});
        /*计量板4地址    索引为 71 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x79});
        /*计量板5地址    索引为 72 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x7A});
        /*计量板6地址    索引为 73 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x7B});
        /*计量板7地址    索引为 74 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x7C});
        /*计量板8地址    索引为 75 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x7D});
        /*计量板9地址    索引为 76 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x7E});
        /*计量板10地址    索引为 77 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x7F});
        /*计量板11地址    索引为 78 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x80});
        /*计量板12地址    索引为 79 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x81});
        /*计量板13地址    索引为 80 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x82});
        /*计量板14地址    索引为 81 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x83});
        /*计量板15地址    索引为 82 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x84});
        /*计量板16地址    索引为 83 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x85});
        /*计量板17地址    索引为 84 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x86});
        /*计量板18地址    索引为 85 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x87});
        /*计量板19地址    索引为 86 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x88});
        /*计量板20地址    索引为 87 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x89});
        // 改
        /*计量板1计量光强阈值    索引为 88 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xA8});
        /*计量板2计量光强阈值    索引为 89 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xAC});
        /*计量板3计量光强阈值    索引为 90 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xB0});
        /*计量板4计量光强阈值    索引为 91 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xB4});
        /*计量板5计量光强阈值    索引为 92 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xB8});
        /*计量板6计量光强阈值    索引为 93 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xBC});
        /*计量板7计量光强阈值    索引为 94 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xC0});
        /*计量板8计量光强阈值    索引为 95 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xC4});
        /*计量板9计量光强阈值    索引为 96 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xC8});
        /*计量板10计量光强阈值    索引为 97 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xCC});
        /*计量板11计量光强阈值    索引为 98 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xD0});
        /*计量板12计量光强阈值    索引为 99 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xD4});
        /*计量板13计量光强阈值    索引为 100 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xD8});
        /*计量板14计量光强阈值    索引为 101 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xDC});
        /*计量板15计量光强阈值    索引为 102 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xE0});
        /*计量板16计量光强阈值    索引为 103 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xE4});
        /*计量板17计量光强阈值    索引为 104 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xE8});
        /*计量板18计量光强阈值    索引为 105 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xEC});
        /*计量板19计量光强阈值    索引为 106 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xF0});
        /*计量板20计量光强阈值    索引为 107 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0xF4});
        // 改
        /* 计量板1的计量参数寄存器起始地址，索引为108（Z）*/
        regAddrList.add(new byte[]{0x17, (byte) 0xD4});
        /* 计量板2的计量参数寄存器起始地址，索引为109（Z）*/
        regAddrList.add(new byte[]{0x18, (byte) 0x38});
        /* 计量板3的计量参数寄存器起始地址，索引为110（Z）*/
        regAddrList.add(new byte[]{0x18, (byte) 0x9C});
        /* 计量板4的计量参数寄存器起始地址，索引为111（Z）*/
        regAddrList.add(new byte[]{0x19, 0x00});
        /* 计量板5的计量参数寄存器起始地址，索引为112（Z）*/
        regAddrList.add(new byte[]{0x19, 0x64});
        /* 计量板6的计量参数寄存器起始地址，索引为113（Z）*/
        regAddrList.add(new byte[]{0x19, (byte) 0xC8});
        /* 计量板7的计量参数寄存器起始地址，索引为114（Z）*/
        regAddrList.add(new byte[]{0x1A, (byte) 0x2C});
        /* 计量板8的计量参数寄存器起始地址，索引为115（Z）*/
        regAddrList.add(new byte[]{0x1A, (byte) 0x90});
        /* 计量板9的计量参数寄存器起始地址，索引为116（Z）*/
        regAddrList.add(new byte[]{0x1A, (byte) 0xF4});
        /* 计量板10的计量参数寄存器起始地址，索引为117（Z）*/
        regAddrList.add(new byte[]{0x1B, (byte) 0x58});
        /* 计量板11的计量参数寄存器起始地址，索引为118（Z）*/
        regAddrList.add(new byte[]{0x1B, (byte) 0xBC});
        /* 计量板12的计量参数寄存器起始地址，索引为119（Z）*/
        regAddrList.add(new byte[]{0x1C, (byte) 0x20});
        /* 计量板13的计量参数寄存器起始地址，索引为120（Z）*/
        regAddrList.add(new byte[]{0x1C, (byte) 0x84});
        /* 计量板14的计量参数寄存器起始地址，索引为121（Z）*/
        regAddrList.add(new byte[]{0x1C, (byte) 0xE8});
        /* 计量板15的计量参数寄存器起始地址，索引为122（Z）*/
        regAddrList.add(new byte[]{0x1D, (byte) 0x4C});
        /* 计量板16的计量参数寄存器起始地址，索引为123（Z）*/
        regAddrList.add(new byte[]{0x1D, (byte) 0xB0});
        /* 计量板17的计量参数寄存器起始地址，索引为124（Z）*/
        regAddrList.add(new byte[]{0x1E, (byte) 0x14});
        /* 计量板18的计量参数寄存器起始地址，索引为125（Z）*/
        regAddrList.add(new byte[]{0x1E, (byte) 0x78});
        /* 计量板19的计量参数寄存器起始地址，索引为126（Z）*/
        regAddrList.add(new byte[]{0x1E, (byte) 0xDC});
        /* 计量板20的计量参数寄存器起始地址，索引为127（Z）*/
        regAddrList.add(new byte[]{0x1F, (byte) 0x40});
        /*滴定参数 滴定转速    索引为 128 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x58});
        /*滴定参数 填充转速    索引为 129 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x59});
        /*滴定参数 终点执行滴数    索引为 130 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x5A});
        /*滴定参数 校准等待时间    索引为 131 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x5B});
        /*滴定参数 最小滴定体积    索引为 132 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x5C}); //废弃  2018年12月6日17:34:14
        /*滴定参数 最大滴定体积    索引为 133 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x5D}); //废弃  2018年12月6日17:34:22
        /*滴定参数 最小等待时间    索引为 134 (Z)*/      // 注射泵:     设置滴定A阶段间隔 2019年9月9日15:50:01
        regAddrList.add(new byte[]{0x16, (byte) 0x5E});
        /*滴定参数 最大等待时间    索引为 135 (Z)*/      //  注射泵:    设置滴定B阶段间隔 2019年9月9日15:50:16
        regAddrList.add(new byte[]{0x16, (byte) 0x5F});
        /*滴定参数 滴定液计数阈值    索引为 136 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x60});
        /*滴定参数 终点判断阈值    索引为 137 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x61});
        /*滴定测量参数 滴定液体积    索引为 138 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x4E});
        /************VOC 测量参数***********/
        /*VOC测量参数 加压时的终点压力值    索引为 139 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x28});
        /*VOC测量参数 加压时的终点压力阈值    索引为 140 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x29});
        /*VOC测量参数 进压时的终点压力值    索引为 141 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x2A});
        /*VOC测量参数 进压时的终点压力阈值    索引为 142 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x2B});
        /*VOC测量参数 加压时准备时间T1    索引为 143 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x2C});
        /*VOC测量参数 关载气2时间T2    索引为 144 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x2D});
        /*VOC测量参数 排废时间T3    索引为 145 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x2E});
        /*VOC测量参数 氮气吹扫时间T4    索引为 146 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x2F});
        /*VOC测量参数 电压与压力的K值    索引为 147 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x30});
        /*VOC测量参数 电压与压力的B值    索引为 148 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x31});
        /*VOC测量参数 压力传感器压力值   索引为 149 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x32});
        /*VOC测量参数 压力传感器AD值   索引为 150 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x33});
        /*TN寻峰*/
        /*TN自动寻峰参数值 标1浓度值  索引为 151 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x8C});
        /*TN自动寻峰参数值 标2浓度值  索引为 152 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x8D});
        /*TN自动寻峰参数值 △C值  索引为 153 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x8E});
        /*TN自动寻峰参数值 水样浓度值  索引为 154(Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x8F});
        /*TN自动寻峰参数值 光谱寻峰成功标志位  索引为 155 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0x90});
        /*滴定测量参数 ORP滴定判断阈值    索引为 156 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x62});
        /*滴定参数 最小滴定体积    索引为 157 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x63});
        /*滴定参数 最大滴定体积    索引为 158 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x64});
        /* 等待静置寄存器起始地址，索引为159（Z）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xBC});
        /*滴定参数 滴定间隔    索引为 160 (Z)*/            //  注射泵: 设置滴定氧化剂体积  2019年9月9日16:13:28
        regAddrList.add(new byte[]{0x16, (byte) 0x66});
        /*滴定温控参数 内环冲温温度（消解5）    索引为 161 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x67});
        /*滴定温控参数 内环保温温度（消解5）    索引为 162 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x68});
        /*滴定温控参数 保温效率1（消解5）    索引为 163 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x69});
        /*滴定温控参数 保温效率2（消解5）    索引为 164 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x6A});
        /*滴定温控参数 保温效率3（消解5）    索引为 165 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x6B});
        /*滴定温控参数 内环冲温温度（消解6）    索引为 166 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x6C});
        /*滴定温控参数 内环保温温度（消解6）    索引为 167 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x6D});
        /*滴定温控参数 保温效率1（消解6）    索引为 168 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x6E});
        /*滴定温控参数 保温效率2（消解6）    索引为 169 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x6F});
        /*滴定温控参数 保温效率3（消解6）    索引为 170 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x70});
        /*BS_LED 开关    索引为 171 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x46});


        /*波长1 打灯电压    索引为 172 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x46});
        /*波长2 打灯电压    索引为 173 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x47});
        /*波长3 打灯电压    索引为 174 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x48});
        /*波长4 打灯电压    索引为 175 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x49});
        /*波长5 打灯电压    索引为 176 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x4A});
        /*波长6 打灯电压    索引为 177 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x4B});
        /*波长7 打灯电压    索引为 178 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x4C});
        /*波长8 打灯电压    索引为 179 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x4D});
        /*波长9 打灯电压    索引为 180 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x4E});
        /*波长10 打灯电压    索引为 181 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x4F});
        /*波长11 打灯电压    索引为 182 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x50});
        /*波长12 打灯电压    索引为 183 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x51});
        /*波长13 打灯电压    索引为 184 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x52});
        /*波长14 打灯电压    索引为 185 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x53});
        /*波长15 打灯电压    索引为 186 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x54});
        /*波长16 打灯电压    索引为 187 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x55});
        /*波长17 打灯电压    索引为 188 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x56});
        /*波长18 打灯电压    索引为 189 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x57});
        /*波长19 打灯电压    索引为 190 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x58});
        /*波长20 打灯电压    索引为 191 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x59});


        /*波长1 打灯次数    索引为 192 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x5A});
        /*波长2 打灯次数    索引为 193 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x5B});
        /*波长3 打灯次数    索引为 194 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x5C});
        /*波长4 打灯次数    索引为 195 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x5D});
        /*波长5 打灯次数    索引为 196 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x5E});
        /*波长6 打灯次数    索引为 197 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x5F});
        /*波长7 打灯次数    索引为 198 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x60});
        /*波长8 打灯次数    索引为 199 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x61});
        /*波长9 打灯次数    索引为 200 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x62});
        /*波长10 打灯次数    索引为 201 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x63});
        /*波长11 打灯次数    索引为 202 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x64});
        /*波长12 打灯次数    索引为 203 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x65});
        /*波长13 打灯次数    索引为 204 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x66});
        /*波长14 打灯次数    索引为 205 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x67});
        /*波长15 打灯次数    索引为 206 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x68});
        /*波长16 打灯次数    索引为 207 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x69});
        /*波长17 打灯次数    索引为 208 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x6A});
        /*波长18 打灯次数    索引为 209 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x6B});
        /*波长19 打灯次数    索引为 210 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x6C});
        /*波长20 打灯次数    索引为 211 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0x6D});
        /*滴定计时A    索引为 212 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x71});     //  注射泵: 设置A阶段步长    2019年9月9日16:14:14
        /*滴定计时B    索引为 213 (Z)*/                     //  注射泵: 设置B阶段步长    2019年9月9日16:14:14
        regAddrList.add(new byte[]{0x16, (byte) 0x72});
        /*滴定光强上限    索引为 214 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x73});     //  注射泵: 设置A阶段滴数 2019年9月9日16:05:53
        /*滴定光强下限    索引为 215 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x74});
        /*仪器类型    索引为 216 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x45});
        /*校准消解温度    索引为 217 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0xBE});
        /*校准消解时长    索引为 218 (Z)*/
        regAddrList.add(new byte[]{0x14, (byte) 0xBF});
        /*滴定微量模式    索引为 219 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x75}); // 注射泵：2019年9月9日16:35:09
        /*滴定终点判定方式    索引为 220 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x4F}); // 注射泵：2019年9月9日17:35:44
        /*配置流程开关    索引为 221 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x47}); //2019年9月20日17:16:32

        /*还原剂体积     索引为 222 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x50}); //注射泵 2019年9月20日17:16:32
        /*催化剂体积     索引为 223 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x51}); //注射泵 2019年9月20日17:16:32

        /*报错屏蔽1     索引为 224 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF0}); //2019年11月1日 16:14:14
        /*报错屏蔽2     索引为 225 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF1}); //2019年11月1日 16:14:14
        /*报错屏蔽3     索引为 226 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF2}); //2019年11月1日 16:14:14
        /*报错屏蔽4     索引为 227 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF3}); //2019年11月1日 16:14:14
        /*报错屏蔽5     索引为 228 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF4}); //2019年11月1日 16:14:14
        /*报错屏蔽6     索引为 229 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF5}); //2019年11月1日 16:14:14
        /*报错屏蔽7     索引为 230 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF6}); //2019年11月1日 16:14:14
        /*报错屏蔽8     索引为 231 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF7}); //2019年11月1日 16:14:14
        /*报错屏蔽9     索引为 232 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF8}); //2019年11月1日 16:14:14
        /*报错屏蔽10     索引为 233 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xF9}); //2019年11月1日 16:14:14
        /*报错屏蔽11     索引为 234 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xFA}); //2019年11月1日 16:14:14
        /*报错屏蔽12     索引为 235 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xFB}); //2019年11月1日 16:14:14
        /*报错屏蔽13     索引为 236 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xFC}); //2019年11月1日 16:14:14
        /*报错屏蔽14     索引为 237 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xFD}); //2019年11月1日 16:14:14
        /*报错屏蔽15     索引为 238 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xFE}); //2019年11月1日 16:14:14
        /*报错屏蔽16     索引为 239 (Z)*/
        regAddrList.add(new byte[]{0x23, (byte) 0xFF}); //2019年11月1日 16:14:14
        /*报错屏蔽17     索引为 240 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x00}); //2019年11月1日 16:14:14
        /*报错屏蔽18     索引为 241 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x01}); //2019年11月1日 16:14:14
        /*报错屏蔽19     索引为 242 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x02}); //2019年11月1日 16:14:14
        /*报错屏蔽20     索引为 243 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x03}); //2019年11月1日 16:14:14

        /*氧化剂体积2    索引为 244 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x52}); //2019年11月5日12:03:44

        /*氧化剂冲洗体积    索引为 245 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x53}); //2019年12月10日09:30:58
        /*还原剂冲洗体积    索引为 246 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x54}); //2019年12月10日09:31:05
        /*催化剂冲洗体积    索引为 247 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x55}); //2019年12月10日09:31:09

        /*消解阈值     索引为 248 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x04}); //2019年12月11日 09:48:13
        /*平均时间     索引为 249 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x05}); //2019年12月11日 09:48:13

        /*催化剂缺液-LED判断阈值     索引为 250 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x54}); //2020年1月2日08:58:49
        /*氧化剂缺液-LED判断阈值     索引为 251 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x55}); //2020年1月2日08:58:49
        /*还原剂缺液-LED判断阈值     索引为 252 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x56}); //2020年1月2日08:58:49

        /*缓冲剂冲洗体积     索引为 253 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0xB8}); //2020年2月21日 14:55:56
        /*缓冲剂体积     索引为 254 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0xB9}); //2020年2月21日 14:55:56
        /*显色剂冲洗体积     索引为 255 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0xBA}); //2020年2月21日 14:55:56
        /*显色剂体积    索引为 256 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0xBB}); //2020年2月21日 14:55:56
        /*催化剂冲洗体积     索引为 257 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0xBC}); //2020年2月21日 14:55:56
        /*催化剂体积     索引为 258 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0xBD}); //2020年2月21日 14:55:56

        /*智能消解开关     索引为 259 (Z)*/
        regAddrList.add(new byte[]{0x24, (byte) 0x06}); //2020年4月6日09:53:46

        /* 开采样时间2寄存器起始地址，索引为260（WL）*/
        regAddrList.add(new byte[]{0x14, (byte) 0xC0});//2020年4月10日11:06:49

        /* 计量板1的计量参数寄存器起始地址，索引为261（Z）*/ //2020年4月10日16:14:19
        regAddrList.add(new byte[]{0x17, (byte) 0xEE});
        /* 计量板2的计量参数寄存器起始地址，索引为262（Z）*/
        regAddrList.add(new byte[]{0x18, (byte) 0x52});
        /* 计量板3的计量参数寄存器起始地址，索引为263（Z）*/
        regAddrList.add(new byte[]{0x18, (byte) 0xB6});
        /* 计量板4的计量参数寄存器起始地址，索引为264（Z）*/
        regAddrList.add(new byte[]{0x19, (byte) 0x1A});
        /* 计量板5的计量参数寄存器起始地址，索引为265（Z）*/
        regAddrList.add(new byte[]{0x19, (byte) 0x7E});
        /* 计量板6的计量参数寄存器起始地址，索引为266（Z）*/
        regAddrList.add(new byte[]{0x19, (byte) 0xE2});
        /* 计量板7的计量参数寄存器起始地址，索引为267（Z）*/
        regAddrList.add(new byte[]{0x1A, (byte) 0x46});
        /* 计量板8的计量参数寄存器起始地址，索引为268（Z）*/
        regAddrList.add(new byte[]{0x1A, (byte) 0xAA});
        /* 计量板9的计量参数寄存器起始地址，索引为269（Z）*/
        regAddrList.add(new byte[]{0x1B, (byte) 0x0E});
        /* 计量板10的计量参数寄存器起始地址，索引为270（Z）*/
        regAddrList.add(new byte[]{0x1B, (byte) 0x72});
        /* 计量板11的计量参数寄存器起始地址，索引为271（Z）*/
        regAddrList.add(new byte[]{0x1B, (byte) 0xD6});
        /* 计量板12的计量参数寄存器起始地址，索引为272（Z）*/
        regAddrList.add(new byte[]{0x1C, (byte) 0x3A});
        /* 计量板13的计量参数寄存器起始地址，索引为273（Z）*/
        regAddrList.add(new byte[]{0x1C, (byte) 0x9E});
        /* 计量板14的计量参数寄存器起始地址，索引为274（Z）*/
        regAddrList.add(new byte[]{0x1D, (byte) 0x02});
        /* 计量板15的计量参数寄存器起始地址，索引为275（Z）*/
        regAddrList.add(new byte[]{0x1D, (byte) 0x66});
        /* 计量板16的计量参数寄存器起始地址，索引为276（Z）*/
        regAddrList.add(new byte[]{0x1D, (byte) 0xCA});
        /* 计量板17的计量参数寄存器起始地址，索引为277（Z）*/
        regAddrList.add(new byte[]{0x1E, (byte) 0x2E});
        /* 计量板18的计量参数寄存器起始地址，索引为278（Z）*/
        regAddrList.add(new byte[]{0x1E, (byte) 0x92});
        /* 计量板19的计量参数寄存器起始地址，索引为279（Z）*/
        regAddrList.add(new byte[]{0x1E, (byte) 0xF6});
        /* 计量板20的计量参数寄存器起始地址，索引为280（Z）*/
        regAddrList.add(new byte[]{0x1F, (byte) 0x5A});
        /* 时间切换按钮，索引为281（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x80});
        /* TOC 保留，索引为282（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 参数*/
        /* TOC上升温度，索引为283（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x1C});
        /* TOC下降温度，索引为284（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x1D});

        /* TOC IC吹扫时间，索引为285（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x26});
        /* TOC TC吹扫时间，索引为286（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x27});
        /* TOC 采集时间，索引为287（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x28});
        /* TOC 清洗3体积，索引为288（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x29});
        /* TOC 混合抽取体积，索引为289（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x2A});
        /* TOC 进样间隔时间，索引为290（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x3A});
        /* TOC 进样次数，索引为291（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x3B});
        /* TOC 填充体积，索引为292（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x3C});
        /* TOC 单次进样体积，索引为293（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x3D});
        /* TOC 保留，索引为294（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为295（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为296（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为297（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为298（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为299（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 量程2进样间隔时间，索引为300（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x44});
        /* TOC 量程2进样次数，索引为301（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x45});
        /* TOC 量程2填充体积，索引为302（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x46});
        /* TOC 量程2单次进样体积，索引为303（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x47});
        /* TOC 保留，索引为304（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为305（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为306（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为307（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为308（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 保留，索引为309（Z）*/
        regAddrList.add(new byte[]{0, (byte) 0});
        /* TOC 量程3进样间隔时间，索引为310（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x4E});
        /* TOC 量程3进样次数，索引为311（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x4F});
        /* TOC 量程3填充体积，索引为312（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x50});
        /* TOC 量程3单次进样体积，索引为313（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x51});

        /*碱液冲洗体积    索引为 314 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x57}); //注射泵 2020年8月3日10:21:25
        /*碱液体积     索引为 315 (Z)*/
        regAddrList.add(new byte[]{0x16, (byte) 0x56}); //注射泵 2020年8月3日10:21:25
        /*TOC 测量次数     索引为 316 (Z)*/
        regAddrList.add(new byte[]{0x25, (byte) 0x1E}); //TOC 2020年8月3日10:21:25

        /* 生物毒性 清洗体积，索引为317（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x48});
        /* 生物毒性 润洗体积，索引为318（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x49});
        /* 生物毒性 菌液体积，索引为319（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x4A});
        /* 生物毒性 填充体积，索引为320（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x4B});
        /* 生物毒性 回抽体积，索引为321（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x4C});
        /* 生物毒性 水样体积，索引为322（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x4D});
        /* 生物毒性 复苏体积，索引为323（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x4E});
        /* 生物毒性 盐水体积，索引为324（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x4F});
        /* 生物毒性 顶空体积，索引为325（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x50});
        /* 生物毒性 参比阈值，索引为326（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x52});
        /* 生物毒性 吸收阈值，索引为327（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x53});
        /* 生物毒性 毒性测试时间，索引为328（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x51});
        /* 生物毒性 是否读取温度标志位，索引为329（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x54});
        /* 生物毒性 当前通道倍数，索引为330（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x55});
        /* 生物毒性 缓冲体积，索引为331（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x56});

        /* TOC TC流量，索引为332（Z）*/
        regAddrList.add(new byte[]{0x25, (byte) 0x1F}); //TOC 2020年9月9日10:03:57


        /*计量参数B(样品进样时间1、样品进样时间2、冲洗时间1、冲洗时间2、初始装液时间1、初始装液时间2) 索引为333 （D）*/
        regAddrList.add(new byte[]{0x27, (byte) 0x10});//List4_Content3_pjlcs2 2020年9月7日11：00：00

        /* 生物毒性 对照体积，索引为334（Z）*/
        regAddrList.add(new byte[]{0x26, (byte) 0x57});

        /*BS_LED 存储开关    索引为 335 (Z)*/
        regAddrList.add(new byte[]{0x25, (byte) 0x81});

    }

    public static byte[] getRegAddr(int index) {
        return regAddrList.get(index);
    }
}
