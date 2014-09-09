package com.iflytek.iFrameworkDemo;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.iflytek.iFramework.downloader.DownloadManager;
import com.iflytek.iFramework.downloader.DownloadTask;
import com.iflytek.iFrameworkDemo.download.DownloadTaskAdapter;
import com.iflytek.iFrameworkDemo.download.SourceProvicer;

import java.util.List;

public class MainActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */

    String TAG = "MainActivity";

    TextView text;
    Button startBtn;
    Button pauseBtn;
    Button resumeBtn;
    Button cancelBtn;
    ProgressBar pBar;
    DownloadTask ddtask = new DownloadTask();

    DownloadBroadcastReceiver receiver = new DownloadBroadcastReceiver();

    List<DownloadTask> taskList;
    DownloadTaskAdapter adapter;
    Integer STOP = new Integer(0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  setContentView(R.layout.download_simple_layout);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.DWONLOAD_ACTION);
        registerReceiver(receiver, intentFilter);
        //   init();

        setContentView(R.layout.downloadlib_main_layout);
        taskList = SourceProvicer.getTaskList();
        adapter = new DownloadTaskAdapter(this, taskList);
        setListAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    void init() {

        ddtask.setName("APK");
        ddtask.setUrl("http://static.huaqianapp.com/apk/HuaQian-release.apk");
        //  ddtask.setDownloadSavePath("/mnt/sdcard/");
        ddtask.setId(ddtask.getUrl());
        String customParam = "{\"fileType\":\"zip\"}";
        ddtask.setCustomParam(customParam);

        text = (TextView) findViewById(R.id.textView);
        startBtn = (Button) findViewById(R.id.btnStart);
        pauseBtn = (Button) findViewById(R.id.btnPause);
        resumeBtn = (Button) findViewById(R.id.btnResume);
        cancelBtn = (Button) findViewById(R.id.btnCancel);
        pBar = (ProgressBar) findViewById(R.id.progressBar);


        text.setText(ddtask.getName());


        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.getInstance(getApplicationContext()).pauseDownload(ddtask.getId());
            }
        });

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.getInstance(getApplicationContext()).resumeDownload(ddtask.getId());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.getInstance(getApplicationContext()).cancelDownload(ddtask.getId());
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ddtask.getStatus() == DownloadTask.STATUS_FINISHED || ddtask.getStatus() == DownloadTask.STATUS_ERROR) {//如果结束了，则重新下载
                    ddtask.setStatus(DownloadTask.STATUS_RUNNING);
                }
                DownloadManager.getInstance(getApplicationContext()).addDownloadTask(ddtask);
            }
        });
    }

    class DownloadBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            DownloadTask task = (DownloadTask) intent.getSerializableExtra("task");
            int msgType = task.getStatus();
            //更新数据源
            for (DownloadTask t : taskList) {
                if (t.getId().equals(task.getId())) {
                    t.setCustomParam(task.getCustomParam());
                    t.setDownloadFinishedSize(task.getDownloadFinishedSize());
                    t.setDownloadSavePath(task.getDownloadSavePath());
                    t.setDownloadSpeed(task.getDownloadSpeed());
                    t.setDownloadTotalSize(task.getDownloadTotalSize());
                    t.setStatus(task.getStatus());
                }
            }
            if (msgType == DownloadTask.STATUS_FINISHED) {
                Toast.makeText(context, task.getId() + "下载成功", Toast.LENGTH_LONG).show();
                if (!task.getCustomParam().isEmpty()) {
                    Log.i(TAG, task.getCustomParam());//这里可以做特殊处理了。比如解压、打开
                }
            } else if (msgType == DownloadTask.STATUS_CANCELED) {
                Toast.makeText(context, task.getId() + "取消下载", Toast.LENGTH_LONG).show();
            }
            synchronized (STOP) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
