package org.activity.promofire.entity;

import java.io.Serializable;

/**
 * Created by DESARROLLO on 18/02/17.
 */

public class DetalleServicio implements Serializable {


    public Long idDetalleServicio;
    public String urlPhoto;
    public Integer principal;
    //public String informacion;
    public Integer precio;
    public Producto producto;
    public Servicio servicio;
}
