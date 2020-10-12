package com.yzlm.cyl.cfragment.Frame.PublicContent;

import android.support.v4.app.Fragment;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;

/**
 * Created by zwj on 2019/3/27
 */

public class ScreenContent extends SubFragment {
    private static ScreenContent fragment = null;


    public static ScreenContent newInstance() {
        if (fragment == null) {
            fragment = new ScreenContent();
        }
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_content;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        main.removeDestopText(mfb);
    }

    public interface Callbacks {
        void onDialogRS();
    }

}
