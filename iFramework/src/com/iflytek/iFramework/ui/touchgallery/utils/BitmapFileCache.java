package com.iflytek.iFramework.ui.touchgallery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.*;

/**
 * Created by xhrong on 2014/9/17.
 */
public class BitmapFileCache {

    private static final String TAG = "BitmapFileCache";

    private static BitmapFileCache fileCache;

    private File cacheDir;

    public static BitmapFileCache getInstance(Context context){
        if(fileCache==null)
            fileCache=new BitmapFileCache(context);
        return fileCache;
    }

    private BitmapFileCache(Context context) {
        // Find the dir to save cached images
        String defaultCacheDir= "";
        if (android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    defaultCacheDir);
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();

        Log.d(TAG, "cache dir: " + cacheDir.getAbsolutePath());
    }

    public File getFile(String key) {
        File f = new File(cacheDir, key);
        if (f.exists()){
            Log.i(TAG, "the file you wanted exists " + f.getAbsolutePath());
            return f;
        }else{
            Log.w(TAG, "the file you wanted does not exists: " + f.getAbsolutePath());
        }

        return null;
    }

    public void put(String key, Bitmap value){
        File f = new File(cacheDir, key);
        if(!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if(saveBitmap(f, value))
            Log.d(TAG, "Save file to sdcard successfully!");
        else
            Log.w(TAG, "Save file to sdcard failed!");
    }

    public File createFile(String key) {
        Log.d(TAG, "cache a file: " + key);
        File f = new File(cacheDir, key);
        if(!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return f;
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }


    private boolean saveBitmap(File file, Bitmap bitmap){
        if(file == null || bitmap == null)
            return false;
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


}
