package com.zhukic.sectionedrecyclerview;

import com.zhukic.sectionedrecyclerview.result.NotifyResultNew;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class SectionManagerCollapseSectionTest {

    private SectionManager sectionManager;

    private SectionProvider sectionProvider;

    @Before
    public void beforeEachTest() {
        sectionProvider = mock(SectionProvider.class);
        sectionManager = new SectionManager(sectionProvider);
    }

    @Test
    public void test01() {

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

        NotifyResultNew actualResult = sectionManager.collapseSection(1, true);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item ( index = 4 ) COLLAPSED
        //Item ( index = 5 ) COLLAPSED
        //Subheader 6
        //Item 7 ( index = 6 )
        //Item 8 ( index = 7 )
        //Item 9 ( index = 8 )
        //Item 10 ( index = 9 )
        //Item 11 ( index = 10 )

        NotifyResultNew expectedResult = NotifyResultNew.create(Arrays.asList(
                Notifier.createChanged(5),
                Notifier.createRemoved(6,2)
        ));

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, true),
                Section.create(5, 2, false),
                Section.create(6, 5, true)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);
        
    }

    @Test
    public void test02() {

        sectionManager.addSection(Section.create(0, 4, true));
        sectionManager.addSection(Section.create(5, 2, false));
        sectionManager.addSection(Section.create(6, 5, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item ( index = 4 ) COLLAPSED
        //Item ( index = 5 ) COLLAPSED
        //Subheader 6
        //Item 7 ( index = 6 )
        //Item 8 ( index = 7 )
        //Item 9 ( index = 8 )
        //Item 10 ( index = 9 )
        //Item 11 ( index = 10 )

        NotifyResultNew actualResult = sectionManager.collapseSection(0, false);

        //Subheader 0
        //Item ( index = 0 ) COLLAPSED
        //Item ( index = 1 ) COLLAPSED
        //Item ( index = 2 ) COLLAPSED
        //Item ( index = 3 ) COLLAPSED
        //Subheader 1
        //Item ( index = 4 ) COLLAPSED
        //Item ( index = 5 ) COLLAPSED
        //Subheader 2
        //Item 3 ( index = 6 )
        //Item 4 ( index = 7 )
        //Item 5 ( index = 8 )
        //Item 6 ( index = 9 )
        //Item 7 ( index = 10 )

        NotifyResultNew expectedResult = NotifyResultNew.create(Collections.singletonList(
                Notifier.createRemoved(1, 4)
        ));

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, false),
                Section.create(1, 2, false),
                Section.create(2, 5, true)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test03() {

        sectionManager.addSection(Section.create(0, 4, false));
        sectionManager.addSection(Section.create(1, 2, false));
        sectionManager.addSection(Section.create(2, 5, true));

        //Subheader 0
        //Item ( index = 0 ) COLLAPSED
        //Item ( index = 1 ) COLLAPSED
        //Item ( index = 2 ) COLLAPSED
        //Item ( index = 3 ) COLLAPSED
        //Subheader 1
        //Item ( index = 4 ) COLLAPSED
        //Item ( index = 5 ) COLLAPSED
        //Subheader 2
        //Item 3 ( index = 6 )
        //Item 4 ( index = 7 )
        //Item 5 ( index = 8 )
        //Item 6 ( index = 9 )
        //Item 7 ( index = 10 )

        NotifyResultNew actualResult = sectionManager.collapseSection(2, true);

        //Subheader 0
        //Item ( index = 0 ) COLLAPSED
        //Item ( index = 1 ) COLLAPSED
        //Item ( index = 2 ) COLLAPSED
        //Item ( index = 3 ) COLLAPSED
        //Subheader 1
        //Item ( index = 4 ) COLLAPSED
        //Item ( index = 5 ) COLLAPSED
        //Subheader 2
        //Item ( index = 6 ) COLLAPSED
        //Item ( index = 7 ) COLLAPSED
        //Item ( index = 8 ) COLLAPSED
        //Item ( index = 9 ) COLLAPSED
        //Item ( index = 10 ) COLLAPSED

        NotifyResultNew expectedResult = NotifyResultNew.create(Arrays.asList(
                Notifier.createChanged(2),
                Notifier.createRemoved(3, 5)
        ));

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, false),
                Section.create(1, 2, false),
                Section.create(2, 5, false)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test04() {

        sectionManager.addSection(Section.create(0, 4, false));
        sectionManager.addSection(Section.create(1, 2, false));
        sectionManager.addSection(Section.create(2, 5, false));

        NotifyResultNew actualResult = sectionManager.collapseSection(1, true);

        //Subheader 0
        //Item ( index = 0 ) COLLAPSED
        //Item ( index = 1 ) COLLAPSED
        //Item ( index = 2 ) COLLAPSED
        //Item ( index = 3 ) COLLAPSED
        //Subheader 1
        //Item ( index = 4 ) COLLAPSED
        //Item ( index = 5 ) COLLAPSED
        //Subheader 2
        //Item ( index = 6 ) COLLAPSED
        //Item ( index = 7 ) COLLAPSED
        //Item ( index = 8 ) COLLAPSED
        //Item ( index = 9 ) COLLAPSED
        //Item ( index = 10 ) COLLAPSED

        NotifyResultNew expectedResult = NotifyResultNew.create(Collections.singletonList(
                Notifier.createChanged(1)
        ));

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, false),
                Section.create(1, 2, false),
                Section.create(2, 5, false)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test05() {

        sectionManager.addSection(Section.create(0, 4, false));
        sectionManager.addSection(Section.create(1, 2, false));
        sectionManager.addSection(Section.create(2, 5, false));

        NotifyResultNew actualResult = sectionManager.collapseSection(2, false);

        //Subheader 0
        //Item ( index = 0 ) COLLAPSED
        //Item ( index = 1 ) COLLAPSED
        //Item ( index = 2 ) COLLAPSED
        //Item ( index = 3 ) COLLAPSED
        //Subheader 1
        //Item ( index = 4 ) COLLAPSED
        //Item ( index = 5 ) COLLAPSED
        //Subheader 2
        //Item ( index = 6 ) COLLAPSED
        //Item ( index = 7 ) COLLAPSED
        //Item ( index = 8 ) COLLAPSED
        //Item ( index = 9 ) COLLAPSED
        //Item ( index = 10 ) COLLAPSED

        NotifyResultNew expectedResult = NotifyResultNew.empty();

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 4, false),
                Section.create(1, 2, false),
                Section.create(2, 5, false)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);
        
    }

}
