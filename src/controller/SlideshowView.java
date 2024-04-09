package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;

import java.io.IOException;
import java.util.ArrayList;


/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class SlideshowView {
    @FXML public Button prevPhotoButton;
    @FXML public Button nextPhotoButton;

    @FXML public ImageView imageView;

    @FXML public AnchorPane anchorPane;

    private ArrayList<Photo> photos;

    private User currentUser;

    Stage primaryStage;

    Album album;

    Admin admin;

    Photo currentPhoto;

    int currentIndex;

    public void start(Stage primaryStage, User currentUser, Album album, Admin admin){
        this.primaryStage = primaryStage;
        this.currentUser = currentUser;
        this.album = album;
        this.admin = admin;
        currentIndex = 0;
        photos = album.getPhotos();

        if(!photos.isEmpty()){
            currentPhoto = album.getPhotos().get(currentIndex);
            prevPhotoButton.setVisible(false);

            if(photos.size() == 1){
                nextPhotoButton.setVisible(false);
            }
            imageView.setImage(currentPhoto.getImage());
        }

        primaryStage.setResizable(true);
    }

    public void returnToAlbumView(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/albumView.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        AlbumViewController apg = loader.getController();

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("/view/user.fxml"));
        AnchorPane root2 = (AnchorPane)loader2.load();
        UserController apg2 = loader2.getController();
        Scene userScene = new Scene(root2);

        apg.start(primaryStage, currentUser, userScene, apg2, album, admin,false);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        root.requestFocus();
    }

    public void previousPhoto(ActionEvent actionEvent) {
        currentIndex--;
        currentPhoto = album.getPhotos().get(currentIndex);

        imageView.setImage(currentPhoto.getImage());

        if(currentIndex == 0){
            prevPhotoButton.setVisible(false);
        }
        if(currentIndex < photos.size()){
            nextPhotoButton.setVisible(true);
        }
    }

    public void nextPhoto(ActionEvent actionEvent) {
        currentIndex++;
        currentPhoto = album.getPhotos().get(currentIndex);

        imageView.setImage(currentPhoto.getImage());

        if(currentIndex == album.numPhotos-1){
            nextPhotoButton.setVisible(false);
        }
        if(currentIndex > 0){
            prevPhotoButton.setVisible(true);
        }
    }

    public void quitApp(ActionEvent actionEvent) throws IOException {
        Admin.writeAdmin(admin);
        Platform.exit();
    }
}
