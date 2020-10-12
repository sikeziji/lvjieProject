package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.yzlm.cyl.cfragment.Dialog.NoBoard.Dialog_NoBoardExternalKBCoe;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.preWinForDialog;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/*
 * Created by zwj 2018年10月24日10:17:11
 */

public class NoBoard_List1_Content7_pExtKBCoe extends SubFragment {
    private Callbacks mCallbacks;
    private static NoBoard_List1_Content7_pExtKBCoe fragment = null;

    public static NoBoard_List1_Content7_pExtKBCoe newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content7_pExtKBCoe();
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
        return R.layout.list4_content3_puserpassword;
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
            Dialog_NoBoardExternalKBCoe dialog_ExtKBCoe = new Dialog_NoBoardExternalKBCoe();
            dialog_ExtKBCoe.setTargetFragment(this, 1);
            dialog_ExtKBCoe.show(fm, "coeK");
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]" + e.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (preWinForDialog.equals("fragment_up_container")) {

                mCallbacks.showUpContent();
            }
        } catch (Exception e) {
            Log.e("exception",e.toString());
        }
        mCallbacks.onDialogRS();

    }
}
