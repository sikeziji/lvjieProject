package com.yzlm.cyl.cfragment.Frame.NoBoardContent;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.NoBoard.NoBoard_Left_list1;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by zwj on 2019/07/24
 */

public class NoBoardUp_content1 extends SubFragment {
    private static boolean reNewSubx = true;
    private static NoBoardUp_content1 fragment = null;

    public static NoBoardUp_content1 newInstance(boolean reNew, boolean reNewSub) {
        reNewSubx = reNewSub;
        if (fragment == null || reNew) {
            fragment = new NoBoardUp_content1();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return NoBoard_Left_list1.newInstance(reNewSubx);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.no_board_up_content1_h;
    }

    @Override
    protected int getListResId() {
        return R.id.NoBoard_Left_list1;
    }

    @Override
    protected void DoThings() {

        getChildFragment();
    }
}
