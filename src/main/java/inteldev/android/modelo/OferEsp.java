package inteldev.android.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Operador on 11/04/14.
 */
public class OferEsp extends BaseModel implements Parcelable
{

    public static final Creator<OferEsp> CREATOR = new Creator<OferEsp>()
    {
        @Override
        public OferEsp createFromParcel(Parcel in)
        {
            return new OferEsp(in);
        }

        @Override
        public OferEsp[] newArray(int size)
        {
            return new OferEsp[size];
        }
    };
    public String Codigo;
    public String Nombre;
    public int D_Cant;
    public int H_Cant;
    public String tipo;
    public int CtrlCant;
    public int Base_Dcant;
    public int Base_Hcant;
    public int Comb_Oblig;
    public int Comb_CantB;
    public int Comb_CantC;
    public int Bon_Cada;
    public String Bon_Tipo;
    public int Bon_Cant;
    public float PFinal;
    public int TomaConv;
    public String Lista;

    public OferEsp()
    {
        this.setKey("Codigo");
    }

    protected OferEsp(Parcel in)
    {
        Codigo = in.readString();
        Nombre = in.readString();
        D_Cant = in.readInt();
        H_Cant = in.readInt();
        tipo = in.readString();
        CtrlCant = in.readInt();
        Base_Dcant = in.readInt();
        Base_Hcant = in.readInt();
        Comb_Oblig = in.readInt();
        Comb_CantB = in.readInt();
        Comb_CantC = in.readInt();
        Bon_Cada = in.readInt();
        Bon_Tipo = in.readString();
        Bon_Cant = in.readInt();
        PFinal = in.readFloat();
        TomaConv = in.readInt();
        Lista = in.readString();
    }

    public String getCodigo()
    {
        return this.Codigo;
    }

    public String getNombre()
    {
        return this.Nombre;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(Codigo);
        dest.writeString(Nombre);
        dest.writeInt(D_Cant);
        dest.writeInt(H_Cant);
        dest.writeString(tipo);
        dest.writeInt(CtrlCant);
        dest.writeInt(Base_Dcant);
        dest.writeInt(Base_Hcant);
        dest.writeInt(Comb_Oblig);
        dest.writeInt(Comb_CantB);
        dest.writeInt(Comb_CantC);
        dest.writeInt(Bon_Cada);
        dest.writeString(Bon_Tipo);
        dest.writeInt(Bon_Cant);
        dest.writeFloat(PFinal);
        dest.writeInt(TomaConv);
        dest.writeString(Lista);
    }
}
