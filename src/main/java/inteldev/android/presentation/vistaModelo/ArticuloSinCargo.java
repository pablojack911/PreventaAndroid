package inteldev.android.presentation.vistaModelo;

import java.util.Date;

/**
 * Created by Pocho on 04/04/2017.
 */

public class ArticuloSinCargo
{
    private int sinCargo;
    private String idCabBonif;
    private String idArticulo;
    private String nombre;
    private Date vencimiento;

    public ArticuloSinCargo()
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

    public int getSinCargo()
    {
        return sinCargo;
    }

    public void setSinCargo(int sinCargo)
    {
        this.sinCargo = sinCargo;
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
}
