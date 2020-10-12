package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.List;

import static com.yzlm.cyl.cfragment.File.MFile.getFiles;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;

public class List4_Content3_FileNameList extends SubFragment {

    private Callbacks mCallbacks;

    private static List4_Content3_FileNameList fragment = null;

    public static List4_Content3_FileNameList newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_FileNameList();
        }
        return fragment;
    }

    TextView txtFileNameInfo;
    TextView txtFileNameName;

    public interface Callbacks {
        void onListSelected(View view);

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
        return R.layout.list4_content3_file_name_list;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturnFlow);
        mBtnReturn.setOnClickListener(new btnClick());

        List<String> files = getFiles("/sdcard/Csoft/" + mCompName + "/", ".txt");
        String strInfo = "";
        String strName = "";
        for (String filePath : files) {
            String[] strFilePath = filePath.split("_");
            strInfo += filePath.substring(filePath.indexOf("_") + 1) + "\n";
            strName += strFilePath[0] + "\n";
        }
        txtFileNameInfo = (TextView) v.findViewById(R.id.txt_fileNameInfo);
        txtFileNameInfo.setText(strInfo);

        txtFileNameName = (TextView) v.findViewById(R.id.txt_fileNameName);
        txtFileNameName.setText(strName);
    }

    public static String formatString(String str, int length, String slot) {
        StringBuffer sb = new StringBuffer();
        sb.append(str);

        int count = length - str.length();

        while (count > 0) {
            sb.append(slot);
            count--;
        }

        return sb.toString();
    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btnReturnFlow:
                        mCallbacks.onListSelected(v);
                        break;
                }
            } catch (Exception e) {
                Log.e("btnClick", e.toString());
            }
        }
    }
}
