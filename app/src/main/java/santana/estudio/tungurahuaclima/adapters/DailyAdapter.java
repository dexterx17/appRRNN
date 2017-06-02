package santana.estudio.tungurahuaclima.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import santana.estudio.tungurahuaclima.R;
import santana.estudio.tungurahuaclima.utilities.PreferencesUtils;
import santana.estudio.tungurahuaclima.utilities.Utils;

/**
 * Created by dexter on 17/05/2017.
 */

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ParamAdapterViewHolder>{

    Dato[] datos;

    private final Context mContext;

    private final ParamAdapterOnClickHandler clickHandler;

    private final String unidad;
    public DailyAdapter(@NonNull Context context, ParamAdapterOnClickHandler handler, String unidad) {
        mContext = context;
        clickHandler = handler;
        this.unidad = unidad;
    }

    public interface ParamAdapterOnClickHandler{
        void onClick(String param);
    }

    @Override
    public ParamAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.param_list_item, parent, false);
        return new ParamAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParamAdapterViewHolder holder, int position) {
        String fecha = datos[position].fecha;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(fecha));
            long dateInMillis = c.getTimeInMillis();
         /* Get human readable string using our utility method */
            String dateString = Utils.getFriendlyDateString(mContext, dateInMillis, false);

            holder.tvDate.setText(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String decimalesFormat = "%."+ PreferencesUtils.getDecimales(holder.itemView.getContext())+"f";
        String min = String.format( decimalesFormat, datos[position].min );
        String max = String.format( decimalesFormat, datos[position].max );
        String value = String.format( decimalesFormat, datos[position].value );

        holder.tvMin.setText(min+" "+unidad);
        holder.tvMax.setText(max+" "+unidad);
        holder.tvValue.setText(value+" "+unidad);
        holder.tvCount.setText(String.valueOf(datos[position].count));
    }

    @Override
    public int getItemCount() {
        if(datos == null) return 0;
        return datos.length;
    }

    public void setDatos(Dato[] data){
        datos = data;
        notifyDataSetChanged();
    }
    public class ParamAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tvDate;
        public final TextView tvValue;
        public final TextView tvMax;
        public final TextView tvMin;
        public final TextView tvCount;
        public ParamAdapterViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_param_list_date);
            tvValue = (TextView) itemView.findViewById(R.id.tv_param_list_value);
            tvMax = (TextView) itemView.findViewById(R.id.tv_param_list_max);
            tvMin = (TextView) itemView.findViewById(R.id.tv_param_list_min);
            tvCount = (TextView) itemView.findViewById(R.id.tv_param_list_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            String valor = datos[pos].fecha;
            clickHandler.onClick(valor);
        }
    }

    public static class Dato{
        public String fecha;
        public double value;
        public double min;
        public double max;
        public int count;


        public Dato(String fecha, double value, double min, double max, int count) {
            this.fecha = fecha;
            this.value = value;
            this.min = min;
            this.max = max;
            this.count = count;
        }

        public static double MAX(Dato[] datos) {
            double max = datos[0].max;
            for (Dato d:datos) {
                if (d.max > max) {
                    max=d.max;
                }
            }
            return max;
        }
        public static double MIN(Dato[] datos) {
            double min = datos[0].min;
            for (Dato d:datos) {
                if (d.min < min) {
                    min=d.min;
                }
            }
            return min;
        }
    }
}
