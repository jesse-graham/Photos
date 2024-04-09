package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */


public class User implements Serializable{
    /*
    * serialVersionUID - The constant serial number
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /*

    userName - contains name of user
    */
    public String userName;

    /*

    albums - contains list of albums
    */

    public ArrayList<Album> albums;

    /*

    userName - contains name of user
    albums - contains list of albums
    */

    public User(String userName){
        this.userName = userName;
        albums = new ArrayList<>();
    }
    /**

     initiates to add album to list of albums
     @param album - contains lsit of album

     */
    public boolean addAlbum(Album album){
        if(albums.isEmpty()){
            albums.add(album);
            return true;
        } else {
            for(Album i : albums){
                if(i.getAlbumName().equals(album.getAlbumName())){
                    return false;
                }
            }
            albums.add(album);
            return true;
        }
    }
    /**

     returns username for user


     */

    public String getUserName(){
        return userName;
    }

    /**

     returns album from list

     */

    public ArrayList<Album> getAlbums(){
        return albums;
    }

    /**

     initiates to display photo
     @param album - passes in album to be renamed
     @param newName - passes in new name of album

     */

    public boolean renameAlbum(Album album, String newName){
        for(Album a : albums){
            if(a.getAlbumName().equals(newName)){
                return false;
            }
        }
        for(Album i : albums){
            if(i.getAlbumName().equals(album.getAlbumName())){
                i.albumName = newName;
                return true;
            }
        }
        return false;
    }

    /**

     returns name of user

     */

    public String toString(){
        return userName;
    }

    /**

     return album based on passed in name
     @param albumName - name of album

     */

    public Album getAlbum(String albumName){
        for(Album i : albums){
            if(i.getAlbumName().equals(albumName)){
                return i;
            }
        }
        return null;
    }
}