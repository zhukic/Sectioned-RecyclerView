package com.zhukic.sectionedrecyclerview;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class SectionManagerCollapseAllSectionsTest {

    private SectionManager sectionManager;

    private SectionProvider sectionProvider;

    @Before
    public void beforeEachTest() {
        sectionProvider = mock(SectionProvider.class);
        sectionManager = new SectionManager(sectionProvider);
    }

    @Test
    public void test01() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 4, true));

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

        NotifyResult actualResult = sectionManager.collapseAllSections();

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 2
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 3
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createRemoved(1, 3),
                Notifier.createChanged(1),
                Notifier.createRemoved(2, 1),
                Notifier.createChanged(2),
                Notifier.createRemoved(3, 2),
                Notifier.createChanged(3),
                Notifier.createRemoved(4, 4)
        ));

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, false),
                Section.create(1, 1, false),
                Section.create(2, 2, false),
                Section.create(3, 4, false)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test02() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, true));
        sectionManager.addSection(Section.create(6, 2, true));
        sectionManager.addSection(Section.create(9, 4, true));

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

        NotifyResult actualResult = sectionManager.collapseAllSections();

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 2
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 3
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createRemoved(1, 3),
                Notifier.createChanged(1),
                Notifier.createRemoved(2, 1),
                Notifier.createChanged(2),
                Notifier.createRemoved(3, 2),
                Notifier.createChanged(3),
                Notifier.createRemoved(4, 4)
        ));

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, false),
                Section.create(1, 1, false),
                Section.create(2, 2, false),
                Section.create(3, 4, false)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test03() {

        sectionManager.addSection(Section.create(0, 3, false));
        sectionManager.addSection(Section.create(1, 1, false));
        sectionManager.addSection(Section.create(2, 2, false));
        sectionManager.addSection(Section.create(3, 4, false));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 2
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 3
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED

        NotifyResult actualResult = sectionManager.collapseAllSections();

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 2
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 3
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.empty();

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, false),
                Section.create(1, 1, false),
                Section.create(2, 2, false),
                Section.create(3, 4, false)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test04() {

        sectionManager.addSection(Section.create(0, 3, false));
        sectionManager.addSection(Section.create(1, 1, false));
        sectionManager.addSection(Section.create(2, 2, false));
        sectionManager.addSection(Section.create(3, 4, false));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 2
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 3
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED

        NotifyResult actualResult = sectionManager.collapseAllSections();

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Item ( index = 2 ) //COLLAPSED
        //Subheader 1
        //Item ( index = 3 ) //COLLAPSED
        //Subheader 2
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 3
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Item ( index = 8 ) //COLLAPSED
        //Item ( index = 9 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.empty();

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, false),
                Section.create(1, 1, false),
                Section.create(2, 2, false),
                Section.create(3, 4, false)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

}
