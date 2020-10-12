package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_SpecPeak;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.startSpecPeakFlow;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/*
 * Created by zwj on 2018年9月28日15:54:00
 */

public class List4_Content3_pSpecPeak extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pSpecPeak fragment = null;

    public static List4_Content3_pSpecPeak newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pSpecPeak();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();

        void showUpContent();
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
        return R.layout.list4_content3_pcoek;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            /* 关闭窗体悬浮*/
            MainActivity.main.removeDestopText(mfb);
            Dialog_SpecPeak dialog_kzCoe = new Dialog_SpecPeak();
            dialog_kzCoe.setTargetFragment(this, 1);
            dialog_kzCoe.show(fm, "SpecPeak");
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_SpecPeak");
            String[] str = stringExtra.split("_");
            List<String> flows = new ArrayList<>();
            flows.add(str[1]);
            flows.add(str[2]);
            Log.i("T2", str[1]);
            startSpecPeakFlow(str[0], flows);
            AutoSampleEnable.put(mCompName, "true");
            updateConfigData(mCompName, "ycjzFlag", "true");

            mCallbacks.showUpContent();
            saveOperationLogMsg(mCompName, "光谱寻峰校准", ErrorLog.msgType.操作_信息);
        }
        mCallbacks.onDialogRS();

    }
}
