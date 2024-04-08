package controller;

import java.util.ArrayList;
import java.util.Calendar;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.Admin;

public class LoginController {
    @FXML
    private Button LoginButton;

    @FXML
    private TextField UserNameField;

    ArrayList<User> users;

    Stage primaryStage;

    Admin admin;

    Scene loginScene;

    public void start(Stage primaryStage, ArrayList<User> user, Admin admin) {
        this.primaryStage = primaryStage;
        this.users = user;
        this.admin = admin;

    }

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



}
