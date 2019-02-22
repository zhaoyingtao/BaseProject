package com.zyt.master.common.net.net;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.zyt.master.common.service.NetworkUtil;
import com.zyt.master.common.tool.utils.AppUtils;
import com.zyt.master.common.tool.utils.SystemUtil;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/20
 * desc   :数据拦截器
 */

public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
//        ddd(chain);
        final Request.Builder builder = chain.request().newBuilder();
        //添加请求头
        builder.addHeader("client-method", chain.request().method());
        builder.addHeader("client-source", "app");
        builder.addHeader("client-version", AppUtils.init().getAppVersionName(context));
        builder.addHeader("client-platform", "android");
        builder.addHeader("client-network", NetworkUtil.getNetWorkStatus(context));
        builder.addHeader("client-platform_version", Build.VERSION.RELEASE);
        builder.addHeader("client-deviceid", SystemUtil.getDeviceUniqueIndicationCode(context));
        //添加cookie
//        builder.addHeader("client-token", "ss");
        Request request = builder.build();
        //proceed方法只能调用一次，调用多次会请求多次接口
        Response response = chain.proceed(request);
        collectionLog(request, response);
        return response;
    }

    /**
     * 日志收集
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void collectionLog(Request request, Response response) throws IOException {
        /**获取请求接口前的数据 */
        //the request url
        String url = request.url().toString();
        //the request method
        String method = request.method();
        //the request body
        RequestBody requestBody = request.body();
        if (requestBody != null) {//post请求会有请求体
            StringBuilder sb = new StringBuilder("Request Body [");
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            if (isPlaintext(buffer)) {
                sb.append(buffer.readString(charset));
                sb.append(" (Content-Type = ").append(String.valueOf(contentType)).append(",")
                        .append(requestBody.contentLength()).append("-byte body)");
            } else {
                sb.append(" (Content-Type = ").append(String.valueOf(contentType))
                        .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
            }
            sb.append("]");
            log("post参数---------", sb.toString());
        }
        log("url---------", request.url() + "");
        log("header---------", request.headers().toString());
        /**获取请求接口后的数据 ===剔除掉下载的大数据，否则会报oom的*/
        String bodyString = "";
        if (!url.endsWith("mp4") && !url.endsWith("mp3") && !url.endsWith("apk")) {
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
                bodyString = buffer.clone().readString(charset);
                log("请求后的数据==", bodyString);
            }
        } else {
            log("请求后的数据==", url);
        }
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    /**
     * 打印请求链接、请求头、post请求参数、返回结果
     *
     * @param remind
     * @param logData
     */
    private void log(String remind, String logData) {
        Log.e("_api", remind + logData);
    }
}