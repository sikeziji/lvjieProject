package com.yzlm.cyl.cfragment.Content.Component.List5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.DialogMsg;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Dialog.Dialog_datetime;
import com.yzlm.cyl.cfragment.Excel.ExcelUtils;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Control.CPullable.CPullToRefreshLayout;
import com.yzlm.cyl.clibrary.Control.CPullable.CPullableScrollView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getExtSdcard;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.makeDir;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.SDCardUtils.getStoragePath;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List5_Content5 extends SubFragment {
    private static List5_Content5 fragment = null;
    private int index = 0;
    private ErrorLog mError;
    private TextView tv;
    private String startTime = null;
    private String endTime = null;
    private Callbacks mCallbacks;
    private ErrorLog.msgType[] types = {ErrorLog.msgType.报错_警告, ErrorLog.msgType.报错_错误};
    private static Timer timerMsg = null;
    private static Thread export = null;
    ImageButton ImgBtnModuleWinReturn;
    LinearLayout mlayoutReturnBtn;

    public static List5_Content5 newInstance() {
        if (fragment == null) {
            fragment = new List5_Content5();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogRS();

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
        return R.layout.list5_content5;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        if (QueryMeasCateg(mCompName).equals("5") || QueryMeasCateg(mCompName).equals("6")) {
            Global.LockDisplayShowFlag = true;
        }
        tv = (TextView) v.findViewById(R.id.textView);
        tv.setOnClickListener(new tvClick());
//        tv.setOnLongClickListener(new tvlClick());
//        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        ((CPullToRefreshLayout) v.findViewById(R.id.PLwarn))
                .setOnRefreshListener(new MyListener());
        mError = new ErrorLog(context);
        refreshData();

        Button mSelect = (Button) v.findViewById(R.id.Btn_warn_select);
        mSelect.setOnClickListener(new Select());

        Button btnOutPutErrorInfo = (Button) v.findViewById(R.id.Btn_warn_OutPut);
        btnOutPutErrorInfo.setOnClickListener(new OutPutErrorInfoClickListener());

    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private class OutPutErrorInfoClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (export == null) {
                export = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            List<String[]> allDataItems;
                            List<Map> allList = mError.selectAlarmAll(mCompName, null, null, types, 0);
                            if (allList.size() > 0) {
                                allDataItems = new ArrayList<>();
                                try {
                                    for (Map item : allList) {
                                        String[] dataTitle = new String[]{"time", "errNum", "errInfo"};
                                        String[] dataSingleItem = new String[dataTitle.length];
                                        for (int i = 0; i < dataSingleItem.length; i++) {
                                            try {
                                                dataSingleItem[i] = item.get(dataTitle[i]).toString();
                                            } catch (NullPointerException e) {
                                                dataSingleItem[i] = "NULL";
                                            }
                                        }
                                        allDataItems.add(dataSingleItem);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String extSdcard = getExtSdcard();
                                if (extSdcard != null) {
                                    File file = new File(extSdcard + "waterAlarm");
                                    makeDir(file);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
                                    String[] title = new String[]{/*"ID",*/ getString(R.string.time), getString(R.string.error_num), getString(R.string.info)/*"时间", "错误号", "信息"*/};
                                    ExcelUtils.createExcelAndTitle(file.toString() + "/" + mCompName + "Alarm_" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".xls", getString(R.string.alarm), title);
                                    ExcelUtils.writeObjListToExcel(ErrorHandler, allDataItems, extSdcard + "waterAlarm" + "/" + mCompName + "alarm_" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".xls", context);
                                } else {
                                    Message msg = new Message();
                                    msg.what = 400;
                                    ErrorHandler.sendMessage(msg);
                                }
                            } else {
                                Message msg = new Message();
                                msg.what = 300;
                                ErrorHandler.sendMessage(msg);
                            }
                        }finally {
                            export = null;
                        }
                    }
                });
                export.start();
            }
        }
    }


    private static Handler ErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 300: {
                    export = null;
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.no_data_to_export));
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                }
                break;
                case 400: {
                    export = null;
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.no_external_SD_card));
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                }
                break;
                case 500: {
                    main.removeDestopText(mfb);

                    final DialogMsg dialogMsg = new DialogMsg();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-msg", context.getString(R.string.data_export));
                    dialogMsg.setArguments(bundle);
                    dialogMsg.show(fm, "Dialog_msg");
                    if (timerMsg == null) {
                        saveOperationLogMsg(mCompName, "报警记录导出", ErrorLog.msgType.操作_信息);
                        timerMsg = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                dialogMsg.dismiss();
                                main.removeDestopText(mfb);
                                Dialog_Err st = new Dialog_Err();
                                Bundle bundle = new Bundle();
                                String extSdcard = getExtSdcard();
                                if (extSdcard == null) {
                                    bundle.putString("alert-err", context.getString(R.string.data_export_fail));
                                } else {
                                    bundle.putString("alert-err", context.getString(R.string.data_export_success));
                                }
                                st.setArguments(bundle);
                                st.show(fm, "Dialog_err");
                                timerMsg.cancel();
                                timerMsg = null;
                                export = null;
                            }
                        };
                        timerMsg.schedule(task, 30000, 1000);
                    }
                }
                break;
            }
        }
    };

    private void refreshData() {
        List<Map> list = mError.select(mCompName, startTime, endTime, types, index);
        if (list.size() == 0) {
            tv.setText("");
            index -= 100;
        } else {
            tv.setText("");
            for (Map item : list) {
                tv.append(item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + "\t" + item.get("errInfo").toString() + "\n");
            }
            ((CPullableScrollView) v.findViewById(R.id.SVwarn)).fullScroll(ScrollView.FOCUS_UP);
        }
    }

    private class MyListener implements CPullToRefreshLayout.OnRefreshListener {
        @SuppressLint("HandlerLeak")
        @Override
        public void onRefresh(final CPullToRefreshLayout CPullToRefreshLayout) {
            index -= 100;
            if (index < 0) index = 0;
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

    private class tvClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            RealTimeStatusThread.resume();
        }
    }

    private class tvlClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            RealTimeStatusThread.suspend();
            return true;
        }
    }

    private class Select implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /* 关闭窗体悬浮（WL注释）*/
            main.removeDestopText(mfb);

            Dialog_datetime st = new Dialog_datetime();
            st.setShowPrint(false);
            st.setTargetFragment(List5_Content5.this, 1);
            st.show(fm, "alert-datetime");
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
            }
            refreshData();
        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }
}
