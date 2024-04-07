package model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
    public String userName;

    public ArrayList<Album> albums;

    public User(String userName){
        this.userName = userName;
        albums = new ArrayList<>();
    }

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

    public String getUserName(){
        return userName;
    }

    public ArrayList<Album> getAlbums(){
        return albums;
    }

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

    public String toString(){
        return userName;
    }
}