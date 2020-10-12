package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.yzlm.cyl.cfragment.Dialog.Dialog_UserPassword;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.preWinForDialog;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/*
 * Created by zwj 2019年1月11日14:42:11
 */

public class List4_Content3_pUserPassword2 extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pUserPassword2 fragment = null;

    public static List4_Content3_pUserPassword2 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pUserPassword2();
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

            Dialog_UserPassword dialog_userPassword = new Dialog_UserPassword();
            Bundle bundle = new Bundle();
            bundle.putString("alert-UserPassword", "userPassword2_52");
            dialog_userPassword.setArguments(bundle);
            dialog_userPassword.setTargetFragment(this, 1);
            dialog_userPassword.show(fm, "userPassword");
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
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
            Log.e("exception", e.toString());
        }
        mCallbacks.onDialogRS();

    }
}
