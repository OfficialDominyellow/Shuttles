package com.shuttles.shuttlesapp.ConnectionController;

import android.os.AsyncTask;

import com.shuttles.shuttlesapp.AsyncCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daeyonglee on 2018. 1. 22..
 */
public class RequestHandler extends AsyncTask<UploadData, Void, String> {
    private AsyncCallback delegate;
    private HttpURLConnection conn;
    private BufferedReader reader;

    public RequestHandler(AsyncCallback delegate){
        this.delegate=delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(UploadData... uploadData) {//params[0] ip params[1] method
        String result = null;

        try {
            URL requestURL = new URL(uploadData[0].getRestURL());
            conn = (HttpURLConnection)requestURL.openConnection();

            /*TODO: set detail options and timeout exception*/

            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setRequestMethod(uploadData[0].getMethod());


            if(uploadData[0].equals("POST") || uploadData[0].equals("PUT")) {
                conn.setDoOutput(true);
                //get json data for post or put method
                uploadData[0].getUploadJson();
            }


            conn.setDoInput(true);
            //conn.setDoOutput(false);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {//check server connection state
                StringBuilder builder = new StringBuilder();
                String line;

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()
                        , "UTF-8"));

                while ((line = reader.readLine()) != null) {
                    builder.append((line));
                }
                result = builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn!=null)
                conn.disconnect();
            if(reader!=null)
                try {
                    reader.close();
                } catch (IOException e) {
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
        //return result to caller
        delegate.onTaskFinish(result);
    }


}

