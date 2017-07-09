package com.zhukic.sectionedrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public abstract class SectionedRecyclerViewAdapter<SH extends RecyclerView.ViewHolder, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_HEADER = -1;

    private SectionManager sectionManager;

    public SectionedRecyclerViewAdapter() {
        this.sectionManager = new SectionManager();
    }

    SectionedRecyclerViewAdapter(SectionManager sectionManager) {
        this.sectionManager = sectionManager;
    }

    void initSubheaderPositions() {

        sectionManager.clear();

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
     * @param position position of the first item to be compared.
     * @return true if you want to place subheader between two neighboring
     * items.
     */
    public abstract boolean onPlaceSubheaderBetweenItems(int position);

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract SH onCreateSubheaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(VH holder, int itemPosition);

    /**
     * Called to display the data for the subheader at the specified position.
     *
     * @param nextItemPosition position of the first element in your data set
     *                         that belongs to the section corresponding to {@code subheaderHolder}.
     */
    public abstract void onBindSubheaderViewHolder(SH subheaderHolder, int nextItemPosition);

    /**
     * Returns the total number of items in your data set.
     *
     * @return the total number of items in your data set.
     */
    public abstract int getItemSize();

    /**
     * Don't return {@link SectionedRecyclerViewAdapter#TYPE_HEADER}.
     * It's reserved for subheader view type.
     */
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        if (sectionManager.isSectionSubheaderOnPosition(position)) {
            return TYPE_HEADER;
        } else {
            final int viewType = getViewType(position);
            if (viewType == TYPE_HEADER) {
                throw new IllegalStateException("wrong view type = " + viewType + " at position = " +
                        position + " . It's reserved for subheader view type");
            }
            return viewType;
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

    /**
     * Notify that item at {@code itemPosition} has been inserted.
     *
     * @param itemPosition position of the new item in your data set.
     * @return first adapter position of the inserted items.
     */
    public final int notifyItemInsertedAtPosition(int itemPosition) {
        if (itemPosition == 0) {
            if (getItemCount() == 0 || onPlaceSubheaderBetweenItems(itemPosition)) {
                sectionManager.insertItem(0, true);
                notifyItemRangeInserted(0, 2);
                return 0;
            } else {
                sectionManager.insertItem(1, false);
                notifyItemInserted(1);
                return 1;
            }
        } else if (itemPosition == getItemSize() - 1) {
            int itemCount = getItemCount();
            if (onPlaceSubheaderBetweenItems(itemPosition - 1)) {
                sectionManager.insertItem(itemCount, true);
                notifyItemRangeInserted(itemCount, 2);
                return itemCount;
            } else {
                sectionManager.insertItem(itemCount, false);
                notifyItemInserted(itemCount);
                return itemCount;
            }
        } else {
            int itemAdapterPosition = sectionManager.getAdapterPositionForItem(itemPosition);
            if (onPlaceSubheaderBetweenItems(itemPosition - 1) && onPlaceSubheaderBetweenItems(itemPosition)) {
                sectionManager.insertItem(itemAdapterPosition - 1, true);
                notifyItemRangeInserted(itemAdapterPosition - 1, 2);
                return itemAdapterPosition - 1;
            } else if (onPlaceSubheaderBetweenItems(itemPosition - 1)){
                sectionManager.insertItem(itemAdapterPosition, false);
                notifyItemInserted(itemAdapterPosition);
                return itemAdapterPosition;
            } else if (onPlaceSubheaderBetweenItems(itemPosition)){
                sectionManager.insertItem(itemAdapterPosition - 1, false);
                notifyItemInserted(itemAdapterPosition - 1);
                return itemAdapterPosition - 1;
            } else {
                sectionManager.insertItem(itemAdapterPosition, false);
                notifyItemInserted(itemAdapterPosition);
                return itemAdapterPosition;
            }
        }
    }

    /**
     * Notify that item at {@code itemPosition} has been changed.
     *
     * @param itemPosition position of the item that has changed in your data set.
     * @return adapter position of the changed items.
     */
    public final int notifyItemChangedAtPosition(int itemPosition) {
        final int itemAdapterPosition = sectionManager.getAdapterPositionForItem(itemPosition);
        notifyItemChanged(itemAdapterPosition);
        return itemAdapterPosition;
    }

    /**
     * Notify that item at {@code itemPosition} has been removed.
     *
     * @param itemPosition position of the item that has removed in your data set.
     * @return adapter position of the removed items.
     */
    public final int notifyItemRemovedAtPosition(int itemPosition) {

        final int itemAdapterPosition = sectionManager.getAdapterPositionForItem(itemPosition);
        final boolean isSectionRemoved = sectionManager.removeItem(itemAdapterPosition);

        if (isSectionRemoved) {
            notifyItemRangeRemoved(itemAdapterPosition - 1, 2);
            return itemAdapterPosition - 1;
        } else {
            notifyItemRemoved(itemAdapterPosition);
            return itemAdapterPosition;
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

    public final boolean isSubheaderAtPosition(int adapterPosition) {
        return sectionManager.isSectionSubheaderOnPosition(adapterPosition);
    }

    /**
     * Expand the section at the specified index.
     *
     * @param sectionIndex index of the section to be expanded.
     */
    public final void expandSection(int sectionIndex) {

        if (isSectionExpanded(sectionIndex)) {
            return;
        }

        int sectionSubheaderPosition = sectionManager.getSection(sectionIndex).getSubheaderPosition();
        notifyItemChanged(sectionSubheaderPosition);

        int insertedItemCount = sectionManager.expandSection(sectionIndex);
        notifyItemRangeInserted(sectionSubheaderPosition + 1, insertedItemCount);

    }

    /**
     * Expand all sections.
     */
    public final void expandAllSections() {
        sectionManager.expandAllSections();
        notifyDataSetChanged();
    }

    /**
     * Collapse the section at the specified index.
     *
     * @param sectionIndex index of the section to be collapsed.
     */
    public final void collapseSection(int sectionIndex) {

        if (!isSectionExpanded(sectionIndex)) {
            return;
        }

        int sectionSubheaderPosition = sectionManager.getSection(sectionIndex).getSubheaderPosition();
        notifyItemChanged(sectionSubheaderPosition);

        int removedItemCount = sectionManager.collapseSection(sectionIndex);
        notifyItemRangeRemoved(sectionSubheaderPosition + 1, removedItemCount);

    }

    /**
     * Collapse all sections.
     */
    public final void collapseAllSections() {
        sectionManager.collapseAllSections();
        notifyDataSetChanged();
    }

    /**
     * Returns true if section at the specified position is expanded.
     *
     * @param sectionIndex index of section whose expansion to be tested.
     * @return true if section at the specified position is expanded.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public final boolean isSectionExpanded(int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= getSectionCount()) {
            throw new IndexOutOfBoundsException("sectionIndex: " + sectionIndex + ", sectionCount: " + getSectionCount());
        }
        return sectionManager.isSectionExpanded(sectionIndex);
    }

    /**
     * Returns the index of the section which contains the item at the specified adapterPosition.
     *
     * @param adapterPosition adapter position of the item in adapter's data set.
     * @return the index of the section which contains the item at the specified adapterPosition.
     * @throws IndexOutOfBoundsException if the adapter position is out of range.
     */
    public final int getSectionIndex(int adapterPosition) {
        if (adapterPosition < 0 || adapterPosition >= getItemCount()) {
            throw new IndexOutOfBoundsException("adapterPosition: " + adapterPosition + ", itemCount: " + getItemCount());
        }
        return sectionManager.sectionIndex(adapterPosition);
    }

    /**
     * Returns the position of the item in the section this item belongs,
     * or -1 if subheader is placed at the specified adapter position.
     *
     * @param adapterPosition adapter position of the item in adapter's data set.
     * @return the position of the item in the section this item belongs,
     *         or -1 if subheader is placed at the specified adapter position.
     * @throws IndexOutOfBoundsException if the adapter position is out of range.
     */
    public final int getPositionInSection(int adapterPosition) {
        if (adapterPosition < 0 || adapterPosition >= getItemCount()) {
            throw new IndexOutOfBoundsException("adapterPosition: " + adapterPosition + ", itemCount: " + getItemCount());
        }
        if (sectionManager.isSectionSubheaderOnPosition(adapterPosition)) {
            return -1;
        }
        return sectionManager.positionInSection(adapterPosition);
    }

    /**
     * Returns true if the item at the specified adapter position
     * is the first in the section this item belongs.
     *
     * @param adapterPosition adapter position of the item in adapter's data set.
     * @return true if the item at the specified adapter position
     *         is the first in the section this item belongs.
     * @throws IndexOutOfBoundsException if the adapter position is out of range.
     */
    public final boolean isFirstItemInSection(int adapterPosition) {
        return getPositionInSection(adapterPosition) == 0;
    }

    /**
     * Returns true if the item at the specified adapter position
     * is the last in the section this item belongs.
     *
     * @param adapterPosition adapter position of the item in adapter's data set.
     * @return true if the item at the specified adapter position
     *         is the last in the section this item belongs.
     * @throws IndexOutOfBoundsException if the adapter position is out of range.
     */
    public final boolean isLastItemInSection(int adapterPosition) {
        return getPositionInSection(adapterPosition) == getSectionSize(getSectionIndex(adapterPosition)) - 1;
    }

    /**
     * Returns the number of items(not including subheader) in the section at the specified position.
     *
     * @param sectionIndex index of the section.
     * @return the number of items(not including subheader) in the section at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public final int getSectionSize(int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= getSectionCount()) {
            throw new IndexOutOfBoundsException("sectionIndex: " + sectionIndex + ", getSectionCount: " + getSectionCount());
        }
        return sectionManager.sectionSize(sectionIndex);
    }

    /**
     * Returns the subheader adapter position of the section at the specified index.
     *
     * @param sectionIndex index of the section.
     * @return the subheader adapter position of the section at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public final int getSectionSubheaderPosition(int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= getSectionCount()) {
            throw new IndexOutOfBoundsException("sectionIndex: " + sectionIndex + ", sectionCount: " + getSectionCount());
        }
        return sectionManager.getSection(sectionIndex).getSubheaderPosition();
    }

    /**
     * Returns the number of sections in this adapter.
     *
     * @return the number of sections in this adapter
     */
    public final int getSectionCount() {
        return sectionManager.getSectionCount();
    }

    SectionManager getSectionManager() {
        return sectionManager;
    }

    void setSectionManager(SectionManager sectionManager) {
        this.sectionManager = sectionManager;
    }

}
