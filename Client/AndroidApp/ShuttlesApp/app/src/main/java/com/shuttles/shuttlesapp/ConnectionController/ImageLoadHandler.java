package com.shuttles.shuttlesapp.ConnectionController;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.shuttles.shuttlesapp.GlobalApplication;
import com.shuttles.shuttlesapp.ServerResultCallback;
import com.shuttles.shuttlesapp.Utils.Constants;
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

public class ImageLoadHandler extends AsyncTask<List<? extends Product>, Void, String> implements ServerResultCallback{
    private ServerResultCallback delegate = null;
    private Context context = null;
    private List<Product> productList = null;

    public ImageLoadHandler(ServerResultCallback delegate){
        this.context = GlobalApplication.getGlobalApplicationContext();
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(List<? extends Product>... params) {
        String result = "success";
        productList = (List<Product>) params[0];
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
                result = null;
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
        for(Product element : productList){
            //Load image to VO class
            try {
                FileInputStream fis = context.openFileInput(element.getPictureFileName());
                int len;
                byte buf[] = new byte[fis.available()];
                while((len = fis.read(buf))!=-1){
                    Log.i(Constants.LOG_TAG,"Load image from storage "+len);
                }
                fis.close();
                Bitmap bitmap = BitmapFactory.decodeByteArray(buf,0,buf.length);
                //convert bitmap to drwable
                Drawable drawble = new BitmapDrawable(context.getResources(), bitmap);
                element.setImg(drawble);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(Constants.LOG_TAG,"End Download");
        delegate.onTaskFinish(Constants.TYPE_IMAGE_LOAD_HANDLER, null);
    }

    @Override
    public void onTaskFinish(int type, String result) {
        delegate.onTaskFinish(Constants.TYPE_IMAGE_LOAD_HANDLER, result);
    }
}
