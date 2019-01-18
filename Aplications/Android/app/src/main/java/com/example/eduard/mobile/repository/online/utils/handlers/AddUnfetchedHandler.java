package com.example.eduard.mobile.repository.online.utils.handlers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.example.eduard.mobile.utils.configuration.Config;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddUnfetchedHandler extends NetworkHandler<String, Void, Pair<Integer, String>> {

    public AddUnfetchedHandler(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Pair<Integer, String> doInBackground(String... strings) {

        String jwt = strings[0];
        String tableName = strings[1];
        String data = strings[2];
        int type = Integer.parseInt(strings[3]);

        int responseCode;
        String errorMessage = "";

        try {

            // prepare connection
            String url = Config.getInstance().getComposed(context.get(), "server-address", "fetching-add-url");
            String header = Config.getInstance().getProperty(context.get(), "jwt-header");
            JSONObject entity = createEntity(tableName, data, type);

            // get response code
            HttpURLConnection connection = doPost(url, header, jwt, entity);
            responseCode = connection.getResponseCode();

            if (connection.getErrorStream() != null) {
                errorMessage = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream())
                ).readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return  new Pair<>(HttpURLConnection.HTTP_CLIENT_TIMEOUT, "Could not connect!");
        }

        return new Pair<>(responseCode, errorMessage);
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