package com.zhukic.sectionedrecyclerview;

interface SectionProvider {

    /**
     * Called when adapter needs to know whether to place subheader between two neighboring
     * items.
     *
     * @param position position of the first item to be compared.
     * @return true if you want to place subheader between two neighboring
     * items.
     */
    boolean onPlaceSubheaderBetweenItems(int position);

    /**
     * Returns the total number of items in your data set.
     *
     * @return the total number of items in your data set.
     */
    int getItemSize();

}
