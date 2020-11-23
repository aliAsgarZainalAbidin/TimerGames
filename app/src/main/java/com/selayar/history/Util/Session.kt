package com.selayar.history.Util

import android.content.Context
import android.content.SharedPreferences

class Session(internal var _context: Context) {
    // Shared Preferences
    internal var pref: SharedPreferences

    // Editor for Shared preferences
    internal var editor: SharedPreferences.Editor

    // Shared pref mode
    internal var PRIVATE_MODE = 0

    //    val filterCabang: Int
//        get() = pref.getInt(ID_CABANG, 54)

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
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

    //    fun setFilterBranch(idBranch : Int){
//        editor.putInt(ID_CABANG,idBranch)
//        editor.commit()
//    }

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

    }

}