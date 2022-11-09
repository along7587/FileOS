//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filemanage;

import filemanage.basic.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.Map;

public class Rename {
    private DiskBlock block;
    private FAT fat;
    private Label icon;
    private Map<Path, TreeItem<String>> pathMap;
    private Stage stage;
    private Scene scene;
    private HBox hBox;
    private VBox vBox;
    private TextField nameField;
    private Button okButton;
    private Button cancelButton;
    private String oldName;
    private String location;

    public Rename(DiskBlock block, FAT fat, Label icon, Map<Path, TreeItem<String>> pathMap) {
        this.block = block;
        this.fat = fat;
        this.icon = icon;
        this.pathMap = pathMap;
        this.showView();
    }

    private void showView() {
        if (this.block.getObject() instanceof Folder) {
            this.oldName = ((Folder)this.block.getObject()).getFolderName();
            this.location = ((Folder)this.block.getObject()).getLocation();
        } else {
            this.oldName = ((File)this.block.getObject()).getFileName();
            this.location = ((File)this.block.getObject()).getLocation();
        }

        this.nameField = new TextField(this.oldName);
        this.okButton = new Button("确  认");
        this.okButton.setId("okbtn");
        this.okButton.setStyle("-fx-background-color: rgba(60,225,255,0.1) ; -fx-border-color: rgba(60,225,255,1); -fx-border-width: 0.5px ;");
        this.okButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Rename.this.okButton.setStyle("-fx-background-color: rgba(60,225,255,1) ; -fx-border-color: rgba(60,225,255,1); -fx-border-width: 0.5px; -fx-text-fill: white; -fx-font-weight: bold");
            }
        });
        this.okButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Rename.this.okButton.setStyle("-fx-background-color:rgba(60,225,255,0.25) ; -fx-border-color: rgba(60,225,255,1); -fx-border-width: 0.5px");
            }
        });
        this.okButton.setOnAction((ActionEvent) -> {
            String newName = this.nameField.getText();
            Alert alert;
            if (newName.length() > 3) {
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText((String)null);
                alert.setContentText("命名超出长度!");
                alert.showAndWait();
            } else if (!newName.equals(this.oldName)) {
                if (this.fat.hasSame(this.location, newName)) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText((String)null);
                    alert.setContentText("重名！");
                    alert.showAndWait();
                } else {
                    if (this.block.getObject() instanceof Folder) {
                        Folder thisFolder = (Folder)this.block.getObject();
                        thisFolder.setFolderName(newName);
                        ((TreeItem)this.pathMap.get(thisFolder.getPath())).setValue(newName);
                        this.reLoc(this.location, this.location, this.oldName, newName, thisFolder);
                    } else {
                        ((File)this.block.getObject()).setFileName(newName);
                    }

                    this.icon.setText(newName);
                }
            }

            this.stage.close();
        });
        this.cancelButton = new Button("取 消");
        this.cancelButton.setId("cancelbtn");
        this.cancelButton.setStyle("-fx-background-color: rgba(220,20,60,0.05) ; -fx-border-color: rgba(220,20,60,0.8); -fx-border-width: 0.5px ; ");
        this.cancelButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Rename.this.cancelButton.setStyle("-fx-background-color: rgba(220,20,60,0.9) ; -fx-border-color: transparent; -fx-border-width: 0.5px ; -fx-text-fill: white; -fx-font-weight: bold");
            }
        });
        this.cancelButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Rename.this.cancelButton.setStyle("-fx-background-color: rgba(220,20,60,0.1) ; -fx-border-color: rgba(220,20,60,0.8); -fx-border-width: 0.5px ;");
            }
        });
        this.cancelButton.setOnAction((ActionEvent) -> {
            this.stage.close();
        });
        this.hBox = new HBox(new Node[]{this.okButton, this.cancelButton});
        this.hBox.setSpacing(30.0D);
        this.hBox.setPadding(new Insets(20.0D, 50.0D, 20.0D, 50.0D));
        this.vBox = new VBox(new Node[]{this.nameField, this.hBox});
        this.vBox.setId("Rename");
        this.scene = new Scene(this.vBox);
        this.scene.getStylesheets().add("resourses/css/PV.css");
        this.stage = new Stage();
        this.stage.setWidth(250.0D);
        this.stage.setHeight(130.0D);
        this.stage.setScene(this.scene);
        Image icon = new Image("resourses/images/rename.png");
        this.stage.getIcons().add(icon);
        this.stage.setTitle("重命名");
        this.stage.setAlwaysOnTop(false);
        this.stage.show();
    }

    private void reLoc(String oldP, String newP, String oldN, String newN, Folder folder) {
        String oldLoc = oldP + "\\" + oldN;
        String newLoc = newP + "\\" + newN;
        Path oldPath = this.fat.getPath(oldLoc);
        this.fat.replacePath(oldPath, newLoc);
        Iterator var10 = folder.getChildren().iterator();

        while(var10.hasNext()) {
            Object child = var10.next();
            if (child instanceof File) {
                ((File)child).setLocation(newLoc);
            } else {
                Folder nextFolder = (Folder)child;
                nextFolder.setLocation(newLoc);
                if (nextFolder.hasChild()) {
                    this.reLoc(oldLoc, newLoc, nextFolder.getFolderName(), nextFolder.getFolderName(), nextFolder);
                } else {
                    Path nextPath = this.fat.getPath(oldLoc + "\\" + nextFolder.getFolderName());
                    this.fat.replacePath(nextPath, newLoc + "\\" + nextFolder.getFolderName());
                }
            }
        }

    }
}
