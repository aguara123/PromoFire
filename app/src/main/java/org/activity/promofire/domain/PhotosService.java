package org.activity.promofire.domain;

import org.activity.promofire.entity.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by DESARROLLO on 23/01/17.
 */

public interface PhotosService {
    @GET("detalleservicio/premium")
    Call<List<Photo>> premium();

    @GET("detalleservicio/normal")
    Call<List<Photo>> normal();

    @POST("detalleservicio/categoria")
    Call<List<Photo>> categoria(@Body String entity);

}
