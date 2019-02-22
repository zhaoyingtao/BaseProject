package com.zyt.master.common.net.net;

import android.text.TextUtils;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
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
    private static int default_timeout = 20;//请求超时时间
    private static int connectionPoolNums = 8;//连接池个数
    private static int connectionPoolKeepTime = 15;//连接池保活时间
    private static OkHttpClient.Builder sOkHttpClient;
    private static Retrofit sRetrofit;
    private static RetrofitClientUtil retrofitClientUtil;
    private String baseUrl;
    private Interceptor cookiesInterceptor;

    private RetrofitClientUtil() {
        sOkHttpClient = new OkHttpClient.Builder();
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
     * <p>
     * 注意自主添加拦截器等功能后需要手动调用changeApiBaseUrl方法，否则设置无效
     *
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
     * 设置拦截器
     *
     * @param interceptor
     * @return
     */
    public RetrofitClientUtil useCookiesInterceptor(Interceptor interceptor) {
        cookiesInterceptor = interceptor;
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param outTime 单位秒
     * @return
     */
    public RetrofitClientUtil setRequestOutTime(int outTime) {
        default_timeout = outTime;
        return this;
    }

    /**
     * 连接池个数
     *
     * @param nums 大于0的正数
     * @return
     */
    public RetrofitClientUtil setConnectionPoolNums(int nums) {
        if (nums > 0) {
            connectionPoolNums = nums;
        }
        return this;
    }

    /**
     * 连接池保活时间
     *
     * @param outTime 单位秒
     * @return
     */
    public RetrofitClientUtil setConnectionPoolKeepTime(int outTime) {
        connectionPoolKeepTime = outTime;
        return this;
    }

    /**
     * baseUrl
     *
     * @param baseUrl 请求域名
     * @return
     */
    public RetrofitClientUtil setRequestBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public void build() {
        if (sOkHttpClient == null) {
            sOkHttpClient = new OkHttpClient.Builder();
        }
        if (cookiesInterceptor != null) {//有拦截器
            sOkHttpClient.addInterceptor(cookiesInterceptor);
        }
        sOkHttpClient.connectTimeout(default_timeout, TimeUnit.SECONDS)
                .writeTimeout(default_timeout, TimeUnit.SECONDS)
                .readTimeout(default_timeout, TimeUnit.SECONDS)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))//支持https证书
                .connectionPool(new ConnectionPool(connectionPoolNums, connectionPoolKeepTime, TimeUnit.SECONDS));
        //最后集成
        changeApiBaseUrl(baseUrl);
    }

    /**
     * 改变baseurl
     *
     * @param apiUrl
     */
    public void changeApiBaseUrl(String apiUrl) {
        if (TextUtils.isEmpty(baseUrl)){
            throw new NullPointerException("请求的域名地址不能为null");
        }
        sRetrofit = new Retrofit.Builder()
                .client(sOkHttpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CustomConverterFactory.create())
                .baseUrl(apiUrl)
                .build();
    }
}
