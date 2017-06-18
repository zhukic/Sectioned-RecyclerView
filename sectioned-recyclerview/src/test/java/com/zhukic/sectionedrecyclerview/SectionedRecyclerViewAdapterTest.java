package com.zhukic.sectionedrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class SectionedRecyclerViewAdapterTest {

    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    @Before
    public void beforeEachTest() {

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Subheader 3
        //Item 4 ( index = 2 )
        //Subheader 5
        //Item 6 ( index = 3 )
        //Subheader 7
        //Item 8 ( index = 4 )
        //Item 9 ( index = 5 )
        //Item 10 ( index = 6 )

        sectionedRecyclerViewAdapter = mock(SectionedRecyclerViewAdapter.class, Mockito.CALLS_REAL_METHODS);

        sectionedRecyclerViewAdapter.setSectionManager(new SectionManager());

        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(0)).thenReturn(false);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(1)).thenReturn(true);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(2)).thenReturn(true);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(3)).thenReturn(true);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(4)).thenReturn(false);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(5)).thenReturn(false);

        when(sectionedRecyclerViewAdapter.getItemSize()).thenReturn(7);

    }

    @Test
    public void sectionManagerNotNull() {
        assertNotNull(sectionedRecyclerViewAdapter.getSectionManager());
    }

    @Test
    public void onAttachedToRecyclerView() {

        sectionedRecyclerViewAdapter.onAttachedToRecyclerView(mock(RecyclerView.class));

        assertThat(sectionedRecyclerViewAdapter.getItemCount()).isEqualTo(11);

        assertThat(sectionedRecyclerViewAdapter.getSectionManager().getSections().size()).isEqualTo(4);

        assertThat(sectionedRecyclerViewAdapter.getItemViewType(0)).isEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(1)).isNotEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(2)).isNotEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(3)).isEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(4)).isNotEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(5)).isEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(6)).isNotEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(7)).isEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(8)).isNotEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(9)).isNotEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);
        assertThat(sectionedRecyclerViewAdapter.getItemViewType(10)).isNotEqualTo(SectionedRecyclerViewAdapter.TYPE_HEADER);

    }

    @Test
    public void onCreateViewHolder_shouldCallOnCreateSubheaderViewHolder() {

        ViewGroup viewGroup = mock(ViewGroup.class);

        sectionedRecyclerViewAdapter.onCreateViewHolder(viewGroup,
                SectionedRecyclerViewAdapter.TYPE_HEADER);

        verify(sectionedRecyclerViewAdapter).onCreateSubheaderViewHolder(viewGroup, SectionedRecyclerViewAdapter.TYPE_HEADER);
        //verifyNoMoreInteractions(sectionedRecyclerViewAdapter);

    }

    @Test
    public void onCreateViewHolder_shouldCallOnCreateItemViewHolder() {

        ViewGroup viewGroup = mock(ViewGroup.class);

        int viewType = sectionedRecyclerViewAdapter.getViewType(0);

        sectionedRecyclerViewAdapter.onCreateViewHolder(viewGroup, viewType);

        verify(sectionedRecyclerViewAdapter).onCreateItemViewHolder(viewGroup, viewType);
        //verifyNoMoreInteractions(sectionedRecyclerViewAdapter);

    }

    @Test
    public void onBindViewHolder_shouldCallOnBindSubheaderViewHolder() {

        RecyclerView.ViewHolder viewHolder = mock(RecyclerView.ViewHolder.class);

    }

    @Test
    public void getItemCount_shouldBeZeroByDefault() {
        assertThat(sectionedRecyclerViewAdapter.getItemCount()).isEqualTo(0);
    }

}