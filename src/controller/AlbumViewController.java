package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import java.util.List;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;
import javafx.stage.Modality;
import model.User;
import javafx.scene.effect.Glow;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.ImageView;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class AlbumViewController {
    public Label albumName;
    public ScrollPane scrollPane;
    @FXML
    public Button makealbumformsearchbutton;

    @FXML
    private TilePane tilePane;

    @FXML private AnchorPane selPane, prevSelPane;

    @FXML private Photo selectedPhoto;

    public static ArrayList<Photo> photos;

    User currentUser;

    Stage primaryStage;

    Album album;

    Admin admin;

    Scene prev;

    UserController uc;

    public static String selected;

    public static Boolean isSelected = false;

    Scene AlbumViewScene;

    public void start(Stage primaryStage, User currentUser, Scene prev, UserController uc, Album album, Admin admin, Boolean fromSearch){
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

        if(!fromSearch){
            makealbumformsearchbutton.setVisible(false);
        }

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
        restoreAlbumData();
        primaryStage.setResizable(true);
        albumName.setText(STR."Album: \{album.getAlbumName()}");
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
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().add(
                new ExtensionFilter("Images (PNG,JPG,BMP,GIF)", "*.png", "*.jpg", "*.bmp", "*.gif"));
        List<File> files = filechooser.showOpenMultipleDialog(primaryStage);
        if(files == null){
            return;
        }
        ArrayList<File> filesThatExist = new ArrayList<>();
        for (File file : files) {
            if (file != null) {
                boolean add = true;
                String stockPath = "data/StockPhotos";
                for (Photo value : photos) {
                    if (value.getPath().equalsIgnoreCase(file.getAbsolutePath())) {
                        filesThatExist.add(file);
                        add = false;
                        break;
                    }
                }
                if (add) {
                    Photo photo = null;

                    if (file.getParentFile().getName().equals("StockPhotos")) {
                        if (file.getParentFile().getParentFile().getName().equals("data")) {
                            photo = checkAlbums(stockPath + file.getName(), file);
                            if (photo == null) {
                                photo = new Photo(file, album, true);
                            }
                        }
                    } else {
                        photo = checkAlbums(file.getAbsolutePath(), file);
                        if (photo == null) {
                            photo = new Photo(file, album, false);
                        }

                    }

                    photos.addFirst(photo);
                    if (photo.getLabel() == null) {
                        photo.setPhotoThumbnail();
                    }
                    tilePane.getChildren().addFirst(photo.getLabel());
                    album.updateOldestandNewest();
                    restoreAlbumData();

                }
            }
        }
        if(!filesThatExist.isEmpty()){
            primaryStage.setResizable(false);
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(primaryStage);
            message.setTitle("Add Photo");
            message.setHeaderText("Cannot Add Photo");
            message.setContentText("Photo already exists in album. Could not add the following files:");
            message.setGraphic(null);
            String photoPaths = "";
            for (File file : filesThatExist) {
                photoPaths = STR."\{photoPaths}\{file.getAbsolutePath()}\n";
            }

            TextArea ta = new TextArea(photoPaths);
            ta.setEditable(false);
            ta.setMaxWidth(Double.MAX_VALUE);
            ta.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(ta, Priority.ALWAYS);
            GridPane.setHgrow(ta, Priority.ALWAYS);
            GridPane gp = new GridPane();

            gp.add(ta, 0, 0);
            message.getDialogPane().setExpandableContent(gp);
            message.showAndWait();
            primaryStage.setResizable(true);
        }
    }
    public Photo checkAlbums(String temp, File file){
        for(int k = 0; k < currentUser.getAlbums().size(); k++){
            for(int m = 0; m < currentUser.getAlbums().get(k).getPhotos().size(); m++){
                if(currentUser.getAlbums().get(k).getPhotos().get(m).getPath().equals(temp)){
                    return currentUser.getAlbums().get(k).getPhotos().get(m);
                }
            }
        }
        return null;
    }

    public void removePhoto(ActionEvent actionEvent) {
//        if(selectedPhoto.isStock()){
//            Alert message = new Alert(Alert.AlertType.INFORMATION);
//            message.initOwner(primaryStage);
//            message.setTitle("Deletion Error");
//            message.setHeaderText("Photo Cannot Be Deleted");
//            message.setContentText("This is a stock Photo, it cannot be deleted.");
//            message.setGraphic(null);
//            message.showAndWait();
//            return;
//        }
        photos.remove(selectedPhoto);
        album.numPhotos = photos.size();
        restoreAlbumData();
    }

    public void movePhoto(ActionEvent actionEvent) {
        // Create a new stage for the input window
        Stage inputStage = new Stage();

        // Create a text field for user input
        javafx.scene.control.TextField inputTextField = new javafx.scene.control.TextField();
        inputTextField.setPromptText("Destination album name");

        // Create a button to submit the input
        javafx.scene.control.Button submitButton = new Button("Move");
        submitButton.setOnAction(e -> {
            String userInput = inputTextField.getText();
            if(userInput.trim().isEmpty()){
                Alert message = new Alert(Alert.AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Error Moving Photo");
                message.setHeaderText("Invalid Album Name");
                message.setContentText("Enter album name to move the photo to.");
                message.setGraphic(null);
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }

            boolean validAlbum = false;
            for(Album i : currentUser.albums){
                if(i.getAlbumName().equals(userInput)){
                    validAlbum = true;
                    break;
                }
            }
            if(!validAlbum){
                Alert message = new Alert(Alert.AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Error Moving Photo");
                message.setHeaderText("Invalid Album Name");
                message.setContentText("This album does not exist, enter the name of an existing album.");
                message.setGraphic(null);
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }
            boolean validMove = currentUser.getAlbum(userInput).addPhoto(selectedPhoto);

            if(!validMove){
                Alert message = new Alert(Alert.AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Error Moving Photo");
                message.setHeaderText(STR."Photo Already Exists in Album: \{userInput}");
                message.setContentText(STR."This photo already exists in the album: \{userInput}. Dupllicates are not allowed, pick a differient album.");
                message.setGraphic(null);
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }
            album.removePhoto(selectedPhoto);
            restoreAlbumData();
            try {
                Admin.writeAdmin(admin);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            inputStage.close(); // Close the input window
        });

        // Layout for the input window
        VBox inputLayout = new VBox(10);
        inputLayout.setPadding(new Insets(20));
        inputLayout.getChildren().addAll(inputTextField, submitButton);

        // Create the input scene
        Scene inputScene = new Scene(inputLayout, 300, 100);

        // Set the input scene and show the input window
        inputStage.setScene(inputScene);
        inputStage.setTitle("Move Photo");
        inputStage.show();
    }

    public void displayCaptionEditior(ActionEvent actionEvent) {
        // Create a new stage for the input window
        Stage inputStage = new Stage();

        // Create a text field for user input
        javafx.scene.control.TextField inputTextField = new javafx.scene.control.TextField();
        inputTextField.setText(selectedPhoto.getCaption());

        // Create a button to submit the input
        javafx.scene.control.Button submitButton = new Button("Confirm");
        submitButton.setOnAction(e -> {
            String userInput = inputTextField.getText();
            selectedPhoto.setCaption(userInput);
            restoreAlbumData();
            inputStage.close();
        });

        // Layout for the input window
        VBox inputLayout = new VBox(10);
        inputLayout.setPadding(new Insets(20));
        inputLayout.getChildren().addAll(inputTextField, submitButton);

        // Create the input scene
        Scene inputScene = new Scene(inputLayout, 300, 100);

        // Set the input scene and show the input window
        inputStage.setScene(inputScene);
        inputStage.setTitle("Edit Caption");
        inputStage.show();
    }

    public void displayTagEditor(ActionEvent actionEvent) throws IOException {
        Stage stageAdd = new Stage();
        FXMLLoader load = new FXMLLoader();
        AnchorPane root;
        load.setLocation(getClass().getResource("/view/tagEditor.fxml"));
        root = (AnchorPane)load.load();
        tagEditorController tpc= load.getController();
        tpc.start(stageAdd, selectedPhoto, admin);
        Scene add = new Scene(root);
        stageAdd.setScene(add);
        stageAdd.setTitle("Tag Photo");
        stageAdd.setResizable(false);
        stageAdd.initModality(Modality.WINDOW_MODAL);
        stageAdd.initOwner(primaryStage);
        root.requestFocus();
        primaryStage.setResizable(false);

        stageAdd.showAndWait();
        primaryStage.setResizable(true);
    }

    public void displaySlideshowView(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/slideshowView.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        SlideshowView apg = loader.getController();

        apg.start(primaryStage, currentUser, album, admin);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        root.requestFocus();
    }

    public void returnToUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/user.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        UserController apg = loader.getController();

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("/view/login.fxml"));
        AnchorPane root2 = (AnchorPane)loader2.load();
        LoginController apg2 = loader2.getController();
        Scene loginScene = new Scene(root2);

        apg.start(primaryStage,admin.users, loginScene, apg2, admin, currentUser);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        root.requestFocus();
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
        Image image = new Image(STR."file:\{newPhoto.getPath()}");
        imgView.setImage(image);


        Label lblName = new Label();
        lblName.setLayoutX(15);
        lblName.setLayoutY(134);
        lblName.setMinWidth(150);
        lblName.setMaxWidth(150);
        lblName.setMinHeight(17);
        lblName.setMaxHeight(17);
        lblName.setTextAlignment(TextAlignment.CENTER);
        lblName.setText(newPhoto.getCaption());


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

        selectedPhoto = album.getPhoto(tilePane.getChildren().indexOf(selPane));
        event.consume();
    }

    private void restoreAlbumData() {
        ArrayList<Photo> arrPhotos = album.getPhotos();
        tilePane.getChildren().clear();
        for(Photo pic : arrPhotos)
        {
            createTile(pic);
        }
        selPane = null;
        prevSelPane = null;
    }

    public void makeAlbumFromSearch(ActionEvent actionEvent) {
        // Create a new stage for the input window
        Stage inputStage = new Stage();

        // Create a text field for user input
        javafx.scene.control.TextField inputTextField = new javafx.scene.control.TextField();
        inputTextField.setPromptText("Type album name here");

        // Create a button to submit the input
        javafx.scene.control.Button submitButton = new Button("Confirm");
        submitButton.setOnAction(e -> {
            String userInput = inputTextField.getText();

            if (userInput.isEmpty()){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Error Making Album");
                message.setHeaderText("No Name Entered");
                message.setContentText("Enter a name for the album.");
                message.setGraphic(null);
                message.showAndWait();
                return;
            }
            boolean result = album.changeName(userInput, currentUser.getAlbums());
            if(!result){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Error Making Album");
                message.setHeaderText("Album Name Already Taken");
                message.setContentText("This album name is taken, choose a different one.");
                message.setGraphic(null);
                message.showAndWait();
                return;
            }
            album.updateOldestandNewest();
            album.updateNumPhotos();
            currentUser.addAlbum(album);
            restoreAlbumData();
            inputStage.close();
        });

        // Layout for the input window
        VBox inputLayout = new VBox(10);
        inputLayout.setPadding(new Insets(20));
        inputLayout.getChildren().addAll(inputTextField, submitButton);

        // Create the input scene
        Scene inputScene = new Scene(inputLayout, 300, 100);

        // Set the input scene and show the input window
        inputStage.setScene(inputScene);
        inputStage.setTitle("Enter a name for the album.");
        inputStage.show();
    }

    public void quitApp(ActionEvent actionEvent) throws IOException {
        Admin.writeAdmin(admin);
        Platform.exit();
    }
}
