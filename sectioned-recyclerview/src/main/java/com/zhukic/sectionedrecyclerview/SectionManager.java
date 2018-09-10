package com.zhukic.sectionedrecyclerview;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    NotifyResult expandSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {
        final Section sectionToExpand = sections.get(sectionIndex);

        if (sectionToExpand.isExpanded()) {
            return NotifyResult.empty();
        }

        sectionToExpand.setExpanded(true);

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section = sections.get(i);
            section.setSubheaderPosition(section.getSubheaderPosition() + sectionToExpand.getItemCount());
        }

        final List<Notifier> notifiers = new ArrayList<>();
        notifiers.add(Notifier.createChanged(sectionToExpand.getSubheaderPosition()));
        notifiers.add(Notifier.createInserted(sectionToExpand.getSubheaderPosition() + 1, sectionToExpand.getItemCount()));

        return NotifyResult.create(notifiers);
    }

    NotifyResult expandAllSections() {
        final List<Notifier> notifiers = new ArrayList<>();

        for (int i = 0; i < sections.size(); i++) {
            final Section sectionToExpand = sections.get(i);
            if (sectionToExpand.isExpanded()) {
                continue;
            }

            sectionToExpand.setExpanded(true);

            if (i != 0) {
                final Section previousSection = sections.get(i - 1);
                sectionToExpand.setSubheaderPosition(previousSection.getSubheaderPosition() + previousSection.getItemCount() + 1);
            }

            final List<Notifier> expandSectionNotifiers = new ArrayList<>();
            expandSectionNotifiers.add(Notifier.createChanged(sectionToExpand.getSubheaderPosition()));
            expandSectionNotifiers.add(Notifier.createInserted(sectionToExpand.getSubheaderPosition() + 1, sectionToExpand.getItemCount()));

            notifiers.addAll(expandSectionNotifiers);
        }

        return NotifyResult.create(notifiers);
    }

    NotifyResult collapseSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {
        final Section sectionToCollapse = sections.get(sectionIndex);

        if (!sectionToCollapse.isExpanded()) {
            return NotifyResult.empty();
        }

        sectionToCollapse.setExpanded(false);

        for (int i = sectionIndex + 1; i < sections.size(); i++) {
            final Section section = sections.get(i);
            section.setSubheaderPosition(section.getSubheaderPosition() - sectionToCollapse.getItemCount());
        }

        return NotifyResult.create(Arrays.asList(
                Notifier.createChanged(sectionToCollapse.getSubheaderPosition()),
                Notifier.createRemoved(sectionToCollapse.getSubheaderPosition() + 1, sectionToCollapse.getItemCount())
        ));
    }

    NotifyResult collapseAllSections() {
        final List<Notifier> notifiers = new ArrayList<>();
        for (int i = 0; i < sections.size(); i++) {
            final Section sectionToCollapse = sections.get(i);
            sectionToCollapse.setSubheaderPosition(i);
            if (!sectionToCollapse.isExpanded()) {
                continue;
            }

            sectionToCollapse.setExpanded(false);

            final List<Notifier> collapseSectionNotifiers = new ArrayList<>();
            collapseSectionNotifiers.add(Notifier.createChanged(i));
            collapseSectionNotifiers.add(Notifier.createRemoved(i + 1, sectionToCollapse.getItemCount()));

            notifiers.addAll(collapseSectionNotifiers);
        }
        return NotifyResult.create(notifiers);
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

    NotifyResult onItemChanged(int itemPosition) {
        final int adapterPosition = getAdapterPositionForItem(itemPosition, false);

        final int sectionIndex = sectionIndexByItemPosition(itemPosition);

        final Section section = sections.get(sectionIndex);

        final int positionInSection = positionInSectionByItemPosition(itemPosition);

        if (getDataItemCount() == 1) {
            if (section.isExpanded()) {
                return NotifyResult.create(Notifier.createChanged(0, 2));
            } else {
                return NotifyResult.create(Notifier.createChanged(0));
            }
        }

        if (itemPosition == 0) {
            final boolean oldState = section.getItemCount() == 1;

            final boolean newState = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition);

            if (oldState == newState) {
                if (section.isExpanded()) {
                    return NotifyResult.create(Notifier.createChanged(0, 2));
                } else {
                    return NotifyResult.create(Notifier.createChanged(0));
                }
            } else {
                if (!oldState && newState) {
                    final Section firstSplitSection;
                    final Section secondSplitSection;

                    if (section.isExpanded()) {
                        firstSplitSection = Section.create(0, 1, true);
                        secondSplitSection = Section.create(2, section.getItemCount() - 1, true);
                    } else {
                        firstSplitSection = Section.create(0, 1, false);
                        secondSplitSection = Section.create(1, section.getItemCount() - 1, false);
                    }

                    removeSection(section);

                    addSection(sectionIndex, firstSplitSection);
                    addSection(sectionIndex + 1, secondSplitSection);

                    if (section.isExpanded()) {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(0, 2),
                                        Notifier.createInserted(2)
                                )
                        );
                    } else {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(0),
                                        Notifier.createInserted(1)
                                )
                        );
                    }
                } else {
                    final Section nextSection = sections.get(sectionIndex + 1);
                    final Section newSection = Section.create(0, nextSection.getItemCount() + 1, nextSection.isExpanded());

                    removeSection(section);
                    removeSection(nextSection);

                    addSection(0, newSection);

                    if (section.isExpanded() && nextSection.isExpanded()) {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(0, 2),
                                        Notifier.createRemoved(2)
                                )
                        );
                    } else if (section.isExpanded() && !nextSection.isExpanded()) {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(0),
                                        Notifier.createRemoved(1, 2)
                                )
                        );
                    } else if (!section.isExpanded() && nextSection.isExpanded()) {
                        return NotifyResult.create(Notifier.createChanged(0, 2));
                    } else {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(0),
                                        Notifier.createRemoved(1)
                                )
                        );
                    }
                }
            }
        } else if (itemPosition == getDataItemCount() - 1) {
            final boolean oldState = section.getItemCount() == 1;

            final boolean newState = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1);

            if (oldState == newState) {
                if (section.isExpanded()) {
                    if (positionInSection == 0) {
                        return NotifyResult.create(
                                Notifier.createChanged(section.getSubheaderPosition(), 2)
                        );
                    } else {
                        return NotifyResult.create(Arrays.asList(
                                Notifier.createChanged(section.getSubheaderPosition()),
                                //TODO потестить такие кейсы, когда все секции collapsed кроме этой на предмет adapterPosition
                                Notifier.createChanged(adapterPosition)
                        ));
                    }
                } else {
                    return NotifyResult.create(
                            Notifier.createChanged(section.getSubheaderPosition())
                    );
                }
            } else {
                if (!oldState && newState) {
                    final Section firstSplitSection;
                    final Section secondSplitSection;

                    if (section.isExpanded()) {
                        firstSplitSection = Section.create(section.getSubheaderPosition(), section.getItemCount() - 1, true);
                        secondSplitSection = Section.create(adapterPosition, 1);
                    } else {
                        firstSplitSection = Section.create(section.getSubheaderPosition(), section.getItemCount() - 1, false);
                        secondSplitSection = Section.create(firstSplitSection.getSubheaderPosition() + 1, 1, false);
                    }

                    removeSection(section);

                    addSection(sectionIndex, firstSplitSection);
                    addSection(sectionIndex + 1, secondSplitSection);

                    if (section.isExpanded()) {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(section.getSubheaderPosition(), 1),
                                        Notifier.createChanged(adapterPosition),
                                        Notifier.createInserted(adapterPosition + 1)
                                )
                        );
                    } else {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(section.getSubheaderPosition()),
                                        Notifier.createInserted(section.getSubheaderPosition() + 1)
                                )
                        );
                    }
                } else {
                    removeSection(section);

                    final Section previousSection = sections.get(sectionIndex - 1);

                    previousSection.setItemCount(previousSection.getItemCount() + 1);

                    if (section.isExpanded() && previousSection.isExpanded()) {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(previousSection.getSubheaderPosition()),
                                        Notifier.createChanged(section.getSubheaderPosition()),
                                        Notifier.createRemoved(section.getSubheaderPosition() + 1)
                                )
                        );
                    } else if (section.isExpanded() && !previousSection.isExpanded()) {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(previousSection.getSubheaderPosition()),
                                        Notifier.createRemoved(section.getSubheaderPosition(), 2)
                                )
                        );
                    } else if (!section.isExpanded() && previousSection.isExpanded()) {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(previousSection.getSubheaderPosition()),
                                        Notifier.createChanged(section.getSubheaderPosition())
                                )
                        );
                    } else {
                        return NotifyResult.create(
                                Arrays.asList(
                                        Notifier.createChanged(previousSection.getSubheaderPosition()),
                                        Notifier.createRemoved(section.getSubheaderPosition())
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
            if (section.isExpanded()) {
                if (positionInSection(adapterPosition) == 0) {
                    return NotifyResult.create(
                            Notifier.createChanged(section.getSubheaderPosition(), 2)
                    );
                } else {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(section.getSubheaderPosition()),
                                    Notifier.createChanged(adapterPosition)
                            )
                    );
                }
            } else {
                return NotifyResult.create(
                        Notifier.createChanged(section.getSubheaderPosition())
                );
            }
        }

        if (!oldState0 && !oldState1) {
            final Section firstSplitSection;
            final Section secondSplitSection;
            final Section thirdSplitSection;

            if (section.isExpanded()) {
                firstSplitSection = Section.create(section.getSubheaderPosition(), positionInSection, true);
                secondSplitSection = Section.create(section.getSubheaderPosition() + positionInSection + 1, 1, true);
                thirdSplitSection = Section.create(secondSplitSection.getSubheaderPosition() + 2, section.getItemCount() - positionInSection - 1, true);
            } else {
                firstSplitSection = Section.create(section.getSubheaderPosition(), positionInSection, false);
                secondSplitSection = Section.create(section.getSubheaderPosition() + 1, 1, false);
                thirdSplitSection = Section.create(section.getSubheaderPosition() + 2, section.getItemCount() - positionInSection - 1, false);
            }

            removeSection(section);

            addSection(sectionIndex, firstSplitSection);
            addSection(sectionIndex + 1, secondSplitSection);
            addSection(sectionIndex + 2, thirdSplitSection);

            if (section.isExpanded()) {
                return NotifyResult.create(
                        Arrays.asList(
                                Notifier.createChanged(firstSplitSection.getSubheaderPosition()),
                                Notifier.createChanged(adapterPosition, 1),
                                Notifier.createInserted(adapterPosition + 1, 2)
                        )
                );
            } else {
                return NotifyResult.create(
                        Arrays.asList(
                                Notifier.createChanged(firstSplitSection.getSubheaderPosition()),
                                Notifier.createInserted(firstSplitSection.getSubheaderPosition() + 1, 2)
                        )
                );
            }
        }
        if (!oldState0 && oldState1) {
            if (newState0 && !newState1) {
                nextItemSection.setItemCount(nextItemSection.getItemCount() + 1);

                if (section.isExpanded()) {
                    //TODO в следующих секциях сдвига нет
                    nextItemSection.setSubheaderPosition(nextItemSection.getSubheaderPosition() - 1);
                } else {
                    //TODO что-то мне кажется, что и тут что-то должно быть
                }

                section.setItemCount(section.getItemCount() - 1);

                if (section.isExpanded() && nextItemSection.isExpanded()) {
                    return NotifyResult.create(Arrays.asList(
                            Notifier.createChanged(section.getSubheaderPosition()),
                            Notifier.createChanged(adapterPosition, 2)
                    ));
                } else if (section.isExpanded() && !nextItemSection.isExpanded()) {
                    return NotifyResult.create(Arrays.asList(
                            Notifier.createChanged(section.getSubheaderPosition()),
                            Notifier.createChanged(adapterPosition),
                            Notifier.createRemoved(adapterPosition + 1)
                    ));
                } else if (!section.isExpanded() && nextItemSection.isExpanded()) {
                    return NotifyResult.create(Arrays.asList(
                            Notifier.createChanged(section.getSubheaderPosition()),
                            Notifier.createChanged(section.getSubheaderPosition() + 1),
                            Notifier.createInserted(section.getSubheaderPosition() + 2)
                    ));
                } else {
                    return NotifyResult.create(Notifier.createChanged(section.getSubheaderPosition(), 2));
                }
            } else {
                final Section firstSplitSection;
                final Section secondSplitSection;

                if (section.isExpanded()) {
                    firstSplitSection = Section.create(section.getSubheaderPosition(), section.getItemCount() - 1, true);
                    secondSplitSection = Section.create(adapterPosition, 1, true);
                } else {
                    firstSplitSection = Section.create(section.getSubheaderPosition(), section.getItemCount() - 1, false);
                    secondSplitSection = Section.create(section.getSubheaderPosition() + 1, 1, false);
                }

                removeSection(section);

                addSection(sectionIndex, firstSplitSection);
                addSection(sectionIndex + 1, secondSplitSection);

                if (section.isExpanded()) {
                    return NotifyResult.create(Arrays.asList(
                            Notifier.createChanged(section.getSubheaderPosition()),
                            Notifier.createChanged(adapterPosition),
                            Notifier.createInserted(adapterPosition + 1)
                    ));
                } else {
                    return NotifyResult.create(Arrays.asList(
                            Notifier.createChanged(section.getSubheaderPosition()),
                            Notifier.createInserted(section.getSubheaderPosition() + 1)
                    ));
                }
            }
        }

        if (oldState0 && !oldState1) {
            if (!newState0 && newState1) {
                previousItemSection.setItemCount(previousItemSection.getItemCount() + 1);

                if (previousItemSection.isExpanded() && section.isExpanded()) {
                    section.setSubheaderPosition(section.getSubheaderPosition() + 1);
                } else if (previousItemSection.isExpanded() && !section.isExpanded()) {
                    for (int i = sectionIndex; i < sections.size(); i++) {
                        final Section s = sections.get(i);
                        s.setSubheaderPosition(s.getSubheaderPosition() + 1);
                    }
                } else if (!previousItemSection.isExpanded() && section.isExpanded()) {
                    for (int i = sectionIndex + 1; i < sections.size(); i++) {
                        final Section s = sections.get(i);
                        s.setSubheaderPosition(s.getSubheaderPosition() - 1);
                    }
                }

                section.setItemCount(section.getItemCount() - 1);

                if (section.isExpanded() && previousItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                    Notifier.createChanged(adapterPosition - 1, 2)
                            )
                    );
                } else if (section.isExpanded() && !previousItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition(), 2),
                                    Notifier.createRemoved(previousItemSection.getSubheaderPosition() + 2)
                            )
                    );
                } else if (!section.isExpanded() && previousItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                    Notifier.createChanged(section.getSubheaderPosition() - 1),
                                    Notifier.createInserted(section.getSubheaderPosition())
                            )
                    );
                } else {
                    return NotifyResult.create(Notifier.createChanged(previousItemSection.getSubheaderPosition(), 2));
                }
            } else {
                final Section firstSplitSection;
                final Section secondSplitSection;

                if (section.isExpanded()) {
                    firstSplitSection = Section.create(section.getSubheaderPosition(), 1, true);
                    secondSplitSection = Section.create(section.getSubheaderPosition() + 2, section.getItemCount() - 1, true);
                } else {
                    firstSplitSection = Section.create(section.getSubheaderPosition(), 1, false);
                    secondSplitSection = Section.create(section.getSubheaderPosition() + 1, section.getItemCount() - 1, false);
                }

                removeSection(section);

                addSection(sectionIndex, firstSplitSection);
                addSection(sectionIndex + 1, secondSplitSection);

                if (section.isExpanded()) {
                    return NotifyResult.create(Arrays.asList(
                            Notifier.createChanged(section.getSubheaderPosition(), 2),
                            Notifier.createInserted(section.getSubheaderPosition() + 2)
                    ));
                } else {
                    return NotifyResult.create(Arrays.asList(
                            Notifier.createChanged(section.getSubheaderPosition()),
                            Notifier.createInserted(section.getSubheaderPosition() + 1)
                    ));
                }
            }
        }

        if (oldState0 && oldState1) {
            if (newState0 && !newState1) {
                final Section newSection = Section.create(section.getSubheaderPosition(), nextItemSection.getItemCount() + 1, nextItemSection.isExpanded());

                removeSection(section);
                removeSection(nextItemSection);

                addSection(sectionIndex, newSection);

                if (section.isExpanded() && nextItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(section.getSubheaderPosition(), 2),
                                    Notifier.createRemoved(section.getSubheaderPosition() + 2)
                            )
                    );
                } else if (section.isExpanded() && !nextItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(section.getSubheaderPosition()),
                                    Notifier.createRemoved(section.getSubheaderPosition() + 1, 2)
                            )
                    );
                } else if (!section.isExpanded() && nextItemSection.isExpanded()) {
                    return NotifyResult.create(Notifier.createChanged(section.getSubheaderPosition(), 2));
                } else {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(section.getSubheaderPosition()),
                                    Notifier.createRemoved(section.getSubheaderPosition() + 1)
                            )
                    );
                }
            }

            if (!newState0 && newState1) {
                final int sectionSubheaderPosition = section.getSubheaderPosition();

                removeSection(previousItemSection);
                removeSection(section);

                final Section newSection = new Section(previousItemSection.getSubheaderPosition(), previousItemSection.getItemCount() + 1, previousItemSection.isExpanded());
                addSection(sectionIndex - 1, newSection);

                if (section.isExpanded() && previousItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                    Notifier.createChanged(sectionSubheaderPosition),
                                    Notifier.createRemoved(sectionSubheaderPosition + 1)
                            )
                    );
                } else if (section.isExpanded() && !previousItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                    Notifier.createRemoved(sectionSubheaderPosition, 2)
                            )
                    );
                } else if (!section.isExpanded() && previousItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                    Notifier.createChanged(sectionSubheaderPosition)
                            )
                    );
                } else {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousItemSection.getSubheaderPosition()),
                                    Notifier.createRemoved(sectionSubheaderPosition)
                            )
                    );
                }
            }
            if (!newState0 && !newState1) {
                final Section newSection = Section.create(previousItemSection.getSubheaderPosition(), previousItemSection.getItemCount() + 1 + nextItemSection.getItemCount(), previousItemSection.isExpanded());

                final int previousSectionSubheaderPosition = previousItemSection.getSubheaderPosition();
                final int sectionSubheaderPosition = section.getSubheaderPosition();
                final int nextSectionSubheaderPosition = nextItemSection.getSubheaderPosition();

                removeSection(previousItemSection);
                removeSection(section);
                removeSection(nextItemSection);

                addSection(sectionIndex - 1, newSection);

                if (section.isExpanded() && previousItemSection.isExpanded() && nextItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousSectionSubheaderPosition),
                                    Notifier.createChanged(sectionSubheaderPosition),
                                    Notifier.createRemoved(sectionSubheaderPosition + 1, 2)
                            )
                    );
                } else if (section.isExpanded() && previousItemSection.isExpanded() && !nextItemSection.isExpanded()) {
                    //TODO протестить в реале
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousSectionSubheaderPosition),
                                    Notifier.createChanged(sectionSubheaderPosition),
                                    Notifier.createRemoved(sectionSubheaderPosition + 1, 2),
                                    Notifier.createInserted(sectionSubheaderPosition + 1, nextItemSection.getItemCount())
                            )
                    );
                } else if (section.isExpanded() && !previousItemSection.isExpanded() && nextItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousSectionSubheaderPosition),
                                    Notifier.createRemoved(sectionSubheaderPosition, nextItemSection.getItemCount() + 3)
                            )
                    );
                } else if (section.isExpanded() && !previousItemSection.isExpanded() && !nextItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousSectionSubheaderPosition),
                                    Notifier.createRemoved(sectionSubheaderPosition, 3)
                            )
                    );
                } else if (!section.isExpanded() && previousItemSection.isExpanded() && nextItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousSectionSubheaderPosition),
                                    Notifier.createChanged(sectionSubheaderPosition),
                                    Notifier.createRemoved(nextSectionSubheaderPosition)
                            )
                    );
                } else if (!section.isExpanded() && previousItemSection.isExpanded() && !nextItemSection.isExpanded()) {
                    //TODO протестить в реале
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousSectionSubheaderPosition),
                                    Notifier.createRemoved(sectionSubheaderPosition, 2),
                                    Notifier.createInserted(sectionSubheaderPosition, nextItemSection.getItemCount() + 1)
                            )
                    );
                } else if (!section.isExpanded() && !previousItemSection.isExpanded() && nextItemSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousSectionSubheaderPosition),
                                    Notifier.createRemoved(sectionSubheaderPosition, nextItemSection.getItemCount() + 2)
                            )
                    );
                } else {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(previousSectionSubheaderPosition),
                                    Notifier.createRemoved(sectionSubheaderPosition, 2)
                            )
                    );
                }
            }
        }

        return NotifyResult.empty();
    }

    NotifyResult onItemRemoved(int itemPosition) {
        final int sectionIndex = sectionIndexByItemPosition(itemPosition);

        final Section section = sections.get(sectionIndex);

        if (section.getItemCount() == 1) {
            removeSection(section);

            if (section.isExpanded()) {
                return NotifyResult.create(
                        Notifier.createRemoved(section.getSubheaderPosition(), 2)
                );
            } else {
                return NotifyResult.create(
                        Notifier.createRemoved(section.getSubheaderPosition())
                );
            }
        } else {
            final int itemAdapterPosition = getAdapterPositionForItem(itemPosition, false);

            section.setItemCount(section.getItemCount() - 1);

            if (section.isExpanded()) {
                for (int i = sectionIndex + 1; i < sections.size(); i++) {
                    final Section s = sections.get(i);
                    s.setSubheaderPosition(s.getSubheaderPosition() - 1);
                }
            }

            if (section.isExpanded()) {
                return NotifyResult.create(
                        Arrays.asList(
                                Notifier.createChanged(section.getSubheaderPosition()),
                                Notifier.createRemoved(itemAdapterPosition)
                        )
                );
            } else {
                return NotifyResult.create(
                        Notifier.createChanged(section.getSubheaderPosition())
                );
            }
        }
    }

    NotifyResult onItemInserted(int itemPosition) {
        if (getDataItemCount() == 0) {
            final Section section = Section.create(0, 1);
            sections.add(section);
            return NotifyResult.create(Notifier.createInserted(0, 2));
        }

        if (itemPosition == 0) {
            if (sectionProvider.onPlaceSubheaderBetweenItems(itemPosition)) {
                final Section section = Section.create(0, 1);
                addSection(0, section);
                return NotifyResult.create(Notifier.createInserted(0, 2));
            } else {
                final Section currentSection = getSection(0);
                currentSection.setItemCount(currentSection.getItemCount() + 1);
                if (currentSection.isExpanded()) {
                    for (int i = 1; i < sections.size(); i++) {
                        final Section section = sections.get(i);
                        section.setSubheaderPosition(section.getSubheaderPosition() + 1);
                    }
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(0),
                                    Notifier.createInserted(1)
                            )
                    );
                }
                return NotifyResult.create(Notifier.createChanged(0));
            }
        }

        final int newItemCount = getDataItemCount() + 1;
        if (itemPosition == newItemCount - 1) {
            if (sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1)) {
                final Section section = Section.create(getItemCount(), 1);
                sections.add(section);
                return NotifyResult.create(Notifier.createInserted(section.getSubheaderPosition(), 2));
            } else {
                final Section currentSection = sections.get(sections.size() - 1);
                currentSection.setItemCount(currentSection.getItemCount() + 1);
                if (currentSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(currentSection.getSubheaderPosition()),
                                    Notifier.createInserted(currentSection.getSubheaderPosition() + currentSection.getItemCount())
                            )
                    );
                }
                return NotifyResult.create(
                        Notifier.createChanged(currentSection.getSubheaderPosition())
                );
            }
        }

        final boolean oldState = positionInSectionByItemPosition(itemPosition) == 0;

        if (!oldState) {
            final boolean newState = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1);

            final int positionInSection = positionInSectionByItemPosition(itemPosition);
            final int sectionIndex = sectionIndexByItemPosition(itemPosition);
            final Section currentSection = sections.get(sectionIndex);

            if (newState) {
                final Section firstSplitSection;
                final Section secondSplitSection;
                final Section thirdSplitSection;

                if (currentSection.isExpanded()) {
                    firstSplitSection = Section.create(currentSection.getSubheaderPosition(), positionInSection, true);
                    secondSplitSection = Section.create(currentSection.getSubheaderPosition() + positionInSection + 1, 1, true);
                    thirdSplitSection = Section.create(secondSplitSection.getSubheaderPosition() + 2, currentSection.getItemCount() - positionInSection, true);
                } else {
                    firstSplitSection = Section.create(currentSection.getSubheaderPosition(), positionInSection, false);
                    secondSplitSection = Section.create(currentSection.getSubheaderPosition() + 1, 1, false);
                    thirdSplitSection = Section.create(currentSection.getSubheaderPosition() + 2, currentSection.getItemCount() - positionInSection, false);
                }

                removeSection(currentSection);

                addSection(sectionIndex, firstSplitSection);
                addSection(sectionIndex + 1, secondSplitSection);
                addSection(sectionIndex + 2, thirdSplitSection);

                if (currentSection.isExpanded()) {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(firstSplitSection.getSubheaderPosition()),
                                    Notifier.createChanged(secondSplitSection.getSubheaderPosition(), 1),
                                    Notifier.createInserted(secondSplitSection.getSubheaderPosition() + 1, 2)
                            )
                    );
                } else {
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(firstSplitSection.getSubheaderPosition()),
                                    Notifier.createInserted(firstSplitSection.getSubheaderPosition() + 1, 2)
                            )
                    );
                }
            } else {
                currentSection.setItemCount(currentSection.getItemCount() + 1);

                if (currentSection.isExpanded()) {
                    for (int i = sectionIndex + 1; i < sections.size(); i++) {
                        final Section section = sections.get(i);
                        section.setSubheaderPosition(section.getSubheaderPosition() + 1);
                    }

                    final int adapterPosition = getAdapterPositionForItem(itemPosition);
                    return NotifyResult.create(
                            Arrays.asList(
                                    Notifier.createChanged(currentSection.getSubheaderPosition()),
                                    Notifier.createInserted(adapterPosition)
                            )
                    );
                } else {
                    return NotifyResult.create(Notifier.createChanged(currentSection.getSubheaderPosition()));
                }
            }
        }

        final boolean newState1 = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition - 1);
        final boolean newState2 = sectionProvider.onPlaceSubheaderBetweenItems(itemPosition);

        if (!newState1 && newState2) {
            final int sectionIndex = sectionIndexByItemPosition(itemPosition - 1);
            final Section currentSection = sections.get(sectionIndex);
            currentSection.setItemCount(currentSection.getItemCount() + 1);
            if (currentSection.isExpanded()) {
                for (int i = sectionIndex + 1; i < sections.size(); i++) {
                    final Section section = sections.get(i);
                    section.setSubheaderPosition(section.getSubheaderPosition() + 1);
                }
                return NotifyResult.create(
                        Arrays.asList(
                                Notifier.createChanged(currentSection.getSubheaderPosition()),
                                Notifier.createInserted(currentSection.getSubheaderPosition() + currentSection.getItemCount())
                        )
                );
            } else {
                return NotifyResult.create(Notifier.createChanged(currentSection.getSubheaderPosition()));
            }
        }

        if (newState1 && !newState2) {
            final int sectionIndex = sectionIndexByItemPosition(itemPosition);
            final Section currentSection = sections.get(sectionIndex);
            currentSection.setItemCount(currentSection.getItemCount() + 1);
            if (currentSection.isExpanded()) {
                for (int i = sectionIndex + 1; i < sections.size(); i++) {
                    final Section section = sections.get(i);
                    section.setSubheaderPosition(section.getSubheaderPosition() + 1);
                }
                return NotifyResult.create(
                        Arrays.asList(
                                Notifier.createChanged(currentSection.getSubheaderPosition()),
                                Notifier.createInserted(currentSection.getSubheaderPosition() + 1)
                        )
                );
            } else {
                return NotifyResult.create(Notifier.createChanged(currentSection.getSubheaderPosition()));
            }
        }

        final Section nextSection = sections.get(sectionIndexByItemPosition(itemPosition));
        final int nextSectionIndex = sections.indexOf(nextSection);
        final Section newSection = new Section(nextSection.getSubheaderPosition(), 1, true);
        addSection(nextSectionIndex, newSection);
        return NotifyResult.create(Notifier.createInserted(newSection.getSubheaderPosition(), 2));
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

    private int positionInSectionByItemPosition(int itemPosition) {
        if (itemPosition < 0 || itemPosition > getDataItemCount() - 1) {
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

    int getSectionSubheaderPosition(int sectionIndex) {
        return getSection(sectionIndex).getSubheaderPosition();
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

    void addSection(Section section) {
        sections.add(section);
    }

    private void addSection(int index, Section newSection) {
        for (int i = index; i < sections.size(); i++) {
            Section section = getSection(i);
            if (newSection.isExpanded()) {
                section.setSubheaderPosition(section.getSubheaderPosition() + newSection.getItemCount() + 1);
            } else {
                section.setSubheaderPosition(section.getSubheaderPosition() + 1);
            }
        }
        sections.add(index, newSection);
    }

    private void removeSection(Section section) {
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

    private Section getSection(@IntRange(from = 0, to = Integer.MAX_VALUE) int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= getSections().size()) {
            throw new IllegalArgumentException("sectionIndex: " + sectionIndex + ", size: " + getSections().size());
        }
        return sections.get(sectionIndex);
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    private int sectionIndexByItemPosition(@IntRange(from = 0, to = Integer.MAX_VALUE) int itemPosition) {
        if (itemPosition < 0 || itemPosition > getDataItemCount() - 1) {
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

    private int getDataItemCount() {
        int dataItemCount = 0;
        for (Section section : sections) {
            dataItemCount += section.getItemCount();
        }
        return dataItemCount;
    }
}
