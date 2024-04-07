package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;
import javafx.scene.effect.Glow;
import javafx.scene.text.TextAlignment;
import java.awt.image.BufferedImage;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;


//import javax.swing.text.html.ImageView;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.io.File;

import java.awt.*;

public class AlbumViewController {
    public Label albumName;
    public ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML private AnchorPane selPane, prevSelPane;
    @FXML private Photo selectedPhoto;

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

    //public String selectedPhoto;

    Scene AlbumViewScene;

    public void start(Stage primaryStage, User currentUser, Scene prev, UserController uc, Album album, Admin admin){
        AlbumViewScene = primaryStage.getScene();
        this.primaryStage = primaryStage;
        this.currentUser = currentUser;
        this.prev = prev;
        this.uc = uc;
        this.album = album;
        this.admin = admin;
        photos = album.getPhotos();
        selected = null;
        isSelected = false;

        for(int k = 0; k < photos.size(); k++){
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
//        for(int i = 0; i < album.getPhotos().size(); i++){
//
//            tilePane.getChildren().add(album.getPhotos().get(i).getLabel());
//        }
        restoreAlbumData();
        primaryStage.setResizable(true);
        albumName.setText("Album: " + album.getAlbumName());
    }

    public void displayPhoto(ActionEvent actionEvent) {
        try{
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/displayPhoto.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            DisplayPhotoController apg = loader.getController();

            apg.start(primaryStage, currentUser, AlbumViewScene,this, album, admin, selectedPhoto);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            root.requestFocus();

        } catch(Exception e) {
            e.printStackTrace();
        }
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

    public Photo getSelectedPhoto(String id){
        for(Photo i : photos){
            if(i.getPath().equals(id)){
                return i;
            }
        }
        return null;
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

    /**
     * Deselect the photo.
     */
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

    private void createTile(Photo newPhoto) {
        AnchorPane anchTile = new AnchorPane();
        anchTile.setMinWidth(180);
        anchTile.setMaxWidth(180);
        anchTile.setMinHeight(160);
        anchTile.setMaxHeight(160);

        ImageView imgView = new ImageView();
        imgView.setX(15);
        imgView.setY(15);
        imgView.setFitWidth(150);
        imgView.setFitHeight(100);

        imgView.setPreserveRatio(true);
        //imgView.setViewport(new Rectangle2D(150,150,0,0));

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(newPhoto.getPath()));
            Image image = new Image("file:"+newPhoto.getPath());

            imgView.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imgView.getFitWidth() < 150) {

            //imgView.setX(90-((int) imgView.getFitWidth()/2));

        }




        Label lblName = new Label();
        lblName.setLayoutX(15);
        lblName.setLayoutY(134);
        lblName.setMinWidth(150);
        lblName.setMaxWidth(150);
        lblName.setMinHeight(17);
        lblName.setMaxHeight(17);
        lblName.setTextAlignment(TextAlignment.CENTER);


        anchTile.getChildren().addAll(imgView,lblName);
        anchTile.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                selectPhoto(event, anchTile);

            }
        });

        imgView.toFront();
        tilePane.getChildren().add(anchTile);
    }

    private void selectPhoto(MouseEvent event, AnchorPane anchTile) {
        if (selPane != anchTile) {
            prevSelPane = selPane;
            selPane = anchTile;
        }
        anchTile.setStyle("-fx-border-color: black; -fx-alignment: top-center");
        anchTile.setEffect(new Glow(0.5));
        ////System.out.println("Tile pressed ");
        if (prevSelPane != null) {
            if (prevSelPane != selPane) {
                prevSelPane.setStyle("-fx-alignment: top-center");
                prevSelPane.setEffect(new Glow(0));
            }
        } else {
            prevSelPane = selPane;
        }
        ////System.out.println("Tile pressed " + tilePane.getChildren().indexOf(selPane));

        selectedPhoto = album.getPhoto(tilePane.getChildren().indexOf(selPane));
        event.consume();


    }

    private void addPhotoToTilePane(Photo newPhoto) {

        createTile(newPhoto);
        album.addPhoto(newPhoto);

    }

    private void restoreAlbumData() {
        ArrayList<Photo> arrPhotos = album.getPhotos();
        for(Photo pic : arrPhotos)
        {
            createTile(pic);
        }
        selPane = null;
        prevSelPane = null;
    }

}
