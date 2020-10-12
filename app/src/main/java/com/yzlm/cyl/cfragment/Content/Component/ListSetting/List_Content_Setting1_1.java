package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogPublicDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.updatePublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting1_2.getIOBORD;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.strComponent;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List_Content_Setting1_1 extends SubFragment {

    private static Button mBtnTD1;
    private static Button mBtnTD2;
    private Callbacks mCallbacks;
    private Button mIoBordCfg;

    private static List_Content_Setting1_1 fragment = null;

    public static List_Content_Setting1_1 newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Setting1_1();
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
        return R.layout.list_content_setting1_1;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturn_p3);
        mBtnReturn.setOnClickListener(new BtnClickListener());
        mIoBordCfg = (Button) v.findViewById(R.id.btn_ioBord);
        mIoBordCfg.setOnClickListener(new BtnClickListener());
        int blIoBordEn = getIOBORD();
        String sIoBordCfg = getString(R.string.unConfigured);
        if (blIoBordEn == 0) {
            sIoBordCfg = getString(R.string.unConfigured);
        } else if (blIoBordEn == 1) {
            sIoBordCfg = getString(R.string.configured1);
        } else {
            sIoBordCfg = getString(R.string.configured2);
        }
        mIoBordCfg.setText(sIoBordCfg);
        showIoBord(blIoBordEn);

        try {
            mBtnTD1 = (Button) v.findViewById(R.id.btnTD1);
            if (getPublicConfigData("MNTD1").equals("-1") || Integer.parseInt(getPublicConfigData("MNTD1")) >= strComponent.get(1).length) {
                mBtnTD1.setText(getString(R.string.unConfigured));
            } else {
                mBtnTD1.setText(strComponent.get(1)[Integer.parseInt(getPublicConfigData("MNTD1"))]);
            }
            mBtnTD1.setOnClickListener(new BtnTD1());
            mBtnTD2 = (Button) v.findViewById(R.id.btnTD2);
            if (getPublicConfigData("MNTD2").equals("-1") || Integer.parseInt(getPublicConfigData("MNTD2")) >= strComponent.get(1).length) {
                mBtnTD2.setText(getString(R.string.unConfigured));
            } else {
                mBtnTD2.setText(strComponent.get(1)[Integer.parseInt(getPublicConfigData("MNTD2"))]);
            }
            mBtnTD2.setOnClickListener(new BtnTD2());
        } catch (Exception e) {

        }
    }

    private class BtnTD1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if ((Integer.parseInt(getPublicConfigData("MNTD1")) + 1) >= strComponent.get(1).length) {
                updatePublicConfigData("MNTD1", "-1");
                mBtnTD1.setText(getString(R.string.unConfigured));
            } else {
                saveOperationLogPublicDataModifyMsg("公共", "MNTD1", String.valueOf(Integer.parseInt(getPublicConfigData("MNTD1")) + 1), "模拟通道1", ErrorLog.msgType.操作_信息);
                updatePublicConfigData("MNTD1", String.valueOf(Integer.parseInt(getPublicConfigData("MNTD1")) + 1));
                mBtnTD1.setText(strComponent.get(1)[Integer.parseInt(getPublicConfigData("MNTD1"))]);
            }
        }
    }

    private class BtnTD2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if ((Integer.parseInt(getPublicConfigData("MNTD2")) + 1) >= strComponent.get(1).length) {
                updatePublicConfigData("MNTD2", "-1");
                mBtnTD2.setText(getString(R.string.unConfigured));
            } else {
                saveOperationLogPublicDataModifyMsg("公共", "MNTD2", String.valueOf(Integer.parseInt(getPublicConfigData("MNTD2")) + 1), "模拟通道2", ErrorLog.msgType.操作_信息);
                updatePublicConfigData("MNTD2", String.valueOf(Integer.parseInt(getPublicConfigData("MNTD2")) + 1));
                mBtnTD2.setText(strComponent.get(1)[Integer.parseInt(getPublicConfigData("MNTD2"))]);
            }
        }
    }

    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ioBord:
                    int blIoBordEn = getIOBORD();
                    if (blIoBordEn == 0) {
                        saveOperationLogPublicDataModifyMsg("公共", "IO_BORD", "true", "接口板配置", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("IO_BORD", "1");
                        mIoBordCfg.setText(getString(R.string.configured1));
                        IOBoardUsed = true;
                    } else if (blIoBordEn == 1) {
                        saveOperationLogPublicDataModifyMsg("公共", "IO_BORD", "true", "接口板配置", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("IO_BORD", "2");
                        mIoBordCfg.setText(getString(R.string.configured2));
                        IOBoardUsed = true;
                    } else {
                        saveOperationLogPublicDataModifyMsg("公共", "IO_BORD", "false", "接口板配置", ErrorLog.msgType.操作_信息);
                        updatePublicConfigData("IO_BORD", "0");
                        mIoBordCfg.setText(getString(R.string.unConfigured));
                        IOBoardUsed = false;
                    }
                    showIoBord(2 - blIoBordEn);
                    break;
                case R.id.btnReturn_p3:
                    mCallbacks.onListSelected(v);
                    break;
            }

        }
    }

    private void showIoBord(int blIoBordEn) {

        LinearLayout mLayout_analog = (LinearLayout) v.findViewById(R.id.layout_analog);
        TextView mTxtAnalogName = (TextView) v.findViewById(R.id.txtAnalogName);

        if (blIoBordEn == 0) {
            mTxtAnalogName.setVisibility(View.INVISIBLE);
            mLayout_analog.setVisibility(View.GONE);
        } else {
            mTxtAnalogName.setVisibility(View.VISIBLE);
            mLayout_analog.setVisibility(View.VISIBLE);
        }
    }
}
