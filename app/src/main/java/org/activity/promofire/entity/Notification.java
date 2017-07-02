package org.activity.promofire.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Notification implements Serializable {

    public Long id;
    @Expose
    public String title;
    public String content;
    public String type;
    public Long obj_id;
    @Expose
    public String image;

    // extra attribute
    public Boolean read = false;
    public Long created_at;

}
