package model;

import java.io.File;
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
            User stock = new User("stock");
            Album stockAlbum = new Album("stock");
            addStockPhotos(stockAlbum);
            stock.addAlbum(stockAlbum);
            admin.getUsers().add(stock);
            admin.getUsers().add(new User("testUser"));
        }

    }

    public void addStockPhotos(Album album){
        album.addPhoto(new File("Data/StockPhotos/caspar-camille-rubin-89xuP-XmyrA-unsplash.jpg"), true);
        album.addPhoto(new File("Data/StockPhotos/muhammad-awan-Jwby0ysbCV4-unsplash.jpg"), true);
        album.addPhoto(new File("Data/StockPhotos/rafay-ansari-4dzcgZxGYQA-unsplash.jpg"), true);
        album.addPhoto(new File("Data/StockPhotos/sujith-r-xWw4BJmfB5Y-unsplash.jpg"), true);
        album.addPhoto(new File("Data/StockPhotos/tim-schmidbauer-TE_lOmkKq04-unsplash.jpg"), true);

        for(Photo i : album.photos){
            i.caption = "stock photo" + i;
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

    public static void main(String[] args) {
        launch(args);
    }
}

