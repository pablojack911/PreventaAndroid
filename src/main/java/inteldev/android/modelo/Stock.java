package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 11/04/14.
 */
public class Stock extends BaseModel {

    public String idArticulo;
    public Date fecha;
    public String idVendedor;
    public int bultos;
    public int unidades;

    public Stock() {
        this.setKey("Stock");
    }
}
