package santana.estudio.tungurahuaclima.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.net.URL;

import santana.estudio.tungurahuaclima.data.RrnnContract;
import santana.estudio.tungurahuaclima.utilities.NetworkUtils;
import santana.estudio.tungurahuaclima.utilities.RrnnJsonUtils;

/**
 * Created by dexter on 17/05/2017.
 */

public class StationsSyncTask  {
    synchronized public static void syncStations(Context context){

        URL urlStations = NetworkUtils.buildListUrl(context,NetworkUtils.URL_PREFIX_STATIONS);
        try {
            String json = NetworkUtils.getResponseFromHttpUrl(urlStations);
            ContentValues[] stationValues = RrnnJsonUtils.getStationsContentValuesFromJson(context,json);

            if (stationValues != null && stationValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(RrnnContract.StationEntry.CONTENT_URI,
                        null,//select query
                        null); //select attributes

                contentResolver.bulkInsert(RrnnContract.StationEntry.CONTENT_URI, stationValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static void syncParamsStation(Context context, String stationID) {
        URL urlParamsStation = NetworkUtils.buildParamsStationUrl(context,stationID);

        try {
            String json = NetworkUtils.getResponseFromHttpUrl(urlParamsStation);
            ContentValues[] paramValues = RrnnJsonUtils.getParamsStationContentValuesFromJson(context,json, stationID);

            if (paramValues != null && paramValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.bulkInsert(RrnnContract.ParamEntry.CONTENT_URI, paramValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
