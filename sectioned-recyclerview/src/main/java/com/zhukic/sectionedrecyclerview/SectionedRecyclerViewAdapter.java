package com.zhukic.sectionedrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public abstract class SectionedRecyclerViewAdapter<SH extends RecyclerView.ViewHolder, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionProvider {

    protected static final int TYPE_HEADER = -1;

    private SectionManager sectionManager;

    public SectionedRecyclerViewAdapter() {
        this.sectionManager = new SectionManager(this);
    }

    SectionedRecyclerViewAdapter(SectionManager sectionManager) {
        this.sectionManager = sectionManager;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        sectionManager.init();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        sectionManager.clear();
    }

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract SH onCreateSubheaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(VH holder, int itemPosition);

    /**
     * Called to display the data for the subheader at the specified position.
     *
     * @param nextItemPosition position of the first item in the section to which
     *                         {@code subheaderHolder} belongs.
     */
    public abstract void onBindSubheaderViewHolder(SH subheaderHolder, int nextItemPosition);

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

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateSubheaderViewHolder(parent, viewType);
        } else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
        sectionManager.init();
        notifyDataSetChanged();
    }

    /**
     * Notify that item at {@code itemPosition} has been changed.
     *
     * @param itemPosition position of the item that has changed in your data set.
     * @return adapter position of the changed item.
     */
    public final int notifyItemChangedAtPosition(int itemPosition) {
        final NotifyResult result = sectionManager.onItemChanged(itemPosition);
        applyResult(result);
        return result.getPositionStart();
    }

    /**
     * Notify that item at {@code itemPosition} has been inserted.
     *
     * @param itemPosition position of the new item in your data set.
     * @return first adapter position of the inserted items
     *         or -1 if the section new item belongs to is collapsed .
     */
    public final int notifyItemInsertedAtPosition(int itemPosition) {
        final NotifyResult result = sectionManager.onItemInserted(itemPosition);
        applyResult(result);
        return result.getPositionStart();
    }

    /**
     * Notify that item at {@code itemPosition} has been removed.
     *
     * @param itemPosition position of the item that has removed in your data set.
     * @return adapter position of the first removed item
     *         or -1 if the section removed item belongs to is collapsed.
     */
    public final int notifyItemRemovedAtPosition(int itemPosition) {
        final NotifyResult result = sectionManager.onItemRemoved(itemPosition);
        applyResult(result);
        return result.getPositionStart();
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

    /**
     * Returns true if section subheader is placed at the specified adapter position.
     *
     * @param adapterPosition adapter position of the item in adapter's data set.
     * @return true if section subheader is placed at the specified adapter position.
     * @throws IndexOutOfBoundsException if the adapter position is out of range.
     */
    public final boolean isSubheaderAtPosition(int adapterPosition) {
        if (adapterPosition < 0 || adapterPosition >= getItemCount()) {
            throw new IndexOutOfBoundsException("adapterPosition: " + adapterPosition + ", itemCount: " + getItemCount());
        }
        return sectionManager.isSectionSubheaderOnPosition(adapterPosition);
    }

    /**
     * Expand the section at the specified index.
     *
     * @param sectionIndex index of the section to be expanded.
     */
    public final void expandSection(int sectionIndex) {
        final NotifyResult result = sectionManager.expandSection(sectionIndex);
        applyResult(result);
    }

    /**
     * Expand all sections.
     */
    public final void expandAllSections() {
        final NotifyResult result = sectionManager.expandAllSections();
        applyResult(result);
    }

    /**
     * Collapse the section at the specified index.
     *
     * @param sectionIndex index of the section to be collapsed.
     */
    public final void collapseSection(int sectionIndex) {
        final NotifyResult result = sectionManager.collapseSection(sectionIndex);
        applyResult(result);
    }

    /**
     * Collapse all sections.
     */
    public final void collapseAllSections() {
        final NotifyResult result = sectionManager.collapseAllSections();
        applyResult(result);
    }

    /**
     * Returns true if section at the specified position is expanded.
     *
     * @param sectionIndex index of section whose expansion to be tested.
     * @return true if section at the specified position is expanded.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public final boolean isSectionExpanded(int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= getSectionsCount()) {
            throw new IndexOutOfBoundsException("sectionIndex: " + sectionIndex + ", sectionCount: " + getSectionsCount());
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
    public final int getItemPositionInSection(int adapterPosition) {
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
        return getItemPositionInSection(adapterPosition) == 0;
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
        return getItemPositionInSection(adapterPosition) == getSectionSize(getSectionIndex(adapterPosition)) - 1;
    }

    /**
     * Returns the number of items(not including subheader) in the section at the specified position.
     *
     * @param sectionIndex index of the section.
     * @return the number of items(not including subheader) in the section at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public final int getSectionSize(int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= getSectionsCount()) {
            throw new IndexOutOfBoundsException("sectionIndex: " + sectionIndex + ", sectionCount: " + getSectionsCount());
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
        if (sectionIndex < 0 || sectionIndex >= getSectionsCount()) {
            throw new IndexOutOfBoundsException("sectionIndex: " + sectionIndex + ", sectionCount: " + getSectionsCount());
        }
        return sectionManager.getSectionSubheaderPosition(sectionIndex);
    }

    /**
     * Returns the number of sections in this adapter.
     *
     * @return the number of sections in this adapter
     */
    public final int getSectionsCount() {
        return sectionManager.getSectionsCount();
    }

    private void applyResult(NotifyResult notifyResult) {
        for (Notifier notifier : notifyResult.getNotifiers()) {
            applyNotifier(notifier);
        }
    }

    private void applyNotifier(Notifier notifier) {
        switch (notifier.getType()) {
            case ALL_DATA_CHANGED:
                notifyDataChanged();
                break;
            case CHANGED:
                notifyItemRangeChanged(notifier.getPositionStart(), notifier.getItemCount());
                break;
            case INSERTED:
                notifyItemRangeInserted(notifier.getPositionStart(), notifier.getItemCount());
                break;
            case REMOVED:
                notifyItemRangeRemoved(notifier.getPositionStart(), notifier.getItemCount());
                break;
        }
    }
}
