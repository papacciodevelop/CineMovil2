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
import com.carlos.cinemovil.activities.DetailSerieActivity;

import java.util.ArrayList;
import java.util.List;

import BBDD.DBHandler;
import adapters.AdapterSeries;
import modelos.SerieModel;
/**
 * Created by Carlos Chica.
 */
public class FragmentFavoriteSeries extends Fragment implements AdapterSeries.onSelectData {

    private RecyclerView rvMovieFav;
    private List<SerieModel> serieModel = new ArrayList<>();
    private DBHandler dbHandler;
    private TextView txtNoData;

    public FragmentFavoriteSeries() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ventana_fav_peliculas, container, false);

        dbHandler = new DBHandler(getActivity());

        rvMovieFav = rootView.findViewById(R.id.rvMovieFav);
        rvMovieFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovieFav.setAdapter(new AdapterSeries(getActivity(), serieModel, this));
        rvMovieFav.setHasFixedSize(true);
        txtNoData = rootView.findViewById(R.id.tvNotFound);

        setData();

        return rootView;
    }

    private void setData() {
        serieModel = dbHandler.showFavoriteTV();
        if (serieModel.size() == 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvMovieFav.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvMovieFav.setVisibility(View.VISIBLE);
            rvMovieFav.setAdapter(new AdapterSeries(getActivity(), serieModel, this));
        }
    }

    @Override
    public void onSelected(SerieModel serieModel) {
        Intent intent = new Intent(getActivity(), DetailSerieActivity.class);
        intent.putExtra("detailTV", serieModel);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}
