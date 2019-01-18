package com.example.eduard.mobile.repository.online.utils.handlers;

import android.os.AsyncTask;

import com.example.eduard.mobile.utils.configuration.Config;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

abstract class NetworkHandler<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    HttpURLConnection doPost(String link, String headerName, String header, JSONObject entityToSend) throws Exception {

        URL url = new URL(link);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty(headerName, header);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        // send the request to server
        connection
                .getOutputStream()
                .write(
                        entityToSend
                                .toString()
                                .getBytes("UTF-8")
                );

        return connection;
    }

    HttpURLConnection doGet(String link, String headerName, String header) throws Exception {

        URL url = new URL(link);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setConnectTimeout(5000);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty(headerName, header);


        return connection;
    }

}
