package com.yzlm.cyl.cfragment.Communication.Component.Cmd;

import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionStep;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.IOBoardRegAddr;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.RegAddr;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.intToBytes;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.shortToByteArray;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.Util.DataUtil.HexString2Bytes;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toPrimitives;

/**
 * Created by caoyiliang on 2017/2/7.
 */
public class CommandPackaging {
    /**
     * 测控板协议06功能码的设置命令的打包组装（WL）
     */
    private static byte[] SetCmdPackaging(String Component, byte SetNumber, String RegAddrIndex, Object SetParameters) {
        byte[] Parameters = (byte[]) SetParameters;
        int regAddrIndex = Integer.parseInt(RegAddrIndex);
        byte SetCount = (byte) Parameters.length;
        byte[] regNum = toByteArray(Parameters.length / 4, 2);
        reverse(regNum);

        if (regAddrIndex == 38 || regAddrIndex == 65|| regAddrIndex == 216) {
            return copybyte(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, new byte[]{SetNumber}, RegAddr.getRegAddr(regAddrIndex), regNum, new byte[]{SetCount}, Parameters);
        } else if (regAddrIndex == 9999) {
            return copybyte(findAddr(Component), new byte[]{SetNumber}, Parameters);
        } else {
            return copybyte(findAddr(Component), new byte[]{SetNumber}, RegAddr.getRegAddr(regAddrIndex), regNum, new byte[]{SetCount}, Parameters);
        }
    }

    private static byte[] directoryCmdPackaging(String Component, String command, byte OperationNumber, String OperationCode, Object OperationParameters) {
        byte[] Parameters = new byte[0];
        byte OperationCount = (byte) 1;//操作参数数量
        switch (OperationCode) {
            case "01": {
                OperationCount = (byte) 1;
                Parameters = (byte[]) OperationParameters;
            }
            break;
            case "02": {
                OperationCount = (byte) 2;
                Parameters = (byte[]) OperationParameters;
            }
            break;
            case "03":{
                OperationCount = (byte) 2;
                Parameters = (byte[]) OperationParameters;
            }
            break;

            default:
                if (OperationParameters == null) {
                    Parameters = new byte[]{0x00};
                } else {
                    Parameters = (byte[]) OperationParameters;
                    OperationCount = (byte) Parameters.length;
                }
                return copybyte(findAddr(Component), new byte[]{OperationNumber}, new byte[]{(byte) (2 + Parameters.length)}, HexString2Bytes(OperationCode), new byte[]{OperationCount}, Parameters);

        }
        return copybyte(findAddr(Component), new byte[]{OperationNumber}, new byte[]{(byte) (2 + Parameters.length)}, HexString2Bytes(OperationCode), new byte[]{OperationCount}, Parameters);
    }

    private static byte[] comForwardCmdPackaging(String Component, String command, byte Number, String Code, Object mObject) {
        byte[] Parameters = null;
        Parameters = (byte[]) mObject;

        return copybyte(findAddr(Component), new byte[]{Number}, shortToByteArray(Parameters.length + 1), new byte[]{(byte) Integer.parseInt(Code)}, Parameters);
    }

    private static byte[] ReadCmdPackaging(String Component, byte ReadNumber, String ReadCode, byte[] ReadCount) {
        return copybyte(findAddr(Component), new byte[]{ReadNumber}, HexString2Bytes(ReadCode), ReadCount);
    }

