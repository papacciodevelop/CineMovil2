package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.cinemovil.R;

import java.util.List;

import modelos.PeliculaModel;
import networking.endpointApi;

/**
 * Created by Carlos Chica.
 */
public class AdapterPeliculaVertical extends RecyclerView.Adapter<AdapterPeliculaVertical.ViewHolder> {

    private List<PeliculaModel> items;
    private AdapterPeliculaVertical.onSelectData onSelectData;
    private Context mContext;

    public interface onSelectData {
        void onSelected(PeliculaModel peliculaModel);
    }

    public AdapterPeliculaVertical(Context context, List<PeliculaModel> items, AdapterPeliculaVertical.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_items_peliculas_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PeliculaModel data = items.get(position);

        Glide.with(mContext)
                .load(endpointApi.URLIMAGE + data.getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                .into(holder.imgPhoto);

        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectData.onSelected(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
        }
    }

}
