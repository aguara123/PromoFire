package org.activity.promofire.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DESARROLLO on 29/04/17.
 */

public class Usuarios implements Serializable {

    public Integer idUsuario;
    public String usuario;
    public String contrasenha;
    public String estado;
    public String usuAlta;
    public Date fechaAlta;
    public String usuMod;
    public Date fechaMod;
    public String primera;
    public String email;

    public Usuarios(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
