package com.iflytek.iFrameworkDemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.iflytek.iFramework.logger.Logger;
import com.iflytek.iFramework.ui.universalimageloader.core.DisplayImageOptions;
import com.iflytek.iFramework.ui.universalimageloader.core.ImageLoader;
import com.iflytek.iFramework.ui.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by xhrong on 2014/9/2.
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        intiImageLoader(getApplicationContext());
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
}
