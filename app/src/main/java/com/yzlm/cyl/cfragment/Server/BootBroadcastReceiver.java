package com.yzlm.cyl.cfragment.Server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yzlm.cyl.cfragment.Frame.MainActivity;

/**
 * Created by caoyiliang on 2017/1/3.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        }
    }
}
