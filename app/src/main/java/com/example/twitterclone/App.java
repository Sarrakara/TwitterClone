package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("YH75RBo2iy2UigNDLzZcKsklIXKgmWZ3tCQ05liV")
                // if defined
                .clientKey("YeCrdNv9q18Bp9WfGgtu8axQc9oCCfB4ZeOZmp5K")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
