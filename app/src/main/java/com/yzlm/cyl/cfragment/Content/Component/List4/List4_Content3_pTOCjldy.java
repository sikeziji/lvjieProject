package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.lang.ref.WeakReference;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditFloatData;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.setCKAEditIntData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmdsValue;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List4_Content3_pTOCjldy extends SubFragment {

    private Callbacks mCallbacks;
    private static List4_Content3_pTOCjldy fragment = null;

    private static EditText editFillVolume;
    private static EditText editSingleInVolume;
    private static EditText editInSpaceTimes;
    private static EditText editInNumber;
    private static Spinner spRangeParSelect;
    private int iPoint = 2;


    public static List4_Content3_pTOCjldy newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pTOCjldy();
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
        return R.layout.list4_content3_ptoc_jldy;
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
            // 设计两个量程
            String[] ArrayItems = new String[3];
            for (int i = 0; i < 3; i++) {
                ArrayItems[i] = (getResources().getString(R.string.range) + String.valueOf(i + 1));
            }
            spRangeParSelect = (Spinner) v.findViewById(R.id.spRangeParSelect);
            InitSpinner(context, spRangeParSelect, ArrayItems, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            spRangeParSelect.setSelection(Integer.parseInt(getConfigData(mCompName, "RANGE")) - 1);
            spRangeParSelect.setOnItemSelectedListener(new mSpinnerSelect());
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + "List4_Content3_pTOCjldy" + e.toString());
        }

    }

    private void InitWidget() {
        editFillVolume = v.findViewById(R.id.editFillVolume);
        editFillVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        editFillVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        editSingleInVolume = v.findViewById(R.id.editSingleInVolume);
        editSingleInVolume.setOnFocusChangeListener(new EditChangeListenerHandler());
        editSingleInVolume.setOnEditorActionListener(new EditChangeListenerHandler());

        editInSpaceTimes = v.findViewById(R.id.editInSpaceTimes);
        editInSpaceTimes.setOnFocusChangeListener(new EditChangeListenerHandler());
        editInSpaceTimes.setOnEditorActionListener(new EditChangeListenerHandler());

        editInNumber = v.findViewById(R.id.editInNumber);
        editInNumber.setOnFocusChangeListener(new EditChangeListenerHandler());
        editInNumber.setOnEditorActionListener(new EditChangeListenerHandler());
    }

    private class EditChangeListenerHandler implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.editFillVolume: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 10.0, Float.parseFloat(getParDefaultValue(String.valueOf(spRangeParSelect.getSelectedItemPosition() + 1), "填充体积")), 292 + (spRangeParSelect.getSelectedItemPosition() * 10), "设置填充体积" + "量程" + (spRangeParSelect.getSelectedItemPosition() + 1));
                    }
                    break;
                    case R.id.editSingleInVolume: {
                        setCKAEditFloatData(mCompName, (EditText) v, iPoint, 0, 10.0, Float.parseFloat(getParDefaultValue(String.valueOf(spRangeParSelect.getSelectedItemPosition() + 1), "单次进样体积")), 293 + (spRangeParSelect.getSelectedItemPosition() * 10), "设置单次进样体积" + "量程" + (spRangeParSelect.getSelectedItemPosition() + 1));
                    }
                    break;
                    case R.id.editInSpaceTimes: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 10, Integer.parseInt(getParDefaultValue(String.valueOf(spRangeParSelect.getSelectedItemPosition() + 1), "进样间隔时间")), 290 + (spRangeParSelect.getSelectedItemPosition() * 10), "设置进样间隔时间" + "量程" + (spRangeParSelect.getSelectedItemPosition() + 1));
                    }
                    break;
                    case R.id.editInNumber: {
                        setCKAEditIntData(mCompName, (EditText) v, iPoint, 0, 10, Integer.parseInt(getParDefaultValue(String.valueOf(spRangeParSelect.getSelectedItemPosition() + 1), "进样次数")), 291 + (spRangeParSelect.getSelectedItemPosition() * 10), "设置进样次数" + "量程" + (spRangeParSelect.getSelectedItemPosition() + 1));
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

    private class mSpinnerSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            byte[] arrayOfByte2 = DataUtil.shortToByte((short) (9530 + (position * 10)));
            DataUtil.reverse(arrayOfByte2);
            SendManager.SendCmd(mCompName + "_查TOC计量单元参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 3, 200, 4);
            FullWindows(mActivityWindow);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
    public static Handler mTOCjldyHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 9530:
                    case 9540:
                    case 9550:
                        Global.activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int position = spRangeParSelect.getSelectedItemPosition();
                                editInSpaceTimes.setText(getCmdsValue(mCompName, (9530 + (position * 10))));
                                editInNumber.setText(getCmdsValue(mCompName, (9531 + (position * 10))));
                                editFillVolume.setText(getCmdsValue(mCompName, 9532 + (position * 10)));
                                editSingleInVolume.setText(getCmdsValue(mCompName, (9533 + (position * 10))));
                            }
                        });
                        break;
                }
            } catch (Exception e) {
                Log.e("mTOCjldyHandler", e.toString());
            }
        }
    };

    public static class TOCJldyHandler2 extends Handler {
        //持有弱引用HandlerActivity,GC回收时会被回收掉.
        private final WeakReference<MainActivity> mActivty;

        public TOCJldyHandler2(MainActivity activity) {
            mActivty = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivty.get();
            super.handleMessage(msg);
            if (activity != null) {
                //执行业务逻辑
                switch (msg.what) {
                    case 9530:
                    case 9540:
                    case 9550:
                        Global.activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int position = spRangeParSelect.getSelectedItemPosition();
                                editInSpaceTimes.setText(getCmdsValue(mCompName, (9530 + (position * 10))));
                                editInNumber.setText(getCmdsValue(mCompName, (9531 + (position * 10))));
                                editFillVolume.setText(getCmdsValue(mCompName, 9532 + (position * 10)));
                                editSingleInVolume.setText(getCmdsValue(mCompName, (9533 + (position * 10))));

                            }
                        });


                        break;
                }
            }
        }
    }


    /**
     * @param sRange  量程
     * @param parName 参数名称
     * @return
     */
    private String getParDefaultValue(String sRange, String parName) {
        String sValue = "";
        switch (sRange) {
            case "1":
                switch (parName) {
                    case "填充体积":
                        sValue = "1";
                        break;
                    case "单次进样体积":
                        sValue = "0.1";
                        break;
                    case "进样间隔时间":
                        sValue = "2";
                        break;
                    case "进样次数":
                        sValue = "3";
                        break;
                }
                break;
            case "2":
                switch (parName) {
                    case "填充体积":
                        sValue = "1";
                        break;
                    case "单次进样体积":
                        sValue = "0.05";
                        break;
                    case "进样间隔时间":
                        sValue = "2";
                        break;
                    case "进样次数":
                        sValue = "1";
                        break;
                }
                break;
            case "3":
                switch (parName) {
                    case "填充体积":
                        sValue = "1";
                        break;
                    case "单次进样体积":
                        sValue = "0.05";
                        break;
                    case "进样间隔时间":
                        sValue = "5";
                        break;
                    case "进样次数":
                        sValue = "1";
                        break;
                }
                break;
        }
        return sValue;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
