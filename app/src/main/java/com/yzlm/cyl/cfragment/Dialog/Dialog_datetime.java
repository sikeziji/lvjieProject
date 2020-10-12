package com.yzlm.cyl.cfragment.Dialog;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Control.SetDateDialog;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;


/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_datetime extends ZDialogFragment {
    private EditText mD_ET1;
    private EditText mD_ET2;
    private String Date1 = "";
    private String Date2 = "";
    private Boolean isShowPrint = false;
    private Boolean isShowBtnLayout = false;

    private LinearLayout layoutRecordBtn;
    private CheckBox btnLoginRecord,btnRunRecord,btnOtherRecord;//登录、运维、其他日志
    private Boolean checkLoginRecord = false;//查看登录日志
    private Boolean checkRunRecord = false;//查看运维日志
    private Boolean checkOtherRecord = false;//查看其它日志


    public void setShowPrint(boolean showPrint) {
        this.isShowPrint = showPrint;
    }

    /**
     * 设置显示和隐藏运行日志查询功能
     * @param showBtnLayout
     */
    public void setShowBtnLayout(boolean showBtnLayout){
        this.isShowBtnLayout = showBtnLayout;
    }

    @Override
    protected int getDialogView() {
        return R.layout.dialog_datetime;
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void InitUI(View view) {

        mD_ET1 = (EditText) view.findViewById(R.id.D_ET1);
        mD_ET1.setInputType(InputType.TYPE_NULL);
        mD_ET1.setOnFocusChangeListener(new mET1());
        mD_ET1.setOnEditorActionListener(new mET1());
        mD_ET2 = (EditText) view.findViewById(R.id.D_ET2);
        mD_ET2.setInputType(InputType.TYPE_NULL);
        mD_ET2.setOnFocusChangeListener(new mET2());
        mD_ET2.setOnEditorActionListener(new mET2());

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_Cancel);
        mBtnD_Cancel.setOnClickListener(new BtnCancelClick());

        Button mBtnD_Print = (Button) view.findViewById(R.id.D_Print);
        mBtnD_Print.setOnClickListener(new BtnPrintClick());
        if (isShowPrint) {
            mBtnD_Print.setVisibility(View.VISIBLE);
        } else {
            mBtnD_Print.setVisibility(View.GONE);
        }

        if (isShowBtnLayout) {
            mBtnD_Print.setVisibility(View.VISIBLE);
        } else {
            mBtnD_Print.setVisibility(View.GONE);
        }


        layoutRecordBtn = view.findViewById(R.id.layout_record_btn);
        if (isShowBtnLayout){
            layoutRecordBtn.setVisibility(View.VISIBLE);
        }else{
            layoutRecordBtn.setVisibility(View.GONE);
        }
        btnLoginRecord = view.findViewById(R.id.btn_login_record);
        btnRunRecord = view.findViewById(R.id.btn_run_record);
        btnOtherRecord = view.findViewById(R.id.btn_other_record);
        btnLoginRecord.setOnCheckedChangeListener(new CheckChangeListener());
        btnRunRecord.setOnCheckedChangeListener(new CheckChangeListener());
        btnOtherRecord.setOnCheckedChangeListener(new CheckChangeListener());
    }

    @Override
    protected boolean setCanceledOnTouchOutside() {
        return true;
    }

    @Override
    protected String getTitleName() {
        return null;
    }

    @Override
    protected String getPositiveButtonName() {
        return null;
    }

    @Override
    protected String getNegativeButtonName() {
        return null;
    }

    @Override
    protected void PositiveButtonListener() {

    }

    @Override
    protected void NegativeButtonListener() {

    }

    @Override
    protected void dialogCancel() {

    }


    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                Date oldDate = sdf.parse(Date1);
                Date newDate = sdf.parse(Date2);
                if (oldDate.compareTo(newDate) > 0) {
                    Toast.makeText(getContext(), getString(R.string.start_time_before_end_time), Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            setData(1, "Dialog_datetime", Date1 + ";" + Date2 + ";" + checkLoginRecord + ";" + checkRunRecord + ";" + checkOtherRecord);
            dialog.dismiss();

        }
    }

    private class BtnPrintClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                Date oldDate = sdf.parse(Date1);
                Date newDate = sdf.parse(Date2);
                if (oldDate.compareTo(newDate) > 0) {
                    Toast.makeText(getContext(), getString(R.string.start_time_before_end_time), Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            setData(3, "Dialog_datetime", Date1 + ";" + Date2 + ";" + checkLoginRecord + ";" + checkRunRecord + ";" + checkOtherRecord);
            dialog.dismiss();
        }
    }

    private class BtnCancelClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setData(2);
            dialog.dismiss();
        }
    }

    private class mET1 implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                SetDateDialog DateDialog = new SetDateDialog();
                DateDialog.getDateEvent(new SetDate1());
                DateDialog.show(fm, "select_date");

            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return true;
            }
            return  false;
        }
    }

    private class SetDate1 implements SetDateDialog.ICallBack {
        @Override
        public void getDateEvent(int year, int month, int day) {
            mD_ET1.setText(year + "-" + month + "-" + day );
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date1 = year + "-" + month + "-" + day + " 00:00:00";
            try {
                Date1 = new Timestamp(format.parse(Date1).getTime()).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (mD_ET1.getText().toString().equals("")) {
                Date1 = "";
            }
            mD_ET1.clearFocus();
        }
    }

    private class mET2 implements View.OnFocusChangeListener, TextView.OnEditorActionListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                SetDateDialog DateDialog = new SetDateDialog();
                DateDialog.getDateEvent(new SetDate2());
                DateDialog.show(fm, "select_date");
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return true;
            }
            return  false;
        }
    }

    private class SetDate2 implements SetDateDialog.ICallBack {
        @Override
        public void getDateEvent(int year, int month, int day) {
            mD_ET2.setText(year + "-" + month + "-" + day );
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date2 = year + "-" + month + "-" + day + " 23:59:59";
            try {
                Date2 = new Timestamp(format.parse(Date2).getTime()).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (mD_ET2.getText().toString().equals("")) {
                Date2 = "";
            }
            mD_ET2.clearFocus();
        }
    }


    //checkBox监听
    private class CheckChangeListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.btn_login_record:
                    if (isChecked){
                        checkLoginRecord = true;
                    }else{
                        checkLoginRecord = false;
                    }
                    break;
                case R.id.btn_run_record:
                    if (isChecked){
                        checkRunRecord = true;
                    }else{
                        checkRunRecord = false;
                    }
                    break;
                case R.id.btn_other_record:
                    if (isChecked){
                        checkOtherRecord = true;
                    }else{
                        checkOtherRecord = false;
                    }
                    break;
            }
        }
    }

}
