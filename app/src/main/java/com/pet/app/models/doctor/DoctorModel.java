package com.pet.app.models.doctor;


import com.fasterxml.jackson.annotation.JsonProperty;

// DoctorModel.java

public class DoctorModel {
    private long doctorID;
    private String doctorName;
    private String doctorAddress;
    private String specification;
    private long contact;
    private String createdAt;
    private String updatedAt;

    @JsonProperty("doctorId")
    public long getDoctorID() { return doctorID; }
    @JsonProperty("doctorId")
    public void setDoctorID(long value) { this.doctorID = value; }

    @JsonProperty("doctorName")
    public String getDoctorName() { return doctorName; }
    @JsonProperty("doctorName")
    public void setDoctorName(String value) { this.doctorName = value; }

    @JsonProperty("doctorAddress")
    public String getDoctorAddress() { return doctorAddress; }
    @JsonProperty("doctorAddress")
    public void setDoctorAddress(String value) { this.doctorAddress = value; }

    @JsonProperty("specification")
    public String getSpecification() { return specification; }
    @JsonProperty("specification")
    public void setSpecification(String value) { this.specification = value; }

    @JsonProperty("contact")
    public long getContact() { return contact; }
    @JsonProperty("contact")
    public void setContact(long value) { this.contact = value; }

    @JsonProperty("created_at")
    public String getCreatedAt() { return createdAt; }
    @JsonProperty("created_at")
    public void setCreatedAt(String value) { this.createdAt = value; }

    @JsonProperty("updated_at")
    public String getUpdatedAt() { return updatedAt; }
    @JsonProperty("updated_at")
    public void setUpdatedAt(String value) { this.updatedAt = value; }
}
