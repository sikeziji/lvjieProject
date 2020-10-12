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
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditFloatData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pddcsdy extends SubFragment {

    private Callbacks mCallbacks;
    private static List4_Content3_pddcsdy fragment = null;


    private static EditText mEditTitrationSpace;
    private static EditText mEditMinVolume;
    private static EditText mEditMaxVolume;
    private static EditText mEditMinTime;
    private static EditText mEditMaxTime;
    private static EditText mEditTitrationTimerA;
    private static EditText mEditTitrationTimerB;
    private static EditText mEditEnUp;
    private static EditText mEditEnDown;
    private int iPoint = 2;


    public static List4_Content3_pddcsdy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pddcsdy();
        }
        return fragment;
    }

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
        return R.layout.list4_content3_pddcsdy;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            Global.LockDisplayShowFlag = true;
            iPoint = Integer.parseInt(getConfigData(mCompName, "YXWS"));

            ImageButton mBtnReturn = v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());
            InitWidget();

            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 5726);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(mCompName + "_查滴定过程控制参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 23);

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }
    }

    private void InitWidget() {

        mEditMinVolume = v.findViewById(R.id.edit_minVolume);
        mEditMinVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditMinVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditMaxVolume = v.findViewById(R.id.edit_maxVolume);
        mEditMaxVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditMaxVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditMinTime = v.findViewById(R.id.edit_minTime);
        mEditMinTime.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditMinTime.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditMaxTime = v.findViewById(R.id.edit_maxTime);
        mEditMaxTime.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditMaxTime.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTitrationSpace = v.findViewById(R.id.edit_titrationSpace);
        mEditTitrationSpace.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTitrationSpace.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTitrationTimerA = v.findViewById(R.id.edit_titrationTimerA);
        mEditTitrationTimerA.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTitrationTimerA.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTitrationTimerB = v.findViewById(R.id.edit_titrationTimerB);
        mEditTitrationTimerB.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTitrationTimerB.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditEnUp = v.findViewById(R.id.edit_titrationLightEnUp);
        mEditEnUp.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditEnUp.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditEnDown = v.findViewById(R.id.edit_titrationLightEnDown);
        mEditEnDown.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditEnDown.setOnEditorActionListener(new EditChangeListenerHandler());
    }

    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {

                    case R.id.edit_minVolume: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0.0, 10.0, 1, 157, "设置最小滴定体积");
                    }
                    break;
                    case R.id.edit_maxVolume: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0.0, 10.0, 9, 158, "设置最大滴定体积");
                    }
                    break;
                    case R.id.edit_minTime: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 20, 5, 134, "设置最小等待时间");
                    }
                    break;
                    case R.id.edit_maxTime: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 20, 10, 135, "设置最大等待时间");
                    }
                    break;
                    case R.id.edit_titrationSpace: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 20, 10, 160, "设置滴定间隔");
                    }
                    break;
                    case R.id.edit_titrationTimerA: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 500, 160, 212, "设置滴定计时A");
                    }
                    break;
                    case R.id.edit_titrationTimerB: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 500, 160, 213, "设置滴定计时B");
                    }
                    break;
                    case R.id.edit_titrationLightEnUp: {
                        String strEnUp = mEditEnUp.getText().toString();
                        String strEnDown = mEditEnDown.getText().toString();
                        if (!strEnUp.equals("") && !strEnDown.equals("")) {
                            int iEnUp = Integer.parseInt(strEnUp);
                            int iEnDown = Integer.parseInt(strEnDown);
                            if (iEnDown < iEnUp) {
                                setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 50000, 20000, 214, "设置滴定起始光强上限");
                            } else {
                                mEditEnUp.setText("20000");
                                setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 50000, 20000, 214, "设置滴定起始光强上限");
                                Toast.makeText(context, context.getResources().getString(R.string.en_par_low_down_limit) + "," + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                    case R.id.edit_titrationLightEnDown: {
                        String strEnUp = mEditEnUp.getText().toString();
                        String strEnDown = mEditEnDown.getText().toString();
                        if (!strEnUp.equals("") && !strEnDown.equals("")) {
                            int iEnUp = Integer.parseInt(strEnUp);
                            int iEnDown = Integer.parseInt(strEnDown);
                            if (iEnDown < iEnUp) {
                                setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 50000, 10000, 215, "设置滴定起始光强下限");

                            } else {
                                mEditEnDown.setText("10000");
                                setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 50000, 10000, 215, "设置滴定起始光强下限");
                                Toast.makeText(context, context.getResources().getString(R.string.en_par_high_up_limit) + "," + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            }
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


    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mddcsdyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 5726:
                    if (mEditMinTime != null) {

                        mEditMinTime.setText(getCmds(mCompName).getCmd(5726).getValue() == null ? "" : getCmds(mCompName).getCmd(5726).getValue().toString());
                        mEditMaxTime.setText(getCmds(mCompName).getCmd(5727).getValue() == null ? "" : getCmds(mCompName).getCmd(5727).getValue().toString());

                        mEditMinVolume.setText(getCmds(mCompName).getCmd(5731).getValue() == null ? "" : getCmds(mCompName).getCmd(5731).getValue().toString());
                        mEditMaxVolume.setText(getCmds(mCompName).getCmd(5732).getValue() == null ? "" : getCmds(mCompName).getCmd(5732).getValue().toString());

                        mEditTitrationSpace.setText(getCmds(mCompName).getCmd(5734).getValue() == null ? "" : getCmds(mCompName).getCmd(5734).getValue().toString());

                        mEditTitrationTimerA.setText(getCmds(mCompName).getCmd(5745).getValue() == null ? "" : getCmds(mCompName).getCmd(5745).getValue().toString());
                        mEditTitrationTimerB.setText(getCmds(mCompName).getCmd(5746).getValue() == null ? "" : getCmds(mCompName).getCmd(5746).getValue().toString());

                        mEditEnUp.setText(getCmds(mCompName).getCmd(5747).getValue() == null ? "" : getCmds(mCompName).getCmd(5747).getValue().toString());
                        mEditEnDown.setText(getCmds(mCompName).getCmd(5748).getValue() == null ? "" : getCmds(mCompName).getCmd(5748).getValue().toString());
                    }
                    break;
            }
        }
    };
}
