package com.iflytek.iFrameworkDemo.http;

import com.iflytek.iFramework.http.AsyncHttpClient;
import com.iflytek.iFramework.http.AsyncHttpResponseHandler;
import org.apache.http.Header;

/**
 * Created by xhrong on 2014/8/27.
 */
public class AsycHttpTest {

    public void makeAsycHttp() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.baidu.com", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
