package com.example.eduard.mobile.repository.online.utils.handlers;

import android.content.Context;
import android.util.Pair;
import android.widget.Toast;

import com.example.eduard.mobile.repository.online.utils.OnSuccess;
import com.example.eduard.mobile.utils.configuration.Config;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

/**
 * Execute the call for inserting a new to rest server
 */
public class InsertHandler extends NetworkHandler<String, Void, Pair<Integer, String>> {

    public InsertHandler(Context context, OnSuccess success, boolean toastNotify) {
        this.context = new WeakReference<>(context);
        this.success = success;
        this.toast = toastNotify;
    }

    @Override
    protected Pair<Integer, String> doInBackground(String... strings) {

        jwt = strings[0];
        String first = strings[1];
        String last = strings[2];
        String phone = strings[3];

        int responseCode;
        String errorMessage = "";

        try {

            // prepare connection
            String url = Config.getInstance().getComposed(context.get(), "server-address", "add-contact-url");
            String header = Config.getInstance().getProperty(context.get(), "jwt-header");
            JSONObject entity = createEntity(first, last, phone);

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

    @Override
    protected void onPostExecute(Pair<Integer, String> integerStringPair) {

        super.onPostExecute(integerStringPair);

        if (integerStringPair.first == HttpURLConnection.HTTP_OK) {

            if(toast) {
                Toast.makeText(
                        context.get(),
                        "Contact inserted successfully",
                        Toast.LENGTH_LONG
                ).show();
            }

            success.update();
            return;
        }

        if(toast) {
            Toast.makeText(
                    context.get(),
                    "Error:" + integerStringPair.second,
                    Toast.LENGTH_LONG
            ).show();
        }
    }



    private JSONObject createEntity(String first, String last, String phone) throws Exception {
        JSONObject object = new JSONObject();
        object.put("first", first);
        object.put("last", last);
        object.put("phone", phone);
        return object;
    }


    private String jwt;
    private boolean toast;
    private OnSuccess success;
    private WeakReference<Context> context;
}

