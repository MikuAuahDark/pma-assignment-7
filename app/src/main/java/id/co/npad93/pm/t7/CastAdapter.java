package id.co.npad93.pm.t7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastAdapter extends RecyclerView.Adapter<CastViewHolder> {
    public CastAdapter(Cast[] casts) {
        this.casts = casts;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_cast, parent, false);

        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        holder.bind(casts[position]);
    }

    @Override
    public int getItemCount() {
        return casts.length;
    }

    private final Cast[] casts;
}
