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
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by daeyonglee on 2018. 1. 22..
 */
public class RequestHandler extends AsyncTask<RequestData, Void, ConnectionResponse> {
    private ConnectionImpl delegate;
    private Context context;
    private HttpURLConnection httpURLConnection;
    private BufferedReader reader;
    private RequestData requestData;
    private String connectionResult;

    public RequestHandler(ConnectionImpl delegate) {
        this.context = GlobalApplication.getGlobalApplicationContext();
        this.delegate = delegate;
    }

    private void sendRequestToServer() throws IOException {
        URL requestURL = new URL(requestData.getRestURL());
        httpURLConnection = (HttpURLConnection) requestURL.openConnection();
        httpURLConnection.setRequestMethod(requestData.getMethod());
        httpURLConnection.setRequestProperty("Content-Type", "Application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");

        /*TODO: set detail options and timeout exception*/
        httpURLConnection.setReadTimeout(Constants.CONNECTION_TIME_OUT);
        httpURLConnection.setConnectTimeout(Constants.READ_TIME_OUT);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDefaultUseCaches(false);
        httpURLConnection.setDoInput(true);

        if (requestData.getMethod().equals("POST") || requestData.getMethod().equals("PUT")) {
            httpURLConnection.setDoOutput(true); //only use post or put

            OutputStream os = httpURLConnection.getOutputStream();
            os.write(requestData.getPostData().getBytes("UTF-8"));
            os.flush();
            os.close();
            Log.i(Constants.LOG_TAG, "Send" + requestData.getPostData());
        } else
            httpURLConnection.setDoOutput(false);

        Log.i(Constants.LOG_TAG, "Method "+ requestData.getMethod() + " Use RESTAPI:" + requestData.getRestURL());
    }

    private void receiveResponseFromServer() throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;

        reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()
                , "UTF-8"));

        while ((line = reader.readLine()) != null) {
            builder.append((line));
        }
        connectionResult = builder.toString();
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
            sendRequestToServer();

            receiveResponseFromServer();

        } catch (Exception e) {
            e.printStackTrace();
            connectionResponse.setRequestType(RestAPI.REQUEST_TYPE.FAILED);
            Toast.makeText(context,"연결에 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
        } finally {
            connectionResponse.setResult(connectionResult);

            if (httpURLConnection != null)
                httpURLConnection.disconnect();
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

