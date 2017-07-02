package org.activity.promofire.connection.callbacks;

import org.activity.promofire.entity.Categoria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CallbackCategory implements Serializable {

    public String status = "";
    public List<Categoria> categories = new ArrayList<>();

}
