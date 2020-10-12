package com.yzlm.cyl.cfragment.list.NoBoard;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class NoBoard_Left_list1 extends SubFragment {
    private Callbacks mCallbacks;
    private static NoBoard_Left_list1 fragment = null;
    private List<ImageButton> btnList;

    public static NoBoard_Left_list1 newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new NoBoard_Left_list1();
        }
        return fragment;
    }

    public interface Callbacks {
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
        return R.layout.no_board_left_list1_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mLeft1 = (ImageButton) v.findViewById(R.id.noBoard_Left1_1);
        ImageButton mLeft2 = (ImageButton) v.findViewById(R.id.noBoard_Left1_2);
        ImageButton mLeft3 = (ImageButton) v.findViewById(R.id.noBoard_Left1_3);
        ImageButton mLeft4 = (ImageButton) v.findViewById(R.id.noBoard_Left1_4);
        ImageButton mLeft5 = (ImageButton) v.findViewById(R.id.noBoard_Left1_5);
        ImageButton mLeft6 = (ImageButton) v.findViewById(R.id.noBoard_Left1_6);
        ImageButton mLeft7 = (ImageButton) v.findViewById(R.id.noBoard_Left1_7);
        ImageButton mLeft8 = v.findViewById(R.id.noBoard_Left1_8);
        btnList = new ArrayList<>();
        btnList.add(mLeft1);
        btnList.add(mLeft2);
        btnList.add(mLeft3);
        btnList.add(mLeft4);
        btnList.add(mLeft5);
        btnList.add(mLeft6);
        btnList.add(mLeft7);
        btnList.add(mLeft8);

        for (ImageButton btn : btnList) {
            btn.setOnClickListener(new mLeftBtnClick());
        }
        selectList(mLeft1);
    }

    private class mLeftBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            selectList(view);

        }
    }

    private void setBackgroundColorById(int btnId) {
        for (ImageButton btn : btnList) {
            if (btn.getId() == btnId) {
                btn.setBackgroundResource(R.drawable.select_list);
            } else {
                btn.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private void selectList(View view) {
        mCallbacks.onListSelected(view);
        setBackgroundColorById(view.getId());
    }


}
