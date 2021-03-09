package com.example.warriorsocial.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.warriorsocial.R;

import org.jetbrains.annotations.NotNull;


public class CalendarEventViewHolder extends RecyclerView.ViewHolder {

    public TextView event_organization_name;
    public TextView event_title;
    public TextView event_date;
    public CalendarEventViewHolder(View itemView) {
        super(itemView);

        event_organization_name = itemView.findViewById(R.id.event_organization_name);
        event_title = itemView.findViewById(R.id.event_title);
        event_date = itemView.findViewById(R.id.event_date);
    }

    public void bindToPost(@NotNull CalendarEvent ce/*View.OnClickListener starClickListener*/) {
        event_organization_name.setText(ce.getOrganizationName());
        event_title.setText(ce.getEventTitle());
        event_date.setText(ce.getEventTimestamp().toString());
    }
}

