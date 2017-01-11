package com.nfesquet.swapiapp.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfesquet.swapiapp.R;
import com.nfesquet.swapiapp.model.Starship;

import java.util.ArrayList;
import java.util.List;

public class StarshipsAdapter {/*extends IItemAdapter<Starship> {

    private static final int ITEM_TYPE = 0;
    private static final int BUTTON_TYPE = 1;

    private final List<Starship> mStarshipList;

    public StarshipsAdapter(View.OnClickListener loadNextListener) {
        super(loadNextListener);
        mStarshipList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = viewType == ITEM_TYPE ? R.layout.starship_item_layout : R.layout.load_next_layout;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == mStarshipList.size()) {
            if (!mHasNextPage) holder.loadNext.setVisibility(View.GONE);
            else holder.loadNext.setOnClickListener(mLoadNextListener);
        } else {
            Starship s = mStarshipList.get(position);
            holder.itemName.setText(s.getName());
            holder.itemManufacturer.setText(s.getManufacturer());
            holder.rootLayout.setOnClickListener(v -> onClickItem.onNext(mStarshipList.get(position).getUrl()));
        }
    }

    @Override
    public int getItemCount() {
        return mStarshipList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == mStarshipList.size() ? BUTTON_TYPE : ITEM_TYPE;
    }

    @Override
    public void addItems(List<Starship> starshipList) {
        mStarshipList.addAll(starshipList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup rootLayout;
        TextView itemName;
        TextView itemManufacturer;
        View loadNext;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == ITEM_TYPE) {
                rootLayout = (ViewGroup) itemView.findViewById(R.id.item_root_layout);
                itemName = (TextView) itemView.findViewById(R.id.starship_item_name);
                itemManufacturer = (TextView) itemView.findViewById(R.id.starship_item_manufacturer);
            } else {
                loadNext = itemView.findViewById(R.id.btn_load_next_root);
            }
        }
    }*/
}
