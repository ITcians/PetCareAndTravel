package com.pet.app.models;

import com.pet.app.resources.Apis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PetModel {
    String petName, petSpecie, petImage, petGender;
    String petHeight, petWeight, petPrice, petAge, petAddress,contact;
    float lat, lang;
    String userToken;
    int id;
    String userPhone;

    public PetModel() {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPetAddress() {
        return petAddress;
    }

    public void setPetAddress(String petAddress) {
        this.petAddress = petAddress;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLang() {
        return lang;
    }

    public void setLang(float lang) {
        this.lang = lang;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetSpecie() {
        return petSpecie;
    }

    public void setPetSpecie(String petSpecie) {
        this.petSpecie = petSpecie;
    }

    public String getPetImage() {
        return petImage;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public String getPetHeight() {
        return petHeight;
    }

    public void setPetHeight(String petHeight) {
        this.petHeight = petHeight;
    }

    public String getPetWeight() {
        return petWeight;
    }

    public void setPetWeight(String petWeight) {
        this.petWeight = petWeight;
    }

    public String getPetPrice() {
        return petPrice;
    }

    public void setPetPrice(String petPrice) {
        this.petPrice = petPrice;
    }

    public PetModel fromJson(JSONObject object) throws JSONException {
        PetModel pet = new PetModel();

        pet.setPetGender(object.getString("petGender"));
        pet.setPetHeight(object.getString("petHeight"));
        if (object.getString("petPhoto").contains(Apis.ImageUrl))
            pet.setPetImage(object.getString("petPhoto"));
        else
            pet.setPetImage(Apis.ImageUrl + object.getString("petPhoto"));
        pet.setPetName(object.getString("petName"));
        pet.setPetPrice(object.getString("petPrice"));
        if (object.has("petSpeice"))
            pet.setPetSpecie(object.getString("petSpeice"));
        else
            pet.setPetSpecie(object.getString("petSpecie"));
        pet.setPetWeight(object.getString("petWeight"));
        pet.setPetAge(object.getString("petAge"));
        pet.setId(object.getInt("petId"));
        //pet.setPetAddress(object.getString("petAddress"));
        pet.setLang((float) object.getDouble("petLang"));
        pet.setLat((float) object.getDouble("petLat"));
        pet.setContact(object.getString("petContactNo"));
        return pet;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("petName", petName);
        object.put("petAge", petAge);
        object.put("petGender", petGender);
        object.put("petHeight", petHeight);
        object.put("petWeight", petWeight);
        object.put("petImage", petImage);
        object.put("petPrice", petPrice);
        object.put("petId", id);
        object.put("petSpecie", petSpecie);
        object.put("petLat", lat);
        object.put("petLang", lang);
        object.put("petPhoto", petImage);
        object.put("petContactNo",contact);
        return object;
    }

    public Map<String, String> toMap() {
        Map<String, String> object = new HashMap<>();
        object.put("petName", petName);
        object.put("petAge", petAge);
        object.put("petGender", petGender);
        object.put("petHeight", petHeight);
        object.put("petWeight", petWeight);
        object.put("petImage", petImage);
        object.put("petPrice", petPrice);
        object.put("petSpecie", petSpecie);
        object.put("petId", String.valueOf(id));
        object.put("petLat", String.valueOf(lat));
        object.put("petLang", String.valueOf(lang));
        object.put("petContactNo",contact);
        object.put("petAddress",String.valueOf(petAddress));
        return object;
    }
}
