package com.example.warriorsocial.ui.discover;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.organizations.StudentOrganization;

import org.jetbrains.annotations.NotNull;

public class DiscussionCategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView discussion_category_name;
    public DiscussionCategoryViewHolder(View itemView) {
        super(itemView);

        discussion_category_name = itemView.findViewById(R.id.discussion_category_name);
    }

    public void bindToPost(@NotNull DiscussionCategory ce/*View.OnClickListener starClickListener*/) {
        discussion_category_name.setText(ce.getCategoryName());
    }
}