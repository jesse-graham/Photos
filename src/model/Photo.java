package model;

import java.io.File;
import java.io.Serial;
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
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class Photo implements Serializable{
    /*
     * serialVersionUID - The constant serial number.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /*
     * photoPath - The file path to the photo.
     */
    String photoPath;

    /*
     * date - The date of the photo.
     */
    Calendar date;

    /*
     * caption - The photo's caption.
     */
    String caption;

    /*
     * tags - The list of tags corresponding to the photo.
     */
    ArrayList<Tag> tags;

    /*
     * isStock - Variable containing weather or not the photo is a stock one.
     */
    boolean isStock = false;

    /*
     * label - The photo's label.
     */
    transient Label label;

    /*
     * image - The image.
     */
    transient Image image;

    /*
     * imageView - The imageView, which holds the image.
     */
    transient ImageView imageView;

    /*
     * Album - creates a photo object.
     * @param file
     * @param album
     * @param isStock
     */
    public Photo(File file, Album album, boolean isStock){
        caption = "";
        photoPath = file.getPath();
        label = new Label();
        label.setStyle("-fx-text-fill: black;");
        tags = new ArrayList<>();
        date = Calendar.getInstance();
        date.setTimeInMillis(file.lastModified());
        date.set(Calendar.MILLISECOND, 0);
        album.numPhotos++;
        setPhotoThumbnail();
        this.isStock = isStock;
    }

    /*
     * getCal - gets the date.
     */
    public Calendar getCal(){
        return date;
    }

    /*
     * getCaption - gets the caption of the photo.
     */
    public String getCaption(){
        return caption;
    }

    /*
     * setCaption - sets the caption of the photo to the provided string.
     * @param string
     */
    public void setCaption(String string){
        caption = string;
    }

    /*
     * getPath - gets the path of the photo.
     */
    public String getPath(){
        return photoPath;
    }

    /*
     * setPath - sets the path of the photo to the provided string.
     * @param path
     */
    public void setPath(String path){
        photoPath = path;
    }

    /*
     * getTags - gets the list of tags.
     */
    public ArrayList<Tag> getTags(){
        return tags;
    }

    /*
     * setTags - sets the tags to provided tag.
     * @param t
     */
    public void setTags(ArrayList<Tag> t){
        this.tags = t;
    }

    /*
     * getLabel - gets the photo's label.
     */
    public Label getLabel(){
        return label;
    }

    /*
     * getImage - gets the photo's image.
     */
    public Image getImage(){
        return image;
    }

    /*
     * setPhotoThumbnail - sets up the label so that it contains the image and the caption corresponding to the photo.
     */
    public void setPhotoThumbnail(){
        image = new Image(STR."file:\{this.photoPath}");
        imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(100);
        imageView.setId(STR."\{this.caption} ");
        if(label == null){
            label = new Label();
        }
        label.setText(imageView.getId());
        label.setGraphic(imageView);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setContentDisplay(ContentDisplay.TOP);
        label.setId(this.photoPath);

        label.setPadding(new Insets(5,6,5,6));
        label.setMaxSize(label.getWidth(), label.getHeight());
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
    }

    /*
     * getDate - gets the date of the photo.
     */
    public String getDate(){
        if(date == null){
            return "";
        }
        return new SimpleDateFormat("MM/dd/yyyy").format(date.getTime());
    }

    /*
     * getTime - gets the time of the photo.
     */
    public String getTime(){
        if(date == null){
            return "";
        }
        return new SimpleDateFormat("hh:mm:ss aa").format(date.getTime());
    }

    /*
     * inStock - returns weather or not the photo is stock.
     * @param userName
     */
    public boolean isStock(){
        return isStock;
    }
}