package com.shuttles.shuttlesapp.ConnectionController;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.shuttles.shuttlesapp.Utils.Constants;
import com.shuttles.shuttlesapp.vo.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by daeyonglee on 2018. 2. 11..
 */

public class ImageLoadHandler extends AsyncTask<List<? extends Product>, Void, Void> {
    private Context mContext;
    private List<Product> prodctList;

    public ImageLoadHandler(Context context){
        this.mContext=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(List<? extends Product>... params) {

        prodctList = (List<Product>) params[0];

        //Download picture
        for(Product element : prodctList){
            try {
                URL imgURL = new URL(element.getPicture_url());
                HttpURLConnection conn = (HttpURLConnection)imgURL.openConnection();
                byte[] buf = new byte[1024];
                InputStream is = conn.getInputStream();

                //file name 확인 후 이미 저장되어 있으면 continue
                OutputStream fos = mContext.openFileOutput(element.getPictureFileName(),Context.MODE_PRIVATE);

                int len;
                while((len = is.read(buf))>0){
                    Log.i(Constants.LOG_TAG,"Start Download "+len);
                    fos.write(buf,0,len);
                }
                is.close();
                fos.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... voids) {

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        for(Product element : prodctList){
            //Load image to VO class

            try {
                FileInputStream fis = mContext.openFileInput(element.getPictureFileName());
                int len;
                byte buf[] = new byte[fis.available()];
                while((len = fis.read(buf))!=-1){
                    Log.i(Constants.LOG_TAG,"Now Download "+len);
                }
                fis.close();
                Bitmap bitmap = BitmapFactory.decodeByteArray(buf,0,buf.length);
                //convert bitmap to drwable
                Drawable drawble = new BitmapDrawable(mContext.getResources(), bitmap);
                element.setImg(drawble);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(Constants.LOG_TAG,"End Download");
    }
}
