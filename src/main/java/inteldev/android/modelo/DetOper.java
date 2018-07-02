package inteldev.android.modelo;

/**
 * Created by Operador on 13/05/14.
 */
public class DetOper extends BaseModel {

    public String claveUnica;
    public String idArticulo;
    public float precio;
    public float tasaIva;
    public float impInternos;
    public float descuento;
    public int entero;
    public int fraccion;
    public int enviado;
    public String idFila;
    public float tasa_iibb;
    public String oferta;
    public int sincargo;
    public int unidadVenta;

    public DetOper() {
        this.setKey("claveUnica");
    }

    public String getIdArticulo() {
        return idArticulo;
    }
}
