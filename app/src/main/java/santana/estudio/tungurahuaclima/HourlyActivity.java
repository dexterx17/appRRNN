package santana.estudio.tungurahuaclima;

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

import santana.estudio.tungurahuaclima.adapters.ParamAdapter;
import santana.estudio.tungurahuaclima.utilities.NetworkUtils;
import santana.estudio.tungurahuaclima.utilities.RrnnJsonUtils;

/**
 * Created by dexter on 18/05/2017.
 */

public class HourlyActivity extends AppCompatActivity implements
        ParamAdapter.ParamAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ParamAdapter.Dato[]> {

    private RecyclerView recyclerView;
    private TextView tvErrorList;
    private ProgressBar pbLoaderList;

    private static final String TAG = ParamActivity.class.getSimpleName();

    private static final int PARAMS_LOADER_ID = 2;
    private static final String STATION_KEY_ID = "station_id";
    private static final String PARAM_KEY_ID = "param_id";
    private static final String DATE_KEY_ID = "date";

    String stationID;
    String paramID;
    String dateID;

    ParamAdapter paramsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_param);
        tvErrorList = (TextView) findViewById(R.id.tv_error_param);
        pbLoaderList = (ProgressBar) findViewById(R.id.pb_loader_list_param);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        paramsAdapter = new ParamAdapter(this,this,"");
        recyclerView.setAdapter(paramsAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String param) {
        Toast toast = Toast.makeText(this, "CLICK: " + param, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public Loader<ParamAdapter.Dato[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ParamAdapter.Dato[]>(this) {
            ParamAdapter.Dato[] datos = null;
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
            public ParamAdapter.Dato[] loadInBackground() {
                String stationID = args.getString(STATION_KEY_ID);
                String paramID = args.getString(PARAM_KEY_ID);
                String fecha = args.getString(DATE_KEY_ID);
                URL urlDatos = NetworkUtils.buildHourlyUrl(stationID,fecha,24,paramID);

                try {
                    String json = NetworkUtils.getResponseFromHttpUrl(urlDatos);

                    ParamAdapter.Dato[] dats = RrnnJsonUtils.getDatosObjectFromJson(json,paramID);
                    return dats;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(ParamAdapter.Dato[] data) {
                datos = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ParamAdapter.Dato[]> loader, ParamAdapter.Dato[] data) {
        pbLoaderList.setVisibility(View.INVISIBLE);
        paramsAdapter.setDatos(data);
        if (data != null) {
            showData();
        }else{
            showError();
        }
    }

    @Override
    public void onLoaderReset(Loader<ParamAdapter.Dato[]> loader) {

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
