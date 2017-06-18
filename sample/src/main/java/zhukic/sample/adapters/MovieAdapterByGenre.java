package zhukic.sample.adapters;

import android.view.View;

import java.util.List;

import zhukic.sample.Movie;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class MovieAdapterByGenre extends BaseMovieAdapter {


    public MovieAdapterByGenre(List<Movie> itemList) { super(itemList); }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final Movie movie = movieList.get(position);
        final Movie nextMovie = movieList.get(position + 1);

        return !movie.getGenre().equals(nextMovie.getGenre());
    }

    @Override
    public void onBindItemViewHolder(final MovieViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);

        holder.textMovieName.setText(movie.getName());
        holder.textMovieGenre.setText(movie.getGenre());
        holder.textMovieYear.setText(String.valueOf(movie.getYear()));

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(movie));
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition);
        final Movie nextMovie = movieList.get(nextItemPosition);
        subheaderHolder.mSubheaderText.setText(nextMovie.getGenre());
    }
}
