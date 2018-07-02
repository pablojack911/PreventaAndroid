package inteldev.android.negocios;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import inteldev.android.R;
import inteldev.android.accesoadatos.Dao;

/**
 * Created by Operador on 7/05/14.
 */
public class SpinnerManager<T>
{
    Context context;
    String consulta;
    T objeto;
    String campo;
    Spinner spinner;

    public SpinnerManager(T objeto, Context context, Spinner spinner)
    {
        this.context = context;
     //  this.consulta = consulta;
        this.objeto = objeto;
     //   this.campo  = campo;
        this.spinner = spinner;

    }

    public void llenarDesdeCursor(String consulta, String campo)
    {
        Dao controladorDb = new Dao(this.context);
        Cursor cursor = controladorDb.ejecutarConsultaSql(consulta);

        Mapeador<T> lineaMapeador = new Mapeador<T>(this.objeto);

        String[] from = new String[]{campo};
        int[] to = new int[]{android.R.id.text1};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.simple_spinner_item, cursor, from, to,0 );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(adapter);


    }



}
