package com.yzlm.cyl.cfragment.list.ModuleList;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.doFlowing;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class Module_Left_list1 extends SubFragment {
    private Callbacks mCallbacks;
    private static Module_Left_list1 fragment = null;
    private List<ImageButton> btnList;

    public static Module_Left_list1 newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new Module_Left_list1();
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
        return R.layout.module_left_list1_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mLeft1 = v.findViewById(R.id.module_Left1_1);
        ImageButton mLeft2 = v.findViewById(R.id.module_Left1_2);
        ImageButton mLeft3 = v.findViewById(R.id.module_Left1_3);
        ImageButton mLeft4 = v.findViewById(R.id.module_Left1_4);
        ImageButton mLeft5 = v.findViewById(R.id.module_Left1_5);
        ImageButton mLeft6 = v.findViewById(R.id.module_Left1_6);
        ImageButton mLeft7 = v.findViewById(R.id.module_Left1_7);
        ImageButton mLeft8 = v.findViewById(R.id.module_Left1_8);
        ImageButton mLeft9 = v.findViewById(R.id.module_Left1_9);
        ImageButton mLeft10 = v.findViewById(R.id.module_Left1_10);
        ImageButton mLeft11 = v.findViewById(R.id.module_Left1_11);
        ImageButton mLeft12 = v.findViewById(R.id.module_Left1_12);
        btnList = new ArrayList<>();
        btnList.add(mLeft1); // 软件版本
        btnList.add(mLeft2); // 功能测试
        btnList.add(mLeft3); // 泵阀组件
        btnList.add(mLeft4); // 计量参数
        btnList.add(mLeft5); // 计量单元
        btnList.add(mLeft6); // 报警记录
        btnList.add(mLeft7); // 稀释增加校准记录
        btnList.add(mLeft8); // 用户管理
        btnList.add(mLeft9); // 蒸馏测量参数
        btnList.add(mLeft10);// 配置流程
        btnList.add(mLeft11);// 流程信息
        btnList.add(mLeft12);// 蒸馏常规测试

        // 显示控件及添加事件
        for (ImageButton btn : btnList) {
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new mLeftBtnClick());
        }

        // 根据背板显示需要菜单
        if (QueryMeasCateg(mCompName).equals("6")) {
            mLeft7.setVisibility(View.GONE);// 蒸馏 没有校准记录

        } else if (QueryMeasCateg(mCompName).equals("5")) {
            mLeft9.setVisibility(View.GONE); // 稀释 没有测量参数
        }

        // 根据权限 隐藏菜单
        switch (getPublicConfigData("LogInName")) {
            case "0":
                // 泵阀组件
                mLeft3.setVisibility(View.GONE);
                // 计量参数
                mLeft4.setVisibility(View.GONE);
                // 计量单元
                mLeft5.setVisibility(View.GONE);
                // 用户管理
                mLeft8.setVisibility(View.GONE);
                // 配置流程
                mLeft10.setVisibility(View.GONE);
                // 流程信息
                mLeft11.setVisibility(View.GONE);
            case "1":
                // 用户管理
                mLeft8.setVisibility(View.GONE);
                // 配置流程
                mLeft10.setVisibility(View.GONE);
                // 流程信息
                mLeft11.setVisibility(View.GONE);
            case "2":
            case "3":
                break;
        }


        selectList(mLeft1);
    }

    private class mLeftBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            selectList(view);
        }
    }

    public void selectFirst() {
        if (btnList != null && btnList.size() > 0) {
            selectList(btnList.get(0));
        }
    }

    private void selectList(View view) {
        if (Global.LockModuleDisplayShowFlag) {
            return;
        }
        mCallbacks.onListSelected(view);
        if (!doFlowing.get(mCompName).equals(getString(R.string.waiting_for_instructions))) {
            switch (view.getId()) {
                case R.id.module_Left1_2:
                case R.id.module_Left1_3:
                case R.id.module_Left1_4:
                case R.id.module_Left1_5:
                case R.id.module_Left1_10:
                case R.id.module_Left1_11:
                    return;
            }
        }
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
}
