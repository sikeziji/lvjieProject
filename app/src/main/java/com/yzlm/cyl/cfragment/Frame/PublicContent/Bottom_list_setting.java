package com.yzlm.cyl.cfragment.Frame.PublicContent;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.Component.Left_list_setting;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;

/**
 * Created by caoyiliang on 2016/10/26.
 */

public class Bottom_list_setting extends SubFragment {
    private Callbacks mCallbacks;
    private static Bottom_list_setting fragment = null;

    public static Bottom_list_setting newInstance() {
        if (fragment == null) {
            fragment = new Bottom_list_setting();
        }
        return fragment;
    }

    public interface Callbacks {
        void onSelected(View view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.botton_list_setting_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mImBtn3 = (ImageButton) v.findViewById(R.id.ImBtn_3);
        mImBtn3.setOnClickListener(new mImBtnClick());
    }

    private class mImBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Left_list_setting.newInstance().selectFirst();
            main.preWinForImBtn_3 = "Bottom_list_setting";
            mCallbacks.onSelected(view);
        }
    }
}
