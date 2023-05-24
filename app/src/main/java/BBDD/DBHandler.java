package BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import modelos.PeliculaModel;
import modelos.SerieModel;

/**
 * Created by Carlos Chica.
 */
public class DBHandler {
    private DBHelper dbHelper;

    public DBHandler(Context context){
        dbHelper = new DBHelper(context);
    }

    public void addFavoriteMovie(PeliculaModel movie) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ID, movie.getId());
        values.put(DBHelper.KEY_TITLE, movie.getTitle());
        values.put(DBHelper.KEY_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(DBHelper.KEY_OVERVIEW, movie.getOverview());
        values.put(DBHelper.KEY_RELEASE_DATE, movie.getReleaseDate());
        values.put(DBHelper.KEY_POSTER_PATH, movie.getPosterPath());
        values.put(DBHelper.KEY_BACKDROP_PATH, movie.getBackdropPath());
        values.put(DBHelper.KEY_POPULARITY, movie.getPopularity());

        db.insert(DBHelper.TABLE_MOVIES, null, values);
        db.close();
    }

    public void addFavoriteTV(SerieModel tvShow) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ID, tvShow.getId());

        values.put(DBHelper.KEY_VOTE_AVERAGE, tvShow.getVoteAverage());
        values.put(DBHelper.KEY_OVERVIEW, tvShow.getOverview());
        values.put(DBHelper.KEY_RELEASE_DATE, tvShow.getReleaseDate());
        values.put(DBHelper.KEY_POSTER_PATH, tvShow.getPosterPath());
        values.put(DBHelper.KEY_BACKDROP_PATH, tvShow.getBackdropPath());
        values.put(DBHelper.KEY_POPULARITY, tvShow.getPopularity());

        db.insert(DBHelper.TABLE_TV, null, values);
        db.close();
    }

    public ArrayList<PeliculaModel> showFavoriteMovie() {
        ArrayList<PeliculaModel> movieList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_MOVIES;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PeliculaModel movie = new PeliculaModel();
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                if(idIndex != -1) {
                    movie.setId(cursor.getInt(idIndex));
                }

                int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                if(titleIndex != -1) {
                    movie.setTitle(cursor.getString(titleIndex));
                }

                int voteIndex = cursor.getColumnIndex(DBHelper.KEY_VOTE_AVERAGE);
                if(voteIndex != -1) {
                    movie.setVoteAverage(cursor.getDouble(voteIndex));
                }

                int overviewIndex = cursor.getColumnIndex(DBHelper.KEY_OVERVIEW);
                if(overviewIndex != -1) {
                    movie.setOverview(cursor.getString(overviewIndex));
                }

                int releaseDateIndex = cursor.getColumnIndex(DBHelper.KEY_RELEASE_DATE);
                if(releaseDateIndex != -1) {
                    movie.setReleaseDate(cursor.getString(releaseDateIndex));
                }

                int posterPathIndex = cursor.getColumnIndex(DBHelper.KEY_POSTER_PATH);
                if(posterPathIndex != -1) {
                    movie.setPosterPath(cursor.getString(posterPathIndex));
                }

                int backdropPathIndex = cursor.getColumnIndex(DBHelper.KEY_BACKDROP_PATH);
                if(backdropPathIndex != -1) {
                    movie.setBackdropPath(cursor.getString(backdropPathIndex));
                }

                int popularityIndex = cursor.getColumnIndex(DBHelper.KEY_POPULARITY);
                if(popularityIndex != -1) {
                    movie.setPopularity(cursor.getString(popularityIndex));
                }

                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return movieList;
    }

    public ArrayList<SerieModel> showFavoriteTV() {
        ArrayList<SerieModel> tvShowList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_TV;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SerieModel tvShow = new SerieModel();
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                if(idIndex != -1) {
                    tvShow.setId(cursor.getInt(idIndex));
                }

                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                if(nameIndex != -1) {
                    tvShow.setName(cursor.getString(nameIndex));
                }

                int voteIndex = cursor.getColumnIndex(DBHelper.KEY_VOTE_AVERAGE);
                if(voteIndex != -1) {
                    tvShow.setVoteAverage(cursor.getDouble(voteIndex));
                }

                int overviewIndex = cursor.getColumnIndex(DBHelper.KEY_OVERVIEW);
                if(overviewIndex != -1) {
                    tvShow.setOverview(cursor.getString(overviewIndex));
                }

                int releaseDateIndex = cursor.getColumnIndex(DBHelper.KEY_RELEASE_DATE);
                if(releaseDateIndex != -1) {
                    tvShow.setReleaseDate(cursor.getString(releaseDateIndex));
                }

                int posterPathIndex = cursor.getColumnIndex(DBHelper.KEY_POSTER_PATH);
                if(posterPathIndex != -1) {
                    tvShow.setPosterPath(cursor.getString(posterPathIndex));
                }

                int backdropPathIndex = cursor.getColumnIndex(DBHelper.KEY_BACKDROP_PATH);
                if(backdropPathIndex != -1) {
                    tvShow.setBackdropPath(cursor.getString(backdropPathIndex));
                }

                int popularityIndex = cursor.getColumnIndex(DBHelper.KEY_POPULARITY);
                if(popularityIndex != -1) {
                    tvShow.setPopularity(cursor.getString(popularityIndex));
                }

                tvShowList.add(tvShow);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tvShowList;
    }


    public void deleteFavoriteMovie(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_MOVIES, DBHelper.KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    public void deleteFavoriteTV(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_TV, DBHelper.KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }

}
