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

import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class Left_list2 extends SubFragment {
    private Callbacks mCallbacks;
    private static Left_list2 fragment = null;
    private List<ImageButton> btnList;
    private static ImageButton mLeft1;
    private static ImageButton mLeft3;
    private static ImageButton mLeft4;
    private static ImageButton mLeft5;
    private static ImageButton mLeft6;

    public static Left_list2 newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new Left_list2();
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
        return R.layout.left_list2_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        mLeft1 = v.findViewById(R.id.Left2_1);
        ImageButton mLeft2 = v.findViewById(R.id.Left2_2);
        mLeft3 = v.findViewById(R.id.Left2_3);
        mLeft4 = v.findViewById(R.id.Left2_4);
        mLeft5 = v.findViewById(R.id.Left2_5);
        mLeft6 = v.findViewById(R.id.Left2_6);

        if (QueryMeasCateg(MainActivity.mCompName).equals("4")) {
            mLeft3.setImageResource(R.drawable.l2l32);
        } else {

        }

        btnList = new ArrayList<>();
        btnList.add(mLeft1);
        btnList.add(mLeft2);
        btnList.add(mLeft3);
        btnList.add(mLeft4);
        btnList.add(mLeft5);
        btnList.add(mLeft6);
        for (ImageButton btn : btnList) {
            btn.setOnClickListener(new mLeftBtnClick());
        }
        showLeftWidget();

        if (getModePermissions(MainActivity.mCompName, "测量参数")) {
            selectList(mLeft1);
            mLeft1.setVisibility(View.VISIBLE);

        } else {
            mLeft1.setVisibility(View.GONE);
            selectList(mLeft2);
        }
    }

    private class mLeftBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            selectList(view);
        }
    }

    private void selectList(View view) {
        mCallbacks.onListSelected(view);
        setBackgroundColorById(view.getId());
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

    public static void showLeftWidget() {

        if (!getModePermissions(MainActivity.mCompName, "测量参数")) {
            mLeft1.setVisibility(View.GONE);
        } else {
            mLeft1.setVisibility(View.VISIBLE);
        }
        if (!getModePermissions(MainActivity.mCompName, "量程选择")) {
            mLeft3.setVisibility(View.GONE);
        } else {
            mLeft3.setVisibility(View.VISIBLE);
        }
        if (!getModePermissions(MainActivity.mCompName, "其他设置")) {
            mLeft4.setVisibility(View.GONE);
        } else {
            mLeft4.setVisibility(View.VISIBLE);
        }
        if (!getModePermissions(MainActivity.mCompName, "时间管理")) {
            mLeft5.setVisibility(View.GONE);
        } else {
            mLeft5.setVisibility(View.VISIBLE);
        }
        if (!getModePermissions(MainActivity.mCompName, "余量管理")) {
            mLeft6.setVisibility(View.GONE);
        } else {
            mLeft6.setVisibility(View.VISIBLE);
        }
    }

}
