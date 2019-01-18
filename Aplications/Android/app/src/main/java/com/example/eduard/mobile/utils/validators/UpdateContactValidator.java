package com.example.eduard.mobile.utils.validators;

import java.util.regex.Pattern;

public class UpdateContactValidator {

    public static void validate(String first, String last, String phone) throws Exception {

        if (first.isEmpty() || last.isEmpty() || phone.isEmpty()) {
            throw new Exception("All fields are mandatory!");
        }

        if(Pattern.compile("[^0-9+]").matcher(phone).find()){
            throw new Exception("You must enter a valid number");
        }
    }
}
