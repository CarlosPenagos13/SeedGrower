package com.example.seedgrower;

import android.app.Application
import android.content.ContextWrapper

import com.example.seedgrower.handler.SocketHandler;
import com.pixplicity.easyprefs.library.Prefs

public class SeedGrowerApplication : Application() {

    override fun onCreate() {
        super.onCreate();

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        SocketHandler()
    }
}