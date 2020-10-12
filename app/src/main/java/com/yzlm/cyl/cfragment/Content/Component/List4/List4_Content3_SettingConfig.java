package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_CopyConfigSelect;
import com.yzlm.cyl.cfragment.Dialog.Component.Dialog_RsConfigSelect;
import com.yzlm.cyl.cfragment.Dialog.Dialog_PublicOk;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStrNowSysTemTimeWithOutms;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.File.MFile.getFiles;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.backupOrRestoreFiles;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.SetMD5;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.extSdcardPath;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;


public class List4_Content3_SettingConfig extends SubFragment {
    static List4_Content3_SettingConfig fragment = null;
    private Callbacks mCallbacks;

    public static List4_Content3_SettingConfig newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_SettingConfig();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogRS();

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
        return R.layout.list4_content3_settingconfig;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {

        ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturnFlow);
        mBtnReturn.setOnClickListener(new btnClick());

        Button btnUpConfig = (Button) v.findViewById(R.id.btnUpdateConfig);
        btnUpConfig.setOnClickListener(new BtnClickListener());

        Button btnCpConfig = (Button) v.findViewById(R.id.btncopyConfig);
        btnCpConfig.setOnClickListener(new BtnClickListener());

        Button btnIpConfig = (Button) v.findViewById(R.id.btnimportConfig);
        btnIpConfig.setOnClickListener(new BtnClickListener());

        Button btnRsConfig = (Button) v.findViewById(R.id.btnrestoreConfig);
        btnRsConfig.setOnClickListener(new BtnClickListener());

        Button btnbupConfig = (Button) v.findViewById(R.id.btnbackupConfig);
        btnbupConfig.setOnClickListener(new BtnClickListener());

