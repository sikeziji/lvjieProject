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
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6.FloatStringFormatInput;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pcldy.setBsLedStatus;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.editTextInputStatus4;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditFloatData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData4;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getCmdsValue;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pTOCcldy extends SubFragment {

    private Callbacks mCallbacks;


    private static EditText mEditIcPurgingTime;
    private static EditText mEditTcPurgingTime;
    private static EditText mEditCollectTime;
    private static EditText mEditMixedExtractionVolume;
    private static EditText mEditCleanVolume;

    private static EditText mEditTocK;
    private static EditText mEditTocB;
    private static EditText mEditCLCS;
    //private static EditText mEditTCLL;

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
        return R.layout.list4_content3_ptoc_cldy;
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

            mEditTocK.setText(getConfigData(mCompName, "ConversionFactorK"));
            mEditTocB.setText(getConfigData(mCompName, "ConversionFactorB"));

            byte[] arrayOfByte = DataUtil.shortToByte((short) 0);
            DataUtil.reverse(arrayOfByte);
            SendManager.SendCmd(mCompName + "_查温度K" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 2);

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) 9510);
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(mCompName + "_查TOC测量单元参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 5);

            byte[] arrayOfByte3 = DataUtil.shortToByte((short) 9502);
            DataUtil.reverse(arrayOfByte3);
            SendManager.SendCmd(mCompName + "_查TOC测量次数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 1);

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void InitWidget() {


        mEditIcPurgingTime = v.findViewById(R.id.edit_icPurgingTime);
        mEditIcPurgingTime.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditIcPurgingTime.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTcPurgingTime = v.findViewById(R.id.edit_tcPurgingTime);
        mEditTcPurgingTime.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTcPurgingTime.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditCollectTime = v.findViewById(R.id.edit_collectTime);
        mEditCollectTime.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditCollectTime.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditMixedExtractionVolume = v.findViewById(R.id.edit_mixedExtractionVolume);
        mEditMixedExtractionVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditMixedExtractionVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditCleanVolume = v.findViewById(R.id.edit_clean);
        mEditCleanVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditCleanVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTocK = v.findViewById(R.id.edit_toc_k);
        mEditTocK.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTocK.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditTocB = v.findViewById(R.id.edit_toc_b);
        mEditTocB.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTocB.setOnEditorActionListener(new EditChangeListenerHandler());

        mEditCLCS = v.findViewById(R.id.edit_clcs);
        mEditCLCS.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditCLCS.setOnEditorActionListener(new EditChangeListenerHandler());

        /*mEditTCLL = v.findViewById(R.id.edit_tcll);
        mEditTCLL.setOnFocusChangeListener(new EditChangeListenerHandler());
        mEditTCLL.setOnEditorActionListener(new EditChangeListenerHandler());*/

    }

    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.edit_icPurgingTime: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 1000, 200, 285, "设置IC吹扫时间");
                    }
                    break;
                    case R.id.edit_tcPurgingTime: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 1000, 200, 286, "设置TC吹扫时间");
                    }
                    break;
                    case R.id.edit_collectTime: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 600, 90, 287, "设置采集时间");
                    }
                    break;
                    case R.id.edit_mixedExtractionVolume: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0.0, 10.0, 1.5, 289, "设置混合抽取体积");
                    }
                    break;
                    case R.id.edit_clean: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0.0, 10.0, 1.2, 288, "设置清洗3体积");
                    }
                    break;
                    case R.id.edit_toc_k: {
                        if (editTextInputStatus((EditText) v, 0.0, 10.0)) {
                            updateConfigData(mCompName, "ConversionFactorK", String.format("%.2f", Double.parseDouble(mEditTocK.getText().toString())));
                        } else {
                            updateConfigData(mCompName, "ConversionFactorK", "1");
                            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + 0.0 + " ~ " + 10.0 + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                        }
                        mEditTocK.setText(getConfigData(mCompName, "ConversionFactorK"));
                    }
                    break;
                    case R.id.edit_toc_b: {
                        if (editTextInputStatus((EditText) v, -99.0, 99.0)) {
                            updateConfigData(mCompName, "ConversionFactorB", String.format("%.2f", Double.parseDouble(mEditTocB.getText().toString())));
                        } else {
                            updateConfigData(mCompName, "ConversionFactorB", "0");
                            Toast.makeText(context, context.getResources().getString(R.string.please_confirm_the_range_input_par) + " ( " + -99.0 + " ~ " + 99.0 + " ) " + context.getResources().getString(R.string.change_to_default), Toast.LENGTH_SHORT).show();
                        }
                        mEditTocB.setText(getConfigData(mCompName, "ConversionFactorB"));
                    }
                    break;
                    case R.id.edit_clcs: {
                        setCKAEditIntData4(mCompName, (EditText) v, iPoint, 1, 5, 1, 316, "设置测量次数");
                    }
                    break;
                    case R.id.edit_tcll: {
                        setCKAEditIntData4(mCompName, (EditText) v, iPoint, 0, 500, 180, 332, "设置TC流量");
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
            setBsLedStatus(mCompName, false);
            mCallbacks.onListSelected(v);
        }
    }

    @SuppressLint("HandlerLeak")
    public static Handler mTOCcldyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 9502:
                    mEditCLCS.setText(getCmdsValue(mCompName, 9502));
                    //mEditTCLL.setText(getCmdsValue(mCompName, 9503));
                    break;
                case 9510:
                    mEditIcPurgingTime.setText(getCmdsValue(mCompName, 9510));
                    mEditTcPurgingTime.setText(getCmdsValue(mCompName, 9511));
                    mEditCollectTime.setText(getCmdsValue(mCompName, 9512));

                    mEditCleanVolume.setText(getCmdsValue(mCompName, 9513));
                    mEditMixedExtractionVolume.setText(getCmdsValue(mCompName, 9514));
                    break;
            }
        }
    };
}
