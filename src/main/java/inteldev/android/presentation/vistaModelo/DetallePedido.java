package inteldev.android.presentation.vistaModelo;

/**
 * Created by Operador on 16/05/14.
 */
public class DetallePedido
{
    public DetallePedido()
    {

    }
    public String idArticulo;
    public String nombre;
    public int entero;
    public float precio;
    public float descuento;
    public String idFila;
    public String oferta;
    public String claveUnica;

    public String getIdArticulo(){return idArticulo;}
    public String getNombre(){return nombre;}
    public int getEntero(){return entero;}
    public float getPrecio(){return precio;}
    public float getDescuento(){return descuento;}
    public String getIdFila(){return idFila;}
    public String getOferta(){return oferta;}

}
