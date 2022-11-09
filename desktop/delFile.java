//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package desktop;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import filemanage.basic.DiskBlock;
import filemanage.basic.FAT;
import filemanage.basic.Folder;
import filemanage.basic.Path;

public class delFile {
    private DiskBlock block;
    private FAT fat;
    private Alert mainAlert;
    private Alert okAlert;
    private Alert errAlert;

    public delFile(DiskBlock block, FAT fat) {
        this.block = block;
        this.fat = fat;
        this.showView();
    }

    private void showView() {
        this.mainAlert = new Alert(AlertType.CONFIRMATION);
        this.mainAlert.setHeaderText("确认删除");
        this.mainAlert.setContentText((String)null);
        this.okAlert = new Alert(AlertType.INFORMATION);
        this.okAlert.setTitle("成功");
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
