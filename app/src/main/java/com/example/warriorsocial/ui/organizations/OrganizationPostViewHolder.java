package com.example.warriorsocial.ui.organizations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class OrganizationPostViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "OrganizationPostViewHolder";

    public TextView post_title;
    public TextView post_description;
    public ImageView post_image;
    public ImageView post_likes_image;
    public TextView post_num_likes;

    public OrganizationPostViewHolder(@NonNull View itemView) {
        super(itemView);

        post_title = itemView.findViewById(R.id.post_title);
        post_description = itemView.findViewById(R.id.post_description);
        post_image = itemView.findViewById(R.id.post_image);
        post_likes_image = itemView.findViewById(R.id.post_likes_image);
        post_num_likes = itemView.findViewById(R.id.post_num_likes);
    }

    public void bindToPost(@NotNull final StudentOrganizationPost ce, View.OnClickListener likeClickListener, final Handler h) {
        post_title.setText(ce.getPostTitle());
        post_description.setText(ce.getPostDescription());
        post_num_likes.setText(String.valueOf(ce.likeCount));

        post_likes_image.setOnClickListener(likeClickListener);

        // Setting postImage on separate thread due to open connection requirements
        new Thread(new Runnable() {
            public void run() {
                final Bitmap[] bm = {null};
                bm[0] = getImageBitmap(ce.getPostImage());
                h.post(
                        new Runnable() {
                            public void run() {
                                post_image.setImageBitmap(bm[0]);
                            }
                        }
                );
            }
        }).start();
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }
}
