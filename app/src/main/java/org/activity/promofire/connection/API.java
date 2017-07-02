package org.activity.promofire.connection;

import org.activity.promofire.connection.callbacks.CallBackMensaje;
import org.activity.promofire.connection.callbacks.CallBackPromocion;
import org.activity.promofire.connection.callbacks.CallBackPromocionDetalle;
import org.activity.promofire.connection.callbacks.CallBackVersion;
import org.activity.promofire.connection.callbacks.CallbackCategory;
import org.activity.promofire.connection.callbacks.CallbackDevice;
import org.activity.promofire.connection.callbacks.CallbackFavorito;
import org.activity.promofire.connection.callbacks.CallbackFeaturedNews;
import org.activity.promofire.connection.callbacks.CallbackInfo;
import org.activity.promofire.connection.callbacks.CallbackNewsInfo;
import org.activity.promofire.connection.callbacks.CallbackNewsInfoDetails;
import org.activity.promofire.connection.callbacks.CallbackOrder;
import org.activity.promofire.connection.callbacks.CallbackProduct;
import org.activity.promofire.connection.callbacks.CallbackProductDetails;
import org.activity.promofire.entity.Checkout;
import org.activity.promofire.entity.DeviceInfo;
import org.activity.promofire.entity.Favoritos;
import org.activity.promofire.entity.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by DESARROLLO on 18/02/17.
 */

public  interface API {

    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "User-Agent: PromoFire";
    String CONTENT_TYPE = "Content-Type: application/json";

    /* Recipe API transaction ------------------------------- */

    @Headers({CACHE, AGENT})
    @GET("services/info")
    Call<CallbackInfo> getInfo(
            @Query("version") int version
    );


    /* News Info API ---------------------------------------------------- */

    @Headers({CACHE, AGENT})
    @GET("services/listFeaturedNews")
    Call<CallbackFeaturedNews> getFeaturedNews();

    @Headers({CACHE, AGENT})
    @GET("services/listNews")
    Call<CallbackNewsInfo> getListNewsInfo(
            @Query("page") int page,
            @Query("count") int count,
            @Query("q") String query
    );

    @Headers({CACHE, AGENT})
    @GET("services/getNewsDetails")
    Call<CallbackNewsInfoDetails> getNewsDetails(
            @Query("id") long id
    );

    /* Category API ---------------------------------------------------
    @Headers({CACHE, AGENT})
    @GET("services/listCategory")
    Call<CallbackCategory> getListCategory();

*/
    /* Product API ---------------------------------------------------- */

    @Headers({CACHE, AGENT})
    @GET("services/listProduct")
    Call<CallbackProduct> getListProduct(
            @Query("page") int page,
            @Query("count") int count,
            @Query("q") String query,
            @Query("category_id") long category_id
    );

    @Headers({CACHE, AGENT})
    @GET("services/getProductDetails")
    Call<CallbackProductDetails> getProductDetails(
            @Query("id") long id
    );

    /* Checkout API ---------------------------------------------------- */
    @Headers({CACHE, AGENT})
    @POST("services/submitProductOrder")
    Call<CallbackOrder> submitProductOrder(
            @Body Checkout checkout
    );

    @GET("detalleservicio/version")
    Call<CallBackVersion> getVersion(
            @Query("version") int version
    );



    /* Fcm API ----------------------------------------------------------- */
    @POST("detalleservicio/insertOneFcm")
    Call<CallbackDevice> registerDevice(
            @Body DeviceInfo deviceInfo
    );

    @GET("detalleservic/premium")
    Call<List<Photo>> premium();

    @GET("detalleservi/normal")
    Call<List<Photo>> normal();

    @POST("detalleservicio/categoria")
    Call<List<Photo>> categoria(@Body String entity);

    @GET("detalleservicio/premium")
    Call<CallBackPromocion> getServicioPremium();

    @GET("detalleservicio/normal")
    Call<CallBackPromocion> getServicioNormal();

    @GET("detalleservicio/categoriaServicios")
    Call<CallBackPromocion> getServicioCategoria(@Query("categoria") int categoria);

    @GET("detalleservicio/servicio")
    Call<CallBackPromocionDetalle> getServicioDetalle(@Query("id") long id);

    @POST("detalleservicio/insertFavorito")
    Call<CallbackFavorito> insertFavorito(
            @Body Favoritos favorito);

    /*Categoria */

    @Headers({CACHE, AGENT})
    @GET("detalleservicio/categorias")
    Call<CallbackCategory> getListCategory();

    @Headers({CACHE, AGENT})
    @GET("detalleservicio/listPromos")
    Call<CallBackPromocion> getListPromo(
            @Query("page") int page,
            @Query("count") int count,
            @Query("q") String query,
            @Query("category_id") long category_id
    );

    @Headers({CONTENT_TYPE})
    @POST("detalleservicio/insertCantVisitas")
    Call<CallBackMensaje> insertCantidadVisitas(@Body String entity);

}