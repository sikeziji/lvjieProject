package com.yzlm.cyl.cfragment.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yzlm.cyl.clibrary.CFragment.CDialogFragment;

public abstract class ZDialogFragment extends CDialogFragment {

    protected abstract void dialogCancel();


    public void onCancel(DialogInterface dialog) {

        ZDialogFragment.this.dialogCancel();
        super.onCancel(dialog);
        this.setData(2);
    }
    protected void InitSpinner(Context context, Spinner spinner, String[] arrList, int OutStyle, int InStyle) {
        ArrayAdapter<String> adapter = new ArrayAdapter(context, OutStyle);

        for (String s : arrList) {
            adapter.add(s);
        }

        adapter.setDropDownViewResource(InStyle);
        spinner.setAdapter(adapter);
    }
}
