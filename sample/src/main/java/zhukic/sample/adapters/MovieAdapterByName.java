package zhukic.sample.adapters;

import android.content.Context;

import java.util.List;

import zhukic.sample.Movie;
import zhukic.sectionedrecyclerview.R;

public class MovieAdapterByName extends BaseMovieAdapter {

    public MovieAdapterByName(List<Movie> itemList) {
        super(itemList);
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final Movie movie = movieList.get(position);
        final Movie nextMovie = movieList.get(position + 1);

        return !movie.getName().substring(0, 1).equals(nextMovie.getName().substring(0, 1));
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
        final Context context = subheaderHolder.itemView.getContext();
        final Movie nextMovie = movieList.get(nextItemPosition);
        final int sectionSize = getSectionSize(getSectionIndex(subheaderHolder.getAdapterPosition()));
        final String genre = nextMovie.getName().substring(0, 1);
        final String subheaderText = String.format(
                context.getString(R.string.subheader),
                genre,
                context.getResources().getQuantityString(R.plurals.item, sectionSize, sectionSize)
        );
        subheaderHolder.mSubheaderText.setText(subheaderText);
    }
}
