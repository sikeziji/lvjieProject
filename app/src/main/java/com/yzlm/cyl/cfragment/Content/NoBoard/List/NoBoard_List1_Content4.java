package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.yzlm.cyl.cfragment.Adapter.CalibAdapter;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardDataDBHelper;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardKbf;
import com.yzlm.cyl.cfragment.Dialog.DialogMsg;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.Dialog.Dialog_datetime;
import com.yzlm.cyl.cfragment.Excel.ExcelUtils;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getExtSdcard;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.makeDir;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.SDCardUtils.getStoragePath;

/**
 * Created by zwj on 2019/07/29.
 */
public class NoBoard_List1_Content4 extends SubFragment {

    private static NoBoard_List1_Content4 fragment = null;
    private static ListView listViewCalib;
    private static CustomAdapter lvAdapter;
    private long index = 0;
    private int onePageNum = 9;
    private NoBoardKbf mKbf;
    private static List<String> items = new ArrayList<>();
    private static List<String[]> allKBFItems;
    private Context contextCalib;
    private Callbacks mCallbacks;
    private static Timer timerMsg = null;
    private static Thread export = null;

    public static NoBoard_List1_Content4 newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content4();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogRS();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (NoBoard_List1_Content4.Callbacks) context;
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
        return R.layout.no_board_list1_content4;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        items.clear();
        listViewCalib = (ListView) v.findViewById(R.id.listviewCalib);
        contextCalib = getContext();
        mKbf = new NoBoardKbf(context);
        refreshData(onePageNum);
        lvAdapter = new CustomAdapter(contextCalib, items);
        listViewCalib.setAdapter(lvAdapter);

        Button calibFirstPage = (Button) v.findViewById(R.id.calib_firstPage);
        calibFirstPage.setOnClickListener(new calibFirstPageClick());
        Button calibLastPage = (Button) v.findViewById(R.id.calib_lastPage);
        calibLastPage.setOnClickListener(new calibLastPageClick());
        Button calibNextPage = (Button) v.findViewById(R.id.calib_nextPage);
        calibNextPage.setOnClickListener(new calibNextPageClick());
        Button calibTailPage = (Button) v.findViewById(R.id.calib_tailPage);
        calibTailPage.setOnClickListener(new calibTailPageClick());

        Button calibSelect = (Button) v.findViewById(R.id.Btn_cal_select);
        calibSelect.setOnClickListener(new calibSelectClick());

