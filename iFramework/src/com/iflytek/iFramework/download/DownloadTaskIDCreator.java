package com.iflytek.iFramework.download;

public interface DownloadTaskIDCreator {

    /**
     * 这个接口的要求是：同样的Task生成的ID要一样，否则会重复下载
     * @param task
     * @return
     */
	public String createId(DownloadTask task);

}
