package com.yzlm.cyl.cfragment.Config.Public;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;

/**
 * Created by caoyiliang on 2016/11/15.
 */

public class CommPublic extends PublicConfig {
    private void InitCommPublic() {
        configMap.put("MNTD1", "-1");// 模拟通道1
        configMap.put("MNTD2", "-1");
        configMap.put("DIGITAL1", "0");
        configMap.put("DIGITAL2", "0");
        configMap.put("DIGITAL3", "0");

        configMap.put("CK_PUMP_TIME", "60");//开采样设置时间
        configMap.put("CK_PUMP_TIME2", "60");//开采样设置时间2(W200对应的采样系统C使用)

        configMap.put("MNTD1BAUD", "0");// 模拟通道1波特率
        configMap.put("MNTD2BAUD", "0");// 通道2波特率 PC
        configMap.put("MNTD3BAUD", "0");// 通道3波特率


        //configMap.put("IO_BORD", "true");
        configMap.put("IO_BORD", "1");//初始是true和false，现在改成0(未配置)，1(配置一)，2(配置二)，原本的true对应1，false对应0

        // 将继电器操作更新为系统级别，所有组份共用
        configMap.put("SYS_RELAYCONF", "0"); //0:   1:  2:  3:

        // TCP 传输
        configMap.put("TCP_1", "false");//端口一
        configMap.put("TCP_2", "false");//端口二
        configMap.put("TCP_3", "false");//端口三
        configMap.put("TCP_1_Protocol", "0");//端口一协议
        configMap.put("TCP_1_IP", "192.168.167.112");//端口一ip
        configMap.put("TCP_1_PORT", "2756");//端口一端口
        configMap.put("TCP_2_Protocol", "0");//端口二协议
        configMap.put("TCP_2_IP", "hzztkj.com");//端口二ip
        configMap.put("TCP_2_PORT", "22227");//端口二端口
        configMap.put("TCP_3_Protocol", "0");//端口三协议
        configMap.put("TCP_3_IP", "hzztkj.com");//端口三ip
        configMap.put("TCP_3_PORT", "22227");//端口三端口

        // 主动上传协议上传间隔时间
        configMap.put("ProtocolUpdateTime", "60");  // 秒
        //将通道更改为系统校准是否上传
        configMap.put("CAL_DATA_UPDATE", "false");
        //将通道更改为系统吸光度是否上传
        configMap.put("ABSORB_UPDATE", "false");

        configMap.put("COM1_DIGITAL", "0");//COM1 数字量
        configMap.put("COM1BAUD", "0");// COM1波特率

        configMap.put("COM3_DIGITAL", "0");//COM3 数字量
        configMap.put("COM3BAUD", "0");// COM3波特率

        // 协议配置
        //2005
        configMap.put("ORG_ADDR", "0000000");
        configMap.put("DEV_ADDR", "0000001");
        configMap.put("GB2005_CRC", "0");//CRC校验方式

        //2017
        configMap.put("GB2017ORG_ADDR", "000000000000");
        configMap.put("GB2017DEV_ADDR", "000000000001");
        configMap.put("GB2017_CRC", "0");//CRC校验方式

        // 2017V2
        configMap.put("GB2017V2ORG_ADDR", "000000000000");
        configMap.put("GB2017V2DEV_ADDR", "000000000001");
        configMap.put("GB2017V2_CRC", "0");//CRC校验方式

        //江苏协议
        configMap.put("JS_ORG_ADDR", "000000000000");
        configMap.put("JS_DEV_ADDR", "000000000001");

        // modbusrtu 2018
        configMap.put("DEV_SERIAL_NUM", "000000000000000000000000");//设备序列号
        configMap.put("SYS_RTU_ID", "55");

        //  RTU  江苏南水  河北协议     贵州协议
        configMap.put("DIGITAL_ADDR", "0");
        configMap.put("PC_DIGITAL_ADDR", "0");
        configMap.put("RS485_DIGITAL_ADDR", "0");

        //配置文件的MD5码
        configMap.put("MD5", "");

        // 登录信息
        configMap.put("LogInName", "1");    //用户名：1：用户，2：维护，3：管理员
        configMap.put("LogInUserPassword", "20199");    // 用户密码
        configMap.put("LogInOPSPassword", "88888");      // 运维密码

        //河南
        configMap.put("HE_NAN_ORG_ADDR", "0000000");
        configMap.put("HE_NAN_DEV_ADDR", "0000001");
        configMap.put("HE_NAN_CRC", "0");//CRC校验方式

        // 看门狗板
        configMap.put("watchDogSwitch", "close");

        /*反控多点线性数据修正功能开关*/
        configMap.put("Multi_Point_Linear_Features", "false");

        //解析模式
        configMap.put("Modbus_Rtu_ANALYSIS_MODE", "FLOAT");
        configMap.put("ModbusRtu2018_ANALYSIS_MODE", "FLOAT");
        configMap.put("Modbus_Rtu2_ANALYSIS_MODE", "FLOAT");

        //校验模式
        configMap.put("Modbus_Rtu_CRC_MODE", "1");
        configMap.put("ModbusRtu2018_CRC_MODE", "1");
        configMap.put("Modbus_Rtu2_CRC_MODE", "1");


        //服务端配置
        configMap.put("Server_1", "false");
        configMap.put("Server_2", "false");
        configMap.put("Server_1_Protocol", "0");//协议配置
        configMap.put("Server_2_Protocol", "0");
        configMap.put("Server_1_IP", "");//配置1端口及IP
        configMap.put("Server_1_PORT", "");//
        configMap.put("Server_2_IP", "");//配置2端口及IP
        configMap.put("Server_2_PORT", "");//
    }


    public CommPublic(Context context) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        InitCommPublic();
        syncDB();
    }
}
