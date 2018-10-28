package com.zhukic.sectionedrecyclerview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SectionManagerOnDataChangedTest {

    private SectionManager sectionManager;

    private SectionProvider sectionProvider;

    @Before
    public void beforeEachTest() {
        sectionProvider = Mockito.mock(SectionProvider.class);
        sectionManager = new SectionManager(sectionProvider);
    }

    @Test
    public void test() {
        sectionManager.addSection(Section.create(0, 4));
        sectionManager.addSection(Section.create(5, 2));
        sectionManager.addSection(Section.create(8, 5));

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
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        when(sectionProvider.onPlaceSubheaderBetweenItems(3)).thenReturn(true);
        when(sectionProvider.onPlaceSubheaderBetweenItems(5)).thenReturn(true);
        when(sectionProvider.getItemSize()).thenReturn(9);

        NotifyResult actualResult = sectionManager.onDataChanged();

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

        NotifyResult expectedResult = NotifyResult.create(Notifier.createAllDataChanged());

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, true),
                Section.create(5, 2, true),
                Section.create(8, 3, true)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);
    }
}
