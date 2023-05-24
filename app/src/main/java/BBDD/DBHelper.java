package BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carlos Chica.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db_movie";

    // Table Names
    public static final String TABLE_MOVIES = "movies";
    public static final String TABLE_TV = "tv";

    // Column names
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NAME = "name";  // this is for TV Shows
    public static final String KEY_VOTE_AVERAGE = "vote_average";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_RELEASE_DATE = "release_date";
    public static final String KEY_POSTER_PATH = "poster_path";
    public static final String KEY_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_POPULARITY = "popularity";


    private static final String CREATE_TABLE_MOVIES = "CREATE TABLE "
            + TABLE_MOVIES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_VOTE_AVERAGE + " REAL,"
            + KEY_OVERVIEW + " TEXT,"
            + KEY_RELEASE_DATE + " TEXT,"
            + KEY_POSTER_PATH + " TEXT,"
            + KEY_BACKDROP_PATH + " TEXT,"
            + KEY_POPULARITY + " TEXT"
            + ")";

    private static final String CREATE_TABLE_TV_SHOWS = "CREATE TABLE "
            + TABLE_TV + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_VOTE_AVERAGE + " REAL,"
            + KEY_OVERVIEW + " TEXT,"
            + KEY_RELEASE_DATE + " TEXT,"
            + KEY_POSTER_PATH + " TEXT,"
            + KEY_BACKDROP_PATH + " TEXT,"
            + KEY_POPULARITY + " TEXT"
            + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIES);
        db.execSQL(CREATE_TABLE_TV_SHOWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TV);
        onCreate(db);
    }
}
