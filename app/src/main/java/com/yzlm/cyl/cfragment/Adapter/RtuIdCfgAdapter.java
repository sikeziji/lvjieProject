package com.yzlm.cyl.cfragment.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.util.List;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.sContext;

public class RtuIdCfgAdapter extends BaseAdapter {


    private List<RtuIdInfo> items;

    private Context context;

    private String parameter;


    protected RtuIdCfgAdapter(Context context, List<RtuIdInfo> items, String parameter) {
        this.context = context;
        this.items = items;
        this.parameter = parameter;
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int position) {
        return this.items.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        RtuIdHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(this.context, R.layout.rtu2018iditem, (ViewGroup) null);
            viewHolder = new RtuIdHolder();

            viewHolder.mTxtCompName = (TextView) convertView.findViewById(R.id.txCompName);
            viewHolder.mEdRtuID = (EditText) convertView.findViewById(R.id.eTRtu2018Id);
            viewHolder.mEdRtuID.setOnFocusChangeListener(new EtFocusChange());
            viewHolder.mEdRtuID.setOnEditorActionListener(new EtActionListener());
            viewHolder.mEdRtuID.setId(position);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RtuIdHolder) convertView.getTag();
        }
        RtuIdInfo rtuIdInfo = items.get(position);
        viewHolder.mTxtCompName.setText(rtuIdInfo.getCompName());
        viewHolder.mEdRtuID.setText(String.valueOf(rtuIdInfo.getId()));
        viewHolder.mEdRtuID.setId(position);

        //强制清除当前焦点
        View currentFocus = Global.activity.getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }

        return convertView;
    }


    private class EtActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return true;
            }
            return false;
        }
    }

    private class EtFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String strValue = ((EditText) v).getText().toString();
                EditText editText = (EditText) v;

                if (strValue.equals("") || (Integer.parseInt(strValue) < 0) || (Integer.parseInt(strValue) > 247)) {
                    editText.setText("1");
                }
                String strId = getConfigData(strComponent.get(1)[v.getId()], parameter);
                if (strId.equals(strValue)) {
                    return;
                }
                if (!setIDConflicting(strValue, parameter)) {
                    editText.setText(getConfigData(strComponent.get(1)[v.getId()], parameter));
                    return;
                }
                RtuIdInfo rtuIdInfoBefore = items.get(v.getId());
                rtuIdInfoBefore.setId(Integer.valueOf(strValue));
                items.set(v.getId(), rtuIdInfoBefore);
                notifyDataSetChanged();
                saveOperationLogDataModifyMsg(strComponent.get(1)[v.getId()], parameter, strValue, "RTU Id", ErrorLog.msgType.操作_信息);
                updateConfigData(strComponent.get(1)[v.getId()], parameter, strValue);
                FullWindows(mActivityWindow);
                for (String item : strComponent.get(1)) {
                    String strId1 = getConfigData(item, parameter);
                    Log.i(parameter, item + strId1);
                }
            }
        }
    }


    public static boolean setIDConflicting(String setStrValue, String parameter) {
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {
                String strId = getConfigData(item, parameter);
                String strSysId = getPublicConfigData("SYS_RTU_ID");

                if (strId.equals(setStrValue) || strSysId.equals(setStrValue)) {
                    Toast.makeText(Global.context, Global.context.getString(R.string.set_rtu2018_id_exception), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    public void UpdateList(List<RtuIdInfo> litems) {
        this.items = litems;
        notifyDataSetChanged();
    }

}
