package inteldev.android.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Articulo
 *
 * @author Gabriel Verdi
 * @version 07 march of 2014
 */
public class Articulo extends BaseModel implements Parcelable
{

    public static final Creator<Articulo> CREATOR = new Creator<Articulo>()
    {
        @Override
        public Articulo createFromParcel(Parcel in)
        {
            return new Articulo(in);
        }

        @Override
        public Articulo[] newArray(int size)
        {
            return new Articulo[size];
        }
    };
    public String idArticulo;
    public String idRubro;
    public String idLinea;
    public String nombre;
    public int tasaIva;
    public int suspendido;
    public int unidadVenta;
    public int minimoVenta;
    public int multiploVenta;
    public int tasaIIBB;
    public int borrado;
    public int usaCanal;
    public float impInternos;
    public Date lastUpdate;

    public Articulo()
    {
        this.setKey("idArticulo");
    }

    protected Articulo(Parcel in)
    {
        idArticulo = in.readString();
        idRubro = in.readString();
        idLinea = in.readString();
        nombre = in.readString();
        tasaIva = in.readInt();
        suspendido = in.readInt();
        unidadVenta = in.readInt();
        minimoVenta = in.readInt();
        multiploVenta = in.readInt();
        tasaIIBB = in.readInt();
        borrado = in.readInt();
        usaCanal = in.readInt();
        impInternos = in.readFloat();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(idArticulo);
        dest.writeString(idRubro);
        dest.writeString(idLinea);
        dest.writeString(nombre);
        dest.writeInt(tasaIva);
        dest.writeInt(suspendido);
        dest.writeInt(unidadVenta);
        dest.writeInt(minimoVenta);
        dest.writeInt(multiploVenta);
        dest.writeInt(tasaIIBB);
        dest.writeInt(borrado);
        dest.writeInt(usaCanal);
        dest.writeFloat(impInternos);
    }
}
