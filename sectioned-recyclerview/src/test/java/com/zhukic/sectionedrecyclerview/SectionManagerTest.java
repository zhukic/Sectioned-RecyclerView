package com.zhukic.sectionedrecyclerview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
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

        assertTrue(sectionManager.isSectionSubheaderAtPosition(0));
        assertTrue(sectionManager.isSectionSubheaderAtPosition(4));
        assertTrue(sectionManager.isSectionSubheaderAtPosition(7));

        assertFalse(sectionManager.isSectionSubheaderAtPosition(1));
        assertFalse(sectionManager.isSectionSubheaderAtPosition(5));
        assertFalse(sectionManager.isSectionSubheaderAtPosition(8));

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