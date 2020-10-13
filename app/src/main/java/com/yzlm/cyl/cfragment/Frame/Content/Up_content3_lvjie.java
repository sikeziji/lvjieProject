package com.yzlm.cyl.cfragment.Frame.Content;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.list.Component.Left_list4;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static android.view.View.VISIBLE;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getLastValue;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.showBottleText;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.workState;

/*
常规加稀释加蒸馏
 * Created by caoyiliang on 2016/10/27.
 */

public class Up_content3_lvjie extends SubFragment implements View.OnClickListener {
    private long firstClick;
    private TextView txtCalHitMsg;
    private Left_list4.Callbacks mCallbacks;
    private static Up_content3_lvjie fragment = null;


    //隐藏按钮
    private ImageButton hideOrShowBtn;
    //项目名称
    private TextView name;
    //量程（结果）
    private TextView range;
    //反应池温度
    private TextView reactionTemp;
    //机箱温度
    private TextView crateTemp;
    //测试时间
    private TextView testTime;
    //当前状态
    private TextView nowState;
    //当前进度
    private TextView nowRate;
    //正在执行工作名称和时间
    private TextView work;
    //进度条
    private static ProgressBar progress_rate;


    public static Up_content3_lvjie newInstance() {
        if (fragment == null) {
            fragment = new Up_content3_lvjie();
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
        return R.layout.up_content3_lvjie_h;
    }

    @Override
    protected int getListResId() {

        return 0;
    }

    @Override
    protected void DoThings() {
        initView();
        initEvent();


    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        hideOrShowBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 初始化视图
     */
    private void initView() {
        hideOrShowBtn = v.findViewById(R.id.hideOrShowBtn);
        name = v.findViewById(R.id.name);
        range = v.findViewById(R.id.range);
        reactionTemp = v.findViewById(R.id.reactionTemp);
        crateTemp = v.findViewById(R.id.crateTemp);
        testTime = v.findViewById(R.id.testTime);
        nowState = v.findViewById(R.id.nowState);
        nowRate = v.findViewById(R.id.nowRate);
        work = v.findViewById(R.id.work);
        progress_rate = v.findViewById(R.id.progress_rate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Handler Up_content3_lvjie_Handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e("TAG", "消息发送成功");
                    componentMeasDisplayLJBean bean = (componentMeasDisplayLJBean) msg.obj;
                    String component = bean.getComponent();
                    ActionTable at = bean.getAt();
                    String status = bean.getStatus();
                    componentMeasDisplayLJ(component, at, status);
                    progress_rate.setProgress(getTate());
                    break;
            }
        }
    };


    /**
     * 常规内容  主界面
     *
     * @param component
     * @param at
     * @param status
     */
    private void componentMeasDisplayLJ(String component, ActionTable at, String status) {
        boolean wait = (status.equals(getString(R.string.waiting_for_instructions)) || status.equals(getString(R.string.selfCheck)));
        int iAction = 0;
        try {
            iAction = Integer.parseInt(getCmds(component).getCmd(52).getValue().toString());
        } catch (Exception e) {
            iAction = 0;
        }
        String action = at.GetActionName(getCmds(component).getCmd(52).getValue());
        String actionNext = at.GetActionName(getCmds(component).getCmd(53).getValue());
        String ActionTime = getCmds(component).getCmd(62).getValue() == null ? "" : " " + getCmds(component).getCmd(62).getValue() + "s";
        Log.v("FLOW", "MAINACTIVITY:" + component);
        String Unit = getConfigData(component, "UNIT");

        String Range = getConfigData(component, "RANGE");
        if (!wait) {
            if (!getConfigData(component, "PreCalSelectRange").equals("") && !getConfigData(component, "PreCalSelectRange").equals(getConfigData(component, "RANGE"))) {
                Range = getConfigData(component, "PreCalSelectRange");
            }
        }
        int point = Integer.parseInt(getConfigData(component, "YXWS"));


        //上次测量的结果值
        range.setText(getLastValue(component, null, Unit) + " " + Unit);
        //单位转换
        String LCL = ConvertUnit("mg/L", Unit, (getConfigData(component, "LC" + Range + "L")), point);
        String LCH = ConvertUnit("mg/L", Unit, (getConfigData(component, "LC" + Range + "H")), point);

        //当前状态
        nowState.setText(status);
        if (wait && action.equals(getString(R.string.ready))) {
            work.setVisibility(View.GONE);
            nowRate.setVisibility(View.GONE);
        } else {
            work.setVisibility(VISIBLE);
            nowRate.setVisibility(VISIBLE);
            //显示当前动作和时间
            work.setText(action + " " + ActionTime);
            nowRate.setText("当前进度 " + getTate() + "%");
        }
    }

    int a = 0;

    private int getTate() {
        //TODO 测试使用，默认为10
        a += 10;
        return a;
    }
}

