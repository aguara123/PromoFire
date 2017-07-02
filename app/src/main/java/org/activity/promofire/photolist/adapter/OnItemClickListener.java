package org.activity.promofire.photolist.adapter;

import android.widget.ImageView;

import org.activity.promofire.entity.Photo;

/**
 * Created by DESARROLLO on 07/01/17.
 */

public interface OnItemClickListener {
    void onPlaceClick(Photo servicio);
    void onShareClick(Photo servicio, ImageView img);
    void onDeleteClick(Photo servicio);
    void onClick(Photo photo);
}
