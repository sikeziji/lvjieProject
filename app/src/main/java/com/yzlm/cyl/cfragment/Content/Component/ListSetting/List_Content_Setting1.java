package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

/**
 * Created by zwj on 2019/03/26.
 */

public class List_Content_Setting1 extends SubFragment {


    private static List_Content_Setting1 fragment = null;
    private Callbacks mCallbacks;

    public static List_Content_Setting1 newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Setting1();
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
        return R.layout.list_content_setting;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        Button btn420Cfg = (Button) v.findViewById(R.id.btn420);
        btn420Cfg.setOnClickListener(new BtnClickListener());
        Button btnUartCfg = (Button) v.findViewById(R.id.btnUartCfg);
        btnUartCfg.setOnClickListener(new BtnClickListener());
        Button btnTcpCfg = (Button) v.findViewById(R.id.btnTcpCfg);
        btnTcpCfg.setOnClickListener(new BtnClickListener());
        Button btnProtocolCfg = (Button) v.findViewById(R.id.btnProtocolCfg);
        btnProtocolCfg.setOnClickListener(new BtnClickListener());

    }

    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

}
