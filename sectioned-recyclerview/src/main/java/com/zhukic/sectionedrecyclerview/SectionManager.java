package com.zhukic.sectionedrecyclerview;

import android.support.annotation.IntRange;

import com.zhukic.sectionedrecyclerview.result.NotifyItemInsertedResult;
import com.zhukic.sectionedrecyclerview.result.NotifyItemRemovedResult;
import com.zhukic.sectionedrecyclerview.result.NotifyResultNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

class SectionManager {

    private final List<Section> sections = new ArrayList<>();

    private final SectionProvider sectionProvider;

    SectionManager(SectionProvider sectionProvider) {
        this.sectionProvider = sectionProvider;
    }

    void init() {

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
                previousSection.setItemCount(sectionItemCount);
                sections.add(section);
                lastSectionItemCount -= sectionItemCount;
            }
        }

        final Section lastSection = getLastSection();
        lastSection.setItemCount(lastSectionItemCount);

    }

    void addSection(int index, Section newSection) {
        for (int i = index; i < sections.size(); i++) {
            Section section = getSection(i);
            if (section.isExpanded()) {
                section.setSubheaderPosition(section.getSubheaderPosition() + newSection.getItemCount() + 1);
            } else {
                section.setSubheaderPosition(section.getSubheaderPosition() + 1);
            }
        }
        sections.add(index, newSection);
    }

    void addSection(Section section) {
        sections.add(section);
    }

    void removeSection(Section section) {
        final int index = sections.indexOf(section);
        for (int i = index + 1; i < sections.size(); i++) {
            final Section s = sections.get(i);
            if (section.isExpanded()) {
                s.setSubheaderPosition(s.getSubheaderPosition() - section.getItemCount() - 1);
            } else {
                s.setSubheaderPosition(s.getSubheaderPosition() - 1);
            }
        }
        sections.remove(section);
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

    NotifyResultNew expandSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex, boolean notifySubheader) {

        final Section sectionToExpand = sections.get(sectionIndex);

        if (sectionToExpand.isExpanded()) {
            return notifySubheader ? NotifyResultNew.create(Notifier.createChanged(sectionToExpand.getSubheaderPosition())) : NotifyResultNew.empty();
        }

        sectionToExpand.setExpanded(true);

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section = sections.get(i);
            section.setSubheaderPosition(section.getSubheaderPosition() + sectionToExpand.getItemCount());
        }

        final List<Notifier> notifiers = new ArrayList<>();

        if (notifySubheader) {
            notifiers.add(Notifier.createChanged(sectionToExpand.getSubheaderPosition()));
        }

        notifiers.add(Notifier.createInserted(sectionToExpand.getSubheaderPosition() + 1, sectionToExpand.getItemCount()));

        return NotifyResultNew.create(notifiers);

    }

    //TODO  REMOVE
    int expandSection(@IntRange(from = 0, to = Integer.MAX_VALUE) Integer sectionIndex) {

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

    NotifyResultNew expandAllSections(boolean notifySectionSubheaders) {
        final List<Notifier> notifiers = new ArrayList<>();
        for (int i = 0; i < sections.size(); i++) {

            final Section sectionToExpand = sections.get(i);

            if (sectionToExpand.isExpanded()) {
                if (notifySectionSubheaders) {
                    notifiers.add(Notifier.createChanged(sectionToExpand.getSubheaderPosition()));
                }
                continue;
            }

            sectionToExpand.setExpanded(true);

            if (i != 0) {
                final Section previousSection = sections.get(i - 1);
                sectionToExpand.setSubheaderPosition(previousSection.getSubheaderPosition() + previousSection.getItemCount() + 1);
            }

            final List<Notifier> expandSectionNotifiers = new ArrayList<>();

            if (notifySectionSubheaders) {
                expandSectionNotifiers.add(Notifier.createChanged(sectionToExpand.getSubheaderPosition()));
            }

            expandSectionNotifiers.add(Notifier.createInserted(sectionToExpand.getSubheaderPosition() + 1, sectionToExpand.getItemCount()));

            notifiers.addAll(expandSectionNotifiers);

        }
        return NotifyResultNew.create(notifiers);
    }

    NotifyResultNew collapseSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex, boolean notifySubheader) {

        final Section sectionToCollapse = sections.get(sectionIndex);

        if (!sectionToCollapse.isExpanded()) {
            return notifySubheader ? NotifyResultNew.create(Notifier.createChanged(sectionToCollapse.getSubheaderPosition())) : NotifyResultNew.empty();
        }

        sectionToCollapse.setExpanded(false);

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section = sections.get(i);
            section.setSubheaderPosition(section.getSubheaderPosition() - sectionToCollapse.getItemCount());
        }

        final List<Notifier> notifiers = new ArrayList<>();

        if (notifySubheader) {
            notifiers.add(Notifier.createChanged(sectionToCollapse.getSubheaderPosition()));
        }

        notifiers.add(Notifier.createRemoved(sectionToCollapse.getSubheaderPosition() + 1, sectionToCollapse.getItemCount()));

        return NotifyResultNew.create(notifiers);

    }

    //TODO  REMOVE
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

    NotifyResultNew collapseAllSections(boolean notifySectionSubheaders) {
        final List<Notifier> notifiers = new ArrayList<>();
        for (int i = 0; i < sections.size(); i++) {

            final Section sectionToCollapse = sections.get(i);

            sectionToCollapse.setSubheaderPosition(i);

            if (!sectionToCollapse.isExpanded()) {
                if (notifySectionSubheaders) {
                    notifiers.add(Notifier.createChanged(i));
                }
                continue;
            }

            sectionToCollapse.setExpanded(false);

            final List<Notifier> collapseSectionNotifiers = new ArrayList<>();

            if (notifySectionSubheaders) {
                collapseSectionNotifiers.add(Notifier.createChanged(i));
            }

            collapseSectionNotifiers.add(Notifier.createRemoved(i + 1, sectionToCollapse.getItemCount()));

            notifiers.addAll(collapseSectionNotifiers);

        }
        return NotifyResultNew.create(notifiers);
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

    //TODO РАЗОБРАТЬСЯ С ASSERTIONS
    int getAdapterPositionForItem(int itemPosition, boolean ignoreCollapsing) {

        if (itemPosition < 0 || itemPosition >= getDataItemCount()) {
            throw new IllegalArgumentException("itemPosition: " + itemPosition + ", itemCount: " + getDataItemCount());
        }

        if (!ignoreCollapsing && !sections.get(sectionIndexByItemPosition(itemPosition)).isExpanded()) {
            return -1;
        }

        int adapterPosition = 0;

        int itemCount = 0;

        for (Section section : sections) {

            adapterPosition += 1;

            if (!ignoreCollapsing && !section.isExpanded()) {
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

    int getAdapterPositionForItem(int itemPosition) {
        return getAdapterPositionForItem(itemPosition, false);
    }

    NotifyResultNew onItemChanged(int itemPosition) {

        final int adapterPosition = getAdapterPositionForItem(itemPosition, true);

        final int sectionIndex = sectionIndexByItemPosition(itemPosition);

        final Section section = sections.get(sectionIndex);

        final int positionInSection = positionInSectionByItemPosition(itemPosition);

        final List<Notifier> notifiers = new ArrayList<>();

        if (itemPosition == 0) {

            final boolean oldState = section.getItemCount() == 1;

            final boolean newState = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition);

            if (oldState == newState) {
                if (section.isExpanded()) {
                    return NotifyResultNew.create(Notifier.createChanged(0, 2));
                } else {
                    return NotifyResultNew.create(Notifier.createChanged(0));
                }
            } else {

                if (!oldState && newState) {

                    //TODO если collapsed, то будут другие subheader positions
                    final Section firstSplitSection = Section.create(0, 1);
                    final Section secondSplitSection = Section.create(2, section.getItemCount() - 1);

                    removeSection(section);

                    addSection(sectionIndex, firstSplitSection);
                    addSection(sectionIndex + 1, secondSplitSection);

                    if (section.isExpanded()) {
                        return NotifyResultNew.create(
                                Arrays.asList(
                                        Notifier.createChanged(0, 2),
                                        Notifier.createInserted(2)
                                )
                        );
                    } else {
                        return NotifyResultNew.create(
                                Arrays.asList(
                                        Notifier.createChanged(0),
                                        Notifier.createInserted(1)
                                )
                        );
                    }

                } else {

                    //TODO если collapsed, то будут другие subheader positions
                    final Section nextSection = sections.get(sectionIndex + 1);

                    final Section newSection = Section.create(0, nextSection.getItemCount() + 1);

                    removeSection(section);
                    removeSection(nextSection);

                    addSection(0, newSection);

                    if (nextSection.isExpanded()) {
                        return NotifyResultNew.create(
                                Arrays.asList(
                                        Notifier.createChanged(0, 2),
                                        Notifier.createRemoved(2)
                                )
                        );
                    } else {
                        return NotifyResultNew.create(
                                Arrays.asList(
                                        Notifier.createChanged(0),
                                        Notifier.createRemoved(1, 2)
                                )
                        );
                    }


                }

            }

        } else if (itemPosition == sectionProvider.getItemSize() - 1) {

            final boolean oldState = section.getItemCount() == 1;

            final boolean newState = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1);

            if (oldState == newState) {

                if (section.getItemCount() == 1) {
                    if (section.isExpanded()) {
                        return NotifyResultNew.create(
                                Notifier.createChanged(section.getSubheaderPosition(), 2)
                        );
                    } else {
                        return NotifyResultNew.create(
                                Notifier.createChanged(section.getSubheaderPosition())
                        );
                    }
                } else {
                    if (section.isExpanded()) {
                        return NotifyResultNew.create(Arrays.asList(
                                Notifier.createChanged(section.getSubheaderPosition()),
                                Notifier.createChanged(adapterPosition)
                        ));
                    } else {
                        return NotifyResultNew.create(
                                Notifier.createChanged(section.getSubheaderPosition())
                        );
                    }
                }

            } else {

                if (!oldState && newState) {

                    //TODO если collapsed, то будут другие subheader positions
                    final Section firstSplitSection = Section.create(section.getSubheaderPosition(), section.getItemCount() - 1);
                    final Section secondSplitSection = Section.create(adapterPosition, 1);

                    removeSection(section);

                    addSection(sectionIndex, firstSplitSection);
                    addSection(sectionIndex + 1, secondSplitSection);

                    if (section.isExpanded()) {
                        return NotifyResultNew.create(
                                Arrays.asList(
                                        Notifier.createChanged(section.getSubheaderPosition(), 1),
                                        Notifier.createChanged(adapterPosition),
                                        Notifier.createInserted(adapterPosition + 1)
                                )
                        );
                    } else {
                        return NotifyResultNew.create(
                                Arrays.asList(
                                        Notifier.createChanged(section.getSubheaderPosition()),
                                        Notifier.createInserted(section.getSubheaderPosition() + 1)
                                )
                        );
                    }

                } else {

                    //TODO если collapsed, то будут другие subheader positions
                    removeSection(section);

                    final Section previousSection = sections.get(sectionIndex - 1);

                    previousSection.setItemCount(previousSection.getItemCount() + 1);

                    if (previousSection.isExpanded()) {
                        return NotifyResultNew.create(
                                Arrays.asList(
                                        Notifier.createChanged(previousSection.getSubheaderPosition()),
                                        Notifier.createChanged(section.getSubheaderPosition()),
                                        Notifier.createRemoved(section.getSubheaderPosition() + 1)
                                )
                        );
                    } else {
                        return NotifyResultNew.create(
                                Arrays.asList(
                                        Notifier.createChanged(previousSection.getSubheaderPosition()),
                                        Notifier.createRemoved(section.getSubheaderPosition(), 2)
                                )
                        );
                    }

                }

            }

        }

        final Section previousItemSection = sections.get(sectionIndexByItemPosition(itemPosition - 1));
        final Section nextItemSection = sections.get(sectionIndexByItemPosition(itemPosition + 1));

        final boolean oldState0 = section != previousItemSection;
        final boolean oldState1 = section != nextItemSection;

        final boolean newState0 = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1);
        final boolean newState1 = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition);

        if (oldState0 == newState0 && oldState1 == newState1) {

            if (section.getItemCount() == 1) {
                if (section.isExpanded()) {
                    return NotifyResultNew.create(
                            Notifier.createChanged(section.getSubheaderPosition(), 2)
                    );
                } else {
                    return NotifyResultNew.create(
                            Notifier.createChanged(section.getSubheaderPosition())
                    );
                }
            }

            if (section.isExpanded()) {
                return NotifyResultNew.create(
                        Arrays.asList(
                                Notifier.createChanged(section.getSubheaderPosition()),
                                Notifier.createChanged(adapterPosition)
                        )
                );
            } else {
                return NotifyResultNew.create(
                        Notifier.createChanged(section.getSubheaderPosition())
                );
            }

        }

        if (!oldState0 && !oldState1) {

            //TODO если collapsed, то будут другие subheader positions
            final Section firstSplitSection = Section.create(section.getSubheaderPosition(), positionInSection);
            final Section secondSplitSection = Section.create(section.getSubheaderPosition() + positionInSection + 1, 1);
            final Section thirdSplitSection = Section.create(secondSplitSection.getSubheaderPosition() + 2, section.getItemCount() - positionInSection - 1);

            removeSection(section);

            addSection(sectionIndex, firstSplitSection);
            addSection(sectionIndex + 1, secondSplitSection);
            addSection(sectionIndex + 2, thirdSplitSection);

            if (section.isExpanded()) {
                return NotifyResultNew.create(
                        Arrays.asList(
                                Notifier.createChanged(firstSplitSection.getSubheaderPosition()),
                                Notifier.createChanged(adapterPosition, 1),
                                Notifier.createInserted(adapterPosition + 1, 2)
                        )
                );
            } else {
                return NotifyResultNew.create(
                        Arrays.asList(
                                Notifier.createChanged(firstSplitSection.getSubheaderPosition()),
                                Notifier.createInserted(firstSplitSection.getSubheaderPosition() + 1, 2)
                        )
                );
            }

        }

        if (oldState0 && !oldState1) {

            //TODO если collapsed, то будут другие subheader positions
            previousItemSection.setItemCount(previousItemSection.getItemCount() + 1);

            section.setSubheaderPosition(section.getSubheaderPosition() + 1);
            section.setItemCount(section.getItemCount() - 1);

            if (previousItemSection.isExpanded()) {
                return NotifyResultNew.create(
                        Arrays.asList(
                                Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                Notifier.createChanged(adapterPosition - 1, 2)
                        )
                );
            } else {
                return NotifyResultNew.create(
                        Notifier.createChanged(previousItemSection.getSubheaderPosition(), 2)
                );
            }

        }

        if (!oldState0 && oldState1) {

            //TODO если collapsed, то будут другие subheader positions
            nextItemSection.setItemCount(nextItemSection.getItemCount() + 1);
            nextItemSection.setSubheaderPosition(nextItemSection.getSubheaderPosition() - 1);

            section.setItemCount(section.getItemCount() - 1);

            if (section.isExpanded() && nextItemSection.isExpanded()) {
                return NotifyResultNew.create(Arrays.asList(
                        Notifier.createChanged(section.getSubheaderPosition()),
                        Notifier.createChanged(adapterPosition, 2)
                ));
            } else if (section.isExpanded()) {
                return NotifyResultNew.create(Arrays.asList(
                        Notifier.createChanged(section.getSubheaderPosition()),
                        Notifier.createChanged(adapterPosition),
                        Notifier.createRemoved(adapterPosition + 1)
                ));
            } else if (nextItemSection.isExpanded()) {
                return NotifyResultNew.create(Arrays.asList(
                        Notifier.createChanged(section.getSubheaderPosition()),
                        Notifier.createChanged(nextItemSection.getSubheaderPosition()),
                        Notifier.createInserted(nextItemSection.getSubheaderPosition() + 1)
                ));
            } else {
                return NotifyResultNew.create(
                        Notifier.createChanged(section.getSubheaderPosition(), 2)
                );
            }

        }

        if (oldState0 && oldState1) {

            if (!newState0 && !newState1) {

                final Section newSection = Section.create(previousItemSection.getSubheaderPosition(), previousItemSection.getItemCount() + 1 + nextItemSection.getItemCount());

                removeSection(previousItemSection);
                removeSection(section);
                removeSection(nextItemSection);

                addSection(sectionIndex - 1, newSection);

                if (previousItemSection.isExpanded()) {
                    return NotifyResultNew.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                    Notifier.createChanged(adapterPosition - 1),
                                    Notifier.createRemoved(adapterPosition, 2)
                            )
                    );
                } else {
                    return NotifyResultNew.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                    Notifier.createChanged(adapterPosition - 1),
                                    Notifier.createRemoved(adapterPosition, 2)
                            )
                    );
                }

            }

            if (!newState0 && newState1) {

                section.setItemCount(0);
                removeSection(section);

                previousItemSection.setItemCount(previousItemSection.getItemCount() + 1);

                nextItemSection.setSubheaderPosition(adapterPosition);

                return NotifyResultNew.create(
                        Arrays.asList(
                                Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                Notifier.createChanged(section.getSubheaderPosition(), 1),
                                Notifier.createRemoved(section.getSubheaderPosition() + 1)
                        )
                );

            }

            if (newState0 && !newState1) {

                final Section newSection = Section.create(section.getSubheaderPosition(), nextItemSection.getItemCount() + 1);

                removeSection(section);
                removeSection(nextItemSection);

                addSection(sectionIndex, newSection);

                return NotifyResultNew.create(
                        Arrays.asList(
                                Notifier.createChanged(section.getSubheaderPosition(), 2),
                                Notifier.createRemoved(section.getSubheaderPosition() + 2)
                        )
                );

            }

        }

        return NotifyResultNew.create(notifiers);

    }

    //TODO А ЧТО ЕСЛИ СЕКЦИЯ COLLAPSED?
    NotifyResultNew onItemRemoved(Integer itemPosition) {
        return null;
    }

    //TODO А ЧТО ЕСЛИ СЕКЦИЯ COLLAPSED?
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
            section.setItemCount(section.getItemCount() - 1);
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

    //TODO А ЧТО ЕСЛИ СЕКЦИЯ COLLAPSED?
    NotifyResultNew onItemInserted(int itemPosition) {
        return null;
    }

    //TODO А ЧТО ЕСЛИ СЕКЦИЯ COLLAPSED?
    NotifyItemInsertedResult onItemInserted(Integer itemPosition) {
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
            newSection.setItemCount(1);

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
            currentSection.setItemCount(currentSection.getItemCount() + 1);

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

        if (itemPosition < 0 || itemPosition > sectionProvider.getItemSize() - 1) {
            throw new IllegalArgumentException("itemPosition: " + itemPosition + ", itemCount:" + sectionProvider.getItemSize());
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

    int positionInSectionByItemPosition(int itemPosition) {

        if (itemPosition < 0 || itemPosition > sectionProvider.getItemSize() - 1) {
            throw new IllegalArgumentException("itemPosition: " + itemPosition + ", itemCount:" + sectionProvider.getItemSize());
        }

        final int sectionIndex = sectionIndexByItemPosition(itemPosition);

        int itemCount = 0;

        for (int i = 0; i < sectionIndex; i++) {
            itemCount += sections.get(i).getItemCount();
        }

        return itemPosition - itemCount;

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
