package com.yzlm.cyl.cfragment.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;

public class Dialog_SDRes extends Dialog implements View.OnClickListener {
    ListView listView;
    TextView textView;
    //记录当前的父文件夹
    File currentParent;
    //记录当前目录路径下的所有文件的文件数组
    File[] currentFiles;


    private ResCallBack resCallBack;

    public Dialog_SDRes(@NonNull Context context, ResCallBack callBack) {
        super(context, R.style.MyDialogStyle);
        this.resCallBack = callBack;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_res);
        updateUI();
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取系统的SD卡的目录
        File root = new File("/mnt/sdcard/");
        //如果SD卡存在
        if (root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();
            //使用当前目录下的全部文件、文件夹来填充ListView
            inflateListView(currentFiles);
        }
    }

    /**
     * 初始化视图
     */
    @SuppressLint("ResourceAsColor")
    private void initView() {
        //获取列出全部文件的ListView
        listView = (ListView) findViewById(R.id.dialog_res_list);
        textView = (TextView) findViewById(R.id.dialog_res_path);
        //获取上一级目录的按钮
        Button parent = (Button) findViewById(R.id.dialog_res_parent);
        parent.setOnClickListener(this);
        //为ListView的列表项的单击事件绑定监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //用户单击了文件，直接返回，不做任何处理
                if (currentFiles[arg2].isFile()) {
                    resCallBack.updateBin(currentFiles[arg2].getPath());
                    return;
                }
                //获取用户单击的文件夹下的所有文件
                File[] tmp = currentFiles[arg2].listFiles();
                if (tmp == null || tmp.length == 0) {
                    Toast.makeText(getContext(), "当前路径不可访问或该路径下没有文件", Toast.LENGTH_SHORT).show();
                } else {
                    //获取用户单击的列表项对应的文件夹，设为当前的父文件夹
                    currentParent = currentFiles[arg2];
                    //保存当前的父文件夹内的全部文件和文件夹
                    currentFiles = tmp;
                    //再次更新ListView
                    inflateListView(currentFiles);
                }
            }
        });
    }

    /**
     * 根据视图宽高设置dialog
     */
    private void updateUI() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        //lp.x与lp.y表示相对于原始位置的偏移.
        //将对话框的大小按屏幕大小的百分比设置
        lp.width = (int) (width * 0.6); // 宽度
        lp.height = (int) (height * 0.9); // 高度

        dialogWindow.setAttributes(lp);
    }


    private void inflateListView(File[] files) {
        try {
            textView.setText("当前路径为: " + currentParent.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建一个List集合，List集合的元素是Map
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();

            if (files[i].isDirectory()) {
                listItem.put("icon", R.drawable.file_folder);
            } else {
                listItem.put("icon", R.drawable.file);
            }

            listItem.put("fileName", files[i].getName());


            //添加List项
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), listItems, R.layout.dialog_line,
                new String[]{"icon", "fileName"}, new int[]{R.id.icon, R.id.file_name});
        //为ListView设置Adapter
        listView.setAdapter(simpleAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_res_parent:
                try {
                    if (!currentParent.getCanonicalPath().equals("/mnt/sdcard")) {
                        //获取上级目录
                        currentParent = currentParent.getParentFile();
                        //列出当前目录下所有文件
                        currentFiles = currentParent.listFiles();
                        //再次更新ListView
                        inflateListView(currentFiles);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public interface ResCallBack {
        //根据形影的路径更新固件
        void updateBin(String path);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        main.onDialogRS();
    }
}
