package inteldev.android.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Operador on 30/11/2015.
 */
public class PosicionesGPS implements Parcelable
{
    public String usuario;
    public String fecha;
    public String imei;
    public float latitud;
    public float longitud;
    public int visitados;
    public int compradores;
    public int bultos;
    public float pesos;
    public int estado;
    // Valores ESTADO
    // ok = 0
    // gps_apagado = 1
    // datos_apagados = 2
    // fuera_de_zona = 3
    // deslogueado = 4
    // no_reporta = 5
    // checkin_cliente = 6
    // checkout_cliente = 7
    public int enviado;
    public String idCliente;
    public int motivoNoCompra;
    // Valores motivoNoCompra
    //-1 no hay nada registrado
    // 0 comprador
    // 1 cerrado
    // 2 no esta el responsable
    // 3 tiene stock
    // 4 no tiene dinero

    public PosicionesGPS()
    {
        this.enviado = 0;
    }

    protected PosicionesGPS(Parcel in)
    {
        usuario = in.readString();
        fecha = in.readString();
        imei = in.readString();
        latitud = in.readFloat();
        longitud = in.readFloat();
        visitados = in.readInt();
        compradores = in.readInt();
        bultos = in.readInt();
        pesos = in.readFloat();
        estado = in.readInt();
        enviado = in.readInt();
        idCliente = in.readString();
        motivoNoCompra = in.readInt();
    }

    public static final Creator<PosicionesGPS> CREATOR = new Creator<PosicionesGPS>()
    {
        @Override
        public PosicionesGPS createFromParcel(Parcel in)
        {
            return new PosicionesGPS(in);
        }

        @Override
        public PosicionesGPS[] newArray(int size)
        {
            return new PosicionesGPS[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(usuario);
        dest.writeString(fecha);
        dest.writeString(imei);
        dest.writeFloat(latitud);
        dest.writeFloat(longitud);
        dest.writeInt(visitados);
        dest.writeInt(compradores);
        dest.writeInt(bultos);
        dest.writeFloat(pesos);
        dest.writeInt(estado);
        dest.writeInt(enviado);
        dest.writeString(idCliente);
        dest.writeInt(motivoNoCompra);
    }
}
