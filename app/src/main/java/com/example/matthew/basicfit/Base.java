package com.example.matthew.basicfit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Matthew on 21/11/2017.
 */

public class Base extends SQLiteOpenHelper {

    private static Base instance;

    private final static String DB_NAME = "basic_fat.db";
    private final static String DB_TABLE_ALIMENT = "table_aliment";
    private final static String DB_TABLE_MOI = "table_moi";
    private final static String DB_TABLE_MATIN = "table_matin";
    private final static String DB_TABLE_MIDI = "table_midi";
    private final static String DB_TABLE_SOIR = "table_soir";

    private final static int VERSION = 5;
    private final static int VERSION = 1;

    private final static String CREATE_TABLE_ALIMENT = "create table " + DB_TABLE_ALIMENT + "(" + "aliment string, " + "calories integer, _id integer primary key );";
    private final static String CREATE_TABLE_MOI = "create table " + DB_TABLE_MOI + "(" + "date datetime, " + "calories integer );";
    private final static String CREATE_TABLE_ALIMENT_MATIN = "create table " + DB_TABLE_MATIN + "(" + "date datetime, " + "aliment string);";
    private final static String CREATE_TABLE_ALIMENT_MIDI = "create table " + DB_TABLE_MIDI + "(" + "date datetime, " + "aliment string);";
    private final static String CREATE_TABLE_ALIMENT_SOIR = "create table " + DB_TABLE_SOIR + "(" + "date datetime, " + "aliment string);";
    private final static String CREATE_TABLE_MOI = "create table " + DB_TABLE_MOI + "(" + "date datetime, " + "calories integer );";
    private final static String CREATE_TABLE_ALIMENT_MATIN = "create table " + DB_TABLE_MATIN + "(" + "date datetime, " + "aliment string, "+ "calories integer);";
    private final static String CREATE_TABLE_ALIMENT_MIDI = "create table " + DB_TABLE_MIDI + "(" + "date datetime, " + "aliment string, "+ "calories integer);";
    private final static String CREATE_TABLE_ALIMENT_SOIR = "create table " + DB_TABLE_SOIR + "(" + "date datetime, " + "aliment string, "+ "calories integer);";


    public static Base getInstance(Context context) {
        if (instance == null) {
            instance = new Base(context);
        }
        return instance;
    }
    
    private Base(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ALIMENT);
        sqLiteDatabase.execSQL(CREATE_TABLE_MOI);
        sqLiteDatabase.execSQL(CREATE_TABLE_ALIMENT_MATIN);
        sqLiteDatabase.execSQL(CREATE_TABLE_ALIMENT_MIDI);
        sqLiteDatabase.execSQL(CREATE_TABLE_ALIMENT_SOIR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        if (new_version > old_version) {
            String e = "exists";
            db.execSQL("drop table if " + e + " " + DB_TABLE_ALIMENT);
            db.execSQL("drop table if " + e + " " + DB_TABLE_MOI);
            db.execSQL("drop table if " + e + " " + DB_TABLE_MATIN);
            db.execSQL("drop table if " + e + " " + DB_TABLE_MIDI);
            db.execSQL("drop table if " + e + " " + DB_TABLE_SOIR);
            onCreate(db);
        }
    }

}
