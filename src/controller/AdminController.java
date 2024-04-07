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
    public void handleCancelButton(ActionEvent e) throws IOException {
        primaryStage.setScene(prev); // Go back to the previous scene
    }

    @FXML
    public void handleAddButton(ActionEvent e) {
        String username = userField.getText().trim();

        if (username.isEmpty()) {
            showError("Username cannot be empty.");
            return;
        }

        try {
            Admin admin = Admin.readAdmin(); // Load the admin object

            if (admin.addUser(username)) {
                Admin.writeAdmin(admin); // Save the updated admin object
                showMessage("User added successfully.");
            } else {
                showError("Username is already used.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            showError("An error occurred while processing: " + ex.getMessage());
        }
    }

    @FXML
    public void handleDeleteUserButton(ActionEvent actionEvent) {
        String username = userField.getText().trim();
        if (admin.removeUser(username)) {
            try {
                Admin.writeAdmin(admin); // Persist changes
                updateUsersDisplay(); // Refresh the displayed list of users
                showMessage("User deleted successfully.");
            } catch (IOException ioException) {
                showError("Persistence Error: Could not save the admin data.");
            }
        } else {
            showError("User Deletion Error: User not found.");
        }
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
}