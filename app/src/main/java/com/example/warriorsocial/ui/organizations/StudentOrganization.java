package com.example.warriorsocial.ui.organizations;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class StudentOrganization {
    private String organizationName;
    private String organizationEmail;
    private String organizationDescription;
    private String organizationImageUrl;
    private String organizationPhoneNumber;
    private String organizationPresident;
    private String organizationVicePresident;

    public StudentOrganization() {}

    public StudentOrganization(String organizationName, String organizationEmail, String organizationDescription, String organizationImageUrl) {
        this.organizationName = organizationName;
        this.organizationEmail = organizationEmail;
        this.organizationDescription = organizationDescription;
        this.organizationImageUrl = organizationImageUrl;
    }

    public StudentOrganization(String organizationName, String organizationEmail, String organizationDescription, String organizationImageUrl
    , String organizationPhoneNumber, String organizationPresident, String organizationVicePresident) {
        this.organizationName = organizationName;
        this.organizationEmail = organizationEmail;
        this.organizationDescription = organizationDescription;
        this.organizationImageUrl = organizationImageUrl;
        this.organizationPhoneNumber = organizationPhoneNumber;
        this.organizationPresident = organizationPresident;
        this.organizationVicePresident = organizationVicePresident;
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

    public String getOrganizationPhoneNumber() { return organizationPhoneNumber; }

    public String getOrganizationPresident() { return organizationPresident; }

    public String getOrganizationVicePresident() { return organizationVicePresident; }

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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("organizationName", organizationName);
        result.put("organizationEmail", organizationEmail);
        result.put("organizationDescription", organizationDescription);
        result.put("organizationImageUrl", organizationImageUrl);
        result.put("organizationPhoneNumber", organizationPhoneNumber);
        result.put("organizationPresident", organizationPresident);
        result.put("organizationVicePresident", organizationVicePresident);

        return result;
    }
}
