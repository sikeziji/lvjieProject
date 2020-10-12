package com.yzlm.cyl.cfragment.Frame.PublicContent;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by caoyiliang on 2016/11/4.
 */

public class Frame extends SubFragment {
    private static Frame fragment = null;

    public static Frame newInstance() {
        if (fragment == null) {
            fragment = new Frame();
        }
        return fragment;
    }
    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frame;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

    }
}
