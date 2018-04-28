package com.etsdk.app.huov7.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class SearchHistoryUtil {

    public static List<String> getSearchHistory(SQLiteDatabase db){
        List<String> resault = new ArrayList<>();
        Cursor cursor = db.query(HistoryDBHelper.TABLE, new String[]{"name"}, null, null, null, null, null);
        while(cursor.moveToNext()){
            resault.add(cursor.getString(cursor.getColumnIndex("name")));
//            L.e("查询结果："+cursor.getString(cursor.getColumnIndex("name")));
        }
        return resault;
    }
    public static void addSearchHistory(SQLiteDatabase db, String name){
        ContentValues values = new ContentValues();
        values.put("name", name);
        deleteSearchHistory(db,name);
        long l = db.insert(HistoryDBHelper.TABLE, null, values);
//        L.e("插入数据："+name+", 结果"+l);
    }
    public static void deleteSearchHistory(SQLiteDatabase db, String name){
        db.delete(HistoryDBHelper.TABLE, "name = ?", new String[]{name});
    }
    public static void deleteAllSearchHistory(SQLiteDatabase db){
        long l = db.delete(HistoryDBHelper.TABLE, null, null);
//        L.e("删除结果："+l);
    }
}
