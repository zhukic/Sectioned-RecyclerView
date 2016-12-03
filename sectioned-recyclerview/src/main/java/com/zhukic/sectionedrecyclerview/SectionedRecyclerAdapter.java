package com.zhukic.sectionedrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladislav Zhukov (zhukic)
 */
public abstract class SectionedRecyclerAdapter<SH extends RecyclerView.ViewHolder, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_HEADER = -1;

    private List<Integer> subheaderPositions;

    public SectionedRecyclerAdapter() {
        subheaderPositions = new ArrayList<>();
    }

    private void initSubheaderPositions() {
        subheaderPositions.clear();

        if(getCount() != 0) {
            subheaderPositions.add(0);
        } else {
            return;
        }

        for(int i = 1; i < getCount(); i++) {
            if(onPlaceSubheaderBetweenItems(i - 1, i)) {
                subheaderPositions.add(i + subheaderPositions.size());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        initSubheaderPositions();
    }

    /**
     * Called when adapter needs to know whether to place subheader between two neighboring
     * items.
     *
     * @return true if you want to place subheader between two neighboring
     * items.
     */
    public abstract boolean onPlaceSubheaderBetweenItems(int itemPosition, int nextItemPosition);

    public abstract SH onCreateSubheaderViewHolder(ViewGroup parent, int viewType);

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindSubheaderViewHolder(SH subheaderHolder, int nextItemPosition);

    public abstract void onBindItemViewHolder(VH holder, int itemPosition);

    public abstract int getCount();

    public int getViewType(int position) {
        return 0;
    }


    /**
     * Return the view type of the item at position for the purposes
     * of view recycling.
     * Don't return -1. It's reserved for subheader view type.
     */
    @Override
    public final int getItemViewType(int position) {
        if(isHeaderOnPosition(position)) {
            return VIEW_TYPE_HEADER;
        } else {
            return getViewType(position);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_HEADER) {
            return onCreateSubheaderViewHolder(parent, viewType);
        } else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked_cast")
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isHeaderOnPosition(position)) {
            onBindSubheaderViewHolder((SH)holder, getItemPositionForViewHolder(position));
        } else {
            onBindItemViewHolder((VH)holder, getItemPositionForViewHolder(position));
        }
    }

    @Override
    public final int getItemCount() {
        return getCount() + subheaderPositions.size();
    }

    public void setGridLayoutManager(final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(subheaderPositions.contains(position)) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
    }

    private boolean isHeaderOnPosition(int position) {
        return subheaderPositions.contains(position);
    }

    private int getCountOfSubheadersBeforePosition(int position) {
        int count = 0;
        for(int subheaderPosition : subheaderPositions) {
            if(subheaderPosition < position) {
                count++;
            }
        }
        return count;
    }

    private int getItemPositionForViewHolder(int viewHolderPosition) {
        return viewHolderPosition - getCountOfSubheadersBeforePosition(viewHolderPosition);
    }

    public int getHeaderCount() {
        return subheaderPositions.size();
    }


}
