package com.example.eduard.mobile.login.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.login.handles.RegisterHandle;
import com.example.eduard.mobile.repository.RepositoryProvider;
import com.example.eduard.mobile.repository.room.database.AppDatabase;
import com.example.eduard.mobile.repository.room.entity.User;
import com.example.eduard.mobile.utils.gui.GuiUtils;
import com.example.eduard.mobile.utils.validators.RegisterValidator;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
    }

    public void handleLoginClick(View view){
        finish();
    }

    public void handleClickRegister(View view){

        try{

            String user = username.getText().toString();
            String pass = password.getText().toString();

            RegisterValidator.validate(
                    user,
                    pass,
                    confirmPassword.getText().toString()
            );

            new RegisterHandle(this, user, pass, ()->{
                AppDatabase
                        .getInstance(this)
                        .userDao()
                        .add(new User(
                                user,
                                pass
                        ));
            }).execute();

        }catch (Exception e){
            GuiUtils.displayErrorDialog(e.getMessage(), RegisterActivity.this, "Error");
        }

    }

    @Override
    public  void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private EditText username;
    private EditText password;
    private EditText confirmPassword;
}
