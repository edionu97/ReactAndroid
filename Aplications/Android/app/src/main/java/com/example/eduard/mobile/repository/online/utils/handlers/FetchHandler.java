package com.example.eduard.mobile.repository.online.utils.handlers;

import android.content.Context;
import android.os.AsyncTask;

import com.example.eduard.mobile.utils.configuration.Config;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchHandler extends NetworkHandler<String, Void, Void> {

    public FetchHandler(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(String... strings) {

        String jwt = strings[0];
        String tableName = strings[1];
        String data = strings[2];
        int type = Integer.parseInt(strings[3]);

        try {

            // prepare connection
            String url = Config.getInstance().getComposed(context.get(), "server-address", "fetching-fetch-url");
            String header = Config.getInstance().getProperty(context.get(), "jwt-header");
            JSONObject entity = createEntity(tableName, data, type);

            // get response
            HttpURLConnection connection = doPost(url, header, jwt, entity);
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private JSONObject createEntity(String tableName, String data, int type) throws Exception {

        JSONObject object = new JSONObject();

        object.put("table", tableName);
        object.put("data", data);
        object.put("type", type);

        return object;
    }


    private WeakReference<Context> context;
}
