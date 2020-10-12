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
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditFloatData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditFloatData2;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData4;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pddcsdy2 extends SubFragment {

    private Callbacks mCallbacks;
    private static List4_Content3_pddcsdy2 fragment = null;


    private static EditText mEditOxidizingVolume;
    private static EditText mEditOxidizingVolume2;
    private static EditText mEditMinVolume;
    private static EditText mEditMaxVolume;

    private static EditText mEditASpace;
    private static EditText mEditBSpace;
    private static EditText mEditASpaceStepLength;
    private static EditText mEditBSpaceStepLength;

    private static EditText mEditASpaceDropNum;
    private static ToggleButton mTraceMode;
    private int iPoint = 2;


    private static EditText mEditOxidizingWashVolume;
    private static EditText mEditReductantWashVolume;
    private static EditText mEditCatalystWashVolume;
    private static EditText mEditLyeWashVolume;


    public static List4_Content3_pddcsdy2 newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pddcsdy2();
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
        return R.layout.list4_content3_pddcsdy2;
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
            SendManager.SendCmd(mCompName + "_查滴定过程控制参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 24);

            byte[] arrayOfByte4 = DataUtil.shortToByte((short) 5714);
            DataUtil.reverse(arrayOfByte4);
            SendManager.SendCmd(mCompName + "_查滴定参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte4, 2).replace(" ", ""), Global.S0, 3, 200, 6);

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

        mEditASpace = v.findViewById(R.id.edit_aSpace);
        mEditASpace.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditASpace.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditBSpace = v.findViewById(R.id.edit_bSpace);
        mEditBSpace.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditBSpace.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditOxidizingVolume = v.findViewById(R.id.edit_oxidizingVolume);
        mEditOxidizingVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditOxidizingVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditOxidizingVolume2 = v.findViewById(R.id.edit_oxidizingVolume2);
        mEditOxidizingVolume2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditOxidizingVolume2.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditASpaceStepLength = v.findViewById(R.id.edit_aSpaceStepLength);
        mEditASpaceStepLength.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditASpaceStepLength.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditBSpaceStepLength = v.findViewById(R.id.edit_bSpaceStepLength);
        mEditBSpaceStepLength.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditBSpaceStepLength.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditASpaceDropNum = v.findViewById(R.id.edit_aSpaceDropNum);
        mEditASpaceDropNum.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditASpaceDropNum.setOnEditorActionListener(new EditChangeListenerHandler());

        mTraceMode = v.findViewById(R.id.Btn_traceMode);
        mTraceMode.setOnClickListener(new mToggleBtnClick());

        mEditOxidizingWashVolume = v.findViewById(R.id.edit_oxidizingWashVolume);
        mEditOxidizingWashVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditOxidizingWashVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditReductantWashVolume = v.findViewById(R.id.edit_reductantWashVolume);
        mEditReductantWashVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditReductantWashVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditCatalystWashVolume = v.findViewById(R.id.edit_catalystWashVolume);
        mEditCatalystWashVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditCatalystWashVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditLyeWashVolume = v.findViewById(R.id.edit_lyeWashVolume);
        mEditLyeWashVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditLyeWashVolume.setOnEditorActionListener(new EditChangeListenerHandler());

    }

    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edit_minVolume: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0.0, 8, 0.3, 157, "设置最小滴定体积");
                    }
                    break;
                    case R.id.edit_maxVolume: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0.0, 21, 2.5, 158, "设置最大滴定体积");
                    }
                    break;
                    case R.id.edit_aSpace: {
                        setCKAEditIntData4(mCompName, (EditText) v, iPoint, 0, 90, 3, 134, "设置滴定A阶段间隔");
                    }
                    break;
                    case R.id.edit_bSpace: {
                        setCKAEditIntData4(mCompName, (EditText) v, iPoint, 0, 90, 8, 135, "设置滴定B阶段间隔");
                    }
                    break;
                    case R.id.edit_oxidizingVolume: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 11, 5, 160, "设置滴定氧化剂体积");
                    }
                    break;
                    case R.id.edit_oxidizingVolume2: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 11, 5, 244, "设置滴定氧化剂体积2");
                    }
                    break;
                    case R.id.edit_aSpaceStepLength: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 3001, 30, 212, "设置A阶段步长");
                    }
                    break;
                    case R.id.edit_bSpaceStepLength: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 3001, 60, 213, "设置B阶段步长");
                    }
                    break;
                    case R.id.edit_aSpaceDropNum: {
                        setCKAEditIntData4(mCompName, (EditText) v, iPoint, 0, 1000, 10, 214, "设置A阶段滴数");
                    }
                    break;
                    case R.id.edit_oxidizingWashVolume:
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 11, 2.5, 245, "设置滴定氧化剂冲洗体积");
                        break;
                    case R.id.edit_reductantWashVolume:
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 11, 2.5, 246, "设置滴定还原剂冲洗体积");
                        break;
                    case R.id.edit_catalystWashVolume:
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 11, 5, 247, "设置滴定催化剂冲洗体积");
                        break;
                    case R.id.edit_lyeWashVolume:
                        setCKAEditFloatData2(mCompName, (EditText) v, iPoint, 0, 10, 5, 314, "设置滴定碱液冲洗体积");
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
            Global.LockDisplayShowFlag = false;
            mCallbacks.onListSelected(v);
        }
    }

    private class mToggleBtnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.Btn_traceMode:
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(String.valueOf((((ToggleButton) v).isChecked() ? 0x01 : 0x00))), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置微量模式" + "_06_219", S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置微量模式:" + (((ToggleButton) v).isChecked() ? 0x01 : 0x00), ErrorLog.msgType.操作_信息);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mddcsdy2Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 5726:
                    if (mEditASpace != null) {
                        mEditASpace.setText(getCmds(mCompName).getCmd(5726).getValue() == null ? "" : getCmds(mCompName).getCmd(5726).getValue().toString());
                        mEditBSpace.setText(getCmds(mCompName).getCmd(5727).getValue() == null ? "" : getCmds(mCompName).getCmd(5727).getValue().toString());
                        mEditMinVolume.setText(getCmds(mCompName).getCmd(5731).getValue() == null ? "" : getCmds(mCompName).getCmd(5731).getValue().toString());
                        mEditMaxVolume.setText(getCmds(mCompName).getCmd(5732).getValue() == null ? "" : getCmds(mCompName).getCmd(5732).getValue().toString());
                        mEditOxidizingVolume.setText(getCmds(mCompName).getCmd(5734).getValue() == null ? "" : getCmds(mCompName).getCmd(5734).getValue().toString());
                        mEditASpaceStepLength.setText(getCmds(mCompName).getCmd(5745).getValue() == null ? "" : getCmds(mCompName).getCmd(5745).getValue().toString());
                        mEditBSpaceStepLength.setText(getCmds(mCompName).getCmd(5746).getValue() == null ? "" : getCmds(mCompName).getCmd(5746).getValue().toString());
                        mEditASpaceDropNum.setText(getCmds(mCompName).getCmd(5747).getValue() == null ? "" : getCmds(mCompName).getCmd(5747).getValue().toString());
                        mTraceMode.setChecked((getCmds(mCompName).getCmd(5749).getValue() != null && (getCmds(mCompName).getCmd(5749).getValue().toString().equals("1"))));
                    }
                    break;
                case 5714:
                    mEditOxidizingVolume2.setText(getCmds(mCompName).getCmd(5714).getValue() == null ? "" : getCmds(mCompName).getCmd(5714).getValue().toString());

                    mEditOxidizingWashVolume.setText(getCmds(mCompName).getCmd(5715).getValue() == null ? "" : getCmds(mCompName).getCmd(5715).getValue().toString());
                    mEditReductantWashVolume.setText(getCmds(mCompName).getCmd(5716).getValue() == null ? "" : getCmds(mCompName).getCmd(5716).getValue().toString());
                    mEditCatalystWashVolume.setText(getCmds(mCompName).getCmd(5717).getValue() == null ? "" : getCmds(mCompName).getCmd(5717).getValue().toString());

                    mEditLyeWashVolume.setText(getCmds(mCompName).getCmd(5719).getValue() == null ? "" : getCmds(mCompName).getCmd(5719).getValue().toString());
                    break;
            }
        }
    };
}
