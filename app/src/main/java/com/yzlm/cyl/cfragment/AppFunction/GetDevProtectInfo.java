package com.yzlm.cyl.cfragment.AppFunction;

/**
 * Created by zwj on 2017/11/9.
 */

public class GetDevProtectInfo {

    public GetDevProtectInfo() {

        Thread getDevProtectInfoThread = new Thread(new GetDevProtectInfoRunnable());
        getDevProtectInfoThread.start();
    }
}
