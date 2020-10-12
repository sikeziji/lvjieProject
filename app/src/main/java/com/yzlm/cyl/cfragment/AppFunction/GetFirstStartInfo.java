package com.yzlm.cyl.cfragment.AppFunction;

public class GetFirstStartInfo {

    public GetFirstStartInfo() {

        Thread getFirstStartInfoThread = new Thread(new GetFirstStartInfoRunnable());
        getFirstStartInfoThread.start();
    }
}
