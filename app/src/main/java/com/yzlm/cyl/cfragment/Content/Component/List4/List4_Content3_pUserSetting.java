package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Global.strComponent;

/**
 * 创建日期：  2020年5月14日
 *
 * @author zwj
 * @version 1.0
 * 文件名称： 用户参数设置界面
 * 类说明：
 */
public class List4_Content3_pUserSetting extends SubFragment {

    private Callbacks mCallbacks;
    private static List4_Content3_pUserSetting fragment = null;


    public static List4_Content3_pUserSetting newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_pUserSetting();
        }
        return fragment;
    }

    public interface Callbacks {
        void onListSelected(View view);

        void onDialogRS();
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
        return R.layout.list4_content3_puser_setting;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        LinearLayout layLine1 = (LinearLayout)v.findViewById(R.id.lay_line1);
        layLine1.setVisibility(View.GONE);

        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p4);
        mBtnReturn.setOnClickListener(new btnClick());

        ToggleButton tBtnIsShowEn = (ToggleButton) v.findViewById(R.id.tBtnIsShowEn);
        tBtnIsShowEn.setOnCheckedChangeListener(new togBtnClickListener());
        tBtnIsShowEn.setChecked(getPublicConfigData("EnIsShow").equals("true"));

        ToggleButton tBtnIsShowClearNotes = (ToggleButton) v.findViewById(R.id.tBtnIsShowClearNotes);
        tBtnIsShowClearNotes.setOnCheckedChangeListener(new togBtnClickListener());
        tBtnIsShowClearNotes.setChecked(getPublicConfigData("ClearNotesBtnIsShow").equals("true"));

        ToggleButton tBtnIsShowA = v.findViewById(R.id.tBtnIsShowAValue);
        tBtnIsShowA.setOnCheckedChangeListener(new togBtnClickListener());
        tBtnIsShowA.setChecked(getPublicConfigData("AIsShow").equals("true"));

        LinearLayout mLayoutAi = v.findViewById(R.id.layout_IsShowASwitch);
        if (getPublicConfigData("userSettingFlag").equals("true") && (strComponent.get(1).length == 1)) {
            mLayoutAi.setVisibility(View.VISIBLE);
        } else {
            mLayoutAi.setVisibility(View.GONE);
            if (getPublicConfigData("AIsShow").equals("true")) {
                saveOperationLogPublicDataModifyMsg("公共", "AIsShow", ("false"), "吸光度是否显示 ", ErrorLog.msgType.操作_信息);
                updatePublicConfigData("AIsShow", "false");
            }
        }
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private class togBtnClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tBtnIsShowEn: {
                    saveOperationLogPublicDataModifyMsg("公共", "EnIsShow", (isChecked ? "true" : "false"), "光强是否显示 ", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("EnIsShow", (isChecked ? "true" : "false"));
                }
                break;
                case R.id.tBtnIsShowClearNotes: {
                    saveOperationLogPublicDataModifyMsg("公共", "ClearNotesBtnIsShow", (isChecked ? "true" : "false"), "清除记录按钮显示 ", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("ClearNotesBtnIsShow", (isChecked ? "true" : "false"));
                }
                break;
                case R.id.tBtnIsShowAValue: {
                    saveOperationLogPublicDataModifyMsg("公共", "AIsShow", (isChecked ? "true" : "false"), "吸光度是否显示 ", ErrorLog.msgType.操作_信息);
                    updatePublicConfigData("AIsShow", (isChecked ? "true" : "false"));
                }
                break;
            }
            syncList();
        }
    }

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
        } else {

        }
    }


}
