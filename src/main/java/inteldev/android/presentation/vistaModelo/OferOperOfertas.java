package inteldev.android.presentation.vistaModelo;

import android.os.Parcel;

import inteldev.android.modelo.OferOper;

/**
 * Created by Operador on 2/06/14.
 */
public class OferOperOfertas extends OferOper
{
    public static final Creator<OferOperOfertas> CREATOR = new Creator<OferOperOfertas>()
    {
        @Override
        public OferOperOfertas createFromParcel(Parcel in)
        {
            return new OferOperOfertas(in);
        }

        @Override
        public OferOperOfertas[] newArray(int size)
        {
            return new OferOperOfertas[size];
        }
    };
    public int rowIdOferOper;
    public boolean procesado;

    public OferOperOfertas()
    {
        super();
    }

    protected OferOperOfertas(Parcel in)
    {
        super(in);
        rowIdOferOper = in.readInt();
        procesado = in.readByte() != 0;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeInt(rowIdOferOper);
        dest.writeByte((byte) (procesado ? 1 : 0));
    }
}
