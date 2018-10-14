package zhukic.sample.adapters;

import android.content.Context;

import java.util.List;

import zhukic.sample.Movie;
import zhukic.sectionedrecyclerview.R;

public class MovieAdapterByGenre extends BaseMovieAdapter {


    public MovieAdapterByGenre(List<Movie> itemList) { super(itemList); }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final String movieGenre = movieList.get(position).getGenre();
        final String nextMovieGenre = movieList.get(position + 1).getGenre();

        return !movieGenre.equals(nextMovieGenre);
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
        final String genre = nextMovie.getGenre();
        final String subheaderText = String.format(
                context.getString(R.string.subheader),
                genre,
                context.getResources().getQuantityString(R.plurals.item, sectionSize, sectionSize)
        );
        subheaderHolder.mSubheaderText.setText(subheaderText);
    }
}
