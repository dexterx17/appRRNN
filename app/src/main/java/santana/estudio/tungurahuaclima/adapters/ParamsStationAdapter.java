package santana.estudio.tungurahuaclima.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import santana.estudio.tungurahuaclima.R;
import santana.estudio.tungurahuaclima.data.RrnnContract;

/**
 * Created by dexter on 16/05/2017.
 */

public class ParamsStationAdapter extends RecyclerView.Adapter<ParamsStationAdapter.ParamsStationAdapterViewHolder>{

    Cursor params;

    Context context;

    private final ParamsStationAdapterOnClickHander clickHander;

    public ParamsStationAdapter(Context context,ParamsStationAdapterOnClickHander handler){
        this.context = context;
        clickHander = handler;
    }

    public interface ParamsStationAdapterOnClickHander{
        void onClick(String param);
    }
    public class ParamsStationAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView tvParamName;
        public final TextView tvParamUnity;
        public final TextView tvParamValue;

        public ParamsStationAdapterViewHolder(View view) {
            super(view);
            tvParamName = (TextView) view.findViewById(R.id.tv_params_name);
            tvParamUnity = (TextView) view.findViewById(R.id.tv_params_unity);
            tvParamValue= (TextView) view.findViewById(R.id.tv_params_value);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String codigo = (String) v.getTag();
            clickHander.onClick(codigo);
        }
    }
    @Override
    public ParamsStationAdapter.ParamsStationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.param_station_list_item, parent, false);
        view.setFocusable(true);
        return new ParamsStationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParamsStationAdapter.ParamsStationAdapterViewHolder holder, int position) {
        params.moveToPosition(position);
        String paramKey = params.getString(params.getColumnIndex(RrnnContract.ParamEntry.COLUMN_KEY));
        String paramName = params.getString(params.getColumnIndex(RrnnContract.ParamEntry.COLUMN_NAME));
        String paramUnity = params.getString(params.getColumnIndex(RrnnContract.ParamEntry.COLUMN_UNITY));
        holder.tvParamName.setText(paramName);
        holder.tvParamUnity.setText(paramUnity);

        holder.itemView.setTag(paramKey);
    }

    @Override
    public int getItemCount() {
        if( params == null) return 0;
        return params.getCount();
    }

    public void swapCursor(Cursor data) {
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
