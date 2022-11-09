package filemanage;

//EventHandler，事件处理

import desktop.ContainPane;
import desktop.Desktop;
import filemanage.basic.*;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.Map.Entry;

public class MainView {
    public static MainView mainView0;
    private static final String DURATION_PATH = "disk";
    private static SimpleSetProperty<Label> selectedThumbnailsProperty = new SimpleSetProperty(FXCollections.observableSet(new HashSet()));
    private Scene scene;
    private VBox midBox;
    private BorderPane bp;
    private GridPane gridPane;
    private MiddleLayout mid;
    private FAT fat;
    private int index;
    private List<DiskBlock> blockList;
    private String recentPath;
    private Map<Path, TreeItem<String>> pathMap;
    private FlowPane flowPane;
    private Label[] icons;
    private TreeView<String> treeView;
    private TreeItem<String> rootNode;
    private TreeItem<String> recentNode;
    private TableView<DiskBlock> blockTable;
    private ObservableList<DiskBlock> dataBlock;
    private ContextMenu contextMenu;
    private ContextMenu contextMenu2;
    private MenuItem createFileItem;
    private MenuItem createFolderItem;
    private MenuItem openItem;
    private MenuItem renameItem;
    private MenuItem delItem;
    private MenuItem propItem;
    Desktop ds;
    ContainPane ct;

    public MainView() {
        mainView0 = this;
        Stage stage = new Stage();
        this.pathMap = new HashMap();
        this.ds = Desktop.getInstance();
        this.ct = this.ds.containPane;
        this.fat = this.ds.getFat();
        this.mid = new MiddleLayout(this.fat);
        this.mid.middleLayout();
        this.recentPath = "C:";
        this.initFrame(stage);
    }

    private void initFrame(Stage stage) {
        //borderpane
        this.flowPane = new FlowPane();
        this.flowPane.setPrefSize(300.0D, 300.0D);
        this.flowPane.setHgap(10.0D);
        this.flowPane.setVgap(10.0D);
        this.flowPane.setId("flowPane");
        this.flowPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (me) -> {
            if (me.getButton() == MouseButton.SECONDARY && !this.contextMenu2.isShowing()) {
                this.contextMenu.show(this.flowPane, me.getScreenX(), me.getScreenY());
            } else {
                this.contextMenu.hide();
            }

        });
        this.initContextMenu();
        this.menuItemSetOnAction();
        this.initTables();
        this.initTreeView();
        this.flowPane.setStyle("-fx-background-color: rgba(49,49,58);-fx-border-color: rgba(49,49,58);-fx-border-width:0.5px;-fx-padding:15px 0 0 20px;");
        //this.flowPane.setStyle("-fx-background-color: rgba(255,255,255,0.2);-fx-border-color: rgba(255,255,255,0.2);-fx-border-width:0.5px;-fx-padding:15px 0 0 20px;");
        this.gridPane = this.mid.updateGridPane();
        this.midBox = new VBox(new Node[]{this.flowPane, this.mid.getLabel1(), this.gridPane});
        this.bp = new BorderPane();
        this.bp.setPrefSize(1200.0D, 600.0D);
        //treeView.setStyle("-fx-background-color: rgb(49,49,58)");
        this.bp.setLeft(this.treeView);
        //blockTable.setStyle("-fx-background-color: rgb(49,49,58)");
        this.bp.setRight(this.blockTable);
        this.bp.setCenter(this.midBox);
        this.scene = new Scene(this.bp);


