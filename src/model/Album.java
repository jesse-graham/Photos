package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.File;

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

    public boolean addPhoto(File file, boolean isStock){
        if(photos.isEmpty()){
            photos.add(new Photo(file, this, isStock));
            updateOldestandNewest();
            return true;
        } else {
            for(Photo i : photos){
                if(i.getPath().equals(file.getPath())){
                    return false;
                }
            }
            photos.add(new Photo(file, this, isStock));
            updateOldestandNewest();
            return true;
        }
    }

    public Photo getPhoto(int i){
        return photos.get(i);
    }

    public ArrayList<Photo> getPhotos(){
        return photos;
    }

    @Override
    public String toString() {
        String name = STR."\{albumName}     \{numPhotos} photos\n";

        if(photos.isEmpty()){
            return name;
        } else {
            String date = STR."\{new SimpleDateFormat("MM/dd/yyyy").format(oldest.getTime())} - \{new SimpleDateFormat("MM/dd/yyyy").format(newest.getTime())}";
            return name + date;
        }
    }

    public void addPhoto(Photo newPhoto) {
        photos.add(newPhoto);
    }
}