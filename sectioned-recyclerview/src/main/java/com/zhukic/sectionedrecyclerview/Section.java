package com.zhukic.sectionedrecyclerview;

class Section {

    private int subheaderPosition;

    private int itemCount;

    private boolean isExpanded;

    public static Section create(int subheaderPosition, int itemCount, boolean isExpanded) {
        return new Section(subheaderPosition, itemCount, isExpanded);
    }

    public static Section create(int subheaderPosition, int itemCount) {
        return create(subheaderPosition, itemCount, true);
    }

    Section(int subheaderPosition, int itemCount, boolean isExpanded) {
        this.subheaderPosition = subheaderPosition;
        this.itemCount = itemCount;
        this.isExpanded = isExpanded;
    }

    Section(int subheaderPosition) {
        this(subheaderPosition, 0, true);
    }

    int getSubheaderPosition() {
        return subheaderPosition;
    }

    void setSubheaderPosition(int subheaderPosition) {
        this.subheaderPosition = subheaderPosition;
    }

    int getItemCount() {
        return itemCount;
    }

    void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    boolean isExpanded() {
        return isExpanded;
    }

    void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (subheaderPosition != section.subheaderPosition) return false;
        if (itemCount != section.itemCount) return false;
        return isExpanded == section.isExpanded;
    }

    @Override
    public int hashCode() {
        int result = subheaderPosition;
        result = 31 * result + itemCount;
        result = 31 * result + (isExpanded ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Section{" +
                "subheaderPosition=" + subheaderPosition +
                ", itemCount=" + itemCount +
                ", isExpanded=" + isExpanded +
                '}';
    }

}
