package com.creative.share.apps.wash_squad_driver.services;


import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.models.PlaceGeocodeData;
import com.creative.share.apps.wash_squad_driver.models.PlaceMapDetailsData;
import com.creative.share.apps.wash_squad_driver.models.Resson_Model;
import com.creative.share.apps.wash_squad_driver.models.UserModel;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {



    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @FormUrlEncoded
    @POST("api/driver/login")
    Call<UserModel> login(
            @Field("username") String username,
            @Field("password")String password
    );
    @FormUrlEncoded
    @POST("api/driver/logout")
    Call<ResponseBody> logout(
            @Field("user_id") int user_id
    );
    @FormUrlEncoded
    @POST("api/orders")
    Call<Order_Model> MyOrder(
            @Field("page")int page,
            @Field("user_id") int user_id,
            @Field("status")int status
    );
    @FormUrlEncoded
    @POST("api/current-orders")
    Call<Order_Model> MyOrder(
            @Field("page")int page,
            @Field("user_id") int user_id

    );
    @GET("api/cancelReasons")
    Call<Resson_Model> getreasson();
    @FormUrlEncoded
    @POST("api/driver/order/cancel")
    Call<ResponseBody> Csncel_order(
            @Field("order_id") int order_id,
            @Field("cancel_reason_id")int cancel_reason_id
    );
    @Multipart
    @POST("api/driver/order/start")
    Call<ResponseBody> Step1
            (@Part("order_id") RequestBody id_part,
             @Part("start_time_work") RequestBody time_part,
             @Part List<MultipartBody.Part> partimageInsideList,
             @Part List<MultipartBody.Part> partimageOutsideList);
    @Multipart
    @POST("api/driver/order/end")
    Call<ResponseBody> Step2(
            @Part("order_id") RequestBody id_part,
            @Part("end_time_work") RequestBody time_part,

            @Part("feed_back")  RequestBody feed_part,
           @Part List<MultipartBody.Part> partimageInsideList,
            @Part List<MultipartBody.Part> partimageOutsideList);
}


