package br.com.agilles.filmesfamosos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.agilles.filmesfamosos.data.contract.DBContract;
import br.com.agilles.filmesfamosos.models.Movie;

/**
 * Created by jille on 24/01/2018.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "favoriteMovies.db";
    private final static int DATABASE_VERSION = 1;


    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTableSQL = "CREATE TABLE " + DBContract.FavoriteMoviesEntry.TABLE_NAME
                + " (" + DBContract.FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY + " INTEGER NOT NULL, " +
                DBContract.FavoriteMoviesEntry.COLUMN_NAME_TITLE + "  TEXT NOT NULL, " +
                DBContract.FavoriteMoviesEntry.COLUMN_NAME_FAVORITE + " INTEGER NOT NULL);";
        Log.v("SQLITE_MENSAGEM", createTableSQL);
        db.execSQL(createTableSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(db);
    }

    public boolean exists(Movie movie) {
        SQLiteDatabase db = getReadableDatabase();
        String sqlExiste = "SELECT " + DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY +
                " FROM " + DBContract.FavoriteMoviesEntry.TABLE_NAME +
                " where " + DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY +
                "=? LIMIT 1";
        Cursor cursor = db.rawQuery(sqlExiste, new String[]{movie.getId().toString()});
        int quantidade = cursor.getCount();
        return quantidade > 0;
    }

    public void sync(List<Movie> moviesList) {
        for (Movie m : moviesList) {
            if (exists(m)) {
                update(m);
            } else {
                insert(m);
            }
        }
    }

    public void insert(Movie m) {
        SQLiteDatabase db = getWritableDatabase();
        if (!exists(m)) {
            ContentValues cv = getMoviesInfo(m);
            db.insert(DBContract.FavoriteMoviesEntry.TABLE_NAME, null, cv);
        } else {
            update(m);
        }


    }

    public void update(Movie m) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = getMoviesInfo(m);
        String params[] = {m.getId().toString()};
        db.update(DBContract.FavoriteMoviesEntry.TABLE_NAME, cv, DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY + "=?", params);


    }

    private ContentValues getMoviesInfo(Movie m) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.FavoriteMoviesEntry.COLUMN_NAME_TITLE, m.getTitle());
        cv.put(DBContract.FavoriteMoviesEntry.COLUMN_NAME_FAVORITE, m.isFavorite() ? 1 : 0);
        cv.put(DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY, m.getId());
        return cv;

    }

    public List<Movie> allMoviesList() {
        String sql = "SELECT * FROM " + DBContract.FavoriteMoviesEntry.TABLE_NAME + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Movie> movies = changeFavoriteMoviesButton(c);
        c.close();
        return movies;

    }

    private List<Movie> changeFavoriteMoviesButton(Cursor c) {
        List<Movie> movieList = new ArrayList<>();
        while (c.moveToNext()) {
            Movie movie = new Movie();
            int favorite = c.getInt(c.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_FAVORITE));
            movie.setId(c.getLong(c.getColumnIndex(DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY)));

            if (favorite == 1) {
                movie.setFavorite(true);
            }
            movieList.add(movie);

        }
        return movieList;
    }


    public void remove(Movie m) {
        SQLiteDatabase db = getWritableDatabase();
        String params[] = {m.getId().toString()};
        db.delete(DBContract.FavoriteMoviesEntry.TABLE_NAME, DBContract.FavoriteMoviesEntry.COLUMN_NAME_KEY + "=?", params);

    }
}
