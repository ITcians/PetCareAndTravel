package com.pet.app.views.pets.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class Report {
    private long hygieneID;
    private String hygieneType;
    private String createdAt;
    private String updatedAt;
    private long petID;

    @JsonProperty("hygieneId")
    public long getHygieneID() {
        return hygieneID;
    }

    @JsonProperty("hygieneId")
    public void setHygieneID(long value) {
        this.hygieneID = value;
    }

    @JsonProperty("hygieneType")
    public String getHygieneType() {
        return hygieneType;
    }

    @JsonProperty("hygieneType")
    public void setHygieneType(String value) {
        this.hygieneType = value;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String value) {
        this.createdAt = value;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String value) {
        this.updatedAt = value;
    }

    @JsonProperty("petId")
    public long getPetID() {
        return petID;
    }

    @JsonProperty("petId")
    public void setPetID(long value) {
        this.petID = value;
    }
}
