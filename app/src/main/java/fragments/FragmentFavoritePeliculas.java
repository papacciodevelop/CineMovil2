package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.carlos.cinemovil.R;
import com.carlos.cinemovil.activities.DetailMovieActivity;

import java.util.ArrayList;
import java.util.List;

import BBDD.DBHandler;
import adapters.AdapterPelicula;
import modelos.PeliculaModel;


/**
 * Created by Carlos Chica.
 */
public class FragmentFavoritePeliculas extends Fragment implements AdapterPelicula.onSelectData {
    private RecyclerView rvMovieFav;
    private List<PeliculaModel> peliculaModel = new ArrayList<>();
    private DBHandler dbHandler;
    private TextView txtNoData;

    public FragmentFavoritePeliculas() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ventana_fav_peliculas, container, false);
        dbHandler = new DBHandler(getActivity());

        rvMovieFav = rootView.findViewById(R.id.rvMovieFav);
        rvMovieFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovieFav.setAdapter(new AdapterPelicula(getActivity(), peliculaModel, this));
        rvMovieFav.setHasFixedSize(true);

        txtNoData = rootView.findViewById(R.id.tvNotFound);

        setData();

        return rootView;
    }

    private void setData() {
        peliculaModel = dbHandler.showFavoriteMovie();
        if (peliculaModel.size() == 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvMovieFav.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvMovieFav.setVisibility(View.VISIBLE);
            rvMovieFav.setAdapter(new AdapterPelicula(getActivity(), peliculaModel, this));
        }
    }

    @Override
    public void onSelected(PeliculaModel peliculaModel) {
        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra("detailMovie", peliculaModel);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}
