package santana.estudio.tungurahuaclima;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import santana.estudio.tungurahuaclima.utilities.PreferencesUtils;

/**
 * Created by dexter on 26/05/2017.
 */

public class EmbalsesActivity extends AppCompatActivity {

    private static final String TAG = EmbalsesActivity.class.getSimpleName();

    private static final int STATIONS_LOADER_ID =0;

    private RecyclerView recyclerView;
    private TextView tvErrorList;
    private ProgressBar pbLoaderList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embalses);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_embalses);
        tvErrorList = (TextView) findViewById(R.id.tv_error_list_embalses);
        pbLoaderList = (ProgressBar) findViewById(R.id.pb_loader_list_embalses);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setOrigenUrl();

    }


    private void setOrigenUrl(){
        String url_load = PreferencesUtils.getServerUrl(this);
        String msg = getResources().getString(R.string.loading_from_rrnn);
        tvErrorList.setText(msg+" "+url_load);
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
}
