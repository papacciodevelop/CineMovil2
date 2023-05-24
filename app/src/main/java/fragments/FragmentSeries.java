package fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.carlos.cinemovil.R;
import com.carlos.cinemovil.activities.DetailTelevisionActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.AdapterSeries;
import adapters.TvHorizontalAdapter;
import modelos.SerieModel;
import networking.endpointApi;
/**
 * Created by Carlos Chica.
 */
public class FragmentSeries extends Fragment implements TvHorizontalAdapter.onSelectData, AdapterSeries.onSelectData{

    private RecyclerView rvNowPlaying, rvFilmRecommend, rvNewSeriesSection;
    private TvHorizontalAdapter tvHorizontalAdapter;
    private AdapterSeries adapterSeries;
    private SearchView searchFilm;
    private List<SerieModel> tvPlayNow = new ArrayList<>();
    private List<SerieModel> tvPopular = new ArrayList<>();
    private List<SerieModel> newSeriesSection;
    private TvHorizontalAdapter newSeriesSectionAdapter;

    public FragmentSeries() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_peliculas, container, false);


        //banner
        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        searchFilm = rootView.findViewById(R.id.searchFilm);
        searchFilm.setQueryHint(getString(R.string.search_film));
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearchTv(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    getFilmTV();
                return false;
            }
        });

        int searchPlateId = searchFilm.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchFilm.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        rvNowPlaying = rootView.findViewById(R.id.rvNowPlaying);
        rvNowPlaying.setHasFixedSize(true);
        rvNowPlaying.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvNowPlaying);

        rvFilmRecommend = rootView.findViewById(R.id.rvFilmRecommend);
        rvFilmRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilmRecommend.setHasFixedSize(true);

        rvNewSeriesSection = rootView.findViewById(R.id.rvNewSection);
        rvNewSeriesSection.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNewSeriesSection.setHasFixedSize(true);

        getTvHorizontal();
        getNewSectionSeries();
        getFilmTV();


        return rootView;
    }

    private void setSearchTv(String query) {
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.SEARCH_TV
                + endpointApi.APIKEY + endpointApi.LANGUAGE + endpointApi.QUERY + query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tvPopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SerieModel dataApi = new SerieModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setName(jsonObject.getString("name"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                tvPopular.add(dataApi);
                                showFilmTV();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Sin datos!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "La llamada no trae nada", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getTvHorizontal() {
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.TV_PLAYNOW + endpointApi.APIKEY + endpointApi.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SerieModel dataApi = new SerieModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setName(jsonObject.getString("name"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                tvPlayNow.add(dataApi);

                                showTvVertical();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "No hay datos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), " Coneción perdida!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getNewSectionSeries() {
        // realiza la llamada a la API
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.UPCOMING_RAIL_SERIES + endpointApi.APIKEY + endpointApi.LANGUAGE + endpointApi.REGION)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            newSeriesSection = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SerieModel dataApi = new SerieModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setName(jsonObject.getString("name"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                newSeriesSection.add(dataApi);
                            }
                            showNewSectionSeries();

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Sin datos!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "No trajo datos da llamada!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void getFilmTV() {
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.TV_POPULAR + endpointApi.APIKEY + endpointApi.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tvPopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SerieModel dataApi = new SerieModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setName(jsonObject.getString("name"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                tvPopular.add(dataApi);
                                showFilmTV();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "No hay datos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "Sin internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showTvVertical() {
        tvHorizontalAdapter = new TvHorizontalAdapter(getActivity(), tvPlayNow, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        rvNowPlaying.setLayoutManager(layoutManager);
        rvNowPlaying.setAdapter(tvHorizontalAdapter);
        rvNowPlaying.setNestedScrollingEnabled(false);
    }

    private void showFilmTV() {
        adapterSeries = new AdapterSeries(getActivity(), tvPopular, this);
        rvFilmRecommend.setAdapter(adapterSeries);
        adapterSeries.notifyDataSetChanged();
    }


    private void showNewSectionSeries() {
        newSeriesSectionAdapter = new TvHorizontalAdapter(getActivity(), newSeriesSection, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        rvNewSeriesSection.setLayoutManager(layoutManager);
        rvNewSeriesSection.setAdapter(newSeriesSectionAdapter);
        rvNewSeriesSection.setNestedScrollingEnabled(false);
    }

    @Override
    public void onSelected(SerieModel serieModel) {
        Intent intent = new Intent(getActivity(), DetailTelevisionActivity.class);
        intent.putExtra("detailTV", serieModel);
        startActivity(intent);
    }
}
