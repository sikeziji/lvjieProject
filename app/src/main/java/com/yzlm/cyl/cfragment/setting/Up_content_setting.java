package com.yzlm.cyl.cfragment.setting;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.Component.Left_list_setting;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by caoyiliang on 2016/10/27.
 */

public class Up_content_setting extends SubFragment {

    private static Up_content_setting fragment = null;

    public static Up_content_setting newInstance() {

        if (fragment == null) {
            fragment = new Up_content_setting();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return Left_list_setting.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.up_content_setting_h;
    }

    @Override
    protected int getListResId() {
        return R.id.Left_list_setting;
    }

    @Override
    protected void DoThings() {
        getChildFragment();
    }
}
