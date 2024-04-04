package model;

import java.util.ArrayList;

import controller.LoginController;
import javafx.application.Application;
import controller.AdminController;
import controller.LoginController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 * Photo Album Class - defines attributes of the Photo Album
 */
public class PhotoAlbumApplication extends Application {

    Admin admin;
    /** The admin obj. */
    public void init() throws Exception{
        try{
            admin = Admin.readAdmin();

        }
        catch(Exception e){
            admin = new Admin();
        }

    }
    public void start(Stage mainStage) {
        try {
            if(admin.users == null){
                admin.users = new ArrayList<User>();
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login.fxml"));
            Pane root = (Pane) loader.load();
            Scene scene = new Scene(root);

            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.setTitle("Photo Album");
            mainStage.show();

            LoginController controller = loader.<LoginController>getController();
            controller.start(mainStage, admin.users, admin);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * main method
     *
     * @param args
     *            command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

