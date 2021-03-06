package com.yzlm.cyl.cfragment.Dialog.Component;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.CDialogFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.doubleRound;


/*
 * Created by zwj 2018年10月24日09:26:24
 * 外部量程KB
 */

public class Dialog_ExternalKBCoe extends CDialogFragment {

    private EditText mEditC1;
    private EditText mEditC2;
    private EditText mEditC3;
    private EditText mEditC4;
    private EditText mEditC5;
    private EditText mEditC6;
    private EditText mEditC7;
    private EditText mEditC8;


    private EditText mEditExtRange1K;
    private EditText mEditExtRange2K;
    private EditText mEditExtRange3K;
    private EditText mEditExtRange1B;
    private EditText mEditExtRange2B;
    private EditText mEditExtRange3B;

    private TextView textKscope1;
    private TextView textKscope2;

    private static String componentName = "";
    private View v;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_extkb_coe;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void InitUI(View view) {
        componentName = mCompName;
        v = view;

        mEditExtRange1K = (EditText) view.findViewById(R.id.edit_extRange1K);
        mEditExtRange1K.setText(getConfigData(componentName, "ExtRange1K"));
        mEditExtRange1K.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange1K.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange2K = (EditText) view.findViewById(R.id.edit_extRange2K);
        mEditExtRange2K.setText(getConfigData(componentName, "ExtRange2K"));
        mEditExtRange2K.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange2K.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange3K = (EditText) view.findViewById(R.id.edit_extRange3K);
        mEditExtRange3K.setText(getConfigData(componentName, "ExtRange3K"));
        mEditExtRange3K.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange3K.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange1B = (EditText) view.findViewById(R.id.edit_extRange1B);
        mEditExtRange1B.setText(getConfigData(componentName, "ExtRange1B"));
        mEditExtRange1B.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange1B.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange2B = (EditText) view.findViewById(R.id.edit_extRange2B);
        mEditExtRange2B.setText(getConfigData(componentName, "ExtRange2B"));
        mEditExtRange2B.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange2B.setOnEditorActionListener(new editTextFocusChange());

        mEditExtRange3B = (EditText) view.findViewById(R.id.edit_extRange3B);
        mEditExtRange3B.setText(getConfigData(componentName, "ExtRange3B"));
        mEditExtRange3B.setOnFocusChangeListener(new editTextFocusChange());
        mEditExtRange3B.setOnEditorActionListener(new editTextFocusChange());

        mEditC1 = (EditText) v.findViewById(R.id.editExc1);
        mEditC1.setText(getConfigData(componentName, "ExtC1"));
        mEditC1.setOnFocusChangeListener(new editTextFocusChange());
        mEditC1.setOnEditorActionListener(new editTextFocusChange());

        mEditC2 = (EditText) v.findViewById(R.id.editExc2);
        mEditC2.setText(getConfigData(componentName, "ExtC2"));
        mEditC2.setOnFocusChangeListener(new editTextFocusChange());
        mEditC2.setOnEditorActionListener(new editTextFocusChange());

        mEditC3 = (EditText) v.findViewById(R.id.editExc3);
        mEditC3.setText(getConfigData(componentName, "ExtC3"));
        mEditC3.setOnFocusChangeListener(new editTextFocusChange());
        mEditC3.setOnEditorActionListener(new editTextFocusChange());

        mEditC4 = (EditText) v.findViewById(R.id.editExc4);
        mEditC4.setText(getConfigData(componentName, "ExtC4"));
        mEditC4.setOnFocusChangeListener(new editTextFocusChange());
        mEditC4.setOnEditorActionListener(new editTextFocusChange());

        mEditC5 = (EditText) v.findViewById(R.id.editExc5);
        mEditC5.setText(getConfigData(componentName, "ExtC5"));
        mEditC5.setOnFocusChangeListener(new editTextFocusChange());
        mEditC5.setOnEditorActionListener(new editTextFocusChange());

        mEditC6 = (EditText) v.findViewById(R.id.editExc6);
        mEditC6.setText(getConfigData(componentName, "ExtC6"));
        mEditC6.setOnFocusChangeListener(new editTextFocusChange());
        mEditC6.setOnEditorActionListener(new editTextFocusChange());

        mEditC7 = (EditText) v.findViewById(R.id.editExc7);
        mEditC7.setText(getConfigData(componentName, "ExtC7"));
        mEditC7.setOnFocusChangeListener(new editTextFocusChange());
        mEditC7.setOnEditorActionListener(new editTextFocusChange());

        mEditC8 = (EditText) v.findViewById(R.id.editExc8);
        mEditC8.setText(getConfigData(componentName, "ExtC8"));
        mEditC8.setOnFocusChangeListener(new editTextFocusChange());
        mEditC8.setOnEditorActionListener(new editTextFocusChange());

        textKscope1 = (TextView) view.findViewById(R.id.textKscope1);
        textKscope2 = (TextView) view.findViewById(R.id.textKscope2);

        getComponentScope(componentName);

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
    private boolean setTextExtKCoe(String strExtK, int range, String value) {
        try {
            if (!strExtK.equals("")) {
                double val1 = Double.parseDouble(strExtK);
                if (val1 < getParKDownLimit(componentName) || val1 > getParKUpLimit(componentName)) {
                    Toast.makeText(getActivity(), getString(R.string.please_confirm_the_range_input_par), Toast.LENGTH_SHORT).show();
                    return false;
                }
                strExtK = editDataFormat(strExtK, Integer.parseInt(getConfigData(mCompName, "YXWS")));

                saveOperationLogDataModifyMsg(mCompName, "ExtRange" + range + "K", strExtK, "ExtK" + range, ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "ExtRange" + range + "K", strExtK);
                switch (range) {
                    case 1:
                        CommandParsing.AddError(componentName, 641, String.valueOf(doubleRound(Double.valueOf(value), 3)), ErrorLog.msgType.运维_信息);
                        break;
                    case 2:
                        CommandParsing.AddError(componentName, 662, String.valueOf(doubleRound(Double.valueOf(value), 3)), ErrorLog.msgType.运维_信息);
                        break;
                    case 3:
                        CommandParsing.AddError(componentName, 664, String.valueOf(doubleRound(Double.valueOf(value), 3)), ErrorLog.msgType.运维_信息);
                        break;
                }
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
    private boolean setTextExtBCoe(String strExtB, int range, String value) {
        try {
            if (!strExtB.equals("")) {
                double val1 = Double.parseDouble(strExtB);
                if (val1 < getParDownLimit(componentName) || val1 > getParUpLimit(componentName)) {
                    Toast.makeText(getActivity(), getString(R.string.please_confirm_the_range_input_par), Toast.LENGTH_SHORT).show();
                    return false;
                }
                strExtB = editDataFormat(strExtB, Integer.parseInt(getConfigData(mCompName, "YXWS")));

                saveOperationLogDataModifyMsg(mCompName, "ExtRange" + range + "B", strExtB, "ExtB" + range, ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "ExtRange" + range + "B", strExtB);
                switch (range) {
                    case 1:
                        CommandParsing.AddError(componentName, 642, String.valueOf(doubleRound(Double.valueOf(value), 3)), ErrorLog.msgType.运维_信息);
                        break;
                    case 2:
                        CommandParsing.AddError(componentName, 663, String.valueOf(doubleRound(Double.valueOf(value), 3)), ErrorLog.msgType.运维_信息);
                        break;
                    case 3:
                        CommandParsing.AddError(componentName, 665, String.valueOf(doubleRound(Double.valueOf(value), 3)), ErrorLog.msgType.运维_信息);
                        break;
                }
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
     * 参数编辑框输入
     * **/
    private boolean setTextCPar(String strC, int parNum, String value) {
        try {
            if (!strC.equals("")) {
                double val1 = Double.parseDouble(strC);
                if (val1 <= -10000 || val1 >= 10000) {
                    Toast.makeText(getActivity(), getString(R.string.please_confirm_the_range_input_par) + "(-10000~10000)", Toast.LENGTH_SHORT).show();
                    return false;
                }
                strC = editDataFormat(strC, Integer.parseInt(getConfigData(mCompName, "YXWS")));

                saveOperationLogDataModifyMsg(mCompName, "ExtC" + parNum, strC, "ExtC" + parNum, ErrorLog.msgType.操作_信息);
                updateConfigData(mCompName, "ExtC" + parNum, strC);
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
                        setTextExtKCoe(strKz, 1, strKz);
                        mEditExtRange1K.setText(getConfigData(componentName, "ExtRange1K"));
                        break;
                    case R.id.edit_extRange2K:
                        strKz = mEditExtRange2K.getText().toString().trim();
                        setTextExtKCoe(strKz, 2, strKz);
                        mEditExtRange2K.setText(getConfigData(componentName, "ExtRange2K"));
                        break;
                    case R.id.edit_extRange3K:
                        strKz = mEditExtRange3K.getText().toString().trim();
                        setTextExtKCoe(strKz, 3, strKz);
                        mEditExtRange3K.setText(getConfigData(componentName, "ExtRange3K"));
                        break;
                    case R.id.edit_extRange1B:
                        strKz = mEditExtRange1B.getText().toString().trim();
                        setTextExtBCoe(strKz, 1, strKz);
                        mEditExtRange1B.setText(getConfigData(componentName, "ExtRange1B"));
                        break;
                    case R.id.edit_extRange2B:
                        strKz = mEditExtRange2B.getText().toString().trim();
                        setTextExtBCoe(strKz, 2, strKz);
                        mEditExtRange2B.setText(getConfigData(componentName, "ExtRange2B"));
                        break;
                    case R.id.edit_extRange3B:
                        strKz = mEditExtRange3B.getText().toString().trim();
                        setTextExtBCoe(strKz, 3, strKz);
                        mEditExtRange3B.setText(getConfigData(componentName, "ExtRange3B"));
                        break;
                    case R.id.editExc1:
                        strKz = mEditC1.getText().toString().trim();
                        setTextCPar(strKz, 1, strKz);
                        mEditC1.setText(getConfigData(componentName, "ExtC1"));
                        break;
                    case R.id.editExc2:
                        strKz = mEditC2.getText().toString().trim();
                        setTextCPar(strKz, 2, strKz);
                        mEditC2.setText(getConfigData(componentName, "ExtC2"));
                        break;
                    case R.id.editExc3:
                        strKz = mEditC3.getText().toString().trim();
                        setTextCPar(strKz, 3, strKz);
                        mEditC3.setText(getConfigData(componentName, "ExtC3"));
                        break;
                    case R.id.editExc4:
                        strKz = mEditC4.getText().toString().trim();
                        setTextCPar(strKz, 4, strKz);
                        mEditC4.setText(getConfigData(componentName, "ExtC4"));
                        break;
                    case R.id.editExc5:
                        strKz = mEditC5.getText().toString().trim();
                        setTextCPar(strKz, 5, strKz);
                        mEditC5.setText(getConfigData(componentName, "ExtC5"));
                        break;
                    case R.id.editExc6:
                        strKz = mEditC6.getText().toString().trim();
                        setTextCPar(strKz, 6, strKz);
                        mEditC6.setText(getConfigData(componentName, "ExtC6"));
                        break;
                    case R.id.editExc7:
                        strKz = mEditC7.getText().toString().trim();
                        setTextCPar(strKz, 7, strKz);
                        mEditC7.setText(getConfigData(componentName, "ExtC7"));
                        break;
                    case R.id.editExc8:
                        strKz = mEditC8.getText().toString().trim();
                        setTextCPar(strKz, 8, strKz);
                        mEditC8.setText(getConfigData(componentName, "ExtC8"));
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
    public static void resetExtRangeKb(String compName) {
        if (QueryMeasCateg(compName).equals("1") || QueryMeasCateg(compName).equals("2") || QueryMeasCateg(compName).equals("3") || QueryMeasCateg(compName).equals("8")
                || QueryMeasCateg(compName).equals("9") || QueryMeasCateg(compName).equals("12")) {
            for (int i = 1; i < 4; i++) {
                saveOperationLogDataModifyMsg(compName, "ExtRange" + i + "K", "1", "ExtK" + i, ErrorLog.msgType.操作_信息);
                updateConfigData(compName, "ExtRange" + i + "K", "1");
                saveOperationLogDataModifyMsg(compName, "ExtRange" + i + "B", "0", "ExtB" + i, ErrorLog.msgType.操作_信息);
                updateConfigData(compName, "ExtRange" + i + "B", "0");
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

    /**
     * 获得不同组分下修正因子范围
     */
    void getComponentScope(String compName) {
        String extRangeKUp = getConfigData(compName, "ExtRangeKUp");
        String extRangeKDown = getConfigData(compName, "ExtRangeKDown");
        String extRangeBUp = getConfigData(compName, "ExtRangeBUp");
        String extRangeBDown = getConfigData(compName, "ExtRangeBDown");
        if (!extRangeKUp.equals("") && !extRangeKDown.equals("")) {
            textKscope1.setText(getString(R.string.scope) + extRangeKDown + "~" + extRangeKUp);
        }
        if (!extRangeBUp.equals("") && !extRangeBDown.equals("")) {
            textKscope2.setText(getString(R.string.scope) + extRangeBDown + "~" + extRangeBUp);
            return;
        }
        if (compName.equals("TN") || compName.equals("总氮")) {
            textKscope2.setText(getString(R.string.scopeTNb));
        } else if (compName.equals("TP") || compName.equals("总磷")) {
            textKscope2.setText(getString(R.string.scopeTPb));
        } else if (compName.equals("CODcr") || compName.equals("化学需氧量")) {
            textKscope2.setText(getString(R.string.scopeCODcrb));
        } else if (compName.equals("CODmn") || compName.equals("高锰酸盐指数")) {
            textKscope2.setText(getString(R.string.scopeCODmnb));
        } else if (compName.equals("NH3N") || compName.equals("氨氮")) {
            textKscope2.setText(getString(R.string.scopeNH3Nb));
        } else {
            textKscope2.setText(" ");
        }
    }

    public static double getParUpLimit(String compName) {
        try {
            String extRangeBUp = getConfigData(compName, "ExtRangeBUp");
            if (!extRangeBUp.equals("")) {
                return Double.parseDouble(extRangeBUp);
            }
            if (compName.equals("TN") || compName.equals("总氮")) {
                return 1.00;
            } else if (compName.equals("TP") || compName.equals("总磷")) {
                return 0.2;
            } else if (compName.equals("CODcr") || compName.equals("化学需氧量")) {
                return 10.0;
            } else if (compName.equals("CODmn") || compName.equals("高锰酸盐指数")) {
                return 0.2;
            } else if (compName.equals("NH3N") || compName.equals("氨氮")) {
                return 0.3;
            } else {
                return 1.00;
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return 0;
    }

    public static double getParDownLimit(String compName) {
        try {
            String extRangeBDown = getConfigData(compName, "ExtRangeBDown");
            if (!extRangeBDown.equals("")) {
                return Double.parseDouble(extRangeBDown);
            }
            if (compName.equals("TN") || compName.equals("总氮")) {
                return -1.00;
            } else if (compName.equals("TP") || compName.equals("总磷")) {
                return -0.2;
            } else if (compName.equals("CODcr") || compName.equals("化学需氧量")) {
                return -10.0;
            } else if (compName.equals("CODmn") || compName.equals("高锰酸盐指数")) {
                return -0.2;
            } else if (compName.equals("NH3N") || compName.equals("氨氮")) {
                return -0.3;
            } else {
                return -1.00;
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return 0;
    }

    public static double getParKUpLimit(String compName) {
        try {
            String extRangeKUp = getConfigData(compName, "ExtRangeKUp");
            if (!extRangeKUp.equals("")) {
                return Double.parseDouble(extRangeKUp);
            }
            return 1.05;
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return 1.05;
    }

    public static double getParKDownLimit(String compName) {
        try {
            String extRangeKDown = getConfigData(compName, "ExtRangeKDown");
            if (!extRangeKDown.equals("")) {
                return Double.parseDouble(extRangeKDown);
            }
            return 0.95;
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return 0.95;
    }

}
