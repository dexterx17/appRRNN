package santana.estudio.tungurahuaclima;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by dexter on 24/05/2017.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button imEmbalses;
    Button btnEstacionesTiempoReal;
    Button btnHistoricoDatos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        imEmbalses = (Button) findViewById(R.id.ibEmbalses);
        btnEstacionesTiempoReal = (Button) findViewById(R.id.btn_estaciones_tiempo_real);
        btnHistoricoDatos = (Button) findViewById(R.id.btn_historico_estaciones);
        imEmbalses.setOnClickListener(this);
        btnEstacionesTiempoReal.setOnClickListener(this);
        btnHistoricoDatos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_historico_estaciones:
                Intent intent = new Intent(this, StationsActivity.class);
                startActivity(intent);
                break;
            case R.id.ibEmbalses:
                Intent intentEmbalses = new Intent(this, EmbalsesActivity.class);
                startActivity(intentEmbalses);
                break;
            case R.id.btn_estaciones_tiempo_real:

                break;
        }
    }
}
