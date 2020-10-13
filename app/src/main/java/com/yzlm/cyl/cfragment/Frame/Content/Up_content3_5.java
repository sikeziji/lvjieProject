package com.yzlm.cyl.cfragment.Frame.Content;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.Component.Left_list4;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static android.view.View.VISIBLE;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.showBottleText;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;

/*
常规加稀释加蒸馏
 * Created by caoyiliang on 2016/10/27.
 */

public class Up_content3_5 extends SubFragment {
    private long firstClick;
    private TextView txtCalHitMsg;
    private Left_list4.Callbacks mCallbacks;
    // 计算点击的次数
    private int count;
    private static Up_content3_5 fragment = null;

    public static Up_content3_5 newInstance() {
        if (fragment == null) {
            fragment = new Up_content3_5();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Left_list4.Callbacks) context;

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
        return R.layout.up_content3_5_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            HorizontalScrollView mHorizontalScrollView = v.findViewById(R.id.main_d_hor_view);

            ImageButton mBtnJJTZ = v.findViewById(R.id.btn_jjtz);
            mBtnJJTZ.setOnClickListener(new btnJJTZClick());
            showBottleText(mCompName, v);
            TextView tZhcs = v.findViewById(R.id.M_CLZT);
            tZhcs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), getConfigData(mCompName, "zhcsFlow"), Toast.LENGTH_SHORT).show();
                }
            });
            TextView mDqzt = v.findViewById(R.id.M_DQZT);
            mDqzt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), ((AutoSampleEnable.get(mCompName).equals("true")) ? "" : getString(R.string.auto_measurement_is_closed))
                            + getString(R.string.working_condition) + ":" + workState.get(mCompName), Toast.LENGTH_LONG).show();
                }
            });

            ImageButton mBtnModuleXS_JJTZ = v.findViewById(R.id.btn_Module_XS_jjtz);
            mBtnModuleXS_JJTZ.setOnClickListener(new btnJJTZClick());

            ImageButton mBtnModuleZL_JJTZ = v.findViewById(R.id.btn_Module_ZL_jjtz);
            mBtnModuleZL_JJTZ.setOnClickListener(new btnJJTZClick());

            LinearLayout mLinearLayoutXS, mLinearLayoutZL;
            mLinearLayoutXS = v.findViewById(R.id.layout_dilution);
            mLinearLayoutZL = v.findViewById(R.id.layout_distillModule);

            if (isHaveMeasCategory(mCompName, "5")) {
                mLinearLayoutXS.setVisibility(View.VISIBLE);
            } else {
                mLinearLayoutXS.setVisibility(View.GONE);
            }
            if (isHaveMeasCategory(mCompName, "6")) {
                mLinearLayoutZL.setVisibility(View.VISIBLE);
            } else {
                mLinearLayoutZL.setVisibility(View.GONE);
            }
            LinearLayout mLayoutPump = v.findViewById(R.id.peristalticPump);
            LinearLayout mLayoutMeasuringModule = v.findViewById(R.id.measuringModule);
            if (QueryMeasBoardType(mCompName).equals("2")) {
                mLayoutPump.setVisibility(View.GONE);
                mLayoutMeasuringModule.setVisibility(View.GONE);
            } else {
                mLayoutPump.setVisibility(View.VISIBLE);
                mLayoutMeasuringModule.setVisibility(View.VISIBLE);
            }

            TextView mTvM_XJWD = (TextView) v.findViewById(R.id.M_XJWD);
            if (isHaveMeasCategory(mCompName, "6")) {
                mTvM_XJWD.setVisibility(View.GONE);
            } else {
                mTvM_XJWD.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "] Up_content3_5 " + e.toString());
        }
//        mCallbacks.onDesplayForMain();
    }

    private class btnJJTZClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
            if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
                count = 0;
            }
            count++;
            if (count == 1) {
                firstClick = System.currentTimeMillis();
            } else if (count == 2) {
                long lastClick = System.currentTimeMillis();
                // 两次点击小于300ms 也就是连续点击
                if (lastClick - firstClick < 300) {// 判断是否是执行了双击事件
                    //System.out.println(">>>>>>>>执行了双击事件");
                    switch (v.getId()) {
                        case R.id.btn_jjtz:
                            stopWorking(mCompName, true);
                            break;
                        case R.id.btn_Module_XS_jjtz:
                            if (QueryMeasCateg(mCompName + getModuleName("5")).equals("5")) {
                                SendManager.SendCmd(mCompName + getModuleName("5") + "_紧急停止" + "_8_07", S0, 3, 1000, null);
                            }
                            break;
                        case R.id.btn_Module_ZL_jjtz:
                            if (QueryMeasCateg(mCompName + getModuleName("6")).equals("6")) {
                                SendManager.SendCmd(mCompName + getModuleName("6") + "_紧急停止" + "_8_07", S0, 3, 1000, null);
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
