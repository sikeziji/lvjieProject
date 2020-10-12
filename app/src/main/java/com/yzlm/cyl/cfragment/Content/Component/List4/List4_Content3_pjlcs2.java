package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmdsValue;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pjlcs2 extends SubFragment {
    private static List4_Content3_pjlcs2 fragment = null;
    private static EditText SID;
    private static EditText SIA;
    private static EditText SWD;
    private static EditText SWA;
    private static EditText IFD;
    private static EditText IFA;
    @SuppressLint("HandlerLeak")
    public static Handler mjlcs2Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {

                    case 10000:
                        SID.setText(getCmdsValue(mCompName, 10000));
                        SIA.setText(getCmdsValue(mCompName, 10001));
                        SWD.setText(getCmdsValue(mCompName, 10002));
                        SWA.setText(getCmdsValue(mCompName, 10003));
                        IFD.setText(getCmdsValue(mCompName, 10004));
                        IFA.setText(getCmdsValue(mCompName, 10005));
                        break;
                    default:
                        break;
                }
                Toast.makeText(context, context.getString(R.string.is_dirty), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, context.getString(R.string.exception_par), Toast.LENGTH_SHORT).show();
            }
        }
    };
    private static Button saveBtn;
    private Callbacks mCallbacks;
    private List<String> valveList = new ArrayList<>();

    public static List4_Content3_pjlcs2 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pjlcs2();
        }
        return fragment;
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
        return R.layout.list4_content3_pjlcs2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.LockDisplayShowFlag = true;
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());

            SID = (EditText) v.findViewById(R.id.eT_pjlcs2_SID);
            SID.setOnFocusChangeListener(new EditFocusChangeComp());
            SID.setOnEditorActionListener(new EditFocusChangeComp());

            SIA = (EditText) v.findViewById(R.id.eT_pjlcs2_SIA);
            SIA.setOnFocusChangeListener(new EditFocusChangeComp());
            SIA.setOnEditorActionListener(new EditFocusChangeComp());

            SWD = (EditText) v.findViewById(R.id.eT_pjlcs2_SWD);
            SWD.setOnFocusChangeListener(new EditFocusChangeComp());
            SWD.setOnEditorActionListener(new EditFocusChangeComp());

            SWA = (EditText) v.findViewById(R.id.eT_pjlcs2_SWA);
            SWA.setOnFocusChangeListener(new EditFocusChangeComp());
            SWA.setOnEditorActionListener(new EditFocusChangeComp());

            IFD = (EditText) v.findViewById(R.id.eT_pjlcs2_IFD);
            IFD.setOnFocusChangeListener(new EditFocusChangeComp());
            IFD.setOnEditorActionListener(new EditFocusChangeComp());

            IFA = (EditText) v.findViewById(R.id.eT_pjlcs2_IFA);
            IFA.setOnFocusChangeListener(new EditFocusChangeComp());
            IFA.setOnEditorActionListener(new EditFocusChangeComp());

            saveBtn = (Button) v.findViewById(R.id.Btn_pjlcs2_save);
            saveBtn.setOnClickListener(new btnSaveClick());
            setEditTouch();


            SendManager.SendCmd(mCompName + "_查SI-D计量参数" + "_3_2710", S0, 2, 500, 6);
            /*SendManager.SendCmd(mCompName + "_查SI-A计量参数" + "_3_2711", S0, 2, 500, 10);
            SendManager.SendCmd(mCompName + "_查SW-D计量参数" + "_3_2712", S0, 2, 500, 10);
            SendManager.SendCmd(mCompName + "_查SW-A计量参数" + "_3_2713", S0, 2, 500, 10);
            SendManager.SendCmd(mCompName + "_查IF-D计量参数" + "_3_2714", S0, 2, 500, 10);
            SendManager.SendCmd(mCompName + "_查IF-A计量参数" + "_3_2715", S0, 2, 500, 10);*/

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private void setEditTouch() {
        // 非管理员情况，则全部不可修改
        if (getPublicConfigData("LogInName").equals("2") || getPublicConfigData("LogInName").equals("3")) {

            SID.setEnabled(true);
            SIA.setEnabled(true);
            SWD.setEnabled(true);
            SWA.setEnabled(true);
            IFD.setEnabled(true);
            IFA.setEnabled(true);

            saveBtn.setVisibility(View.VISIBLE);
        } else {
            SID.setEnabled(false);
            SIA.setEnabled(false);
            SWD.setEnabled(false);
            SWA.setEnabled(false);
            IFD.setEnabled(false);
            IFA.setEnabled(false);

            saveBtn.setVisibility(View.GONE);

        }
    }


    public interface Callbacks {
        void onListSelected(View view);
    }

    private class EditFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                FullWindows(mActivityWindow);
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

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private class btnSaveClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String SIDStr = SID.getText().toString().trim();
            String SIAStr = SIA.getText().toString().trim();
            String SWDStr = SWD.getText().toString().trim();
            String SWAStr = SWA.getText().toString().trim();
            String IFDStr = IFD.getText().toString().trim();
            String IFAStr = IFA.getText().toString().trim();


            if (!SIDStr.equals("") && !SIAStr.equals("") && !SWDStr.equals("") &&
                    !SWAStr.equals("") && !IFDStr.equals("") && !IFAStr.equals("")) {
                byte[] jlcsByte = copybyte(toByteArray(Integer.parseInt(SIDStr), 4), toByteArray(Integer.parseInt(SIAStr), 4),
                        toByteArray(Integer.parseInt(SWDStr), 4), toByteArray(Integer.parseInt(SWDStr), 4),
                        toByteArray(Integer.parseInt(IFDStr), 4), toByteArray(Integer.parseInt(IFAStr), 4));

                saveOperationLogMsg(mCompName, "设置计量参数", ErrorLog.msgType.操作_信息);
                SendManager.SendCmd(mCompName + "_设置计量参数_06_" + 333, S0, 3, 200, jlcsByte);
            } else {
                Toast.makeText(getActivity(), getString(R.string.measurement_par_empty), Toast.LENGTH_SHORT).show();
            }

            FullWindows(mActivityWindow);
        }
    }

}
