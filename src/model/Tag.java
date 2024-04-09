package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class Tag implements Serializable{
    /*
     * serialVersionUID - The constant serial number
     */
    @Serial
    private static final long serialVersionUID = 1L;
    String tagName;
    ArrayList<String> tagValues;

    /*

    tag - contains name for tag
    */

    public Tag (String tagName){
        this.tagName = tagName;
        tagValues = new ArrayList<>();
    }

    /**

     returns name of tag


     */

    public String getTagName() {
        return tagName;
    }

    /**

     returns tagValues from list of tag values

     */

    public ArrayList<String> getTagValues() {
        return tagValues;
    }

    /**

     initiates to add tag value
     @param value - value for tag

     */

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

    /**

     initiates to delete tag value
     @param value - contains value for tag to compare to delete

     */

    public boolean deleteTagValue(String value) {
        for (String i : tagValues){
            if (i.equals(value)){
                tagValues.remove(i);
                return true;
            }
        }
        return false;
    }

    /**

     returns name of tag

     */

    public String toString(){
        return tagName;
    }

    /**

     initiates to add tag with name
     @param name - name of tag to add

     */

    public Boolean addTag(String name){
        for(String i : tagValues){
            if(i.equalsIgnoreCase(name)){
                return false;
            }
        }
        if(tagValues.isEmpty()){
            tagValues.add(name);
            return true;
        }
        else{
            for(int i = 0;  i < tagValues.size(); i ++){
                if(tagValues.get(i).compareToIgnoreCase(name) > 0 ){
                    tagValues.add(i,name);
                    return true;
                }
                else if(tagValues.size()-1 == i && tagValues.get(i).compareToIgnoreCase(name) < 0){
                    tagValues.add(name);
                    return true;
                }
            }
        }
        return true;



    }

}