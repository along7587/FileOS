//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filemanage.basic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Folder implements Serializable {
    private static final long serialVersionUID = 1L;
    private String folderName;
    private String type;
    private int diskNum;
    private String location;
    private double size;
    private String space;
    private Folder parent;
    private List<Object> children;
    private Path path;
    private transient StringProperty folderNameP = new SimpleStringProperty();

    public Folder(String folderName) {
        this.folderName = folderName;
        this.setFolderNameP();
    }

    public Folder(String folderName, String location, int diskNum, Folder parent) {
        this.folderName = folderName;
        this.location = location;
        this.size = 0.0D;
        this.space = this.size + "KB";
        this.diskNum = diskNum;
        this.type = "FOLDER";
        this.parent = parent;
        this.setChildren(new ArrayList());
        this.setFolderNameP();
    }

    public StringProperty folderNamePProperty() {
        return this.folderNameP;
    }

    private void setFolderNameP() {
        this.folderNameP.set(this.folderName);
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
        this.setFolderNameP();
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
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<Object> getChildren() {
        return this.children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    public void addChildren(Object child) {
        this.children.add(child);
    }

    public void removeChildren(Object child) {
        this.children.remove(child);
    }

    public boolean hasChild() {
        return !this.children.isEmpty();
    }

    public Path getPath() {
        return this.path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.folderNameP = new SimpleStringProperty(this.folderName);
    }

    public String toString() {
        return this.folderName;
    }
}
