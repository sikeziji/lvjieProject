package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_datetime;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Control.CPullable.CPullToRefreshLayout;
import com.yzlm.cyl.clibrary.Control.CPullable.CPullableScrollView;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pczrz extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pczrz fragment = null;

    private TextView tv;
    private int index = 0;
    private DevLog mOperationLog;
    private String startTime = null;
    private String endTime = null;
    private ErrorLog.msgType[] types = {ErrorLog.msgType.操作_信息};


    public static List4_Content3_pczrz newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pczrz();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

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
        return R.layout.list4_content3_pczrz;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p2);
            mBtnReturn.setOnClickListener(new btnClick());

            tv = (TextView) v.findViewById(R.id.tvOperationNotes);
            ((CPullToRefreshLayout) v.findViewById(R.id.PLOperationNotes))
                    .setOnRefreshListener(new MyListener());
            mOperationLog = new DevLog(context);
            refreshData();

            Button mSelect = (Button) v.findViewById(R.id.Btn_Operation_select);
            mSelect.setOnClickListener(new Select());

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


    private class Select implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /* 关闭窗体悬浮（WL注释）*/
            main.removeDestopText(mfb);

            Dialog_datetime st = new Dialog_datetime();
            st.setShowPrint(false);
            st.setTargetFragment(List4_Content3_pczrz.this, 1);
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

    private void refreshData() {
        List<Map> list = mOperationLog.select(MainActivity.mCompName, startTime, endTime, types, index);
        if (list.size() == 0) {
            tv.setText("");
            index -= 100;
        } else {
            tv.setText("");
            for (Map item : list) {
                tv.append(item.get("time").toString() + "\t" + item.get("content").toString() + "\n");
            }
            ((CPullableScrollView) v.findViewById(R.id.SVOperationNotes)).fullScroll(ScrollView.FOCUS_UP);
        }
    }
}
