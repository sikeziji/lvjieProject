package com.yzlm.cyl.cfragment.AppFunction;

        import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
        import com.yzlm.cyl.clibrary.Util.DataUtil;

        import static com.yzlm.cyl.cfragment.Global.S0;
        import static com.yzlm.cyl.cfragment.Global.strComponent;

public class GetFirstStartInfoRunnable implements Runnable {

    @Override
    public void run() {
        for (String item : strComponent.get(1)) {

            //开机查一下相关信息
            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 557);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(item + "_配置流程校验信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), S0, 3, 200, 1);

            byte[] arrayOfByte1 = DataUtil.shortToByte((short) 5703);
            DataUtil.reverse(arrayOfByte1);
            SendManager.SendCmd(item + "_配置流程开关" + "_3_" + DataUtil.bytesToHexString(arrayOfByte1, 2).replace(" ", ""), S0, 3, 200, 1);
        }
    }
}
