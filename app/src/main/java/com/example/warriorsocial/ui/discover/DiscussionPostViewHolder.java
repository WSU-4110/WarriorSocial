package com.example.warriorsocial.ui.discover;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

import org.jetbrains.annotations.NotNull;

public class DiscussionPostViewHolder extends RecyclerView.ViewHolder {

    public TextView discussion_category_name;
    public TextView discussion_post_text;
    public TextView discussion_post_title;
    public TextView discussion_poster_id;

    public DiscussionPostViewHolder(View itemView) {
        super(itemView);

        discussion_post_text = itemView.findViewById(R.id.discussion_post_text);
        discussion_post_title = itemView.findViewById(R.id.discussion_post_title);

    }

    public void bindToPost(@NotNull DiscussionPost ce/*View.OnClickListener starClickListener*/) {
        discussion_post_text.setText(ce.getPostText());
        discussion_post_title.setText(ce.getPostTitle());

    }
}