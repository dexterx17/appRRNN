package santana.estudio.tungurahuaclima.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dexter on 02/06/2017.
 */

public class EmbalsesAdapter extends RecyclerView.Adapter<EmbalsesAdapter.EmbalsesAdapterViewHolder>{

    public Cursor embalses;

    public class EmbalsesAdapterViewHolder extends RecyclerView.ViewHolder{
        public EmbalsesAdapterViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public EmbalsesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EmbalsesAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(null == embalses) return 0;
        return embalses.getCount();
    }

    public void swapCursor(Cursor embalsesData){
        embalses= embalsesData;
        notifyDataSetChanged();
    }
}
