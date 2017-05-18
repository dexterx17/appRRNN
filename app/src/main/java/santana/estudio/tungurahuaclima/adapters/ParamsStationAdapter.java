package santana.estudio.tungurahuaclima.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import santana.estudio.tungurahuaclima.R;

/**
 * Created by dexter on 16/05/2017.
 */

public class ParamsStationAdapter extends RecyclerView.Adapter<ParamsStationAdapter.ParamsStationAdapterViewHolder>{

    ParamsStationAdapter.Param [] params;

    private final ParamsStationAdapterOnClickHander clickHander;

    public ParamsStationAdapter(ParamsStationAdapterOnClickHander handler){
        clickHander = handler;
    }

    public interface ParamsStationAdapterOnClickHander{
        void onClick(String param);
    }
    public class ParamsStationAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView tvParamName;
        public final TextView tvParamUnity;
        public final TextView tvParamDateMin;
        public final TextView tvParamDateMax;

        public ParamsStationAdapterViewHolder(View view) {
            super(view);
            tvParamName = (TextView) view.findViewById(R.id.tv_params_name);
            tvParamUnity = (TextView) view.findViewById(R.id.tv_params_unity);
            tvParamDateMin = (TextView) view.findViewById(R.id.tv_params_date_min);
            tvParamDateMax = (TextView) view.findViewById(R.id.tv_params_date_max);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            String valor = params[pos].key;
            clickHander.onClick(valor);
        }
    }
    @Override
    public ParamsStationAdapter.ParamsStationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.param_station_list_item, parent, false);
        return new ParamsStationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParamsStationAdapter.ParamsStationAdapterViewHolder holder, int position) {
        holder.tvParamName.setText(params[position].name);
        holder.tvParamUnity.setText(params[position].unity);
        holder.tvParamDateMin.setText(params[position].dateMin);
        holder.tvParamDateMax.setText(params[position].dateMax);
    }

    @Override
    public int getItemCount() {
        if( params == null) return 0;
        return params.length;
    }

    public void setParamsStationData(ParamsStationAdapter.Param[] data) {
        params = data;
        notifyDataSetChanged();
    }

    public static class Param{
        String id;
        String name;
        String key;
        String unity;
        String dateMin;
        String dateMax;
        String operation;

        public Param(String id, String name, String key, String unity, String dateMin, String dateMax, String operation) {
            this.id = id;
            this.name = name;
            this.key = key;
            this.unity = unity;
            this.dateMin = dateMin;
            this.dateMax = dateMax;
            this.operation = operation;
        }
    }
}
