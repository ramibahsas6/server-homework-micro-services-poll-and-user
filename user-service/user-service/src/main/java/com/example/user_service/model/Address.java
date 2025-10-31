package com.example.user_service.model;

public class Address {
    private String country;
    private Integer postalCode;
    private String city;
    private Integer street_number;
    private Integer house_number;

    public Address(String country, Integer postalCode, String city, Integer street_number, Integer house_number) {
        this.country = country;
        this.postalCode = postalCode;
        this.city = city;
        this.street_number = street_number;
        this.house_number = house_number;
    }

    public Address() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStreet_number() {
        return street_number;
    }

    public void setStreet_number(Integer street_number) {
        this.street_number = street_number;
    }

    public Integer getHouse_number() {
        return house_number;
    }

    public void setHouse_number(Integer house_number) {
        this.house_number = house_number;
    }

    @Override
    public String toString() {
        return  "" + country +
                "," + postalCode +
                "," + city +
                "," + street_number +
                "," + house_number + "";
    }
}
