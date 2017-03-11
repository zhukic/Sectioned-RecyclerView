package zhukic.sample.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import zhukic.sample.Movie;
import zhukic.sectionedrecyclerview.R;

/**
 * Created by RUS on 02.11.2016.
 */

public abstract class BaseMovieAdapter extends SectionedRecyclerViewAdapter<BaseMovieAdapter.SubheaderHolder, BaseMovieAdapter.MovieViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(Movie movie);
    }

    List<Movie> movieList;

    OnItemClickListener onItemClickListener;

    static class SubheaderHolder extends RecyclerView.ViewHolder {

        private static Typeface meduiumTypeface = null;

        TextView mSubheaderText;

        SubheaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = (TextView) itemView.findViewById(R.id.subheaderText);

            if(meduiumTypeface == null) {
                meduiumTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Medium.ttf");
            }
            this.mSubheaderText.setTypeface(meduiumTypeface);
        }

    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView textMovieName;
        TextView textMovieGenre;
        TextView textMovieYear;

        MovieViewHolder(View itemView) {
            super(itemView);
            this.textMovieName = (TextView) itemView.findViewById(R.id.movieName);
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
    public int getItemSize() {
        return movieList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
