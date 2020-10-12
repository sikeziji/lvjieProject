package com.yzlm.cyl.cfragment.Content.Component.List2;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.lComponentVoc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.byte2float;
import static com.yzlm.cyl.clibrary.Util.DataUtil.byteToShort;


/**
 * Created by zwj on 2018/04/18.
 */

public class List2_Content3_2 extends SubFragment {


    private static List2_Content3_2 fragment = null;
    private LinearLayout mLayout_voc1;
    private LinearLayout mLayout_voc2;
    private LinearLayout mLayout_voc3;
    private LinearLayout mLayout_voc4;
    private LinearLayout mLayout_voc5;
    private LinearLayout mLayout_voc6;
    private LinearLayout mLayout_voc7;
    private LinearLayout mLayout_voc8;
    private LinearLayout mLayout_voc9;
    private LinearLayout mLayout_voc10;
    private TextView mTxtV1Name;
    private TextView mTxtV2Name;
    private TextView mTxtV3Name;
    private TextView mTxtV4Name;
    private TextView mTxtV5Name;
    private TextView mTxtV6Name;
    private TextView mTxtV7Name;
    private TextView mTxtV8Name;
    private TextView mTxtV9Name;
    private TextView mTxtV10Name;
    private ToggleButton tBtnV1;
    private ToggleButton tBtnV2;
    private ToggleButton tBtnV3;
    private ToggleButton tBtnV4;
    private ToggleButton tBtnV5;
    private ToggleButton tBtnV6;
    private ToggleButton tBtnV7;
    private ToggleButton tBtnV8;
    private ToggleButton tBtnV9;
    private ToggleButton tBtnV10;


