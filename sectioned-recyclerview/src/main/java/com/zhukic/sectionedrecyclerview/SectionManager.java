package com.zhukic.sectionedrecyclerview;

import android.support.annotation.IntRange;

import com.zhukic.sectionedrecyclerview.result.NotifyItemChangedResult;
import com.zhukic.sectionedrecyclerview.result.NotifyItemInsertedResult;
import com.zhukic.sectionedrecyclerview.result.NotifyItemRemovedResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

class SectionManager {

    private final List<Section> sections = new ArrayList<>();

    void init(SectionProvider sectionProvider) {

        this.sections.clear();

        if (sectionProvider.getItemSize() != 0) {
            this.sections.add(new Section(0));
        } else {
            return;
        }

        int lastSectionItemCount = sectionProvider.getItemSize();

        for (int i = 1; i < sectionProvider.getItemSize(); i++) {
            if (sectionProvider.onPlaceSubheaderBetweenItems(i - 1)) {
                final Section section = new Section(i + getSections().size());
                final Section previousSection = getLastSection();
                final int sectionItemCount = section.getSubheaderPosition() - previousSection.getSubheaderPosition() - 1;
                previousSection.setItemsCount(sectionItemCount);
                sections.add(section);
                lastSectionItemCount -= sectionItemCount;
            }
        }

        final Section lastSection = getLastSection();
        lastSection.setItemsCount(lastSectionItemCount);

    }

    void addSection(Section section) {
        sections.add(section);
    }

    boolean isSectionSubheaderOnPosition(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
        for (Section section : sections) {
            if (section.getSubheaderPosition() == position) {
                return true;
            }
        }
        return false;
    }

