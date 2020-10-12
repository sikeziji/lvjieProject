package com.yzlm.cyl.cfragment.Dialog.Component;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.CDialogFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;


/*
 * Created by zwj 2020年5月14日10点38分
 * 外部量程KB2
 */

public class Dialog_ExternalKBCoe2 extends CDialogFragment {


    private EditText mEditExtRange1K;
    private EditText mEditExtRange2K;
    private EditText mEditExtRange3K;
    private EditText mEditExtRange1B;
    private EditText mEditExtRange2B;
    private EditText mEditExtRange3B;


    private static String componentName = "";
    private View v;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_extkb_coe2;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void InitUI(View view) {
        componentName = mCompName;
        v = view;

        mEditExtRange1K = (EditText) view.findViewById(R.id.edit_extRange1K);
        mEditExtRange1K.setText(getConfigData(componentName, "UserExtRange1K"));
        mEditExtRange1K.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange1K.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange2K = (EditText) view.findViewById(R.id.edit_extRange2K);
        mEditExtRange2K.setText(getConfigData(componentName, "UserExtRange2K"));
        mEditExtRange2K.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange2K.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange3K = (EditText) view.findViewById(R.id.edit_extRange3K);
        mEditExtRange3K.setText(getConfigData(componentName, "UserExtRange3K"));
        mEditExtRange3K.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange3K.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange1B = (EditText) view.findViewById(R.id.edit_extRange1B);
        mEditExtRange1B.setText(getConfigData(componentName, "UserExtRange1B"));
        mEditExtRange1B.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange1B.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange2B = (EditText) view.findViewById(R.id.edit_extRange2B);
        mEditExtRange2B.setText(getConfigData(componentName, "UserExtRange2B"));
        mEditExtRange2B.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange2B.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange3B = (EditText) view.findViewById(R.id.edit_extRange3B);
        mEditExtRange3B.setText(getConfigData(componentName, "UserExtRange3B"));
        mEditExtRange3B.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange3B.setOnEditorActionListener(new editTextFocusChange());


        Button btn_close = (Button) view.findViewById(R.id.btnClose);
        btn_close.setOnClickListener(new btnCloseClick());
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

    /*
     * 设置外部量程系数K
     *  设置值， 量程(1,2,3)
     * **/
    private boolean setTextExtKCoe(String strExtK, int range) {
        try {
            if (!strExtK.equals("")) {
                double val1 = Double.parseDouble(strExtK);
                if (val1 < 0.01 || val1 > 100) {
                    Toast.makeText(getActivity(), getString(R.string.please_confirm_the_range_input_par)+"[0.01,100]", Toast.LENGTH_SHORT).show();
                    return false;
                }
                strExtK = editDataFormat(strExtK, Integer.parseInt(getConfigData(componentName, "YXWS")));

                saveOperationLogDataModifyMsg(componentName, "UserExtRange" + range + "K", strExtK, "ExtK" + range, ErrorLog.msgType.操作_信息);
                updateConfigData(componentName, "UserExtRange" + range + "K", strExtK);
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

    /*
     * 设置外部量程系数B
     *  设置值， 量程(1,2,3)
     * **/
    private boolean setTextExtBCoe(String strExtB, int range) {
        try {
            if (!strExtB.equals("")) {
                double val1 = Double.parseDouble(strExtB);
                if (val1 < -100 || val1 > 100) {
                    Toast.makeText(getActivity(), getString(R.string.please_confirm_the_range_input_par)+"[-100,100]", Toast.LENGTH_SHORT).show();
                    return false;
                }
                strExtB = editDataFormat(strExtB, Integer.parseInt(getConfigData(componentName, "YXWS")));

                saveOperationLogDataModifyMsg(componentName, "UserExtRange" + range + "B", strExtB, "ExtB" + range, ErrorLog.msgType.操作_信息);
                updateConfigData(componentName, "UserExtRange" + range + "B", strExtB);
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


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class editTextFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String strKz;
                switch (v.getId()) {
                    case R.id.edit_extRange1K:
                        strKz = mEditExtRange1K.getText().toString().trim();
                        setTextExtKCoe(strKz, 1);
                        mEditExtRange1K.setText(getConfigData(componentName, "UserExtRange1K"));
                        break;
                    case R.id.edit_extRange2K:
                        strKz = mEditExtRange2K.getText().toString().trim();
                        setTextExtKCoe(strKz, 2);
                        mEditExtRange2K.setText(getConfigData(componentName, "UserExtRange2K"));
                        break;
                    case R.id.edit_extRange3K:
                        strKz = mEditExtRange3K.getText().toString().trim();
                        setTextExtKCoe(strKz, 3);
                        mEditExtRange3K.setText(getConfigData(componentName, "UserExtRange3K"));
                        break;
                    case R.id.edit_extRange1B:
                        strKz = mEditExtRange1B.getText().toString().trim();
                        setTextExtBCoe(strKz, 1);
                        mEditExtRange1B.setText(getConfigData(componentName, "UserExtRange1B"));
                        break;
                    case R.id.edit_extRange2B:
                        strKz = mEditExtRange2B.getText().toString().trim();
                        setTextExtBCoe(strKz, 2);
                        mEditExtRange2B.setText(getConfigData(componentName, "UserExtRange2B"));
                        break;
                    case R.id.edit_extRange3B:
                        strKz = mEditExtRange3B.getText().toString().trim();
                        setTextExtBCoe(strKz, 3);
                        mEditExtRange3B.setText(getConfigData(componentName, "UserExtRange3B"));
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

    /**
     * 重置所有外部KB
     */
    public static void resetUserExtRangeKb(String compName) {
        {
            for (int i = 1; i < 4; i++) {
                saveOperationLogDataModifyMsg(compName, "UserExtRange" + i + "K", "1", "UserExtK" + i, ErrorLog.msgType.操作_信息);
                updateConfigData(compName, "UserExtRange" + i + "K", "1");
                saveOperationLogDataModifyMsg(compName, "UserExtRange" + i + "B", "0", "UserExtB" + i, ErrorLog.msgType.操作_信息);
                updateConfigData(compName, "UserExtRange" + i + "B", "0");
            }
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
