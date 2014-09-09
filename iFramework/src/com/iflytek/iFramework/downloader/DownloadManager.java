package com.iflytek.iFramework.downloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xhrong on 2014/9/5.
 */
public class DownloadManager {

    private static final String TAG = "DownloadManager";
    public static final String DWONLOAD_ACTION = "com.iflytek.iFramework.downloader.broadcast";

    private static DownloadManager instance;
    private static DownloadDAO dao;
    private DownloadConfig config;
    private ExecutorService pool;
    private Context context;

    private HashMap<String, DownloadTask> downloadTasks = new HashMap<String, DownloadTask>();
    private HashMap<String, DownloadOperator> taskOperators = new HashMap<String, DownloadOperator>();
    private static Handler handler = new Handler();


    private DownloadManager(Context context) {
        this.context = context;
    }

    public static synchronized DownloadManager getInstance(Context context) {
        if (instance == null) {
            instance = new DownloadManager(context);
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 默认设置
     */
    public void init() {
        init(DownloadConfig.getDefaultDownloadConfig());
    }

    /**
     * 自定义设置
     *
     * @param config
     */
    public void init(DownloadConfig config) {
        if (config == null) {
            this.config = DownloadConfig.getDefaultDownloadConfig();
        } else {
            this.config = config;
        }
        pool = Executors.newFixedThreadPool(config.getMaxDownloadThread());
        dao = DownloadDAO.getInstance(this);
    }

    public DownloadConfig getConfig() {
        return config;
    }

    public void setConfig(DownloadConfig config) {
        this.config = config;
        pool = Executors.newFixedThreadPool(config.getMaxDownloadThread());
    }


    /**
     * 添加下载任务
     *
     * @param newTask
     */
    public void addDownloadTask(DownloadTask newTask) {
        //这里对Task进行了复制，为了不改变传入的任务状态
        DownloadTask task = (DownloadTask) newTask.clone();
        //如果没有下载地下载，就不要添加任务了
        if (TextUtils.isEmpty(task.getUrl())) {
            throw new IllegalArgumentException("task's url cannot be empty");
        }
        //如果没有任务id，就不要下载了，让用户自行添加任务id，有利于用户管理下载任务
        if (TextUtils.isEmpty(task.getId())) {
            throw new IllegalArgumentException("task's id cannot be empty");
        }
        //如果这个任务已经存在了，就不要添加了
        if (taskOperators.containsKey(task.getId())) {
            return;
        }


        Log.v(TAG, "addDownloadTask: " + task.getName());

        //查询是否有下载记录
        DownloadTask historyTask = dao.findDownloadTaskById(task.getId());
        if (historyTask == null) {
            dao.saveDownloadTask(task);
        } else {
            if (historyTask.getStatus() == com.iflytek.iFramework.download.DownloadTask.STATUS_FINISHED
                    || historyTask.getStatus() == com.iflytek.iFramework.download.DownloadTask.STATUS_ERROR
                    || historyTask.getDownloadTotalSize() <= historyTask.getDownloadFinishedSize()) {//如果该任务已经完成了，则重新下载
                task.setDownloadFinishedSize(0);
                task.setDownloadTotalSize(0);
            } else {//否则，继续之后的位置下载
                task.setDownloadFinishedSize(historyTask.getDownloadFinishedSize());
                task.setDownloadTotalSize(historyTask.getDownloadTotalSize());
            }
            dao.updateDownloadTask(task);
        }
        //记录任务
        downloadTasks.put(task.getId(), task);
        DownloadOperator operator = new DownloadOperator(this, task);
        taskOperators.put(task.getId(), operator);
        task.setStatus(DownloadTask.STATUS_PENDDING);
        pool.submit(operator);
    }


    public void pauseDownload(String taskId) {
        Log.v(TAG, "pauseDownload: " + taskId);
        DownloadOperator operator = taskOperators.get(taskId);
        if (operator != null) {
            operator.pauseDownload();
        }
    }

    public void resumeDownload(String taskId) {
        Log.v(TAG, "resumeDownload: " + taskId);
        DownloadOperator operator = taskOperators.get(taskId);
        if (operator != null) {
            operator.resumeDownload();
        }
    }

    public void cancelDownload(String taskId) {
        Log.v(TAG, "cancelDownload: " + taskId);
        DownloadOperator operator = taskOperators.get(taskId);
        if (operator != null) {
            operator.cancelDownload();
        }
    }

    public DownloadTask findDownloadTaskByTaskId(String taskId) {
        if (downloadTasks.containsKey(taskId)) {
            return downloadTasks.get(taskId);
        }
        Log.v(TAG, "findDownloadTaskByAdId from provider");
        return dao.findDownloadTaskById(taskId);
    }

    public List<DownloadTask> getAllDownloadTask() {
        return dao.getAllDownloadTask();
    }


    public void close() {
        pool.shutdownNow();
    }

    /**
     * 更新任务状态
     *
     * @param task
     * @param finishedSize 已经下载大小
     * @param trafficSpeed 下载速度
     */
    void onUpdatedDownloadTask(final DownloadTask task, final long finishedSize, final long trafficSpeed) {
        task.setStatus(DownloadTask.STATUS_RUNNING);
        Log.i(TAG, "finished Size" + finishedSize);
        handler.post(new Runnable() {
            @Override
            public void run() {
                dao.updateDownloadTask(task);
                sendDownloadBroadcast(task);
            }

        });
    }

    void onDownloadStarted(final DownloadTask task) {
        task.setStatus(DownloadTask.STATUS_RUNNING);
        handler.post(new Runnable() {
            @Override
            public void run() {
                dao.updateDownloadTask(task);
                int status = task.getStatus();
                task.setStatus(DownloadTask.STATUS_STARTED);
                sendDownloadBroadcast(task);
                task.setStatus(status);
            }
        });
    }


    void onDownloadPaused(final DownloadTask task) {
        task.setStatus(DownloadTask.STATUS_PAUSED);
        handler.post(new Runnable() {
            @Override
            public void run() {
                dao.updateDownloadTask(task);
                sendDownloadBroadcast(task);
            }
        });
    }

    void onDownloadResumed(final DownloadTask task) {
        task.setStatus(DownloadTask.STATUS_RUNNING);
        handler.post(new Runnable() {
            @Override
            public void run() {
                dao.updateDownloadTask(task);
                int status = task.getStatus();
                task.setStatus(DownloadTask.STATUS_RESUMED);
                sendDownloadBroadcast(task);
                task.setStatus(status);
            }
        });
    }

    void onDownloadCanceled(final DownloadTask task) {
        task.setStatus(DownloadTask.STATUS_CANCELED);
        removeTask(task.getId());
        handler.post(new Runnable() {
            @Override
            public void run() {
                dao.deleteDownloadTask(task);
                sendDownloadBroadcast(task);
            }
        });
    }

    void onDownloadSuccessed(final DownloadTask task) {
        task.setStatus(DownloadTask.STATUS_FINISHED);
        removeTask(task.getId());
        handler.post(new Runnable() {
            @Override
            public void run() {
                dao.updateDownloadTask(task);
                sendDownloadBroadcast(task);
            }

        });
    }

    void onDownloadFailed(final DownloadTask task) {
        task.setStatus(DownloadTask.STATUS_ERROR);
        removeTask(task.getId());
        handler.post(new Runnable() {
            @Override
            public void run() {
                dao.updateDownloadTask(task);
                sendDownloadBroadcast(task);
            }
        });
    }

    void onDownloadRetry(final DownloadTask task) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int status = task.getStatus();
                task.setStatus(DownloadTask.STATUS_RETRY);
                sendDownloadBroadcast(task);
                task.setStatus(status);
            }
        });
    }

    private void removeTask(String taskID) {
        taskOperators.remove(taskID);
        downloadTasks.remove(taskID);
    }

    private void sendDownloadBroadcast(DownloadTask task) {
        Intent intent = new Intent();
        intent.setAction(DWONLOAD_ACTION);
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);//传递过去
    }

}
