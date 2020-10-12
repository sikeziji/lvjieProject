package com.yzlm.cyl.cfragment.Frame.Content;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.Component.Left_list5;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by caoyiliang on 2016/10/27.
 */

public class Up_content5 extends SubFragment {
    private static boolean reNewSubx = false;
    private static Up_content5 fragment = null;

    public static Up_content5 newInstance(boolean reNew, boolean reNewSub) {
        reNewSubx = reNewSub;
        if (fragment == null || reNew) {
            fragment = new Up_content5();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return Left_list5.newInstance(reNewSubx);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.up_content5_h;
    }

    @Override
    protected int getListResId() {
        return R.id.Left_list5;
    }

    @Override
    protected void DoThings() {
        getChildFragment();
    }
}
