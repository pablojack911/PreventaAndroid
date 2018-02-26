package inteldev.android.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import inteldev.android.R;
import inteldev.android.presentation.vistaModelo.IListenerDialogoCantidad;

/**
 * Created by Operador on 14/05/14.
 */
public class FabricaMensaje extends DialogFragment
{
    static IListenerDialogoCantidad listenerDialogoCantidad;

    public static AlertDialog.Builder dialogoAlertaSiNo(Context context, String titulo, String mensaje, final DialogoAlertaSiNo dialogoAlertaSiNo)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(titulo.toUpperCase());
        alertDialog.setMessage(mensaje);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogoAlertaSiNo.Positivo();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogoAlertaSiNo.Negativo();
            }
        });
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    public static AlertDialog.Builder dialogoOk(Context context, String titulo, String mensaje, final DialogoAlertaNeutral dialogoAlerta)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(titulo.toUpperCase());
        alertDialog.setMessage(mensaje);
        alertDialog.setNeutralButton("Ok", (new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogoAlerta.Neutral();
            }
        }));
        alertDialog.setCancelable(false);
        return alertDialog;
    }


    public static void setListenerDialogoCantidad(IListenerDialogoCantidad listener)
    {
        listenerDialogoCantidad = listener;
    }

    public static Dialog dialogoCantidad(final Activity activity)
    {


        final AlertDialog.Builder dialogoCantidad = new AlertDialog.Builder(activity);
        dialogoCantidad.setTitle("Cantidad".toUpperCase());

        final LayoutInflater inflater = activity.getLayoutInflater();
        dialogoCantidad.setView(inflater.inflate(R.layout.activity_dialogo_cantidad, null))
                // Add action buttons
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                        Dialog f = (Dialog) dialog;
                        EditText inputTemp = (EditText) f.findViewById(R.id.edtDialogoCantidad);
                        String query = inputTemp.getText().toString();

                        listenerDialogoCantidad.Positivo(Integer.valueOf(query));

                    }
                })

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }

                });

        // Create the AlertDialog object and return it
        return dialogoCantidad.create();
    }

}
