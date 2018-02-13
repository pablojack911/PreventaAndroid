package inteldev.android.accesoadatos;

public class CreateTables
{

    public String TablaAlcances = "create table Alcance " +
            "(idCabBonif int, " +
            "lastUpdate date, " +
            "borrado int, " +
            "idCliente text)";

    public String IndiceAlcance_idCabBonif = "create index IF NOT EXISTS Alcance_idCabBonif on Alcance (idCabBonif asc)";
    public String IndiceAlcance_idCliente = "create index IF NOT EXISTS Alcance_idCliente on Alcance (idCliente asc)";

    public String TablaArticulos = "create table Articulos " +
            "(idArticulo text," +
            "idRubro text," +
            "idLinea text," +
            "nombre text," +
            "tasaIva int," +
            "suspendido int," +
            "unidadVenta int," +
            "minimoVenta int," +
            "multiploVenta int," +
            "tasaIIBB int," +
            "borrado int," +
            "usaCanal int," +
            "impInternos float," +
            "lastUpdate date)";

    public String IndiceArticulosidArticulo = "create index Articulos_idArticulo on Articulos (idArticulo asc)";
    public String IndiceArticulosidLinea = "create index Articulos_idLinea on Articulos (idLinea asc)";
    public String IndiceArticulosidRubro = "create index Articulos_idRubro on Articulos (idRubro asc)";

    public String TablacabBonif = "create table cabBonif " +
            "(idCabBonif int, " +
            "tipo int, " +
            "fdesde date, " +
            "fhasta date, " +
            "suspendido int, " +
            "lastUpdate datetime, " +
            "borrado int, " +
            "sindescbase int, " +
            "permiteoe int)";

    public String IndiceCabBonif_idCabBonif = "create index IF NOT EXISTS cabBonif_idCabBonif on cabBonif (idCabBonif asc)";
    public String IndiceCabBonif_fDesde = "create index IF NOT EXISTS Alcance_fDesde on cabBonif (fdesde asc)";
    public String IndiceCabBonif_fHasta = "create index IF NOT EXISTS Alcance_fHasta on cabBonif (fhasta asc)";

    public String TablacabListas = "create table cabListas " +
            "(IdLista text, " +
            "nombre text, " +
            "fvigencia date, " +
            "lastUpdate date, " +
            "borrado int)";

    public String TablacabOper = "create table cabOper " +
            "(claveUnica text, " +
            "idOperacion int, " +
            "idCliente text, " +
            "fecha date, " +
            "fechaEntrega date, " +
            "letra text, " +
            "sucursal text, " +
            "numero text, " +
            "cuit text, " +
            "razonSocial text, " +
            "domicilio text, " +
            "baseImpIIBB float, " +
            "lastUpdate datetime, " +
            "borrado int, " +
            "enviado int, " +
            "idVendedor text, " +
            "estado text, " +
            "idCabBonif int)";

    public String IndiceCabOper_claveunica = "create index if not exists cabOper_claveUnica on cabOper (claveUnica asc)";
    public String IndiceCabOper_idCliente = "create index if not exists cabOper_idCliente on cabOper (idCliente asc)";
    public String IndiceCabOper_fecha = "create index if not exists cabOper_fecha on cabOper (fecha asc)";
    public String IndiceCabOper_enviado = "create index if not exists cabOper_enviado on cabOper (enviado asc)";
    public String IndiceCabOper_idCabBonif = "create index if not exists cabOper_idCabBonif on cabOper (idCabBonif asc)";

    public String TablaCanales = "create table Canales " +
            "(codigo text, " +
            "nombre text," +
            "lista text)";

    public String TablaClientes = "create table Clientes " +
            "(idCliente text, " +
            "idZona text, " +
            "idEstClie int, " +
            "localidad text, " +
            "condiva int, " +
            "nombre text, " +
            "razonSocial text, " +
            "domicilio text, " +
            "saldoCtacte float, " +
            "limiteCtacte float, " +
            "lastUpdate datetime, " +
            "borrado int, " +
            "idCondVta text, " +
            "idRamo text, " +
            "visitado int, " +
            "cuit text, " +
            "lista text, " +
            "telefono text, " +
            "tasaIibb float, " +
            "canal text)";

    public String IndiceClientes_idCliente = "create index if not exists clientes_idCliente on clientes (idCliente asc)";

    public String Tablacondvtas = "create table condVtas " +
            "(idCondVta text, " +
            "nombre text, " +
            "contado int, " +
            "lastUpdate datetime, " +
            "borrado int)";

    public String TablaconfigRutas = "create table configRutas " +
            "(idRutaVenta text, " +
            "idCliente text, " +
            "lastUpdate datetime, " +
            "borrado int," +
            "recorrido int)";

    public String IndiceConfigRutas_idRutaVenta = "create index if not exists configRutas_idRutaVenta on configRutas (idRutaVenta asc)";

//    public String AlterTablaconfigRutas = "alter table configRutas add column recorrido text";

