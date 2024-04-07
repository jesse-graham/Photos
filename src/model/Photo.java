package model;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import controller.AlbumViewController;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ContentDisplay;
import javafx.geometry.Insets;
import javafx.scene.control.OverrunStyle;

public class Photo implements Serializable{

    String photoPath;

    Calendar date;

    String caption;

    ArrayList<Tag> tags;

    boolean isStock;

    transient Label label;

    transient Image image;

    transient ImageView imageView;


    public Photo(File file, Album album, boolean isStock){
        caption = "";
        photoPath = file.getPath();
        label = new Label();
        tags = new ArrayList<>();
        date = Calendar.getInstance();
        date.setTimeInMillis(file.lastModified());
        date.set(Calendar.MILLISECOND, 0);
        album.numPhotos++;
        setPhotoThumbnail();
        this.isStock = isStock;
    }

    public Calendar getCal(){
        return date;
    }

    public String getCaption(){
        return caption;
    }

    public void setCaption(String string){
        caption = string;
    }

    public String getPath(){
        return photoPath;
    }

    public void setPath(String path){
        photoPath = path;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }

    public void setTags(ArrayList<Tag> t){
        this.tags = t;
    }

    public Label getLabel(){
        return label;
    }

    public void setPhotoThumbnail(){
        image = new Image("file:"+this.photoPath);
        imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(100);
        imageView.setId(this.caption + " ");
        if(label == null){
            label = new Label();
        }
        label.setText(imageView.getId());
        label.setGraphic(imageView);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setContentDisplay(ContentDisplay.TOP);
        label.setId(this.photoPath);
        label.setStyle("-fx-text-fill: white");

        label.setPadding(new Insets(5,6,5,6));
        label.setMaxSize(label.getWidth(), label.getHeight());
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setOnMouseClicked(e -> selectImage());
    }

    public String getDate(){
        if(date == null){
            return "";
        }
        return new SimpleDateFormat("MM/dd/yyyy").format(date.getTime());
    }

    public String getTime(){
        if(date == null){
            return "";
        }
        return new SimpleDateFormat("hh:mm:ss aa").format(date.getTime());
    }

    public void selectImage(){
        for(int i = 0; i < AlbumViewController.photos.size(); i++){
            if(AlbumViewController.photos.get(i).label.getStylesheets().size() == 2){
                AlbumViewController.photos.get(i).label.getStylesheets().remove(1);
                if(AlbumViewController.photos.get(i).photoPath.equalsIgnoreCase(this.photoPath)){
                    AlbumViewController.photos.get(i).label.getStylesheets().add(getClass().getResource("/view/border.css").toExternalForm());
                }
                else{
                    AlbumViewController.photos.get(i).label.getStylesheets().add(getClass().getResource("/view/emptyBorder.css").toExternalForm());
                }
            }
        }
        AlbumViewController.selected = this.photoPath;
        AlbumViewController.isSelected= true;

    }
}