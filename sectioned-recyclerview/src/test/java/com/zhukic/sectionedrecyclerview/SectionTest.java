package com.zhukic.sectionedrecyclerview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class SectionTest {

    @Test
    public void test_constructor() {

        Section section = new Section(10);

        assertEquals(10, section.getSubheaderPosition());
        assertEquals(0, section.getItemsCount());
        assertEquals(true, section.isExpanded());

    }

}