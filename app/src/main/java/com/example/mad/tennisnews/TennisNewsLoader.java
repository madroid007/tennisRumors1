package com.example.mad.tennisnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class TennisNewsLoader extends AsyncTaskLoader<List<TennisNews>> {
    private String mUrl ;
    private Context mContext ;

    public TennisNewsLoader(@NonNull Context context , String url) {
        super(context);
        mUrl = url ;
        mContext = context;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<TennisNews> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }

        // Perform the HTTP request for news data and process the response.
        List<TennisNews> tennisNew = new QueryUtils(mContext).fetchTennisNewsData(mUrl);

        return tennisNew;
    }
}
