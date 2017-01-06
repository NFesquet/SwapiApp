package com.nfesquet.swapiapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class IItemAdapter<T> extends RecyclerView.Adapter<StarshipsAdapter.ViewHolder> {

    private boolean mHasNextPage = true;

    private final PublishSubject<String> onClickItem = PublishSubject.create();

    final View.OnClickListener mLoadNextListener;

    IItemAdapter(View.OnClickListener loadNextListener){
        mLoadNextListener = loadNextListener;
    }

    @Override
    public void onBindViewHolder(StarshipsAdapter.ViewHolder holder, int position) {

    }

    public abstract void addItems(List<T> itemList);

    public Observable<String> getItemClicks() {
        return onClickItem.asObservable();
    }

    public void setHasNextPage(boolean hasNextPage) {
        mHasNextPage = hasNextPage;
    }

}