    /**
     *
     * @param Component 组分名
     * @param command
     * @param OperationNumber 命令功能码
     * @param OperationCode  命令动作号或寄存器地址
     * @param OperationParameters 命令数据内容或寄存器个数
     * @return
     */
    private static byte[] OperationCmdPackaging(String Component, String command, byte OperationNumber, String OperationCode, Object OperationParameters) {
        byte[] Parameters = new byte[0];
        byte OperationCount = (byte) 1;
        switch (OperationCode) {
            case "01": {
                Parameters = (byte[]) OperationParameters;
                OperationCount = (byte) 4;
            }
            break;
            case "02": {
                Parameters = (byte[]) OperationParameters;
                OperationCount = (byte) 3;
            }
            break;
            case "03":
            case "16":{
                /*组装常规测试的08功能码命令*/
                List<FlowClass> fc = (List<FlowClass>) OperationParameters;
                Parameters = CommandPackaging.FlowPackaging(Component, 30, fc);
                OperationCount = (byte) 30;
            }
            break;
            case "04": {
                if (command.equals(context.getResources().getString(R.string.infusionTest) /*"抽液测试"*/) || command.equals(context.getResources().getString(R.string.drainageTest) /*"排液测试"*/)) {
                    Parameters = (byte[]) OperationParameters;
                    OperationCount = (byte) 3;
                } else {
                    ActionStep as = (ActionStep) OperationParameters;
                    ActionTable at = Global.getActions(Component);
                    byte[] ActionCmd = shortToByte(at.getAction(as.getName()).getCmd());
                    reverse(ActionCmd);
                    Parameters = copybyte(ActionCmd, new byte[]{as.getSampleCount(), as.getMeasurement()});
                    OperationCount = (byte) 3;
                }
            }
            break;
            case "00":
            case "0B":
            case "07": {
                Parameters = new byte[]{0x00};
            }
            break;
            case "0C":
            case "0F":
            case "10": {
                Parameters = new byte[]{0x00};
            }
            break;
            case "0":
            case "1": {
                Parameters = (byte[]) OperationParameters;
                OperationCount = (byte) (1 + Integer.parseInt(OperationCode));
                return copybyte(findAddr(Component), new byte[]{OperationNumber}, new byte[]{(byte) (2 + Parameters.length)}, HexString2Bytes("0" + OperationCode), new byte[]{OperationCount}, Parameters);
            }
            case "31": {
                Parameters = (byte[]) OperationParameters;
                OperationCount = (byte) Parameters.length;
                return copybyte(findAddr(Component), new byte[]{OperationNumber}, new byte[]{OperationCount}, HexString2Bytes(OperationCode), new byte[]{0x04}, Parameters);
            }
            default:
                if (OperationParameters == null) {
                    Parameters = new byte[]{0x00};
                } else {
                    Parameters = (byte[]) OperationParameters;
                    OperationCount = (byte) Parameters.length;
                }
                return copybyte(findAddr(Component), new byte[]{OperationNumber}, new byte[]{(byte) (2 + Parameters.length)}, HexString2Bytes(OperationCode), new byte[]{OperationCount}, Parameters);

        }
        return copybyte(findAddr(Component), new byte[]{OperationNumber}, new byte[]{(byte) (2 + Parameters.length)}, HexString2Bytes(OperationCode), new byte[]{OperationCount}, Parameters);
    }

    private static byte[] ReadLogPackaging(String Component, byte Number, String ActionCode, Object object) {
        byte[] count = new byte[0];
        byte[] content = new byte[0];
        byte LogNum = 1;
        switch (ActionCode) {
            case "01": {
                count = toByteArray(2, 2);
                reverse(count);
                LogNum = (byte) object;
            }
            break;
            case "02": {
                count = toByteArray(6, 2);
                reverse(count);
                String[] temp = ((String) object).split("_");
                LogNum = (byte) Integer.parseInt(temp[0]);
                byte[] Index = toByteArray(Integer.parseInt(temp[1]), 2);
                reverse(Index);
                byte[] Count = toByteArray(Integer.parseInt(temp[2]), 2);
                reverse(Count);
                content = copybyte(Index, Count);
            }
            break;
        }
        return copybyte(findAddr(Component), new byte[]{Number}, count, HexString2Bytes(ActionCode), new byte[]{LogNum}, content);
    }

    private static byte[] WriteLogPackaging(String Component, byte Number, String ActionCode, Object object) {
        byte[] count = new byte[0];
        byte[] content = new byte[0];
        byte LogNum = 1;
        switch (ActionCode) {
            case "01": {
                count = toByteArray(2, 2);
                reverse(count);
                LogNum = (byte) object;
            }
            break;
            case "02": {
                count = toByteArray(6, 2);
                reverse(count);
                String[] temp = ((String) object).split("_");
                LogNum = (byte) Integer.parseInt(temp[0]);
                byte[] Index = toByteArray(Integer.parseInt(temp[1]), 2);
                reverse(Index);
                byte[] Count = toByteArray(Integer.parseInt(temp[2]), 2);
                reverse(Count);
                content = copybyte(Index, Count);
            }
            break;
            case "04":
                byte[] LogContent = (byte[]) object;
                byte[] countNum = toByteArray(LogContent.length + 1, 2);
                reverse(countNum);

                return copybyte(findAddr(Component), new byte[]{Number}, countNum, HexString2Bytes(ActionCode), LogContent);
        }
        return copybyte(findAddr(Component), new byte[]{Number}, count, HexString2Bytes(ActionCode), new byte[]{LogNum}, content);
    }

    private static byte[] Read28Packaging(String Component, byte Number, String ActionCode, Object object) {
        byte[] count = new byte[0];
        byte[] count2 = new byte[0];
        byte[] content = new byte[0];
        switch (ActionCode) {
            case "01": {
                content = (byte[]) object;
                count = toByteArray(content.length + 2, 1);
                count2 = toByteArray(content.length, 1);
            }
            break;
        }
        return copybyte(findAddr(Component), new byte[]{Number}, count, HexString2Bytes(ActionCode), count2, content);
    }

