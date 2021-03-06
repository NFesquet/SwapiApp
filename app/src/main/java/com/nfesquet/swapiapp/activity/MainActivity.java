package com.nfesquet.swapiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nfesquet.swapiapp.R;
import com.nfesquet.swapiapp.adapter.StarshipsAdapter;
import com.nfesquet.swapiapp.model.ApiResult;
import com.nfesquet.swapiapp.model.Starship;
import com.nfesquet.swapiapp.service.ApiClient;
import com.nfesquet.swapiapp.service.SwapiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SwapiService mService;

    private RecyclerView mListView;
    private StarshipsAdapter mAdapter;
    private ProgressBar mProgressBar;

    private String nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_layout_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mListView = (RecyclerView) findViewById(R.id.main_layout_list_view);
        mProgressBar = (ProgressBar) findViewById(R.id.main_layout_progress_bar);

        mListView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new StarshipsAdapter(this::loadNext);
        mAdapter.getItemClicks().subscribe(this::openDetails);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPage(1, null);
    }

    private SwapiService getService() {
        if (mService == null) {
            mService = ApiClient.getClient().create(SwapiService.class);
        }
        return mService;
    }

    private void loadNext(View v) {
        if (nextPage != null) {
            loadPage(getIndexFromUrl(nextPage), v);
        }
    }

    private void openDetails(String url) {
        int spaceshipId = getIndexFromUrl(url);

        Intent i = new Intent(MainActivity.this, StarshipDetailActivity.class);
        i.putExtra(StarshipDetailActivity.SPACESHIP_ID, spaceshipId);
        startActivity(i);
    }

    private int getIndexFromUrl(String url) {
        String nextIndexString = url.replaceAll("\\D+", "");
        try {
            return Integer.parseInt(nextIndexString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return 0;
    }

    private void loadPage(int page, View v) {
        ProgressBar loadNextProgressBar = null;
        TextView loadNextTextView = null;

        if (v != null) {
            loadNextProgressBar = (ProgressBar) v.findViewById(R.id.btn_load_next_progress);
            loadNextTextView = (TextView) v.findViewById(R.id.btn_load_next_text);

            loadNextProgressBar.setVisibility(View.VISIBLE);
            loadNextTextView.setVisibility(View.GONE);
        }

        TextView finalLoadNextTextView = loadNextTextView;
        ProgressBar finalLoadNextProgressBar = loadNextProgressBar;
        getService().listStarships(page).enqueue(new Callback<ApiResult<Starship>>() {
            @Override
            public void onResponse(Call<ApiResult<Starship>> call, Response<ApiResult<Starship>> response) {
                ApiResult<Starship> bodyResponse = response.body();
                nextPage = bodyResponse.getNext();
                mAdapter.addItems(bodyResponse.getResults());
                if (nextPage == null) mAdapter.setHasNextPage(false);

                mProgressBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                if (finalLoadNextProgressBar != null && finalLoadNextTextView != null) {
                    finalLoadNextProgressBar.setVisibility(View.GONE);
                    finalLoadNextTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResult<Starship>> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.loading_error, Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_spaceships) {
            // Handle the camera action
        } else if (id == R.id.nav_films) {

        } else if (id == R.id.nav_people) {

        } else if (id == R.id.nav_planets) {

        } else if (id == R.id.nav_species) {

        } else if (id == R.id.nav_vehicles) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
