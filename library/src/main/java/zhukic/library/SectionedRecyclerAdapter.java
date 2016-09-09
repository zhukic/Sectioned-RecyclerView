package zhukic.library;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by RUS on 31.08.2016.
 */

public abstract class SectionedRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_HEADER = 1;

    private List<T> mItemList;
    private Set<Integer> mSubheadersPositions = new HashSet<>();
    private int mHeadersCount;

    public static final class SubheaderHolder extends RecyclerView.ViewHolder {

        private static Typeface meduiumTypeface = null;

        public TextView mSubheaderText;

        public SubheaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = (TextView) itemView.findViewById(R.id.subheaderText);

            if(meduiumTypeface == null) {
                meduiumTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Medium.ttf");
            }
            this.mSubheaderText.setTypeface(meduiumTypeface);
        }

    }

    public SectionedRecyclerAdapter(List<T> itemList) {
        this.mItemList = itemList;
        initSubheadersPositions();
    }

    private void initSubheadersPositions() {
        mSubheadersPositions.clear();

        if(!mItemList.isEmpty()) {
            mHeadersCount = 1;
            mSubheadersPositions.add(0);
        } else {
            mHeadersCount = 0;
        }

        for(int i = 1; i < mItemList.size(); i++) {
            T t1 = mItemList.get(i - 1);
            T t2 = mItemList.get(i);
            if(onItems(t1, t2)) {
                mHeadersCount++;
                mSubheadersPositions.add(i + mSubheadersPositions.size());
            }
        }
    }

    public abstract boolean onItems(T o1, T o2);

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(VH holder, int position);

    public abstract void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, T t);

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public final int getItemViewType(int position) {
        if(isHeaderOnPosition(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            return onCreateItemViewHolder(parent, viewType);
        } else if(viewType == TYPE_HEADER) {
            return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false));
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isHeaderOnPosition(position)) {
            onBindSubheaderViewHolder((SubheaderHolder)holder, mItemList.get(getContactPositionForViewHolder(position)));
        } else {
            onBindItemViewHolder((VH)holder, getContactPositionForViewHolder(position));
        }
        Log.d("TAG", mItemList.get(getContactPositionForViewHolder(position)).toString());
    }

    @Override
    public final int getItemCount() {
        return mItemList.size() + mHeadersCount;
    }

    private boolean isHeaderOnPosition(int position) {
        return mSubheadersPositions.contains(position);
    }

    private int getCountOfSubheadersBeforePosition(int position) {
        int count = 0;
        for(int subheaderPosition : mSubheadersPositions) {
            if(subheaderPosition < position) {
                count++;
            }
        }
        return count;
    }

    private int getContactPositionForViewHolder(int viewHolderPosition) {
        return viewHolderPosition - getCountOfSubheadersBeforePosition(viewHolderPosition);
    }
}
