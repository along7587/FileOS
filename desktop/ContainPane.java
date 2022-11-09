//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package desktop;

import filemanage.PropertyPane;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import filemanage.basic.DiskBlock;
import filemanage.basic.FAT;
import filemanage.basic.File;
import filemanage.basic.Folder;
import filemanage.FileView;
import filemanage.MainView;

import java.util.ArrayList;
import java.util.List;

public class ContainPane extends FlowPane {
    public static ContainPane containPane;
    private MenuItem openItem;
    private MenuItem delItem;
    private MenuItem renameItem;
    private MenuItem propItem;
    private static ContextMenu fileContextMenu;
    private FAT fat;
    List<DiskBlock> bList;
    //标志用户目前点击了哪一个文件/文件夹图标
    public static int index;
    Label[] fIcons;
    private Image aboutImg = new Image(this.getClass().getResourceAsStream("/resourses/images/about.png"));
    private Image helpImg = new Image(this.getClass().getResourceAsStream("/resourses/images/help.png"));
    private Image fileImg = new Image(this.getClass().getResourceAsStream("/resourses/images/filemanageIcon.png"));
    private Image filesImg = new Image(this.getClass().getResourceAsStream("/resourses/images/file.png"));
    private Image folderImg = new Image(this.getClass().getResourceAsStream("/resourses/images/folder.png"));

    public ContainPane(FAT fat) {
        this.fat = fat;
        //创建右键菜单栏
        initContextMenu();
        //给containPane面板添加图标，包括文件夹图标，文件图标，功能图标（例如：帮助图标，文件管理图标）
        addFileIcon();
    }

    //获取一个containPane面板实例
    public static ContainPane getInstance(FAT fat) {
        if (containPane == null) {
            containPane = new ContainPane(fat);
        }
        return containPane;
    }

    public void upDateDesktop() {
        //先清除放置在containPane面板的所有节点
        getChildren().removeAll(this.getChildren());
        //重新放置containPane面板的所有节点
        addFileIcon();
    }

    //给containPane面板添加图标，包括文件夹图标，文件图标，功能图标（例如：帮助图标，文件管理器图标）
    public void addFileIcon() {
        this.bList = new ArrayList();
        this.bList = this.fat.getBlockBegin("C:");
        int n = this.bList.size();
        this.fIcons = new Label[n + 3];
        //创建并添加功能图标，并把3个功能图标分别放到ficons数组第n,n+1,n+2的位置上(0~n-1位置的数组元素是存放文件夹，文件图标)
        this.createIcon(this.aboutImg, "关于", n);
        this.createIcon(this.helpImg, "帮助", n + 1);
        this.createIcon(this.fileImg, "文件管理", n + 2);
        //添加文件夹，文件图标，并把文件夹，文件图标放在0-n-1位置的数组元素上
        for(int i = 0; i < n; ++i) {
            ImageView imageView;
            if (((DiskBlock)this.bList.get(i)).getObject() instanceof Folder) {
                imageView = new ImageView(this.folderImg);
                imageView.setFitHeight(65.0D);
                imageView.setFitWidth(65.0D);
                this.fIcons[i] = new Label(((Folder)((DiskBlock)this.bList.get(i)).getObject()).getFolderName(), imageView);
            } else {
                imageView = new ImageView(this.filesImg);
                imageView.setFitHeight(65.0D);
                imageView.setFitWidth(65.0D);
                this.fIcons[i] = new Label(((File)((DiskBlock)this.bList.get(i)).getObject()).getFileName(), imageView);
            }
            this.fIcons[i].setContentDisplay(ContentDisplay.TOP);
            this.fIcons[i].setWrapText(true);
            this.fIcons[i].setId("deskIcon");
            getChildren().add(this.fIcons[i]);
            this.setPadding(new Insets(5.0D, 0.0D, 5.0D, 0.0D));
            this.setHgap(2.0D);
            this.setVgap(2.0D);
            //添加鼠标点击事件监听
            clickOnFile(this.fIcons[i], fileContextMenu, i);
        }
    }

