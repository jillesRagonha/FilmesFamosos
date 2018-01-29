package br.com.agilles.filmesfamosos.data.contract;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jille on 28/01/2018.
 */

public class ContentProviderContract implements BaseColumns {


    public static final String CONTENT_AUTHORITY = "br.com.agilles.filmesfamosos";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();




}
