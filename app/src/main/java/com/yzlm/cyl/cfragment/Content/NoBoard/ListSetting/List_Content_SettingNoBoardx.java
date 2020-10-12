package com.yzlm.cyl.cfragment.Content.NoBoard.ListSetting;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Component;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardComponent;
import com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Settingx;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CAdapter.CAdapter;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Control.CSideslipListView;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogMsg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.WinWidgetHandler;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Global.addComponentLockDisplayShowFlag;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/22
 */
public class List_Content_SettingNoBoardx extends SubFragment {

    private static List_Content_SettingNoBoardx fragment = null;

    private EditText ETpoint;
    private EditText ETpointAddr;


    private NoBoardComponent mNoBoardComponent;
    private Component mComponent;


    private List_Content_Settingx.Callbacks mCallbacks;
    private CSideslipListView mSideslipListView;
    private CSideslipListView mSideslipListView1;

    private ArrayList<String> mDataList = new ArrayList<>();
    private ArrayList<String> mDataLists = new ArrayList<>();


    private final int sum = 10;


    public static List_Content_SettingNoBoardx newInstance() {
        if (fragment == null) {
            fragment = new List_Content_SettingNoBoardx();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogSelected(View view, Fragment Fm, String name);

        void onDialogRS();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (List_Content_Settingx.Callbacks) context;
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
        return R.layout.list_content_setting_noboardx;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        mComponent = Component.newInstance(getContext());
        mNoBoardComponent = NoBoardComponent.newInstance(getContext());
        ETpoint = (EditText) v.findViewById(R.id.eTpoint);
        ETpointAddr = (EditText) v.findViewById(R.id.eTpointAddr);
        Button AddPoint = (Button) v.findViewById(R.id.addPoint);
        AddPoint.setOnClickListener(new AddPointClick());

        Button reStart = (Button) v.findViewById(R.id.Btn_reStart);
        reStart.setOnClickListener(new BtnOnClickListener());

        mSideslipListView = (CSideslipListView) v.findViewById(R.id.CsList);
        mSideslipListView1 = (CSideslipListView) v.findViewById(R.id.CsLists);
        UpdateList();

        //设置item点击事件
        mSideslipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSideslipListView.isAllowItemClick()) {
                    try {
                        if ((mComponent.getAll5721Component().get(1).length + mDataLists.size()) > 12) {
                            Toast.makeText(getContext(), getString(R.string.add_comp_to_more), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (mDataLists.size() < sum) {
                            mNoBoardComponent.UpdateZfUsed(mDataList.get(position).split(" / ")[1], true);
                            upDate();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.the_most_select) + sum + getString(R.string.a_component), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mSideslipListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSideslipListView1.isAllowItemClick()) {
                    try {
                        mNoBoardComponent.UpdateZfUsed(mDataLists.get(position).split(" / ")[1], false);
                        upDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Message msg = new Message();
            msg.what = 110;
            WinWidgetHandler.sendMessage(msg);
        }
    }


    private void upDate() {
        try {
            UpdateList();
            addComponentLockDisplayShowFlag = true;
            Toast.makeText(getContext(), getString(R.string.please_click_restart_button), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectPoints() {
        try {
            List<String[]> zf = mNoBoardComponent.getComponent();
            mDataList = new ArrayList<>();
            int i = 0;
            for (String item : zf.get(0)) {
                mDataList.add(zf.get(2)[i++] + " / " + item);
            }
            i = 0;
            mDataLists = new ArrayList<>();
            for (String item : zf.get(1)) {
                mDataLists.add(zf.get(3)[i++] + " / " + item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpdateList() {
        selectPoints();
        mSideslipListView.setAdapter(new CustomAdapter(getContext(), mDataList, mSideslipListView));//设置适配器
        mSideslipListView1.setAdapter(new CustomAdapter1(getContext(), mDataLists, mSideslipListView1));//设置适配器
    }

    private class AddPointClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            /* 获取编辑框组分名，组分地址，组分测量单元类型值（WL）*/
            String pointName = ETpoint.getText().toString();
            String pointMeasCateg = "0";

            FullWindows(mActivityWindow);
            if (mNoBoardComponent.Exist(pointName)) {
                Toast.makeText(getContext(), getString(R.string.component_already_exists), Toast.LENGTH_SHORT).show();
                return;
            }
            if (pointName.equals("")) {
                Toast.makeText(getContext(), getString(R.string.please_sure_par), Toast.LENGTH_SHORT).show();
                return;
            }

            /* 若组分地址，组分测量单元类型值为空，则默认地址为00，*/
            saveNoBoardOperationLogMsg(pointName, "增加组份" + pointName + "_" + 0 + "_" + pointMeasCateg, ErrorLog.msgType.操作_信息);

            pointMeasCateg = String.valueOf(0);
            mNoBoardComponent.AddZf(pointName, String.valueOf(0), pointMeasCateg);
            UpdateList();
            ETpoint.setText("");
            ETpointAddr.setText("");
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            UpdateList();
            mCallbacks.onDialogRS();
        }
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }

    class CustomAdapter extends CAdapter {

        CustomAdapter(Context context, List<String> items, CSideslipListView cSideslipListView) {
            super(context, items, cSideslipListView);
        }

        @Override
        public void Delete(int pos) {
            try {
                Toast.makeText(getContext(), mDataList.get(pos) + getString(R.string.been_deleted),
                        Toast.LENGTH_SHORT).show();
                String component = "";
                if (mDataList.get(pos).split(" / ").length > 1) {
                    component = mDataList.get(pos).split(" / ")[1];
                }
                mNoBoardComponent.DeleteZf(component);
                mDataList.remove(pos);

            } catch (Exception e) {
            }
        }
    }

    class CustomAdapter1 extends CAdapter {

        CustomAdapter1(Context context, List<String> items, CSideslipListView cSideslipListView) {
            super(context, items, cSideslipListView);
        }

        @Override
        public void Delete(int pos) {
            try {
                String component = "";
                if (mDataLists.get(pos).split(" / ").length > 1) {
                    component = mDataLists.get(pos).split(" / ")[1];
                }
                mNoBoardComponent.UpdateZfUsed(component, false);
                UpdateList();
            } catch (Exception e) {
                Log.e("CustomAdapter1", e.toString());
            }
        }
    }
}