package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 13/05/14.
 */
public class CabOper extends BaseModel {

    public String claveUnica;
    public int idOperacion;
    public String idCliente;
    public Date fecha;
    public Date fechaEntrega;
    public String letra;
    public String sucursal;
    public String numero;
    public String cuit;
    public String razonSocial;
    public String domicilio;
    public float baseImpIIBB;
    public Date lastUpdate;
    public int borrado;
    public int enviado;
    public String idVendedor;
    public String estado;
    public int IdCabBonif;

    public CabOper() {
        this.setKey("claveUnica");
    }
}
