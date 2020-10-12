package com.yzlm.cyl.cfragment.Communication.Component.AppCmd;

import java.util.ArrayList;

/**
 * 接口板命令寄存器起始地址表（WL）IO 06 0C命令
 * Created by WL on 2017/5/3.
 */
public class IOBoardRegAddr {
    private static ArrayList<byte[]> boardRegAddrList = new ArrayList<>();

    public IOBoardRegAddr() {
    }

    public static void initBoardRegAddrList() {
        boardRegAddrList.clear();
        /* 4-20mA输入输出寄存器起始地址，索引为0（WL）*/
        boardRegAddrList.add(new byte[]{0x00, 0x01});

        /* 0C命令开启采集水样动作寄存器起始地址，索引为1（ZWJ）*/
        boardRegAddrList.add(new byte[]{0x00, 0x01});

        /* 0C命令采样动作寄存器起始地址，索引为2（ZWJ）*/
        boardRegAddrList.add(new byte[]{0x00, 0x02});

        /* 0C命令搅拌动作寄存器起始地址，索引为3（ZWJ）*/
        boardRegAddrList.add(new byte[]{0x00, 0x03});

        /* 0C命令排废寄存器起始地址，索引为4（ZWJ）*/
        boardRegAddrList.add(new byte[]{0x00, 0x04});

        /* 0C命令清洗寄存器起始地址，索引为5（ZWJ）*/
        boardRegAddrList.add(new byte[]{0x00, 0x05});

        /* 06命令清洗寄存器起始地址，索引为6（ZWJ）*/
        boardRegAddrList.add(new byte[]{0x00, 0x07});

        /* 09命令往DCI口转发数据 ，索引为7（zwj）*/
        boardRegAddrList.add(new byte[]{0x00, 0x01});

        /* 09命令往PC口转发数据 ，索引为8（zwj）*/
        boardRegAddrList.add(new byte[]{0x00, 0x02});

        /* 09命令往SPC口转发数据 ，索引为9（zwj）*/
        boardRegAddrList.add(new byte[]{0x00, 0x03});

        /* 09命令往PRIN口转发数据 ，索引为10（zwj）*/
        boardRegAddrList.add(new byte[]{0x00, 0x04});

        /* 06命令 开关量输出 ，索引为11（zwj）*/
        boardRegAddrList.add(new byte[]{0x00, 0x02});

        /* 06命令 设置系统时间 ，索引为12（zwj）*/
        boardRegAddrList.add(new byte[]{0x00, 0x03});

        /* 06命令 波特率设置 ，索引为13（zwj）*/
        boardRegAddrList.add(new byte[]{0x00, 0x05});

        /* 06命令 紧急停止，索引为14（ZWJ）*/
        boardRegAddrList.add(new byte[]{0x00, 0x06});

         /* 06命令 串口属性设置，索引为15（ZWJ）*/
        boardRegAddrList.add(new byte[]{0x00, 0x08});
    }

    public static byte[] getBoardRegAddr(int index) {
        return boardRegAddrList.get(index);
    }
}
