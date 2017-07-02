package org.activity.promofire.domain;

import org.activity.promofire.entity.Photo;

import java.util.List;

/**
 * Created by DESARROLLO on 23/01/17.
 */

public class PhotoSearch {
    private int count;
    private Photo photo;
    //@SerializedName("idDetalleServicio")
    private List<Photo> photoList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Photo> getPhotos() {
        return this.photoList;
    }

    public void setPhotos(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public Photo getFirstRecipe(){
        Photo first = null;
        if (!photoList.isEmpty()) {
            first = photoList.get(0);
        }
        return first;
    }
}
