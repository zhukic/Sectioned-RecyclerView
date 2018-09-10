package com.zhukic.sectionedrecyclerview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class SectionManagerTest {

    private SectionManager sectionManager;

    private SectionProvider sectionProvider;

    @Before
    public void beforeEachTest() {
        sectionProvider = Mockito.mock(SectionProvider.class);
        sectionManager = new SectionManager(sectionProvider);
    }

    @Test
    public void constructor() {
        assertTrue(sectionManager.getSections().isEmpty());
    }

    @Test
    public void init() {

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(true);
        when(sectionProvider.getItemSize()).thenReturn(9);

        sectionManager.init();

        //Subheader 0
        //Item 1
        //Item 2
        //Item 3
        //Item 4
        //Subheader 5
        //Item 6
        //Item 7
        //Subheader 8
        //Item 9
        //Item 10
        //Item 11

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, true),
                Section.create(5, 2, true),
                Section.create(8, 3, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void isSectionSubheaderOnPosition() {

        Section section1 = new Section(0);
        Section section2 = new Section(4);
        Section section3 = new Section(7);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        assertTrue(sectionManager.isSectionSubheaderOnPosition(0));
        assertTrue(sectionManager.isSectionSubheaderOnPosition(4));
        assertTrue(sectionManager.isSectionSubheaderOnPosition(7));

        assertFalse(sectionManager.isSectionSubheaderOnPosition(1));
        assertFalse(sectionManager.isSectionSubheaderOnPosition(5));
        assertFalse(sectionManager.isSectionSubheaderOnPosition(8));

    }

    @Test(expected = IllegalArgumentException.class)
    public void collapseSection_shouldThrowException() {

        Section section1 = new Section(0);
        Section section2 = new Section(3);
        Section section3 = new Section(5);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        sectionManager.collapseSection(3);

    }

    @Test
    public void isSectionExpanded() {

        Section section1 = new Section(0);
        Section section2 = new Section(4);
        Section section3 = new Section(7);

        section2.setExpanded(false);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        assertTrue(sectionManager.isSectionExpanded(0));
        assertFalse(sectionManager.isSectionExpanded(1));
        assertTrue(sectionManager.isSectionExpanded(2));

    }

    @Test(expected = IllegalArgumentException.class)
    public void isSectionExpanded_shouldThrowException() {

        Section section1 = new Section(0);
        Section section2 = new Section(4);
        Section section3 = new Section(7);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        sectionManager.isSectionExpanded(4);

    }

    @Test
    public void getItemPositionForSubheaderViewHolder() {

        //Subheader 0
        //Item
        //Item
        //Item
        //Subheader 4
        //Item
        //Item
        //Subheader 7
        //Item

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(0)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(4)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(7)).isEqualTo(5);

        sectionManager.collapseSection(0);

        //Subheader 0
        //Subheader 1
        //Item
        //Item
        //Subheader 4
        //Item

        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(0)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(1)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(4)).isEqualTo(5);

        sectionManager.collapseSection(1);

        //Subheader 0
        //Subheader 1
        //Subheader 2
        //Item

        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(0)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(1)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(2)).isEqualTo(5);

        sectionManager.collapseSection(2);

        //Subheader 0
        //Subheader 1
        //Subheader 2

        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(0)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(1)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(2)).isEqualTo(5);

        sectionManager.expandSection(1);

        //Subheader 0
        //Subheader 1
        //Item
        //Item
        //Subheader 4

        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(0)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(1)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(4)).isEqualTo(5);

        sectionManager.expandSection(0);

        //Subheader 0
        //Item
        //Item
        //Item
        //Subheader 4
        //Item
        //Item
        //Subheader 7

        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(0)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(4)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(7)).isEqualTo(5);

        sectionManager.expandSection(2);

        //Subheader 0
        //Item
        //Item
        //Item
        //Subheader 4
        //Item
        //Item
        //Subheader 7
        //Item

        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(0)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(4)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForSubheaderViewHolder(7)).isEqualTo(5);

    }

    @Test(expected = IllegalArgumentException.class)
    public void getItemPositionForSubheaderViewHolder_shouldThrowException() {

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        sectionManager.getItemPositionForSubheaderViewHolder(9);

    }

    @Test
    public void getItemPositionForItemViewHolder() {

        //Subheader 0
        //Item 1
        //Item 2
        //Item 3
        //Subheader 4
        //Item 5
        //Item 6
        //Subheader 7
        //Item 8

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        assertThat(sectionManager.getItemPositionForItemViewHolder(1)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForItemViewHolder(2)).isEqualTo(1);
        assertThat(sectionManager.getItemPositionForItemViewHolder(3)).isEqualTo(2);
        assertThat(sectionManager.getItemPositionForItemViewHolder(5)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForItemViewHolder(6)).isEqualTo(4);
        assertThat(sectionManager.getItemPositionForItemViewHolder(8)).isEqualTo(5);

        sectionManager.collapseSection(1);

        //Subheader 0
        //Item 1
        //Item 2
        //Item 3
        //Subheader 4
        //Subheader 5
        //Item 6

        assertThat(sectionManager.getItemPositionForItemViewHolder(1)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForItemViewHolder(2)).isEqualTo(1);
        assertThat(sectionManager.getItemPositionForItemViewHolder(3)).isEqualTo(2);
        assertThat(sectionManager.getItemPositionForItemViewHolder(6)).isEqualTo(5);

        sectionManager.collapseSection(0);

        //Subheader 0
        //Subheader 1
        //Subheader 2
        //Item 3

        assertThat(sectionManager.getItemPositionForItemViewHolder(3)).isEqualTo(5);

        sectionManager.collapseSection(2);

        //Subheader 0
        //Subheader 1
        //Subheader 2

        sectionManager.expandSection(1);

        //Subheader 0
        //Subheader 1
        //Item 2
        //Item 3
        //Subheader 4

        assertThat(sectionManager.getItemPositionForItemViewHolder(2)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForItemViewHolder(3)).isEqualTo(4);

        sectionManager.expandSection(2);

        //Subheader 0
        //Subheader 1
        //Item 2
        //Item 3
        //Subheader 4
        //Item 5

        assertThat(sectionManager.getItemPositionForItemViewHolder(2)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForItemViewHolder(3)).isEqualTo(4);

        sectionManager.expandSection(0);

        //Subheader 0
        //Item 1
        //Item 2
        //Item 3
        //Subheader 4
        //Item 5
        //Item 6
        //Subheader 7
        //Item 8

        assertThat(sectionManager.getItemPositionForItemViewHolder(1)).isEqualTo(0);
        assertThat(sectionManager.getItemPositionForItemViewHolder(2)).isEqualTo(1);
        assertThat(sectionManager.getItemPositionForItemViewHolder(3)).isEqualTo(2);
        assertThat(sectionManager.getItemPositionForItemViewHolder(5)).isEqualTo(3);
        assertThat(sectionManager.getItemPositionForItemViewHolder(6)).isEqualTo(4);
        assertThat(sectionManager.getItemPositionForItemViewHolder(8)).isEqualTo(5);

    }

    @Test
    public void getItemPositionForItemViewHolder_shouldThrowException() {

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        try {
            sectionManager.getItemPositionForItemViewHolder(0);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {}

        try {
            sectionManager.getItemPositionForItemViewHolder(9);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {}

    }

    @Test
    public void getAdapterPositionForItem() {

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Subheader 7
        //Item 8 ( index = 5 )
        //Item 9 ( index = 6 )
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Subheader 12
        //Item 13 ( index = 9 )

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(4);

        Section section4 = new Section(12);
        section4.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);
        sectionManager.addSection(section4);

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(2);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(3);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(5);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(6);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(8);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(9);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(10);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(11);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(13);

        sectionManager.collapseSection(2);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Subheader 7
        //Subheader 8
        //Item 9 ( index = 9 )

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(2);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(3);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(5);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(6);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(9);

        sectionManager.collapseSection(0);

        //Subheader 0
        //Subheader 1
        //Item 2 ( index = 3 )
        //Item 3 ( index = 4 )
        //Subheader 4
        //Subheader 5
        //Item 6 ( index = 9 )

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(2);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(3);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(6);

        sectionManager.collapseSection(3);

        //Subheader 0
        //Subheader 1
        //Item 2 ( index = 3 )
        //Item 3 ( index = 4 )
        //Subheader 4
        //Subheader 5

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(2);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(3);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(-1);

        sectionManager.collapseSection(1);

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(-1);

        //Subheader 0
        //Subheader 1
        //Subheader 2
        //Subheader 3

        sectionManager.expandSection(3);

        //Subheader 0
        //Subheader 1
        //Subheader 2
        //Subheader 3
        //Item 4 ( index = 9 )

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(4);

        sectionManager.expandSection(0);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Subheader 5
        //Subheader 6
        //Item 7 ( index = 9 )

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(2);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(3);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(7);

        sectionManager.expandSection(2);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Subheader 5
        //Item 6 ( index = 5 )
        //Item 7 ( index = 6 )
        //Item 8 ( index = 7 )
        //Item 9 ( index = 8 )
        //Subheader 10
        //Item 11 ( index = 9 )

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(2);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(3);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(-1);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(6);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(7);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(8);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(9);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(11);

        sectionManager.expandSection(1);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Subheader 7
        //Item 8 ( index = 5 )
        //Item 9 ( index = 6 )
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Subheader 12
        //Item 13 ( index = 9 )

        assertThat(sectionManager.getAdapterPositionForItem(0)).isEqualTo(1);
        assertThat(sectionManager.getAdapterPositionForItem(1)).isEqualTo(2);
        assertThat(sectionManager.getAdapterPositionForItem(2)).isEqualTo(3);
        assertThat(sectionManager.getAdapterPositionForItem(3)).isEqualTo(5);
        assertThat(sectionManager.getAdapterPositionForItem(4)).isEqualTo(6);
        assertThat(sectionManager.getAdapterPositionForItem(5)).isEqualTo(8);
        assertThat(sectionManager.getAdapterPositionForItem(6)).isEqualTo(9);
        assertThat(sectionManager.getAdapterPositionForItem(7)).isEqualTo(10);
        assertThat(sectionManager.getAdapterPositionForItem(8)).isEqualTo(11);
        assertThat(sectionManager.getAdapterPositionForItem(9)).isEqualTo(13);

    }

    @Test(expected = IllegalArgumentException.class)
    public void getAdapterPositionForItem_shouldThrowException() {

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(4);

        Section section4 = new Section(12);
        section4.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);
        sectionManager.addSection(section4);

        sectionManager.getAdapterPositionForItem(10);

    }

    @Test
    public void onItemChanged() {

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item 7 ( index = 4 )
        //Item 8 ( index = 5 )
        //Subheader 9
        //Item 10 ( index = 6 )
        //Subheader 11
        //Item 12 ( index = 7 )
        //Item 13 ( index = 8 )
        //Item 14 ( index = 9 )
        //Item 15 ( index = 10 )

        sectionManager.addSection(Section.create(0, 3));
        sectionManager.addSection(Section.create(4, 1));
        sectionManager.addSection(Section.create(6, 2));
        sectionManager.addSection(Section.create(9, 1));
        sectionManager.addSection(Section.create(11, 4));

        when(sectionProvider.getItemSize()).thenReturn(11);

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item 7 ( index = 4 )
        //Item 8 ( index = 5 )
        //Subheader 9
        //Item 10 ( index = 6 )
        //Subheader 11
        //Item 12 ( index = 7 )
        //Item 13 ( index = 8 )
        //Item 14 ( index = 9 )
        //Item 15 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(4, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 2),
                Section.create(9, 1),
                Section.create(11, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Subheader 8
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item 11 ( index = 7 )
        //Item 12 ( index = 8 )
        //Item 13 ( index = 9 )
        //Item 14 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(4, 1),
                Notifier.createChanged(5, 1)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 4),
                Section.create(5, 2),
                Section.create(8, 1),
                Section.create(10, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);

        actualResult = sectionManager.onItemChanged(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Subheader 6
        //Item 7 ( index = 5 )
        //Subheader 8
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item 11 ( index = 7 )
        //Item 12 ( index = 8 )
        //Item 13 ( index = 9 )
        //Item 14 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(5, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 5),
                Section.create(6, 1),
                Section.create(8, 1),
                Section.create(10, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(true);

        actualResult = sectionManager.onItemChanged(5);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Item 6 ( index = 5 )
        //Subheader 7
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(6),
                Notifier.createRemoved(7)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 6),
                Section.create(7, 1),
                Section.create(9, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(6)).thenReturn(true);

        actualResult = sectionManager.onItemChanged(6);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Item 6 ( index = 5 )
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Item 10 ( index = 8 )
        //Item 11 ( index = 9 )
        //Item 12 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(7),
                Notifier.createRemoved(8)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(true);

        actualResult = sectionManager.onItemChanged(0);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0, 2),
                Notifier.createInserted(2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 6),
                Section.create(9, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(true);

        actualResult = sectionManager.onItemChanged(10);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Subheader 13
        //Item 14 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(13),
                Notifier.createInserted(14)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 6),
                Section.create(9, 3),
                Section.create(13, 1)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Subheader 5
        //Item 6 ( index = 3 )
        //Subheader 7
        //Item 8 ( index = 4 )
        //Item 9 ( index = 5 )
        //Item 10 ( index = 6 )
        //Subheader 11
        //Item 12 ( index = 7 )
        //Item 13 ( index = 8 )
        //Item 14 ( index = 9 )
        //Subheader 15
        //Item 16 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(5),
                Notifier.createInserted(6, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 2),
                Section.create(5, 1),
                Section.create(7, 3),
                Section.create(11, 3),
                Section.create(15, 1)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        actualResult = sectionManager.onItemChanged(9);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Subheader 5
        //Item 6 ( index = 3 )
        //Subheader 7
        //Item 8 ( index = 4 )
        //Item 9 ( index = 5 )
        //Item 10 ( index = 6 )
        //Subheader 11
        //Item 12 ( index = 7 )
        //Item 13 ( index = 8 )
        //Subheader 14
        //Item 15 ( index = 9 )
        //Item 16 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(14, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 2),
                Section.create(5, 1),
                Section.create(7, 3),
                Section.create(11, 2),
                Section.create(14, 2)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Subheader 12
        //Item 13 ( index = 9 )
        //Item 14 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(5),
                Notifier.createRemoved(6, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 6),
                Section.create(9, 2),
                Section.create(12, 2)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(7)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(false);

        actualResult = sectionManager.onItemChanged(8);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Subheader 11
        //Item 12 ( index = 8 )
        //Item 13 ( index = 9 )
        //Item 14 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(11, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 6),
                Section.create(9, 1),
                Section.create(11, 3)
        );

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        actualResult = sectionManager.onItemChanged(0);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Item 6 ( index = 5 )
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Subheader 10
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0, 2),
                Notifier.createRemoved(2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 1),
                Section.create(10, 3)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(true);

        actualResult = sectionManager.onItemChanged(10);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Item 6 ( index = 5 )
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Subheader 10
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Subheader 13
        //Item 14 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(13),
                Notifier.createInserted(14)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 1),
                Section.create(10, 2),
                Section.create(13, 1)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        actualResult = sectionManager.onItemChanged(10);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Item 6 ( index = 5 )
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Subheader 10
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(13),
                Notifier.createRemoved(14)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 1),
                Section.create(10, 3)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //__________________________________________________________________________________________

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(false);

        actualResult = sectionManager.onItemChanged(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Item 6 ( index = 5 )
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Subheader 10
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(5)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 1),
                Section.create(10, 3)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void removeItem_shouldThrowException() {

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(1);

        Section section3 = new Section(6);
        section3.setItemCount(2);

        Section section4 = new Section(9);
        section4.setItemCount(1);

        Section section5 = new Section(11);
        section5.setItemCount(4);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);
        sectionManager.addSection(section4);
        sectionManager.addSection(section5);

        /*try {
            sectionManager.removeItem(0);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {}

        try {
            sectionManager.removeItem(16);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {}*/

    }

    @Test
    public void sectionIndex() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, false));
        sectionManager.addSection(Section.create(5, 2, true));

        //Subheader 0
        //Item 1
        //Item 2
        //Item 3
        //Subheader 4
        //Item //COLLAPSED
        //Subheader 5
        //Item 6
        //Item 7

        assertThat(sectionManager.sectionIndex(0)).isEqualTo(0);
        assertThat(sectionManager.sectionIndex(1)).isEqualTo(0);
        assertThat(sectionManager.sectionIndex(2)).isEqualTo(0);
        assertThat(sectionManager.sectionIndex(3)).isEqualTo(0);
        assertThat(sectionManager.sectionIndex(4)).isEqualTo(1);
        assertThat(sectionManager.sectionIndex(5)).isEqualTo(2);
        assertThat(sectionManager.sectionIndex(6)).isEqualTo(2);
        assertThat(sectionManager.sectionIndex(7)).isEqualTo(2);

    }

    @Test(expected = IllegalArgumentException.class)
    public void sectionIndex_shouldThrowException() {

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        sectionManager.sectionIndex(9);

    }

    @Test
    public void clear() {

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        sectionManager.clear();

        assertTrue(sectionManager.getSections().isEmpty());

    }

    @Test
    public void indexInSection() {

        //Subheader 0
        //Item 1
        //Item 2
        //Item 3
        //Subheader 4
        //Item 5
        //Item 6
        //Subheader 7
        //Item 8

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        assertThat(sectionManager.positionInSection(1)).isEqualTo(0);
        assertThat(sectionManager.positionInSection(2)).isEqualTo(1);
        assertThat(sectionManager.positionInSection(3)).isEqualTo(2);
        assertThat(sectionManager.positionInSection(5)).isEqualTo(0);
        assertThat(sectionManager.positionInSection(6)).isEqualTo(1);
        assertThat(sectionManager.positionInSection(8)).isEqualTo(0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void indexInSection_shouldThrowException() {

        //Subheader 0
        //Item 1
        //Item 2
        //Item 3
        //Subheader 4
        //Item 5
        //Item 6
        //Subheader 7
        //Item 8

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        sectionManager.positionInSection(4);

    }

    @Test
    public void sectionSize() {

        Section section1 = new Section(0);
        section1.setItemCount(3);

        Section section2 = new Section(4);
        section2.setItemCount(2);

        Section section3 = new Section(7);
        section3.setItemCount(1);

        sectionManager.addSection(section1);
        sectionManager.addSection(section2);
        sectionManager.addSection(section3);

        assertThat(sectionManager.sectionSize(0)).isEqualTo(3);
        assertThat(sectionManager.sectionSize(1)).isEqualTo(2);
        assertThat(sectionManager.sectionSize(2)).isEqualTo(1);

    }
}