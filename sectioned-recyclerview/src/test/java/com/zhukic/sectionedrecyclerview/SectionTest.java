package com.zhukic.sectionedrecyclerview;

import org.junit.Test;

import static org.junit.Assert.*;

public class SectionTest {

    @Test
    public void checkConstructor() {

        Section section = new Section(10);

        assertEquals(10, section.getSubheaderPosition());
        assertEquals(0, section.getItemCount());
        assertEquals(true, section.isExpanded());

    }

}