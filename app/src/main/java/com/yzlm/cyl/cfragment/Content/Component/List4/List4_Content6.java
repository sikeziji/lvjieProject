package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy.getCalcAFormula;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.isHaveFlow;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/10/28.
 */

@SuppressWarnings("unchecked")
public class List4_Content6 extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content6 fragment = null;


    public static List4_Content6 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content6();
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
        return R.layout.list4_content6;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            //只有运维和管理员才会进该界面
            List list = new ArrayList();
            list.add(7);
            list.add(4);
            list.add(9);
            if (MainActivity.mCompName.equals("TN") || MainActivity.mCompName.equals("TP")) {
                String calcARevise = getCalcAFormula(mCompName, Byte.valueOf(getConfigData(mCompName, "RANGE")));
                if (calcARevise.contains("KZ1")) {
                    list.add(1);// 浊度补偿
                }
            }
            if (MainActivity.mCompName.equals("TN")) {
                list.add(2);// 自动寻峰
            }
            if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                if (isHaveFlow(mCompName, context.getResources().getString(R.string.KDHC), null, GetPlatRangSum(mCompName))) {
                    list.add(3);// 跨度核查量程选择
                }
            }
            if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                if (QueryMeasCateg(mCompName).equals("2") || QueryMeasCateg(mCompName).equals("1") || QueryMeasCateg(mCompName).equals("9")) {
                    list.add(5);
                }
            }
            list.add(6);
            if (getPublicConfigData("LogInName").equals("3")) {
                list.add(8);
            }
            list.add(10);
            getShowButton(list);
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private void getShowButton(List list) {

        GridLayout mGL = (GridLayout) v.findViewById(R.id.gl_content4_6);
        mGL.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            try {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.ft_linelayout, null);
                Button btn = (Button) inflater.inflate(R.layout.ft_0505button, null);

                switch ((Integer) list.get(i)) {
                    case 1:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.turbidityCom));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_turbidity_compensation);
                        break;
                    case 2:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.Homing_peak));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_homing_peak);
                        break;
                    case 3:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.displayKDHCSetting));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_kdhc_range_select);
                        break;
                    case 4:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.flowConfigInfo));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_flow_cfg_setting);
                        break;
                    case 5:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.oneKeyCal));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_range_cal_setting);
                        break;
                    case 6:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.systemConfig));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_update_config);
                        break;
                    case 7:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.upgradeBoard));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_upgrade_board);
                        break;
                    case 8:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText("数据恢复");
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_sjhf);
                        break;
                    case 9:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.directoryLook));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_directory);
                        break;
                    case 10:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_big));
                        btn.setText(getResources().getString(R.string.version));
                        btn.setTextSize(30);
                        btn.setId(R.id.btn_fileinfo);
                        break;
                }
                layout.addView(btn);
                mGL.addView(layout);
                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) layout.getLayoutParams();
                layoutParams.setMargins(5, 5, 10, 5);
                layout.setLayoutParams(layoutParams);
                btn.setOnClickListener(new btnClick());

            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }

}
