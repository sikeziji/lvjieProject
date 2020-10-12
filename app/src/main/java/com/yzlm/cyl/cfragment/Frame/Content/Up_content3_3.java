package com.yzlm.cyl.cfragment.Frame.Content;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getHistoryData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;

/*
 * VOC主界面
 * Created by zwj on 2018/03/21.
 */

public class Up_content3_3 extends SubFragment {
    private long firstClick;
    private Callbacks mCallbacks;
    // 计算点击的次数
    private int count;
    private static Up_content3_3 fragment = null;


    public static Up_content3_3 newInstance() {
        if (fragment == null) {
            fragment = new Up_content3_3();
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
        return R.layout.up_content3_3_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            ImageButton mBtnJJTZ = v.findViewById(R.id.btn_jjtz);
            mBtnJJTZ.setOnClickListener(new btnJJTZClick());
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
            TextView mVOCCompName1 = v.findViewById(R.id.VOCCompName1);
            TextView mVOCCompName2 = v.findViewById(R.id.VOCCompName2);
            TextView mVOCCompName3 = v.findViewById(R.id.VOCCompName3);
            TextView mVOCCompName4 = v.findViewById(R.id.VOCCompName4);
            TextView mVOCCompName5 = v.findViewById(R.id.VOCCompName5);
            TextView mVOCCompName6 = v.findViewById(R.id.VOCCompName6);
            TextView mVOCCompName7 = v.findViewById(R.id.VOCCompName7);
            TextView mVOCCompName8 = v.findViewById(R.id.VOCCompName8);
            TextView mVOCCompName9 = v.findViewById(R.id.VOCCompName9);
            TextView mVOCCompName10 = v.findViewById(R.id.VOCCompName10);

            TextView mVOCCompMea1 = v.findViewById(R.id.VOCCompMea1);
            TextView mVOCCompMea2 = v.findViewById(R.id.VOCCompMea2);
            TextView mVOCCompMea3 = v.findViewById(R.id.VOCCompMea3);
            TextView mVOCCompMea4 = v.findViewById(R.id.VOCCompMea4);
            TextView mVOCCompMea5 = v.findViewById(R.id.VOCCompMea5);
            TextView mVOCCompMea6 = v.findViewById(R.id.VOCCompMea6);
            TextView mVOCCompMea7 = v.findViewById(R.id.VOCCompMea7);
            TextView mVOCCompMea8 = v.findViewById(R.id.VOCCompMea8);
            TextView mVOCCompMea9 = v.findViewById(R.id.VOCCompMea9);
            TextView mVOCCompMea10 = v.findViewById(R.id.VOCCompMea10);

            TextView mVOCCompUnit1 = v.findViewById(R.id.txtVocUnit1);
            TextView mVOCCompUnit2 = v.findViewById(R.id.txtVocUnit2);
            TextView mVOCCompUnit3 = v.findViewById(R.id.txtVocUnit3);
            TextView mVOCCompUnit4 = v.findViewById(R.id.txtVocUnit4);
            TextView mVOCCompUnit5 = v.findViewById(R.id.txtVocUnit5);
            TextView mVOCCompUnit6 = v.findViewById(R.id.txtVocUnit6);
            TextView mVOCCompUnit7 = v.findViewById(R.id.txtVocUnit7);
            TextView mVOCCompUnit8 = v.findViewById(R.id.txtVocUnit8);
            TextView mVOCCompUnit9 = v.findViewById(R.id.txtVocUnit9);
            TextView mVOCCompUnit10 = v.findViewById(R.id.txtVocUnit10);

            String Unit = getConfigData(mCompName, "UNIT");
            String YXWS = getConfigData(mCompName, "YXWS");

            mVOCCompUnit1.setText(Unit);
            mVOCCompUnit2.setText(Unit);
            mVOCCompUnit3.setText(Unit);
            mVOCCompUnit4.setText(Unit);
            mVOCCompUnit5.setText(Unit);
            mVOCCompUnit6.setText(Unit);
            mVOCCompUnit7.setText(Unit);
            mVOCCompUnit8.setText(Unit);
            mVOCCompUnit9.setText(Unit);
            mVOCCompUnit10.setText(Unit);

            LinearLayout mLayoutVOCComp1 = v.findViewById(R.id.layout_VOCComp1);
            LinearLayout mLayoutVOCComp2 = v.findViewById(R.id.layout_VOCComp2);
            LinearLayout mLayoutVOCComp3 = v.findViewById(R.id.layout_VOCComp3);
            LinearLayout mLayoutVOCComp4 = v.findViewById(R.id.layout_VOCComp4);
            LinearLayout mLayoutVOCComp5 = v.findViewById(R.id.layout_VOCComp5);
            LinearLayout mLayoutVOCComp6 = v.findViewById(R.id.layout_VOCComp6);
            LinearLayout mLayoutVOCComp7 = v.findViewById(R.id.layout_VOCComp7);
            LinearLayout mLayoutVOCComp8 = v.findViewById(R.id.layout_VOCComp8);
            LinearLayout mLayoutVOCComp9 = v.findViewById(R.id.layout_VOCComp9);
            LinearLayout mLayoutVOCComp10 = v.findViewById(R.id.layout_VOCComp10);


            List<Map> list = getHistoryData(mCompName, 0, 1);
            String str = "";
            if (list.size() > 0) {
                str = list.get(0).get("energy").toString();
                String[] strMea = str.split(";");

                switch (strMea.length) {
                    case 10:
                        mVOCCompName10.setText(strMea[9].split("=")[0]);
                        mVOCCompMea10.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[9].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit10.setVisibility(View.VISIBLE);
                        mLayoutVOCComp10.setVisibility(View.VISIBLE);
                    case 9:
                        mVOCCompName9.setText(strMea[8].split("=")[0]);
                        mVOCCompMea9.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[8].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit9.setVisibility(View.VISIBLE);
                        mLayoutVOCComp9.setVisibility(View.VISIBLE);
                    case 8:
                        mVOCCompName8.setText(strMea[7].split("=")[0]);
                        mVOCCompMea8.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[7].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit8.setVisibility(View.VISIBLE);
                        mLayoutVOCComp8.setVisibility(View.VISIBLE);
                    case 7:
                        mVOCCompName7.setText(strMea[6].split("=")[0]);
                        mVOCCompMea7.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[6].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit7.setVisibility(View.VISIBLE);
                        mLayoutVOCComp7.setVisibility(View.VISIBLE);
                    case 6:
                        mVOCCompName6.setText(strMea[5].split("=")[0]);
                        mVOCCompMea6.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[5].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit6.setVisibility(View.VISIBLE);
                        mLayoutVOCComp6.setVisibility(View.VISIBLE);
                    case 5:
                        mVOCCompName5.setText(strMea[4].split("=")[0]);
                        mVOCCompMea5.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[4].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit5.setVisibility(View.VISIBLE);
                        mLayoutVOCComp5.setVisibility(View.VISIBLE);
                    case 4:
                        mVOCCompName4.setText(strMea[3].split("=")[0]);
                        mVOCCompMea4.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[3].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit4.setVisibility(View.VISIBLE);
                        mLayoutVOCComp4.setVisibility(View.VISIBLE);
                    case 3:
                        mVOCCompName3.setText(strMea[2].split("=")[0]);
                        mVOCCompMea3.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[2].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit3.setVisibility(View.VISIBLE);
                        mLayoutVOCComp3.setVisibility(View.VISIBLE);
                    case 2:
                        mVOCCompName2.setText(strMea[1].split("=")[0]);
                        mVOCCompMea2.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[1].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit2.setVisibility(View.VISIBLE);
                        mLayoutVOCComp2.setVisibility(View.VISIBLE);
                    case 1:
                        mVOCCompName1.setText(strMea[0].split("=")[0]);
                        mVOCCompMea1.setText(ConvertUnit("mg/L", Unit, Double.valueOf(strMea[0].split("=")[1].split(" ")[0]), Integer.parseInt(YXWS)));
                        mVOCCompUnit1.setVisibility(View.VISIBLE);
                        mLayoutVOCComp1.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "] Up_content3_3 " + e.toString());
        }
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
                    stopWorking(mCompName, true);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
