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

public class DeleteHandler extends NetworkHandler<String, Void, Pair<Integer, String>> {

    public DeleteHandler(Context context, OnSuccess success, boolean toastNotify) {
        reference = new WeakReference<>(context);
        this.success = success;
        this.toast = toastNotify;
    }

    @Override
    protected Pair<Integer, String> doInBackground(String... strings) {

        jwt = strings[0];
        String first = strings[1];

        int responseCode;
        String errorMessage = "";

        try {

            String url = Config.getInstance().getComposed(reference.get(), "server-address", "delete-contact-url");
            String header = Config.getInstance().getProperty(reference.get(), "jwt-header");
            JSONObject entity = createEntity(first);

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
                        reference.get(),
                        "Contact deleted successfully",
                        Toast.LENGTH_LONG
                ).show();
            }

            success.update();
            return;
        }

        if(toast) {
            Toast.makeText(
                    reference.get(),
                    "Error:" + integerStringPair.second,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private JSONObject createEntity(String first) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first", first);
        return jsonObject;
    }

    private String jwt;
    private boolean toast;
    private OnSuccess success;
    private WeakReference<Context> reference;

}