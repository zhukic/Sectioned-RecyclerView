package zhukic.sample;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import zhukic.sample.adapters.BaseMovieAdapter;
import zhukic.sample.adapters.MovieAdapterByDecade;
import zhukic.sample.adapters.MovieAdapterByGenre;
import zhukic.sample.adapters.MovieAdapterByName;
import zhukic.sectionedrecyclerview.R;

/**
 * Created by RUS on 04.09.2016.
 */
public class MovieFragment extends Fragment implements BaseMovieAdapter.OnItemClickListener {

    private ArrayList<Movie> movieList;

    private RecyclerView recyclerView;
    private BaseMovieAdapter sectionedRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Resources resources = getResources();
        String[] names = resources.getStringArray(R.array.names);
        String[] genres = resources.getStringArray(R.array.genres);
        int[] years = resources.getIntArray(R.array.years);

        movieList = new ArrayList<>(20);

        for(int i = 0; i < 20; i++) {
            Movie movie = new Movie(names[i], years[i], genres[i]);
            movieList.add(movie);
        }

        int position = getArguments().getInt("POSITION");

        switch (position) {
            case 0:
                setAdapterByName();
                break;
            case 1:
                setAdapterByGenre();
                break;
            case 2:
                setAdapterByDecade();
                break;
            case 3:
                setAdapterWithGridLayout();
                break;
        }

        sectionedRecyclerAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(sectionedRecyclerAdapter);

        return recyclerView;
    }

    private void setAdapterByName() {
        Collections.sort(movieList, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        sectionedRecyclerAdapter = new MovieAdapterByName(movieList);
    }

    private void setAdapterByGenre() {
        Collections.sort(movieList, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getGenre().compareTo(o2.getGenre());
            }
        });
        sectionedRecyclerAdapter = new MovieAdapterByGenre(movieList);
    }

    private void setAdapterByDecade() {
        Collections.sort(movieList, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getYear() - o2.getYear();
            }
        });
        sectionedRecyclerAdapter = new MovieAdapterByDecade(movieList);
    }

    private void setAdapterWithGridLayout() {
        setAdapterByGenre();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        sectionedRecyclerAdapter.setGridLayoutManager(gridLayoutManager);
    }

    @Override
    public void onItemClicked(int adapterPosition, int positionInCollection) {
        final String text = "Item clicked: adapter position = " + adapterPosition +
                ", position in collection = " + positionInCollection;

        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
