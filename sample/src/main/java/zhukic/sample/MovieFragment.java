package zhukic.sample;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import zhukic.sample.adapters.BaseMovieAdapter;
import zhukic.sample.adapters.MovieAdapterByDecade;
import zhukic.sample.adapters.MovieAdapterByGenre;
import zhukic.sample.adapters.MovieAdapterByTitle;
import zhukic.sectionedrecyclerview.R;

public class MovieFragment extends Fragment implements BaseMovieAdapter.OnItemClickListener, NewMovieDialogFragment.DialogListener {

    private List<Movie> mMovieList;

    private Comparator<Movie> movieComparator;

    private RecyclerView recyclerView;

    private BaseMovieAdapter mSectionedRecyclerAdapter;

    private GridDividerDecoration gridDividerDecoration;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gridDividerDecoration = new GridDividerDecoration(getContext());
        recyclerView.addItemDecoration(gridDividerDecoration);

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
        this.movieComparator = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
        Collections.sort(mMovieList, movieComparator);
        mSectionedRecyclerAdapter = new MovieAdapterByTitle(mMovieList);
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
        mMovieList.remove(movie);
        mSectionedRecyclerAdapter.notifyItemRemovedAtPosition(index);
    }

    @Override
    public void onSubheaderClicked(int position) {
        if (mSectionedRecyclerAdapter.isSectionExpanded(mSectionedRecyclerAdapter.getSectionIndex(position))) {
            mSectionedRecyclerAdapter.collapseSection(mSectionedRecyclerAdapter.getSectionIndex(position));
        } else {
            mSectionedRecyclerAdapter.expandSection(mSectionedRecyclerAdapter.getSectionIndex(position));
        }
    }

    public void onFabClick() {
        NewMovieDialogFragment newMovieDialogFragment = new NewMovieDialogFragment();
        newMovieDialogFragment.setTargetFragment(this, 1);
        newMovieDialogFragment.show(getFragmentManager(), "newMovie");
    }

    @Override
    public void onMovieCreated(Movie movie) {
        for (int i = 0; i < mMovieList.size(); i++) {
            if (movieComparator.compare(mMovieList.get(i), movie) >= 0) {
                mMovieList.add(i, movie);
                mSectionedRecyclerAdapter.notifyItemInsertedAtPosition(i);
                return;
            }
        }
        mMovieList.add(mMovieList.size(), movie);
        mSectionedRecyclerAdapter.notifyItemInsertedAtPosition(mMovieList.size() - 1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_expand_all_sections:
                mSectionedRecyclerAdapter.expandAllSections();
                break;
            case R.id.action_collapse_all_sections:
                mSectionedRecyclerAdapter.collapseAllSections();
                break;
        }

        return true;
    }
}
