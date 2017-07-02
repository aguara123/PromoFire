package org.activity.promofire;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import org.activity.promofire.lib.GlideImageLoader;
import org.activity.promofire.photolist.adapter.CustomAdapterPage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PruebaSliderActivity extends AppCompatActivity {


    CustomAdapterPage customAdapterPage;

    @BindView(R.id.viewPeger)
    ViewPager viewPeger;
    @BindView(R.id.activity_prueba_slider)
    RelativeLayout activityPruebaSlider;


    GlideImageLoader glideImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_slider);
        ButterKnife.bind(this);

        customAdapterPage = new CustomAdapterPage(this);

        viewPeger.setAdapter(customAdapterPage);


    }
}