    // ctactes

    public String TabladetBonif = "create table detBonif " +
            "(idCabBonif int, " +
            "idRubro text, " +
            "idArticulo text, " +
            "idLinea text, " +
            "porcentaje float, " +
            "importe float, " +
            "cant_sc int, " +
            "lastUpdate datetime, " +
            "borrado int)";

    public String IndiceDetBonif_idCabBonif = "create index if not exists detBonif_idCabBonif on detBonif (idCabBonif asc)";
    public String IndiceDetBonif_idArticulo = "create index if not exists detBonif_idArticulo on detBonif (idArticulo asc)";
    public String IndiceDetBonif_idRubro = "create index if not exists detBonif_idRubro on detBonif (idRubro asc)";
    public String IndiceDetBonif_idLinea = "create index if not exists detBonif_idLinea on detBonif (idLinea asc)";

    public String TabladetListas = "create table detListas " +
            "(idLista text, " +
            "precio float, " +
            "articulo text, " +
            "lastUpdate datetime, " +
            "borrado int, " +
            "descuento float)";

    public String IndiceDetListas_idLista = "create index if not exists detListas_idLista on detListas (idLista asc)";
    public String IndiceDetListas_articulo = "create index if not exists detListas_articulo on detListas (articulo asc)";
    // detObservacion

    public String TabladetOper = "create table detOper " +
            "(claveUnica text, " +
            "idArticulo text, " +
            "precio float, " +
            "tasaIva float, " +
            "impInternos float, " +
            "descuento float, " +
            "entero int, " +
            "fraccion int, " +
            "enviado int, " +
            "idFila text, " +
            "tasa_iibb float, " +
            "oferta text," +
            "sincargo int)";

    public String IndiceDetOper_enviado = "create index if not exists detOper_enviado on detOper (enviado asc)";
    public String IndiceDetOper_claveUnica = "create index if not exists detOper_claveUnica on detOper (claveUnica asc)";
    public String IndiceDetOper_sinCargo = "create index if not exists detOper_sinCargo on detOper (sinCargo asc)";
    public String IndiceDetOper_idArticulo = "create index if not exists detOper_idArticulo on detOper (idArticulo asc)";

    public String TablaLineas = "create table Lineas " +
            "(idLinea text, " +
            "nombre text, " +
            "empresa int, " +
            "lastUpdate datetime, " +
            "borrado int)";

    public String IndiceLineas_idLinea = "create index if not exists lineas_idLinea on lineas (idLinea asc)";

    public String TablaLocalidades = "create table Localidad " +
            "(idLocalidad text, " +
            "nombre, " +
            "lastUpdate datetime)";

    // Numeraciones

    public String Tablaofer_Det = "create table ofer_Det " +
            "(codigo text, " +
            "tipo text, " +
            "linea text, " +
            "rubro text, " +
            "articulo text, " +
            "descuento1 float, " +
            "descuento2 float, " +
            "descuento3 float, " +
            "descuento4 float, " +
            "cpra_oblig int, " +
            "descrip text, " +
            "d_cant int, " +
            "h_cant int, " +
            "multiplo int," +
            "borrado int)";

    public String IndiceOfer_det_codigo ="create index if not exists ofer_det_codigo on ofer_det (codigo asc)";
    public String IndiceOfer_det_tipo ="create index if not exists ofer_det_tipo on ofer_det (tipo asc)";

    public String TablaoferEsp = "create table oferEsp " +
            "(codigo text, " +
            "nombre text, " +
            "d_cant int, " +
            "h_cant int, " +
            "tipo text, " +
            "ctrlcant int, " +
            "base_dcant int, " +
            "base_hcant int, " +
            "comb_oblig int, " +
            "comb_cantb int, " +
            "comb_cantc int, " +
            "bon_cada int, " +
            "bon_tipo text, " +
            "bon_cant int, " +
            "pfinal float, " +
            "tomaconv int, " +
            "lista text," +
            "borrado int)";

    public String IndiceOferEsp_lista = "create index if not exists oferEsp_lista on oferEsp (lista asc)";
    public String IndiceOferEsp_codigo = "create index if not exists oferEsp_codigo on oferEsp (codigo asc)";

    public String TablaoferOper = "create table oferOper " +
            "(claveUnica text, " +
            "claveOferta text, " +
            "idArticulo text, " +
            "tasaIva float, " +
            "precio float, " +
            "impInternos float, " +
            "descuento float, " +
            "enviado int, " +
            "idFila text, " +
            "tasa_iibb float, " +
            "cantidad int, " +
            "tipo text)";

    public String IndiceOferOper_claveUnica = "create index if not exists oferOper_claveUnica on oferOper (claveunica asc)";
    public String IndiceOferOper_enviado = "create index if not exists oferOper_enviado on oferOper (enviado asc)";

    // Param

    public String TablaRamos = "create table Ramos " +
            "(idRamo text, " +
            "nombre text, " +
            "lastUpdate datetime, " +
            "borrado int)";

