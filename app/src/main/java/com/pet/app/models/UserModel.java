package com.pet.app.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    private String name;
    private String email;
    private String password;
    private String photo;
    private String accountType;
    private String fcm;
    private String token;

    public UserModel() {
    }

    private UserModel(String name, String email, String password, String photo, String accountType, String fcm, String token) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.accountType = accountType;
        this.fcm = fcm;
        this.token = token;
    }

    public UserModel fromJson(JSONObject map) throws JSONException {
        return new UserModel(
                map.getString("name"),
                map.getString("email"),
                map.has("password") ?
                map.getString("password"):null,
                map.getString("photo"),
                map.getString("accountType"),
                map.getString("fcm"),
                map.getString("token"));
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object=new JSONObject();
        object.put("name",name);
        object.put("email",email);
        object.put("fcm",fcm);
        object.put("password",password);
        object.put("accountType",accountType);
        object.put("token",token);
        object.put("photo",photo);
        return object;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
