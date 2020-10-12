package com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool;

import com.yzlm.cyl.cfragment.Communication.Communication;

public abstract class Protocol {

    // 测量值主动上传
    public abstract String getMeaDataValueUpLoading(DataStruct DataInfo, boolean isHistory);

    // 数据解析
    public abstract void ParsingProtocolPortData(Communication port, int sCom, byte[] rs);

    // 主动上传
    public abstract void activeProtocol(Communication port, int sCom);


    //协议上传测试界面-测量值上传
    public abstract String testProtocolUpLoading(Communication port, int sCom, DataStruct DataInfo);

}
