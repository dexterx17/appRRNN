package santana.estudio.tungurahuaclima.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dexter on 16/05/2017.
 */

public class RrnnContract {
    public static final String CONTENT_AUTHORITY = "santana.estudio.tungurahuaclima";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for ClimaTungurahua.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STATIONS = "stations";
    public static final String PATH_PARAMS = "params";

    public static final String PATH_WEATHER = "weather";

    public static class StationEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STATIONS)
                .build();

        public static final String TABLE_NAME = "stations";

        public static final String COLUMN_STATION_ID = "station_id";
        public static final String COLUMN_STATION_CODE = "station_code";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CANTON = "canton";
        public static final String COLUMN_PARROQUIA = "parroquia";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_LATITUD = "lat";
        public static final String COLUMN_LONGITUD = "lng";

    }

    public static class ParamEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PARAMS)
                .build();

        public static final String TABLE_NAME = "params";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KEY = "parametro";
        public static final String COLUMN_UNITY = "unity";
        public static final String COLUMN_AVERAGE = "average";
        public static final String COLUMN_MIN = "min";
        public static final String COLUMN_MAX = "max";
        public static final String COLUMN_PARAM_ID = "parametro_id";
        public static final String COLUMN_STATION_ID = "station_id";

    }
    public static class WeatherHourlyEntry implements BaseColumns {
        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        public static final String TABLE_NAME = "weather_hourly";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_STATION_ID = "station_id";

        /* Temperatura */
        public static final String COLUMN_TEMP = "temp";
        public static final String COLUMN_TEMP_N = "temp_n";
        public static final String COLUMN_MIN_TEMP = "temp_min";
        public static final String COLUMN_MAX_TEMP = "temp_max";

        /* Humidity is stored as a float representing percentage */
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_HUMIDITY_N = "humidity_n";
        public static final String COLUMN_MIN_HUMIDITY = "humidity_min";
        public static final String COLUMN_MAX_HUMIDITY = "humidity_max";

        /* Pressure is stored as a float representing percentage */
        public static final String COLUMN_RAIN = "rain";
        public static final String COLUMN_RAIN_N = "rain_n";
        public static final String COLUMN_MIN_RAIN = "rain_min";
        public static final String COLUMN_MAX_RAIN = "rain_max";

        /* Wind speed is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        public static final String COLUMN_WIND_SPEED_N = "wind_speed_n";
        public static final String COLUMN_MIN_WIND_SPEED = "wind_speed_min";
        public static final String COLUMN_MAX_WIND_SPEED = "wind_speed_max";

        /* Wind direction is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_DIR = "wind_dir";
        public static final String COLUMN_WIND_DIR_N = "wind_dir_n";
        public static final String COLUMN_MIN_WIND_DIR = "wind_dir_min";
        public static final String COLUMN_MAX_WIND_DIR = "wind_dir_max";

    }

    public static class WeatherDailyEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        public static final String TABLE_NAME = "weather_daily";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_STATION_ID = "station_id";

        /* Temperatura */
        public static final String COLUMN_TEMP = "temp";
        public static final String COLUMN_TEMP_N = "temp_n";
        public static final String COLUMN_MIN_TEMP = "temp_min";
        public static final String COLUMN_MAX_TEMP = "temp_max";

        /* Humidity is stored as a float representing percentage */
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_HUMIDITY_N = "humidity_n";
        public static final String COLUMN_MIN_HUMIDITY = "humidity_min";
        public static final String COLUMN_MAX_HUMIDITY = "humidity_max";

        /* Pressure is stored as a float representing percentage */
        public static final String COLUMN_RAIN = "rain";
        public static final String COLUMN_RAIN_N = "rain_n";
        public static final String COLUMN_MIN_RAIN = "rain_min";
        public static final String COLUMN_MAX_RAIN = "rain_max";

        /* Wind speed is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        public static final String COLUMN_WIND_SPEED_N = "wind_speed_n";
        public static final String COLUMN_MIN_WIND_SPEED = "wind_speed_min";
        public static final String COLUMN_MAX_WIND_SPEED = "wind_speed_max";

        /* Wind direction is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_DIR = "wind_dir";
        public static final String COLUMN_WIND_DIR_N = "wind_dir_n";
        public static final String COLUMN_MIN_WIND_DIR = "wind_dir_min";
        public static final String COLUMN_MAX_WIND_DIR = "wind_dir_max";
    }

    public static class WeatherMonthlyEntry implements BaseColumns {
        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        public static final String TABLE_NAME = "weather_monthly";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_STATION_ID = "station_id";

        /* Temperatura */
        public static final String COLUMN_TEMP = "temp";
        public static final String COLUMN_TEMP_N = "temp_n";
        public static final String COLUMN_MIN_TEMP = "temp_min";
        public static final String COLUMN_MAX_TEMP = "temp_max";

        /* Humidity is stored as a float representing percentage */
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_HUMIDITY_N = "humidity_n";
        public static final String COLUMN_MIN_HUMIDITY = "humidity_min";
        public static final String COLUMN_MAX_HUMIDITY = "humidity_max";

        /* Pressure is stored as a float representing percentage */
        public static final String COLUMN_RAIN = "rain";
        public static final String COLUMN_RAIN_N = "rain_n";
        public static final String COLUMN_MIN_RAIN = "rain_min";
        public static final String COLUMN_MAX_RAIN = "rain_max";

        /* Wind speed is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        public static final String COLUMN_WIND_SPEED_N = "wind_speed_n";
        public static final String COLUMN_MIN_WIND_SPEED = "wind_speed_min";
        public static final String COLUMN_MAX_WIND_SPEED = "wind_speed_max";

        /* Wind direction is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_DIR = "wind_dir";
        public static final String COLUMN_WIND_DIR_N = "wind_dir_n";
        public static final String COLUMN_MIN_WIND_DIR = "wind_dir_min";
        public static final String COLUMN_MAX_WIND_DIR = "wind_dir_max";
    }

}
