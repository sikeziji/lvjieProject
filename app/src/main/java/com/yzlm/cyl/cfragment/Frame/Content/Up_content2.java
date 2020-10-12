package com.yzlm.cyl.cfragment.Frame.Content;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.Component.Left_list2;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by caoyiliang on 2016/10/27.
 */

public class Up_content2 extends SubFragment {
    static boolean reNewSubx = false;
    static Up_content2 fragment = null;

    public static Up_content2 newInstance(boolean reNew, boolean reNewSub) {
        reNewSubx = reNewSub;
        if (fragment == null || reNew) {
            fragment = new Up_content2();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return Left_list2.newInstance(reNewSubx);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.up_content2_h;
    }

    @Override
    protected int getListResId() {
        return R.id.Left_list2;
    }

    @Override
    protected void DoThings() {
        getChildFragment();
    }
}
