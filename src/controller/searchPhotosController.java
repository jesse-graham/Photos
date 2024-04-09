package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class searchPhotosController {

    @FXML DatePicker from;

    @FXML DatePicker to;

    @FXML ComboBox<Tag> tagType;

    @FXML ComboBox<String> tagValue;

    @FXML TextArea tagArea;

    private Stage localStage;

    User currentUser;

    ArrayList<Album> album;

    Stage primaryStage;

    ArrayList<Tag> searchTag;

    private Admin admin;

    AlbumViewController avc;


    public void start(Stage primaryStage, Stage localStage, AlbumViewController avc, ArrayList<Album> a, Admin admin, User currentUser) {
        this.localStage=localStage;
        this.avc=avc;
        this.album=a;
        this.admin = admin;
        this.primaryStage = primaryStage;
        this.currentUser = currentUser;
        ArrayList<Tag> treeTags = new ArrayList<Tag>();
        searchTag = new ArrayList<Tag>();

        for (Album value : this.album) {
            ArrayList<Photo> tempPhotos = value.getPhotos();
            for (Photo tempPhoto : tempPhotos) {
                ArrayList<Tag> tags = tempPhoto.getTags();
                for (Tag tag : tags) {
                    boolean foundTag = false;
                    for (Tag treeTag : treeTags) {
                        if (treeTag.getTagName().equalsIgnoreCase(tag.getTagName())) {
                            foundTag = true;
                        }
                    }
                    if (!foundTag) {
                        Tag nTag = new Tag(tag.getTagName());
                        nTag.getTagValues().addAll(tag.getTagValues());
                        treeTags.add(nTag);
                    } else {
                        ArrayList<String> val = tag.getTagValues();
                        for (String s : val) {
                            int index = indexTag(tag.getTagName(), treeTags);
                            if (index == -1) {
                                continue;
                            }
                            boolean found = false;
                            for (int k = 0; k < treeTags.get(index).getTagValues().size(); k++) {
                                if (treeTags.get(index).getTagValues().get(k).equalsIgnoreCase(s)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                treeTags.get(index).getTagValues().add(s);
                            }
                        }
                    }
                }
            }
        }


        tagType.setItems(FXCollections.observableArrayList(treeTags));
        tagType.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->selectVal());
        tagValue.setVisible(false);
    }

    public int indexTag(String type, ArrayList<Tag> tags){
        for(int i = 0; i < tags.size(); i ++){
            if(tags.get(i).getTagName().equalsIgnoreCase(type)){
                return i;
            }
        }
        return -1;
    }

    public void selectVal()
    {
        if(tagType.getSelectionModel().getSelectedItem()!=null) {
            tagValue.setVisible(true);
            tagValue.setItems(FXCollections.observableArrayList(tagType.getSelectionModel().getSelectedItem().getTagValues()));
        }
    }

    public void addTag(ActionEvent e){

        if(tagType.getSelectionModel().getSelectedItem()==null)
        {
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(localStage);
            message.setTitle("Search");
            message.setHeaderText("Can't Add Tag");
            message.setContentText("Must Select A Tag Type");
            message.setGraphic(null);
            message.showAndWait();
            tagType.getSelectionModel().clearSelection();
            tagValue.getSelectionModel().clearSelection();

            return;
        }

        if(tagType.getSelectionModel().getSelectedItem()==null || tagValue.getSelectionModel().getSelectedItem()==null)
        {
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(localStage);
            message.setTitle("Search");
            message.setHeaderText("Cannot Add Tag");
            message.setContentText("Must Select A Type And Value For The Tag");
            message.setGraphic(null);
            message.showAndWait();
            tagType.getSelectionModel().clearSelection();
            tagValue.getSelectionModel().clearSelection();
            return;
        }

        Tag temp= tagType.getSelectionModel().getSelectedItem();
        String s=tagValue.getSelectionModel().getSelectedItem();
        Boolean add = true;;
        int index = -1;
        for(int j = 0; j < searchTag.size(); j++){
            if(searchTag.get(j).getTagName().equalsIgnoreCase(temp.getTagName())){
                add = false;
                index = j;
                break;
            }
        }
        if(!add)
        {

            boolean found = false;
            for(int i = 0; i < searchTag.get(index).getTagValues().size(); i ++){
                if(searchTag.get(index).getTagValues().get(i).equalsIgnoreCase(s))
                {
                    found = true;
                    break;
                }
            }
            if(!found){
                searchTag.get(index).addTag(s);
                String out = tagArea.getText();
                out = out+temp.getTagName()+" : "+ s+"\n";
                tagArea.setText(out);
            }

        }
        else
        {
            Tag temp2= new Tag(temp.getTagName());
            temp2.addTag(s);
            searchTag.add(temp2);
            String out = tagArea.getText();
            out = STR."\{out}\{temp.getTagName()} : \{s}\n";
            tagArea.setText(out);
        }

        tagType.getSelectionModel().clearSelection();
        tagValue.getSelectionModel().clearSelection();
    }

    public void searchOK(ActionEvent e) throws ParseException {

        Boolean hasTags = false;
        ArrayList<Photo> output = new ArrayList<Photo>();

        if((from.getValue()!=null && to.getValue()==null) || (from.getValue()==null && to.getValue()!=null))
        {
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(localStage);
            message.setTitle("Search");
            message.setHeaderText("Can't Search");
            message.setContentText("Must Enter Both A From And And To Date.");
            message.setGraphic(null);
            message.showAndWait();
            return;
        }

        if(!searchTag.isEmpty())
        {
            ArrayList<Photo> tag = new ArrayList<Photo>();
            Album currAlbum;
            Photo currPhoto;

            for (Album value : album) {
                currAlbum = value;
                for (int y = 0; y < value.getPhotos().size(); y++) {
                    currPhoto = currAlbum.getPhotos().get(y);
                    for (int z = 0; z < currPhoto.getTags().size(); z++) {
                        for (Tag item : searchTag) {
                            if (currPhoto.getTags().get(z).getTagName().equalsIgnoreCase(item.getTagName())) {
                                for (int b = 0; b < item.getTagValues().size(); b++) {
                                    if (currPhoto.getTags().get(z).getTagValues().contains(item.getTagValues().get(b))) {
                                        if (!tag.contains(currPhoto)) {
                                            tag.add(currPhoto);
                                            hasTags = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            output.addAll(tag);
        }
        if((from.getValue()!=null && to.getValue()!=null))
        {
            Calendar fd = Calendar.getInstance();
            fd.set(from.getValue().getYear(), from.getValue().getMonthValue()-1, from.getValue().getDayOfMonth());
            fd.set(Calendar.MILLISECOND, 0);

            Calendar td = Calendar.getInstance();
            td.set(to.getValue().getYear(), to.getValue().getMonthValue()-1, to.getValue().getDayOfMonth());
            td.set(Calendar.MILLISECOND, 0);
            if(td.before(fd)){
                Alert message = new Alert(AlertType.INFORMATION);
                message.initOwner(localStage);
                message.setTitle("Search");
                message.setHeaderText("Can't Search");
                message.setContentText("From Date must be before on the to To Date.");
                message.setGraphic(null);
                message.showAndWait();
                return;
            }
            ArrayList<Photo> date = new ArrayList<Photo>();
            Album currAlbum;
            Photo currPhoto;

            for (Album value : album) {
                currAlbum = value;
                for (int y = 0; y < value.getPhotos().size(); y++) {
                    currPhoto = currAlbum.getPhotos().get(y);
                    if (((fd.before(currPhoto.getCal())) || isSameDay(currPhoto, fd)) && (td.after(currPhoto.getCal()) || isSameDay(currPhoto, td))) {
                        if (!date.contains(currPhoto)) {
                            date.add(currPhoto);
                        }

                    }
                }
            }
            ArrayList<Photo> both = new ArrayList<Photo>();
            if(!hasTags && !date.isEmpty()){
                output.addAll(date);
            }
            else if(hasTags && !date.isEmpty())
            {

                for (Photo photo : date) {
                    if (output.contains(photo)) {
                        both.add(photo);
                    }
                }
                output = both;
            }
            else if(hasTags && date.isEmpty()){
                output.clear();
            }
        }
        if(output.isEmpty()){
            Alert message = new Alert(AlertType.INFORMATION);
            message.initOwner(localStage);
            message.setTitle("Search");
            message.setHeaderText("Cannot Search");
            message.setContentText("No Photos Found Matching Your Search");
            message.setGraphic(null);
            message.showAndWait();
            return;
        }
        try{

            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/view/albumView.fxml"));
            AnchorPane root = (AnchorPane)load.load();
            AlbumViewController avc = load.getController();
            Album aa = new Album("search album");
            aa.getPhotos().addAll(output);

            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("/view/user.fxml"));
            AnchorPane root2 = (AnchorPane)loader2.load();
            UserController apg2 = loader2.getController();
            Scene userScene = new Scene(root2);

            Album search = new Album("search");
            search.photos.addAll(output);
            avc.start(primaryStage, currentUser, userScene, apg2, search, admin,true);
            Scene scene = new Scene(root);
            double w = avc.primaryStage.getWidth();
            double h = avc.primaryStage.getHeight();
            avc.primaryStage.setScene(scene);
            avc.primaryStage.setWidth(w);
            avc.primaryStage.setHeight(h);
            localStage.close();
            root.requestFocus();

        }catch(Exception ee){
            ee.printStackTrace();
        }



        localStage.close();
    }

    public Boolean isSameDay(Photo currPhoto, Calendar day)
    {
        String date = new SimpleDateFormat("MM/dd/yyyy").format(day.getTime());
        if(currPhoto.getDate().equalsIgnoreCase(date))
        {
            return true;
        }
        return false;
    }

    public void searchCancel(ActionEvent e){
        localStage.close();
    }


    public void quitApp(ActionEvent actionEvent) throws IOException {
        Admin.writeAdmin(admin);
        Platform.exit();
    }
}