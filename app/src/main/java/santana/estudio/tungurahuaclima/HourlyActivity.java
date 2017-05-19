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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import santana.estudio.tungurahuaclima.adapters.HourlyAdapter;
import santana.estudio.tungurahuaclima.adapters.DailyAdapter;
import santana.estudio.tungurahuaclima.data.RrnnContract;
import santana.estudio.tungurahuaclima.utilities.NetworkUtils;
import santana.estudio.tungurahuaclima.utilities.PreferencesUtils;
import santana.estudio.tungurahuaclima.utilities.RrnnJsonUtils;

/**
 * Created by dexter on 18/05/2017.
 */

public class HourlyActivity extends AppCompatActivity implements
        HourlyAdapter.HourlyAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<DailyAdapter.Dato[]> {

    private RecyclerView recyclerView;
    private TextView tvErrorList;
    private ProgressBar pbLoaderList;

    private TextView tvParam;
    private TextView tvMin;
    private TextView tvMax;
    private TextView tvDate;
    private static final String TAG = DailyActivity.class.getSimpleName();

    private static final int HOURLY_LOADER_ID = 2;
    private static final String STATION_KEY_ID = "station_id";
    private static final String PARAM_KEY_ID = "param_id";
    private static final String DATE_KEY_ID = "date";

    String stationID;
    String paramID;
    String dateID;

    HourlyAdapter paramsAdapter;

    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_param);
        tvErrorList = (TextView) findViewById(R.id.tv_error_param);
        tvMin = (TextView) findViewById(R.id.tv_ph_min);
        tvMax = (TextView) findViewById(R.id.tv_ph_max);
        tvParam = (TextView) findViewById(R.id.tv_ph_param);
        tvDate = (TextView) findViewById(R.id.tv_ph_date);
        pbLoaderList = (ProgressBar) findViewById(R.id.pb_loader_list_param);

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
                dateID = intent.getStringExtra(RrnnContract.WeatherHourlyEntry.COLUMN_DATE);

                loadStationData(stationID);
                loadParamData(stationID,paramID,dateID);

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

            cursor.close();
        }
    }

    private void loadParamData(String stationID,String paramID,String fecha){
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
            String paramUnity = cursor.getString(cursor.getColumnIndex(RrnnContract.ParamEntry.COLUMN_UNITY));

            getSupportActionBar().setSubtitle(paramName.toUpperCase());
            tvParam.setText(paramName);
            tvDate.setText(fecha);
            Bundle bundle = new Bundle();
            bundle.putString(STATION_KEY_ID,stationID);
            bundle.putString(PARAM_KEY_ID,paramID);
            bundle.putString(DATE_KEY_ID,fecha);

            paramsAdapter = new HourlyAdapter(this,this,paramUnity);
            recyclerView.setAdapter(paramsAdapter);

            LoaderManager.LoaderCallbacks<DailyAdapter.Dato[]> callback = HourlyActivity.this;
            getSupportLoaderManager().initLoader(HOURLY_LOADER_ID, bundle, callback);

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
        Toast toast = Toast.makeText(this, "CLICK: " + fecha, Toast.LENGTH_SHORT);
        toast.show();
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
                URL urlDatos = NetworkUtils.buildHourlyUrl(getApplicationContext(),stationID,fecha, PreferencesUtils.getNumHours(mContext),paramID);
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
