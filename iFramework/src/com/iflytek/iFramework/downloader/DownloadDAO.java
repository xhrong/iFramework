package com.iflytek.iFramework.downloader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhrong on 2014/9/5.
 */
public class DownloadDAO {

    private static DownloadDAO instance;

    private static String DOWNLOAD_TABLE = "tb_download";
    private static String DB_NAME = "download.db";
    private SQLiteDatabase db;

    private DownloadManager downloadManager;

    private DownloadDAO(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
        createDb();
        createTables();
    }

    public static synchronized DownloadDAO getInstance(DownloadManager downloadManager) {
        if (instance == null) {
            instance = new DownloadDAO(downloadManager);
        }
        return instance;
    }

    //TODO:这个地方需要进一步优化处理
    private void createDb() {
        Context context = downloadManager.getContext();
        DownloadConfig config = downloadManager.getConfig();
        if (config.getDownloadDBPath()==null || config.getDownloadDBPath().isEmpty()) {
            db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            if (db == null) {
                throw new IllegalAccessError("cannot create database file of path: " + DB_NAME);
            }
        } else {
            File dbFile = new File(config.getDownloadDBPath());
            if (!dbFile.isFile()) {
                dbFile = new File(config.getDownloadDBPath(), DB_NAME);
            }
            if (!dbFile.getParentFile().isDirectory()) {
                dbFile.getParentFile().mkdirs();
            }
            try {
                dbFile.createNewFile();
                db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalAccessError("cannot create database file of path: " + dbFile.getAbsolutePath());
            }
        }


//        File dbFile = new File(Environment.getExternalStorageDirectory(), DB_NAME);
//        if (dbFile.exists()) {
//            db = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
//        } else {
//            if (!dbFile.getParentFile().isDirectory()) {
//                dbFile.getParentFile().mkdirs();
//            }
//            try {
//                dbFile.createNewFile();
//                db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new IllegalAccessError("cannot create database file of path: " + dbFile.getAbsolutePath());
//            }
//        }
    }

    private void createTables() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE IF NOT EXISTS ").append(DOWNLOAD_TABLE);
        buffer.append("(");
        buffer.append("`").append(DownloadTask.ID).append("` VARCHAR PRIMARY KEY,");
        buffer.append("`").append(DownloadTask.URL).append("` VARCHAR,");
        buffer.append("`").append(DownloadTask.MIMETYPE).append("` VARCHAR,");
        buffer.append("`").append(DownloadTask.SAVEPATH).append("` VARCHAR,");
        buffer.append("`").append(DownloadTask.NAME).append("` VARCHAR,");
        buffer.append("`").append(DownloadTask.FINISHEDSIZE).append("` LONG,");
        buffer.append("`").append(DownloadTask.TOTALSIZE).append("` LONG,");
        buffer.append("`").append(DownloadTask.STATUS).append("` int,");
        buffer.append("`").append(DownloadTask.CUSTOMPARAM).append("` VARCHAR");
        buffer.append(")");
        db.execSQL(buffer.toString());
    }


    public void saveDownloadTask(DownloadTask task) {
        //   printDb();
        ContentValues values = createDownloadTaskValues(task);
        db.insert(DOWNLOAD_TABLE, null, values);
    }

    public void deleteDownloadTask(DownloadTask task) {
        db.delete(DOWNLOAD_TABLE, DownloadTask.ID + "=?", new String[]{task.getId()});
        //    printDb();
    }

    public void updateDownloadTask(DownloadTask task) {
        ContentValues values = createDownloadTaskValues(task);
        db.update(DOWNLOAD_TABLE, values, DownloadTask.ID + "=?", new String[]{task.getId()});
    }


    public DownloadTask findDownloadTaskById(String id) {
        if (TextUtils.isEmpty(id)) {
            return null;
        }
        DownloadTask task = null;
        Cursor cursor = db.query(DOWNLOAD_TABLE, null, DownloadTask.ID + "=?", new String[]{id}, null, null, null);
        if (cursor.moveToNext()) {
            task = restoreDownloadTaskFromCursor(cursor);
        }
        cursor.close();

        return task;
    }

    public DownloadTask findDownloadTask(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        DownloadTask task = null;
        Cursor cursor = db.query(DOWNLOAD_TABLE, columns, selection, selectionArgs, groupBy, having, orderBy);
        if (cursor.moveToNext()) {
            task = restoreDownloadTaskFromCursor(cursor);
        }
        cursor.close();

        return task;
    }


    public List<DownloadTask> getAllDownloadTask() {
        List<DownloadTask> list = new ArrayList<DownloadTask>();
        DownloadTask task = null;
        Cursor cursor = db.query(DOWNLOAD_TABLE, null, null, null, null, null, DownloadTask.STATUS);
        while (cursor.moveToNext()) {
            task = restoreDownloadTaskFromCursor(cursor);
            list.add(task);
        }
        cursor.close();
        return list;
    }

    private ContentValues createDownloadTaskValues(DownloadTask task) {
        ContentValues values = new ContentValues();
        values.put(DownloadTask.ID, task.getId());
        values.put(DownloadTask.URL, task.getUrl());
        values.put(DownloadTask.MIMETYPE, task.getMimeType());
        values.put(DownloadTask.SAVEPATH, task.getDownloadSavePath());
        values.put(DownloadTask.FINISHEDSIZE, task.getDownloadFinishedSize());
        values.put(DownloadTask.TOTALSIZE, task.getDownloadTotalSize());
        values.put(DownloadTask.NAME, task.getName());
        values.put(DownloadTask.STATUS, task.getStatus());
        values.put(DownloadTask.CUSTOMPARAM, task.getCustomParam());
        return values;
    }

    private DownloadTask restoreDownloadTaskFromCursor(Cursor cursor) {
        DownloadTask task = new DownloadTask();
        task.setId(cursor.getString(cursor.getColumnIndex(DownloadTask.ID)));
        task.setName(cursor.getString(cursor.getColumnIndex(DownloadTask.NAME)));
        task.setUrl(cursor.getString(cursor.getColumnIndex(DownloadTask.URL)));
        task.setMimeType(cursor.getString(cursor.getColumnIndex(DownloadTask.MIMETYPE)));
        task.setDownloadSavePath(cursor.getString(cursor.getColumnIndex(DownloadTask.SAVEPATH)));
        task.setDownloadFinishedSize(cursor.getLong(cursor.getColumnIndex(DownloadTask.FINISHEDSIZE)));
        task.setDownloadTotalSize(cursor.getLong(cursor.getColumnIndex(DownloadTask.TOTALSIZE)));
        task.setStatus(cursor.getInt(cursor.getColumnIndex(DownloadTask.STATUS)));
        task.setCustomParam(cursor.getString(cursor.getColumnIndex(DownloadTask.CUSTOMPARAM)));

        return task;
    }

//    private void printDb() {
//        List<DownloadTask> list = getAllDownloadTask();
//        for (int i = 0; i < list.size(); i++) {
//            Log.i("DataContent", list.get(i).toString());
//        }
//    }
}
