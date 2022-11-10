package desktop;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AboutPane extends Pane {
    public AboutPane() {
        String str = new String("\n\n\n\n\t\t\t\t\t2020级软件工程1班\n\n\n\t\t\t\t开发人员：漆永康   张正法  徐铭志");
        Label textLabel = new Label(str);
        this.setId("AboutPane");
        this.getChildren().add(textLabel);
        Stage stage = new Stage();
        Image icon = new Image("resourses/images/about.png");
        stage.getIcons().add(icon);
        stage.setTitle("关于");
        Scene scene = new Scene(this, 580.0D, 400.0D);
        scene.getStylesheets().add("resourses/css/PV.css");
        stage.setScene(scene);
        stage.show();
    }
}
