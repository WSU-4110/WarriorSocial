package com.example.warriorsocial.ui.organizations;

public class StudentOrganization {
    private String organizationName;
    private String organizationEmail;
    private String organizationDescription;
    private String organizationImageUrl;


    public StudentOrganization() {}

    public StudentOrganization(String organizationName, String organizationEmail, String organizationDescription, String organizationImageUrl) {
        this.organizationName = organizationName;
        this.organizationEmail = organizationEmail;
        this.organizationDescription = organizationDescription;
        this.organizationImageUrl = organizationImageUrl;
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

    public String getOrganizationImageUrl() { return organizationImageUrl; }

    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription;
    }

    public void setOrganizationEmail(String organizationEmail) {
        this.organizationEmail = organizationEmail;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setOrganizationImageUrl(String organizationImageUrl) {
        this.organizationImageUrl = organizationImageUrl;
    }
}
