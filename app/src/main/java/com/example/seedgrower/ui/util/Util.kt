package com.example.seedgrower.ui.util

import com.pixplicity.easyprefs.library.Prefs

public class Util {

    fun isDebugModeOn() : Boolean{
        return true
    }

    fun isValidEmail(email : String) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun saveStringPref(key: String, value: String){
        Prefs.putString(key, value)
    }

    fun loadStringPref(key: String) : String {
        return Prefs.getString(key, null)
    }



}