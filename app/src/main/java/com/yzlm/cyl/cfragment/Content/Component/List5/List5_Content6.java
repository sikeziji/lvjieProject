package com.yzlm.cyl.cfragment.Content.Component.List5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_datetime;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Control.CPullable.CPullToRefreshLayout;
import com.yzlm.cyl.clibrary.Control.CPullable.CPullableScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * Created by caoyiliang on 2016/10/28.
 * 运行日志
 */

public class List5_Content6 extends SubFragment {
    private static List5_Content6 fragment = null;

    private TextView tv;
    private int index = 0;
    private ErrorLog mError;
    private String startTime = null;
    private String endTime = null;
    private Callbacks mCallbacks;
    private ErrorLog.msgType[] types = {ErrorLog.msgType.报错_信息, ErrorLog.msgType.校准_信息
            , ErrorLog.msgType.运行_信息, ErrorLog.msgType.登录_信息, ErrorLog.msgType.运维_信息
            , ErrorLog.msgType.其他_信息};


    private Boolean isCheckLoginRecord = false;
    private Boolean isCheckRunRecord = false;
    private Boolean isCheckOtherRecord = false;
    private List<Map> loginDataRecord = new ArrayList<>();
    private List<Map> runDataRecord = new ArrayList<>();
    private List<Map> otherDataRecord = new ArrayList<>();


    public static List5_Content6 newInstance() {
        if (fragment == null) {
            fragment = new List5_Content6();
        }
        return fragment;
    }


    public interface Callbacks {
        void onDialogRS();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (List5_Content6.Callbacks) context;
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
        return R.layout.list5_content6;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        tv = (TextView) v.findViewById(R.id.tvRunningNotes);
        ((CPullToRefreshLayout) v.findViewById(R.id.PLRunningNotes))
                .setOnRefreshListener(new List5_Content6.MyListener());
        mError = new ErrorLog(context);
        refreshData();

        //查询
        Button mSelect = (Button) v.findViewById(R.id.Btn_runningnotes_select);
        mSelect.setOnClickListener(new List5_Content6.Select());
    }


    private class MyListener implements CPullToRefreshLayout.OnRefreshListener {
        @SuppressLint("HandlerLeak")
        @Override
        public void onRefresh(final CPullToRefreshLayout CPullToRefreshLayout) {
            index -= 100;
            if (index < 0)
                index = 0;
            refreshData();
            // 下拉刷新操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    CPullToRefreshLayout.refreshFinish(com.yzlm.cyl.clibrary.Control.CPullable.CPullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 100);
        }

        @SuppressLint("HandlerLeak")
        @Override
        public void onLoadMore(final CPullToRefreshLayout CPullToRefreshLayout) {
            index += 100;
            refreshData();
            // 加载操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件加载完毕了哦！
                    CPullToRefreshLayout.loadmoreFinish(com.yzlm.cyl.clibrary.Control.CPullable.CPullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 100);
        }
    }


    private class Select implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.Btn_runningnotes_select:
                    /* 关闭窗体悬浮（WL注释）*/
                    main.removeDestopText(mfb);

                    Dialog_datetime st = new Dialog_datetime();
                    st.setShowPrint(false);
                    st.setShowBtnLayout(true);//显示选择框
                    st.setTargetFragment(List5_Content6.this, 1);
                    st.show(fm, "alert-datetime");
                    break;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_datetime");
            mCallbacks.onDialogRS();
            if (stringExtra.equals(";")) {
                startTime = null;
                endTime = null;
            } else {
                String[] str = stringExtra.split(";");
                startTime = str[0];
                endTime = str[1];
                isCheckLoginRecord = Boolean.valueOf(str[2]);
                isCheckRunRecord = Boolean.valueOf(str[3]);
                isCheckOtherRecord = Boolean.valueOf(str[4]);
            }


            refreshData();
        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }


    //普通日志
    private void refreshData() {
        List<Map> list = new ArrayList<>();
        loginDataRecord.clear();
        runDataRecord.clear();
        otherDataRecord.clear();

        if (isCheckLoginRecord){
            loginDataRecord = mError.select(MainActivity.mCompName, startTime, endTime, new ErrorLog.msgType[]{ErrorLog.msgType.登录_信息}, index);
        }

        if (isCheckRunRecord){
            runDataRecord = mError.select(MainActivity.mCompName, startTime, endTime, new ErrorLog.msgType[]{ErrorLog.msgType.运维_信息}, index);
        }

        if (isCheckOtherRecord){
            otherDataRecord = mError.select(MainActivity.mCompName, startTime, endTime, new ErrorLog.msgType[]{ErrorLog.msgType.其他_信息}, index);
        }


        //若有勾选筛选条件，则显示筛选的数据，否则显示全部
        if (!isCheckLoginRecord && !isCheckRunRecord && !isCheckOtherRecord) {
            list.addAll(mError.select(MainActivity.mCompName, startTime, endTime, types, index));
        }else{
            list.addAll(loginDataRecord);
            list.addAll(runDataRecord);
            list.addAll(otherDataRecord);
        }


        if (list.size() == 0) {
            tv.setText("");
            index -= 100;
        } else {
            tv.setText("");


            //登录
            if (isCheckLoginRecord) {//登录信息
                for (Map item : loginDataRecord) {
                    tv.append(item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + "\t" + item.get("errInfo").toString() + "\n");
                }
            }


            //运维
            if (isCheckRunRecord) {//登录账号、操作时间、操作内容
                for (Map item : runDataRecord) {

                    String account = item.get("account").toString();
                    if (!TextUtils.isEmpty(account)) {
                        tv.append(account + "账号\t" + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + "\t" + item.get("errInfo").toString() + "\n");
                    } else {
                        tv.append("账号\t\t\t" + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + "\t" + item.get("errInfo").toString() + "\n");
                    }

                }
            }

            //其他
            if (isCheckOtherRecord) {//登录账号、操作时间、操作内容
                for (Map item : otherDataRecord) {
                    String account = item.get("account").toString();
                    if (!TextUtils.isEmpty(account)) {
                        tv.append(account + "账号\t" + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + "\t" + item.get("errInfo").toString() + "\n");
                    } else {
                        tv.append("账号\t\t\t" + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + "\t" + item.get("errInfo").toString() + "\n");
                    }
                }
            }


            //若没有任何筛选条件，则全部显示
            if (!isCheckLoginRecord && !isCheckRunRecord && !isCheckOtherRecord) {
                for (Map item : list) {
                    Log.i("aaa", item.toString() + "\n");
                    tv.append(item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + "\t" + item.get("errInfo").toString() + "\n");
                }
            }

            ((CPullableScrollView) v.findViewById(R.id.SVRunningNotes)).fullScroll(ScrollView.FOCUS_UP);
        }
    }

}
