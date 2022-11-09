//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package desktop;

import filemanage.basic.FAT;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.Optional;

public class Desktop extends BorderPane {
    public static Desktop desktop;
    public FAT fat = FAT.getNewFAT();
    public ContainPane containPane;
    
    public Desktop() {
        //读取已经保存到项目里面的FAT对象
        readFAT();
        //创建containPane面板
        containPane = ContainPane.getInstance(this.fat);
        //更新containPane界面显示的内容
        //刚开始创建的时候，这个containPane面板里面没有存放什么东西的,需要调用一下界面更新函数
        //这个函数名多多少少有点歧义啊
        containPane.upDateDesktop();
        //设置物体排列的方向：垂直方向
        containPane.setOrientation(Orientation.VERTICAL);
        //设置放置物体间隔距离
        containPane.setStyle("-fx-padding: 8px;");
        //设置壁纸 /img/bj5++.jpg为壁纸相对路径
        setStyle("-fx-background-color: transparent;-fx-background-image: url(/resourses/images/wallpapper1.png);-fx-background-size: 100%;");
        Pane calenderPane=createCalenderWallPapper();
        setRight(calenderPane);
        //将containPane面板放在desktop面板的中心位置,可以把下面语句注释掉，看看运行效果，体会containPane面板的作用
        setCenter(containPane);
        //创建桌面导航栏
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        AnchorPane guideBar=createGuideBar(screenRectangle.getWidth(),50);
        setTop(guideBar);
        //在desktop面板的下方创建系统关闭按钮
        //createShutDownIcon();
    }

    //创建一个DeskTop对象,并返回该对象
    public static Desktop getInstance() {
        if (desktop == null) {
            desktop = new Desktop();
        }
        return desktop;
    }

    //读取已经保存到项目里面的FAT对象
    private void readFAT() {
        try {
            //创建输入流，"disk"为文件相对路径
            ObjectInputStream read = new ObjectInputStream(new FileInputStream("disk"));
            fat = (FAT)read.readObject();
        } catch (IOException | ClassNotFoundException var2) {
            var2.printStackTrace();
        }
    }

