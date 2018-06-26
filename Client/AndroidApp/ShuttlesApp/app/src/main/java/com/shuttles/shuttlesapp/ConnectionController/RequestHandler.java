package com.shuttles.shuttlesapp.ConnectionController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.shuttles.shuttlesapp.GlobalApplication;
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
public class RequestHandler extends AsyncTask<RequestData, Void, ConnectionResponse> {
    private ConnectionImpl delegate;
    private Context context;
    private HttpURLConnection conn = null;
    private BufferedReader reader = null;
    private RequestData requestData = null;
    private String connectionResult = null;

    public RequestHandler(ConnectionImpl delegate) {
        this.context = GlobalApplication.getGlobalApplicationContext();
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ConnectionResponse doInBackground(RequestData... params) {
        requestData = params[0];
        ConnectionResponse connectionResponse = new ConnectionResponse();
        connectionResponse.setRequestType(requestData.getRequestType());

        try {
            URL requestURL = new URL(requestData.getRestURL());
            conn = (HttpURLConnection) requestURL.openConnection();
            conn.setRequestMethod(requestData.getMethod());
            conn.setRequestProperty("Content-Type", "Application/json");

            /*TODO: set detail options and timeout exception*/
            conn.setReadTimeout(Constants.CONNECTION_TIME_OUT);
            conn.setConnectTimeout(Constants.READ_TIME_OUT);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);

            if (requestData.getMethod().equals("POST") || requestData.getMethod().equals("PUT")) {
                conn.setDoOutput(true); //only use post or put
                OutputStream os = conn.getOutputStream();
                os.write(requestData.getPostData().getBytes("UTF-8"));
                os.flush();
                os.close();
                Log.i(Constants.LOG_TAG, "upload : " + requestData.getPostData());
            } else
                conn.setDoOutput(false);
            Log.i(Constants.LOG_TAG, "Method "+ requestData.getMethod() + " Use RESTAPI:" + requestData.getRestURL());

            StringBuilder builder = new StringBuilder();
            String line;

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()
                    , "UTF-8"));

            while ((line = reader.readLine()) != null) {
                builder.append((line));
            }
            connectionResult = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            connectionResponse.setRequestType(RestAPI.REQUEST_TYPE.FAILED);
            Toast.makeText(context,"연결에 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
        } finally {
            connectionResponse.setResult(connectionResult);

            if (conn != null)
                conn.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return connectionResponse;
        }
    }

    @Override
    protected void onProgressUpdate(Void... voids) {

    }

    @Override
    protected void onPostExecute(ConnectionResponse connectionResponse) {
        super.onPostExecute(connectionResponse);

        Log.i(Constants.LOG_TAG, "onPostExecute result : " + connectionResponse.getResult());

        if(connectionResponse.getRequestType() != RestAPI.REQUEST_TYPE.FAILED) {
            Log.i(Constants.LOG_TAG,"Reponse success!");
            connectionResponse.setRequestType(requestData.getRequestType());
        }

        delegate.requestCallback(connectionResponse);
    }
}

