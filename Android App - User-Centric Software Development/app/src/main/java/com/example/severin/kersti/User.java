package com.example.severin.kersti;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String name;
    private HashMap<String, Boolean> Tags;
    private String profilepic;


    public User(String name, HashMap<String, Boolean> tags, String profilepic) {
        this.name = name;
        Tags = tags;
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTags() {
        HashMap<String, Boolean> map = Tags;
        ArrayList<String> list = new ArrayList<>();
        for (HashMap.Entry<String, Boolean> entry : map.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            if(value == true) {
                list.add(key);
            }
        }
        return list;
    }

    public void setTags(HashMap<String, Boolean> tags) {
        Tags = tags;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public User(){

    }

}
