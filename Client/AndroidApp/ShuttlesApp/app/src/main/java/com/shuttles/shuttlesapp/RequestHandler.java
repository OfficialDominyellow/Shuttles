package com.shuttles.shuttlesapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daeyonglee on 2018. 1. 22..
 */
public class RequestHandler extends AsyncTask<String, Void, String> {
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
    protected String doInBackground(String... params) {//params[0] ip params[1] method
        String result = null;

        try {
            URL requestURL = new URL(params[0]);
            conn = (HttpURLConnection)requestURL.openConnection();

            /*TODO: set detail options and timeout exception*/

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod(params[1]);
            if(params[1].equals("POST"))
                conn.setDoOutput(true);
            conn.setDoInput(true);
            //conn.setDoOutput(false);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);

            StringBuilder builder = new StringBuilder();
            String line;

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()
                    ,"UTF-8"));

            while((line = reader.readLine())!=null){
                builder.append((line));
            }
            result = builder.toString();

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

        delegate.onTaskFinish(result);//callback Activity after communicating to the server


    }


}

