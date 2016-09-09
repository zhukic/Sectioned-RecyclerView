package zhukic.sectionedrecyclerview;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import zhukic.library.SectionedRecyclerAdapter;

/**
 * Created by RUS on 04.09.2016.
 */
public class MovieFragment extends Fragment {

    private SectionedRecyclerAdapter mSectionedRecyclerAdapter;
    private ArrayList<Movie> mMovieList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Resources resources = getResources();
        String[] names = resources.getStringArray(R.array.names);
        String[] genres = resources.getStringArray(R.array.genres);
        int[] years = resources.getIntArray(R.array.years);

        mMovieList = new ArrayList<>(20);

        for(int i = 0; i < 20; i++) {
            Movie movie = new Movie(names[i], years[i], genres[i]);
            mMovieList.add(movie);
        }

        int position = getArguments().getInt("POSITION");

        Log.d("TAG", String.valueOf(position));

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
        }

        recyclerView.setAdapter(mSectionedRecyclerAdapter);

        return recyclerView;
    }

    private void setAdapterByName() {
        Collections.sort(mMovieList, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        mSectionedRecyclerAdapter = new MovieAdapterByName(mMovieList);
    }

    private void setAdapterByGenre() {
        Collections.sort(mMovieList, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getGenre().compareTo(o2.getGenre());
            }
        });
        mSectionedRecyclerAdapter = new MovieAdapterByGenre(mMovieList);
    }

    private void setAdapterByDecade() {
        Collections.sort(mMovieList, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getYear() - o2.getYear();
            }
        });
        mSectionedRecyclerAdapter = new MovieAdapterByDecade(mMovieList);
    }
}
