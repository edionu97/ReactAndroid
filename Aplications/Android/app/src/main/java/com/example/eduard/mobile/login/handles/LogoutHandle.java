package com.example.eduard.mobile.login.handles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import com.example.eduard.mobile.utils.configuration.Config;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class LogoutHandle extends AsyncTask<Void, Void, Boolean> {

    public LogoutHandle(Context context, final String username, final String jwtToken){


        this.username = username;
        this.jwtToken = jwtToken;
        this.context = context;

        this.url = Config
                .getInstance()
                .getComposed(context, "server-address", "logout-url");
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try{

            URL url = new URL(this.url);
            String header = Config.getInstance().getProperty(context, "jwt-header");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty(header, jwtToken);
            connection.setDoOutput(true);


            JSONObject object = createEntity(username);

            connection
                    .getOutputStream()
                    .write(
                            object.toString().getBytes("UTF-8")
                    );

            if(connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean response) {

        super.onPostExecute(response);

        if(!response){
            Toast toast = Toast.makeText(context, "Could not logout!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0,0);
            toast.show();
            return;
        }

        deleteJwt();

        // return to main activity
        ((Activity)context).finish();
    }

    private JSONObject createEntity(final String username) throws  Exception{
        JSONObject object = new JSONObject();
        object.put("user", username);
        return object;
    }

    private void deleteJwt(){

        String name="";
        try{
            name = Config.getInstance().getProperty(context, "shared-pref-name");
        }catch (Exception e){
            e.printStackTrace();
        }

        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwt-token");
        editor.remove("username");
        editor.apply();
    }


    @SuppressLint("StaticFieldLeak")
    private Context context;

    private String username;
    private String jwtToken;
    private String url;
}
