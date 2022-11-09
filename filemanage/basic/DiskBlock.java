//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package filemanage.basic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
//javaFX的属性包含实际值，并提供更改支持，无效支持和绑定功能。所有JavaFX属性类都位于javafx.beans.property.*包命名空间中。
//SimpleStringProperty类创建一个字符串属性，该属性对包装的字符串值是可读写的。Simple的属性是读/写属性类。拥有ReadOnly的属性是只读属性。

//StringProperty，JAVAfX定义了一个接口 javafx.beans.property.Property，它有一个非常有用的功能：
// 绑定GUI组件（视图层）到java类（MODULE）的“properties”中。
// 当绑定建立后，如果module层的值发生改变，GUI自动会自动接到通知，反之亦然。说明：绑定本质上就是建立一个双向监听。

//IOException 是输入或输出异常,一般出现在文件处理中,IO操作引起的异常
//ObjectInputStream 反序列化流，将之前使用 ObjectOutputStream 序列化的原始数据恢复为对象，以流的方式读取对象
//Serializable序列化处理,主要作用将类的实例持久化保存，序列化就是保存，反序列化就是读取。保存也不一定保存在本地，也可以保存到远方。 类一定要实现Serializable才可以
public class DiskBlock implements Serializable {
    private int num;
    private int index;
    private String type;
    private Object object;
    private boolean begin;
    private static final long serialVersionUID = 1L;
    private transient StringProperty noP = new SimpleStringProperty();
    private transient StringProperty indexP = new SimpleStringProperty();
    private transient StringProperty typeP = new SimpleStringProperty();
    private transient StringProperty objectP = new SimpleStringProperty();

    public DiskBlock(int num, int index, String type, Object object) {
        this.num = num;
        this.index = index;
        this.type = type;
        this.object = object;
        this.begin = false;
        this.setProperty();
    }

    private void setProperty() {
        this.setNoP();
        this.setIndexP();
        this.setTypeP();
        this.setObjectP();
    }

    public void setObject(Object object) {
        this.object = object;
        if (object instanceof File) {
            this.objectP.bind(((File)object).fileNamePProperty());
        } else if (object instanceof Folder) {
            this.objectP.bind(((Folder)object).folderNamePProperty());
        } else {
            this.objectP.unbind();
            this.setObjectP();
        }

    }

    public void updateBlock(int index, String type, Object object, boolean begin) {
        this.setIndex(index);
        this.setType(type);
        this.setObject(object);
        this.setBegin(begin);
    }

    public void clearBlock() {
        this.setIndex(0);
        this.setType("NULL");
        this.setObject((Object)null);
        this.setBegin(false);
    }

    public boolean isFree() {
        return this.index == 0;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.noP = new SimpleStringProperty(String.valueOf(this.num));
        this.indexP = new SimpleStringProperty(String.valueOf(this.index));
        this.typeP = new SimpleStringProperty(this.type);
        this.objectP = new SimpleStringProperty(this.object == null ? "" : this.object.toString());
        this.setObject(this.object);
    }

    public String toString() {
        Object object = this.getObject();
        return object instanceof File ? ((File)object).toString() : ((Folder)object).toString();
    }

    public StringProperty noPProperty() {
        return this.noP;
    }

    public StringProperty indexPProperty() {
        return this.indexP;
    }

    public StringProperty typePProperty() {
        return this.typeP;
    }

    public StringProperty objectPProperty() {
        return this.objectP;
    }

    private void setNoP() {
        this.noP.set(String.valueOf(this.num));
    }

    private void setIndexP() {
        this.indexP.set(String.valueOf(this.index));
    }

    private void setTypeP() {
        this.typeP.set(this.type);
    }

    private void setObjectP() {
        this.objectP.set(this.object == null ? "" : this.object.toString());
    }

    public int getNo() {
        return this.num;
    }

    public void setNo(int no) {
        this.num = no;
        this.setNoP();
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
        this.setIndexP();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
        this.setTypeP();
    }

    public Object getObject() {
        return this.object;
    }

    public boolean isBegin() {
        return this.begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }
}
