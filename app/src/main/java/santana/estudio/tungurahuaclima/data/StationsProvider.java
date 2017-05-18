package santana.estudio.tungurahuaclima.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by dexter on 16/05/2017.
 */

public class StationsProvider extends ContentProvider {
    public static final int CODE_STATIONS = 100;
    public static final int CODE_PARAMS = 200;
    public static final int CODE_STATIONS_WITH_ID = 101;
    public static final int CODE_PARAMS_BY_STATION = 201;
    public static final int CODE_PARAM_BY_ID = 202;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RrnnContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RrnnContract.PATH_STATIONS, CODE_STATIONS);
        matcher.addURI(authority, RrnnContract.PATH_STATIONS + "/#", CODE_STATIONS_WITH_ID);

        matcher.addURI(authority, RrnnContract.PATH_PARAMS, CODE_PARAMS);
        matcher.addURI(authority, RrnnContract.PATH_PARAMS + "/*", CODE_PARAMS_BY_STATION);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_STATIONS:
                cursor = mOpenHelper.getReadableDatabase().query(
                        RrnnContract.StationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_STATIONS_WITH_ID:
                String stationId = uri.getLastPathSegment();
                String [] selectArgs = new String[]{stationId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        RrnnContract.StationEntry.TABLE_NAME,
                        projection,
                        RrnnContract.StationEntry.COLUMN_STATION_ID + " =  ?",
                        selectArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_PARAMS_BY_STATION:
                String statId = uri.getLastPathSegment();
                String[] selectArg = new String[]{statId};
                cursor = mOpenHelper.getReadableDatabase().query(
                        RrnnContract.ParamEntry.TABLE_NAME,
                        projection,
                        RrnnContract.ParamEntry.COLUMN_STATION_ID + " =  ?",
                        selectArg,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_PARAMS:
                cursor = mOpenHelper.getReadableDatabase().query(
                        RrnnContract.ParamEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("URL desconocida: "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsInserted;
        switch (sUriMatcher.match(uri)) {

            case CODE_STATIONS:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RrnnContract.StationEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            case CODE_PARAMS:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RrnnContract.ParamEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int nRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_STATIONS:
                nRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        RrnnContract.StationEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (nRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return nRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
