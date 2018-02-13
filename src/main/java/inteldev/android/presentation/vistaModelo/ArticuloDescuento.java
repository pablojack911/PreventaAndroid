package inteldev.android.presentation.vistaModelo;

import java.util.Date;

/**
 * Created by Pocho on 04/04/2017.
 */

public class ArticuloDescuento
{
    private String idCabBonif;
    private String idArticulo;
    private String nombre;
    private float porcentaje;
    private float precio;
    private Date vencimiento;

    public ArticuloDescuento()
    {
    }

    public Date getVencimiento()
    {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento)
    {
        this.vencimiento = vencimiento;
    }

    public String getIdCabBonif()
    {
        return idCabBonif;
    }

    public void setIdCabBonif(String idCabBonif)
    {
        this.idCabBonif = idCabBonif;
    }

    public String getIdArticulo()
    {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo)
    {
        this.idArticulo = idArticulo;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public float getPorcentaje()
    {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje)
    {
        this.porcentaje = porcentaje;
    }

    public float getPrecio()
    {
        return precio;
    }

    public void setPrecio(float precio)
    {
        this.precio = precio;
    }
}
