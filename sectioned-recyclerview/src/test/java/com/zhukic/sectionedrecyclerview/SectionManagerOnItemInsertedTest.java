package com.zhukic.sectionedrecyclerview;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SectionManagerOnItemInsertedTest {

    private SectionManager sectionManager;

    private SectionProvider sectionProvider;

    @Before
    public void beforeEachTest() {
        sectionProvider = mock(SectionProvider.class);
        sectionManager = new SectionManager(sectionProvider);
    }

    @Test
    public void test01() {

        //

        NotifyResult actualResult = sectionManager.onItemInserted(0);

        //Subheader 0
        //Item 1

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createInserted(0, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Collections.singletonList(
                Section.create(0, 1)
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

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(true);

        NotifyResult actualResult = sectionManager.onItemInserted(0);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item 7 ( index = 4 )
        //Subheader 8
        //Item 9 ( index = 5 )
        //Item 10 ( index = 6 )
        //Subheader 11
        //Item 12 ( index = 7 )
        //Subheader 13
        //Item 14 ( index = 8 )
        //Item 15 ( index = 9 )
        //Item 16 ( index = 10 )
        //Item 17 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createInserted(0, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1),
                Section.create(2, 3),
                Section.create(6, 1),
                Section.create(8, 2),
                Section.create(11, 1),
                Section.create(13, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test03() {

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemInserted(0);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 6 ( index = 4 )
        //Subheader 7
        //Item 8 ( index = 5 )
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item 11 ( index = 7 )
        //Subheader 12
        //Item 13 ( index = 8 )
        //Item 14 ( index = 9 )
        //Item 15 ( index = 10 )
        //Item 16 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0),
                        Notifier.createInserted(1)
                )
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4),
                Section.create(5, 1),
                Section.create(7, 2),
                Section.create(10, 1),
                Section.create(12, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test04() {

        sectionManager.addSection(Section.create(0, 3, false));
        sectionManager.addSection(Section.create(1, 1));
        sectionManager.addSection(Section.create(3, 2));
        sectionManager.addSection(Section.create(6, 1));
        sectionManager.addSection(Section.create(8, 4));

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(0)).thenReturn(false);

        NotifyResult actualResult = sectionManager.onItemInserted(0);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 4 )
        //Subheader 3
        //Item 4 ( index = 5 )
        //Item 5 ( index = 6 )
        //Subheader 6
        //Item 7 ( index = 7 )
        //Subheader 8
        //Item 9 ( index = 8 )
        //Item 10 ( index = 9 )
        //Item 11 ( index = 10 )
        //Item 12 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(Notifier.createChanged(0));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, false),
                Section.create(1, 1),
                Section.create(3, 2),
                Section.create(6, 1),
                Section.create(8, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test05() {

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(10)).thenReturn(true);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(11);

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
        //Subheader 16
        //Item 17 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createInserted(16, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 2),
                Section.create(9, 1),
                Section.create(11, 4),
                Section.create(16, 1)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test06() {

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(10)).thenReturn(false);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(11);

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
        //Item 16 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(11),
                        Notifier.createInserted(16)
                )
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 2),
                Section.create(9, 1),
                Section.create(11, 5)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test07() {

        sectionManager.addSection(Section.create(0, 3));
        sectionManager.addSection(Section.create(4, 1));
        sectionManager.addSection(Section.create(6, 2));
        sectionManager.addSection(Section.create(9, 1));
        sectionManager.addSection(Section.create(11, 4, false));

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
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Item ( index = 10 ) //COLLAPSED

        when(sectionProvider.onPlaceSubheaderBetweenItems(10)).thenReturn(false);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(11);

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
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED
        //Item ( index = 10 ) //COLLAPSED
        //Item ( index = 11 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Notifier.createChanged(11));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 2),
                Section.create(9, 1),
                Section.create(11, 5, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test08() {

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(5);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item 7 ( index = 4 )
        //Subheader 8
        //Item 9 ( index = 5 )
        //Subheader 10
        //Item 11 ( index = 6 )
        //Subheader 12
        //Item 13 ( index = 7 )
        //Subheader 14
        //Item 15 ( index = 8 )
        //Item 16 ( index = 9 )
        //Item 17 ( index = 10 )
        //Item 18 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(6),
                        Notifier.createChanged(8),
                        Notifier.createInserted(9, 2)
                )
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 1),
                Section.create(8, 1),
                Section.create(10, 1),
                Section.create(12, 1),
                Section.create(14, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test09() {

        sectionManager.addSection(Section.create(0, 3));
        sectionManager.addSection(Section.create(4, 1));
        sectionManager.addSection(Section.create(6, 2, false));
        sectionManager.addSection(Section.create(7, 1));
        sectionManager.addSection(Section.create(9, 4));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 7
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(true);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(5);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 7
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 8
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 9
        //Item 10 ( index = 7 )
        //Subheader 11
        //Item 12 ( index = 8 )
        //Item 13 ( index = 9 )
        //Item 14 ( index = 10 )
        //Item 15 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(6),
                        Notifier.createInserted(7, 2)
                )
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 1, false),
                Section.create(7, 1, false),
                Section.create(8, 1, false),
                Section.create(9, 1),
                Section.create(11, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test10() {

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

        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(false);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(5);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item 7 ( index = 4 )
        //Item 8 ( index = 5 )
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item 11 ( index = 7 )
        //Subheader 12
        //Item 13 ( index = 8 )
        //Item 14 ( index = 9 )
        //Item 15 ( index = 10 )
        //Item 16 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(6),
                        Notifier.createInserted(8)
                )
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 3),
                Section.create(10, 1),
                Section.create(12, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test11() {

        sectionManager.addSection(Section.create(0, 3));
        sectionManager.addSection(Section.create(4, 1));
        sectionManager.addSection(Section.create(6, 2, false));
        sectionManager.addSection(Section.create(7, 1));
        sectionManager.addSection(Section.create(9, 4));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item ( index = 4 )
        //Item ( index = 5 )
        //Subheader 7
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(4)).thenReturn(false);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(5);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item ( index = 4 )
        //Item ( index = 5 )
        //Item ( index = 6 )
        //Subheader 7
        //Item 8 ( index = 7 )
        //Subheader 9
        //Item 10 ( index = 8 )
        //Item 11 ( index = 9 )
        //Item 12 ( index = 10 )
        //Item 13 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(Notifier.createChanged(6));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 3, false),
                Section.create(7, 1),
                Section.create(9, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test12() {

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
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 6 ( index = 4 )
        //Subheader 7
        //Item 8 ( index = 5 )
        //Item 9 ( index = 6 )
        //Subheader 10
        //Item 11 ( index = 7 )
        //Subheader 12
        //Item 13 ( index = 8 )
        //Item 14 ( index = 9 )
        //Item 15 ( index = 10 )
        //Item 16 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0),
                        Notifier.createInserted(4)
                )
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4),
                Section.create(5, 1),
                Section.create(7, 2),
                Section.create(10, 1),
                Section.create(12, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test13() {

        sectionManager.addSection(Section.create(0, 3, false));
        sectionManager.addSection(Section.create(1, 1));
        sectionManager.addSection(Section.create(3, 2));
        sectionManager.addSection(Section.create(6, 1));
        sectionManager.addSection(Section.create(8, 4));

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
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(3);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 4 )
        //Subheader 3
        //Item 4 ( index = 5 )
        //Item 5 ( index = 6 )
        //Subheader 6
        //Item 7 ( index = 7 )
        //Subheader 8
        //Item 9 ( index = 8 )
        //Item 10 ( index = 9 )
        //Item 11 ( index = 10 )
        //Item 12 ( index = 11 )


        NotifyResult expectedResult = NotifyResult.create(Notifier.createChanged(0));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, false),
                Section.create(1, 1),
                Section.create(3, 2),
                Section.create(6, 1),
                Section.create(8, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test14() {

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
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(3);

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
        //Subheader 10
        //Item 11 ( index = 7 )
        //Subheader 12
        //Item 13 ( index = 8 )
        //Item 14 ( index = 9 )
        //Item 15 ( index = 10 )
        //Item 16 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(4),
                        Notifier.createInserted(5)
                )
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 2),
                Section.create(7, 2),
                Section.create(10, 1),
                Section.create(12, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test15() {

        sectionManager.addSection(Section.create(0, 3));
        sectionManager.addSection(Section.create(4, 1, false));
        sectionManager.addSection(Section.create(5, 2));
        sectionManager.addSection(Section.create(8, 1));
        sectionManager.addSection(Section.create(10, 4));

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
        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(false);
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 5 )
        //Item 7 ( index = 6 )
        //Subheader 8
        //Item 9 ( index = 7 )
        //Subheader 10
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )
        //Item 14 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(Notifier.createChanged(4));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 2, false),
                Section.create(5, 2),
                Section.create(8, 1),
                Section.create(10, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }

    @Test
    public void test16() {

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
        when(sectionProvider.getItemSize()).thenReturn(12);

        NotifyResult actualResult = sectionManager.onItemInserted(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item 7 ( index = 4 )
        //Subheader 8
        //Item 9 ( index = 5 )
        //Item 10 ( index = 6 )
        //Subheader 11
        //Item 12 ( index = 7 )
        //Subheader 13
        //Item 14 ( index = 8 )
        //Item 15 ( index = 9 )
        //Item 16 ( index = 10 )
        //Item 17 ( index = 11 )

        NotifyResult expectedResult = NotifyResult.create(Notifier.createInserted(4, 2));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3),
                Section.create(4, 1),
                Section.create(6, 1),
                Section.create(8, 2),
                Section.create(11, 1),
                Section.create(13, 4)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }
}
