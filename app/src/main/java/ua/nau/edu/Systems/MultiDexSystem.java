package ua.nau.edu.Systems;

import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

public class MultiDexSystem extends MultiDexApplication {
    public void onCreate(Bundle arguments) {
        MultiDex.install(this);
        super.onCreate();
    }
}
