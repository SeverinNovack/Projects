package com.example.severin.kersti;

import java.util.HashMap;

public class Anfragen {
    private String anfrage;
    private String name;
    private String pic;
    private HashMap<String, Boolean> Tags;

    public Anfragen() {
    }

    public Anfragen(String anfrage) {
        this.anfrage = anfrage;
    }

    public String getAnfrage() {
        return anfrage;


    }

    public void setAnfrage(String anfrage) {
        this.anfrage = anfrage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public HashMap<String, Boolean> getTags() {
        return Tags;
    }

    public void setTags(HashMap<String, Boolean> tags) {
        Tags = tags;
    }
}
