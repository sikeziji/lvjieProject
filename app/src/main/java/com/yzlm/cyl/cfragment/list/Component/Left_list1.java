package com.yzlm.cyl.cfragment.list.Component;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.LockDisplayShowFlag;
import static com.yzlm.cyl.cfragment.Global.doFlowing;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class Left_list1 extends SubFragment {
    private List<ImageButton> btnList;
    private Callbacks mCallbacks;
    private static Left_list1 fragment = null;


    public static Left_list1 newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new Left_list1();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);
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
        return R.layout.left_list1_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mLeft1 = v.findViewById(R.id.Left1_1);
        ImageButton mLeft2 = v.findViewById(R.id.Left1_2);
        ImageButton mLeft3 = v.findViewById(R.id.Left1_3);
        btnList = new ArrayList<>();
        btnList.add(mLeft1);
        btnList.add(mLeft2);
        btnList.add(mLeft3);
        mLeft3.setVisibility(View.GONE);
        for (ImageButton btn : btnList) {
            btn.setOnClickListener(new mLeftBtnClick());
        }
        //运维可见
        //生物毒性平台不可见
        if (!getPublicConfigData("LogInName").equals("0") && !getPublicConfigData("LogInName").equals("1") && !QueryMeasCateg(mCompName).equals("13")) {
            mLeft3.setVisibility(View.VISIBLE);
        }
        if (doFlowing.get(MainActivity.mCompName).equals(getString(R.string.waiting_for_instructions)) && !LockDisplayShowFlag) {
            selectList(mLeft1);
        } else {
            selectList(mLeft2);
        }

    }

    private class mLeftBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!LockDisplayShowFlag) {
                selectList(view);
            }
        }
    }

    private void selectList(View view) {
        mCallbacks.onListSelected(view);
        setBackgroundColorById(view.getId());
    }

    public void selectFirst() {
        if (btnList != null && btnList.size() > 0) {
            selectList(btnList.get(0));
        }
    }

    private void setBackgroundColorById(int btnId) {
        for (ImageButton btn : btnList) {
            if (btn.getId() == btnId) {
                btn.setBackgroundResource(R.drawable.select_list);
            } else {
                btn.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
