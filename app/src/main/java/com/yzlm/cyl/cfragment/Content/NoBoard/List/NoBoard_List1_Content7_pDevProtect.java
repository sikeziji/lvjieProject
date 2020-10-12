package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_DevProtect.getWinPassword;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.passwordRandom;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by zwj on 2017/11/03.
 */

public class NoBoard_List1_Content7_pDevProtect extends SubFragment {

    private static NoBoard_List1_Content7_pDevProtect fragment = null;
    private Callbacks mCallbacks;


    public static NoBoard_List1_Content7_pDevProtect newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content7_pDevProtect();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.no_board_list1_content7_pdevprotect;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnNoBoardReturn_p2);
            mBtnReturn.setOnClickListener(new btnClick());

            /*安卓屏界面随机码*/
            TextView mWinPassword = (TextView) v.findViewById(R.id.winPassword);
            passwordRandom = getWinPassword(15);
            mWinPassword.setText(passwordRandom);

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]" + e.toString());
        }


    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            mCallbacks.onListSelected(v);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

}
