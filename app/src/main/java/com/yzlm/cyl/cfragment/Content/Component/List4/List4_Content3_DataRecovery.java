package com.yzlm.cyl.cfragment.Content.Component.List4;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.DataDBHelper;
import com.yzlm.cyl.cfragment.Dialog.Dialog_datetime1;
import com.yzlm.cyl.cfragment.Dialog.Dialog_ok1;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.CFragment.SubFragment;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.blUpdateCKBFlag;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;

public class List4_Content3_DataRecovery extends SubFragment {
    static List4_Content3_DataRecovery fragment = null;
    public DataDBHelper crsj;
    private Kbf mKbf;
    private History mHistory;
    private MyDatabaseHelper dbHelper;
    private String mDbName;
    private String startTime, endTime;
    private List<Map> cursor;
    public static boolean threadRun = false;
    private Callbacks mCallbacks;


    public static List4_Content3_DataRecovery newInstance() {
        if (fragment == null) {
            fragment = new List4_Content3_DataRecovery();
        }
        return fragment;
    }

    public interface Callbacks {
        void onDialogRS();

        void onListSelected(View view);

        void showUpContent();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list4_content3_datarecovery;
    }

    @Override
    protected int getListResId() {
        return 0;
    }

    @Override
    protected void DoThings() {
        try {
            crsj = DataDBHelper.getInstance(context);
            mHistory = new History(context);
            dbHelper = new MyDatabaseHelper(context, "ZT_Water_Data.db", (SQLiteDatabase.CursorFactory) null, 2);
            mKbf = new Kbf(context);
            //还原历史数据
            Button mBtnFlowAdd1 = v.findViewById(R.id.HFLSSJ);
            mBtnFlowAdd1.setOnClickListener(new BtnOnClickListener());
            //根据时间还原历史数据button4
            Button mBtnFlowAdd2 = v.findViewById(R.id.gjsjhflssj);
            mBtnFlowAdd2.setOnClickListener(new BtnOnClickListener());

            ImageButton mBtnReturn = (ImageButton) v.findViewById(R.id.btnReturnFlow);
            mBtnReturn.setOnClickListener(new BtnOnClickListener());
        } catch (Exception e) {
            Toast.makeText(context, context.getResources().getString(R.string.error_data), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.HFLSSJ: //恢复历史数据
                    main.removeDestopText(mfb);
                    Dialog_ok1 stok = new Dialog_ok1();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-ok1", context.getResources().getString(R.string.make_sure_recovery_history));
                    stok.setArguments(bundle);
                    stok.setTargetFragment(List4_Content3_DataRecovery.this, 1);
                    stok.okcz(7);
                    stok.show(fm, "alert-ok1");
                    break;
                case R.id.gjsjhflssj: //根据时间还原历史数据
                    /* 关闭窗体悬浮（WL注释）*/
                    main.removeDestopText(mfb);
                    Dialog_datetime1 st = new Dialog_datetime1();
                    st.setShowPrint(true);
                    st.setTargetFragment(List4_Content3_DataRecovery.this, 1);
                    st.cz(1);
                    st.show(fm, "alert-datetime1");
                    break;
                case R.id.btnReturnFlow:
                    blUpdateCKBFlag = false;
                    threadRun = false;
                    mCallbacks.onListSelected(v);
                    break;
            }
        }
    }

