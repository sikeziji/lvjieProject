package com.yzlm.cyl.cfragment.Dialog;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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

public class Dialog_datetime1 extends ZDialogFragment {
    private EditText mD_ET1;
    private EditText mD_ET2;
    private String Date1 = "";
    private String Date2 = "";
    private Boolean isShowPrint = false;
    private int cz;


    public void setShowPrint(boolean showPrint) {
        this.isShowPrint = showPrint;
    }

    @Override
    protected int getDialogView() {
        return R.layout.dialog_datetime1;
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void InitUI(View view) {
        mD_ET1 = (EditText) view.findViewById(R.id.D_ET1);
        mD_ET1.setInputType(InputType.TYPE_NULL);
        mD_ET1.setOnFocusChangeListener(new Dialog_datetime1.mET1());
        mD_ET1.setOnEditorActionListener(new Dialog_datetime1.mET1());
        mD_ET2 = (EditText) view.findViewById(R.id.D_ET2);
        mD_ET2.setInputType(InputType.TYPE_NULL);
        mD_ET2.setOnFocusChangeListener(new Dialog_datetime1.mET2());
        mD_ET2.setOnEditorActionListener(new Dialog_datetime1.mET2());

        Button mBtnD_OK = (Button) view.findViewById(R.id.D_OK);
        mBtnD_OK.setOnClickListener(new Dialog_datetime1.BtnOKClick());

        Button mBtnD_Cancel = (Button) view.findViewById(R.id.D_Cancel);
        mBtnD_Cancel.setOnClickListener(new Dialog_datetime1.BtnCancelClick());
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

    /**
     * 判断requestCode值；
     * @param requestCode
     */
    public void   cz(int requestCode)
    {
        cz = requestCode;
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
            setData(cz, "Dialog_datetime1", Date1 + ";" + Date2);
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
                DateDialog.getDateEvent(new Dialog_datetime1.SetDate1());
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
                DateDialog.getDateEvent(new Dialog_datetime1.SetDate2());
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
}
