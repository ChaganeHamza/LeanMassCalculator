package com.example.leanmass.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "leanmass.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE users (
                id            INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name     TEXT,
                email         TEXT UNIQUE,
                password_hash TEXT,
                created_at    TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE records (
                id              INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id         INTEGER REFERENCES users(id),
                date            TEXT,
                gender          TEXT,
                weight_kg       REAL,
                height_cm       REAL,
                lbm             REAL,
                is_satisfactory INTEGER
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS records")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // ── Users ──────────────────────────────────────────────
    fun insertUser(name: String, email: String, hash: String): Long {
        val cv = ContentValues().apply {
            put("full_name", name)
            put("email", email)
            put("password_hash", hash)
            put("created_at", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
        }
        return writableDatabase.insert("users", null, cv)
    }

    fun getUserByEmail(email: String): UserRecord? {
        val c = readableDatabase.query(
            "users", null, "email=?", arrayOf(email), null, null, null
        )
        return c.use {
            if (!it.moveToFirst()) null
            else UserRecord(
                id           = it.getLong(it.getColumnIndexOrThrow("id")),
                fullName     = it.getString(it.getColumnIndexOrThrow("full_name")),
                email        = it.getString(it.getColumnIndexOrThrow("email")),
                passwordHash = it.getString(it.getColumnIndexOrThrow("password_hash"))
            )
        }
    }

    // ── Records ────────────────────────────────────────────
    fun insertRecord(r: LbmRecord): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val cv = ContentValues().apply {
            put("user_id", r.userId)
            put("date", sdf.format(Date()))
            put("gender", r.gender)
            put("weight_kg", r.weight)
            put("height_cm", r.height)
            put("lbm", r.lbm)
            put("is_satisfactory", if (r.isSatisfactory) 1 else 0)
        }
        return writableDatabase.insert("records", null, cv)
    }

    fun getRecordsByUser(userId: Long): List<LbmRecord> {
        val list = mutableListOf<LbmRecord>()
        val c = readableDatabase.query(
            "records", null, "user_id=?", arrayOf(userId.toString()),
            null, null, "id DESC"
        )
        c.use { while (it.moveToNext()) list.add(cursorToRecord(it)) }
        return list
    }

    fun deleteRecord(id: Long) {
        writableDatabase.delete("records", "id=?", arrayOf(id.toString()))
    }

    private fun cursorToRecord(c: Cursor) = LbmRecord(
        id             = c.getLong(c.getColumnIndexOrThrow("id")),
        userId         = c.getLong(c.getColumnIndexOrThrow("user_id")),
        date           = c.getString(c.getColumnIndexOrThrow("date")),
        gender         = c.getString(c.getColumnIndexOrThrow("gender")),
        weight         = c.getFloat(c.getColumnIndexOrThrow("weight_kg")),
        height         = c.getFloat(c.getColumnIndexOrThrow("height_cm")),
        lbm            = c.getFloat(c.getColumnIndexOrThrow("lbm")),
        isSatisfactory = c.getInt(c.getColumnIndexOrThrow("is_satisfactory")) == 1
    )
}