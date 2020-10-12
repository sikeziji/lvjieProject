package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;

public class List4_Content3_pfyzs extends SubFragment {

    private static List4_Content3_pfyzs fragment = null;
    private Callbacks mCallbacks;

    private EditText mEtfyzs_K1;
    private EditText mEtfyzs_B1;
    private EditText mEtfyzs_K2;
    private EditText mEtfyzs_B;
    private TextView mT_fyzs_jrl;

    public static List4_Content3_pfyzs newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pfyzs();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();
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
    protected int getLayoutResId() {
        return R.layout.list4_content3_pfyzs;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p2);
        mBtnReturn.setOnClickListener(new btnClick());

        mEtfyzs_K1 = (EditText) v.findViewById(R.id.eTfyzs_K1);
        mEtfyzs_K1.setText(getConfigData(mCompName, "fyzs_k1"));
        mEtfyzs_K1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEtfyzs_K1.setOnEditorActionListener(new EditChangeListenerHandler());

        mEtfyzs_B1 = (EditText) v.findViewById(R.id.eTfyzs_B1);
        mEtfyzs_B1.setText(getConfigData(mCompName, "fyzs_b1"));
        mEtfyzs_B1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEtfyzs_B1.setOnEditorActionListener(new EditChangeListenerHandler());

        mEtfyzs_K2 = (EditText) v.findViewById(R.id.eTfyzs_K2);
        mEtfyzs_K2.setText(getConfigData(mCompName, "fyzs_k2"));
        mEtfyzs_K2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEtfyzs_K2.setOnEditorActionListener(new EditChangeListenerHandler());

        mEtfyzs_B = (EditText) v.findViewById(R.id.eTfyzs_B);
        mEtfyzs_B.setText(getConfigData(mCompName, "fyzs_b"));
        mEtfyzs_B.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEtfyzs_B.setOnEditorActionListener(new EditChangeListenerHandler());

        mT_fyzs_jrl = (TextView) v.findViewById(R.id.t_fyzs_jrl);
        mT_fyzs_jrl.setText(getConfigData(mCompName, "fyzs_M"));
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eTfyzs_K1: {
                        String strETfyzs_K1 = mEtfyzs_K1.getText().toString().trim();
                        if (!strETfyzs_K1.equals("")) {
                            double k1Double = Double.parseDouble(strETfyzs_K1);
                            if (k1Double < 0.0001 || k1Double > 2) {
                                mEtfyzs_K1.setText("0.0111");
                            }
                            saveOperationLogDataModifyMsg(mCompName, "fyzs_k1", mEtfyzs_K1.getText().toString(), "设置废液再生K1", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "fyzs_k1", mEtfyzs_K1.getText().toString());
                        } else {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTfyzs_B1: {
                        String strETfyzs_B1 = mEtfyzs_B1.getText().toString().trim();
                        if (!strETfyzs_B1.equals("")) {
                            double B1Double = Double.parseDouble(strETfyzs_B1);
                            if (B1Double < -10 || B1Double > 10) {
                                mEtfyzs_B1.setText("1");
                            }
                            saveOperationLogDataModifyMsg(mCompName, "fyzs_b1", mEtfyzs_B1.getText().toString(), "设置废液再生B1", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "fyzs_b1", mEtfyzs_B1.getText().toString());
                        } else {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTfyzs_K2: {
                        String strETfyzs_K2 = mEtfyzs_K2.getText().toString().trim();
                        if (!strETfyzs_K2.equals("")) {
                            double k2Double = Double.parseDouble(strETfyzs_K2);
                            if (k2Double < 0.0001 || k2Double > 2) {
                                mEtfyzs_K2.setText("0.0111");
                            }
                            saveOperationLogDataModifyMsg(mCompName, "fyzs_k2", mEtfyzs_K2.getText().toString(), "设置废液再生K2", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "fyzs_k2", mEtfyzs_K2.getText().toString());
                        } else {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case R.id.eTfyzs_B: {
                        String strETfyzs_B = mEtfyzs_B.getText().toString().trim();
                        if (!strETfyzs_B.equals("")) {
                            double BDouble = Double.parseDouble(strETfyzs_B);
                            if (BDouble < -300 || BDouble > 100) {
                                mEtfyzs_B.setText("0");
                            }
                            saveOperationLogDataModifyMsg(mCompName, "fyzs_b", mEtfyzs_B.getText().toString(), "设置废液再生原始B值", ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "fyzs_b", mEtfyzs_B.getText().toString());
                        } else {
                            Toast.makeText(getActivity(), context.getResources().getString(R.string.set_value_is_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
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

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

}