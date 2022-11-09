//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package desktop;

import filemanage.FileView;
import filemanage.MainView;
import filemanage.PropertyPane;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import filemanage.basic.DiskBlock;
import filemanage.basic.FAT;
import filemanage.basic.File;
import filemanage.basic.Folder;

import java.util.ArrayList;
import java.util.List;

public class FolderPane {
    private FAT fat;
    private DiskBlock block;
    private String path;
    private Folder folder;
    private List<DiskBlock> bList;
    private Label[] fIcons;
    public static int index;
    private Stage stage;
    private Scene scene;
    private FlowPane flowPane;
    private MenuItem openItem;
    private MenuItem delItem;
    private MenuItem renameItem;
    private MenuItem propItem;
    private ContextMenu fileContextMenu;

    public FolderPane(FAT fat, DiskBlock block, Folder folder, String path) {
        this.fat = fat;
        this.block = block;
        this.folder = folder;
        this.path = path;
        this.flowPane = new FlowPane();
        this.flowPane.setId("flowPane");
        this.bList = new ArrayList();
        this.bList = fat.getBlockBegin(path);
        this.initContextMenu();
        this.showView();
    }

    public void showView() {
        this.addFileIcon();
        this.stage = new Stage();
        this.scene = new Scene(this.flowPane, 600.0D, 400.0D);
        this.scene.getStylesheets().add("resourses/css/PV.css");
        this.stage.setScene(this.scene);
        this.stage.setTitle(this.folder.getFolderName());
        Image image = new Image("resourses/images/folder.png");
        this.stage.getIcons().add(image);
        this.stage.show();
    }

    private void initContextMenu() {
        this.openItem = new MenuItem("打开");
        this.delItem = new MenuItem("删除");
        this.propItem = new MenuItem("属性");
        this.fileContextMenu = new ContextMenu(new MenuItem[]{this.openItem, this.delItem, this.propItem});
        this.fileContextMenu.setId("fileContextMenu");
        this.openItem.setOnAction((ActionEvent) -> {
            this.onOpen();
        });
        this.delItem.setOnAction((ActionEvent) -> {
            DiskBlock thisBlock = (DiskBlock)this.bList.get(index);
            if (this.fat.isOpenedFile(thisBlock)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText((String)null);
                alert.setContentText("文件未关闭");
                alert.showAndWait();
            } else {
                new delFile(thisBlock, this.fat);
                this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
                this.addFileIcon();
            }

        });
        this.propItem.setOnAction((ActionEvent) -> {
            DiskBlock thisBlock = (DiskBlock)this.bList.get(index);
            new PropertyPane(thisBlock);
        });
    }

    private void onOpen() {
        DiskBlock thisBlock = (DiskBlock)this.bList.get(index);
        if (thisBlock.getObject() instanceof File) {
            if (this.fat.isOpenedFile(thisBlock)) {
                Alert duplicate = new Alert(AlertType.ERROR, "文件已打开", new ButtonType[0]);
                duplicate.showAndWait();
            } else {
                this.fat.addFilesOpened(thisBlock);
                new FileView((File)thisBlock.getObject(), this.fat, thisBlock, MainView.mainView0);
            }
        } else {
            Folder thisFolder = (Folder)thisBlock.getObject();
            String newPath = thisFolder.getLocation() + "\\" + thisFolder.getFolderName();
            new FolderPane(this.fat, thisBlock, thisFolder, newPath);
        }

    }

    public void addFileIcon() {
        int n = this.bList.size();
        this.fIcons = new Label[n];

        for(int i = 0; i < n; ++i) {
            if (((DiskBlock)this.bList.get(i)).getObject() instanceof Folder) {
                this.fIcons[i] = new Label(((Folder)((DiskBlock)this.bList.get(i)).getObject()).getFolderName(), new ImageView(new Image("resourses/images/folder1.png", 60.0D, 60.0D, true, true)));
            } else {
                this.fIcons[i] = new Label(((File)((DiskBlock)this.bList.get(i)).getObject()).getFileName(), new ImageView(new Image("resourses/images/file1.png", 60.0D, 60.0D, true, true)));
            }

            this.fIcons[i].setContentDisplay(ContentDisplay.TOP);
            this.fIcons[i].setWrapText(true);
            this.fIcons[i].setId("innerIcon");
            this.flowPane.getChildren().add(this.fIcons[i]);
            this.clickOnFile(this.fIcons[i], this.fileContextMenu, i);
        }

    }

    public void clickOnFile(Label fileIcon, final ContextMenu fileContextMenu, final int index) {
        fileIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Label src = (Label)event.getSource();
                FolderPane.index = index;
                if (event.getButton() == MouseButton.SECONDARY) {
                    fileContextMenu.show(src, event.getScreenX(), event.getScreenY());
                } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    FolderPane.this.onOpen();
                } else {
                    fileContextMenu.hide();
                }

            }
        });
    }
}
