package zhukic.sample.adapters;

import android.view.View;

import java.util.List;

import zhukic.sample.Movie;

/**
 * Created by RUS on 04.09.2016.
 */

public class MovieAdapterByName extends BaseMovieAdapter {

    public MovieAdapterByName(List<Movie> itemList) {
        super(itemList);
    }

    @Override
    public boolean onItems(int position1, int position2) {
        final Movie movie1 = movieList.get(position1);
        final Movie movie2 = movieList.get(position2);

        return !movie1.getName().substring(0, 1).equals(movie2.getName().substring(0, 1));
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
        subheaderHolder.mSubheaderText.setText(nextMovie.getName().substring(0, 1));
    }
}
