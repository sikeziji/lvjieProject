package com.yzlm.cyl.cfragment.Thread;

import android.os.Message;

import com.yzlm.cyl.clibrary.Thread.CThread;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.其他_信息;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateDay;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateDayOfMonth;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateHour;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMin;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.WinWidgetHandler;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.strAll5721Component;

public class RebootApp extends CThread {

    private int startDate = 0;

    public RebootApp(String name, boolean suspend) {
        super(name, suspend);
        SUSPEND_TIME_MILLISECONDS = 1000 * 60;

        startDate = getSystemDateDay();
    }

    @Override
    public void process() {
        try {
            //每个月1号，01:20分重启软件
            if (getSystemDateDayOfMonth() == 1) {
                if (startDate != getSystemDateDay() && getSystemDateHour() == 1 && getSystemDateMin() == 20) {
                    saveRunInfo2File("定点重启软件");
                    Message msg = new Message();
                    msg.what = 110;
                    WinWidgetHandler.sendMessage(msg);
                    for (String item : strAll5721Component.get(1)) {
                        AddError(item, 618, 其他_信息);
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onDestory() {

    }
}