        MotionBlur motionBlur = new MotionBlur();

//        //Setting the radius to the effect
//        motionBlur.setRadius(10.5);
//
//        //Setting angle to the effect
//        motionBlur.setAngle(45);
//        //this.bp.setEffect(motionBlur);
        this.scene.getStylesheets().add("resourses/css/PV.css");
        //stage.initStyle(StageStyle.UNIFIED);
        stage.setScene(this.scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("resourses/images/ico.png", 60.0D, 60.0D, true, true));
        stage.setTitle("文件系统");
        stage.setOpacity(0.8);
        stage.show();
        stage.setOnCloseRequest((e) -> {
            try {
                Throwable var2 = null;
                Object var3 = null;

                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("disk"));

                    try {
                        outputStream.writeObject(this.fat);
                    } finally {
                        if (outputStream != null) {
                            outputStream.close();
                        }

                    }
                } catch (Throwable var14) {
                    if (var2 == null) {
                        var2 = var14;
                    } else if (var2 != var14) {
                        var2.addSuppressed(var14);
                    }

                    throw var2;
                }

            } catch (FileNotFoundException var15) {
                var15.printStackTrace();
            } catch (Throwable var16) {
                var16.printStackTrace();
            }

        });
    }

    private void initContextMenu() {
        this.createFileItem = new MenuItem("新建文件");
        this.createFolderItem = new MenuItem("新建文件夹");
        this.openItem = new MenuItem("打开");
        this.delItem = new MenuItem("删除");
        this.renameItem = new MenuItem("重命名");
        this.propItem = new MenuItem("属性");
        this.contextMenu = new ContextMenu(new MenuItem[]{this.createFileItem, this.createFolderItem});
        this.contextMenu.setId("contextMenu");
        this.contextMenu2 = new ContextMenu(new MenuItem[]{this.openItem, this.delItem, this.renameItem, this.propItem});
        this.contextMenu2.setId("contextMenu2");
    }

    private void menuItemSetOnAction() {
        this.createFileItem.setOnAction((ActionEvent) -> {
            int no = this.fat.create_file(this.recentPath);
            if (no == 255) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("空间不足！");
                alert.showAndWait();
            } else {
                this.ct.upDateDesktop();
                this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
                this.updateShow(this.fat.getBlockBegin(this.recentPath), this.recentPath);
                this.midBox.getChildren().remove(2);
                GridPane gridPane = this.mid.updateGridPane();
                this.midBox.getChildren().add(2, gridPane);
            }

        });
        this.createFolderItem.setOnAction((ActionEvent) -> {
            int no = this.fat.create_folder(this.recentPath);
            if (no == 255) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("空间不足！");
                alert.showAndWait();
            } else {
                this.ct.upDateDesktop();
                Folder newFolder = (Folder)this.fat.getBlock(no).getObject();
                Path newPath = newFolder.getPath();
                this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
                this.updateShow(this.fat.getBlockBegin(this.recentPath), this.recentPath);
                this.addNode(this.recentNode, newPath);
                this.midBox.getChildren().remove(2);
                GridPane gridPane = this.mid.updateGridPane();
                this.midBox.getChildren().add(2, gridPane);
            }

        });
        this.openItem.setOnAction((ActionEvent) -> {
            this.onOpen();
        });
        this.delItem.setOnAction((ActionEvent) -> {
            DiskBlock thisBlock = (DiskBlock)this.blockList.get(this.index);
            if (this.fat.isOpenedFile(thisBlock)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText((String)null);
                alert.setContentText("文件未关闭!");
                alert.showAndWait();
            } else {
                new deleteFile(thisBlock, this.fat, this);
                this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
                this.updateShow(this.fat.getBlockBegin(this.recentPath), this.recentPath);
                this.ct.upDateDesktop();
                this.midBox.getChildren().remove(2);
                GridPane gridPane = this.mid.updateGridPane();
                this.midBox.getChildren().add(2, gridPane);
            }

        });
        this.renameItem.setOnAction((ActionEvent) -> {
            DiskBlock thisBlock = (DiskBlock)this.blockList.get(this.index);
            new Rename(thisBlock, this.fat, this.icons[this.index], this.pathMap);
            this.ct.upDateDesktop();
        });
        this.propItem.setOnAction((ActionEvent) -> {
            DiskBlock thisBlock = (DiskBlock)this.blockList.get(this.index);
            new Attribute(thisBlock, this.fat, this.icons[this.index], this.pathMap);
        });
    }

    private void initTables() {
        this.blockTable = new TableView();
        this.blockTable.setId("blockTable");
        this.dataBlock = FXCollections.observableArrayList(this.fat.getDiskBlocks());
        TableColumn noCol = new TableColumn("index");
        noCol.setCellValueFactory(new PropertyValueFactory("noP"));
        noCol.setSortable(false);
        noCol.setMaxWidth(50.0D);
        noCol.setResizable(false);
        TableColumn indexCol = new TableColumn("next");
        indexCol.setCellValueFactory(new PropertyValueFactory("indexP"));
        indexCol.setSortable(false);
        indexCol.setMaxWidth(50.0D);
        indexCol.setResizable(false);
        TableColumn typeCol = new TableColumn("type");
        typeCol.setCellValueFactory(new PropertyValueFactory("typeP"));
        typeCol.setSortable(false);
        typeCol.setMaxWidth(50.0D);
        typeCol.setResizable(false);
        TableColumn objCol = new TableColumn("name");
        objCol.setCellValueFactory(new PropertyValueFactory("objectP"));
        objCol.setSortable(false);
        objCol.setMinWidth(133.0D);
        objCol.setResizable(false);
        this.blockTable.setItems(this.dataBlock);
        this.blockTable.getColumns().addAll(new TableColumn[]{noCol, indexCol, objCol});
        this.blockTable.setEditable(false);
        this.blockTable.setPrefWidth(235.0D);
    }

    private void initTreeView() {
        this.rootNode = new TreeItem("C:", new ImageView(new Image("resourses/images/disk.png", 22.0D, 22.0D, true, true)));
        this.rootNode.setExpanded(true);
        this.recentNode = this.rootNode;
        this.pathMap.put(this.fat.getPath("C:"), this.rootNode);
        this.treeView = new TreeView(this.rootNode);
        this.treeView.setPrefWidth(200.0D);
        this.treeView.setPrefHeight(600.0D);
        this.treeView.setCellFactory((p) -> {
            return new MainView.TextFieldTreeCellImpl();
        });
        this.treeView.setStyle("-fx-background-color: #ffffff;-fx-border-color: #d3d3d3;-fx-border-width:0.5px;-fx-padding:15px 0 0 0;-fx-font-size:15px;");
        Iterator var2 = this.fat.getPaths().iterator();

        while(var2.hasNext()) {
            Path path = (Path)var2.next();
            if (path.hasParent() && path.getParent().getPathName().equals(this.rootNode.getValue())) {
                this.initTreeNode(path, this.rootNode);
            }
        }

        this.updateShow(this.fat.getBlockBegin(this.recentPath), this.recentPath);
    }

    private void initTreeNode(Path newPath, TreeItem<String> parentNode) {
        TreeItem<String> newNode = this.addNode(parentNode, newPath);
        if (newPath.hasChild()) {
            Iterator var5 = newPath.getChildren().iterator();

            while(var5.hasNext()) {
                Path child = (Path)var5.next();
                this.initTreeNode(child, newNode);
            }
        }

    }

    private void updateShow(List<DiskBlock> bList, String path) {
        this.blockList = bList;
        final int n = bList.size();
        this.icons = new Label[n];

        for(int i = 0; i < n; ++i) {
            if (((DiskBlock)bList.get(i)).getObject() instanceof Folder) {
                this.icons[i] = new Label(((Folder)((DiskBlock)bList.get(i)).getObject()).getFolderName(), new ImageView(new Image("resourses/images/folder1.png", 60.0D, 60.0D, true, true)));
            } else {
                this.icons[i] = new Label(((File)((DiskBlock)bList.get(i)).getObject()).getFileName(), new ImageView(new Image("resourses/images/file1.png", 60.0D, 60.0D, true, true)));
            }

            this.icons[i].setContentDisplay(ContentDisplay.TOP);
            this.icons[i].setWrapText(true);
            this.icons[i].setId("icon");
            this.flowPane.getChildren().add(this.icons[i]);
            this.flowPane.setId("flowPane");
            this.icons[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    for(int i = 0; i < n; ++i) {
                        MainView.this.icons[i].setStyle("-fx-padding:5px;");
                    }

                    ((Label)event.getSource()).setStyle("-fx-background-color:rgba(51,204,255,0.4);-fx-border-width:0.5px;-fx-border-color:rgba(0,0,255,0.9);-fx-padding:4.5px");
                    Label src = (Label)event.getSource();

                    for(int j = 0; j < n; ++j) {
                        if (src == MainView.this.icons[j]) {
                            MainView.this.index = j;
                        }
                    }

                    if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                        MainView.this.contextMenu2.show(src, event.getScreenX(), event.getScreenY());
                    } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        MainView.this.onOpen();
                    } else {
                        MainView.this.contextMenu2.hide();
                    }

                }
            });
        }

    }

    private TreeItem<String> addNode(TreeItem<String> parentNode, Path newPath) {
        String pathName = newPath.getPathName();
        String value = pathName.substring(pathName.lastIndexOf(92) + 1);
        TreeItem<String> newNode = new TreeItem(value, new ImageView(new Image("resourses/images/node.png", 20.0D, 20.0D, true, true)));
        newNode.setExpanded(true);
        this.pathMap.put(newPath, newNode);
        parentNode.getChildren().add(newNode);
        return newNode;
    }

    public void removeNode(TreeItem<String> recentNode, Path remPath) {
        recentNode.getChildren().remove(this.pathMap.get(remPath));
        this.pathMap.remove(remPath);
    }

    public TreeItem<String> getRecentNode() {
        return this.recentNode;
    }

    public void setRecentNode(TreeItem<String> recentNode) {
        this.recentNode = recentNode;
    }

    private void onOpen() {
        DiskBlock thisBlock = (DiskBlock)this.blockList.get(this.index);
        if (thisBlock.getObject() instanceof File) {
            Alert duplicate;
            if (this.fat.getOpenedFiles().size() < 8) {
                if (this.fat.isOpenedFile(thisBlock)) {
                    duplicate = new Alert(AlertType.ERROR, "文件已打开", new ButtonType[0]);
                    duplicate.showAndWait();
                } else {
                    this.fat.addFilesOpened(thisBlock);
                    new FileView((File)thisBlock.getObject(), this.fat, thisBlock, mainView0);
                }
            } else {
                duplicate = new Alert(AlertType.ERROR, "文件打开已到上限", new ButtonType[0]);
                duplicate.showAndWait();
            }
        } else {
            Folder thisFolder = (Folder)thisBlock.getObject();
            String newPath = thisFolder.getLocation() + "\\" + thisFolder.getFolderName();
            this.flowPane.getChildren().removeAll(this.flowPane.getChildren());
            this.updateShow(this.fat.getBlockBegin(newPath), newPath);
            this.recentPath = newPath;
            this.recentNode = (TreeItem)this.pathMap.get(thisFolder.getPath());
        }

    }

    public VBox getMidBox() {
        return this.midBox;
    }

    public void setMidBox(VBox midBox) {
        this.midBox = midBox;
    }

    public MiddleLayout getMid() {
        return this.mid;
    }

    public void setMid(MiddleLayout mid) {
        this.mid = mid;
    }

    public final class TextFieldTreeCellImpl extends TreeCell<String> {
        private TextField textField;

        public TextFieldTreeCellImpl() {
            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && TextFieldTreeCellImpl.this.getTreeItem() != null) {
                        String pathName = null;
                        Iterator var4 = MainView.this.pathMap.entrySet().iterator();

                        while(var4.hasNext()) {
                            Entry<Path, TreeItem<String>> entry = (Entry)var4.next();
                            if (TextFieldTreeCellImpl.this.getTreeItem() == entry.getValue()) {
                                pathName = ((Path)entry.getKey()).getPathName();
                                break;
                            }
                        }

                        List<DiskBlock> fats = MainView.this.fat.getBlockBegin(pathName);
                        MainView.this.flowPane.getChildren().removeAll(MainView.this.flowPane.getChildren());
                        MainView.this.updateShow(fats, pathName);
                        MainView.this.recentPath = pathName;
                        MainView.this.recentNode = TextFieldTreeCellImpl.this.getTreeItem();
                    }

                }
            });
        }

        public void startEdit() {
            super.startEdit();
            if (this.textField == null) {
                this.createTextField();
            }

            this.setText((String)null);
            this.setGraphic(this.textField);
            this.textField.selectAll();
        }

        public void cancelEdit() {
            super.cancelEdit();
            this.setText((String)this.getItem());
            this.setGraphic(this.getTreeItem().getGraphic());
        }

        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setText((String)null);
                this.setGraphic((Node)null);
            } else if (this.isEditing()) {
                if (this.textField != null) {
                    this.textField.setText(this.getString());
                }

                this.setText((String)null);
                this.setGraphic(this.textField);
            } else {
                this.setText(this.getString());
                this.setGraphic(this.getTreeItem().getGraphic());
            }

        }

        private void createTextField() {
            this.textField = new TextField(this.getString());
            this.textField.setOnKeyReleased((t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    this.commitEdit(this.textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    this.cancelEdit();
                }

            });
        }

        private String getString() {
            return this.getItem() == null ? "" : ((String)this.getItem()).toString();
        }
    }
}
