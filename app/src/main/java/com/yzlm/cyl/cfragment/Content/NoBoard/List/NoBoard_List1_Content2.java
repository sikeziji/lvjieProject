package com.yzlm.cyl.cfragment.Content.NoBoard.List;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalc;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalcTable;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardKbf;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.GetNoBoardPlatRangSum;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getNoBoardCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/25
 */
public class NoBoard_List1_Content2 extends SubFragment {

    private static NoBoard_List1_Content2 fragment = null;
    private TextView TVRange1K;
    private TextView TVRange1B;
    private TextView TVRange2K;
    private TextView TVRange2B;
    private TextView TVRange3K;
    private TextView TVRange3B;


    private String str1 = context.getResources().getString(R.string.working_curve_info);

    public static NoBoard_List1_Content2 newInstance() {
        if (fragment == null) {
            fragment = new NoBoard_List1_Content2();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.no_board_list1_content2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            NBCalcTable ct = getNoBoardCalc(mNoBoardCompName);
            NoBoardKbf mKbf = new NoBoardKbf(context);

            /*值显示的小数位数*/
            int roundNum = 2;
            try {
                roundNum = Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS"));
            } catch (Exception e) {
                roundNum = 2;
            }

            LinearLayout layout_kbf1 = (LinearLayout) v.findViewById(R.id.layout_KBF_R1);
            LinearLayout layout_kbf2 = (LinearLayout) v.findViewById(R.id.layout_KBF_R2);
            LinearLayout layout_kbf3 = (LinearLayout) v.findViewById(R.id.layout_KBF_R3);

            layout_kbf1.setVisibility(View.INVISIBLE);
            layout_kbf2.setVisibility(View.INVISIBLE);
            layout_kbf3.setVisibility(View.INVISIBLE);

            try {
                NBCalc calc;
                List<Map> list;

                switch (GetNoBoardPlatRangSum(mNoBoardCompName)) {
                    case 3:
                        layout_kbf3.setVisibility(View.VISIBLE);
                        calc = ct.getCalc("3");
                        list = mKbf.select(mNoBoardCompName, "3", null, null, 0, 1);
                        TVRange3K = (TextView) v.findViewById(R.id.tvRange3k);
                        TVRange3B = (TextView) v.findViewById(R.id.tvRange3b);
                        TextView TVRange3F = (TextView) v.findViewById(R.id.tvRange3f);

                        if (list.size() > 0) {
                            TVRange3K.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("K").toString())));
                            TVRange3B.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("B").toString())));
                            TVRange3F.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("F").toString())));

                        } else {
                            TVRange3K.setText(String.format("%." + roundNum + "f", calc.getK("3", mNoBoardCompName)));
                            TVRange3B.setText(String.format("%." + roundNum + "f", calc.getB("3", mNoBoardCompName)));
                            TVRange3F.setText(String.format("%." + roundNum + "f", calc.getF("3", mNoBoardCompName)));
                        }
                    case 2:
                        layout_kbf2.setVisibility(View.VISIBLE);
                        calc = ct.getCalc("2");
                        list = mKbf.select(mNoBoardCompName, "2", null, null, 0, 1);
                        TVRange2K = (TextView) v.findViewById(R.id.tvRange2k);
                        TVRange2B = (TextView) v.findViewById(R.id.tvRange2b);
                        TextView TVRange2F = (TextView) v.findViewById(R.id.tvRange2f);
                        if (list.size() > 0) {
                            TVRange2K.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("K").toString())));
                            TVRange2B.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("B").toString())));
                            TVRange2F.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("F").toString())));
                        } else {
                            TVRange2K.setText(String.format("%." + roundNum + "f", calc.getK("2", mNoBoardCompName)));
                            TVRange2B.setText(String.format("%." + roundNum + "f", calc.getB("2", mNoBoardCompName)));
                            TVRange2F.setText(String.format("%." + roundNum + "f", calc.getF("2", mNoBoardCompName)));
                        }
                    case 1:
                        layout_kbf1.setVisibility(View.VISIBLE);
                        calc = ct.getCalc("1");
                        list = mKbf.select(mNoBoardCompName, "1", null, null, 0, 1);
                        TVRange1K = (TextView) v.findViewById(R.id.tvRange1k);
                        TVRange1B = (TextView) v.findViewById(R.id.tvRange1b);
                        TextView TVRange1F = (TextView) v.findViewById(R.id.tvRange1f);
                        TextView TVRange1D = (TextView) v.findViewById(R.id.tvRange1d);
                        if (list.size() > 0) {
                            TVRange1K.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("K").toString())));
                            TVRange1B.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("B").toString())));
                            TVRange1F.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("F").toString())));
                        } else {
                            TVRange1K.setText(String.format("%." + roundNum + "f", calc.getK("1", mNoBoardCompName)));
                            TVRange1B.setText(String.format("%." + roundNum + "f", calc.getB("1", mNoBoardCompName)));
                            TVRange1F.setText(String.format("%." + roundNum + "f", calc.getF("1", mNoBoardCompName)));
                        }
                        break;
                    default:
                        TVRange1K.setText("");
                        TVRange2K.setText("");
                        TVRange3K.setText("");
                        TVRange1B.setText("");
                        TVRange2B.setText("");
                        TVRange3B.setText("");
                        break;
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Please check the calc files！", Toast.LENGTH_LONG).show();
            }

            TextView mTxtCalExplain = (TextView) v.findViewById(R.id.txtCalExplain);
            String strInfo = str1;
            mTxtCalExplain.setText(strInfo);
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mNoBoardCompName + "]" + e.toString());
        }
    }


}
