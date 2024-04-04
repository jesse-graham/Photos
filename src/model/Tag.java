package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;

public class Tag implements Serializable{
    String tagName;
    ArrayList<String> tagValues;

    public Tag (String tagName){
        this.tagName = tagName;
        tagValues = new ArrayList<>();
    }

    public String getTagName() {
        return tagName;
    }

    public ArrayList<String> getTagValues() {
        return tagValues;
    }

    public boolean addTagValue(String value) {
        for (String i : tagValues){
            if (i.equals(value)){
                return false;
            }
        }

        tagValues.add(value);
        Collections.sort(tagValues);
        return true;
    }

    public boolean deleteTagValue(String value) {
        for (String i : tagValues){
            if (i.equals(value)){
                tagValues.remove(i);
                return true;
            }
        }
        return false;
    }

    public String toString(){
        return tagName;
    }

}