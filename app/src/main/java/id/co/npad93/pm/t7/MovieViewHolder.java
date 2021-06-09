package id.co.npad93.pm.t7;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);

        this.context = itemView.getContext();
        this.title = itemView.findViewById(R.id.textView);
        this.originalTitle = itemView.findViewById(R.id.textView2);
        this.poster = itemView.findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra("id", movie.getId());
        context.startActivity(i);
    }

    public void bind(BasicMovie data) {
        movie = data;
        title.setText(data.getTitle());
        originalTitle.setText(data.getOriginalTitle());
        Api.loadImage(context, "w500", data.getPosterPath(), poster);
    }

    private TextView title, originalTitle;
    private ImageView poster;
    private Context context;
    private BasicMovie movie;
}
