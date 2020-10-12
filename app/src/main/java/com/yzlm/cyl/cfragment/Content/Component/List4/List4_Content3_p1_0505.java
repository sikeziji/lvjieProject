package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/10/28.
 */

@SuppressWarnings("unchecked")
public class List4_Content3_p1_0505 extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_p1_0505 fragment = null;


    public static List4_Content3_p1_0505 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_p1_0505();
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
        return R.layout.list4_content3_p2_0505;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            List list = new ArrayList();

            if (QueryMeasBoardType(mCompName).equals("2")) {
                switch (QueryMeasCateg(MainActivity.mCompName)) {
                    case "1":
                    case "2":
                    case "7":
                        list.add(2);// 测量单元
                        list.add(3);// 泵阀组件
                        list.add(5);// 计量板设置
                        list.add(9);// 计量参数C
                        break;
                    default:
                        break;
                }
            } else {
                switch (QueryMeasCateg(MainActivity.mCompName)) {
                    case "4":
                    case "7":
                        list.add(2);// 测量单元
                        list.add(5);// 计量板设置
                        list.add(6);// 计量板参数
                        list.add(3);// 泵阀组件
                        break;
                    case "3":
                        list.add(1);// 计量单元
                        list.add(2);// 测量单元
                        list.add(5);// 计量板设置
                        list.add(6);// 计量板参数
                        list.add(3);// 泵阀组件
                        list.add(4);// 计量参数
                        list.add(7);// 滴定温控参数
                        list.add(8);// 滴定参数
                        break;
                    case "8":
                        list.add(2);// 测量单元
                        list.add(5);// 计量板设置
                        list.add(6);// 计量板参数
                        list.add(3);// 泵阀组件
                        list.add(8);// 滴定参数
                        break;
                    case "9":
                        //list.add(1);// 计量单元
                        list.add(2);// 测量单元
                        list.add(3);// 泵阀组件
                        list.add(10);// 注射泵参数
                        //list.add(4);// 计量参数
                        break;
                    case "11":
                        list.add(2);// 测量单元
                        list.add(1);//计量单元
                        list.add(3);// 泵阀组件
                        break;
                    case "12"://
                        list.add(2);// 测量单元
                        list.add(3);// 泵阀组件
                        list.add(11);// 计量参数B
                        break;
						case "13":
                        list.add(2);// 测量单元
                        list.add(12);//多轴机器人
                        list.add(3);// 泵阀组件
                        list.add(13);//注射泵设置
                        break;
                    default:
                        list.add(1);// 计量单元
                        list.add(2);// 测量单元
                        list.add(3);// 泵阀组件
                        list.add(4);// 计量参数
                        break;
                }
            }
            get0505ShowButton(list);
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

    private void get0505ShowButton(List list) {

        GridLayout mGL = (GridLayout) v.findViewById(R.id.gl_0505);
        mGL.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            try {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.ft_linelayout, null);
                Button btn = (Button) inflater.inflate(R.layout.ft_0505button, null);

                switch ((Integer) list.get(i)) {
                    case 1://常规计量单元
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_jldy));
                        btn.setId(R.id.btnJLDY);
                        break;
                    case 2:// 测量单元
                        switch (QueryMeasCateg(MainActivity.mCompName)) {
                            case "3":
                            case "8":
                                btn.setBackground(getResources().getDrawable(R.drawable.btn_ddcldy));
                                break;
                            case "4":
                                btn.setBackground(getResources().getDrawable(R.drawable.btn_voccldy));
                                break;
                            case "2":
                            case "7":
                                btn.setBackground(getResources().getDrawable(R.drawable.btn_gpcldy));
                                break;
                            default:
                                btn.setBackground(getResources().getDrawable(R.drawable.btn_cldy));
                                break;
                        }
                        btn.setId(R.id.btnCLDY);
                        break;
                    case 3:// 常规泵阀组件
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_bfzj));
                        btn.setId(R.id.btnBFZJ);
                        break;
                    case 4:// 常规计量参数
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_jlcs));
                        btn.setId(R.id.btnJLCS);
                        break;
                    case 5:// 滴定计量板设置(计量单元B)
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_jldyb));
                        btn.setId(R.id.btnJLBSZ);
                        break;
                    case 6:// 滴定计量板参数
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_jlcsb));
                        btn.setId(R.id.btnJLBCS);
                        break;
                    case 7:// 滴定温控参数
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_temp_ctl));
                        btn.setId(R.id.btnWKCS);
                        break;
                    case 8: // 滴定参数
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_titration_param));
                        btn.setId(R.id.btnDDCS);
                        break;
                    case 9: // W200 计量参数C
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_jlcsb));
                        btn.setId(R.id.btnJLBCS_C);
                        break;
                    case 10: // 注射泵参数
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_zsbcs));
                        btn.setId(R.id.btnZSBCS);
                        break;
                    case 11: // 计量参数B
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_jlcsb));
                        btn.setId(R.id.btnJLCS);
                        break;
					case 12: // 多轴机器人
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_robot));
                        btn.setId(R.id.btnROBOT);
                        break;
                    case 13: // 注射泵设置
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_zsbcs));
                        btn.setId(R.id.btnZSBSZ);
                        break;
                }
                layout.addView(btn);
                mGL.addView(layout);
                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) layout.getLayoutParams();
                layoutParams.setMargins(5, 5, 10, 5);
                layout.setLayoutParams(layoutParams);
                btn.setOnClickListener(new List4_Content3_p1_0505.btnClick());

            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }
}
