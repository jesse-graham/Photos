package model;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Photo implements Serializable{

    String photoPath;

    Calendar date;

    String caption;

    ArrayList<Tag> tags;

    public Photo(File file, Album album){
        caption = "";
        photoPath = file.getPath();
        tags = new ArrayList<>();
        date = Calendar.getInstance();
        date.setTimeInMillis(file.lastModified());
        date.set(Calendar.MILLISECOND, 0);
        album.numPhotos++;
    }

    public Calendar getCal(){
        return date;
    }

    public String getCaption(){
        return caption;
    }

    public void setCaption(String string){
        caption = string;
    }

    public String getPath(){
        return photoPath;
    }

    public void setPath(String path){
        photoPath = path;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }

    public void setTags(ArrayList<Tag> t){
        this.tags = t;
    }

    public String getDate(){
        if(date == null){
            return "";
        }
        return new SimpleDateFormat("MM/dd/yyyy").format(date.getTime());
    }

    public String getTime(){
        if(date == null){
            return "";
        }
        return new SimpleDateFormat("hh:mm:ss aa").format(date.getTime());
    }
}