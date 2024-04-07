package controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Admin;
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
//        for(int i = 0; i < users.size(); i++){
//            users.get(i).setUserImage();
//        }
//        for(int i = 0; i < users.size(); i++){
//            tilePane.getChildren().add(users.get(i).getLabel());
//        }
        primaryStage.setResizable(true);
    }

    @FXML
    public void handleLogoutButton(ActionEvent e) throws IOException {
        primaryStage.setScene(prev); // Go back to the previous scene
    }

    @FXML
    public void handleAddButton(ActionEvent e) {
        String username = userName.getText();
        if (username.isEmpty()) {
            showAlert("Error", "User creation error", "Username cannot be empty.");
            return;
        }
        if (admin.addUser(username)) {
            try {
                Admin.writeAdmin(admin); // Persist changes
                updateUsersDisplay(); // Update UI
            } catch (IOException ioException) {
                showAlert("Error", "Persistence Error", "Could not save the admin data.");
            }
        } else {
            showAlert("Error", "User Creation Error", "A user with this name already exists.");
        }
    }

    @FXML
    public void handleDeleteUserButton(ActionEvent actionEvent) {
        String username = userName.getText();
        if (admin.removeUser(username)) {
            try {
                Admin.writeAdmin(admin); // Persist changes
                updateUsersDisplay(); // Update UI
            } catch (IOException ioException) {
                showAlert("Error", "Persistence Error", "Could not save the admin data.");
            }
        } else {
            showAlert("Error", "User Deletion Error", "User not found.");
        }
    }


    }

    @FXML
    public void handleListUsersButton(ActionEvent actionEvent) {
    updateUsersDisplay();

    }

    @FXML
    public void handleConfirmButton(ActionEvent actionEvent) {

    }

    @FXML
    public void handleCancelButton(ActionEvent actionEvent) {

    }

    public void handleLogoutButton(ActionEvent actionEvent) {
    }
}
