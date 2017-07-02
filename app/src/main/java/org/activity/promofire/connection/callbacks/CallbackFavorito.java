package org.activity.promofire.connection.callbacks;

import org.activity.promofire.entity.Favoritos;

import java.io.Serializable;

/**
 * Created by DESARROLLO on 29/04/17.
 */

public class CallbackFavorito  implements Serializable {

    public String status = "";
    public Favoritos favoritos;
}
