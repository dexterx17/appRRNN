package santana.estudio.tungurahuaclima.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import santana.estudio.tungurahuaclima.R;
import santana.estudio.tungurahuaclima.data.RrnnContract;

/**
 * Created by dexter on 16/05/2017.
 */

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationsAdapterViewHolder> {

    private Cursor stations;

    private final StationsAdapterOnClickHandler clickHandler;

    public StationsAdapter(StationsAdapterOnClickHandler clickHandl){
        clickHandler = clickHandl;
    }

    public interface StationsAdapterOnClickHandler{
        void onClick(String stationName);
    }

    public class StationsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tvStationName;
        public final TextView tvStationCanton;
        public final TextView tvStationAltura;
        public final ImageView imgStationImagen;
        public StationsAdapterViewHolder(View itemView) {
            super(itemView);
            tvStationName = (TextView) itemView.findViewById(R.id.tv_stations_name);
            tvStationCanton = (TextView) itemView.findViewById(R.id.tv_stations_canton);
            tvStationAltura= (TextView) itemView.findViewById(R.id.tv_stations_altura);
            imgStationImagen = (ImageView) itemView.findViewById(R.id.iv_stations_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String codigo = (String) v.getTag();
            clickHandler.onClick(codigo);
        }
    }

    @Override
    public StationsAdapter.StationsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.stations_list_item, parent, false);
        view.setFocusable(true);
        return new StationsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StationsAdapter.StationsAdapterViewHolder holder, int position) {
        stations.moveToPosition(position);
        String stationCode = stations.getString(stations.getColumnIndex(RrnnContract.StationEntry.COLUMN_STATION_ID));
        String stationType = stations.getString(stations.getColumnIndex(RrnnContract.StationEntry.COLUMN_TYPE));
        String stationName = stations.getString(stations.getColumnIndex(RrnnContract.StationEntry.COLUMN_NAME));
        String stationCanton = stations.getString(stations.getColumnIndex(RrnnContract.StationEntry.COLUMN_CANTON));
        String stationAltura = stations.getString(stations.getColumnIndex(RrnnContract.StationEntry.COLUMN_HEIGHT));
        holder.tvStationName.setText(stationName);
        holder.tvStationCanton.setText(stationCanton.toUpperCase());
        holder.tvStationAltura.setText(stationAltura);
        if (stationType.equals("meteorologica")) {
            holder.imgStationImagen.setImageResource(R.drawable.ic_light_clouds);
        }else {
            holder.imgStationImagen.setImageResource(R.drawable.ic_light_rain);
        }
        holder.itemView.setTag(stationCode);
    }

    @Override
    public int getItemCount() {
        if(null == stations) return 0;
        return stations.getCount();
    }

    public void swapCursor(Cursor stationsData){
        stations= stationsData;
        notifyDataSetChanged();
    }

    public static class Station{
        String id;
        String name;
        String tipo;
        String code;

        public Station(String id, String name, String tipo, String code) {
            this.id = id;
            this.name = name;
            this.tipo = tipo;
            this.code = code;
        }
    }
}
