package com.yzlm.cyl.cfragment.Content.Component.List2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Adapter.SurplusFileInfo;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_select;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getNumbers;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.lsurPlus;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/**
 * Created by zwj on 2018/3/12.
 */

public class List2_Content6 extends SubFragment {


    static List2_Content6 fragment = null;

    LinearLayout layoutSurPlusAlarm;
    LinearLayout layoutSurPlus1;
    LinearLayout layoutSurPlus2;
    LinearLayout layoutSurPlus3;
    LinearLayout layoutSurPlus4;
    LinearLayout layoutSurPlus5;
    LinearLayout layoutSurPlus6;
    LinearLayout layoutSurPlus7;
    private ToggleButton togBtnSurplus;
    private TextView txtReagent1Name;
    private TextView txtReagent2Name;
    private TextView txtReagent3Name;
    private TextView txtReagent4Name;
    private TextView txtReagent5Name;
    private TextView txtReagent6Name;
    private TextView txtReagent7Name;
    private EditText edFullBottle1;
    private EditText edFullBottle2;
    private EditText edFullBottle3;
    private EditText edFullBottle4;
    private EditText edFullBottle5;
    private EditText edFullBottle6;
    private EditText edFullBottle7;
    private EditText edSurplus1;
    private EditText edSurplus2;
    private EditText edSurplus3;
    private EditText edSurplus4;
    private EditText edSurplus5;
    private EditText edSurplus6;
    private EditText edSurplus7;
    private EditText edSurplusAlarm;
    private EditText edSurplusAlarm2;
    private Button mbtnFullBottle;
    private Button mbtnEmptyWasteLiquid;

    private TextView txtFYBJ;

    private static Callbacks mCallbacks;


    public static List2_Content6 newInstance() {
        if (fragment == null) {
            fragment = new List2_Content6();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list2_content6;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            intWidget();
            layoutSurPlus1.setVisibility(View.GONE);
            layoutSurPlus2.setVisibility(View.GONE);
            layoutSurPlus3.setVisibility(View.GONE);
            layoutSurPlus4.setVisibility(View.GONE);
            layoutSurPlus5.setVisibility(View.GONE);
            layoutSurPlus6.setVisibility(View.GONE);
            LinearLayout layout1 = (LinearLayout) v.findViewById(R.id.layout_surplus);
            layout1.setVisibility(View.VISIBLE);
            LinearLayout layout2 = (LinearLayout) v.findViewById(R.id.layout_surplusTitle);
            layout2.setVisibility(View.VISIBLE);

            txtFYBJ.setVisibility(View.GONE);
            edSurplusAlarm2.setVisibility(View.GONE);
            mbtnEmptyWasteLiquid.setVisibility(View.GONE);

            int iSize = lsurPlus.get(mCompName).getSurplusInfoMap().size();
            SurplusFileInfo surplusFileInfo = lsurPlus.get(mCompName).getSurplusInfoMap().get(String.valueOf(iSize));
            if (surplusFileInfo.getReagent().equals("废液")) {
                layoutSurPlus7.setVisibility(View.VISIBLE);
                txtReagent7Name.setText(surplusFileInfo.getReagent());
                edFullBottle7.setText(String.valueOf(getConfigData(mCompName, "fullBottle7")));
                edSurplus7.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus7"))));
                txtFYBJ.setVisibility(View.VISIBLE);
                edSurplusAlarm2.setVisibility(View.VISIBLE);
                mbtnEmptyWasteLiquid.setVisibility(View.VISIBLE);

                iSize -= 1;
            }

            try {
                if (lsurPlus.get(mCompName) != null) {

                    switch (iSize) {
                        case 7:
                            /*layoutSurPlus7.setVisibility(View.VISIBLE);
                            SurplusFileInfo surplusFileInfo7 = lsurPlus.get(mCompName).getSurplusInfoMap().get("7");
                            txtReagent7Name.setText(surplusFileInfo7.getReagent());
                            edFullBottle7.setText(String.valueOf(getConfigData(mCompName, "fullBottle7")));
                            edSurplus7.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus7"))));
                            txtFYBJ.setVisibility(View.VISIBLE);
                            edSurplusAlarm2.setVisibility(View.VISIBLE);
                            mbtnEmptyWasteLiquid.setVisibility(View.VISIBLE);*/
                        case 6:
                            layoutSurPlus6.setVisibility(View.VISIBLE);
                            SurplusFileInfo surplusFileInfo6 = lsurPlus.get(mCompName).getSurplusInfoMap().get("6");
                            txtReagent6Name.setText(surplusFileInfo6.getReagent());
                            edFullBottle6.setText(String.valueOf(getConfigData(mCompName, "fullBottle6")));
                            edSurplus6.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus6"))));
                        case 5:
                            layoutSurPlus5.setVisibility(View.VISIBLE);
                            SurplusFileInfo surplusFileInfo5 = lsurPlus.get(mCompName).getSurplusInfoMap().get("5");
                            txtReagent5Name.setText(surplusFileInfo5.getReagent());
                            edFullBottle5.setText(String.valueOf(getConfigData(mCompName, "fullBottle5")));
                            edSurplus5.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus5"))));
                        case 4:
                            layoutSurPlus4.setVisibility(View.VISIBLE);
                            SurplusFileInfo surplusFileInfo4 = lsurPlus.get(mCompName).getSurplusInfoMap().get("4");
                            txtReagent4Name.setText(surplusFileInfo4.getReagent());
                            edFullBottle4.setText(String.valueOf(getConfigData(mCompName, "fullBottle4")));
                            edSurplus4.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus4"))));
                        case 3:
                            layoutSurPlus3.setVisibility(View.VISIBLE);
                            SurplusFileInfo surplusFileInfo3 = lsurPlus.get(mCompName).getSurplusInfoMap().get("3");
                            txtReagent3Name.setText(surplusFileInfo3.getReagent());
                            edFullBottle3.setText(String.valueOf(getConfigData(mCompName, "fullBottle3")));
                            edSurplus3.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus3"))));
                        case 2:
                            SurplusFileInfo surplusFileInfo2 = lsurPlus.get(mCompName).getSurplusInfoMap().get("2");
                            layoutSurPlus2.setVisibility(View.VISIBLE);
                            txtReagent2Name.setText(surplusFileInfo2.getReagent());
                            edFullBottle2.setText(String.valueOf(getConfigData(mCompName, "fullBottle2")));
                            edSurplus2.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus2"))));
                        case 1:
                            layoutSurPlus1.setVisibility(View.VISIBLE);
                            SurplusFileInfo surplusFileInfo1 = lsurPlus.get(mCompName).getSurplusInfoMap().get("1");
                            txtReagent1Name.setText(surplusFileInfo1.getReagent());
                            edFullBottle1.setText(String.valueOf(getConfigData(mCompName, "fullBottle1")));
                            edSurplus1.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus1"))));
                            break;
                    }
                } else {
                    layout1.setVisibility(View.INVISIBLE);
                    layout2.setVisibility(View.INVISIBLE);
                    if (getConfigData(mCompName, "surplusOpen").equals("true")) {
                        updateConfigData(mCompName, "surplusOpen", "false");
                    }
                }
            } catch (Exception e) {

            }
            edSurplusAlarm.setText(getConfigData(mCompName, "surplusAlarm"));
            edSurplusAlarm2.setText(getConfigData(mCompName, "surplusAlarm2"));
            togBtnSurplus.setChecked(getConfigData(mCompName, "surplusOpen").equals("true"));
            togBtnSurplus.setOnCheckedChangeListener(new mToggleBtnClick());

            if (doFlowing.get(MainActivity.mCompName).equals(context.getResources().getString(R.string.waiting_for_instructions)/*"等待指令"*/) && !getPublicConfigData("LogInName").equals("0")) {
                widgetTouch(true);
            } else {
                widgetTouch(false);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + mCompName + "]" + e.toString());
        }

    }

