package org.activity.promofire.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DESARROLLO on 13/02/17.
 */

public class EnvioParametroCategoria {
    @SerializedName("categoria")
    @Expose
    private Integer categoria;

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }
}
