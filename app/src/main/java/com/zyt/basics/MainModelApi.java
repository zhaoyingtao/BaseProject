package com.zyt.basics;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/20
 * desc   :
 */
public interface MainModelApi {
    //获取年级列表
    @GET("sign/grade/list")
    Flowable<JsonObject> getGrades();

    /**
     * 版本检测
     *
     * @param build    版本号id
     * @param platform 平台标志 0:苹果1:安卓2:Phone苹果
     * @return
     */
    @GET("version")
    Flowable<JsonObject> checkVersion(@Query("build") String build,
                                                  @Query("platform") String platform);


    /**
     * 记录用户使用时长使用，客户端每60秒请求一次
     *
     * type   1:余额2:才气值3:积分4:使用时长
     * status 操作状态：1:增加2:减少
     * @return
     */
    @FormUrlEncoded
    @POST("account/time")
    Flowable<JsonObject> uploadUserTime(@FieldMap Map<String,Object> params);

}
