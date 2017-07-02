package org.activity.promofire.connection.callbacks;

import org.activity.promofire.entity.Servicio;

import java.io.Serializable;

/**
 * Created by DESARROLLO on 11/03/17.
 */

public class CallBackPromocionDetalle implements Serializable {

    public String status = "";
    public Servicio servicio;
}
