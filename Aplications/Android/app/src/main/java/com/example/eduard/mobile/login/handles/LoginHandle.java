package com.example.eduard.mobile.login.handles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.Gravity;
import android.widget.Toast;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.login.activities.AuthenticatedActivity;
import com.example.eduard.mobile.utils.configuration.Config;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

import dmax.dialog.SpotsDialog;

public class LoginHandle extends AsyncTask<Void, String, Pair<String, Integer>> {

    public LoginHandle(final String username, final String password, Context context) {

        this.username = username;
        this.password = password;
        this.context = context;

        Config config = Config.getInstance();

        this.url = config.getComposed(
                context, "server-address", "login-url"
        );

        this.header = config.getProperty(context, "jwt-header");

        dialog = new SpotsDialog(context, R.style.SpotsDialog);
        dialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    /**
     * Does in background the request to server for login
     *
     * @return the
     */
    @Override
    protected Pair<String, Integer> doInBackground(Void... voids) {

        String jwtToken = null;
        Integer responseCode = -1;

        try {

            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            JSONObject entity = createEntity(username, password);

            // send the request to server
            connection
                    .getOutputStream()
                    .write(
                            entity
                                    .toString()
                                    .getBytes("UTF-8")
                    );

            if ((responseCode = connection.getResponseCode()) == HttpURLConnection.HTTP_ACCEPTED) {
                jwtToken = connection.getHeaderField(header);
            }


        } catch (Exception e) {
            responseCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT;
            e.printStackTrace();
        }

        return new Pair<>(jwtToken, responseCode);
    }

    @Override
    protected void onPostExecute(Pair<String, Integer> response) {

        String jwtToken = response.first;
        int errorCode = response.second;

        super.onPostExecute(response);

        dialog.dismiss();

        if(errorCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
            Toast toast = Toast.makeText(context, "Could not connect!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
            return;
        }

        if (errorCode != HttpURLConnection.HTTP_ACCEPTED) {
            Toast toast = Toast.makeText(context, "Wrong username or password!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
            return;
        }

        Intent intent = new Intent(
                context,
                AuthenticatedActivity.class);

        intent.putExtra("Token", jwtToken);
        intent.putExtra("Username", username);

        context.startActivity(intent);

        ((Activity)context)
                .overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                );
    }

    /**
     * Creates the entity that will be sent to server
     *
     * @param username: username
     * @param password: password
     * @return a json object representing the request mapping
     * @throws Exception if the json cannot be created
     */
    private JSONObject createEntity(final String username, final String password) throws Exception {

        JSONObject entity = new JSONObject();

        entity.put("user", username);
        entity.put("pass", password);

        return entity;
    }

    private String url;
    private String username;
    private String password;
    private String header;
    private SpotsDialog dialog;

    @SuppressLint("StaticFieldLeak")
    private Context context;
}
