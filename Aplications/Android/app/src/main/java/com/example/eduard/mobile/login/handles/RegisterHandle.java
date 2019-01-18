package com.example.eduard.mobile.login.handles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import com.example.eduard.mobile.login.messages.RegisterMessage;
import com.example.eduard.mobile.repository.room.database.AppDatabase;
import com.example.eduard.mobile.repository.room.entity.User;
import com.example.eduard.mobile.utils.configuration.Config;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterHandle extends AsyncTask<Void, Void, RegisterMessage> {


    public RegisterHandle(final Context context, final  String username, final String password, Runnable runnable){

        Config config = Config.getInstance();

        this.username = username;
        this.password = password;
        this.context = context;
        this.onSucess = runnable;

        this.url = config.getComposed(context, "server-address", "register-url");
    }

    @Override
    protected RegisterMessage doInBackground(Void... voids) {

        RegisterMessage message = new RegisterMessage();

        try{
            URL url = new URL(this.url);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject entity = createEntity();

            connection
                    .getOutputStream()
                    .write(
                            entity.toString().getBytes("UTF-8")
                    );

            message.setCode(
                    connection.getResponseCode()
            );

        }catch (Exception e){
            message.setCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
            e.printStackTrace();
        }

        return message;
    }

    @Override
    protected void onPostExecute(RegisterMessage registerMessage) {

        super.onPostExecute(registerMessage);

        if(registerMessage.getCode() == HttpURLConnection.HTTP_MOVED_TEMP){

            Toast toast = Toast.makeText(
                    context,
                    "Username already exists",
                    Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM, 0,0);
            toast.show();
            return;
        }

        if(registerMessage.getCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT){

            Toast toast = Toast.makeText(
                    context,
                    "Could not connect!",
                    Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM, 0,0);
            toast.show();
            return;
        }

        Toast toast = Toast.makeText(context, "Account successfully created!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0,0);
        toast.show();

        new Thread(onSucess).start();

        ((Activity)context).finish();
    }

    private JSONObject createEntity() throws  Exception{

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("user", username);
        jsonObject.put("pass", password);

        return jsonObject;
    }

    private String url;
    private final String username;
    private final String password;
    private  Runnable onSucess;

    @SuppressLint("StaticFieldLeak")
    private final Context context;
}