        Button btnimportbackupData = (Button) v.findViewById(R.id.btn_importbackupdata);
        btnimportbackupData.setOnClickListener(new BtnClickListener());

    }

    private class btnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallbacks.onListSelected(v);
        }
    }

    private class BtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnUpdateConfig: {
                    main.removeDestopText(mfb);
                    Dialog_PublicOk st = new Dialog_PublicOk();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", context.getResources().getString(R.string.updateConfig));
                    st.setArguments(bundle);
                    st.setTargetFragment(List4_Content3_SettingConfig.this, 1);
                    st.show(fm, "alert-ok");
                }
                break;
                case R.id.btncopyConfig: {
                    main.removeDestopText(mfb);
                    Dialog_CopyConfigSelect st = new Dialog_CopyConfigSelect();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", context.getResources().getString(R.string.copyConfig));
                    st.setArguments(bundle);
                    st.setTargetFragment(List4_Content3_SettingConfig.this, 3);
                    st.show(fm, "alert-ok");
                }
                break;
                case R.id.btnimportConfig: {
                    main.removeDestopText(mfb);
                    Dialog_PublicOk st = new Dialog_PublicOk();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", context.getResources().getString(R.string.importConfig));
                    st.setArguments(bundle);
                    st.setTargetFragment(List4_Content3_SettingConfig.this, 1);
                    st.show(fm, "alert-ok");
                }
                break;
                case R.id.btnrestoreConfig: {
                    main.removeDestopText(mfb);
                    Dialog_RsConfigSelect st = new Dialog_RsConfigSelect();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", context.getResources().getString(R.string.restoreConfig));
                    st.setArguments(bundle);
                    st.setTargetFragment(List4_Content3_SettingConfig.this, 4);
                    st.show(fm, "alert-ok");
                }
                break;
                case R.id.btnbackupConfig: {
                    main.removeDestopText(mfb);
                    Dialog_PublicOk st = new Dialog_PublicOk();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", context.getResources().getString(R.string.backupConfig));
                    st.setArguments(bundle);
                    st.setTargetFragment(List4_Content3_SettingConfig.this, 1);
                    st.show(fm, "alert-ok");
                }
                break;
                case R.id.btn_importbackupdata: {
                    main.removeDestopText(mfb);
                    Dialog_PublicOk st = new Dialog_PublicOk();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok", context.getResources().getString(R.string.import_backup_data));
                    st.setArguments(bundle);
                    st.setTargetFragment(List4_Content3_SettingConfig.this, 1);
                    st.show(fm, "alert-ok");
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String stringExtra = data.getStringExtra("DialogPublicOk");
            if (stringExtra == null) {
                return;
            }
            switch (stringExtra) {
                case "更新配置文件": {
                    AddError(mCompName, 589, ErrorLog.msgType.其他_信息);
                    SetMD5();
                    Toast.makeText(context, context.getResources().getString(R.string.updateConfig_complete), Toast.LENGTH_SHORT).show();
                }
                break;
                case "导入配置文件": {
                    if (extSdcardPath == null || extSdcardPath.equals(SdcardPath)) {
                        Toast.makeText(context, context.getResources().getString(R.string.no_external_SD_card), Toast.LENGTH_SHORT).show();
                    } else {
                        //先备份当前Csoft
                        CopyFile(SdcardPath + "Csoft/", SdcardPath + "Backup/" + getStrNowSysTemTimeWithOutms());

                        CopyFile(extSdcardPath + "Csoft/", SdcardPath);
                        AddError(mCompName, 593, ErrorLog.msgType.其他_信息);
                        Toast.makeText(context, context.getResources().getString(R.string.importConfig_complete), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case "备份配置文件": {
                    //先备份当前Csoft
                    CopyFile(SdcardPath + "Csoft/", SdcardPath + "Backup/" + getStrNowSysTemTimeWithOutms());

                    Toast.makeText(context, context.getResources().getString(R.string.backupConfig_complete), Toast.LENGTH_SHORT).show();
                }
                break;
                case "导入备份数据": {
                    if (extSdcardPath != null) {
                        String extSdcardBFPath = extSdcardPath + "备份数据/ZTWaterData/";//数据备份路径

                        List<String> files = getFiles(extSdcardBFPath, ".db");
                        for (String filePath : files) {
                            //CopyFile(extSdcardBFPath + filePath, SdcardPath + "Csoft/");
                            backupOrRestoreFiles(extSdcardBFPath, filePath, SdcardPath, "Csoft", "RESTORE_BACKUP");
                        }

                        Toast.makeText(context, context.getResources().getString(R.string.backup_data_complete) + "     " + context.getResources().getString(R.string.please_restart), Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            }

        } else if (requestCode == 3) {
            String stringExtra = data.getStringExtra("Dialog_CopyConfigSelect");
            if (stringExtra == null) {
                return;
            }
            switch (stringExtra) {
                case "全选": {
                    if (extSdcardPath == null || extSdcardPath.equals(SdcardPath)) {
                        Toast.makeText(context, context.getResources().getString(R.string.no_external_SD_card), Toast.LENGTH_SHORT).show();
                    } else {
                        //CopyFile(SdcardPath + "Csoft/", extSdcardPath);
                        CopyFile(SdcardPath + "waterrun/", extSdcardPath);
                        CopyFile(SdcardPath + "watercrash/", extSdcardPath);
                        Toast.makeText(context, context.getResources().getString(R.string.copyConfig_complete), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case "Csoft": {
                    if (extSdcardPath == null || extSdcardPath.equals(SdcardPath)) {
                        Toast.makeText(context, context.getResources().getString(R.string.no_external_SD_card), Toast.LENGTH_SHORT).show();
                    } else {
                        CopyFile(SdcardPath + "Csoft/", extSdcardPath);
                        Toast.makeText(context, context.getResources().getString(R.string.copyConfig_complete), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case "waterrun": {
                    if (extSdcardPath == null || extSdcardPath.equals(SdcardPath)) {
                        Toast.makeText(context, context.getResources().getString(R.string.no_external_SD_card), Toast.LENGTH_SHORT).show();
                    } else {
                        CopyFile(SdcardPath + "waterrun/", extSdcardPath);
                        Toast.makeText(context, context.getResources().getString(R.string.copyConfig_complete), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case "watercrash": {
                    if (extSdcardPath == null || extSdcardPath.equals(SdcardPath)) {
                        Toast.makeText(context, context.getResources().getString(R.string.no_external_SD_card), Toast.LENGTH_SHORT).show();
                    } else {
                        CopyFile(SdcardPath + "watercrash/", extSdcardPath);
                        Toast.makeText(context, context.getResources().getString(R.string.copyConfig_complete), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        } else if (requestCode == 4) {
            String stringExtra = data.getStringExtra("Dialog_RsConfigSelect");
            if (stringExtra == null || stringExtra.equals("")) {
                return;
            }
            CopyFile(SdcardPath + "Backup/" + stringExtra + "/Csoft", SdcardPath);
            AddError(mCompName, 593, ErrorLog.msgType.其他_信息);
            Toast.makeText(context, context.getResources().getString(R.string.restoreConfig_complete), Toast.LENGTH_SHORT).show();
        } else {

        }
        mCallbacks.onDialogRS();
    }

    public static void copy(File file, File toFile) throws Exception {
        byte[] b = new byte[1024];
        int a;
        FileInputStream fis;
        FileOutputStream fos;
        if (file.isDirectory()) {
            String filepath = file.getAbsolutePath();
            filepath = filepath.replaceAll("\\\\", "/");
            String toFilepath = toFile.getAbsolutePath();
            toFilepath = toFilepath.replaceAll("\\\\", "/");
            int lastIndexOf = filepath.lastIndexOf("/");
            toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());
            File fcopy = new File(toFilepath);
            //复制文件夹CsoftBackUpFile
            if (!fcopy.exists()) {
                fcopy.mkdirs();
            }
            //遍历文件夹
            for (File f : file.listFiles()) {
                copy(f, fcopy);
            }
        } else {
            String fileName = file.getName();
            // 通过切割获取文件后缀名
            String str = fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
            if (str.equals(".txt") || str.equals(".log")) {
                if (toFile.isDirectory()) {
                    String filepath = file.getAbsolutePath();
                    filepath = filepath.replaceAll("\\\\", "/");
                    String toFilepath = toFile.getAbsolutePath();
                    toFilepath = toFilepath.replaceAll("\\\\", "/");
                    int lastIndexOf = filepath.lastIndexOf("/");
                    toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());

                    //写文件
                    File newFile = new File(toFilepath);
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(newFile);
                    while ((a = fis.read(b)) != -1) {
                        fos.write(b, 0, a);
                    }
                } else {
                    //写文件
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(toFile);
                    while ((a = fis.read(b)) != -1) {
                        fos.write(b, 0, a);
                    }
                }
            }
        }
    }

    private void CopyFile(String pathname, String topathname) {
        //需要复制的目标文件或目标文件夹
        File file = new File(pathname);
        //复制到的位置
        File toFile = new File(topathname);
        try {
            copy(file, toFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
