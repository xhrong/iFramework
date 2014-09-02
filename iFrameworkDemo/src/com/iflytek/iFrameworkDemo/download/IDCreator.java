package com.iflytek.iFrameworkDemo.download;

import com.iflytek.iFramework.download.DownloadTask;
import com.iflytek.iFramework.download.DownloadTaskIDCreator;

/**
 * Created by xhrong on 2014/6/28.
 */
public class IDCreator implements DownloadTaskIDCreator {

    @Override
    public String createId(DownloadTask task) {
        return task.getUrl();
    }

}
