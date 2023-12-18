package com.expressvpn.securesafe.AVA_Api_Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AVA_AdsApiInterface {
    @FormUrlEncoded
    @POST("com.expressvpn.securesafe.php")
    Call<Object> getid(@Field("arg1") String str, @Field("arg2") String type);
    @POST("json")
    Call<Object> getIPLocation();
}
