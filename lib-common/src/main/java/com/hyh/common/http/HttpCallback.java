package com.hyh.common.http;

/**
 * @author Administrator
 * @description
 * @data 2019/1/3
 */

public interface HttpCallback {

    void onResponse(HttpCall call, Response response);

    void onFailure(HttpCall call, Throwable t);

}
