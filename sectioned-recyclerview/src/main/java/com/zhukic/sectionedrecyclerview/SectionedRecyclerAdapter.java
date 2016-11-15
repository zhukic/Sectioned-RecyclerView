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

    public static final String TAG = SectionedRecyclerAdapter.class.getSimpleName();

    public static final int TYPE_HEADER = -1;

    private List<Integer> mSubheaderPositions = new ArrayList<>();

    public SectionedRecyclerAdapter() { }

    private void initSubheaderPositions() {
        mSubheaderPositions.clear();

        if(getItemSize() != 0) {
            mSubheaderPositions.add(0);
        } else {
            return;
        }

        for(int i = 1; i < getItemSize(); i++) {
            if(onItems(i - 1, i)) {
                mSubheaderPositions.add(i + mSubheaderPositions.size());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        initSubheaderPositions();
    }

    public abstract boolean onItems(int position1, int position2);

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract SH onCreateSubheaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(VH holder, int itemPosition);

    public abstract void onBindSubheaderViewHolder(SH subheaderHolder, int nextItemPosition);

    public abstract int getItemSize();

    public int getViewType(int position) {
        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        if(isHeaderOnPosition(position)) {
            return TYPE_HEADER;
        } else {
            return getViewType(position);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
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
        return getItemSize() + mSubheaderPositions.size();
    }

    public void setGridLayoutManager(final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mSubheaderPositions.contains(position)) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
    }

    private boolean isHeaderOnPosition(int position) {
        return mSubheaderPositions.contains(position);
    }

    private int getCountOfSubheadersBeforePosition(int position) {
        int count = 0;
        for(int subheaderPosition : mSubheaderPositions) {
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
        return mSubheaderPositions.size();
    }


}
