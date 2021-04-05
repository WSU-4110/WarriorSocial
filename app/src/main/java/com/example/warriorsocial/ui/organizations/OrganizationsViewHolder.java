package com.example.warriorsocial.ui.organizations;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;
import com.example.warriorsocial.ui.home.CalendarEvent;

import org.jetbrains.annotations.NotNull;

public class OrganizationsViewHolder extends RecyclerView.ViewHolder {

    public TextView student_organization_name;
    public TextView student_organization_description;
    public OrganizationsViewHolder(View itemView) {
        super(itemView);

        student_organization_name = itemView.findViewById(R.id.student_organization_name);
        student_organization_description = itemView.findViewById(R.id.student_organization_description);
    }

    public void bindToPost(@NotNull StudentOrganization ce/*View.OnClickListener starClickListener*/) {
        student_organization_name.setText(ce.getOrganizationName());
        student_organization_description.setText(ce.getOrganizationDescription());
    }
}