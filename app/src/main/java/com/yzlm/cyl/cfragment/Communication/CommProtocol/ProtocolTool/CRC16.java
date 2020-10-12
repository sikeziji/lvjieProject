package com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool;

/**
 * Created by zwj on 2017/5/15.
 */

public class CRC16 {

    // 力合科技校验 （国标协议算法）
    public static String calCrc16(byte[] data, int offset, int len) {
        int high;
        int flag;

        // 16位寄存器，所有数位均为1
        int wCrc = 0xffff;
        for (int i = 0; i < len; i++) {
            // 16 位寄存器的高位字节
            high = wCrc >> 8;
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
            wCrc = high ^ data[offset + i];

            for (int j = 0; j < 8; j++) {
                flag = wCrc & 0x0001;
                // 把这个 16 寄存器向右移一位
                wCrc = wCrc >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if (flag == 1)
                    wCrc ^= 0xa001;
            }
        }
        return Integer.toHexString(wCrc);
    }

    // 212校验算法          /*其实也是MODBUS_16校验算法*/
    public static String calCrc162(byte[] data, int offset, int len) {
        int high;
        int flag;

        // 16位寄存器，所有数位均为1
        int wCrc = 0xffff;
        for (int i = 0; i < len; i++) {

            wCrc ^= data[offset + i];

            for (int j = 0; j < 8; j++) {
                flag = wCrc & 0x0001;
                // 把这个 16 寄存器向右移一位
                wCrc = wCrc >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if (flag == 1)
                    wCrc ^= 0xa001;
            }
        }
        wCrc = ((wCrc & 0x00ff) << 8) | (wCrc & 0xff00) >> 8 & 0xffff;

        return Integer.toHexString(wCrc);
    }


}
