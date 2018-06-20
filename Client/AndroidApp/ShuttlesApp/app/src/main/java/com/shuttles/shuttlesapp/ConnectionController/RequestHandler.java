package com.shuttles.shuttlesapp.ConnectionController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.shuttles.shuttlesapp.GlobalApplication;
import com.shuttles.shuttlesapp.Utils.Constants;
import com.shuttles.shuttlesapp.Utils.Utils;

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
    private ConnectionImpl delegate;
    private Context context;
    private HttpURLConnection conn = null;
    private BufferedReader reader = null;
    private RequestData requestData = null;
    private String connectionResult = Constants.RESPONSE_SUCCESS;
    private boolean isNetworkConnected = true;

    public RequestHandler(ConnectionImpl delegate) {
        this.context = GlobalApplication.getGlobalApplicationContext();
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!Utils.checkNetworkState())
            isNetworkConnected = false;
    }

    @Override
    protected String doInBackground(RequestData... params) {
        requestData = params[0];

        if (!isNetworkConnected)
            return Constants.RESPONSE_FAIL;
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
            /*
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
            }*/

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
            connectionResult = Constants.RESPONSE_FAIL;
            Toast.makeText(context,"연결에 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
        } finally {
            if (conn != null)
                conn.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return connectionResult;
        }
    }

    @Override
    protected void onProgressUpdate(Void... voids) {

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ConnectionResponse connectionResponse = new ConnectionResponse();

        Log.i(Constants.LOG_TAG, "onPostExecute result : " + result);

        if(result.equals(Constants.RESPONSE_FAIL)) {
            Log.e(Constants.LOG_TAG, "Reponse Failed!");
            connectionResponse.setRequestType(RestAPI.REQUEST_TYPE.FAILED);
        }
        else {
            Log.i(Constants.LOG_TAG,"Reponse success!");
            /*
            JsonObject rootObject = new JsonParser().parse(result).getAsJsonObject();
            JsonArray resultArray = rootObject.get("result").getAsJsonArray();

            Log.i(Constants.LOG_TAG, resultArray.toString()+"  response : " + rootObject.get("response").toString());
            String response = rootObject.get("response").toString();

            //TODO if 조건 만족하면
            requestData.setResult(resultArray.toString());
            */
            connectionResponse.setRequestType(requestData.getRequestType());
            connectionResponse.setResult(result);
        }

        delegate.requestCallback(connectionResponse);
    }
}

