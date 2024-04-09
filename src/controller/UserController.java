package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.Admin;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class UserController {

    @FXML
    private TextField albumTextField;

    @FXML
    private Label Username;

    @FXML
    private ListView<Album> albumList;
    ArrayList<User> users;
    User user;

    Stage primaryStage;

    Admin admin;

    LoginController lpg;

    Scene prev;

    Scene userScene;

    public void start(Stage primaryStage, ArrayList<User> users, Scene prev, LoginController lpg, Admin admin, User user) {
        userScene = primaryStage.getScene();
        this.user = user;
        this.users = users;
        this.primaryStage = primaryStage;
        this.prev = prev;
        this.lpg = lpg;
        this.admin = admin;
        albumList.setItems(FXCollections.observableArrayList(user.getAlbums()));
        albumList.getSelectionModel().select(0);
        Username.setText(STR."User Dashboard For - \{user.getUserName()}");
    }

    public void logOutButton(ActionEvent actionEvent) throws IOException {
        Admin.writeAdmin(admin);
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/login.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        LoginController apg = loader.getController();

        apg.start(primaryStage,admin.users,admin);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        root.requestFocus();
    }

    public void searchButton(ActionEvent actionEvent) {
        String target = albumTextField.getText().trim().toLowerCase();
        ArrayList<Album> albums = user.getAlbums();
        ArrayList<Album> filtered = new ArrayList<>();

        if(albums.isEmpty()){
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(primaryStage);
            message.setTitle("Search Error");
            message.setHeaderText("Album Not Found");
            message.setContentText(STR."Cannot search for album \{'"'}\{albumTextField.getText()}\{'"'}, because \{user.getUserName()} has no albums.");
            message.setGraphic(null);
            message.showAndWait();
            return;
        }
        for(Album i : albums){
            if(i.getAlbumName().contains(target)){
                filtered.add(i);
            }
        }
        albumList.setItems(FXCollections.observableArrayList(filtered));
        albumList.getSelectionModel().select(0);
    }

    public void addAlbumButton(ActionEvent actionEvent) {
        // Create a new stage for the input window
        Stage inputStage = new Stage();

        // Create a text field for user input
        TextField inputTextField = new TextField();
        inputTextField.setPromptText("New Album");

        // Create a button to submit the input
        Button submitButton = new Button("Create");
        submitButton.setOnAction(e -> {
            String userInput = inputTextField.getText();
            if(userInput.trim().isEmpty()){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Album Creation Error");
                message.setHeaderText("Invalid Album Name");
                message.setContentText("Enter album name");
                message.setGraphic(null);
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }

            boolean result = user.addAlbum(new Album(userInput));
            if(!result){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Album Creation Error");
                message.setHeaderText("Invalid Album Name");
                message.setContentText("This album name is taken, choose a different name.");
                message.setGraphic(null);
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }
            inputStage.close(); // Close the input window
            albumList.setItems(FXCollections.observableArrayList(user.getAlbums()));
            albumList.getSelectionModel().select(0);
        });

        // Layout for the input window
        VBox inputLayout = new VBox(10);
        inputLayout.setPadding(new Insets(20));
        inputLayout.getChildren().addAll(inputTextField, submitButton);

        // Create the input scene
        Scene inputScene = new Scene(inputLayout, 300, 100);

        // Set the input scene and show the input window
        inputStage.setScene(inputScene);
        inputStage.setTitle("Create Album");
        inputStage.show();
    }

    public void deleteAlbumButton(ActionEvent actionEvent) throws IOException {
        Album album = albumList.getSelectionModel().getSelectedItem();
//        if(album.isStock() && user.getUserName().equals("stock")){
//            Alert message = new Alert(AlertType.INFORMATION);
//            message.initOwner(primaryStage);
//            message.setTitle("Deletion Error");
//            message.setHeaderText("Album Cannot Be Deleted");
//            message.setContentText("This is the stock album, it cannot be deleted.");
//            message.setGraphic(null);
//            message.showAndWait();
//            return;
//        }

        admin.getUser(user.userName).albums.remove(album);
        Admin.writeAdmin(admin);
        albumList.setItems(FXCollections.observableArrayList(user.getAlbums()));
        albumList.getSelectionModel().select(0);
    }

    public void openButton(ActionEvent actionEvent) {
        try{
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/albumView.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            AlbumViewController apg = loader.getController();

            apg.start(primaryStage,user, primaryStage.getScene(),this, albumList.getSelectionModel().getSelectedItem(), admin, false);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            root.requestFocus();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelSearchButton(ActionEvent actionEvent) {
        albumList.setItems(FXCollections.observableArrayList(user.getAlbums()));
        albumList.getSelectionModel().select(0);
    }

    public void renameButton(ActionEvent actionEvent) {
        Album album = albumList.getSelectionModel().getSelectedItem();
        // Create a new stage for the input window
        Stage inputStage = new Stage();

        // Create a text field for user input
        TextField inputTextField = new TextField();
        inputTextField.setPromptText("Enter new name");

        // Create a button to submit the input
        Button submitButton = new Button("Change");
        submitButton.setOnAction(e -> {
            String userInput = inputTextField.getText();
            if(userInput.trim().isEmpty()){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Album Rename Error");
                message.setHeaderText("Invalid Album Name");
                message.setContentText("Enter a name");
                message.setGraphic(null);
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }

//            if(album.getAlbumName().equals("stock") && user.getUserName().equals("stock")){
//                Alert message = new Alert(AlertType.INFORMATION);
//                message.initOwner(primaryStage);
//                message.setTitle("Album Rename Error");
//                message.setHeaderText("Invalid Album Selected");
//                message.setContentText("This is the stock users stock album, it cannot be renamed.");
//                message.setGraphic(null);
//                message.showAndWait();
//                inputStage.toFront();
//                inputTextField.clear();
//                return;
//            }

            boolean result = user.renameAlbum(album, userInput);
            if(!result){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Album Rename Error");
                message.setHeaderText("Invalid Album Name");
                message.setContentText("This album name is taken, choose a different name.");
                message.setGraphic(null);
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }
            inputStage.close(); // Close the input window
            albumList.setItems(FXCollections.observableArrayList(user.getAlbums()));
            albumList.getSelectionModel().select(0);
        });

        // Layout for the input window
        VBox inputLayout = new VBox(10);
        inputLayout.setPadding(new Insets(20));
        inputLayout.getChildren().addAll(inputTextField, submitButton);

        // Create the input scene
        Scene inputScene = new Scene(inputLayout, 300, 100);

        // Set the input scene and show the input window
        inputStage.setScene(inputScene);
        inputStage.setTitle("Change Album Name");
        inputStage.show();
    }

    public void searchPhotosButton(ActionEvent actionEvent) {
        if(user.getAlbums().isEmpty())
        {
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(primaryStage);
            message.setTitle("Search Album");
            message.setHeaderText("Cannot Search Album");
            message.setContentText("There are no albums to Search");
            message.setGraphic(null);
            message.showAndWait();
            return;
        }
        try{
            Stage stageAdd = new Stage();
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/view/searchPhotos.fxml"));
            AnchorPane root = (AnchorPane)load.load();
            searchPhotosController spc= load.getController();

            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/albumView.fxml"));
            AnchorPane root2 = (AnchorPane)loader.load();
            AlbumViewController avc = loader.getController();

            spc.start(primaryStage, stageAdd,avc, user.getAlbums(), admin, user);
            Scene add = new Scene(root);
            stageAdd.setScene(add);
            stageAdd.setTitle("Search Results");
            stageAdd.setResizable(false);
            stageAdd.initModality(Modality.WINDOW_MODAL);
            stageAdd.initOwner(primaryStage);
            root.requestFocus();
            primaryStage.setResizable(false);
            stageAdd.showAndWait();
            primaryStage.setResizable(true);

        }catch(Exception ee){
            ee.printStackTrace();
        }
    }

    public void quitApp(ActionEvent actionEvent) throws IOException {
        Admin.writeAdmin(admin);
        Platform.exit();
    }
}
