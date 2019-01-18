package com.example.eduard.mobile.login.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.login.handles.LoginHandle;
import com.example.eduard.mobile.utils.configuration.Config;
import com.example.eduard.mobile.utils.gui.GuiUtils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        config = Config.getInstance();
    }

    /**
     * Handle the click event from register button
     * @param view: button
     */
    public void handleRegisterClick(View view){

        startActivity(
                new Intent(
                        LoginActivity.this,
                        RegisterActivity.class
                )
        );

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        redirectIfStillLoggedIn();
    }

    /**
     * Handle the login click action
     * @param view: the view
     */
    public void handleLogin(View view) {

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if(username.isEmpty() || password.isEmpty()){
            GuiUtils.displayErrorDialog(
                    "Username and password fields are mandatory!",
                    LoginActivity.this,
                    "Error"
            );
            return;
        }

        new LoginHandle(username, password, LoginActivity.this).execute();
    }

    /**
     * If the user is still authenticated, then the the page after login will be displayed
     */
    private void redirectIfStillLoggedIn(){

        String name = config.getProperty(getApplicationContext(), "shared-pref-name");
        String jwtToken = getSharedPreferences(name, Context.MODE_PRIVATE).getString("jwt-token", "");

        if(Objects.requireNonNull((jwtToken)).isEmpty()){
            return;
        }

        String username = getSharedPreferences(name, Context.MODE_PRIVATE).getString("username", "");

        Intent intent = new Intent(
                LoginActivity.this,
                AuthenticatedActivity.class);

        intent.putExtra("Token", jwtToken);
        intent.putExtra("Username", username);

        startActivity(intent);
    }

    private EditText username;
    private EditText password;
    private Config config;
}
