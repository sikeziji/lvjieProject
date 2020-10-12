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

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditFloatData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pddwkcsdy extends SubFragment {
    private Callbacks mCallbacks;
    private static List4_Content3_pddwkcsdy fragment = null;


    private static EditText mEditTempA1;
    private static EditText mEditTempB1;
    private static EditText mEditTempA2;
    private static EditText mEditTempB2;
    private static EditText mEditThermalEfficiencyA1;
    private static EditText mEditThermalEfficiencyB1;
    private static EditText mEditThermalEfficiencyC1;
    private static EditText mEditThermalEfficiencyA2;
    private static EditText mEditThermalEfficiencyB2;
    private static EditText mEditThermalEfficiencyC2;
    private int iPoint = 2;

    public static List4_Content3_pddwkcsdy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pddwkcsdy();
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
        return R.layout.list4_content3_pddwkdy;
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
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());
            InitWidget();


            byte[] arrayOfByte4 = DataUtil.shortToByte((short) 5735);
            DataUtil.reverse(arrayOfByte4);
            SendManager.SendCmd(mCompName + "_查滴定过程控制参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte4, 2).replace(" ", ""), Global.S0, 3, 200, 10);


        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void InitWidget() {

        mEditTempA1 = (EditText) v.findViewById(R.id.edit_tempA1);
        mEditTempA1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTempA1.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTempB1 = (EditText) v.findViewById(R.id.edit_tempB1);
        mEditTempB1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTempB1.setOnEditorActionListener(new EditChangeListenerHandler());

        EditText mEditTempC1 = (EditText) v.findViewById(R.id.edit_tempC1);
        mEditTempC1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTempC1.setOnEditorActionListener(new EditChangeListenerHandler());

        EditText mEditTempD1 = (EditText) v.findViewById(R.id.edit_tempD1);
        mEditTempD1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTempD1.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTempA2 = (EditText) v.findViewById(R.id.edit_tempA2);
        mEditTempA2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTempA2.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTempB2 = (EditText) v.findViewById(R.id.edit_tempB2);
        mEditTempB2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTempB2.setOnEditorActionListener(new EditChangeListenerHandler());

        EditText mEditTempC2 = (EditText) v.findViewById(R.id.edit_tempC2);
        mEditTempC2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTempC2.setOnEditorActionListener(new EditChangeListenerHandler());

        EditText mEditTempD2 = (EditText) v.findViewById(R.id.edit_tempD2);
        mEditTempD2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTempD2.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditThermalEfficiencyA1 = (EditText) v.findViewById(R.id.edit_thermal_efficiency_A1);
        mEditThermalEfficiencyA1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditThermalEfficiencyA1.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditThermalEfficiencyB1 = (EditText) v.findViewById(R.id.edit_thermal_efficiency_B1);
        mEditThermalEfficiencyB1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditThermalEfficiencyB1.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditThermalEfficiencyC1 = (EditText) v.findViewById(R.id.edit_thermal_efficiency_C1);
        mEditThermalEfficiencyC1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditThermalEfficiencyC1.setOnEditorActionListener(new EditChangeListenerHandler());

        EditText mEditThermalEfficiencyD1 = (EditText) v.findViewById(R.id.edit_thermal_efficiency_D1);
        mEditThermalEfficiencyD1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditThermalEfficiencyD1.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditThermalEfficiencyA2 = (EditText) v.findViewById(R.id.edit_thermal_efficiency_A2);
        mEditThermalEfficiencyA2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditThermalEfficiencyA2.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditThermalEfficiencyB2 = (EditText) v.findViewById(R.id.edit_thermal_efficiency_B2);
        mEditThermalEfficiencyB2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditThermalEfficiencyB2.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditThermalEfficiencyC2 = (EditText) v.findViewById(R.id.edit_thermal_efficiency_C2);
        mEditThermalEfficiencyC2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditThermalEfficiencyC2.setOnEditorActionListener(new EditChangeListenerHandler());

        EditText mEditThermalEfficiencyD2 = (EditText) v.findViewById(R.id.edit_thermal_efficiency_D2);
        mEditThermalEfficiencyD2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditThermalEfficiencyD2.setOnEditorActionListener(new EditChangeListenerHandler());
    }


    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edit_tempA1: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 50, 120, 85, 161, "设置消解5温度A");
                    }
                    break;
                    case R.id.edit_tempB1: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 50, 120, 95, 162, "设置消解5温度B");
                    }
                    break;
                    case R.id.edit_tempC1: {
                    }
                    break;
                    case R.id.edit_tempD1: {
                    }
                    break;
                    case R.id.edit_thermal_efficiency_A1: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 2000, 1500, 163, "设置消解5保温效率A");
                    }
                    break;
                    case R.id.edit_thermal_efficiency_B1: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 2000, 1500, 164, "设置消解5保温效率B");
                    }
                    break;
                    case R.id.edit_thermal_efficiency_C1: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 2000, 1500, 165, "设置消解5保温效率C");
                    }
                    break;
                    case R.id.edit_thermal_efficiency_D1: {

                    }
                    break;
                    case R.id.edit_tempA2: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 50, 120, 75, 166, "设置消解6温度A");
                    }
                    break;
                    case R.id.edit_tempB2: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 50, 120, 85, 167, "设置消解6温度B");
                    }
                    break;
                    case R.id.edit_tempC2: {
                    }
                    break;
                    case R.id.edit_tempD2: {
                    }
                    break;
                    case R.id.edit_thermal_efficiency_A2: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 2000, 1500, 168, "设置消解6保温效率A");
                    }
                    break;
                    case R.id.edit_thermal_efficiency_B2: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 2000, 1500, 169, "设置消解6保温效率B");
                    }
                    break;
                    case R.id.edit_thermal_efficiency_C2: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 2000, 1500, 170, "设置消解6保温效率C");
                    }
                    break;
                    case R.id.edit_thermal_efficiency_D2: {
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
    public static Handler mddwkdyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 5735:
                    if (mEditTempA1 != null) {
                        mEditTempA1.setText(getCmds(mCompName).getCmd(5735).getValue() == null ? "" : getCmds(mCompName).getCmd(5735).getValue().toString());
                        mEditTempB1.setText(getCmds(mCompName).getCmd(5736).getValue() == null ? "" : getCmds(mCompName).getCmd(5736).getValue().toString());

                        mEditTempA2.setText(getCmds(mCompName).getCmd(5740).getValue() == null ? "" : getCmds(mCompName).getCmd(5740).getValue().toString());
                        mEditTempB2.setText(getCmds(mCompName).getCmd(5741).getValue() == null ? "" : getCmds(mCompName).getCmd(5741).getValue().toString());

                        mEditThermalEfficiencyA1.setText(getCmds(mCompName).getCmd(5737).getValue() == null ? "" : getCmds(mCompName).getCmd(5737).getValue().toString());
                        mEditThermalEfficiencyB1.setText(getCmds(mCompName).getCmd(5738).getValue() == null ? "" : getCmds(mCompName).getCmd(5738).getValue().toString());
                        mEditThermalEfficiencyC1.setText(getCmds(mCompName).getCmd(5739).getValue() == null ? "" : getCmds(mCompName).getCmd(5739).getValue().toString());

                        mEditThermalEfficiencyA2.setText(getCmds(mCompName).getCmd(5742).getValue() == null ? "" : getCmds(mCompName).getCmd(5742).getValue().toString());
                        mEditThermalEfficiencyB2.setText(getCmds(mCompName).getCmd(5743).getValue() == null ? "" : getCmds(mCompName).getCmd(5743).getValue().toString());
                        mEditThermalEfficiencyC2.setText(getCmds(mCompName).getCmd(5744).getValue() == null ? "" : getCmds(mCompName).getCmd(5744).getValue().toString());
                    }
                    break;
            }
        }
    };
}
