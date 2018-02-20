package com.shuttles.shuttlesapp.ConnectionController;

import android.os.AsyncTask;
import android.util.Log;

import com.shuttles.shuttlesapp.ServerResultCallback;
import com.shuttles.shuttlesapp.Utils.Constants;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daeyonglee on 2018. 1. 22..
 */
public class RequestHandler extends AsyncTask<UploadData, Void, String> {
    private ServerResultCallback delegate = null;
    private HttpURLConnection conn = null;
    private BufferedReader reader = null;
    private UploadData uploadData = null;

    public RequestHandler(ServerResultCallback delegate){
        this.delegate=delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(UploadData... params) {
        String result = null;
        uploadData = params[0];
        try {
            URL requestURL = new URL(uploadData.getRestURL());
            conn = (HttpURLConnection)requestURL.openConnection();

            /*TODO: set detail options and timeout exception*/
            conn.setReadTimeout(Constants.CONNECTION_TIME_OUT);
            conn.setConnectTimeout(Constants.READ_TIME_OUT);
            conn.setRequestMethod(uploadData.getMethod());
            //conn.setRequestProperty("Content-Type","Application/json");

            if(uploadData.getMethod().equals("POST") || uploadData.getMethod().equals("PUT")) {
                conn.setDoOutput(true); //only use post or put
                OutputStream os = conn.getOutputStream();
                os.write(uploadData.getUploadJsonArray().toString().getBytes());
                os.flush();
                Log.i(Constants.LOG_TAG, "upload : "+uploadData.getUploadJsonArray().toString());
                os.close();
            }

            conn.setDoInput(true);
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
            conn.disconnect();
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
        delegate.onTaskFinish(Constants.TYPE_REQUEST_HANDLER, result);
    }
}

