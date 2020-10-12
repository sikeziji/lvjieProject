package com.yzlm.cyl.cfragment.AppFunction;

/**
 * Created by zwj on 2018/1/11.
 */

public class AutoDeleteFiles {

    public AutoDeleteFiles(){
        new Thread(new AutoDeleteFilesRunnable()).start();
    }
}
