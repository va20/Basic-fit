package com.example.matthew.basicfit;

import android.content.ContentValues;
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

    private final static String DB_UTILISATEUR = "users";
    private final static String ID_USER = "id_user";
    private final static String NOM_UTILISATEUR = "nom";
    private final static String CALORIES_JOURS = "caloriesParJour";


    private final static int VERSION = 3;

    private final static String CREATE_TABLE = "create table " + DB_TABLE_ALIMENT + "(" + "aliment string, " + "calories integer, _id integer primary key );";

    private final static String CREATE_TABLE_USERS = ""+
            "create table "+ DB_UTILISATEUR + "( " +
            ID_USER+" integer primary_key not null auto_increment, "+
            NOM_UTILISATEUR+" string not null," +
            CALORIES_JOURS+" integer not null);";

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

        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        if (new_version > old_version) {
            String e = "exists";
            db.execSQL("drop table if " + e + " " + DB_TABLE_ALIMENT);
            onCreate(db);
        }
    }

}
