package santana.estudio.tungurahuaclima;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import santana.estudio.tungurahuaclima.adapters.ParamsStationAdapter;
import santana.estudio.tungurahuaclima.data.RrnnContract;
import santana.estudio.tungurahuaclima.sync.SyncUtils;
import santana.estudio.tungurahuaclima.utilities.PreferencesUtils;

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

        setOrigenUrl();
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

        paramsStationAdapter = new ParamsStationAdapter(this,this);
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

    private void setOrigenUrl(){
        String url_load = PreferencesUtils.getServerUrl(this);
        String msg = getResources().getString(R.string.loading_from_rrnn);
        tvErrorList.setText(msg+" "+url_load);
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
            String stationDescription = cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_DESCRIPTION));
            String stationAltura = cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_HEIGHT));
            getSupportActionBar().setSubtitle(stationType.toUpperCase());
            tvName.setText(stationName);
            tvCanton.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_CANTON)));
            tvParroquia.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_PARROQUIA)));
            tvAddress.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_ADDRESS)));
            tvLat.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_LATITUD)));
            tvLng.setText(cursor.getString(cursor.getColumnIndex(RrnnContract.StationEntry.COLUMN_LONGITUD)));
            tvDescription.setText(stationDescription.trim());
            tvAltura.setText(stationAltura+" "+getResources().getString(R.string.msnm));
            cursor.close();
        }

        Bundle bundle = new Bundle();
        bundle.putString(STATION_KEY_ID,stationID);
        showError();
        SyncUtils.syncParamsStation(this,stationID);
        getSupportLoaderManager().initLoader(PARAMS_STATION_LOADER_ID, bundle, this);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATION_KEY_ID,stationID);
        super.onSaveInstanceState(outState);
    }

    private void showError(){
        pbLoaderList.setVisibility(View.VISIBLE);
        tvErrorList.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showData(){
        pbLoaderList.setVisibility(View.INVISIBLE);
        tvErrorList.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String param) {
        Intent intent = new Intent(this, DailyActivity.class);
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
            pbLoaderList.setVisibility(View.INVISIBLE);
            tvErrorList.setText(getResources().getString(R.string.error_load_list_params_station));
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        paramsStationAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                getSupportLoaderManager().restartLoader(PARAMS_STATION_LOADER_ID, null, this);
                break;
            case R.id.action_map:
                openMap();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                onBackPressed();
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openMap(){
        String address = "Ambato, Ecuador";
        Uri geoLocation = Uri.parse("geo:0.0?q=" + address);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Log.d(TAG,"No se pudo llamar "+geoLocation.toString()+", sin app de mapas");
        }
    }
}
