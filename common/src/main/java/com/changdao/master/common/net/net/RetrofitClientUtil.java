package com.changdao.master.common.net.net;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/21
 * desc   :
 */

public class RetrofitClientUtil {
    private static final int DEFAULT_TIMEOUT = 20;//请求超时时间
    private static OkHttpClient.Builder sOkHttpClient;
    private static Retrofit sRetrofit;
    private static RetrofitClientUtil retrofitClientUtil;

    private RetrofitClientUtil() {
        sOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))//支持https证书
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));
    }

    private static RetrofitClientUtil getSimpleRetrofitClient() {
        if (retrofitClientUtil == null) {
            synchronized (RetrofitClientUtil.class) {
                if (retrofitClientUtil == null) {
                    retrofitClientUtil = new RetrofitClientUtil();
                }
            }
        }
        return retrofitClientUtil;
    }

    /**
     * 获得OkHttpClient.Builder可以添加拦截器等
     *
     * 注意自主添加拦截器等功能后需要手动调用changeApiBaseUrl方法，否则设置无效
     * @return
     */
    public OkHttpClient.Builder getsOkHttpClient() {
        return sOkHttpClient;
    }

    public static RetrofitClientUtil initClient() {
        return getSimpleRetrofitClient();
    }

    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        if (sRetrofit == null) {
            throw new RuntimeException("sRetrofit is null! you should init Retrofit");
        }
        return sRetrofit.create(service);
    }

    /**
     * 改变baseurl
     *
     * @param apiUrl
     */
    public void changeApiBaseUrl(String apiUrl) {
        sRetrofit = new Retrofit.Builder()
                .client(sOkHttpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CustomConverterFactory.create())
                .baseUrl(apiUrl)
                .build();
    }
}
