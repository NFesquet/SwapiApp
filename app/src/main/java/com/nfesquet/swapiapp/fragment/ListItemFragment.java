package com.nfesquet.swapiapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nfesquet.swapiapp.R;
import com.nfesquet.swapiapp.activity.StarshipDetailActivity;
import com.nfesquet.swapiapp.adapter.IItemAdapter;
import com.nfesquet.swapiapp.adapter.StarshipsAdapter;
import com.nfesquet.swapiapp.model.ApiResult;
import com.nfesquet.swapiapp.model.Starship;
import com.nfesquet.swapiapp.service.ApiClient;
import com.nfesquet.swapiapp.service.SwapiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListItemFragment<T, U extends IItemAdapter<T>> extends Fragment {

    private SwapiService mService;

    private RecyclerView mListView;
    private U mAdapter;
    private ProgressBar mProgressBar;

    private String nextPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.content_main, null);

        mListView = (RecyclerView) root.findViewById(R.id.main_layout_list_view);
        mProgressBar = (ProgressBar) root.findViewById(R.id.main_layout_progress_bar);

        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new StarshipsAdapter(this::loadNext);
        mAdapter.getItemClicks().subscribe(this::openDetails);
        mListView.setAdapter(mAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
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

        Intent i = new Intent(getActivity(), StarshipDetailActivity.class);
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
        getService().listStarships(page).enqueue(new Callback<ApiResult<T>>() {
            @Override
            public void onResponse(Call<ApiResult<T>> call, Response<ApiResult<T>> response) {
                ApiResult<T> bodyResponse = response.body();
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
            public void onFailure(Call<ApiResult<T>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.loading_error, Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}
