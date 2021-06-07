package id.co.npad93.pm.t7;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
        View root = inflater.inflate(R.layout.fragment_movie, container, false);
        root.<SearchView>findViewById(R.id.searchView).setIconifiedByDefault(false);

        recyclerView = root.findViewById(R.id.recyclerView);

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        return root;
    }

    @Override
    public void onRefresh() {
        // TODO
        swipeRefreshLayout.setRefreshing(false);
    }

    public void loadRecyclerViewDataset(ArrayList<BasicMovie> dataset) {
        MovieAdapter movieAdapter = new MovieAdapter();
        movieAdapter.setDataset(dataset);
        recyclerView.setAdapter(movieAdapter);
    }

    public void setDataKind(int kind) {
        if (recyclerView == null) {
            return;
        }

        boolean switchData = this.kind != kind;

        this.kind = kind;

        if (switchData) {
            recyclerView.setAdapter(null);
            Helper.switchData(this, false, kind);
        }
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int kind;
}
