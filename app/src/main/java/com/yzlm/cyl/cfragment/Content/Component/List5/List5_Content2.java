package com.yzlm.cyl.cfragment.Content.Component.List5;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.KbfVal;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by WL on 2016/10/28.
 */
public class List5_Content2 extends SubFragment {
    private static List5_Content2 fragment = null;
    private Kbf mKbf;
    List<String> items = new ArrayList<>();
    private TextView TVRange1K;
    private TextView TVRange1B;
    private TextView TVRange2K;
    private TextView TVRange2B;
    private TextView TVRange3K;
    private TextView TVRange3B;

    private static EditText mEtRange1K;
    private static EditText mEtRange1B;
    private static EditText mEtRange1F;
    private static EditText mEtRange2K;
    private static EditText mEtRange2B;
    private static EditText mEtRange2F;
    private static EditText mEtRange3K;
    private static EditText mEtRange3B;
    private static EditText mEtRange3F;
    private String str1 = context.getResources().getString(R.string.working_curve_info);
    private String str2 = context.getResources().getString(R.string.titration_working_curve_info);

    public static List5_Content2 newInstance() {
        if (fragment == null) {
            fragment = new List5_Content2();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list5_content2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            CalcTable ct = getCalc(mCompName);
            mKbf = new Kbf(context);

            /*值显示的小数位数*/
            int roundNum = 2;
            try {
                roundNum = Integer.parseInt(getConfigData(mCompName, "YXWS"));
            } catch (Exception e) {
                roundNum = 2;
            }

            LinearLayout layout_kbf1 = (LinearLayout) v.findViewById(R.id.layout_KBF_R1);
            LinearLayout layout_kbf2 = (LinearLayout) v.findViewById(R.id.layout_KBF_R2);
            LinearLayout layout_kbf3 = (LinearLayout) v.findViewById(R.id.layout_KBF_R3);

            LinearLayout layout_d1 = (LinearLayout) v.findViewById(R.id.layout_d1);
            LinearLayout layout_d2 = (LinearLayout) v.findViewById(R.id.layout_d2);
            LinearLayout layout_d3 = (LinearLayout) v.findViewById(R.id.layout_d3);
            if (isHaveMeasCategory(mCompName, "5")) {
                layout_d1.setVisibility(View.VISIBLE);
                layout_d2.setVisibility(View.VISIBLE);
                layout_d3.setVisibility(View.VISIBLE);
            } else {
                layout_d1.setVisibility(View.INVISIBLE);
                layout_d2.setVisibility(View.INVISIBLE);
                layout_d3.setVisibility(View.INVISIBLE);
            }
            layout_kbf1.setVisibility(View.INVISIBLE);
            layout_kbf2.setVisibility(View.INVISIBLE);
            layout_kbf3.setVisibility(View.INVISIBLE);

            try {
                Calc calc;
                List<Map> list;

                switch (GetPlatRangSum(mCompName)) {
                    case 3:
                        layout_kbf3.setVisibility(View.VISIBLE);
                        calc = ct.getCalc("3");
                        list = mKbf.select(mCompName, "3", null, null, 0, 1);
                        TVRange3K = (TextView) v.findViewById(R.id.tvRange3k);
                        TVRange3B = (TextView) v.findViewById(R.id.tvRange3b);
                        TextView TVRange3F = (TextView) v.findViewById(R.id.tvRange3f);
                        TextView TVRange3D = (TextView) v.findViewById(R.id.tvRange3d);
                        if (list.size() > 0) {
                            TVRange3K.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("K").toString())));
                            TVRange3B.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("B").toString())));
                            TVRange3F.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("F").toString())));

                        } else {
                            TVRange3K.setText(String.format("%." + roundNum + "f", calc.getK("3", mCompName)));
                            TVRange3B.setText(String.format("%." + roundNum + "f", calc.getB("3", mCompName)));
                            TVRange3F.setText(String.format("%." + roundNum + "f", calc.getF("3", mCompName)));
                        }
                        try {
                            if (isHaveMeasCategory(mCompName, "5")) {
                                TVRange3D.setText(String.format("%." + roundNum + "f", (getNewestKBF(mCompName + getModuleName("5"), getConfigData(mCompName + getModuleName("5"), "XS_MODE"), "K"))));
                            }
                        } catch (Exception e) {
                        }
                    case 2:
                        layout_kbf2.setVisibility(View.VISIBLE);
                        calc = ct.getCalc("2");
                        list = mKbf.select(mCompName, "2", null, null, 0, 1);
                        TVRange2K = (TextView) v.findViewById(R.id.tvRange2k);
                        TVRange2B = (TextView) v.findViewById(R.id.tvRange2b);
                        TextView TVRange2F = (TextView) v.findViewById(R.id.tvRange2f);
                        TextView TVRange2D = (TextView) v.findViewById(R.id.tvRange2d);
                        if (list.size() > 0) {
                            TVRange2K.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("K").toString())));
                            TVRange2B.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("B").toString())));
                            TVRange2F.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("F").toString())));
                        } else {
                            TVRange2K.setText(String.format("%." + roundNum + "f", calc.getK("2", mCompName)));
                            TVRange2B.setText(String.format("%." + roundNum + "f", calc.getB("2", mCompName)));
                            TVRange2F.setText(String.format("%." + roundNum + "f", calc.getF("2", mCompName)));
                        }
                        try {
                            if (isHaveMeasCategory(mCompName, "5")) {
                                TVRange2D.setText(String.format("%." + roundNum + "f", (getNewestKBF(mCompName + getModuleName("5"), "2", "K"))));
                            }
                        } catch (Exception e) {
                        }
                    case 1:
                        layout_kbf1.setVisibility(View.VISIBLE);
                        calc = ct.getCalc("1");
                        list = mKbf.select(mCompName, "1", null, null, 0, 1);
                        TVRange1K = (TextView) v.findViewById(R.id.tvRange1k);
                        TVRange1B = (TextView) v.findViewById(R.id.tvRange1b);
                        TextView TVRange1F = (TextView) v.findViewById(R.id.tvRange1f);
                        TextView TVRange1D = (TextView) v.findViewById(R.id.tvRange1d);
                        if (list.size() > 0) {
                            TVRange1K.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("K").toString())));
                            TVRange1B.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("B").toString())));
                            TVRange1F.setText(String.format("%." + roundNum + "f", Double.parseDouble(list.get(0).get("F").toString())));
                        } else {
                            TVRange1K.setText(String.format("%." + roundNum + "f", calc.getK("1", mCompName)));
                            TVRange1B.setText(String.format("%." + roundNum + "f", calc.getB("1", mCompName)));
                            TVRange1F.setText(String.format("%." + roundNum + "f", calc.getF("1", mCompName)));
                        }
                        try {
                            if (isHaveMeasCategory(mCompName, "5")) {
                                TVRange1D.setText(String.format("%." + roundNum + "f", (getNewestKBF(mCompName + getModuleName("5"), "1", "K"))));
                            }
                        } catch (Exception e) {
                            Log.e("exception", e.toString());
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

            Button mBtnkbf = (Button) v.findViewById(R.id.btn_KBFOK);
            mBtnkbf.setOnClickListener(new BtnOnClick());
            mEtRange1K = (EditText) v.findViewById(R.id.eTRange1k);
            mEtRange1B = (EditText) v.findViewById(R.id.eTRange1b);
            mEtRange1F = (EditText) v.findViewById(R.id.eTRange1f);
            mEtRange2K = (EditText) v.findViewById(R.id.eTRange2k);
            mEtRange2B = (EditText) v.findViewById(R.id.eTRange2b);
            mEtRange2F = (EditText) v.findViewById(R.id.eTRange2f);
            mEtRange3K = (EditText) v.findViewById(R.id.eTRange3k);
            mEtRange3B = (EditText) v.findViewById(R.id.eTRange3b);
            mEtRange3F = (EditText) v.findViewById(R.id.eTRange3f);

            TextView mtxtCalExplain = (TextView) v.findViewById(R.id.txtCalExplain);
            String strInfo = str1;
            if (QueryMeasCateg(mCompName).equals("3") || QueryMeasCateg(mCompName).equals("8")) {
                strInfo = str2;
            }
            mtxtCalExplain.setText(strInfo);
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private class BtnOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                String rang1k = mEtRange1K.getText().toString();
                String rang1b = mEtRange1B.getText().toString();
                String rang1f = mEtRange1F.getText().toString();

                String rang2k = mEtRange2K.getText().toString();
                String rang2b = mEtRange2B.getText().toString();
                String rang2f = mEtRange2F.getText().toString();

                String rang3k = mEtRange3K.getText().toString();
                String rang3b = mEtRange3B.getText().toString();
                String rang3f = mEtRange3F.getText().toString();

                if (!rang1k.equals("") && !rang1b.equals("") && !rang1f.equals("") && !rang2k.equals("") && !rang2b.equals("") && !rang2f.equals("")
                        && !rang3k.equals("") && !rang3b.equals("") && !rang3f.equals("")) {
                    Calendar cal = Calendar.getInstance();
                    double range1kd = Double.parseDouble(rang1k);
                    double range1bd = Double.parseDouble(rang1b);
                    double range1fd = Double.parseDouble(rang1f);

                    KbfVal kbfVal = new KbfVal();
                    kbfVal.setYear((short) cal.get(Calendar.YEAR));
                    kbfVal.setMonth((byte) (cal.get(Calendar.MONTH) + 1));
                    kbfVal.setDay((byte) cal.get(Calendar.DATE));
                    kbfVal.setHour((byte) cal.get(Calendar.HOUR_OF_DAY));
                    kbfVal.setMinute((byte) cal.get(Calendar.MINUTE));
                    kbfVal.setSecond((byte) cal.get(Calendar.SECOND));
                    kbfVal.setComponent(mCompName);
                    kbfVal.setRange("1");
                    kbfVal.setK(range1kd);
                    kbfVal.setB(range1bd);
                    kbfVal.setF(range1fd);
                    mKbf.Add(kbfVal);

                    double range2kd = Double.parseDouble(rang2k);
                    double range2bd = Double.parseDouble(rang2b);
                    double range2fd = Double.parseDouble(rang2f);

                    kbfVal = new KbfVal();
                    kbfVal.setYear((short) cal.get(Calendar.YEAR));
                    kbfVal.setMonth((byte) (cal.get(Calendar.MONTH) + 1));
                    kbfVal.setDay((byte) cal.get(Calendar.DATE));
                    kbfVal.setHour((byte) cal.get(Calendar.HOUR_OF_DAY));
                    kbfVal.setMinute((byte) cal.get(Calendar.MINUTE));
                    kbfVal.setSecond((byte) cal.get(Calendar.SECOND));
                    kbfVal.setComponent(mCompName);
                    kbfVal.setRange("2");
                    kbfVal.setK(range2kd);
                    kbfVal.setB(range2bd);
                    kbfVal.setF(range2fd);
                    mKbf.Add(kbfVal);

                    double range3kd = Double.parseDouble(rang3k);
                    double range3bd = Double.parseDouble(rang3b);
                    double range3fd = Double.parseDouble(rang3f);

                    kbfVal = new KbfVal();
                    kbfVal.setYear((short) cal.get(Calendar.YEAR));
                    kbfVal.setMonth((byte) (cal.get(Calendar.MONTH) + 1));
                    kbfVal.setDay((byte) cal.get(Calendar.DATE));
                    kbfVal.setHour((byte) cal.get(Calendar.HOUR_OF_DAY));
                    kbfVal.setMinute((byte) cal.get(Calendar.MINUTE));
                    kbfVal.setSecond((byte) cal.get(Calendar.SECOND));
                    kbfVal.setComponent(mCompName);
                    kbfVal.setRange("3");
                    kbfVal.setK(range3kd);
                    kbfVal.setB(range3bd);
                    kbfVal.setF(range3fd);
                    mKbf.Add(kbfVal);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


}
