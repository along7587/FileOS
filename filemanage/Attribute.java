package filemanage;

import filemanage.basic.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.Map;

public class Attribute {
    private DiskBlock block;
    private FAT fat;
    private Label icon;
    private Map<Path, TreeItem<String>> pathMap;
    private String oldName;
    private String location;
    private Stage stage;
    private Scene scene;
    private VBox vBox;
    private HBox hBox;
    private GridPane gridPane;
    private TextField nameField;
    private Label typeField;
    private Label locField;
    private Label sizeField;
    private Button okButton;
    private Button cancelButton;
    //当放置在ToggleGroup中时，只能选择一个RadioButton,单选按钮只能执行：选择或取消选择
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private Image ico;
    private String name;

    public Attribute(DiskBlock block, FAT fat, Label icon, Map<Path, TreeItem<String>> pathMap) {
        this.block = block;
        this.fat = fat;
        this.icon = icon;
        this.pathMap = pathMap;
        this.showView();
    }

    private void showView() {
        RadioButton checkRead = new RadioButton("只读");
        checkRead.setToggleGroup(this.toggleGroup);
        checkRead.setUserData(0);
        RadioButton checkWrite = new RadioButton("读写");
        checkWrite.setToggleGroup(this.toggleGroup);
        checkWrite.setUserData(1);
        HBox checkBoxGroup = new HBox(new Node[]{checkRead, checkWrite});
        checkBoxGroup.setSpacing(10.0D);
        if (this.block.getObject() instanceof Folder) {
            Folder folder = (Folder)this.block.getObject();
            this.nameField = new TextField(folder.getFolderName());
            this.typeField = new Label(folder.getType());
            this.locField = new Label(folder.getLocation());
            this.sizeField = new Label(folder.getSize() + "KB");
            this.oldName = folder.getFolderName();
            this.location = folder.getLocation();
            checkRead.setDisable(true);
            checkWrite.setDisable(true);
            this.ico = new Image("resourses/images/folder1.png");
        } else {
            File file = (File)this.block.getObject();
            this.nameField = new TextField(file.getFileName());
            this.typeField = new Label(file.getType());
            this.locField = new Label(file.getLocation());
            this.sizeField = new Label(file.getSize() + "KB");
            this.oldName = file.getFileName();
            this.location = file.getLocation();
            this.toggleGroup.selectToggle(file.getFlag() == 0 ? checkRead : checkWrite);
            this.ico = new Image("resourses/images/file1.png");
        }

        this.name = this.nameField.getText();
        this.okButton = new Button("确 定");
        this.okButton.setPrefSize(80.0D, 16.0D);
        this.okButton.setStyle("-fx-background-color: rgba(60,225,255,0.1) ; -fx-border-color: rgba(60,225,255,1); -fx-border-width: 0.5px ; -fx-font-size: 14px;");
        this.okButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Attribute.this.okButton.setStyle("-fx-background-color: rgba(60,225,255,1) ; -fx-border-color: rgba(60,225,255,1); -fx-border-width: 0.5px; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold");
            }
        });
        this.okButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Attribute.this.okButton.setStyle("-fx-background-color: rgba(60,225,255,0.1) ; -fx-border-color: rgba(60,225,255,1); -fx-border-width: 0.5px ; -fx-font-size: 14px;");
            }
        });
        this.cancelButton = new Button("取 消");
        this.cancelButton.setPrefSize(80.0D, 16.0D);
        this.cancelButton.setStyle("-fx-background-color: rgba(220,20,60,0.05) ; -fx-border-color: rgba(220,20,60,0.8); -fx-border-width: 0.5px ; -fx-font-size: 14px;");
        this.cancelButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Attribute.this.cancelButton.setStyle("-fx-background-color: rgba(220,20,60,0.85) ; -fx-border-color: transparent; -fx-border-width: 0.5px ; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold ;");
            }
        });
        this.cancelButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Attribute.this.cancelButton.setStyle("-fx-background-color: rgba(220,20,60,0.05) ; -fx-border-color: rgba(220,20,60,0.8); -fx-border-width: 0.5px ; -fx-font-size: 14px;");
            }
        });
        this.buttonOnAction();
        this.nameField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("") || newValue.equals(Attribute.this.name)) {
                    Attribute.this.okButton.setDisable(true);
                }

            }
        });
        this.nameField.setPrefWidth(150.0D);
        this.hBox = new HBox(new Node[]{this.okButton, this.cancelButton});
        this.hBox.setPadding(new Insets(30.0D, 30.0D, 20.0D, 30.0D));
        this.hBox.setSpacing(40.0D);
        this.gridPane = new GridPane();
        this.gridPane.add(new Label("名称:"), 0, 0);
        this.gridPane.add(new Label("文件类型:"), 0, 1);
        this.gridPane.add(new Label("位置:"), 0, 2);
        this.gridPane.add(new Label("大小:"), 0, 3);
        this.gridPane.add(new Label("属性:"), 0, 6);
        this.gridPane.add(this.nameField, 1, 0);
        this.gridPane.add(this.typeField, 1, 1);
        this.gridPane.add(this.locField, 1, 2);
        this.gridPane.add(this.sizeField, 1, 3);
        this.gridPane.add(checkBoxGroup, 1, 6);
        this.gridPane.setPadding(new Insets(25.0D, 0.0D, 0.0D, 20.0D));
        this.gridPane.setVgap(10.0D);
        this.gridPane.setHgap(10.0D);
        this.gridPane.setId("gridPane");
        this.vBox = new VBox();
        this.vBox.getChildren().addAll(new Node[]{this.gridPane, this.hBox});
        this.vBox.setStyle("-fx-background-color: #ffffff;");
        this.scene = new Scene(this.vBox);
        this.scene.getStylesheets().add("resourses/css/PV.css");
        this.stage = new Stage();
        this.stage.setScene(this.scene);
        this.stage.getIcons().add(this.ico);
        this.stage.setTitle(this.oldName);
        this.stage.setAlwaysOnTop(true);
        this.stage.show();
    }

    private void buttonOnAction() {
        this.cancelButton.setOnAction((ActionEvent) -> {
            this.stage.close();
        });
        this.okButton.setOnAction((ActionEvent) -> {
            String newName = this.nameField.getText();
            if (!this.oldName.equals(newName)) {
                if (this.fat.hasSame(this.location, newName)) {
                    Alert alert = new Alert(AlertType.ERROR);
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

            if (this.block.getObject() instanceof File) {
                File thisFile = (File)this.block.getObject();
                int newFlag = this.toggleGroup.getSelectedToggle().getUserData().hashCode();
                thisFile.setFlag(newFlag);
            }

            this.stage.close();
        });
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
                    String newNext = newLoc + "\\" + nextFolder.getFolderName();
                    this.fat.replacePath(nextPath, newNext);
                }
            }
        }

    }
}
