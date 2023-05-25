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
import com.carlos.cinemovil.activities.DetailMovieActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.AdapterPelicula;
import adapters.AdapterPeliculaVertical;
import modelos.PeliculaModel;
import networking.endpointApi;
/**
 * Created by Carlos Chica.
 */
public class FragmentPelicula extends Fragment implements AdapterPeliculaVertical.onSelectData, AdapterPelicula.onSelectData {

    private RecyclerView rvNowPlaying, rvFilmRecommend;
    private AdapterPeliculaVertical movieHorizontalAdapter;
    private AdapterPelicula movieAdapter;
    private SearchView searchFilm;
    private List<PeliculaModel> moviePlayNow = new ArrayList<>();
    private List<PeliculaModel> moviePopular = new ArrayList<>();

    private RecyclerView rvNewSection;

    private List<PeliculaModel> movieNewSection = new ArrayList<>();
    private AdapterPeliculaVertical movieNewSectionAdapter;


    public FragmentPelicula() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_peliculas, container, false);

        //Banner1
        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        searchFilm = rootView.findViewById(R.id.searchFilm);
        searchFilm.setQueryHint(getString(R.string.search_film));
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearchMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    getMovie();
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


        rvFilmRecommend = rootView.findViewById(R.id.rvFilmRecommend);
        rvFilmRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilmRecommend.setHasFixedSize(true);

        rvNewSection = rootView.findViewById(R.id.rvNewSection);
        rvNewSection.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNewSection.setHasFixedSize(true);

        getMovieHorizontal();
        getNewSectionMovies();
        getMovie();

        return rootView;
    }

    private void setSearchMovie(String query) {
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.SEARCH_MOVIE
                + endpointApi.APIKEY + endpointApi.LANGUAGE + endpointApi.QUERY + query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            moviePopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PeliculaModel dataApi = new PeliculaModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setTitle(jsonObject.getString("title"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                moviePopular.add(dataApi);
                                showMovie();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Sin datos!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "Error al obtener los datos!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getMovieHorizontal() {
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.MOVIE_PLAYNOW + endpointApi.APIKEY + endpointApi.LANGUAGE )
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PeliculaModel dataApi = new PeliculaModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setTitle(jsonObject.getString("title"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                moviePlayNow.add(dataApi);

                                showMovieVertical();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Sin datos!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "Sin internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void getNewSectionMovies() {
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.UPCOMING_RAIL_FILM + endpointApi.APIKEY + endpointApi.LANGUAGE + endpointApi.REGION)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            movieNewSection = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PeliculaModel dataApi = new PeliculaModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setTitle(jsonObject.getString("title"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                movieNewSection.add(dataApi);
                            }
                            showNewSectionMovies();

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


    private void getMovie() {
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.MOVIE_POPULAR + endpointApi.APIKEY + endpointApi.LANGUAGE )
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            moviePopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PeliculaModel dataApi = new PeliculaModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("release_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setTitle(jsonObject.getString("title"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                moviePopular.add(dataApi);
                                showMovie();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Sin datos!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(getActivity(), "Sin internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void showMovie() {
        movieAdapter = new AdapterPelicula(getActivity(), moviePopular, this);
        rvFilmRecommend.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    private void showMovieVertical() {
        movieHorizontalAdapter = new AdapterPeliculaVertical(getActivity(), moviePlayNow, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        rvNowPlaying.setLayoutManager(layoutManager);
        rvNowPlaying.setAdapter(movieHorizontalAdapter);
        rvNowPlaying.setNestedScrollingEnabled(false);
    }

    private void showNewSectionMovies() {
        movieNewSectionAdapter = new AdapterPeliculaVertical(getActivity(), movieNewSection, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        rvNewSection.setLayoutManager(layoutManager);
        rvNewSection.setAdapter(movieNewSectionAdapter);
        rvNewSection.setNestedScrollingEnabled(false);
    }


    @Override
    public void onSelected(PeliculaModel peliculaModel) {
        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra("detailMovie", peliculaModel);
        startActivity(intent);
    }
}