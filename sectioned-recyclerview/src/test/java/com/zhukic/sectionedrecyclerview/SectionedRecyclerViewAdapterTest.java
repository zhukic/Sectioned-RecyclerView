package com.zhukic.sectionedrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;

import static org.junit.Assert.*;

import static org.assertj.core.api.Java6Assertions.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SectionedRecyclerViewAdapterTest {

    private SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder> sectionedRecyclerViewAdapter;

    private SectionManager sectionManager;

    private void init() {

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
        sectionManager = new SectionManager();

        sectionedRecyclerViewAdapter.setSectionManager(sectionManager);

        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(0)).thenReturn(false);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(1)).thenReturn(true);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(2)).thenReturn(true);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(3)).thenReturn(true);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(4)).thenReturn(false);
        when(sectionedRecyclerViewAdapter.onPlaceSubheaderBetweenItems(5)).thenReturn(false);

        when(sectionedRecyclerViewAdapter.getItemSize()).thenReturn(7);

        sectionedRecyclerViewAdapter.initSubheaderPositions();

    }

    @Test
    public void sectionManagerNotNull() {
        init();
        assertNotNull(sectionedRecyclerViewAdapter.getSectionManager());
    }

    @Test
    public void onAttachedToRecyclerView() {

        init();

        sectionedRecyclerViewAdapter.onAttachedToRecyclerView(mock(RecyclerView.class));

        assertThat(sectionedRecyclerViewAdapter.getItemCount()).isEqualTo(11);

        assertThat(sectionedRecyclerViewAdapter.getSectionManager().getSections().size()).isEqualTo(4);

        assertThat(sectionManager.getSection(0).getSubheaderPosition()).isEqualTo(0);
        assertThat(sectionManager.getSection(0).getItemCount()).isEqualTo(2);
        assertThat(sectionManager.getSection(1).getSubheaderPosition()).isEqualTo(3);
        assertThat(sectionManager.getSection(1).getItemCount()).isEqualTo(1);
        assertThat(sectionManager.getSection(2).getSubheaderPosition()).isEqualTo(5);
        assertThat(sectionManager.getSection(2).getItemCount()).isEqualTo(1);
        assertThat(sectionManager.getSection(3).getSubheaderPosition()).isEqualTo(7);
        assertThat(sectionManager.getSection(3).getItemCount()).isEqualTo(3);

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

        init();

        ViewGroup viewGroup = mock(ViewGroup.class);

        sectionedRecyclerViewAdapter.onCreateViewHolder(viewGroup,
                SectionedRecyclerViewAdapter.TYPE_HEADER);

        verify(sectionedRecyclerViewAdapter).onCreateSubheaderViewHolder(viewGroup, SectionedRecyclerViewAdapter.TYPE_HEADER);

    }

    @Test
    public void onCreateViewHolder_shouldCallOnCreateItemViewHolder() {

        init();

        ViewGroup viewGroup = mock(ViewGroup.class);

        int viewType = sectionedRecyclerViewAdapter.getViewType(0);

        sectionedRecyclerViewAdapter.onCreateViewHolder(viewGroup, viewType);

        verify(sectionedRecyclerViewAdapter).onCreateItemViewHolder(viewGroup, viewType);

    }

    @Test
    public void onBindViewHolder() {

        init();

        for (int i = 0; i < sectionedRecyclerViewAdapter.getItemCount(); i++) {

            RecyclerView.ViewHolder viewHolder = mock(RecyclerView.ViewHolder.class);

            sectionedRecyclerViewAdapter.onBindViewHolder(viewHolder, i);

            if (i == 0 || i == 3 || i == 5 || i == 7) {
                verify(sectionedRecyclerViewAdapter).onBindSubheaderViewHolder(viewHolder, sectionManager.getItemPositionForSubheaderViewHolder(i));
            } else {
                verify(sectionedRecyclerViewAdapter).onBindItemViewHolder(viewHolder, sectionManager.getItemPositionForItemViewHolder(i));
            }

        }

    }

    @Test
    public void getItemCount() {
        init();
        assertThat(sectionedRecyclerViewAdapter.getItemCount()).isEqualTo(11);
    }

    @Test
    public void notifyDataChanged() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        sectionedRecyclerViewAdapter.notifyDataChanged();

        verify(sectionManager).clear();
        verify(adapterDataObserver).onChanged();

    }

    @Test
    public void notifyItemInsertedAtPosition() {

        //

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                switch (sectionManager.getItemCount()) {
                    case 5:
                        return true;
                    case 7:
                        return false;
                    case 8:
                        return true;
                    case 10:
                        if (position == 5) {
                            return true;
                        }
                        return false;
                    case 11:
                        return false;
                    case 12:
                        return false;
                    case 13:
                        if (position == 6) {
                            return true;
                        }
                        return false;
                    default:
                        return false;
                }
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                switch (sectionManager.getItemCount()) {
                    case 4:
                        return 4;
                    case 5:
                        return 5;
                    case 7:
                        return 6;
                    case 8:
                        return 7;
                    case 10:
                        return 8;
                    case 11:
                        return 9;
                    case 12:
                        return 10;
                    case 13:
                        return 11;
                    default:
                        return 0;
                }
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(0);

        int adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(0);

        //Subheader 0
        //Item 1 ( index = 0 )

        assertThat(adapterPosition).isEqualTo(0);

        verify(sectionManager).getItemCount();
        verify(sectionManager).insertItem(0, true);
        verifyNoMoreInteractions(sectionManager);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(0, 2);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(2);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(0);

        //Subheader 0
        //newItem 1 ( index = 0 )
        //Item 2 ( index = 1 )

        assertThat(adapterPosition).isEqualTo(1);

        verify(sectionManager, times(2)).getItemCount();
        verify(sectionManager).insertItem(1, false);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(1, 1);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(3);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(0);

        //Subheader 0
        //newItem 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )

        assertThat(adapterPosition).isEqualTo(1);

        verify(sectionManager, times(2)).getItemCount();
        verify(sectionManager).insertItem(1, false);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(1, 1);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(4);
        when(sectionManager.getSectionsCount()).thenReturn(1);
        when(sectionManager.isSectionExpanded(0)).thenReturn(true);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(3);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //newItem 4 ( index = 3 )

        assertThat(adapterPosition).isEqualTo(4);

        verify(sectionManager, times(3)).getItemCount();
        verify(sectionManager).insertItem(4, false);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(4, 1);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(5);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //newItem 6 ( index = 4 )

        assertThat(adapterPosition).isEqualTo(5);

        verify(sectionManager, times(3)).getItemCount();
        verify(sectionManager).insertItem(5, true);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(5, 2);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(7);
        when(sectionManager.getAdapterPositionForItem(4)).thenReturn(6);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //newItem 6 ( index = 4 )
        //Item 7 ( index = 5 )

        assertThat(adapterPosition).isEqualTo(6);

        verify(sectionManager, times(4)).getItemCount();
        verify(sectionManager).insertItem(6, false);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(6, 1);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(8);
        when(sectionManager.getAdapterPositionForItem(4)).thenReturn(6);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //newItem 6 ( index = 4 )
        //Subheader 7
        //Item 8 ( index = 5 )
        //Item 9 ( index = 6 )

        assertThat(adapterPosition).isEqualTo(5);

        verify(sectionManager, times(3)).getItemCount();
        verify(sectionManager).insertItem(5, true);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(5, 2);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(10);
        when(sectionManager.getAdapterPositionForItem(5)).thenReturn(8);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(5);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 6 ( index = 4 )
        //newItem 7 ( index = 5 )
        //Subheader 8
        //Item 9 ( index = 6 )
        //Item 10 ( index = 7 )

        assertThat(adapterPosition).isEqualTo(7);

        verify(sectionManager, times(4)).getItemCount();
        verify(sectionManager).insertItem(7, false);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(7, 1);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(11);
        when(sectionManager.getAdapterPositionForItem(4)).thenReturn(6);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(4);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //newItem 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )

        assertThat(adapterPosition).isEqualTo(6);

        verify(sectionManager, times(4)).getItemCount();
        verify(sectionManager).insertItem(6, false);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(6, 1);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(12);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(9);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Item 8 ( index = 6 )
        //Subheader 9
        //Item 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //newItem 12 ( index = 9 )

        assertThat(adapterPosition).isEqualTo(12);

        verify(sectionManager, times(3)).getItemCount();
        verify(sectionManager).insertItem(12, false);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(12, 1);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

        when(sectionManager.getItemCount()).thenReturn(13);
        when(sectionManager.getAdapterPositionForItem(7)).thenReturn(10);

        adapterPosition = sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(7);

        //Subheader 0
        //Item 1 ( index = 0 )
        //Item 2 ( index = 1 )
        //Item 3 ( index = 2 )
        //Item 4 ( index = 3 )
        //Subheader 5
        //Item 6 ( index = 4 )
        //Item 7 ( index = 5 )
        //Item 8 ( index = 6 )
        //Subheader 9
        //newItem 10 ( index = 7 )
        //Item 11 ( index = 8 )
        //Item 12 ( index = 9 )
        //Item 13 ( index = 10 )

        assertThat(adapterPosition).isEqualTo(10);

        verify(sectionManager, times(4)).getItemCount();
        verify(sectionManager).insertItem(10, false);
        reset(sectionManager);

        verify(adapterDataObserver).onItemRangeInserted(10, 1);
        verifyNoMoreInteractions(adapterDataObserver);
        reset(adapterDataObserver);

    }

    @Test
    public void notifyItemChangedAtPosition() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        int itemPosition = 0;
        int adapterPosition = 1;

        when(sectionManager.getAdapterPositionForItem(itemPosition)).thenReturn(adapterPosition);

        int actualAdapterPosition = sectionedRecyclerViewAdapter.notifyItemChangedAtPosition(itemPosition);

        assertThat(actualAdapterPosition).isEqualTo(adapterPosition);

        verify(sectionManager).getAdapterPositionForItem(itemPosition);
        verifyNoMoreInteractions(sectionManager);

        verify(adapterDataObserver).onItemRangeChanged(adapterPosition, 1, null);
        verifyNoMoreInteractions(adapterDataObserver);

    }

    @Test
    public void notifyItemRemovedAtPosition_shouldRemoveItemAndSectionSubheader() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        int itemPosition = 0;
        int itemAdapterPosition = 1;

        when(sectionManager.getAdapterPositionForItem(itemPosition)).thenReturn(1);
        when(sectionManager.removeItem(itemAdapterPosition)).thenReturn(true);
        when(sectionManager.getItemCount()).thenReturn(2);

        int actualAdapterPosition = sectionedRecyclerViewAdapter.notifyItemRemovedAtPosition(itemPosition);

        assertThat(actualAdapterPosition).isEqualTo(0);

        verify(sectionManager).getAdapterPositionForItem(itemPosition);
        verify(sectionManager).removeItem(itemAdapterPosition);
        verify(sectionManager).getItemCount();
        verifyNoMoreInteractions(sectionManager);

        verify(adapterDataObserver).onItemRangeRemoved(itemAdapterPosition - 1, 2);
        verifyNoMoreInteractions(adapterDataObserver);

    }

    @Test
    public void notifyItemRemovedAtPosition_shouldRemoveOnlyItem() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        int itemPosition = 0;
        int itemAdapterPosition = 1;

        when(sectionManager.getAdapterPositionForItem(itemPosition)).thenReturn(1);
        when(sectionManager.removeItem(itemAdapterPosition)).thenReturn(false);
        when(sectionManager.getItemCount()).thenReturn(2);

        int actualAdapterPosition = sectionedRecyclerViewAdapter.notifyItemRemovedAtPosition(itemPosition);

        assertThat(actualAdapterPosition).isEqualTo(1);

        verify(sectionManager).getAdapterPositionForItem(itemPosition);
        verify(sectionManager).removeItem(itemAdapterPosition);
        verify(sectionManager).getItemCount();
        verifyNoMoreInteractions(sectionManager);

        verify(adapterDataObserver).onItemRangeRemoved(itemAdapterPosition, 1);
        verifyNoMoreInteractions(adapterDataObserver);

    }

    @Test
    public void setGridLayoutManager() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        int spanCount = 2;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(RuntimeEnvironment.application, spanCount);

        sectionedRecyclerViewAdapter.setGridLayoutManager(gridLayoutManager);

        int position = 0;

        when(sectionManager.isSectionSubheaderOnPosition(position)).thenReturn(true);
        assertThat(gridLayoutManager.getSpanSizeLookup().getSpanSize(position)).isEqualTo(spanCount);

        position = 1;

        when(sectionManager.isSectionSubheaderOnPosition(position)).thenReturn(false);
        assertThat(gridLayoutManager.getSpanSizeLookup().getSpanSize(position)).isEqualTo(1);

    }

    @Test
    public void isSubheaderAtPosition() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        Boolean isSubheaderAtPosition = true;

        int position = 0;

        when(sectionManager.isSectionSubheaderOnPosition(position)).thenReturn(isSubheaderAtPosition);
        when(sectionManager.getItemCount()).thenReturn(5);

        Boolean result = sectionedRecyclerViewAdapter.isSubheaderAtPosition(position);

        assertThat(isSubheaderAtPosition).isSameAs(result);

        verify(sectionManager).isSectionSubheaderOnPosition(position);
        verify(sectionManager).getItemCount();
        verifyNoMoreInteractions(sectionManager);

    }

    @Test
    public void expandSection_shouldExpandSection() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        Integer sectionIndex = 0;
        Integer subheaderPosition = 0;
        Section section = new Section(subheaderPosition);
        Integer expandedItemCount = 5;

        when(sectionManager.getSectionsCount()).thenReturn(1);
        when(sectionManager.isSectionExpanded(sectionIndex)).thenReturn(false);
        when(sectionManager.getSection(sectionIndex)).thenReturn(section);
        when(sectionManager.expandSection(sectionIndex)).thenReturn(expandedItemCount);

        sectionedRecyclerViewAdapter.expandSection(sectionIndex);

        verify(sectionManager).getSectionsCount();
        verify(sectionManager).isSectionExpanded(sectionIndex);
        verify(sectionManager).getSection(sectionIndex);
        verify(sectionManager).expandSection(sectionIndex);
        verifyNoMoreInteractions(sectionManager);

        verify(adapterDataObserver).onItemRangeChanged(subheaderPosition, 1, null);
        verify(adapterDataObserver).onItemRangeInserted(subheaderPosition + 1, expandedItemCount);
        verifyNoMoreInteractions(adapterDataObserver);

    }

    @Test
    public void expandSection_shouldDoNothing() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        Integer sectionIndex = 0;

        when(sectionManager.getSectionsCount()).thenReturn(1);
        when(sectionManager.isSectionExpanded(sectionIndex)).thenReturn(true);

        sectionedRecyclerViewAdapter.expandSection(sectionIndex);

        verify(sectionManager).getSectionsCount();
        verify(sectionManager).isSectionExpanded(sectionIndex);
        verifyNoMoreInteractions(sectionManager);

        verifyZeroInteractions(adapterDataObserver);

    }

    @Test
    public void expandAllSections() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        sectionedRecyclerViewAdapter.expandAllSections();

        verify(sectionManager).expandAllSections();
        verifyNoMoreInteractions(sectionManager);

        verify(adapterDataObserver).onChanged();
        verifyNoMoreInteractions(adapterDataObserver);

    }

    @Test
    public void collapseSection() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        Integer sectionIndex = 0;
        Integer subheaderPosition = 0;
        Section section = new Section(subheaderPosition);
        Integer collapsedItemCount = 5;

        when(sectionManager.getSectionsCount()).thenReturn(1);
        when(sectionManager.getSections()).thenReturn(Collections.singletonList(section));
        when(sectionManager.isSectionExpanded(sectionIndex)).thenReturn(true);
        when(sectionManager.getSection(sectionIndex)).thenReturn(section);
        when(sectionManager.collapseSection(sectionIndex)).thenReturn(collapsedItemCount);

        sectionedRecyclerViewAdapter.collapseSection(sectionIndex);

        verify(sectionManager).getSectionsCount();
        verify(sectionManager).isSectionExpanded(sectionIndex);
        verify(sectionManager).getSection(sectionIndex);
        verify(sectionManager).collapseSection(sectionIndex);
        verifyNoMoreInteractions(sectionManager);

        verify(adapterDataObserver).onItemRangeChanged(subheaderPosition, 1, null);
        verify(adapterDataObserver).onItemRangeRemoved(subheaderPosition + 1, collapsedItemCount);
        verifyNoMoreInteractions(adapterDataObserver);

    }

    @Test
    public void collapseSection_shouldDoNothing() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        Integer sectionIndex = 0;

        when(sectionManager.getSectionsCount()).thenReturn(1);
        when(sectionManager.getSections()).thenReturn(Collections.singletonList(new Section(0)));
        when(sectionManager.isSectionExpanded(sectionIndex)).thenReturn(false);

        sectionedRecyclerViewAdapter.collapseSection(sectionIndex);

        verify(sectionManager).getSectionsCount();
        verify(sectionManager).isSectionExpanded(sectionIndex);
        verifyNoMoreInteractions(sectionManager);

        verifyZeroInteractions(adapterDataObserver);

    }

    @Test
    public void collapseAllSections() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        sectionedRecyclerViewAdapter.collapseAllSections();

        verify(sectionManager).collapseAllSections();
        verifyNoMoreInteractions(sectionManager);

        verify(adapterDataObserver).onChanged();
        verifyNoMoreInteractions(adapterDataObserver);

    }

    @Test
    public void isSectionExpanded() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 0;
            }
        };

        Integer sectionIndex = 0;
        Boolean isSectionExpanded = true;

        when(sectionManager.getSectionsCount()).thenReturn(1);
        when(sectionManager.isSectionExpanded(sectionIndex)).thenReturn(isSectionExpanded);

        Boolean actualIsSectionExpanded = sectionedRecyclerViewAdapter.isSectionExpanded(sectionIndex);

        assertThat(actualIsSectionExpanded).isSameAs(isSectionExpanded);

        verify(sectionManager).getSectionsCount();
        verify(sectionManager).isSectionExpanded(sectionIndex);
        verifyNoMoreInteractions(sectionManager);

    }

    @Test
    public void getSectionIndex() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 1;
            }
        };

        Integer adapterPosition = 0;
        Integer sectionIndex = 0;

        when(sectionManager.getItemCount()).thenReturn(1);
        when(sectionManager.sectionIndex(adapterPosition)).thenReturn(sectionIndex);

        Integer actualSectionIndex = sectionedRecyclerViewAdapter.getSectionIndex(adapterPosition);

        assertThat(actualSectionIndex).isSameAs(sectionIndex);

        verify(sectionManager).getItemCount();
        verify(sectionManager).sectionIndex(adapterPosition);
        verifyNoMoreInteractions(sectionManager);

    }

    @Test
    public void getPositionInSection() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 1;
            }
        };

        Integer adapterPosition = 0;
        Integer positionInSection = 0;

        when(sectionManager.getItemCount()).thenReturn(1);
        when(sectionManager.isSectionSubheaderOnPosition(adapterPosition)).thenReturn(false);
        when(sectionManager.positionInSection(adapterPosition)).thenReturn(positionInSection);

        Integer actualPositionInSection = sectionedRecyclerViewAdapter.getItemPositionInSection(adapterPosition);

        assertThat(actualPositionInSection).isSameAs(positionInSection);

        verify(sectionManager).getItemCount();
        verify(sectionManager).isSectionSubheaderOnPosition(adapterPosition);
        verify(sectionManager).positionInSection(adapterPosition);
        verifyNoMoreInteractions(sectionManager);

    }

    @Test
    public void isFirstItemInSection_shouldReturnTrue() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 1;
            }
        };

        Integer adapterPosition = 0;
        Integer positionInSection = 0;

        when(sectionManager.getItemCount()).thenReturn(1);
        when(sectionManager.isSectionSubheaderOnPosition(adapterPosition)).thenReturn(false);
        when(sectionManager.positionInSection(adapterPosition)).thenReturn(positionInSection);

        Boolean actualIsFirstItemInSection = sectionedRecyclerViewAdapter.isFirstItemInSection(adapterPosition);

        assertTrue(actualIsFirstItemInSection);

        verify(sectionManager).getItemCount();
        verify(sectionManager).isSectionSubheaderOnPosition(adapterPosition);
        verify(sectionManager).positionInSection(adapterPosition);
        verifyNoMoreInteractions(sectionManager);

    }

    @Test
    public void isFirstItemInSection_shouldReturnFalse() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 1;
            }
        };

        Integer adapterPosition = 0;
        Integer positionInSection = 2;

        when(sectionManager.getItemCount()).thenReturn(1);
        when(sectionManager.isSectionSubheaderOnPosition(adapterPosition)).thenReturn(false);
        when(sectionManager.positionInSection(adapterPosition)).thenReturn(positionInSection);

        Boolean actualIsFirstItemInSection = sectionedRecyclerViewAdapter.isFirstItemInSection(adapterPosition);

        assertFalse(actualIsFirstItemInSection);

        verify(sectionManager).getItemCount();
        verify(sectionManager).isSectionSubheaderOnPosition(adapterPosition);
        verify(sectionManager).positionInSection(adapterPosition);
        verifyNoMoreInteractions(sectionManager);

    }

    @Test
    public void isLastItemInSection_shouldReturnTrue() {

        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>(sectionManager) {
            @Override
            public boolean onPlaceSubheaderBetweenItems(int position) {
                return false;
            }

            @Override
            public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {

            }

            @Override
            public void onBindSubheaderViewHolder(RecyclerView.ViewHolder subheaderHolder, int nextItemPosition) {

            }

            @Override
            public int getItemSize() {
                return 1;
            }
        };

        Integer adapterPosition = 0;
        Integer positionInSection = 2;
        Integer sectionIndex = 0;
        Integer sectionSize = 3;

        when(sectionManager.getItemCount()).thenReturn(4);
        when(sectionManager.getSectionsCount()).thenReturn(1);
        when(sectionManager.sectionIndex(adapterPosition)).thenReturn(sectionIndex);
        when(sectionManager.sectionSize(sectionIndex)).thenReturn(sectionSize);
        when(sectionManager.isSectionSubheaderOnPosition(adapterPosition)).thenReturn(false);
        when(sectionManager.positionInSection(adapterPosition)).thenReturn(positionInSection);

        Boolean actualIsFirstItemInSection = sectionedRecyclerViewAdapter.isLastItemInSection(adapterPosition);

        assertTrue(actualIsFirstItemInSection);

        verify(sectionManager, times(2)).getItemCount();
        verify(sectionManager).getSectionsCount();
        verify(sectionManager).sectionIndex(adapterPosition);
        verify(sectionManager).sectionSize(sectionIndex);
        verify(sectionManager).isSectionSubheaderOnPosition(adapterPosition);
        verify(sectionManager).positionInSection(adapterPosition);
        verifyNoMoreInteractions(sectionManager);

    }

    @Test
    public void getSectionSize() {}

    @Test
    public void getSectionSubheaderPosition() {}

    @Test
    public void getSectionCount() {
        init();
        assertThat(sectionedRecyclerViewAdapter.getSectionsCount()).isEqualTo(4);
    }

}