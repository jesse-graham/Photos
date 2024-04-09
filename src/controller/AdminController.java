package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;
import model.User;

/** Admin controller controls the admin fxml file and implements various functions relating to admin
 * @author Jesse Graham | Arsal Shaikh
 * */

public class AdminController{
    /*

    confirmButton - confirms users input to apply
    createUserButton - allows user to create additional user
    deleteUserButton - allows user to delete another user
    logOutButton - allows user to log out of application
    */
    @FXML
    Button confirmButton, createUserButton, deleteUserButton, logOutButton;

    /*

    userList - contains list of users
    */

    @FXML
    ListView<User> userList;

    /*

    userField - space to input username
    */

    @FXML
    TextField userField;

    /*

   admin - contains admin
    */


    Admin admin;
    /*

    primaryStage - prompted space for function
    */

    Stage primaryStage;
    /*

    users - references list of users
    */


    public static ArrayList<User> users;

    /*

    prev - prompts previous stage
    */

    Scene prev;

    /*

    lpg - controllers login
    */

    LoginController lpg;

    /*

    selected - points to what was selected
    */

    public static String selected;

    /*

    isSelected - boolean value to note selected or not
    */

    public static Boolean isSelected = false;

    /**

     Start initializes the album pane controllers
     @param primaryStage Stage of the previous scene
     @param user list of users
     @param prev prev scene
     @param lpg login pane controller
     @param admin - admin object
     */

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
    /**

     Logs user out of application
     @param e takes mouse click event

     */


    @FXML
    public void handleLogoutButton(ActionEvent e) throws IOException {
        Admin.writeAdmin(admin);
        primaryStage.setScene(prev); // Go back to the previous scene
    }
    /**

     prompts user for additional user
     @param e takes mouse click event

     */

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
            if(userInput.trim().isEmpty()){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("User Creation Error");
                message.setHeaderText("Invalid Username");
                message.setContentText("Enter a username.");
                message.setGraphic(null);
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
    /**

     allows user to delete previously created user
     @param actionEvent takes mouse click event
     */

    @FXML
    public void handleDeleteUserButton(ActionEvent actionEvent) throws IOException {
        User user = userList.getSelectionModel().getSelectedItem();
//        if(user.getUserName().equals("stock") && user.getUserName().equals("stock")){
//            Alert message = new Alert(AlertType.INFORMATION);
//            message.initOwner(primaryStage);
//            message.setTitle("Deletion Error");
//            message.setHeaderText("User Cannot Be Deleted");
//            message.setContentText("This is the stock user, it cannot be deleted.");
//            message.setGraphic(null);
//            message.showAndWait();
//            return;
//        }

        deleteUser(user);
        Admin.writeAdmin(admin);
        userList.setItems(FXCollections.observableArrayList(users));
        userList.getSelectionModel().select(0);
    }
    /**

     allows users newly created username for new user
     @param actionEvent takes  mouse click event

     */

    public void handleConfirmButton(ActionEvent actionEvent) {
        String target = userField.getText().trim().toLowerCase();
        ArrayList<User> filtered = new ArrayList<>();

        if(users.isEmpty()){
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(primaryStage);
            message.setTitle("Search Error");
            message.setHeaderText("User Not Found");
            message.setContentText(STR."Cannot search for user: \{userField.getText()}, because they do not exist.");
            message.setGraphic(null);
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
    /**

     removes user from saved users

     @param user to remove passed in user

     */
    public void deleteUser(User user){
        users.remove(user);
    }
    /**

     Cancels users search

     @param actionEvent takes action from
     */

    public void cancelSearchButton(ActionEvent actionEvent) {
        userList.setItems(FXCollections.observableArrayList(users));
        userList.getSelectionModel().select(0);
    }
    /**

     initiates user to quit from app

     @param actionEvent - receives mouse click event
     */

    public void quitApp(ActionEvent actionEvent) throws IOException {
        Admin.writeAdmin(admin);
        Platform.exit();
    }
}