    public String TablaRubros = "create table Rubros " +
            "(idRubro text, " +
            "nombre text, " +
            "lastUpdate datetime, " +
            "borrado int)";

    public String IndiceRubros_idRubro = "create index if not exists rubros_idRubro on rubros (idRubro asc)";

    public String TablarutaVentas = "create table rutaVentas " +
            "(idRutaVenta text, " +
            "lun int, " +
            "mar int, " +
            "mie int, " +
            "jue int, " +
            "vie int, " +
            "sab int, " +
            "dom int, " +
            "lastUpdate datetime, " +
            "borrado int, " +
            "vendedor text, " +
            "suplente text)";

    public String IndiceRutaVentas_lun = "create index if not exists rutaventas_lun on rutaventas (lun asc)";
    public String IndiceRutaVentas_mar = "create index if not exists rutaventas_mar on rutaventas (mar asc)";
    public String IndiceRutaVentas_mie = "create index if not exists rutaventas_mie on rutaventas (mie asc)";
    public String IndiceRutaVentas_jue = "create index if not exists rutaventas_jue on rutaventas (jue asc)";
    public String IndiceRutaVentas_vie = "create index if not exists rutaventas_vie on rutaventas (vie asc)";
    public String IndiceRutaVentas_sab = "create index if not exists rutaventas_sab on rutaventas (sab asc)";
    public String IndiceRutaVentas_dom = "create index if not exists rutaventas_dom on rutaventas (dom asc)";
    public String IndiceRutaVentas_vendedor = "create index if not exists rutaventas_vendedor on rutaventas (vendedor asc)";
    public String IndiceRutaVentas_suplente = "create index if not exists rutaventas_suplente on rutaventas (suplente asc)";

    public String TablaStock = "create table Stock " +
            "(fecha date, " +
            "idVendedor text, " +
            "idArticulo text, " +
            "bultos int, " +
            "unidades int)";

    public String IndiceStock_idArticulo = "create index IF NOT EXISTS stock_idArticulo on stock (idArticulo asc)";

    public String TablasysCondivas = "create table Condiva " +
            "(idCondIva int, " +
            "nombre text, " +
            "tasa float, " +
            "letra text, " +
            "lastUpdate datetime)";

    public String TablasysEstadoClientes = "create table sysEstadoCliente " +
            "(idEstClie int, " +
            "nombre text)";

    public String TablasysEstadoClientesObsevaciones = "create table sysEstadoClienteObservacion " +
            "(idOperacion int, " +
            "idEstClie int, " +
            "readonly int)";

    public String TablasysObservaciones = "create table sysObservacion " +
            "(idObservacion int, " +
            "nombre text, " +
            "noCompra int, " +
            "noPago int, " +
            "noVisito int, " +
            "borrado int)";

    public String TablasysOperaciones = "create table sysOperacion " +
            "(idOperacion int, " +
            "nombre text, " +
            "abreviatura text, " +
            "borrado int)";

    public String TablasysTasasIva = "create table sysTasaIva " +
            "(id int, " +
            "nombre text, " +
            "valor float, " +
            "lastUpdate datetime)";

    public String TablatipoBonif = "create table tipoBonif " +
            "(id int, " +
            "nombre text, " +
            "lastUpdate datetime, " +
            "borrado int)";

    public String TablaVendedores = "create table Vendedores " +
            "(idVendedor text, " +
            "usuario text, " +
            "pass text, " +
            "autoventa int, " +
            "sucursal text, " +
            "lastUpdate datetime, " +
            "borrado int)";

    public String IndiceVendedores_usuario = "create index if not exists vendedores_usuario on vendedores(usuario asc)";
    public String IndiceVendedores_pass = "create index if not exists vendedores_pass on vendedores(pass asc)";

    public String TablaZonas = "create table Zona " +
            "(idZona text, " +
            "nombre text, " +
            "lastUpdate datetime, " +
            "borrado int)";

    public String TablaPosicionesGPS = "create table PosicionesGPS " +
            "(fecha datetime, " +
            "imei text," +
            "usuario text," +
            "latitud float, " +
            "longitud float, " +
            "visitados int, " +
            "compradores int, " +
            "bultos int, " +
            "pesos float, " +
            "estado int," +
            "enviado int," +
            "idCliente text," +
            "motivoNoCompra int)";

    public String IndicePosicionesGPSIdCliente = "create index if not exists PosicionesGPS_idCliente on PosicionesGPS (idCliente asc)";
    public String IndicePosicionesGPSEnviado = "create index if not exists PosicionesGPS_enviado on PosicionesGPS (enviado asc)";
    public String IndicePosicionesGPS_fecha = "create index if not exists PosicionesGPS_fecha on PosicionesGPS (fecha asc)";
    public String IndicePosicionesGPS_usuario = "create index if not exists PosicionesGPS_usuario on PosicionesGPS (usuario asc)";
}
