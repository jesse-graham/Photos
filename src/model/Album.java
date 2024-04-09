package model;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.File;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class Album implements Serializable{
    /*
     * serialVersionUID - The constant serial number.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /*
     * albumName - The name of the album
     */
    String albumName;

    /*
     * numPhotos - The number of photos on the album.
     */
    public int numPhotos;

    /*
     * oldest - The date of the oldest photo on the album.
     */
    Calendar oldest = null;

    /*
     * newest - The date of the newest photo on the album.
     */
    Calendar newest = null;

    /*
     * isStock - Sores weather or not the photo is a stock photo.
     */
    boolean isStock = false;

    /*
     * photos - The list of all photos on the album.
     */
   public ArrayList<Photo> photos;

    /*
     * Album - creates an album object.
     * @param name
     */
    public Album(String name){
        this.albumName = name;
        photos = new ArrayList<>();
        numPhotos = photos.size();
        updateOldestandNewest();
    }

    /*
     * getAlbumName - gets the name of the album.
     * @param name
     */
    public String getAlbumName()
    {
        return albumName;
    }

    /*
     * setAlbum - sets the name of an album.
     * @param name
     */
    public void setAlbum(String name)
    {
        albumName = name;
    }

    /*
     * updateOldestandNewest - updates the dates of the newest and oldest photos on the album.
     */
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

    /*
     * addPhoto - adds a photo to the album using the input parameters.
     * @param file
     * @param isStock
     */
    public boolean addPhoto(File file, boolean isStock){
        if(photos.isEmpty()){
            photos.add(new Photo(file, this, isStock));
            numPhotos = photos.size();
            updateOldestandNewest();
            return true;
        } else {
            for(Photo i : photos){
                if(i.getPath().equals(file.getPath())){
                    return false;
                }
            }
            photos.add(new Photo(file, this, isStock));
            numPhotos = photos.size();
            updateOldestandNewest();
            return true;
        }
    }

    /*
     * getPhoto - gets the photo at the provided index.
     * @param i
     */
    public Photo getPhoto(int i){
        return photos.get(i);
    }

    /*
     * getPhotos - gets the list of photos on the album.
     */
    public ArrayList<Photo> getPhotos(){
        return photos;
    }

    /*
     * toString - converts the album as a string.
     */
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

    /*
     * addPhoto - Adds the provided photo to the album.
     * @param photo
     */
    public boolean addPhoto(Photo newPhoto) {
        if(photos.isEmpty()){
            photos.add(newPhoto);
            numPhotos = photos.size();
            updateOldestandNewest();
            return true;
        } else {
            for(Photo i : photos){
                if(i.getPath().equals(newPhoto.getPath())){
                    return false;
                }
            }
            photos.add(newPhoto);
            numPhotos = photos.size();
            updateOldestandNewest();
            return true;
        }
    }

    /*
     * removePhoto - removes the provide photo from the album.
     * @param photo
     */
    public void removePhoto(Photo photo){
        int index = 0;
        for(Photo i :  photos){
            if(i.getPath().equals(photo.getPath())){
                break;
            }
            index++;
        }
        photos.remove(index);
        numPhotos = photos.size();
        updateOldestandNewest();
    }

    /*
     * isStock - returns weather or not the album is stock.
     */
    public boolean isStock() {
        return isStock;
    }

    /*
     * setStock - makes the album a stock album.
     */
    public void setStock(){
        isStock = true;
    }

    /*
     * changeName - Changes the name of the album to the name provided
     * @param newName
     * @param albums
     */
    public boolean changeName(String newName, ArrayList<Album> albums){
        for(Album i : albums){
            if(i.getAlbumName().equals(newName)){
                return false;
            }
        }
        albumName = newName;
        return true;
    }

    /*
     * updateNumPhotos - updates the number of photos to the number of photos in the photos list.
     */
    public void updateNumPhotos(){
        numPhotos = photos.size();
    }
}