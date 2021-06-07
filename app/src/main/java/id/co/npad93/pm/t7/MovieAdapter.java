package id.co.npad93.pm.t7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        MovieViewHolder vh = new MovieViewHolder(view);

        view.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset != null ? dataset.size() : 0;
    }

    public ArrayList<BasicMovie> getDataset() {
        return dataset;
    }

    public void setDataset(ArrayList<BasicMovie> data) {
        dataset = data;
    }

    private ArrayList<BasicMovie> dataset;
}
