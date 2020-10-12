package com.yzlm.cyl.cfragment.Display;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.CmdTable;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Dialog_ok;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mainDisplayControlDevicesInfo;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.addComponentLockDisplayShowFlag;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.strNoBoardComponent;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

/*
 * Created by caoyiliang on 2016/11/4.
 */
public class Display extends SubFragment {
    private Callbacks mCallbacks;
    public static List<LinearLayout> Ld = new ArrayList<>();
    private static Display fragment = null;
    public static TextView mTxTControlDevicesInfo;


    public static Display newInstance() {
        if (fragment == null) {
            fragment = new Display();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDisplaySelected(View view, int id);

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
        return R.layout.display;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        HorizontalScrollView displayScrollView = v.findViewById(R.id.displayScrollView);
        mTxTControlDevicesInfo = v.findViewById(R.id.txtControlDevicesInfo);
        mainDisplayControlDevicesInfo();
        LinearLayout d1 = v.findViewById(R.id.d1);
        LinearLayout d2 = v.findViewById(R.id.d2);
        LinearLayout d3 = v.findViewById(R.id.d3);
        LinearLayout d4 = v.findViewById(R.id.d4);
        LinearLayout d5 = v.findViewById(R.id.d5);
        LinearLayout d6 = v.findViewById(R.id.d6);
        LinearLayout d7 = v.findViewById(R.id.d7);
        LinearLayout d8 = v.findViewById(R.id.d8);
        LinearLayout d9 = v.findViewById(R.id.d9);
        LinearLayout d10 = v.findViewById(R.id.d10);
        LinearLayout d11 = v.findViewById(R.id.d11);
        LinearLayout d12 = v.findViewById(R.id.d12);
        Ld = new ArrayList<>();
        Ld.add(d1);
        Ld.add(d2);
        Ld.add(d3);
        Ld.add(d4);
        Ld.add(d5);
        Ld.add(d6);
        Ld.add(d7);
        Ld.add(d8);
        Ld.add(d9);
        Ld.add(d10);
        Ld.add(d11);
        Ld.add(d12);

        if (strComponent.get(1).length == 0) {
            displayScrollView.setVisibility(View.GONE);
        } else {
            displayScrollView.setVisibility(View.VISIBLE);
        }
        if (addComponentLockDisplayShowFlag) {
            displayScrollView.setVisibility(View.GONE);
        }
        int compsAndMeasParam = strComponent.get(1).length + strNoBoardComponent.get(1).length;

        displayScrollView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, compsAndMeasParam));

