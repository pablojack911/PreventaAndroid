package inteldev.android.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Operador on 11/04/14.
 */
public class Ofer_Det extends BaseModel implements Parcelable
{

    public static final Creator<Ofer_Det> CREATOR = new Creator<Ofer_Det>()
    {
        @Override
        public Ofer_Det createFromParcel(Parcel in)
        {
            return new Ofer_Det(in);
        }

        @Override
        public Ofer_Det[] newArray(int size)
        {
            return new Ofer_Det[size];
        }
    };
    public int rowid;
    public String Codigo;
    public String Tipo;
    public String Linea;
    public String Rubro;
    public String Articulo;
    public float Descuento1;
    public float Descuento2;
    public float Descuento3;
    public float Descuento4;
    public int Cpra_Oblig;
    public String Descrip;
    public int D_Cant;
    public int H_Cant;
    public int Multiplo;

    public Ofer_Det()
    {
        this.setKey("rowid");
    }

    protected Ofer_Det(Parcel in)
    {
        rowid = in.readInt();
        Codigo = in.readString();
        Tipo = in.readString();
        Linea = in.readString();
        Rubro = in.readString();
        Articulo = in.readString();
        Descuento1 = in.readFloat();
        Descuento2 = in.readFloat();
        Descuento3 = in.readFloat();
        Descuento4 = in.readFloat();
        Cpra_Oblig = in.readInt();
        Descrip = in.readString();
        D_Cant = in.readInt();
        H_Cant = in.readInt();
        Multiplo = in.readInt();
    }

    public String getArticulo()
    {
        return this.Articulo;
    }

    public float getDescuento1()
    {
        return this.Descuento1;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(rowid);
        dest.writeString(Codigo);
        dest.writeString(Tipo);
        dest.writeString(Linea);
        dest.writeString(Rubro);
        dest.writeString(Articulo);
        dest.writeFloat(Descuento1);
        dest.writeFloat(Descuento2);
        dest.writeFloat(Descuento3);
        dest.writeFloat(Descuento4);
        dest.writeInt(Cpra_Oblig);
        dest.writeString(Descrip);
        dest.writeInt(D_Cant);
        dest.writeInt(H_Cant);
        dest.writeInt(Multiplo);
    }
}
