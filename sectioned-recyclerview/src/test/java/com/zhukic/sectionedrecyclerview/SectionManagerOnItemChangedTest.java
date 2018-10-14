package com.zhukic.sectionedrecyclerview;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SectionManagerOnItemChangedTest {

    private SectionManager sectionManager;

    private SectionProvider sectionProvider;

    @Before
    public void beforeEachTest() {
        sectionProvider = mock(SectionProvider.class);
        sectionManager = new SectionManager(sectionProvider);
        when(sectionProvider.getItemSize()).thenReturn(11);
    }

    @Test
    public void test01() {

        sectionManager.addSection(Section.create(0, 3));
        sectionManager.addSection(Section.create(4, 1));
        sectionManager.addSection(Section.create(6, 2));
        sectionManager.addSection(Section.create(9, 1));
        sectionManager.addSection(Section.create(11, 4));

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

        NotifyResult expectedResult = NotifyResult.create(Collections.singletonList(
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

    }

    @Test
    public void test02() {

        sectionManager.addSection(Section.create(0, 3));
        sectionManager.addSection(Section.create(4, 1));
        sectionManager.addSection(Section.create(6, 2));
        sectionManager.addSection(Section.create(9, 1));
        sectionManager.addSection(Section.create(11, 4));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createChanged(4, 1),
                Notifier.createRemoved(5, 1)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4),
                Section.create(5, 2),
                Section.create(8, 1),
                Section.create(10, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test03() {

        sectionManager.addSection(Section.create(0, 4));
        sectionManager.addSection(Section.create(5, 2));
        sectionManager.addSection(Section.create(8, 1));
        sectionManager.addSection(Section.create(10, 4));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(4);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createChanged(5, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 5),
                Section.create(6, 1),
                Section.create(8, 1),
                Section.create(10, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test04() {

        sectionManager.addSection(Section.create(0, 5));
        sectionManager.addSection(Section.create(6, 1));
        sectionManager.addSection(Section.create(8, 1));
        sectionManager.addSection(Section.create(10, 4));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(5);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createChanged(6),
                Notifier.createRemoved(7)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 6),
                Section.create(7, 1),
                Section.create(9, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test05() {

        sectionManager.addSection(Section.create(0, 6));
        sectionManager.addSection(Section.create(7, 1));
        sectionManager.addSection(Section.create(9, 4));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(6)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(6);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createChanged(7),
                Notifier.createRemoved(8)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test06() {

        sectionManager.addSection(Section.create(0, 7));
        sectionManager.addSection(Section.create(8, 4));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(0);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0, 2),
                Notifier.createInserted(2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 6),
                Section.create(9, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test07() {

        sectionManager.addSection(Section.create(0, 1));
        sectionManager.addSection(Section.create(2, 6));
        sectionManager.addSection(Section.create(9, 4));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(9),
                Notifier.createChanged(13),
                Notifier.createInserted(14)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 6),
                Section.create(9, 3),
                Section.create(13, 1)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test08() {

        sectionManager.addSection(Section.create(0, 1));
        sectionManager.addSection(Section.create(2, 6));
        sectionManager.addSection(Section.create(9, 3));
        sectionManager.addSection(Section.create(13, 1));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createChanged(5),
                Notifier.createInserted(6, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 2),
                Section.create(5, 1),
                Section.create(7, 3),
                Section.create(11, 3),
                Section.create(15, 1)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test09() {

        sectionManager.addSection(Section.create(0, 1));
        sectionManager.addSection(Section.create(2, 2));
        sectionManager.addSection(Section.create(5, 1));
        sectionManager.addSection(Section.create(7, 3));
        sectionManager.addSection(Section.create(11, 3));
        sectionManager.addSection(Section.create(15, 1));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(9);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(11),
                Notifier.createChanged(14, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 2),
                Section.create(5, 1),
                Section.create(7, 3),
                Section.create(11, 2),
                Section.create(14, 2)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test10() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 3, true));
        sectionManager.addSection(Section.create(11, 2, true));
        sectionManager.addSection(Section.create(14, 2, true));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createChanged(5),
                Notifier.createRemoved(6, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 6, true),
                Section.create(9, 2, true),
                Section.create(12, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test11() {

        sectionManager.addSection(Section.create(0, 1));
        sectionManager.addSection(Section.create(2, 6));
        sectionManager.addSection(Section.create(9, 2));
        sectionManager.addSection(Section.create(12, 2));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(7)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(8);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(9),
                Notifier.createChanged(11, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 6),
                Section.create(9, 1),
                Section.create(11, 3)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test12() {

        sectionManager.addSection(Section.create(0, 1));
        sectionManager.addSection(Section.create(2, 6));
        sectionManager.addSection(Section.create(9, 1));
        sectionManager.addSection(Section.create(11, 3));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(0);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0, 2),
                Notifier.createRemoved(2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 1),
                Section.create(10, 3)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test14() {

        sectionManager.addSection(Section.create(0, 7));
        sectionManager.addSection(Section.create(8, 1));
        sectionManager.addSection(Section.create(10, 2));
        sectionManager.addSection(Section.create(13, 1));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(10),
                Notifier.createChanged(13),
                Notifier.createRemoved(14)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 1),
                Section.create(10, 3)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test15() {

        sectionManager.addSection(Section.create(0, 7));
        sectionManager.addSection(Section.create(8, 1));
        sectionManager.addSection(Section.create(10, 3));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(4);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createChanged(5)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 1),
                Section.create(10, 3)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test16() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 1, true));
        sectionManager.addSection(Section.create(11, 3, true));
        sectionManager.addSection(Section.create(15, 1, true));

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
        //Subheader 15
        //Item 16 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(6)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(6);

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
        //Item 11 ( index = 7 )
        //Item 12 ( index = 8 )
        //Item 13 ( index = 9 )
        //Subheader 14
        //Item 15 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(9, 2),
                Notifier.createRemoved(11)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 4, true),
                Section.create(14, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test17() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 1, true));
        sectionManager.addSection(Section.create(11, 3, true));
        sectionManager.addSection(Section.create(15, 1, true));

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
        //Subheader 15
        //Item 16 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(0);

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
        //Subheader 15
        //Item 16 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(0, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 1, true),
                Section.create(11, 3, true),
                Section.create(15, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test18() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 1, true));
        sectionManager.addSection(Section.create(11, 3, true));
        sectionManager.addSection(Section.create(15, 1, true));

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
        //Subheader 15
        //Item 16 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

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
        //Subheader 15
        //Item 16 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(15, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 1, true),
                Section.create(11, 3, true),
                Section.create(15, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test19() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 1, true));
        sectionManager.addSection(Section.create(11, 2, true));
        sectionManager.addSection(Section.create(14, 2, true));

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
        //Subheader 14
        //Item 15 ( index = 9 )
        //Item 16 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

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
        //Subheader 14
        //Item 15 ( index = 9 )
        //Item 16 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(14),
                Notifier.createChanged(16)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 1, true),
                Section.create(11, 2, true),
                Section.create(14, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test20() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, false));
        sectionManager.addSection(Section.create(5, 2, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 4, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
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

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
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

        NotifyResult expectedResult = NotifyResult.create(Collections.singletonList(
                Notifier.createChanged(4)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, false),
                Section.create(5, 2, true),
                Section.create(8, 1, true),
                Section.create(10, 4, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test21() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, false));
        sectionManager.addSection(Section.create(5, 2, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 4, false));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Subheader 8
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Subheader 8
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 11
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(10),
                Notifier.createInserted(11)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, false),
                Section.create(5, 2, true),
                Section.create(8, 1, true),
                Section.create(10, 3, false),
                Section.create(11, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test22() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, false));
        sectionManager.addSection(Section.create(5, 2, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 3, false));
        sectionManager.addSection(Section.create(11, 1, false));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Subheader 8
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 11
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(6)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(6);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Subheader 8
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 9
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(8),
                Notifier.createRemoved(9, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, false),
                Section.create(5, 2, true),
                Section.create(8, 4, false),
                Section.create(9, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test23() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, false));
        sectionManager.addSection(Section.create(5, 2, true));
        sectionManager.addSection(Section.create(8, 4, false));
        sectionManager.addSection(Section.create(9, 1, false));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Subheader 8
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 9
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 5 )
        //Subheader 7
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 8
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(4, 2),
                Notifier.createRemoved(6)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 2, false),
                Section.create(5, 1, true),
                Section.create(7, 4, false),
                Section.create(8, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test24() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 2, false));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 4, false));
        sectionManager.addSection(Section.create(8, 1, false));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 5 )
        //Subheader 7
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 8
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(7)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(8);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 5 )
        //Subheader 7
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Subheader 8
        //Item ( index = 8 ) //COLLAPSED
        //Subheader 9
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 10
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(7),
                Notifier.createInserted(8, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 2, false),
                Section.create(5, 1, true),
                Section.create(7, 2, false),
                Section.create(8, 1, false),
                Section.create(9, 1, false),
                Section.create(10, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test25() {
        when(sectionProvider.getItemSize()).thenReturn(1);

        sectionManager.addSection(Section.create(0, 1, true));

        //Subheader 0
        //Item 1 ( index = 0 )

        NotifyResult actualResult = sectionManager.onItemChanged(0);

        //Subheader 0
        //Item 1 ( index = 0 )

        NotifyResult expectedResult = NotifyResult.create(Notifier.createChanged(0, 2));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Collections.singletonList(
                Section.create(0, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test26() {
        when(sectionProvider.getItemSize()).thenReturn(1);

        sectionManager.addSection(Section.create(0, 1, false));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED

        NotifyResult actualResult = sectionManager.onItemChanged(0);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(0, 1)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Collections.singletonList(
                Section.create(0, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test27() {

        sectionManager.addSection(Section.create(0, 3, false));
        sectionManager.addSection(Section.create(1, 1, true));
        sectionManager.addSection(Section.create(3, 2, true));
        sectionManager.addSection(Section.create(6, 1, true));
        sectionManager.addSection(Section.create(8, 3, true));
        sectionManager.addSection(Section.create(12, 1, true));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 3 )
        //Subheader 3
        //Item 4 ( index = 4 )
        //Item 5 ( index = 5 )
        //Subheader 6
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Item 10 ( index = 8 )
        //Item 11 ( index = 9 )
        //Subheader 12
        //Item 13 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(0);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 3 )
        //Subheader 3
        //Item 4 ( index = 4 )
        //Item 5 ( index = 5 )
        //Subheader 6
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Item 10 ( index = 8 )
        //Item 11 ( index = 9 )
        //Subheader 12
        //Item 13 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(0)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, false),
                Section.create(1, 1, true),
                Section.create(3, 2, true),
                Section.create(6, 1, true),
                Section.create(8, 3, true),
                Section.create(12, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test28() {

        sectionManager.addSection(Section.create(0, 7, false));
        sectionManager.addSection(Section.create(1, 4, true));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 7 )
        //Item 3 ( index = 8 )
        //Item 4 ( index = 9 )
        //Item 5 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(0);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 2
        //Item ( index = 7 )
        //Item ( index = 8 )
        //Item ( index = 9 )
        //Item ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createInserted(1)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, false),
                Section.create(1, 6, false),
                Section.create(2, 4, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test29() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 6, false));
        sectionManager.addSection(Section.create(3, 1, true));
        sectionManager.addSection(Section.create(5, 3, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 3
        //Item 4 ( index = 7 )
        //Subheader 5
        //Item 6 ( index = 8 )
        //Item 7 ( index = 9 )
        //Item 8 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(0);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 7 )
        //Subheader 3
        //Item 4( index = 8 )
        //Item 5 ( index = 9 )
        //Item 6 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createRemoved(1, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7, false),
                Section.create(1, 1, true),
                Section.create(3, 3, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test30() {

        sectionManager.addSection(Section.create(0, 1, false));
        sectionManager.addSection(Section.create(1, 6, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 3, true));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Subheader 1
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

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(0);

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

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(0, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7, true),
                Section.create(8, 1, true),
                Section.create(10, 3, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test31() {

        sectionManager.addSection(Section.create(0, 1, false));
        sectionManager.addSection(Section.create(1, 6, false));
        sectionManager.addSection(Section.create(2, 1, true));
        sectionManager.addSection(Section.create(4, 3, true));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 2
        //Item 3 ( index = 7 )
        //Subheader 4
        //Item 5 ( index = 8 )
        //Item 6 ( index = 9 )
        //Item 7 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(0);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 7 )
        //Subheader 3
        //Item 4( index = 8 )
        //Item 5 ( index = 9 )
        //Item 6 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createRemoved(1)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7, false),
                Section.create(1, 1, true),
                Section.create(3, 3, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test32() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 1, true));
        sectionManager.addSection(Section.create(11, 3, true));
        sectionManager.addSection(Section.create(15, 1, false));

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
        //Subheader 15
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

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
        //Subheader 15
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(15)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 1, true),
                Section.create(11, 3, true),
                Section.create(15, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test33() {

        sectionManager.addSection(Section.create(0, 7, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 2, false));
        sectionManager.addSection(Section.create(11, 1, true));

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
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 11
        //Item 12 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

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
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(10),
                Notifier.createRemoved(11, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7, true),
                Section.create(8, 1, true),
                Section.create(10, 3, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test34() {

        sectionManager.addSection(Section.create(0, 7, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 2, true));
        sectionManager.addSection(Section.create(13, 1, false));

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
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(10),
                Notifier.createChanged(13)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7),
                Section.create(8, 1),
                Section.create(10, 3)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test35() {

        sectionManager.addSection(Section.create(0, 7, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 2, false));
        sectionManager.addSection(Section.create(11, 1, false));

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
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 11
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(10);

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
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(10),
                Notifier.createRemoved(11)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 7, true),
                Section.create(8, 1,true),
                Section.create(10, 3, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test36() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 3, true));
        sectionManager.addSection(Section.create(11, 3, true));
        sectionManager.addSection(Section.create(15, 1, false));

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
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(9);

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
        //Item ( index = 9 ) //COLLAPSED
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(11),
                Notifier.createChanged(14),
                Notifier.createRemoved(15)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 2, true),
                Section.create(5, 1, true),
                Section.create(7, 3, true),
                Section.create(11, 2, true),
                Section.create(14, 2, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test37() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 3, true));
        sectionManager.addSection(Section.create(11, 3, false));
        sectionManager.addSection(Section.create(12, 1, true));

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
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 12
        //Item 13 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(9);

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
        //Item ( index = 7 )
        //Item ( index = 8 )
        //Subheader 12
        //Item 13 ( index = 9 ) //COLLAPSED
        //Item 14 ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(11),
                Notifier.createChanged(12),
                Notifier.createInserted(13)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 2, true),
                Section.create(5, 1, true),
                Section.create(7, 3, true),
                Section.create(11, 2, false),
                Section.create(12, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test38() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 3, true));
        sectionManager.addSection(Section.create(11, 3, false));
        sectionManager.addSection(Section.create(12, 1, false));

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
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 12
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(9);

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
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Subheader 12
        //Item ( index = 9 ) //COLLAPSED
        //Item ( index = 10 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Notifier.createChanged(11, 2));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 2, true),
                Section.create(5, 1, true),
                Section.create(7, 3, true),
                Section.create(11, 2, false),
                Section.create(12, 2, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test39() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 3, true));
        sectionManager.addSection(Section.create(11, 3, true));
        sectionManager.addSection(Section.create(15, 1, true));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(9);

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
        //Subheader 16
        //Item 17 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(11),
                Notifier.createChanged(14),
                Notifier.createInserted(15)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 2, true),
                Section.create(5, 1, true),
                Section.create(7, 3, true),
                Section.create(11, 2, true),
                Section.create(14, 1, true),
                Section.create(16, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test40() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 3, true));
        sectionManager.addSection(Section.create(11, 3, false));
        sectionManager.addSection(Section.create(12, 1, true));

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
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 12
        //Item 13 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(8)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(9)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(9);

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
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Subheader 12
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 13
        //Item 14 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(11),
                Notifier.createInserted(12)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 2, true),
                Section.create(5, 1, true),
                Section.create(7, 3, true),
                Section.create(11, 2, false),
                Section.create(12, 1, false),
                Section.create(13, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test41() {

        sectionManager.addSection(Section.create(0, 4, true));
        sectionManager.addSection(Section.create(5, 2, false));
        sectionManager.addSection(Section.create(6, 1, true));
        sectionManager.addSection(Section.create(8, 4, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 6
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Item 10 ( index = 8 )
        //Item 11 ( index = 9 )
        //Item 12 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Subheader 6
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 7
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createChanged(5),
                Notifier.createInserted(6)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 5, true),
                Section.create(6, 1, false),
                Section.create(7, 1, true),
                Section.create(9, 4, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test42() {

        sectionManager.addSection(Section.create(0, 4, false));
        sectionManager.addSection(Section.create(1, 2, false));
        sectionManager.addSection(Section.create(2, 1, true));
        sectionManager.addSection(Section.create(4, 4, true));

        //Subheader 0
        //Item 1 ( index = 0 ) //COLLAPSED
        //Item 2 ( index = 1 ) //COLLAPSED
        //Item 3 ( index = 2 ) //COLLAPSED
        //Item 4 ( index = 3 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 2
        //Item 3 ( index = 6 )
        //Subheader 4
        //Item 5 ( index = 7 )
        //Item 6 ( index = 8 )
        //Item 7 ( index = 9 )
        //Item 8 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(4);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 2
        //Item 3 ( index = 6 )
        //Subheader 4
        //Item 5 ( index = 7 )
        //Item 6 ( index = 8 )
        //Item 7 ( index = 9 )
        //Item 8 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(0, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 5, false),
                Section.create(1, 1, false),
                Section.create(2, 1, true),
                Section.create(4, 4, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test43() {

        sectionManager.addSection(Section.create(0, 4, true));
        sectionManager.addSection(Section.create(5, 2, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 4, true));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 6 ( index = 4 )
        //Subheader 7
        //Item 8 ( index = 5 )
        //Subheader 9
        //Item 10 ( index = 6 )
        //Subheader 11
        //Item 12 ( index = 7 )
        //Item 13 ( index = 8 )
        //Item 14 ( index = 9 )
        //Item 15 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(5, 2),
                Notifier.createInserted(7)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, true),
                Section.create(5, 1, true),
                Section.create(7, 1, true),
                Section.create(9, 1, true),
                Section.create(11, 4, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test44() {

        sectionManager.addSection(Section.create(0, 4, true));
        sectionManager.addSection(Section.create(5, 2, false));
        sectionManager.addSection(Section.create(6, 1, true));
        sectionManager.addSection(Section.create(8, 4, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 6
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Item 10 ( index = 8 )
        //Item 11 ( index = 9 )
        //Item 12 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 6
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 7
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(5),
                Notifier.createInserted(6)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, true),
                Section.create(5, 1, false),
                Section.create(6, 1, false),
                Section.create(7, 1, true),
                Section.create(9, 4, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test45() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 1, false));
        sectionManager.addSection(Section.create(10, 3, true));
        sectionManager.addSection(Section.create(14, 1, true));

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
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 10
        //Item 11 ( index = 7 )
        //Item 12 ( index = 8 )
        //Item 13 ( index = 9 )
        //Subheader 14
        //Item 15 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(6)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(6);

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
        //Item 11 ( index = 7 )
        //Item 12 ( index = 8 )
        //Item 13 ( index = 9 )
        //Subheader 14
        //Item 15 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(9, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 4, true),
                Section.create(14, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test46() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 1, false));
        sectionManager.addSection(Section.create(10, 3, false));
        sectionManager.addSection(Section.create(11, 1, true));

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
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 10
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 11
        //Item 12 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(6)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(6);

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
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Subheader 10
        //Item 11 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(9),
                Notifier.createRemoved(10)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 4, false),
                Section.create(10, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test47() {

        sectionManager.addSection(Section.create(0, 3, false));
        sectionManager.addSection(Section.create(1, 1, true));
        sectionManager.addSection(Section.create(3, 2, true));
        sectionManager.addSection(Section.create(6, 1, true));
        sectionManager.addSection(Section.create(8, 4, true));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 3 )
        //Subheader 3
        //Item 4 ( index = 4 )
        //Item 5 ( index = 5 )
        //Subheader 6
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Item 10 ( index = 8 )
        //Item 11 ( index = 9 )
        //Item 12 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 4 )
        //Item 3 ( index = 5 )
        //Subheader 4
        //Item 5 ( index = 6 )
        //Subheader 6
        //Item 6 ( index = 7 )
        //Item 7 ( index = 8 )
        //Item 8 ( index = 9 )
        //Item 9 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createRemoved(1, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, false),
                Section.create(1, 2, true),
                Section.create(4, 1, true),
                Section.create(6, 4, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test48() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, false));
        sectionManager.addSection(Section.create(5, 2, true));
        sectionManager.addSection(Section.create(8, 1, true));
        sectionManager.addSection(Section.create(10, 4, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
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

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createChanged(4)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, true),
                Section.create(5, 2, true),
                Section.create(8, 1, true),
                Section.create(10, 4, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test49() {

        sectionManager.addSection(Section.create(0, 3, false));
        sectionManager.addSection(Section.create(1, 1, false));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 4, true));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 2
        //Item 3 ( index = 4 )
        //Item 4 ( index = 5 )
        //Subheader 5
        //Item 6 ( index = 6 )
        //Subheader 7
        //Item 8 ( index = 7 )
        //Item 9 ( index = 8 )
        //Item 10 ( index = 9 )
        //Item 11 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 4 )
        //Item 3 ( index = 5 )
        //Subheader 4
        //Item 5 ( index = 6 )
        //Subheader 6
        //Item 7 ( index = 7 )
        //Item 8 ( index = 8 )
        //Item 9 ( index = 9 )
        //Item 10 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createRemoved(1)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, false),
                Section.create(1, 2),
                Section.create(4, 1),
                Section.create(6, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test50() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, true));
        sectionManager.addSection(Section.create(7, 3, false));
        sectionManager.addSection(Section.create(8, 2, true));
        sectionManager.addSection(Section.create(11, 2, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Subheader 5
        //Item 6 ( index = 3 )
        //Subheader 7
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 8
        //Item 9 ( index = 7 )
        //Item 10 ( index = 8 )
        //Subheader 11
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createChanged(5),
                Notifier.createRemoved(6, 2),
                Notifier.createInserted(6, 3)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 6, true),
                Section.create(9, 2, true),
                Section.create(12, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test51() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, false));
        sectionManager.addSection(Section.create(3, 1, true));
        sectionManager.addSection(Section.create(5, 3, true));
        sectionManager.addSection(Section.create(9, 2, true));
        sectionManager.addSection(Section.create(12, 2, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 3
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Subheader 12
        //Item 13 ( index = 9 )
        //Item 14 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 3
        //Item 4 ( index = 7 )
        //Item 5 ( index = 8 )
        //Subheader 6
        //Item 7 ( index = 9 )
        //Item 8 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createRemoved(3, 6)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 6, false),
                Section.create(3, 2, true),
                Section.create(6, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test52() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, false));
        sectionManager.addSection(Section.create(3, 1, true));
        sectionManager.addSection(Section.create(5, 3, false));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 2, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 ) //COLLAPSED
        //Item 4 ( index = 2 ) //COLLAPSED
        //Subheader 3
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 8 ( index = 4 ) //COLLAPSED
        //Item 9 ( index = 5 ) //COLLAPSED
        //Item 10 ( index = 6 ) //COLLAPSED
        //Subheader 6
        //Item 7 ( index = 7 )
        //Item 8 ( index = 8 )
        //Subheader 9
        //Item 10 ( index = 9 )
        //Item 11 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 3
        //Item 4 ( index = 7 )
        //Item 5 ( index = 8 )
        //Subheader 6
        //Item 7 ( index = 9 )
        //Item 8 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createRemoved(3, 3)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 6, false),
                Section.create(3, 2, true),
                Section.create(6, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test53() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, false));
        sectionManager.addSection(Section.create(6, 3, true));
        sectionManager.addSection(Section.create(10, 2, true));
        sectionManager.addSection(Section.create(13, 2, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Subheader 5
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 6
        //Item 7 ( index = 4 )
        //Item 8 ( index = 5 )
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item 11 ( index = 7 )
        //Item 12 ( index = 8 )
        //Subheader 13
        //Item 14 ( index = 9 )
        //Item 15 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createChanged(5),
                Notifier.createRemoved(6)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 6, true),
                Section.create(9, 2, true),
                Section.create(12, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test54() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, true));
        sectionManager.addSection(Section.create(5, 1, false));
        sectionManager.addSection(Section.create(6, 3, false));
        sectionManager.addSection(Section.create(7, 2, true));
        sectionManager.addSection(Section.create(10, 2, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Subheader 5
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 6
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 7
        //Item 8 ( index = 7 )
        //Item 9 ( index = 8 )
        //Subheader 10
        //Item 11 ( index = 9 )
        //Item 12 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

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

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createRemoved(5, 2),
                Notifier.createInserted(5, 4)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 6, true),
                Section.create(9, 2, true),
                Section.create(12, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test55() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, false));
        sectionManager.addSection(Section.create(3, 1, false));
        sectionManager.addSection(Section.create(4, 3, true));
        sectionManager.addSection(Section.create(8, 2, true));
        sectionManager.addSection(Section.create(11, 2, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 3
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 4
        //Item 5 ( index = 4 )
        //Item 6 ( index = 5 )
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Item 10 ( index = 8 )
        //Subheader 11
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 3
        //Item 4 ( index = 7 )
        //Item 5 ( index = 8 )
        //Subheader 6
        //Item 7 ( index = 9 )
        //Item 8 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createRemoved(3, 5)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 6, false),
                Section.create(3, 2, true),
                Section.create(6, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test56() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 2, false));
        sectionManager.addSection(Section.create(3, 1, false));
        sectionManager.addSection(Section.create(4, 3, false));
        sectionManager.addSection(Section.create(5, 2, true));
        sectionManager.addSection(Section.create(8, 2, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 3
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 4
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 7 )
        //Item 7 ( index = 8 )
        //Subheader 8
        //Item 9 ( index = 9 )
        //Item 10 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(2)).thenReturn(false);
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemChanged(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 3
        //Item 4 ( index = 7 )
        //Item 5 ( index = 8 )
        //Subheader 6
        //Item 7 ( index = 9 )
        //Item 8 ( index = 10 )

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createRemoved(3, 2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 6, false),
                Section.create(3, 2, true),
                Section.create(6, 2, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

}
