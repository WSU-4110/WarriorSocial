package com.example.warriorsocial.ui.organizations;

public class StudentOrganization {
    String organizationName;
    String organizationEmail;
    String organizationDescription;


    public StudentOrganization() {}

    public StudentOrganization(String organizationName, String organizationEmail, String organizationDescription) {
        this.organizationName = organizationName;
        this.organizationEmail = organizationEmail;
        this.organizationDescription = organizationDescription;
    }

    public String getOrganizationDescription() {
        return organizationDescription;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription;
    }

    public void setOrganizationEmail(String organizationEmail) {
        this.organizationEmail = organizationEmail;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
