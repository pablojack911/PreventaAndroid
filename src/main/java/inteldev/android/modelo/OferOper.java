package inteldev.android.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Operador on 19/05/14.
 */
public class OferOper extends BaseModel implements Parcelable
{
    public static final Creator<OferOper> CREATOR = new Creator<OferOper>()
    {
        @Override
        public OferOper createFromParcel(Parcel in)
        {
            return new OferOper(in);
        }

        @Override
        public OferOper[] newArray(int size)
        {
            return new OferOper[size];
        }
    };
    public String claveUnica;
    public String claveOferta;
    public String idArticulo;
    public float tasaIva;
    public float precio;
    public float impInternos;
    public float descuento;
    public int enviado;
    public String idFila;
    public float tasa_iibb;
    public int cantidad;
    public String tipo;

    public OferOper()
    {
        this.setKey("claveUnica");
    }

    protected OferOper(Parcel in)
    {
        claveUnica = in.readString();
        claveOferta = in.readString();
        idArticulo = in.readString();
        tasaIva = in.readFloat();
        precio = in.readFloat();
        impInternos = in.readFloat();
        descuento = in.readFloat();
        enviado = in.readInt();
        idFila = in.readString();
        tasa_iibb = in.readFloat();
        cantidad = in.readInt();
        tipo = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(claveUnica);
        dest.writeString(claveOferta);
        dest.writeString(idArticulo);
        dest.writeFloat(tasaIva);
        dest.writeFloat(precio);
        dest.writeFloat(impInternos);
        dest.writeFloat(descuento);
        dest.writeInt(enviado);
        dest.writeString(idFila);
        dest.writeFloat(tasa_iibb);
        dest.writeInt(cantidad);
        dest.writeString(tipo);
    }
}
