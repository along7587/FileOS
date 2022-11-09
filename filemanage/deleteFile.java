package filemanage;

import filemanage.basic.DiskBlock;
import filemanage.basic.FAT;
import filemanage.basic.Folder;
import filemanage.basic.Path;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

//Alert类可以用于显示对话框，大致上有4种常用的类型，Information，Comfiramtion，Error，Warning
import java.util.Optional;

public class deleteFile {
    private DiskBlock block;
    private FAT fat;
    private MainView mainView;
    private Alert mainAlert;
    private Alert okAlert;
    private Alert errAlert;

    public deleteFile(DiskBlock block, FAT fat, MainView mainView) {
        this.block = block;
        this.fat = fat;
        this.mainView = mainView;
        this.showView();
    }

    private void showView() {
        this.mainAlert = new Alert(AlertType.CONFIRMATION);
        this.mainAlert.setHeaderText("是否确认删除？");
        this.mainAlert.setTitle("删除");
        this.mainAlert.setContentText((String)null);
        this.okAlert = new Alert(AlertType.INFORMATION);
        this.okAlert.setTitle("删除成功");
        this.okAlert.setHeaderText((String)null);
        this.errAlert = new Alert(AlertType.ERROR);
        this.errAlert.setHeaderText((String)null);
        this.showAlert();
    }

    private void showAlert() {
        Optional<ButtonType> result = this.mainAlert.showAndWait();
        Path thisPath = null;
        if (result.get() == ButtonType.OK) {
            if (this.block.getObject() instanceof Folder) {
                thisPath = ((Folder)this.block.getObject()).getPath();
            }

            int res = this.fat.delete(this.block);
            if (res == 0) {
                this.mainView.removeNode(this.mainView.getRecentNode(), thisPath);
                this.okAlert.setContentText("删除成功");
                this.okAlert.show();
            } else if (res == 1) {
                this.okAlert.setContentText("删除成功");
                this.okAlert.show();
            } else if (res == 2) {
                this.errAlert.setHeaderText("文件夹不为空");
                this.errAlert.show();
            } else {
                this.errAlert.setHeaderText("文件未关闭");
                this.errAlert.show();
            }
        }

    }
}
