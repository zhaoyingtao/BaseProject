package com.changdao.master.common.net.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyt on 2017/11/30.
 * 封装请求参数------只针对post、get请求，上传文件不能使用这个
 */

public class RequestParams {
    private static Map<String, Object> params;

    public RequestParams() {
        if (params != null) {
            params.clear();
        } else {
            params = new HashMap<>();
        }
    }

    /**
     * 添加请求参数参数
     *
     * @param key
     * @param o
     */
    public void addFormPart(String key, Object o) {
        if (params == null || o == null) {
            return;
        }
        params.put(key, o);
    }

    public Map<String, Object> create() {
        return params;
    }

    /**
     * 清除所有的请求参数
     */
    public void removeAllParams() {
        if (params == null) {
            params.clear();
        }
    }
}
