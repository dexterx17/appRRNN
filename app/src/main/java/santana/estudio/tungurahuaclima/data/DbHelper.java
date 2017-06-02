package santana.estudio.tungurahuaclima.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dexter on 16/05/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "clima.db";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_STATIONS_TABLE =
                "CREATE TABLE " + RrnnContract.StationEntry.TABLE_NAME + " (" +
                        RrnnContract.StationEntry._ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RrnnContract.StationEntry.COLUMN_STATION_ID   + " STRING NOT NULL, " +
                        RrnnContract.StationEntry.COLUMN_NAME + " STRING NOT NULL, "+
                        RrnnContract.StationEntry.COLUMN_TYPE + " STRING NOT NULL, "+
                        RrnnContract.StationEntry.COLUMN_IMAGE + " STRING, "+
                        RrnnContract.StationEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "+
                        RrnnContract.StationEntry.COLUMN_HEIGHT + " NUMERIC NOT NULL DEFAULT 0, " +
                        RrnnContract.StationEntry.COLUMN_LATITUD +" REAL NOT NULL, "+
                        RrnnContract.StationEntry.COLUMN_LONGITUD + " REAL NOT NULL, "+
                        RrnnContract.StationEntry.COLUMN_MIN + " STRING, "+
                        RrnnContract.StationEntry.COLUMN_MAX + " STRING, "+
                        RrnnContract.StationEntry.COLUMN_CANTON + " STRING NOT NULL, "+
                        RrnnContract.StationEntry.COLUMN_PARROQUIA + " STRING NOT NULL, "+
                        RrnnContract.StationEntry.COLUMN_ADDRESS + " STRING) ";
        db.execSQL(SQL_CREATE_STATIONS_TABLE);

        final String SQL_CREATE_PARAMS_TABLE =
                "CREATE TABLE " + RrnnContract.ParamEntry.TABLE_NAME + " (" +
                        RrnnContract.ParamEntry._ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RrnnContract.ParamEntry.COLUMN_STATION_ID   + " STRING NOT NULL, " +
                        RrnnContract.ParamEntry.COLUMN_PARAM_ID   + " STRING NOT NULL, " +
                        RrnnContract.ParamEntry.COLUMN_KEY + " STRING NOT NULL, "+
                        RrnnContract.ParamEntry.COLUMN_NAME + " STRING NOT NULL, "+
                        RrnnContract.ParamEntry.COLUMN_UNITY + " STRING NOT NULL, "+
                        RrnnContract.ParamEntry.COLUMN_AVERAGE + " STRING NOT NULL, "+
                        " UNIQUE (" + RrnnContract.ParamEntry.COLUMN_KEY +","+RrnnContract.ParamEntry.COLUMN_STATION_ID +") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_PARAMS_TABLE);

        final String SQL_CREATE_EMBALSES_TABLE =
                "CREATE TABLE " + RrnnContract.EmbalseEntry.TABLE_NAME + " (" +
                        RrnnContract.EmbalseEntry._ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RrnnContract.EmbalseEntry.COLUMN_EMBALSE_ID   + " STRING NOT NULL, " +
                        RrnnContract.EmbalseEntry.COLUMN_NAME + " STRING NOT NULL, "+
                        RrnnContract.EmbalseEntry.COLUMN_IMAGE + " STRING, "+
                        RrnnContract.EmbalseEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "+
                        RrnnContract.EmbalseEntry.COLUMN_HEIGHT + " NUMERIC NOT NULL DEFAULT 0, " +
                        RrnnContract.EmbalseEntry.COLUMN_LATITUD +" REAL NOT NULL, "+
                        RrnnContract.EmbalseEntry.COLUMN_LONGITUD + " REAL NOT NULL, "+
                        RrnnContract.EmbalseEntry.COLUMN_CANTON + " STRING NOT NULL, "+
                        RrnnContract.EmbalseEntry.COLUMN_PARROQUIA + " STRING NOT NULL, "+
                        RrnnContract.EmbalseEntry.COLUMN_ADDRESS + " STRING) ";
        db.execSQL(SQL_CREATE_EMBALSES_TABLE);

        final String SQL_CREATE_WEATHER_HOURLY_TABLE =
                "CREATE TABLE " + RrnnContract.WeatherHourlyEntry.TABLE_NAME + " (" +
                        RrnnContract.WeatherHourlyEntry._ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RrnnContract.WeatherHourlyEntry.COLUMN_DATE       + " INTEGER, "      +
                        RrnnContract.WeatherHourlyEntry.COLUMN_WEATHER_ID + " INTEGER, "      +
                        RrnnContract.WeatherHourlyEntry.COLUMN_STATION_ID + " STRING, "      +

                        RrnnContract.WeatherHourlyEntry.COLUMN_TEMP   + " REAL, "             +
                        RrnnContract.WeatherHourlyEntry.COLUMN_TEMP_N   + " NUMERIC DEFAULT 0, "        +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MIN_TEMP   + " REAL, "         +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MAX_TEMP   + " REAL, "         +

                        RrnnContract.WeatherHourlyEntry.COLUMN_HUMIDITY   + " REAL, "             +
                        RrnnContract.WeatherHourlyEntry.COLUMN_HUMIDITY_N   + " NUMERIC DEFAULT 0, "        +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MIN_HUMIDITY   + " REAL, "         +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MAX_HUMIDITY   + " REAL, "         +

                        RrnnContract.WeatherHourlyEntry.COLUMN_RAIN   + " REAL, "             +
                        RrnnContract.WeatherHourlyEntry.COLUMN_RAIN_N   + " NUMERIC DEFAULT 0, "        +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MIN_RAIN   + " REAL, "         +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MAX_RAIN   + " REAL, "         +

                        RrnnContract.WeatherHourlyEntry.COLUMN_WIND_SPEED   + " REAL, "             +
                        RrnnContract.WeatherHourlyEntry.COLUMN_WIND_SPEED_N   + " NUMERIC DEFAULT 0, "        +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MIN_WIND_SPEED   + " REAL, "         +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MAX_WIND_SPEED   + " REAL, "         +

                        RrnnContract.WeatherHourlyEntry.COLUMN_WIND_DIR   + " REAL, "             +
                        RrnnContract.WeatherHourlyEntry.COLUMN_WIND_DIR_N   + " NUMERIC DEFAULT 0, "        +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MIN_WIND_DIR   + " REAL, "         +
                        RrnnContract.WeatherHourlyEntry.COLUMN_MAX_WIND_DIR   + " REAL, "         +
                        " UNIQUE (" + RrnnContract.WeatherHourlyEntry.COLUMN_DATE +","+RrnnContract.WeatherHourlyEntry.COLUMN_STATION_ID +") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_WEATHER_HOURLY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+RrnnContract.StationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE "+RrnnContract.ParamEntry.TABLE_NAME);
        db.execSQL("DROP TABLE "+RrnnContract.EmbalseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE "+RrnnContract.WeatherHourlyEntry.TABLE_NAME);
        onCreate(db);
    }
}
