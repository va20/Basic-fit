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

    private final static String STRING_ALIMENT = "aliment";
    private final static String STRING_CALORIES = "calorie";
    private final static String STRING_TABLE_ALIMENT = "table_aliment";



    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(authority, "table_aliment", TABLE_ALIMENT);
        matcher.addURI(authority, "aliment", ALIMENT);
        matcher.addURI(authority, "aliment/calories", CALORIES);
        matcher.addURI(authority, "aliment/#", ALIMENT_ID);


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
        Log.d(LOG, "insert uri=" + uri.toString());
        long id;
        String path;
        Log.d("DEBUG:", Integer.toString(code));
        switch (code) {
            case ALIMENT:
                id = db.insert(STRING_TABLE_ALIMENT, null, contentValues);
                path = STRING_ALIMENT;
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
                id = ContentUris.parseId(uri);
                i = db.delete(STRING_TABLE_ALIMENT, "_id=" + id, null);
                break;
            default:
                throw new UnsupportedOperationException("Uri non reconnu");
        }
        return i;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
