package com.etsdk.app.huov7.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class HistoryDBHelper extends SQLiteOpenHelper {
    public static String TABLE = "history";

    public HistoryDBHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "history.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE
                + "(_id integer primary key autoincrement,"
                + "name" + " String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
