package br.com.viniciusalmada.civilapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.viniciusalmada.civilapp.interfaces.HandlerDownload;

/**
 * Created by vinicius-almada on 18/03/17.
 */

public class DownloaderImagesAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private Context context;
    private HandlerDownload handlerDownload;

    public DownloaderImagesAsyncTask(Context context, HandlerDownload handlerDownload) {
        this.context = context;
        this.handlerDownload = handlerDownload;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap img = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            img = BitmapFactory.decodeStream(is);
        } catch (IOException e) {

        }
        return img;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        handlerDownload.setImageAfterDownload(bitmap);
    }
}
