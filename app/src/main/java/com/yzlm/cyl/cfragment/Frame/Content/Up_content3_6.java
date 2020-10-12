package com.yzlm.cyl.cfragment.Frame.Content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Content.Chart.CurveChart;
import com.yzlm.cyl.cfragment.Content.Chart.DrawLineChart;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getHistoryData;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.showBottleText;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.readCurvePoint;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;

/*
 *  TOC主界面
 * Created by zwj on 2018/03/21.
 */

public class Up_content3_6 extends SubFragment {
    private long firstClick;
    private Callbacks mCallbacks;
    // 计算点击的次数
    private int count;

    static CurveChart curve_Chart;

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
        return R.layout.up_content3_6_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            ImageButton mBtnJJTZ = (ImageButton) v.findViewById(R.id.btn_jjtz);
            mBtnJJTZ.setOnClickListener(new BtnJJTZClick());
            InitWidget();
            showBottleText(mCompName, v);
            TextView tZhcs = (TextView) v.findViewById(R.id.M_CLZT);
            tZhcs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), getConfigData(mCompName, "zhcsFlow"), Toast.LENGTH_SHORT).show();
                }
            });
            TextView mDqzt = (TextView) v.findViewById(R.id.M_DQZT);
            mDqzt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), ((AutoSampleEnable.get(mCompName).equals("true")) ? "" : getString(R.string.auto_measurement_is_closed))
                            + getString(R.string.working_condition) + ":" + workState.get(mCompName), Toast.LENGTH_LONG).show();
                }
            });

            curve_Chart = v.findViewById(R.id.curve_chart);

            List<Map> list = getHistoryData(mCompName, 0, 1);
            if (list.size() > 0) {
                String timeStr = list.get(0).get("time").toString();

                float[] fVals = readCurvePoint(mCompName, timeStr);
                if (fVals != null) {
                    setDate(curve_Chart, fVals);
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "] Up_content3_2 " + e.toString());
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler refreshCurve_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            {
                try {
                    String strObj = (String) msg.obj;
                    String[] strs = strObj.split("_");
                    if (strs[0].equals(mCompName)) {
                        float[] fVals = readCurvePoint(strs[0], strs[1]);
                        if (fVals != null) {
                            curve_Chart.clearAnimation();
                            setDate(curve_Chart, fVals);
                        }
                    }
                } catch (Exception e) {
                    String strcatch = e.toString();
                }
            }
        }
    };

    private void InitWidget() {

    }

    private class BtnJJTZClick implements View.OnClickListener {
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

    public static void setDate(DrawLineChart chart, float[] fVals) {
        chart.setBrokenLineLTRB(60, 20, 10, 5);
        chart.setRadius(2.5f);
        chart.setCircleWidth(1f);
        chart.setBorderTextSize(10);
        chart.setBrokenLineTextSize(10);
//        chart.setNumberLine(0);
        chart.setBorderWidth(1f);
        chart.setBrokenLineWidth(1.5f);
        chart.setBorderTransverseLineWidth(0.3f);
        chart.setValue(fVals);

        float fMax = 0;
        for (float fV : fVals) {
            if (fV > fMax) {
                fMax = fV;
            }
        }
        fMax += 20;
        chart.setMaxVlaue(fMax);

        chart.setMinValue(0);
        chart.invalidate();
    }
}
