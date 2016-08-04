package com.zhj.retrofitwithrxjavademo.http;

import com.zhj.retrofitwithrxjavademo.entity.HttpResult;
import com.zhj.retrofitwithrxjavademo.entity.ListDayDiscount;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hjzhang on 2016/7/26.
 */
public interface APIService {
    @GET("GetDayDiscountList")
    Observable<HttpResult<ListDayDiscount>> getDayDiscount(@Query("param") String param);

    @FormUrlEncoded
    @POST("GetDayDiscountList")
    Observable<HttpResult<ListDayDiscount>> getDayDiscountPost(@Field("param") String param);
}
