package controller;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.Admin;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class LoginController {
    /*
     * UserNameField - The username text field.
     */
    @FXML
    private TextField UserNameField;

    /*
     * users - The list of users.
     */
    ArrayList<User> users;

    /*
     * primaryStage - The primary stage.
     */
    Stage primaryStage;

    /*
     * admin - The admin object.
     */
    Admin admin;

    /*
     * loginScene - The login scene.
     */
    Scene loginScene;

    /*
     * Initializes the Login Controller
     * @param primaryStage
     * @param users
     * @param admin
     */
    public void start(Stage primaryStage, ArrayList<User> user, Admin admin) {
        this.primaryStage = primaryStage;
        this.users = user;
        this.admin = admin;

    }

    /*
     * login - logs in either the user or the admin.
     * @param e
     */
    public void login(ActionEvent e) {
       loginScene = primaryStage.getScene();
        String s = UserNameField.getText().trim().toLowerCase();
        String admin = "admin";
        if(s.equalsIgnoreCase(admin)){
            UserNameField.clear();
            adminLogin();
        } else{
            boolean found = false;
            for(int i = 0; i < users.size(); i++){
                if(users.get(i).userName.equalsIgnoreCase(s)){
                    UserNameField.clear();
                    found = true;
                    userLogin(s, users.get(i).getAlbums(), users.get(i), i);
                    break;
                }
            }

            if(!found){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(primaryStage);
                message.setTitle("Login Error");
                message.setHeaderText("Cannot Login");
                message.setContentText("Username is incorrect, enter a valid username.");
                message.setGraphic(null);
                message.showAndWait();
            }
        }
    }

    /*
     * adminLogin - logs in as an admin.
     */
    public void adminLogin() {
        try{
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/admin.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            AdminController apg = loader.getController();
            apg.start(primaryStage,users, loginScene,this, admin);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            root.requestFocus();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * indexTag - logs in as the provided user.
     */
    public void userLogin(String name, ArrayList<Album> album, User user, int index){
        try{
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/user.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            UserController apg = loader.getController();

            apg.start(primaryStage,users, loginScene,this, admin, user);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            root.requestFocus();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * indexTag - logs the user out and quits the application, saving user data.
     * @param actionEvent
     */
    public void quitApp(ActionEvent actionEvent) throws IOException {
        Platform.exit();
    }
}
