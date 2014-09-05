package com.iflytek.iFramework.downloader;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by xhrong on 2014/9/5.
 */
public class DownloadConfig {
    private String downloadSavePath;
    private int maxDownloadThread;
    private int retryTime;
    private String downloadDBPath;


    private DownloadConfig() {
        downloadSavePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "download";
        maxDownloadThread = 2;
        retryTime = 2;
    }

    public String getDownloadSavePath() {
        return downloadSavePath;
    }

    public int getMaxDownloadThread() {
        return maxDownloadThread;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public String getDownloadDBPath() {
        return downloadDBPath;
    }


    public static DownloadConfig getDefaultDownloadConfig() {
        DownloadConfig config = new DownloadConfig();
        return config;
    }


    public static class Builder {

        private DownloadConfig config;

        public Builder(Context context) {
            config = new DownloadConfig();
        }

        public DownloadConfig build() {
            return config;
        }

        public Builder setDownloadSavePath(String downloadSavePath) {
            config.downloadSavePath = downloadSavePath;
            return this;
        }

        public Builder setMaxDownloadThread(int maxDownloadThread) {
            config.maxDownloadThread = maxDownloadThread;
            return this;
        }

        public Builder setRetryTime(int retryTime) {
            config.retryTime = retryTime;
            return this;
        }

        public Builder setDownloadDBPath(String downloadDBPath) {
            config.downloadDBPath = downloadDBPath;
            return this;
        }
    }
}
