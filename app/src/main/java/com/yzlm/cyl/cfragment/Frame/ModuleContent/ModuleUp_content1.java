package com.yzlm.cyl.cfragment.Frame.ModuleContent;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.ModuleList.Module_Left_list1;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by zwj on 2018/07/17.
 */

public class ModuleUp_content1 extends SubFragment {
    private static boolean reNewSubx = true;
    private static ModuleUp_content1 fragment = null;

    public static ModuleUp_content1 newInstance(boolean reNew, boolean reNewSub) {
        reNewSubx = reNewSub;
        if (fragment == null || reNew) {
            fragment = new ModuleUp_content1();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return Module_Left_list1.newInstance(reNewSubx);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.module_up_content1_h;
    }

    @Override
    protected int getListResId() {
        return R.id.Module_Left_list1;
    }

    @Override
    protected void DoThings() {

        getChildFragment();
    }
}