        Button calibExport = (Button) v.findViewById(R.id.calib_export);
        calibExport.setOnClickListener(new calibExportClick());
    }

    private class calibExportClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (export == null) {
                export = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            List<Map> allList = mKbf.selectAllData(MainActivity.mNoBoardCompName);
                            if (allList.size() > 0) {
                                allKBFItems = new ArrayList<>();
                                for (Map item : allList) {
                                    String[] KBFSingleItem = new String[]{item.get("time").toString(), item.get("component").toString(),
                                            item.get("range").toString(), item.get("K").toString(), item.get("B").toString(), item.get("F").toString()};
                                    allKBFItems.add(KBFSingleItem);
                                }
                                String extSdcard = getExtSdcard();
                                if (extSdcard != null) {
                                    File file = new File(extSdcard + "waterData");
                                    makeDir(file);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
                                    String[] title = new String[]{getString(R.string.time), getString(R.string.component), getString(R.string.range), "K", "B", "F"};
                                    ExcelUtils.createExcelAndTitle(file.toString() + "/" + mNoBoardCompName + "KBF_" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".xls", "KBF", title);
                                    ExcelUtils.writeObjListToExcel(calibHandler, allKBFItems, extSdcard + "waterData" + "/" + mNoBoardCompName + "KBF_" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".xls", context);
                                } else {
                                    Message msg = new Message();
                                    msg.what = 400;
                                    calibHandler.sendMessage(msg);
                                }
                            } else {
                                Message msg = new Message();
                                msg.what = 300;
                                calibHandler.sendMessage(msg);
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


    @SuppressLint("HandlerLeak")
    private static Handler calibHandler = new Handler() {
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
                        saveNoBoardOperationLogMsg(mNoBoardCompName, "校准记录导出", ErrorLog.msgType.操作_信息);
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

    class CustomAdapter extends CalibAdapter {
        CustomAdapter(Context context, List<String> items) {
            super(context, items);
        }
    }

    private class calibFirstPageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            index = 0;
            refreshData(onePageNum);

            //listViewCalib.setAdapter(new CustomAdapter(contextCalib, items));
            lvAdapter.UpdateList(items);
        }
    }

    private class calibLastPageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /*将-20作为尾页索引的标志值*/
            if (index == -20) {
                index = 0;
            }
            index -= onePageNum;
            if (index < 0) index = 0;
            refreshData(onePageNum);

            //listViewCalib.setAdapter(new CustomAdapter(contextCalib, items));
            lvAdapter.UpdateList(items);
        }
    }

    private class calibNextPageClick implements View.OnClickListener {
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
                //listViewCalib.setAdapter(new CustomAdapter(contextCalib, items));
                lvAdapter.UpdateList(items);
            }
        }
    }

    private class calibTailPageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /*将-20作为尾页索引的标志值*/
            index = -20;
            long kbfSize = countKBFSelectData(MainActivity.mNoBoardCompName);
            long len = (kbfSize % onePageNum);
            refreshData(len);

            //listViewCalib.setAdapter(new CustomAdapter(contextCalib, items));
            lvAdapter.UpdateList(items);
        }
    }

    private class calibSelectClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            /* 关闭窗体悬浮（WL注释）*/
            main.removeDestopText(mfb);

            Dialog_datetime st = new Dialog_datetime();
            st.setShowPrint(false);
            st.setTargetFragment(NoBoard_List1_Content4.this, 1);
            st.show(fm, "alert-datetime");
        }
    }

    private boolean refreshData(long len) {

        if (index == -20) {
            long histSize = countKBFSelectData(MainActivity.mNoBoardCompName);
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
        List<Map> list = mKbf.select(MainActivity.mNoBoardCompName, null, null, null, index, len);
        int roundNum = Integer.parseInt(getNoBoardConfigData(MainActivity.mNoBoardCompName, "YXWS"));
        try {

            if (list.size() == 0) {
                index -= onePageNum;
                return false;
            } else {
                items.clear();
                items = new ArrayList<>();
                int count = 1;
                for (Map item : list) {
                    items.add((index + count++)/*item.get("id").toString()*/ + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + item.get("component").toString() + "\t" + item.get("range").toString() + "\t"
                            + (String.format("%." + roundNum + "f", Double.parseDouble(item.get("K").toString()))) + "\t"
                            + (String.format("%." + roundNum + "f", Double.parseDouble(item.get("B").toString()))) + "\t"
                            + (String.format("%." + roundNum + "f", Double.parseDouble(item.get("F").toString()))));
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]进行校准数据查询时发生错误：" + e.getClass().getName() + ",list大小为：" + list.size());
        }
        return true;
    }


    private long countKBFSelectData(String Component) {
        CompNoBoardDataDBHelper kbfDBHelper;
        kbfDBHelper = CompNoBoardDataDBHelper.getInstance(context);
        String sql = "select count(*) from KBF where component=?";
        String[] params = new String[]{Component};

        return kbfDBHelper.queryItemCount(sql, params);
    }


    private void refreshSelectData(String Component, String Range, String startTime, String endTime, long index, long len) {

        List<Map> list = mKbf.select(Component, Range, startTime, endTime, index, len);
        int roundNum = Integer.parseInt(getNoBoardConfigData(MainActivity.mNoBoardCompName, "YXWS"));
        try {
            items.clear();
            if (list.size() == 0) {
            } else {
                items = new ArrayList<>();
                int count = 1;
                for (Map item : list) {
                    items.add((index + count++)/*item.get("id").toString()*/ + "\t" + item.get("time").toString().substring(0, item.get("time").toString().length() - 3) + "\t" + item.get("component").toString() + "\t" + item.get("range").toString() + "\t"
                            + (String.format("%." + roundNum + "f", Double.parseDouble(item.get("K").toString()))) + "\t"
                            + (String.format("%." + roundNum + "f", Double.parseDouble(item.get("B").toString()))) + "\t"
                            + (String.format("%." + roundNum + "f", Double.parseDouble(item.get("F").toString()))));
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]进行校准数据查询时发生错误：" + e.getClass().getName() + ",list大小为：" + list.size());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
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
            refreshSelectData(MainActivity.mNoBoardCompName, null, startTime, endTime, 0, countKBFSelectData(MainActivity.mNoBoardCompName));
            //listViewCalib.setAdapter(new CustomAdapter(contextCalib, items));
            lvAdapter.UpdateList(items);
        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }
}
