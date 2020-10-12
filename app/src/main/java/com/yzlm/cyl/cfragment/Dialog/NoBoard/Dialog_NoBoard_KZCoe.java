package com.yzlm.cyl.cfragment.Dialog.NoBoard;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Content.NoBoard.NoBoardContentTool.GetNoBoardPlatRangSum;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mNoBoardCompName;


/*
 * Created by zwj 20180713
 * 浊度系数KZ1 KZ2
 */

public class Dialog_NoBoard_KZCoe extends ZDialogFragment {

    private EditText mEditRange1KZ1;
    private EditText mEditRange1KZ2;
    private EditText mEditRange1KZ3;
    private EditText mEditRange2KZ1;
    private EditText mEditRange2KZ2;
    private EditText mEditRange2KZ3;
    private EditText mEditRange3KZ1;
    private EditText mEditRange3KZ2;
    private EditText mEditRange3KZ3;


    private static String componentName = "";
    private View v;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_no_board_kz_coe;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void InitUI(View view) {
        componentName = mNoBoardCompName;
        v = view;

        mEditRange1KZ1 = (EditText) view.findViewById(R.id.edit_range1KZ1);
        mEditRange1KZ1.setText(getNoBoardConfigData(componentName, "KZ1Range1"));
        mEditRange1KZ1.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange1KZ1.setOnEditorActionListener(new editTextFocusChange());

        mEditRange1KZ2 = (EditText) view.findViewById(R.id.edit_range1KZ2);
        mEditRange1KZ2.setText(getNoBoardConfigData(componentName, "KZ2Range1"));
        mEditRange1KZ2.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange1KZ2.setOnEditorActionListener(new editTextFocusChange());

        mEditRange1KZ3 = (EditText) view.findViewById(R.id.edit_range1KZ3);
        mEditRange1KZ3.setText(getNoBoardConfigData(componentName, "KZ3Range1"));
        mEditRange1KZ3.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange1KZ3.setOnEditorActionListener(new editTextFocusChange());


        mEditRange2KZ1 = (EditText) view.findViewById(R.id.edit_range2KZ1);
        mEditRange2KZ1.setText(getNoBoardConfigData(componentName, "KZ1Range2"));
        mEditRange2KZ1.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange2KZ1.setOnEditorActionListener(new editTextFocusChange());

        mEditRange2KZ2 = (EditText) view.findViewById(R.id.edit_range2KZ2);
        mEditRange2KZ2.setText(getNoBoardConfigData(componentName, "KZ2Range2"));
        mEditRange2KZ2.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange2KZ2.setOnEditorActionListener(new editTextFocusChange());

        mEditRange2KZ3 = (EditText) view.findViewById(R.id.edit_range2KZ3);
        mEditRange2KZ3.setText(getNoBoardConfigData(componentName, "KZ3Range2"));
        mEditRange2KZ3.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange2KZ3.setOnEditorActionListener(new editTextFocusChange());


        mEditRange3KZ1 = (EditText) view.findViewById(R.id.edit_range3KZ1);
        mEditRange3KZ1.setText(getNoBoardConfigData(componentName, "KZ1Range3"));
        mEditRange3KZ1.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange3KZ1.setOnEditorActionListener(new editTextFocusChange());

        mEditRange3KZ2 = (EditText) view.findViewById(R.id.edit_range3KZ2);
        mEditRange3KZ2.setText(getNoBoardConfigData(componentName, "KZ2Range3"));
        mEditRange3KZ2.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange3KZ2.setOnEditorActionListener(new editTextFocusChange());

        mEditRange3KZ3 = (EditText) view.findViewById(R.id.edit_range3KZ3);
        mEditRange3KZ3.setText(getNoBoardConfigData(componentName, "KZ3Range3"));
        mEditRange3KZ3.setOnFocusChangeListener(new editTextFocusChange());
        mEditRange3KZ3.setOnEditorActionListener(new editTextFocusChange());


        Button btn_close = (Button) view.findViewById(R.id.btnClose);
        btn_close.setOnClickListener(new btnCloseClick());

        LinearLayout mLayoutRange1Kz3 = (LinearLayout) v.findViewById(R.id.layout_Kz3Range1);
        LinearLayout mLayoutRange2Kz3 = (LinearLayout) v.findViewById(R.id.layout_Kz3Range2);
        LinearLayout mLayoutRange3Kz3 = (LinearLayout) v.findViewById(R.id.layout_Kz3Range3);


        LinearLayout mLayoutRange1Kz = (LinearLayout) v.findViewById(R.id.layout_KzRange1);
        LinearLayout mLayoutRange2Kz = (LinearLayout) v.findViewById(R.id.layout_KzRange2);
        LinearLayout mLayoutRange3Kz = (LinearLayout) v.findViewById(R.id.layout_KzRange3);
        switch (GetNoBoardPlatRangSum(mNoBoardCompName)) {
            case 3:
                mLayoutRange3Kz.setVisibility(View.VISIBLE);
            case 2:
                mLayoutRange2Kz.setVisibility(View.VISIBLE);
            case 1:
                mLayoutRange1Kz.setVisibility(View.VISIBLE);
                break;
        }
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

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void syncList() {

        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 2);
            v.clearFocus();
        }
    }


    private boolean setTextUnEquleZero(String strValue) {

        try {
            if (!strValue.equals("")) {
                double val1 = Double.parseDouble(strValue);
                if (val1 == 0) {
                    Toast.makeText(getActivity(), getString(R.string.setting_par_zero_unallowed), Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), getString(R.string.set_value_is_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*
     * 设置浊度系数KZ
     *  设置值， 系数几（1/2），量程(1,2,3)
     * **/
    private boolean setTextKzCoe(String strkz, int kzNum, int range) {
        try {
            if (!strkz.equals("")) {
                double val1 = Double.parseDouble(strkz);
                if (val1 < -100 || val1 > 100) {
                    Toast.makeText(getActivity(), getString(R.string.please_confirm_the_range_input_par) + "(-100~100)", Toast.LENGTH_SHORT).show();
                    return false;
                }
                strkz = editDataFormat(strkz, Integer.parseInt(getNoBoardConfigData(mNoBoardCompName, "YXWS")));

                saveNoBoardOperationLogDataModifyMsg(mNoBoardCompName, "KZ" + kzNum + "Range" + range, strkz, "KZ", ErrorLog.msgType.操作_信息);
                updateNoBoardConfigData(mNoBoardCompName, "KZ" + kzNum + "Range" + range, strkz);
                syncList();
            } else {
                Toast.makeText(getActivity(), getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), getString(R.string.set_value_is_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void dialogCancel() {

    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class editTextFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String strKz;
                switch (v.getId()) {
                    case R.id.edit_range1KZ1:
                        strKz = mEditRange1KZ1.getText().toString().trim();
                        setTextKzCoe(strKz, 1, 1);
                        mEditRange1KZ1.setText(getNoBoardConfigData(componentName, "KZ1Range1"));
                        break;
                    case R.id.edit_range1KZ2:
                        strKz = mEditRange1KZ2.getText().toString().trim();
                        setTextKzCoe(strKz, 2, 1);
                        mEditRange1KZ2.setText(getNoBoardConfigData(componentName, "KZ2Range1"));
                        break;
                    case R.id.edit_range1KZ3:
                        strKz = mEditRange1KZ3.getText().toString().trim();
                        setTextUnEquleZero(strKz);
                        setTextKzCoe(strKz, 3, 1);
                        mEditRange1KZ3.setText(getNoBoardConfigData(componentName, "KZ3Range1"));
                        break;
                    case R.id.edit_range2KZ1:
                        strKz = mEditRange2KZ1.getText().toString().trim();
                        setTextKzCoe(strKz, 1, 2);
                        mEditRange2KZ1.setText(getNoBoardConfigData(componentName, "KZ1Range2"));
                        break;
                    case R.id.edit_range2KZ2:
                        strKz = mEditRange2KZ2.getText().toString().trim();
                        setTextKzCoe(strKz, 2, 2);
                        mEditRange2KZ2.setText(getNoBoardConfigData(componentName, "KZ2Range2"));
                        break;
                    case R.id.edit_range2KZ3:
                        strKz = mEditRange2KZ3.getText().toString().trim();
                        setTextUnEquleZero(strKz);
                        setTextKzCoe(strKz, 3, 2);
                        mEditRange2KZ3.setText(getNoBoardConfigData(componentName, "KZ3Range2"));

                        break;
                    case R.id.edit_range3KZ1:
                        strKz = mEditRange3KZ1.getText().toString().trim();
                        setTextKzCoe(strKz, 1, 3);
                        mEditRange3KZ1.setText(getNoBoardConfigData(componentName, "KZ1Range3"));

                        break;
                    case R.id.edit_range3KZ2:
                        strKz = mEditRange3KZ2.getText().toString().trim();
                        setTextKzCoe(strKz, 2, 3);
                        mEditRange3KZ2.setText(getNoBoardConfigData(componentName, "KZ2Range3"));

                        break;
                    case R.id.edit_range3KZ3:
                        strKz = mEditRange3KZ3.getText().toString().trim();
                        setTextUnEquleZero(strKz);
                        setTextKzCoe(strKz, 3, 3);
                        mEditRange3KZ3.setText(getNoBoardConfigData(componentName, "KZ3Range3"));
                        break;
                }
                syncList();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return true;
            }
            return false;
        }
    }


    private class btnCloseClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();

        }
    }
}
