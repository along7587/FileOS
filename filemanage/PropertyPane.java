//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filemanage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import filemanage.basic.DiskBlock;
import filemanage.basic.File;
import filemanage.basic.Folder;

public class PropertyPane {
    private DiskBlock block;
    private Stage stage;
    private Scene scene;
    private VBox vBox;
    private HBox hBox;
    private GridPane gridPane;
    private Label nameField;
    private Label typeField;
    private Label locField;
    private Label sizeField;
    private Button okButton;
    private Button cancelButton;
    private Button applyButton;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private Image ico;

    public PropertyPane(DiskBlock block) {
        this.block = block;
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
            this.nameField = new Label(folder.getFolderName());
            this.typeField = new Label(folder.getType());
            this.locField = new Label(folder.getLocation());
            this.sizeField = new Label(folder.getSize() + "KB");
            checkRead.setDisable(true);
            checkWrite.setDisable(true);
            this.ico = new Image(this.getClass().getResourceAsStream("/resourses/images/stageicon.png"));
        } else {
            File file = (File)this.block.getObject();
            this.nameField = new Label(file.getFileName());
            this.typeField = new Label(file.getType());
            this.locField = new Label(file.getLocation());
            this.sizeField = new Label(file.getSize() + "KB");
            this.toggleGroup.selectToggle(file.getFlag() == 0 ? checkRead : checkWrite);
            this.ico = new Image(this.getClass().getResourceAsStream("/resourses/images/stageicon.png"));
        }

        this.okButton = new Button("确定");
        this.okButton.setPrefSize(100.0D, 20.0D);
        this.okButton.setStyle("-fx-background-color:#d3d3d3;");
        this.okButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                PropertyPane.this.okButton.setStyle("-fx-background-color: #808080;");
            }
        });
        this.okButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                PropertyPane.this.okButton.setStyle("-fx-background-color: #d3d3d3;");
            }
        });
        this.cancelButton = new Button("取消");
        this.cancelButton.setPrefSize(100.0D, 20.0D);
        this.cancelButton.setStyle("-fx-background-color:#d3d3d3;");
        this.cancelButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                PropertyPane.this.cancelButton.setStyle("-fx-background-color: #ffffff;");
            }
        });
        this.cancelButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                PropertyPane.this.cancelButton.setStyle("-fx-background-color: #d3d3d3;");
            }
        });
        this.applyButton = new Button("应用");
        this.applyButton.setPrefSize(100.0D, 20.0D);
        this.applyButton.setStyle("-fx-background-color:#d3d3d3;");
        this.applyButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                PropertyPane.this.applyButton.setStyle("-fx-background-color: #808080;");
            }
        });
        this.applyButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                PropertyPane.this.applyButton.setStyle("-fx-background-color: #d3d3d3;");
            }
        });
        this.buttonOnAction();
        this.toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                PropertyPane.this.applyButton.setDisable(false);
            }
        });
        this.hBox = new HBox(new Node[]{this.okButton, this.cancelButton});
        this.hBox.setPadding(new Insets(30.0D, 12.0D, 5.0D, 12.0D));
        this.hBox.setSpacing(50.0D);
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
        this.gridPane.setPadding(new Insets(15.0D, 12.0D, 0.0D, 12.0D));
        this.gridPane.setVgap(10.0D);
        this.gridPane.setHgap(10.0D);
        this.vBox = new VBox();
        this.vBox.getChildren().addAll(new Node[]{this.gridPane, this.hBox});
        this.vBox.setStyle("-fx-background-color: #ffffff;");
        this.scene = new Scene(this.vBox);
        this.stage = new Stage();
        this.stage.setScene(this.scene);
        this.stage.getIcons().add(this.ico);
        this.stage.setAlwaysOnTop(true);
        this.stage.show();
    }

    private void buttonOnAction() {
        this.applyButton.setOnAction((ActionEvent) -> {
            if (this.block.getObject() instanceof File) {
                File thisFile = (File)this.block.getObject();
                int newFlag = this.toggleGroup.getSelectedToggle().getUserData().hashCode();
                thisFile.setFlag(newFlag);
            }

            this.applyButton.setDisable(true);
        });
        this.cancelButton.setOnAction((ActionEvent) -> {
            this.stage.close();
        });
        this.okButton.setOnAction((ActionEvent) -> {
            if (this.block.getObject() instanceof File) {
                File thisFile = (File)this.block.getObject();
                int newFlag = this.toggleGroup.getSelectedToggle().getUserData().hashCode();
                thisFile.setFlag(newFlag);
            }

            this.stage.close();
        });
    }
}
