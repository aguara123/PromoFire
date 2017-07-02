package org.activity.promofire.lib;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.activity.promofire.base.ImageLoader;

/**
 * Created by DESARROLLO on 07/01/17.
 */
public class GlideImageLoader implements ImageLoader {
    private RequestManager glideRequestManager;


    public GlideImageLoader(Context context) {
        this.glideRequestManager = Glide.with(context);
    }

    @Override
    public void load(ImageView imageView, String URL) {
       // glideRequestManager
         //       .load(URL)
           //     .diskCacheStrategy(DiskCacheStrategy.ALL)
             //   .override(400, 250)
               // .centerCrop()
                //.into(imageView);

       glideRequestManager.load(URL).override(400, 250).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
}