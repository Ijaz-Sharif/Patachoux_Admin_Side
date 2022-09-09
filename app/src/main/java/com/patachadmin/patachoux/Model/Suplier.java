package com.patachadmin.patachoux.Model;

public class Suplier {

    String name,email,number,firstName;

    public Suplier(String name,String email,String firstName ,String number) {
        this.email = email;
        this.name=name;
        this.firstName=firstName;
        this.number=number;
    }
    public Suplier(String name,String email) {
        this.email = email;
        this.name=name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
