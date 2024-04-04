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

    public String getUserName(){
        return userName;
    }

    public ArrayList<Album> getAlbums(){
        return albums;
    }
}