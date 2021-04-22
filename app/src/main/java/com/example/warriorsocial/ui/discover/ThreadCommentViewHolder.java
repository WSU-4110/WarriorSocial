package com.example.warriorsocial.ui.discover;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

import org.jetbrains.annotations.NotNull;

public class ThreadCommentViewHolder extends RecyclerView.ViewHolder {

    public TextView thread_comment_text;
    public TextView tread_comment_user;
//    public TextView thread_comment_date;

    public ThreadCommentViewHolder(View itemView) {
        super(itemView);

        thread_comment_text = itemView.findViewById(R.id.thread_comment_text);
        tread_comment_user = itemView.findViewById(R.id.thread_comment_user);

    }

    public void bindToPost(@NotNull ThreadComment ce/*View.OnClickListener starClickListener*/) {
        thread_comment_text.setText(ce.getCommentText());
        tread_comment_user.setText(ce.getUser());
//        thread_comment_date.setText(ce.getDate());

    }
}