package com.pet.app.views.orders;

// Converter.java

// To use this code, add the following Maven dependency to your project:
//
//
//     com.fasterxml.jackson.core     : jackson-databind          : 2.9.0
//     com.fasterxml.jackson.datatype : jackson-datatype-jsr310   : 2.9.0
//
// Import this package:
//
//     import io.quicktype.Converter;
//
// Then you can deserialize a JSON string with
//
//     OrderModel[] data = Converter.fromJsonString(jsonString);


import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderModel {
    private String petName;
    private String paymentStatus;
    private long saleFees;
    private long payment;
    private String createdAt;
    private long saleStatus;
    private long id;

    @JsonProperty("petName")
    public String getPetName() { return petName; }
    @JsonProperty("petName")
    public void setPetName(String value) { this.petName = value; }

    @JsonProperty("saleId")
    public long getId() { return id; }
    @JsonProperty("saleId")
    public void setId(long value) { this.id = value; }


    @JsonProperty("paymentStatus")
    public String getPaymentStatus() { return paymentStatus; }
    @JsonProperty("paymentStatus")
    public void setPaymentStatus(String value) { this.paymentStatus = value; }

    @JsonProperty("saleFees")
    public long getSaleFees() { return saleFees; }
    @JsonProperty("saleFees")
    public void setSaleFees(long value) { this.saleFees = value; }

    @JsonProperty("payment")
    public long getPayment() { return payment; }
    @JsonProperty("payment")
    public void setPayment(long value) { this.payment = value; }

    @JsonProperty("created_at")
    public String getCreatedAt() { return createdAt; }
    @JsonProperty("created_at")
    public void setCreatedAt(String value) { this.createdAt = value; }

    @JsonProperty("saleStatus")
    public long getSaleStatus() { return saleStatus; }
    @JsonProperty("saleStatus")
    public void setSaleStatus(long value) { this.saleStatus = value; }
}

