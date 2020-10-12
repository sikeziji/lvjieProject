package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.yzlm.cyl.cfragment.Adapter.HistoryAdapter;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardDataDBHelper;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardHistory;
import com.yzlm.cyl.cfragment.Dialog.DialogMsg;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Dialog.Dialog_datetime;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.HistoryExport;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.PrintHistoryData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getExtSdcard;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.AnalogQuantity_CycleThread;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * Created by zwj on 2019/07/25.
 */

public class NoBoard_List1_Content3 extends SubFragment {

    private static ListView listView;
    private static CustomAdapter lvAdapter;
    private long index = 0;
    private int onePageNum = 10;

    private NoBoardHistory mHistory;
    private static List<String> items = new ArrayList<>();
    private static NoBoard_List1_Content3 fragment = null;
    private static Context contextHistory;

    private static Timer timerMsg;
    private Callbacks mCallbacks;

    private static DialogMsg dialogMsg;
    private static Thread export = null;
    //private boolean isShow = true;

    private int isShow = 1;

    public static NoBoard_List1_Content3 newInstance() {
        if (fragment == null) {

            fragment = new NoBoard_List1_Content3();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogRS();
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
        return R.layout.no_board_list1_content3;
    }

    @Override
    protected int getListResId() {
        return 0;
    }


    @Override
    protected void DoThings() {
        listView = (ListView) v.findViewById(R.id.listview);
        contextHistory = getContext();

        mHistory = new NoBoardHistory(context);
        items.clear();
        isShow = Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "EnOrAIsShow"));
        refreshData(onePageNum);
        //isShow = getPublicConfigData("EnIsShow").equals("true");
        lvAdapter = new CustomAdapter(contextHistory, items, isShow == 0 ? false : true);
        listView.setAdapter(lvAdapter);

        Button btnFirstPage = (Button) v.findViewById(R.id.Btn_firstPage);
        btnFirstPage.setOnClickListener(new btnFirstPageClick());
        Button btnLastPage = (Button) v.findViewById(R.id.Btn_lastPage);
        btnLastPage.setOnClickListener(new btnLastPageClick());
        Button btnNextPage = (Button) v.findViewById(R.id.Btn_nextPage);
        btnNextPage.setOnClickListener(new btnNextPageClick());
        Button btnTailPage = (Button) v.findViewById(R.id.Btn_tailPage);
        btnTailPage.setOnClickListener(new btnTailPageClick());

        Button btnDataSelect = (Button) v.findViewById(R.id.Btn_history_select);
        btnDataSelect.setOnClickListener(new btnHistorySelectClick());

