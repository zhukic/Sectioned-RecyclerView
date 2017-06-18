package com.zhukic.sectionedrecyclerview;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Pack200;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

class SectionManager {

    private List<Section> sections = new ArrayList<>();

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

    int expandSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {

        final Section sectionToExpand = sections.get(sectionIndex);

        if (sectionToExpand.isExpanded()) {
            return 0;
        }

        sectionToExpand.setExpanded(true);

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section = sections.get(i);
            section.setSubheaderPosition(section.getSubheaderPosition() + sectionToExpand.getItemsCount());
        }

        return sectionToExpand.getItemsCount();

    }

    void expandAllSections() {
        for (Section section : sections) {
            expandSection(sections.indexOf(section));
        }
    }

    int collapseSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {

        final Section sectionToCollapse = sections.get(sectionIndex);

        if (!sectionToCollapse.isExpanded()) {
            return 0;
        }

        sectionToCollapse.setExpanded(false);

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section = sections.get(i);
            section.setSubheaderPosition(section.getSubheaderPosition() - sectionToCollapse.getItemsCount());
        }

        return sectionToCollapse.getItemsCount();

    }

    void collapseAllSections() {
        for (Section section : sections) {
            collapseSection(sections.indexOf(section));
        }
    }

    boolean isSectionExpanded(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {
        return sections.get(sectionIndex).isExpanded();
    }

    int getItemPositionForSubheaderViewHolder(@IntRange(from = 0, to = Integer.MAX_VALUE) int subheaderPosition) {

        //TODO check isSubheaderOnPosition

        int itemPosition = 0;

        int sectionIndex = sectionIndex(subheaderPosition);

        for (int i = 0; i < sectionIndex; i++) {
            itemPosition += sections.get(i).getItemsCount();
        }

        return itemPosition;

    }

    int getItemPositionForItemViewHolder(@IntRange(from = 0, to = Integer.MAX_VALUE) int holderPosition) {

        //TODO check subheader is placed at holder position

        int sectionIndex = sectionIndex(holderPosition);

        holderPosition -= sectionIndex + 1;

        for (int i = 0; i < sectionIndex; i++) {

            final Section section = sections.get(i);

            if (!section.isExpanded()) {
                holderPosition += section.getItemsCount();
            }

        }

        return holderPosition;

    }

    int getAdapterPositionForItem(int itemPosition) {

        //TODO check if itemPosition is out of range

        int adapterPosition = 0;

        int itemCount = 0;

        for (Section section : sections) {

            adapterPosition += 1;

            if (!section.isExpanded()) {
                adapterPosition -= section.getItemsCount();
            }

            if (itemCount + section.getItemsCount() <= itemPosition) {
                itemCount += section.getItemsCount();
            } else {
                break;
            }

        }

        return adapterPosition + itemPosition;

    }

    boolean removeItem(int itemAdapterPosition) {

        //TODO check if subheader at itemAdapterPosition

        final int sectionIndex = sectionIndex(itemAdapterPosition);
        final Section section = sections.get(sectionIndex);

        boolean isSectionRemoved;

        int positionsToDecrease;

        if (section.getItemsCount() == 1) {
            isSectionRemoved = true;
            positionsToDecrease = 2;
        } else {
            isSectionRemoved = false;
            section.setItemsCount(section.getItemsCount() - 1);
            positionsToDecrease = 1;
        }

        for (int i = sectionIndex + 1; i < sections.size(); i++) {

            final Section section1 = sections.get(i);

            section1.setSubheaderPosition(section1.getSubheaderPosition() - positionsToDecrease);

        }

        if (isSectionRemoved) {
            sections.remove(section);
        }

        return isSectionRemoved;

    }

    void insertItem(int adapterPosition, boolean sectionShouldBeInserted) {


        if (sectionShouldBeInserted) {

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

            if (isSectionSubheaderOnPosition(adapterPosition)) {

                adapterPosition--;

            }

            int sectionIndex = sectionIndex(adapterPosition);

            Section currentSection = getSection(sectionIndex);

            currentSection.setItemsCount(currentSection.getItemsCount() + 1);

            for (int i = sectionIndex + 1; i < sections.size(); i++) {

                Section section = getSection(i);

                section.setSubheaderPosition(section.getSubheaderPosition() + 1);

            }

        }

    }

    Section getSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {
        return sections.get(sectionIndex);
    }

    Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    int sectionIndex(@IntRange(from = 0, to = Integer.MAX_VALUE) int holderPosition) {

        //TODO check holder position is out of range

        int sectionIndex = 0;

        for (Section section : sections) {
            if (holderPosition == section.getSubheaderPosition()) {
                return sections.indexOf(section);
            } else if (holderPosition > section.getSubheaderPosition()) {
                sectionIndex = sections.indexOf(section);
            }
        }

        return sectionIndex;

    }

    List<Section> getSections() {
        return sections;
    }

    int getItemCount() {

        int itemCount = 0;

        for (Section section : sections) {
            itemCount += 1;
            if (section.isExpanded()) {
                itemCount += section.getItemsCount();
            }
        }

        return itemCount;

    }

}