    String path;

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        //根据时间还原历史记录 校准记录
        if (requestCode == 1 || requestCode == 4) {
            String startTime, endTime;
            String stringExtra = data.getStringExtra("Dialog_datetime1");
            if (stringExtra.equals(";")) {

                startTime = null;
                endTime = null;
            } else {
                String[] str = stringExtra.split(";");
                startTime = str[0];
                endTime = str[1];
            }
            if (requestCode == 1) {
                SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                db1.beginTransaction(); //开启事务
                try {
                    String del = "delete from History where component =" + "'" + mCompName + "'" + " and datetime(time) between " + "'" + startTime + "'" + " and " + "'" + endTime + "'";
                    crsj.execSQL(del); //根据时间删除
                    String ins = "insert into History select * from newHistory where component =" + "'" + mCompName + "'" + " and datetime(time) between " + "'" + startTime + "'" + " and " + "'" + endTime + "'";
                    crsj.execSQL(ins);
                    db1.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
                    Toast.makeText(context, context.getResources().getString(R.string.recovery_successful), Toast.LENGTH_SHORT).show();
                    saveOperationLogMsg(mCompName, "恢复数据", ErrorLog.msgType.操作_信息);
                } catch (Exception E) {
                    Toast.makeText(context, context.getResources().getString(R.string.recovery_failed), Toast.LENGTH_SHORT).show();
                } finally {
                    mCallbacks.onDialogRS();
                    db1.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
                }
            }
            if (requestCode == 4) {//按时间还原校准记录
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction(); //开启事务
                try {
                    String del = "delete from KBF where component =" + "'" + mCompName + "'" + " and datetime(time) between " + "'" + startTime + "'" + " and " + "'" + endTime + "'";
                    crsj.execSQL(del); //根据时间删除
                    String ins = "insert into KBF select * from newKBF where component =" + "'" + mCompName + "'" + " and datetime(time) between " + "'" + startTime + "'" + " and " + "'" + endTime + "'";
                    crsj.execSQL(ins);
                    db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
                    Toast.makeText(context, context.getResources().getString(R.string.restore_successful), Toast.LENGTH_SHORT).show();
                    saveOperationLogMsg(mCompName, "时间恢复数据:" + startTime + " -- " + endTime, ErrorLog.msgType.操作_信息);
                } catch (Exception E) {
                    Toast.makeText(context, context.getResources().getString(R.string.restore_failed), Toast.LENGTH_SHORT).show();
                } finally {
                    db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
                }
            }
        }
        //恢复历史数据
        if (requestCode == 7) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction(); //开启事务
            try {
                String[] a = hyxj("History", "newHistory");
                crsj.execSQL(a[0]);
                crsj.execSQL(a[1]);
                db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
                Toast.makeText(context, context.getResources().getString(R.string.recovery_successful), Toast.LENGTH_SHORT).show();
                saveOperationLogMsg(mCompName, "全部恢复数据", ErrorLog.msgType.操作_信息);
            } catch (Exception E) {
                Toast.makeText(context, context.getResources().getString(R.string.restore_failed), Toast.LENGTH_SHORT).show();
            } finally {
                mCallbacks.onDialogRS();
                db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
            }
        }
        //恢复校准数据
        if (requestCode == 8) {
            SQLiteDatabase db1 = dbHelper.getWritableDatabase();
            db1.beginTransaction(); //开启事务
            try {
                String[] a1 = hyxj("KBF", "newKBF");
                crsj.execSQL(a1[0]);
                crsj.execSQL(a1[1]);
                db1.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
                Toast.makeText(context, context.getResources().getString(R.string.recovery_successful), Toast.LENGTH_SHORT).show();
                saveOperationLogMsg(mCompName, "恢复数据", ErrorLog.msgType.操作_信息);
            } catch (Exception E) {
                Toast.makeText(context, context.getResources().getString(R.string.restore_failed), Toast.LENGTH_SHORT).show();
            } finally {
                db1.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
            }
        }
        //显示按钮
        if (requestCode == 2) {
            mCallbacks.onDialogRS();
        }
    }

    /**
     * 导入数据
     * @param excelPath
     */
  /*  public void readExcel(String excelPath, int requestCode, String startTime,String endTime) {
        dbHelper = new MyDatabaseHelper(context, "ZT_Water_Data.db", (SQLiteDatabase.CursorFactory) null, 2);
        SQLiteDatabase  db = dbHelper.getWritableDatabase();
        db.beginTransaction(); //开启事务
        try {
            //表头列名
            List<String> list1 = new ArrayList<String>();
            InputStream input = new FileInputStream(new File(excelPath));
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            // Iterate over each row in the sheet
            Iterator<Row> rows = sheet.rowIterator();

            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator<Cell> cells = row.cellIterator();
                List<String> list = new ArrayList<String>();
                while (cells.hasNext()) {
                    if (row.getRowNum() == 0) {
                        HSSFCell cell = (HSSFCell) cells.next();
                        String mc =cell.toString();
                        String aaa;
                        if(isMatch(mc)){
                            aaa = mc.substring(mc.indexOf("(")+1,mc.indexOf(")"));
                            list1.add(aaa); //列名
                        } else{
                            main.removeDestopText(mfb);
                            Dialog_Err st = new Dialog_Err();
                            Bundle aa = new Bundle();
                            aa.putString("alert-err", context.getString(R.string.data_sjgscw));
                            st.setArguments(aa);
                            st.show(fm, "Dialog_err");
                            return;
                        }
                        //判断表格字段 和 目标数据表的字段是否相同 （）
                        boolean bool = false;
                        if (requestCode == 2 || requestCode == 5) { //History字段
                            cursor = mHistory.selectcolumns();
                        }

                        for (Map item : cursor)  {
                            String a = item.get("name").toString();
                            if (a == aaa || a.equals(aaa)){
                                bool=true;
                                //备份数据
                                if(requestCode == 2 || requestCode == 5){
                                    //导入数据前备份数据
                                    String sql1 ="create table if not exists newHistory(id ,time,flow,component,C,unit,temperature,energy,A,tag)";  //创建
                                    crsj.execSQL(sql1);
                                    String insert ="insert into newHistory select *  from History"; //数据备份
                                    crsj.execSQL(insert);
                                    //删除备份数据库里面重复的数据
                                    String sql2 ="delete from newHistory where newHistory.rowid not in (select MAX(newHistory.rowid) from newHistory group by id)";
                                    crsj.execSQL(sql2);
                                }
                                if (requestCode == 3 || requestCode == 6){
                                    String sql1 ="create table  if not exists newKBF(id ,time,component,range,K,B,F)";  //创建
                                    crsj.execSQL(sql1);
                                    String insert ="insert into newKBF select * from KBF"; //数据备份
                                    crsj.execSQL(insert);
                                    //删除备份数据库里面重复的数据
                                    String sql2 ="delete from newKBF where newKBF.rowid not in (select MAX(newKBF.rowid) from newKBF group by id)";
                                    crsj.execSQL(sql2);
                                }
                                //删除数据
                                //删除原来数据库的所有数据
                                if (requestCode == 2){
                                    crsj.execSQL("delete from History WHERE component =" + "\'" + mCompName + "\'");
                                }
                                if (requestCode== 3){
                                    crsj.execSQL("delete from KBF WHERE component =" + "\'" + mCompName + "\'");
                                }
                                //按时间导入历史数据
                                if (requestCode == 5){
                                    crsj.execSQL("delete from History where component = " + "'" +mCompName+"'" +" and datetime(time) between " + "'" +startTime+"'" + " and " + "'" + endTime + "'");
                                }
                                //按时间导入校准数据
                                if (requestCode == 6){
                                    crsj.execSQL("delete from KBF where component = " + "'" +mCompName+"'" +" and datetime(time) between " + "'" +startTime+"'" + " and " + "'" + endTime + "'");
                                }
                                break;
                            }
                        }
                        if(bool == false){
                            main.removeDestopText(mfb);
                            Dialog_Err st = new Dialog_Err();
                            Bundle aa = new Bundle();
                            aa.putString("alert-err", context.getString(R.string.data_wjcw));
                            st.setArguments(aa);
                            st.show(fm, "Dialog_err");
                            return;
                        }
                    }
                    else{
                        HSSFCell cell = (HSSFCell) cells.next();
                        list.add(cell.toString());
                    }
                }
                //去掉表头
                if (row.getRowNum() != 0) {
                    Map<String, Object> map = new HashMap<>();
                    int j = 0;
                    for(String attribute : list)
                    {
                        j++; //获取到excel有几列数据
                    }
                    for (int i = 0; i<j; i++){
                        map.put(list1.get(i), list.get(i));
                    }
                    if (requestCode == 2){//导入历史
                        crsj.insert("History", map);
                    }
                    if (requestCode == 3){//导入校准
                        crsj.insert("KBF", map);
                    }
                    if (requestCode == 5){ //按时间导入历史
                        crsj.insert("History", map);
                    }
                    if (requestCode == 6){ //按时间导入校准数据
                        crsj.insert("KBF", map);
                    }
                }
            }
            db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle aa = new Bundle();
            aa.putString("alert-err", context.getString(R.string.data_sjdrcg));
            st.setArguments(aa);
            st.show(fm, "Dialog_err");
        } catch (IOException ex) {
            Toast.makeText(context, "导入数据错误" + "!", Toast.LENGTH_SHORT).show();
        }finally{
            db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
        }
    }*/

    /**
     * 截取字符串括号内容
     *
     * @param s
     * @return
     */
    static boolean isMatch(String s) {
        int result1 = s.indexOf("(");
        int result2 = s.indexOf(")");
        if (result1 != -1 && result2 != -1) {
            return true;
        }
        return false;
    }

    /**
     * 还原数据
     *
     * @param tab1 原有表
     * @param tab2 备份表
     */
    public static String[] hyxj(String tab1, String tab2) {
        //1,按组分删除原有数据
        String dele1 = "delete from " + tab1 + "  where component =" + "'" + mCompName + "'";
        //2,从备份表中取出数据还原
        String inse1 = "insert into " + tab1 + " select * from " + tab2 + " where component =" + "'" + mCompName + "'";
        String[] a = new String[2];
        a[0] = dele1;
        a[1] = inse1;
        return a;
    }

    private class MyDatabaseHelper extends SQLiteOpenHelper {
        MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }


}

