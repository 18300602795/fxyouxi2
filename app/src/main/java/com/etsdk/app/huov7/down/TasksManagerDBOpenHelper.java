package com.etsdk.app.huov7.down;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.liang530.log.L;

// ----------------------- model
public class TasksManagerDBOpenHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "tasksmanager.db";
    public final static int DATABASE_VERSION =32;

    public TasksManagerDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        initTable(db);
    }
    private void  initTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TasksManagerDBController.TABLE_NAME
                + String.format(
                "("
                        + "%s INTEGER PRIMARY KEY, " // id, download id
                        + "%s VARCHAR, " // url
                        + "%s VARCHAR, " // path
                        + "%s VARCHAR UNIQUE, " // gameId
                        + "%s VARCHAR, " // gameName
                        + "%s VARCHAR, " // gameIcon
                        + "%s VARCHAR, " // gameSize
                        + "%s VARCHAR UNIQUE," // packageName
                        + "%s INTEGER ," // onlyWifi
                        + "%s INTEGER ," // userPause
                        + "%s INTEGER ," // installed
                        + "%s VARCHAR " // gameType
                        + ")"
                , TasksManagerModel.ID
                , TasksManagerModel.URL
                , TasksManagerModel.PATH
                , TasksManagerModel.GAME_ID
                , TasksManagerModel.GAME_NAME
                , TasksManagerModel.GAME_ICON
                , TasksManagerModel.GAME_SIZE
                , TasksManagerModel.PACKAGE_NAME
                , TasksManagerModel.ONLY_WIFI
                , TasksManagerModel.USER_PAUSE
                , TasksManagerModel.INSTALLED
                , TasksManagerModel.GAME_TYPE

        ));
        //创建索引
        StringBuffer buffer=new StringBuffer("CREATE UNIQUE INDEX IF NOT EXISTS gameIdIndex on ")
                .append(TasksManagerDBController.TABLE_NAME).append("(").append(TasksManagerModel.GAME_ID).append(")");
        db.execSQL(buffer.toString());

        buffer=new StringBuffer("CREATE UNIQUE INDEX IF NOT EXISTS pakageNameIndex on ")
                .append(TasksManagerDBController.TABLE_NAME).append("(").append(TasksManagerModel.PACKAGE_NAME).append(")");
        db.execSQL(buffer.toString());
        L.e("hongliang","创建了新的数据库");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            db.execSQL("DROP TABLE IF  EXISTS "+TasksManagerDBController.TABLE_NAME);
            L.e("hongliang","删除了旧的数据库:");
            initTable(db);
        }
    }
}