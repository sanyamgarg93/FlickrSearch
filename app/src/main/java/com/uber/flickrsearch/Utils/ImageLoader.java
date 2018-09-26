package com.uber.flickrsearch.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

/*
 * ImageLoader class downloads a bitmap from a given url and
 * fills the imageview with the bitmap.
 * AsyncTask is used to construct bitmap using an HTTP connection.
 * Further,
 */
public class ImageLoader {

    private final ImageView imageView;
    private static final String TAG = "ImageLoaderClass";
    private static ArrayList<AsyncTask> imageLoaderTasks = new ArrayList<>();

    public ImageLoader(final ImageView imageView, String imageUrl) {
        this.imageView = imageView;

        // Surrounded in a try/catch because of RejectedExecutionException due to more than 128
        // queued AsyncTasks.
        try {
            // executeOnExecutor() used instead of execute() to enable parallel execution of multiple AsyncTasks
            AsyncTask task = new BitmapFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);

            // Each AsyncTask is stored in a static array to enable cancellation later
            imageLoaderTasks.add(task);
        }
        catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }

    /*
     * Cancels pending AsyncTasks
     */
    public static void stopPendingTasks() {
        for (int i=0; i<imageLoaderTasks.size(); i++) {
            imageLoaderTasks.get(i).cancel(true);
        }
        imageLoaderTasks.clear();
    }

    /*
     * Fetches byte stream using HTTP connection and converts into Bitmap.
     * The bitmap is then filled into the provided imageview.
     */
    private class BitmapFromURL extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            String imageUrl = params[0];

            try {
                Log.d(TAG, "Fetching image with url: " + imageUrl);
                URL url = new URL(imageUrl);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}



