package zhukic.sample.adapters;

import android.view.View;

import java.util.List;

import zhukic.sample.Movie;

/**
 * Created by Vladislav Zhukov on 04.09.2016.
 */
public class MovieAdapterByDecade extends BaseMovieAdapter {

    public MovieAdapterByDecade(List<Movie> itemList) {
        super(itemList);
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int itemPosition, int nextItemPosition) {
        final Movie movie1 = movieList.get(itemPosition);
        final Movie movie2 = movieList.get(nextItemPosition);

        return movie1.getYear() / 10 != movie2.getYear() / 10;
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
    public void onBindSubheaderViewHolder(SubheaderViewHolder subheaderViewHolder, int nextItemPosition) {
        final Movie nextMovie = movieList.get(nextItemPosition);
        String decade = String.valueOf(nextMovie.getYear() - nextMovie.getYear() % 10) + "s";
        subheaderViewHolder.subheaderText.setText(decade);
    }
}
