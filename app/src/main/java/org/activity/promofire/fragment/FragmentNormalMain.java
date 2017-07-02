package org.activity.promofire.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.activity.promofire.ActivityMain;
import org.activity.promofire.R;
import org.activity.promofire.adapter.AdapterServicioNormal;
import org.activity.promofire.connection.API;
import org.activity.promofire.connection.RestAdapter;
import org.activity.promofire.connection.callbacks.CallBackPromocion;
import org.activity.promofire.connection.callbacks.CallbackCategory;
import org.activity.promofire.entity.Servicio;
import org.activity.promofire.utils.NetworkCheck;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentNormalMain extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;
    private Call<CallbackCategory> callbackCall;
    // private AdapterCategory adapter;

    private AdapterServicioNormal adapter;

    TextView categoriaTxt;

    private Call<CallBackPromocion> callbackCallServicio;

    public static FragmentNormalMain newInstance(Bundle arguments){
        FragmentNormalMain f = new FragmentNormalMain();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public FragmentNormalMain(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_category, null);
        initComponent();
        requestListNormal();

        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        categoriaTxt = (TextView) root_view.findViewById(R.id.categoriaTxt);
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        //set data and list adapter
        adapter = new AdapterServicioNormal(getActivity(), new ArrayList<Servicio>());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.GONE);

        adapter.setOnItemClickListener(new AdapterServicioNormal.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Servicio obj) {
                Snackbar.make(root_view, obj.informacion, Snackbar.LENGTH_SHORT).show();
                //ActivityCategoryDetails.navigate(getActivity(), obj);
            }
        });
    }

    /*
        private void requestListCategory() {
            API api = RestAdapter.createAPI();
            callbackCall = api.getListCategory();
            callbackCall.enqueue(new Callback<CallbackCategory>() {
                @Override
                public void onResponse(Call<CallbackCategory> call, Response<CallbackCategory> response) {
                    CallbackCategory resp = response.body();
                    if (resp != null && resp.status.equals("success")) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setItems(resp.categories);
                        ActivityMain.getInstance().category_load = true;
                        ActivityMain.getInstance().showDataLoaded();
                    } else {
                        onFailRequest();
                    }
                }

                @Override
                public void onFailure(Call<CallbackCategory> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                    if (!call.isCanceled()) onFailRequest();
                }

            });
        }
    */
    private void requestListNormal() {
        Bundle argBundle = getArguments();
        API api = RestAdapter.createAPI();
        callbackCallServicio = api.getServicioCategoria(argBundle.getInt("categoria"));
        callbackCallServicio.enqueue(new Callback<CallBackPromocion>() {
            @Override
            public void onResponse(Call<CallBackPromocion> call, Response<CallBackPromocion> response) {
                CallBackPromocion resp = response.body();
                if (resp != null && resp.status.equals("success")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setItems(resp.servicioList);
                    if (!resp.servicioList.isEmpty()){
                        categoriaTxt.setText(resp.servicioList.get(0).detalleServicioList.get(0).producto.categoria.categoria);
                    }
                    ActivityMain.getInstance().category_load = true;
                    ActivityMain.getInstance().showDataLoaded();
                } else {
                    onFailRequest();
                }
            }

            @Override
            public void onFailure(Call<CallBackPromocion> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                if (!call.isCanceled()) onFailRequest();
            }

        });
    }

    private void onFailRequest() {
        if (NetworkCheck.isConnect(getActivity())) {
            showFailedView(R.string.msg_failed_load_data);
        } else {
            showFailedView(R.string.no_internet_text);
        }
    }

    private void showFailedView(@StringRes int message) {
        ActivityMain.getInstance().showDialogFailed(message);
    }

}
