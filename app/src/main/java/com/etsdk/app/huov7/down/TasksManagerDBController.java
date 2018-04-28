package com.etsdk.app.huov7.down;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.liang530.application.BaseApplication;
import com.liang530.log.L;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.ArrayList;
import java.util.List;

public class TasksManagerDBController {
    public final static String TABLE_NAME = "tasksmanger";
    private TasksManagerDBOpenHelper openHelper;
    SQLiteDatabase db=null;
    public TasksManagerDBController() {
        openHelper = new TasksManagerDBOpenHelper(BaseApplication.getInstance());
        db = openHelper.getWritableDatabase();//频繁的打开连接数据库导致耗时卡顿
    }

    /**
     * 可以从数据库中获得点击了下载的任务，再根据id到下载管理里面去判断下载状态
     * @return
     */
    public List<TasksManagerModel> getAllTasks() {
        Cursor c=null;
        List<TasksManagerModel> list = new ArrayList<>();
        try {
            if(db == null || !db.isOpen()) {
                db = openHelper.getWritableDatabase();
            }
            c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (!c.moveToLast()) {
                return list;
            }
            do {
                TasksManagerModel model = dbToBean(c);
                list.add(model);
            } while (c.moveToPrevious());
        } finally {
            if (c != null) {
                c.close();
//                db.close();
            }
        }
        return list;
    }
    public TasksManagerModel getTaskModelByGameId(String gameId){
        if(gameId==null) return null;
        StringBuffer sql=new StringBuffer("select * from ").append(TABLE_NAME).append(" where gameId=?");
        Cursor c=null;
        TasksManagerModel model =null;
        try{
            if(db == null || !db.isOpen()) {
                db = openHelper.getWritableDatabase();
            }
            c = db.rawQuery(sql.toString(), new String[]{gameId});
            while (c.moveToNext()){
                model=dbToBean(c);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(c!=null) c.close();
//            if(db!=null) db.close();
        }
        return model;
    }
    public TasksManagerModel getTaskModelByKeyVal(String key,String value){
        if(value==null) return null;
        StringBuffer sql=new StringBuffer("select * from ")
                .append(TABLE_NAME)
                .append(" where ")
                .append(key).append("=?");
        L.d("TasksManagerDBController","getTaskModelByKeyVal-sql="+sql.toString());
        TasksManagerModel model = null;
        try {
            if(db == null || !db.isOpen()) {
                db = openHelper.getWritableDatabase();
            }
            Cursor c = db.rawQuery(sql.toString(), new String[]{value});
            model = null;
            while (c.moveToNext()){
                model=dbToBean(c);
            }
            c.close();
//            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
    private TasksManagerModel dbToBean(Cursor c){
        TasksManagerModel model=new TasksManagerModel();
        model.setId(c.getInt(c.getColumnIndex(TasksManagerModel.ID)));
        model.setGameSize(c.getString(c.getColumnIndex(TasksManagerModel.GAME_SIZE)));
        model.setUrl(c.getString(c.getColumnIndex(TasksManagerModel.URL)));
        model.setPath(c.getString(c.getColumnIndex(TasksManagerModel.PATH)));
        model.setGameId(c.getString(c.getColumnIndex(TasksManagerModel.GAME_ID)));
        model.setGameName(c.getString(c.getColumnIndex(TasksManagerModel.GAME_NAME)));
        model.setGameIcon(c.getString(c.getColumnIndex(TasksManagerModel.GAME_ICON)));
        model.setPackageName(c.getString(c.getColumnIndex(TasksManagerModel.PACKAGE_NAME)));
        model.setOnlyWifi(c.getInt(c.getColumnIndex(TasksManagerModel.ONLY_WIFI)));
        model.setUserPause(c.getInt(c.getColumnIndex(TasksManagerModel.USER_PAUSE)));
        model.setInstalled(c.getInt(c.getColumnIndex(TasksManagerModel.INSTALLED)));
        model.setGameType(c.getString(c.getColumnIndex(TasksManagerModel.GAME_TYPE)));
        return model;
    }
    /**
     * 添加新的下载任务
     * @param gameId
     * @param gameName
     * @param gameIcon
     * @param url
     * @param path
     * @return
     */
    public TasksManagerModel addTask( String gameId, String gameName, String gameIcon, String url,  String path,int onlyWifi, String gameType) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }
        // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
        final int id = FileDownloadUtils.generateId(url, path);

        TasksManagerModel model = new TasksManagerModel();
        model.setId(id);
        model.setUrl(url);
        model.setPath(path);
        model.setGameId(gameId);
        model.setGameName(gameName);
        model.setGameIcon(gameIcon);
        model.setInstalled(0);
        model.setOnlyWifi(onlyWifi);
        model.setGameType(gameType);

        boolean succeed=false;
        try {
            if(db == null || !db.isOpen()) {
                db = openHelper.getWritableDatabase();
            }
            succeed = db.insert(TABLE_NAME, null, model.toContentValues()) != -1;
//            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return succeed ? model : null;
    }

    /**
     * 添加新的下载任务，保存到默认目录
     * @param gameId
     * @param gameName
     * @param gameIcon
     * @param url
     * @return
     */
    public TasksManagerModel addTask( String gameId, String gameName, String gameIcon, String url,int onlyWifi, String gameType) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return addTask(gameId,gameName,gameIcon,url,FileDownloadUtils.getDefaultSaveFilePath(url),onlyWifi, gameType);
    }
    public boolean updateTask(TasksManagerModel tasksManagerModel){
        String[] args={String.valueOf(tasksManagerModel.getId())};
        if(db == null || !db.isOpen()) {
            db = openHelper.getWritableDatabase();
        }
        return db.update(TABLE_NAME, tasksManagerModel.toContentValues(), "id=?", args)>0;
    }
    public boolean deleteTaskById(int id){
        String[] args={String.valueOf(id)};
        try {
            if(db == null || !db.isOpen()) {
                db = openHelper.getWritableDatabase();
            }
            return db.delete(TABLE_NAME,"id=?",args)>0;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(db!=null){
//                db.close();
            }
        }
        return false;
    }
    public boolean deleteTaskByGameId(String gameId){
        try {
            String[] args={gameId};
            if(db == null || !db.isOpen()) {
                db = openHelper.getWritableDatabase();
            }
            return db.delete(TABLE_NAME,"gameId=?",args)>0;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(db!=null){
//                db.close();
            }
        }
        return false;
    }

}