package org.activity.promofire.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {

    public Long id;
    public String name;
    public String image;
    public Double price;
    public Long stock;
    public Integer draft;
    public String description;
    public String status;
    public Long created_at;
    public Long last_update;

    public List<Categoria> categories = new ArrayList<>();
    public List<ProductImage> product_images = new ArrayList<>();

}
