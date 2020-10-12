package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditFloatData4;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static java.lang.Thread.sleep;

public class List4_Content3_pzsbcs extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pzsbcs fragment = null;

    public static List4_Content3_pzsbcs newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pzsbcs();
        }
        return fragment;
    }

    private static EditText mEthcjtj;
    private static EditText mEtxsjtj;
    private static EditText mEtchjtj;
    private static EditText mEthcjcxtj;
    private static EditText mEtxsjcxtj;
    private static EditText mEtchjcxtj;

    private int iPoint = 2;

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
        return R.layout.list4_content3_pzsbcs;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.LockDisplayShowFlag = true;
            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());

            mEthcjtj = (EditText) v.findViewById(R.id.eThcjtj);
            mEthcjtj.setOnFocusChangeListener(new EditTextFocusChange());
            mEthcjtj.setOnEditorActionListener(new EditTextFocusChange());

            mEtxsjtj = (EditText) v.findViewById(R.id.eTxsjtj);
            mEtxsjtj.setOnFocusChangeListener(new EditTextFocusChange());
            mEtxsjtj.setOnEditorActionListener(new EditTextFocusChange());

            mEtchjtj = (EditText) v.findViewById(R.id.eTchjtj);
            mEtchjtj.setOnFocusChangeListener(new EditTextFocusChange());
            mEtchjtj.setOnEditorActionListener(new EditTextFocusChange());

            mEthcjcxtj = (EditText) v.findViewById(R.id.eThcjcxtj);
            mEthcjcxtj.setOnFocusChangeListener(new EditTextFocusChange());
            mEthcjcxtj.setOnEditorActionListener(new EditTextFocusChange());

            mEtxsjcxtj = (EditText) v.findViewById(R.id.eTxsjcxtj);
            mEtxsjcxtj.setOnFocusChangeListener(new EditTextFocusChange());
            mEtxsjcxtj.setOnEditorActionListener(new EditTextFocusChange());

            mEtchjcxtj = (EditText) v.findViewById(R.id.eTchjcxtj);
            mEtchjcxtj.setOnFocusChangeListener(new EditTextFocusChange());
            mEtchjcxtj.setOnEditorActionListener(new EditTextFocusChange());

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 9400);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(mCompName + "_查挥发酚参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 6);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }

    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private class EditTextFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.eThcjtj: {
                        setCKAEditFloatData4(mCompName, (EditText) v, iPoint, 0, 5, 4, 254, "设置缓冲剂体积");
                    }
                    break;
                    case R.id.eTxsjtj: {
                        setCKAEditFloatData4(mCompName, (EditText) v, iPoint, 0, 2.5, 0.7, 256, "设置显色剂体积");
                    }
                    break;
                    case R.id.eTchjtj: {
                        setCKAEditFloatData4(mCompName, (EditText) v, iPoint, 0, 2.5, 0.7, 258, "设置催化剂体积");
                    }
                    break;
                    case R.id.eThcjcxtj: {
                        setCKAEditFloatData4(mCompName, (EditText) v, iPoint, 0, 2.5, 2.5, 253, "设置缓冲剂冲洗体积");
                    }
                    break;
                    case R.id.eTxsjcxtj: {
                        setCKAEditFloatData4(mCompName, (EditText) v, iPoint, 0, 2.5, 0.7, 255, "设置显色剂冲洗体积");
                    }
                    break;
                    case R.id.eTchjcxtj: {
                        setCKAEditFloatData4(mCompName, (EditText) v, iPoint, 0, 2.5, 0.7, 257, "设置催化剂冲洗体积");
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
                FullWindows(mActivityWindow);
                return true;
            }
            return false;
        }
    }

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    @SuppressLint("HandlerLeak")
    public static Handler mZsbcsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 9400:
                    if (mEthcjtj != null) {
                        mEthcjtj.setText(getCmds(mCompName).getCmd(9401).getValue() == null ? "" : getCmds(mCompName).getCmd(9401).getValue().toString());
                        mEtxsjtj.setText(getCmds(mCompName).getCmd(9403).getValue() == null ? "" : getCmds(mCompName).getCmd(9403).getValue().toString());
                        mEtchjtj.setText(getCmds(mCompName).getCmd(9405).getValue() == null ? "" : getCmds(mCompName).getCmd(9405).getValue().toString());
                        mEthcjcxtj.setText(getCmds(mCompName).getCmd(9400).getValue() == null ? "" : getCmds(mCompName).getCmd(9400).getValue().toString());
                        mEtxsjcxtj.setText(getCmds(mCompName).getCmd(9402).getValue() == null ? "" : getCmds(mCompName).getCmd(9402).getValue().toString());
                        mEtchjcxtj.setText(getCmds(mCompName).getCmd(9404).getValue() == null ? "" : getCmds(mCompName).getCmd(9404).getValue().toString());
                    }
                    break;
            }
        }
    };
}
