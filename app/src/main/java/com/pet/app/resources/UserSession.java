package com.pet.app.resources;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static volatile UserSession session;

    private SharedPreferences preferences;
    private Context context;

    private UserSession(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
    }

    public static UserSession getSession(Context context) {
        if (session == null)
            session = new UserSession(context);
        return session;
    }

    private String username;
    private String email;
    private String fcm;
    private String photo;
    private String token;
    private String password;
    private String accountType;

    public String getUsername() {
        return  preferences.getString("username", null);
    }

    public void setUsername(String username) {
        this.username = username;
        preferences.edit().putString("username", username).apply();
    }

    public String getEmail() {
        return preferences.getString("email", null);
    }

    public void setEmail(String email) {
        this.email = email;
        preferences.edit().putString("email", email).apply();
    }

    public String getFcm() {
        return preferences.getString("fcm", null);
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
        preferences.edit().putString("fcm", fcm).apply();
    }

    public String getPhoto() {
        return  preferences.getString("photo", null);
    }

    public void setPhoto(String photo) {
        this.photo = photo;
        preferences.edit().putString("photo", photo).apply();
    }

    public String getToken() {
        return preferences.getString("token", "sfs");
    }

    public void setToken(String token) {
        this.token = token;
        preferences.edit().putString("token", token).apply();
    }

    public String getAccountType() {
        return preferences.getString("accountType", null);
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
        preferences.edit().putString("accountType", accountType).apply();
    }
    public String getPassword() {
        return password = preferences.getString("pass", null);
    }

    public void setPassword(String pass) {
        this.password = pass;
        preferences.edit().putString("pass", pass).apply();
    }

    public void destroy() {
        preferences.edit().clear().apply();
    }
}
