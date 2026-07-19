package com.aqlab.dynamicobjectproperties;

public class ProfileBean {
    private String name;
    private int age;
    private CityBean details;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public CityBean getDetails() { return details; }
    public void setDetails(CityBean details) { this.details = details; }
}