    //创建并添加功能图标，并把3个功能图标分别放到ficons数组第n,n+1,n+2的位置上
    public void createIcon(Image img, final String iconName, int n) {
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(65.0D);
        imageView.setFitWidth(65.0D);
        Label icon = new Label(iconName, imageView);
        icon.setId("deskIcon");
        //设置图片显示在文字的上方
        icon.setContentDisplay(ContentDisplay.TOP);
        icon.setWrapText(true);
        icon.setPadding(new Insets(60.0D, 60.0D, 60.0D, 60.0D));
        this.fIcons[n] = icon;
        //设置鼠标点击事件监听
        icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //把所有图标的颜色初始化为原始状态
                for(int i = 0; i < ContainPane.this.fat.getBlockBegin("C:").size() + 3; ++i) {
                    ContainPane.this.fIcons[i].setStyle("-fx-font-family:'Microsoft Yahei';-fx-text-fill: #f0f0f0;-fx-padding: 10px;-fx-font-weight: bolder;-fx-font-size: 14px;-fx-border-color: rgba(255,255,255,0);-fx-border-width: 0.5px;");
                }
                //对用户点击的图标再设置深一点的颜色，不同于其他图标
                ((Label)event.getSource()).setStyle("-fx-font-family:'Microsoft Yahei'; -fx-text-fill: #f0f0f0;-fx-padding: 9.75px; -fx-font-weight: bolder; -fx-font-size: 14px; -fx-background-color: rgba(51,204,255,0.45); -fx-border-color: rgba(0,0,225,0.9); -fx-border-width: 0.5px;");
                //鼠标左键双击
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    String var3;
                    switch((var3 = iconName).hashCode()) {
                    case 666491:
                        if (var3.equals("关于")) {
                            //打开关于窗口
                            new AboutPane();
                            return;
                        }
                        break;
                    case 768571:
                        if (var3.equals("帮助")) {
                            //打开帮助窗口
                            new HelpPane();
                            return;
                        }
                    }
                    //打开文件管理窗口
                    new MainView();
                }
            }
        });
        //将icon图标放置到containPane面板上
        this.getChildren().add(icon);
    }

    //创建右键菜单栏
    private void initContextMenu() {
        //创建右键菜单栏的按钮选项：打开按钮，删除按钮，重命名按钮，属性按钮
        openItem = new MenuItem("打开");
        delItem = new MenuItem("删除");
        renameItem = new MenuItem("重命名");
        propItem = new MenuItem("属性");
        //创建右键菜单栏fileContextMenu，并把上面创建的3个选项按钮添加到右键菜单栏fileContextMenu
        fileContextMenu = new ContextMenu(new MenuItem[]{openItem, delItem, propItem});
        fileContextMenu.setId("fileContextMenu");
        //为"打开"选项按钮，“删除”选项按钮，"属性"选项按钮分别设置鼠标点击事件监听
        openItem.setOnAction((ActionEvent) -> {
            onOpen();
        });
        delItem.setOnAction((ActionEvent) -> {
            DiskBlock thisBlock = (DiskBlock)this.bList.get(index);
            if (this.fat.isOpenedFile(thisBlock)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText((String)null);
                alert.setContentText("文件未关闭");
                alert.showAndWait();
            } else {
                new delFile(thisBlock, this.fat);
                this.upDateDesktop();
            }

        });
        propItem.setOnAction((ActionEvent) -> {
            DiskBlock thisBlock = (DiskBlock)bList.get(index);
            new PropertyPane(thisBlock);
        });
    }

    //打开文件夹/文件,都是创建一个窗口然后显示这个文件/文件夹的内容
    private void onOpen() {
        DiskBlock thisBlock = (DiskBlock)this.bList.get(index);
        if (thisBlock.getObject() instanceof File) {
            //打开文本文件
            if (this.fat.isOpenedFile(thisBlock)) {
                //文本文件已经被打开
                Alert duplicate = new Alert(AlertType.ERROR, "文件已打开", new ButtonType[0]);
                duplicate.showAndWait();
            } else {
                //文本文件未被打开
                this.fat.addFilesOpened(thisBlock);
                //创建一个文本文件视窗
                new FileView((File)thisBlock.getObject(), this.fat, thisBlock, MainView.mainView0);
            }
        } else {
            //打开文件夹
            Folder thisFolder = (Folder)thisBlock.getObject();
            String newPath = thisFolder.getLocation() + "\\" + thisFolder.getFolderName();
            //创建一个文件夹视窗
            new FolderPane(this.fat, thisBlock, thisFolder, newPath);
        }
    }

    //添加鼠标点击事件监听
    public void clickOnFile(Label fileIcon, final ContextMenu fileContextMenu, final int index) {
        fileIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //将所有的文件夹/文件图标的颜色设置为原始状态
                for(int i = 0; i < ContainPane.this.fat.getBlockBegin("C:").size() + 3; ++i) {
                    ContainPane.this.fIcons[i].setStyle("-fx-font-family:'Microsoft Yahei';-fx-text-fill: #f0f0f0;-fx-padding: 10px;-fx-font-weight: bolder;-fx-font-size: 14px;-fx-border-color: rgba(255,255,255,0);-fx-border-width: 0.5px;");
                }
                //把用户点击的文件夹/文件图标的颜色变深，字体改变
                ((Label)event.getSource()).setStyle("-fx-font-family:'Microsoft Yahei'; -fx-text-fill: #f0f0f0;-fx-padding: 9.75px; -fx-font-weight: bolder; -fx-font-size: 14px; -fx-background-color: rgba(51,204,255,0.45); -fx-border-color: rgba(0,0,225,0.9); -fx-border-width: 0.5px;");
                Label src = (Label)event.getSource();
                //更新下标索引
                ContainPane.index = index;
                if (event.getButton() == MouseButton.SECONDARY) {
                    //展示右键菜单栏
                    fileContextMenu.show(src, event.getScreenX(), event.getScreenY());
                } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    //打开文件
                    ContainPane.this.onOpen();
                } else {
                    //隐藏右键菜单栏
                    fileContextMenu.hide();
                }
            }
        });
    }

    //这个方法没用到
    public static ContextMenu getFileContextMenu() {
        return fileContextMenu;
    }
}
