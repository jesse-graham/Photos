package Componets;
import java.util.*;
import java.util.ArrayList;

public class Album {
    private List<User> users;
    private List<String> photos;

    // Default constructor
    public Album() {
        this.users = new ArrayList<>();
        this.photos = new ArrayList<>();
    }

    // Constructor with initial data
    public Album(List<User> users, List<String> photos) {
        this.users = new ArrayList<>(users);
        this.photos = new ArrayList<>(photos);
    }

    // Getters
    public List<User> getUsers() {
        return users;
    }

    public List<String> getPhotos() {
        return photos;
    }

    // Setters
    public void setUsers(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    public void setPhotos(List<String> photos) {
        this.photos = new ArrayList<>(photos);
    }

    // Methods to modify the lists
    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public void addPhoto(String photo) {
        this.photos.add(photo);
    }

    public void removePhoto(String photo) {
        this.photos.remove(photo);
    }