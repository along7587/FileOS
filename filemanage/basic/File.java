//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filemanage.basic;

import filemanage.util.FATUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
//ObjectInputStream 反序列化流，将之前使用 ObjectOutputStream 序列化的原始数据恢复为对象，以流的方式读取对象。
public class File implements Serializable {
    private String fileName;
    private String type;
    private int diskNum;
    private int flag;
    private int length;
    private String content;
    private Folder parent;
    private String location;
    private double size;
    private String space;
    private boolean isOpen;
    private static final long serialVersionUID = 1L;
    private transient StringProperty fileNameP = new SimpleStringProperty();
    private transient StringProperty flagP = new SimpleStringProperty();
    private transient StringProperty diskNumP = new SimpleStringProperty();
    private transient StringProperty locationP = new SimpleStringProperty();
    private transient StringProperty lengthP = new SimpleStringProperty();

    public File(String fileName) {
        this.fileName = fileName;
        this.setOpened(false);
        this.setFileNameP();
    }

    public File(String fileName, String location, int diskNum, Folder parent) {
        this.fileName = fileName;
        this.type = "FILE";
        this.diskNum = diskNum;
        this.length = 1;
        this.content = "";
        this.location = location;
        this.size = FATUtil.getSize(this.content.length());
        this.space = this.size + "KB";
        this.parent = parent;
        this.setOpened(false);
        this.setFileNameP();
        this.setFlagP();
        this.setDiskNumP();
        this.setLocationP();
        this.setLengthP();
    }

    public StringProperty fileNamePProperty() {
        return this.fileNameP;
    }

    public StringProperty flagPProperty() {
        return this.flagP;
    }

    public StringProperty diskNumPProperty() {
        return this.diskNumP;
    }

    public StringProperty locationPProperty() {
        return this.locationP;
    }

    public StringProperty lengthPProperty() {
        return this.lengthP;
    }

    private void setFileNameP() {
        this.fileNameP.set(this.fileName);
    }

    private void setFlagP() {
        this.flagP.set(this.flag == 0 ? "只读" : "读写");
    }

    private void setDiskNumP() {
        this.diskNumP.set(String.valueOf(this.diskNum));
    }

    private void setLocationP() {
        this.locationP.set(this.location);
    }

    private void setLengthP() {
        this.lengthP.set(String.valueOf(this.length));
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.setFileNameP();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDiskNum() {
        return this.diskNum;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
        this.setDiskNumP();
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
        this.setFlagP();
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
        this.setLengthP();
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
        this.setLocationP();
    }

    public double getSize() {
        return this.size;
    }

    public void setSize(double KBcount) {
        this.size = KBcount;
        this.setSpace(this.size + "KB");
    }

    public String getSpace() {
        return this.space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public Folder getParent() {
        return this.parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean isOpened() {
        return this.isOpen;
    }

    public void setOpened(boolean isOpen) {
        this.isOpen = isOpen;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.fileNameP = new SimpleStringProperty(this.fileName);
        this.flagP = new SimpleStringProperty(this.flag == 0 ? "只读" : "读写");
        this.diskNumP = new SimpleStringProperty(String.valueOf(this.type));
        this.locationP = new SimpleStringProperty(this.location);
        this.lengthP = new SimpleStringProperty(String.valueOf(this.length));
    }

    public String toString() {
        return this.fileName;
    }
}
