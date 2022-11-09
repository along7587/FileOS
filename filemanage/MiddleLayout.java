//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filemanage;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import filemanage.basic.DiskBlock;
import filemanage.basic.FAT;

public class MiddleLayout {
    private ScrollPane scrollPane;
    private Pane pane;
    private Label label1;
    private FlowPane flowPane;
    private GridPane gridPane;
    private StackPane[] stackpane;
    private Label[] label2;
    private Rectangle[] rec;
    private Rectangle[] rec2;
    private DiskBlock[] diskBlocks;
    private FAT fat;

    public MiddleLayout(FAT fat) {
        this.fat = fat;
        this.diskBlocks = fat.getDiskBlocks();
        this.scrollPane = new ScrollPane();
        this.pane = new Pane();
        this.label1 = new Label("磁盘使用情况");
        this.flowPane = new FlowPane();
        this.gridPane = new GridPane();
        this.stackpane = new StackPane[128];
        this.label2 = new Label[128];
        this.rec = new Rectangle[128];
        this.rec2 = new Rectangle[128];

        for(int i = 0; i < 128; ++i) {
            this.rec2[i] = new Rectangle(25.0D, 20.0D, new Color(0.8D, 0.0D, 0.0D, 0.8D));
            this.rec2[i].setId("rec2");
        }

    }

    public GridPane getGridPane() {
        return this.gridPane;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public GridPane updateGridPane() {
        this.diskBlocks = this.fat.getDiskBlocks();

        for(int i = 0; i < 128; ++i) {
            if (this.diskBlocks[i].getIndex() == 0) {
                this.stackpane[i] = new StackPane();
                this.label2[i].setStyle("-fx-text-fill:#202020;-fx-font-family:'Microsoft YaHei';-fx-font-weight:bold;");
                this.stackpane[i].getChildren().addAll(new Node[]{this.rec[i], this.label2[i]});
            } else {
                this.stackpane[i] = new StackPane();
                this.label2[i].setStyle("-fx-text-fill:#ffffff;-fx-font-family:'Microsoft YaHei';-fx-font-weight:bold;");
                this.stackpane[i].getChildren().addAll(new Node[]{this.rec2[i], this.label2[i]});
            }
        }

        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(5.0D);
        gridPane.setLayoutY(444.0D);
        gridPane.setPadding(new Insets(12.0D, 0.0D, 20.0D, 65.0D));
        gridPane.setHgap(2.0D);
        gridPane.setVgap(2.0D);
        int tmp = 0;

        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 16; ++j) {
                gridPane.add(this.stackpane[tmp], j, i);
                ++tmp;
            }
        }

        return gridPane;
    }

    public Pane middleLayout() {
        this.flowPane.setPadding(new Insets(20.0D));
        this.flowPane.setHgap(20.0D);
        this.flowPane.setVgap(20.0D);
        this.flowPane.setPrefHeight(400.0D);
        this.flowPane.setPrefWidth(632.0D);
        this.scrollPane.setPrefHeight(400.0D);
        this.scrollPane.setPrefWidth(632.0D);
        this.scrollPane.setLayoutX(0.0D);
        this.scrollPane.setLayoutY(0.0D);
        this.scrollPane.setContent(this.flowPane);
        this.label1.setPrefHeight(40.0D);
        this.label1.setPrefWidth(1000.0D);
        this.label1.setStyle("-fx-font-family:'Microsoft YaHei';-fx-text-fill: #233333;-fx-padding:25px 0 0 270px;-fx-font-size:18px;");

        int tmp;
        for(tmp = 0; tmp < 128; ++tmp) {
            this.label2[tmp] = new Label("" + tmp);
            this.label2[tmp].setPadding(new Insets(5.0D, 5.0D, 5.0D, 5.0D));
        }

        for(tmp = 0; tmp < 128; ++tmp) {
            this.rec[tmp] = new Rectangle(25.0D, 20.0D, new Color(0.4D, 0.8D, 0.0D, 0.6D));
            this.stackpane[tmp] = new StackPane();
            this.stackpane[tmp].getChildren().addAll(new Node[]{this.rec[tmp], this.label2[tmp]});
        }

        this.gridPane.setLayoutX(5.0D);
        this.gridPane.setLayoutY(444.0D);
        this.gridPane.setPadding(new Insets(0.0D, 0.0D, 0.0D, 0.0D));
        this.gridPane.setHgap(2.0D);
        this.gridPane.setVgap(2.0D);
        tmp = 0;

        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 16; ++j) {
                this.gridPane.add(this.stackpane[tmp], j, i);
                ++tmp;
            }
        }

        this.pane.setPrefHeight(800.0D);
        this.pane.setPrefWidth(600.0D);
        this.pane.getChildren().addAll(new Node[]{this.scrollPane, this.label1, this.gridPane});
        return this.pane;
    }

    public Label getLabel1() {
        return this.label1;
    }

    public void setLabel1(Label label1) {
        this.label1 = label1;
    }
}
