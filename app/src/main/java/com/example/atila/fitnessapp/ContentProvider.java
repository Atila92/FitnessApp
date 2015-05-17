package com.example.atila.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

/**
 * Created by SidonKK on 17-05-2015.
 */
public class ContentProvider extends android.content.ContentProvider {

    private DatabaseHandler dbHelper;
    //fields for provider
    public static final String AUTH = "com.example.atila.fitnessapp.ContentProvider";
    public static final  Uri CONTENT_URI = Uri.parse("content://"+AUTH + "/"+UserData.Info.DATABASE_TABLE);

    public static final int USER = 1;
    public static final int TIME = 2;

    private SQLiteDatabase database;


    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTH, UserData.Info.DATABASE_TABLE, USER);
        sURIMatcher.addURI(AUTH, UserData.Info.DATABASE_TABLE + "/#",
                TIME);
    }
    @Override
    public boolean onCreate() {
       dbHelper = new DatabaseHandler(getContext(),null,null,1);
        return false;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserData.Info.DATABASE_TABLE);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case TIME:
                queryBuilder.appendWhere(UserData.Info.TIME + "="
                        + uri.getLastPathSegment());
                break;
            case USER:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case USER:
                id = sqlDB.insert(UserData.Info.DATABASE_TABLE,
                        null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(UserData.Info.DATABASE_TABLE + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case USER:
                rowsDeleted = sqlDB.delete(UserData.Info.DATABASE_TABLE,
                        selection,
                        selectionArgs);
                break;

            case TIME:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(UserData.Info.DATABASE_TABLE,
                            UserData.Info.KEY_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(UserData.Info.DATABASE_TABLE,
                            UserData.Info.KEY_ID  + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case USER:
                rowsUpdated = sqlDB.update(UserData.Info.DATABASE_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TIME:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(UserData.Info.DATABASE_TABLE,
                                    values,
                                    UserData.Info.KEY_ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(UserData.Info.DATABASE_TABLE,
                                    values,
                                    UserData.Info.KEY_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }
}
