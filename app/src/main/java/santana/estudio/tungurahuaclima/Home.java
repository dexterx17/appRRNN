package santana.estudio.tungurahuaclima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import santana.estudio.tungurahuaclima.adapters.StationsAdapter;
import santana.estudio.tungurahuaclima.data.RrnnContract;
import santana.estudio.tungurahuaclima.sync.SyncUtils;
import santana.estudio.tungurahuaclima.utilities.NetworkUtils;
import santana.estudio.tungurahuaclima.utilities.RrnnJsonUtils;

public class Home extends AppCompatActivity implements
        StationsAdapter.StationsAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = Home.class.getSimpleName();

    private static final int STATIONS_LOADER_ID =0;

    private RecyclerView recyclerView;
    private TextView tvErrorList;
    private ProgressBar pbLoaderList;

    StationsAdapter stationsAdapter;

    private static boolean PREFERENCES_UPDATED = false;

    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_estaciones);
        tvErrorList = (TextView) findViewById(R.id.tv_error_list_stations);
        pbLoaderList = (ProgressBar) findViewById(R.id.pb_loader_list_stations);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        stationsAdapter = new StationsAdapter(this);
        recyclerView.setAdapter(stationsAdapter);

        showError();

        getSupportLoaderManager().initLoader(STATIONS_LOADER_ID, null, this);

        SyncUtils.init(this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                getSupportLoaderManager().restartLoader(STATIONS_LOADER_ID, null, this);
                break;
            case R.id.action_map:
                openMap();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        switch (id) {
            case STATIONS_LOADER_ID:
                Uri stationsURI = RrnnContract.StationEntry.CONTENT_URI;
                String sort = RrnnContract.StationEntry.COLUMN_NAME + " ASC";
                return new CursorLoader(this,
                        stationsURI,
                        null,//projection
                        null,//selection
                        null,//selection params
                        sort);
            default:
                throw new RuntimeException("Loader no implementado: "+id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            stationsAdapter.swapCursor(data);
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
        stationsAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String stationId) {
        Intent intent = new Intent(this, StationActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,stationId);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_UPDATED) {
            getSupportLoaderManager().restartLoader(STATIONS_LOADER_ID, null, this);
            PREFERENCES_UPDATED=false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_UPDATED = true;
    }
}
