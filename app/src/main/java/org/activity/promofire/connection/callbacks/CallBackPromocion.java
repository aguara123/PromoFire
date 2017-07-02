package org.activity.promofire.connection.callbacks;

import org.activity.promofire.entity.Servicio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESARROLLO on 19/02/17.
 */

public class CallBackPromocion implements Serializable {

    public String status = "";
    public List<Servicio> servicioList = new ArrayList<>();

}
