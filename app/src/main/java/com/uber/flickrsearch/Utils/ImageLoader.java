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

public class ImageLoader {

    private final ImageView imageView;
    private static final String TAG = "ImageLoaderClass";
    private static ArrayList<AsyncTask> imageLoaderTasks = new ArrayList<>();

    public ImageLoader(final ImageView imageView, String imageUrl) {
        this.imageView = imageView;

        //
        try {
            AsyncTask task = new BitmapFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
            imageLoaderTasks.add(task);
        }
        catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void stopPendingTasks() {
        for (int i=0; i<imageLoaderTasks.size(); i++) {
            imageLoaderTasks.get(i).cancel(true);
        }
        imageLoaderTasks.clear();
    }

    private class BitmapFromURL extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            String imageUrl = params[0];

            try {
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
            Log.d(TAG, "Bitmap: " + result.getHeight() + " " + result.getWidth());
            imageView.setImageBitmap(result);
        }
    }
}



