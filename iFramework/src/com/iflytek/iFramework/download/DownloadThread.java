package com.iflytek.iFramework.download;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xhrong on 2014/7/1.
 */
public class DownloadThread extends Thread {

    private static final String TAG = "DownloadThread";
    private File saveFile;
    private URL downUrl;
    private int block;
    private int threadId = -1;
    private int downLength;
    private boolean finish = false;
    private DownloadOperator downloadOperator;


    public DownloadThread(DownloadOperator downloadOperator, URL downUrl, File saveFile, int block, int downLength, int threadId) {
        this.downUrl = downUrl;
        this.saveFile = saveFile;
        this.block = block;
        this.downloadOperator = downloadOperator;
        this.threadId = threadId;
        this.downLength = downLength;
    }

    @Override
    public void run() {
        if(downLength < block){//未下载完成
            try {
                //使用Get方式下载
                HttpURLConnection http = (HttpURLConnection) downUrl.openConnection();
                http.setConnectTimeout(5 * 1000);
                http.setRequestMethod("GET");
                http.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                http.setRequestProperty("Accept-Language", "zh-CN");
                http.setRequestProperty("Referer", downUrl.toString());
                http.setRequestProperty("Charset", "UTF-8");

                int startPos = block * (threadId - 1) + downLength;//开始位置
                int endPos = block * threadId -1;//结束位置
                http.setRequestProperty("Range", "bytes=" + startPos + "-"+ endPos);//设置获取实体数据的范围
                http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                http.setRequestProperty("Connection", "Keep-Alive");

                InputStream inStream = http.getInputStream();
                byte[] buffer = new byte[8096];
                int offset = 0;
                print("Thread " + this.threadId + " start download from position "+ startPos);
                RandomAccessFile threadfile = new RandomAccessFile(this.saveFile, "rwd");
                threadfile.seek(startPos);

                while ((offset = inStream.read(buffer, 0, 8096)) != -1) {
                    threadfile.write(buffer, 0, offset);
                    downLength += offset;

               //     downloadOperator.update(this.threadId, downLength);
                 //   downloadOperator.append(offset);
                }

                threadfile.close();
                inStream.close();
                print("Thread " + this.threadId + " download finish");
                this.finish = true;
            } catch (Exception e) {
                this.downLength = -1;
                print("Thread "+ this.threadId+ ":"+ e);
            }
        }
    }

    /**
     * 打印日志信息
     * @param msg
     */
    private static void print(String msg){
        Log.i(TAG, msg);
    }

    /**
     * 下载是否完成
     * @return
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * 已经下载的内容大小
     * @return 如果返回值为-1,代表下载失败
     */
    public long getDownLength() {
        return downLength;
    }
}
