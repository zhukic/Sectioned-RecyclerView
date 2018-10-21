package zhukic.sample.adapters;

import android.graphics.Typeface;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import zhukic.sample.Movie;
import zhukic.sectionedrecyclerview.R;

public abstract class BaseMovieAdapter extends SectionedRecyclerViewAdapter<BaseMovieAdapter.SubheaderHolder, BaseMovieAdapter.MovieViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(Movie movie);
        void onSubheaderClicked(int position);
    }

    List<Movie> movieList;

    OnItemClickListener onItemClickListener;

    static class SubheaderHolder extends RecyclerView.ViewHolder {

        private static Typeface meduiumTypeface = null;

        TextView mSubheaderText;
        ImageView mArrow;

        SubheaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = (TextView) itemView.findViewById(R.id.subheaderText);
            this.mArrow = (ImageView) itemView.findViewById(R.id.arrow);

            if(meduiumTypeface == null) {
                meduiumTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Medium.ttf");
            }
            this.mSubheaderText.setTypeface(meduiumTypeface);
        }

    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView textMovieTitle;
        TextView textMovieGenre;
        TextView textMovieYear;

        MovieViewHolder(View itemView) {
            super(itemView);
            this.textMovieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            this.textMovieGenre = (TextView) itemView.findViewById(R.id.movieGenre);
            this.textMovieYear = (TextView) itemView.findViewById(R.id.movieYear);
        }
    }

    BaseMovieAdapter(List<Movie> itemList) {
        super();
        this.movieList = itemList;
    }

    @Override
    public MovieViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public SubheaderHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    @Override
    @CallSuper
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {

        boolean isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.getAdapterPosition()));

        if (isSectionExpanded) {
            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_up_black_24dp));
        } else {
            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_down_black_24dp));
        }

        subheaderHolder.itemView.setOnClickListener(v -> onItemClickListener.onSubheaderClicked(subheaderHolder.getAdapterPosition()));

    }

    @Override
    public int getItemSize() {
        return movieList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
