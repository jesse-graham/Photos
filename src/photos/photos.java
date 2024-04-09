package photos;

import java.io.File;
import java.util.ArrayList;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class photos extends Application {

    Admin admin;

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
        }
    }

    public void addStockPhotos(Album album){
        album.addPhoto(new File("Data/StockPhotos/caspar-camille-rubin-89xuP-XmyrA-unsplash.jpg"), true);
        album.addPhoto(new File("Data/StockPhotos/muhammad-awan-Jwby0ysbCV4-unsplash.jpg"), true);
        album.addPhoto(new File("Data/StockPhotos/rafay-ansari-4dzcgZxGYQA-unsplash.jpg"), true);
        album.addPhoto(new File("Data/StockPhotos/sujith-r-xWw4BJmfB5Y-unsplash.jpg"), true);
        album.addPhoto(new File("Data/StockPhotos/tim-schmidbauer-TE_lOmkKq04-unsplash.jpg"), true);

        int c = 1;
        for(Photo i : album.photos){
            i.setCaption(STR."stock photo: \{c}");
            c++;
        }
        album.setStock();
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

