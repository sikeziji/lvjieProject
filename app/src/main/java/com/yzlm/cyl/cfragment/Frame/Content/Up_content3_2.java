package com.yzlm.cyl.cfragment.Frame.Content;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.showBottleText;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;

/*
 * 滴定主界面
 * Created by zwj on 2018/03/21.
 */

public class Up_content3_2 extends SubFragment {
    private long firstClick;
    private Callbacks mCallbacks;
    // 计算点击的次数
    private int count;
    static Up_content3_2 fragment = null;
    LineChart mChart;
    TextView mTxtVn;


    public static Up_content3_2 newInstance() {
        if (fragment == null) {
            fragment = new Up_content3_2();
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
        return R.layout.up_content3_2_h;
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
            List<Map> list;
            History mHistory = new History(context);

            list = mHistory.select(mCompName, null, null, null, 0, 1);
            if (list.size() > 0) {
                String energy = list.get(0).get("energy").toString();
                mTxtVn.setText(editDataFormat(energy.split(";")[0], Integer.parseInt(getConfigData(mCompName, "YXWS"))));
            }
        }catch (Exception e){
            saveExceptInfo2File("组分[" + mCompName + "] Up_content3_2 "+e.toString());
        }

      /*  InitChart();
        final String[] aa = {"12.23", "30.33", "43.2", "49.2", "57.3", "77.1", "88.2","100","50","123","124","134","135","134"};
        final String[] bb = {"12.43", "25.55", "20.333", "26.44", "60.4", "28.44", "20.33","23","80","2","4","4","4","56.4"};
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(int i=0;i<aa.length;i++) {

                        addEntry(Float.valueOf( bb[i]),Float.valueOf(aa[i]));
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                }
            }
        }).start();*/
    }

    private void InitWidget() {
        mTxtVn = (TextView) v.findViewById(R.id.txt_ddVn);

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
                    stopWorking(mCompName,true);
                }
            }
        }
    }

    //画图
    private void InitChart() {
        mChart = (LineChart) v.findViewById(R.id.chart);
        // 曲线描述 -标题
        mChart.setDescription("");
        // 如果没有数据的时候，会显示这个，类似文本框的placeholder
        mChart.setNoDataTextDescription("");
        // 没有数据的时候，显示“”
        mChart.setNoDataText("");

        //设置是否启动触摸响应
        mChart.setTouchEnabled(true);
        // 可拖曳
        mChart.setDragEnabled(true);
        // 可缩放
        mChart.setScaleEnabled(true);
        // 是否显示表格颜色
        mChart.setDrawGridBackground(false);

        mChart.setPinchZoom(true);

        // 设置图表的背景颜色
        //mChart.setBackgroundColor(Color.WHITE);
        // 不显示图例
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        LineData data = new LineData();
        // 数据显示的颜色
        data.setValueTextColor(Color.WHITE);

        data.setDrawValues(false);

        // 先增加一个空的数据，随后往里面动态添加
        mChart.setData(data);

        // 图表的注解(只有当数据集存在时候才生效)
        Legend l = mChart.getLegend();
        // 可以修改图表注解部分的位置
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        // 线性，也可是圆
        l.setForm(Legend.LegendForm.LINE);
        // 颜色
        l.setTextColor(Color.WHITE);

        // x坐标轴
        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(false);
        xl.setTextSize(25f);

        // 几个x坐标轴之间才绘制？
        xl.setSpaceBetweenLabels(1);

        // 如果false，那么x坐标轴将不可见
        xl.setEnabled(true);

        // 将X坐标轴放置在底部，默认是在顶部。
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 图表左边的y坐标轴线
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(25f);
        // 最大值
        //leftAxis.setAxisMaxValue(90f);
        // 最小值
        //leftAxis.setAxisMinValue(40f);
        // 不一定要从0开始
        leftAxis.setStartAtZero(true);

        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        // 不显示图表的右边y坐标轴线
        rightAxis.setEnabled(false);


    }

    // 添加进去一个坐标
    public void addEntry(float yValue, float xValue) {

        LineData data = mChart.getData();

        // 每一个LineDataSet代表一条线，每张统计图表可以同时存在若干个统计折线，这些折线像数组一样从0开始下标。
        // 本例只有一个，那么就是第0条折线
        LineDataSet set = data.getDataSetByIndex(0);

        // 如果该统计折线图还没有数据集，则创建一条出来，如果有则跳过此处代码。
        if (set == null) {
            set = createLineDataSet();
            data.addDataSet(set);
        }
        // 设置曲线为圆滑的线
        set.setDrawCubic(false);
        set.setCubicIntensity(0.2f);

        // 先添加一个x坐标轴的值
        // 因为是从0开始，data.getXValCount()每次返回的总是全部x坐标轴上总数量，所以不必多此一举的加1
        data.addXValue(String.valueOf(xValue));

        // 生成随机测试数
        //float f = (float) ((Math.random()) * 20 + 50);

        // set.getEntryCount()获得的是所有统计图表上的数据点总量，
        // 如从0开始一样的数组下标，那么不必多次一举的加1
        Entry entry = new Entry(yValue, set.getEntryCount());

        // 往linedata里面添加点。注意：addentry的第二个参数即代表折线的下标索引。
        // 因为本例只有一个统计折线，那么就是第一个，其下标为0.
        // 如果同一张统计图表中存在若干条统计折线，那么必须分清是针对哪一条（依据下标索引）统计折线添加。
        data.addEntry(entry, 0);

        // 像ListView那样的通知数据更新
        mChart.notifyDataSetChanged();

        // 当前统计图表中最多在x轴坐标线上显示的总量
        mChart.setVisibleXRangeMaximum(50);

        // y坐标轴线最大值
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);

        // 将坐标移动到最新
        // 此代码将刷新图表的绘图
        mChart.moveViewToX(data.getXValCount() - 1);
        // mChart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);
    }

    // 初始化数据集，添加一条统计折线，可以简单的理解是初始化y坐标轴线上点的表征
    private LineDataSet createLineDataSet() {

        LineDataSet set = new LineDataSet(null, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        // 折线的颜色
        //set.setColor(ColorTemplate.getHoloBlue());
        set.setColor(Color.BLACK);
        // 不显示坐标点的小圆点
        set.setDrawCircles(true);

        set.setCircleColor(Color.BLACK);
        set.setLineWidth(3f);
        set.setCircleSize(5f);
        set.setFillAlpha(128);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.GREEN);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        set.setDrawValues(true);
        return set;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
