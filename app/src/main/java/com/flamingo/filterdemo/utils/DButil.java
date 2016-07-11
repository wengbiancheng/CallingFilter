package com.flamingo.filterdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq on 2016/5/11.
 */
public class DButil {

    private static final String[] COLS=new String[]{"PhoneNumber"};

    private static String DB="Calling";

    private static String TABLE_USER="BlackPhone";
    public static String create_table_user="create table "+TABLE_USER+
            "(PhoneNumber text);";

    private Context ctx;
    private SQLiteDatabase read_sqlite;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase write_sqlite;

    private int version=1;

    public DButil(Context context){
        this.ctx=context;
        init();
    }

    private void init(){
        if(helper==null){
            helper=new MySQLiteHelper(ctx,DB,null,version);
            write_sqlite=helper.getWritableDatabase();
            read_sqlite=helper.getReadableDatabase();
        }
    }
    public long addPhone(String phoneNumber){

        Cursor cursor=read_sqlite.query(TABLE_USER,COLS, null, null, null, null, null);
        cursor.moveToFirst();

        ContentValues values=new ContentValues();
        values.put("PhoneNumber", phoneNumber);
        return write_sqlite.insert(TABLE_USER,null,values);
    }

    public void delPhone(String phone){
        String []args={phone};
        write_sqlite.delete(TABLE_USER, "PhoneNumber=?", args);
    }

    public List<String> getAllPhone(){

        Cursor cursor=read_sqlite.query(TABLE_USER,COLS, null, null, null, null, null);
        cursor.moveToFirst();

        List<String> list=new ArrayList<String>();
        if(cursor!=null&&cursor.getCount()>0){
            for(int i=0;i<cursor.getCount();i++){
                list.add(cursor.getString(0));
                Log.i("TestActivity", "数据库查找所有的黑名单为:" +i+"--"+cursor.getString(0));
                cursor.moveToNext();
            }
        }
        if(list.size()>0){
            return list;
        }
        return null;
    }

    public void close(){
        if(write_sqlite.isOpen()){
            write_sqlite.close();
        }
        if(read_sqlite.isOpen()){
            write_sqlite.close();
        }
    }


    class MySQLiteHelper extends SQLiteOpenHelper{

        public MySQLiteHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(create_table_user);
            Log.i("TestActivity","创建黑名单的数据库");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
            this.onCreate(db);
        }
    }

}
