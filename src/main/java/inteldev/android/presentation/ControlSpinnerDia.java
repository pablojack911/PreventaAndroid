package inteldev.android.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import inteldev.android.R;

/**
 * Created by Operador on 29/04/14.
 */
public class ControlSpinnerDia extends LinearLayout
{
    private OnSpinnerDiaListener onSpinnerDiaListener;

    Spinner spDia;

    public void setOnSpinnerDiaListener(OnSpinnerDiaListener onSpinnerDiaListener)
    {
        this.onSpinnerDiaListener = onSpinnerDiaListener;
    }

    public ControlSpinnerDia(Context context){
        super(context);
        inicializar(context);
    }
    public ControlSpinnerDia(Context context, AttributeSet attributeSet )
    {
        super(context, attributeSet);
        inicializar(context);
    }

    private void inicializar(Context context)
    {
        String infService = Context.LAYOUT_INFLATER_SERVICE;

        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(infService);
        layoutInflater.inflate(R.layout.control_spinner_dia,this,true);

        spDia = (Spinner)findViewById(R.id.spDia);

        this.cargarDias(context);
        this.asignarEventos();

    }

    private void cargarDias(Context context)
    {
        Spinner spDia = (Spinner)findViewById(R.id.spDia);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.dias,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDia.setAdapter(adapter);


    }

    private void asignarEventos()
    {
        spDia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                onSpinnerDiaListener.Ejecutar();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
