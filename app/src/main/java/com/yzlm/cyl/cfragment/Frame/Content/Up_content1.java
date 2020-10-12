package com.yzlm.cyl.cfragment.Frame.Content;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.Component.Left_list1;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by caoyiliang on 2016/10/27.
 */

public class Up_content1 extends SubFragment {

    private static boolean reNewSubxUpcontent1 = false;
    private static Up_content1 fragment = null;

    public static Up_content1 newInstance(boolean reNew, boolean reNewSub) {
        reNewSubxUpcontent1 = reNewSub;
        if (fragment == null || reNew) {
            fragment = new Up_content1();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return Left_list1.newInstance(reNewSubxUpcontent1);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.up_content1_h;
    }

    @Override
    protected int getListResId() {
        return R.id.Left_list1;
    }

    @Override
    protected void DoThings() {
        getChildFragment();
    }
}
