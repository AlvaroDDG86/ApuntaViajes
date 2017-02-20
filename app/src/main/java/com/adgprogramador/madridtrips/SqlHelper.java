package com.adgprogramador.madridtrips;

/**
 * Created by Alvaro on 21/01/2017.
 */

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelper  extends  SQLiteOpenHelper{
    private final String sqlCreate = "CREATE TABLE Viajes (id integer primary key, fecha TEXT, medio TEXT, importe TEXT, kms TEXT, trayecto TEXT)";

    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
