package com.nfesquet.swapiapp.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nfesquet.swapiapp.R;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class IItemAdapter<T extends ItemViewModel> extends RecyclerView.Adapter<IItemAdapter.ViewHolder> {

    private List<T> mItems;

    private boolean mHasNextPage = true;

    private final PublishSubject<String> onClickItem = PublishSubject.create();

    private final View.OnClickListener mLoadNextListener;

    public IItemAdapter(View.OnClickListener loadNextListener) {
        mLoadNextListener = loadNextListener;
    }

    public IItemAdapter(List<T> items, View.OnClickListener loadNextListener){
        this(loadNextListener);
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding bind = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        return new ViewHolder<>(bind);
    }

    @Override
    public void onBindViewHolder(IItemAdapter.ViewHolder holder, int position) {
        if (position == mItems.size()) {
//            if (!mHasNextPage) holder.loadNext.setVisibility(View.GONE);
//            else holder.loadNext.setOnClickListener(mLoadNextListener);
        } else {
            final T model = mItems.get(position);
//        holder.getBinding().setVariable(BR.model, model);
            holder.getBinding().executePendingBindings();
            holder.v.getRoot().setOnClickListener(v -> onClickItem.onNext(model.getUrl()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).layoutId();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItems(List<? extends T> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public Observable<String> getItemClicks() {
        return onClickItem.asObservable();
    }

    public void setHasNextPage(boolean hasNextPage) {
        mHasNextPage = hasNextPage;
    }

    static class ViewHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private V v;
//        private View loadNext;

        ViewHolder(V v) {
            super(v.getRoot());
            this.v = v;
//            this.loadNext = v.getRoot().findViewById(R.id.btn_load_next_root);
        }

        V getBinding() {
            return v;
        }
    }

}
