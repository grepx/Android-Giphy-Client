package gregpearce.gifhub.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import gregpearce.gifhub.app.DB_FILENAME
import gregpearce.gifhub.app.DB_VERSION
import org.jetbrains.anko.db.*

class DbOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, DB_FILENAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(GifTable.NAME, true,
                GifTable.COLUMN_ID to INTEGER + PRIMARY_KEY,
                GifTable.COLUMN_URL to TEXT,
                GifTable.COLUMN_PATH to TEXT,
                GifTable.COLUMN_THUMBNAIL_URL to TEXT,
                GifTable.COLUMN_THUMBNAIL_PATH to TEXT,
                GifTable.COLUMN_FAVOURITED to INTEGER
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}