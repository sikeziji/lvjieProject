package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Component;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_ok1;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CAdapter.CAdapter;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;
import com.yzlm.cyl.clibrary.Control.CSideslipListView;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.findArrayMex;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.WinWidgetHandler;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.addComponentLockDisplayShowFlag;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List_Content_Settingx extends SubFragment {
    private static List_Content_Settingx fragment = null;
    private EditText ETpoint;
    private EditText ETpointAddr;

    private Spinner spCompBoardType;// 主控板硬件信息：常规(178)\W200
    private Spinner spComponents;
    private Spinner spPointMeasCateg;
    private Component mComponent;
    private Callbacks mCallbacks;
    private CSideslipListView mSideslipListView;
    private CSideslipListView mSideslipListView1;

    private ArrayList<String> mDataList = new ArrayList<>();
    private ArrayList<String> mDataLists = new ArrayList<>();

    private final int sum = 10;


    private String addPointName;
    private String addPointAddr;
    private String addPointMeasCateg;

    public static List_Content_Settingx newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Settingx();
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
        return R.layout.list_content_settingx;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        mComponent = Component.newInstance(getContext());
        ETpoint = v.findViewById(R.id.eTpoint);
        ETpointAddr = v.findViewById(R.id.eTpointAddr);
        Button AddPoint = v.findViewById(R.id.addPoint);
        AddPoint.setOnClickListener(new AddPointClick());

        Button reStart = v.findViewById(R.id.Btn_reStart);
        reStart.setOnClickListener(new BtnOnClickListener());
        spComponents = v.findViewById(R.id.spComponents);

        List<String[]> zf = mComponent.getComponent();
        String[] sComponentZF = new String[zf.get(1).length];
        System.arraycopy(zf.get(1), 0, sComponentZF, 0, zf.get(1).length);
        InitSpinner(context, spComponents, sComponentZF, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        spComponents.setOnItemSelectedListener(new pointMeasCategSelect());

        spPointMeasCateg = v.findViewById(R.id.pointMeasCategory);
        InitSpinner(context, spPointMeasCateg, new String[]{getString(R.string.LED), getString(R.string.SPEC), getString(R.string.titration), "VOC", getString(R.string.dilution), context.getResources().getString(R.string.distill), context.getResources().getString(R.string.multSpec), getString(R.string.injectionPumptitration), getString(R.string.injectionPumpLED),getString(R.string.reserve),getString(R.string.TOC),getString(R.string.RotaryInjectionPumpSPEC),getString(R.string.Biological_toxicity)}, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        spPointMeasCateg.setOnItemSelectedListener(new pointMeasCategSelect());

        mSideslipListView = v.findViewById(R.id.CsList);
        mSideslipListView1 = v.findViewById(R.id.CsLists);
        UpdateList();

        //设置item点击事件
        mSideslipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSideslipListView.isAllowItemClick()) {
                    try {
                        if (mComponent.getAll5721Component().get(1).length > 12) {
                            Toast.makeText(getContext(), getString(R.string.add_comp_to_more), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (mDataLists.size() < sum) {
                            mComponent.UpdateZfUsed(mDataList.get(position).split(" / ")[1], true);
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
                        mComponent.UpdateZfUsed(mDataLists.get(position).split(" / ")[1], false);
                        upDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        spCompBoardType = v.findViewById(R.id.spBoardType);
        InitSpinner(context, spCompBoardType, getResources().getStringArray(R.array.boardType), R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
        spCompBoardType.setOnItemSelectedListener(new pointMeasCategSelect());

    }

    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Message msg = new Message();
            msg.what = 110;
            WinWidgetHandler.sendMessage(msg);
        }
    }


    private class pointMeasCategSelect implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.pointMeasCategory:
                    if ((!spPointMeasCateg.getSelectedItem().toString().equals(getString(R.string.dilution))) &&
                            (!spPointMeasCateg.getSelectedItem().toString().equals(getString(R.string.distill)))) {
                        spComponents.setVisibility(View.GONE);
                        ETpoint.setVisibility(View.VISIBLE);
                    } else {
                        spComponents.setVisibility(View.VISIBLE);
                        ETpoint.setVisibility(View.GONE);
                    }
                    break;
                case R.id.spBoardType:

                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void upDate() {
        try {
            UpdateList();
            //   InitSyncTable();
            addComponentLockDisplayShowFlag = true;
            List<String[]> zf = mComponent.getComponent();
            String[] sComponentZF = new String[zf.get(1).length];
            System.arraycopy(zf.get(1), 0, sComponentZF, 0, zf.get(1).length);
            InitSpinner(context, spComponents, sComponentZF, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
            Toast.makeText(getContext(), getString(R.string.please_click_restart_button), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectPoints() {
        try {
            List<String[]> zf = mComponent.getAll5721Component();
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
            String pointAddr = String.valueOf(getStringComponentAddr());
            String pointMeasCateg = spPointMeasCateg.getSelectedItem().toString();
            String pointBoardType = spCompBoardType.getSelectedItem().toString();
            pointName = addModuleGetName(pointName);
            FullWindows(mActivityWindow);
            if (mComponent.Exist(pointName)) {
                Toast.makeText(getContext(), getString(R.string.component_already_exists), Toast.LENGTH_SHORT).show();
                return;
            }
            if (pointName.equals("") || pointAddr.equals("")) {
                Toast.makeText(getContext(), getString(R.string.please_sure_par), Toast.LENGTH_SHORT).show();
                return;
            }
            List<String[]> zf = mComponent.getComponent();
            for (int i = 0; i < zf.get(2).length; i++) {
                if (Integer.parseInt(pointAddr) == Integer.parseInt(zf.get(2)[i++])) {
                    Toast.makeText(getContext(), getString(R.string.address_already_exists), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            for (int i = 0; i < zf.get(3).length; i++) {
                if (Integer.parseInt(pointAddr) == Integer.parseInt(zf.get(3)[i++])) {
                    Toast.makeText(getContext(), getString(R.string.address_already_exists), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            main.removeDestopText(mfb);
            Dialog_ok1 stok = new Dialog_ok1();
            Bundle bundle = new Bundle();
            bundle.putString("alert-ok1", context.getResources().getString(R.string.make_sure_single_board_connect));
            stok.setArguments(bundle);
            stok.setTargetFragment(List_Content_Settingx.this, 1);
            stok.okcz(3);
            stok.show(fm, "alert-ok1");
            addPointName = pointName;
            addPointAddr = pointAddr;
            addPointMeasCateg = pointMeasCateg;
        }


    }

    private void addCompment(String pointName, String pointAddr, String pointMeasCateg) {
        String pointBoardType;/* 若组分地址，组分测量单元类型值为空，则默认地址为00，测量单元类型为1（1为比色，2为光谱）（WL）*/
        saveOperationLogMsg(pointName, "增加组份" + pointName + "_" + pointAddr + "_" + pointMeasCateg, ErrorLog.msgType.操作_信息);

        Log.i("选择的类型", String.valueOf(spPointMeasCateg.getSelectedItemPosition()));

        pointMeasCateg = String.valueOf(spPointMeasCateg.getSelectedItemPosition() + 1);
        pointBoardType = String.valueOf(spCompBoardType.getSelectedItemPosition() + 1);
        mComponent.AddZf(pointName, pointAddr, pointMeasCateg, pointBoardType);
        UpdateList();
        ETpoint.setText("");
        ETpointAddr.setText("");

        /* 268435456的十六进制为0x10 0x00 0x00 0x00*/
        byte[] addrByte = toByteArray((268435456 + Integer.parseInt(pointAddr)), 4);

        SendManager.SendCmd(pointName + "_设备地址_06_38", S0, 3, 300, addrByte);

        String[] components = getContext().getResources().getStringArray(R.array.strComponents);

        for (int i = 0; i < components.length; i++) {
            if (pointName.equals(components[i])) {

                byte[] nameByte = toByteArray(i + 1, 4);
                SendManager.SendCmd(pointName + "_设备平台_06_65", S0, 3, 300, nameByte);
                break;
            }
        }

        // 增加指令 设置主控板软件匹配硬件信息（常规/W200）
        int iDeviceType = getCKADeviceType(pointMeasCateg, pointBoardType);
        byte[] btDeviceType = toByteArray(iDeviceType, 4);
        SendManager.SendCmd(pointName + "_仪器类型_06_216", S0, 3, 300, btDeviceType);


        List<String[]> zf1 = mComponent.getComponent();
        String[] sComponentZF = new String[zf1.get(1).length];
        for (int i = 0; i < zf1.get(1).length; i++) {
            sComponentZF[i] = zf1.get(1)[i];
        }
        InitSpinner(context, spComponents, sComponentZF, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
    }

    /**
     * 选择模块时候获取模块名称
     *
     * @param pointName 输入名称
     * @return 输入名称+模块名称：TPzl
     */
    private String addModuleGetName(String pointName) {
        if (spPointMeasCateg.getSelectedItem().equals(getString(R.string.dilution))) {
            if (spComponents.getCount() > 0) {
                pointName = spComponents.getSelectedItem().toString() + getModuleName("5");
            }
        } else if (spPointMeasCateg.getSelectedItem().equals(getString(R.string.distill))) {
            if (spComponents.getCount() > 0) {
                pointName = spComponents.getSelectedItem().toString() + getModuleName("6");
            }
        }
        return pointName;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            UpdateList();
            mCallbacks.onDialogRS();
        } else if (requestCode == 3) {

            addCompment(addPointName, addPointAddr, addPointMeasCateg);
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
                mComponent.DeleteZf(component);
                mDataList.remove(pos);
                List<String[]> zf = mComponent.getComponent();
                String[] sComponentZF = new String[zf.get(1).length];
                for (int i = 0; i < zf.get(1).length; i++) {
                    sComponentZF[i] = zf.get(1)[i];
                }
                InitSpinner(context, spComponents, sComponentZF, R.layout.simple_spinner_item_out_black, R.layout.simple_spinner_item_in_black);
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
                mComponent.UpdateZfUsed(component, false);
                UpdateList();
            } catch (Exception e) {
                Log.e("CustomAdapter1", e.toString());
            }
        }
    }

    /**
     * 找到推荐地址
     */
    private int getStringComponentAddr() {
        int i = 0, index = 0;
        List<String[]> zf = mComponent.getAll5721Component();
        int[] addrs = new int[zf.get(2).length + zf.get(3).length];
        for (i = 0; i < zf.get(2).length; i++) {
            addrs[index++] = Integer.parseInt(zf.get(2)[i]);
        }
        for (i = 0; i < zf.get(3).length; i++) {
            addrs[index++] = Integer.parseInt(zf.get(3)[i]);
        }
        return findArrayMex(addrs, addrs.length);
    }


    /**
     * 获取主控板仪器类型
     *
     * @param measCategory //* "1 比色", "2 光谱", "3 滴定", "4 VOC", "5 稀释" "6 蒸馏"  "7 多光谱" "8 注射泵滴定" "9 注射泵蒸馏" "10 AAS平台" "11 TOC平台"
     * @param boardType    // 1:常规 2：W200 3：8000
     * @return //1（1常规；2 8000；3高锰酸盐；4稀释背板；5蒸馏背板；6 VOCs；7 W200）
     */
    private int getCKADeviceType(String measCategory, String boardType) {

        switch (measCategory) {
            case "1":   //
            case "2":
            case "7":
                switch (boardType) {
                    case "1":
                        return 1;
                    case "2":
                        return 7;
                    case "3":
                        return 2;
                }
            case "3":
            case "8":
            case "13":
                return 3;
            case "4":
                return 6;
            case "5":
                return 4;
            case "6":
                return 5;
            case "9":
            case "11":
            case "12":
                return 1;
        }
        return 1;
    }


}
