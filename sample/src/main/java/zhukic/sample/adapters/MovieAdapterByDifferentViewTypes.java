package zhukic.sample.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zhukic.sample.Movie;
import zhukic.sectionedrecyclerview.R;

/**
 * Created by Vladislav Zhukov on 13.11.2016.
 */

public class MovieAdapterByDifferentViewTypes extends BaseMovieAdapter {

    public MovieAdapterByDifferentViewTypes(List<Movie> itemList) {
        super(itemList);
    }

    @Override
    public int getViewType(int position) {
        if(position % 2 == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public boolean onItems(int position1, int position2) {
        final Movie movie1 = movieList.get(position1);
        final Movie movie2 = movieList.get(position2);

        return !movie1.getGenre().equals(movie2.getGenre());
    }

    @Override
    public MovieViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 1) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
        } else if (viewType == 2) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_another_movie, parent, false));
        }
        return null;
    }

    @Override
    public void onBindItemViewHolder(final MovieViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);

        holder.textMovieName.setText(movie.getName());
        holder.textMovieGenre.setText(movie.getGenre());
        holder.textMovieYear.setText(String.valueOf(movie.getYear()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition(), position);
            }
        });
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        final Movie nextMovie = movieList.get(nextItemPosition);
        subheaderHolder.mSubheaderText.setText(nextMovie.getGenre());
    }
}
