package inteldev.android.accesoadatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * DbHelper
 *
 * @author Pablo Vigolo
 * @version 1.0 06 february of 2014
 */
public class DbHelper extends SQLiteOpenHelper
{

    static final File directory = Environment.getExternalStorageDirectory();

    private static final String DB_NAME = directory + "/" + Environment.DIRECTORY_DOWNLOADS + "/InteldevMobile.sqlite";
    private static final int DB_SCHEMA_VERSION = 1;

    // Version 57
    //   Table: TablaconfigRutas
    //   Add column Recorrido Text

    // Version 58
    //   Table: TablaconfigRutas
    //   Se cambio campo recorrido de text a int , se hizo

    // Version 59
    // Version 60
    //  Tabla: PosicionesGPS
    // Nuevo indice idCliente
    // 61
    // 62
    // 63
    // POCHO
    // 64 recreación tabla PosicionGPS
    // 65 id en PosicionesGPS
    // ...
    // 68 imei en PosicionesGPS
    // 69 corregido
    // 70 Index en PosicionesGPS
    // 71 Index en detBonif
    // 72 Index en cabBonif
    // 73 Index en stock-detListas-rutaventas-cabOper-Clientes-oferEsp
    // 74 Index en oferOper-detoper-lineas-vendedores-rubros-oferdet-poscionesgps --- pasé a schema 1 porque voy a generar de cero la base de datos

    private CreateTables createTables;

