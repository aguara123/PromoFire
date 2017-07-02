package org.activity.promofire.entity;

import java.io.Serializable;

/**
 * Created by DESARROLLO on 18/02/17.
 */
public class Producto implements Serializable{

    public Integer idProducto;
    public String producto;
    public Categoria categoria;
    private String imagen;

    public Producto(Integer idProducto) {
        this.idProducto = idProducto;
    }
}