        int index = 0;
        for (int i = 0; i < Ld.size(); i++) {

            if (i < compsAndMeasParam) {
                Ld.get(index).setVisibility(View.VISIBLE);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                if (Ld.get(index).findViewById(R.id.display_item) == null) {
                    Ld.get(index).addView(inflater.inflate(R.layout.display_item, null));
                }
                if (i >= strComponent.get(1).length) {
                    //无背板 5722参数显示
                    String compName = strNoBoardComponent.get(1)[i - strComponent.get(1).length];
                    ((TextView) Ld.get(index).findViewById(R.id.Name)).setText(compName);
                    int point = Integer.parseInt(getNoBoardConfigData(compName, "YXWS"));
                    String YBLCL = ConvertUnit("mg/L", getNoBoardConfigData(compName, "UNIT"), (getNoBoardConfigData(compName, "YBLCL")), point);
                    String YBLCH = ConvertUnit("mg/L", getNoBoardConfigData(compName, "UNIT"), (getNoBoardConfigData(compName, "YBLCH")), point);

                    ((TextView) Ld.get(index).findViewById(R.id.tvLC)).setText(YBLCL + "-" + YBLCH);

                    String range = getNoBoardConfigData(compName, "RANGE");
                    String DQLCL = ConvertUnit("mg/L", getNoBoardConfigData(compName, "UNIT"), (getNoBoardConfigData(compName, "LC" + range + "L")), point);
                    String DQLCH = ConvertUnit("mg/L", getNoBoardConfigData(compName, "UNIT"), (getNoBoardConfigData(compName, "LC" + range + "H")), point);
                    ((TextView) Ld.get(index).findViewById(R.id.tvSYLC)).setText(DQLCL + "-" + DQLCH);

                    Ld.get(index).setOnClickListener(new ld2Click());
                    index++;
                } else {
                    // 有背板组分 5721
                    String compName = strComponent.get(1)[i];
                    if (QueryMeasCateg(compName).equals("5") || QueryMeasCateg(compName).equals("6")) {
                        continue;
                    }
                    ((TextView) Ld.get(index).findViewById(R.id.Name)).setText(compName);
                    int point = Integer.parseInt(getConfigData(compName, "YXWS"));
                    String YBLCL = ConvertUnit("mg/L", getConfigData(compName, "UNIT"), (getConfigData(compName, "YBLCL")), point);
                    String YBLCH = ConvertUnit("mg/L", getConfigData(compName, "UNIT"), (getConfigData(compName, "YBLCH")), point);

                    ((TextView) Ld.get(index).findViewById(R.id.tvLC)).setText(YBLCL + "-" + YBLCH);

                    String range = getConfigData(compName, "RANGE");
                    String DQLCL = ConvertUnit("mg/L", getConfigData(compName, "UNIT"), (getConfigData(compName, "LC" + range + "L")), point);
                    String DQLCH = ConvertUnit("mg/L", getConfigData(compName, "UNIT"), (getConfigData(compName, "LC" + range + "H")), point);
                    ((TextView) Ld.get(index).findViewById(R.id.tvSYLC)).setText(DQLCL + "-" + DQLCH);

                    Ld.get(index).setOnClickListener(new ldClick());
                    TextView txtState = Ld.get(i).findViewById(R.id.tv_workSate);
                    if (txtState == null) {
                        txtState = Ld.get(i).findViewById(i);
                    }
                    txtState.setText(workState.get(strComponent.get(1)[i]));
                    txtState.setOnClickListener(new textViewClick());
                    txtState.setId(i);
                    index++;
                }
            } else {
                Ld.get(index++).setVisibility(View.GONE);
            }
        }


    }

    private class ldClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mCallbacks.onDisplaySelected(view, 1);
        }
    }

    private class ld2Click implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mCallbacks.onDisplaySelected(view, 2);
        }
    }

    private class textViewClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                String name = strComponent.get(1)[view.getId()];
                MainActivity.mCompName = name;

                if (!workState.get(name).equals(context.getString(R.string.normal)) && !workState.get(name).equals(context.getString(R.string.off_line))
                        && doFlowing.get(name).equals(context.getString(R.string.waiting_for_instructions))) {
                    main.removeDestopText(mfb);
                    Dialog_ok st = new Dialog_ok();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", context.getResources().getString(R.string.ZY));
                    st.setArguments(bundle);
                    st.setTargetFragment(Display.this, 1);
                    st.show(fm, "alert-ok");
                }
            } catch (Exception e) {
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            final String stringExtra = data.getStringExtra("Dialog_Command");
            mCallbacks.onDialogRS();

            if (!workState.get(mCompName).equals(context.getString(R.string.off_line))) {
                Log.i("T1", stringExtra);
                final String[] str = stringExtra.split("_");
                if (doFlowing.get(str[0]).equals(context.getString(R.string.waiting_for_instructions))) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[] str = stringExtra.split("_");

                            // 界面工作状态异常是直接点击启动（做样流程）
                            //判定流程是否是标记为手动流程名称，启动日志需要记录（手动/自动）
                            if (str[1].contains(context.getString(R.string.manual))) {
                            } else {
                                str[1] = context.getString(R.string.manual) + str[1];
                            }
                            doControlJob(str[0], str[1]);
                        }
                    }).start();

                    Log.i("T3", str[0]);
                    getCmds(str[0]).doSampleMode = CmdTable.DoSampleMode.手动;
                    Toast.makeText(context, context.getString(R.string.ready_to_start) + stringExtra + context.getString(R.string.pleaseWaiting), Toast.LENGTH_SHORT).show();
                    saveOperationLogMsg(mCompName, "主界面报错启动" + str[1], ErrorLog.msgType.操作_信息);
                }
            } else {
                Toast.makeText(context, context.getString(R.string.communication_is_disconnected), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
