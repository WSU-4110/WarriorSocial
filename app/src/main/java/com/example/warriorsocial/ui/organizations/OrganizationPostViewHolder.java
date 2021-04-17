package com.example.warriorsocial.ui.organizations;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

import org.jetbrains.annotations.NotNull;

public class OrganizationPostViewHolder extends RecyclerView.ViewHolder{

    public TextView post_time;
    public TextView post_description;
    public ImageView post_image;

    public OrganizationPostViewHolder(@NonNull View itemView) {
        super(itemView);

        post_time = itemView.findViewById(R.id.post_time);
        post_description = itemView.findViewById(R.id.post_description);
        post_image = itemView.findViewById(R.id.post_image);
    }

    public void bindToPost(@NotNull StudentOrganizationPost ce/*View.OnClickListener starClickListener*/) {
        //post_time.setText(ce.getPostTime().toString());
        post_description.setText(ce.getPostDescription());
        post_image.setImageURI(Uri.parse(ce.getPostImage()));
    }
}
