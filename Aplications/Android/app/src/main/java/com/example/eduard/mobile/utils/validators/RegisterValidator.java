package com.example.eduard.mobile.utils.validators;

import android.content.Context;

public class RegisterValidator {

    public static void validate(
            String username,
            String password,
            String confirmedPassword) throws  Exception{

        if(username.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()){
            throw new Exception("All fields are mandatory!");
        }

        if(!password.equals(confirmedPassword)){
            throw new Exception("Passwords are not equal!");
        }
    }
}
