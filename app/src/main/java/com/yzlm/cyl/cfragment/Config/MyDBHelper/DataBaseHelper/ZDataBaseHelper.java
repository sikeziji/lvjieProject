package com.yzlm.cyl.cfragment.Config.MyDBHelper.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by zwj on 2017/6/27.
 */

@SuppressWarnings("unchecked")
public abstract class ZDataBaseHelper {
    private ZDataBaseHelper.DBHelper mDbHelper;
    private SQLiteDatabase mDb;
    private int mDbVersion;
    private String mDbName;
    private String[] mDbCreateSql;

    private String dirName = null;
    private Context mContext = null;

    protected abstract int getMDbVersion(Context var1);

    protected abstract String getDbName(Context var1);

    protected abstract String getDbPath(Context var1);

    protected abstract String[] getDbCreateSql(Context var1);

    protected abstract String[] getDbUpdateSql(int oldVersion, int newVersion);

    protected abstract String[] getDbDowngradeSql(int oldVersion, int newVersion);



    protected ZDataBaseHelper(Context context) {
        this.mContext = context;
        this.mDbVersion = this.getMDbVersion(context);
        this.mDbName = this.getDbName(context);
        this.dirName = this.getDbPath(context);
        this.mDbCreateSql = this.getDbCreateSql(context);

        if (this.dirName == null) {
            this.mDbHelper = new ZDataBaseHelper.DBHelper(context, this.mDbName, (SQLiteDatabase.CursorFactory) null, this.mDbVersion);
        } else {
            ZDataBaseHelper.DatabaseContext dc = new ZDataBaseHelper.DatabaseContext(context, this.dirName);
            this.mDbHelper = new ZDataBaseHelper.DBHelper(dc, this.mDbName, (SQLiteDatabase.CursorFactory) null, this.mDbVersion);
        }

    }

