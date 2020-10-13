package com.yzlm.cyl.cfragment.list.Component;

import android.content.Context;
import android.content.Intent;
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
import static com.yzlm.cyl.cfragment.Global.LockDisplayShowFlag;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class Left_list4 extends SubFragment {
    private Callbacks mCallbacks;
    private static Left_list4 fragment = null;
    private List<ImageButton> btnList;

    public static Left_list4 newInstance() {
        if (fragment == null) {
            fragment = new Left_list4();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

//        void onDesplayForMain();
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
        return R.layout.left_list4_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try{
            ImageButton mLeft1 = v.findViewById(R.id.Left4_1);
            ImageButton mLeft2 = v.findViewById(R.id.Left4_2);
            ImageButton mLeft3 = v.findViewById(R.id.Left4_3);    // （用户管理）
            ImageButton mLeft4 = v.findViewById(R.id.Left4_4);    // （仪器维护）0505 界面
            ImageButton mLeft5 = v.findViewById(R.id.Left4_5);    // （设备管理）302 界面
            ImageButton mLeft6 = v.findViewById(R.id.Left4_6);    // （系统设置）寻峰、浊度 界面 流程配置
            ImageButton mLeft7 = v.findViewById(R.id.Left4_7);    //  (串口配置)
            btnList = new ArrayList<>();
            btnList.add(mLeft1);
            btnList.add(mLeft2);
            btnList.add(mLeft3);
            btnList.add(mLeft4);
            btnList.add(mLeft5);
            btnList.add(mLeft6);
            btnList.add(mLeft7);

            for (ImageButton btn : btnList) {
                btn.setOnClickListener(new mLeftBtnClick());
            }
            if (getModePermissions(MainActivity.mCompName, "功能测试")) {
                selectList(mLeft1);
                mLeft1.setVisibility(View.VISIBLE);
            } else {
                mLeft1.setVisibility(View.GONE);
                selectList(mLeft2);
            }

            mLeft4.setVisibility(View.GONE);
            mLeft5.setVisibility(View.GONE);
            mLeft6.setVisibility(View.GONE);
            if (!getPublicConfigData("LogInName").equals("0")) {
                if (getModePermissions(MainActivity.mCompName, "用户管理0505")) {
                    mLeft4.setVisibility(View.VISIBLE);
                }
                if (getModePermissions(MainActivity.mCompName, "用户管理302")) {
                    mLeft5.setVisibility(View.VISIBLE);
                }
                if (getPublicConfigData("LogInName").equals("3") || (getPublicConfigData("LogInName").equals("2"))) {
                    if (getModePermissions(MainActivity.mCompName, "用户管理123")) {
                        mLeft6.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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

    private void setBackgroundColorById(int btnId) {
        for (ImageButton btn : btnList) {
            if (btn.getId() == btnId) {
                btn.setBackgroundResource(R.drawable.select_list);
            } else {
                btn.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
