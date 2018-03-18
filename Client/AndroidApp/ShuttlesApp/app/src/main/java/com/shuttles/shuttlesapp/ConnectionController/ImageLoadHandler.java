package com.shuttles.shuttlesapp.ConnectionController;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

    public ImageLoadHandler(ConnectionImpl delegate){
        this.context = GlobalApplication.getGlobalApplicationContext();
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!Utils.checkNetworkState())
            isNetworkConnected = false;
    }

    @Override
    protected String doInBackground(List<? extends Product>... params) {
        String result = "success";
        productList = (List<Product>) params[0];
        if(!isNetworkConnected)
            return "fail";

        //Download picture
        for(Product element : productList)
        {
            try {
                File file = context.getFileStreamPath(element.getPictureFileName());
                if (file.exists()) {
                    //file name 확인 후 이미 저장되어 있으면 continue
                    Log.i(Constants.LOG_TAG, "File already exist, Name : " + element.getPictureFileName());
                    continue;
                }

                URL imgURL = new URL(element.getPicture_url());
                HttpURLConnection conn = (HttpURLConnection) imgURL.openConnection();
                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                    Log.e(Constants.LOG_TAG, "HTTP Connection Error");
                    conn.disconnect();
                    continue;
                }

                byte[] buf = new byte[1024];
                InputStream is = conn.getInputStream();

                FileOutputStream fos = context.openFileOutput(element.getPictureFileName(), Context.MODE_PRIVATE);

                Log.i(Constants.LOG_TAG, "Start Download " + element.getPictureFileName());
                int len;
                while ((len = is.read(buf)) > 0) {
                    Log.i(Constants.LOG_TAG, "Now downloading... " + len);
                    fos.write(buf, 0, len);
                }
                is.close();
                fos.close();
                conn.disconnect();
            }
            catch (IOException e) {
                result = "fail";
                e.printStackTrace();
            }
            catch (Exception e){
                result = "fail";
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

        if (result == null || result.equals("fail")) {
            Log.e(Constants.LOG_TAG, "Image download fail!");
            delegate.requestCallback(RestAPI.REQUEST_TYPE_FAILED);
        } else if (result.equals("success")) {
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
            Log.i(Constants.LOG_TAG, "End Download");
            delegate.requestCallback(RestAPI.REQUEST_TYPE_IMAGE_LOAD);
        }
    }
}