    public static List2_Content3_2 newInstance() {
        if (fragment == null) {
            fragment = new List2_Content3_2();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list2_content3_2;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        try {
            WidgetInit();
            vocComponentCfgShow(mCompName);
            tBtnV1.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV1.setChecked(getConfigData(mCompName, "VOC_V1").equals("true"));

            tBtnV2.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV2.setChecked(getConfigData(mCompName, "VOC_V2").equals("true"));

            tBtnV3.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV3.setChecked(getConfigData(mCompName, "VOC_V3").equals("true"));

            tBtnV4.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV4.setChecked(getConfigData(mCompName, "VOC_V4").equals("true"));

            tBtnV5.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV5.setChecked(getConfigData(mCompName, "VOC_V5").equals("true"));

            tBtnV6.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV6.setChecked(getConfigData(mCompName, "VOC_V6").equals("true"));

            tBtnV7.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV7.setChecked(getConfigData(mCompName, "VOC_V7").equals("true"));

            tBtnV8.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV8.setChecked(getConfigData(mCompName, "VOC_V8").equals("true"));

            tBtnV9.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV9.setChecked(getConfigData(mCompName, "VOC_V9").equals("true"));

            tBtnV10.setOnCheckedChangeListener(new mToggleBtnClick());
            tBtnV10.setChecked(getConfigData(mCompName, "VOC_V10").equals("true"));
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void WidgetInit() {
        mLayout_voc1 = (LinearLayout) v.findViewById(R.id.layout_VOC1);
        mLayout_voc2 = (LinearLayout) v.findViewById(R.id.layout_VOC2);
        mLayout_voc3 = (LinearLayout) v.findViewById(R.id.layout_VOC3);
        mLayout_voc4 = (LinearLayout) v.findViewById(R.id.layout_VOC4);
        mLayout_voc5 = (LinearLayout) v.findViewById(R.id.layout_VOC5);
        mLayout_voc6 = (LinearLayout) v.findViewById(R.id.layout_VOC6);
        mLayout_voc7 = (LinearLayout) v.findViewById(R.id.layout_VOC7);
        mLayout_voc8 = (LinearLayout) v.findViewById(R.id.layout_VOC8);
        mLayout_voc9 = (LinearLayout) v.findViewById(R.id.layout_VOC9);
        mLayout_voc10 = (LinearLayout) v.findViewById(R.id.layout_VOC10);

        mTxtV1Name = (TextView) v.findViewById(R.id.txtV1Name);
        tBtnV1 = (ToggleButton) v.findViewById(R.id.tBtnV1);
        mTxtV2Name = (TextView) v.findViewById(R.id.txtV2Name);
        tBtnV2 = (ToggleButton) v.findViewById(R.id.tBtnV2);
        mTxtV3Name = (TextView) v.findViewById(R.id.txtV3Name);
        tBtnV3 = (ToggleButton) v.findViewById(R.id.tBtnV3);
        mTxtV4Name = (TextView) v.findViewById(R.id.txtV4Name);
        tBtnV4 = (ToggleButton) v.findViewById(R.id.tBtnV4);
        mTxtV5Name = (TextView) v.findViewById(R.id.txtV5Name);
        tBtnV5 = (ToggleButton) v.findViewById(R.id.tBtnV5);
        mTxtV6Name = (TextView) v.findViewById(R.id.txtV6Name);
        tBtnV6 = (ToggleButton) v.findViewById(R.id.tBtnV6);
        mTxtV7Name = (TextView) v.findViewById(R.id.txtV7Name);
        tBtnV7 = (ToggleButton) v.findViewById(R.id.tBtnV7);
        mTxtV8Name = (TextView) v.findViewById(R.id.txtV8Name);
        tBtnV8 = (ToggleButton) v.findViewById(R.id.tBtnV8);
        mTxtV9Name = (TextView) v.findViewById(R.id.txtV9Name);
        tBtnV9 = (ToggleButton) v.findViewById(R.id.tBtnV9);
        mTxtV10Name = (TextView) v.findViewById(R.id.txtV10Name);
        tBtnV10 = (ToggleButton) v.findViewById(R.id.tBtnV10);


    }

    /*
     * 根据配置文件 显示界面
     */
    private void vocComponentCfgShow(String component) {
        try {
            String[] cfg = lComponentVoc.get(component);
            if (cfg.length == 0)
                return;
            switch (cfg.length) {
                case 10:
                    mLayout_voc10.setVisibility(View.VISIBLE);
                case 9:
                    mLayout_voc9.setVisibility(View.VISIBLE);
                case 8:
                    mLayout_voc8.setVisibility(View.VISIBLE);
                case 7:
                    mLayout_voc7.setVisibility(View.VISIBLE);
                case 6:
                    mLayout_voc6.setVisibility(View.VISIBLE);
                case 5:
                    mLayout_voc5.setVisibility(View.VISIBLE);
                case 4:
                    mLayout_voc4.setVisibility(View.VISIBLE);
                case 3:
                    mLayout_voc3.setVisibility(View.VISIBLE);
                case 2:
                    mLayout_voc2.setVisibility(View.VISIBLE);
                case 1:
                    mLayout_voc1.setVisibility(View.VISIBLE);
                    break;
            }
            for (int i = 0; i < cfg.length; i++) {
                switch (i) {
                    case 9:
                        mTxtV10Name.setText(cfg[i]);
                        break;
                    case 8:
                        mTxtV9Name.setText(cfg[i]);
                        break;
                    case 7:
                        mTxtV8Name.setText(cfg[i]);
                        break;
                    case 6:
                        mTxtV7Name.setText(cfg[i]);
                        break;
                    case 5:
                        mTxtV6Name.setText(cfg[i]);
                        break;
                    case 4:
                        mTxtV5Name.setText(cfg[i]);
                        break;
                    case 3:
                        mTxtV4Name.setText(cfg[i]);
                        break;
                    case 2:
                        mTxtV3Name.setText(cfg[i]);
                        break;
                    case 1:
                        mTxtV2Name.setText(cfg[i]);
                        break;
                    case 0:
                        mTxtV1Name.setText(cfg[i]);
                        break;
                }
            }
        } catch (Exception e) {
        }
    }

    private void syncList() {

        closeInputMethod(v);
    }

    private class mToggleBtnClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            try {
                switch (buttonView.getId()) {
                    case R.id.tBtnV1:
                        if (!getConfigData(mCompName, "VOC_V1").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V1", isChecked ? "true" : "false", "选择 VOC_V1" + lComponentVoc.get(mCompName)[0], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V1", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                    case R.id.tBtnV2:
                        if (!getConfigData(mCompName, "VOC_V2").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V2", isChecked ? "true" : "false", "选择  VOC_V2" + lComponentVoc.get(mCompName)[1], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V2", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                    case R.id.tBtnV3:
                        if (!getConfigData(mCompName, "VOC_V3").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V3", isChecked ? "true" : "false", "选择 VOC_V3" + lComponentVoc.get(mCompName)[2], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V3", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                    case R.id.tBtnV4:
                        if (!getConfigData(mCompName, "VOC_V4").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V4", isChecked ? "true" : "false", "选择 VOC_V4" + lComponentVoc.get(mCompName)[3], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V4", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                    case R.id.tBtnV5:
                        if (!getConfigData(mCompName, "VOC_V5").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V5", isChecked ? "true" : "false", "选择 VOC_V5" + lComponentVoc.get(mCompName)[4], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V5", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                    case R.id.tBtnV6:
                        if (!getConfigData(mCompName, "VOC_V6").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V6", isChecked ? "true" : "false", "选择 VOC_V6" + lComponentVoc.get(mCompName)[5], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V6", isChecked ? "true" : "false");
                        }
                        break;
                    case R.id.tBtnV7:
                        if (!getConfigData(mCompName, "VOC_V7").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V7", isChecked ? "true" : "false", "选择 VOC_V7" + lComponentVoc.get(mCompName)[6], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V7", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                    case R.id.tBtnV8:
                        if (!getConfigData(mCompName, "VOC_V8").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V8", isChecked ? "true" : "false", "选择 VOC_V8" + lComponentVoc.get(mCompName)[7], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V8", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                    case R.id.tBtnV9:
                        if (!getConfigData(mCompName, "VOC_V9").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V9", isChecked ? "true" : "false", "选择 VOC_V9" + lComponentVoc.get(mCompName)[8], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V9", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                    case R.id.tBtnV10:
                        if (!getConfigData(mCompName, "VOC_V10").equals(isChecked ? "true" : "false")) {
                            saveOperationLogDataModifyMsg(mCompName, "VOC_V10", isChecked ? "true" : "false", "选择 VOC_V10 " + lComponentVoc.get(mCompName)[9], ErrorLog.msgType.操作_信息);
                            updateConfigData(mCompName, "VOC_V10", isChecked ? "true" : "false");
                            syncList();
                        }
                        break;
                }
            } catch (Exception e) {
            }

        }
    }

    /*
     * 获取VOC的测量组份，
     * 现在最多支持10个
     */
    public static int getVocMeaComponentNum(String compName) {
        int num = 0;
        for (int i = 0; i < 10; i++) {
            if (getConfigData(compName, "VOC_V" + (i + 1)).equals("true")) {
                num++;
            }
        }
        return num;
    }

    /** 获取主控板历史数据光强能量个数
     * @param component 组分名
     * @param Command   接收指令
     * @return  能量个数
     */
    public static int getNoZeroCKAEnergy(String component, byte[] Command) {
        int iEnergySum = 0;
        switch (QueryMeasCateg(component)) {
            case "1":
            case "7":
            case "9":
                for (int i = 0; i < 30; i++) {
                    float temperature = byte2float(Command, (26 + i * 4));
                    int energy = ((int) byteToShort(new byte[]{Command[146 + i * 2], Command[147 + i * 2]})) & '\uffff';
                    if (temperature != 0 || energy != 0) {
                        iEnergySum++;
                    } else {
                        break;
                    }
                }
                break;
            default:
                break;
        }
        Log.i("主控板能量个数", String.valueOf(iEnergySum));
        return iEnergySum;

    }

    /*
     * 获取当前配置的组份的名字
     */
    public static String[] getVocMeaComponent(String compName) {

        String[] str = new String[getVocMeaComponentNum(compName)];
        int index = 0;
        for (int i = 0; i < 10; i++) {
            if (getConfigData(compName, "VOC_V" + (i + 1)).equals("true")) {
                str[index++] = lComponentVoc.get(compName)[i];
            }
        }
        return str;
    }

}
