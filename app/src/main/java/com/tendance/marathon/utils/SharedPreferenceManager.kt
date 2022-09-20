package com.tendance.marathon.utils

import android.content.Context
import com.tendance.marathon.models.UserResponse

class SharedPreferenceManager(private val mCtx: Context) {

    fun saveUser(user: UserResponse): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(AGENTID, user.agentId!!)
        editor.putString(CODE, user.code)
        editor.putString(NAME, user.name)
        editor.putString(PHONENUMBER, user.phoneNumber)
        editor.putString(IMAGE, user.image)
        editor.putString(PASSWORD, user.password)
        editor.putString(TOKEN, user.token)
        editor.putBoolean(ISACTIVATED, user.isActivated!!)
        editor.putBoolean(HAVEASSIGNEDVALUE, user.haveAssignedDevice!!)
        editor.apply()
        return true
    }

    fun getUserResponse(): UserResponse {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return UserResponse(
            sharedPreferences.getInt(AGENTID,0),
            null,
            null,
            null,
            sharedPreferences.getString(CODE,null),
            sharedPreferences.getBoolean(HAVEASSIGNEDVALUE,false),
            sharedPreferences.getString(IMAGE,null),
            sharedPreferences.getBoolean(ISACTIVATED,false),
            sharedPreferences.getString(NAME,null),
            sharedPreferences.getString(PASSWORD,null),
            sharedPreferences.getString(PHONENUMBER,null),
            sharedPreferences.getString(TOKEN,null))
    }


    companion object {
        private var mInstance: SharedPreferenceManager? = null
        private const val SHARED_PREF_NAME = "tendanceM"

        private const val AGENTID = "agentid"
        private const val NAME = "name"
        private const val CODE = "code"
        private const val PHONENUMBER = "telephone"
        private const val IMAGE = "image"
        private const val PASSWORD = "pass"
        private const val TOKEN = "token"
        private const val ISACTIVATED = "isactivated"
        private const val HAVEASSIGNEDVALUE = "haveassignedvalue"

        @Synchronized
        fun getInstance(context: Context): SharedPreferenceManager? {
            if (mInstance == null) {
                mInstance =
                    SharedPreferenceManager(context)
            }
            return mInstance
        }
    }
}