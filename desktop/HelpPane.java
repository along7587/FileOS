//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package desktop;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HelpPane extends Pane {
    public HelpPane() {
        String str = new String("\n\n\t1、在桌面中，打开关于界面可查看小组信息；打开帮助可查看系统操作指南；\n\n\t2、鼠标左键双击文件夹可进入该文件目录；双击文件可查看文件内容进行编辑；\n\n\t     鼠标右键单击文件或文件可选择打开、删除、属性，打开文件(文件夹)，删\n\n\t     除文件(文件夹)，查看文件(文件夹)属性。\n\n\t3、双击文件管理可进入文件系统管理界面，左边选择目录树可查看相对应的目录\n\n\t     下的文夹与文件夹；右边为文件分配表信息；中下为磁盘使用情况；中上为选\n\n\t     中目录中的文件与文件夹信息，左键双击可打开文件(文件夹)，右键单击可选\n\n\t     择进行打开、删除、重命名、属性等操作，右键单击空白可新建文件或文件夹。");
        Label textLabel = new Label(str);
        this.getChildren().add(textLabel);
        this.setId("HelpPane");
        Stage stage = new Stage();
        Image icon = new Image("resourses/images/help.png");
        stage.getIcons().add(icon);
        stage.setTitle("帮助");
        Scene scene = new Scene(this, 600.0D, 400.0D);
        scene.getStylesheets().add("resourses/css/PV.css");
        stage.setScene(scene);
        stage.show();
    }
}
