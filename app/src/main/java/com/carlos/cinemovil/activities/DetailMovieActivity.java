package com.carlos.cinemovil.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlos.cinemovil.R;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import BBDD.DBHandler;
import modelos.PeliculaModel;
import modelos.TrailerModel;
import networking.endpointApi;

/**
 * Created by Carlos Chica.
 */
public class DetailMovieActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTitle, tvName, tvRating, tvRelease, tvOverview;
    ImageView imgCover, imgPhoto;

    MaterialFavoriteButton imgFavorite;
    FloatingActionButton fabShare;
    RatingBar ratingBar;
    String NameFilm, ReleaseDate, Popularity, Overview, Cover, Thumbnail, movieURL;
    int Id;
    double Rating;
    PeliculaModel peliculaModel;
    List<TrailerModel> trailerModel = new ArrayList<>();

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = findViewById(R.id.tvName);
        tvRating = findViewById(R.id.tvRating);
        tvRelease = findViewById(R.id.tvRelease);
        ratingBar = findViewById(R.id.ratingBar);
        imgCover = findViewById(R.id.imgCover);
        imgPhoto = findViewById(R.id.imgPhoto);
        imgFavorite = findViewById(R.id.imgFavorite);
        tvTitle = findViewById(R.id.tvTitle);


        tvOverview = findViewById(R.id.tvOverview);
        fabShare = findViewById(R.id.fabShare);


        // creamos el objeto de la bbdd
        dbHandler = new DBHandler(this);

        peliculaModel = (PeliculaModel) getIntent().getSerializableExtra("detailMovie");
        if (peliculaModel != null) {

            Id = peliculaModel.getId();
            NameFilm = peliculaModel.getTitle();
            Rating = peliculaModel.getVoteAverage();
            ReleaseDate = peliculaModel.getReleaseDate();
            Popularity = peliculaModel.getPopularity();
            Overview = peliculaModel.getOverview();
            Cover = peliculaModel.getBackdropPath();
            Thumbnail = peliculaModel.getPosterPath();
            movieURL = endpointApi.URLFILM + "" + Id;

            tvTitle.setText(NameFilm);
            tvName.setText(NameFilm);
            tvRating.setText(Rating + "/10");
            tvRelease.setText(ReleaseDate);
            tvOverview.setText(Overview);
            tvTitle.setSelected(true);
            tvName.setSelected(true);

            float newValue = (float)Rating;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setRating(newValue / 2);

            Glide.with(this)
                    .load(endpointApi.URLIMAGE + Cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            Glide.with(this)
                    .load(endpointApi.URLIMAGE + Thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);



            getTrailer();

        }

        imgFavorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            PeliculaModel peliculaModel = new PeliculaModel();
                            peliculaModel.setId(Id);
                            peliculaModel.setTitle(NameFilm);
                            peliculaModel.setVoteAverage(Rating);
                            peliculaModel.setOverview(Overview);
                            peliculaModel.setReleaseDate(ReleaseDate);
                            peliculaModel.setPosterPath(Thumbnail);
                            peliculaModel.setBackdropPath(Cover);
                            peliculaModel.setPopularity(Popularity);
//añade
                            dbHandler.addFavoriteMovie(peliculaModel);
                            Snackbar.make(buttonView, peliculaModel.getTitle() + " Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            dbHandler.deleteFavoriteMovie(Id);
                            Snackbar.make(buttonView, peliculaModel.getTitle() + " Removed from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
        );

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String subject = peliculaModel.getTitle();
                String description = peliculaModel.getOverview();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, subject + "\n\n" + description + "\n\n" + movieURL);
                startActivity(Intent.createChooser(shareIntent, "Abriendo :"));
            }
        });

    }

    private void getTrailer() {
        AndroidNetworking.get(endpointApi.BASEURL + endpointApi.MOVIE_VIDEO + endpointApi.APIKEY + endpointApi.LANGUAGE)
                .addPathParameter("id", String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            WebView webView = findViewById(R.id.webView);
                            TextView errorMessage = findViewById(R.id.error_message);

                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TrailerModel dataApi = new TrailerModel();
                                dataApi.setKey(jsonObject.getString("key"));
                                dataApi.setType(jsonObject.getString("type"));
                                trailerModel.add(dataApi);
                            }
                            // Si hay tráilers disponibles, reproduce el primero
                            if (!trailerModel.isEmpty()) {
                                String videoPath = "https://www.youtube.com/embed/" + trailerModel.get(0).getKey() + "?autoplay=1&vq=small";
                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.loadUrl(videoPath);
                                errorMessage.setVisibility(View.GONE);  // Oculta el mensaje de error si hay un trailer
                            } else {
                                // Si no hay tráilers, muestra el mensaje de error y establece el fondo de WebView como negro
                                errorMessage.setVisibility(View.VISIBLE);
                                webView.setBackgroundColor(Color.BLACK);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailMovieActivity.this, "sin datos!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DetailMovieActivity.this,
                                "Sin  internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
