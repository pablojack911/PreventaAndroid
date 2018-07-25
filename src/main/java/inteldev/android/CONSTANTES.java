package inteldev.android;


import android.Manifest;

/**
 * Created by Pocho on 22/03/2017.
 */

public final class CONSTANTES
{
    //EXTRAS
    public static final String USUARIO_KEY = "usuario-key";
    public static final String IMEI_KEY = "imei-key";
    public static final String PASSWORD_KEY = "password-key";
    public static final String ID_VENDEDOR_KEY = "id-vendedor-key";
    public static final String NOMBRE_CLIENTE_KEY = "nombre-cliente-key";
    public static final String CODIGO_OFERTA = "codigo-oferta";
    public static final String ID_CLIENTE_SELECCIONADO = "id-cliente-seleccionado";
    public static final String CLAVE_UNICA_CABOPER = "clave-unica-cab-oper";
    public static final String OFERTA_ESPECIAL = "oferta-especial";
    public static final String TOTAL_OFERTA = "total-oferta";
    //USADOS EN GPS LOCATION
    public static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA};
    public static final int REQUEST_CHECK_SETTINGS = 1;
    public static final int REQUEST_STORAGE_PERMISSION = 4;
    public static final int REQUEST_LOCATION_PERMISSION = 5;
    public static final int REQUEST_PHONE_STATE_PERMISSION = 6;
    public static final String LOCATION_KEY = "location-key";
    public static final String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    // Request code to use when launching the resolution activity
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    public static final String DIALOG_ERROR = "dialog_error";
    public static final String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    public static final String POSICION_GPS_KEY = "posicion-gps-key";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000; //5 minutos
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    //CODIGOS DE INTENT
    public static final int INTENT_LOGIN = 1024;
    public static final int INTENT_OFERTA = 2;
    public static final int INTENT_CONVENIO = 1;
    public static final int INTENT_DETALLE_OFERTA_NO_CONFIRMADA = 1138;
    public static final int INTENT_BUSCAR_ARTICULO = 3;
    public static final int INTENT_TAKE_PICTURE = 115;
    public static final int INTENT_FOLDER = 4;
    public static final int INTENT_CLIENTE_MAYORISTA = 5;
    //VIEWS PARA EL VIEW PAGER DE CLIENTE
    public static final int CANTIDAD_VIEWS_CLIENTE = 4;
    public static final int ARTICULOS_VIEW = 0;
    public static final int OFERTAS_VIEW = 2;
    //    public static final int CONVENIOS_VIEW = 2;
    public static final int DETALLE_PEDIDO_VIEW = 1;
    public static final int MOTIVO_NO_COMPRA_VIEW = 3;
    public static final java.lang.String CODIGO_ARTICULO = "codigo-articulo";
    public static final java.lang.String CODIGO_CONVENIO = "codigo-convenio";
    public static final java.lang.String DETALLE_PEDIDO_CLIENTE = "detalle-pedido-cliente";
    public static final java.lang.String MOTIVO_NO_COMPRA_CLIENTE = "motivo-no-compra-cliente";
    public static final int DESCUENTOS_ARTICULOS_VIEW = 0;
    public static final int CONVENIOS_SIN_CARGO_VIEW = 1;
    public static final int CANTIDAD_VIEWS_DTOS_CONVS = 2;
    public static final java.lang.String FORMATO_DIA_MES_AÑO = "dd/MM/yyyy";
    public static final java.lang.String FORMATO_FECHA_COMPLETA = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ARTICULO_SELECCIONADO = "articulo-seleccionado-busqueda";
    public static final String SESION_INICIADA = "sesion-iniciada-key";
    public static final String PREFERENCIAS = "preference_file_key";
    public static final String POSICION_GPS_VIAJE_KEY = "posicion-gps-viaje-key";
    public static final long GPS_INTERVAL_REQUEST = 15 * 1000;
    public static final String CONFIG_CONVENIO_DETALLE_TIPO = "detalle-tipo-convenio-key";
    public static final String CONFIG_CONVENIO_DESCUENTO = "descuento-convenio-key";
    public static final String CONFIG_CONVENIO_PRECIO = "precio-convenio-key";
    public static final String CONFIG_CONVENIO_TIPO = "tipo-convenio-key";
    public static final String QUERY_BUSQUEDA_ARTICULO = "select * from articulos order by nombre";
    public static final String QUERY_BUSQUEDA_FOLDER = "";
    public static final String BUSCAR_ARTICULO = "buscar-articulo-key";
    public static final String TITULO = "titulo-activity-key";
}
