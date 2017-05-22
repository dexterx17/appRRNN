package santana.estudio.tungurahuaclima;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import santana.estudio.tungurahuaclima.adapters.HourlyAdapter;
import santana.estudio.tungurahuaclima.adapters.DailyAdapter;
import santana.estudio.tungurahuaclima.data.RrnnContract;
import santana.estudio.tungurahuaclima.utilities.NetworkUtils;
import santana.estudio.tungurahuaclima.utilities.PreferencesUtils;
import santana.estudio.tungurahuaclima.utilities.RrnnJsonUtils;
import santana.estudio.tungurahuaclima.utilities.Utils;

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
    private LineChart chart;

    private static final String TAG = DailyActivity.class.getSimpleName();

    private static final int HOURLY_LOADER_ID = 2;
    private static final String STATION_KEY_ID = "station_id";
    private static final String PARAM_KEY_ID = "param_id";
    private static final String DATE_KEY_ID = "date";

    String stationID;
    String paramID;
    String dateID;
    String paramUnity;
    private ImageView imIcon;

    HourlyAdapter paramsAdapter;

    Context mContext;

    List<Entry> entriesMax = new ArrayList<Entry>();
    LineDataSet dataSetMax;
    List<Entry> entriesMin = new ArrayList<Entry>();
    LineDataSet dataSetMin;
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
        imIcon = (ImageView) findViewById(R.id.iv_ph_icon);
        chart = (LineChart) findViewById(R.id.chart_daily);

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
            if (stationType.equals("meteorologica")) {
                imIcon.setImageResource(R.drawable.ic_light_clouds);
            }else {
                imIcon.setImageResource(R.drawable.ic_light_rain);
            }
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
            paramUnity = cursor.getString(cursor.getColumnIndex(RrnnContract.ParamEntry.COLUMN_UNITY));

            getSupportActionBar().setSubtitle(paramName.toUpperCase());
            tvParam.setText(paramName);
            int year = Integer.valueOf(fecha.substring(0, 4));
            int month = Integer.valueOf(fecha.substring(5, 7));
            int day = Integer.valueOf(fecha.substring(8, 10));

            long dateInMillis = new Date(year,month,day).getTime();
         /* Get human readable string using our utility method */
            String dateString = Utils.getFriendlyDateString(mContext, dateInMillis, false);


            tvDate.setText(dateString +" "+year);
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
            double max = DailyAdapter.Dato.MAX(data);
            double min = DailyAdapter.Dato.MIN(data);
            String decimalesFormat = "%."+ PreferencesUtils.getDecimales(mContext)+"f";
            String minimo = String.format( decimalesFormat, min);
            String maximo = String.format( decimalesFormat, max);
            tvMax.setText(maximo+paramUnity);
            tvMin.setText(minimo+paramUnity);

            chart.setDrawGridBackground(false);
            chart.getDescription().setEnabled(false);
            chart.setBackgroundColor(Color.rgb(255, 255, 255));

            IAxisValueFormatter xAxisFormatter = new HourlyActivity.FechaValueFormatter(chart,data);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(xAxisFormatter);

            for (int i = 0; i <data.length ; i++) {
                DailyAdapter.Dato d = data[data.length-1-i];
                entriesMax.add(new Entry((float)i, (float)d.max));
                entriesMin.add(new Entry((float)i, (float)d.min));
            }
            dataSetMax= new LineDataSet(entriesMax, paramID+ " " +getResources().getString(R.string.maxima));
            dataSetMax.setLineWidth(2.5f);
            dataSetMax.setCircleRadius(4.5f);
            dataSetMax.setHighLightColor(Color.rgb(244, 117, 117));
            dataSetMin = new LineDataSet(entriesMin, paramID+" "+ getResources().getString(R.string.minima));
            dataSetMin.setLineWidth(2.5f);
            dataSetMin.setCircleRadius(4.5f);
            dataSetMin.setHighLightColor(Color.rgb(244, 117, 117));
            dataSetMin.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            dataSetMin.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);

            ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
            sets.add(dataSetMin);
            sets.add(dataSetMax);

            LineData cd = new LineData(sets);

            chart.setData(cd);

            chart.invalidate();
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


    private class FechaValueFormatter implements IAxisValueFormatter {

        private BarLineChartBase<?> chart;
        DailyAdapter.Dato[] datos;
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int position= (int) value;
            try {
                DailyAdapter.Dato d = datos[datos.length-1-position];
                String day = d.fecha.substring(11, 13);
                return String.valueOf(day);
            } catch (Exception e) {
                return String.valueOf(position);
            }
        }

        public FechaValueFormatter(BarLineChartBase<?> chart,DailyAdapter.Dato[] datos) {
            this.chart = chart;
            this.datos = datos;
        }
    }
}
