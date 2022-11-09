//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package debug;

import filemanage.basic.FAT;
import filemanage.basic.File;
import filemanage.basic.Folder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Test2 {
    public Test2() {
    }

    public static void main(String[] args) {
        FAT fat = new FAT();
        try {
            ObjectInputStream read = new ObjectInputStream(new FileInputStream("Data"));
            fat = (FAT)read.readObject();
        } catch (IOException | ClassNotFoundException var3) {
            var3.printStackTrace();
        }
        for(int i = 3; i < 128; ++i) {
            if (fat.getDiskBlocks()[i].getObject() instanceof Folder) {
                System.out.println(((Folder)fat.getDiskBlocks()[i].getObject()).getLocation() + "----" + ((Folder)fat.getDiskBlocks()[i].getObject()).getFolderName());
            } else if (fat.getDiskBlocks()[i].getObject() instanceof File) {
                System.out.println(((File)fat.getDiskBlocks()[i].getObject()).getLocation() + "----" + ((File)fat.getDiskBlocks()[i].getObject()).getFileName());
            }
        }
    }
}
