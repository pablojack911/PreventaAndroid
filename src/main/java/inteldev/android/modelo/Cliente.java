package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 7/04/14.
 */
public class Cliente extends BaseModel {

    public String idCliente;
    public String idZona;
    public int idEstClie;
    public String Localidad;
    public int condiva;
    public String nombre;
    public String razonSocial;
    public String domicilio;
    public float saldoCtacte;
    public float limiteCtacte;
    public Date lastUpdate;
    public int Borrado;
    public String idCondVta;
    public String idRamo;
    public int visitado;
    public String lista;
    public float tasaIibb;
    public String canal;
    public String cuit;
    public String telefono;

    public Cliente() {
        this.setKey("idCliente");
    }

    public Cliente(String idCliente, String Nombre, String Domicilio, String Telefono, String Cuit) {
        this.idCliente = idCliente;
        this.nombre = Nombre;
        this.domicilio = Domicilio;
        this.telefono = Telefono;
        this.cuit = Cuit;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCuit() {
        return cuit;
    }
}
