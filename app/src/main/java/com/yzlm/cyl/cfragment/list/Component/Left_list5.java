package com.yzlm.cyl.cfragment.list.Component;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class Left_list5 extends SubFragment {
    private Callbacks mCallbacks;
    private static Left_list5 fragment = null;
    private List<ImageButton> btnList;

    public static Left_list5 newInstance(boolean reNew) {
        if (fragment == null || reNew) {
            fragment = new Left_list5();
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
        return R.layout.left_list5_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mLeft1 = v.findViewById(R.id.Left5_1);
        ImageButton mLeft2 = v.findViewById(R.id.Left5_2);
        ImageButton mLeft3 = v.findViewById(R.id.Left5_3);
        ImageButton mLeft4 = v.findViewById(R.id.Left5_4);
        ImageButton mLeft5 = v.findViewById(R.id.Left5_5);
        ImageButton mLeft6 = v.findViewById(R.id.Left5_6);
        btnList = new ArrayList<>();
        btnList.add(mLeft1);
        btnList.add(mLeft2);
        btnList.add(mLeft3);
        btnList.add(mLeft4);
        btnList.add(mLeft5);
        btnList.add(mLeft6);

        if (QueryMeasCateg(mCompName).equals("4") || QueryMeasCateg(mCompName).equals("13")) {
            mLeft2.setVisibility(View.GONE);
            mLeft4.setVisibility(View.GONE);
        }
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

    private void selectList(View view) {
        mCallbacks.onListSelected(view);
        setBackgroundColorById(view.getId());
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
}
