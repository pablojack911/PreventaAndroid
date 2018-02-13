package inteldev.android.presentation;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import inteldev.android.negocios.BorrarArchivos;
import inteldev.android.negocios.IncorporaXml;


/**
 * DownloadManager
 * <p>
 * Helps downloading a file and saving it.
 *
 * @author Gabriel Verdi
 * @version 1.0 06 march of 2014
 */
public class DescargaManager {

    private String title;
    private String fileName;
    private Context view;
    private long enqueue;
    public int veces;
    private DescargaListener descargaListener;
    private boolean descargoInteldevMobile;
    private boolean descargoInteldevMobile2;
    public ArrayList<Long> listaIdDownload;

    public void setDescargaListener(DescargaListener descargaListener) {
        this.descargaListener = descargaListener;
    }

    public DescargaManager(Context view) {
        this.view = view;
        this.listaIdDownload = new ArrayList<Long>();
    }

    public void downloadFile(String url) {
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(url));
        request.setDescription("");
        request.setTitle(this.title);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, this.fileName);
        // get download service and enqueue file
        android.app.DownloadManager manager = (android.app.DownloadManager) view.getSystemService(Context.DOWNLOAD_SERVICE);

        receptorDescarga(manager);

        String directory = Environment.getExternalStorageDirectory().toString();
        String ubicacion = directory + "/" + Environment.DIRECTORY_DOWNLOADS;

        BorrarArchivos borrarArchivos = new BorrarArchivos();
        borrarArchivos.BorrarPorContenido(ubicacion, this.fileName);

        enqueue = manager.enqueue(request);
        listaIdDownload.add(enqueue);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void receptorDescarga(final android.app.DownloadManager manager) {
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                    boolean encontro = false;
                    for (int i = 0; i < listaIdDownload.size(); i++) {
                        Long item = listaIdDownload.get(i);
                        if (item == downloadId) {
                            encontro = true;
                        }
                    }

                    if (encontro) {
                        Query query = new Query();
                        query.setFilterById(downloadId);
                        Cursor c = manager.query(query);
                        if (c.moveToFirst()) {
                            int columnIndex = c
                                    .getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (DownloadManager.STATUS_SUCCESSFUL == c
                                    .getInt(columnIndex)) {

                                String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                                if (uriString.contains("InteldevMobile.xml") || uriString.contains("InteldevMobile-1.xml"))
                                    descargoInteldevMobile = true;

                                if (uriString.contains("InteldevMobile2.xml"))
                                    descargoInteldevMobile2 = true;


                                if (descargoInteldevMobile && descargoInteldevMobile2) {

                                    descargaListener.Notificar(true);
                                    view.unregisterReceiver(this);


                                }
                            }
                            if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {

                                descargaListener.Notificar(false);
                                view.unregisterReceiver(this);

                            }

                        }
                    }
                }

            }
        };

        view.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }
}
