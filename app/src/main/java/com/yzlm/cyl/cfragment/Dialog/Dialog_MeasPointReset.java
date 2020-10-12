package com.yzlm.cyl.cfragment.Dialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;

/**
 * Created by WL on 2017/3/29. sun created
 */

public class Dialog_MeasPointReset extends ZDialogFragment {
    private EditText mD_text1;
    private EditText mD_text2;
    private EditText mD_PointVoltage;
    private EditText mD_PointLampTimes;
    private TextView mTvPromptInfo;


    @Override
    protected int getDialogView() {
        return R.layout.dialog_measpoint_reset;
    }

    @Override
    protected void InitUI(View view) {
        String[] wave = getPrompt("reset-wave").split("[- ]");//波长
        String wave2 = "";
        String wave1 = "";
        if (wave.length > 2){
            wave1 = wave[0];
            wave2 = wave[1];
        }else{
            wave1 = wave2 = wave[0];
        }

        String voltage = getPrompt("reset-voltage");//电压
        String time = getPrompt("reset-time");//次数

        mD_text1 = (EditText) view.findViewById(R.id.edit_measpoint1);
        mD_text1.setText(wave1);
        mD_text2 = (EditText) view.findViewById(R.id.edit_measpoint2);
        mD_text2.setText(wave2);
        mD_PointVoltage = (EditText) view.findViewById(R.id.edit_pointVoltage);
        mD_PointVoltage.setText(voltage);
        mD_PointLampTimes = (EditText) view.findViewById(R.id.edit_pointLampTimes);
        mD_PointLampTimes.setText(time);

        mTvPromptInfo = (TextView) view.findViewById(R.id.tvPromptInfo);

        Button mBtnD_OK = (Button) view.findViewById(R.id.btn_OK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Close = (Button) view.findViewById(R.id.btnClose);
        mBtnD_Close.setOnClickListener(new BtnOKClick());
    }

    @Override
    protected boolean setCanceledOnTouchOutside() {
        return false;
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

    @Override
    protected void dialogCancel() {
    }

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_OK:
                    mTvPromptInfo.setText("");

                    // 打灯电压及次数
                    String measPointVoltage = mD_PointVoltage.getText().toString().trim();
                    String measPointLampTimes = mD_PointLampTimes.getText().toString().trim();
                    int iMPointVol = 0;
                    int iMPointLampTimes = 0;
                    try {
                        iMPointVol = Integer.parseInt(measPointVoltage);
                        iMPointLampTimes = Integer.parseInt(measPointLampTimes);
                    } catch (NumberFormatException e) {
                        mTvPromptInfo.setText(getString(R.string.the_measurement_pointsPar_set_failed));
                    }
                    if (!measPointVoltage.equals("")) {
                        // 范围合适
                        if (((iMPointVol < 20 && (iMPointVol != 0)) || iMPointVol > 50)) {
                            mTvPromptInfo.setText(getString(R.string.set_measurement_pointsVol_beyond_range));
                            return;
                        }
                    } else {
                        mTvPromptInfo.setText(getString(R.string.the_points_Par_is_empty));
                        return;
                    }
                    if (!measPointLampTimes.equals("")) {
                        // 范围合适
                        if ((iMPointLampTimes < 0 || iMPointLampTimes > 50)) {
                            mTvPromptInfo.setText(getString(R.string.set_measurement_pointsLampTimes_beyond_range));
                            return;
                        }
                    } else {
                        mTvPromptInfo.setText(getString(R.string.the_points_Par_is_empty));
                        return;
                    }
                    // 测量点
                    String measPoint1 = mD_text1.getText().toString().trim();
                    String measPoint2 = mD_text2.getText().toString().trim();

                    int point1 = 0;
                    int point2 = 0;
                    try {
                        point1 = Integer.parseInt(measPoint1);
                        point2 = Integer.parseInt(measPoint2);
                    } catch (NumberFormatException e) {
                        mTvPromptInfo.setText(getString(R.string.the_measurement_points_set_failed));
                    }

                    if ((point1 < 200 || point1 > 800) || (point2 < 200 || point2 > 800)) {
                        mTvPromptInfo.setText(getString(R.string.set_measurement_points_beyond_range));
                    } else {
                        if (!measPoint1.equals("") && !measPoint2.equals("")) {
                            if (measPoint1.equals(measPoint2)) {
                                setData(3, "measPointData", "shrink_"+getTargetRequestCode()+"," + measPoint1 + " nm,null" + "," + measPointVoltage + "," + measPointLampTimes);
                                dialog.dismiss();
                            } else {
                                if (point1 > point2) {
                                    mTvPromptInfo.setText(getString(R.string.first_points_value_set_failed));
                                } else {
                                    setData(3, "measPointData", "shrink_"+getTargetRequestCode()+"," + measPoint1 + "-" + measPoint2 + " nm,null" + "," + measPointVoltage + "," + measPointLampTimes);
                                    dialog.dismiss();
                                }
                            }
                        } else {
                            mTvPromptInfo.setText(getString(R.string.the_points_is_empty));
                        }
                    }
                    break;
                case R.id.btnClose:
                    dialog.dismiss();
                    break;
            }
        }
    }
}
