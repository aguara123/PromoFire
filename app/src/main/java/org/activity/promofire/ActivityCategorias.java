package org.activity.promofire;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.activity.promofire.fragment.FragmentCategory;

public class ActivityCategorias extends AppCompatActivity {

    private SwipeRefreshLayout swipe_refresh;
    public boolean category_load = false;

    static ActivityCategorias activityCategorias;

    public static ActivityCategorias getInstance() {
        return activityCategorias;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        activityCategorias = this;
        initFragment();

        initComponent();

        swipeProgress(true);
    }


    public void initFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // init fragment category
        FragmentCategory fragmentCategory = new FragmentCategory();
        fragmentTransaction.replace(R.id.frame_content_category, fragmentCategory);

        fragmentTransaction.commit();

    }

    public void initComponent() {
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // on swipe list
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFragment();
            }
        });
    }

    private void refreshFragment() {
        category_load = false;
        swipeProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initFragment();
            }
        }, 500);
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

    public void showDataLoaded() {
        if (category_load) {
            swipeProgress(false);
            //Snackbar.make(parent_view, R.string.msg_data_loaded, Snackbar.LENGTH_SHORT).show();
        }
    }
}
