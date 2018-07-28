package com.zhukic.sectionedrecyclerview;

import com.zhukic.sectionedrecyclerview.result.NotifyResultNew;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

//TODO review
public class SectionManagerExpandAllSectionsTest {

    private SectionManager sectionManager;

    private SectionProvider sectionProvider;

    @Before
    public void beforeEachTest() {
        sectionProvider = mock(SectionProvider.class);
        sectionManager = new SectionManager(sectionProvider);
    }

    @Test
    public void test01() {

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

        NotifyResultNew actualResult = sectionManager.expandAllSections();

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


        NotifyResultNew expectedResult = NotifyResultNew.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createInserted(1, 3),
                Notifier.createChanged(4),
                Notifier.createInserted(5, 1),
                Notifier.createChanged(6),
                Notifier.createInserted(7, 2),
                Notifier.createChanged(9),
                Notifier.createInserted(10, 4)
        ));

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 4, true)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test02() {

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

        NotifyResultNew actualResult = sectionManager.expandAllSections();

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

        NotifyResultNew expectedResult = NotifyResultNew.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createInserted(1, 3),
                Notifier.createChanged(4),
                Notifier.createInserted(5, 1),
                Notifier.createChanged(6),
                Notifier.createInserted(7, 2),
                Notifier.createChanged(9),
                Notifier.createInserted(10, 4)
        ));

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 4, true)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test03() {

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

        NotifyResultNew actualResult = sectionManager.expandAllSections();

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

        NotifyResultNew expectedResult = NotifyResultNew.empty();

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 4, true)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        //TODO review
        verifyZeroInteractions(sectionProvider);

    }

    @Test
    public void test04() {

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

        NotifyResultNew actualResult = sectionManager.expandAllSections();

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

        NotifyResultNew expectedResult = NotifyResultNew.empty();

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, true),
                Section.create(6, 2, true),
                Section.create(9, 4, true)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

        verifyZeroInteractions(sectionProvider);

    }

}
