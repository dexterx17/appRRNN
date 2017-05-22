package santana.estudio.tungurahuaclima;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import santana.estudio.tungurahuaclima.adapters.DailyAdapter;
import santana.estudio.tungurahuaclima.data.RrnnContract;
import santana.estudio.tungurahuaclima.utilities.NetworkUtils;
import santana.estudio.tungurahuaclima.utilities.PreferencesUtils;
import santana.estudio.tungurahuaclima.utilities.RrnnJsonUtils;

/**
 * Created by dexter on 17/05/2017.
 */

public class DailyActivity extends AppCompatActivity implements
    DailyAdapter.ParamAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<DailyAdapter.Dato[]>
{

    private RecyclerView recyclerView;
    private TextView tvErrorList;
    private ProgressBar pbLoaderList;

    private TextView tvParam;
    private TextView tvMin;
    private TextView tvMax;
    private TextView tvDate;
    private ImageView imIcon;

    private static final String TAG = DailyActivity.class.getSimpleName();

    private static final int PARAMS_LOADER_ID = 2;
    private static final String STATION_KEY_ID = "station_id";
    private static final String PARAM_KEY_ID = "param_id";
    private static final String DATE_KEY_ID = "date";

    String stationID;
    String paramID;
    String paramUnity;
    DailyAdapter paramsAdapter;

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_param);
        tvErrorList = (TextView) findViewById(R.id.tv_error_param);
        pbLoaderList = (ProgressBar) findViewById(R.id.pb_loader_list_param);
        tvMin = (TextView) findViewById(R.id.tv_ph_min);
        tvMax = (TextView) findViewById(R.id.tv_ph_max);
        tvParam = (TextView) findViewById(R.id.tv_ph_param);
        tvDate = (TextView) findViewById(R.id.tv_ph_date);
        imIcon = (ImageView) findViewById(R.id.iv_ph_icon);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mContext = getApplicationContext();
        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                paramID = intent.getStringExtra(Intent.EXTRA_TEXT);
                tvErrorList.setVisibility(View.VISIBLE);
            }
            if (intent.hasExtra(RrnnContract.StationEntry.COLUMN_STATION_ID)) {
                stationID = intent.getStringExtra(RrnnContract.StationEntry.COLUMN_STATION_ID);
                loadStationData(stationID);
                loadParamData(stationID,paramID);
            }
        }
    }

    private void loadStationData(String stationID){
        String select = RrnnContract.StationEntry.COLUMN_STATION_ID + " = ?";
        String[] params = new String[]{stationID};

        Cursor cursor = this.getContentResolver().query(
                RrnnContract.StationEntry.CONTENT_URI,
                null,
                select,
                params,
                null);

        if (cursor != null || cursor.getCount() != 0) {
            cursor.moveToFirst();
            String stationName = cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_NAME));
            String stationType = cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_TYPE));
            getSupportActionBar().setTitle(stationName.toUpperCase());
            getSupportActionBar().setSubtitle(stationType.toUpperCase());
            if (stationType.equals("meteorologica")) {
                imIcon.setImageResource(R.drawable.ic_light_clouds);
            }else {
                imIcon.setImageResource(R.drawable.ic_light_rain);
            }
            cursor.close();
        }
    }


    private void loadParamData(String stationID,String paramID){
        String select = RrnnContract.ParamEntry.COLUMN_STATION_ID + " = ? AND "+
                RrnnContract.ParamEntry.COLUMN_KEY+ " = ?";
        String[] params = new String[]{stationID,paramID};

        Cursor cursor = this.getContentResolver().query(
                RrnnContract.ParamEntry.CONTENT_URI,
                null,
                select,
                params,
                null);

        if (cursor != null || cursor.getCount() != 0) {
            cursor.moveToFirst();
            String paramName = cursor.getString(cursor.getColumnIndex(RrnnContract.ParamEntry.COLUMN_NAME));
            paramUnity = cursor.getString(cursor.getColumnIndex(RrnnContract.ParamEntry.COLUMN_UNITY));
            String paramDateMax = cursor.getString(cursor.getColumnIndex(RrnnContract.ParamEntry.COLUMN_MAX));
            paramDateMax = paramDateMax.replace('/','-');
            tvParam.setText(paramName);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(paramDateMax));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DAY_OF_MONTH,-14);
            //SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            tvDate.setText(sdf.format(c.getTime())+" / "+paramDateMax );
            Bundle bundle = new Bundle();
            bundle.putString(STATION_KEY_ID,stationID);
            bundle.putString(PARAM_KEY_ID,paramID);
            bundle.putString(DATE_KEY_ID,paramDateMax);

            paramsAdapter = new DailyAdapter(this,this,paramUnity);
            recyclerView.setAdapter(paramsAdapter);

            LoaderManager.LoaderCallbacks<DailyAdapter.Dato[]> callback = DailyActivity.this;
            getSupportLoaderManager().initLoader(PARAMS_LOADER_ID, bundle, callback);


            cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String fecha) {
        Intent intent = new Intent(this, HourlyActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,paramID);
        intent.putExtra(RrnnContract.StationEntry.COLUMN_STATION_ID,stationID);
        intent.putExtra(RrnnContract.WeatherHourlyEntry.COLUMN_DATE,fecha.substring(0,10));
        startActivity(intent);
    }

    @Override
    public Loader<DailyAdapter.Dato[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<DailyAdapter.Dato[]>(this) {
            DailyAdapter.Dato[] datos = null;
            @Override
            protected void onStartLoading() {
                Log.v( TAG, "PARAM START NAME: "+args.getString(STATION_KEY_ID));
                if (datos != null) {
                    deliverResult(datos);
                }else{
                    pbLoaderList.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public DailyAdapter.Dato[] loadInBackground() {
                String stationID = args.getString(STATION_KEY_ID);
                String paramID = args.getString(PARAM_KEY_ID);
                String fecha = args.getString(DATE_KEY_ID);
                URL urlDatos = NetworkUtils.buildDailyUrl(mContext,stationID,fecha, PreferencesUtils.getNumDays(mContext),paramID);

                try {
                    String json = NetworkUtils.getResponseFromHttpUrl(urlDatos);

                    DailyAdapter.Dato[] dats = RrnnJsonUtils.getDatosObjectFromJson(json,paramID);
                    return dats;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(DailyAdapter.Dato[] data) {
                datos = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<DailyAdapter.Dato[]> loader, DailyAdapter.Dato[] data) {
        pbLoaderList.setVisibility(View.INVISIBLE);
        paramsAdapter.setDatos(data);
        if (data != null) {
            showData();
            if (data.length > 0) {
                double max = DailyAdapter.Dato.MAX(data);
                double min = DailyAdapter.Dato.MIN(data);
                String decimalesFormat = "%."+ PreferencesUtils.getDecimales(mContext)+"f";
                String minimo = String.format( decimalesFormat, min);
                String maximo = String.format( decimalesFormat, max);
                tvMax.setText(maximo+paramUnity);
                tvMin.setText(minimo+paramUnity);
            }
        }else{
            showError();
        }
    }

    @Override
    public void onLoaderReset(Loader<DailyAdapter.Dato[]> loader) {

    }


    private void showError(){
        tvErrorList.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showData(){
        tvErrorList.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
