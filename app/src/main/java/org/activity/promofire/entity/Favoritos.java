package org.activity.promofire.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DESARROLLO on 29/04/17.
 */

public class Favoritos implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer idFavoritos;
    public Date fechaFav;
    public Producto idProducto;
    public Servicio idServicio;
    public Usuarios idUsuario;

    public Favoritos(Producto idProducto, Servicio idServicio, Usuarios idUsuario) {
        this.idProducto = idProducto;
        this.idServicio = idServicio;
        this.idUsuario = idUsuario;
    }
}
