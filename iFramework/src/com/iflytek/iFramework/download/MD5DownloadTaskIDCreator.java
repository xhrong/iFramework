package com.iflytek.iFramework.download;

import com.iflytek.iFramework.download.util.MD5;

public class MD5DownloadTaskIDCreator implements DownloadTaskIDCreator {

	@Override
	public String createId(DownloadTask task) {
        return MD5.getMD5(task.getUrl());
	}

}
