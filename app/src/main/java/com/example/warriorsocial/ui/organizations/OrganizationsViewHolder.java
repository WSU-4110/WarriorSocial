package com.example.warriorsocial.ui.organizations;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.warriorsocial.R;

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