    /**
     * 测控板协议根据组分名获取组分地址用于命令发送头部(WL)
     */
    public static byte[] findAddr(String component) {
        //return new byte[]{0x10, 0x00, 0x00, 0x00};
        String componentAddr = Global.QueryCompAddr(component);

        /* 268435456的十六进制为0x10 0x00 0x00 0x00*/
        byte[] addrbyte = toByteArray((268435456 + Integer.parseInt(componentAddr)), 4);
        reverse(addrbyte);

        return addrbyte;
    }

    //测控板协议FC为30个流程的集合
    private static byte[] FlowPackaging(String Component, int OperationCount, List<FlowClass> FC) {
        List<Byte> Flow = new ArrayList<>();
        for (int j = 0; j < OperationCount; j++) {
            if (j < FC.size()) {
                for (byte item : FC.get(j).findFlow(Component)) {
                    Flow.add(item);
                }
            }
        }
        for (int i = Flow.size(); i < OperationCount; i++) {
            Flow.add((byte) 0x00);
        }
        return toPrimitives(Flow.toArray(new Byte[OperationCount]));
    }

    /**
     * 测控板协议根据不同的命令功能码进行不同的命令打包组装（WL）
     *
     * @param Component 组分名
     * @param Number    命令功能码
     * @param Code      命令动作号或寄存器地址
     * @param mObject   命令数据内容或寄存器个数
     */
    public static byte[] getCmd(String Component, String command, byte Number, String Code, Object mObject) {
        byte[] Cmd = null;
        switch (Number) {
            case 0x08: {
                Cmd = OperationCmdPackaging(Component, command, Number, Code, mObject);
            }
            break;
            case 0x03: {
                byte[] Count = toByteArray((int) mObject, 2);
                reverse(Count);
                Cmd = ReadCmdPackaging(Component, Number, Code, Count);
            }
            break;
            case 0x23: {
                Cmd = ReadLogPackaging(Component, Number, Code, mObject);
            }
            break;
            case 0x26: {
                Cmd = WriteLogPackaging(Component, Number, Code, mObject);
            }
            break;
            case 0x06: {
                Cmd = SetCmdPackaging(Component, Number, Code, mObject);
                System.out.println("打印 ：" + Arrays.toString(Cmd));
            }
            break;
            case 0x30: {
                Cmd = directoryCmdPackaging(Component, command, Number, Code, mObject);
            }
            break;
            case 0x28: {
                Cmd = Read28Packaging(Component, Number, Code, mObject);
            }
            break;
            case 0x32: {
                Cmd = comForwardCmdPackaging(Component, command, Number, Code, mObject);
            }
            break;

        }
        if (Cmd != null) {
            Cmd = copybyte(Cmd, crc16(Cmd, Cmd.length));
        }
        return Cmd;
    }

    /**
     * 测控板协议根据不同的命令功能码进行不同的命令打包组装（WL）
     *
     * @param Component 组分名
     * @param mObject   命令数据内容或寄存器个数
     */
    public static byte[] getCmd(String Component, Object mObject) {
        byte[] Cmd = findAddr(Component);
        Cmd = copybyte(Cmd, (byte[]) mObject);
        if (Cmd != null) {
            Cmd = copybyte(Cmd, crc16(Cmd, Cmd.length));
        }
        return Cmd;
    }

    /**
     * 接口板协议根据不同的命令功能码进行不同的命令打包组装（WL）
     *
     * @param Number  命令功能码
     * @param Code    寄存器地址
     * @param mObject 命令数据内容或寄存器个数
     */
    public static byte[] getBoardCmd(byte Number, String Code, Object mObject) {
        byte[] Cmd = null;
        switch (Number) {
            case 0x03: {
                byte[] Count = toByteArray((int) mObject, 2);
                reverse(Count);
                Cmd = boardReadCmdPackaging(Number, Code, Count);
            }
            break;
            case 0x06:
            case 0x09:
            case 0x0C: {
                Cmd = boardSetCmdPackaging(Number, Code, mObject);
            }
            break;
            case 0x00:/*数据直接发送，不带帧头帧尾，并不是给IO口发送*/
                Cmd = (byte[]) mObject;
                return Cmd;
            case 0x01:/*String数据直接发送，不带帧头帧尾，并不是给IO口发送*/
                Cmd = ((String) mObject).getBytes();
                return Cmd;
        }
        if (Cmd != null) {
            Cmd = copybyte(Cmd, crc16(Cmd, Cmd.length));
        }
        return Cmd;
    }

