package inteldev.android.negocios;

import java.io.File;

/**
 * Created by Operador on 6/05/14.
 */
public class BorrarArchivos
{
    public void BorrarPorContenido(String ubicacion, String contenidoCadena)
    {
        File carpeta = new File(ubicacion);
        if (carpeta.canRead())
        {
            for (File archivo : carpeta.listFiles())
            {
                if (archivo.getName().contains(contenidoCadena))
                {
                    boolean borro;
                    borro = archivo.delete();
                }
            }
        }
    }

}
