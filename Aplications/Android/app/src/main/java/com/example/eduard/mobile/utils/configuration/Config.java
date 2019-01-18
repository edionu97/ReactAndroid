package com.example.eduard.mobile.utils.configuration;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
    }

    public String getProperty(Context context, final String propertyName) {
        try {
            InputStream inputStream = context.getAssets().open("config.properties");
            Properties properties = new Properties();

            properties.load(inputStream);

            return properties.getProperty(propertyName);
        }catch (Exception ex){
            return null;
        }
    }

    public  String getComposed(Context context,
                                       final  String firstPart,
                                       final  String secondPart ){
        String first = getProperty(context, firstPart);
        String second = getProperty(context, secondPart);
        return first + second;
    }

    private static final Config ourInstance = new Config();
}
