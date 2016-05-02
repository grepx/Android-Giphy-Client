package gregpearce.gifhub.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import gregpearce.gifhub.app.DB_FILENAME
import gregpearce.gifhub.app.DB_VERSION

class DbOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_FILENAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}