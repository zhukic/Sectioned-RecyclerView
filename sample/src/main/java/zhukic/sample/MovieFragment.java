package zhukic.sample;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import zhukic.sample.adapters.BaseMovieAdapter;
import zhukic.sample.adapters.MovieAdapterByDecade;
import zhukic.sample.adapters.MovieAdapterByGenre;
import zhukic.sample.adapters.MovieAdapterByName;
import zhukic.sectionedrecyclerview.R;

/**
 * Created by RUS on 04.09.2016.
 */
public class MovieFragment extends Fragment implements BaseMovieAdapter.OnItemClickListener, NewMovieDialog.DialogListener {

    private List<Movie> mMovieList;

    private Comparator<Movie> movieComparator;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private BaseMovieAdapter mSectionedRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(v -> onFabClick());

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

        mSectionedRecyclerAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(mSectionedRecyclerAdapter);
    }

    private void setAdapterByName() {
        this.movieComparator = (o1, o2) -> o1.getName().compareTo(o2.getName());
        Collections.sort(mMovieList, movieComparator);
        mSectionedRecyclerAdapter = new MovieAdapterByName(mMovieList);
    }

    private void setAdapterByGenre() {
        this.movieComparator = (o1, o2) -> o1.getGenre().compareTo(o2.getGenre());
        Collections.sort(mMovieList, movieComparator);
        mSectionedRecyclerAdapter = new MovieAdapterByGenre(mMovieList);
    }

    private void setAdapterByDecade() {
        this.movieComparator = (o1, o2) -> o1.getYear() - o2.getYear();
        Collections.sort(mMovieList, movieComparator);
        mSectionedRecyclerAdapter = new MovieAdapterByDecade(mMovieList);
    }

    private void setAdapterWithGridLayout() {
        setAdapterByGenre();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSectionedRecyclerAdapter.setGridLayoutManager(gridLayoutManager);
    }

    @Override
    public void onItemClicked(Movie movie) {
        final int index = mMovieList.indexOf(movie);
        movie.setName("AAAAA");
        mSectionedRecyclerAdapter.changeItem(index);
    }

    private void onFabClick() {
        NewMovieDialog newMovieDialog = new NewMovieDialog();
        newMovieDialog.setTargetFragment(this, 1);
        newMovieDialog.show(getFragmentManager(), "newMovie");
    }

    @Override
    public void onMovieCreated(Movie movie) {
        for (int i = 0; i < mMovieList.size(); i++) {
            if (movieComparator.compare(mMovieList.get(i), movie) >= 0) {
                mMovieList.add(i, movie);
                mSectionedRecyclerAdapter.addItem(i);
                return;
            }
        }
        mMovieList.add(mMovieList.size(), movie);
        mSectionedRecyclerAdapter.addItem(mMovieList.size() - 1);
    }
}
