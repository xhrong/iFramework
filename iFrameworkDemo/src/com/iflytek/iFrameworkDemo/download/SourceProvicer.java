package com.iflytek.iFrameworkDemo.download;

import com.iflytek.iFramework.download.DownloadManager;
import com.iflytek.iFramework.download.DownloadTask;

import java.util.ArrayList;

/**
 * Created by xhrong on 2014/6/28.
 */
public class SourceProvicer {



    public static ArrayList<DownloadTask> getTaskList() {

        ArrayList<DownloadTask> ddList=new ArrayList<DownloadTask>();
        DownloadTask task1 = new DownloadTask();
        task1.setName("APK");
        task1.setUrl("http://static.huaqianapp.com/apk/HuaQian-release.apk");
        task1.setDownloadSavePath("/mnt/sdcard/");
        task1.setId(DownloadManager.getInstance().getConfig().getTaskIdCreator().createId(task1));
        ddList.add(task1);
//        DownloadTask task2 = new DownloadTask();
//        task2.setName("IMAGE");
//        task2.setUrl("http://image16-c.poco.cn/mypoco/myphoto/20140609/10/3048611720140609105056075_640.jpg");
//        task2.setDownloadSavePath("/mnt/sdcard/");
//        task2.setId(DownloadManager.getInstance().getConfig().getTaskIdCreator().createId(task2));
//        ddList.add(task2);
//        DownloadTask task3 = new DownloadTask();
//        task3.setName("ZIP");
//        task3.setUrl("http://download.thinkbroadband.com/20MB.zip");
//        task3.setDownloadSavePath("/mnt/sdcard/");
//        task3.setId(DownloadManager.getInstance().getConfig().getTaskIdCreator().createId(task3));
//        ddList.add(task3);
//        DownloadTask task4 = new DownloadTask();
//        task4.setName("MTBK");
//        task4.setUrl("http://download.cycore.cn/3/files/35501ba4b8c841158f266946754bc4bb.mtbk");
//        task4.setDownloadSavePath("/mnt/sdcard/");
//        task4.setId(DownloadManager.getInstance().getConfig().getTaskIdCreator().createId(task4));
//        ddList.add(task4);

        return ddList;
    }
}
