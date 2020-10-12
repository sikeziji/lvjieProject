package com.yzlm.cyl.cfragment.Content.Component.ListSetting;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.Calendar;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.FullWindows;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mActivityWindow;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

/**
 * Created by caoyiliang on 2016/10/28.
 */

public class List_Content_Setting3 extends SubFragment {
    private EditText mETy;
    private EditText mETM;
    private EditText mETd;
    private EditText mETH;
    private EditText mETm;
    private EditText mETs;

    private static List_Content_Setting3 fragment = null;

    public static List_Content_Setting3 newInstance() {
        if (fragment == null) {
            fragment = new List_Content_Setting3();
        }
        return fragment;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list_content_setting3;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        mETy = (EditText) v.findViewById(R.id.eTy);
        mETM = (EditText) v.findViewById(R.id.eTM);
        mETd = (EditText) v.findViewById(R.id.eTd);
        mETH = (EditText) v.findViewById(R.id.eTH);
        mETm = (EditText) v.findViewById(R.id.eTm);
        mETs = (EditText) v.findViewById(R.id.eTs);

        /* 获取时间管理界面的确定按钮对象（WL）*/
        /* 时间管理界面的确定按钮对象（WL）*/
        Button btnTimeManageOK = (Button) v.findViewById(R.id.Btn_timeManageOK);

        Calendar c = Calendar.getInstance();
        mETy.setText(Integer.toString(c.get(Calendar.YEAR)));
        mETM.setText(Integer.toString(c.get(Calendar.MONTH) + 1));
        mETd.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
        mETH.setText(Integer.toString(c.get(Calendar.HOUR_OF_DAY)));
        mETm.setText(Integer.toString(c.get(Calendar.MINUTE)));
        mETs.setText(Integer.toString(c.get(Calendar.SECOND)));
        mETy.setOnFocusChangeListener(new dateFocusChange());
        mETy.setOnEditorActionListener(new dateFocusChange());
        mETM.setOnFocusChangeListener(new dateFocusChange());
        mETM.setOnEditorActionListener(new dateFocusChange());
        mETd.setOnFocusChangeListener(new dateFocusChange());
        mETd.setOnEditorActionListener(new dateFocusChange());
        mETH.setOnFocusChangeListener(new timeFocusChange());
        mETH.setOnEditorActionListener(new timeFocusChange());
        mETm.setOnFocusChangeListener(new timeFocusChange());
        mETm.setOnEditorActionListener(new timeFocusChange());
        mETs.setOnFocusChangeListener(new timeFocusChange());
        mETs.setOnEditorActionListener(new timeFocusChange());

        /* 设置时间管理界面的确定按钮的单击事件（WL）*/
        btnTimeManageOK.setOnClickListener(new btnOKOnClick());
    }

    private class btnOKOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            String year = mETy.getText().toString().trim();
            String month = mETM.getText().toString().trim();
            String day = mETd.getText().toString().trim();
            String hour = mETH.getText().toString().trim();
            String minute = mETm.getText().toString().trim();
            String second = mETs.getText().toString().trim();

            if (!year.equals("") && !month.equals("") && !day.equals("") && !hour.equals("") && !minute.equals("") && !second.equals("")) {
                byte[] timeByte = copybyte(toByteArray(Integer.parseInt(year), 4), toByteArray(Integer.parseInt(month), 4),
                        toByteArray(Integer.parseInt(day), 4), toByteArray(Integer.parseInt(hour), 4),
                        toByteArray(Integer.parseInt(minute), 4), toByteArray(Integer.parseInt(second), 4));

                for (String item : strComponent.get(1)) {
                    SendManager.SendCmd(item + "_时间管理_06_0", S0, 3, 200, timeByte);
                    //时间矫正
                    AddError(item, 614, 运维_信息);
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.time_empty), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void syncList() {
        closeInputMethod(v);
        FullWindows(mActivityWindow);
    }

    private class dateFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                SetDateTime("all", Integer.parseInt(mETy.getText().toString()), Integer.parseInt(mETM.getText().toString()) - 1, Integer.parseInt(mETd.getText().toString()), 0, 0, 0);

                /*try {
                    setDate(Integer.parseInt(mETy.getText().toString()), Integer.parseInt(mETM.getText().toString()) - 1, Integer.parseInt(mETd.getText().toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                syncList();
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

    private class timeFocusChange implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                SetDateTime("all",0,0,0,Integer.parseInt(mETH.getText().toString()), Integer.parseInt(mETm.getText().toString()), Integer.parseInt(mETs.getText().toString()));
                /*try {
                    setTime(Integer.parseInt(mETH.getText().toString()), Integer.parseInt(mETm.getText().toString()), Integer.parseInt(mETs.getText().toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                syncList();
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
}
