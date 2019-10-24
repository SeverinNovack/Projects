package com.example.severin.kersti;

import java.util.ArrayList;
import java.util.HashMap;

public class Friends {
    private String date;
    private String name;
    private String pic;
    private HashMap<String, Boolean> Tags;

    public String getName() {
        return name;
    }

    public Friends(String date) {
        this.date = date;
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Friends(){};

}
