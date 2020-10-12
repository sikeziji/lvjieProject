package com.yzlm.cyl.cfragment.Content.NoBoard.List;

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

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/10/28.
 */

@SuppressWarnings("unchecked")
public class NoBoard_List1_Content7_p2 extends SubFragment {
    private Callbacks mCallbacks;
    private static NoBoard_List1_Content7_p2 fragment = null;

    public static NoBoard_List1_Content7_p2 newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content7_p2();
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
        return R.layout.no_board_list1_content7_p2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            List list = new ArrayList();
            list.add(1);// 仪器信息
            if (getPublicConfigData("LogInName").equals("3")) {
                list.add(2);// 操作日志 //管理员可查看

                list.add(4);// 管理员参数
            }
            if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                list.add(3);// 用户参数
            }
            get302ShowButton(list);
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


    private void get302ShowButton(List list) {

        GridLayout mGL = (GridLayout) v.findViewById(R.id.gl_302);
        mGL.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            try {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.ft_linelayout, null);
                Button btn = (Button) inflater.inflate(R.layout.ft_0505button, null);

                switch ((Integer) list.get(i)) {
                    case 1:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_yqxx));
                        btn.setId(R.id.btnNBYQXX);
                        break;
                    case 2:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_czrz));
                        btn.setId(R.id.btnNBCZRZ);
                        break;
                    case 3:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_yhcs));
                        btn.setId(R.id.btnNBUserParSet);
                        break;
                    case 4:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_glycs));
                        btn.setId(R.id.btnNBAdminParSet);
                        break;
                }
                layout.addView(btn);
                mGL.addView(layout);
                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) layout.getLayoutParams();
                layoutParams.setMargins(5, 5, 10, 5);
                layout.setLayoutParams(layoutParams);
                btn.setOnClickListener(new NoBoard_List1_Content7_p2.btnClick());

            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }
}
