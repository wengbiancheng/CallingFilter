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
 * Created by qq on 2016/7/11.
 */
public class DButil1 {

    private static final String[] COLS=new String[]{"Location"};

    private static String DB="Calling1";

    private static String TABLE_USER="LocationName";
    public static String create_table_user="create table "+TABLE_USER+
            "(Location text);";

    private Context ctx;
    private SQLiteDatabase read_sqlite;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase write_sqlite;

    private int version=1;

    public DButil1(Context context){
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
    public long addLocation(List<String> Location){

        Cursor cursor=read_sqlite.query(TABLE_USER,COLS, null, null, null, null, null);
        cursor.moveToFirst();

        ContentValues values=new ContentValues();
        for(int i=0;i<Location.size();i++){
            values.put("Location", Location.get(i));
            write_sqlite.insert(TABLE_USER,null,values);
        }
        return 1;
    }

    public void delLocation(String Location){
        String []args={Location};
        write_sqlite.delete(TABLE_USER, "Location=?", args);
    }

    public List<String> getAllLocation(){

        Cursor cursor=read_sqlite.query(TABLE_USER,COLS, null, null, null, null, null);
        cursor.moveToFirst();

        List<String> list=new ArrayList<String>();
        if(cursor!=null&&cursor.getCount()>0){
            for(int i=0;i<cursor.getCount();i++){
                list.add(cursor.getString(0));
                Log.i("TestActivity", "数据库查找所有的归属地为:" + i + "--" + cursor.getString(0));
                cursor.moveToNext();
            }
        }
        if(list.size()>0){
            return list;
        }
        return null;
    }
    public void clear(){
        write_sqlite.delete(TABLE_USER,null,null);
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
            Log.i("TestActivity","创建归属地的数据库");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
            this.onCreate(db);
        }
    }

}
