package org.activity.promofire;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.activity.promofire.base.ImageLoader;
import org.activity.promofire.domain.PhotosService;
import org.activity.promofire.domain.RetrofitClient;
import org.activity.promofire.entity.Photo;
import org.activity.promofire.lib.CircleTransform;
import org.activity.promofire.photolist.adapter.OnItemClickListener;
import org.activity.promofire.photolist.adapter.PhotoListAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener, OnItemClickListener {

    @BindView(R.id.sliderPremium)
    SliderLayout sliderPremium;
    @BindView(R.id.custom_indicator)
    PagerIndicator customIndicator;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerViewNornal)
    RecyclerView recyclerViewNornal;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.content)
    CoordinatorLayout content;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.recyclerViewRopa)
    RecyclerView recyclerViewRopa;

    private PhotosService service;
    RetrofitClient client;

    PhotoListAdapter photoListAdapter;
    PhotoListAdapter photoListAdapterRopas;
    LinearLayoutManager horizontalLayoutManager;
    LinearLayoutManager horizontalLayoutManager2;
    ImageLoader imageLoader;

    public static final String AVATAR_URL = "http://lorempixel.com/200/200/people/1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        client = new RetrofitClient();
        sliderPremium.setCustomIndicator(customIndicator);
        setupAdapter();
        setupSlider();
        cargarImagenesPremium();

        initToolbar();
        setupDrawerLayout();

        cargarImagenesNormal();
        cargarImagenesRopa();

        final ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        Picasso.with(this).load(AVATAR_URL).transform(new CircleTransform()).into(avatar);


        //transformers.setAdapter(new TransformerAdapter(this));
        // transformers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //  @Override
        //  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
        //Toast.makeText(MEn.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
        //   }
        // });
        //

    }

    private void setupSlider() {

        sliderPremium.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderPremium.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderPremium.setCustomAnimation(new DescriptionAnimation());
        sliderPremium.setDuration(2000);
        sliderPremium.addOnPageChangeListener(this);
    }

    private void setupDrawerLayout() {

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void initToolbar() {

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void cargarImagenesPremium() {
        service = client.getRetrofit();
        Call<List<Photo>> call = service.premium();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    List<Photo> photoSearch = response.body();
                    if (photoSearch.isEmpty()) {

                    } else {
                        cargarImagenesPremiumList(photoSearch);


                    }
                } else {
                    Toast.makeText(MenuActivity.this, "No Funciono", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "No Funciono " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.v("Prueba", t.getLocalizedMessage());
            }
        });
    }

    public void cargarImagenesNormal() {
        service = client.getRetrofit();
        Call<List<Photo>> call = service.normal();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    List<Photo> photoSearch = response.body();
                    if (photoSearch.isEmpty()) {

                    } else {
                        cargarImagenesNormalList(photoSearch);


                    }
                } else {
                    Toast.makeText(MenuActivity.this, "No Funciono", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "No Funciono " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.v("Prueba", t.getLocalizedMessage());
            }
        });
    }

    public void cargarImagenesRopa() {
        service = client.getRetrofit();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("categoria", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String entidad = jsonObject.toString();
        Call<List<Photo>> call = service.categoria(entidad);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    Log.v("Codigo retorno", response.code() + "");
                    List<Photo> photoSearch = response.body();
                    if (photoSearch.isEmpty()) {

                    } else {
                        cargarImagenesRopasList(photoSearch);


                    }
                } else {
                    Toast.makeText(MenuActivity.this, "No Funciono", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "No Funciono " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.v("Prueba", t.getLocalizedMessage());
            }
        });
    }


    public void cargarImagenesPremiumList(List<Photo> photoList) {
        //HashMap<String, String> url_maps = new HashMap<String, String>();
        //url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        //url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        //url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        //url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for (Photo photo : photoList) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(photo.getInformacion())
                    .image(photo.getUrlPhoto())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", photo.getInformacion());

            sliderPremium.addSlider(textSliderView);
        }

    }

    public void cargarImagenesNormalList(List<Photo> photoList) {

        for (Photo photo : photoList) {
            photoListAdapter.addPhoto(photo);
            //photoListAdapter = new PhotoListAdapter(photoList, imageLoader, this);
            recyclerViewNornal.setLayoutManager(horizontalLayoutManager);
            recyclerViewNornal.setAdapter(photoListAdapter);
        }

    }

    public void cargarImagenesRopasList(List<Photo> photoList) {

        for (Photo photo : photoList) {
            photoListAdapterRopas.addPhoto(photo);
            //photoListAdapter = new PhotoListAdapter(photoList, imageLoader, this);
            recyclerViewRopa.setLayoutManager(horizontalLayoutManager2);
            recyclerViewRopa.setAdapter(photoListAdapterRopas);
        }

    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderPremium.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.drawer_action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupAdapter() {
        PromoFireApp app = (PromoFireApp) getApplication();
        imageLoader = app.getImageLoader();
        List<Photo> photoList = new ArrayList<>();
        List<Photo> photoList2 = new ArrayList<>();
        photoListAdapter = new PhotoListAdapter(photoList, imageLoader, this);
        horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalLayoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photoListAdapterRopas = new PhotoListAdapter(photoList2, imageLoader, this);
    }

    @Override
    public void onPlaceClick(Photo servicio) {

    }

    @Override
    public void onShareClick(Photo servicio, ImageView img) {

    }

    @Override
    public void onDeleteClick(Photo servicio) {

    }

    @Override
    public void onClick(Photo photo) {

    }
}
