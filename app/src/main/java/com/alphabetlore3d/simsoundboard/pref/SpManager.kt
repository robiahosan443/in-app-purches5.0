package com.alphabetlore3d.simsoundboard.pref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.alphabetlore3d.simsoundboard.j

object SpManager {
    private lateinit var sp: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor

    private const val SP_APP = "spApp"
    const val SP_SHOW_CUSTOM_INTERS = "showCustomInters"
    const val SP_IS_SUBSCRIBED = "isSubscribed"

    fun init(context: Context?) {
        try {
            sp = context?.getSharedPreferences(SP_APP, Context.MODE_PRIVATE)!!
            spEditor = sp.edit()
        }
        catch (e: Exception) {
            Log.e("SPManager", "SPManager: "+e.message)
        }

    }

    fun saveSPString(keySP: String, value: String) {
        init(j.context)
        spEditor.putString(keySP, value)
        spEditor.commit()
    }

    fun saveSPInt(keySP: String, value: Int) {
        init(j.context)
        spEditor.putInt(keySP, value)
        spEditor.commit()
    }

    fun saveSPDouble(keySP: String, value: Double) {
        init(j.context)
        spEditor.putLong(keySP, java.lang.Double.doubleToRawLongBits(value))
        spEditor.commit()
    }

    fun saveSPLong(keySP: String, value: Long) {
        init(j.context)
        spEditor.putLong(keySP, value)
        spEditor.commit()
    }

    fun saveSPFloat(keySP: String, value: Float) {
        init(j.context)
        spEditor.putFloat(keySP, value)
        spEditor.commit()
    }

    fun saveSPBoolean(keySP: String, value: Boolean) {
        init(j.context)
        spEditor.putBoolean(keySP, value)
        spEditor.commit()
    }

    fun getDoubleValue(key: String, defaultValue: Double) : Double {
        return java.lang.Double.longBitsToDouble(getLongValueFromPref(key, java.lang.Double.doubleToRawLongBits(defaultValue)))
    }

    fun getLongValueFromPref(key: String, defaultValue: Long): Long {
        init(j.context)
        return sp.getLong(key, defaultValue)
    }

    fun getIntValueFromPref(key: String, defaultValue: Int): Int {
        init(j.context)
        return sp.getInt(key, defaultValue)
    }

    fun getFloatValueFromPref(key: String, defaultValue: Float): Float {
        init(j.context)
        return sp.getFloat(key, defaultValue)
    }

    fun getSpString(key: String, defaultValue: String = ""): String? {
        init(j.context)
        return sp.getString(key, defaultValue)
    }

    fun getSpBoolean(key: String, default: Boolean): Boolean {
        init(j.context)
        return sp.getBoolean(key, default)
    }

    fun clearAll() {
        init(j.context)
        sp.edit().clear().apply()
    }
}