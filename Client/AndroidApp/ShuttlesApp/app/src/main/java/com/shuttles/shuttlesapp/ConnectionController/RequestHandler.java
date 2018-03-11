package com.shuttles.shuttlesapp.ConnectionController;

import android.os.AsyncTask;
import android.util.Log;

import com.shuttles.shuttlesapp.Utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daeyonglee on 2018. 1. 22..
 */
public class RequestHandler extends AsyncTask<RequestData, Void, String> {
    private ConnectionImpl delegate = null;
    private HttpURLConnection conn = null;
    private BufferedReader reader = null;
    private RequestData requestData = null;

    public RequestHandler(ConnectionImpl delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(RequestData... params) {
        String result = null;
        requestData = params[0];
        try {
            URL requestURL = new URL(requestData.getRestURL());
            conn = (HttpURLConnection) requestURL.openConnection();

            /*TODO: set detail options and timeout exception*/
            conn.setReadTimeout(Constants.CONNECTION_TIME_OUT);
            conn.setConnectTimeout(Constants.READ_TIME_OUT);
            if (requestData.getMethod().equals("POST") || requestData.getMethod().equals("PUT")) {
                conn.setDoOutput(true); //only use post or put
            }
            conn.setRequestMethod(requestData.getMethod());
            //conn.setRequestProperty("Content-Type", "Application/json");

            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {//check server connection state
                Log.e(Constants.LOG_TAG, "HTTP Connection Error");
                conn.disconnect();
                return null;
            }

            if (requestData.getMethod().equals("POST") || requestData.getMethod().equals("PUT")) {
                //conn.setDoOutput(true); //only use post or put
                OutputStream os = conn.getOutputStream();
                os.write(requestData.getUploadJsonArray().toString().getBytes("UTF-8"));
                os.flush();
                Log.i(Constants.LOG_TAG, "upload : " + requestData.getUploadJsonArray().toString());
                os.close();
            }

            StringBuilder builder = new StringBuilder();
            String line;

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()
                    , "UTF-8"));

            while ((line = reader.readLine()) != null) {
                builder.append((line));
            }
            result = builder.toString();
            Log.i(Constants.LOG_TAG,"request result : "+result);

        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        } finally {
            if(conn!=null)
                conn.disconnect();
            try {
                if (reader != null)
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

        requestData.setResult(result);

        if(requestData.getResult()==null)
            requestData.setRequest_type(RestAPI.REQUEST_TYPE_FAILED);

        delegate.requestCallback(requestData.getRequest_type());
    }
}

