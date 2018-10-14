package com.zhukic.sectionedrecyclerview;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SectionManageIsLastItemInSectionTest {

    private SectionManager sectionManager;

    @Before
    public void beforeEachTest() {
        sectionManager = new SectionManager(mock(SectionProvider.class));
    }

    @Test
    public void test() {

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

        assertThat(sectionManager.isLastItemInSection(0)).isFalse();
        assertThat(sectionManager.isLastItemInSection(1)).isFalse();
        assertThat(sectionManager.isLastItemInSection(2)).isFalse();
        assertThat(sectionManager.isLastItemInSection(3)).isTrue();
        assertThat(sectionManager.isLastItemInSection(4)).isFalse();
        assertThat(sectionManager.isLastItemInSection(5)).isTrue();
        assertThat(sectionManager.isLastItemInSection(6)).isFalse();
        assertThat(sectionManager.isLastItemInSection(7)).isFalse();
        assertThat(sectionManager.isLastItemInSection(8)).isTrue();
        assertThat(sectionManager.isLastItemInSection(9)).isFalse();
        assertThat(sectionManager.isLastItemInSection(10)).isFalse();
        assertThat(sectionManager.isLastItemInSection(11)).isFalse();
        assertThat(sectionManager.isLastItemInSection(12)).isFalse();
        assertThat(sectionManager.isLastItemInSection(13)).isTrue();
    }
}
