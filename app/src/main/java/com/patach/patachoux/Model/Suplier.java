package com.patach.patachoux.Model;

public class Suplier {

    String name,email;

    public Suplier(String name,String email) {
        this.email = email;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
