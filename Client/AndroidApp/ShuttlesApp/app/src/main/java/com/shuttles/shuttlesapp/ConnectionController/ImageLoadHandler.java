package com.shuttles.shuttlesapp.ConnectionController;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.shuttles.shuttlesapp.GlobalApplication;
import com.shuttles.shuttlesapp.Utils.Constants;
import com.shuttles.shuttlesapp.Utils.Utils;
import com.shuttles.shuttlesapp.vo.Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by daeyonglee on 2018. 2. 11..
 */

public class ImageLoadHandler extends AsyncTask<List<? extends Product>, Void, String> {
    private ConnectionImpl delegate = null;
    private Context context = null;
    private List<Product> productList = null;
    private boolean isNetworkConnected = true;

    private static SharedPreferences preferences = GlobalApplication.getGlobalApplicationContext().getSharedPreferences("image_cache", GlobalApplication.getGlobalApplicationContext().MODE_PRIVATE);
    private static SharedPreferences.Editor editor = preferences.edit();


    public ImageLoadHandler(ConnectionImpl delegate) {
        this.context = GlobalApplication.getGlobalApplicationContext();
        this.delegate = delegate;
    }

    public boolean isCached(Product element) {
        String prefKey = element.getName() + element.getID();
        String prefValue = preferences.getString(prefKey, null);

        File file = context.getFileStreamPath(element.getPictureFileName());
        if (file.exists() && prefValue != null) {
            if (prefValue.equals(element.getPicture_version())) {
                Log.i(Constants.LOG_TAG, "File exist, Name : " + element.getPictureFileName() + " prefKey : " +prefKey +" prefValue : "+prefValue + " == "+element.getPicture_version() );
                return true;
            }
        }
        return false;
    }

    public void savePreference(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!Utils.checkNetworkState())
            isNetworkConnected = false;
    }

    @Override
    protected String doInBackground(List<? extends Product>... params) {
        if(!isNetworkConnected)
            return "fail";

        String result = Constants.RESPONSE_SUCCESS;
        productList = (List<Product>) params[0];

        Log.i(Constants.LOG_TAG, "start download file count : " + productList.size());
        //Download picture
        for(Product element : productList)
        {
            try {
                if (isCached(element))
                    continue;

                Log.i(Constants.LOG_TAG, "not cachend");

                URL imgURL = new URL(element.getPicture_url());
                HttpURLConnection conn = (HttpURLConnection) imgURL.openConnection();

                /*
                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                    Log.e(Constants.LOG_TAG, "HTTP Connection Error");
                    conn.disconnect();
                    throw new IOException();
                }
                */

                //Download Product's picture from server
                byte[] buf = new byte[1024];
                InputStream is = conn.getInputStream();
                FileOutputStream fos = context.openFileOutput(element.getPictureFileName(), Context.MODE_PRIVATE);

                Log.i(Constants.LOG_TAG, "Start Download " + element.getPictureFileName());
                int len;
                while ((len = is.read(buf)) > 0) {
                    Log.i(Constants.LOG_TAG, "Now downloading... " + len);
                    fos.write(buf, 0, len);
                }

                savePreference(element.getName() + element.getID(), element.getPicture_version());

                is.close();
                fos.close();
                conn.disconnect();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(Void... voids) {

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ConnectionResponse connectionResponse = new ConnectionResponse();

        if (result.equals(Constants.RESPONSE_SUCCESS)) {
            for (Product element : productList) {
                //Load image to VO class
                try {
                    FileInputStream fis = context.openFileInput(element.getPictureFileName());
                    int len;
                    byte buf[] = new byte[fis.available()];
                    while ((len = fis.read(buf)) != -1) {
                        Log.i(Constants.LOG_TAG, "Load image from storage " + len);
                    }
                    fis.close();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length);
                    //convert bitmap to drwable
                    Drawable drawble = new BitmapDrawable(context.getResources(), bitmap);
                    element.setImg(drawble);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    element.setImg(null);
                } catch (IOException e) {
                    e.printStackTrace();
                    element.setImg(null);
                }
            }
            Log.i(Constants.LOG_TAG, "End Load Picture");
            connectionResponse.setResponseType(RestAPI.REQUEST_TYPE_IMAGE_LOAD);
        } else {
            Log.e(Constants.LOG_TAG, "Image download fail!");
            connectionResponse.setResponseType(RestAPI.REQUEST_TYPE_FAILED);
        }
        delegate.requestCallback(connectionResponse);
    }
}
