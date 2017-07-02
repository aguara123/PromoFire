package org.activity.promofire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.activity.promofire.connection.API;
import org.activity.promofire.connection.RestAdapter;
import org.activity.promofire.connection.callbacks.CallBackPromocionDetalle;
import org.activity.promofire.connection.callbacks.CallbackFavorito;
import org.activity.promofire.data.DatabaseHandler;
import org.activity.promofire.entity.Cart;
import org.activity.promofire.entity.DetalleServicio;
import org.activity.promofire.entity.Favoritos;
import org.activity.promofire.entity.Producto;
import org.activity.promofire.entity.Servicio;
import org.activity.promofire.entity.Usuarios;
import org.activity.promofire.entity.Wishlist;
import org.activity.promofire.utils.NetworkCheck;
import org.activity.promofire.utils.Tools;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPromocionesDetalle extends AppCompatActivity implements OnMapReadyCallback {

    private static final String EXTRA_OBJECT_ID = "key.EXTRA_OBJECT_ID";
    private static final String EXTRA_FROM_NOTIF = "key.EXTRA_FROM_NOTIF";

    TextView txtPrecio;
    TextView txtProducto;
    TextView txtCategoria;

    TextView txtFeatured;

    private Long idServicio;
    private Boolean from_notif;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private View parent_view;
    private SwipeRefreshLayout swipe_refresh;
    private WebView webview;

    private MenuItem wishlist_menu;

    private Call<CallBackPromocionDetalle> callbackCall = null;

    private GoogleMap mMap;

    Servicio servicio;

    private DatabaseHandler db;
    private boolean flag_wishlist = false;
    private boolean flag_cart = false;

    // activity transition
    public static void navigate(Activity activity, Long id, Boolean from_notif) {
        Intent i = navigateBase(activity, id, from_notif);
        activity.startActivity(i);
    }

    public static Intent navigateBase(Context context, Long id, Boolean from_notif) {
        Intent i = new Intent(context, ActivityPromocionesDetalle.class);
        i.putExtra(EXTRA_OBJECT_ID, id);
        i.putExtra(EXTRA_FROM_NOTIF, from_notif);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info_details);

        db = new DatabaseHandler(this);

        txtPrecio = (TextView) findViewById(R.id.txtPrecio);
        txtProducto = (TextView) findViewById(R.id.txtProducto);
        txtCategoria = (TextView) findViewById(R.id.txtCategoria);
        txtFeatured = (TextView) findViewById(R.id.featured);

        idServicio = getIntent().getLongExtra(EXTRA_OBJECT_ID, -1L);
        from_notif = getIntent().getBooleanExtra(EXTRA_FROM_NOTIF, false);
        initComponent();
        initToolbar();
        requestAction();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
    }


    private void initComponent() {
        parent_view = findViewById(android.R.id.content);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        // on swipe
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestAction();
            }
        });
    }

    private void requestAction() {
        showFailedView(false, "");
        swipeProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestNewsInfoDetailsApi();
            }
        }, 1000);
    }

    private void requestNewsInfoDetailsApi() {
        API api = RestAdapter.createAPI();
        callbackCall = api.getServicioDetalle(idServicio);
        callbackCall.enqueue(new Callback<CallBackPromocionDetalle>() {
            @Override
            public void onResponse(Call<CallBackPromocionDetalle> call, Response<CallBackPromocionDetalle> response) {
                CallBackPromocionDetalle resp = response.body();
                if (resp != null && resp.status.equals("success")) {
                    servicio = resp.servicio;
                    displayPostData();
                    swipeProgress(false);
                } else {
                    onFailRequest();
                }
            }

            @Override
            public void onFailure(Call<CallBackPromocionDetalle> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                if (!call.isCanceled()) onFailRequest();
            }
        });
    }

    private void displayPostData() {
        //((TextView) findViewById(R.id.title)).setText(Html.fromHtml(servicio.informacion));

        webview = (WebView) findViewById(R.id.content);
        String html_data = "<style>img{max-width:100%;height:auto;} iframe{width:100%;}</style> ";
        html_data += servicio.informacion;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings();
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setBackgroundColor(Color.TRANSPARENT);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadData(html_data, "text/html; charset=UTF-8", null);
        // disable scroll on touch
        webview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        ((TextView) findViewById(R.id.date)).setText(servicio.fechaDesde);
        // if (newsInfo.status.equalsIgnoreCase("FEATURED")) {
        ((TextView) findViewById(R.id.featured)).setVisibility(View.VISIBLE);
        //}

        Tools.displayImageOriginal(this, ((ImageView) findViewById(R.id.image)), servicio.detalleServicioList.get(0).urlPhoto);

        ((MaterialRippleLayout) findViewById(R.id.lyt_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> images_list = new ArrayList<>();
                ArrayList<DetalleServicio> detalleServicioList = servicio.detalleServicioList;
                for (DetalleServicio o : detalleServicioList) {
                    images_list.add(o.urlPhoto);
                }

                Intent i = new Intent(ActivityPromocionesDetalle.this, ActivityFullScreenImage.class);
                i.putStringArrayListExtra(ActivityFullScreenImage.EXTRA_IMGS, images_list);
                startActivity(i);
            }
        });

        Snackbar.make(parent_view, R.string.msg_data_loaded, Snackbar.LENGTH_SHORT).show();

        txtPrecio.setText(String.valueOf(servicio.precio));
        txtProducto.setText(servicio.detalleServicioList.get(0).producto.producto);
        txtCategoria.setText(servicio.detalleServicioList.get(0).producto.categoria.categoria);
        txtFeatured.setText(servicio.situacion);

        // analytics track
        PromoFireApp.getInstance().saveLogEvent(servicio.idServicio, servicio.informacion, "NEWS_DETAILS");

        cargarMapa();
    }

    public void cargarMapa() {
        //LatLng localComercial = new LatLng(servicio.latitud, servicio.longitud);
        // mMap.addMarker(new MarkerOptions().position(localComercial).title(servicio.localComercial));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(localComercial));
        LatLng localComercial = new LatLng(servicio.latitud, servicio.longitud);
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(localComercial);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.addMarker(new MarkerOptions().position(localComercial).title(servicio.localComercial));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = (View) findViewById(R.id.lyt_failed);
        View lyt_main_content = (View) findViewById(R.id.lyt_main_content);

        ((TextView) findViewById(R.id.failed_message)).setText(message);
        if (show) {
            lyt_main_content.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            lyt_main_content.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        ((Button) findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction();
            }
        });
    }

    private void onFailRequest() {
        swipeProgress(false);
        if (NetworkCheck.isConnect(this)) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(show);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_product_details, menu);
        wishlist_menu = menu.findItem(R.id.action_wish);
        refreshWishlistMenu();
        return true;
    }

    private void insertFav(Favoritos favoritos) {
        API api = RestAdapter.createAPI();
        Call<CallbackFavorito> callbackCall = api.insertFavorito(favoritos);
        callbackCall.enqueue(new Callback<CallbackFavorito>() {
            @Override
            public void onResponse(Call<CallbackFavorito> call, Response<CallbackFavorito> response) {
                CallbackFavorito resp = response.body();
                if (resp != null && resp.status.equals("success") && resp.favoritos != null) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<CallbackFavorito> call, Throwable t) {
                Log.e("onFailure", t.getMessage());

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == android.R.id.home) {
            onBackAction();
        } else if (item_id == R.id.action_wish) {
            if (servicio == null || servicio.informacion.equals("")) {
                Snackbar.make(parent_view, R.string.cannot_add_wishlist, Snackbar.LENGTH_SHORT).show();
                return true;
            }
            if (flag_wishlist) {
                db.deleteWishlist(idServicio);
                Snackbar.make(parent_view, R.string.remove_wishlist, Snackbar.LENGTH_SHORT).show();
            } else {
                Wishlist w = new Wishlist(servicio.idServicio, servicio.informacion, servicio.detalleServicioList.get(0).urlPhoto, System.currentTimeMillis());
                insertFav(new Favoritos(servicio.detalleServicioList.get(0).producto, servicio, new Usuarios(1)));
                db.saveWishlist(w);
                Snackbar.make(parent_view, R.string.add_wishlist, Snackbar.LENGTH_SHORT).show();
            }
            refreshWishlistMenu();
        } else if (item_id == R.id.action_cart) {
            // Intent i = new Intent(this, ActivityShoppingCart.class);
            //startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onBackAction();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webview != null) webview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webview != null) webview.onPause();
        //refreshCartButton();
    }

    private void onBackAction() {
        if (from_notif) {
            if (ActivityMain.active) {
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), ActivitySplash.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void refreshWishlistMenu() {
        Wishlist w = db.getWishlist(idServicio);
        flag_wishlist = (w != null);
        if (flag_wishlist) {
            wishlist_menu.setIcon(R.drawable.ic_wish);
        } else {
            wishlist_menu.setIcon(R.drawable.ic_wish_outline);
        }
    }

    /*
        private void toggleCartButton() {
            if (flag_cart) {
                db.deleteActiveCart(news_id);
                Snackbar.make(parent_view, R.string.remove_cart, Snackbar.LENGTH_SHORT).show();
            } else {
                // check stock product
                if (product.stock == 0 || product.status.equalsIgnoreCase("OUT OF STOCK")) {
                    Snackbar.make(parent_view, R.string.msg_out_of_stock, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (product.status.equalsIgnoreCase("SUSPEND")) {
                    Snackbar.make(parent_view, R.string.msg_suspend, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Cart cart = new Cart(product.id, product.name, product.image, 1, product.stock, product.price, System.currentTimeMillis());
                db.saveCart(cart);
                Snackbar.make(parent_view, R.string.add_cart, Snackbar.LENGTH_SHORT).show();
            }
            refreshCartButton();
        }

        private void refreshCartButton() {
            Cart c = db.getCart(product_id);
            flag_cart = (c != null);
            if (flag_cart) {
                lyt_add_cart.setBackgroundColor(getResources().getColor(R.color.colorRemoveCart));
                tv_add_cart.setText(R.string.bt_remove_cart);
            } else {
                lyt_add_cart.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tv_add_cart.setText(R.string.bt_add_cart);
            }
        }

    */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }
}
