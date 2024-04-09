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
    /*
     * prevPhotoButton - Takes user to the previous photo.
     */
    @FXML public Button prevPhotoButton;

    /*
     * nextPhotoButton - Takes user to the next photo.
     */
    @FXML public Button nextPhotoButton;

    /*
     * imageView - Contains image of the current photo.
     */
    @FXML public ImageView imageView;

    /*
     * anchorPane - Contains entire window.
     */
    @FXML public AnchorPane anchorPane;

    /*
     * photos - Contains the list of all photos belonging to the current user.
     */
    private ArrayList<Photo> photos;

    /*
     * currentUser - Contains the current user.
     */
    private User currentUser;

    /*
     * primaryStage - Contains the primary stage.
     */
    Stage primaryStage;

    /*
     * album - Contains the current album
     */
    Album album;

    /*
     * admin - Contains the admin object.
     */
    Admin admin;

    /*
     * currentPhoto - Contains the current photo.
     */
    Photo currentPhoto;

    /*
     * currentIndex - Contains current index.
     */
    int currentIndex;

    /*
     * Initializes the slideshow Controller
     * @param primaryStage
     * @param currentUser
     * @param album
     * @param admin
     */
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

    /*
     * returnToAlbumView - returns user to album view.
     * @param actionEvent
     */
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

    /*
     * previousPhoto - displays the previous photo.
     * @param actionEvent
     */
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

    /*
     * nextPhoto - displays the next photo.
     * @param actionEvent
     */
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

    /*
     * quitApp - logs the user out, saving their data.
     * @param actionEvent
     */
    public void quitApp(ActionEvent actionEvent) throws IOException {
        Admin.writeAdmin(admin);
        Platform.exit();
    }
}
