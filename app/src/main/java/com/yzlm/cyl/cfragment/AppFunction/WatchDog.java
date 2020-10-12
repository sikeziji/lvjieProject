package com.yzlm.cyl.cfragment.AppFunction;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;

import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S3;
import static com.yzlm.cyl.cfragment.Global.blWatchDogFeedFlagForCK;
import static com.yzlm.cyl.cfragment.Global.blWatchDogFeedFlagForIO;
import static com.yzlm.cyl.cfragment.Global.strComponent;

public class WatchDog {

    /**
     * 看门狗板喂狗
     **/
    public static void feedWatchDogBoard() {
        if (strComponent.get(1).length == 1) {
            byte[] sByte = {0x01, 0x08, 0x03, 0x01, 0x01, 0x00, (byte) (0xb0 & 0xff), 0x1F};

            if (IOBoardUsed) {
                if (blWatchDogFeedFlagForCK && blWatchDogFeedFlagForIO) {
                    SendManager.SendCmd("DTU" + "_" + "打印输出_0_0", S3, 2, 500, sByte);
                }
            } else {
                if (blWatchDogFeedFlagForCK) {
                    SendManager.SendCmd("DTU" + "_" + "打印输出_0_0", S3, 2, 500, sByte);
                }
            }
            //清除要保护板的状态
            blWatchDogFeedFlagForCK = false;
            blWatchDogFeedFlagForIO = false;
        }
    }
}
