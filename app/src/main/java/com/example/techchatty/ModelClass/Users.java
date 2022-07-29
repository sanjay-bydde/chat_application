package com.example.techchatty.ModelClass;

public class Users {

    String uid;
    public String name;
    public String username;
    public String imageUri;
    public String status;

    public Users() {
    }

    public Users(String uid, String name, String username, String imageUri,String status) {
        this.uid = uid;
        this.name = name;
        this.username = username;
        this.imageUri = imageUri;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
