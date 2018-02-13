package inteldev.android.negocios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Operador on 4/06/14.
 */
public class ConvertirFotoABase64
{

    public static String convertir(Context context, Uri uri) {

        byte[] data = null;
        String base64 = "";

        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            BufferedInputStream bis = new BufferedInputStream(is);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeStream(bis, null, options);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            data = bos.toByteArray();
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception ex) {
        }


        return base64;
    }
}
