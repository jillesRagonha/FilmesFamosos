package br.com.agilles.filmesfamosos.data.contract;

import android.provider.BaseColumns;

/**
 * Created by jille on 24/01/2018.
 */

public class DBContract {
    private DBContract() {

    }

    public class FavoriteMoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "Favorites_Movies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_KEY = "key";
        public static final String COLUMN_NAME_FAVORITE = "favorite";


    }
}
