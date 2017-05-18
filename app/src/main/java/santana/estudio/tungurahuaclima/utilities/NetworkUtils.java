package santana.estudio.tungurahuaclima.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by dexter on 16/05/2017.
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    //private static final String URL_REST_RRNN = "http://rrnn.tungurahua.gob.ec/services/Ws_red";
    private static final String URL_REST_RRNN = "http://192.168.1.8/rrnn/services/Ws_red";

    // Prefijos para datos
    public static final String URL_PREFIX_STATIONS = "stations";
    public static final String URL_PREFIX_PARAMS = "params";

    private static final String URL_PREFIX_HOURLY = "hourly";
    private static final String URL_PREFIX_DAILY = "daily";
    private static final String URL_PREFIX_MONTHLY = "monthly";
    private static final String format = "json";

    final static String FORMAT_PARAM = "format";
    final static String ID_PARAM = "id";

    public static URL buildListUrl(String entity){
        Uri uri = Uri.parse(URL_REST_RRNN).buildUpon()
                .appendPath(entity)
                .appendQueryParameter(FORMAT_PARAM,format)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"REST list_url: "+url);
        return url;
    }

    public static URL buildParamsStationUrl(String stationID){
        Uri uri = Uri.parse(URL_REST_RRNN).buildUpon()
                .appendPath(URL_PREFIX_STATIONS)
                .appendPath(ID_PARAM)
                .appendPath(stationID)
                .appendQueryParameter(FORMAT_PARAM,format)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"REST params_station_url: "+url);
        return url;
    }

    public static URL buildItemUrl(String entity, String id){
        Uri uri = Uri.parse(URL_REST_RRNN).buildUpon()
                .appendPath(entity)
                .appendQueryParameter(ID_PARAM,id)
                .appendQueryParameter(FORMAT_PARAM,format)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"REST item_url: "+url);
        return url;
    }

    public static URL buildHourlyUrl(String station,String fecha, int total_items, String paramId) {
        Uri uri = Uri.parse(URL_REST_RRNN).buildUpon()
                .appendPath(URL_PREFIX_HOURLY)
                .appendPath(station)
                .appendPath(fecha)
                .appendPath(String.valueOf(total_items))
                .appendQueryParameter(URL_PREFIX_PARAMS,paramId)
                .appendQueryParameter(FORMAT_PARAM,format)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"REST buildHourlyUrl: "+url);
        return url;
    }

    public static URL buildDailyUrl(String station,String fecha, int total_items, String paramId) {
        Uri uri = Uri.parse(URL_REST_RRNN).buildUpon()
                .appendPath(URL_PREFIX_DAILY)
                .appendPath(station)
                .appendPath(fecha)
                .appendPath(String.valueOf(total_items))
                .appendQueryParameter(URL_PREFIX_PARAMS,paramId)
                .appendQueryParameter(FORMAT_PARAM,format)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"REST weather_url: "+url);
        return url;
    }

    public static URL buildMonthlyUrl(String station,String fecha, int total_items) {
        Uri uri = Uri.parse(URL_REST_RRNN).buildUpon()
                .appendPath(URL_PREFIX_MONTHLY)
                .appendPath(station)
                .appendPath(fecha)
                .appendPath(String.valueOf(total_items))
                .appendQueryParameter(FORMAT_PARAM,format)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"REST weather_url: "+url);
        return url;
    }

    /***
     * Returns the entire result from HTTP response
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
