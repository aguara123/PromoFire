package org.activity.promofire.entity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESARROLLO on 23/01/17.
 */
public class Servicio implements Serializable {

    public Long idServicio;
    public String fechaIngreso;
    public String fechaDesde;
    public String fechaHasta;
    public String informacion;
    public Integer precio;
    public Double latitud;
    public Double longitud;
    public String localComercial;
    public String situacion;
    public ArrayList<DetalleServicio> detalleServicioList = new ArrayList<>();
    public TipoServicio tipoServicio;

}
