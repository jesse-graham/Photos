package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Album implements Serializable{
    String albumName;

    public int numPhotos;

    Calendar oldest = null;

    Calendar newest = null;

    ArrayList<Photo> photos;

    public Album(String name){
        this.albumName = name;
        photos = new ArrayList<>();
        numPhotos = photos.size();
        updateOldestandNewest();
    }

    public String getAlbumName()
    {
        return albumName;
    }

    public void setAlbum(String name)
    {
        albumName = name;
    }

    public void updateOldestandNewest(){
        if(photos.isEmpty()){
            oldest = null;
            newest = null;
            return;
        }
        oldest = photos.getFirst().date;
        newest = photos.getFirst().date;

        for (Photo photo : photos) {
            if (photo.date.before(oldest)) {
                oldest = photo.date;
            }
            if (photo.date.after(newest)) {
                newest = photo.date;
            }
        }
    }

    public ArrayList<Photo> getPhotos(){
        return photos;
    }
}