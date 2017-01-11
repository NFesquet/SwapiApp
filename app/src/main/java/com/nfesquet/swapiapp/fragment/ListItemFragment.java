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
import com.nfesquet.swapiapp.adapter.ItemViewModel;
import com.nfesquet.swapiapp.model.ApiResult;
import com.nfesquet.swapiapp.model.SwapiModel;
import com.nfesquet.swapiapp.service.ApiClient;
import com.nfesquet.swapiapp.service.SwapiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListItemFragment<U extends ItemViewModel> extends Fragment {

    private static final String ITEM_VM_KEY = "itemVmKey";
    private static SwapiService mService;

    private RecyclerView mListView;
    private IItemAdapter<U> mAdapter;
    private ProgressBar mProgressBar;
    private ItemViewModel mItemViewModel;

    private String nextPage;

    public static ListItemFragment newInstance(ItemViewModel itemVm) {

        Bundle args = new Bundle();
        args.putParcelable(ITEM_VM_KEY, itemVm);

        ListItemFragment fragment = new ListItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemViewModel = getArguments().getParcelable(ITEM_VM_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.content_main, null);

        mListView = (RecyclerView) root.findViewById(R.id.main_layout_list_view);
        mProgressBar = (ProgressBar) root.findViewById(R.id.main_layout_progress_bar);

        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new IItemAdapter<>(this::loadNext);
        mAdapter.getItemClicks().subscribe(this::openDetails);
        mListView.setAdapter(mAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static SwapiService getService() {
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
        mItemViewModel.getService(page).enqueue(new Callback<ApiResult<? extends SwapiModel>>() {
            @Override
            public void onResponse(Call<ApiResult<? extends SwapiModel>> call, Response<ApiResult<? extends SwapiModel>> response) {
                ApiResult<? extends U> bodyResponse = (ApiResult<? extends U>) response.body();
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
            public void onFailure(Call<ApiResult<? extends SwapiModel>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.loading_error, Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
