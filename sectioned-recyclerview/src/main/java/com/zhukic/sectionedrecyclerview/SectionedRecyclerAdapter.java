package com.zhukic.sectionedrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vladislav Zhukov (zhukic)
 */
public abstract class SectionedRecyclerAdapter<T, SH extends RecyclerView.ViewHolder, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_ITEM = 0;

    private List<T> mItemList;
    private Set<Integer> mSubheaderPositions = new HashSet<>();

    public SectionedRecyclerAdapter(List<T> itemList) {
        this.mItemList = itemList;
        initSubheaderPositions();
    }

    private void initSubheaderPositions() {
        mSubheaderPositions.clear();

        if(!mItemList.isEmpty()) {
            mSubheaderPositions.add(0);
        } else {
            return;
        }

        for(int i = 1; i < mItemList.size(); i++) {
            T t1 = mItemList.get(i - 1);
            T t2 = mItemList.get(i);
            if(onItems(t1, t2)) {
                mSubheaderPositions.add(i + mSubheaderPositions.size());
            }
        }
    }

    public abstract boolean onItems(T o1, T o2);

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract SH onCreateSubheaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(VH holder, int itemPosition);

    public abstract void onBindSubheaderViewHolder(SH subheaderHolder, int nextItemPosition);

    @Override
    public final int getItemViewType(int position) {
        if(isHeaderOnPosition(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            return onCreateItemViewHolder(parent, viewType);
        } else if(viewType == TYPE_HEADER) {
            return onCreateSubheaderViewHolder(parent, viewType);
        } else {
            throw new IllegalArgumentException("");
        }
    }

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
        return mItemList.size() + mSubheaderPositions.size();
    }

    public int getHeaderCount() {
        return mSubheaderPositions.size();
    }

    public Set<Integer> getSubheaderPositions() {
        return mSubheaderPositions;
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

}
