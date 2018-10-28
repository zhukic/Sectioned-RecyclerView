package com.zhukic.sectionedrecyclerview;

import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import androidx.recyclerview.widget.RecyclerView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SectionedRecyclerViewAdapterTest {

    private SectionManager sectionManager;

    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;

    @Before
    public void beforeEachTest() {
        sectionedRecyclerViewAdapter = spy(new TestSectionedRecyclerViewAdapter());
        sectionManager = mock(SectionManager.class);
        sectionedRecyclerViewAdapter.setSectionManager(sectionManager);
    }

    @Test
    public void onAttachedToRecyclerView_shouldInitialize() {
        final RecyclerView recyclerView = mock(RecyclerView.class);

        sectionedRecyclerViewAdapter.onAttachedToRecyclerView(recyclerView);

        verify(sectionManager).init();
    }

    @Test
    public void onDetachedFromRecyclerView_shouldClear() {
        final RecyclerView recyclerView = mock(RecyclerView.class);

        sectionedRecyclerViewAdapter.onDetachedFromRecyclerView(recyclerView);

        verify(sectionManager).clear();
    }

    @Test
    public void onCreateViewHolder_shouldCallOnCreateSubheaderViewHolder() {
        final ViewGroup viewGroup = mock(ViewGroup.class);

        sectionedRecyclerViewAdapter.onCreateViewHolder(viewGroup, SectionedRecyclerViewAdapter.DEFAULT_TYPE_HEADER);

        verify(sectionedRecyclerViewAdapter).onCreateSubheaderViewHolder(viewGroup, SectionedRecyclerViewAdapter.DEFAULT_TYPE_HEADER);
    }

    @Test
    public void onCreateViewHolder_shouldCallOnCreateItemViewHolder() {
        final ViewGroup viewGroup = mock(ViewGroup.class);
        final int viewType = sectionedRecyclerViewAdapter.getViewType(0);

        sectionedRecyclerViewAdapter.onCreateViewHolder(viewGroup, viewType);

        verify(sectionedRecyclerViewAdapter).onCreateItemViewHolder(viewGroup, viewType);
    }

    @Test
    public void onBindViewHolder_shouldCallOnBindSubheaderViewHolder() {
        final RecyclerView.ViewHolder viewHolder = mock(RecyclerView.ViewHolder.class);

        when(sectionManager.isSectionSubheaderAtPosition(5)).thenReturn(true);
        when(sectionManager.getItemPositionForSubheaderViewHolder(5)).thenReturn(6);

        sectionedRecyclerViewAdapter.onBindViewHolder(viewHolder, 5);

        verify(sectionedRecyclerViewAdapter).onBindSubheaderViewHolder(viewHolder, 6);
    }

    @Test
    public void onBindViewHolder_shouldCallOnBindItemViewHolder() {
        final RecyclerView.ViewHolder viewHolder = mock(RecyclerView.ViewHolder.class);

        when(sectionManager.isSectionSubheaderAtPosition(5)).thenReturn(false);
        when(sectionManager.getItemPositionForItemViewHolder(5)).thenReturn(6);

        sectionedRecyclerViewAdapter.onBindViewHolder(viewHolder, 5);

        verify(sectionedRecyclerViewAdapter).onBindItemViewHolder(viewHolder, 6);
    }

    @Test
    public void getItemCount() {
        when(sectionManager.getItemCount()).thenReturn(10);

        assertThat(sectionedRecyclerViewAdapter.getItemCount()).isEqualTo(10);
    }

    @Test
    public void notifyDataChanged() {
        final RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);

        sectionedRecyclerViewAdapter.notifyDataChanged();

        verify(sectionManager).init();
        verifyNoMoreInteractions(sectionManager);

        verify(adapterDataObserver).onChanged();
        verifyNoMoreInteractions(adapterDataObserver);
    }

    @Test
    public void notifyItemChangedAtPosition() {
        final int itemPosition = 5;
        final RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        final NotifyResult notifyResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0, 1),
                        Notifier.createInserted(1, 2),
                        Notifier.createRemoved(3, 3)
                )
        );
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);
        when(sectionManager.onItemChanged(itemPosition)).thenReturn(notifyResult);

        sectionedRecyclerViewAdapter.notifyItemChangedAtPosition(itemPosition);

        verify(adapterDataObserver).onItemRangeChanged(0, 1, null);
        verify(adapterDataObserver).onItemRangeInserted(1, 2);
        verify(adapterDataObserver).onItemRangeRemoved(3, 3);
        verifyNoMoreInteractions(adapterDataObserver);
    }

    @Test
    public void notifyItemInsertedAtPosition() {
        final int itemPosition = 5;
        final RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        final NotifyResult notifyResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0, 1),
                        Notifier.createInserted(1, 2),
                        Notifier.createRemoved(3, 3)
                )
        );
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);
        when(sectionManager.onItemInserted(itemPosition)).thenReturn(notifyResult);

        sectionedRecyclerViewAdapter.notifyItemInsertedAtPosition(itemPosition);

        verify(adapterDataObserver).onItemRangeChanged(0, 1, null);
        verify(adapterDataObserver).onItemRangeInserted(1, 2);
        verify(adapterDataObserver).onItemRangeRemoved(3, 3);
        verifyNoMoreInteractions(adapterDataObserver);
    }

    @Test
    public void notifyItemRemovedAtPosition() {
        final int itemPosition = 5;
        final RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        final NotifyResult notifyResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0, 1),
                        Notifier.createInserted(1, 2),
                        Notifier.createRemoved(3, 3)
                )
        );
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);
        when(sectionManager.onItemRemoved(itemPosition)).thenReturn(notifyResult);

        sectionedRecyclerViewAdapter.notifyItemRemovedAtPosition(itemPosition);

        verify(adapterDataObserver).onItemRangeChanged(0, 1, null);
        verify(adapterDataObserver).onItemRangeInserted(1, 2);
        verify(adapterDataObserver).onItemRangeRemoved(3, 3);
        verifyNoMoreInteractions(adapterDataObserver);
    }

    @Test
    public void isSubheaderAtPosition_shouldWorkCorrectly() {
        final int adapterPosition = 5;
        final boolean isSubheaderAtPosition = true;
        when(sectionManager.getItemCount()).thenReturn(10);
        when(sectionManager.isSectionSubheaderAtPosition(adapterPosition)).thenReturn(isSubheaderAtPosition);

        final boolean actualResult = sectionedRecyclerViewAdapter.isSubheaderAtPosition(adapterPosition);

        assertThat(actualResult).isEqualTo(isSubheaderAtPosition);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isSubheaderAtPosition_shouldThrowException_whenAdapterPositionIsLessThanZero() {
        final int adapterPosition = -2;
        sectionedRecyclerViewAdapter.isSubheaderAtPosition(adapterPosition);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isSubheaderAtPosition_shouldThrowException_whenAdapterPositionIsMoreThanItemCount() {
        final int adapterPosition = 5;
        when(sectionManager.getItemCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.isSubheaderAtPosition(adapterPosition);
    }

    @Test
    public void expandSection_shouldWorkCorrectly() {
        final int sectionIndex = 5;
        final RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        final NotifyResult notifyResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0, 1),
                        Notifier.createInserted(1, 2),
                        Notifier.createRemoved(3, 3)
                )
        );
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);
        when(sectionManager.getSectionsCount()).thenReturn(10);
        when(sectionManager.expandSection(sectionIndex)).thenReturn(notifyResult);

        sectionedRecyclerViewAdapter.expandSection(sectionIndex);

        verify(adapterDataObserver).onItemRangeChanged(0, 1, null);
        verify(adapterDataObserver).onItemRangeInserted(1, 2);
        verify(adapterDataObserver).onItemRangeRemoved(3, 3);
        verifyNoMoreInteractions(adapterDataObserver);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void expandSection_shouldThrowException_whenSectionIndexIsLessThanZero() {
        final int sectionIndex = -2;
        sectionedRecyclerViewAdapter.expandSection(sectionIndex);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void expandSection_shouldThrowException_whenSectionIndexIsMoreThanSectionCount() {
        final int sectionIndex = 5;
        when(sectionManager.getSectionsCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.expandSection(sectionIndex);
    }

    @Test
    public void expandAllSections() {
        final RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        final NotifyResult notifyResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0, 1),
                        Notifier.createInserted(1, 2),
                        Notifier.createRemoved(3, 3)
                )
        );
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);
        when(sectionManager.expandAllSections()).thenReturn(notifyResult);

        sectionedRecyclerViewAdapter.expandAllSections();

        verify(adapterDataObserver).onItemRangeChanged(0, 1, null);
        verify(adapterDataObserver).onItemRangeInserted(1, 2);
        verify(adapterDataObserver).onItemRangeRemoved(3, 3);
        verifyNoMoreInteractions(adapterDataObserver);
    }

    @Test
    public void collapseSection_shouldWorkCorrectly() {
        final int sectionIndex = 5;
        final RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        final NotifyResult notifyResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0, 1),
                        Notifier.createInserted(1, 2),
                        Notifier.createRemoved(3, 3)
                )
        );
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);
        when(sectionManager.getSectionsCount()).thenReturn(10);
        when(sectionManager.collapseSection(sectionIndex)).thenReturn(notifyResult);

        sectionedRecyclerViewAdapter.collapseSection(sectionIndex);

        verify(adapterDataObserver).onItemRangeChanged(0, 1, null);
        verify(adapterDataObserver).onItemRangeInserted(1, 2);
        verify(adapterDataObserver).onItemRangeRemoved(3, 3);
        verifyNoMoreInteractions(adapterDataObserver);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void collapseSection_shouldThrowException_whenSectionIndexIsLessThanZero() {
        final int sectionIndex = -2;
        sectionedRecyclerViewAdapter.collapseSection(sectionIndex);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void collapseSection_shouldThrowException_whenSectionIndexIsMoreThanSectionCount() {
        final int sectionIndex = 5;
        when(sectionManager.getSectionsCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.collapseSection(sectionIndex);
    }

    @Test
    public void collapseAllSections() {
        final RecyclerView.AdapterDataObserver adapterDataObserver = mock(RecyclerView.AdapterDataObserver.class);
        final NotifyResult notifyResult = NotifyResult.create(
                Arrays.asList(
                        Notifier.createChanged(0, 1),
                        Notifier.createInserted(1, 2),
                        Notifier.createRemoved(3, 3)
                )
        );
        sectionedRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);
        when(sectionManager.collapseAllSections()).thenReturn(notifyResult);

        sectionedRecyclerViewAdapter.collapseAllSections();

        verify(adapterDataObserver).onItemRangeChanged(0, 1, null);
        verify(adapterDataObserver).onItemRangeInserted(1, 2);
        verify(adapterDataObserver).onItemRangeRemoved(3, 3);
        verifyNoMoreInteractions(adapterDataObserver);
    }

    @Test
    public void isSectionExpanded() {
        final int sectionIndex = 2;
        final boolean isSectionExpanded = ThreadLocalRandom.current().nextBoolean();
        when(sectionManager.getSectionsCount()).thenReturn(10);
        when(sectionManager.isSectionExpanded(sectionIndex)).thenReturn(isSectionExpanded);

        final boolean actualResult = sectionedRecyclerViewAdapter.isSectionExpanded(sectionIndex);

        assertThat(actualResult).isEqualTo(isSectionExpanded);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isSectionExpanded_shouldThrowException_whenSectionIndexIsLessThanZero() {
        final int sectionIndex = -2;
        sectionedRecyclerViewAdapter.isSectionExpanded(sectionIndex);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isSectionExpanded_shouldThrowException_whenSectionIndexIsMoreThanSectionCount() {
        final int sectionIndex = 5;
        when(sectionManager.getSectionsCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.isSectionExpanded(sectionIndex);
    }

    @Test
    public void getSectionIndex() {
        final int adapterPosition = 5;
        final int sectionIndex = 2;
        when(sectionManager.getItemCount()).thenReturn(10);
        when(sectionManager.sectionIndex(adapterPosition)).thenReturn(sectionIndex);

        final int actualResult = sectionedRecyclerViewAdapter.getSectionIndex(adapterPosition);

        assertThat(actualResult).isEqualTo(sectionIndex);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSectionIndex_shouldThrowException_whenAdapterPositionIsLessThanZero() {
        final int adapterPosition = -2;
        sectionedRecyclerViewAdapter.getSectionIndex(adapterPosition);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSectionIndex_shouldThrowException_whenAdapterPositionIsMoreThanItemCount() {
        final int adapterPosition = 5;
        when(sectionManager.getItemCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.getSectionIndex(adapterPosition);
    }

    @Test
    public void getItemPositionInSection_shouldWorkCorrectly_whenItemIsPlacedAtSpecifiedAdapterPosition() {
        final int adapterPosition = 5;
        final int itemPositionInSection = 2;
        when(sectionManager.getItemCount()).thenReturn(10);
        when(sectionManager.isSectionSubheaderAtPosition(adapterPosition)).thenReturn(false);
        when(sectionManager.positionInSection(adapterPosition)).thenReturn(itemPositionInSection);

        final int actualResult = sectionedRecyclerViewAdapter.getItemPositionInSection(adapterPosition);

        assertThat(actualResult).isEqualTo(itemPositionInSection);
    }

    @Test
    public void getItemPositionInSection_shouldWorkCorrectly_whenSectionSubheaderIsPlacesAtSpecifiedAdapterPosition() {
        final int adapterPosition = 5;
        when(sectionManager.getItemCount()).thenReturn(10);
        when(sectionManager.isSectionSubheaderAtPosition(adapterPosition)).thenReturn(true);

        final int actualResult = sectionedRecyclerViewAdapter.getItemPositionInSection(adapterPosition);

        assertThat(actualResult).isEqualTo(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getItemPositionInSection_shouldThrowException_whenAdapterPositionIsLessThanZero() {
        final int adapterPosition = -2;
        sectionedRecyclerViewAdapter.getItemPositionInSection(adapterPosition);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getItemPositionInSection_shouldThrowException_whenAdapterPositionIsMoreThanItemCount() {
        final int adapterPosition = 5;
        when(sectionManager.getItemCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.getItemPositionInSection(adapterPosition);
    }

    @Test
    public void isFirstItemInSection() {
        final int adapterPosition = 5;
        final boolean isFirstItemInSection = ThreadLocalRandom.current().nextBoolean();
        when(sectionManager.getItemCount()).thenReturn(10);
        when(sectionManager.isFirstItemInSection(adapterPosition)).thenReturn(isFirstItemInSection);

        final boolean actualResult = sectionedRecyclerViewAdapter.isFirstItemInSection(adapterPosition);

        assertThat(actualResult).isEqualTo(isFirstItemInSection);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isFirstItemInSection_shouldThrowException_whenAdapterPositionIsLessThanZero() {
        final int adapterPosition = -2;
        sectionedRecyclerViewAdapter.isFirstItemInSection(adapterPosition);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isFirstItemInSection_shouldThrowException_whenAdapterPositionIsMoreThanItemCount() {
        final int adapterPosition = 5;
        when(sectionManager.getItemCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.isFirstItemInSection(adapterPosition);
    }

    @Test
    public void isLastItemInSection() {
        final int adapterPosition = 5;
        final boolean isFirstItemInSection = ThreadLocalRandom.current().nextBoolean();
        when(sectionManager.getItemCount()).thenReturn(10);
        when(sectionManager.isLastItemInSection(adapterPosition)).thenReturn(isFirstItemInSection);

        final boolean actualResult = sectionedRecyclerViewAdapter.isLastItemInSection(adapterPosition);

        assertThat(actualResult).isEqualTo(isFirstItemInSection);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isLastItemInSection_shouldThrowException_whenAdapterPositionIsLessThanZero() {
        final int adapterPosition = -2;
        sectionedRecyclerViewAdapter.isLastItemInSection(adapterPosition);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void isLastItemInSection_shouldThrowException_whenAdapterPositionIsMoreThanItemCount() {
        final int adapterPosition = 5;
        when(sectionManager.getItemCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.isLastItemInSection(adapterPosition);
    }

    @Test
    public void getSectionSize() {
        final int sectionIndex = 2;
        final int sectionSize = 3;
        when(sectionManager.getSectionsCount()).thenReturn(10);
        when(sectionManager.sectionSize(sectionIndex)).thenReturn(sectionSize);

        final int actualResult = sectionedRecyclerViewAdapter.getSectionSize(sectionIndex);

        assertThat(actualResult).isEqualTo(sectionSize);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSectionSize_shouldThrowException_whenSectionIndexIsLessThanZero() {
        final int sectionIndex = -2;
        sectionedRecyclerViewAdapter.getSectionSize(sectionIndex);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSectionSize_shouldThrowException_whenSectionIndexIsMoreThanSectionCount() {
        final int sectionIndex = 5;
        when(sectionManager.getSectionsCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.getSectionSize(sectionIndex);
    }

    @Test
    public void getSectionSubheaderPosition() {
        final int sectionIndex = 2;
        final int sectionSubheaderPosition = 3;
        when(sectionManager.getSectionsCount()).thenReturn(10);
        when(sectionManager.getSectionSubheaderPosition(sectionIndex)).thenReturn(sectionSubheaderPosition);

        final int actualResult = sectionedRecyclerViewAdapter.getSectionSubheaderPosition(sectionIndex);

        assertThat(actualResult).isEqualTo(sectionSubheaderPosition);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSectionSubheaderPosition_shouldThrowException_whenSectionIndexIsLessThanZero() {
        final int sectionIndex = -2;
        sectionedRecyclerViewAdapter.getSectionSubheaderPosition(sectionIndex);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSectionSubheaderPosition_shouldThrowException_whenSectionIndexIsMoreThanSectionCount() {
        final int sectionIndex = 5;
        when(sectionManager.getSectionsCount()).thenReturn(5);
        sectionedRecyclerViewAdapter.getSectionSubheaderPosition(sectionIndex);
    }

    @Test
    public void getSectionCount() {
        final int sectionCount = 5;
        when(sectionManager.getSectionsCount()).thenReturn(sectionCount);

        final int actualResult = sectionedRecyclerViewAdapter.getSectionsCount();

        assertThat(actualResult).isEqualTo(sectionCount);
    }

    private static class TestSectionedRecyclerViewAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder> {

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
        public boolean onPlaceSubheaderBetweenItems(int position) {
            return false;
        }

        @Override
        public int getItemSize() {
            return 0;
        }
    }
}