    //在desktop面板的下方创建系统关闭按钮icon
    public void createShutDownIcon() {
        Image shutDownImg = new Image(this.getClass().getResourceAsStream("/resourses/images/shutdown.png"));
        ImageView shutDown = new ImageView(shutDownImg);
        shutDown.setFitHeight(50.0D);
        shutDown.setFitWidth(50.0D);
        //创建一个关闭系统按钮
        Label icon = new Label("", shutDown);
        icon.setContentDisplay(ContentDisplay.TOP);
        icon.setWrapText(true);
        icon.setId("shutDown");
        //设置鼠标点击事件监听，如果鼠标点击icon,就会弹出提示框，提示用户是否关闭系统，如果用户选择确认，就会保存fat对象，并关闭系统
        icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("提示");
                    alert.setGraphic(new ImageView(new Image("resourses/images/exit.png", 40.0D, 40.0D, true, true)));
                    alert.setHeaderText("即将退出系统");
                    alert.setContentText("确认退出？");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        ((Stage)icon.getScene().getWindow()).close();
                    }
                }
            }
        });
        //将icon放在desktop面板的底部位置
        setBottom(icon);
    }

    //获取FAT对象
    public FAT getFat() {
        return this.fat;
    }
    
    //创建日历
    private Pane createCalenderWallPapper(){
        Pane pane1=new Pane();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int totalDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
        GridPane cp = new GridPane();
        String[] week = new String[]{"日\n\n", "一\n\n", "二\n\n", "三\n\n", "四\n\n", "五\n\n", "六\n\n"};
        cp.setPadding(new Insets(30, 30, 10, 10));
        Text text1 = new Text(year+"    "+month+"月\n");
        text1.setFill(Color.GREEN);
        text1.setStyle("-fx-font-size: 20;"+"-fx-font-family: '微软雅黑'");
        cp.setAlignment(Pos.CENTER);
        cp.add(text1, 0, 0);
        GridPane.setColumnSpan(text1, 7);
        GridPane.setHalignment(text1, HPos.CENTER);
        for(int i=0;i<7;i++){
            Label label = new Label(week[i]);
            label.setStyle("-fx-font-size: 15;"+"-fx-font-family: '微软雅黑'");
            if(i==0||i==6){
                label.setTextFill(Color.YELLOW);
            }
            cp.add(label, i, 5);
            GridPane.setHalignment(label, HPos.CENTER);
            cp.getColumnConstraints().add(new ColumnConstraints(40));
        }
        for(int j=0;j<totalDayOfMonth;j++){
            Label label=new Label(j+1+"\n\n");
            label.setStyle("-fx-font-size: 15;"+"-fx-font-family: '微软雅黑'");
            int k=firstDayOfWeek+j;
            if((k%7==0)||(k%7==6)){
                label.setTextFill(Color.YELLOW);
            }
            if (j+1==day) {
                label.setPrefWidth(30);
                label.setAlignment(Pos.CENTER);
                label.setTextFill(Color.RED);
                label.setStyle("-fx-background-color: #ffff00;"+"-fx-font-family: '微软雅黑'");
            }
            cp.add(label, k%7,7+k/7);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        cp.setStyle("-fx-background-color: #ffffff44");
        pane1.getChildren().add(cp);
        return pane1;
    }
    
    //创建桌面导航栏
    private AnchorPane createGuideBar(double width,double height){
        AnchorPane anchorPane=new AnchorPane();
        anchorPane.setStyle("-fx-background-color: rgba(255,255,255,0.1)");
        anchorPane.setPrefHeight(height);
        anchorPane.setPrefWidth(width);
        Button closeButton=new Button();
        closeButton.setStyle("-fx-background-color: rgba(255,255,255,0.1)");
        ImageView closeIcon=new ImageView(new Image(getClass().getResourceAsStream("/resourses/images/shutdown.png")));
        closeIcon.setFitHeight(height-5);
        closeIcon.setFitWidth(height-5);
        closeButton.setGraphic(closeIcon);
        Button sleepButton=new Button();
        ImageView sleepIcon=new ImageView(new Image(getClass().getResourceAsStream("/resourses/images/ico.png")));
        sleepIcon.setFitHeight(height-5);
        sleepIcon.setFitWidth(height-5);
        sleepButton.setGraphic(sleepIcon);
        sleepButton.setStyle("-fx-background-color: #ffffff00");
        //添加事件
        closeButton.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    closeButton.setStyle("-fx-background-color: #0000ff55");
                }
                else{
                    closeButton.setStyle("-fx-background-color: #0000ff00");
                }
            }
        });
        closeButton.setOnMousePressed(e->{
            if (e.getButton() == MouseButton.PRIMARY) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("提示");
                alert.setGraphic(new ImageView(new Image("resourses/images/exit.png", 40.0D, 40.0D, true, true)));
                alert.setHeaderText("即将退出系统");
                alert.setContentText("确认退出？");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ((Stage)closeButton.getScene().getWindow()).close();
                }
            }
        });
        sleepButton.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    sleepButton.setStyle("-fx-background-color: #0000ff55");
                }
                else{
                    sleepButton.setStyle("-fx-background-color: #0000ff00");
                }
            }
        });
        sleepButton.setOnMousePressed(e->{
            ((Stage)closeButton.getScene().getWindow()).setIconified(true);
        });
        
        HBox hBox=new HBox();
        hBox.getChildren().addAll(closeButton,sleepButton);
        anchorPane.getChildren().add(hBox);
        AnchorPane.setLeftAnchor(hBox,0.0);
        AnchorPane.setBottomAnchor(hBox,0.0);
        AnchorPane.setTopAnchor(hBox,0.0);
        return anchorPane;
    }
}
