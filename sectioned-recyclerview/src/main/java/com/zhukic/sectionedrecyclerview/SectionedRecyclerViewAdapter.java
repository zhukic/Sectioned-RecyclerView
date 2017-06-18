package com.zhukic.sectionedrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public abstract class SectionedRecyclerViewAdapter<SH extends RecyclerView.ViewHolder, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int TYPE_HEADER = -1;

    private SectionManager sectionManager = new SectionManager();

    public SectionedRecyclerViewAdapter() {}

    void initSubheaderPositions() {

        sectionManager.getSections().clear();

        if (getItemSize() != 0) {
            sectionManager.addSection(new Section(0));
        } else {
            return;
        }

        int lastSectionItemCount = getItemSize();

        for (int i = 1; i < getItemSize(); i++) {
            if (onPlaceSubheaderBetweenItems(i - 1)) {
                final Section section = new Section(i + sectionManager.getSections().size());
                final Section previousSection = sectionManager.getLastSection();
                final int sectionItemCount = section.getSubheaderPosition() - previousSection.getSubheaderPosition() - 1;
                previousSection.setItemsCount(sectionItemCount);
                sectionManager.addSection(section);
                lastSectionItemCount -= sectionItemCount;
            }
        }

        final Section lastSection = sectionManager.getLastSection();
        lastSection.setItemsCount(lastSectionItemCount);

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
        if (sectionManager.isSectionSubheaderOnPosition(position)) {
            return TYPE_HEADER;
        } else {
            //TODO check header view type
            return getViewType(position);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateSubheaderViewHolder(parent, viewType);
        } else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (sectionManager.isSectionSubheaderOnPosition(position)) {
            onBindSubheaderViewHolder((SH)holder, sectionManager.getItemPositionForSubheaderViewHolder(position));
        } else {
            onBindItemViewHolder((VH)holder, sectionManager.getItemPositionForItemViewHolder(position));
        }
    }

    @Override
    public final int getItemCount() {
        return sectionManager.getItemCount();
    }

    public final void notifyDataChanged() {
        initSubheaderPositions();
        notifyDataSetChanged();
    }

    public void notifyItemInsertedAtPosition(int itemPosition) {

        if (itemPosition == 0) {
            if (getItemCount() == 0 || onPlaceSubheaderBetweenItems(itemPosition)) {
                sectionManager.insertItem(0, true);
            } else {
                sectionManager.insertItem(1, false);
            }
        } else if (itemPosition == getItemSize() - 1) {
            if (onPlaceSubheaderBetweenItems(itemPosition - 1)) {
                sectionManager.insertItem(sectionManager.getItemCount(), true);
            } else {
                sectionManager.insertItem(sectionManager.getItemCount(), false);
            }
        } else {
            if (onPlaceSubheaderBetweenItems(itemPosition - 1) && onPlaceSubheaderBetweenItems(itemPosition)) {
                int itemAdapterPosition = sectionManager.getAdapterPositionForItem(itemPosition);
                sectionManager.insertItem(itemAdapterPosition, true);
            } else {
                sectionManager.insertItem(itemPosition, false);
            }
        }

        notifyDataChanged();

 /*       if (itemPosition == 0) {
           if (getItemCount() == 0 || onPlaceSubheaderBetweenItems(itemPosition)) {
               sectionManager.insertItem(0, true);
               notifyItemRangeInserted(0, 2);
           } else {
               sectionManager.insertItem(0, false);
               notifyItemInserted(1);
           }
        } else if (itemPosition == getItemSize() - 1) {
            if (onPlaceSubheaderBetweenItems(itemPosition - 1)) {
                sectionManager.insertItem(getItemCount() - 1, true);
                notifyItemRangeInserted(getItemCount() - 1, 2);
            } else {
                notifyItemInserted(sectionManager.getAdapterPositionForItem(itemPosition));
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
        }*/
    }

    public final void notifyItemChangedAtPosition(int itemPosition) {
        final int itemAdapterPosition = sectionManager.getAdapterPositionForItem(itemPosition);
        notifyItemChanged(itemAdapterPosition);
    }

    public void notifyItemRemovedAtPosition(int itemPosition) {

        final int itemAdapterPosition = sectionManager.getAdapterPositionForItem(itemPosition);
        final boolean isSectionRemoved = sectionManager.removeItem(itemAdapterPosition);

        if (isSectionRemoved) {
            notifyItemRangeRemoved(itemAdapterPosition - 1, 2);
        } else {
            notifyItemRemoved(itemAdapterPosition);
        }

    }

    public final void setGridLayoutManager(final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(sectionManager.isSectionSubheaderOnPosition(position)) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
    }

    public final boolean isSubheaderOnPosition(int adapterPosition) {
        return sectionManager.isSectionSubheaderOnPosition(adapterPosition);
    }

    public final void expandSection(int sectionPosition) {

        if (isSectionExpanded(sectionPosition)) {
            return;
        }

        int sectionSubheaderPosition = sectionManager.getSection(sectionPosition).getSubheaderPosition();

        notifyItemChanged(sectionSubheaderPosition);

        int insertedItemCount = sectionManager.expandSection(sectionPosition);

        notifyItemRangeInserted(sectionSubheaderPosition + 1, insertedItemCount);

    }

    public final void expandAllSections() {
        sectionManager.expandAllSections();
        notifyDataChanged();
    }

    public final void collapseSection(int sectionPosition) {

        if (!isSectionExpanded(sectionPosition)) {
            return;
        }

        int sectionSubheaderPosition = sectionManager.getSection(sectionPosition).getSubheaderPosition();

        notifyItemChanged(sectionSubheaderPosition);

        int removedItemCount = sectionManager.collapseSection(sectionPosition);

        notifyItemRangeRemoved(sectionSubheaderPosition + 1, removedItemCount);

    }

    public final void collapseAllSections() {
        sectionManager.collapseAllSections();
        notifyDataChanged();
    }

    public final boolean isSectionExpanded(int position) {
        return sectionManager.isSectionExpanded(position);
    }

    public final int getSectionIndex(int adapterPosition) {
        return sectionManager.sectionIndex(adapterPosition);
    }

    SectionManager getSectionManager() {
        return sectionManager;
    }

    void setSectionManager(SectionManager sectionManager) {
        this.sectionManager = sectionManager;
    }

}
