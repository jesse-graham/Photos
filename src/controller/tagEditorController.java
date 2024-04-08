package controller;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.Photo;
import model.Tag;

/**
 * tag Controller controls the tag dialog FXML file and implements all the functionalities dealing with tag.
 * @author Daivik Sheth | Rohan Patel
 *
 * */
public class tagEditorController {

    /** The tag type. */
    @FXML TextField tagType;

    /** The tag value. */
    @FXML TextField tagValue;

    /** The tree view. */
    @FXML TreeView<String>  treeView;

    /** The local stage. */
    private Stage localStage;

    /** The photo obj. */
    Photo photo;

    /** The input. */
    ArrayList<String> input;

    /** The temp tags */
    ArrayList<Tag> temp;

    /**
     * Start initializes the controller.
     *
     * @param localStage the previous scene
     * @param photo the photo object
     */
    public void start(Stage localStage,  Photo photo) {
        this.localStage = localStage;
        this.photo = photo;
        input = new ArrayList<String>();
        temp = photo.getTags();
        TreeItem<String> dumb = new TreeItem<String>("dumb");
        dumb.setExpanded(true);
        treeView.setRoot(dumb);
        treeView.setShowRoot(false);
        temp = new ArrayList<Tag>();
        for(int i = 0; i < photo.getTags().size(); i++){
            Tag t = new Tag(photo.getTags().get(i).toString());
            for(int j = 0; j< photo.getTags().get(i).getTagValues().size(); j++){
                t.addTagValue(photo.getTags().get(i).getTagValues().get(j));
            }
            temp.add(t);
        }

        for(int i = 0; i < temp.size(); i++){
            TreeItem<String> tagT = new TreeItem<String>(temp.get(i).toString());
            for(int j = 0; j < temp.get(i).getTagValues().size(); j++){
                TreeItem<String> tagV = new TreeItem<String>(temp.get(i).getTagValues().get(j));
                tagT.getChildren().add(tagV);
            }
            dumb.getChildren().add(tagT);
        }
        ContextMenu menu = new ContextMenu();
        MenuItem del = new MenuItem("Delete");
        menu.getItems().add(del);
        del.setOnAction(e -> delete());
        treeView.setContextMenu(menu);


    }

    public void delete(){
        if(treeView.getSelectionModel().getSelectedItem() == null){
            return;
        }
        String s = treeView.getSelectionModel().getSelectedItem().getValue();
        for(int i = 0; i < temp.size(); i ++){
            if(temp.get(i).getTagName().equalsIgnoreCase(s)){
                temp.remove(i);
                treeView.getRoot().getChildren().remove(treeView.getSelectionModel().getSelectedItem());
                return;
            }
        }
        boolean del = false;
        String parent = treeView.getSelectionModel().getSelectedItem().getParent().getValue();
        for(int i = 0; i < temp.size(); i ++){
            if(temp.get(i).getTagName().equalsIgnoreCase(parent)){
                for(int j = 0; j < temp.get(i).getTagValues().size(); j++){
                    if(temp.get(i).getTagValues().get(j).equalsIgnoreCase(s)){
                        temp.get(i).getTagValues().remove(j);
                        del = true;
                    }
                }
            }
        }
        if(del){
            int index = treeView.getRoot().getChildren().indexOf(treeView.getSelectionModel().getSelectedItem().getParent());
            for(int i = 0; i < treeView.getRoot().getChildren().get(index).getChildren().size(); i ++){
                if(treeView.getRoot().getChildren().get(index).getChildren().get(i).getValue().equalsIgnoreCase(s)){
                    treeView.getRoot().getChildren().get(index).getChildren().remove(i);
                    return;
                }
            }
        }

    }

