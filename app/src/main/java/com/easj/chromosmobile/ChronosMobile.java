package com.easj.chromosmobile;

import android.app.Application;

import androidx.room.Room;

public class ChronosMobile extends Application {
    private static ChronosMobile instance;
    private static AppDatabase appDatabase;

    public static boolean isPassPassed() {
        return passPassed;
    }

    public void setPassPassed(boolean passPassed) {
        this.passPassed = passPassed;
    }

    public static boolean passPassed = false;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dbChronosMobile")
                .allowMainThreadQueries()
                .build();
        // Puedes realizar inicializaciones generales aquí
    }

    public static AppDatabase getAppDatabase(){
        return appDatabase;
    }

    public static synchronized ChronosMobile getInstance() {
        return instance;
    }
}
