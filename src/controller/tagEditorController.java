package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
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
import model.Admin;
import model.Photo;
import model.Tag;

/**
 * @author Jesse Graham | Arsal Shaikh
 * */

public class tagEditorController {

    @FXML TextField tagType;

    @FXML TextField tagValue;

    @FXML TreeView<String>  treeView;

    private Stage localStage;

    Photo photo;

    ArrayList<String> input;

    ArrayList<Tag> temp;

    Admin admin;

    public void start(Stage localStage,  Photo photo, Admin admin) {
        this.localStage = localStage;
        this.photo = photo;
        input = new ArrayList<>();
        temp = photo.getTags();
        this.admin = admin;
        TreeItem<String> dumb = new TreeItem<>("dumb");
        dumb.setExpanded(true);
        treeView.setRoot(dumb);
        treeView.setShowRoot(false);
        temp = new ArrayList<>();
        for(int i = 0; i < photo.getTags().size(); i++){
            Tag t = new Tag(photo.getTags().get(i).toString());
            for(int j = 0; j< photo.getTags().get(i).getTagValues().size(); j++){
                t.addTagValue(photo.getTags().get(i).getTagValues().get(j));
            }
            temp.add(t);
        }

        for (Tag tag : temp) {
            TreeItem<String> tagT = new TreeItem<>(tag.toString());
            for (int j = 0; j < tag.getTagValues().size(); j++) {
                TreeItem<String> tagV = new TreeItem<>(tag.getTagValues().get(j));
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
        for (Tag tag : temp) {
            if (tag.getTagName().equalsIgnoreCase(parent)) {
                for (int j = 0; j < tag.getTagValues().size(); j++) {
                    if (tag.getTagValues().get(j).equalsIgnoreCase(s)) {
                        tag.getTagValues().remove(j);
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
            message2.setContentText("Enter Both A Type And Value For The Tag");
            message2.setGraphic(null);
            message2.showAndWait();
            return;
        }
        if(tagType.getText().trim().equalsIgnoreCase(tagValue.getText().trim())){
            Alert message2 = new Alert(AlertType.INFORMATION);
            message2.initOwner(localStage);
            message2.setTitle("Tag Photo");
            message2.setHeaderText("Cannot Add Tag.");
            message2.setContentText("Tag Type Must Be Different From TagValue");
            message2.setGraphic(null);
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

            TreeItem<String> tagT = new TreeItem<>(tagType.getText().trim());
            TreeItem<String> tagV = new TreeItem<>(tagValue.getText().trim());
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
            TreeItem<String> tagV = new TreeItem<>(tagValue.getText().trim());
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

    public void quitApp(ActionEvent actionEvent) throws IOException {
        Admin.writeAdmin(admin);
        Platform.exit();
    }
}