    public void add(ActionEvent e){
        treeView.getSelectionModel().clearSelection();
        if(tagType.getText().trim().isEmpty() || tagValue.getText().trim().isEmpty()){
            Alert message2 = new Alert(AlertType.INFORMATION);
            message2.initOwner(localStage);
            message2.setTitle("Tag Photo");
            message2.setHeaderText("Cannot Add Tag.");
            message2.setContentText("You must enter both a tag type and tag value.");
            message2.setGraphic(null);
            message2.getDialogPane().getStylesheets().add("/view/loginPane.css");
            message2.showAndWait();
            return;
        }
        if(tagType.getText().trim().equalsIgnoreCase(tagValue.getText().trim())){
            Alert message2 = new Alert(AlertType.INFORMATION);
            message2.initOwner(localStage);
            message2.setTitle("Tag Photo");
            message2.setHeaderText("Cannot Add Tag.");
            message2.setContentText("Tag Type cannot equal Tag Value.");
            message2.setGraphic(null);
            message2.getDialogPane().getStylesheets().add("/view/loginPane.css");
            message2.showAndWait();
            return;
        }

        if(tagType.getText().trim().length() > 30 || tagValue.getText().trim().length() > 30){
            Alert message2 = new Alert(AlertType.INFORMATION);
            message2.initOwner(localStage);
            message2.setTitle("Tag Photo");
            message2.setHeaderText("Cannot Add Tag.");
            message2.setContentText("Tag Type and Tag Value must be less than 30 characters.");
            message2.setGraphic(null);
            message2.getDialogPane().getStylesheets().add("/view/loginPane.css");
            message2.showAndWait();
            return;
        }
        int indexTag = -1;
        for(int i = 0; i < temp.size();i++){
            if(temp.get(i).getTagName().equalsIgnoreCase(tagType.getText().trim())){
                for(int j = 0; j < temp.get(i).getTagValues().size();j++){
                    if(temp.get(i).getTagValues().get(j).equalsIgnoreCase(tagValue.getText().trim())){
                        Alert message2 = new Alert(AlertType.INFORMATION);
                        message2.initOwner(localStage);
                        message2.setTitle("Tag Photo");
                        message2.setHeaderText("Cannot Add Tag.");
                        message2.setContentText("This tag already exists for this photo.");
                        message2.setGraphic(null);
                        message2.getDialogPane().getStylesheets().add("/view/loginPane.css");
                        message2.showAndWait();
                        return;
                    }

                }
                indexTag = i;
            }
        }
        if(indexTag == -1){
            Tag tag = new Tag(tagType.getText().trim());
            tag.addTag(tagValue.getText().trim());
            int index = -1;
            if(temp.isEmpty()){
                temp.add(tag);
            }
            else{
                for(int i = 0;  i < temp.size(); i ++){
                    if(temp.get(i).getTagName().compareToIgnoreCase(tagType.getText().trim()) > 0 ){
                        temp.add(i,tag);
                        index = i;
                        break;
                    }
                    else if(temp.size()-1 == i && temp.get(i).getTagName().compareToIgnoreCase(tagType.getText().trim()) < 0){
                        temp.add(tag);
                        break;
                    }
                }
            }

            TreeItem<String> tagT = new TreeItem<String>(tagType.getText().trim());
            TreeItem<String> tagV = new TreeItem<String>(tagValue.getText().trim());
            tagT.getChildren().add(tagV);
            if(index == -1){
                treeView.getRoot().getChildren().add(tagT);
            }
            else{
                treeView.getRoot().getChildren().add(index,tagT);
            }
            treeView.getSelectionModel().clearSelection();
            treeView.getSelectionModel().select(tagV);
            treeView.scrollTo(treeView.getSelectionModel().getSelectedIndex());

        }
        else{
            temp.get(indexTag).addTag(tagValue.getText().trim());
            TreeItem<String> tagV = new TreeItem<String>(tagValue.getText().trim());
            for(int i = 0; i < treeView.getRoot().getChildren().size(); i ++){
                if(treeView.getRoot().getChildren().get(i).getValue().equalsIgnoreCase(tagType.getText().trim())){
                    int index = temp.get(indexTag).getTagValues().indexOf(tagValue.getText().trim());
                    treeView.getRoot().getChildren().get(i).getChildren().add(index,tagV);
                }
            }
            treeView.getSelectionModel().clearSelection();
            treeView.getSelectionModel().select(tagV);
            treeView.scrollTo(treeView.getSelectionModel().getSelectedIndex());

        }




        tagType.clear();
        tagValue.clear();



    }

    public void ok(ActionEvent e){
        photo.setTags(temp);
        localStage.close();
    }

    public void cancel(ActionEvent e){
        localStage.close();
    }

}