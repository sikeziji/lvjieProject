package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yzlm.cyl.cfragment.Dialog.NoBoard.Dialog_NoBoard_KZCoe;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/*
 * Created by zwj on 2018/1/23.
 */

public class NoBoard_List1_Content7_pKZCoe extends SubFragment {
    private Callbacks mCallbacks;
    private static NoBoard_List1_Content7_pKZCoe fragment = null;

    public static NoBoard_List1_Content7_pKZCoe newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content7_pKZCoe();
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
            Dialog_NoBoard_KZCoe dialog_kzCoe = new Dialog_NoBoard_KZCoe();
            dialog_kzCoe.setTargetFragment(this, 1);
            dialog_kzCoe.show(fm, "coeK");
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]" + e.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbacks.onDialogRS();
    }
}
