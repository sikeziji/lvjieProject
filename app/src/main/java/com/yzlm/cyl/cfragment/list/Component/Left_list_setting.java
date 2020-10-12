package com.yzlm.cyl.cfragment.list.Component;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.strComponent;


/**
 * Created by caoyiliang on 2016/10/28.
 */

public class Left_list_setting extends SubFragment {
    private List<ImageButton> btnList;
    private Callbacks mCallbacks;
    private static Left_list_setting fragment = null;

    public static Left_list_setting newInstance() {
        if (fragment == null) {
            fragment = new Left_list_setting();
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
        return R.layout.left_list_setting_h;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mLeft1 = v.findViewById(R.id.Left_setting1);
        ImageButton mLeft2 = v.findViewById(R.id.Left_setting2);
        ImageButton mLeft3 = v.findViewById(R.id.Left_setting3);
        ImageButton mLeft5 = v.findViewById(R.id.Left_setting5);
        ImageButton mLeftx = v.findViewById(R.id.Left_settingx);
        btnList = new ArrayList<>();
        btnList.add(mLeft1);
        btnList.add(mLeft2);
        btnList.add(mLeft3);

        mLeft5.setVisibility(View.GONE);

        for (ImageButton btn : btnList) {
            btn.setOnClickListener(new mLeftBtnClick());
        }
        selectList(mLeft1);
        mLeftx.setOnLongClickListener(new mLeftxClick());
    }

    private class mLeftBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            selectList(view);
        }
    }

    public void selectFirst() {
        if (btnList != null && btnList.size() > 0) {
            selectList(btnList.get(0));
        }
    }

    private void selectList(View view) {
        /*非等待指令情况下扩展连接不可以进入*/
        if (view.getId() == R.id.Left_setting2) {
            for (int i = 0; i < strComponent.get(1).length; i++) {
                if (!doFlowing.get(strComponent.get(1)[i]).equals(getString(R.string.waiting_for_instructions))) {
                    Toast.makeText(getContext(), getString(R.string.devices_running), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        mCallbacks.onListSelected(view);
        setBackgroundColorById(view.getId());
    }

    private void setBackgroundColorById(int btnId) {
        for (ImageButton btn : btnList) {
            if (btn.getId() == btnId) {
                btn.setBackgroundResource(R.drawable.select_list);
            } else {
                btn.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private class mLeftxClick implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            selectList(v);
            return false;
        }
    }
}
