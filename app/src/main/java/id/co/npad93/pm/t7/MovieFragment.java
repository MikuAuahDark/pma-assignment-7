package id.co.npad93.pm.t7;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;

public class MovieFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public MovieFragment() {
        basicMovieIndex = new ArrayList<ListContainer>();
        basicMovieIndex.add(new ListContainer());
        basicMovieIndex.add(new ListContainer());
        basicMovieIndex.add(new ListContainer());
        basicMovieIndex.add(new ListContainer());

        search = new Runnable() {
            @Override
            public void run() {
                switchData(true, 3);
            }
        };
        searchWrapper = new Runnable() {
            @Override
            public void run() {
                Activity activity = getActivity();

                if (activity != null) {
                    activity.runOnUiThread(search);
                }
            }
        };
        searchDelayer = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                searchDelayer.removeCallbacks(searchWrapper);

                if (!query.isEmpty()) {
                    search.run();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                searchDelayer.removeCallbacks(searchWrapper);

                if (newText.isEmpty()) {
                    switchData(false, kind);
                } else {
                    recyclerView.setAdapter(null);
                    searchDelayer.postDelayed(searchWrapper, 500);
                }

                return true;
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (!fetching && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        fetching = true;
                        requestNextPage(searchQuery.isEmpty() ? kind : 3);
                    }
                }
            }
        });
        recyclerView.setAdapter(inQueue);
        inQueue = null;

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if (!fetching) {
            recyclerView.setAdapter(null);
            switchData(true, searchQuery.isEmpty() ? kind : 3);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void loadRecyclerViewDataset(ArrayList<BasicMovie> dataset) {
        // Workaround setAdapter not working
        inQueue = new MovieAdapter(dataset);
        recyclerView.setAdapter(inQueue);
    }

    public void loadRecyclerViewDataset(int position, int count) {
        RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();

        if (adapter != null) {
            adapter.notifyItemRangeInserted(position, count);
        }
    }

    public void setDataKind(int kind) {
        if (recyclerView == null) {
            return;
        }

        this.kind = kind;
        recyclerView.setAdapter(null);
        switchData(false, kind);
    }

    private void switchData(boolean reload, int kind) {
        if (kind < 0 || kind > 3) {
            throw new IllegalArgumentException("kind out of range");
        }

        ListContainer listContainer = basicMovieIndex.get(kind);

        if (reload || listContainer.basicMovieArrayList == null) {
            listContainer.page = 0;
            requestNextPage(kind);
        } else {
            loadRecyclerViewDataset(listContainer.basicMovieArrayList);
        }
    }

    private void requestNextPage(int kind) {
        TheMovieDBApi api = Api.getApi();
        ListContainer listContainer = basicMovieIndex.get(kind);
        Call<MovieList> call;

        if (listContainer.page > 0 && listContainer.page >= listContainer.totalPages) {
            // Stop requesting new pages.
            return;
        }

        switch (kind) {
            case 0: {
                call = api.getNowPlayingMovies(listContainer.page + 1);
                break;
            }
            case 1: {
                call = api.getUpcomingMovies(listContainer.page + 1);
                break;
            }
            case 2: {
                call = api.getPopularMovies(listContainer.page + 1);
                break;
            }
            case 3: {
                if (searchQuery.isEmpty()) {
                    throw new IllegalStateException("search query empty");
                }

                call = api.searchMovie(searchQuery, listContainer.page + 1);
                break;
            }
            default: {
                assert false;
                throw new IllegalArgumentException("kind out of range");
            }
        }

        if (currentCall != null) {
            currentCall.cancel();
        }

        currentCall = call;
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, retrofit2.Response<MovieList> response) {
                currentCall = null;

                if (response.isSuccessful()) {
                    ArrayList<BasicMovie> basicMovieData = listContainer.basicMovieArrayList;
                    MovieList result = response.body();
                    boolean newPage = listContainer.page == 0;
                    int count = 0;

                    if (newPage) {
                        basicMovieData = new ArrayList<BasicMovie>();
                        listContainer.page = 1;
                        listContainer.totalPages = result.getTotalPages();
                        listContainer.basicMovieArrayList = basicMovieData;

                        if (listContainer.totalPages == 0) {
                            Toast.makeText(getContext(), "No result!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        listContainer.page++;
                        count = basicMovieData.size();
                    }

                    basicMovieData.addAll(Arrays.asList(result.getResults()));

                    if (newPage) {
                        loadRecyclerViewDataset(basicMovieData);
                    } else {
                        loadRecyclerViewDataset(count, result.getResults().length);
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    fetching = false;
                } else {
                    Toast.makeText(getContext(), "HTTP Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                currentCall = null;
                fetching = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private boolean fetching;
    private int kind;
    private String searchQuery;
    private MovieAdapter inQueue;
    private ArrayList<ListContainer> basicMovieIndex;
    private Runnable search, searchWrapper;
    private Handler searchDelayer;
    private Call<MovieList> currentCall;

    static class ListContainer {
        ArrayList<BasicMovie> basicMovieArrayList;
        int page, totalPages;
    }
}