    /**
     * 接口板协议06 09 0C 功能码的设置命令的打包组装（WL）
     */
    private static byte[] boardSetCmdPackaging(byte SetNumber, String RegAddrIndex, Object SetParameters) {
        int regAddrIndex = Integer.parseInt(RegAddrIndex);
        if (SetParameters != null) {
            byte[] Parameters = (byte[]) SetParameters;

            int SetCount = Parameters.length;

            byte[] dataLen = toByteArray(SetCount, 2);
            reverse(dataLen);
            return copybyte(new byte[]{0x1A}, new byte[]{SetNumber}, IOBoardRegAddr.getBoardRegAddr(regAddrIndex), dataLen, Parameters);
        } else {
            byte[] dataLen = toByteArray(0, 2);
            reverse(dataLen);
            return copybyte(new byte[]{0x1A}, new byte[]{SetNumber}, IOBoardRegAddr.getBoardRegAddr(regAddrIndex), dataLen);
        }
    }

    private static byte[] boardReadCmdPackaging(byte ReadNumber, String ReadCode, byte[] ReadCount) {
        return copybyte(new byte[]{0x1A}, new byte[]{ReadNumber}, HexString2Bytes(ReadCode), ReadCount);
    }

    /*逻辑控制板  开关量输入输出*/
    public static byte[] switchBoardSetCmdPackaging(byte SetNumber, Object SetParameters) {
        byte[] Cmd = null;
        byte[] Parameters = (byte[]) SetParameters;
        if (SetNumber == 0x0f) {
            /*开关量输出*/
            Cmd = copybyte(new byte[]{0x01}, new byte[]{SetNumber}, new byte[]{0x00, 0x00, 0x00, 0x10, 0x02}, Parameters);
        } else if (SetNumber == 0x01) {
            /*开关量输入*/
            Cmd = copybyte(new byte[]{0x01}, new byte[]{SetNumber}, new byte[]{0x00, 0x00, 0x00, 0x10});
        }

        if (Cmd != null) {
            Cmd = copybyte(Cmd, crc16(Cmd, Cmd.length));
        }
        return Cmd;
    }

    /**
     * 需要转发的数据进行封装
     *
     * @param iAddr   目标板子地址
     * @param Number  功能码
     * @param Code
     * @param mObject
     * @return
     */
    public static byte[] GetForwardCmd(String command, int iAddr, byte Number, String Code, Object mObject) {
        byte[] Cmd = null;
        switch (Number) {
            case 0x03:
                byte[] Count = toByteArray((int) mObject, 2);
                reverse(Count);
                Cmd = ReadCmdPackaging(command, Number, Code, Count);
                break;
            case 0x04: {
                byte[] bObject = (byte[]) mObject;
                Cmd = copybyte(new byte[]{(byte) iAddr, Number}, bObject);
                Cmd = copybyte(Cmd, crc16(Cmd, Cmd.length));
                return  Cmd;
            }
            case 0x06:
                Cmd = SetCmdPackaging(command, Number, Code, mObject);
                break;
            case 0x08:
                Cmd = OperationCmdPackaging(command, "", Number, Code, mObject);
                break;
            case 0x10: {//目前用来转发抽液/排液
                byte[] bObject = (byte[]) mObject;
                byte[] bInt = intToBytes(Integer.parseInt(Code));
                Cmd = copybyte(new byte[]{(byte) iAddr, Number, bInt[2], bInt[3], (byte) bObject.length}, bObject);
                Cmd = copybyte(Cmd, crc16(Cmd, Cmd.length));
                return Cmd;
            }
            case 0x26: {
                byte[] bObject = (byte[]) mObject;
                Cmd = copybyte(new byte[]{(byte) iAddr, Number, (byte) (bObject.length + 1), (byte) Integer.parseInt(Code)}, bObject);
                Cmd = copybyte(Cmd, crc16(Cmd, Cmd.length));
                return Cmd;
            }
        }
        if (Cmd != null) {
            byte[] newCmd = new byte[Cmd.length - 3];
            newCmd[0] = (byte) iAddr;
            System.arraycopy(Cmd, 4, newCmd, 1, Cmd.length - 4);
            newCmd = copybyte(newCmd, crc16(newCmd, newCmd.length));

            return newCmd;
        }
        return null;
    }

    /**
     * 计算计量板地址
     *
     * @param iJLBNum     计量板号：板1/板2/板...
     * @param selectIndex 计量板上的地址：1-20
     * @return
     */
    public static int getJLBAddr(int iJLBNum, int selectIndex) {
        int ijlbAddr = (iJLBNum - 1) * 16 + selectIndex;
        return ijlbAddr;
    }


}
