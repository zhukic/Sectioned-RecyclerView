package com.zhukic.sectionedrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */
public abstract class SectionedRecyclerViewAdapter<SH extends RecyclerView.ViewHolder, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = SectionedRecyclerViewAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = -1;

    private List<Integer> subheaderPositions = new ArrayList<>();

    public SectionedRecyclerViewAdapter() { }

    private void initSubheaderPositions() {
        subheaderPositions.clear();

        if(getItemSize() != 0) {
            subheaderPositions.add(0);
        } else {
            return;
        }

        for(int i = 1; i < getItemSize(); i++) {
            if(onPlaceSubheaderBetweenItems(i - 1)) {
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
    public abstract boolean onPlaceSubheaderBetweenItems(int position);

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract SH onCreateSubheaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(VH holder, int itemPosition);

    public abstract void onBindSubheaderViewHolder(SH subheaderHolder, int nextItemPosition);

    public abstract int getItemSize();

    /**
     * Return the view type of the item at position for the purposes
     * of view recycling.
     * Don't return -1. It's reserved for subheader view type.
     */
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        if(isSubheaderOnPosition(position)) {
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

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isSubheaderOnPosition(position)) {
            onBindSubheaderViewHolder((SH)holder, getItemPositionForViewHolder(position));
        } else {
            onBindItemViewHolder((VH)holder, getItemPositionForViewHolder(position));
        }
    }

    @Override
    public final int getItemCount() {
        return getItemSize() + subheaderPositions.size();
    }

    public void notifyDataChanged() {
        initSubheaderPositions();
        notifyDataSetChanged();
    }

    public void notifyItemInsertedAtPosition(int itemPosition) {
        if (itemPosition == 0) {
           if (getItemCount() == 1 || onPlaceSubheaderBetweenItems(itemPosition)) {
               subheaderPositions.add(0, 0);
               increaseSubheaderPositions(1, 2);
               notifyItemRangeInserted(0, 2);
           } else {
               increaseSubheaderPositions(1, 1);
               notifyItemInserted(1);
           }
        } else if (itemPosition == getItemSize() - 1) {
            if (onPlaceSubheaderBetweenItems(itemPosition - 1)) {
                subheaderPositions.add(getItemCount() - 1);
                notifyItemRangeInserted(getItemCount() - 1, 2);
            } else {
                notifyItemInserted(getItemPositionInRecyclerView(itemPosition));
            }
        } else {
            if (onPlaceSubheaderBetweenItems(itemPosition - 1) && onPlaceSubheaderBetweenItems(itemPosition)) {
                final int itemPositionInRv = getItemPositionInRecyclerView(itemPosition - 1);
                final int countOfSubheadersBeforePosition = getCountOfSubheadersBeforePosition(itemPositionInRv);
                subheaderPositions.add(countOfSubheadersBeforePosition, itemPositionInRv + 1);
                increaseSubheaderPositions(countOfSubheadersBeforePosition + 1, 2);
                notifyItemRangeInserted(itemPositionInRv + 1, 2);
            } else if (onPlaceSubheaderBetweenItems(itemPosition)){
                final int itemPositionInRv = getItemPositionInRecyclerView(itemPosition - 1);
                increaseSubheaderPositions(getCountOfSubheadersBeforePosition(itemPositionInRv), 1);
                notifyItemInserted(itemPositionInRv + 1);
            } else if (onPlaceSubheaderBetweenItems(itemPosition - 1)) {
                final int itemPositionInRv = getItemPositionInRecyclerView(itemPosition);
                increaseSubheaderPositions(getCountOfSubheadersBeforePosition(itemPositionInRv), 1);
                notifyItemInserted(itemPositionInRv);
            } else {
                final int itemPositionInRv = getItemPositionInRecyclerView(itemPosition);
                increaseSubheaderPositions(getCountOfSubheadersBeforePosition(itemPositionInRv), 1);
                notifyItemInserted(itemPositionInRv);
            }
        }
    }

    public void notifyItemChangedAtPosition(int itemPosition) {
        final int itemPositionInRv = getItemPositionInRecyclerView(itemPosition);
        notifyItemChanged(itemPositionInRv);
    }

    public void notifyItemRemovedAtPosition(int itemPosition) {

        final int itemPositionInRv = getItemPositionInRecyclerView(itemPosition);

        for (int i = 1; i < subheaderPositions.size(); i++) {
            final int subheaderPosition = subheaderPositions.get(i);
            if (subheaderPosition > itemPositionInRv) {
                final int previousSubheaderPosition = subheaderPositions.get(i - 1);
                if (subheaderPosition - previousSubheaderPosition == 2) {
                    subheaderPositions.remove(subheaderPositions.indexOf(previousSubheaderPosition));
                    decreaseSubheaderPositions(subheaderPositions.indexOf(subheaderPosition), 2);
                    notifyItemRangeRemoved(itemPositionInRv - 1, 2);
                } else {
                    decreaseSubheaderPositions(subheaderPositions.indexOf(subheaderPosition), 1);
                    notifyItemRemoved(itemPositionInRv);
                }
                return;
            }
        }

        final int lastSubheaderPosition = subheaderPositions.get(subheaderPositions.size() - 1);
        if (itemPositionInRv - lastSubheaderPosition == 1 && getItemCount() == itemPosition + subheaderPositions.size()) {
            subheaderPositions.remove(subheaderPositions.size() - 1);
            notifyItemRangeRemoved(itemPositionInRv - 1, 2);
        } else {
            notifyItemRemoved(itemPositionInRv);
        }
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

    public boolean isSubheaderOnPosition(int position) {
        return subheaderPositions.contains(position);
    }

    public int getCountOfSubheadersBeforePosition(int position) {
        int count = 0;
        for(int subheaderPosition : subheaderPositions) {
            if(subheaderPosition < position) {
                count++;
            }
        }
        return count;
    }

    public int getItemPositionInRecyclerView(int position) {
        int countOfItems = 0;
        for (int i = 1; i < subheaderPositions.size(); i++) {
            final int previousSubheaderPosition = subheaderPositions.get(i - 1);
            final int nextSubheaderPosition = subheaderPositions.get(i);
            countOfItems += nextSubheaderPosition - previousSubheaderPosition - 1;
            if (countOfItems > position) {
                return position + i;
            }
        }
        return position + subheaderPositions.size();
    }

    public int getItemPositionForViewHolder(int viewHolderPosition) {
        return viewHolderPosition - getCountOfSubheadersBeforePosition(viewHolderPosition);
    }

    private void decreaseSubheaderPositions(int startSubheaderPosition, int decreaseNum) {
        for (int i = startSubheaderPosition; i < subheaderPositions.size(); i++) {
            final int subheaderPosition = subheaderPositions.get(i);
            subheaderPositions.set(i, subheaderPosition - decreaseNum);
        }
    }

    private void increaseSubheaderPositions(int startSubheaderPosition, int increaseNum) {
        for (int i = startSubheaderPosition; i < subheaderPositions.size(); i++) {
            final int subheaderPosition = subheaderPositions.get(i);
            subheaderPositions.set(i, subheaderPosition + increaseNum);
        }
    }

    private List<Integer> getSubheaderPositions() {
        return subheaderPositions;
    }

    public int getSubheaderCount() {
        return subheaderPositions.size();
    }

}
