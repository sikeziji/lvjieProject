package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by zwj on 2019/4/1.
 */

public class List_Content_SettingSys extends SubFragment {


    private static List_Content_SettingSys fragment = null;

    public static List_Content_SettingSys newInstance() {
        if (fragment == null) {
            fragment = new List_Content_SettingSys();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list_content_settingsys;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

    }

    // 导出仪表运行日志信息
    /*String extSdcard = getStoragePath(context, true).size() == 0 ? null : (getStoragePath(context, true).get(0) + File.separator);
        if (extSdcard != null) {
        copyDir("/sdcard/waterrun/", extSdcard + "20190720/");
    }*/


}
