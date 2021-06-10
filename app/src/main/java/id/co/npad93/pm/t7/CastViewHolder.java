package id.co.npad93.pm.t7;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastViewHolder extends RecyclerView.ViewHolder {
    public CastViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.textView14);
        character = itemView.findViewById(R.id.textView19);
        photo = itemView.findViewById(R.id.imageView4);
    }

    public void bind(Cast cast) {
        name.setText(cast.getName());
        character.setText(cast.getCharacter());
        Api.loadImage(photo.getContext(), "w185", cast.getProfilePath(), photo);
    }

    private TextView name, character;
    private ImageView photo;
}
