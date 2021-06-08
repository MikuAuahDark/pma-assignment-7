package id.co.npad93.pm.t7;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MovieFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public MovieFragment() {
        // Required empty public constructor
        kind = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        view.<SearchView>findViewById(R.id.searchView).setIconifiedByDefault(false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (!fetching && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        fetching = true;
                        Api.requestNextPage(MovieFragment.this, kind);
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
            Api.switchData(this, true, kind);
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
        Api.switchData(this, false, kind);
    }

    public void markUnrefreshed() {
        swipeRefreshLayout.setRefreshing(false);
        fetching = false;
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int kind;
    private boolean fetching;
    private MovieAdapter inQueue;
}
