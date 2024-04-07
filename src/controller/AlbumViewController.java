package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import java.util.ArrayList;
import java.io.File;

import java.awt.*;

public class AlbumViewController {
    public Label albumName;
    public ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    public static ArrayList<Photo> photos;

    private User currentUser;

    Stage primaryStage;

    Album album;

    Admin admin;

    Scene previous;

    LoginController lpg;

    Scene prev;

    UserController uc;
    public static String selected;

    public static Boolean isSelected = false;

    public void start(Stage primaryStage, User currentUser, Scene prev, UserController uc, Album album, Admin admin){
        this.primaryStage = primaryStage;
        this.currentUser = currentUser;
        this.prev = prev;
        this.uc = uc;
        this.album = album;
        this.admin = admin;
        AlbumViewController.photos = album.getPhotos();
        selected = null;
        isSelected = false;

        for(int k = 0; k < photos.size(); k++){  // fix this
            File file = new File(photos.get(k).getPath());
            if(!file.exists()){
                photos.remove(k);
                k--;
                album.numPhotos--;
            }
            else{
                photos.get(k).setPhotoThumbnail();
            }

        }
        for(int i = 0; i < album.getPhotos().size(); i++){

            tilePane.getChildren().add(album.getPhotos().get(i).getLabel());
        }
        primaryStage.setResizable(true);
        albumName.setText("Album: " + album.getAlbumName());
    }


    public void displayPhoto(ActionEvent actionEvent) {
    }

    public void addPhoto(ActionEvent actionEvent) {
    }

    public void removePhoto(ActionEvent actionEvent) {
    }

    public void movePhoto(ActionEvent actionEvent) {
    }

    public void displayCaptionEditior(ActionEvent actionEvent) {
    }

    public void displayTagEditor(ActionEvent actionEvent) {
    }

    public void displaySlideshowView(ActionEvent actionEvent) {
    }

    public void returnToUser(ActionEvent actionEvent) {
    }


    public int getSelectedUser(){
        if(!isSelected && selected == null){
            return -1;
        }
        for(int i = 0; i < tilePane.getChildren().size(); i++){
            if(tilePane.getChildren().get(i).getId().equalsIgnoreCase(selected)){
                return i;
            }
        }
        return -1;

    }

    public void deselect(){
        int x = getSelectedUser();
        if(x == -1){
            selected = null;
            isSelected = false;
            return;
        }
        if(isSelected && x >= 0 && x < photos.size()){
            if(((Label)tilePane.getChildren().get(x)).getStylesheets().size() == 2){
                ((Label)tilePane.getChildren().get(x)).getStylesheets().remove(1);
                ((Label)tilePane.getChildren().get(x)).getStylesheets().add(getClass().getResource("/view/emptyBorder.css").toExternalForm());
            }
        }
        selected = null;
        isSelected = false;
    }
}
