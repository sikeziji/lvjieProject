package com.yzlm.cyl.cfragment.Communication.Component.Cmd;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017_V2;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu.RtuProtocol;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.nanshui;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ningxia.ningxia;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.shenzhen.shenzhen;
import com.yzlm.cyl.cfragment.Communication.Communication;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GuiZhou.GuiZhouProtocol.ParsingProtocolGuiZhou;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.HeBei.HeBei.ParsingProtocolHeBei;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018Protocol.ParsingProtocolRtu2018;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu2.ModbusRtu2Protocol.ParsingProtocolRtu2;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.W300.W300.ParsingProtocolW300;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.JiangSu.ParsingProtocolJiangSuDt;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.sichuan.siChuanModbusRtuProtocol.ParsingProtocolSiChuanRtu;
import static com.yzlm.cyl.cfragment.Global.blAuthenticationFunction;
import static com.yzlm.cyl.cfragment.Global.protocolName;

/**
 * Created by zwj on 2017/5/23.
 */

public class ProtocolResponse {

    /* port :串口  sCam: port为 S1接口板时候  sCom 7/9 标识接口板数采仪还是PC口*/
    public void ParsingProtocol(Communication port, int sCom, String sProtocol, byte[] rs) {

        try {
            if (sProtocol.equals(protocolName[0])) {    //GB2005
                if (!blAuthenticationFunction) {
                    GB2005.ParsingProtocolInternational(port, sCom, rs);
                }
            } else if (sProtocol.equals(protocolName[1])) {
            } else if (sProtocol.equals(protocolName[2])) {
                RtuProtocol.ParsingProtocolRtu(port, sCom, rs, sProtocol);//  RTU
            } else if (sProtocol.equals(protocolName[3])) {
                nanshui.ParsingProtocolJiangSu(port, sCom, rs);
            } else if (sProtocol.equals(protocolName[4])) {  //RTU2018
                ParsingProtocolRtu2018(port, sCom, rs, sProtocol);
            } else if (sProtocol.equals(protocolName[5])) {  //江苏
                ParsingProtocolJiangSuDt(port, sCom, rs);
            } else if (sProtocol.equals(protocolName[6])) {  //河北
                ParsingProtocolHeBei(port, sCom, rs);
            } else if (sProtocol.equals(protocolName[7])) {  //GB2017
                new GB2017_V2().ParsingProtocolPortData(port, sCom, rs);
            } else if (sProtocol.equals(protocolName[10])) { //RTU
                ParsingProtocolRtu2(port, sCom, rs, sProtocol);
            } else if (sProtocol.equals(protocolName[11])) { //贵州协议
                ParsingProtocolGuiZhou(port, sCom, rs, sProtocol);
            } else if (sProtocol.equals(protocolName[12])) { //宁夏协议
                ningxia.xieyiml(port, sCom, rs);
            } else if (sProtocol.equals(protocolName[13])) { //深圳协议
                shenzhen.shenzhenxy(port, sCom, rs);
            } else if (sProtocol.equals(protocolName[14])) { //W300
                ParsingProtocolW300(port, sCom, rs, sProtocol);
            } else if (sProtocol.equals(protocolName[15])) {// 四川协议
                ParsingProtocolSiChuanRtu(port, sCom, rs, sProtocol);
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return;
    }
}
