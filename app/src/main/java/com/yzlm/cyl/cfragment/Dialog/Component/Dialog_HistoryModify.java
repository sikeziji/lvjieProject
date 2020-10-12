package com.yzlm.cyl.cfragment.Dialog.Component;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.R;

/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_HistoryModify extends ZDialogFragment {

    private EditText mEtHisYear;
    private EditText mEtHisMonth;
    private EditText mEtHisDay;
    private EditText mEtHisHour;
    private EditText mEtHisMin;
    private EditText mEtHisSec;
    private EditText mEtHisData;
    private EditText mEtHisDataType;
    private String sID;
    private String compName;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_historymodify;
    }

    @Override
    protected void InitUI(View view) {

        String viewSource = getPrompt("alert-historyModify");

        mEtHisYear = (EditText) view.findViewById(R.id.etHisYear);
        mEtHisMonth = (EditText) view.findViewById(R.id.etHisMonth);
        mEtHisDay = (EditText) view.findViewById(R.id.etHisDay);
        mEtHisHour = (EditText) view.findViewById(R.id.etHisHour);
        mEtHisMin = (EditText) view.findViewById(R.id.etHisMin);
        mEtHisSec = (EditText) view.findViewById(R.id.etHisSec);

        mEtHisData = (EditText) view.findViewById(R.id.etHisData);
        mEtHisDataType = (EditText) view.findViewById(R.id.etHisDateType);

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_Cancel);
        mBtnD_Cancel.setOnClickListener(new BtnCancelClick());

        try {
            String[] str = viewSource.split("_");
            sID = str[0];

            mEtHisYear.setText(str[1]);
            mEtHisMonth.setText(str[2]);
            mEtHisDay.setText(str[3]);

            mEtHisHour.setText(str[4]);
            mEtHisMin.setText(str[5]);
            mEtHisSec.setText(str[6]);

            compName = str[7];

            mEtHisData.setText(str[8]);
            mEtHisDataType.setText(str[9]);
        } catch (Exception e) {
        }

    }

    @Override
    protected void dialogCancel() {

    }

    public interface Callbacks {
        void onDialogRS();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    protected boolean setCanceledOnTouchOutside() {
        return true;
    }

    @Override
    protected String getTitleName() {
        return null;
    }

    @Override
    protected String getPositiveButtonName() {
        return null;
    }

    @Override
    protected String getNegativeButtonName() {
        return null;
    }

    @Override
    protected void PositiveButtonListener() {

    }

    @Override
    protected void NegativeButtonListener() {

    }

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String year = mEtHisYear.getText().toString();
            String month = mEtHisMonth.getText().toString();
            String day = mEtHisDay.getText().toString();
            String hour = mEtHisHour.getText().toString();
            String min = mEtHisMin.getText().toString();
            String sec = mEtHisSec.getText().toString();
            String value = mEtHisData.getText().toString();
            String type = mEtHisDataType.getText().toString();
            if (year.equals("") || month.equals("") || day.equals("") ||
                    hour.equals("") || min.equals("") || sec.equals("") ||
                    value.equals("") || type.equals("")) {
                Toast.makeText(getContext(), "请检查参数!", Toast.LENGTH_SHORT).show();
                return;
            }
            if ((Integer.parseInt(year) > 2000 && Integer.parseInt(year) < 2200) &&
                    (Integer.parseInt(month) > 0 && Integer.parseInt(month) < 13) &&
                    (Integer.parseInt(day) > 0 && Integer.parseInt(day) < 32) &&
                    (Integer.parseInt(hour) >= 0 && Integer.parseInt(hour) < 24) &&
                    (Integer.parseInt(min) >= 0 && Integer.parseInt(min) < 60) &&
                    (Integer.parseInt(sec) >= 0 && Integer.parseInt(sec) < 60)) {
                if (type.equals("实时") || type.equals("测量")) {
                    String temp = year + "-" + ((month.length() == 1) ? ("0" + month) : month) + "-" + (day.length() == 1 ? ("0" + day) : day) + " " +
                            (hour.length() == 1 ? ("0" + hour) : hour) + ":" + (min.length() == 1 ? ("0" + min) : min) + ":" + (sec.length() == 1 ? ("0" + sec) : sec);
                    setData(1, "historyModify_", sID + "_" + temp + "_" + compName + "_" + value + "_" + type);
                } else {
                    Toast.makeText(getContext(), "请检查参数!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "请检查参数!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();

        }
    }

    private class BtnCancelClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();

        }
    }
}
