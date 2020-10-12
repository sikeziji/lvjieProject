package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_KDHC_RangeSelect;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * *******************************************
 * 项目名称：water
 *
 * @Author zwj
 * 创建时间：2019/6/18   13:43
 * 用途
 * *******************************************
 **/
public class List4_Content4_KDHC_RangeSelect extends SubFragment {
    private Callbacks mCallbacks;

    private static List4_Content4_KDHC_RangeSelect fragment = null;

    public static List4_Content4_KDHC_RangeSelect newInstance() {
        if (fragment == null) {
            fragment = new List4_Content4_KDHC_RangeSelect();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list4_content3_kdhc_range_select;
    }

    @Override
    protected int getListResId() {
        return 0;
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
    protected void DoThings() {
        try {
            /* 关闭窗体悬浮*/
            MainActivity.main.removeDestopText(mfb);
            Dialog_KDHC_RangeSelect dialogKdhcRangeSelect = new Dialog_KDHC_RangeSelect();
            dialogKdhcRangeSelect.setTargetFragment(this, 1);
            dialogKdhcRangeSelect.show(fm, "kdhc");
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }
    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbacks.onDialogRS();
    }
}
