package org.activity.promofire.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DESARROLLO on 07/01/17.
 */

public class Photo {

    @SerializedName("idDetalleServicio")
    @Expose
    private Integer idDetalleServicio;
    @SerializedName("urlPhoto")
    @Expose
    private String urlPhoto;
    @SerializedName("informacion")
    @Expose
    private String informacion;
    @SerializedName("idServicio")
    @Expose
    private Servicio servicio;

    public Integer getIdDetalleServicio() {
        return idDetalleServicio;
    }

    public void setIdDetalleServicio(Integer idDetalleServicio) {
        this.idDetalleServicio = idDetalleServicio;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setIdServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }
}

