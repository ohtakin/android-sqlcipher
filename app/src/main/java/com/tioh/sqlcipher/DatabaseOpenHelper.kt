package com.tioh.sqlcipher

import android.content.ContentValues
import android.content.Context
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper

class DatabaseOpenHelper(
    context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        SQLiteDatabase.loadLibs(context)
    }

    companion object : SingletonHolder<DatabaseOpenHelper, Context>(::DatabaseOpenHelper) {

        const val DATABASE_CIPHER = "CIPHER"
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "SQLCipherDatabase"
        private const val TABLE = "SQLCipherTable"
        private const val KEY_ID = "uid"
        private const val FIRST_NAME = "firstName"
        private const val LAST_NAME = "lastName"

        private const val CREATE_TABLE = ("CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + FIRST_NAME + " TEXT,"
                + LAST_NAME + " TEXT" + ")")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun clearDbAndRecreate() {
        clearDb()
        onCreate(getWritableDatabase(DATABASE_CIPHER))
    }

    fun clearDb() {
        getWritableDatabase(DATABASE_CIPHER).execSQL("DROP TABLE IF EXISTS $TABLE")
    }

    fun getAllUser(): List<User> {
        val cursor = getWritableDatabase(DATABASE_CIPHER).query(
            TABLE,
            arrayOf(KEY_ID, FIRST_NAME, LAST_NAME),
            null,
            null,
            null,
            null,
            KEY_ID
        )
        val keyId = cursor.getColumnIndexOrThrow(KEY_ID)
        val firstName = cursor.getColumnIndexOrThrow(FIRST_NAME)
        val lastName = cursor.getColumnIndexOrThrow(LAST_NAME)
        val users = ArrayList<User>()
        while (cursor != null && cursor.moveToNext()) {
            val user = User(
                cursor.getInt(keyId), cursor.getString(firstName), cursor.getString(
                    lastName
                )
            )
            users.add(user)
        }

        return users
    }

    fun insertAll(vararg objects: User) {
        getWritableDatabase(DATABASE_CIPHER).beginTransaction()
        for (`object` in objects) {
            val table: User = `object`
            getWritableDatabase(DATABASE_CIPHER).insert(
                TABLE, null, getContentValues(`object`)
            )
        }
        getWritableDatabase(DATABASE_CIPHER).setTransactionSuccessful()
        getWritableDatabase(DATABASE_CIPHER).endTransaction()
    }

    private fun getContentValues(`object`: User): ContentValues? {
        val table: User = `object`
        val contentValues = ContentValues()
        // contentValues.put("_id", table.get_id());
        contentValues.put(FIRST_NAME, table.firstName)
        contentValues.put(LAST_NAME, table.lastName)
        return contentValues
    }
}