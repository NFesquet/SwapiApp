package com.nfesquet.swapiapp.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nfesquet.swapiapp.R;
import com.nfesquet.swapiapp.model.Starship;
import com.nfesquet.swapiapp.service.ApiClient;
import com.nfesquet.swapiapp.service.SwapiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarshipDetailActivity extends AppCompatActivity {

    public static final String SPACESHIP_ID = "id";

    private Toolbar mToolbar;
    private View mProgressBar;
    private View mContentLayout;

    private TextView mTvStarshipModel;
    private TextView mTvStarshipManufacturer;
    private TextView mTvStarshipCost;
    private TextView mTvStarshipMaxSpeed;
    private TextView mTvStarshipCrew;
    private TextView mTvStarshipPassengers;
    private TextView mTvStarshipMglt;
    private TextView mTvStarshipClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starship_detail);

        mToolbar = (Toolbar) findViewById(R.id.starship_detail_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mProgressBar = findViewById(R.id.starship_detail_progress);
        mContentLayout = findViewById(R.id.starship_detail_content_layout);

        mTvStarshipModel = (TextView) findViewById(R.id.starship_detail_model);
        mTvStarshipManufacturer = (TextView) findViewById(R.id.starship_detail_manufacturer);
        mTvStarshipCost = (TextView) findViewById(R.id.starship_detail_cost);
        mTvStarshipMaxSpeed = (TextView) findViewById(R.id.starship_detail_max_speed);
        mTvStarshipCrew = (TextView) findViewById(R.id.starship_detail_crew);
        mTvStarshipPassengers = (TextView) findViewById(R.id.starship_detail_passengers);
        mTvStarshipMglt = (TextView) findViewById(R.id.starship_detail_mglt);
        mTvStarshipClass = (TextView) findViewById(R.id.starship_detail_class);

        int spaceshipId = 0;
        if (getIntent().hasExtra(SPACESHIP_ID)) {
            spaceshipId = getIntent().getExtras().getInt(SPACESHIP_ID);
        }

        loadValues(spaceshipId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadValues(int spaceshipId) {
        mProgressBar.setVisibility(View.VISIBLE);
        SwapiService service = ApiClient.getClient().create(SwapiService.class);

        service.starship(String.valueOf(spaceshipId)).enqueue(new Callback<Starship>() {
            @Override
            public void onResponse(Call<Starship> call, Response<Starship> response) {
                Starship starship = response.body();
                mToolbar.setTitle(starship.getName());
                mTvStarshipModel.setText(getString(R.string.starship_detail_format_model, starship.getModel()));
                mTvStarshipManufacturer.setText(getString(R.string.starship_detail_format_manufacturer, starship.getManufacturer()));
                mTvStarshipCost.setText(starship.getCostInCredits());
                mTvStarshipMaxSpeed.setText(getString(R.string.starship_detail_format_max_speed, starship.getMaxAtmospheringSpeed()));
                mTvStarshipCrew.setText(getString(R.string.starship_detail_format_crew, starship.getCrew()));
                mTvStarshipPassengers.setText(getString(R.string.starship_detail_format_passengers, starship.getPassengers()));
                mTvStarshipMglt.setText(getString(R.string.starship_detail_format_mglt, starship.getMglt()));
                mTvStarshipClass.setText(getString(R.string.starship_detail_format_class, starship.getStarshipClass()));
                mProgressBar.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Starship> call, Throwable t) {
                Toast.makeText(StarshipDetailActivity.this, "Loading spaceship failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
