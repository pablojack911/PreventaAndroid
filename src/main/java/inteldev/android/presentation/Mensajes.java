package inteldev.android.presentation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Operador on 13/05/14.
 */
public class Mensajes {

    private DialogoAlertaSiNo dialogoAlertaSiNo;
    AlertDialog.Builder alertDialog;

    public Mensajes(Context context)
    {
        alertDialog = new AlertDialog.Builder(context);
    }

    public void setDialogoAlertaSiNo(DialogoAlertaSiNo dialogoAlertaSiNo)
    {
        this.dialogoAlertaSiNo = dialogoAlertaSiNo;
    }

    public void alertDialogSiNo(String titulo, String mensaje)
    {
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensaje);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialogoAlertaSiNo.Positivo();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               dialogoAlertaSiNo.Negativo();
            }
        });
        alertDialog.show();

    }

}
