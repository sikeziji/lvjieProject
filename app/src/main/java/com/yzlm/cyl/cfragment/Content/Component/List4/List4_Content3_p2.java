package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.versionID;

/**
 * Created by caoyiliang on 2016/10/28.
 */

@SuppressWarnings("unchecked")
public class List4_Content3_p2 extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_p2 fragment = null;

    public static List4_Content3_p2 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_p2();
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
        return R.layout.list4_content3_p2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.str557View = "List4_Content3_p2";

            List list = new ArrayList();
            list.add(1);// 仪器信息
            if (getPublicConfigData("LogInName").equals("3")) {
                //list.add(2);// 操作日志 //管理员可查看
                list.add(4);// 管理员参数
            }
            if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {
                list.add(3);// 用户参数
                list.add(5);// 报错屏蔽
                /*if (versionID == 0) {
                    // 读取组分版本兼容号
                    byte[] arrayOfByte = DataUtil.shortToByte((short) 557);
                    DataUtil.reverse(arrayOfByte);
                    SendManager.SendCmd(mCompName + "_读取组分版本兼容号" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 1);

                }*/
            }
            get302ShowButton(list);
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
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
                        btn.setId(R.id.btnYQXX);
                        break;
                    case 2:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_czrz));
                        btn.setId(R.id.btnCZRZ);
                        break;
                    case 3:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_yhcs));
                        btn.setId(R.id.btnUserParSet);
                        break;
                    case 4:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_glycs));
                        btn.setId(R.id.btnAdminParSet);
                        break;
                    case 5:
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_bcpb));
                        btn.setId(R.id.btnBCPB);
                        break;
                }
                layout.addView(btn);
                mGL.addView(layout);
                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) layout.getLayoutParams();
                layoutParams.setMargins(5, 5, 10, 5);
                layout.setLayoutParams(layoutParams);
                btn.setOnClickListener(new List4_Content3_p2.btnClick());

            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }
}