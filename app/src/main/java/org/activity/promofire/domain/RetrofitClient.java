package org.activity.promofire.domain;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by DESARROLLO on 23/01/17.
 */

public class RetrofitClient {

    private Retrofit retrofit;
    private final static String BASE_URL = "http://192.168.1.53:8080/WSPromociones/ws/";

    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public PhotosService getRetrofit() {
        return retrofit.create(PhotosService.class);
    }
}
