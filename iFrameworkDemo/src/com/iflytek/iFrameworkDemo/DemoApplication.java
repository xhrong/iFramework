package com.iflytek.iFrameworkDemo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.iflytek.iFramework.downloader.DownloadConfig;
import com.iflytek.iFramework.downloader.DownloadManager;
import com.iflytek.iFramework.logger.Logger;
import com.iflytek.iFramework.ui.universalimageloader.core.DisplayImageOptions;
import com.iflytek.iFramework.ui.universalimageloader.core.ImageLoader;
import com.iflytek.iFramework.ui.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by xhrong on 2014/9/2.
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        intiImageLoader(getApplicationContext());

      //  DownloadManager.getInstance(getApplicationContext()).init();//这是默认配置
        initDownloader();//自定义配置
    }

    private void intiImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.camera)
                .showImageForEmptyUri(R.drawable.camera)
                .showImageOnLoading(R.drawable.camera)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initLogger() {
        Logger.debug = true;
        Logger.level = Log.INFO;
    }

    private void initDownloader(){
       DownloadManager downloadMgr = DownloadManager.getInstance(getApplicationContext());

        DownloadConfig.Builder builder = new DownloadConfig.Builder(this);
        String downloadPath = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "download";
        } else {
            downloadPath = Environment.getDataDirectory().getAbsolutePath() + File.separator + "data" + File.separator + getPackageName() + File.separator + "download";
        }
        File downloadFile = new File(downloadPath);
        if(!downloadFile.isDirectory() && !downloadFile.mkdirs()) {
            throw new IllegalAccessError(" cannot create download folder");
        }
        builder.setDownloadSavePath(downloadPath);
        builder.setMaxDownloadThread(3);
        builder.setDownloadDBPath(downloadPath);
        downloadMgr.init(builder.build());
    }
}
