package santana.estudio.tungurahuaclima;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
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

import santana.estudio.tungurahuaclima.adapters.ParamsStationAdapter;
import santana.estudio.tungurahuaclima.adapters.StationsAdapter;
import santana.estudio.tungurahuaclima.data.RrnnContract;
import santana.estudio.tungurahuaclima.sync.SyncUtils;
import santana.estudio.tungurahuaclima.utilities.NetworkUtils;
import santana.estudio.tungurahuaclima.utilities.RrnnJsonUtils;

/**
 * Created by dexter on 16/05/2017.
 */

public class StationActivity extends AppCompatActivity implements
        ParamsStationAdapter.ParamsStationAdapterOnClickHander,
        LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView;
    private TextView tvErrorList;
    private ProgressBar pbLoaderList;

    private static final String TAG = StationActivity.class.getSimpleName();

    private static final int PARAMS_STATION_LOADER_ID = 1;
    private static final String STATION_KEY_ID = "_id";

    private TextView tvName;
    private TextView tvCanton;
    private TextView tvParroquia;
    private TextView tvAddress;
    private TextView tvLat;
    private TextView tvLng;
    private TextView tvDescription;
    private TextView tvAltura;

    String stationID;

    ParamsStationAdapter paramsStationAdapter;

    private int mPosition = RecyclerView.NO_POSITION;

    private static boolean PREFERENCES_UPDATED = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_params_station);
        tvErrorList = (TextView) findViewById(R.id.tv_error_station);
        pbLoaderList = (ProgressBar) findViewById(R.id.pb_loader_station);

        tvName = (TextView) findViewById(R.id.tv_station_name);
        tvCanton = (TextView) findViewById(R.id.tv_station_canton);
        tvParroquia = (TextView) findViewById(R.id.tv_station_parroquia);
        tvAddress = (TextView) findViewById(R.id.tv_station_address);
        tvLat = (TextView) findViewById(R.id.tv_station_lat);
        tvLng = (TextView) findViewById(R.id.tv_station_lng);
        tvDescription = (TextView) findViewById(R.id.tv_station_description);
        tvAltura = (TextView) findViewById(R.id.tv_station_height);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        paramsStationAdapter = new ParamsStationAdapter(this);
        recyclerView.setAdapter(paramsStationAdapter);
        Log.v(TAG, "onCreate:");
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            Log.v(TAG, "savedInstance:");
            if (!savedInstanceState.getString(STATION_KEY_ID).equals("")) {
                Log.v(TAG, "savedInstanceValue:"+savedInstanceState.getString(STATION_KEY_ID));
                initData(savedInstanceState.getString(STATION_KEY_ID));
            }
        }

        if (intent != null) {
            Log.v(TAG, "intent: ");
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                Log.v(TAG, "intentExtra: "+intent.getStringExtra(Intent.EXTRA_TEXT));
                stationID = intent.getStringExtra(Intent.EXTRA_TEXT);
                initData(stationID);
            }
        }

    }

    private void initData(String stationID) {
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
            String stationAltura = cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_HEIGHT));
            getSupportActionBar().setSubtitle(stationType.toUpperCase());
            tvName.setText(stationName);
            tvCanton.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_CANTON)));
            tvParroquia.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_PARROQUIA)));
            tvAddress.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_ADDRESS)));
            tvLat.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_LATITUD)));
            tvLng.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_LONGITUD)));
            tvDescription.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_DESCRIPTION)));
            tvAltura.setText(stationAltura+" "+getResources().getString(R.string.msnm));
            cursor.close();
        }

        Bundle bundle = new Bundle();
        bundle.putString(STATION_KEY_ID,stationID);
        SyncUtils.syncParamsStation(this,stationID);
        getSupportLoaderManager().initLoader(PARAMS_STATION_LOADER_ID, bundle, this);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATION_KEY_ID,stationID);
        super.onSaveInstanceState(outState);
    }

    private void showError(){
        tvErrorList.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showData(){
        tvErrorList.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String param) {
        Intent intent = new Intent(this, ParamActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,param);
        intent.putExtra(RrnnContract.StationEntry.COLUMN_STATION_ID,stationID);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        switch (id){
            case PARAMS_STATION_LOADER_ID:
                Uri paramsURI = RrnnContract.ParamEntry.CONTENT_URI.buildUpon()
                        .appendPath(stationID)
                        .build();
                String select = RrnnContract.StationEntry.COLUMN_STATION_ID + " = ?";
                String[] params = new String[]{stationID};
                return new CursorLoader(this,
                        paramsURI,
                        null,
                        select,
                        params,
                        null);
            default:
                throw new RuntimeException("Loader no implementado: "+id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            paramsStationAdapter.swapCursor(data);
            if(mPosition==RecyclerView.NO_POSITION) mPosition = 0;
            recyclerView.smoothScrollToPosition(mPosition);
            if (data.getCount() != 0) {
                showData();
            }
        }else{
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        paramsStationAdapter.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
