package com.zhukic.sectionedrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zhukic.sectionedrecyclerview.result.NotifyResultNew;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        sectionManager.init();
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

    private void applyResult(NotifyResultNew notifyResultNew) {
        for (Notifier notifier : notifyResultNew.getNotifiers()) {
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

    public final void notifyDataChanged() {
        sectionManager.init();
        notifyDataSetChanged();
    }

    //TODO А ЧТО ЕСЛИ СЕКЦИЯ COLLAPSED?
    //TODO notify subheader
    /**
     * Notify that item at {@code itemPosition} has been changed.
     *
     * @param itemPosition position of the item that has changed in your data set.
     * @return adapter position of the changed item.
     */
    public final int notifyItemChangedAtPosition(int itemPosition) {
        final NotifyResultNew result = sectionManager.onItemChanged(itemPosition);
        applyResult(result);
        return result.getPositionStart();
    }

    //TODO А ЧТО ЕСЛИ СЕКЦИЯ COLLAPSED?
    public final int notifyItemInsertedAtPosition(int itemPosition, boolean shouldNotifySectionSubheader) {
        final NotifyResultNew result = sectionManager.onItemInserted(itemPosition);
        applyResult(result);
        return result.getPositionStart();

        /*final NotifyItemInsertedResult result = sectionManager.onItemInserted(itemPosition);

        if (result == null) {
            return -1;
        }

        notifyItemRangeInserted(result.getPositionStart(), result.getItemCount());

        if (!result.isNewSection() && shouldNotifySectionSubheader) {
            notifyItemChanged(result.getSubheaderPosition());
        }

        return result.getPositionStart();*/

    }

    /**
     * Notify that item at {@code itemPosition} has been inserted.
     *
     * @param itemPosition position of the new item in your data set.
     * @return first adapter position of the inserted items
     *         or -1 if the section new item belongs to is collapsed .
     */
    public final int notifyItemInsertedAtPosition(int itemPosition) {
        return notifyItemInsertedAtPosition(itemPosition, false);
    }

    //TODO А ЧТО ЕСЛИ СЕКЦИЯ COLLAPSED?
    public final int notifyItemRemovedAtPosition(int itemPosition, boolean notifySectionSubheader) {
        final NotifyResultNew result = sectionManager.onItemRemoved(new Integer(itemPosition));
        applyResult(result);
        return result.getPositionStart();

        /*final NotifyItemRemovedResult result = sectionManager.onItemRemoved(itemPosition);

        if (result == null) {
            return -1;
        }

        notifyItemRangeRemoved(result.getPositionStart(), result.getItemCount());

        if (!result.isSectionRemoved() && notifySectionSubheader) {
            notifyItemChanged(result.getSubheaderPosition());
        }

        return result.getPositionStart();*/

    }

    /**
     * Notify that item at {@code itemPosition} has been removed.
     *
     * @param itemPosition position of the item that has removed in your data set.
     * @return adapter position of the first removed item
     *         or -1 if the section removed item belongs to is collapsed.
     */
    public final int notifyItemRemovedAtPosition(int itemPosition) {
        return notifyItemRemovedAtPosition(itemPosition, false);
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

    public final void expandSection(int sectionIndex, boolean shouldNotifySectionSubheader) {
        final NotifyResultNew result = sectionManager.expandSection(sectionIndex, shouldNotifySectionSubheader);
        applyResult(result);
    }

    /**
     * Expand the section at the specified index.
     *
     * @param sectionIndex index of the section to be expanded.
     */
    public final void expandSection(int sectionIndex) {
        expandSection(sectionIndex, true);
    }

    public final void expandAllSections(boolean notifySectionSubheaders) {
        final NotifyResultNew result = sectionManager.expandAllSections(notifySectionSubheaders);
        applyResult(result);
    }

    /**
     * Expand all sections.
     */
    public final void expandAllSections() {
        expandAllSections(true);
    }

    public final void collapseSection(int sectionIndex, boolean shouldNotifySectionSubheader) {
        final NotifyResultNew result = sectionManager.collapseSection(sectionIndex, shouldNotifySectionSubheader);
        applyResult(result);
    }

    /**
     * Collapse the section at the specified index.
     *
     * @param sectionIndex index of the section to be collapsed.
     */
    public final void collapseSection(int sectionIndex) {
        collapseSection(sectionIndex, true);
    }

    /**
     * Collapse all sections.
     */
    public final void collapseAllSections(boolean notifySectionSubheaders) {
        final NotifyResultNew result = sectionManager.collapseAllSections(notifySectionSubheaders);
        applyResult(result);
    }

    /**
     * Collapse all sections.
     */
    public final void collapseAllSections() {
        collapseAllSections(true);
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
    //TODO А ЕСЛИ СЕКЦИЯ COLLAPSED, ТО ЧТО ТОГДА?
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
        return sectionManager.getSection(sectionIndex).getSubheaderPosition();
    }

    /**
     * Returns the number of sections in this adapter.
     *
     * @return the number of sections in this adapter
     */
    public final int getSectionsCount() {
        return sectionManager.getSectionsCount();
    }

    //TODO remove
    SectionManager getSectionManager() {
        return sectionManager;
    }

    //TODO remove
    void setSectionManager(SectionManager sectionManager) {
        this.sectionManager = sectionManager;
    }

}
