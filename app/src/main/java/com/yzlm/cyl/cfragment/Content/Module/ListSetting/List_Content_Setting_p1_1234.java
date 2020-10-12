package com.yzlm.cyl.cfragment.Content.Module.ListSetting;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;

/**
 * Created by caoyiliang on 2016/10/28.
 */

@SuppressWarnings("unchecked")
public class List_Content_Setting_p1_1234 extends SubFragment {
    private Callbacks mCallbacks;
    private static List_Content_Setting_p1_1234 fragment = null;


    public static List_Content_Setting_p1_1234 newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Setting_p1_1234();
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
        return R.layout.list_content4_p2_1234;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        List list = new ArrayList();

        if (isHaveMeasCategory(mCompName, "5")) {
            list.add(1);// 稀释背板
        }
        if (isHaveMeasCategory(mCompName, "6")) {
            list.add(2);// 蒸馏背板
        }
        getShowButton(list);
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private void getShowButton(List list) {

        GridLayout mGL = (GridLayout) v.findViewById(R.id.gl_1234);
        mGL.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            try {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.ft_linelayout, null);
                Button btn = (Button) inflater.inflate(R.layout.ft_0505button, null);

                switch ((Integer) list.get(i)) {
                    case 1:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_xsmk));
                        btn.setId(R.id.btnXSBB);
                        break;
                    case 2:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_zlmk));
                        btn.setId(R.id.btnZLBB);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
                layout.addView(btn);
                mGL.addView(layout);
                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) layout.getLayoutParams();
                layoutParams.setMargins(5, 5, 10, 5);
                layout.setLayoutParams(layoutParams);
                btn.setOnClickListener(new List_Content_Setting_p1_1234.btnClick());

            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }
}
