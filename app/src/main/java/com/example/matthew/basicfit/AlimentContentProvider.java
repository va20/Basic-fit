package com.example.matthew.basicfit;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Matthew on 22/11/2017.
 */

public class AlimentContentProvider extends ContentProvider {

    private static final String LOG = "AlimentContentProvider";

    private static String authority = "com.example.matthew.basicfit.AlimentContentProvider";

    private Base helper;

    private final static int TABLE_ALIMENT = 1;
    private final static int ALIMENT = 2;
    private final static int CALORIES = 3;
    private final static int ALIMENT_ID = 4;
    private final static int MATIN = 5;
    private final static int MATIN_ALIMENT = 6;
    private final static int MATIN_DATE = 7;
    private final static int MATIN_CALORIES = 8;
    private final static int MIDI = 9;
    private final static int MIDI_ALIMENT = 10;
    private final static int MIDI_DATE = 11;
    private final static int MIDI_CALORIES = 12;
    private final static int SOIR = 13;
    private final static int SOIR_ALIMENT = 14;
    private final static int SOIR_DATE = 15;
    private final static int SOIR_CALORIES = 16;
    private final static int MOI_CALORIE = 17;


    final static String STRING_ALIMENT = "aliment";
    final static String STRING_MATIN = "matin";
    final static String STRING_MIDI = "midi";
    final static String STRING_SOIR = "soir";
    final static String STRING_CALORIES = "calories";
    final static String STRING_ALIMENT_ID = "_id";

    final static String STRING_TABLE_ALIMENT = "table_aliment";
    final static String STRING_TABLE_MATIN = "table_matin";
    final static String STRING_TABLE_MIDI = "table_midi";
    final static String STRING_TABLE_SOIR = "table_soir";
    final static String STRING_TABLE_MOI = "table_soir";

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(authority, "table_aliment", TABLE_ALIMENT);
        matcher.addURI(authority, "aliment", ALIMENT);
        matcher.addURI(authority, "aliment/calories", CALORIES);
        matcher.addURI(authority, "aliment/#", ALIMENT_ID);
        matcher.addURI(authority, "matin", MATIN);
        matcher.addURI(authority, "matin/aliment", MATIN_ALIMENT);
        matcher.addURI(authority, "matin/date", MATIN_DATE);
        matcher.addURI(authority, "matin/calories", MATIN_CALORIES);
        matcher.addURI(authority, "midi", MIDI);
        matcher.addURI(authority, "midi/aliment", MIDI_ALIMENT);
        matcher.addURI(authority, "midi/date", MIDI_DATE);
        matcher.addURI(authority, "midi/calories", MIDI_CALORIES);
        matcher.addURI(authority,"soir",SOIR);
        matcher.addURI(authority,"soir/aliment",SOIR_ALIMENT);
        matcher.addURI(authority,"soir/date",SOIR_DATE);
        matcher.addURI(authority,"soir/calories",SOIR_CALORIES);
        matcher.addURI(authority, "moi/calorie", MOI_CALORIE);
    }

    public AlimentContentProvider() {

    }

    @Override
    public boolean onCreate() {
        helper = Base.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int code = matcher.match(uri);
        Log.e("URI: ", uri.toString());
        Cursor cursor = null;

        switch (code) {
            case ALIMENT:
                cursor = db.query(STRING_TABLE_ALIMENT,strings,s,strings1,null,null,s1 );
                break;

            case MATIN:
                cursor = db.query(STRING_TABLE_MATIN, strings, s, strings1, null, null, s1);
                break;

            case MIDI:
                cursor = db.query(STRING_TABLE_MIDI, strings, s, strings1, null, null, s1);
                break;

            case SOIR:
                cursor = db.query(STRING_TABLE_SOIR, strings, s, strings1, null, null, s1);
                break;

            default:
                Log.d("Uri provider =", uri.toString());
                throw new UnsupportedOperationException("this query is not yet implemented  " +
                        uri.toString());
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri,ContentValues contentValues) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        // Log.d(LOG, "insert uri=" + uri.toString());
        long id;
        String path;
        switch (code) {
            case ALIMENT:
                id = db.insert(STRING_TABLE_ALIMENT, null, contentValues);
                path = STRING_ALIMENT;
                break;

            case MATIN:
                id = db.insert(STRING_TABLE_MATIN, null, contentValues);
                path = STRING_MATIN;
                break;

            case MIDI:
                id = db.insert(STRING_TABLE_MIDI, null, contentValues);
                path = STRING_MIDI;
                break;

            case SOIR:
                id = db.insert(STRING_TABLE_SOIR, null, contentValues);
                path = STRING_SOIR;
                break;

            default:
                throw new UnsupportedOperationException("this insert not yet implemented");
        }
        Log.d("DEBUG URL:", "Construction Uri Builder");
        Uri.Builder builder = (new Uri.Builder())
                .authority(authority)
                .appendPath(path);
        return ContentUris.appendId(builder, id).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        int i;
        long id;
        switch (code) {
            case ALIMENT:
                i = db.delete(STRING_TABLE_ALIMENT, s, strings);
                break;
            case MATIN:
                i = db.delete(STRING_TABLE_MATIN, s, strings);
            case MIDI:
                i = db.delete(STRING_TABLE_MIDI,  s, strings);
            case SOIR:
                i = db.delete(STRING_TABLE_SOIR,  s, strings);
            default:
                throw new UnsupportedOperationException("Uri non reconnu");
        }
        return i;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        int i;
        long id;
        switch (code) {
            case CALORIES:
                i = db.update(STRING_TABLE_MOI, contentValues, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Pas encore implementé");
        }
        return i;
    }
}
