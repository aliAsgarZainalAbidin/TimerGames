package com.selayar.history.Util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.net.URL
import java.util.*

class Session(internal var _context: Context) {
    // Shared Preferences
    internal var pref: SharedPreferences

    // Editor for Shared preferences
    internal var editor: SharedPreferences.Editor

    // Shared pref mode
    internal var PRIVATE_MODE = 0

    val firebase_id: String?
        get() = pref.getString(KEY_FIREBASE_ID, null)

    val token: String?
        get() = pref.getString(KEY_TOKEN, null)

    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)

    val isGmail: Boolean
        get() = pref.getBoolean(IS_GMAIL, false)

    val badgeNumber: Int?
        get() = pref.getInt(BADGE_NUMBER, 0)

//    val filterCabang: Int
//        get() = pref.getInt(ID_CABANG, 54)

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    /**
     * Create login session
     */
    fun createLoginSession(token: String, is_gmail: Boolean) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_TOKEN, token)
        editor.putBoolean(IS_GMAIL, is_gmail)
//        editor.putInt(ID_CABANG, cabang)

        // commit changes
        editor.commit()
    }

    fun setFirebaseId(id: String) {
        editor.putString(KEY_FIREBASE_ID, id)
        editor.commit()
    }

    fun setBadgeNumber(number: Int) {
        editor.putInt(BADGE_NUMBER, number)
            .commit()
    }

//    fun setGmail(is_gmail: Boolean) {
//        editor.putBoolean(IS_GMAIL, is_gmail)
//        editor.commit()
//    }

    //buka fungsi ini, kalau sudah ada act setting atur disini
//    fun checkLogin() {
//        // Check login status
//        if (!this.isLoggedIn) {
//            // exampeModel is not logged in redirect him to Login ActivityLog
//            val i = Intent(_context, ActSignIn::class.java)
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//            // Add new Flag to start new ActivityLog
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//            // Staring Login ActivityLog
//            _context.startActivity(i)
//        }
//    }

    fun logout() {
        // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()

//        // After logout redirect exampeModel to Loing ActivityLog
//        val i = Intent(_context, ActSignIn::class.java)
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//
//        // Staring Login ActivityLog
//        _context.startActivity(i)
    }

//    fun setFilterBranch(idBranch : Int){
//        editor.putInt(ID_CABANG,idBranch)
//        editor.commit()
//    }

    fun setMetodePembayaran(nama: String?, nomor: String?, kode: String?, url: String?) {
        editor.putString(CONST_NAMA_BANK, nama)
        editor.putString(CONST_NOMOR_BANK, nomor)
        editor.putString(CONST_KODE_BANK, kode)
        editor.putString(CONST_URL_BANK, url)
        editor.commit()
    }

    companion object {

        // Sharedpref file name
        private val PREF_NAME = "DapurUserPref"

        // All Shared Preferences Keys
        val IS_LOGIN = "IsLoggedIn"
        val IS_GMAIL = "isgmail"
        val KEY_FIREBASE_ID = "firebase_id"

        //        val ID_CABANG           = "id_cabang"
        val CONST_NAMA_BANK = "nama_bank"
        val CONST_NOMOR_BANK = "nomor_bank"
        val CONST_KODE_BANK = "kode_bank"
        val CONST_URL_BANK = "url_bank"
        val BADGE_NUMBER = "badge_number"

        //token utk authorization ke api
        val KEY_TOKEN = "token"

        private var instance: Session? = null

        fun with(context: Context): Session {

            if (instance == null) {
                instance = Session(context)
            }
            return instance as Session
        }
    }

}