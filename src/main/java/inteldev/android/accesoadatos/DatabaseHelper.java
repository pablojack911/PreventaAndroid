package inteldev.android.accesoadatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;


/**
 * Created by Pocho on 22/02/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper
{
    private static final File directory = Environment.getExternalStorageDirectory();
    private static final String DB_NAME = directory + "/" + Environment.DIRECTORY_DOWNLOADS + "/InteldevMobile.sqlite";
    private static final int DB_SCHEMA_VERSION = 2;
    private static DatabaseHelper mInstance;
    private CreateTables createTables;

    private DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_SCHEMA_VERSION);
        this.createTables = new CreateTables();
    }

    static DatabaseHelper getInstance(Context ctx)
    {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null)
        {
            mInstance = new DatabaseHelper(ctx);
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(createTables.TablaAlcances);
        sqLiteDatabase.execSQL(createTables.IndiceAlcance_idCabBonif);
        sqLiteDatabase.execSQL(createTables.IndiceAlcance_idCliente);
        sqLiteDatabase.execSQL(createTables.TablaArticulos);
        sqLiteDatabase.execSQL(createTables.IndiceArticulosidArticulo);
        sqLiteDatabase.execSQL(createTables.IndiceArticulosidLinea);
        sqLiteDatabase.execSQL(createTables.IndiceArticulosidRubro);
        sqLiteDatabase.execSQL(createTables.TablacabBonif);
        sqLiteDatabase.execSQL(createTables.IndiceCabBonif_idCabBonif);
        sqLiteDatabase.execSQL(createTables.IndiceCabBonif_fDesde);
        sqLiteDatabase.execSQL(createTables.IndiceCabBonif_fHasta);
        sqLiteDatabase.execSQL(createTables.TablacabListas);
        sqLiteDatabase.execSQL(createTables.TablacabOper);
        sqLiteDatabase.execSQL(createTables.IndiceCabOper_claveunica);
        sqLiteDatabase.execSQL(createTables.IndiceCabOper_enviado);
        sqLiteDatabase.execSQL(createTables.IndiceCabOper_fecha);
        sqLiteDatabase.execSQL(createTables.IndiceCabOper_idCabBonif);
        sqLiteDatabase.execSQL(createTables.IndiceCabOper_idCliente);
        sqLiteDatabase.execSQL(createTables.TablaCanales);
        sqLiteDatabase.execSQL(createTables.TablaClientes);
        sqLiteDatabase.execSQL(createTables.IndiceClientes_idCliente);
        sqLiteDatabase.execSQL(createTables.Tablacondvtas);
        sqLiteDatabase.execSQL(createTables.TablaconfigRutas);
        sqLiteDatabase.execSQL(createTables.IndiceConfigRutas_idRutaVenta);
        sqLiteDatabase.execSQL(createTables.TabladetBonif);
        sqLiteDatabase.execSQL(createTables.IndiceDetBonif_idCabBonif);
        sqLiteDatabase.execSQL(createTables.IndiceDetBonif_idArticulo);
        sqLiteDatabase.execSQL(createTables.IndiceDetBonif_idRubro);
        sqLiteDatabase.execSQL(createTables.IndiceDetBonif_idLinea);
        sqLiteDatabase.execSQL(createTables.TabladetListas);
        sqLiteDatabase.execSQL(createTables.IndiceDetListas_idLista);
        sqLiteDatabase.execSQL(createTables.IndiceDetListas_articulo);
        sqLiteDatabase.execSQL(createTables.TabladetOper);
        sqLiteDatabase.execSQL(createTables.IndiceDetOper_enviado);
        sqLiteDatabase.execSQL(createTables.IndiceDetOper_claveUnica);
        sqLiteDatabase.execSQL(createTables.IndiceDetOper_idArticulo);
        sqLiteDatabase.execSQL(createTables.IndiceDetOper_sinCargo);
        sqLiteDatabase.execSQL(createTables.TablaLineas);
        sqLiteDatabase.execSQL(createTables.IndiceLineas_idLinea);
        sqLiteDatabase.execSQL(createTables.TablaLocalidades);
        sqLiteDatabase.execSQL(createTables.Tablaofer_Det);
        sqLiteDatabase.execSQL(createTables.IndiceOfer_det_codigo);
        sqLiteDatabase.execSQL(createTables.IndiceOfer_det_tipo);
        sqLiteDatabase.execSQL(createTables.TablaoferEsp);
        sqLiteDatabase.execSQL(createTables.IndiceOferEsp_lista);
        sqLiteDatabase.execSQL(createTables.IndiceOferEsp_codigo);
        sqLiteDatabase.execSQL(createTables.TablaoferOper);
        sqLiteDatabase.execSQL(createTables.IndiceOferOper_claveUnica);
        sqLiteDatabase.execSQL(createTables.IndiceOferOper_enviado);
        sqLiteDatabase.execSQL(createTables.TablaRamos);
        sqLiteDatabase.execSQL(createTables.TablaRubros);
        sqLiteDatabase.execSQL(createTables.IndiceRubros_idRubro);
        sqLiteDatabase.execSQL(createTables.TablarutaVentas);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_dom);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_lun);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_mar);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_mie);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_jue);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_vie);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_sab);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_vendedor);
        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_suplente);
        sqLiteDatabase.execSQL(createTables.TablaStock);
        sqLiteDatabase.execSQL(createTables.IndiceStock_idArticulo);
        sqLiteDatabase.execSQL(createTables.TablasysCondivas);
        sqLiteDatabase.execSQL(createTables.TablasysEstadoClientes);
        sqLiteDatabase.execSQL(createTables.TablasysEstadoClientesObsevaciones);
        sqLiteDatabase.execSQL(createTables.TablasysObservaciones);
        sqLiteDatabase.execSQL(createTables.TablasysOperaciones);
        sqLiteDatabase.execSQL(createTables.TablasysTasasIva);
        sqLiteDatabase.execSQL(createTables.TablatipoBonif);
        sqLiteDatabase.execSQL(createTables.TablaVendedores);
        sqLiteDatabase.execSQL(createTables.IndiceVendedores_usuario);
        sqLiteDatabase.execSQL(createTables.IndiceVendedores_pass);
        sqLiteDatabase.execSQL(createTables.TablaZonas);
        sqLiteDatabase.execSQL(createTables.TablaPosicionesGPS);
        sqLiteDatabase.execSQL(createTables.IndicePosicionesGPSIdCliente);
        sqLiteDatabase.execSQL(createTables.IndicePosicionesGPSEnviado);
        sqLiteDatabase.execSQL(createTables.IndicePosicionesGPS_fecha);
        sqLiteDatabase.execSQL(createTables.IndicePosicionesGPS_usuario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
//        sqLiteDatabase.execSQL("alter table PosicionesGPS add column usuario varchar(2)");
    }
}
