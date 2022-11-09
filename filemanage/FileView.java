package filemanage;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import filemanage.basic.DiskBlock;
import filemanage.basic.FAT;
import filemanage.basic.File;
import filemanage.basic.Folder;
import filemanage.util.FATUtil;

import java.util.Optional;

public class FileView {
    private File file;
    private FAT fat;
    private DiskBlock block;
    private String newContent;
    private String oldContent;
    private Stage stage;
    private Scene scene;
    private BorderPane borderPane;
    private TextArea contentField;
    //菜单栏(MenuBar)
    private MenuBar menuBar;
    private Menu fileMenu;
    private MenuItem saveItem;
    private MenuItem closeItem;
    private MainView mainView;
//    MenuBar：创建Menu 的底部 Menu需要创建在MenuBar的上面
//    Menu ：创建一个可操作的选项
//    MenuItem ：创建子菜单

    public FileView(File file, FAT fat, DiskBlock block, MainView mainView) {
        this.mainView = mainView;
        this.file = file;
        this.fat = fat;
        this.block = block;
        this.showView();
    }

    private void showView() {
        this.contentField = new TextArea();
        this.contentField.setPrefRowCount(25);
        this.contentField.setWrapText(true);
        this.contentField.setText(this.file.getContent());
        if (this.file.getFlag() == 0) {
            this.contentField.setDisable(true);
        }

        this.saveItem = new MenuItem("保存");
        this.saveItem.setGraphic(new ImageView("resourses/images/save.png"));
        this.saveItem.setOnAction((ActionEvent) -> {
            this.newContent = this.contentField.getText();
            this.oldContent = this.file.getContent();
            if (this.newContent == null) {
                this.newContent = "";
            }

            if (!this.newContent.equals(this.oldContent)) {
                this.saveContent(this.newContent);
            }

        });
        this.closeItem = new MenuItem("关闭");
        this.closeItem.setGraphic(new ImageView("resourses/images/close.png"));
        this.closeItem.setOnAction((ActionEvent) -> {
            this.onClose(ActionEvent);
        });
        this.fileMenu = new Menu("File", (Node)null, new MenuItem[]{this.saveItem, this.closeItem});
        this.menuBar = new MenuBar(new Menu[]{this.fileMenu});
        this.menuBar.setPadding(new Insets(0.0D));
        this.borderPane = new BorderPane(this.contentField, (Node)null, (Node)null, (Node)null, (Node)null);
        this.scene = new Scene(this.borderPane);
        this.scene.getStylesheets().add("resourses/css/PV.css");
        this.stage = new Stage();
        this.stage.setScene(this.scene);
        this.stage.setTitle(this.file.getFileName());
        this.stage.titleProperty().bind(this.file.fileNamePProperty());
        this.stage.getIcons().add(new Image("resourses/images/file1.png"));
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                FileView.this.onClose(event);
            }
        });
        this.stage.show();
    }

    private void onClose(Event event) {
        this.newContent = this.contentField.getText();
        this.oldContent = this.file.getContent();
        boolean isCancel = false;
        if (this.newContent == null) {
            this.newContent = "";
        }

        if (!this.newContent.equals(this.oldContent)) {
            event.consume();
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("保存更改");
            alert.setHeaderText((String)null);
            alert.setContentText("文件内容已更改，是否保存?");
            ButtonType saveType = new ButtonType("保存");
            ButtonType noType = new ButtonType("不保存");
            ButtonType cancelType = new ButtonType("取消", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(new ButtonType[]{saveType, noType});
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == saveType) {
                this.saveContent(this.newContent);
            } else if (result.get() == cancelType) {
                isCancel = true;
            }
        }

        if (!isCancel) {
            this.fat.removeFilesOpened(this.block);
            this.stage.close();
        }

    }

    private void saveContent(String newContent) {
        int newLength = newContent.length();
        int blockCount = FATUtil.blocksCount(newLength);
        this.file.setLength(blockCount);
        this.file.setContent(newContent);
        this.file.setSize(FATUtil.getSize(newLength));
        if (this.file.hasParent()) {
            Folder parent = this.file.getParent();
            parent.setSize(FATUtil.getFolderSize(parent));

            while(parent.hasParent()) {
                parent = parent.getParent();
                parent.setSize(FATUtil.getFolderSize(parent));
            }
        }

        this.fat.reallocBlocks(blockCount, this.block);
        this.mainView.getMidBox().getChildren().remove(2);
        GridPane gridPane = this.mainView.getMid().updateGridPane();
        this.mainView.getMidBox().getChildren().add(2, gridPane);
    }
}
