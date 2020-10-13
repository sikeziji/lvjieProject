package com.yzlm.cyl.cfragment.Frame.PublicContent;

import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.isLvjie;

/**
 * Created by caoyiliang on 2016/11/4.
 */

public class Frame extends SubFragment {
    private static Frame fragment = null;
    private static LinearLayout linear_frame;
    private static LinearLayout fragment_up_container;
    private static LinearLayout fragment_container;

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
        linear_frame = v.findViewById(R.id.linear_frame);
        fragment_up_container = v.findViewById(R.id.fragment_up_container);
        fragment_container = v.findViewById(R.id.fragment_container);
        updateUI(isLvjie);
    }

    public static void updateUI(boolean isLvjies) {
        LinearLayout.LayoutParams fragment_up_container_param;
        LinearLayout.LayoutParams fragment_container_param;
        if (isLvjies) {
            linear_frame.setOrientation(LinearLayout.HORIZONTAL);
            fragment_up_container_param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            fragment_container_param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        } else {
            linear_frame.setOrientation(LinearLayout.VERTICAL);
            fragment_up_container_param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            fragment_container_param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    96);
        }
        fragment_up_container.setLayoutParams(fragment_up_container_param);
        fragment_container.setLayoutParams(fragment_container_param);
    }
}
