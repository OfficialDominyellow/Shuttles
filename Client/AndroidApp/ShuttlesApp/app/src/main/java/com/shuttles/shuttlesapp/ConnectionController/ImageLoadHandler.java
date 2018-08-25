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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ImageLoadHandler extends AsyncTask<String, Void, ConnectionResponse> {
    private ConnectionImpl delegate ;
    private Context context;
    private List<Product> productList;

    private static SharedPreferences preferences = GlobalApplication.getGlobalApplicationContext().getSharedPreferences("image_cache", GlobalApplication.getGlobalApplicationContext().MODE_PRIVATE);
    private static SharedPreferences.Editor editor = preferences.edit();


    public ImageLoadHandler(ConnectionImpl delegate) {
        this.context = GlobalApplication.getGlobalApplicationContext();
        this.delegate = delegate;
    }

    public boolean isCached(String filenName) {
        String prefKey = filenName;
        String prefValue = preferences.getString(prefKey, null);
        File file = context.getFileStreamPath(filenName);

        if (file.exists() && prefValue.equals(filenName))
        {
            Log.i(Constants.LOG_TAG, "File exist, Name : " + filenName);
            return true;
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
    }

    @Override
    protected ConnectionResponse doInBackground(String... params) {
        ConnectionResponse connectionResponse = new ConnectionResponse();
        connectionResponse.setRequestType(RestAPI.REQUEST_TYPE.IMAGE_LOAD);
        String fileName = Utils.convertURLtoFileName(params[0]);

        try {
            if(isCached(fileName))
                return connectionResponse;

            URL imageURL = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection)imageURL.openConnection();

            byte[] buf = new byte[1024];
            InputStream is = conn.getInputStream();
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            int len;
            while ((len = is.read(buf)) > 0) {
                Log.i(Constants.LOG_TAG, "Now downloading... " + len);
                fos.write(buf, 0, len);
            }

            savePreference(fileName, fileName);

            is.close();
            fos.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(Constants.LOG_TAG, "exception " + e.getMessage());
        }

        return connectionResponse;
    }

    @Override
    protected void onProgressUpdate(Void... voids) {

    }

    @Override
    protected void onPostExecute(ConnectionResponse connectionResponse) {
        super.onPostExecute(connectionResponse);
        Log.i(Constants.LOG_TAG, "Load image result " + connectionResponse.getRequestType());

        //loadImageToProductList();

        delegate.requestCallback(connectionResponse);

    }

    private void loadImageToProductList(){
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
                Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                element.setImg(drawable);
            } catch (IOException e) {
                e.printStackTrace();
                element.setImg(null);//이미지가 서버에 없는 경우
            }
        }
        Log.i(Constants.LOG_TAG, "End Load Picture");
    }
}
