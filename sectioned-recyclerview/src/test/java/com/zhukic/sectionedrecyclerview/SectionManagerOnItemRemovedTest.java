package com.zhukic.sectionedrecyclerview;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SectionManagerOnItemRemovedTest {

    private SectionManager sectionManager;

    private SectionProvider sectionProvider;

    @Before
    public void beforeEachTest() {
        sectionProvider = mock(SectionProvider.class);
        sectionManager = new SectionManager(sectionProvider);
        when(sectionProvider.getItemSize()).thenReturn(9);
    }

    @Test
    public void test01() {

        sectionManager.addSection(Section.create(0, 2, false));
        sectionManager.addSection(Section.create(1, 1, true));
        sectionManager.addSection(Section.create(3, 3, true));
        sectionManager.addSection(Section.create(7, 2, false));
        sectionManager.addSection(Section.create(8, 1, true));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Item ( index = 1 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 2 )
        //Subheader 3
        //Item 4 ( index = 3 )
        //Item 5 ( index = 4 )
        //Item 6 ( index = 5 )
        //Subheader 7
        //Item ( index = 6 ) //COLLAPSED
        //Item ( index = 7 ) //COLLAPSED
        //Subheader 8
        //Item 9 ( index = 8 )

        NotifyResult actualResult = sectionManager.onItemRemoved(1);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 1 )
        //Subheader 3
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Subheader 7
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 8
        //Item 9 ( index = 7 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(0)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, false),
                Section.create(1, 1, true),
                Section.create(3, 3, true),
                Section.create(7, 2, false),
                Section.create(8, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test02() {

        sectionManager.addSection(Section.create(0, 1, false));
        sectionManager.addSection(Section.create(1, 1, true));
        sectionManager.addSection(Section.create(3, 3, true));
        sectionManager.addSection(Section.create(7, 2, false));
        sectionManager.addSection(Section.create(8, 1, true));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED
        //Subheader 1
        //Item 2 ( index = 1 )
        //Subheader 3
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Item 6 ( index = 4 )
        //Subheader 7
        //Item ( index = 5 ) //COLLAPSED
        //Item ( index = 6 ) //COLLAPSED
        //Subheader 8
        //Item 9 ( index = 7 )

        NotifyResult actualResult = sectionManager.onItemRemoved(0);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 7
        //Item 8 ( index = 6 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createRemoved(0)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 3, true),
                Section.create(6, 2, false),
                Section.create(7, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test03() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 3, true));
        sectionManager.addSection(Section.create(6, 2, false));
        sectionManager.addSection(Section.create(7, 1, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item 3 ( index = 1 )
        //Item 4 ( index = 2 )
        //Item 5 ( index = 3 )
        //Subheader 6
        //Item ( index = 4 ) //COLLAPSED
        //Item ( index = 5 ) //COLLAPSED
        //Subheader 7
        //Item 8 ( index = 6 )

        NotifyResult actualResult = sectionManager.onItemRemoved(0);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 5 )

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createRemoved(0, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 2, false),
                Section.create(5, 1, true)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test04() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 2, false));
        sectionManager.addSection(Section.create(5, 1, true));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED
        //Subheader 5
        //Item 6 ( index = 5 )

        NotifyResult actualResult = sectionManager.onItemRemoved(5);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createRemoved(5, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 2, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test05() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 2, false));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED
        //Item ( index = 4 ) //COLLAPSED

        NotifyResult actualResult = sectionManager.onItemRemoved(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createChanged(4)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 3, true),
                Section.create(4, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test06() {

        sectionManager.addSection(Section.create(0, 3, true));
        sectionManager.addSection(Section.create(4, 1, false));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Subheader 4
        //Item ( index = 3 ) //COLLAPSED

        NotifyResult actualResult = sectionManager.onItemRemoved(2);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Subheader 3
        //Item ( index = 2 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createRemoved(3)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 2, true),
                Section.create(3, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test07() {

        sectionManager.addSection(Section.create(0, 2, true));
        sectionManager.addSection(Section.create(3, 1, false));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Subheader 3
        //Item ( index = 2 ) //COLLAPSED

        NotifyResult actualResult = sectionManager.onItemRemoved(1);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(Arrays.asList(
                Notifier.createChanged(0),
                Notifier.createRemoved(2)
        ));

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Arrays.asList(
                Section.create(0, 1, true),
                Section.create(2, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test08() {

        sectionManager.addSection(Section.create(0, 1, true));
        sectionManager.addSection(Section.create(2, 1, false));

        //Subheader 0
        //Item 1 ( index = 0 )
        //Subheader 2
        //Item ( index = 1 ) //COLLAPSED

        NotifyResult actualResult = sectionManager.onItemRemoved(0);

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createRemoved(0, 2)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        List<Section> expectedSections = Collections.singletonList(
                Section.create(0, 1, false)
        );

        assertThat(sectionManager.getSections()).containsExactlyElementsOf(expectedSections);

    }

    @Test
    public void test09() {

        sectionManager.addSection(Section.create(0, 1, false));

        //Subheader 0
        //Item ( index = 0 ) //COLLAPSED

        NotifyResult actualResult = sectionManager.onItemRemoved(0);

        //

        NotifyResult expectedResult = NotifyResult.create(
                Notifier.createRemoved(0)
        );

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(sectionManager.getSections()).isEmpty();

    }

}