    protected synchronized void open() {
        this.mDb = this.mDbHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getDB() {
        return this.mDb;
    }

    public void close() {
        this.mDb.close();
        this.mDbHelper.close();
    }

    private void ContentValuesPut(ContentValues contentValues, String key, Object value) {
        if (value == null) {
            contentValues.put(key, "");
        } else {
            String className = value.getClass().getName();
            if (className.equals("java.lang.String")) {
                contentValues.put(key, value.toString());
            } else if (className.equals("java.lang.Integer")) {
                contentValues.put(key, Integer.valueOf(value.toString()));
            } else if (className.equals("java.lang.Float")) {
                contentValues.put(key, Float.valueOf(value.toString()));
            } else if (className.equals("java.lang.Double")) {
                contentValues.put(key, Double.valueOf(value.toString()));
            } else if (className.equals("java.lang.Boolean")) {
                contentValues.put(key, Boolean.valueOf(value.toString()));
            } else if (className.equals("java.lang.Long")) {
                contentValues.put(key, Long.valueOf(value.toString()));
            } else if (className.equals("java.lang.Short")) {
                contentValues.put(key, Short.valueOf(value.toString()));
            }
        }

    }

    public boolean insert(String tableName, String[] columns, Object[] values) {
        ContentValues contentValues = new ContentValues();

        for (int rowId = 0; rowId < columns.length; ++rowId) {
            this.ContentValuesPut(contentValues, columns[rowId], values[rowId]);
        }

        long var7 = this.mDb.insert(tableName, (String) null, contentValues);
        return var7 != -1L;
    }

    public boolean insert(String tableName, Map<String, Object> columnValues) {
        ContentValues contentValues = new ContentValues();
        Iterator iterator = columnValues.keySet().iterator();

        while (iterator.hasNext()) {
            String rowId = (String) iterator.next();
            this.ContentValuesPut(contentValues, rowId, columnValues.get(rowId));
        }

        long rowId1 = this.mDb.insert(tableName, (String) null, contentValues);
        return rowId1 != -1L;
    }

    private String initWhereSqlFromArray(String[] whereColumns) {
        StringBuilder whereStr = new StringBuilder();

        for (int i = 0; i < whereColumns.length; ++i) {
            whereStr.append(whereColumns[i]).append(" = ? ");
            if (i < whereColumns.length - 1) {
                whereStr.append(" and ");
            }
        }

        return whereStr.toString();
    }

    private Map<String, Object> initWhereSqlFromMap(Map<String, String> whereParams) {
        Set set = whereParams.keySet();
        String[] temp = new String[whereParams.size()];
        int i = 0;
        Iterator iterator = set.iterator();

        StringBuffer whereStr;
        for (whereStr = new StringBuffer(); iterator.hasNext(); ++i) {
            String result = (String) iterator.next();
            whereStr.append(result).append(" = ? ");
            temp[i] = (String) whereParams.get(result);
            if (i < set.size() - 1) {
                whereStr.append(" and ");
            }
        }

        HashMap var8 = new HashMap();
        var8.put("whereSql", whereStr);
        var8.put("whereSqlParam", temp);
        return var8;
    }

    public boolean update(String tableName, String[] columns, Object[] values, String[] whereColumns, String[] whereArgs) {
        ContentValues contentValues = new ContentValues();

        for (int whereClause = 0; whereClause < columns.length; ++whereClause) {
            this.ContentValuesPut(contentValues, columns[whereClause], values[whereClause]);
        }

        String var9 = this.initWhereSqlFromArray(whereColumns);
        int rowNumber = this.mDb.update(tableName, contentValues, var9, whereArgs);
        return rowNumber > 0;
    }

    public boolean update(String tableName, Map<String, Object> columnValues, Map<String, String> whereParam) {
        ContentValues contentValues = new ContentValues();
        Iterator iterator = columnValues.keySet().iterator();

        while (iterator.hasNext()) {
            String columns = (String) iterator.next();
            this.ContentValuesPut(contentValues, columns, columnValues.get(columns));
        }

        Map map = this.initWhereSqlFromMap(whereParam);
        int rowNumber = this.mDb.update(tableName, contentValues, map.get("whereSql").toString(), (String[]) ((String[]) map.get("whereSqlParam")));
        return rowNumber > 0;
    }

    public boolean delete(String tableName, String[] whereColumns, String[] whereParam) {
        String whereStr = this.initWhereSqlFromArray(whereColumns);
        int rowNumber = this.mDb.delete(tableName, whereStr, whereParam);
        return rowNumber > 0;
    }

    public boolean delete(String tableName, Map<String, String> whereParams) {
        Map map = this.initWhereSqlFromMap(whereParams);
        int rowNumber = this.mDb.delete(tableName, map.get("whereSql").toString(), (String[]) ((String[]) map.get("whereSqlParam")));
        return rowNumber > 0;
    }

    public List<Map> queryListMap(String sql, String[] params) {

        ArrayList list = new ArrayList();
        Cursor cursor = null;
        try{

            cursor = this.mDb.rawQuery(sql, params);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(cursor==null){
            return list;
        }

        try {

            int columnCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                HashMap item = new HashMap();

                for (int i = 0; i < columnCount; ++i) {
                    int type = cursor.getType(i);
                    switch (type) {
                        case 0:
                            item.put(cursor.getColumnName(i), (Object) null);
                            break;
                        case 1:
                            item.put(cursor.getColumnName(i), Integer.valueOf(cursor.getInt(i)));
                            break;
                        case 2:
                            item.put(cursor.getColumnName(i), Float.valueOf(cursor.getFloat(i)));
                            break;
                        case 3:
                            item.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                }
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            saveExceptInfo2File(sql + params[0]);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        cursor.close();
        return list;
    }

    public Map queryItemMap(String sql, String[] params) {
        Cursor cursor = this.mDb.rawQuery(sql, params);
        HashMap map = new HashMap();
        if (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); ++i) {
                int type = cursor.getType(i);
                switch (type) {
                    case 0:
                        map.put(cursor.getColumnName(i), (Object) null);
                        break;
                    case 1:
                        map.put(cursor.getColumnName(i), Integer.valueOf(cursor.getInt(i)));
                        break;
                    case 2:
                        map.put(cursor.getColumnName(i), Float.valueOf(cursor.getFloat(i)));
                        break;
                    case 3:
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                }
            }
        }

        cursor.close();
        return map;
    }

    public void execSQL(String sql) {
        this.mDb.execSQL(sql);
    }

    public void execSQL(String sql, Object[] params) {
        this.mDb.execSQL(sql, params);
    }

    class DatabaseContext extends ContextWrapper {
        public DatabaseContext(Context base) {
            super(base);
            ZDataBaseHelper.this.mContext = base;
        }

        DatabaseContext(Context base, String path) {
            super(base);
            ZDataBaseHelper.this.mContext = base;
            ZDataBaseHelper.this.dirName = path;
        }

        public File getDatabasePath(String name) {
            String dbDir = ZDataBaseHelper.this.dirName;
            String dbPath = dbDir + File.separator + name + File.separator;
            File dirFile = new File(dbDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            boolean isFileCreateSuccess = false;
            File dbFile = new File(dbPath);
            if (!dbFile.exists()) {
                try {
                    isFileCreateSuccess = dbFile.createNewFile();
                } catch (IOException var8) {
                    var8.printStackTrace();
                }
            } else {
                isFileCreateSuccess = true;
            }

            return isFileCreateSuccess ? dbFile : null;
        }

        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
            return SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath(name), (SQLiteDatabase.CursorFactory) null);
        }

        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
            return SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath(name), (SQLiteDatabase.CursorFactory) null);
        }
    }

    private class DBHelper extends SQLiteOpenHelper {
        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            String[] arr = ZDataBaseHelper.this.mDbCreateSql;

            for (String sql : arr) {
                db.execSQL(sql);
            }

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            String[] arr =getDbUpdateSql(oldVersion, newVersion);

            for (String sql : arr) {
                db.execSQL(sql);
            }

        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            String[] arr =getDbDowngradeSql(oldVersion, newVersion);
            for (String sql : arr) {
                db.execSQL(sql);
            }
        }
    }

    public long queryItemCount(String sql, String[] params) {


        Cursor cursor = null;
        long count = 0;
        try {
            cursor = this.mDb.rawQuery(sql, params);
            cursor.moveToFirst();
            count = cursor.getLong(0);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return count;
    }


}



