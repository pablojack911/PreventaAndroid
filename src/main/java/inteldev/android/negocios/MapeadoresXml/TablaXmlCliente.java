package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 8/04/14.
 */
public class TablaXmlCliente extends TablaXml {

  public TablaXmlCliente()
  {
      super();

      this.NombreTabla = "Clientes";
      this.NombreTablaTagXml = "clientes";

      this.claves.add("idCliente");

      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idCliente","idCliente",String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idZona","idZona",String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idEstClie","idEstClie",String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("localidad","localidad",String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("condiva","condiva",Integer.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("nombre","nombre",String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("razonSocial","razonSocial",String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("domicilio","domicilio",String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("saldoCtacte","saldoCtacte",Double.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("limiteCtaCte","limiteCtaCte",Double.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate", Date.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado", Integer.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idCondvta","idCondVta", String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idRamo","idRamo", String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("visitado","visitado", Integer.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("cuit","cuit", String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idLista","lista", String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("telefono","telefono", String.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("tasaIibb","tasaIibb", Float.class));
      this.tagXmlCampoTablas.add( new TagXmlCampoTabla("canal","canal", String.class));
  }
}
