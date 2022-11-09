//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package main;

import desktop.Desktop;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public void start(Stage primaryStage) {
        //创建桌面
        Desktop root = Desktop.getInstance();
        //创建场景
        Scene scene = new Scene(root, 400.0D, 400.0D);
        //设置样式
        scene.getStylesheets().add("resourses/css/PV.css");
        //设置窗口图标，本人觉得没啥用，因为窗口最大化之后，这个图标根本看不到
        Image image = new Image("resourses/images/OS.png");
        primaryStage.getIcons().add(image);
        //为窗口设置场景
        primaryStage.setScene(scene);
        //窗口最大化
        primaryStage.setMaximized(true);
        //设置窗口样式，去掉窗口上面"最小化，最大化，关闭窗口按钮那一行"
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //显示窗口
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