        Button btnDataExport = (Button) v.findViewById(R.id.Btn_export);
        btnDataExport.setOnClickListener(new btnDataExportClick());
    }

    private class btnDataExportClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (export == null) {
                export = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HistoryExport(dataHandler, (mHistory).selectAllData(mNoBoardCompName), mNoBoardCompName);
                        } finally {
                            export = null;
                        }
                    }
                });
                export.start();
            }
        }
    }


    @SuppressLint("HandlerLeak")
    private static Handler dataHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
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
                        saveNoBoardOperationLogMsg(mNoBoardCompName, "历史数据导出", ErrorLog.msgType.操作_信息);
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
                case 600:
                    export = null;
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", context.getString(R.string.unknow_exception_to_export));
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                    break;
            }
        }
    };

    class CustomAdapter extends HistoryAdapter {

        CustomAdapter(Context context, List<String> items, boolean isShow) {
            super(context, items, isShow);
        }

        @Override
        public void Delete(int pos) {

        }
    }

    private class btnFirstPageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            index = 0;
            refreshData(onePageNum);

            //listView.setAdapter(new CustomAdapter(contextHistory, items, isShow));
            lvAdapter.UpdateList(items, isShow == 0 ? false : true);
        }
    }

    private class btnLastPageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /*将-20作为尾页索引的标志值*/
            if (index == -20) {
                index = 0;
            }
            index -= onePageNum;
            if (index < 0) index = 0;
            refreshData(onePageNum);

            //listView.setAdapter(new CustomAdapter(contextHistory, items, isShow));
            lvAdapter.UpdateList(items, isShow == 0 ? false : true);
        }
    }

    private class btnNextPageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /*将-20作为尾页索引的标志值*/
            if (index == -20) {
                index = 0;
            }
            index += onePageNum;

            if (!refreshData(onePageNum)) {
                main.removeDestopText(mfb);
                Dialog_Err st = new Dialog_Err();
                Bundle bundle = new Bundle();
                bundle.putString("alert-err", getString(R.string.the_last_page));
                st.setArguments(bundle);
                st.show(fm, "Dialog_err");
            } else {
                //listView.setAdapter(new CustomAdapter(contextHistory, items, isShow));
                lvAdapter.UpdateList(items, isShow == 0 ? false : true);
            }
        }
    }

    private class btnTailPageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /*将-20作为尾页索引的标志值*/
            index = -20;
            long histSize = countSelectData(MainActivity.mNoBoardCompName);
            long len = (histSize % onePageNum);
            refreshData(len);

            //listView.setAdapter(new CustomAdapter(contextHistory, items, isShow));
            lvAdapter.UpdateList(items, isShow == 0 ? false : true);
        }
    }

    private class btnHistorySelectClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /* 关闭窗体悬浮（WL注释）*/
            main.removeDestopText(mfb);

            Dialog_datetime st = new Dialog_datetime();
            st.setShowPrint(true);
            st.setTargetFragment(NoBoard_List1_Content3.this, 1);
            st.show(fm, "alert-datetime");
        }
    }

    private boolean refreshData(long len) {

        if (index == -20) {
            long histSize = countSelectData(MainActivity.mNoBoardCompName);
            index = (histSize / onePageNum) * onePageNum;
            // 末页为整数页的情况
            if (len == 0) {
                index -= onePageNum;
                len = onePageNum;
            }
        }
        if (index < 0) {
            index = 0;
        }
        List<Map> list = mHistory.select(MainActivity.mNoBoardCompName, null, null, null, index, len);

        try {

            if (list.size() == 0) {
                index -= onePageNum;
                return false;
            } else {
                items.clear();
                items = new ArrayList<>();
                int count = 1;
                String unitStr = "mg/L";
                if (!getNoBoardConfigData(mNoBoardCompName, "UNIT").equals("")) {
                    unitStr = getNoBoardConfigData(mNoBoardCompName, "UNIT");
                }
                for (Map item : list) {

                    String rs = ConvertUnit(item.get("unit").toString(), unitStr, Double.parseDouble(item.get("C").toString()), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    if (isShow == 2) {
                        items.add((index + count++) + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + item.get("component").toString() + "=" + rs + " " + unitStr + "\t" + item.get("flow") + "\t" + editDataFormat(item.get("A").toString(), 6) + "\t" + (item.get("tag") == null ? "" : item.get("tag")));
                    } else {
                        items.add((index + count++) + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + item.get("component").toString() + "=" + rs + " " + unitStr + "\t" + item.get("flow") + "\t" + item.get("energy") + "\t" + (item.get("tag") == null ? "" : item.get("tag")));
                    }
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]进行历史数据查询时发生错误：" + e.getClass().getName() + ",list大小为：" + list.size());
        }
        return true;
    }

    private long countSelectData(String Component) {

        CompNoBoardDataDBHelper historyDBHelper;
        historyDBHelper = CompNoBoardDataDBHelper.getInstance(context);
        String sql = "select count(*) from History where component=?";
        String[] params;

        params = new String[]{Component};

        return historyDBHelper.queryItemCount(sql, params);
    }

    private void refreshSelectData(String Component, String Flow, String startTime, String endTime, long index, long len) {

        List<Map> list = mHistory.select(Component, Flow, startTime, endTime, index, len);
        try {
            items.clear();
            if (list.size() == 0) {
            } else {
                items = new ArrayList<>();
                int count = 1;
                String unitStr = "mg/L";
                if (!getNoBoardConfigData(mNoBoardCompName, "UNIT").equals("")) {
                    unitStr = getNoBoardConfigData(mNoBoardCompName, "UNIT");
                }
                for (Map item : list) {
                    String rs = ConvertUnit(item.get("unit").toString(), unitStr, Double.parseDouble(item.get("C").toString()), Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
                    items.add((index + count++) + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + item.get("component").toString() + "=" + rs + " " + unitStr + "\t" + item.get("flow") + "\t" + item.get("energy") + "\t" + item.get("tag"));
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]进行历史数据查询时发生错误：" + e.getClass().getName() + ",list大小为：" + list.size());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                findSelectHistoryData(data);
            }
            if (requestCode == 2) {
                mCallbacks.onDialogRS();
            }
            if (requestCode == 3) {
                mCallbacks.onDialogRS();
                findSelectHistoryData(data);
                if (data.getStringExtra("Dialog_datetime").equals(";")) {
                    return;
                }
                PrintHistoryData(handler, items, mNoBoardCompName, Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));
            }
        } catch (Exception e) {
            saveExceptInfo2File(e.toString());
        }
    }


    private void findSelectHistoryData(Intent data) {
        String startTime, endTime;
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
        refreshSelectData(MainActivity.mNoBoardCompName, null, startTime, endTime, 0, countSelectData(MainActivity.mNoBoardCompName));

        //listView.setAdapter(new CustomAdapter(contextHistory, items, isShow));
        lvAdapter.UpdateList(items, isShow == 0 ? false : true);
    }

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    main.removeDestopText(mfb);
                    dialogMsg = new DialogMsg();

                    Bundle bundle = new Bundle();
                    bundle.putString("alert-msg", getString(R.string.printing));
                    dialogMsg.setArguments(bundle);
                    dialogMsg.setTargetFragment(NoBoard_List1_Content3.this, 4);
                    dialogMsg.show(fm, "Dialog_msg");
                    break;
                case 2:
                    AnalogQuantity_CycleThread.resume();
                    if (dialogMsg != null) {
                        dialogMsg.dismiss();
                    }
                    mCallbacks.onDialogRS();
                    break;
            }
        }
    };
}
