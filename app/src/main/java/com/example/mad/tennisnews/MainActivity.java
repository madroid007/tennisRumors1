package com.example.mad.tennisnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<TennisNews>> , NewsRecyclerViewAdapter.ItemClickListener {


    /** URL for news from the GARDIAN API */
    private  String GUARDIAN_REQUEST_URL ;
    private static final int LOADER_ID = 1 ;



    private NewsRecyclerViewAdapter mAdapter ;

    @BindView(R.id.empty_state_text_view)
    TextView emptyStateTV;

    @BindView(R.id.progress_bar)
    ProgressBar loadingBar;

    @BindView(R.id.list)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** URL for news from the GARDIAN API */

        GUARDIAN_REQUEST_URL = getString(R.string.guardian_main_url_);

        QueryUtils queryUtils = new QueryUtils(this);

        ButterKnife.bind(this);

        // making empty state visibility to gone
        emptyStateTV.setVisibility(View.GONE);

        // making it visible
        loadingBar.setVisibility(View.VISIBLE);


        // check for connectivity

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null) {


            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            //check for connectivity
            if (networkInfo!=null && networkInfo.isConnectedOrConnecting()){
                //if connected then init the loader

                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter
                getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            }else {
                // if not connected then make progress bar disappear and set empty state to no network
                loadingBar.setVisibility(View.GONE);
                emptyStateTV.setVisibility(View.VISIBLE);
                emptyStateTV.setText(R.string.no_network_c);
            }
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a new adapter that takes the list of tennisNews as input
        mAdapter = new NewsRecyclerViewAdapter(this, new ArrayList<TennisNews>());
        // make main activity listen to item clicks
        mAdapter.setClickListener(this);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        recyclerView.setAdapter(mAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            startActivity(new Intent(this,SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<List<TennisNews>> onCreateLoader(int i, @Nullable Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `q=tennis'`
        uriBuilder.appendQueryParameter(getString(R.string.q_), getString(R.string.tennis));
        uriBuilder.appendQueryParameter(getString(R.string.order_by), orderBy);
        uriBuilder.appendQueryParameter(getString(R.string.api_key),getString(R.string.test));

        // Return the completed uri https://content.guardianapis.com/search?q=tennis&order-by=orderBy&api-key=test
        return new TennisNewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<TennisNews>> loader, List<TennisNews> tennisNews) {

        loadingBar.setVisibility(View.GONE);

        emptyStateTV.setText(R.string.no_tennis_news);

        // If there is a valid list of news, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (tennisNews != null && !tennisNews.isEmpty()) {
        //    methode in recycle adapter to add new data to the adapter
            mAdapter.swapList(tennisNews);
        }else {
            // if there is no news the set empty state to visible
            emptyStateTV.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<TennisNews>> loader) {

      //  remove all views from recycle view;
        recyclerView.removeAllViews();
        mAdapter.notifyDataSetChanged();

    }


    // on click listener to the recycle view item click

    @Override
    public void onItemClick(View view, int position , List<TennisNews> newsList ) {


       //  Find the current news that was clicked on
                TennisNews currentTennisNews = newsList.get(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri tennisNewsUri = Uri.parse(currentTennisNews.getUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, tennisNewsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

    }
}
