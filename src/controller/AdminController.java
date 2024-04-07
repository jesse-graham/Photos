package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.User;

import javax.swing.*;

public class AdminController{


    @FXML
    TextField userName;

    @FXML
    Button confirmButton, createUserButton, deleteUserButton, logOutButton, listUsersButton, cancelButton;

    @FXML
    TilePane tilePane;
    /***
     * Scrollpane - Scrolling Pane.
     */
    @FXML
    ScrollPane scrollPane;

    @FXML
    ListView<User> userList;

    @FXML
    TextField userField;
    /**
     * Admin  - Admin Object
     */
    Admin admin;
    /**
     * Primary Stage - primaryStage Stage of the previous scene
     */
    Stage primaryStage;
    /**
     * Arraylist of all the users
     */
    public static ArrayList<User> users;
    /**
     * Link to the previous scene
     */
    Scene prev;
    /**
     * Login Pane Controller
     */
    LoginController lpg;




    /***
     * Selected -  Selected Album Name.
     */
    public static String selected;
    /***
     * isSelected  - Tells you what is selected.
     */
    public static Boolean isSelected = false;

    public void start(Stage primaryStage, ArrayList<User> user, Scene prev, LoginController lpg, Admin admin) {
        this.primaryStage = primaryStage;
        AdminController.users = user;
        this.prev = prev;
        this.lpg = lpg;
        selected = null;
        isSelected = false;
        this.admin = admin;
        userList.setItems(FXCollections.observableArrayList(users));
        userList.getSelectionModel().select(0);
        primaryStage.setResizable(true);
    }


    @FXML
    public void handleLogoutButton(ActionEvent e) throws IOException {
        primaryStage.setScene(prev); // Go back to the previous scene
    }
    public void handleCancelButton(ActionEvent e) throws IOException {
        primaryStage.setScene(prev); // Go back to the previous scene
    }

    @FXML
    public void handleAddButton(ActionEvent e) {
        // Create a new stage for the input window
        Stage inputStage = new Stage();

        // Create a text field for user input
        TextField inputTextField = new TextField();
        inputTextField.setPromptText("New User");

        // Create a button to submit the input
        Button submitButton = new Button("Create");
        submitButton.setOnAction(a -> {
            String userInput = inputTextField.getText();
            if(userInput.trim().equals("")){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("User Creation Error");
                message.setHeaderText("Invalid Username");
                message.setContentText("Enter a username.");
                message.setGraphic(null);
                message.getDialogPane().getStylesheets().add("/view/loginPane.css");
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }

            boolean result = admin.addUser(userInput);
            if(!result){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("User Creation Error");
                message.setHeaderText("Invalid Username");
                message.setContentText("This username is taken, choose a different name.");
                message.setGraphic(null);
                message.getDialogPane().getStylesheets().add("/view/loginPane.css");
                message.showAndWait();
                inputStage.toFront();
                inputTextField.clear();
                return;
            }
            inputStage.close(); // Close the input window
            try {
                Admin.writeAdmin(admin);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            userList.setItems(FXCollections.observableArrayList(admin.getUsers()));
            userList.getSelectionModel().select(0);
        });

        // Layout for the input window
        VBox inputLayout = new VBox(10);
        inputLayout.setPadding(new Insets(20));
        inputLayout.getChildren().addAll(inputTextField, submitButton);

        // Create the input scene
        Scene inputScene = new Scene(inputLayout, 300, 100);

        // Set the input scene and show the input window
        inputStage.setScene(inputScene);
        inputStage.setTitle("Create User");
        inputStage.show();
    }

    @FXML
    public void handleDeleteUserButton(ActionEvent actionEvent) throws IOException {
        User user = userList.getSelectionModel().getSelectedItem();
        if(user.getUserName().equals("stock") && user.getUserName().equals("stock")){
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(primaryStage);
            message.setTitle("Deletion Error");
            message.setHeaderText("User Cannot Be Deleted");
            message.setContentText("This is the stock user, it cannot be deleted.");
            message.setGraphic(null);
            message.getDialogPane().getStylesheets().add("/view/loginPane.css");
            message.showAndWait();
            return;
        }

        deleteUser(user);
        Admin.writeAdmin(admin);
        userList.setItems(FXCollections.observableArrayList(users));
        userList.getSelectionModel().select(0);
    }

    @FXML
    public void handleListUsersButton(ActionEvent actionEvent) {
        updateUsersDisplay();
    }

    private void updateUsersDisplay() {
        tilePane.getChildren().clear(); // Clear the tile pane before adding new elements
        for (User user : admin.getUsers()) {
            Label userLabel = new Label(user.getUserName());
            tilePane.getChildren().add(userLabel);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void handleConfirmButton(ActionEvent actionEvent) {
        String target = userField.getText().trim().toLowerCase();
        ArrayList<User> filtered = new ArrayList<>();

        if(users.isEmpty()){
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(primaryStage);
            message.setTitle("Search Error");
            message.setHeaderText("User Not Found");
            message.setContentText(STR."Cannot search for user: " + userField.getText() + ", because they do not exist.");
            message.setGraphic(null);
            message.getDialogPane().getStylesheets().add("/view/loginPane.css");
            message.showAndWait();
            return;
        }
        for(User i : users){
            if(i.userName.contains(target)){
                filtered.add(i);
            }
        }
        userList.setItems(FXCollections.observableArrayList(filtered));
        userList.getSelectionModel().select(0);
    }
    public void deleteUser(User user){
        users.remove(user);
    }

    public void cancelSearchButton(ActionEvent actionEvent) {
        userList.setItems(FXCollections.observableArrayList(users));
        userList.getSelectionModel().select(0);
    }
}