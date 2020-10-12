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
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6.FloatStringFormatInput;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editDataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.floatToBytes;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pVOCcldy extends SubFragment {

    private Callbacks mCallbacks;
    private static List4_Content3_pVOCcldy fragment = null;


    private static TextView tvTempCoef;
    private static EditText mEditPreK;
    private static EditText mEditPreB;
    private static EditText mEditAddEndPre;
    private static EditText mEditAddEndVPT;
    private static EditText mEditInEndPre;
    private static EditText mEditInAddEndVPT;
    private static EditText mEditT1;
    private static EditText mEditT2;
    private static EditText mEditT3;
    private static EditText mEditT4;


    public static List4_Content3_pVOCcldy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pVOCcldy();
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
        return R.layout.list4_content3_pvoc_cldy;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try{
            Global.LockDisplayShowFlag = true;
            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p1);
            mBtnReturn.setOnClickListener(new btnClick());
            InitWidget();

            byte[] arrayOfByte = DataUtil.shortToByte((short) 0);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(mCompName + "_查温度K" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 2);

            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 9000);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(mCompName + "_查voc测量参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 10);

        }catch (Exception e){
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void InitWidget() {

        tvTempCoef = (TextView) v.findViewById(R.id.tvVOCTemperKB);


        mEditPreK = (EditText) v.findViewById(R.id.edit_preK);
        mEditPreK.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditPreK.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditPreB = (EditText) v.findViewById(R.id.edit_preB);
        mEditPreB.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditPreB.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditAddEndPre = (EditText) v.findViewById(R.id.edit_addEndPre);
        mEditAddEndPre.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditAddEndPre.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditAddEndVPT = (EditText) v.findViewById(R.id.edit_addEndVPT);
        mEditAddEndVPT.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditAddEndVPT.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditInEndPre = (EditText) v.findViewById(R.id.edit_inEndPre);
        mEditInEndPre.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditInEndPre.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditInAddEndVPT = (EditText) v.findViewById(R.id.edit_inEndVPT);
        mEditInAddEndVPT.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditInAddEndVPT.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditT1 = (EditText) v.findViewById(R.id.edit_T1);
        mEditT1.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditT1.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditT2 = (EditText) v.findViewById(R.id.edit_T2);
        mEditT2.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditT2.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditT3 = (EditText) v.findViewById(R.id.edit_T3);
        mEditT3.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditT3.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditT4 = (EditText) v.findViewById(R.id.edit_T4);
        mEditT4.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditT4.setOnEditorActionListener(new EditChangeListenerHandler());
    }

    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edit_preK: {
                        if (!editTextInputStatus((EditText) v, -9999.0f, 9999.0f)) {
                            mEditPreK.setText("1");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 20000 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditPreK.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(floatToBytes(Float.parseFloat(strValue)));
                        SendManager.SendCmd(mCompName + "_" + "设置压力校正系数K_06_" + 147, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置压力校正系数K_" + strValue, ErrorLog.msgType.操作_信息);
                        mEditPreK.setText(strValue);
                    }
                    break;
                    case R.id.edit_preB: {
                        if (!editTextInputStatus((EditText) v, -9999.0f, 9999.0f)) {
                            mEditPreB.setText("0");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 29 ~ 300 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditPreB.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(floatToBytes(Float.parseFloat(strValue)));
                        SendManager.SendCmd(mCompName + "_" + "设置压力校正系数B_06_" + 148, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置压力校正系数B_" + strValue, ErrorLog.msgType.操作_信息);
                        mEditPreB.setText(strValue);
                    }
                    break;
                    case R.id.edit_addEndPre: {
                        if (!editTextInputStatus((EditText) v, 0, 500)) {
                            mEditAddEndPre.setText("100");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 500 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditAddEndPre.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(strValue), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置加压终点压力_06_" + 139, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置加压终点压力" + strValue, ErrorLog.msgType.操作_信息);
                        mEditAddEndPre.setText(strValue);
                    }
                    break;
                    case R.id.edit_addEndVPT: {
                        if (!editTextInputStatus((EditText) v, 0, 100)) {
                            mEditAddEndVPT.setText("10");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 100 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditAddEndVPT.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(strValue), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置加压终点加压阈值a_06_" + 140, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置加压终点加压阈值a" + strValue, ErrorLog.msgType.操作_信息);
                        mEditAddEndVPT.setText(strValue);
                    }
                    break;
                    case R.id.edit_inEndPre: {
                        if (!editTextInputStatus((EditText) v, 0, 500)) {
                            mEditInEndPre.setText("100");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 500 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditInEndPre.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(strValue), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置进样终点压力_06_" + 141, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置进样终点压力" + strValue, ErrorLog.msgType.操作_信息);
                        mEditInEndPre.setText(strValue);
                    }
                    break;
                    case R.id.edit_inEndVPT: {
                        if (!editTextInputStatus((EditText) v, 0, 100)) {
                            mEditInAddEndVPT.setText("10");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 100 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditInAddEndVPT.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(strValue), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置进样终点加压阈值_06_" + 142, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置进样终点加压阈值" + strValue, ErrorLog.msgType.操作_信息);
                        mEditInAddEndVPT.setText(strValue);
                    }
                    break;
                    case R.id.edit_T1: {
                        if (!editTextInputStatus((EditText) v, 0, 1200)) {
                            mEditT1.setText("10");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 1200 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditT1.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(strValue), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置T1_06_" + 143, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置T1" + strValue, ErrorLog.msgType.操作_信息);
                        mEditT1.setText(strValue);
                    }
                    break;
                    case R.id.edit_T2: {
                        if (!editTextInputStatus((EditText) v, 0, 1200)) {
                            mEditT2.setText("10");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 1200 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditT2.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(strValue), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置T2_06_" + 144, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置T2" + strValue, ErrorLog.msgType.操作_信息);
                        mEditT2.setText(strValue);
                    }
                    break;
                    case R.id.edit_T3: {
                        if (!editTextInputStatus((EditText) v, 0, 1200)) {
                            mEditT3.setText("10");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 1200 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditT3.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(strValue), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置T3_06_" + 145, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置T3" + strValue, ErrorLog.msgType.操作_信息);
                        mEditT3.setText(strValue);
                    }
                    break;
                    case R.id.edit_T4: {
                        if (!editTextInputStatus((EditText) v, 0, 1200)) {
                            mEditT4.setText("10");
                            Toast.makeText(getContext(), getString(R.string.please_confirm_the_range_input_par) + " ( 0 ~ 1200 ) " + getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String strValue = editDataFormat(mEditT4.getText().toString(), Integer.parseInt(getConfigData(mCompName , "YXWS")));
                        byte[] sByte = copybyte(toByteArray(Integer.parseInt(strValue), 4));
                        SendManager.SendCmd(mCompName + "_" + "设置T4_06_" + 146, S0, 3, 100, sByte);
                        saveOperationLogMsg(mCompName, "设置T4" + strValue, ErrorLog.msgType.操作_信息);
                        mEditT4.setText(strValue);
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
            Global.LockDisplayShowFlag = false;
            mCallbacks.onListSelected(v);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mVoccldyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1: {
                    if (tvTempCoef != null) {
                        tvTempCoef.setText((getCmds(mCompName).getCmd(0).getValue() == null ? "" : FloatStringFormatInput(getCmds(mCompName).getCmd(0).getValue().toString(), 3)) + " / "
                                + (getCmds(mCompName).getCmd(1).getValue() == null ? "" : FloatStringFormatInput(getCmds(mCompName).getCmd(1).getValue().toString(), 3)));
                        saveOperationLogMsg(mCompName, "标定系数" + "k" + getCmds(mCompName).getCmd(0).getValue().toString() + "b" + getCmds(mCompName).getCmd(1).getValue().toString(), ErrorLog.msgType.操作_信息);
                    }
                }
                break;
                case 9000:
                    if (mEditAddEndPre != null) {
                        mEditAddEndPre.setText(getCmds(mCompName).getCmd(9000).getValue() == null ? "" : getCmds(mCompName).getCmd(9000).getValue().toString());
                        mEditAddEndVPT.setText(getCmds(mCompName).getCmd(9001).getValue() == null ? "" : getCmds(mCompName).getCmd(9001).getValue().toString());
                        mEditInEndPre.setText(getCmds(mCompName).getCmd(9002).getValue() == null ? "" : getCmds(mCompName).getCmd(9002).getValue().toString());
                        mEditInAddEndVPT.setText(getCmds(mCompName).getCmd(9003).getValue() == null ? "" : getCmds(mCompName).getCmd(9003).getValue().toString());
                        mEditT1.setText(getCmds(mCompName).getCmd(9004).getValue() == null ? "" : getCmds(mCompName).getCmd(9004).getValue().toString());
                        mEditT2.setText(getCmds(mCompName).getCmd(9005).getValue() == null ? "" : getCmds(mCompName).getCmd(9005).getValue().toString());
                        mEditT3.setText(getCmds(mCompName).getCmd(9006).getValue() == null ? "" : getCmds(mCompName).getCmd(9006).getValue().toString());
                        mEditT4.setText(getCmds(mCompName).getCmd(9007).getValue() == null ? "" : getCmds(mCompName).getCmd(9007).getValue().toString());
                        mEditPreK.setText(getCmds(mCompName).getCmd(9008).getValue() == null ? "" : getCmds(mCompName).getCmd(9008).getValue().toString());
                        mEditPreB.setText(getCmds(mCompName).getCmd(9009).getValue() == null ? "" : getCmds(mCompName).getCmd(9009).getValue().toString());
                    }
                    break;
            }
        }
    };
}
