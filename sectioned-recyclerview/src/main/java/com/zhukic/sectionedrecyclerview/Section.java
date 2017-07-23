package com.zhukic.sectionedrecyclerview;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

class Section {

    private int subheaderPosition;

    private int itemsCount;

    private boolean isExpanded;

    Section(int subheaderPosition) {
        this.subheaderPosition = subheaderPosition;
        this.isExpanded = true;
        this.itemsCount = 0;
    }

    int getSubheaderPosition() {
        return subheaderPosition;
    }

    void setSubheaderPosition(int subheaderPosition) {
        this.subheaderPosition = subheaderPosition;
    }

    int getItemCount() {
        return itemsCount;
    }

    void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    boolean isExpanded() {
        return isExpanded;
    }

    void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

}