    boolean isSectionExpanded(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= getSections().size()) {
            throw new IllegalArgumentException("sectionIndex: " + sectionIndex + ", size: " + getSections().size());
        }
        return sections.get(sectionIndex).isExpanded();
    }

    int expandSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {

        final Section sectionToExpand = sections.get(sectionIndex);

        if (sectionToExpand.isExpanded()) {
            return 0;
        }

        sectionToExpand.setExpanded(true);

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section = sections.get(i);
            section.setSubheaderPosition(section.getSubheaderPosition() + sectionToExpand.getItemCount());
        }

        return sectionToExpand.getItemCount();

    }

    void expandAllSections() {
        for (Section section : sections) {
            expandSection(sections.indexOf(section));
        }
    }

    int collapseSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {

        if (sectionIndex < 0 || sectionIndex >= getSections().size()) {
            throw new IllegalArgumentException("sectionIndex: " + sectionIndex + ", size: " + getSections().size());
        }

        final Section sectionToCollapse = sections.get(sectionIndex);

        if (!sectionToCollapse.isExpanded()) {
            return 0;
        }

        sectionToCollapse.setExpanded(false);

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section = sections.get(i);
            section.setSubheaderPosition(section.getSubheaderPosition() - sectionToCollapse.getItemCount());
        }

        return sectionToCollapse.getItemCount();

    }

    void collapseAllSections() {
        for (Section section : sections) {
            collapseSection(sections.indexOf(section));
        }
    }

    int getItemPositionForSubheaderViewHolder(@IntRange(from = 0, to = Integer.MAX_VALUE) int subheaderPosition) {

        if (subheaderPosition < 0 || subheaderPosition >= getItemCount()) {
            throw new IllegalArgumentException("subheaderPosition: " + subheaderPosition + ", itemCount: " + getItemCount());
        }

        int itemPosition = 0;

        int sectionIndex = sectionIndex(subheaderPosition);

        for (int i = 0; i < sectionIndex; i++) {
            itemPosition += sections.get(i).getItemCount();
        }

        return itemPosition;

    }

    int getItemPositionForItemViewHolder(@IntRange(from = 0, to = Integer.MAX_VALUE) int itemHolderPosition) {

        if (itemHolderPosition < 0 || itemHolderPosition >= getItemCount()) {
            throw new IllegalArgumentException("itemHolderPosition: " + itemHolderPosition + ", itemCount: " + getItemCount());
        }
        if (isSectionSubheaderOnPosition(itemHolderPosition)) {
            throw new IllegalArgumentException("section subheader is placed at " + itemHolderPosition + " position");
        }

        int sectionIndex = sectionIndex(itemHolderPosition);

        itemHolderPosition -= sectionIndex + 1;

        for (int i = 0; i < sectionIndex; i++) {

            final Section section = sections.get(i);

            if (!section.isExpanded()) {
                itemHolderPosition += section.getItemCount();
            }

        }

        return itemHolderPosition;

    }

    int getAdapterPositionForItem(int itemPosition) {

        if (itemPosition < 0 || itemPosition >= getDataItemCount()) {
            throw new IllegalArgumentException("itemPosition: " + itemPosition + ", itemCount: " + getDataItemCount());
        }

        if (!sections.get(sectionIndexByItemPosition(itemPosition)).isExpanded()) {
            return -1;
        }

        int adapterPosition = 0;

        int itemCount = 0;

        for (Section section : sections) {

            adapterPosition += 1;

            if (!section.isExpanded()) {
                adapterPosition -= section.getItemCount();
            }

            if (itemCount + section.getItemCount() <= itemPosition) {
                itemCount += section.getItemCount();
            } else {
                break;
            }

        }

        return adapterPosition + itemPosition;

    }

    NotifyItemChangedResult onItemChanged(int itemPosition) {

        final int adapterPosition = getAdapterPositionForItem(itemPosition);

        final int subheaderPosition = sections.get(sectionIndex(adapterPosition)).getSubheaderPosition();

        return new NotifyItemChangedResult(getAdapterPositionForItem(itemPosition), 1, subheaderPosition);

    }

    NotifyItemRemovedResult onItemRemoved(int itemPosition) {

        final int itemAdapterPosition = getAdapterPositionForItem(itemPosition);

        final boolean isSectionCollapsed = !isSectionExpanded(sectionIndex(itemAdapterPosition));

        final int sectionIndex = sectionIndex(itemAdapterPosition);
        final Section section = sections.get(sectionIndex);

        boolean isSectionRemoved;

        int positionsToDecrease;

        if (section.getItemCount() == 1) {
            isSectionRemoved = true;
            positionsToDecrease = 2;
        } else {
            isSectionRemoved = false;
            section.setItemsCount(section.getItemCount() - 1);
            positionsToDecrease = 1;
        }

        if (!section.isExpanded() && isSectionRemoved) {
            positionsToDecrease = 1;
        }

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section1 = sections.get(i);
            section1.setSubheaderPosition(section1.getSubheaderPosition() - positionsToDecrease);
        }

        if (isSectionRemoved) {
            sections.remove(section);
        }

        if (isSectionRemoved) {
            return new NotifyItemRemovedResult(itemAdapterPosition - 1, 2, itemAdapterPosition - 1, true);
        } else {
            if (isSectionCollapsed) {
                return null;
            }
            return new NotifyItemRemovedResult(itemAdapterPosition, 1, section.getSubheaderPosition(), false);
        }

    }

    NotifyItemInsertedResult onItemInserted(int itemPosition, SectionProvider sectionProvider) {
        if (itemPosition == 0) {
            if (getItemCount() == 0 || sectionProvider.onPlaceSubheaderBetweenItems(itemPosition)) {
                insertItem(0, true);
                return new NotifyItemInsertedResult(0, 2, 0, true);
            } else {
                insertItem(1, false);
                if (getSectionsCount() > 0 && !isSectionExpanded(0)) {
                    return null;
                }
                return new NotifyItemInsertedResult(1, 1, 0, false);
            }
        } else if (itemPosition == sectionProvider.getItemSize() - 1) {
            int itemCount = getItemCount();
            if (sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1)) {
                insertItem(itemCount, true);
                return new NotifyItemInsertedResult(itemCount, 2, itemCount, true);
            } else {
                insertItem(itemCount, false);
                if (!isSectionExpanded(getSectionsCount() - 1)) {
                    return null;
                }
                return new NotifyItemInsertedResult(itemCount, 1, getLastSection().getSubheaderPosition(), false);
            }
        } else {
            int itemAdapterPosition = getAdapterPositionForItem(itemPosition);
            if (sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1) && sectionProvider.onPlaceSubheaderBetweenItems(itemPosition)) {
                insertItem(itemAdapterPosition - 1, true);
                return new NotifyItemInsertedResult(itemAdapterPosition - 1, 2, itemAdapterPosition - 1, true);
            } else if (sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1)) {
                insertItem(itemAdapterPosition, false);
                if (!isSectionExpanded(sectionIndex(itemAdapterPosition))) {
                    return null;
                }
                return new NotifyItemInsertedResult(itemAdapterPosition, 1, getSection(sectionIndex(itemAdapterPosition)).getSubheaderPosition(), false);
            } else if (sectionProvider.onPlaceSubheaderBetweenItems(itemPosition)) {
                insertItem(itemAdapterPosition - 1, false);
                if (!isSectionExpanded(sectionIndex(itemAdapterPosition - 1))) {
                    return null;
                }
                return new NotifyItemInsertedResult(itemAdapterPosition - 1, 1, getSection(sectionIndex(itemAdapterPosition - 1)).getSubheaderPosition(), false);
            } else {
                insertItem(itemAdapterPosition, false);
                if (!isSectionExpanded(sectionIndex(itemAdapterPosition))) {
                    return null;
                }
                return new NotifyItemInsertedResult(itemAdapterPosition, 1, getSection(sectionIndex(itemAdapterPosition)).getSubheaderPosition(), false);
            }
        }
    }

    void insertItem(int adapterPosition, boolean shouldInsertSection) {

        if (shouldInsertSection) {

            Section newSection = new Section(adapterPosition);
            newSection.setItemsCount(1);

            if (adapterPosition == getItemCount()) {
                sections.add(newSection);
                return;
            }

            int sectionIndex = sectionIndex(adapterPosition);

            for (int i = sectionIndex; i < sections.size(); i++) {
                Section section = getSection(i);
                section.setSubheaderPosition(section.getSubheaderPosition() + 2);
            }

            sections.add(sectionIndex, newSection);

        } else {

            if ((isSectionSubheaderOnPosition(adapterPosition) && adapterPosition != 0) || adapterPosition == getItemCount()) {
                adapterPosition--;
            }

            int currentSectionIndex = sectionIndex(adapterPosition);
            Section currentSection = getSection(currentSectionIndex);
            currentSection.setItemsCount(currentSection.getItemCount() + 1);

            if (!currentSection.isExpanded()) {
                return;
            }

            for (int i = currentSectionIndex + 1; i < sections.size(); i++) {
                Section section = getSection(i);
                section.setSubheaderPosition(section.getSubheaderPosition() + 1);
            }

        }

    }

    Section getSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= getSections().size()) {
            throw new IllegalArgumentException("sectionIndex: " + sectionIndex + ", size: " + getSections().size());
        }
        return sections.get(sectionIndex);
    }

    Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    int sectionIndex(@IntRange(from = 0, to = Integer.MAX_VALUE) int adapterPosition) {

        if (adapterPosition < 0 || adapterPosition >= getItemCount()) {
            throw new IllegalArgumentException("adapterPosition: " + adapterPosition + ", itemCount: " + getItemCount());
        }

        int sectionIndex = 0;

        for (Section section : sections) {
            if (adapterPosition == section.getSubheaderPosition()) {
                return sections.indexOf(section);
            } else if (adapterPosition > section.getSubheaderPosition()) {
                sectionIndex = sections.indexOf(section);
            }
        }

        return sectionIndex;

    }

    int sectionIndexByItemPosition(@IntRange(from = 0, to = Integer.MAX_VALUE) int itemPosition) {

        if (itemPosition < 0) {
            throw new IllegalArgumentException("itemPosition < 0");
        }

        int itemCount = 0;

        for (Section section : sections) {

            itemCount += section.getItemCount();

            if (itemCount > itemPosition) {
                return sections.indexOf(section);
            }

        }

        return sections.size() - 1;

    }

    int getItemCount() {

        int itemCount = 0;

        for (Section section : sections) {
            itemCount += 1;
            if (section.isExpanded()) {
                itemCount += section.getItemCount();
            }
        }

        return itemCount;

    }

    //TODO remove, we have sectionProvider.getItemCount()
    int getDataItemCount() {
        int dataItemCount = 0;
        for (Section section : sections) {
            dataItemCount += section.getItemCount();
        }
        return dataItemCount;
    }

    int positionInSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int itemAdapterPosition) {

        if (itemAdapterPosition < 0 || itemAdapterPosition >= getItemCount()) {
            throw new IllegalArgumentException("itemAdapterPosition: " + itemAdapterPosition + ", itemCount: " + getItemCount());
        }
        if (isSectionSubheaderOnPosition(itemAdapterPosition)) {
            throw new IllegalArgumentException("section subheader is placed at " + itemAdapterPosition + " position");
        }

        final Section section = getSection(sectionIndex(itemAdapterPosition));

        return itemAdapterPosition - section.getSubheaderPosition() - 1;

    }

    int sectionSize(int sectionIndex) {
        return getSection(sectionIndex).getItemCount();
    }

    int getSectionsCount() {
        return sections.size();
    }

    List<Section> getSections() {
        return sections;
    }

    void clear() {
        getSections().clear();
    }

}