    public DbHelper(Context context)
    {
        super(context, DB_NAME, null, DB_SCHEMA_VERSION);


        this.createTables = new CreateTables();
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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
       /* sqLiteDatabase.execSQL("drop table if exists Alcance");
        sqLiteDatabase.execSQL("drop table if exists Articulos");
        sqLiteDatabase.execSQL("drop table if exists cabBonif");
        sqLiteDatabase.execSQL("drop table if exists cabListas");
        sqLiteDatabase.execSQL("drop table if exists cabOper");
        sqLiteDatabase.execSQL("drop table if exists Canales");
        sqLiteDatabase.execSQL("drop table if exists Clientes");
        sqLiteDatabase.execSQL("drop table if exists condvtas");*/
        // sqLiteDatabase.execSQL("drop table if exists configRutas");
       /* sqLiteDatabase.execSQL("drop table if exists detBonif");
        sqLiteDatabase.execSQL("drop table if exists detListas");
        sqLiteDatabase.execSQL("drop table if exists detOper");
        sqLiteDatabase.execSQL("drop table if exists Lineas");
        sqLiteDatabase.execSQL("drop table if exists Localidad");
        sqLiteDatabase.execSQL("drop table if exists ofer_Det");
        sqLiteDatabase.execSQL("drop table if exists oferEsp");
        sqLiteDatabase.execSQL("drop table if exists oferOper");
        sqLiteDatabase.execSQL("drop table if exists Ramos");
        sqLiteDatabase.execSQL("drop table if exists Rubros");
        sqLiteDatabase.execSQL("drop table if exists RutaVentas");
        sqLiteDatabase.execSQL("drop table if exists Stock");
        sqLiteDatabase.execSQL("drop table if exists Condiva");
        sqLiteDatabase.execSQL("drop table if exists sysEstadoCliente");
        sqLiteDatabase.execSQL("drop table if exists sysEstadoClienteObservacion");
        sqLiteDatabase.execSQL("drop table if exists sysObservacion");
        sqLiteDatabase.execSQL("drop table if exists sysOperacion");
        sqLiteDatabase.execSQL("drop table if exists sysTasaIva");
        sqLiteDatabase.execSQL("drop table if exists tipoBonif");
        sqLiteDatabase.execSQL("drop table if exists Vendedores");
        sqLiteDatabase.execSQL("drop table if exists Zona");*/
        // sqLiteDatabase.execSQL("drop table if exists PosicionesGPS");

      /*  sqLiteDatabase.execSQL(createTables.TablaAlcances);
        sqLiteDatabase.execSQL(createTables.IndiceAlcanceIdCabBonif);

        sqLiteDatabase.execSQL(createTables.TablaArticulos);
        sqLiteDatabase.execSQL(createTables.IndiceArticulosidArticulo);

        sqLiteDatabase.execSQL(createTables.TablacabBonif);
        sqLiteDatabase.execSQL(createTables.IndicecabBonifIdCabBonif);

        sqLiteDatabase.execSQL(createTables.TablacabListas);
        sqLiteDatabase.execSQL(createTables.TablacabOper);
        sqLiteDatabase.execSQL(createTables.TablaCanales);
        sqLiteDatabase.execSQL(createTables.TablaClientes);
        sqLiteDatabase.execSQL(createTables.Tablacondvtas);*/
        /*sqLiteDatabase.execSQL(createTables.TablaconfigRutas);
        /*sqLiteDatabase.execSQL(createTables.TabladetBonif);
        sqLiteDatabase.execSQL(createTables.IndicedetBonifIdCabBonif);

        sqLiteDatabase.execSQL(createTables.TabladetListas);
        sqLiteDatabase.execSQL(createTables.TabladetOper);
        sqLiteDatabase.execSQL(createTables.TablaLineas);
        sqLiteDatabase.execSQL(createTables.TablaLocalidades);
        sqLiteDatabase.execSQL(createTables.Tablaofer_Det);
        sqLiteDatabase.execSQL(createTables.TablaoferEsp);
        sqLiteDatabase.execSQL(createTables.TablaoferOper);
        sqLiteDatabase.execSQL(createTables.TablaRamos);
        sqLiteDatabase.execSQL(createTables.TablaRubros);
        sqLiteDatabase.execSQL(createTables.TablarutaVentas);
        sqLiteDatabase.execSQL(createTables.TablaStock);
        sqLiteDatabase.execSQL(createTables.TablasysCondivas);
        sqLiteDatabase.execSQL(createTables.TablasysEstadoClientes);
        sqLiteDatabase.execSQL(createTables.TablasysEstadoClientesObsevaciones);
        sqLiteDatabase.execSQL(createTables.TablasysObservaciones);
        sqLiteDatabase.execSQL(createTables.TablasysOperaciones);
        sqLiteDatabase.execSQL(createTables.TablasysTasasIva);
        sqLiteDatabase.execSQL(createTables.TablatipoBonif);
        sqLiteDatabase.execSQL(createTables.TablaVendedores);
        sqLiteDatabase.execSQL(createTables.TablaZonas);*/
        //   sqLiteDatabase.execSQL(createTables.TablaPosicionesGPS);
        //   sqLiteDatabase.execSQL(createTables.AlterTablaconfigRutas);

//        sqLiteDatabase.execSQL("drop table if exists PosicionesGPS");
//        sqLiteDatabase.execSQL(createTables.TablaPosicionesGPS);
//        sqLiteDatabase.execSQL(createTables.IndicePosicionesGPSIdCliente);
//        sqLiteDatabase.execSQL(createTables.IndicePosicionesGPSEnviado);
//        sqLiteDatabase.execSQL(createTables.IndicePosicionesGPS_fecha);
//        sqLiteDatabase.execSQL(createTables.IndiceDetBonif_idCabBonif);
//        sqLiteDatabase.execSQL(createTables.IndiceDetBonif_idArticulo);
//        sqLiteDatabase.execSQL(createTables.IndiceDetBonif_idRubro);
//        sqLiteDatabase.execSQL(createTables.IndiceDetBonif_idLinea);
//        sqLiteDatabase.execSQL(createTables.IndiceCabBonif_idCabBonif);
//        sqLiteDatabase.execSQL(createTables.IndiceCabBonif_fDesde);
//        sqLiteDatabase.execSQL(createTables.IndiceCabBonif_fHasta);
//        sqLiteDatabase.execSQL(createTables.IndiceDetListas_idLista);
//        sqLiteDatabase.execSQL(createTables.IndiceDetListas_articulo);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_dom);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_lun);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_mar);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_mie);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_jue);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_vie);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_sab);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_vendedor);
//        sqLiteDatabase.execSQL(createTables.IndiceRutaVentas_suplente);
//        sqLiteDatabase.execSQL(createTables.IndiceCabOper_claveunica);
//        sqLiteDatabase.execSQL(createTables.IndiceCabOper_enviado);
//        sqLiteDatabase.execSQL(createTables.IndiceCabOper_fecha);
//        sqLiteDatabase.execSQL(createTables.IndiceCabOper_idCabBonif);
//        sqLiteDatabase.execSQL(createTables.IndiceCabOper_idCliente);
//        sqLiteDatabase.execSQL(createTables.IndiceClientes_idCliente);
//        sqLiteDatabase.execSQL(createTables.IndiceOferEsp_lista);
//        sqLiteDatabase.execSQL(createTables.IndiceOferEsp_codigo);
//        sqLiteDatabase.execSQL(createTables.IndiceOferOper_claveUnica);
//        sqLiteDatabase.execSQL(createTables.IndiceOferOper_enviado);
//        sqLiteDatabase.execSQL(createTables.IndiceDetOper_enviado);
//        sqLiteDatabase.execSQL(createTables.IndiceDetOper_claveUnica);
//        sqLiteDatabase.execSQL(createTables.IndiceDetOper_idArticulo);
//        sqLiteDatabase.execSQL(createTables.IndiceDetOper_sinCargo);
//        sqLiteDatabase.execSQL(createTables.IndiceLineas_idLinea);
//        sqLiteDatabase.execSQL(createTables.IndiceVendedores_usuario);
//        sqLiteDatabase.execSQL(createTables.IndiceVendedores_pass);
//        sqLiteDatabase.execSQL(createTables.IndiceRubros_idRubro);
//        sqLiteDatabase.execSQL(createTables.IndiceOfer_det_codigo);
//        sqLiteDatabase.execSQL(createTables.IndiceOfer_det_tipo);
    }
}
