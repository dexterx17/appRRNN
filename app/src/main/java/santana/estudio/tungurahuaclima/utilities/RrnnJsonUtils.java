package santana.estudio.tungurahuaclima.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import santana.estudio.tungurahuaclima.adapters.ParamAdapter;
import santana.estudio.tungurahuaclima.adapters.ParamsStationAdapter;
import santana.estudio.tungurahuaclima.data.RrnnContract;

/**
 * Created by dexter on 16/05/2017.
 */

public class RrnnJsonUtils {

    private static final String TAG = RrnnJsonUtils.class.getSimpleName();

    /* Station information */
    private static final String STATION_ID = "_id";
    private static final String STATION_CODE = "codigo";
    private static final String STATION_NAME = "nombre";
    private static final String STATION_TYPE = "tipo";
    private static final String STATION_DIRECTION = "direccion";
    private static final String STATION_DESCRIPTION = "descripcion";
    private static final String STATION_LATITUDE = "coordenadaY";
    private static final String STATION_LONGITUDE = "coordenadaX";
    private static final String STATION_CANTON = "canton";
    private static final String STATION_PARROQUIA = "parroquia";
    private static final String STATION_MICROCUENCA = "microcuenca";
    private static final String STATION_ALTITUD = "altitud";

    /* Params Station Information */
    private static final String PARAMS_STATION_LIST_KEY = "params";
    private static final String PARAM_NAME = "nombre";
    private static final String PARAM_ID = "_id";
    private static final String PARAM_KEY = "parametro";
    private static final String PARAM_UNITY = "unidad";
    private static final String PARAM_OPERATION_AVG = "promedio";

    private static final String DATO_DATE = "fecha";
    private static final String DATO_STATION_ID = "id_estacion";
    private static final String DATO_MIN = "_min";
    private static final String DATO_MAX = "_max";
    private static final String DATO_COUNT = "_datos";



    public static String[] getSimpleStationsStringsFromJson(Context context, String jsonStr) throws JSONException {
        String[] stations = null;

        JSONArray jsonArray = new JSONArray(jsonStr);

        stations = new String[jsonArray.length()];

        for (int i = 0; i < stations.length; i++) {
            JSONObject jsonStation = jsonArray.getJSONObject(i);
            String stationName = jsonStation.getString(STATION_NAME);
            String stationCanton = jsonStation.getString(STATION_CANTON);
            stations[i] = stationName + " " + stationCanton;
        }
        return stations;
    }

    public static ContentValues[] getStationsContentValuesFromJson(Context context, String jsonStr) throws JSONException {
        ContentValues[] stations = null;

        JSONArray jsonArray = new JSONArray(jsonStr);

        stations = new ContentValues[jsonArray.length()];

        for (int i = 0; i < stations.length; i++) {
            double stationLat;
            double stationLng;
            String stationDesc;

            JSONObject jsonStation = jsonArray.getJSONObject(i);
            String stationId = jsonStation.getString(STATION_ID);
            String stationName = jsonStation.getString(STATION_NAME);
            String stationType = jsonStation.getString(STATION_TYPE);
            try{
                stationDesc = jsonStation.getString(STATION_DESCRIPTION);
            }catch (JSONException e){
                stationDesc = "";
            }
            String stationHeight = jsonStation.getString(STATION_ALTITUD);

            try {
                stationLat = Double.valueOf(jsonStation.getString(STATION_LATITUDE));
            }catch (Exception e){
                stationLat=0;
            }

            try{
                stationLng = Double.valueOf(jsonStation.getString(STATION_LONGITUDE));
            }catch (Exception e){
                stationLng=0;
            }
            String stationCanton = jsonStation.getString(STATION_CANTON);
            String stationParr = jsonStation.getString(STATION_PARROQUIA);
            String stationAddr = jsonStation.getString(STATION_DIRECTION);

            ContentValues values = new ContentValues();

            values.put(RrnnContract.StationEntry.COLUMN_NAME,stationName);
            values.put(RrnnContract.StationEntry.COLUMN_STATION_ID,stationId);
            values.put(RrnnContract.StationEntry.COLUMN_TYPE,stationType);
            values.put(RrnnContract.StationEntry.COLUMN_DESCRIPTION,stationDesc);
            values.put(RrnnContract.StationEntry.COLUMN_HEIGHT,stationHeight);
            values.put(RrnnContract.StationEntry.COLUMN_LATITUD,stationLat);
            values.put(RrnnContract.StationEntry.COLUMN_LONGITUD,stationLng);
            values.put(RrnnContract.StationEntry.COLUMN_CANTON,stationCanton);
            values.put(RrnnContract.StationEntry.COLUMN_PARROQUIA,stationParr);
            values.put(RrnnContract.StationEntry.COLUMN_ADDRESS,stationAddr);

            stations[i] = values;
        }
        return stations;
    }

    public static ParamsStationAdapter.Param[] getParamsStationObjectFromJson(Context context, String jsonStr) throws JSONException{
        ParamsStationAdapter.Param[] params = null;

        JSONObject jsonObject = new JSONObject(jsonStr);

        JSONArray jsonArray = jsonObject.getJSONArray(PARAMS_STATION_LIST_KEY);

        params = new ParamsStationAdapter.Param[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String paramName = object.getString(PARAM_NAME);
            String paramID = object.getString(PARAM_ID);
            String paramKey = object.getString(PARAM_KEY);
            String paramUnity = object.getString(PARAM_UNITY);
            String paramOperation = object.getString(PARAM_OPERATION_AVG);
            ParamsStationAdapter.Param param = new ParamsStationAdapter.Param(paramID,paramName,paramKey,paramUnity," "," ",paramOperation);
            params[i] = param;
        }
        return params;
    }

    public static ParamAdapter.Dato[] getDatosObjectFromJson(String json,String key) throws JSONException {
        ParamAdapter.Dato[] datos = null;

        JSONArray jsonArray = new JSONArray(json);

        datos = new ParamAdapter.Dato[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String fecha = object.getString(DATO_DATE);
            String stationID = object.getString(DATO_STATION_ID);
            double value = object.getDouble(key);
            double min = object.getDouble(key+DATO_MIN);
            double max = object.getDouble(key+DATO_MAX);
            int count = object.getInt(key+DATO_COUNT);

            ParamAdapter.Dato dato = new ParamAdapter.Dato(fecha, value, min, max, count);
            datos[i] = dato;
        }

        return datos;
    }
}