    private void widgetTouch(boolean enable) {
        togBtnSurplus.setEnabled(enable);

        edFullBottle1.setEnabled(enable);
        edFullBottle2.setEnabled(enable);
        edFullBottle3.setEnabled(enable);
        edFullBottle4.setEnabled(enable);
        edFullBottle5.setEnabled(enable);
        edFullBottle6.setEnabled(enable);
        edFullBottle7.setEnabled(enable);

        edSurplus1.setEnabled(enable);
        edSurplus2.setEnabled(enable);
        edSurplus3.setEnabled(enable);
        edSurplus4.setEnabled(enable);
        edSurplus5.setEnabled(enable);
        edSurplus6.setEnabled(enable);
        edSurplus7.setEnabled(enable);

        edSurplusAlarm.setEnabled(enable);
        edSurplusAlarm2.setEnabled(enable);
        mbtnFullBottle.setEnabled(enable);
        mbtnEmptyWasteLiquid.setEnabled(enable);
    }

    private void intWidget() {
        togBtnSurplus = (ToggleButton) v.findViewById(R.id.tBtnSurplus);

        layoutSurPlus1 = (LinearLayout) v.findViewById(R.id.layout_yl1);
        layoutSurPlus2 = (LinearLayout) v.findViewById(R.id.layout_yl2);
        layoutSurPlus3 = (LinearLayout) v.findViewById(R.id.layout_yl3);
        layoutSurPlus4 = (LinearLayout) v.findViewById(R.id.layout_yl4);
        layoutSurPlus5 = (LinearLayout) v.findViewById(R.id.layout_yl5);
        layoutSurPlus6 = (LinearLayout) v.findViewById(R.id.layout_yl6);
        layoutSurPlus7 = (LinearLayout) v.findViewById(R.id.layout_yl7);

        txtReagent1Name = (TextView) v.findViewById(R.id.txtyl1name);
        txtReagent2Name = (TextView) v.findViewById(R.id.txtyl2name);
        txtReagent3Name = (TextView) v.findViewById(R.id.txtyl3name);
        txtReagent4Name = (TextView) v.findViewById(R.id.txtyl4name);
        txtReagent5Name = (TextView) v.findViewById(R.id.txtyl5name);
        txtReagent6Name = (TextView) v.findViewById(R.id.txtyl6name);
        txtReagent7Name = (TextView) v.findViewById(R.id.txtyl7name);

        txtFYBJ = (TextView) v.findViewById(R.id.txt_fybj);

        edFullBottle1 = (EditText) v.findViewById(R.id.edityl1Bottle);
        edFullBottle1.setOnFocusChangeListener(new editFocusChangeComp());
        edFullBottle1.setOnEditorActionListener(new editFocusChangeComp());
        edFullBottle2 = (EditText) v.findViewById(R.id.edityl2Bottle);
        edFullBottle2.setOnFocusChangeListener(new editFocusChangeComp());
        edFullBottle2.setOnEditorActionListener(new editFocusChangeComp());
        edFullBottle3 = (EditText) v.findViewById(R.id.edityl3Bottle);
        edFullBottle3.setOnFocusChangeListener(new editFocusChangeComp());
        edFullBottle3.setOnEditorActionListener(new editFocusChangeComp());
        edFullBottle4 = (EditText) v.findViewById(R.id.edityl4Bottle);
        edFullBottle4.setOnFocusChangeListener(new editFocusChangeComp());
        edFullBottle4.setOnEditorActionListener(new editFocusChangeComp());
        edFullBottle5 = (EditText) v.findViewById(R.id.edityl5Bottle);
        edFullBottle5.setOnFocusChangeListener(new editFocusChangeComp());
        edFullBottle5.setOnEditorActionListener(new editFocusChangeComp());
        edFullBottle6 = (EditText) v.findViewById(R.id.edityl6Bottle);
        edFullBottle6.setOnFocusChangeListener(new editFocusChangeComp());
        edFullBottle6.setOnEditorActionListener(new editFocusChangeComp());
        edFullBottle7 = (EditText) v.findViewById(R.id.edityl7Bottle);
        edFullBottle7.setOnFocusChangeListener(new editFocusChangeComp());
        edFullBottle7.setOnEditorActionListener(new editFocusChangeComp());

        edSurplus1 = (EditText) v.findViewById(R.id.edityl1surplus);
        edSurplus1.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplus1.setOnEditorActionListener(new editFocusChangeComp());
        edSurplus2 = (EditText) v.findViewById(R.id.edityl2surplus);
        edSurplus2.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplus2.setOnEditorActionListener(new editFocusChangeComp());
        edSurplus3 = (EditText) v.findViewById(R.id.edityl3surplus);
        edSurplus3.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplus3.setOnEditorActionListener(new editFocusChangeComp());
        edSurplus4 = (EditText) v.findViewById(R.id.edityl4surplus);
        edSurplus4.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplus4.setOnEditorActionListener(new editFocusChangeComp());
        edSurplus5 = (EditText) v.findViewById(R.id.edityl5surplus);
        edSurplus5.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplus5.setOnEditorActionListener(new editFocusChangeComp());
        edSurplus6 = (EditText) v.findViewById(R.id.edityl6surplus);
        edSurplus6.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplus6.setOnEditorActionListener(new editFocusChangeComp());
        edSurplus7 = (EditText) v.findViewById(R.id.edityl7surplus);
        edSurplus7.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplus7.setOnEditorActionListener(new editFocusChangeComp());

        edSurplusAlarm = (EditText) v.findViewById(R.id.editylsurplusAlarm);
        edSurplusAlarm.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplusAlarm.setOnEditorActionListener(new editFocusChangeComp());
        edSurplusAlarm2 = (EditText) v.findViewById(R.id.editylsurplusAlarm2);
        edSurplusAlarm2.setOnFocusChangeListener(new editFocusChangeComp());
        edSurplusAlarm2.setOnEditorActionListener(new editFocusChangeComp());
        togBtnSurplus = (ToggleButton) v.findViewById(R.id.tBtnSurplus);
        mbtnFullBottle = (Button) v.findViewById(R.id.btnFullBottle);
        mbtnFullBottle.setOnClickListener(new BtnOnClickListener());
        mbtnEmptyWasteLiquid = (Button) v.findViewById(R.id.btnEmptyWasteLiquid);
        mbtnEmptyWasteLiquid.setOnClickListener(new BtnOnClickListener());

        layoutSurPlusAlarm = (LinearLayout) v.findViewById(R.id.layout_surplus);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public interface Callbacks {

        void onDialogRS();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()) {
                    case R.id.btnFullBottle: {
                        main.removeDestopText(mfb);
                        Dialog_select dialog_select = new Dialog_select(mCompName);
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-select", context.getResources().getString(R.string.make_sure_the_reagent_bottle));
                        dialog_select.setArguments(bundle);
                        dialog_select.setTargetFragment(List2_Content6.this, 1);
                        dialog_select.show(fm, "Dialog_select");
                    }
                    break;
                    case R.id.btnEmptyWasteLiquid: {
                        main.removeDestopText(mfb);
                        Dialog_select dialog_select = new Dialog_select(mCompName);
                        Bundle bundle = new Bundle();
                        bundle.putString("alert-select", context.getResources().getString(R.string.make_sure_the_container_empty));
                        dialog_select.setArguments(bundle);
                        dialog_select.setTargetFragment(List2_Content6.this, 1);
                        dialog_select.show(fm, "Dialog_select");
                    }
                    break;
                }
            } catch (Exception e) {

            }
        }
    }


    private class mToggleBtnClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (lsurPlus.get(mCompName) != null) {
                if (lsurPlus.get(mCompName).getSurplusInfoMap().size() > 0) {
                    saveOperationLogDataModifyMsg(mCompName, "surplusOpen", (togBtnSurplus.isChecked() ? "true" : "false"), "余量开关", ErrorLog.msgType.操作_信息);

                    updateConfigData(mCompName, "surplusOpen", togBtnSurplus.isChecked() ? "true" : "false");
                }
            } else {
                togBtnSurplus.setChecked(false);
                layoutSurPlusAlarm.setVisibility(View.INVISIBLE);

            }
        }
    }

    private class editFocusChangeComp implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                try {
                    switch (v.getId()) {
                        case R.id.edityl1surplus:
                            if (!edSurplus1.getText().toString().equals("")) {
                                if ((Float.parseFloat(edSurplus1.getText().toString()) >= 0)
                                        && Float.parseFloat(edSurplus1.getText().toString()) <= 100) {
                                    updateConfigData(mCompName, "surplus1", FloatStringFormatInput(edSurplus1.getText().toString(), 1));
                                    edSurplus1.setText(FloatStringFormatInput(edSurplus1.getText().toString(), 1));
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edSurplus1.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus1"))));
                            break;
                        case R.id.edityl2surplus:
                            if (!edSurplus2.getText().toString().equals("")) {
                                if ((Float.parseFloat(edSurplus2.getText().toString()) >= 0)
                                        && Float.parseFloat(edSurplus2.getText().toString()) <= 100) {
                                    updateConfigData(mCompName, "surplus2", FloatStringFormatInput(edSurplus2.getText().toString(), 1));
                                    edSurplus2.setText(FloatStringFormatInput(edSurplus2.getText().toString(), 1));
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edSurplus2.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus2"))));
                            break;
                        case R.id.edityl3surplus:
                            if (!edSurplus3.getText().toString().equals("") &&
                                    (Float.parseFloat(edSurplus3.getText().toString()) >= 0)
                                    && Float.parseFloat(edSurplus3.getText().toString()) <= 100) {
                                updateConfigData(mCompName, "surplus3", FloatStringFormatInput(edSurplus3.getText().toString(), 1));
                                edSurplus3.setText(FloatStringFormatInput(edSurplus3.getText().toString(), 1));
                                break;
                            } else {
                                Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                                edSurplus3.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus3"))));
                            }
                            break;
                        case R.id.edityl4surplus:
                            if (!edSurplus4.getText().toString().equals("")) {
                                if ((Float.parseFloat(edSurplus4.getText().toString()) >= 0)
                                        && Float.parseFloat(edSurplus4.getText().toString()) <= 100) {
                                    updateConfigData(mCompName, "surplus4", FloatStringFormatInput(edSurplus4.getText().toString(), 1));
                                    edSurplus4.setText(FloatStringFormatInput(edSurplus4.getText().toString(), 1));
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edSurplus4.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus4"))));
                            break;
                        case R.id.edityl5surplus:
                            if (!edSurplus5.getText().toString().equals("")) {
                                if ((Float.parseFloat(edSurplus5.getText().toString()) >= 0)
                                        && Float.parseFloat(edSurplus5.getText().toString()) <= 100) {
                                    updateConfigData(mCompName, "surplus5", FloatStringFormatInput(edSurplus5.getText().toString(), 1));
                                    edSurplus5.setText(FloatStringFormatInput(edSurplus5.getText().toString(), 1));
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edSurplus5.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus5"))));
                            break;
                        case R.id.edityl6surplus:
                            if (!edSurplus6.getText().toString().equals("")) {
                                if ((Float.parseFloat(edSurplus6.getText().toString()) >= 0)
                                        && Float.parseFloat(edSurplus6.getText().toString()) <= 100) {
                                    updateConfigData(mCompName, "surplus6", FloatStringFormatInput(edSurplus6.getText().toString(), 1));
                                    edSurplus6.setText(FloatStringFormatInput(edSurplus6.getText().toString(), 1));
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edSurplus6.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus6"))));
                            break;
                        case R.id.edityl7surplus:
                            if (!edSurplus7.getText().toString().equals("")) {
                                if ((Float.parseFloat(edSurplus7.getText().toString()) >= 0)
                                        && Float.parseFloat(edSurplus7.getText().toString()) <= 100) {
                                    updateConfigData(mCompName, "surplus7", FloatStringFormatInput(edSurplus7.getText().toString(), 1));
                                    edSurplus7.setText(FloatStringFormatInput(edSurplus7.getText().toString(), 1));
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edSurplus7.setText(String.format("%.1f", Float.parseFloat(getConfigData(mCompName, "surplus7"))));
                            break;
                        case R.id.edityl1Bottle:
                            if (!edFullBottle1.getText().toString().equals("")) {
                                if ((Integer.parseInt(edFullBottle1.getText().toString()) >= 0)
                                        && Integer.parseInt(edFullBottle1.getText().toString()) <= 20000) {
                                    edFullBottle1.setText(FloatStringFormatInput(edFullBottle1.getText().toString(), 0));
                                    updateConfigData(mCompName, "fullBottle1", edFullBottle1.getText().toString());

                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edFullBottle1.setText(getConfigData(mCompName, "fullBottle1"));
                            break;
                        case R.id.edityl2Bottle:
                            if (!edFullBottle2.getText().toString().equals("")) {
                                if ((Integer.parseInt(edFullBottle2.getText().toString()) >= 0)
                                        && Integer.parseInt(edFullBottle2.getText().toString()) <= 20000) {
                                    edFullBottle2.setText(FloatStringFormatInput(edFullBottle2.getText().toString(), 0));
                                    updateConfigData(mCompName, "fullBottle2", edFullBottle2.getText().toString());
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edFullBottle2.setText(getConfigData(mCompName, "fullBottle2"));
                            break;
                        case R.id.edityl3Bottle:
                            if (!edFullBottle3.getText().toString().equals("")) {
                                if ((Integer.parseInt(edFullBottle3.getText().toString()) >= 0)
                                        && Integer.parseInt(edFullBottle3.getText().toString()) <= 20000) {
                                    edFullBottle3.setText(FloatStringFormatInput(edFullBottle3.getText().toString(), 0));
                                    updateConfigData(mCompName, "fullBottle3", edFullBottle3.getText().toString());
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edFullBottle3.setText(getConfigData(mCompName, "fullBottle3"));
                            break;
                        case R.id.edityl4Bottle:
                            if (!edFullBottle4.getText().toString().equals("")) {
                                if ((Integer.parseInt(edFullBottle4.getText().toString()) >= 0)
                                        && Integer.parseInt(edFullBottle4.getText().toString()) <= 20000) {
                                    edFullBottle4.setText(FloatStringFormatInput(edFullBottle4.getText().toString(), 0));
                                    updateConfigData(mCompName, "fullBottle4", edFullBottle4.getText().toString());
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edFullBottle4.setText(getConfigData(mCompName, "fullBottle4"));
                            break;
                        case R.id.edityl5Bottle:
                            if (!edFullBottle5.getText().toString().equals("")) {
                                if ((Integer.parseInt(edFullBottle5.getText().toString()) >= 0)
                                        && Integer.parseInt(edFullBottle5.getText().toString()) <= 20000) {
                                    edFullBottle5.setText(FloatStringFormatInput(edFullBottle5.getText().toString(), 0));
                                    updateConfigData(mCompName, "fullBottle5", edFullBottle5.getText().toString());
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edFullBottle5.setText(getConfigData(mCompName, "fullBottle5"));
                            break;
                        case R.id.edityl6Bottle:
                            if (!edFullBottle6.getText().toString().equals("")) {
                                if ((Integer.parseInt(edFullBottle6.getText().toString()) >= 0)
                                        && Integer.parseInt(edFullBottle6.getText().toString()) <= 20000) {
                                    edFullBottle6.setText(FloatStringFormatInput(edFullBottle6.getText().toString(), 0));
                                    updateConfigData(mCompName, "fullBottle6", edFullBottle6.getText().toString());
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edFullBottle6.setText(getConfigData(mCompName, "fullBottle6"));
                            break;
                        case R.id.edityl7Bottle:
                            if (!edFullBottle7.getText().toString().equals("")) {
                                if ((Integer.parseInt(edFullBottle7.getText().toString()) >= 0)
                                        && Integer.parseInt(edFullBottle7.getText().toString()) <= 20000) {
                                    edFullBottle7.setText(FloatStringFormatInput(edFullBottle7.getText().toString(), 0));
                                    updateConfigData(mCompName, "fullBottle7", edFullBottle7.getText().toString());
                                    break;
                                }
                            }
                            Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                            edFullBottle7.setText(getConfigData(mCompName, "fullBottle7"));
                            break;
                        case R.id.editylsurplusAlarm:
                            if (!edSurplusAlarm.getText().toString().equals("") &&
                                    (Float.parseFloat(edSurplusAlarm.getText().toString()) >= 0)
                                    && Float.parseFloat(edSurplusAlarm.getText().toString()) <= 100) {
                                edSurplusAlarm.setText(FloatStringFormatInput(edSurplusAlarm.getText().toString(), 1));
                                updateConfigData(mCompName, "surplusAlarm", edSurplusAlarm.getText().toString());

                                break;
                            } else {
                                Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                                edSurplusAlarm.setText(FloatStringFormatInput(getConfigData(mCompName, "surplusAlarm"), 1));
                            }
                            break;
                        case R.id.editylsurplusAlarm2:
                            if (!edSurplusAlarm2.getText().toString().equals("") &&
                                    (Float.parseFloat(edSurplusAlarm2.getText().toString()) >= 0)
                                    && Float.parseFloat(edSurplusAlarm2.getText().toString()) <= 100) {
                                edSurplusAlarm2.setText(FloatStringFormatInput(edSurplusAlarm2.getText().toString(), 1));
                                updateConfigData(mCompName, "surplusAlarm2", edSurplusAlarm2.getText().toString());

                                break;
                            } else {
                                Toast.makeText(context, getString(R.string.inputParException), Toast.LENGTH_SHORT).show();
                                edSurplusAlarm2.setText(FloatStringFormatInput(getConfigData(mCompName, "surplusAlarm2"), 1));
                            }
                            break;
                    }
                } catch (Exception e) {

                }
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

    public static String FloatStringFormatInput(String fData, int point) {
        try {
            return String.format("%." + point + "f", Float.parseFloat(fData));
        } catch (Exception e) {

        }
        return "";
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("Dialog_Command");
            if (stringExtra.contains(context.getResources().getString(R.string.make_sure_the_reagent_bottle))) {
                resetSurplusBottle(mCompName);
                edSurplus1.setText("100.0");
                edSurplus2.setText("100.0");
                edSurplus3.setText("100.0");
                edSurplus4.setText("100.0");
                edSurplus5.setText("100.0");
                edSurplus6.setText("100.0");
            } else {
                int iSize = lsurPlus.get(mCompName).getSurplusInfoMap().size();
                SurplusFileInfo surplusFileInfo7 = lsurPlus.get(mCompName).getSurplusInfoMap().get(String.valueOf(iSize));
                if (surplusFileInfo7.getReagent().equals("废液")) {
                    edFullBottle7.setText(String.valueOf(surplusFileInfo7.getFullBottle()));
                    updateConfigData(mCompName, "fullBottle7", String.valueOf(surplusFileInfo7.getFullBottle()));
                    updateConfigData(mCompName, "surplus7", "0.0");
                    edSurplus7.setText("0.0");
                }
                /*SurplusFileInfo surplusFileInfo7 = lsurPlus.get(mCompName).getSurplusInfoMap().get("7");
                if (surplusFileInfo7 != null) {
                    edFullBottle7.setText(String.valueOf(surplusFileInfo7.getFullBottle()));
                    updateConfigData(mCompName, "fullBottle7", String.valueOf(surplusFileInfo7.getFullBottle()));
                    updateConfigData(mCompName, "surplus7", "0.0");
                    edSurplus7.setText("0.0");
                }*/
            }
            mCallbacks.onDialogRS();
        } else if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }

    /*重置试剂余量**/
    public static void resetSurplusBottle(String compName) {
        //if (getConfigData(compName, "fullBottle1").equals("0")) {
        int iSize = lsurPlus.get(compName).getSurplusInfoMap().size();
        SurplusFileInfo surplusFileInfo = lsurPlus.get(compName).getSurplusInfoMap().get(String.valueOf(iSize));
        if (surplusFileInfo.getReagent().equals("废液")) {
            iSize -= 1;
        }
        switch (iSize) {
            case 7:
            case 6:
                SurplusFileInfo surplusFileInfo6 = lsurPlus.get(compName).getSurplusInfoMap().get("6");
                updateConfigData(compName, "fullBottle6", String.valueOf(surplusFileInfo6.getFullBottle()));
            case 5:
                SurplusFileInfo surplusFileInfo5 = lsurPlus.get(compName).getSurplusInfoMap().get("5");
                updateConfigData(compName, "fullBottle5", String.valueOf(surplusFileInfo5.getFullBottle()));
            case 4:
                SurplusFileInfo surplusFileInfo4 = lsurPlus.get(compName).getSurplusInfoMap().get("4");
                updateConfigData(compName, "fullBottle4", String.valueOf(surplusFileInfo4.getFullBottle()));
            case 3:
                SurplusFileInfo surplusFileInfo3 = lsurPlus.get(compName).getSurplusInfoMap().get("3");
                updateConfigData(compName, "fullBottle3", String.valueOf(surplusFileInfo3.getFullBottle()));
            case 2:
                SurplusFileInfo surplusFileInfo2 = lsurPlus.get(compName).getSurplusInfoMap().get("2");
                updateConfigData(compName, "fullBottle2", String.valueOf(surplusFileInfo2.getFullBottle()));
            case 1:
                SurplusFileInfo surplusFileInfo1 = lsurPlus.get(compName).getSurplusInfoMap().get("1");
                updateConfigData(compName, "fullBottle1", String.valueOf(surplusFileInfo1.getFullBottle()));
                break;
        }
        //}
        updateConfigData(compName, "surplus1", "100.0");
        updateConfigData(compName, "surplus2", "100.0");
        updateConfigData(compName, "surplus3", "100.0");
        updateConfigData(compName, "surplus4", "100.0");
        updateConfigData(compName, "surplus5", "100.0");
        updateConfigData(compName, "surplus6", "100.0");
    }

    /*重置废液**/
    public static void ResetWaste(String compName) {
        int iSize = lsurPlus.get(compName).getSurplusInfoMap().size();
        SurplusFileInfo surplusFileInfo7 = lsurPlus.get(compName).getSurplusInfoMap().get(String.valueOf(iSize));
        if (surplusFileInfo7.getReagent().equals("废液")) {
            updateConfigData(compName, "fullBottle7", String.valueOf(surplusFileInfo7.getFullBottle()));
            updateConfigData(compName, "surplus7", "0.0");
        }
        /*SurplusFileInfo surplusFileInfo7 = lsurPlus.get(compName).getSurplusInfoMap().get(String.valueOf(lsurPlus.get(compName).getSurplusInfoMap().size()));
        if (surplusFileInfo7 != null) {
            updateConfigData(compName, "fullBottle7", String.valueOf(surplusFileInfo7.getFullBottle()));
            updateConfigData(compName, "surplus7", "0.0");
        }*/
    }

    /*
     * 测量的试剂消耗量计算
     */
    public static void reagentResidueCalculation(String compName, String flowName, int range) {
        if (getConfigData(compName, "surplusOpen").equals("false")) {
            return;
        }
        if (flowName.equals(context.getResources().getString(R.string.ZY)) && (range > 0 && range < 5)) {
            try {
                float fullBottle1 = Float.parseFloat(getConfigData(compName, "fullBottle1"));
                float fullBottle2 = Float.parseFloat(getConfigData(compName, "fullBottle2"));
                float fullBottle3 = Float.parseFloat(getConfigData(compName, "fullBottle3"));
                float fullBottle4 = Float.parseFloat(getConfigData(compName, "fullBottle4"));
                float fullBottle5 = Float.parseFloat(getConfigData(compName, "fullBottle5"));
                float fullBottle6 = Float.parseFloat(getConfigData(compName, "fullBottle6"));
                float fullBottle7 = Float.parseFloat(getConfigData(compName, "fullBottle7"));

                float surplus1 = Float.parseFloat(getConfigData(compName, "surplus1"));
                float surplus2 = Float.parseFloat(getConfigData(compName, "surplus2"));
                float surplus3 = Float.parseFloat(getConfigData(compName, "surplus3"));
                float surplus4 = Float.parseFloat(getConfigData(compName, "surplus4"));
                float surplus5 = Float.parseFloat(getConfigData(compName, "surplus5"));
                float surplus6 = Float.parseFloat(getConfigData(compName, "surplus6"));
                float surplus7 = Float.parseFloat(getConfigData(compName, "surplus7"));
                SurplusFileInfo surplusFileInfo1, surplusFileInfo2, surplusFileInfo3, surplusFileInfo4, surplusFileInfo5, surplusFileInfo6;
                float reagent1UseOnce, reagent2UseOnce, reagent3UseOnce, reagent4UseOnce, reagent5UseOnce, reagent6UseOnce, reagent7UseOnce;

                int iSize = lsurPlus.get(compName).getSurplusInfoMap().size();
                SurplusFileInfo surplusFileInfo = lsurPlus.get(compName).getSurplusInfoMap().get(String.valueOf(iSize));
                if (surplusFileInfo.getReagent().equals("废液")) {
                    reagent7UseOnce = surplusFileInfo.getDosage(range - 1);
                    surplus7 = ((fullBottle7 * surplus7 / 100) + reagent7UseOnce) / fullBottle7 * 100;
                    if (surplus7 >= 100) {
                        surplus7 = 100;
                    }
                    updateConfigData(compName, "surplus7", String.valueOf(surplus7));
                    if (surplus7 >= Float.parseFloat(getConfigData(compName, "surplusAlarm2"))) {
                        AddError(compName, 562, ErrorLog.msgType.报错_警告);
                    }

                    iSize -= 1;
                }

                switch (iSize) {
                    case 7:

                    case 6:
                        surplusFileInfo6 = lsurPlus.get(compName).getSurplusInfoMap().get("6");
                        reagent6UseOnce = surplusFileInfo6.getDosage(range - 1);
                        surplus6 = ((fullBottle6 * surplus6 / 100) - reagent6UseOnce) / fullBottle6 * 100;
                        if (surplus6 <= 0) {
                            surplus6 = 0;
                        }
                        updateConfigData(compName, "surplus6", String.valueOf(surplus6));
                        if (surplus6 < Float.parseFloat(getConfigData(compName, "surplusAlarm"))) {
                            int num = (Integer.valueOf(getNumbers(surplusFileInfo6.getReagentNum())) + 541);
                            if (num >= 542 && num < 562) {
                                AddError(compName, num, ErrorLog.msgType.运行_信息);
                            }
                        }
                    case 5:
                        surplusFileInfo5 = lsurPlus.get(compName).getSurplusInfoMap().get("5");
                        reagent5UseOnce = surplusFileInfo5.getDosage(range - 1);
                        surplus5 = ((fullBottle5 * surplus5 / 100) - reagent5UseOnce) / fullBottle5 * 100;
                        if (surplus5 <= 0) {
                            surplus5 = 0;
                        }
                        updateConfigData(compName, "surplus5", String.valueOf(surplus5));
                        if (surplus5 < Float.parseFloat(getConfigData(compName, "surplusAlarm"))) {
                            int num = (Integer.valueOf(getNumbers(surplusFileInfo5.getReagentNum())) + 541);
                            if (num >= 542 && num < 562) {
                                AddError(compName, num, ErrorLog.msgType.运行_信息);
                            }
                        }
                    case 4:
                        surplusFileInfo4 = lsurPlus.get(compName).getSurplusInfoMap().get("4");
                        reagent4UseOnce = surplusFileInfo4.getDosage(range - 1);
                        surplus4 = ((fullBottle4 * surplus4 / 100) - reagent4UseOnce) / fullBottle4 * 100;
                        if (surplus4 <= 0) {
                            surplus4 = 0;
                        }
                        updateConfigData(compName, "surplus4", String.valueOf(surplus4));
                        if (surplus4 < Float.parseFloat(getConfigData(compName, "surplusAlarm"))) {
                            int num = (Integer.valueOf(getNumbers(surplusFileInfo4.getReagentNum())) + 541);
                            if (num >= 542 && num < 562) {
                                AddError(compName, num, ErrorLog.msgType.运行_信息);
                            }
                        }
                    case 3:
                        surplusFileInfo3 = lsurPlus.get(compName).getSurplusInfoMap().get("3");
                        reagent3UseOnce = surplusFileInfo3.getDosage(range - 1);
                        surplus3 = ((fullBottle3 * surplus3 / 100) - reagent3UseOnce) / fullBottle3 * 100;
                        if (surplus3 <= 0) {
                            surplus3 = 0;
                        }
                        updateConfigData(compName, "surplus3", String.valueOf(surplus3));
                        if (surplus3 < Float.parseFloat(getConfigData(compName, "surplusAlarm"))) {
                            int num = (Integer.valueOf(getNumbers(surplusFileInfo3.getReagentNum())) + 541);
                            if (num >= 542 && num < 562) {
                                AddError(compName, num, ErrorLog.msgType.运行_信息);
                            }
                        }
                    case 2:
                        surplusFileInfo2 = lsurPlus.get(compName).getSurplusInfoMap().get("2");
                        reagent2UseOnce = surplusFileInfo2.getDosage(range - 1);
                        surplus2 = ((fullBottle2 * surplus2 / 100) - reagent2UseOnce) / fullBottle2 * 100;
                        if (surplus2 <= 0) {
                            surplus2 = 0;
                        }
                        updateConfigData(compName, "surplus2", String.valueOf(surplus2));
                        if (surplus2 < Float.parseFloat(getConfigData(compName, "surplusAlarm"))) {
                            int num = (Integer.valueOf(getNumbers(surplusFileInfo2.getReagentNum())) + 541);
                            if (num >= 542 && num < 562) {
                                AddError(compName, num, ErrorLog.msgType.运行_信息);
                            }
                        }
                    case 1:
                        surplusFileInfo1 = lsurPlus.get(compName).getSurplusInfoMap().get("1");
                        reagent1UseOnce = surplusFileInfo1.getDosage(range - 1);
                        surplus1 = ((fullBottle1 * surplus1 / 100) - reagent1UseOnce) / fullBottle1 * 100;
                        if (surplus1 <= 0) {
                            surplus1 = 0;
                        }
                        updateConfigData(compName, "surplus1", String.valueOf(surplus1));
                        if (surplus1 < Float.parseFloat(getConfigData(compName, "surplusAlarm"))) {
                            int num = (Integer.valueOf(getNumbers(surplusFileInfo1.getReagentNum())) + 541);
                            if (num >= 542 && num < 562) {
                                AddError(compName, num, ErrorLog.msgType.运行_信息);
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                saveExceptInfo2File("组分[" + compName + "] + 请检查余量配置文件!");
            }
        }
    }

    /*获取最小试剂余量的瓶子
     * 1~10
     *
     * */
    public static int minimumReagentBottle(String compName) {

        int iSize = lsurPlus.get(compName).getSurplusInfoMap().size();
        SurplusFileInfo surplusFileInfo = lsurPlus.get(compName).getSurplusInfoMap().get(String.valueOf(iSize));
        if (surplusFileInfo.getReagent().equals("废液")) {
            iSize -= 1;
        }

        int[] surplus = new int[iSize];
        for (int i = 0; i < iSize; i++) {
            surplus[i] = (int) Float.parseFloat(getConfigData(compName, "surplus" + (i + 1)));
        }
        int min = surplus[0];
        int bottleNum = 1;
        for (int i = 0; i < iSize; i++) {
            if (min > surplus[i]) {
                min = surplus[i];
                bottleNum = i + 1;
            }
        }
        return bottleNum;
    }


    /*根据最小剩余量的试剂瓶液体量，
     * 在根据该液体当前量程下试剂的使用量进行计算
     * 剩余做样次数
     *
     * **/
    public static short calSampleTheReagent(String compName, int range) {
        SurplusFileInfo surplusFileInfo;
        float UseOnce;
        short usePlusNum = 255;
        int num;
        if (getConfigData(compName, "surplusOpen").equals("true")) {
            num = minimumReagentBottle(compName);
            if (num != 0) {
                int minSurplus = (int) Float.parseFloat(getConfigData(compName, "surplus" + num));
                int fullBottle = (int) Float.parseFloat(getConfigData(compName, "fullBottle" + num));

                surplusFileInfo = lsurPlus.get(compName).getSurplusInfoMap().get(String.valueOf(num));
                UseOnce = surplusFileInfo.getDosage(range - 1);
                usePlusNum = (short) ((fullBottle * minSurplus / 100) / UseOnce);
            } else {
                usePlusNum = 255;
            }
        }

        return usePlusNum;
    }

}
